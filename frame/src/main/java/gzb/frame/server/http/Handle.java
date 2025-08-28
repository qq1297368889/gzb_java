package gzb.frame.server.http;

import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.frame.factory.ClassFactory;
import gzb.frame.server.http.entity.RunRes;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class Handle {
    public static Log log = new LogImpl();
    public static String staticDir = null;
    public static String encoding="UTF-8";
    public static String DEF_CONTENT_TYPE="text/html;charset="+encoding;
    //外部访问入口 默认
    public static ClassFactory classFactory = new ClassFactory();
    static {
        log.d("load  Handle");
        try {
            staticDir = Config.get("gzb.system.server.http.static.dir", Config.thisPath());
            long start = System.currentTimeMillis();
            classFactory.init();
            DataBase dataBase=null;
            String name = Config.get("db.frame.name", "frame");
            try {
                dataBase = DataBaseFactory.getDataBase(name);
            } catch (Exception e) {
                System.out.println("默认数据连接失败：" + name);
            }
            log.d("加载权限信息",name,Tools.loadApiInfo(dataBase));
            long end = System.currentTimeMillis();
            log.d("类编译加载耗时："+(end-start));
        } catch (Exception e) {
            log.e("Handle.static 出错", e);
        }
    }

    public static void start(String met,HttpServletRequest request, HttpServletResponse response) {
        try {
            String requestUrl=URLDecoder.decode(request.getRequestURI(), "UTF-8");
            request.setCharacterEncoding(encoding);
            response.setHeader("content-type",DEF_CONTENT_TYPE);
            RunRes runRes =classFactory.webRequest(requestUrl,met,request,response, null);
            //log.d(requestUrl,met,"耗时NS:",runRes.getEnd()-runRes.getStart(),runRes);
            if (runRes.getState() == 404){
                boolean res = StaticResourceHandler.handleStaticResource(request,response,staticDir,requestUrl);
                log.d(request,response,staticDir,requestUrl,res);
                if (!res) {
                    response.setStatus(404);
                }
            }else{
                if (runRes.getState()!=304 && runRes.getState()!=500 && runRes.getState()!=400) {
                    runRes.setState(200);
                }
                sendData(request,response,runRes.getState(),runRes.getData());
            }
        } catch (Exception e) {
            log.e("Handle.start 出错", e);
            response.setStatus(500);
        }
    }
    public static void sendData(HttpServletRequest request, HttpServletResponse response,int code,Object data) throws IOException, ServletException {
        response.setStatus(code);
        if (data != null){
            response.setCharacterEncoding(encoding);
            if (data.getClass() == byte[].class) {
                response.getOutputStream().write((byte[])data);
            }else{
                response.getOutputStream().write(data.toString().getBytes(Config.encoding));
            }
            response.getOutputStream().flush();
        }
    }
}
