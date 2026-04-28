package gzb.start;

import gzb.tools.Tools;
import gzb.tools.cache.TCP_SDK;
import gzb.tools.cache.entity.Message;
import gzb.tools.log.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class start_tcp_test {
    static AtomicLong qps_queue = new AtomicLong(0);
    static AtomicLong queue = new AtomicLong(0);
    static AtomicLong test_type = new AtomicLong(0);

    public static void main(String[] args) throws IOException {
        TCP_SDK cacheSDK = new TCP_SDK("127.0.0.1", 8081, 0,1);
        queue.set(20);
        Log.log.i(cacheSDK.del(0 + "-key"));
        Log.log.i(cacheSDK.put(0 + "-key", "123456", 100));
        Log.log.i(cacheSDK.get(0 + "-key"));
        Log.log.i(cacheSDK.produce("101010"));
        Message message = cacheSDK.consume(-1);
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
                    while (queue.get() == size) {
                        Tools.sleep(1);
                    }
                    try {
                        TCP_SDK cacheSDK = new TCP_SDK("127.0.0.1", 8081, 0,1);
                        while (true) {
                            for (int i = 0; i < 10000; i++) {
                                //cacheSDK.get(i + "-key");
                                //cacheSDK.ping();
                                if (test_type.get() == 0) {
                                    cacheSDK.ping();
                                    qps_queue.incrementAndGet();
                                } else if (test_type.get() == 1) {
                                    if (!cacheSDK.get(i + "-key").equals(i + "-val")) {
                                        throw new RuntimeException();
                                    }
                                    qps_queue.incrementAndGet();
                                } else if (test_type.get() == 2) {
                                    cacheSDK.produce(i+"-queue");
                                    qps_queue.incrementAndGet();
                                    Message message = cacheSDK.consume(-1);
                                    qps_queue.incrementAndGet();
                                    cacheSDK.confirm(message.id);
                                    qps_queue.incrementAndGet();
                                } else if (test_type.get() == 11) {
                                    cacheSDK.get(i + "-key",i +1 + "-key",i +2+ "-key",i+3 + "-key",i+4 + "-key",i +5+ "-key",i+6 + "-key",i +7+ "-key",i +8+ "-key",i +9+ "-key");
                                    qps_queue.addAndGet(1);
                                }
                            }
                            if (qps_queue.get() >= max_qps) {
                                break;
                            }
                        }
                        queue.incrementAndGet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }

        Tools.sleep(1000);
        qps_queue.set(0);
        queue.set(0);
        test_type.set(1);
        //test_type.set(0);
        start = System.currentTimeMillis();
        while (queue.get() != size) {
            Tools.sleep(1000);
            long end = System.currentTimeMillis();
            Log.log.i("qps", qps_queue.get() / ((end - start) / 1000));
        }
        Log.log.i("qps end request num", qps_queue.get());

    }
}
