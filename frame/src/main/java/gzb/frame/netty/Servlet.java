package gzb.frame.netty;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import io.netty.handler.codec.http.HttpMethod;

public class Servlet {
    public Servlet() {

    }
    public String get(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String post(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String put(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String delete(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String options(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String head(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String patch(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String trace(Request request,Response response){

        return "{\"code\":\"1\"}";
    }
    public String connect(Request request,Response response){

        return "{\"code\":\"1\"}";
    }

}
