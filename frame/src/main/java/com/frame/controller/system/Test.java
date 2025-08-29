package com.frame.controller.system;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.Config;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/test", header = "content-type:application/json;charset=UTF-8")
public class Test {
    /// http://127.0.0.1:2080/test/test1/?name=lisi
    @GetMapping(value = "test1")
    public String hello(String name) throws Exception {
        return "{\"code\":\"1\"\"name3332\":\""+name+"\"}";
    }
}
