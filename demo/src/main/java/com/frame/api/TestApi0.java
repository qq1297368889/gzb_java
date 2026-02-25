package com.frame.api;

import com.frame.dao.SysLogDao;
import com.frame.entity.SysLog;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("test/api0")
public class TestApi0 {
    //test/api0/get1?message=message001

    @EventLoop
    @GetMapping("get1")
    public String get1(String message){
        return "{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\""+message+"\"}";
    }
    @Resource
    SysLogDao sysLogDao;

    @GetMapping("get2")
    public Object get2(String message) throws Exception {
        return sysLogDao.find(new SysLog().setSysLogId(getRandomLong(20, 1)));
    }
/// test/api0/get3

    @GetMapping("get3")
    public Object get3(String message) throws Exception {
        List<SysLog> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(sysLogDao.find(new SysLog().setSysLogId(getRandomLong(20, 1))));
        }

        return list;
    }

    //最终生成 key  className.methodName[parar...]-key_val-page_val-uid_val-可选固定值_null
    @CacheRequest(value = {"key", "page", "uid", "可选固定值"}, second = 10)
    @GetMapping("cache/test")
    public Object cache_test(String key, int page, long uid) throws Exception {

        return null;
    }

    public static Random random = new Random(new Date().getTime() + 100);

    public static Long getRandomLong(long max, long min) {
        if (max == min) {
            return (long) max;
        }
        if (min > max) {
            long a = max;
            max = min;
            min = a;
        }

        return (long) (random.nextInt((int) (max - min + 1)) + min);
    }

}
