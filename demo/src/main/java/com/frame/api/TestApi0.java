package com.frame.api;

import com.frame.dao.SysFileDao;
import com.frame.dao.SysLogDao;
import com.frame.entity.SysFile;
import com.frame.entity.SysLog;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Response;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("test/api0")
public class TestApi0 {

    ///  127.0.0.1:2080/test/api0/test1
    @EventLoop
    @ManualRespond
    @GetMapping("test1")
    public void test1(SysFileDao sysFileDao, Response response,GzbJson gzbJson) throws Exception {
        SysFile sysFile = new SysFile();
        sysFileDao.saveAsync(sysFile.setSysFileMd5(Tools.getRandomString(32)),()->{
            response.sendAndFlush(gzbJson.fail("on",sysFile));
        },()->{
            response.sendAndFlush(gzbJson.success("ok",sysFile));

        });
    }

    @GetMapping("test2")
    public String test2(SysFileDao sysFileDao, GzbJson gzbJson) throws Exception {
        SysFile sysFile = new SysFile();
        sysFileDao.save(sysFile.setSysFileMd5(Tools.getRandomString(32)));
        return (gzbJson.success("ok",sysFile));
    }

    //test/api0/get1?message=message001
    public static final byte[]BYTES="Hello, World!".getBytes(Config.encoding);

    @EventLoop
    @GetMapping("get0")
    public byte[] get0(){
        return BYTES;
    }
    /// 前端请求的 body 直接是 {"sysUsersAcc:"xxx"}  sysUsers的参数 sysUsersAcc 能接受到  而定义的 string sysUsersAcc 也能接受到
    @EventLoop
    @GetMapping("get1")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public String get1(String message){
        return "{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\""+message+"\"}";
    }
    @Resource
    SysLogDao sysLogDao;
    /// value={xx,xx} xx对应请求参数  最终会生成 key 两次请求 同key 将会命中缓存(前提未过期) second 缓存时间 单位秒
    //@CacheRequest(value={"p1","p2","xxx"},second=10)
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
