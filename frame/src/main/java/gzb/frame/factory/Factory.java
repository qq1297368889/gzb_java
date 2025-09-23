package gzb.frame.factory;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.frame.server.http.entity.RunRes;

import java.util.Map;

public interface Factory {
    RunRes request(Request request, Response response) throws Exception;
     void loadJavaDir(String classDir, String pwd, String iv) throws Exception;
    public Map<String,Object> getMappingMap();
}
