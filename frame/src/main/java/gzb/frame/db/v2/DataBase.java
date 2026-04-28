package gzb.frame.db.v2;

import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.exception.GzbException0;
import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.GzbMap;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.impl.codec.DataType;
import io.vertx.sqlclient.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/// 测试数据库流水线 理论性能很好 但是存在很多问题 比如 头阻塞 他最大的价值在与能降低数据库cpu消耗
public class DataBase {
    public DataBaseConfig dataBaseConfig = null;
    public io.vertx.core.Vertx vertx = null;
    public Pool pool = null;
    public Pool pool_transactions = null;
    Log log = Log.log;
    public AsyncFactory asyncFactory = null;

    public Map<String, BaseDaoAsync.Column> columnInfoMap = new ConcurrentHashMap<>();
    public Map<String, String> columnInfoMapSy = new ConcurrentHashMap<>();
    public static String[] montageArr = new String[]{"and", "or", "and(", "or(", ")and", ")or", "(", ")"};
    public static String[] symbolArr = new String[]{"=", ">", ">=", "<", "<=", "<>", "%like%", "like%", "%like"};

    public DataBase(DataBaseConfig dataBaseConfig) {
        init(dataBaseConfig);
    }

    public void init(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
        vertx = io.vertx.core.Vertx.vertx();
        if (dataBaseConfig.sql_type == 1) {
            pool = PgBuilder.pool()
                    .with(new PoolOptions()
                            .setEventLoopSize(Config.cpu)
                            .setMaxWaitQueueSize(Config.cpu * 100)
                            .setMaxSize(dataBaseConfig.pool_size))
                    .connectingTo(new PgConnectOptions()
                            .setPort(dataBaseConfig.port)
                            .setHost(dataBaseConfig.ip)
                            .setDatabase(dataBaseConfig.name)
                            .setUser(dataBaseConfig.acc)
                            .setPassword(dataBaseConfig.pwd)
                            .setCachePreparedStatements(true)
                            .setPreparedStatementCacheMaxSize(dataBaseConfig.cache_sql_num)
                            .setPipeliningLimit(dataBaseConfig.pipeline_size)
                    )
                    .using(vertx)
                    .build();
            pool_transactions = PgBuilder.pool()
                    .with(new PoolOptions()
                            .setEventLoopSize(Config.cpu)
                            .setMaxWaitQueueSize(Config.cpu * 100)
                            .setMaxSize(dataBaseConfig.pool_size_transactions))
                    .connectingTo(new PgConnectOptions()
                            .setPort(dataBaseConfig.port)
                            .setHost(dataBaseConfig.ip)
                            .setDatabase(dataBaseConfig.name)
                            .setUser(dataBaseConfig.acc)
                            .setPassword(dataBaseConfig.pwd)
                            .setCachePreparedStatements(true)
                            .setPreparedStatementCacheMaxSize(dataBaseConfig.cache_sql_num)
                            .setPipeliningLimit(dataBaseConfig.pipeline_size_transactions)
                    )
                    .using(vertx)
                    .build();
        } else {
            pool = MySQLBuilder.pool()
                    .with(new PoolOptions()
                            .setEventLoopSize(Config.cpu)
                            .setMaxWaitQueueSize(Config.cpu * 100)
                            .setMaxSize(dataBaseConfig.pool_size))
                    .connectingTo(new PgConnectOptions()
                            .setPort(dataBaseConfig.port)
                            .setHost(dataBaseConfig.ip)
                            .setDatabase(dataBaseConfig.name)
                            .setUser(dataBaseConfig.acc)
                            .setPassword(dataBaseConfig.pwd)
                            .setCachePreparedStatements(true)
                            .setPreparedStatementCacheMaxSize(dataBaseConfig.cache_sql_num)
                            .setPipeliningLimit(dataBaseConfig.pipeline_size)
                    )
                    .using(vertx)
                    .build();
            pool_transactions = MySQLBuilder.pool()
                    .with(new PoolOptions()
                            .setEventLoopSize(Config.cpu)
                            .setMaxWaitQueueSize(Config.cpu * 100)
                            .setMaxSize(dataBaseConfig.pool_size_transactions))
                    .connectingTo(new PgConnectOptions()
                            .setPort(dataBaseConfig.port)
                            .setHost(dataBaseConfig.ip)
                            .setDatabase(dataBaseConfig.name)
                            .setUser(dataBaseConfig.acc)
                            .setPassword(dataBaseConfig.pwd)
                            .setCachePreparedStatements(true)
                            .setPreparedStatementCacheMaxSize(dataBaseConfig.cache_sql_num)
                            .setPipeliningLimit(dataBaseConfig.pipeline_size_transactions)
                    )
                    .using(vertx)
                    .build();
        }
        readTableInfo();
        asyncFactory = new AsyncFactory(this, dataBaseConfig.async_thread_num, dataBaseConfig.async_await_ms, dataBaseConfig.async_queue_size);
    }

    public void init(String host, int port, String acc, String pwd, String name, int poolMax, int sqlCacheSize, int pipeliningSize) {
        dataBaseConfig = new DataBaseConfig();
        dataBaseConfig.ip = host;
        dataBaseConfig.port = port;
        dataBaseConfig.pool_size = poolMax;
        dataBaseConfig.acc = acc;
        dataBaseConfig.pwd = pwd;
        dataBaseConfig.name = name;
        dataBaseConfig.pool_size_transactions = poolMax;
        dataBaseConfig.pool_size = poolMax;
        dataBaseConfig.cache_sql_num = sqlCacheSize;
        dataBaseConfig.pipeline_size = pipeliningSize;
        init(dataBaseConfig);

    }

    public void close() {
        pool.close()
                .compose(v -> vertx.close())
                .toCompletionStage()
                .toCompletableFuture()
                .join();
        pool_transactions.close()
                .compose(v -> vertx.close())
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }


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
                BaseDaoAsync.Column column = columnInfoMap.get(fields[i].toLowerCase());
                if (column == null) {
                    log.d("column == null");
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
                    log.d("field1 == null || symbol1 == null || value[i] == null");
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
                        list.add("%" + value[i] + "%");
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like $").append(list.size()).append(" ");
                    }
                } else if (symbol1.equals("%like")) {
                    if (dataBaseConfig.type.equals("mysql")) {
                        sb2.append(field1).append(" like ? ");
                        list.add("%" + value[i]);
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        list.add("%" + value[i]);
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like $").append(list.size()).append(" ");
                    }
                } else if (symbol1.equals("like%")) {
                    if (dataBaseConfig.type.equals("mysql")) {
                        list.add(value[i] + "%");
                        sb2.append(field1).append(" like ? ");
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        list.add(value[i] + "%");
                        sb2.append("CAST(").append(field1).append(" AS TEXT) like $").append(list.size()).append(" ");
                    }
                } else {
                    if (dataBaseConfig.type.equals("mysql")) {
                        list.add(column.toValue(value[i]));
                        sb2.append(field1).append(" like ? ");
                    } else if (dataBaseConfig.type.equals("postgresql")) {
                        list.add(column.toValue(value[i]));
                        sb2.append(field1).append(" ").append(symbol1).append(" $").append(list.size()).append(" ");
                    }
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

    public void pueColumnInfoMap(String tableName, String name, String type) {
        BaseDaoAsync.Column column = new BaseDaoAsync.Column();
        try {
            column.clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            log.e(tableName, name, type);
            throw new RuntimeException(e);
        }
        column.name = name;
        columnInfoMap.put(name, column);
        columnInfoMap.put(Tools.lowStr_hump(name, false).toLowerCase(), column);
        columnInfoMap.put((tableName + "." + name).toLowerCase(), column);
        columnInfoMap.put((Tools.lowStr_hump(tableName, false) + "." + Tools.lowStr_hump(name, false)).toLowerCase(), column);

    }

    public void readTableInfo() {
        List<TableInfo> list = getTableInfo();
        for (TableInfo tableInfo : list) {
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                pueColumnInfoMap(tableInfo.name, tableInfo.columnNames.get(i), tableInfo.columnTypes.get(i));
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

    Map<String, String> typeMapping = new HashMap<>();

    {
        typeMapping.put("bigint", "java.lang.Long");
        typeMapping.put("integer", "java.lang.Integer");
        typeMapping.put("timestamp wit", "java.time.LocalDateTime");
        typeMapping.put("time wit", "java.time.LocalTime");
        typeMapping.put("date wit", "java.time.LocalDate");
        typeMapping.put("double", "java.lang.Double");
        typeMapping.put("numeric", "java.lang.Double");
        typeMapping.put("boolean", "java.lang.Boolean");
        typeMapping.put("datetime", "java.time.LocalDateTime");
    }

    public List<TableInfo> getTableInfo() {
        List<TableInfo> list = new ArrayList<>();

        // --- 步骤 1: 获取所有表名和表备注 ---
        String tableSql = "";
        if (dataBaseConfig.type.equals("mysql")) {
            tableSql = "SELECT TABLE_NAME AS name, TABLE_COMMENT AS remarks " +
                    "FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = '" + dataBaseConfig.name + "' AND TABLE_TYPE = 'BASE TABLE'";
        } else if (dataBaseConfig.type.equals("postgresql")) {
            tableSql = "SELECT relname AS name, CAST(obj_description(relid, 'pg_class') AS VARCHAR) AS remarks " +
                    "FROM pg_catalog.pg_stat_user_tables";
        }

        List<GzbMap> tables = queryMap(tableSql);
        for (GzbMap tableRow : tables) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.name = tableRow.get("name").toString().toLowerCase();
            if (tableRow.get("remarks") != null) {
                tableInfo.dbDesc = tableRow.get("remarks").toString();
            }
            String colSql = "";
            if (dataBaseConfig.type.equals("mysql")) {
                colSql = "SELECT COLUMN_NAME, COLUMN_COMMENT, DATA_TYPE, COLUMN_TYPE, " +
                        "CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_DEFAULT " +
                        "FROM information_schema.COLUMNS " +
                        "WHERE TABLE_SCHEMA = '" + dataBaseConfig.name + "' " +
                        "AND TABLE_NAME = '" + tableInfo.name + "' ORDER BY ORDINAL_POSITION";
            } else if (dataBaseConfig.type.equals("postgresql")) {
                // 增加 a.atttypid AS type_oid 用于匹配 Vert.x 的 DataType 枚举
                // 增加 n.nspname 过滤，防止不同 schema 下同名表干扰
                colSql = "SELECT a.attname AS COLUMN_NAME, " +
                        "d.description AS COLUMN_COMMENT, " +
                        "format_type(a.atttypid, a.atttypmod) AS COLUMN_TYPE, " +
                        "a.atttypid AS type_oid, " +
                        "NOT a.attnotnull AS IS_NULLABLE " +
                        "FROM pg_catalog.pg_attribute a " +
                        "INNER JOIN pg_catalog.pg_class c ON a.attrelid = c.oid " +
                        "INNER JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid " +
                        "LEFT JOIN pg_catalog.pg_description d ON d.objoid = c.oid AND d.objsubid = a.attnum " +
                        "WHERE c.relname = '" + tableInfo.name + "' " +
                        "AND n.nspname = 'public' " + // 通常默认是 public，也可以改成 dataBaseConfig.schema
                        "AND a.attnum > 0 AND NOT a.attisdropped " +
                        "ORDER BY a.attnum";
            }

            List<GzbMap> cols = queryMap(colSql);
            String pkSql = "";
            if (dataBaseConfig.type.equals("mysql")) {
                pkSql = "SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = '" + dataBaseConfig.name + "' " +
                        "AND TABLE_NAME = '" + tableInfo.name + "' AND CONSTRAINT_NAME = 'PRIMARY'";
            } else if (dataBaseConfig.type.equals("postgresql")) {
                pkSql = "SELECT a.attname AS COLUMN_NAME FROM pg_index i " +
                        "JOIN pg_attribute a ON a.attrelid = i.indrelid AND a.attnum = ANY(i.indkey) " +
                        "WHERE i.indrelid = '" + tableInfo.name + "'::regclass AND i.indisprimary";
            }

            List<GzbMap> pks = queryMap(pkSql);
            if (pks != null && pks.size() > 0) {
                if (dataBaseConfig.type.equals("mysql")) {
                    tableInfo.id = pks.get(0).getString("COLUMNName");
                } else if (dataBaseConfig.type.equals("postgresql")) {
                    tableInfo.id = pks.get(0).getString("columnName");
                }
            }
            tableInfo.columnNames = new ArrayList<>();
            tableInfo.columnTypes = new ArrayList<>();
            tableInfo.columnTypesDb = new ArrayList<String>();
            tableInfo.columnDesc = new ArrayList<>();
            tableInfo.columnSize = new ArrayList<>();
            for (GzbMap col : cols) {
                String cName = null;
                String cType = null;
                String cDesc = null;
                if (dataBaseConfig.type.equals("mysql")) {
                    cName = col.getString("COLUMNName");
                    cType = col.getString("COLUMNType");
                    cDesc = col.getString("COLUMNComment");
                } else if (dataBaseConfig.type.equals("postgresql")) {
                    cName = col.getString("columnName");
                    cType = col.getString("columnType");
                    cDesc = col.getString("columnComment");
                }
                if (TYPE_MAP.get(cType.split("\\(")[0]) == null) {
                    log.w("new type", cType);
                }
                if (tableInfo.id == null) {
                    tableInfo.id = cName;
                }
                int size = 0;
                String tmp = Tools.textMid(cType, "(", ")", 1);
                if (tmp != null) {
                    tmp = tmp.split(",")[0];
                    size = Integer.parseInt(tmp);
                }
                String columnClassName = getJavaType(cType);
                if (tableInfo.id != null && tableInfo.id.equals(cName)) {
                    tableInfo.idType = columnClassName;
                }
                tableInfo.columnDesc.add(cDesc == null ? cName : cDesc);
                tableInfo.columnNames.add(cName);
                tableInfo.columnTypes.add(columnClassName);
                tableInfo.columnTypesDb.add(cType);
                tableInfo.columnSize.add(size);
            }
            tableInfo.setColumnNames(tableInfo.columnNames, dataBaseConfig.name);
            list.add(tableInfo);
        }
        return list;
    }

    public static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("int", "java.lang.Integer");
        TYPE_MAP.put("integer", "java.lang.Integer");
        TYPE_MAP.put("int4", "java.lang.Integer");
        TYPE_MAP.put("int2", "java.lang.Integer"); // smallint 映射为 Integer 兼容性更好
        TYPE_MAP.put("smallint", "java.lang.Integer");
        TYPE_MAP.put("bigint", "java.lang.Long");
        TYPE_MAP.put("int8", "java.lang.Long");

        TYPE_MAP.put("double", "java.lang.Double");
        TYPE_MAP.put("double precision", "java.lang.Double");
        TYPE_MAP.put("float8", "java.lang.Double");
        TYPE_MAP.put("float", "java.lang.Float");
        TYPE_MAP.put("float4", "java.lang.Float");
        TYPE_MAP.put("numeric", "java.math.BigDecimal");
        TYPE_MAP.put("decimal", "java.math.BigDecimal");

        TYPE_MAP.put("varchar", "java.lang.String");
        TYPE_MAP.put("character varying", "java.lang.String");
        TYPE_MAP.put("char", "java.lang.String");
        TYPE_MAP.put("character", "java.lang.String");
        TYPE_MAP.put("text", "java.lang.String");
        TYPE_MAP.put("longtext", "java.lang.String");
        TYPE_MAP.put("bpchar", "java.lang.String");

        TYPE_MAP.put("date", "java.time.LocalDate");
        TYPE_MAP.put("datetime", "java.time.LocalDateTime");
        TYPE_MAP.put("timestamp", "java.time.LocalDateTime");
        TYPE_MAP.put("timestamp without time zone", "java.time.LocalDateTime");
        TYPE_MAP.put("timestamp with time zone", "java.time.OffsetDateTime"); // 带时区建议用 OffsetDateTime
        TYPE_MAP.put("time", "java.time.LocalTime");
        TYPE_MAP.put("time without time zone", "java.time.LocalTime");

        TYPE_MAP.put("boolean", "java.lang.Boolean");
        TYPE_MAP.put("bool", "java.lang.Boolean");
        TYPE_MAP.put("uuid", "java.util.UUID");
        TYPE_MAP.put("json", "io.vertx.core.json.JsonObject");
        TYPE_MAP.put("jsonb", "io.vertx.core.json.JsonObject");
        TYPE_MAP.put("bytea", "byte[]");


        TYPE_MAP.put("real", "java.lang.Float");
        TYPE_MAP.put("money", "java.math.BigDecimal");

        TYPE_MAP.put("name", "java.lang.String");
        TYPE_MAP.put("timetz", "java.time.OffsetTime");
        TYPE_MAP.put("time with time zone", "java.time.OffsetTime");
        TYPE_MAP.put("timestamptz", "java.time.OffsetDateTime");

        TYPE_MAP.put("xml", "java.lang.String");
        TYPE_MAP.put("bit", "java.lang.Boolean");
        TYPE_MAP.put("varbit", "java.lang.String");
        TYPE_MAP.put("oid", "java.lang.Long");
    }

    /**
     * 将数据库类型转换为 Java 类型名
     *
     * @param dbType 原始类型字符串，如 "varchar(255)" 或 "timestamp with time zone"
     * @return 完整的 Java 类路径字符串
     */
    public static String getJavaType(String dbType) {
        if (dbType == null || dbType.isEmpty()) {
            return "java.lang.Object";
        }

        // 1. 转小写并去除两端空格
        String cleanType = dbType.toLowerCase().trim();

        // 2. 处理带括号的情况，如 "varchar(64)" -> "varchar"
        // 使用正则或简单的 split 剥离括号内容
        if (cleanType.contains("(")) {
            cleanType = cleanType.substring(0, cleanType.indexOf("(")).trim();
        }

        // 3. 处理 PostgreSQL 特有的 "without time zone" 等后缀
        // 如果映射表里没直接命中，尝试模糊匹配最核心的词
        String result = TYPE_MAP.get(cleanType);

        if (result == null) {
            // 针对类似 "timestamp with time zone" 的特殊处理
            if (cleanType.contains("timestamp")) {
                result = cleanType.contains("with time zone") ? "java.time.OffsetDateTime" : "java.time.LocalDateTime";
            } else if (cleanType.contains("time")) {
                result = "java.time.LocalTime";
            } else if (cleanType.contains("char")) {
                result = "java.lang.String";
            } else if (cleanType.contains("int")) {
                result = cleanType.contains("big") ? "java.lang.Long" : "java.lang.Integer";
            }
        }

        return result != null ? result : "java.lang.Object";
    }


    Object[] objects_def = new Object[0];

    public List<GzbMap> queryMap(String sql) {
        return queryMap(sql, objects_def, true);
    }

    public List<GzbMap> queryMap(String sql, Object[] objects) {
        return queryMap(sql, objects, true);
    }

    public List<GzbMap> queryMap(String sql, Object[] objects, boolean hump)  {
        long[] times = new long[3];
        times[0] = System.nanoTime();
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        query(sql, objects, new Cack() {
            @Override
            public void success(RowSet<Row> rowSet) {
                times[1] = System.nanoTime();
                List<GzbMap> list0 = new ArrayList<>(rowSet.size());
                try {
                    List<String> names = rowSet.columnsNames();
                    for (Row row : rowSet) {
                        GzbMap map = new GzbMap();
                        for (int i = 0; i < names.size(); i++) {
                            if (hump) {
                                map.put(Tools.lowStr_hump(names.get(i)), row.getValue(names.get(i)));
                            } else {
                                map.put(names.get(i), row.getValue(names.get(i)));
                            }
                        }
                        list0.add(map);
                    }
                } finally {
                    printLog(times, sql, objects);
                    wake.notifyActivation(list0);
                }
            }

            @Override
            public void fail(String sql, Object[] objects, Throwable throwable) {
                times[1] = System.nanoTime();
                wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
            }
        });
        return wake.waitActivationData();
    }

    public RowSet<Row> queryRowSet(String sql, Object[] objects)  {
        long[] times = new long[3];
        times[0] = System.nanoTime();
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        query(sql, objects, new DataBase.Cack() {
            @Override
            public void success(RowSet<Row> rowSet0) {
                times[1] = System.nanoTime();
                wake.notifyActivation(rowSet0);
            }

            @Override
            public void fail(String sql, Object[] objects, Throwable throwable) {
                times[1] = System.nanoTime();
                wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
            }
        });
        return wake.waitActivationData();
    }


    /// 常规路径（关闭debug日志 最短） ： 获取纳秒 一次减法运算  一次 整数比较  一次布尔比较 激活主线程
    public void printLog(long[] times, String sql, Object[] objects) {
        times[2] = System.nanoTime();
        long all = times[2] - times[0];
        if (all < dataBaseConfig.sql_time_w) {
            if (log.isShowD()) {
                double d01 = (double) (times[1] - times[0]) / 1000 / 1000;
                double d02 = (double) (times[2] - times[1]) / 1000 / 1000;
                log.d(sql, objects, "执行(ms)", Tools.doubleSize(d01, 4), "组装(ms)", Tools.doubleSize(d02, 4), "总(ms)", Tools.doubleSize((double) all / 1000 / 1000, 4));
            }
        } else if (all < dataBaseConfig.sql_time_e) {
            if (log.isShowW()) {
                double d01 = (double) (times[1] - times[0]) / 1000 / 1000;
                double d02 = (double) (times[2] - times[1]) / 1000 / 1000;
                log.w(sql, objects, "执行(ms)", Tools.doubleSize(d01, 4), "组装(ms)", Tools.doubleSize(d02, 4), "总(ms)", Tools.doubleSize((double) all / 1000 / 1000, 4));
            }
        } else {
            if (log.isShowE()) {
                double d01 = (double) (times[1] - times[0]) / 1000 / 1000;
                double d02 = (double) (times[2] - times[1]) / 1000 / 1000;
                log.e(sql, objects, "执行(ms)", Tools.doubleSize(d01, 4), "组装(ms)", Tools.doubleSize(d02, 4), "总(ms)", Tools.doubleSize((double) all / 1000 / 1000, 4));
            }
        }
    }


    public String getOnlyIdDistributedString() {
        return OnlyId.getDistributedString();
    }

    public Long getOnlyIdDistributed() {
        return OnlyId.getDistributed();
    }

    public int getMaxId(String tableName, String idName)  {
        long[] times = new long[3];
        times[0] = System.nanoTime();
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        String sql = "select " + idName + " from " + tableName + " order by " + idName + " desc limit 1";
        Object[] objects = new Object[]{};
        query(sql, objects, new DataBase.Cack() {
            @Override
            public void success(RowSet<Row> rowSet) {
                times[1] = System.nanoTime();
                int count = -1;
                try {
                    for (Row row : rowSet) {
                        count = row.getInteger(idName);
                        break;
                    }
                } finally {
                    wake.notifyActivation(count);
                }
            }

            @Override
            public void fail(String sql, Object[] objects, Throwable throwable) {
                times[1] = System.nanoTime();
                wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
            }
        });
        return wake.waitActivationData();
    }

    Map<String, AtomicInteger> mapCache = new ConcurrentHashMap<>();

    public int getOnlyIdNumber(String tableName, String idName, boolean reset)  {
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


    public void query(String sql, Object[] objects, Cack cack) {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        if (entity.connection_async != null) {
            entity.connection_async.preparedQuery(sql).execute(Tuple.from(objects), ar -> {
                if (ar.succeeded()) {
                    cack.success(ar.result());
                } else {
                    cack.fail(sql, objects, ar.cause());
                }
            });
        } else {
            pool.preparedQuery(sql).execute(Tuple.from(objects), ar -> {
                if (ar.succeeded()) {
                    cack.success(ar.result());
                } else {
                    cack.fail(sql, objects, ar.cause());
                }
            });
        }
    }

    public int execute(String sql, Object[] objects)  {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        if (entity.transaction_simulate != null) {
            simulate_transaction(sql, objects, entity);
            return 1;
        }

        long[] times = new long[3];
        times[0] = System.nanoTime();
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        execute(sql, objects, new DataBase.Cack() {
            @Override
            public void success(int row) {
                times[1] = System.nanoTime();
                if (log.isShowD()) {
                    log.d(sql, "execute ok"
                            , "执行(ms)", Tools.doubleSize((double) (times[2] - times[1]) / 1000000, 4)
                            , "组装(ms)", Tools.doubleSize((double) (times[1] - times[0]) / 1000000, 4)
                            , "总(ms)", Tools.doubleSize((double) (times[2] - times[0]) / 1000000, 4)
                    );
                }
                wake.notifyActivation(row);
            }

            @Override
            public void fail(String sql, Object[] objects, Throwable throwable) {
                times[1] = System.nanoTime();
                wake.notifyActivation(new GzbException0("sql exec fail",throwable,sql,objects));
            }
        });
        return wake.waitActivationData();
    }

    private void simulate_transaction(String sql, Object[] objects, GzbThreadLocal.Entity entity) {
        List<Object[]> list0 = entity.transaction_simulate.get(sql);
        if (list0 == null) {
            list0 = new ArrayList<>();
            entity.transaction_simulate.put(sql, list0);
        }
        log.d("list.add sql", sql, objects);
        list0.add(objects);
    }

    public void execute(String sql, Object[] objects, Cack cack) {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        if (entity.transaction_simulate != null) {
            simulate_transaction(sql, objects, entity);
            return;
        }
        if (entity.connection_async != null) {
            entity.connection_async.preparedQuery(sql).execute(Tuple.from(objects), ar -> {
                if (ar.succeeded()) {
                    cack.success(ar.result().rowCount());
                } else {
                    cack.fail(sql, objects, ar.cause());
                }
            });
        } else {
            pool.preparedQuery(sql).execute(Tuple.from(objects), ar -> {
                if (ar.succeeded()) {
                    cack.success(ar.result().rowCount());
                } else {
                    cack.fail(sql, objects, ar.cause());
                }
            });
        }
    }

    public int execute(String sql, List<Object[]> list, boolean transaction)  {
        long[] times = new long[]{0L, 0L, 0L};
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        if (entity.transaction_simulate != null) {
            List<Object[]> list0 = entity.transaction_simulate.get(sql);
            if (list0 == null) {
                list0 = new ArrayList<>();
                entity.transaction_simulate.put(sql, list0);
            }
            for (Object[] objects : list) {
                list0.add(objects);
            }
            log.d("list.add sql", sql, list);
            return list.size();
        }
        List<Tuple> list0 = new ArrayList<>(list.size() > dataBaseConfig.batch_size ? dataBaseConfig.batch_size : list.size());

        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        Handler<AsyncResult<RowSet<Row>>> handler = ar -> {
            times[2] = System.nanoTime();
            try {
                if (ar.succeeded()) {
                    int num0=0;
                    RowSet<Row> rowSet = ar.result();
                    while (rowSet != null) {
                        num0+= rowSet.rowCount();
                        rowSet = rowSet.next();
                    }
                    wake.notifyActivation(num0);
                    if (log.isShowD()) {
                        log.d(sql, "executeBatch ok", list0.size()
                                , "执行(ms)", Tools.doubleSize((double) (times[2] - times[1]) / 1000000, 4)
                                , "组装(ms)", Tools.doubleSize((double) (times[1] - times[0]) / 1000000, 4)
                                , "总(ms)", Tools.doubleSize((double) (times[2] - times[0]) / 1000000, 4)
                        );
                    }
                } else {
                    wake.notifyActivation(new GzbException0("sql exec fail",sql,list,ar.cause()));
                }
            } catch (Throwable throwable){
                wake.notifyActivation(new GzbException0("sql init exec error",throwable,sql));
            }
        };
        if (transaction) {
            openTransaction(entity, false);
        }
        int num0=0;
        try {
            for (int i = 0; i < list.size(); i++) {
                if (list0.size() == 0) {
                    times[0] = System.nanoTime();
                }
                Object[] objects = list.get(i);
                list0.add(Tuple.from(objects));
                if (list0.size() == dataBaseConfig.batch_size || i + 1 == list.size()) {
                    times[1] = System.nanoTime();
                    if (entity.connection_async != null) {
                        entity.connection_async.preparedQuery(sql).executeBatch(list0, handler);
                    } else {
                        pool.preparedQuery(sql).executeBatch(list0, handler);
                    }
                    num0+=(int)wake.waitActivationData();
                    list0.clear();
                }
            }
            if (transaction) {
                commit(entity);
            }
        } catch (Throwable e) {
            if (transaction) {
                rollback(entity);
            }
            throw new GzbException0(e);
        } finally {
            if (transaction) {
                endTransaction(entity);
            }
        }

        return num0;
    }


    /// //////////////  这几个方法 供手动调用 或者 框架自动调用  确保 open 后 必须 end

    public Integer readTransactionState() {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        return readTransactionState(entity);
    }

    public Integer readTransactionState(GzbThreadLocal.Entity entity) {
        return entity.transaction_state_async;
    }

    public void openTransaction() {
        gzb.tools.thread.GzbThreadLocal.Entity entity = gzb.tools.thread.GzbThreadLocal.context.get();
        openTransaction(entity, false);
    }

    public void openTransaction(boolean simulate) {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        openTransaction(entity, simulate);
    }

    public void openTransaction(GzbThreadLocal.Entity entity) {
        openTransaction(entity, false);
    }

    public void openTransaction(GzbThreadLocal.Entity entity, boolean simulate) {
        if (entity.connection_async != null && entity.transaction_async != null && entity.transaction_simulate != null) {
            return;
        }
        entity.transaction_state_async = 1;
        if (simulate) {
            entity.transaction_simulate = new ConcurrentHashMap<>();
            return;
        }
        Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
        pool_transactions.getConnection(ar -> {
            try {
                if (ar.succeeded()) {
                    entity.connection_async = ar.result();
                    entity.connection_async.begin(ar2 -> {
                        if (ar2.failed()) {
                            entity.connection_async.close();
                            wake.notifyActivation(new GzbException0("事物 开启 获取连接失败",ar.cause()));
                        } else {
                            entity.transaction_async = ar2.result();
                            wake.notifyActivation();
                        }
                    });
                } else {
                    wake.notifyActivation(new GzbException0("事物 开启 获取连接失败",ar.cause()));

                }
            }catch (Throwable throwable){
                wake.notifyActivation(new GzbException0("事物 开启 出现错误",throwable));
            }
        });
        wake.waitActivation();
    }

    public void endTransaction() {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        endTransaction(entity);
    }

    public void endTransaction(GzbThreadLocal.Entity entity) {
        if (entity.connection_async != null) {
            entity.connection_async.close();
            entity.connection_async = null;
            entity.transaction_async = null;
            entity.transaction_simulate = null;
            entity.transaction_state_async = null;
        } else {
            log.t("closeConnection on entity.connection_async == null");
        }
    }

    public void commit() {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        commit(entity);
    }

    public void commit(GzbThreadLocal.Entity entity) {
        if (entity.transaction_simulate != null) {
            Map<String, List<Object[]>> map = entity.transaction_simulate;
            entity.transaction_simulate = null;
            openTransaction(entity, false);
            try {
                for (Map.Entry<String, List<Object[]>> stringListEntry : map.entrySet()) {
                    try {
                        execute(stringListEntry.getKey(), stringListEntry.getValue(), false);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
                commit(entity);
            } catch (Throwable e) {
                log.e("事物提交失败", e, map);
                rollback(entity);
            } finally {
                /// endTransaction(entity); /// 不需要关闭 依赖外部关闭 因为本身模拟事物也需要关闭
            }
            return;
        }
        if (entity.transaction_async != null) {
            Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
            entity.transaction_async.commit(ar -> {
                if (ar.failed()){
                    wake.notifyActivation(new GzbException0("事物 提交失败",ar.cause()));
                }
                wake.notifyActivation();
            });
            wake.waitActivation();
        } else {
            log.t("commit on entity.transaction_async == null");
        }
    }

    public void rollback() {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        rollback(entity);
    }

    public void rollback(GzbThreadLocal.Entity entity) {
        if (entity.transaction_simulate != null) {
            entity.transaction_simulate = null;
            return;
        }
        if (entity.transaction_async != null) {
            Tools.ThreadWakeUp wake = new Tools.ThreadWakeUp();
            entity.transaction_async.rollback(ar -> {
                if (ar.failed()){
                    wake.notifyActivation(new GzbException0("事物 回滚失败",ar.cause()));
                }
                wake.notifyActivation();
            });
            wake.waitActivation();
        } else {
            log.d("rollback on entity.transaction_async == null");
        }
    }

    public static class Cack {
        public void success(RowSet<Row> rowSet) {

        }

        public void success(int row) {

        }


        public void fail(String sql, Object[] objects, Throwable throwable) {

        }

        public void fail(String sql, List<Object[]> objects, Throwable throwable) {

        }
    }


}