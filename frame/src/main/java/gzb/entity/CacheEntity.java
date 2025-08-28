package gzb.entity;

import java.util.concurrent.locks.Lock;

public class CacheEntity {
    private Object val = null;
    private long useTime = 0;
    private Lock lock = null;

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "{" +
                "\"val:\"" + val + "\"" +
                ", \"useTime:\"" + useTime + "\"" +
                ", \"lock:\"" + lock + "\"" +
                '}';
    }
}
