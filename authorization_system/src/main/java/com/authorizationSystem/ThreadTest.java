package com.authorizationSystem;

import com.authorizationSystem.dao.ApplicationDao;
import gzb.frame.annotation.Service;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.GzbMap;
import gzb.tools.cache.Cache;
import gzb.tools.log.Log;

@Service
public class ThreadTest {

    //启动线程 num =1 启动一个线程    value=2000 每个线程 每2000毫秒调用一次方法 async 决定同步还是异步 这个参数好像有bug  记不清了  保持默认就行
    //函数内不要循环 因为本身就会循环调用 这种线程调用方式 主要是为了解决 动态编译类后的 线程持有旧对象问题 需要用的对象定义到参数 会自动传入
    @ThreadInterval(num = 0, value = 2000, async = false)
    public void test001(Log log, ApplicationDao application, GzbMap gzbMap) {
        int num = gzbMap.getInteger("num01", 0);
        log.d("test001", num,application);
        Cache.gzbMap.putIfAbsent("num01", num);
        gzbMap.put("num01", num + 1);
        log.d("test002   ", Cache.gzbMap.get("num01"),application);
        Cache.gzbMap.put("num01", Integer.parseInt(Cache.gzbMap.get("num01").toString())+1);

    }
}
