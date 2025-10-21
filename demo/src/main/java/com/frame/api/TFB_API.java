package com.frame.api;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;

@Controller
@RequestMapping("tfb")
public class TFB_API {
    @GetMapping("text")
    public String text() {
        return "Hello, World!";
    }

}
