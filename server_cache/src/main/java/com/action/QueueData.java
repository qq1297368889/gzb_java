package com.action;

import gzb.exception.GzbException0;
import gzb.tools.Config;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.cache.GzbQueue;
import gzb.tools.cache.GzbQueueImpl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueueData {
    public static int db_num = 0;
    public static GzbQueue[] gzbQueues;
    public static Lock lock = new ReentrantLock();

    public static void create(int num) {
        if (gzbQueues != null) {
            throw new GzbException0("请勿重复初始化缓存");
        }
        db_num = num - 1;
        gzbQueues = new GzbQueue[num];
    }

    public static GzbQueue get(int index) {
        if (index < 0 || index > db_num) {
            return null;
        }
        if (gzbQueues[index] == null) {
            lock.lock();
            try {
                if (gzbQueues[index] == null) {
                    gzbQueues[index] = new GzbQueueImpl(Config.thisPath() + "/" + index + ".cache");
                }
            } finally {
                lock.unlock();
            }
        }
        return gzbQueues[index];
    }
}
