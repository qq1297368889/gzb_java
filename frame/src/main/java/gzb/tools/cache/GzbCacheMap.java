package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPool;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

//再也不让ai写了 写的太垃圾了 有空我重构
public class GzbCacheMap implements GzbCache {
    public static Log log = Log.log;
    // --- 内部数据结构 ---

    /**
     * 缓存条目：实现 Serializable 以支持文件持久化
     */
    private static class CacheEntry implements Serializable {
        // UID 用于序列化版本控制
        private static final long serialVersionUID = 1L;

        volatile Object value;
        volatile long expireTime;

        public CacheEntry(Object value, int second) {
            this.value = value;
            this.expireTime = second > 0 ? System.currentTimeMillis() + (long) second * 1000 : 0;
        }

        public boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }

    // 主缓存：key -> CacheEntry
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // Hash Map 缓存：key -> subMap<subKey, CacheEntry>
    // 注意：AtomicLong 也必须是 Serializable
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, CacheEntry>> mapCache = new ConcurrentHashMap<>();

    // 持久化文件路径
    private final String persistenceFilePath;

    // --- 构造函数和持久化/清理 ---

    /**
     * 默认构造函数，不启用持久化
     */
    public GzbCacheMap() {
        this(null, 600);
    }

    /**
     * 启用持久化的构造函数
     *
     * @param persistenceFilePath 缓存文件路径
     * @param intervalSeconds     内部线程工作间隔 必须大于0
     */
    public GzbCacheMap(String persistenceFilePath, int intervalSeconds) {
        if (intervalSeconds < 1) {
            log.w("缓存实现类 创建对象时 发现 intervalSeconds 小于1 自动重置为 120");
            intervalSeconds = 120;
        }
        this.persistenceFilePath = persistenceFilePath;

        // 1. 尝试从文件加载数据
        if (persistenceFilePath != null) {
            loadFromFile();
        }

        // 2. 启动清理任务
        int finalIntervalSeconds = intervalSeconds;
        ThreadPool.startService(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    cleanExpiredEntries();
                    if (persistenceFilePath != null) {
                        saveToFile();
                    }
                    Tools.sleep(finalIntervalSeconds * 1000L);
                }
            }
        }, "GzbCacheMap-服务线程");

        // 4. 添加关闭钩子，确保安全关闭和最终保存
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (persistenceFilePath != null) {
                saveToFile(); // 程序关闭时最终保存一次
            }
        }));
    }

    /**
     * 将当前缓存数据（String 和 Map）保存到文件
     */
    private synchronized void saveToFile() {
        if (persistenceFilePath == null) return;

        // 清理过期数据后再保存，避免保存脏数据
        cleanExpiredEntries();

        try (FileOutputStream fos = new FileOutputStream(persistenceFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // 创建一个要保存的容器对象
            Map<String, Object> dataContainer = new ConcurrentHashMap<>();
            dataContainer.put("cache", cache);
            dataContainer.put("mapCache", mapCache);
            oos.writeObject(dataContainer);
        } catch (IOException e) {
            log.e(e, "Error saving cache to file: " + persistenceFilePath);
        }
    }

    /**
     * 从文件加载缓存数据到内存
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(persistenceFilePath);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Map<String, Object> dataContainer = (Map<String, Object>) ois.readObject();

            // 安全地设置缓存，如果文件读取失败，当前 Map 保持为空
            cache.putAll((ConcurrentHashMap<String, CacheEntry>) dataContainer.get("cache"));
            mapCache.putAll((ConcurrentHashMap<String, ConcurrentHashMap<String, CacheEntry>>) dataContainer.get("mapCache"));

        } catch (IOException | ClassNotFoundException e) {
            log.e(e, "Error loading cache from file: " + persistenceFilePath);
        }
    }

    // 异步清理过期数据 (保持不变)
    private void cleanExpiredEntries() {
        // 清理 String 缓存
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());

        // 清理 Map 缓存
        for (Map.Entry<String, ConcurrentHashMap<String, CacheEntry>> mainEntry : mapCache.entrySet()) {
            String mainKey = mainEntry.getKey();
            ConcurrentHashMap<String, CacheEntry> subMap = mainEntry.getValue();

            subMap.entrySet().removeIf(subEntry -> subEntry.getValue().isExpired());

            if (subMap.isEmpty()) {
                mapCache.remove(mainKey, subMap);
            }
        }
    }


    @Override
    public Integer getIncr(String key, int second) {
        return getIncrLong(key, second).intValue();
    }

    @Override
    public Integer getIncr(String key, String subKey, int second) {
        return getIncrLong(key, subKey, second).intValue();
    }

    @Override
    public Long getIncrLong(String key, int second) {
        if (key == null || key.isEmpty()) return 0L;
        CacheEntry entry = cache.computeIfAbsent(key, k -> new CacheEntry(new AtomicLong(0), second));
        long newValue;
        if (entry.isExpired() || !(entry.value instanceof AtomicLong)) {
            entry.value = new AtomicLong(1);
            entry.expireTime = second > 0 ? System.currentTimeMillis() + (long) second * 1000 : 0;
            newValue = 1L;
        } else {
            newValue = ((AtomicLong) entry.value).incrementAndGet();
            entry.expireTime = second > 0 ? System.currentTimeMillis() + (long) second * 1000 : 0;
        }
        return newValue;
    }

    @Override
    public Long getIncrLong(String key, String subKey, int second) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return 0L;
        ConcurrentHashMap<String, CacheEntry> subMap = mapCache.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        CacheEntry entry = subMap.computeIfAbsent(subKey, k -> new CacheEntry(new AtomicLong(0), second));
        long newValue;
        if (entry.isExpired() || !(entry.value instanceof AtomicLong)) {
            newValue = 1L;
            entry.value = new AtomicLong(1);
            entry.expireTime = second > 0 ? System.currentTimeMillis() + (long) second * 1000 : 0;
        } else {
            newValue = ((AtomicLong) entry.value).incrementAndGet();
            entry.expireTime = second > 0 ? System.currentTimeMillis() + (long) second * 1000 : 0;
        }
        return newValue;
    }

    @Override
    public void set(String key, String val, int second) {
        if (key == null || val == null || key.isEmpty()) {
            return;
        }
        cache.put(key, new CacheEntry(val, second));
    }

    @Override
    public String get(String key) {
        if (key == null || key.isEmpty()) return null;
        CacheEntry entry = cache.get(key);
        if (entry != null) {
            if (entry.isExpired()) {
                cache.remove(key, entry);
                return null;
            }
            return entry.value instanceof String ? (String) entry.value : null;
        }
        return null;
    }

    // ... (其他 CRUD 和 INCR 方法) ...

    @Override
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        cache.remove(key);
    }

    @Override
    public void setMap(String key, String subKey, String val, int second) {
        if (key == null || subKey == null || val == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }
        ConcurrentHashMap<String, CacheEntry> subMap = mapCache.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        subMap.put(subKey, new CacheEntry(val, second));
    }

    @Override
    public String getMap(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return null;
        ConcurrentHashMap<String, CacheEntry> subMap = mapCache.get(key);
        if (subMap != null) {
            CacheEntry entry = subMap.get(subKey);
            if (entry != null) {
                if (entry.isExpired()) {
                    subMap.remove(subKey, entry);
                    if (subMap.isEmpty()) {
                        mapCache.remove(key, subMap);
                    }
                    return null;
                }
                return entry.value instanceof String ? (String) entry.value : null;
            }
        }
        return null;
    }

    @Override
    public void removeMap(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }
        ConcurrentHashMap<String, CacheEntry> subMap = mapCache.get(key);
        if (subMap != null) {
            if (subMap.remove(subKey) != null) {
                if (subMap.isEmpty()) {
                    mapCache.remove(key, subMap);
                }
            }
        }
    }

    @Override
    public void removeMap(String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        mapCache.remove(key);
    }

    /// 下列各种方法 带序列化和反序列化 其他规则和上述同名方法一致   删除方法共享

    /**
     * 将 Object 序列化为 byte 数组。
     *
     * @param obj 要序列化的对象。
     * @return 序列化后的 byte 数组，失败返回 null。
     */
    private byte[] serialize(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            log.e(e, "Error serializing object.");
            return null;
        }
    }

    /**
     * 将 byte 数组反序列化为 Object。
     *
     * @param bytes 序列化后的 byte 数组。
     * @return 反序列化后的 Object，失败返回 null。
     */
    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.e(e, "Error deserializing object.");
            return null;
        }
    }

    @Override
    public void setMapObject(String key, String subKey, Object val, int second) {
        if (key == null || subKey == null || val == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }

        // 1. 序列化 Object
        byte[] serializedVal = serialize(val);
        if (serializedVal == null) {
            return;
        }

        try {
            // 2. 存储到 MapCache (Hash 结构)
            ConcurrentHashMap<String, CacheEntry> subMap = mapCache.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
            // CacheEntry 中存储的是 byte[]
            subMap.put(subKey, new CacheEntry(serializedVal, second));
        } catch (Exception e) {
            log.e(e, "Error setting object to map cache.");
        }
    }

    @Override
    public void setObject(String key, Object val, int second) {
        if (key == null || val == null || key.isEmpty()) {
            return;
        }

        // 1. 序列化 Object
        byte[] serializedVal = serialize(val);
        if (serializedVal == null) {
            return;
        }

        try {
            // 2. 存储到主 Cache (String 结构)
            // CacheEntry 中存储的是 byte[]
            cache.put(key, new CacheEntry(serializedVal, second));
        } catch (Exception e) {
            log.e(e, "Error setting object to string cache.");
        }
    }

    @Override
    public <T> T getObject(String key) {
        if (key == null || key.isEmpty()) return null;

        try {
            // 1. 从主 Cache 获取条目
            CacheEntry entry = cache.get(key);
            if (entry != null) {
                if (entry.isExpired()) {
                    cache.remove(key, entry);
                    return null;
                }
                // 2. 确保值是 byte[] 类型，并反序列化
                if (entry.value instanceof byte[]) {
                    return deserialize((byte[]) entry.value);
                }
            }
            return null;
        } catch (Exception e) {
            log.e(e, "Error getting object from string cache.");
            return null;
        }
    }

    @Override
    public <T> T getMapObject(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return null;
        ConcurrentHashMap<String, CacheEntry> subMap = mapCache.get(key);
        if (subMap != null) {
            CacheEntry entry = subMap.get(subKey);
            if (entry != null) {
                if (entry.isExpired()) {
                    subMap.remove(subKey, entry);
                    if (subMap.isEmpty()) {
                        mapCache.remove(key, subMap);
                    }
                    return null;
                }
                if (entry.value instanceof byte[]) {
                    return deserialize((byte[]) entry.value);
                }
            }
        }
        return null;
    }
}