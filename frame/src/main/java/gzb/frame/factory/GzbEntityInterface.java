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

package gzb.frame.factory;

import gzb.entity.SqlTemplate;


public interface GzbEntityInterface {
    String toJson(Object object) throws Exception;

    byte[] toJsonBytes(Object object) throws Exception;

    Object[] loadObject(java.util.Map<String, java.util.List<Object>> map) throws Exception;

    SqlTemplate toDeleteSql(Object obj) throws Exception;

    SqlTemplate toUpdateSql(Object obj) throws Exception;

    SqlTemplate toSaveSql(Object obj,gzb.frame.db.DataBase dataBase,boolean reset) throws Exception;

    SqlTemplate toSelectSql(Object obj) throws Exception;
    public Object loadResultSet(java.sql.ResultSet resultSet, java.util.Set<String> names) throws Exception;
    void toJson(Object obj,StringBuilder stringBuilder) throws Exception;

}
