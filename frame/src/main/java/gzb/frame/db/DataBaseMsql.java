package gzb.frame.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gzb.frame.annotation.EntityAttribute;
import gzb.entity.AsyEntity;
import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.frame.factory.ClassFactory;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.log.LogThread;
import gzb.tools.thread.ThreadPool;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class DataBaseMsql implements DataBase {

    public static Log log = new LogImpl();
    public static String[] montageArr = new String[]{"and", "or", "and(", "or(", ")and", ")or", "(", ")"};
    public static String[] symbolArr = new String[]{"=", ">", ">=", "<", "<=", "<>", "%like%", "like%", "%like"};
    public String name = null;
    public String ip = null;
    public String port = null;
    public String acc = null;
    public String pwd = null;
    public String url = null;
    public String clz = null;
    public Boolean auto = null;
    public Integer threadMax = null;
    public Integer overtime = null;
    private Integer asyncSleep = null;
    public Object[] defObjectArr = new Object[0];

    public HikariDataSource hds = null;
    private Map<String, AsyEntity> mapAskSql = new HashMap<>();
    private Map<String, String> humpMap = new HashMap<>();
    public Map<String, String> tableInfoMap = new ConcurrentHashMap<>();

    public DataBaseMsql(String key) throws Exception {
        readConfig(key);
        initDataBase(name, ip, port, acc, pwd, clz, auto, threadMax, overtime, asyncSleep);
    }

    public DataBaseMsql(String name, String ip, String port, String acc, String pwd, String clz, Boolean auto, Integer threadMax, Integer overtime, Integer asyncSleep) throws Exception {
        Config.set("db.mysql." + name + ".name", name);
        Config.set("db.mysql." + name + ".ip", ip);
        Config.set("db.mysql." + name + ".port", port);
        Config.set("db.mysql." + name + ".acc", acc);
        Config.set("db.mysql." + name + ".pwd", pwd);
        Config.set("db.mysql." + name + ".clz", clz);
        Config.set("db.mysql." + name + ".auto", auto.toString());
        Config.set("db.mysql." + name + ".threadMax", threadMax.toString());
        Config.set("db.mysql." + name + ".overtime", overtime.toString());
        Config.set("db.mysql." + name + ".asyncSleep", asyncSleep.toString());
        Config.set("db.mysql." + name + ".url", url);

        initDataBase(name, ip, port, acc, pwd, clz, auto, threadMax, overtime, asyncSleep);
    }

    private void initDataBase(String name, String ip, String port, String acc, String pwd, String clz, Boolean auto, Integer threadMax, Integer overtime, Integer asyncSleep) throws SQLException {
        try {
            if (name != null) {
                this.name = name;
                this.ip = ip;
                this.port = port;
                this.acc = acc;
                this.pwd = pwd;
                this.clz = clz;
                this.auto = auto;
                this.threadMax = threadMax;
                this.overtime = overtime;
                this.asyncSleep = asyncSleep;
                this.url = "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.name + "?" +
                        "autoReconnect=" + this.auto + "&" +
                        "useUnicode=true&" +
                        "characterEncoding=utf8&" +
                        "useSSL=false&" +
                        "zeroDateTimeBehavior=convertToNull&" +
                        "serverTimezone=UTC&" +
                        "rewriteBatchedStatements=true&" +
                        "allowMultiQueries=true";
                log.i("数据库配置信息:{", "name=" + this.name, "ip=" + this.ip, "port=" + this.port, "acc" + this.acc, "pwd=" + this.pwd, "clz=" + this.clz,
                        "auto=" + this.auto,
                        "threadMax=" + this.threadMax,
                        "overtime=" + this.overtime,
                        "asyncSleep" + this.asyncSleep,
                        "url=" + this.url,
                        "}");
                hds = getHikariDataSource();
                log.i("数据库：[" + this.name + "]，连接成功........");

                readTableInfo();
                log.i("数据库：[" + this.name + "]，数据表信息抓取成功........");

                startAsyncThread();
                log.i("数据库：[" + this.name + "]，异步服务启动成功........");

            } else {
                throw new SQLException("数据库名称不能为空，连接失败........");
            }

        } catch (Exception e) {
            log.e(e, "数据库：[" + this.name + "]，连接失败........");
            throw new SQLException("数据库连接失败");
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
        config.setPoolName("hikari-" + this.name + "-pool");

        // 数据库连接基础配置
        config.setJdbcUrl(this.url);        // 数据库连接URL
        config.setUsername(acc);            // 数据库用户名
        config.setPassword(pwd);            // 数据库密码
        config.setDriverClassName(clz);     // 数据库驱动类名

        // 连接池核心参数配置
        // 最小空闲连接数：根据最大连接数动态调整，至少保持一半的连接处于空闲状态
        config.setMinimumIdle(threadMax > 10 ? threadMax / 2 : threadMax);
        // 最大连接数：控制池的最大连接数量，避免资源耗尽
        config.setMaximumPoolSize(threadMax);
        // 自动提交模式：控制是否自动提交事务
        config.setAutoCommit(auto);

        // 连接保活与健康检查配置
        // 连接超时时间：获取连接的最大等待时间（毫秒）
        config.setConnectionTimeout(overtime);
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

    private void readConfig(String name) {
        this.name = Config.get("db.mysql." + name + ".name", null);
        this.ip = Config.get("db.mysql." + name + ".ip", null);
        this.port = Config.get("db.mysql." + name + ".port", null);
        this.acc = Config.get("db.mysql." + name + ".acc", null);
        this.pwd = Config.get("db.mysql." + name + ".pwd", null);
        this.clz = Config.get("db.mysql." + name + ".clz", Config.get("db.mysql." + name + ".class", null));
        this.auto = Config.getBoolean("db.mysql." + name + ".auto", false);
        this.threadMax = Config.getInteger("db.mysql." + name + ".threadMax", Tools.getCPUNum() * 2);
        this.overtime = Config.getInteger("db.mysql." + name + ".overtime", 3000);
        this.asyncSleep = Config.getInteger("db.mysql." + name + ".asyncSleep", 50);
        this.url = Config.get("db.mysql." + name + ".url", "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.name + "?" +
                "autoReconnect=" + this.auto + "&" +
                "useUnicode=true&" +
                "characterEncoding=utf8&" +
                "useSSL=false&" +
                "zeroDateTimeBehavior=convertToNull&" +
                "serverTimezone=UTC&" +
                "rewriteBatchedStatements=true");
    }

    private void startAsyncThread() {
        int num = 1;
        if (threadMax > 4) {
            num = (threadMax / 3);
        }
        log.i("后台线程", num);
        ThreadPool.pool.startThread(num, "startAsyncThread", () -> {
            AsyEntity asyEntity;
            Map<String, AsyEntity> map;
            Map.Entry<String, AsyEntity> en;
            while (true) {
                try {
                    map = getList();
                    if (map == null || map.size() == 0) {
                        Thread.sleep(asyncSleep);
                        continue;
                    }
                    for (Iterator<Map.Entry<String, AsyEntity>> it = map.entrySet().iterator(); it.hasNext(); ) {
                        en = it.next();
                        asyEntity = en.getValue();
                        if (asyEntity == null || asyEntity.list.size() < 1) {
                            continue;
                        }
                        asyEntity.lock.lock();
                        Connection connection = getConnection();
                        try {
                            connection.setAutoCommit(false);
                            runSqlBatch(en.getKey(), asyEntity.list, connection);
                        } catch (Exception e) {
                            log.e(e, "DataBaseMsql.startAsyncThread 执行异常:sql:" + en.getKey() + ",\n data:" + asyEntity.list.toString());
                        } finally {
                            connection.setAutoCommit(true);
                            connection.close();
                            asyEntity.lock.unlock();
                        }
                    }
                } catch (Exception e) {
                    log.e(e, "DataBaseMsql.startAsyncThread");
                }
            }
        });

    }

    private Map<String, AsyEntity> getList0() {
        String key = "DataBaseMsql-AsyncInfo";
        Condition condition = LockFactory.getCondition(key);
        Map<String, AsyEntity> map = null;
        while (true) {
            try {
                if (mapAskSql != null) {
                    map = mapAskSql;
                    mapAskSql = new HashMap<>();
                    break;
                } else {
                    condition.await();
                }
            } catch (Exception e) {
                log.e(e);
            }
        }
        return map;
    }

    private Map<String, AsyEntity> getList() {
        String key = "DataBaseMsql-AsyncInfo";
        Lock lock = LockFactory.getLock(key);
        Map<String, AsyEntity> map = null;
        lock.lock();
        try {
            if (mapAskSql != null) {
                map = mapAskSql;
                mapAskSql = new HashMap<>();
            }
        } finally {
            lock.unlock();
        }
        return map;
    }

    @Override
    public SqlTemplate toSelect(String tableName, String[] fields, String[] symbol, String[] value, String[] montage, String sortField, String sortType) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        List<Object> list = new ArrayList<>();
        String tableName1 = tableInfoMap.get(tableName);
        if (tableName1 == null) {
            System.out.println("tableName1 == null");
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
                    System.out.println("continue 1 ");
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

    Map<Object, EntityClassInfo> mapEntityClassInfo = new ConcurrentHashMap<>();

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
            sql.append(" ");
            sql.append("limit ?,?");
            list.add(start);
            list.add(end);
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
                if (rst.next() == false) {
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
                        columnClassName = "java.lang.Byte";
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
                tableInfo.setColumnNames(tableInfo.columnNames, this.name);
                list.add(tableInfo);
            }
        } finally {
            if (ps != null) ps.close();
            if (rs != null) rs.close();

            conn.close();
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
            close(conn, resultSet, preparedStatement);
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
    public Connection getConnection() {
        try {
            if (hds == null) {
                throw new SQLException("数据库连接池hds为NULL");
            }
            return hds.getConnection();
        } catch (Exception e) {
            log.e(e, "获取数据库连接出错");
            return null;
        }
    }

    @Override
    public void close(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.e(e);
        }
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
            this.close(connection, rs, ps);
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
    public int runSql(String sql) throws Exception {
        return runSql(sql, new Object[]{});
    }

    @Override
    public int runSql(String sql, Connection connection) throws Exception {
        return runSql(sql, null, connection);
    }


    @Override
    public int runSql(String sql, Object[] para) throws Exception {
        List<Object[]> list_parameter = new ArrayList<>();
        list_parameter.add(para);
        return runSqlBatch(sql, list_parameter);
    }

    @Override
    public int runSql(String sql, Object[] para, Connection connection) throws Exception {
        List<Object[]> list_parameter = new ArrayList<>();
        list_parameter.add(para);
        return runSqlBatch(sql, list_parameter, connection);
    }


    @Override
    public int runSqlBatch(String sql, List<Object[]> list_parameter) throws Exception {
        Connection connection = getConnection();
        return runSqlBatch(sql, list_parameter, connection, true);
    }

    @Override
    public int runSqlBatch(String sql, List<Object[]> list_parameter, Connection connection) throws Exception {
        return runSqlBatch(sql, list_parameter, connection, false);
    }

    private boolean isSaveSqlLog() {
        boolean res = true;
        for (int i = 0; i < LogThread.lvConfig.length; i++) {
            if (LogThread.lvConfig[i] == 2) {
                res = false;
                break;
            }
        }
        return res;
    }

    // autoCommit    true的话 自动提交sql     false的话 自己手动提交 connection.setAutoCommit(true false);
    public int runSqlBatch(String sql, List<Object[]> list_parameter, Connection connection, boolean autoCommit) throws Exception {
        //  只有在 debug环境下 执行 带日志输出的 批量执行   LogThread.lvConfig.get(0) != 2表示 debug级别 不显示 也不输出
        if (LogThread.lvConfig[0] == 2) {
            return runSqlBatch01(sql, list_parameter, connection, autoCommit);
        }
        int allRow = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        if (sql == null || list_parameter == null) {
            return -1;
        }
        if (autoCommit) {
            connection.setAutoCommit(false);
        }
        if (sql.endsWith(";")) {
            sql = sql.split(";")[0];
        }
        try {
            ps = connection.prepareStatement(sql);
            for (int j = 0; j < list_parameter.size(); j++) {
                Object[] parameter = list_parameter.get(j);
                if (parameter == null) {
                    continue;
                }
                for (int i = 0; i < parameter.length; i++) {
                    ps.setObject(i + 1, parameter[i]);
                }
                ps.addBatch();
                if ((j + 1) % 200 == 0 || j + 1 == list_parameter.size()) {
                    int[] rows = ps.executeBatch();
                    if (autoCommit) {
                        connection.commit();
                    }
                    allRow += rows.length;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            StringBuilder stringBuilder1 = new StringBuilder(sql);
            stringBuilder1.append(" -> data:[");
            for (int j = 0; j < list_parameter.size(); j++) {
                Object[] parameter = list_parameter.get(j);
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
                if (j + 1 < list_parameter.size()) {
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
                connection.setAutoCommit(true);
                this.close(connection, rs, ps);
            } else {
                this.close(null, rs, ps);
            }
        }
        return allRow;
    }

    // autoCommit    true的话 自动提交sql     false的话 自己手动提交 connection.setAutoCommit(true false);
    public int runSqlBatch01(String sql, List<Object[]> list_parameter, Connection connection, boolean autoCommit) throws Exception {
        int allRow = 0;
        long a = 0, b = 0, c = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        StringBuilder sb = null;
        try {
            if (autoCommit) {
                connection.setAutoCommit(false);
            }
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
                    ps.setObject(i + 1, parameter[i]);
                    sb.append(parameter[i]);
                    if (i != parameter.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("}");
                ps.addBatch();
                b = System.currentTimeMillis();
                if ((j + 1) % 200 == 0 || j + 1 == list_parameter.size()) {
                    int[] rows = ps.executeBatch();
                    if (autoCommit) {
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
            if (autoCommit) {
                connection.setAutoCommit(true);
                this.close(connection, rs, ps);
            } else {
                this.close(null, rs, ps);
            }
            log.d(sb);
        }
        return allRow;
    }

    @Override
    public int runSqlAsync(String sql, Object[] para) {
        List<Object[]> list_parameter = new ArrayList<>();
        list_parameter.add(para);
        return runSqlAsyncBatch(sql, list_parameter);
    }

    @Override
    public int runSqlAsyncBatch(String sql, List<Object[]> list_parameter) {
        String key = "DataBaseMsql-AsyncInfo";
        Lock lock = LockFactory.getLock(key);
        AsyEntity asyEntity;
        lock.lock();
        try {
            asyEntity = mapAskSql.get(sql);
            if (asyEntity == null) {
                asyEntity = new AsyEntity();
                mapAskSql.put(sql, asyEntity);
            }
        } finally {
            lock.unlock();
        }
        asyEntity.lock.lock();
        try {
            for (int i = 0; i < list_parameter.size(); i++) {
                asyEntity.list.add(list_parameter.get(i));
            }
        } finally {
            asyEntity.lock.unlock();
        }
        return 1;
    }

}
