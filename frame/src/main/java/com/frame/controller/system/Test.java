package com.frame.controller.system;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.Limitation;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.Config;
import gzb.tools.JSONResult;
import gzb.tools.Tools;
import gzb.tools.img.PicUtils;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/test", header = "content-type:application/json;charset=UTF-8")
public class Test {

    /// http://127.0.0.1:2080/test/test1/?name=lisi
    @GetMapping(value = "test1")
    public String hello(String name) {
  /*      StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            System.out.println(stackTraceElement.getClassName());
        }
        System.out.println(stackTrace.length);*/
        return "{\"code\":\"1\"\"name\":\""+name+"\"}";
    }
    @GetMapping(value = "image/code")
    public Object image_code(Session session, HttpServletResponse response) {
        session.put(Config.get("key.system.login.image.code"), "123456");
        return null;
    }
}
