package gzb.frame.factory.v4.entity;

import gzb.frame.factory.GzbOneInterface;

import java.util.List;
import java.util.Map;

public class HttpMapping {
    //http端点映射方法
    public GzbOneInterface httpMappingFun;

    public String sign;
    public String path;
    public int met;
    //是否开启事务
    public boolean isOpenTransaction;
    //是否开启跨域 域名替换
    public boolean isCrossDomainOrigin;
    public int limitation;
    public List<DecoratorEntity> start;
    public List<DecoratorEntity> end;
    public Map<String,String> header;
}
