package gzb.start;

import com.frame.dao.SysFileDao;
import com.frame.dao.impl.SysFileDaoImpl;
import com.frame.entity.SysFile;
import gzb.tools.Queue;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class test {
    public static void main(String[] args) throws Exception {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();
        Queue<String> queue0 = new Queue<String>();

        for (int n = 0; n < 10; n++) {
            Thread thread =new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    test(queue);
                }
            });
            thread.start();
        }
    }

    public static void test(Queue<String> queue) {
        int num=10000*5;
        int size = 0;
        long start = System.nanoTime();
        for (int i = 0; i <num; i++) {
            queue.add("test");
        }
        long end = System.nanoTime();
        System.out.println("add size " +size + " 总耗时 " + ((end - start)/1000/1000)+ "毫秒 平均耗时 " + ((end - start)/(num)) +"纳秒");
        start = System.nanoTime();
        for (int i = 0; i <num; i++) {
            size += queue.poll().length();
        }
        end = System.nanoTime();
        System.out.println("poll size " +size + " 总耗时 " + ((end - start)/1000/1000)+ "毫秒 平均耗时 " + ((end - start)/(num)) +"纳秒");


    }
    public static void test(ConcurrentLinkedQueue<String> queue) {
        int num=10000*5;
        int size = 0;
        long start = System.nanoTime();
        for (int i = 0; i <num; i++) {
            queue.add("test");
        }
        long end = System.nanoTime();
        System.out.println("size " +size + " 总耗时 " + ((end - start)/1000/1000)+ "毫秒 平均耗时 " + ((end - start)/(num)) +"纳秒");
        start = System.nanoTime();
        for (int i = 0; i <num; i++) {
            size += queue.poll().length();
        }
        end = System.nanoTime();
        System.out.println("size " +size + " 总耗时 " + ((end - start)/1000/1000)+ "毫秒 平均耗时 " + ((end - start)/(num)) +"纳秒");


    }
}

