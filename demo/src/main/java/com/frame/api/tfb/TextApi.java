package com.frame.api.tfb;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;

@Controller
@RequestMapping("/tfb/text")
public class TextApi {
    @GetMapping
    public String test() {
        return "Hello, Word!";
    }
}
