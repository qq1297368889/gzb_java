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

import gzb.tools.JSONResult;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * 一个通用的数据访问对象（DAO）接口，用于处理数据库操作。
 * 该接口提供了 CRUD（创建、读取、更新、删除）操作、事务管理和缓存功能。
 * 类型参数 T 代表实体类。
 * </p>
 *
 * @param <T> 实体类型。
 */
public interface BaseDao<T> {

    // --- 连接与数据源管理 ---

    /**
     * 获取当前 DAO 实例使用的数据库连接。
     *
     * @return 数据库连接对象。
     */
    Connection getConnect() throws SQLException;

    /**
     * 获取与当前 DAO 关联的数据库信息对象。
     *
     * @return DataBase 对象。
     */
    DataBase getDataBase();

    /**
     * 设置与当前 DAO 关联的数据库信息对象。
     *
     * @param dataBase 要设置的 DataBase 对象。
     */
    void setDataBase(DataBase dataBase);

    // --- 查询操作 ---

    int count(T t) throws Exception;

    public T find(String sql, Object[] params) throws Exception;
    public T find(String sql, Object[] params, int second) throws Exception;
    /**
     * 根据实体对象的非空属性作为查询条件，查询并返回一个实体列表。
     *
     * @param t 作为查询条件的实体对象。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t) throws Exception;

    /**
     * 根据实体对象的非空属性和排序信息，查询并返回一个实体列表。
     *
     * @param t         作为查询条件的实体对象。
     * @param sortField 排序字段。
     * @param sortType  排序类型（"asc" 或 "desc"）。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, String sortField, String sortType) throws Exception;

    /**
     * 根据实体对象的非空属性、排序和分页信息，查询并返回一个实体列表。
     *
     * @param t         作为查询条件的实体对象。
     * @param sortField 排序字段。
     * @param sortType  排序类型（"asc" 或 "desc"）。
     * @param page      页码。
     * @param size      每页记录数。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, String sortField, String sortType, int page, int size) throws Exception;

    /**
     * 根据实体对象的非空属性和分页信息，查询并返回一个实体列表。
     *
     * @param t    作为查询条件的实体对象。
     * @param page 页码。
     * @param size 每页记录数。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, int page, int size) throws Exception;

    /**
     * 根据实体对象的主键，查找并返回单个实体。
     *
     * @param t 包含主键信息的实体对象。
     * @return 单个实体对象，如果未找到则返回 null。
     * @throws Exception 如果发生数据库访问错误。
     */
    T find(T t) throws Exception;

    // --- 数据修改操作 ---

    /**
     * 保存一个新的实体到数据库中。
     *
     * @param t 要保存的实体对象。
     * @return 影响的记录数（通常为 1）。
     * @throws Exception 如果发生数据库访问错误。
     */
    int save(T t) throws Exception;

    /**
     * 根据实体对象的主键，更新数据库中的记录。
     *
     * @param t 要更新的实体对象。
     * @return 影响的记录数（通常为 1）。
     * @throws Exception 如果发生数据库访问错误。
     */
    int update(T t) throws Exception;

    /**
     * 根据实体对象的主键，从数据库中删除记录。
     *
     * @param t 要删除的实体对象。
     * @return 影响的记录数（通常为 1）。
     * @throws Exception 如果发生数据库访问错误。
     */
    int delete(T t) throws Exception;

    /**
     * 异步保存一个新的实体到数据库中。
     *
     * @param t 要保存的实体对象。
     * @return 影响的记录数。
     * @throws Exception 如果发生数据库访问错误。
     */
    int saveAsync(T t) throws Exception;
    int saveAsync(T t,Runnable fail) throws Exception;

    /**
     * 异步更新一个实体。
     *
     * @param t 要更新的实体对象。
     * @return 影响的记录数。
     * @throws Exception 如果发生数据库访问错误。
     */
    int updateAsync(T t) throws Exception;
    int updateAsync(T t,Runnable fail) throws Exception;

    /**
     * 异步删除一个实体。
     *
     * @param t 要删除的实体对象。
     * @return 影响的记录数。
     * @throws Exception 如果发生数据库访问错误。
     */
    int deleteAsync(T t) throws Exception;
    int deleteAsync(T t,Runnable fail) throws Exception;

    /**
     * 根据实体对象、排序和分页信息，返回一个包含分页数据的 JSON 结果。
     *
     * @param t         作为查询条件的实体对象。
     * @param sortField 排序字段。
     * @param sortType  排序类型。
     * @param page      页码。
     * @param size      每页记录数。
     * @param maxPage   最大页数限制。
     * @param maxSize   每页最大记录数限制。
     * @return 包含分页数据的 JSONResult 对象。
     * @throws Exception 如果发生数据库访问错误。
     */
    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;

    // --- 缓存操作 ---

    T find(T t, int second) throws Exception;
    /**
     * 根据实体对象，从缓存中查询实体列表。
     *
     * @param t           作为查询条件的实体对象。
     * @param second  缓存有效期，单位 秒。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, int second) throws Exception;

    /**
     * 根据实体对象和排序信息，从缓存中查询实体列表。
     *
     * @param t           作为查询条件的实体对象。
     * @param sortField   排序字段。
     * @param sortType    排序类型。
     * @param second  缓存有效期，单位 秒。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, String sortField, String sortType, int second) throws Exception;

    /**
     * 根据实体对象、排序和分页信息，从缓存中查询实体列表。
     *
     * @param t           作为查询条件的实体对象。
     * @param sortField   排序字段。
     * @param sortType    排序类型。
     * @param page        页码。
     * @param size        每页记录数。
     * @param second  缓存有效期，单位 秒。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, String sortField, String sortType, int page, int size, int second) throws Exception;

    /**
     * 根据实体对象和分页信息，从缓存中查询实体列表。
     *
     * @param t           作为查询条件的实体对象。
     * @param page        页码。
     * @param size        每页记录数。
     * @param second  缓存有效期，单位 秒。
     * @return 实体列表。
     * @throws Exception 如果发生数据库访问错误。
     */
    List<T> query(T t, int page, int size, int second) throws Exception;

    /**
     * 根据实体对象和分页信息，从缓存中查询并返回分页结果。
     *
     * @param t           作为查询条件的实体对象。
     * @param sortField   排序字段。
     * @param sortType    排序类型。
     * @param page        页码。
     * @param size        每页记录数。
     * @param maxPage     最大页数限制。
     * @param maxSize     每页最大记录数限制。
     * @param second  缓存有效期，单位 秒。
     * @return 包含分页数据的 JSONResult 对象。
     * @throws Exception 如果发生数据库访问错误。
     */
    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception;




    /**
     * 执行一条 SQL DML（数据操作语言）语句，如 INSERT, UPDATE, DELETE。
     *
     * @param sql    SQL 语句。
     * @param params 语句参数数组。
     * @return 影响的记录数。
     * @throws Exception 如果发生数据库访问错误。
     */
    int execute(String sql, Object[] params) throws Exception;
    List<T> query(String sql, Object[] params, String sortField, String sortType, int page, int size, int second) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, Exception;
    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;
    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception;


}