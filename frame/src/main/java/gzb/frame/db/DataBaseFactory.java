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

import gzb.tools.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseFactory {
    public static Lock lock = new ReentrantLock();
    public static Map<String, DataBase> mapDataBase = new ConcurrentHashMap<>();

    public static DataBase getDataBase(String dbKey) throws Exception {
        return getDataBase(DataBaseConfig.readConfig(dbKey));
    }

    public static DataBase getDataBase(DataBaseConfig dataBaseConfig) throws Exception {
        lock.lock();
        try {
            DataBase db = mapDataBase.get(dataBaseConfig.sign);
            if (db == null) {
                db = new DataBaseImpl(dataBaseConfig);
                mapDataBase.put(dataBaseConfig.sign, db);
            }
            return db;
        } finally {
            lock.unlock();
        }
    }

    public static DataBase remove(String dbName) throws Exception {
        return remove(Config.get("db.mysql." + dbName + ".ip"), Config.get("db.mysql." + dbName + ".port"), Config.get("db.mysql." + dbName + ".name"));
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
