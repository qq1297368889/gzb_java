package gzb.tools;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class QueueTest2 {
    static AtomicInteger put_num=new AtomicInteger(0);
    static AtomicInteger get_num=new AtomicInteger(0);
    static Queue<Integer>queue = new Queue<>(100);
    public static void main(String[] args) {
        test_put(10,10000*3);
        test_get(10,10000*3);
        while(true){
            System.out.println("get_num"+get_num.get()+ "  put_num "+put_num.get());

            Tools.sleep(1000);
        }
    }
    public static void test_put(int num,int data_num){
        for (int i = 0; i < num; i++) {
            new Thread(()->{
                for (int i1 = 0; i1 < data_num; i1++) {
                    queue.add(i1);
                    put_num.incrementAndGet();
                }
            }).start();
        }
    }
    public static void test_get(int num,int data_num){
        for (int i = 0; i < num; i++) {
            new Thread(()->{
                for (int i1 = 0; i1 < data_num; i1++) {
                    try {
                        queue.read();
                        get_num.incrementAndGet();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}