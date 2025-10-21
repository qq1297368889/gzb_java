package com.tfb01.controller;

import com.tfb01.dao.WorldDao;
import gzb.frame.annotation.*;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;

@Controller
@RequestMapping("/test")
public class TestApi {

    @GetMapping("test1")
    public String test1(String msg, GzbJson gzbJson, WorldDao worldDao) throws Exception {
        return gzbJson.success(msg, worldDao.findById(1, -1));
    }

}
