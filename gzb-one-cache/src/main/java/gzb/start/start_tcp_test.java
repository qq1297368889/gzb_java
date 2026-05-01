package gzb.start;

import gzb.tools.Tools;
import gzb.tools.cache.CacheSdkTcp;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class start_tcp_test {
    static AtomicLong qps = new AtomicLong(0);
    static AtomicLong state = new AtomicLong(0);
    static AtomicLong test_type = new AtomicLong(0);

    public static void main(String[] args) throws IOException {
        Tools.sleep(1);
        Log.log.i("start");
        CacheSdkTcp cacheSDK = new CacheSdkTcp("127.0.0.1", 3081, 0,1);
        state.set(20);
        Log.log.i(cacheSDK.del(0 + "-key"));
        Log.log.i(cacheSDK.put(0 + "-key", "123456", 100));
        Log.log.i(cacheSDK.get(0 + "-key"));
        Log.log.i(cacheSDK.produce("101010"));
        CacheSdkTcp.Message message = cacheSDK.consume(-1);
        Log.log.i(Tools.toJson0(message));
        if (message == null) {
            throw new RuntimeException();
        }
        Log.log.i(cacheSDK.confirm(message.id));


        for (int i = 0; i < 10000; i++) {
            cacheSDK.put(i + "-key", i+"-val", 100);

        }


        int size = 40;
        int max_qps = 10000 * 1000;
        long start = System.currentTimeMillis();
        for (int n = 0; n < size; n++) {
            new Thread() {
                @Override
                public void run() {
                    while (state.get() == size) {
                        Tools.sleep(1);
                    }
                    try {
                        CacheSdkTcp cacheSDK = new CacheSdkTcp("127.0.0.1", 3081, 0,1);
                        while (true) {
                            for (int i = 0; i < 10000; i++) {
                                //cacheSDK.get(i + "-key");
                                //cacheSDK.ping();
                                if (test_type.get() == 0) {
                                    cacheSDK.ping();
                                    qps.incrementAndGet();
                                } else if (test_type.get() == 1) {
                                    if (!cacheSDK.get(i + "-key").equals(i + "-val")) {
                                        throw new RuntimeException();
                                    }
                                    qps.incrementAndGet();
                                } else if (test_type.get() == 2) {
                                    cacheSDK.produce(i+"-queue");
                                    qps.incrementAndGet();
                                    CacheSdkTcp.Message message = cacheSDK.consume(-1);
                                    qps.incrementAndGet();
                                    cacheSDK.confirm(message.id);
                                    qps.incrementAndGet();
                                } else if (test_type.get() == 11) {
                                    cacheSDK.get(i + "-key",i +1 + "-key",i +2+ "-key",i+3 + "-key",i+4 + "-key",i +5+ "-key",i+6 + "-key",i +7+ "-key",i +8+ "-key",i +9+ "-key");
                                    qps.addAndGet(1);
                                }
                            }
                            if (qps.get() >= max_qps) {
                                break;
                            }
                        }
                        state.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }

        Tools.sleep(1000);
        qps.set(0);
        state.set(0);
        test_type.set(1);
        //test_type.set(0);
        start = System.currentTimeMillis();
        while (state.get() != size) {
            Tools.sleep(1000);
            long end = System.currentTimeMillis();
            Log.log.i("qps", qps.get() / ((end - start) / 1000));
        }
        Log.log.i("qps end request num", qps.get());

    }
}
