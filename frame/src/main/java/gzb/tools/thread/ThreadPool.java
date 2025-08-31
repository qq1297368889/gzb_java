package gzb.tools.thread;

import gzb.tools.Queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    public static ThreadPool pool = new ThreadPool(10, 100);
    int startThread;
    int maxThread;
    List<Thread> listThread = new ArrayList<>();
    Queue<Runnable> queue = new Queue<>();
    Lock mapThreadShareLock = new ReentrantLock();
    Map<Long, Map<String, Object>> mapThreadShare = new ConcurrentHashMap<>();

    public ThreadPool(int startThread, int maxThread) {
        this.startThread = startThread;
        this.maxThread = maxThread;
        startThread(1, "thread-home", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (queue.size() > listThread.size() - 3) {
                            if (maxThread==0 || maxThread > queue.size()) {
                                startExecuteThread(1);
                            }
                        }
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        startExecuteThread(startThread);
    }

    public void startExecuteThread(int num) {
        this.listThread.addAll(startThread(num, "thread-pool", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Runnable runnable = queue.take();
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }));
    }

    public void putThreadShare(String key, Object val) {
        long id = Thread.currentThread().getId();
        Map<String, Object> map = mapThreadShare.get(id);

        if (map == null) {
            mapThreadShareLock.lock();
            try {
                if (map == null) {
                    map = new ConcurrentHashMap<>();
                    mapThreadShare.put(id, map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mapThreadShareLock.unlock();
            }
        }
        map.put(key, val);
    }

    public Object getThreadShare(String key) {
        long id = Thread.currentThread().getId();
        Map<String, Object> map = mapThreadShare.get(id);
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public void stopAll() {
        for (Thread thread : listThread) {
            stop(thread);
        }
    }

    public void stop(Thread thread) {
        thread.interrupt();
    }

    public int execute(int num, Runnable runnable) {
        for (int i = 0; i < num; i++) {
            if (!execute(runnable)) {
                return i + 1;
            }
        }
        return num;
    }

    public boolean execute(Runnable runnable) {
        if (listThread.size() >= maxThread && maxThread>0) {
            return false;
        }
        queue.add(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                runnable.run();
            }
        });
        return true;
    }
    public boolean execute0(Runnable runnable) {
        if (listThread.size() >= maxThread && maxThread>0) {
            return false;
        }
        queue.add(runnable);
        return true;
    }

    public List<Thread> startThread(int startNum, String name, Boolean daemon, Runnable runnable) {
        List<Thread> listThread1 = new ArrayList<>();
        for (int i = 0; i < startNum; i++) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(daemon);
            thread.setName(name + "-" + i);
            thread.start();
            listThread1.add(thread);
        }
        return listThread1;
    }

    public List<Thread> startThread(int startNum, String name, Runnable runnable) {
        List<Thread> listThread1 = startThread(startNum, name, true, runnable);
        if (listThread1 == null || listThread1.size() == 0) {
            return null;
        }
        return listThread1;
    }

    public List<Thread> startThread(int startNum, Runnable runnable) {
        List<Thread> listThread1 = startThread(startNum, "ThreadPool-start-", true, runnable);
        if (listThread1 == null || listThread1.size() == 0) {
            return null;
        }
        return listThread1;
    }

    public Thread startThread(Runnable runnable) {
        List<Thread> listThread1 = startThread(1, "ThreadPool-start-", true, runnable);
        if (listThread1 == null || listThread1.size() == 0) {
            return null;
        }
        return listThread1.get(0);
    }


}
