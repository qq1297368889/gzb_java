package gzb.start;

import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheRedis;
import gzb.tools.log.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ddos {
    ///  m*l*g*b***** 还是没redis快
    /// 哪怕响应速度更快 因为 差距不在我的算法效率 在c的底层优势 系统调度 io效率上
    /// 目前没办法 除非我绕过jvm 就这样吧。。。。。。
    public static void main0(String[] args) throws IOException {
        int thr_num = 10;
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger stop = new AtomicInteger(0);
        GzbCache gzbCache = new GzbCacheRedis("cache1");
        for (int i = 0; i < 10000; i++) {
            gzbCache.set("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(2048), Tools.getRandomInt(20, 1));
        }

        // tcp 地址 端口 数据库索引
        for (int i = 0; i < thr_num; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.log.i("start", Thread.currentThread().getName());
                        long start, end, ok = 0, no = 0, all = 0;
                        // tcp 地址 端口 数据库索引
                        start = System.nanoTime();
                        for (int i = 0; i < 10000; i++) {
                            all++;
                            count.incrementAndGet();
                            gzbCache.set("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(12),
                                    Tools.getRandomInt(40, 1));
                        }
                        long start0 = System.currentTimeMillis();
                        end = System.nanoTime();


                        Log.log.i("put 耗时 微秒", (end - start) / 10000 / 1000);
                        for (int i = 0; i < 20; i++) {
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
                        Log.log.i("本线程总qps", all / ((end0 - start0) / 1000));
                        stop.incrementAndGet();
                    } catch (Exception e) {

                    }

                }
            }.start();
        }
        int sec = 0;
        while (stop.get() < thr_num) {
            Tools.sleep(1000);//误差几毫秒 问题不大
            sec++;
            Log.log.w("qps", count.get() / sec);
        }

    }

    public static void main(String[] args) throws IOException {
        int thr_num = 10;
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger stop = new AtomicInteger(0);
        CacheSDK cacheSDK = new CacheSDK("127.0.0.1", 6666, 0);
        for (int i = 0; i < 10000; i++) {
            cacheSDK.put("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(2048), Tools.getRandomInt(20, 1));
        }

        // tcp 地址 端口 数据库索引
        for (int i = 0; i < thr_num; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        Log.log.i("start", Thread.currentThread().getName());
                        long start, end, ok = 0, no = 0, all = 0;
                        // tcp 地址 端口 数据库索引
                        CacheSDK cacheSDK = new CacheSDK("127.0.0.1", 6666, 0);
                        start = System.nanoTime();
                        for (int i = 0; i < 10000; i++) {
                            all++;
                            count.incrementAndGet();
                            cacheSDK.put("key_" + (i + 1), "val_" + (i + 1) + Tools.getRandomString(12),
                                    Tools.getRandomInt(40, 1));
                        }
                        end = System.nanoTime();
                        Log.log.i("put 耗时 微秒", (end - start) / 10000 / 1000);


                        long start0 = System.currentTimeMillis();
                        for (int i = 0; i < 20; i++) {
                            start = System.nanoTime();
                            for (int i1 = 0; i1 < 10000; i1++) {
                                if (cacheSDK.get("key_" + Tools.getRandomInt(10000, 1)) == null) {
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
                        Log.log.i("本线程总qps", all / ((end0 - start0) / 1000));
                        cacheSDK.close();
                        stop.incrementAndGet();
                    } catch (Exception e) {

                    }

                }
            }.start();
        }
        int sec = 0;
        while (stop.get() < thr_num) {
            Tools.sleep(1000);//误差几毫秒 问题不大
            sec++;
            Log.log.w("qps", count.get() / sec);
        }

    }
}
