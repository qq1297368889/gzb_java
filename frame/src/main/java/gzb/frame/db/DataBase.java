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

import gzb.entity.EntityClassInfo;
import gzb.entity.SqlTemplate;
import gzb.entity.TableInfo;
import gzb.tools.GzbMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface DataBase {

    AsyncFactory getAsyncFactory();


    public String getSign();

    Object[] getDefObjectArr();

    //获取数据库表信息
    List<TableInfo> getTableInfo() throws Exception;

    //获取实体类元数据 自动缓存
    EntityClassInfo getEntityInfo(Object t);

    //各种方式 拼接(主要用于复杂sql生成 数据可能来自用户输入 但这里做了列 表存在性校验) 和 实体类 转 sql
    SqlTemplate toSelect(String tableName, String[] fields, String[] symbol, String[] value, String[] montage, String sortField, String sortType);

    SqlTemplate toSelect(Object t, int page, int size, String sortField, String sortType);

    SqlTemplate toSave(Object t) throws Exception;

    SqlTemplate toSave(Object t, boolean autoId) throws Exception;

    SqlTemplate toUpdate(Object t);

    SqlTemplate toDelete(Object t);

    //获取32位 字符串ID 随机
    String getOnlyIdDistributedString();

    //获取19位 不重复的长整数ID  趋势自增
    Long getOnlyIdDistributed();

    //获取整数最大ID
    int getMaxId(String mapName, String idName);

    //获取整数自增ID
    int getOnlyIdNumber(String mapName, String idName);

    //获取一个数据库连接
    Connection getConnection();

    //获取一个数据库连接
    void close(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement);

    //执行sql查询
    List<GzbMap> selectGzbMap(String sql) throws Exception;

    List<GzbMap> selectGzbMap(String sql, Connection connection) throws Exception;

    //执行sql查询
    List<GzbMap> selectGzbMap(String sql, Object[] objects) throws Exception;

    List<GzbMap> selectGzbMap(String sql, Object[] objects, Connection connection) throws Exception;

    //执行sql查询
    List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump) throws Exception;

    List<GzbMap> selectGzbMap(String sql, Object[] objects, boolean hump, Connection connection) throws Exception;

    //执行sql 修改删除新增
    int runSql(String sql) throws Exception;

    int runSql(String sql, Connection connection) throws Exception;

    //执行sql 修改删除新增
    int runSql(String sql, Object[] para) throws Exception;

    int runSql(String sql, Object[] para, Connection connection) throws Exception;

    //执行sql 修改删除新增 批量
    int runSqlBatch(String sql, List<Object[]> list_parameter) throws Exception;

    int runSqlBatch(String sql, List<Object[]> list_parameter, Connection connection) throws Exception;

    //执行sql 修改删除新增 异步
    int runSqlAsync(String sql, Object[] para);

    //执行sql 修改删除新增 异步 批量
    int runSqlAsyncBatch(String sql, List<Object[]> list_parameter);

}
