package gzb.frame.db.v2;

import gzb.entity.SqlTemplate;
import gzb.frame.PublicEntrance;
import gzb.frame.factory.ClassTools;
import gzb.tools.JSONResult;

import java.util.List;

public interface BaseDao<T> {

    void init(String key);

    List<T> query(String sql, Object[] objects, int second);

    List<T> query(String sql, Object[] objects);

    int execute(String sql, Object[] objects);

    int execute(String sql, List<Object[]> list) throws Throwable;

    int execute(String sql, List<Object[]> list, boolean transaction) throws Throwable;

    Object[] getCountSql(String sql, Object[] objects);

    int count(String sql, Object[] objects, int second) throws Exception;

    int count(String sql, Object[] objects) throws Exception;

    List<T> query(T t, String sortField, String sortType, Integer page, Integer size, int second) throws Exception;

    List<T> query(T t, String sortField, String sortType, Integer page, Integer size) throws Exception;

    List<T> query(T t, Integer page, Integer size, int second) throws Exception;

    List<T> query(T t, Integer page, Integer size) throws Exception;

    List<T> query(T t, int second) throws Exception;

    List<T> query(T t) throws Exception;

    T find(T t, int second) throws Exception;

    T find(T t) throws Exception;


    int count(T t, String sortField, String sortType, Integer page, Integer size, int second) throws Exception;

    int count(T t, String sortField, String sortType, Integer page, Integer size) throws Exception;

    int count(T t, Integer page, Integer size, int second) throws Exception;

    int count(T t, Integer page, Integer size) throws Exception;

    int count(T t, int second) throws Exception;

    int count(T t) throws Exception;

    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception;

    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;

    JSONResult queryPage(T t, String sortField, String sortType, Integer page, Integer size) throws Exception;

    JSONResult queryPage(T t, Integer page, Integer size, int second) throws Exception;

    JSONResult queryPage(T t, Integer page, Integer size) throws Exception;

    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize, int second) throws Exception;

    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size, int maxPage, int maxSize) throws Exception;

    JSONResult queryPage(String sql, Object[] objects, String sortField, String sortType, Integer page, Integer size) throws Exception;

    JSONResult queryPage(String sql, Object[] objects, Integer page, Integer size) throws Exception;

    int save(T t) throws Exception;

    int update(T t) throws Exception;

    int delete(T t) throws Exception;


    int save(List<T> list) throws Throwable;

    int update(List<T> list) throws Throwable;

    int delete(List<T> list) throws Throwable;

    int saveAsync(T t) throws Exception;

    int saveAsync(T t, Runnable fail) throws Exception;

    int saveAsync(T t, Runnable fail, Runnable success) throws Exception;

    int updateAsync(T t) throws Exception;

    int updateAsync(T t, Runnable fail) throws Exception;

    int updateAsync(T t, Runnable fail, Runnable success) throws Exception;

    int deleteAsync(T t) throws Exception;

    int deleteAsync(T t, Runnable fail) throws Exception;

    int deleteAsync(T t, Runnable fail, Runnable success) throws Exception;

    DataBase getDataBase();
}
