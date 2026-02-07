package gzb.frame.db;

import gzb.tools.Config;
import gzb.tools.Tools;

import java.io.IOException;

public class DataBaseConfig {
    String type;
    String key;
    String clz;
    String ip;
    int port;
    String name;
    String acc;
    String pwd;
    int threadMax;
    int overtime;
    int asyncSleepMilli;
    int asyncBatchSize;
    int asyncThreadNum;
    String sign;

    // type 对应数据库类型
    // key 数据库标识
    public static DataBaseConfig readConfig(String key) {
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        dataBaseConfig.setKey(key);
        dataBaseConfig.setType(Config.get("db."+ key + ".type", "127.0.0.1"));

        dataBaseConfig.setIp(Config.get("db." + key + ".ip", "127.0.0.1"));
        dataBaseConfig.setPort(Config.getInteger("db." + key + ".port", 5432));
        dataBaseConfig.setAcc(Config.get("db." + key + ".acc", null));
        dataBaseConfig.setPwd(Config.get("db." + key + ".pwd", null));
        dataBaseConfig.setName(Config.get("db." + key + ".name", null));

        dataBaseConfig.setClz(Config.get("db." + key + ".clz", "com.mysql.cj.jdbc.Driver"));
        dataBaseConfig.setThreadMax(Config.getInteger("db." + key + ".thread.max", Config.cpu * 3));
        dataBaseConfig.setOvertime(Config.getInteger("db." + key + ".overtime", 5000));
        dataBaseConfig.setAsyncSleepMilli(Config.getInteger("db." + key + ".async.sleep", 500));
        dataBaseConfig.setAsyncBatchSize(Config.getInteger("db." + key + ".async.batch", 500));
        dataBaseConfig.setAsyncThreadNum(Config.getInteger("db." + key + ".async.thread.num", Math.max(Config.cpu / 4, 2)));
        dataBaseConfig.setSign(dataBaseConfig.getIp() + "_" + dataBaseConfig.port + "_" + dataBaseConfig.name);

        return dataBaseConfig;
    }

    public String getUrl() {
        return getUrl(null);
    }

    public String getUrl(String parar) {
        return "jdbc:" + type + "://" + ip + ":" + port + "/" + name + (parar != null && parar.length() > 0 ? "?" + parar : "");
    }

    public static DataBaseConfig readConfig(String type, String key, String clz, String ip, int port, String name, String acc, String pwd, int threadMax, int overtime, int asyncSleepMilli, int asyncBatchSize, int asyncThreadNum, String sign) {
        return new DataBaseConfig(type, key, clz, ip, port, name, acc, pwd, threadMax, overtime, asyncSleepMilli, asyncBatchSize, asyncThreadNum, sign);
    }

    public DataBaseConfig() {
    }

    public DataBaseConfig(String type, String key, String clz, String ip, int port, String name, String acc, String pwd, int threadMax, int overtime, int asyncSleepMilli, int asyncBatchSize, int asyncThreadNum, String sign) {
        setType(type);
        setKey(key);
        setClz(clz);
        setThreadMax(threadMax);
        setOvertime(overtime);
        setAsyncSleepMilli(asyncSleepMilli);
        setAsyncBatchSize(asyncBatchSize);
        setAsyncThreadNum(asyncThreadNum);
        setSign(sign);
        setIp(ip);
        setPort(port);
        setName(name);
        setAcc(acc);
        setPwd(pwd);
        setSign(getIp() + "-" + port + "-" + name);
        save();
    }

    public void save() {
        Config.set("db." + key + ".url", type);
        Config.set("db." + key + ".clz", clz);
        Config.set("db." + key + ".thread.max", String.valueOf(threadMax));
        Config.set("db." + key + ".overtime", String.valueOf(overtime));
        Config.set("db." + key + ".async.sleep", String.valueOf(asyncSleepMilli));
        Config.set("db." + key + ".async.batch", String.valueOf(asyncBatchSize));
        Config.set("db." + key + ".async.thread.num", String.valueOf(asyncThreadNum));
        Config.set("db." + key + ".sign", sign);
        Config.set("db." + key + ".ip", ip);
        Config.set("db." + key + ".port", String.valueOf(port));
        Config.set("db." + key + ".acc", acc);
        Config.set("db." + key + ".pwd", pwd);
        Config.set("db." + key + ".name", name);

        Config.save();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public int getThreadMax() {
        return threadMax;
    }

    public void setThreadMax(int threadMax) {
        this.threadMax = threadMax;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getAsyncSleepMilli() {
        return asyncSleepMilli;
    }

    public void setAsyncSleepMilli(int asyncSleepMilli) {
        this.asyncSleepMilli = asyncSleepMilli;
    }

    public int getAsyncBatchSize() {
        return asyncBatchSize;
    }

    public void setAsyncBatchSize(int asyncBatchSize) {
        this.asyncBatchSize = asyncBatchSize;
    }

    public int getAsyncThreadNum() {
        return asyncThreadNum;
    }

    public void setAsyncThreadNum(int asyncThreadNum) {
        this.asyncThreadNum = asyncThreadNum;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\""+type+"\"," +
                "\"key\":\""+key+"\"," +
                "\"clz\":\""+clz+"\"," +
                "\"ip\":\""+ip+"\"," +
                "\"port\":\""+port+"\"," +
                "\"name\":\""+name+"\"," +
                "\"acc\":\""+acc+"\"," +
                "\"pwd\":\""+pwd+"\"," +
                "\"threadMax\":\""+threadMax+"\"," +
                "\"overtime\":\""+overtime+"\"," +
                "\"asyncSleepMilli\":\""+asyncSleepMilli+"\"," +
                "\"asyncBatchSize\":\""+asyncBatchSize+"\"," +
                "\"asyncThreadNum\":\""+asyncThreadNum+"\"," +
                "\"sign\":\""+sign+"\"," +
                "\"className\":\""+this.getClass().getName()+"\"" +
                "}";
    }
}
