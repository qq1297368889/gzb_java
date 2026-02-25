package gzb.frame.factory.v4.entity;

import gzb.entity.RunRes;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheObject {
    public Request request;
    public Response response;
    public Map<String, List<Object>> parar=new HashMap<>();
    public RunRes runRes=new RunRes();
    public Object[] objects=new Object[6];

}
