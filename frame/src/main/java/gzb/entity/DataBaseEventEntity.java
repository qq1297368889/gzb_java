package gzb.entity;

import gzb.frame.factory.GzbOneInterface;

import java.lang.reflect.Method;

public class DataBaseEventEntity {
    //归属某个类
    public Class<?> aClass;
    //对应方法
    public Method method;
    //方法参数 类型
    public Class<?>[] met_types;
    //方法参数 名
    public String[] met_names;
    //方法ID
    public int met_id;
    //方法签名
    public String met_sign;

    //http端点映射方法
    public GzbOneInterface run;

    public Class<?> entityClass;
    public int type;
    public boolean before;
    public boolean openTransaction;
    public int depth;

    public DataBaseEventEntity() {

    }

    public DataBaseEventEntity(Class<?> aClass, Method method, Class<?>[] met_types, String[] met_names, int met_id, String met_sign, GzbOneInterface run,
                               Class<?> entityClass, int type, boolean before, boolean openTransaction,int depth) {
        this.aClass = aClass;
        this.method = method;
        this.met_types = met_types;
        this.met_names = met_names;
        this.met_id = met_id;
        this.met_sign = met_sign;
        this.run = run;
        this.entityClass = entityClass;
        this.type = type;
        this.before = before;
        this.openTransaction = openTransaction;
        this.depth = depth;
    }
}
