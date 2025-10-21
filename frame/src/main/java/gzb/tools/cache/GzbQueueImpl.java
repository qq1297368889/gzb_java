package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.cache.entity.Entity;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPool;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class GzbQueueImpl implements GzbQueue {
    public static void main(String[] args) {
        GzbQueue gzbQueue = new GzbQueueImpl(Tools.getProjectRoot()+"/test.queue");
        if (gzbQueue.size()==0) {
            for (int i = 0; i < 1000; i++) {
                gzbQueue.produce("哈哈哈哈 "+i);
            }
        }

        log.d("consume",gzbQueue.size());
        log.d("consume",gzbQueue.consume(-1));
        log.d("consume",gzbQueue.consume(-1));
        log.d("consume",gzbQueue.consume(-1));
        log.d("consume",gzbQueue.consume(-1));
        log.d("consume",gzbQueue.consume(-1));
        log.d("consume",gzbQueue.size());
    }
    public static Log log = Log.log;

    public AtomicLong idGenerator = new AtomicLong(0);

    public LinkedBlockingQueue<Entity> queueCache = new LinkedBlockingQueue<>();
    Map<Long, Entity> consumeMap = new ConcurrentHashMap<>();

    int consume_max;
    int retries_max;
    int timeSleep;
    String persistenceFilePath = null;

    public GzbQueueImpl() {
        this(60 * 1000, 3, 60 * 1000, null);
    }
    public GzbQueueImpl(String persistenceFilePath) {
        this(60 * 1000, 3, 60 * 1000, persistenceFilePath);
    }

    //检查时间周期 timeSleep
    //最大重新消费次数 retries_max
    //最大等待确认时间 毫秒 超时视为消费失败 consume_max
    public GzbQueueImpl(int consume_max, int retries_max, int timeSleep, String persistenceFilePath) {
        this.consume_max = consume_max;
        this.retries_max = retries_max;
        this.timeSleep = timeSleep;
        this.persistenceFilePath = persistenceFilePath;
        loadFromFile();
        startDog(consume_max, retries_max, timeSleep);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LinkedBlockingQueue<Entity> queueCache0 = queueCache;
            queueCache = null;//后续一定出错  防止继续执行】
            Map<Long, Entity> consumeMap0 = consumeMap;
            consumeMap = null;//后续一定出错  防止继续执行】
            //重新消费
            for (Map.Entry<Long, Entity> longEntityEntry : consumeMap0.entrySet()) {
                log.e("jvm被关闭，消费中未知状态", longEntityEntry.getValue());
            }
            saveToFile(queueCache0, consumeMap0); // 程序关闭时最终保存一次
        }));
    }

    /**
     * 将当前缓存数据（String 和 Map）保存到文件
     */
    private synchronized void saveToFile(LinkedBlockingQueue<Entity> queueCache0, Map<Long, Entity> consumeMap0) {
        if (persistenceFilePath == null) return;

        try (FileOutputStream fos = new FileOutputStream(persistenceFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // 创建一个要保存的容器对象
            Map<String, Object> dataContainer = new ConcurrentHashMap<>();
            dataContainer.put("idGenerator", idGenerator);
            dataContainer.put("queueCache", queueCache0);
            oos.writeObject(dataContainer);
            log.d("Cache saved successfully to: " + persistenceFilePath);
        } catch (IOException e) {
            log.e(e, "Error saving cache to file: " + persistenceFilePath);
        }

    }

    /**
     * 从文件加载缓存数据到内存
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (persistenceFilePath == null) {
            return;
        }
        File file = new File(persistenceFilePath);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Map<String, Object> dataContainer = (Map<String, Object>) ois.readObject();
            idGenerator = (AtomicLong) dataContainer.get("idGenerator");
            queueCache = (LinkedBlockingQueue<Entity>) dataContainer.get("queueCache");
            log.d("Cache loaded successfully from: " + persistenceFilePath);
        } catch (IOException | ClassNotFoundException e) {
            log.e(e, "Error loading cache from file: " + persistenceFilePath);
        }
    }

    private void startDog(int consume_max, int retries_max, int timeSleep) {
        ThreadPool.startService(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        long time0 = System.currentTimeMillis();
                        Iterator<Map.Entry<Long, Entity>> iterator = consumeMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Long, Entity> entry = iterator.next();
                            if (entry == null) {
                                iterator.remove();
                                continue;
                            }
                            Entity entity = entry.getValue();
                            //没超时
                            if (time0 - entity.time < consume_max) {
                                continue;
                            }
                            entry.getValue().retries++;
                            //没超过重试次数
                            if (entity.retries <= retries_max) {
                                queueCache.add(entity);
                                iterator.remove();
                            } else {
                                log.e("消费失败", entity);
                            }
                        }
                    } catch (Exception e) {
                        log.e(e);//防止崩溃
                    }

                    Tools.sleep(timeSleep); //不会被线程中断影响
                    saveToFile(queueCache, consumeMap);//日常保存 覆盖保存
                }
            }
        },"GzbQueueImpl-服务线程");
    }

    // ----------------------------------------------------
    // 1. 生产 (Produce) - O(1)
    // ----------------------------------------------------
    @Override
    public void produce(String val) {
        //这里确保不能为空 避免后续判断
        if (val == null || val.isEmpty()) return;
        Entity cacheEntity = new Entity();
        cacheEntity.id = idGenerator.incrementAndGet();
        cacheEntity.data = val;
        queueCache.add(cacheEntity);
    }

    // ----------------------------------------------------
    // 2. 消费 (Consume) - O(1) 原子操作
    // ----------------------------------------------------

    @Override
    public Entity consume(int second) {
        //queueCache 本身是线程安全的 后续操作 也是安全的无需加锁
        Entity entity = null;
        try {
            if (second < 0) {
                entity = queueCache.poll();
            } else if (second == 0) {
                entity = queueCache.take();
            } else {
                entity = queueCache.poll(second, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.e(e);
        }
        if (entity == null) {
            return null;
        }
        entity.time = System.currentTimeMillis();
        consumeMap.put(entity.id, entity);
        return entity;
    }

    // ----------------------------------------------------
    // 3. 确认与自动确认 (Confirm)
    // ----------------------------------------------------

    @Override
    public String consumeAndConfirm(int second) {
        Entity cacheEntity = null;
        try {
            if (second < 0) {
                cacheEntity = queueCache.poll();
            } else if (second == 0) {
                cacheEntity = queueCache.take();
            } else {
                cacheEntity = queueCache.poll(second, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.e(e);
        }
        if (cacheEntity == null) {
            return null;
        }
        return cacheEntity.data.toString();
    }

    @Override
    public boolean confirm(long messageId) {
        return consumeMap.remove(messageId) != null;
    }

    // ----------------------------------------------------
    // 4. 工具方法 (Size, Read)
    // ----------------------------------------------------

    @Override
    public Entity read() {
        // 尝试从 readyQueue 中获取 ID (O(1) 操作)
        return queueCache.peek();
    }

    @Override
    public long size() {
        return queueCache.size();
    }
}