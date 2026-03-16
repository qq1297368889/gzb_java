package gzb.start;

import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheRedis;
import gzb.tools.log.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ddos {
    // 等待put结束 才开始算qps  因为 put qps比较低 后边get比较高 会导致qps 一直抬升 寻找全局平衡  很久才能到达稳态
    //服务端线程数 6
    //客户端线程数 24
    //key为 key_ + 1-10000 随机数
    //val 为固定 60位
    //过期时间为随机 1-60秒  模拟真实情况 在压测过程中 部分逐渐过期
    //JDK 25(低版本 qps 会有一些波动不过差距不是很大)
    public static void main(String[] args) throws IOException {
        //main_gzb main_redis
        main_gzb(
                6,
                100,
                60);
        //main_redis(6,100,60);
    }
    public static void main_gzb(int thr_num,int num,int data_size) throws IOException {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger stop = new AtomicInteger(0);
        // tcp 地址 端口 数据库索引
        for (int i = 0; i < thr_num; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.log.i("start", Thread.currentThread().getName());
                        long start, end, ok = 0, no = 0, all = 0;
                        // tcp 地址 端口 数据库索引
                        CacheSDK cacheSDK = new CacheSDK("127.0.0.1", 8081, 0);
                        start = System.nanoTime();
                        for (int i = 0; i < 10000; i++) {
                            all++;
                            count.incrementAndGet();
                            cacheSDK.put("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(data_size),
                                    Tools.getRandomInt(60, 1));
                        }
                        end = System.nanoTime();
                        Log.log.i("put 耗时 微秒", (end - start) / 10000 / 1000);
                        stop.incrementAndGet();

                        long start0 = System.currentTimeMillis();
                        for (int i = 0; i < num; i++) {
                            start = System.nanoTime();
                            for (int i1 = 0; i1 < 10000; i1++) {
                                String data=cacheSDK.get("key_" + Tools.getRandomInt(10000, 1));
                                if (data== null) {
                                    no++;
                                } else {
                                    ok++;
                                }
                                all++;
                                count.incrementAndGet();
                            }
                            end = System.nanoTime();
                            Log.log.i("get 耗时 微秒", (end - start) / 10000 / 1000, "未命中", no, "命中", ok);
                        }
                        long end0 = System.currentTimeMillis();
                        Log.log.i("本线程 end qps", all / ((end0 - start0) / 1000));
                        cacheSDK.close();
                        stop.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }
        int sec = 0;
        //等待put结束
        while (stop.get()< thr_num){
            Tools.sleep(1000);
        }
        Tools.sleep(1000); //sleep 1000
        stop.set(0);
        count.set(0);
        while (stop.get() < thr_num) {
            Tools.sleep(1000);//误差几毫秒 问题不大
            sec++;
            Log.log.w("qps", count.get() / sec);
        }
        Log.log.w("end");

    }
    /*
    底层是这样的
    @Override
    public void set(String key, String val, int second) {
        if (key == null || val == null || key.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] valBytes = val.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            if (second > 0) {
                jedis.setex(keyBytes, second, valBytes);
            } else {
                jedis.set(keyBytes, valBytes);
            }
        } catch (Exception e) {
            log.e(e, "set error for key: " + key);
        }
    }

    @Override
    public String get(String key) {
        if (key == null || key.isEmpty()) return null;
        byte[] keyBytes = key.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(keyBytes);
            if (bytes != null) {
                return new String(bytes, UTF_8);
            }
            return null;
        } catch (Exception e) {
            log.e(e, "get error for key: " + key);
            return null;
        }
    }
    * */
    public static void main_redis(int thr_num,int num,int data_size) throws IOException {
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger stop = new AtomicInteger(0);
        GzbCache gzbCache = new GzbCacheRedis("cache1");
        // tcp 地址 端口 数据库索引
        for (int i = 0; i < thr_num; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.log.i("start", Thread.currentThread().getName());
                        long start, end, ok = 0, no = 0, all = 0;
                        start = System.nanoTime();
                        for (int i = 0; i < 10000; i++) {
                            all++;
                            count.incrementAndGet();
                            gzbCache.set("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(data_size),
                                    Tools.getRandomInt(60, 1));
                        }
                        end = System.nanoTime();
                        Log.log.i("put 耗时 微秒", (end - start) / 10000 / 1000);
                        stop.incrementAndGet();
                        long start0 = System.currentTimeMillis();
                        for (int i = 0; i < num; i++) {
                            start = System.nanoTime();
                            for (int i1 = 0; i1 < 10000; i1++) {
                                if (gzbCache.get("key_" + Tools.getRandomInt(10000, 1)) == null) {
                                    no++;
                                } else {
                                    ok++;
                                }
                                all++;
                                count.incrementAndGet();
                            }
                            end = System.nanoTime();
                            Log.log.i("get 耗时 微秒", (end - start) / 10000 / 1000, "未命中", no, "命中", ok);
                        }

                        long end0 = System.currentTimeMillis();
                        Log.log.i("本线程 end qps", all / ((end0 - start0) / 1000));
                        stop.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }

        int sec = 0;
        //等待put结束
        while (stop.get()< thr_num){
            Tools.sleep(1000);
        }
        Tools.sleep(1000); //sleep 1000
        stop.set(0);
        count.set(0);
        while (stop.get() < thr_num) {
            Tools.sleep(1000);//误差几毫秒 问题不大
            sec++;
            Log.log.w("qps", count.get() / sec);
        }
        Log.log.w("end");
    }

}
