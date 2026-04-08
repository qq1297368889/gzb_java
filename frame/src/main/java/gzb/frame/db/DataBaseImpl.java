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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.exception.GzbException0;
import gzb.frame.PublicEntrance;
import gzb.frame.annotation.EntityAttribute;
import gzb.frame.db.entity.TransactionEntity;
import gzb.frame.language.Template;
import gzb.tools.*;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class DataBaseImpl implements DataBase {

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
            throw new GzbException0(Template.THIS_LANGUAGE[48]+" DataBaseImpl.Column.toValue(Object object) "+Template.THIS_LANGUAGE[49]+":" + object + " "+
                    Template.THIS_LANGUAGE[50]+"：" + clazz.getName());
        }
    }

    static Object[] defNullArray = new Object[0];


    public AsyncFactory asyncFactory = null;

    DataBaseConfig dataBaseConfig;
    public static Log log = Log.log;
    public static String[] montageArr = new String[]{"and", "or", "and(", "or(", ")and", ")or", "(", ")"};
    public static String[] symbolArr = new String[]{"=", ">", ">=", "<", "<=", "<>", "%like%", "like%", "%like"};

    public HikariDataSource hds = null;
    public Map<String, Column> columnInfoMap = new ConcurrentHashMap<>();
    public Map<String, String> columnInfoMapSy = new ConcurrentHashMap<>();

    Map<Object, EntityClassInfo> mapEntityClassInfo = new ConcurrentHashMap<>();

    //关闭数据库连接池 这个操作后果自负
    public void close() {
        hds.close();
    }

    public DataBaseImpl(DataBaseConfig dataBaseConfig) throws Exception {
        this.dataBaseConfig = dataBaseConfig;
        initDataBase();
    }

    public DataBaseImpl(String key) throws Exception {
        dataBaseConfig = DataBaseConfig.readConfig(key);
        initDataBase();
    }

    public DataBaseImpl(String type, String key, String clz, String ip, int port, String name, String acc, String pwd, int threadMax, int overtime, int asyncSleepMilli, int asyncBatchSize,
                        int asyncThreadNum, int asyncQueueSize, String parar, String sign) throws Exception {
        dataBaseConfig = DataBaseConfig.readConfig(type, key, clz, ip, port, name, acc, pwd, threadMax, overtime, asyncSleepMilli, asyncBatchSize, asyncThreadNum, asyncQueueSize, parar, sign);
        initDataBase();
    }

    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public String getSign() {
        return dataBaseConfig.getSign();
    }

    public AsyncFactory getAsyncFactory() {
        return asyncFactory;
    }

    private void initDataBase() {
        log.d(Template.THIS_LANGUAGE[0], Template.THIS_LANGUAGE[1], dataBaseConfig);
        try {
            hds = getHikariDataSource();
            log.d(Template.THIS_LANGUAGE[0], Template.THIS_LANGUAGE[2],dataBaseConfig.name);
            readTableInfo();
            log.d(Template.THIS_LANGUAGE[0], Template.THIS_LANGUAGE[3],dataBaseConfig.name);

            asyncFactory = new AsyncFactory(
                    this, dataBaseConfig.asyncThreadNum, dataBaseConfig.asyncBatchSize
                    , dataBaseConfig.asyncSleepMilli, dataBaseConfig.asyncQueueSize);
            log.d(Template.THIS_LANGUAGE[0], Template.THIS_LANGUAGE[4], Template.THIS_LANGUAGE[5],dataBaseConfig.asyncThreadNum,dataBaseConfig.name);

        } catch (Exception e) {
            throw new RuntimeException(Template.THIS_LANGUAGE[0]+"[" + dataBaseConfig.name + "]，"+Template.THIS_LANGUAGE[6], e);
        }
    }

    public void pueColumnInfoMap(String tableName, String name, String type) {
        Column column = new Column();
        try {
            column.clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        column.name = name;
        columnInfoMap.put(name, column);
        columnInfoMap.put(Tools.lowStr_hump(name, false).toLowerCase(), column);
        columnInfoMap.put((tableName + "." + name).toLowerCase(), column);
        columnInfoMap.put((Tools.lowStr_hump(tableName, false) + "." + Tools.lowStr_hump(name, false)).toLowerCase(), column);


    }

    private void readTableInfo() throws Exception {
        List<TableInfo> list = getTableInfo();
        for (TableInfo tableInfo : list) {
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                pueColumnInfoMap(tableInfo.name, tableInfo.columnNames.get(i), tableInfo.columnTypesDb.get(i));
                columnInfoMapSy.put(tableInfo.name.toLowerCase(), tableInfo.name);
                columnInfoMapSy.put(tableInfo.nameHumpLowerCase.toLowerCase(), tableInfo.name);
            }
        }
        for (int i = 0; i < montageArr.length; i++) {
            columnInfoMapSy.put("montage." + (i + 1), montageArr[i]);
        }
        for (int i = 0; i < symbolArr.length; i++) {
            columnInfoMapSy.put("symbol." + (i + 1), symbolArr[i]);
        }
    }


    /**
     * 创建并配置 HikariCP 数据库连接池
     *
     * @return 配置好的 HikariDataSource 实例
     */
    private HikariDataSource getHikariDataSource() {
        HikariConfig config = new HikariConfig();

        // 设置连接池名称（便于监控和日志识别）
        config.setPoolName("hikari-" + dataBaseConfig.sign + "-pool");
        // 数据库连接基础配置
        config.setJdbcUrl(dataBaseConfig.getUrl());        // 数据库连接URL
        config.setUsername(dataBaseConfig.acc);            // 数据库用户名
        config.setPassword(dataBaseConfig.pwd);            // 数据库密码
        config.setDriverClassName(dataBaseConfig.clz);     // 数据库驱动类名

        config.setMinimumIdle(dataBaseConfig.threadMax > 10 ? dataBaseConfig.threadMax / 3 : dataBaseConfig.threadMax);
        // 最大连接数：控制池的最大连接数量，避免资源耗尽
        config.setMaximumPoolSize(dataBaseConfig.threadMax);
        // 自动提交模式：控制是否自动提交事务
        config.setAutoCommit(true);

        // 连接保活与健康检查配置
        // 连接超时时间：获取连接的最大等待时间（毫秒）
        config.setConnectionTimeout(dataBaseConfig.overtime);
        // 空闲连接超时：超过此时间的空闲连接将被回收（5分钟）
        config.setIdleTimeout(300000);
        // 连接最大生命周期：强制回收超过此时间的连接（30分钟）
        config.setMaxLifetime(1800000);
        // 连接保活检查间隔：每隔1分钟检查一次空闲连接的有效性
        config.setKeepaliveTime(60000);
        // 连接验证SQL：用于验证连接是否有效的简单查询
        config.setConnectionTestQuery("SELECT 1");

        // MySQL 性能优化参数（预编译语句缓存）
        // 开启预编译语句缓存，避免重复解析SQL
        config.addDataSourceProperty("cachePrepStmts", "true");
        // 预编译语句缓存大小（最多缓存250条不同SQL）
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        // 允许缓存的SQL最大长度（复杂查询可能需要更大值）
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "4096");
        // 使用服务器端预编译（而非客户端），优化查询执行计划
        config.addDataSourceProperty("useServerPrepStmts", "true");

        // 连接状态与批处理优化
        // 使用本地会话状态，减少与服务器的状态查询开销
        config.addDataSourceProperty("useLocalSessionState", "true");
        // 优化批量SQL语句（合并多个INSERT/UPDATE为一个）
        config.addDataSourceProperty("rewriteBatchedStatements", "true");

        // 元数据与配置缓存
        // 缓存结果集元数据（避免重复获取表结构信息）
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        // 缓存服务器配置信息，减少与服务器的配置交互
        config.addDataSourceProperty("cacheServerConfiguration", "true");

        // 性能优化与统计
        // 减少不必要的setAutoCommit()调用，降低开销
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        // 关闭SQL执行时间统计（生产环境通常不需要详细统计）
        config.addDataSourceProperty("maintainTimeStats", "false");

        // 目录与空值处理
        // 当SQL中catalog为null时，使用当前数据库
        config.addDataSourceProperty("nullCatalogMeansCurrent", "true");

        // 连接容错与重试机制
        // 当所有服务器都不可用时，重试连接的次数
        config.addDataSourceProperty("retriesAllDown", "3");
        // 两次重试之间的间隔时间（秒）
        config.addDataSourceProperty("secondsBeforeRetryAllDown", "5");

        return new HikariDataSource(config);
    }

    @Override
    public SqlTemplate toSelect(String tableName, String[] fields, String[] symbol, String[] value, String[] montage, String sortField, String sortType) throws ParseException {
        StringBuilder sb = new StringBuilder(50);
        StringBuilder sb2 = new StringBuilder(100);
        List<Object> list = new ArrayList<>();
        String tableName1 = columnInfoMapSy.get(tableName);
        if (tableName1 == null) {
            log.d("tableName1 == null");
            return null;
        }
        if (fields != null && symbol != null && value != null && montage != null &&
                fields.length == symbol.length && symbol.length == value.length && montage.length >= value.length - 1) {
            String montage1 = null;
            for (int i = 0; i < fields.length; i++) {
                Column column = columnInfoMap.get(fields[i].toLowerCase());
                if (column == null) {
                    continue;
                }
                String field1 = column.name;
                String symbol1 = columnInfoMapSy.get("symbol." + symbol[i]);
                if (field1 == null || symbol1 == null || value[i] == null) {
                    int size = sb2.length();
                    if (size > 10) {
                        int index;
                        for (String s : montageArr) {
                            index = sb2.lastIndexOf(s);
                            if (index > -1 && sb2.substring(index, size).equals(s + " ")) {
                                sb2.delete(index, size);
                            }
                        }
                    }
                    continue;
                }
                if (i == 0) {
                    sb2.append("where ");
                }
                if (symbol1.equals("%like%")) {
                    if (dataBaseConfig.type.equals("mysql")) {
                        sb2.append(field1).append(" like ? ");
                        list.add("%" + value[i] + "%");
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like ? ");
                        list.add("%" + value[i] + "%");
                    }
                } else if (symbol1.equals("%like")) {
                    if (dataBaseConfig.type.equals("mysql")) {
                        sb2.append(field1).append(" like ? ");
                        list.add("%" + value[i]);
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like ? ");  // 同样加 CAST
                        list.add("%" + value[i]);
                    }
                } else if (symbol1.equals("like%")) {
                    if (dataBaseConfig.type.equals("mysql")) {
                        sb2.append(field1).append(" like ? ");
                        list.add(value[i] + "%");
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like ? ");  // 同样加 CAST
                        list.add(value[i] + "%");
                    }
                } else {
                    sb2.append(field1).append(" ").append(symbol1).append(" ? ");
                    list.add(column.toValue(value[i]));
                }
                if (i < value.length - 1) {
                    if (i < montage.length) {
                        montage1 = columnInfoMapSy.get("montage." + montage[i]);
                        if (montage1 != null) {
                            sb2.append(montage1).append(" ");
                        } else {
                            sb2.append("and").append(" ");
                        }
                    } else {
                        sb2.append("and").append(" ");
                    }

                }
            }
        }
        sb.append("select ").append(tableName).append(".* from ").append(tableName1).append(" ").append(sb2);
        if (sortField != null && sortField.length() > 0) {
            sb.append("order by ").append(sortField).append(" ").append(sortType == null ? "asc" : sortType);
        }
        return new SqlTemplate(sb.toString(), list.toArray());

    }


    @Override
    public EntityClassInfo getEntityInfo(Object t) {
        EntityClassInfo entityClassInfo = mapEntityClassInfo.get(t);
        if (entityClassInfo != null) {
            return entityClassInfo;
        }
        entityClassInfo = new EntityClassInfo();
        EntityAttribute entityAttributeClass = t.getClass().getDeclaredAnnotation(EntityAttribute.class);
        if (entityAttributeClass != null) {
            entityClassInfo.name = entityAttributeClass.name();
            entityClassInfo.aClass = t.getClass();
            Class aClass = t.getClass();
            Field[] arr1 = aClass.getDeclaredFields();
            try {
                for (int i = 0; i < arr1.length; i++) {
                    EntityAttribute entityAttribute = arr1[i].getDeclaredAnnotation(EntityAttribute.class);
                    if (entityAttribute != null) {
                        entityClassInfo.columnNames.add(arr1[i].getName());
                        entityClassInfo.notes.add(entityAttribute.desc());
                        entityClassInfo.attributes.add(entityAttribute.name());
                        entityClassInfo.fields.add(arr1[i]);
                        entityClassInfo.sizes.add(entityAttribute.size());
                        arr1[i].setAccessible(true);
                        entityClassInfo.values.add(arr1[i].get(t));
                        if (entityAttribute.key()) {
                            entityClassInfo.keyIndex = entityClassInfo.sizes.size() - 1;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return entityClassInfo;
    }

    @Override
    public SqlTemplate toSelect(Object t, int page, int size, String sortField, String sortType) {
        EntityClassInfo entityClassInfo = getEntityInfo(t);
        StringBuilder sql = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder where = new StringBuilder();
        List<Object> list = new ArrayList<>();
        int start = 0;
        if (page > 1) {
            start = (page - 1) * size;
        }
        int end = start + size;
        for (int i = 0; i < entityClassInfo.attributes.size(); i++) {
            fields.append(entityClassInfo.attributes.get(i));
            if (i < entityClassInfo.attributes.size() - 1) {
                fields.append(",");
            } else {
                fields.append(" ");
            }
            if (entityClassInfo.values.get(i) != null) {// && tableInfo.value.get(i).toString().length() > 0
                if (where.length() == 0) {
                    where.append("where ");
                } else {
                    where.append("and ");
                }
                where.append(entityClassInfo.attributes.get(i));
                where.append(" = ? ");
                list.add(entityClassInfo.values.get(i));
            }
        }
        sql.append("select ");
        sql.append(fields);
        sql.append("from ");
        sql.append(entityClassInfo.name);
        sql.append(" ");
        sql.append(where);
        if (sortField != null && sortType != null) {
            sql.append("order by ");
            sql.append(sortField);
            sql.append(" ");
            sql.append(sortType);
        }

        if (size > 0) {
            sql.append(" LIMIT ? OFFSET ?");
            list.add(end);
            list.add(start);
        }
        return new SqlTemplate(sql.toString(), list.toArray());
    }

    @Override
    public SqlTemplate toSave(Object t) throws Exception {
        return toSave(t, true);
    }

    @Override
    public SqlTemplate toSave(Object t, boolean autoId) throws Exception {
        EntityClassInfo entityClassInfo = getEntityInfo(t);
        StringBuilder sql = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder value = new StringBuilder();
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < entityClassInfo.attributes.size(); i++) {
            fields.append(entityClassInfo.attributes.get(i));
            value.append("?");
            if (i == entityClassInfo.keyIndex && autoId) {
                Field field = t.getClass().getDeclaredField(entityClassInfo.columnNames.get(i));
                field.setAccessible(true);
                if (entityClassInfo.fields.get(i).getType().getName().equals("java.lang.Long")) {
                    list.add(getOnlyIdDistributed());
                } else if (entityClassInfo.fields.get(i).getType().getName().equals("java.lang.Integer")) {
                    list.add(getOnlyIdNumber(entityClassInfo.name, entityClassInfo.attributes.get(i), false));
                } else if (entityClassInfo.fields.get(i).getType().getName().equals("java.lang.String")) {
                    list.add(getOnlyIdDistributedString());
                }
                field.set(t, entityClassInfo.fields.get(i).getType().getDeclaredConstructor(String.class).newInstance(list.get(list.size() - 1).toString()));
                field.setAccessible(false);
            } else {
                list.add(entityClassInfo.values.get(i));
            }
            if (i < entityClassInfo.attributes.size() - 1) {
                fields.append(",");
                value.append(",");
            }
        }
        sql.append("INSERT INTO ");
        sql.append(entityClassInfo.name);
        sql.append(" (");
        sql.append(fields);
        sql.append(") ");
        sql.append("VALUES ");
        sql.append("(");
        sql.append(value);
        sql.append(");");
        return new SqlTemplate(sql.toString(), list.toArray());
    }

    @Override
    public SqlTemplate toUpdate(Object t) {
        EntityClassInfo entityClassInfo = getEntityInfo(t);
        StringBuilder sql = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder where = new StringBuilder();
        List<Object> list = new ArrayList<>();
        if (entityClassInfo.values.get(entityClassInfo.keyIndex) == null) {
            return null;
        }
        for (int i = 0; i < entityClassInfo.attributes.size(); i++) {
            if (i != entityClassInfo.keyIndex) {
                fields.append(entityClassInfo.attributes.get(i));
                fields.append(" = ?");
                if (i < entityClassInfo.attributes.size() - 1) {
                    fields.append(", ");
                }
                list.add(entityClassInfo.values.get(i));
            }
        }
        where.append(entityClassInfo.attributes.get(entityClassInfo.keyIndex));
        where.append(" = ?");
        list.add(entityClassInfo.values.get(entityClassInfo.keyIndex));
        sql.append("update ");
        sql.append(entityClassInfo.name);
        sql.append(" set ");
        sql.append(fields);
        sql.append(" where ");
        sql.append(where);
        sql.append(";");
        return new SqlTemplate(sql.toString(), list.toArray());
    }

    @Override
    public SqlTemplate toDelete(Object t) {
        EntityClassInfo entityClassInfo = getEntityInfo(t);
        StringBuilder sql = new StringBuilder();
        StringBuilder where = new StringBuilder();
        List<Object> list = new ArrayList<>();
        if (entityClassInfo.values.get(entityClassInfo.keyIndex) == null) {
            return null;
        }
        where.append(entityClassInfo.attributes.get(entityClassInfo.keyIndex));
        where.append(" = ?");
        list.add(entityClassInfo.values.get(entityClassInfo.keyIndex));
        sql.append("delete from ");
        sql.append(entityClassInfo.name);
        sql.append(" where ");
        sql.append(where);
        sql.append(";");
        return new SqlTemplate(sql.toString(), list.toArray());
    }

    @Override
    public Object[] getDefObjectArr() {
        return defNullArray;
    }

    @Override
    public List<TableInfo> getTableInfo() throws Exception {
        List<TableInfo> list = new ArrayList<TableInfo>();
        TableInfo tableInfo;
        Connection conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = getConnection();
        DatabaseMetaData meta = conn.getMetaData();
        try {
            rs = meta.getTables(null, null, null, Tools.toArrayString("TABLE", "VIEW"));
            while (rs.next()) {
                tableInfo = new TableInfo();
                tableInfo.name = rs.getString("TABLE_NAME").toLowerCase();
                tableInfo.dbDesc = rs.getString("REMARKS");
                List<GzbMap> list1 = null;
                if (dataBaseConfig.type.equals("mysql")) {
                    list1 = selectGzbMap("SELECT " +
                            "COLUMN_NAME AS c_name," +
                            "COLUMN_COMMENT AS c_desc" +
                            " FROM " +
                            " information_schema.COLUMNS" +
                            " WHERE" +
                            " TABLE_SCHEMA = '" + dataBaseConfig.name + "'" +
                            " AND TABLE_NAME = '" + tableInfo.name + "'");
                } else if (dataBaseConfig.type.equals("postgresql")) {
                    list1 = selectGzbMap(
                            "SELECT " +
                                    "a.attname AS c_name, " +
                                    "d.description AS c_desc " +
                                    "FROM " +
                                    "pg_catalog.pg_class c " +
                                    "JOIN pg_catalog.pg_attribute a ON a.attrelid = c.oid " +
                                    "LEFT JOIN pg_catalog.pg_description d ON d.objoid = c.oid AND d.objsubid = a.attnum " +
                                    "WHERE " +
                                    "c.relkind = 'r' " +
                                    "AND c.relname = '" + tableInfo.name + "' " +
                                    "AND a.attnum > 0 " +
                                    "ORDER BY " +
                                    "a.attnum");
                } else {
                    log.w(Template.THIS_LANGUAGE[47], dataBaseConfig.type);
                }
                conn = getConnection();
                ps = conn.prepareStatement("select * from " + tableInfo.name + " order by 1 limit 1");
                Map<String, String> map = new HashMap<>();
                for (GzbMap gzbMap : list1) {
                    String c_name = gzbMap.getString("cName");
                    String c_desc = gzbMap.getString("cDesc");
                    if (c_name == null) {
                        continue;
                    }
                    if (c_desc == null) {
                        c_desc = c_name;
                    }
                    map.put(c_name, c_desc);
                }
                ResultSetMetaData col = ps.getMetaData();
                ResultSet rst = meta.getPrimaryKeys(null, null, tableInfo.name);
                if (!rst.next()) {
                    continue;
                }
                tableInfo.id = rst.getString("COLUMN_NAME");
                tableInfo.columnNames = new ArrayList<String>();
                tableInfo.columnTypes = new ArrayList<String>();
                tableInfo.columnTypesDb = new ArrayList<String>();
                tableInfo.columnSize = new ArrayList<Integer>();
                tableInfo.columnDesc = new ArrayList<String>();

                for (int i = 1; i <= col.getColumnCount(); i++) {
                    String columnClassName = col.getColumnClassName(i);
                    String columnName = col.getColumnName(i);
                    String columnDesc = col.getColumnLabel(i);
                    int columnSize = col.getColumnDisplaySize(i);

                    tableInfo.columnTypesDb.add(columnClassName);
                    if ("[B".equals(columnClassName)) {
                        columnClassName = "java.lang.Byte[]";
                    }
                    tableInfo.columnDesc.add(map.get(columnName));
                    tableInfo.columnNames.add(columnName);
                    tableInfo.columnTypes.add(columnClassName);
                    tableInfo.columnSize.add(columnSize);
                    if (tableInfo.id.equals(columnName)) {
                        tableInfo.idType = columnClassName;
                    }
                }
                tableInfo.setColumnNames(tableInfo.columnNames, dataBaseConfig.name);
                list.add(tableInfo);
            }
        } finally {
            close(rs, ps);
        }
        return list;
    }

    @Override
    public String getOnlyIdDistributedString() {
        return OnlyId.getDistributedString();
    }

    @Override
    public Long getOnlyIdDistributed() {
        return OnlyId.getDistributed();
    }


    @Override
    public Connection getConnection() throws SQLException {
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();

        if (entity.connection != null) {
            log.t(Template.THIS_LANGUAGE[46], entity.connection.getAutoCommit(), entity.connection);
            return entity.connection;
        }
        entity.connection = hds.getConnection();
        try {
            Integer is = readTransactionState();
            if (is == null) {
                entity.connection.setAutoCommit(true);
            } else if (is == 1) {
                entity.connection.setAutoCommit(false);
            } else if (is == 2) {
                entity.connection.setAutoCommit(true);
            } else {
                log.w(Template.THIS_LANGUAGE[45], is);
            }
        } catch (SQLException e) {
            log.e(e);
        }
        log.t( Template.THIS_LANGUAGE[44], entity.connection.getAutoCommit(), entity.connection);
        return entity.connection;
    }

    @Override
    public void setConnection(Connection connection0) {
        try {
            log.t(Template.THIS_LANGUAGE[43], connection0.getAutoCommit(), connection0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        entity.connection=connection0;
    }

    @Override
    public Integer readTransactionState() {
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        if (entity.transactionEntity == null) {
            return null;
        }
        return entity.transactionEntity.simulate;
    }

    //开启事务 主要是框架内部调用 当然 也可以手动
    @Override
    public void openTransaction(boolean simulation) {
        if (readTransactionState() != null) {
            throw new GzbException0( Template.THIS_LANGUAGE[42] );
        }
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        entity.transactionEntity=new TransactionEntity(simulation ? 2 : 1);
        log.t(Template.THIS_LANGUAGE[39], simulation ? Template.THIS_LANGUAGE[40] :Template.THIS_LANGUAGE[41]);
    }

    //关闭事务 主要是框架内部调用 当然 也可以手动
    @Override
    public void endTransaction() {
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        entity.transactionEntity=null;
        close(null, null);
        log.t(Template.THIS_LANGUAGE[38], entity.connection);
    }

    //提交并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    @Override
    public void commit() throws Exception {
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        if (entity.transactionEntity == null) {
            log.w(Template.THIS_LANGUAGE[35]);
        } else if (entity.transactionEntity.simulate == 1) {
            log.t(Template.THIS_LANGUAGE[36], entity.connection);
            if (entity.connection != null) {
                entity.connection.commit();
            }
        } else if (entity.transactionEntity.simulate == 2) {
            Map<String, List<Object[]>> map1 = entity.transactionEntity.data;
            log.t(Template.THIS_LANGUAGE[37], map1);
            entity.transactionEntity.simulate = 1;
            try {
                if (map1 != null) {
                    for (Map.Entry<String, List<Object[]>> stringListEntry : map1.entrySet()) {
                        if (stringListEntry.getKey() != null && stringListEntry.getValue() != null) {
                            runSqlBatch(stringListEntry.getKey(), stringListEntry.getValue());
                        }
                    }
                }
                commit();
            } catch (Exception e) {
                rollback();
                throw e;
            }
        }

    }

    //回滚并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    @Override
    public void rollback() throws SQLException {
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        if (entity.transactionEntity == null) {
            log.w(Template.THIS_LANGUAGE[32]);
        } else if (entity.transactionEntity.simulate == 1) {
            log.t(Template.THIS_LANGUAGE[33], entity.connection);
            if (entity.connection != null) {
                entity.connection.rollback();
            }
        } else if (entity.transactionEntity.simulate == 2) {
            log.t(Template.THIS_LANGUAGE[34]);
            entity.transactionEntity.data = null;
        }
    }

    /**
     * @param runnable
     * @throws SQLException
     */
    @Override
    public void transaction(Runnable runnable) throws SQLException {
        transaction(runnable, false);
    }

    /**
     * @param runnable
     * @param simulation
     * @throws SQLException
     */
    @Override
    public void transaction(Runnable runnable, boolean simulation) throws SQLException {
        openTransaction(simulation);
        try {
            runnable.run();
            commit();
        } catch (Exception e) {
            rollback();
        } finally {
            endTransaction();
        }
    }

    /**
     * 事务开启情况 不会close 数据库连接  正常情况会close
     */
    //关闭链接 非自动提交状态例外
    @Override
    public void close(ResultSet resultSet, PreparedStatement preparedStatement) {
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
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        if (entity.connection != null) {
            try {
                if (entity.transactionEntity == null) {
                    log.t(Template.THIS_LANGUAGE[16], entity.connection);
                    entity.connection.close();
                    entity.connection=null;
                } else if (entity.transactionEntity.simulate == 1) {
                    log.t(Template.THIS_LANGUAGE[17], entity.connection);
                } else if (entity.transactionEntity.simulate == 2) {
                    log.t(Template.THIS_LANGUAGE[18], entity.connection);
                    entity.connection.close();
                    entity.connection=null;
                }
            } catch (SQLException e) {
                log.e(e);
            }
        } else {
            log.t(Template.THIS_LANGUAGE[9]);
        }
    }

    @Override
    public int count(String sql, Object[] params) throws Exception {
        String[] arr1 = sql.split(" from ");
        String countSql = "select COUNT(*) as select_count from " + arr1[1];
        List<GzbMap> list = selectGzbMap(countSql, params, false);
        if (list != null && list.size() == 1) {
            return list.get(0).getInteger("select_count", 0);
        }
        return 0;
    }


    @Override
    public int getMaxId(String tableName, String idName) {
        Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement("select " + idName + " from " + tableName + " order by " + idName + " desc limit 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String data = resultSet.getString(idName);
                return Integer.parseInt(data);
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }
        return 0;
    }

    Map<String, AtomicInteger> mapCache = new ConcurrentHashMap<>();

    @Override
    public int getOnlyIdNumber(String tableName, String idName, boolean reset) {
        String key = dataBaseConfig.name + "_" + tableName + "_" + idName;
        if (reset) {
            //log.d("重置缓存 ID", key);
            mapCache.remove(key);
        }
        AtomicInteger atomicInteger = mapCache.get(key);
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(getMaxId(tableName, idName));
            mapCache.put(key, atomicInteger);
        }
        //log.d("读取缓存 ID", atomicInteger.get() + 1);
        return atomicInteger.incrementAndGet();
    }


    @Override
    public List<GzbMap> selectGzbMap(String sql) throws Exception {
        return selectGzbMap(sql, defNullArray);
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Connection connection) throws Exception {
        return selectGzbMap(sql, null, connection);
    }


    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects) throws Exception {
        return selectGzbMap(sql, objects, true);
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, Connection connection) throws Exception {
        return selectGzbMap(sql, objects, true, connection);
    }


    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump) throws Exception {
        Connection connection = getConnection();
        return selectGzbMap(sql, objects, hump, connection);
    }

    @Override
    public List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump, Connection connection) throws Exception {
        StringBuilder sb = new StringBuilder(sql);
        List<GzbMap> list = new ArrayList<>();
        GzbMap gzbMap = null;
        long[] times = new long[5];
        ResultSet rs = null;
        PreparedStatement ps = null;
        ResultSetMetaData rsMetaData = null;
        String key = null;
        String val = null;
        if (objects == null) {
            objects = Tools.toArray();
        }
        times[0] = System.currentTimeMillis();
        try {
            times[1] = System.currentTimeMillis();
            ps = connection.prepareStatement(sql);
            sb.append(" data:[");
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null) {
                    if (objects[i].toString().isEmpty()) {
                        objects[i] = null;
                    }
                }
                ps.setObject(i + 1, objects[i]);
                sb.append(objects[i]);
                if (i < objects.length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            rs = ps.executeQuery();
            times[2] = System.currentTimeMillis();
            while (rs.next()) {
                gzbMap = new GzbMap();
                rsMetaData = rs.getMetaData();
                int count = rsMetaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    //getColumnLabel  getColumnName
                    key = rsMetaData.getColumnLabel(i);
                    val = rs.getString(key);
                    if (val != null) {
                        gzbMap.put(hump ? Tools.lowStr_hump(key) : key, val);
                    }
                }
                list.add(gzbMap);
            }
            times[3] = System.currentTimeMillis();
        } finally {
            times[4] = System.currentTimeMillis();

            sb.append(",[").append(Template.THIS_LANGUAGE[20]).append(':').append(times[1] - times[0]).append(Template.THIS_LANGUAGE[13]).append("]");
            sb.append(",[").append(Template.THIS_LANGUAGE[21]).append(':').append(times[1] - times[0]).append(Template.THIS_LANGUAGE[13]).append("]");
            sb.append(",[").append(Template.THIS_LANGUAGE[22]).append(':').append(times[1] - times[0]).append(Template.THIS_LANGUAGE[13]).append("]");
            sb.append(",[").append(Template.THIS_LANGUAGE[23]).append(':').append(times[1] - times[0]).append(Template.THIS_LANGUAGE[13]).append("]");
            if (times[4] - times[0] > 200) {
                log.w(sb);
            } else {
                log.d(sb);
            }
            close(rs, ps);
        }
        return list;
    }


    @Override
    public int runSql(String sql) throws Exception {
        return runSql(sql, null);
    }


    @Override
    public int runSql(String sql, Object[] params) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        long start = 0, end = 0;
        if (params == null) {
            params = defNullArray;
        }
        if (sql == null) {
            return -1;
        }
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        String key = entity.open_transaction_key;
        if (key != null && !key.equals(dataBaseConfig.getSign())) {
            throw new RuntimeException(Template.THIS_LANGUAGE[0]+" " + key + " "+Template.THIS_LANGUAGE[31]+" " + dataBaseConfig.getSign() );
        }
        if (entity.transactionEntity != null && entity.transactionEntity.simulate == 2) {
            log.t(Template.THIS_LANGUAGE[30], sql, params);
            List<Object[]> list = entity.transactionEntity.data.get(sql);
            if (list == null) {
                list = new ArrayList<>();
                entity.transactionEntity.data.put(sql, list);
            }
            list.add(params);
            return 0;
        }
        Connection connection = getConnection();
        try {
            start = System.currentTimeMillis();
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int res = ps.executeUpdate();
            end = System.currentTimeMillis();
            return res;
        } finally {
            close(rs, ps);
            log.s(sql, start, end);
        }
    }


    public int runSqlBatch(String sql, List<Object[]> list_parameter) throws Exception {
        int allRow = 0;
        long a = 0, b = 0, c = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuilder sb = null;
        GzbThreadLocal.Entity entity=GzbThreadLocal.context.get();
        String key = entity.open_transaction_key;
        if (key != null && !key.equals(dataBaseConfig.getSign())) {
            throw new RuntimeException(Template.THIS_LANGUAGE[0]+" " + key + " "+Template.THIS_LANGUAGE[31]+" " + dataBaseConfig.getSign() );
        }
        if (entity.transactionEntity != null && entity.transactionEntity.simulate == 2) {
            List<Object[]> list = entity.transactionEntity.data.get(sql);
            if (list == null) {
                list = new ArrayList<>();
                entity.transactionEntity.data.put(sql, list);
            }
            log.t(Template.THIS_LANGUAGE[30], sql, list_parameter);
            for (Object[] objects : list_parameter) {
                list.add(objects);
            }
            return 0;
        }
        Connection connection = getConnection();
        try {
            if (sql.lastIndexOf(";") > -1) {
                sql = sql.split(";")[0];
            }
            ps = connection.prepareStatement(sql);
            for (int j = 0; j < list_parameter.size(); j++) {
                if (j == 0) {
                    a = System.currentTimeMillis();
                }
                sb = new StringBuilder(sql);
                Object[] parameter = list_parameter.get(j);
                if (parameter == null) {
                    continue;
                }
                sb.append(" date:{");
                for (int i = 0; i < parameter.length; i++) {
                    if (parameter[i] != null) {
                        if (parameter[i].toString().isEmpty()) {
                            parameter[i] = null;
                        }
                    }
                    ps.setObject(i + 1, parameter[i]);
                    sb.append(parameter[i]);
                    if (i != parameter.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("}");
                ps.addBatch();
                b = System.currentTimeMillis();
                if ((j + 1) % dataBaseConfig.asyncBatchSize == 0 || j + 1 == list_parameter.size()) {
                    int[] rows = ps.executeBatch();
                    for (int i = 0; i < rows.length; i++) {
                        if (rows[i] == -3) {
                            log.e(Template.THIS_LANGUAGE[29], Arrays.toString(list_parameter.get(j - (20 - i))));
                        }
                    }
                    if (readTransactionState() == null) {
                        connection.commit();
                    }

                    c = System.currentTimeMillis();
                    allRow += rows.length;
                    sb.append(",[").append(Template.THIS_LANGUAGE[24]).append(':').append(j + 1).append("/").append(list_parameter.size()).append("]");
                    sb.append(",[").append(Template.THIS_LANGUAGE[21]).append(':').append(b - a).append(Template.THIS_LANGUAGE[13]).append("]");
                    sb.append(",[").append(Template.THIS_LANGUAGE[22]).append(':').append(c - b).append(Template.THIS_LANGUAGE[13]).append("]");
                    sb.append(",[").append(Template.THIS_LANGUAGE[23]).append(':').append(c - a).append(Template.THIS_LANGUAGE[13]).append("]");
                    sb.append(",[").append(Template.THIS_LANGUAGE[26]).append(':').append((j + 1) % 200 == 0 ? 200 : (j + 1) % 200).append(Template.THIS_LANGUAGE[11]).append("]");
                    sb.append(",[").append(Template.THIS_LANGUAGE[25]).append(':').append(rows.length).append("]");
                } else {
                    sb.append(",[").append(Template.THIS_LANGUAGE[24]).append(':').append(j + 1).append("/").append(list_parameter.size()).append("]");
                }
            }
        } catch (Exception e) {
            if (!connection.getAutoCommit()) {
                connection.rollback();
            }
            throw new RuntimeException(Template.THIS_LANGUAGE[28]+"：" + sb, e);
        } finally {
            close(rs, ps);
        }
        log.d(sb);
        return allRow;
    }

    @Override
    public int runSqlAsync(String sql, Object[] para) {
        if (readTransactionState() != null) {
            throw new RuntimeException(Template.THIS_LANGUAGE[27]);
        }
        return asyncFactory.add(sql, para);
    }

    @Override
    public int runSqlAsync(String sql, Object[] para, Runnable fail, Runnable success) {
        if (readTransactionState() != null) {
            throw new RuntimeException(Template.THIS_LANGUAGE[27]);
        }
        return runSqlAsync(new AsyncFactory.Result(sql, para, fail, success));
    }

    @Override
    public int runSqlAsync(AsyncFactory.Result result) {
        if (readTransactionState() != null) {
            throw new RuntimeException(Template.THIS_LANGUAGE[27]);
        }
        return asyncFactory.add(result);
    }

}