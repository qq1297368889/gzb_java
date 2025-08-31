package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GzbCacheFile implements GzbCache {
    static Log log = new LogImpl(GzbCacheFile.class);
    Lock lock = new ReentrantLock();
    File file;
    File fileQueueWait;
    File fileQueueFail;

    public static void main(String[] args) throws InterruptedException {
        GzbCacheFile gzbCacheFile = new GzbCacheFile(Config.thisPath());
        gzbCacheFile.set("key1","val1",3);
        log.i("key2 1 ",gzbCacheFile.getIncr("key2"));
        log.i("key1 1 ",gzbCacheFile.get("key1"));
        Thread.sleep(3000);
        log.i("key1 2 ",gzbCacheFile.get("key1"));
        Thread.sleep(3000);
        log.i("key1 3 ",gzbCacheFile.get("key1"));
        log.i("key2 2 ",gzbCacheFile.getIncr("key2"));
    }
    public GzbCacheFile(String dirName) {
        File file0 = new File(dirName);
        log.i(file0.getAbsoluteFile());
        file0.mkdirs();

        file = new File(file0.getAbsoluteFile() + "/cache");
        file.mkdirs();

        fileQueueWait=new File(file0.getAbsoluteFile() + "/queue_wait");
        fileQueueWait.mkdirs();

        fileQueueFail=new File(file0.getAbsoluteFile() + "/queue_fail");
        fileQueueFail.mkdirs();

    }

    @Override
    public int getIncr(String key) {
        lock.lock();
        try {
            String md5 = Tools.toMd5(key.getBytes());
            File file1 = new File(file.getAbsoluteFile() + "/" + md5);
            int res=1;
            if (file1.exists()) {
                String data = Tools.fileReadString(file1);
                 res = Integer.parseInt(data);
            }
            if (res == Integer.MAX_VALUE) {
                res = Integer.MIN_VALUE;
            }
            Tools.fileSaveString(file1, String.valueOf(res + 1), false);
            return res;
        } catch (Exception e) {
            log.e(e);
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get(String key) {
        return get(key, 0);
    }

    @Override
    public String get(String key, int mm) {
        lock.lock();
        try {
            String md5 = Tools.toMd5(key.getBytes());
            File file1 = new File(file.getAbsoluteFile() + "/" + md5);
            if (!file1.exists()) {
                return null;
            }
            File file2 = new File(file.getAbsoluteFile() + "/" + md5 + ".end");
            if (file2.exists()) {
                String end = Tools.fileReadString(file2);
                if (end.length() != 10) {
                    file1.delete();
                    return null;
                }

                log.i("get 1 ",Integer.parseInt(end));
                log.i("get 2 ",new DateTime().toStampInt());
                if (new DateTime().toStampInt() > Integer.parseInt(end)) {
                    file1.delete();
                    file2.delete();
                    return null;
                }
            }
            if (mm > 0) {
                Tools.fileSaveString(file2, (new DateTime().toStampInt() + mm) + "", false);
            }
            return Tools.fileReadString(file1) ;
        } catch (Exception e) {
            log.e(e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> T getObject(String key) {
        return getObject(key, 0);
    }

    @Override
    public <T> T getObject(String key, int mm) {
        lock.lock();
        try {
            String md5 = Tools.toMd5(key.getBytes());
            File file1 = new File(file.getAbsoluteFile() + "/" + md5);
            if (!file1.exists()) {
                return null;
            }
            File file2 = new File(file.getAbsoluteFile() + "/" + md5 + ".end");
            if (file2.exists()) {
                String end = Tools.fileReadString(file2);
                if (end.length() != 10) {
                    file1.delete();
                    return null;
                }
                if (Integer.parseInt(end) < new DateTime().toStampInt()) {
                    file1.delete();
                    file2.delete();
                    return null;
                }
            }
            if (mm > 0) {
                Tools.fileSaveString(file2, (new DateTime().toStampInt() + mm) + "", false);
            }
            return Tools.objectRestore(Tools.fileReadByte(file1));
        } catch (Exception e) {
            log.e(e);
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(String key, String val) {
        set(key, val, 0);
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
        lock.lock();
        try {
            String md5 = Tools.toMd5(key.getBytes());
            File file2 = new File(file.getAbsoluteFile() + "/" + md5 + ".end");
            File file1 = new File(file.getAbsoluteFile() + "/" + md5);
            if (val == null) {
                if (file1.exists()) {
                    file1.delete();
                    file2.delete();
                }
            } else {
                Tools.fileSaveString(file1, val.toString(), false);
            }
            if (mm > 0) {
                Tools.fileSaveString(file2, (new DateTime().toStampInt() + mm) + "", false);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String del(String key) {
        String val = get(key);
        setObject(key, val, 0);
        return val;
    }

    @Override
    public <T> T delObject(String key) {
        T val = getObject(key);
        setObject(key, val, 0);
        return val;
    }

}
