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

import gzb.entity.SqlTemplate;
import gzb.frame.db.AsyncFactory;
import gzb.frame.db.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Async<T> {
    public Map<String, ConcurrentLinkedQueue<Object[]>> cacheMap = new ConcurrentHashMap<>();
    List<String> list_sql = new ArrayList<>();
    List<Object[]> list_parar = new ArrayList<>();
    DataBase dataBase;

    public Async(DataBase dataBase) {
        this.dataBase=dataBase;
    }

    public Async<T> append(String sql, Object[] parar) {
        list_sql.add(sql);
        list_parar.add(parar);
        return this;
    }

    public Async<T> save(T t) throws Exception {
        SqlTemplate sqlTemplate = dataBase.toSave(t);
        append(sqlTemplate.getSql(),sqlTemplate.getObjects());
        return this;
    }

    public Async<T> update(T t) {
        SqlTemplate sqlTemplate = dataBase.toUpdate(t);
        append(sqlTemplate.getSql(),sqlTemplate.getObjects());
        return this;
    }

    public Async<T> delete(T t) {
        SqlTemplate sqlTemplate = dataBase.toDelete(t);
        append(sqlTemplate.getSql(),sqlTemplate.getObjects());
        return this;
    }

    public int commit() throws Exception {
        int x01 = 0;
        for (int i = 0; i < list_sql.size(); i++) {
            x01 += dataBase.runSql(list_sql.get(i), list_parar.get(i));
        }
        return x01;
    }
}
