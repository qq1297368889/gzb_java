package com.frame.api.tfb;

import com.frame.api.tfb.entity.User;
import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;

@Controller
@RequestMapping("/tfb/json")
public class JsonApi {
    @GetMapping
    public Object test() {
        return new User(1, "user");
    }
}
