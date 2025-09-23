package gzb.frame.db;

import gzb.tools.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseFactory {
    public static Lock lock = new ReentrantLock();
    public static Map<String, DataBase> mapDataBase = new ConcurrentHashMap<>();

    public static DataBase getDataBase(String dbKey) throws Exception {
        lock.lock();
        try {
            String key = Config.get("db.mysql." + dbKey + ".ip") + "_"
                    + Config.get("db.mysql." + dbKey + ".port") + "_"
                    + Config.get("db.mysql." + dbKey + ".name");
            DataBase db = mapDataBase.get(key);
            if (db == null) {
                db = new DataBaseMsql(dbKey);
                mapDataBase.put(key, db);
            }
            return db;
        } finally {
            lock.unlock();
        }
    }

    public static DataBase getDataBase(String dbName, String ip, String port, String acc, String pwd, String clz,
                                       Boolean auto, Integer threadMax, Integer overtime,
                                       Integer asyncSleep,Integer asyBatch,Integer asyThreadMax) throws Exception {
        lock.lock();
        try {
            String key = ip + "_" + port + "_" + dbName;
            DataBase db = mapDataBase.get(key);
            if (db == null) {
                db = new DataBaseMsql(dbName, ip, port, acc, pwd, clz, auto, threadMax, overtime, asyncSleep,asyBatch,asyThreadMax);
                mapDataBase.put(key, db);
            }
            return db;
        } finally {
            lock.unlock();
        }
    }

    public static DataBase remove(String dbName) throws Exception {
        lock.lock();
        try {
            String key = Config.get("db.mysql." + dbName + ".ip") + "_" + Config.get("db.mysql." + dbName + ".port") + "_" + Config.get("db.mysql." + dbName + ".name");
            return mapDataBase.remove(key);
        } finally {
            lock.unlock();
        }
    }

    public static DataBase remove(String dbName, String ip, String port) throws Exception {
        lock.lock();
        try {
            String key = ip + "_" + port + "_" + dbName;
            return mapDataBase.remove(key);
        } finally {
            lock.unlock();
        }
    }
}
