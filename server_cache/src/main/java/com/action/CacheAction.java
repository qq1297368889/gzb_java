package com.action;

import gzb.frame.annotation.*;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.object.ObjectCache;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

/// 写个小玩具
/// 性能报告: 同机器测试 大量请求下 平均响应延迟 50-60微秒  同机器测试redis 也是50-60微秒
/// 小玩具 但是 可以显示框架调度的优越性
@Controller
@RequestMapping("cache")
public class CacheAction {

    @EventLoop
    @PostMapping("/ping")
    public String ping() throws Exception {
        return "ping";
    }
    /// 状态码
    /// 1 调用成功
    /// 2 k为空
    /// 3 v为空
    /// 4 s为空
    /// 5 i对应实例不存在
    /// 6 get 调用时 key对应val 不存在
    @EventLoop
    @PostMapping("/set")
    public String set(String k, String v, Integer s, Integer i) throws Exception {
        if (k == null) {
            return "2";
        }
        if (v == null) {
            return "3";
        }
        if (s == null) {
            return "4";
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return "5";
        }
        gzbCache.set(k, v, s);
        return "1";
    }

    @EventLoop
    @PostMapping("/get")
    public String get(String k, Integer i) throws Exception {
        if (k == null) {
            return "2";
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return "5";
        }
        String str = gzbCache.get(k);
        if (str == null) {
            return "6";
        }
        return "1 " + str;
    }

    @EventLoop
    @PostMapping("/get/all")
    public String getAll(String[] k, Integer i) throws Exception {
        if (k == null) {
            return "2";
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return "5";
        }
        ObjectCache.Entity entity = ObjectCache.SB_CACHE0.get();
        int index0 = entity.open();
        try {
            StringBuilder sb = entity.get(index0);
            sb.append("1 ");
            for (String string : k) {
                String data = gzbCache.get(string);
                sb.append(data.length()).append(" ").append(data).append(" ");
                ///state size data size data......
            }
            return sb.toString();
        } finally {
            entity.close(index0);
        }
    }

    @EventLoop
    @PostMapping("/del")
    public String del(String[] k, Integer i) throws Exception {
        if (k == null) {
            return "2";
        }
        GzbCache gzbCache = CacheData.get(i);
        if (gzbCache == null) {
            return "5";
        }
        for (String string : k) {
            gzbCache.remove(string);
        }
        return "1";
    }
}
