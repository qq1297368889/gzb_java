package com.authorizationSystem.api;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/test/", header = "content-type:application/json;charset=UTF-8")
public class QPSAction {

    /// http://127.0.0.1:2081/test/test1/?name=lisi
    @GetMapping(value = "test1")
    public String hello(String name) throws Exception {
        return "{\"code\":\"1\"\"name3332\":\""+name+"\"}";
    }
}
