package gzb.frame.db;

import com.frame.entity.SysUsers;
import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.EntityAttribute;
import gzb.frame.annotation.Service;
import gzb.tools.GzbMap;
import gzb.tools.JSONResult; 

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.*;

@Service
public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    public static void main(String[] args) throws Exception {
        BaseDao baseDao = new BaseDaoImpl(){};
        SqlTemplate sqlTemplate = new SysUsers().toSelectSql();
        List<GzbMap> list = baseDao.getDataBase().selectGzbMap(sqlTemplate.getSql(),sqlTemplate.getObjects());
        for (GzbMap gzbMap : list) {
            System.out.println(gzbMap);
            System.out.println(gzbMap.getString("sysUsersId"));
            System.out.println(gzbMap.getString("sysUsersId","1"));
            System.out.println(gzbMap.getLong("sysUsersId"));
            System.out.println(gzbMap.getLong("sysUsersId", 1L));
        }
    }
    public T t;
    public DataBase dataBase;
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
            // 捕获并处理可能出现的异常
            e.printStackTrace();
        }
    }
    public void init(DataBase dataBase) throws Exception {
        this.dataBase= dataBase;
    }
    public void init(String dbName) throws Exception {

        this.dataBase= DataBaseFactory.getDataBase(dbName);
    }
    public void init(String dbName, String ip, String port, String acc, String pwd, String clz, Boolean auto, Integer threadMax, Integer overtime, Integer asyncSleep) throws Exception {
        this.dataBase= DataBaseFactory.getDataBase(dbName,ip,port,acc,pwd,clz,auto,threadMax,overtime,asyncSleep);
    }
    public int runSql(List<T> list, boolean autoId, int sqlType, int async, Connection connection) throws Exception {
        Map<String, List<Object[]>> sqlMap = new HashMap<>();
        for (T t1 : list) {
            SqlTemplate sqlTemplate = null;
            if (sqlType == 1) {
                sqlTemplate = dataBase.toSave(t1, autoId);
            } else if (sqlType == 2) {
                sqlTemplate = dataBase.toUpdate(t1);
            } else if (sqlType == 3) {
                sqlTemplate = dataBase.toDelete(t1);
            } else {
                continue;
            }
            List<Object[]> listObject = sqlMap.get(sqlTemplate.getSql());
            if (listObject == null) {
                listObject = new ArrayList<>();
                sqlMap.put(sqlTemplate.getSql(),listObject);
            }
            listObject.add(sqlTemplate.getObjects());
        }
        int row = 0;
        for (Iterator<Map.Entry<String, List<Object[]>>> it = sqlMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, List<Object[]>> en = it.next();
            if (async == 1) {
                if (connection == null) {
                    row += dataBase.runSqlBatch(en.getKey(), en.getValue());
                } else {
                    row += dataBase.runSqlBatch(en.getKey(), en.getValue(), connection);
                }
            } else if (async == 2) {
                row += dataBase.runSqlAsyncBatch(en.getKey(), en.getValue());
            }
        }
        return row;
    }

    //数据 list  是否自动给id autoId sql类型 sqlType  是否异步 async
    public int runSql(List<T> list, boolean autoId, int sqlType, int async) throws Exception {
        return runSql(list, autoId, sqlType, async, null);
    }


    @Override
    public DataBase getDataBase() {
        return dataBase;
    }

    @Override
    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public T find(String sql, Object[] objects) throws Exception {
        List<T> list = query(sql, objects);
        if (list == null || list.size() != 1) {
            return null;
        }
        return list.get(0);
    }


    @Override
    public List<T> query(String sql, Object[] objects) throws Exception {
        return query(sql, objects,null,null, 0, 0);
    }

    @Override
    public List<T> query(String sql, Object[] objects, Integer page, Integer size) throws Exception {
        return query(sql, objects,null,null, page, size);
    }

    @Override
    public List<T> query(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size) throws Exception {
        List<GzbMap> listGzbMap;
        if (sql.toLowerCase().indexOf(" order ") == -1 && sql.toLowerCase().indexOf(" limit ") == -1) {
            StringBuilder sb = new StringBuilder(sql);
            if (sortField == null) {
                EntityClassInfo entityClassInfo = dataBase.getEntityInfo(t);
                EntityAttribute entityAttributeClass = entityClassInfo.fields.get(entityClassInfo.keyIndex).getDeclaredAnnotation(EntityAttribute.class);
                sortField = entityAttributeClass.name();
            }
            sb.append(" order by ").append(sortField).append(" ").append(sortType == null ? "asc" : sortType);
            listGzbMap = dataBase.selectGzbMap(sb.toString(), objects);
        } else {
            listGzbMap = dataBase.selectGzbMap(sql, objects);
        }
        int start = 0;
        if (page == null || page < 1) {
            page = 1;
        }
        if (page > 1) {
            start = (page - 1) * size;
        }
        List<T> listThis = new ArrayList<>();
        for (int i = start; i < listGzbMap.size(); i++) {
            listThis.add((T)t.getClass().getDeclaredConstructor(GzbMap.class).newInstance(listGzbMap.get(i)));
            if (listThis.size() == size) {
                break;
            }
        }
        return listThis;
    }

    @Override
    public JSONResult queryPage(String sql, Object[] objects, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        return queryPage(sql, objects, null, null, page, size, maxPage, maxSize);
    }

    @Override
    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        if (t == null) {
            return null;
        }
        List<GzbMap> listGzbMap;
        if (sql.toLowerCase().indexOf(" order ") == -1 && sql.toLowerCase().indexOf(" limit ") == -1) {
            StringBuilder sb = new StringBuilder(sql);
            if (sortField == null) {
                EntityClassInfo entityClassInfo = dataBase.getEntityInfo(t);
                EntityAttribute entityAttributeClass = entityClassInfo.fields.get(entityClassInfo.keyIndex).getDeclaredAnnotation(EntityAttribute.class);
                sortField = entityAttributeClass.name();
            }
            sb.append(" order by ").append(sortField).append(" ").append(sortType == null ? "asc" : sortType);
            listGzbMap = dataBase.selectGzbMap(sb.toString(), objects);
        } else {
            listGzbMap = dataBase.selectGzbMap(sql, objects);
        }

        List<T> listThis = new ArrayList<>();
        for (int i = 0; i < listGzbMap.size(); i++) {
            listThis.add((T)t.getClass().getDeclaredConstructor(GzbMap.class).newInstance(listGzbMap.get(i)));
        }
        return new JSONResult().paging(listThis,page, size,maxPage,maxSize);
    }

    @Override
    public int save(String sql, Object[] objects) throws Exception {
        return dataBase.runSql(sql, objects);
    }


    @Override
    public int update(String sql, Object[] objects) throws Exception {
        return dataBase.runSql(sql, objects);
    }

    @Override
    public int delete(String sql, Object[] objects) throws Exception {
        return dataBase.runSql(sql, objects);
    }

    @Override
    public int saveAsync(String sql, Object[] objects) {
        return dataBase.runSqlAsync(sql, objects);
    }


    @Override
    public int updateAsync(String sql, Object[] objects) {
        return dataBase.runSqlAsync(sql, objects);
    }

    @Override
    public int deleteAsync(String sql, Object[] objects) {
        return dataBase.runSqlAsync(sql, objects);
    }

    @Override
    public int saveBatch(String sql, List<Object[]> list) throws Exception {
        return dataBase.runSqlBatch(sql, list);
    }


    @Override
    public int updateBatch(String sql, List<Object[]> list) throws Exception {
        return dataBase.runSqlBatch(sql, list);
    }

    @Override
    public int deleteBatch(String sql, List<Object[]> list) throws Exception {
        return dataBase.runSqlBatch(sql, list);
    }

    @Override
    public int saveBatchAsync(String sql, List<Object[]> list) {
        return dataBase.runSqlAsyncBatch(sql, list);
    }

    @Override
    public int updateBatchAsync(String sql, List<Object[]> list) {
        return dataBase.runSqlAsyncBatch(sql, list);
    }

    @Override
    public int deleteBatchAsync(String sql, List<Object[]> list) {
        return dataBase.runSqlAsyncBatch(sql, list);
    }

    @Override
    public T find(T t) throws Exception {
        SqlTemplate sqlTemplate = dataBase.toSelect(t, 1, 2, null, null);
        return find(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public List<T> query(T t) throws Exception {
        SqlTemplate sqlTemplate = dataBase.toSelect(t, 1, 0, null, null);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public List<T> query(T t, Integer page, Integer size) throws Exception {
        return query(t, null, null, page, size);
    }

    @Override
    public List<T> query(T t, String sortField, String sortType, Integer page, Integer size) throws Exception {
        SqlTemplate sqlTemplate = dataBase.toSelect(t, 1, 0, sortField, sortType);
        return query(sqlTemplate.getSql(), sqlTemplate.getObjects(), page, size);
    }

    @Override
    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        t= t==null ? this.t:t;
        SqlTemplate sqlTemplate = dataBase.toSelect(t, 1, 0, sortField, sortType);
        return queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), page, size, maxPage, maxSize);
    }

    @Override
    public int save(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toSave(t);
        return save(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int save(T t, boolean autoId) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toSave(t, autoId);
        return save(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int update(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toUpdate(t);
        return update(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int delete(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toDelete(t);
        return delete(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int saveAsync(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toSave(t);
        return saveAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int saveAsync(T t, boolean autoId) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toSave(t, autoId);
        return saveAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int updateAsync(T t) {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toUpdate(t);
        return updateAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int deleteAsync(T t) {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toDelete(t);
        return deleteAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
    }

    @Override
    public int saveBatch(List<T> list) throws Exception {
        return saveBatch(list, true);
    }

    @Override
    public int saveBatch(List<T> list, boolean autoId) throws Exception {
        return runSql(list, autoId, 1, 1);
    }

    @Override
    public int updateBatch(List<T> list) throws Exception {
        return runSql(list, false, 2, 1);
    }

    @Override
    public int deleteBatch(List<T> list) throws Exception {
        return runSql(list, false, 3, 1);
    }

    @Override
    public int saveBatchAsync(List<T> list) throws Exception {
        return saveBatchAsync(list, true);
    }

    @Override
    public int saveBatchAsync(List<T> list, boolean autoId) throws Exception {
        return runSql(list, autoId, 1, 2);
    }

    @Override
    public int updateBatchAsync(List<T> list) throws Exception {
        return runSql(list, false, 2, 2);
    }

    @Override
    public int deleteBatchAsync(List<T> list) throws Exception {
        return runSql(list, false, 3, 2);
    }

    @Override
    public int save(String sql, Object[] objects, Connection connection) throws Exception {
        return dataBase.runSql(sql, objects, connection);
    }

    @Override
    public int update(String sql, Object[] objects, Connection connection) throws Exception {
        return dataBase.runSql(sql, objects, connection);
    }

    @Override
    public int delete(String sql, Object[] objects, Connection connection) throws Exception {
        return dataBase.runSql(sql, objects, connection);
    }

    @Override
    public int saveBatch(String sql, List<Object[]> list, Connection connection) throws Exception {
        return dataBase.runSqlBatch(sql, list, connection);
    }

    @Override
    public int updateBatch(String sql, List<Object[]> list, Connection connection) throws Exception {
        return dataBase.runSqlBatch(sql, list, connection);
    }

    @Override
    public int deleteBatch(String sql, List<Object[]> list, Connection connection) throws Exception {
        return dataBase.runSqlBatch(sql, list, connection);
    }

    @Override
    public int save(T t, Connection connection) throws Exception {
        if (t == null) {
            return -1;
        }
        return save(t, true, connection);
    }

    @Override
    public int save(T t, boolean autoId, Connection connection) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toSave(t, autoId);
        return dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects(), connection);
    }

    @Override
    public int update(T t, Connection connection) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toUpdate(t);
        return dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects(), connection);
    }

    @Override
    public int delete(T t, Connection connection) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = dataBase.toDelete(t);
        return dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects(), connection);
    }

    @Override
    public int saveBatch(List<T> list, Connection connection) throws Exception {
        return saveBatch(list,true, connection);
    }

    @Override
    public int saveBatch(List<T> list, boolean autoId, Connection connection) throws Exception {
        return runSql(list, autoId, 1, 1, connection);
    }

    @Override
    public int updateBatch(List<T> list, Connection connection) throws Exception {
        return runSql(list, false, 2, 1, connection);
    }

    @Override
    public int deleteBatch(List<T> list, Connection connection) throws Exception {
        return runSql(list, false, 3, 1, connection);
    }
}
