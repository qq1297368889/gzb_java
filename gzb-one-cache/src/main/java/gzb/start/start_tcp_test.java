package gzb.start;

import gzb.tools.*;
import gzb.sdk.BaseSdk;
import gzb.sdk.CacheSdk;
import gzb.sdk.CacheSdkTcp;
import gzb.tools.log.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class start_tcp_test {
    static AtomicLong qps = new AtomicLong(0);
    static AtomicLong state = new AtomicLong(0);
    static AtomicLong test_type = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException, IOException {
        int pip=128;
        int io=1;
        int port=3081;
        int index=0;
        String host="127.0.0.1";
        Tools.sleep(1);
        Log.log.i("start");
         //port=3081;
        ///  CacheSdkHttp  CacheSdkTcp  tcp 3081   http 2081
        CacheSdk cacheSDK = new CacheSdkTcp(host, port, index,io);
        Log.log.i(cacheSDK.del(0 + "-key"));
        Log.log.i(cacheSDK.put(0 + "-key", "123456", 100));
        Log.log.i(cacheSDK.get(0 + "-key"));
        Log.log.i(cacheSDK.getAll(0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key",0 + "-key"));
        Log.log.i(cacheSDK.get(10,0 + "-key"));
        Log.log.i(cacheSDK.produce("101010"));
        BaseSdk.Message message = cacheSDK.consume(-1);
        Log.log.i(message.id,message.data);

        Log.log.i(cacheSDK.confirm(message.id));

        for (int i = 0; i < 10000; i++) {
            cacheSDK.put(i + "-key", i+"-val", 100);
        }


        int size = 24;
        int max_qps = 10000 * 1000;
        state.set(size);
        long start = System.currentTimeMillis();
        for (int n = 0; n < size; n++) {
            new Thread() {
                @Override
                public void run() {
                    while (state.get() == size) {
                        Tools.sleep(1);
                    }
                    try {
                        CacheSdk cacheSDK = new CacheSdkTcp(host, port, index,io);
                        while (true) {
                            for (int i = 0; i < 10000; i++) {
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
                                    BaseSdk.Message message = cacheSDK.consume(-1);
                                    qps.incrementAndGet();
                                    cacheSDK.confirm(message.id);
                                    qps.incrementAndGet();
                                } else if (test_type.get() == 11) {
                                    cacheSDK.get(i + "-key",i +1 + "-key",i +2+ "-key",i+3 + "-key",i+4 + "-key",i +5+ "-key",i+6 + "-key",i +7+ "-key",i +8+ "-key",i +9+ "-key");
                                    qps.addAndGet(10);
                                }else if (test_type.get() == 12) {
                                    qps.addAndGet(cacheSDK.get(pip,i + "-key").size());
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
        test_type.set(12);
        //test_type.set(0);
        start = System.currentTimeMillis();
        int max_sec=10;
        while (state.get() != size) {
            Tools.sleep(1000);
            long end = System.currentTimeMillis();
            long res=end - start;
            Log.log.i("sec",((end - start)/1000)+"/"+max_sec,"qps", qps.get() / ((end - start) / 1000));
            if (res > max_sec*1000){
                start = System.currentTimeMillis();
                qps.set(0);
            }
        }
        Log.log.i("qps end request num", qps.get());

    }
}
