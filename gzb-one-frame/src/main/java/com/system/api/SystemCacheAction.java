package com.system.api;

import gzb.frame.annotation.EventLoop;
import gzb.frame.annotation.RequestMapping;
import com.system.tools.CacheData;
import com.system.tools.ResponseData;
import gzb.tools.cache.GzbCache;

/// 写个小玩具
/// 性能报告: 同机器测试 大量请求下 平均响应延迟 50-60微秒  同机器测试redis 也是50-60微秒

public class SystemCacheAction {


    @EventLoop
    @RequestMapping("/ping")
    public String ping(long sid) throws Exception {
        return ResponseData.send(sid, 1);
    }

    /// 状态码
    /// 1 调用成功
    /// 2 k为空
    /// 3 v为空
    /// 4 s为空
    /// 5 i对应实例不存在
    /// 6 get 调用时 key对应val 不存在
    @EventLoop
    @RequestMapping("/set")
    public String set(String k, String v, Integer s, Integer i, long sid) throws Exception {
        if (k == null) {
            return ResponseData.send(sid, 2);
        }
        if (v == null) {
            return ResponseData.send(sid, 3);
        }
        if (s == null) {
            return ResponseData.send(sid, 4);
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return ResponseData.send(sid, 5);
        }
        gzbCache.set(k, v, s);
        return ResponseData.send(sid, 1);
    }

    @EventLoop
    @RequestMapping("/get")
    public String get(String k, Integer i, long sid) throws Exception {
        if (k == null) {
            return ResponseData.send(sid, 2);
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return ResponseData.send(sid, 5);
        }
        String str = gzbCache.get(k);
        if (str == null) {
            return ResponseData.send(sid, 6);
        }
        return ResponseData.send(sid, 1, str);
    }

    @EventLoop
    @RequestMapping("/get/all")
    public String getAll(String[] k, Integer i, long sid) throws Exception {
        if (k == null) {
            return ResponseData.send(sid, 2);
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return ResponseData.send(sid, 5);
        }
        String[]arr=new String[k.length];
        for (int i1 = 0; i1 < k.length; i1++) {
            arr[i1]=gzbCache.get(k[i1]);
        }
        return ResponseData.send(sid, 1, (Object[]) arr);
    }
    @EventLoop
    @RequestMapping("/del")
    public String del(String[] k, Integer i, long sid) throws Exception {
        if (k == null) {
            return ResponseData.send(sid, 2);
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return ResponseData.send(sid, 5);
        }
        for (String string : k) {
            gzbCache.remove(string);
        }
        return ResponseData.send(sid, 1);
    }
}
