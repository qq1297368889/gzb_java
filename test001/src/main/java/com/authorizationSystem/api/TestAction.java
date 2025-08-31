package com.authorizationSystem.api;

import com.authorizationSystem.dao.ApplicationDao;
import com.authorizationSystem.entity.Application;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.Tools;
import gzb.tools.http.HTTPV2;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
@Controller
@RequestMapping(value = "/test/", header = "content-type:application/json;charset=UTF-8")
public class TestAction {
    static Log log = new LogImpl();


    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(TestAction.class));
        HTTPV2 httpv2 = new HTTPV2();
        String path = "http://127.0.0.1:2081/test/";
        log.d("image_code", httpv2.request(path + "image_code", null, 0).toByte());
        log.d("get_byte", httpv2.request(path + "get_byte", null, 0).toByte());
        log.d("post", httpv2.request(path + "post", "msg=msg001", 1).toString());
        log.d("put", httpv2.request(path + "put?msg=msg002", null, 2).toString());
        log.d("delete", httpv2.request(path + "delete?msg=msg003", null, 3).toString());
        log.d("request", httpv2.request(path + "request?msg=msg004", null, 0).toString());
        log.d("dao_test_save", httpv2.request(path + "dao_test_save", "{\"applicationName\":\"name1\",\"applicationDesc\":\"desc1\"}", 11).toString());

        log.d("dao_test_query", httpv2.request(path + "dao_test_query", "{\"applicationName\":\"name1\",\"applicationDesc\":\"desc1\"}", 11).toString());


    }

    //获取验证码图片   /test/image_code
    @GetMapping("image_code")
    public Object image_code(HttpServletResponse response, Session session) throws IOException {
        String code = Tools.getPictureCode2(response);
        log.d("code", code);
        session.put("code", code);
        log.d("code session.get ", session.getString("code"));
        return null;
    }

    //返回 byte数据  /test/get_byte
    @GetMapping("get_byte")
    public Object get_byte() throws IOException {
        byte[] bytes = new JSONResult().success("OK").toString().getBytes("UTF-8");
        log.d("bytes", bytes);
        return bytes;
    }

    //post请求  /test/post
    @PostMapping("post")
    public Object post(JSONResult result, String msg) {
        return result.success(msg);
    }

    //put请求  /test/put
    @PutMapping("put")
    public Object put(JSONResult result, String msg) {
        return result.success(msg);
    }

    //delete请求  /test/delete
    @DeleteMapping("delete")
    public Object delete(JSONResult result, String msg) {
        return result.success(msg);
    }

    //通用请求 get post put delete  /test/request"
    @RequestMapping("request")
    public Object request(JSONResult result, String msg) {
        return result.success(msg);
    }

    //测试 分页查询  /test/dao_test_query
    @RequestMapping("dao_test_query")
    public Object dao_test_query(ApplicationDao applicationDao, Application application, HttpServletRequest request, Map<String, Object> map) throws Exception {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }
        log.d("dao_test_query application", application);
        return applicationDao.queryPage(application, null, null, 1, 10, 0, 0);
    }

    //测试 数据库操作   /test/dao_test_save
    @Transaction //注解开启事务 关闭自动提交 当方法结束后 框架会自动提交
    @RequestMapping("dao_test_save")
    public Object dao_test_save(
            JSONResult result, //自动注入的响应体对象
            ApplicationDao applicationDao, //自动注入的dao对象
            Application application//根据请求参数 自动匹配的实体对象
    ) throws Exception {
        applicationDao.save(application);
        log.d("事务未提交", applicationDao.find(application));
        //applicationDao.openTransaction();//手动开启事务
        //applicationDao.commit();//手动提交事务
        //applicationDao.rollback();//手动回滚事务
        //applicationDao.endTransaction();//手动关闭事务

        //application.setApplicationDesc(Tools.getRandomString(12));
        //applicationDao.update(application);
        //applicationDao.delete(application);
        //applicationDao.find(application);
        log.d("dao_test_save result", result);
        log.d("dao_test_save application", application);
        return result.success("suc", applicationDao.query(application));
    }


}
