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

package gzb.frame.db.v2;
import gzb.exception.GzbException0;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import javax.tools.Tool;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBaseFactory {
    public static Lock lock = new ReentrantLock();
    public static Map<String, DataBase> mapDataBase = new ConcurrentHashMap<>();

    public static DataBase getDataBase(String dbKey) throws Exception {
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        dataBaseConfig.read(dbKey);
        return getDataBase(dataBaseConfig);
    }

    public static DataBase getDataBase(DataBaseConfig dataBaseConfig){
        lock.lock();
        try {
            DataBase dataBase = mapDataBase.get(dataBaseConfig.sign);
            if (dataBase == null) {
                 dataBase = new DataBase(dataBaseConfig);
                Log.log.d("init dataBase",dataBase, dataBaseConfig);
                mapDataBase.put(dataBaseConfig.sign, dataBase);
            }
            return dataBase;
        } finally {
            lock.unlock();
        }
    }
    @Deprecated
    public static void remove(String dbKey) throws Exception {
        DataBaseConfig dataBaseConfig = new DataBaseConfig();
        dataBaseConfig.read(dbKey);
        remove(dataBaseConfig.sign);
    }
}
