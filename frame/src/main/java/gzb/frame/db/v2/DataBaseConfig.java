package gzb.frame.db.v2;

import gzb.tools.Config;

public class DataBaseConfig {
    public int sql_type = 0;
    /// 数据库类型
    public String type;
    /// ip
    public String ip;
    /// 端口
    public Integer port;
    /// 账号
    public String acc;
    /// 密码
    public String pwd;
    /// 数据库名
    public String name;
    /// 缓存SQL 数量
    public Integer cache_sql_num;
    /// 事物独占 sql 连接数
    public Integer pool_size_transactions;
    /// 普通 sql 连接数
    public Integer pool_size;
    /// 流水线深度
    public Integer pipeline_size;
    /// 流水线深度 事物专属
    public Integer pipeline_size_transactions;
    public String sign = null;
    public Integer sql_time_w;
    public Integer sql_time_e;
    public Integer batch_size;
    public Integer async_thread_num;
    public Integer async_await_ms;
    public Integer async_queue_size;

    public DataBaseConfig read(String key) {
        type = Config.get("gzb.db." + key + ".type", null);
        ip = Config.get("gzb.db." + key + ".ip", null);
        port = Config.getInteger("gzb.db." + key + ".port", null);
        acc = Config.get("gzb.db." + key + ".acc", null);
        pwd = Config.get("gzb.db." + key + ".pwd", null);
        name = Config.get("gzb.db." + key + ".name", null);
        pool_size = Config.getInteger("gzb.db." + key + ".thread.num", Config.cpu + 3);
        pool_size_transactions = Config.getInteger("gzb.db." + key + ".transaction.thread.num", Config.cpu * 5);
        pipeline_size_transactions = Config.getInteger("gzb.db." + key + ".transaction.pipeline", 16);
        pipeline_size = Config.getInteger("gzb.db." + key + ".pipeline", 16);
        cache_sql_num = Config.getInteger("gzb.db." + key + ".cache.num", 1024);
        sql_time_w = Config.getInteger("gzb.db." + key + ".run.time.w", 100);
        sql_time_e = Config.getInteger("gzb.db." + key + ".run.time.e", 1000);
        batch_size = Config.getInteger("gzb.db." + key + ".batch.size", 1024);

        async_thread_num = Config.getInteger("gzb.db." + key + ".async.thread.num", 1);
        async_queue_size = Config.getInteger("gzb.db." + key + ".async.queue.size", Config.cpu * 100);
        async_await_ms = Config.getInteger("gzb.db." + key + ".async.await.ms", 100);

        sign = ip + "_" + port + "_" + name + "_" + acc + "_" + pwd;
        if (type.equals("postgresql")) {
            sql_type = 1;
        }
        sql_time_w = sql_time_w * 1000 * 1000;
        sql_time_e = sql_time_e * 1000 * 1000;
        return this;
    }

    public static DataBaseConfig readConfig(String key) {
        return new DataBaseConfig().read(key);
    }

    @Override
    public String toString() {
        return "DataBaseConfig@" + this.hashCode() + "{" +
                "sql_type=" + sql_type +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", acc='" + acc + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", cache_sql_num=" + cache_sql_num +
                ", pool_size_transactions=" + pool_size_transactions +
                ", pool_size=" + pool_size +
                ", pipeline_size=" + pipeline_size +
                ", pipeline_size_transactions=" + pipeline_size_transactions +
                ", sign='" + sign + '\'' +
                ", sql_time_w=" + sql_time_w +
                ", sql_time_e=" + sql_time_e +
                ", batch_size=" + batch_size +
                "}";
    }

    public int getSql_type() {
        return sql_type;
    }

    public void setSql_type(int sql_type) {
        this.sql_type = sql_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCache_sql_num() {
        return cache_sql_num;
    }

    public void setCache_sql_num(Integer cache_sql_num) {
        this.cache_sql_num = cache_sql_num;
    }

    public Integer getPool_size_transactions() {
        return pool_size_transactions;
    }

    public void setPool_size_transactions(Integer pool_size_transactions) {
        this.pool_size_transactions = pool_size_transactions;
    }

    public Integer getPool_size() {
        return pool_size;
    }

    public void setPool_size(Integer pool_size) {
        this.pool_size = pool_size;
    }

    public Integer getPipeline_size() {
        return pipeline_size;
    }

    public void setPipeline_size(Integer pipeline_size) {
        this.pipeline_size = pipeline_size;
    }

    public Integer getPipeline_size_transactions() {
        return pipeline_size_transactions;
    }

    public void setPipeline_size_transactions(Integer pipeline_size_transactions) {
        this.pipeline_size_transactions = pipeline_size_transactions;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getSql_time_w() {
        return sql_time_w;
    }

    public void setSql_time_w(Integer sql_time_w) {
        this.sql_time_w = sql_time_w;
    }

    public Integer getSql_time_e() {
        return sql_time_e;
    }

    public void setSql_time_e(Integer sql_time_e) {
        this.sql_time_e = sql_time_e;
    }

    public Integer getBatch_size() {
        return batch_size;
    }

    public void setBatch_size(Integer batch_size) {
        this.batch_size = batch_size;
    }
}
