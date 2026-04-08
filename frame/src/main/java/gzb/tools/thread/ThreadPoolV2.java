package gzb.tools.thread;

import gzb.tools.Config;
import gzb.tools.OSUtils;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolV2 {

    public Log log = Log.log;

    private final int await_sec = 15;
    public  LinkedBlockingQueue<Runnable> runnableQueue = null;
    public ThreadPoolV2() {
        this(Config.cpu, Config.cpu * 100);
    }
    public ThreadPoolV2(int THREAD_NUM, int MAX_QUEUE_SIZE) {
        this.THREAD_NUM = THREAD_NUM;
        this.MAX_QUEUE_SIZE = MAX_QUEUE_SIZE;
        runnableQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
        threads = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            startWork(i);
        }
    }

    public int THREAD_NUM = 1;
    public int MAX_QUEUE_SIZE = 1;
    public Thread[] threads;


    private void startWork(int index) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        runnableQueue.take().run();
                    } catch (InterruptedException e) {
                        log.d("线程被中断", Thread.currentThread().getName(), e);
                        break;
                    } catch (Exception e) {
                        log.e("线程运行出错", Thread.currentThread().getName(), e);
                    }
                }
            }
        };
        thread.setName("thread-pool-v2");
        thread.start();
        threads[index] = thread;
    }


    public boolean execute(Runnable runnable) {
        return runnableQueue.offer(runnable);
    }

    public int size() {
        return runnableQueue.size();
    }


}
