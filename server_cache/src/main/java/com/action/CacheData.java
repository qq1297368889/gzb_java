package com.action;

import gzb.exception.GzbException0;
import gzb.tools.Config;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheData {
    public static int db_num = 0;
    public static GzbCache[] gzbCaches;
    public static Lock lock = new ReentrantLock();

    public static void create(int num) {
        if (gzbCaches != null) {
            throw new GzbException0("请勿重复初始化缓存");
        }
        db_num = num - 1;
        gzbCaches = new GzbCacheMap[num];
    }

    public static GzbCache get(int index) {
        if (index < 0 || index > db_num) {
            return null;
        }
        if (gzbCaches[index] == null) {
            lock.lock();
            try {
                if (gzbCaches[index] == null) {
                    gzbCaches[index] = new GzbCacheMap(Config.thisPath() + "/" + index + ".cache", 60 * 10);
                }
            } finally {
                lock.unlock();
            }
        }
        return gzbCaches[index];
    }
}
