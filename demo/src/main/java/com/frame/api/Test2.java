package com.frame.api;

import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;

@Controller
@RequestMapping(value = "/test2")
public class Test2 {
    //test2/get1?msg=hello
    @Header(item = {@HeaderItem(key = "content-type", val = "text/html;charset=UTF-8")})
    @GetMapping("get1")
    public Object get1(String msg, GzbJson gzbJson) throws Exception {
        return gzbJson.success(msg);
    }


}
