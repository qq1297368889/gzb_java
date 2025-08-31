package gzb.tools.cache;

import gzb.tools.DateTime;
import gzb.tools.Queue;
import gzb.entity.CacheEntity;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.thread.ThreadPool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GzbCacheMap implements GzbCache {
    public Map<String, CacheEntity> GzbCacheMap_map = new ConcurrentHashMap<>();
    public Lock GzbCacheMap_lock = new ReentrantLock();
    public static Log Log = new LogImpl(GzbCacheMap.class);

    public GzbCacheMap() {
        ThreadPool.pool.startThread(1, "GzbCacheMap", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        GzbCacheMap_lock.lock();
                        try {
                            int TimeStamp = new DateTime().toStampInt();
                            for (Iterator<Map.Entry<String, CacheEntity>> it = GzbCacheMap_map.entrySet().iterator(); it.hasNext(); ) {
                                Map.Entry<String, CacheEntity> en = it.next();
                                if (en != null && en.getValue().getUseTime() + 0 > 0 && en.getValue().getUseTime() + 0 < (long) TimeStamp) {
                                    Log.i("缓存删除：" + en.getKey() + ",this:" + TimeStamp + ",use:" + en.getValue().getUseTime());
                                    it.remove();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            GzbCacheMap_lock.unlock();
                            Thread.sleep(1000 * 10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public int getIncr(String key) {
        CacheEntity ce = GzbCacheMap_map.get(key);
        if (ce == null) {
            GzbCacheMap_lock.lock();
            try {
                ce = GzbCacheMap_map.get(key);
                if (ce == null) {
                    ce = new CacheEntity();
                    ce.setVal("0");
                    ce.setUseTime(0);
                    ce.setLock(new ReentrantLock());
                    GzbCacheMap_map.put(key, ce);
                }
            } catch (Exception e) {
                Log.e(e);
            } finally {
                GzbCacheMap_lock.unlock();
            }
        }
        if (ce.getUseTime() + 0 > 0 && ce.getUseTime() + 0 < (long) new DateTime().toStampInt()) {
            GzbCacheMap_map.remove(key);
            return getIncr(key);
        }
        int id = 0;
        ce.getLock().lock();
        try {
            id = Integer.valueOf(ce.getVal().toString()) + 1;
            ce.setVal(String.valueOf(id));
        } catch (Exception e) {
            Log.e(e);
            id = 0;
            ce.setVal(String.valueOf(id));
        } finally {
            ce.getLock().unlock();
        }
        return id;
    }

    @Override
    public String get(String key) {
        return get(key, 0);
    }

    @Override
    public String get(String key, int mm) {
        Object obj = getObject(key, mm);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public <T> T getObject(String key) {
        return getObject(key, 0);
    }

    @Override
    public <T> T getObject(String key, int mm) {
        CacheEntity ce = GzbCacheMap_map.get(key);
        if (ce == null) {
            return null;
        }
        if (ce.getUseTime() > 0 && ce.getUseTime() < (long) new DateTime().toStampInt()) {
            GzbCacheMap_map.remove(key);
            return null;
        }
        if (mm > 0) {
            ce.getLock().lock();
            try {
                ce.setUseTime(new DateTime().toStampInt() + mm);
            } catch (Exception e) {
                Log.e(e);
            } finally {
                ce.getLock().unlock();
            }
        }
        Object obj = ce.getVal();
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    @Override
    public void set(String key, String val) {
        setObject(key, val);
    }

    @Override
    public void set(String key, String val, int mm) {
        setObject(key, val, mm);
    }

    @Override
    public void setObject(String key, Object val) {
        setObject(key, val, 0);
    }

    @Override
    public void setObject(String key, Object val, int mm) {
        CacheEntity ce = GzbCacheMap_map.get(key);
        int useMM = mm == 0 ? mm : new DateTime().toStampInt() + mm;
        if (ce == null) {
            GzbCacheMap_lock.lock();
            try {
                ce = GzbCacheMap_map.get(key);
                if (ce == null) {
                    ce = new CacheEntity();
                    ce.setVal(val);
                    ce.setUseTime(useMM);
                    ce.setLock(new ReentrantLock());
                    GzbCacheMap_map.put(key, ce);
                }
            } catch (Exception e) {
                Log.e(e);
            } finally {
                GzbCacheMap_lock.unlock();
            }
        }
        ce.getLock().lock();
        try {
            ce.setVal(val);
            ce.setUseTime(useMM);
        } catch (Exception e) {
            Log.e(e);
        } finally {
            ce.getLock().unlock();
        }
    }

    @Override
    public String del(String key) {
        Object obj = delObject(key);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override
    public <T> T delObject(String key) {
        CacheEntity ce = GzbCacheMap_map.get(key);
        if (ce == null) {
            return null;
        }
        ce.getLock().lock();
        try {
            Object obj = GzbCacheMap_map.remove(key).getVal();
            if (obj == null) {
                return null;
            }
            return (T) obj;
        } catch (Exception e) {
            Log.e(e);
        } finally {
            ce.getLock().unlock();
        }
        return null;
    }

}
