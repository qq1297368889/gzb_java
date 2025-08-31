package gzb.frame.db;

import gzb.tools.JSONResult;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao2<T> {
    //################ 通用方法
    DataBase getDataBase();

    void setDataBase(DataBase dataBase);

    //################SQL 方式
    //单个查询 sql
    T find(String sql, Object[] objects) throws Exception;

    //正常查询 sql
    List<T> query(String sql, Object[] objects) throws Exception;

    List<T> query(String sql, Object[] objects, Integer page, Integer size) throws Exception;

    List<T> query(String sql, Object[] objects,String sortField,String sortType, Integer page, Integer size) throws Exception;

    JSONResult queryPage(String sql, Object[] objects, Integer page, Integer size, int maxPage, int maxSize) throws Exception;

    JSONResult queryPage(String sql, Object[] objects,String sortField,String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;


    //正常增删改 sql
    int save(String sql, Object[] objects) throws Exception;

    int update(String sql, Object[] objects) throws Exception;

    int delete(String sql, Object[] objects) throws Exception;

    //异步提交 sql
    int saveAsync(String sql, Object[] objects);

    int updateAsync(String sql, Object[] objects);

    int deleteAsync(String sql, Object[] objects);

    //批量提交 sql
    int saveBatch(String sql, List<Object[]> list) throws Exception;

    int updateBatch(String sql, List<Object[]> list) throws Exception;

    int deleteBatch(String sql, List<Object[]> list) throws Exception;

    //批量异步提交 sql
    int saveBatchAsync(String sql, List<Object[]> list);


    int updateBatchAsync(String sql, List<Object[]> list);

    int deleteBatchAsync(String sql, List<Object[]> list);


    //################对象 方式
    //单个查询 对象
    T find(T t) throws Exception;

    //正常查询 对象
    List<T> query(T t) throws Exception;

    List<T> query(T t, Integer page, Integer size) throws Exception;

    List<T> query(T t,String sortField,String sortType, Integer page, Integer size) throws Exception;

    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;

    //正常增删改 对象
    int save(T t) throws Exception;

    int save(T t, boolean autoId) throws Exception;

    int update(T t) throws Exception;

    int delete(T t) throws Exception;

    //异步提交 对象
    int saveAsync(T t) throws Exception;

    int saveAsync(T t, boolean autoId) throws Exception;

    int updateAsync(T t);

    int deleteAsync(T t);

    //批量提交 对象
    int saveBatch(List<T> list) throws Exception;

    int saveBatch(List<T> list, boolean autoId) throws Exception;

    int updateBatch(List<T> list) throws Exception;

    int deleteBatch(List<T> list) throws Exception;

    //批量异步提交 对象
    int saveBatchAsync(List<T> list) throws Exception;

    int saveBatchAsync(List<T> list, boolean autoId) throws Exception;

    int updateBatchAsync(List<T> list) throws Exception;

    int deleteBatchAsync(List<T> list) throws Exception;

    /////////////////////////////////必须要传入 Connection 且需要手动提交
    //正常增删改 sql
    int save(String sql, Object[] objects, Connection connection) throws Exception;

    int update(String sql, Object[] objects, Connection connection) throws Exception;

    int delete(String sql, Object[] objects, Connection connection) throws Exception;


    //批量提交
    int saveBatch(String sql, List<Object[]> list, Connection connection) throws Exception;

    int updateBatch(String sql, List<Object[]> list, Connection connection) throws Exception;

    int deleteBatch(String sql, List<Object[]> list, Connection connection) throws Exception;

    //正常增删改 对象
    int save(T t, Connection connection) throws Exception;

    int save(T t, boolean autoId, Connection connection) throws Exception;

    int update(T t, Connection connection) throws Exception;

    int delete(T t, Connection connection) throws Exception;

    //批量提交 对象
    int saveBatch(List<T> list, Connection connection) throws Exception;

    int saveBatch(List<T> list, boolean autoId, Connection connection) throws Exception;

    int updateBatch(List<T> list, Connection connection) throws Exception;

    int deleteBatch(List<T> list, Connection connection) throws Exception;
    //避免单例 在这里创建新对象 并且在这对象里设置为手动提交
/*
    BaseDao<T> openTransaction() throws Exception;
    void commit() throws Exception;
    void rollback() throws Exception;
*/

}
