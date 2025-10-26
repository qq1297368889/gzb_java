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

public class ThreadPool {

    public static Map<String, List<Thread>> serviceThread = new ConcurrentHashMap<>();

    public static void startService(int threadNum, String name, Runnable runnable) {
        for (int i = 0; i < threadNum; i++) {
            startService(runnable, name + "-" + i);
        }
    }

    public static void startService(Runnable runnable) {
        startService(runnable,null);
    }
    public static void startService(Runnable runnable, String name) {
        if (name == null) {
            name = Tools.getUUID()+"-" + OnlyId.getDistributed();
        }
        Thread thread = new Thread(runnable, name);
        List<Thread> list = serviceThread.get(name);
        if (list == null) {
            list = new ArrayList<>();
            serviceThread.put(name, list);
        }
        list.add(thread);
        thread.start();
    }

    public Log log = Log.log;

    public static List<Thread> readService(String name) {
        return serviceThread.get(name);
    }

    private final int await_sec = 15;
    public  LinkedBlockingQueue<Runnable> runnableQueue = null;
    public ThreadPool() {
        this(Config.cpu, Config.cpu * 100);
    }
    public ThreadPool(int THREAD_NUM, int MAX_QUEUE_SIZE) {
        this.THREAD_NUM = THREAD_NUM;
        this.MAX_QUEUE_SIZE = MAX_QUEUE_SIZE;
        runnableQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
        threads = new Thread[THREAD_NUM];
        for (int i = 0; i < THREAD_NUM; i++) {
            startWork(i);
        }
        log.t("线程池启动成功",THREAD_NUM);
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
