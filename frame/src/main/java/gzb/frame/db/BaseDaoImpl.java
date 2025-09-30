/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.db;

import gzb.entity.SqlTemplate;
import gzb.exception.GzbException0;
import gzb.frame.annotation.Resource;
import gzb.frame.annotation.Service;
import gzb.frame.factory.ClassTools;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;
import gzb.tools.log.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.Lock;

@Service
public abstract class BaseDaoImpl<T> implements BaseDao<T> {

    @Resource
    public ForeignKeyFactory foreignKeyFactory;
    @Resource
    public Log log;

    private final GzbCache gzbCache = Cache.dataBaseCache;
    public T t;
    private DataBase dataBase = null;

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
        this.dataBase = DataBaseFactory.getDataBase(key);
    }


    public Connection getConnect() throws SQLException {
        return dataBase.getConnection();
    }


    @Override
    public DataBase getDataBase() {
        return this.dataBase;
    }

    @Override
    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception {
        if (t == null) {
            return null;
        }
        JSONResult jsonResult = new JSONResult();
        int count = dataBase.count(sql, objects);
        if (count <= (page - 1) * size) {
            return jsonResult.paging(new ArrayList<>(), page, size, count);
        }
        if (maxSize > 0 && size > maxSize) {
            size = maxSize;
        }
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        List<T> list = query(sql, objects, sortField, sortType, page, size, second);
        return jsonResult.paging(list, page, size, count);
    }

    @Override
    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        return queryPage(sql, objects, sortField, sortType, page, size, maxPage, maxSize, -1);
    }

    @Override
    public List<T> query(String sql, Object[] params, String sortField, String sortType, int page, int size, int second) {
        log.d(sql, params, sortField, sortType, page, size, second);
        String pageSql = sql.toLowerCase();
        if (pageSql.endsWith(";")) {
            pageSql = pageSql.substring(0, pageSql.length() - 1);
        }
        if (sortField != null && sortType != null) {
            pageSql += " order by " + sortField + " " + sortType;
        }
        if (page > 0 && size > 0) {
            pageSql += " LIMIT " + size + " OFFSET " + ((page - 1) * size);
        }
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        long[] times = new long[5];
        String key = toKey(pageSql, params);
        List<T> list = null;
        if (second > 0) {
            list = gzbCache.getObject(key, null);
            if (list != null) {
                log.d("缓存命中-0", key);
                return list;
            }
        }
        StringBuilder sb = new StringBuilder(key.length() + 50);
        sb.append(key);
        Lock lock = null;
        if (second > 0) {
            lock = LockFactory.getLock(key, second);
            lock.lock();
        }
        try {
            if (second > 0) {
                list = gzbCache.getObject(key, null);
            }
            if (list == null) {
                list = new ArrayList<>();
                if (params == null) {
                    params = Tools.toArray();
                }
                times[0] = System.currentTimeMillis();
                connection = getConnect();
                times[1] = System.currentTimeMillis();
                ps = connection.prepareStatement(pageSql);
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
                rs = ps.executeQuery();
                times[2] = System.currentTimeMillis();
                Constructor<T> constructor = null;
                ResultSetMetaData rsMetaData = rs.getMetaData();
                int columnCount = rsMetaData.getColumnCount();
                Set<String> names = new HashSet<>();
                for (int i = 0; i < columnCount; i++) {
                    names.add(rsMetaData.getColumnLabel(i + 1));
                }
                if (!names.isEmpty()) {
                    constructor = (Constructor<T>) t.getClass().getDeclaredConstructor(ResultSet.class, Set.class);
                    while (rs.next()) {
                        list.add(constructor.newInstance(rs, names));
                    }
                }
                times[3] = System.currentTimeMillis();
                if (second > 0) {
                    gzbCache.setObject(key, list, second);
                }
            } else {
                log.d("缓存命中", key);
            }

        } catch (Exception e) {
            log.e(sb.toString(), e);
        } finally {
            dataBase.close(rs, ps);
            if (lock != null) {
                lock.unlock();
            }
            if (times[0] > 0) {
                times[4] = System.currentTimeMillis();
                sb.append(",[连接：").append(times[1] - times[0]).append("ms]");
                sb.append(",[执行：").append(times[2] - times[1]).append("ms]");
                sb.append(",[组装：").append(times[3] - times[2]).append("ms]");
                log.s(sb.toString(), times[0], times[4]);
            }
        }

        return list;
    }


    @Override
    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception {
        if (t == null) {
            return null;
        }
        SqlTemplate sqlTemplate = ClassTools.toSelectSql(t);
        if (sqlTemplate == null) {
            return null;
        }
        foreignKeyFactory.eventSelect(t,true);
        JSONResult result = queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, size, maxPage, maxSize, second);
        foreignKeyFactory.eventSelect(t,false);
        return result;
    }

    @Override
    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        return queryPage(t, sortField, sortType, page, size, maxPage, maxSize, -1);
    }

    @Override
    public int count(T t) throws Exception {
        SqlTemplate sqlTemplate = ClassTools.toSelectSql(t);
        if (sqlTemplate == null) {
            return -1;
        }
        foreignKeyFactory.eventSelect(t,true);
        int size = dataBase.count(sqlTemplate.getSql(), sqlTemplate.getObjects());
        foreignKeyFactory.eventSelect(t,false);
        return size;
    }

    @Override
    public List<T> query(T t) throws Exception {
        return query(t, null, null);
    }

    @Override
    public List<T> query(T t, String sortField, String sortType) throws Exception {
        return query(t, sortField, sortType, 0, 0);
    }

    @Override
    public List<T> query(T t, int page, int size) throws Exception {
        return query(t, null, null, page, size);
    }

    @Override
    public List<T> query(T t, String sortField, String sortType, int page, int size) throws Exception {
        return query(t, sortField, sortType, page, size, -1);
    }

    @Override
    public T find(T t) throws Exception {
        if (t == null) {
            return null;
        }
        List<T> list = query(t, 1, 2);
        if (list.size() != 1) {
            return null;
        }
        return list.get(0);
    }


    private String toKey(String sql, Object[] params) {
        StringBuilder key = new StringBuilder(sql).append("_");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                key.append(params[i].toString());
                if (i < params.length - 1) {
                    key.append("_");
                }
            }
        }
        return key.toString();
    }


    @Override
    public List<T> query(T t, int second) throws Exception {
        return query(t, null, null, 0, 0, second);
    }

    @Override
    public List<T> query(T t, String sortField, String sortType, int second) throws Exception {
        return query(t, sortField, sortType, 0, 0, second);
    }

    @Override
    public List<T> query(T t, int page, int size, int second) throws Exception {
        return query(t, null, null, page, size, second);
    }


    @Override
    public List<T> query(T t, String sortField, String sortType, int page, int size, int second) throws Exception {
        if (t == null) {
            return null;
        }
        SqlTemplate sqlTemplate = ClassTools.toSelectSql(t);
        if (sqlTemplate == null) {
            return null;
        }
        foreignKeyFactory.eventSelect(t,true);
        List<T> res = query(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, size, second);
        foreignKeyFactory.eventSelect(t,false);
        return res;
    }

    @Override
    public int save(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toSaveSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        foreignKeyFactory.eventSave(t,true);
        int size = dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects());
        foreignKeyFactory.eventSave(t,false);
        return size;
    }

    @Override
    public int update(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toUpdateSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        foreignKeyFactory.eventUpdate(t,true);
        int size = dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects());
        foreignKeyFactory.eventUpdate(t,false);
        return size;
    }

    @Override
    public int delete(T t) throws Exception {
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toDeleteSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        foreignKeyFactory.eventDelete(t,true);
        int size = dataBase.runSql(sqlTemplate.getSql(), sqlTemplate.getObjects());
        foreignKeyFactory.eventDelete(t,false);
        return size;
    }

    @Override
    public int saveAsync(T t) throws Exception {
        if (dataBase.isOpenTransaction()) {
            throw new RuntimeException("开启事务的时候无法使用异步执行");
        }
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toSaveSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        int size = dataBase.runSqlAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
        return size;
    }

    @Override
    public int updateAsync(T t) throws Exception {
        if (dataBase.isOpenTransaction()) {
            throw new RuntimeException("开启事务的时候无法使用异步执行");
        }
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toUpdateSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        int size = dataBase.runSqlAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
        return size;
    }

    @Override
    public int deleteAsync(T t) throws Exception {
        if (dataBase.isOpenTransaction()) {
            throw new RuntimeException("开启事务的时候无法使用异步执行");
        }
        if (t == null) {
            return -1;
        }
        SqlTemplate sqlTemplate = ClassTools.toDeleteSql(t);
        if (sqlTemplate == null) {
            return -2;
        }
        int size = dataBase.runSqlAsync(sqlTemplate.getSql(), sqlTemplate.getObjects());
        return size;
    }


    @Override
    public int execute(String sql, Object[] params) throws Exception {
        return dataBase.runSql(sql, params);
    }


}
