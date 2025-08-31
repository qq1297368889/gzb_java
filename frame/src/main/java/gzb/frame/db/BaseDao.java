package gzb.frame.db;

import gzb.tools.JSONResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> {

    DataBase getDataBase();
    void setDataBase(DataBase dataBase);
    int count(String sql, Object[] params) throws Exception;
    List<T> query(String sql, Object[]params);
    List<T> query(String sql, Object[]params,String sortField,String sortType,int page,int size);
    List<T> query(T t) throws Exception;
    List<T> query(T t,String sortField,String sortType)throws Exception;
    List<T> query(T t,String sortField,String sortType,int page,int size)throws Exception;
    List<T> query(T t,int page,int size)throws Exception;
    T find(String sql, Object...params)throws Exception;
    T find(T t)throws Exception;
    int save(T t)throws Exception;
    int update(T t)throws Exception;
    int delete(T t)throws Exception;
    int saveBatch(List<T> list)throws Exception;
    int updateBatch(List<T> list)throws Exception;
    int deleteBatch(List<T> list)throws Exception;
    int saveAsync(T t)throws Exception;
    int updateAsync(T t)throws Exception;
    int deleteAsync(T t)throws Exception;
    int execute(String sql, Object[] params)throws Exception;
    int executeBatch(String sql, List<Object[]> params)throws Exception;
    int executeBatch(String sql, List<Object[]> params,int batch) throws Exception;
    int executeAsync(String sql, Object[] params)throws Exception;
    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;
    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;
    //开启事务
    void openTransaction();

    //关闭事务
    void endTransaction();

    //提交并且关闭事务
    void commit() throws SQLException;

    //回滚并且关闭事务
    void rollback() throws SQLException;

}
