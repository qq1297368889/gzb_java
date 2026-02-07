package com.frame.api;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.EventLoop;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.json.GzbJson;

@Controller
@RequestMapping("test/api0")
public class TestApi0 {
    //test/api0/get1?message=message001
    @EventLoop
    @GetMapping("get1")
    public String get1(String message, GzbJson gzbJson){
        return gzbJson.success(message);//等同于 但是是模板标准化 "{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\""+message+"\"}";
    }
}
