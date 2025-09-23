package gzb.tools;

import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFactory {
    public static GzbCache gzbCache= Cache.gzbMap;
    public static Lock lock=new ReentrantLock();
    public static int defMM=0;//0不限时  大于0 过期会失效
    public static final Condition getCondition(String key) {
        return getCondition(key,defMM);
    }

    public static final Condition getCondition(String key, int mm) {
        String key2 =key+"_lock";
        String key3 =key+"_condition";
        Condition val = gzbCache.getObject(key3,null);
        if (val == null) {
            lock.lock();
            try {
                val = gzbCache.getObject(key3,null);
                if (val == null) {
                    Lock lock = new ReentrantLock();
                    val = lock.newCondition();
                    gzbCache.set(key3, val,mm);
                    gzbCache.set(key2, lock,mm);
                }
            }finally {
                lock.unlock();
            }
        }
        return val;
    }
    public static final Lock getLock(String key) {
        return getLock(key,defMM);
    }

    public static final Lock getLock(String key, int mm) {
        String key2 =key+"_lock";
        String key3 =key+"_condition";
        Lock val =gzbCache.getObject(key3,null);
        if (val == null) {
            lock.lock();
            try {
                val = gzbCache.getObject(key3,null);
                if (val == null) {
                    val = new ReentrantLock();
                    gzbCache.set(key2, val,mm);
                    Condition condition = val .newCondition();
                    gzbCache.set(key3, condition,mm);
                }
            }finally {
                lock.unlock();
            }
        }
        return val;
    }
    public static final void setLock(String key, Lock val,int mm) {
        String key2 =key+"_lock";
        String key3 =key+"_condition";
        lock.lock();
        try {
            gzbCache.set(key2, val,mm);
        }finally {
            lock.unlock();
        }
    }
    public static final Lock delLock(String key) {
        String key2 =key+"_lock";
        String key3 =key+"_condition";
        lock.lock();
        try {
           return (Lock)gzbCache.del(key2);
        }finally {
            lock.unlock();
        }
    }

}
