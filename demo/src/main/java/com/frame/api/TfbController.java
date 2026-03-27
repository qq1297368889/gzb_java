package com.frame.api;

import gzb.frame.annotation.*;

@Controller
@RequestMapping(value = "/") // TFB 通常要求根路径或指定路径
@Header(item = {@HeaderItem(key = "Content-Type", val = "application/json")})
public class TfbController {

    @EventLoop
    @GetMapping("json")
    public Object json() {
        Message msg1 = new Message();
        msg1.message="Hello, World!";
        return msg1;
    }
    @EventLoop
    @GetMapping("json2")
    public Object json2(String message) {
        Message msg1 = new Message();
        msg1.message=message;
        Message msg2 = new Message();
        msg2.message=message;
        Message msg3 = new Message();
        msg3.message=message;
        Message msg4= new Message();
        msg4.message=message;
        msg1.data=msg2;
        msg2.data=msg3;
        msg3.data=msg4;
        msg4.data = "end";
        return msg1;
    }
    @EventLoop
    @GetMapping("plaintext")
    @Header(item = {@HeaderItem(key = "Content-Type", val = "text/plain")})
    public Object plaintext() {
        return "Hello, World!";
    }
}
