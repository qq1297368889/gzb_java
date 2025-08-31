package gzb.frame.db;

import com.frame.entity.SysUsers;
import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.EntityAttribute;
import gzb.frame.annotation.Service;
import gzb.tools.GzbMap;
import gzb.tools.JSONResult;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    public static void main(String[] args) throws Exception {
        String path = Tools.getProjectRoot(BaseDaoImpl2.class);
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", path);
        BaseDao<SysUsers> dao = new BaseDaoImpl<SysUsers>() {

        };
        dao.setDataBase(new DataBaseMsql("frame"));
        System.out.println(dao.count("select * from sys_users",null));
        dao.save(new SysUsers().setSysUsersAcc("acc001").setSysUsersPwd("pwd001"));
        System.out.println(dao.count("select * from sys_users",null));
        SysUsers sysUsers=dao.find(new SysUsers().setSysUsersAcc("acc001"));
        System.out.println(sysUsers);
        dao.update(sysUsers.setSysUsersPwd("pwd000005"));
        System.out.println(dao.count("select * from sys_users",null));
        dao.delete(sysUsers);
        System.out.println(dao.count("select * from sys_users",null));

        System.out.println("事务开启");
        dao.openTransaction();
        dao.save(new SysUsers().setSysUsersAcc("acc001").setSysUsersPwd("pwd001"));
        System.out.println(dao.count("select * from sys_users",null));
        dao.commit();
        System.out.println(dao.count("select * from sys_users",null));
        SysUsers sysUsers2=dao.find(new SysUsers().setSysUsersAcc("acc001"));
        dao.delete(sysUsers2);
        System.out.println(dao.count("select * from sys_users",null));
        System.out.println("关闭后再次尝试 回滚");
        dao.openTransaction();
        dao.save(new SysUsers().setSysUsersAcc("acc001").setSysUsersPwd("pwd001"));
        System.out.println(dao.count("select * from sys_users",null));
        dao.rollback();
        System.out.println(dao.count("select * from sys_users",null));
        System.out.println("翻页函数测试");
        System.out.println(dao.queryPage("select * from sys_users",null,null,null,1,10,100,100));
        System.out.println(dao.queryPage("select * from sys_users",null,null,null,2,10,100,100));
    }

    public T t;
    private DataBase dataBase = null;
    private final Log log = new LogImpl();
    private boolean autoCommit = true;
    Connection connection_class = null;

    public BaseDaoImpl() {
        try {
            // 获取当前类的泛型父类
            Type superclass = this.getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) superclass;
                // 获取泛型类型参数数组
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                    Class<T> entityClass = (Class<T>) actualTypeArguments[0];
                    // 获取无参构造函数
                    Constructor<T> constructor = entityClass.getDeclaredConstructor();
                    // 设置构造函数可访问
                    constructor.setAccessible(true);
                    // 通过构造函数实例化对象
                    t = constructor.newInstance();
                }
            }
        } catch (Exception e) {
          log.e(e);
        }
    }

    public void init(String key) throws Exception {

        this.dataBase=DataBaseFactory.getDataBase(key);
    }
    @Override
    public DataBase getDataBase() {
        return this.dataBase;
    }

    @Override
    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }
    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        JSONResult jsonResult = new JSONResult();
        int count=count(sql,objects);
        if (count<=(page-1)*size) {
            return jsonResult.paging(new ArrayList<>(), page, size, count);
        }
        if (size>maxSize) {
            size=maxSize;
        }
        if (page>maxPage) {
            page=maxPage;
        }
        List<T> list=query(sql,objects,sortField,sortType,page,size);
        return jsonResult.paging(list,page,size,count);
    }
    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        return queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, size, maxPage, maxSize);
    }
    @Override
    public int count(String sql, Object[] params) throws Exception {
        String[]arr1=sql.split(" from ");
        String countSql="select COUNT(*) as select_count from "+arr1[1];
        List<GzbMap> list= getDataBase().selectGzbMap(countSql,params,false);
        if (list!=null && list.size() == 1) {
            return list.get(0).getInteger("select_count",0);
        }
        return 0;
    }

    @Override
    public List<T> query(String sql, Object[] objects) {
        if (t==null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(sql);
        List<T> list = new ArrayList<>();
        long[] times = new long[5];
        ResultSet rs = null;
        PreparedStatement ps = null;
        if (objects == null) {
            objects = Tools.toArray();
        }
        times[0] = System.currentTimeMillis();
        Connection connection = getConnection();
        try {
            times[1] = System.currentTimeMillis();
            ps = connection.prepareStatement(sql);
            sb.append(" data:[");
            for (int i = 0; i < objects.length; i++) {
                ps.setObject(i + 1, objects[i]);
                sb.append(objects[i]);
                if (i < objects.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            rs = ps.executeQuery();
            times[2] = System.currentTimeMillis();
            list.add((T) t.getClass().getDeclaredConstructor(ResultSet.class).newInstance(rs));
            times[3] = System.currentTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            close(connection, rs, ps);
            times[4] = System.currentTimeMillis();
            sb.append(",[连接：").append(times[1] - times[0]).append("ms]");
            sb.append(",[执行：").append(times[2] - times[1]).append("ms]");
            sb.append(",[组装：").append(times[3] - times[2]).append("ms]");
            sb.append(",[耗时：").append(times[4] - times[0]).append("ms]");
            if (times[4] - times[0] > 200) {
                log.w(sb);
            } else {
                log.d(sb);
            }
        }
        return list;
    }

    @Override
    public List<T> query(String sql, Object[] params, String sortField, String sortType, int page, int size) {
        if (t==null) {
            return null;
        }
        String pageSql = sql.toLowerCase();
        if (pageSql.endsWith(";")) {
            pageSql = pageSql.substring(0, pageSql.length() - 1);
        }
        if (sortField != null && sortType != null) {
            pageSql += " order by " + sortField + " " + sortType;
        }
        if (page > 0 && size > 0) {
            pageSql += " limit " + ((page - 1) * size) + "," + size;
        }
        return query(pageSql, params);
    }

    @Override
    public List<T> query(T t) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public List<T> query(T t, String sortField, String sortType) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, 0, 0);
    }

    @Override
    public List<T> query(T t, String sortField, String sortType, int page, int size) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, size);
    }
    @Override
    public List<T> query(T t, int page, int size) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects(), null, null, page, size);
    }

    @Override
    public T find(String sql, Object... params) throws Exception {
        if (t==null) {
            return null;
        }
        List<T> list = query(sql, params, null, null, 1, 2);
        if (list.size() != 1) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public T find(T t) throws Exception {
        if (t==null) {
            return null;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSelectSql", String.class, String.class, Integer.class, Boolean.class)
                .invoke(t, null, null, 0, false);
        List<T> list = query(sqlTemplate.getSql(), sqlTemplate.getObjects(), null, null, 1, 2);
        if (list.size() != 1) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public int save(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSave")
                .invoke(t);
        return execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int update(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toUpdate")
                .invoke(t);
        return execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int delete(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toDelete", Boolean.class)
                .invoke(t, true);
        return execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int saveBatch(List<T> list) throws Exception {
        String sql = null;
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                    .getMethod("toSave")
                    .invoke(t);
            sql = sqlTemplate.getSql();
            data.add(sqlTemplate.getObjects());
        }
        return executeBatch(sql, data);
    }

    @Override
    public int updateBatch(List<T> list) throws Exception {
        String sql = null;
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                    .getMethod("toUpdate")
                    .invoke(t);
            sql = sqlTemplate.getSql();
            data.add(sqlTemplate.getObjects());
        }
        return executeBatch(sql, data);
    }

    @Override
    public int deleteBatch(List<T> list) throws Exception {
        String sql = null;
        List<Object[]> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                    .getMethod("toDelete", Boolean.class)
                    .invoke(t, true);
            sql = sqlTemplate.getSql();
            data.add(sqlTemplate.getObjects());
        }
        return executeBatch(sql, data);
    }

    @Override
    public int saveAsync(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toSave")
                .invoke(t);
        return executeAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int updateAsync(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toUpdate")
                .invoke(t);
        return executeAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int deleteAsync(T t) throws Exception {
        if (t==null) {
            return -1;
        }
        SqlTemplate sqlTemplate = (SqlTemplate) t.getClass()
                .getMethod("toDelete", Boolean.class)
                .invoke(t, true);
        return executeAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int execute(String sql, Object[] params) throws Exception {
        List<Object[]> paramsList = new ArrayList<>();
        paramsList.add(params);
        return executeBatch(sql, paramsList, 1);
    }

    @Override
    public int executeBatch(String sql, List<Object[]> params) throws Exception {
        return executeBatch(sql, params, 1000);
    }

    @Override
    public int executeBatch(String sql, List<Object[]> params, int batch) throws Exception {
        int allRow = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        if (sql == null || params == null) {
            return -1;
        }
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        if (sql.endsWith(";")) {
            sql = sql.split(";")[0];
        }
        try {
            ps = connection.prepareStatement(sql);
            for (int j = 0; j < params.size(); j++) {
                Object[] parameter = params.get(j);
                if (parameter == null) {
                    continue;
                }
                for (int i = 0; i < parameter.length; i++) {
                    ps.setObject(i + 1, parameter[i]);
                }
                ps.addBatch();

                if ((j + 1) % batch == 0 || j + 1 == params.size()) {
                    int[] rows = ps.executeBatch();
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] == -3) {
                            log.e("SQL执行失败：", Arrays.toString(params.get(j - (20 - i))));
                        }
                    }
                    allRow += rows.length;
                    if (autoCommit) {
                        connection.commit();
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            StringBuilder stringBuilder1 = new StringBuilder(sql);
            stringBuilder1.append(" -> data:[");
            for (int j = 0; j < params.size(); j++) {
                Object[] parameter = params.get(j);
                if (parameter == null) {
                    continue;
                }
                stringBuilder1.append("{");
                for (int i = 0; i < parameter.length; i++) {
                    stringBuilder1.append(parameter[i]);
                    if (i != parameter.length - 1) {
                        stringBuilder1.append(",");
                    }
                }
                stringBuilder1.append("}");
                if (j + 1 < params.size()) {
                    stringBuilder1.append(",");
                }
            }
            stringBuilder1.append("]");
            log.e(e, "异常：" + stringBuilder1);
            throw e;
        } catch (Exception e) {
            log.e(e, "Exception 非SQL异常");
            throw e;
        } finally {
            if (autoCommit) {
                close(connection, rs, ps);
            } else {
                close(null, rs, ps);
            }
        }
        return allRow;

    }

    @Override
    public int executeAsync(String sql, Object[] params) throws Exception {
        return dataBase.runSqlAsync(sql, params);
    }
    //关闭链接 非自动提交状态例外
    public void close(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.e(e);
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                log.e(e);
            }
        }
        if (connection != null) {
            try {
                if (autoCommit) {
                    connection.setAutoCommit(true);
                    connection.close();
                    connection_class=null;
                }
            } catch (SQLException e) {
                log.e(e);
            }
        }
    }
    //获取链接 非自动提交状态 会重复获取同一个
    public Connection getConnection(){
        if (autoCommit) {
            return dataBase.getConnection();
        } else {
            if (connection_class == null) {
                connection_class = dataBase.getConnection();
                try {
                    connection_class.setAutoCommit(autoCommit);
                } catch (SQLException e) {
                    log.e(e);
                }
            }
        }
        return connection_class;
    }

    //开启事务 主要是框架内部调用 当然 也可以手动
    public void openTransaction() {
        autoCommit = false;
    }

    //关闭事务 主要是框架内部调用 当然 也可以手动
    public void endTransaction() {
        autoCommit=true;
        close(connection_class,null,null);
    }

    //提交并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    public void commit() throws SQLException {
        connection_class.commit();
    }

    //回滚并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    public void rollback() throws SQLException {
        connection_class.rollback();
    }



}
