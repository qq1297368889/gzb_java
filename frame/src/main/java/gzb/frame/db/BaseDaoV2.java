package gzb.frame.db;


public interface BaseDaoV2<T>{
    //基础方法   millisecond
    T findMemory (T t);
    int saveMemory (T t);
    int updateMemory (T t);
    int deleteMemory (T t);
}
