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
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.log.Log;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class DataBaseImpl implements DataBase {

    //数据库连接
    private final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    //数据库事务是否开启
    private final ThreadLocal<Boolean> openTransactionThreadLocal = new ThreadLocal<>();

    public AsyncFactory asyncFactory = null;

    DataBaseConfig dataBaseConfig;
    public static Log log = Config.log;
    public static String[] montageArr = new String[]{"and", "or", "and(", "or(", ")and", ")or", "(", ")"};
    public static String[] symbolArr = new String[]{"=", ">", ">=", "<", "<=", "<>", "%like%", "like%", "%like"};

    public HikariDataSource hds = null;
    private Map<String, String> humpMap = new HashMap<>();
    public Map<String, String> tableInfoMap = new ConcurrentHashMap<>();

    Map<Object, EntityClassInfo> mapEntityClassInfo = new ConcurrentHashMap<>();


    public DataBaseImpl(DataBaseConfig dataBaseConfig) throws Exception {
        this.dataBaseConfig = dataBaseConfig;
        initDataBase();
    }

    public DataBaseImpl(String key) throws Exception {
        dataBaseConfig = DataBaseConfig.readConfig(key);
        initDataBase();
    }

    public DataBaseImpl(String type, String key, String clz, String ip, int port, String name, String acc, String pwd, int threadMax, int overtime, int asyncSleepMilli, int asyncBatchSize, int asyncThreadNum, String sign) throws Exception {
        dataBaseConfig = DataBaseConfig.readConfig(type, key, clz, ip, port, name, acc, pwd, threadMax, overtime, asyncSleepMilli, asyncBatchSize, asyncThreadNum, sign);
        initDataBase();
    }

    public String getSign() {
        return dataBaseConfig.getSign();
    }

    public AsyncFactory getAsyncFactory() {
        return asyncFactory;
    }

    private void initDataBase() {
        log.d("数据库配置信息", dataBaseConfig);
        try {
            hds = getHikariDataSource();
            log.d("数据库：[" + dataBaseConfig.name + "]，连接成功........");

            readTableInfo();
            log.d("数据库：[" + dataBaseConfig.name + "]，数据表信息抓取成功........");

            asyncFactory = new AsyncFactory(this, dataBaseConfig.asyncThreadNum, dataBaseConfig.asyncBatchSize, dataBaseConfig.asyncSleepMilli);
            log.d("数据库：[" + dataBaseConfig.name + "]，异步服务启动成功........", "后台异步线程数", dataBaseConfig.asyncThreadNum);

        } catch (Exception e) {
            throw new RuntimeException("数据库：[" + dataBaseConfig.name + "]，连接失败........", e);
        }
    }

    private void readTableInfo() throws Exception {
        List<TableInfo> list = getTableInfo();
        for (TableInfo tableInfo : list) {
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                //表名 对应 表名
                tableInfoMap.put((tableInfo.name).toLowerCase(), tableInfo.name);
                //表名驼峰 对应 表名
                tableInfoMap.put((tableInfo.nameHumpUpperCase).toLowerCase(), tableInfo.name);

                //列名 对应 列名
                tableInfoMap.put((tableInfo.columnNames.get(i)).toLowerCase(), tableInfo.columnNames.get(i));
                //列名驼峰 对应 列名
                tableInfoMap.put((tableInfo.columnNamesHumpLowerCase.get(i)).toLowerCase(), tableInfo.columnNames.get(i));

                //表名.列名 对应 列名
                tableInfoMap.put((tableInfo.name + "." + tableInfo.columnNames.get(i)).toLowerCase(), tableInfo.columnNames.get(i));
                //表名.列名驼峰 对应 列名
                tableInfoMap.put((tableInfo.name + "." + tableInfo.columnNamesHumpLowerCase.get(i)).toLowerCase(), tableInfo.columnNames.get(i));

                //表名驼峰.列名 对应 列名
                tableInfoMap.put((tableInfo.nameHumpLowerCase + "." + tableInfo.columnNames.get(i)).toLowerCase(), tableInfo.columnNames.get(i));
                //表名驼峰.列名驼峰 对应 列名
                tableInfoMap.put((tableInfo.nameHumpLowerCase + "." + tableInfo.columnNamesHumpLowerCase.get(i)).toLowerCase(), tableInfo.columnNames.get(i));

            }
        }
        for (int i = 0; i < montageArr.length; i++) {
            tableInfoMap.put("montage." + (i + 1), montageArr[i]);
        }
        for (int i = 0; i < symbolArr.length; i++) {
            tableInfoMap.put("symbol." + (i + 1), symbolArr[i]);
        }
    }

    private String getName(String name) {
        if (name == null) {
            return null;
        }
        String nameHump = humpMap.get(name);
        if (nameHump != null) {
            return nameHump;
        }
        String[] arr1 = name.split("_");
        String n = arr1[0];
        for (int i = 1; i < arr1.length; i++) {
            char[] chars = arr1[i].toCharArray();
            for (int i1 = 0; i1 < chars.length; i1++) {
                if (i1 == 0) {
                    n += String.valueOf(chars[i1]).toUpperCase();
                } else {
                    n += String.valueOf(chars[i1]).toLowerCase();
                }
            }
        }
        humpMap.put(name, n);
        return n;
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
        String para01 = "autoReconnect=true&" +/// 自动重连
                "useUnicode=true&" +/// 使用 Unicode
                "characterEncoding=" + Config.encoding + "&" +/// 设置编码
                "useSSL=false&" +/// 允许不使用ssl
                "zeroDateTimeBehavior=convertToNull&" +/// 防止日期转换错误
                "serverTimezone=UTC&" +/// 统一时区
                "rewriteBatchedStatements=true&" +/// 允许sql合并
                "allowMultiQueries=true&" +/// 允许1次执行多个sql
                "useServerPrepStmts=true&" +/// 服务端缓存sql
                "cachePrepStmts=true&";/// 客户端缓存sql
        // 数据库连接基础配置
        config.setJdbcUrl(dataBaseConfig.getUrl(para01));        // 数据库连接URL
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
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
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
    public SqlTemplate toSelect(String tableName, String[] fields, String[] symbol, String[] value, String[] montage, String sortField, String sortType) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        List<Object> list = new ArrayList<>();
        String tableName1 = tableInfoMap.get(tableName);
        if (tableName1 == null) {
            log.d("tableName1 == null");
            return null;
        }
        if (fields != null && symbol != null && value != null && montage != null && fields.length == symbol.length && symbol.length == value.length && montage.length >= value.length - 1) {
            String montage1 = null;
            for (int i = 0; i < fields.length; i++) {
                String field1 = tableInfoMap.get((fields[i]).toLowerCase());
                String symbol1 = tableInfoMap.get("symbol." + symbol[i]);
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
                    sb2.append(field1).append(" like ? ");
                    list.add("%" + value[i] + "%");
                } else if (symbol1.equals("%like")) {
                    sb2.append(field1).append(" like ? ");
                    list.add("%" + value[i]);
                } else if (symbol1.equals("like%")) {
                    sb2.append(field1).append(" like ? ");
                    list.add(value[i] + "%");
                } else {
                    sb2.append(field1).append(" ").append(symbol1).append(" ? ");
                    list.add(value[i]);
                    //暂时停用
                 /*   String val = tableInfoMap.get(value[i]);
                    if (val==null){
                        sb2.append(field1).append(" ").append(symbol1).append(" ? ");
                        list.add(value[i]);
                    }else{
                        sb2.append(field1).append(" ").append(symbol1).append(" ").append(val).append(" ");
                    }*/

                }
                if (i < value.length - 1) {
                    if (i < montage.length) {
                        montage1 = tableInfoMap.get("montage." + montage[i]);
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
        sb.append("select * from ").append(tableName1).append(" ").append(sb2);
        if (sortField != null && sortField.length() > 0) {
            sb.append("order by ").append(sortField).append(" ").append(sortType == null ? "asc" : sortType);
        }
        SqlTemplate sqlTemplate = new SqlTemplate(sb.toString(), list.toArray());
        return sqlTemplate;

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
                    list.add(getOnlyIdNumber(entityClassInfo.name, entityClassInfo.attributes.get(i)));
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
        return new Object[0];
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
                ps = conn.prepareStatement("select * from " + tableInfo.name + " order by 1 limit 1");
                ResultSetMetaData col = ps.getMetaData();
                ResultSet rst = meta.getPrimaryKeys(null, null, tableInfo.name);
                if (!rst.next()) {
                    continue;
                }
                tableInfo.id = rst.getString("COLUMN_NAME");
                tableInfo.columnNames = new ArrayList<String>();
                tableInfo.columnTypes = new ArrayList<String>();
                tableInfo.columnSize = new ArrayList<Integer>();
                tableInfo.columnDesc = new ArrayList<String>();
                for (int i = 1; i <= col.getColumnCount(); i++) {
                    String columnClassName = col.getColumnClassName(i);
                    String columnName = col.getColumnName(i);
                    String columnDesc = col.getColumnLabel(i);
                    int columnSize = col.getColumnDisplaySize(i);

                    if ("[B".equals(columnClassName)) {
                        columnClassName = "java.lang.Byte[]";
                    }
                    if ("java.sql.Timestamp".equals(columnClassName)) {
                        columnClassName = "java.lang.String";
                    }
                    if ("java.time.LocalDateTime".equals(columnClassName)) {
                        columnClassName = "java.lang.String";
                    }
                    if ("java.lang.Boolean".equals(columnClassName)) {
                        columnClassName = "java.lang.Integer";
                    }
                    if ("java.math.BigDecimal".equals(columnClassName)) {
                        columnClassName = "java.lang.Double";
                    }
                    if ("java.math.BigInteger".equals(columnClassName)) {
                        columnClassName = "java.lang.Long";
                    }
                    tableInfo.columnDesc.add(columnDesc);
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
        Connection connection1 = connectionThreadLocal.get();
        if (connection1 != null) {
            log.t("获取连接 缓存",connection1);
            return connection1;
        }
        connection1 = hds.getConnection();
        try {
            connection1.setAutoCommit(!isOpenTransaction());
        } catch (SQLException e) {
            log.e(e);
        }
        connectionThreadLocal.set(connection1);
        log.t("获取连接 新的",connection1);
        return connection1;
    }

    @Override
    public void setConnection(Connection connection) {
        setConnection(connection, false);
    }

    @Override
    public void setConnection(Connection connection0, boolean openTransaction0) {
        log.t("框架 指定连接", connection0, "是否开启事物", openTransaction0);
        openTransactionThreadLocal.set(openTransaction0);
        connectionThreadLocal.set(connection0);
        try {
            connection0.setAutoCommit(!openTransaction0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean isOpenTransaction() {
        return openTransactionThreadLocal.get() != null && openTransactionThreadLocal.get();
    }

    //开启事务 主要是框架内部调用 当然 也可以手动
    @Override
    public void openTransaction() {
        log.t("框架 开启事物");
        openTransactionThreadLocal.set(true);
    }

    //关闭事务 主要是框架内部调用 当然 也可以手动
    @Override
    public void endTransaction() {
        openTransactionThreadLocal.remove();
        log.t("框架 关闭事物");
        close(null, null);
    }

    //提交并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    @Override
    public void commit() throws SQLException {
        Connection connection1 = connectionThreadLocal.get();
        log.t("框架 提交事物", connection1);
        if (connection1 != null) {
            connection1.commit();
        }
    }

    //回滚并且关闭事务  同时也会归还连接 主要是框架内部调用 当然 也可以手动
    @Override
    public void rollback() throws SQLException {
        Connection connection1 = connectionThreadLocal.get();
        log.t("框架 回滚事物", connection1);
        if (connection1 != null) {
            connection1.rollback();
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
        Connection connection0 = connectionThreadLocal.get();
        if (connection0 != null) {
            try {

                if (!isOpenTransaction()) {
                    log.t("数据连接 由于未开启事物 数据库连接 归还", connection0);
                    connection0.setAutoCommit(true);
                    connection0.close();
                    connectionThreadLocal.remove();
                } else {
                    log.t("数据连接 由于已开启事务 数据库连接 不归还", connection0);
                }
            } catch (SQLException e) {
                log.e(e);
            }
        } else {
            log.t("数据连接 为空 跳过");
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
    public int getMaxId(String mapName, String idName) {
        Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement("select " + idName + " from " + mapName + " order by " + idName + " desc limit 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String data = resultSet.getString(idName);
                return Integer.valueOf(data);
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

    @Override
    public int getOnlyIdNumber(String mapName, String idName) {
        String key = "DataBaseMsql-getOnlyIdNumber-" + mapName + "-" + idName;
        Lock lock = LockFactory.getLock(key);
        int id;
        lock.lock();
        try {
            id = Cache.gzbCache.getIncr(key);
            if (id < 1) {
                id = getMaxId(mapName, idName);
                Cache.gzbCache.set(key, String.valueOf(id));
            }
        } finally {
            lock.unlock();
        }
        return Integer.valueOf(String.valueOf(id));
    }


    @Override
    public List<GzbMap> selectGzbMap(String sql) throws Exception {
        return selectGzbMap(sql, new Object[]{});
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
                        gzbMap.put(hump ? getName(key) : key, val);
                    }
                }
                list.add(gzbMap);
            }
            times[3] = System.currentTimeMillis();
        } finally {
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
            close(rs, ps);
        }
        return list;
    }


    @Override
    public int runSql(String sql) throws Exception {
        return runSql(sql, new Object[]{});
    }


    @Override
    public int runSql(String sql, Object[] params) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        long start = 0, end = 0;
        if (sql == null || params == null) {
            return -1;
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
        } catch (SQLException e) {
            StringBuilder stringBuilder1 = new StringBuilder(sql);
            stringBuilder1.append(" -> data:[");
            for (Object param : params) {
                stringBuilder1.append(param).append(",");
            }
            stringBuilder1.append("]");
            throw new RuntimeException("\n SQLException "+stringBuilder1.toString(),e);
        } catch (Exception e) {
            log.e(e, "Exception 非SQL异常");
            throw e;
        } finally {
            close(rs, ps);
            log.s(sql, start, end);
        }
    }


    // autoCommit    true的话 自动提交sql     false的话 自己手动提交 connection.setAutoCommit(true false);
    public int runSqlBatch(String sql, List<Object[]> list_parameter) throws Exception {
        int allRow = 0;
        long a = 0, b = 0, c = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuilder sb = null;
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
                            log.e("SQL执行失败：", Arrays.toString(list_parameter.get(j - (20 - i))));
                        }
                    }
                    if (!isOpenTransaction()) {
                        connection.commit();
                    }
                    c = System.currentTimeMillis();
                    allRow += rows.length;
                    sb.append(",[进度:").append(j + 1).append("/").append(list_parameter.size()).append("]");
                    sb.append(",[组装:").append(b - a).append("ms]");
                    sb.append(",[执行:").append(c - b).append("ms]");
                    sb.append(",[耗时:").append(c - a).append("ms]");
                    sb.append(",[条数:").append((j + 1) % 200 == 0 ? 200 : (j + 1) % 200).append("条]");
                    sb.append(",[影响:").append(rows.length).append("行]");
                } else {
                    sb.append(",[进度:").append(j + 1).append("/").append(list_parameter.size()).append("]");
                }
            }
        } catch (Exception e) {
            connection.rollback();
            log.e(e, "异常：" + sb);
            throw e;
        } finally {
            close(rs, ps);
        }
        log.d(sb);
        return allRow;
    }

    @Override
    public int runSqlAsync(String sql, Object[] para) {
        return asyncFactory.add(sql, para);
    }

    @Override
    public int runSqlAsyncBatch(String sql, List<Object[]> list_parameter) {
        if (isOpenTransaction()) {
            throw new GzbException0("开启事务的时候无法使用异步执行");
        }
        int num = 0;
        for (Object[] objects : list_parameter) {
            num += runSqlAsync(sql, objects);
        }
        return num;
    }

}
