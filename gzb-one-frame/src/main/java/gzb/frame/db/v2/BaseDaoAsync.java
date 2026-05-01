package gzb.frame.db.v2;

import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.exception.GzbException0;
import gzb.frame.PublicEntrance;
import gzb.frame.factory.ClassTools;
import gzb.frame.language.Template;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
public abstract class BaseDaoAsync<T> implements BaseDao<T> {

    GzbCache gzbCache = Cache.dataBaseCache;
    Class<?> entityClass;
    T entity;
    Log log = Log.log;
    public DataBase dataBase = null;
    public DataBaseConfig dataBaseConfig = null;

    public BaseDaoAsync() {
        try {
            // 获取当前类的泛型父类
            Type superclass = this.getClass().getGenericSuperclass();
            if (superclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) superclass;
                // 获取泛型类型参数数组
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                    entityClass = (Class<?>) actualTypeArguments[0];
                    entity = (T) entityClass.getDeclaredConstructor().newInstance();
                }
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    public static class Column {
        public String name;
        public Class<?> clazz;

        public Object toValue(Object object) throws ParseException {
            if (object == null) {
                return null;
            }
            if (object.toString().isEmpty()) {
                return null;
            }
            if (object.getClass() == clazz) {
                return object;
            }
            if (clazz == String.class) return object.toString();
            if (clazz == Long.class) return Long.valueOf(object.toString());
            if (clazz == Integer.class) return Integer.valueOf(object.toString());
            if (clazz == Short.class) return Short.valueOf(object.toString());
            if (clazz == Float.class) return Float.valueOf(object.toString());
            if (clazz == Double.class) return Double.valueOf(object.toString());
            if (clazz == Boolean.class) return Boolean.valueOf(object.toString());
            if (clazz == byte[].class) return object.toString().getBytes(Config.encoding);

            if (clazz == Timestamp.class) {
                return new DateTime((String) object).toTimestamp();
            }
            if (clazz == LocalDateTime.class) {
                return new DateTime((String) object).toLocalDateTime();
            }
            throw new GzbException0(Template.THIS_LANGUAGE[48] + " Column.toValue(Object object) " + Template.THIS_LANGUAGE[49] + ":" + object + " " +
                    Template.THIS_LANGUAGE[50] + "：" + clazz.getName());
        }
    }

    public void init(String key) {
        dataBaseConfig = new DataBaseConfig();
        dataBaseConfig.read(key);
        dataBase = DataBaseFactory.getDataBase(dataBaseConfig);
    }


    public List<T> query(String sql, Object[] objects, int second) {
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        String key = Tools.toKey(sql, objects);
        Lock lock;
        if (second > -1) {
            List<T> list0 = gzbCache.getObject(key);
            if (list0 != null) {
                log.d("cache hit",sql,objects);
                return list0;
            }
            lock = LockFactory.getLock(key, second);
            lock.lock();
        } else {
            lock = null;
        }

        try {
            if (second > -1) {
                List<T> list0 = gzbCache.getObject(key);
                if (list0 != null) {
                    log.d("cache hit",sql,objects);
                    return list0;
                }
            }
            dataBase.query(sql, objects, new DataBase.Cack() {
                @Override
                public void success(RowSet<Row> rowSet) {
                    List<T> list0 = null;
                    try {
                        list0 = ClassTools.loadResultSet(entityClass, rowSet);
                        if (second > -1) {
                            gzbCache.setObject(key, list0, second);
                        }
                    } finally {
                        if (list0 == null) {
                            wake.notifyActivation(new ArrayList<>());
                        } else {
                            wake.notifyActivation(list0);
                        }
                    }
                }

                @Override
                public void fail(String sql, Object[] objects, Throwable throwable) {
                    wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
                }
            });
            return wake.waitActivationData();
        } finally {
            if (second > -1) {
                lock.unlock();
            }
        }
    }

    public List<T> query(String sql, Object[] objects) {
        return query(sql, objects, -1);
    }

    public int execute(String sql, Object[] objects) {
        return dataBase.execute(sql, objects);
    }

    public int execute(String sql, List<Object[]> list) throws Throwable {
        return dataBase.execute(sql, list, true);
    }

    public int execute(String sql, List<Object[]> list, boolean transaction) throws Throwable {
        return dataBase.execute(sql, list, transaction);
    }

    private static final String FROM_KEY = " from ";
    private static final String LIMIT_KEY = " LIMIT ";
    private static final String COUNT_PREFIX = "select COUNT(*) as select_count from ";

    public Object[] getCountSql(String sql, Object[] objects) {
        int index = sql.indexOf(FROM_KEY);
        if (index == -1) {
            index = sql.toLowerCase().indexOf(FROM_KEY);
            if (index == -1) {
                return null;
            }
        }
        int index2 = sql.lastIndexOf(LIMIT_KEY);
        if (index2 == -1) {
            index2 = sql.toUpperCase().lastIndexOf(LIMIT_KEY);
        }
        if (index2 == -1) {
            return new Object[]{COUNT_PREFIX + sql.substring(index + FROM_KEY.length()), objects};
        } else {
            int size = 0;
            for (int i = index2 + LIMIT_KEY.length(); i < sql.length(); i++) {
                if (dataBaseConfig.sql_type == 0) {
                    if (sql.charAt(i) == '?') {
                        size++;
                    }
                } else {
                    if (sql.charAt(i) == '$') {
                        size++;
                    }
                }
            }
            String s = COUNT_PREFIX + sql.substring(index + FROM_KEY.length(), index2);
            if (size == 0) {
                return new Object[]{s, objects};
            }
            Object[] objects1 = new Object[objects.length - size];
            System.arraycopy(objects, 0, objects1, 0, objects1.length);
            return new Object[]{s, objects1};
        }

    }

    public int count(String sql, Object[] objects, int second) throws Exception {
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        Object[] tem = getCountSql(sql, objects);
        if (tem[0] == null) {
            throw new GzbException0("sql error" + sql);
        }
        sql = (String) tem[0];
        objects = (Object[]) tem[1];
        String key = Tools.toKey(sql, objects);
        Lock lock;
        if (second > -1) {
            String tmp = gzbCache.get(key);
            if (tmp != null) {
                log.d("cache hit",sql,objects);
                return Integer.parseInt(tmp);
            }
            lock = LockFactory.getLock(key, second);
            lock.lock();
        } else {
            lock = null;
        }
        try {
            if (second > -1) {
                String tmp = gzbCache.get(key);
                if (tmp != null) {
                    log.d("cache hit",sql,objects);
                    return Integer.parseInt(tmp);
                }
            }
            dataBase.query(sql, objects, new DataBase.Cack() {
                @Override
                public void success(RowSet<Row> rowSet) {
                    int count = 0;
                    try {
                        for (Row row : rowSet) {
                            count = row.getInteger("select_count");
                            if (second > -1) {
                                gzbCache.set(key, String.valueOf(count), second);
                            }
                            break;
                        }
                    } finally {
                        wake.notifyActivation(count);
                    }
                }

                @Override
                public void fail(String sql, Object[] objects, Throwable throwable) {
                    wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
                }
            });
            return wake.waitActivationData();
        } catch (Throwable throwable) {
            wake.notifyActivation(new GzbException0("sql init exec error",throwable,sql,objects));
        } finally {
            if (second > -1) {
                lock.unlock();
            }
        }
        return 0;
    }

    public int count(String sql, Object[] objects) throws Exception {
        return count(sql, objects, -1);
    }

    public List<T> query(T t, String sortField, String sortType, Integer page, Integer size, int second) throws Exception {
        List<T> list = null;
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            sqlTemplate = ClassTools.toSelectSql(entity, dataBaseConfig.sql_type, sortField, sortType, page, size);
        } else {
            sqlTemplate = ClassTools.toSelectSql(t, dataBaseConfig.sql_type, sortField, sortType, page, size);
        }
        if (sqlTemplate == null) {
            return new ArrayList<>(0);
        }
        if (!PublicEntrance.eventFactory.eventSelect(t, true)) {
            return new ArrayList<>(0);
        }
        list = query(sqlTemplate.getSql(), sqlTemplate.getObjects(), second);
        if (!PublicEntrance.eventFactory.eventSelect(t, false)) {
            return new ArrayList<>(0);
        }
        return list;
    }

    public List<T> query(T t, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return query(t, sortField, sortType, page, size, -1);
    }

    public List<T> query(T t, Integer page, Integer size, int second) throws Exception {
        return query(t, null, null, page, size, second);
    }

    public List<T> query(T t, Integer page, Integer size) throws Exception {
        return query(t, page, size, -1);
    }

    public List<T> query(T t, int second) throws Exception {
        return query(t, null, null, null, null, second);
    }

    public List<T> query(T t) throws Exception {
        return query(t, -1);
    }

    public T find(T t, int second) throws Exception {
        List<T> list = query(t, null, null, null, null, second);
        if (list.size() != 1) {
            return null;
        }
        return list.get(0);
    }

    public T find(T t) throws Exception {
        return find(t, -1);
    }


    public int count(T t, String sortField, String sortType, Integer page, Integer size, int second) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            sqlTemplate = ClassTools.toSelectSql(entity, dataBaseConfig.sql_type, sortField, sortType, page, size);
        } else {
            sqlTemplate = ClassTools.toSelectSql(t, dataBaseConfig.sql_type, sortField, sortType, page, size);
        }
        if (sqlTemplate == null) {
            return 0;
        }
        if (!PublicEntrance.eventFactory.eventSelect(t, true)) {
            return 0;
        }
        int count = count(sqlTemplate.getSql(), sqlTemplate.getObjects(), second);
        if (!PublicEntrance.eventFactory.eventSelect(t, false)) {
            return 0;
        }
        return count;
    }

    public int count(T t, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return count(t, sortField, sortType, page, size, -1);
    }

    public int count(T t, Integer page, Integer size, int second) throws Exception {
        return count(t, null, null, page, size, second);
    }

    public int count(T t, Integer page, Integer size) throws Exception {
        return count(t, page, size, -1);
    }

    public int count(T t, int second) throws Exception {
        return count(t, null, null, null, null, second);
    }

    public int count(T t) throws Exception {
        return count(t, -1);
    }

    /// 为了兼容性
    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }
        JSONResult jsonResult = new JSONResult();
        int count = count(sql, objects, second);
        if (count <= (page - 1) * size) {
            return jsonResult.paging(new ArrayList<>(), page, size, count);
        }
        if (maxSize > 0 && size > maxSize) {
            size = maxSize;
        }
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        List<T> list = query(sql, objects, second);
        return jsonResult.paging(list, page, size, count);
    }

    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        return queryPage(sql, objects, sortField, sortType, page, size, maxPage, maxSize, -1);
    }

    public JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return queryPage(sql, objects, sortField, sortType, page, size, 0, 0, -1);
    }

    public JSONResult queryPage(String sql, Object[] objects, Integer page, Integer size) throws Exception {
        return queryPage(sql, objects, null, null, page, size, 0, 0, -1);
    }


    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }
        JSONResult jsonResult = new JSONResult();
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            sqlTemplate = ClassTools.toSelectSql(entity, dataBaseConfig.sql_type, sortField, sortType, page, size);
        } else {
            sqlTemplate = ClassTools.toSelectSql(t, dataBaseConfig.sql_type, sortField, sortType, page, size);
        }
        if (sqlTemplate == null) {
            return jsonResult.paging(new ArrayList<>(0), page, size, 0);
        }
        int count = count(sqlTemplate.getSql(), sqlTemplate.getObjects(), second);
        if (count <= (page - 1) * size) {
            return jsonResult.paging(new ArrayList<>(), page, size, count);
        }
        if (maxSize > 0 && size > maxSize) {
            size = maxSize;
        }
        if (maxPage > 0 && page > maxPage) {
            page = maxPage;
        }
        List<T> list = query(sqlTemplate.getSql(), sqlTemplate.getObjects(), second);
        return jsonResult.paging(list, page, size, count);
    }

    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception {
        return queryPage(t, sortField, sortType, page, size, maxPage, maxSize, -1);
    }

    public JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return queryPage(t, sortField, sortType, page, size, 0, 0, -1);
    }

    public JSONResult queryPage(T t, Integer page, Integer size, int second) throws Exception {
        return queryPage(t, null, null, page, size, 0, 0, second);
    }

    public JSONResult queryPage(T t, Integer page, Integer size) throws Exception {
        return queryPage(t, page, size, -1);
    }


    public int save(T t) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toSaveSql(t, dataBase, false, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventSave(t, true)) {
            return -3;
        }
        int res = execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
        if (!PublicEntrance.eventFactory.eventSave(t, true)) {
            return -4;
        }
        return res;
    }

    public int update(T t) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toUpdateSql(t, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventUpdate(t, true)) {
            return -3;
        }
        int res = execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
        if (!PublicEntrance.eventFactory.eventUpdate(t, true)) {
            return -4;
        }
        return res;
    }

    public int delete(T t) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toDeleteSql(t, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventDelete(t, true)) {
            return -3;
        }
        int res = execute(sqlTemplate.getSql(), sqlTemplate.getObjects());
        if (!PublicEntrance.eventFactory.eventDelete(t, true)) {
            return -4;
        }
        return res;
    }


    public int save(List<T> list) throws Throwable {
        int all = 0;
        Map<String, List<Object[]>> map = new HashMap<>();
        Map<String, List<Integer>> mapIndex = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t == null) {
                throw new GzbException0("list.get(" + i + ") == null");
            }
            SqlTemplate sqlTemplate = ClassTools.toSaveSql(t, dataBase, false, dataBaseConfig.sql_type);
            if (sqlTemplate == null) {
                throw new GzbException0("list.get(" + i + ") error sql");
            }
            List<Object[]> list1 = map.get(sqlTemplate.getSql());
            if (list1 == null) {
                list1 = new ArrayList<>(list.size());
                map.put(sqlTemplate.getSql(), list1);
            }
            List<Integer> list2 = mapIndex.get(sqlTemplate.getSql());
            if (list2 == null) {
                list2 = new ArrayList<>(list.size());
                mapIndex.put(sqlTemplate.getSql(), list2);
            }
            list1.add(sqlTemplate.getObjects());
            list2.add(i);
        }
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        dataBase.openTransaction(entity);
        try {
            for (Map.Entry<String, List<Object[]>> stringListEntry : map.entrySet()) {
                List<Integer> index = mapIndex.get(stringListEntry.getKey());
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventSave(t, true)) {
                        throw new GzbException0("event save Interruption index before = true");
                    }
                }
                all += dataBase.execute(stringListEntry.getKey(), stringListEntry.getValue(), false);
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventSave(t, false)) {
                        throw new GzbException0("event save Interruption index before = false");
                    }
                }
            }
            dataBase.commit(entity);
        } catch (Throwable e) {
            dataBase.rollback(entity);
            throw e;
        } finally {
            dataBase.endTransaction(entity);
        }
        return all;
    }

    public int update(List<T> list) throws Throwable {
        int all = 0;
        Map<String, List<Object[]>> map = new HashMap<>();
        Map<String, List<Integer>> mapIndex = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t == null) {
                throw new GzbException0("list.get(" + i + ") == null");
            }
            SqlTemplate sqlTemplate = ClassTools.toUpdateSql(t, dataBaseConfig.sql_type);
            if (sqlTemplate == null) {
                throw new GzbException0("list.get(" + i + ") error sql");
            }
            List<Object[]> list1 = map.get(sqlTemplate.getSql());
            if (list1 == null) {
                list1 = new ArrayList<>(list.size());
                map.put(sqlTemplate.getSql(), list1);
            }
            List<Integer> list2 = mapIndex.get(sqlTemplate.getSql());
            if (list2 == null) {
                list2 = new ArrayList<>(list.size());
                mapIndex.put(sqlTemplate.getSql(), list2);
            }
            list1.add(sqlTemplate.getObjects());
            list2.add(i);
        }
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        dataBase.openTransaction(entity);
        try {
            for (Map.Entry<String, List<Object[]>> stringListEntry : map.entrySet()) {
                List<Integer> index = mapIndex.get(stringListEntry.getKey());
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventUpdate(t, true)) {
                        throw new GzbException0("event update Interruption index before = true");
                    }
                }
                all += dataBase.execute(stringListEntry.getKey(), stringListEntry.getValue(), false);
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventUpdate(t, false)) {
                        throw new GzbException0("event update Interruption index before = false");
                    }
                }
            }
            dataBase.commit(entity);
        } catch (Throwable e) {
            dataBase.rollback(entity);
            throw e;
        } finally {
            dataBase.endTransaction(entity);
        }
        return all;
    }

    public int delete(List<T> list) throws Throwable {
        int all = 0;
        Map<String, List<Object[]>> map = new HashMap<>();
        Map<String, List<Integer>> mapIndex = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t == null) {
                throw new GzbException0("list.get(" + i + ") == null");
            }
            SqlTemplate sqlTemplate = ClassTools.toDeleteSql(t, dataBaseConfig.sql_type);
            if (sqlTemplate == null) {
                throw new GzbException0("list.get(" + i + ") error sql");
            }
            List<Object[]> list1 = map.get(sqlTemplate.getSql());
            if (list1 == null) {
                list1 = new ArrayList<>(list.size());
                map.put(sqlTemplate.getSql(), list1);
            }
            List<Integer> list2 = mapIndex.get(sqlTemplate.getSql());
            if (list2 == null) {
                list2 = new ArrayList<>(list.size());
                mapIndex.put(sqlTemplate.getSql(), list2);
            }
            list1.add(sqlTemplate.getObjects());
            list2.add(i);
        }
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        dataBase.openTransaction(entity);
        try {
            for (Map.Entry<String, List<Object[]>> stringListEntry : map.entrySet()) {
                List<Integer> index = mapIndex.get(stringListEntry.getKey());
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventDelete(t, true)) {
                        throw new GzbException0("event delete Interruption index before = true");
                    }
                }
                all += dataBase.execute(stringListEntry.getKey(), stringListEntry.getValue(), false);
                for (int i = 0; i < stringListEntry.getValue().size(); i++) {
                    T t = list.get(index.get(i));
                    if (!PublicEntrance.eventFactory.eventDelete(t, false)) {
                        throw new GzbException0("event delete Interruption index before = false");
                    }
                }
            }
            dataBase.commit(entity);
        } catch (Throwable e) {
            dataBase.rollback(entity);
            throw e;
        } finally {
            dataBase.endTransaction(entity);
        }
        return all;
    }

    public int saveAsync(T t) throws Exception {
        return saveAsync(t, null, null);
    }

    public int saveAsync(T t, Runnable fail) throws Exception {
        return saveAsync(t, fail, null);
    }

    public int saveAsync(T t, Runnable fail, Runnable success) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toSaveSql(t, dataBase, false, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventSave(t, true)) {
            return -3;
        }
        int res = dataBase.asyncFactory.add(new AsyncFactory.Result(sqlTemplate.getSql(), sqlTemplate.getObjects(), fail, success));
        if (!PublicEntrance.eventFactory.eventSave(t, true)) {
            return -4;
        }
        return res;
    }

    public int updateAsync(T t) throws Exception {
        return updateAsync(t, null, null);
    }

    public int updateAsync(T t, Runnable fail) throws Exception {
        return updateAsync(t, fail, null);
    }

    public int updateAsync(T t, Runnable fail, Runnable success) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toUpdateSql(t, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventUpdate(t, true)) {
            return -3;
        }
        int res = dataBase.asyncFactory.add(new AsyncFactory.Result(sqlTemplate.getSql(), sqlTemplate.getObjects(), fail, success));
        if (!PublicEntrance.eventFactory.eventUpdate(t, true)) {
            return -4;
        }
        return res;
    }

    public int deleteAsync(T t) throws Exception {
        return deleteAsync(t, null, null);
    }

    public int deleteAsync(T t, Runnable fail) throws Exception {
        return deleteAsync(t, fail, null);
    }

    public int deleteAsync(T t, Runnable fail, Runnable success) throws Exception {
        SqlTemplate sqlTemplate = null;
        if (t == null) {
            return -1;
        } else {
            sqlTemplate = ClassTools.toDeleteSql(t, dataBaseConfig.sql_type);
        }
        if (sqlTemplate == null) {
            return -2;
        }
        if (!PublicEntrance.eventFactory.eventDelete(t, true)) {
            return -3;
        }
        int res = dataBase.asyncFactory.add(new AsyncFactory.Result(sqlTemplate.getSql(), sqlTemplate.getObjects(), fail, success));
        if (!PublicEntrance.eventFactory.eventDelete(t, true)) {
            return -4;
        }
        return res;
    }


    public DataBase getDataBase() {
        return dataBase;
    }
}
