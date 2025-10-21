package gzb.entity;

import gzb.frame.factory.GzbOneInterface;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class HttpMappingV4 {
    //归属某个类
    Class<?> aClass;
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

    //是否开启事务
    public boolean isOpenTransaction;
    //是否开启跨域 域名替换
    public boolean isCrossDomainOrigin;
    //限流器
    public Semaphore semaphore;
    //协议头
    public Map<String, String> header;
    //装饰器 调用前
    public List<DecoratorEntity> start;
    //装饰器调用后
    public List<DecoratorEntity> end;

    //http端点映射方法
    public GzbOneInterface run;
}
