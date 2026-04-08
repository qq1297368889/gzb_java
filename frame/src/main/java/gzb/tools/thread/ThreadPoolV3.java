package gzb.tools.thread;

import gzb.frame.language.Template;
import gzb.tools.*;
import gzb.tools.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class ThreadPoolV3 {

    public Log log = Log.log;
    private boolean AUTO_MATIC = true;
    private double CPU_LOAD = 0.0;
    private final int await_sec = 3;
    public LinkedBlockingQueue<Runnable> runnableQueue = null;
    AtomicInteger thrNum = new AtomicInteger(0);
    public int THREAD_MIN_NUM = 10;
    public int THREAD_MAX_NUM = Config.cpu * 500;
    double MAX_CPU_JVM_LOAD = 85.0;
    AtomicInteger stopNum = new AtomicInteger(0);

    /**
     * 创建线程池
     * 队列最大堆积数量 默认为 cpu核心数*10000
     * 最小线程数量 默认为 cpu核心数*2
     * 最大线程数量 默认为 cpu核心数*600
     * jvm占用cpu上限 默认为 85.0
     * 是否自动扩容，CPU密集型不需要扩容 只要设置最小线程数为cpu核心数*0.85 即可
     */
    public ThreadPoolV3() {
        this(85.0);
    }

    public ThreadPoolV3(double MAX_CPU_JVM_LOAD) {
        this(Config.cpu * 1000, Config.cpu, Config.cpu * 500, MAX_CPU_JVM_LOAD, true);
    }

    /**
     * 创建线程池
     *
     * @param MAX_QUEUE_SIZE   队列最大堆积数量
     * @param THREAD_MIN_NUM   最小线程数量
     * @param THREAD_MAX_NUM   最大线程数量
     * @param MAX_CPU_JVM_LOAD jvm占用cpu上限 百分比 比如 85.00
     * @param AUTO_MATIC       是否自动扩容，CPU密集型不需要扩容 只要设置最小线程数为cpu核心数*0.85 即可
     */
    public ThreadPoolV3(int MAX_QUEUE_SIZE, int THREAD_MIN_NUM, int THREAD_MAX_NUM, double MAX_CPU_JVM_LOAD, boolean AUTO_MATIC) {
        this.THREAD_MIN_NUM = THREAD_MIN_NUM;
        this.THREAD_MAX_NUM = THREAD_MAX_NUM;
        this.MAX_CPU_JVM_LOAD = MAX_CPU_JVM_LOAD;
        this.AUTO_MATIC = AUTO_MATIC;
        runnableQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
        init();
    }

    private void init() {
        for (int i = 0; i < THREAD_MIN_NUM; i++) {
            startWork();
        }
        autoStartThread();
    }

    private void autoStartThread() {
        for (int i = 0; i < THREAD_MIN_NUM; i++) {
            startWork();
        }
        if (!AUTO_MATIC) {
            log.d(Template.THIS_LANGUAGE[51]);
            return;
        }
        ThreadPoolV3 threadPoolV3 =this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (stopNum.get()<0) {
                        stopNum.set(0);//防止扣除到小于-21亿
                    }
                    CPU_LOAD = OSUtils.getSystemCpuLoadPercentage();
                    if (thrNum.get() > THREAD_MAX_NUM) {
                        continue;
                    }
                    int workNum = runnableQueue.size();
                    int threadNum = thrNum.get();
                    log.t( Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get(), Template.THIS_LANGUAGE[54],CPU_LOAD,threadPoolV3.toString());
                    //积压小于线程数 不扩张
                    if (workNum*1.5 < threadNum) {
                        log.t(Template.THIS_LANGUAGE[55], Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get());
                        //目前 积压数量小于线程数量 并且线程数量大于cpu*2可以考虑缩容
                        if (threadNum > Config.cpu * 2) {
                            stopNum.set(1);
                            log.t(Template.THIS_LANGUAGE[56], 1,thrNum.get(),runnableQueue.size());
                            Tools.sleep(1000);
                            continue;
                        }
                        Tools.sleep(1000);
                        continue;
                    }
                    double cpu_1 = OSUtils.getSystemCpuLoadPercentage();//获取cpu变化
                    if (workNum < threadNum*2) {
                        continue;
                    }
                    for (int i = 0; i < Config.cpu; i++) {
                        startWork();//启动一个线程
                        log.t(Template.THIS_LANGUAGE[57], Template.THIS_LANGUAGE[54],CPU_LOAD,Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get());
                    }
                    double cpu_2 = OSUtils.getSystemCpuLoadPercentage();//获取cpu变化

                    int workNum2 = runnableQueue.size();
                    int threadNum2 = thrNum.get();
                    //显著停止恶化 不扩张
                    if (workNum2 < workNum * 0.8) {
                        log.t(Template.THIS_LANGUAGE[58], Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get());
                        continue;
                    }
                    //积压小于线程数 不扩张
                    if (workNum2 < threadNum2) {
                        log.t(Template.THIS_LANGUAGE[59], Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get());
                        continue;
                    }
                    double k01 = (cpu_2 - cpu_1) / 5; //获取平均负载
                    if (k01 < cpu_1 / (threadNum2 - 5)) {
                        k01 = cpu_1 / (threadNum2 - 5);
                    }
                    if (k01 < 1) {
                        k01 = 1;
                    }
                    //根据平均负载 启动新线程 激进 但是问题不大 因为是给io线程用的
                    while (cpu_2 < MAX_CPU_JVM_LOAD) {
                        startWork();//启动一个线程
                        log.t(Template.THIS_LANGUAGE[57],Template.THIS_LANGUAGE[60], CPU_LOAD, Template.THIS_LANGUAGE[52], runnableQueue.size(), Template.THIS_LANGUAGE[53], thrNum.get()
                                , Template.THIS_LANGUAGE[61], k01);
                        cpu_2 += k01;
                    }
                }
            }
        };
        thread.setName("thread-pool-main");
        thread.start();
    }

    Lock lock = LockFactory.getLock("thread-pool-startWork-exit");
    private void startWork() {
        if (thrNum.get() > THREAD_MAX_NUM) {
            return;
        }
        if (thrNum.get() > THREAD_MIN_NUM && runnableQueue.size() < thrNum.get()) {
            return;
        }
        thrNum.incrementAndGet();
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int end0 = stopNum.decrementAndGet();
                        if (end0 > -1) {
                            lock.lock();
                            try {
                                if (thrNum.get() > THREAD_MIN_NUM) {
                                    thrNum.decrementAndGet();
                                    log.t(Template.THIS_LANGUAGE[62], Thread.currentThread().getName(),Template.THIS_LANGUAGE[53], thrNum.get());
                                    break;
                                }
                                continue;
                            } finally {
                                lock.unlock();
                            }
                        }

                        Runnable runnable = runnableQueue.poll(await_sec, TimeUnit.SECONDS);
                        if (runnable == null) {
                            lock.lock();
                            try {
                                if (thrNum.get() > THREAD_MIN_NUM) {
                                    thrNum.decrementAndGet();
                                    log.t(Template.THIS_LANGUAGE[63], Thread.currentThread().getName(),Template.THIS_LANGUAGE[53], thrNum.get());
                                    break;
                                }
                                continue;
                            } finally {
                                lock.unlock();
                            }
                        }
                        runnable.run();
                    } catch (InterruptedException e) {
                        log.d(Template.THIS_LANGUAGE[64], Thread.currentThread().getName(), e);
                        thrNum.decrementAndGet();
                        break;
                    } catch (Throwable e) {
                        log.e(Template.THIS_LANGUAGE[65], Thread.currentThread().getName(), e);
                    }
                }
            }
        };
        thread.setName("thread-pool-v3");
        thread.start();
    }


    public boolean execute(Runnable runnable) {
        return runnableQueue.offer(runnable);
    }

    public int size() {
        return runnableQueue.size();
    }


}
