package gzb.start;

import com.frame.dao.SysFileDao;
import com.frame.test.Test;
import gzb.frame.factory.GzbOneInterface;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;


public class GzbHome_V1 {
    SysFileDao sysFileDao;
    /// action
    static GzbOneInterface gzbOneInterface_test=null;
    public static void start(Request request, Response response) throws Exception {
        String url = request.getUri();
        switch (url) {
            case "/home/index":
                response.sendAndFlush("");
                break;
            case "/home/index2":
                response.sendAndFlush("home index2");
                break;
            default:
                response.sendAndFlush("404 not found");
        }
    }
}
