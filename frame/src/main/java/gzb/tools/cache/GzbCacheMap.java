/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.log.Log;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GzbCacheMap implements GzbCache {

    public static Log log= Config.log;
    String persistFilePath = null;

    /**
     * 内部缓存实体类，包含值和过期时间。
     */
    private static class CacheEntry implements Serializable {
        private static final long serialVersionUID = 1L;
        public Object value; // 改为非 final 以便在内部更新
        public long expireTime;

        public CacheEntry(Object value, int second) {
            this.value = value;
            this.expireTime = (second > 0) ? System.currentTimeMillis() + (long) second * 1000 : -1;
        }

        public boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }

    private int isSave = 0;
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Condition> queueConditions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> keyLocks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public GzbCacheMap() {
        this(null);
    }


    // （在带路径的构造器中调用加载方法，实现初始化时自动恢复）
    public GzbCacheMap(String persistFilePath) {
        this.persistFilePath = persistFilePath;
        if (persistFilePath != null && !persistFilePath.trim().isEmpty()) {
            // 新增：初始化时从文件加载缓存
            loadCacheFromFile();
            scheduler.scheduleAtFixedRate(this::saveCacheToFile, 1, 10, TimeUnit.SECONDS);
        }
        // 启动过期清理和定时保存任务（原逻辑保留）
        scheduler.scheduleAtFixedRate(this::cleanExpiredEntries, 1, 1, TimeUnit.MINUTES);
        log.d("GzbCacheMap,初始化成功");
    }

    /**
     * 新增：从指定文件加载之前保存的缓存数据，并恢复到当前缓存中
     * 仅恢复数据，锁、条件变量等控制结构需重新创建
     */
    private void loadCacheFromFile() {
        // 若文件路径无效，直接返回
        if (persistFilePath == null || persistFilePath.trim().isEmpty()) {
            log.d("持久化文件未指定，跳过缓存恢复");
            return;
        }

        File persistFile = new File(persistFilePath);
        if (!persistFile.exists() || persistFile.length() == 0) {
            log.d("持久化文件不存在或为空，跳过缓存恢复");
            return;
        }

        try (FileInputStream fis = new FileInputStream(persistFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // 读取序列化的缓存数据（Map<String, CacheEntry>）
            Map<String, CacheEntry> savedCache = (Map<String, CacheEntry>) ois.readObject();

            // 过滤已过期的条目，恢复到当前缓存
            int restoredCount = 0;
            for (Map.Entry<String, CacheEntry> entry : savedCache.entrySet()) {
                String key = entry.getKey();
                CacheEntry cacheEntry = entry.getValue();
                if (!cacheEntry.isExpired()) { // 只恢复未过期的条目
                    cache.put(key, cacheEntry);
                    restoredCount++;
                }
            }

            log.d("从文件恢复缓存完成",persistFilePath,"共恢复", restoredCount,"条有效条目");
        } catch (Exception e) {
            log.d("从文件加载缓存失败！",persistFilePath,e);
        }
    }

    /**
     * 新增：将当前缓存中的有效（未过期）条目序列化保存到文件
     * 仅保存缓存数据，不保存锁、条件变量等控制结构
     */
    private void saveCacheToFile() {
        if (isSave == 0) {
            return;
        }
        isSave = 0;
        // 1. 过滤出未过期的缓存条目（避免持久化无效数据）
        Map<String, CacheEntry> validCache = new HashMap<>();
        cache.forEach((key, entry) -> {
            if (!entry.isExpired()) { // 只保存未过期的条目
                validCache.put(key, entry);
            }
        });

        // 2. 若无可持久化的数据，直接返回
        if (validCache.isEmpty()) {
            log.d("无有效缓存数据，跳过持久化");
            return;
        }

        // 3. 序列化有效缓存条目到文件
        try (FileOutputStream fos = new FileOutputStream(persistFilePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(validCache); // 序列化过滤后的缓存数据
            log.d("缓存数据已成功保存到文件",persistFilePath,"共保存", validCache.size(),"条有效条目");
        } catch (Exception e) {
            log.d("保存缓存到文件失败！",persistFilePath,e);
        }
    }

    private ReentrantLock getLock(String key) {
        return keyLocks.computeIfAbsent(key, k -> new ReentrantLock());
    }

    private Condition getQueueCondition(String key) {
        ReentrantLock lock = getLock(key);
        return queueConditions.computeIfAbsent(key, k -> lock.newCondition());
    }

    private void cleanExpiredEntries() {
        cache.forEach((key, entry) -> {
            if (entry.isExpired()) {
                // 确保原子移除锁和 Condition
                ReentrantLock lock = keyLocks.get(key);
                if (lock != null) {
                    lock.lock();
                    try {
                        cache.remove(key, entry);
                        keyLocks.remove(key);
                        queueConditions.remove(key);
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
    }

    /**
     * 检查并处理过期项。
     */
    private Object getAndCheck(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            // 惰性删除也需要原子性，但由于这里是获取操作，不加锁也能工作，
            // 因为并发的 set/del 操作会加锁
            cache.remove(key, entry);
            keyLocks.remove(key);
            queueConditions.remove(key);
            isSave = 1;
            return null;
        }
        return entry.value;
    }

    /**
     * 递归获取哈希表中的值。
     */
    private Object getMapValue(String key, String mapKey) {
        Object parent = getAndCheck(key);
        if (parent instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parent;
            return map.get(mapKey);
        }
        return null;
    }

    /**
     * 递归获取列表中指定索引的元素。
     */
    private Object getListValue(String key, int index) {
        Object parent = getAndCheck(key);
        if (parent instanceof List) {
            List<?> list = (List<?>) parent;
            if (index >= 0 && index < list.size()) {
                return list.get(index);
            }
        }
        return null;
    }

    // --- Core Operations ---

    @Override
    public int getIncr(String key) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object val = entry != null && !entry.isExpired() ? entry.value : null;

            int intVal;
            if (val == null) {
                intVal = 1;
            } else if (val instanceof Integer) {
                intVal = (Integer) val + 1;
            } else {
                return 0; // 类型不匹配
            }
            cache.put(key, new CacheEntry(intVal, entry != null ? (int) (entry.expireTime - System.currentTimeMillis()) / 1000 : -1));
            isSave = 1;
            return intVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int getIncr(String key, String mapKey) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object parent = entry != null && !entry.isExpired() ? entry.value : null;

            Map<String, Object> map;
            if (parent == null || !(parent instanceof Map)) {
                map = new HashMap<>();
            } else {
                map = (Map<String, Object>) parent;
            }

            Object val = map.get(mapKey);
            int intVal;
            if (val == null || !(val instanceof Integer)) {
                intVal = 1;
            } else {
                intVal = (Integer) val + 1;
            }
            map.put(mapKey, intVal);
            cache.put(key, new CacheEntry(map, entry != null ? (int) (entry.expireTime - System.currentTimeMillis()) / 1000 : -1));
            isSave = 1;
            return intVal;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object get(String key) {
        return getAndCheck(key);
    }

    @Override
    public Object get(String key, String mapKey) {
        return getMapValue(key, mapKey);
    }

    @Override
    public Object get(String key, int index) {
        return getListValue(key, index);
    }

    @Override
    public void set(String key, Object val) {
        isSave = 1;
        cache.put(key, new CacheEntry(val, -1));
    }

    @Override
    public void setMap(String key, String mapKey, Object val) {
        setMap(key, mapKey, val, -1);
    }

    @Override
    public void setList(String key, int index, Object val) {
        setList(key, index, val, -1);
    }

    @Override
    public void set(String key, Object val, int second) {
        isSave = 1;
        cache.put(key, new CacheEntry(val, second));
    }

    @Override
    public void setMap(String key, String mapKey, Object val, int second) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object parent = entry != null && !entry.isExpired() ? entry.value : null;

            Map<String, Object> map;
            if (parent == null || !(parent instanceof Map)) {
                map = new HashMap<>();
            } else {
                map = (Map<String, Object>) parent;
            }
            map.put(mapKey, val);
            cache.put(key, new CacheEntry(map, second));
            isSave = 1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setList(String key, int index, Object val, int second) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object parent = entry != null && !entry.isExpired() ? entry.value : null;
            if (parent instanceof List) {
                List<Object> list = (List<Object>) parent;
                if (index >= 0 && index < list.size()) {
                    list.set(index, val);
                    isSave = 1;
                    cache.put(key, new CacheEntry(list, second));
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(String key, Object val) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object parent = entry != null && !entry.isExpired() ? entry.value : null;

            if (parent == null || !(parent instanceof List)) {
                List<Object> newList = new LinkedList<>();
                newList.add(val);
                set(key, newList);
            } else {
                ((List<Object>) parent).add(val);
            }
            isSave = 1;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object del(String key) {
        ReentrantLock lock = keyLocks.get(key);
        if (lock != null) {
            lock.lock();
            try {
                // 原子性移除
                CacheEntry entry = cache.remove(key);
                keyLocks.remove(key);
                queueConditions.remove(key);
                isSave = 1;
                return entry != null ? entry.value : null;
            } finally {
                lock.unlock();
            }
        } else {
            CacheEntry entry = cache.remove(key);
            isSave = 1;
            return entry != null ? entry.value : null;
        }
    }

    @Override
    public Object del(String key, String mapKey) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            Object parent = getAndCheck(key);
            if (parent instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) parent;
                isSave = 1;
                return map.remove(mapKey);
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public Object del(String key, int index) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            Object parent = getAndCheck(key);
            if (parent instanceof List) {
                List<?> list = (List<?>) parent;
                isSave = 1;
                if (index >= 0 && index < list.size()) {
                    return list.remove(index);
                }
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    // --- 类型转换方法 ---
    private Integer getIntegerInternal(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Integer getInteger(String key, Integer defVal) {
        Object val = get(key);
        Integer result = getIntegerInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Integer getInteger(String key, String mapKey, Integer defVal) {
        Object val = get(key, mapKey);
        Integer result = getIntegerInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Integer getInteger(String key, int index, Integer defVal) {
        Object val = get(key, index);
        Integer result = getIntegerInternal(val);
        return result != null ? result : defVal;
    }

    private String getStringInternal(Object value) {
        if (value != null) return value.toString();
        return null;
    }

    @Override
    public String getString(String key, String defVal) {
        Object val = get(key);
        String result = getStringInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public String getString(String key, String mapKey, String defVal) {
        Object val = get(key, mapKey);
        String result = getStringInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public String getString(String key, int index, String defVal) {
        Object val = get(key, index);
        String result = getStringInternal(val);
        return result != null ? result : defVal;
    }

    private Boolean getBooleanInternal(Object value) {
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof String) return Boolean.parseBoolean((String) value);
        return null;
    }

    @Override
    public Boolean getBoolean(String key, Boolean defVal) {
        Object val = get(key);
        Boolean result = getBooleanInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Boolean getBoolean(String key, String mapKey, Boolean defVal) {
        Object val = get(key, mapKey);
        Boolean result = getBooleanInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Boolean getBoolean(String key, int index, Boolean defVal) {
        Object val = get(key, index);
        Boolean result = getBooleanInternal(val);
        return result != null ? result : defVal;
    }

    private Long getLongInternal(Object value) {
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Long getLong(String key, Long defVal) {
        Object val = get(key);
        Long result = getLongInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Long getLong(String key, String mapKey, Long defVal) {
        Object val = get(key, mapKey);
        Long result = getLongInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Long getLong(String key, int index, Long defVal) {
        Object val = get(key, index);
        Long result = getLongInternal(val);
        return result != null ? result : defVal;
    }

    private Double getDoubleInternal(Object value) {
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Double getDouble(String key, Double defVal) {
        Object val = get(key);
        Double result = getDoubleInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Double getDouble(String key, String mapKey, Double defVal) {
        Object val = get(key, mapKey);
        Double result = getDoubleInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public Double getDouble(String key, int index, Double defVal) {
        Object val = get(key, index);
        Double result = getDoubleInternal(val);
        return result != null ? result : defVal;
    }

    private byte[] getByteArrayInternal(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        return null;
    }

    @Override
    public byte[] getByteArray(String key, byte[] defVal) {
        Object val = get(key);
        byte[] result = getByteArrayInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public byte[] getByteArray(String key, String mapKey, byte[] defVal) {
        Object val = get(key, mapKey);
        byte[] result = getByteArrayInternal(val);
        return result != null ? result : defVal;
    }

    @Override
    public byte[] getByteArray(String key, int index, byte[] defVal) {
        Object val = get(key, index);
        byte[] result = getByteArrayInternal(val);
        return result != null ? result : defVal;
    }

    // --- 序列化方法 ---

    private byte[] serialize(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setObject(String key, Object val, int second) {
        set(key, serialize(val), second);
    }

    @Override
    public void setObject(String key, String mapKey, Object val, int second) {
        setMap(key, mapKey, serialize(val), second);
    }

    @Override
    public void setObject(String key, int index, Object val, int second) {
        setList(key, index, serialize(val), second);
    }

    @Override
    public void addObject(String key, Object val) {
        add(key, serialize(val));
    }

    @Override
    public <T> T getObject(String key, T defVal) {
        Object val = get(key);
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) (obj != null ? obj : defVal);
    }

    @Override
    public <T> T getObject(String key, String mapKey, T defVal) {
        Object val = get(key, mapKey);
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) (obj != null ? obj : defVal);
    }

    @Override
    public <T> T getObject(String key, int index, T defVal) {
        Object val = get(key, index);
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) (obj != null ? obj : defVal);
    }

    // --- 队列方法 ---
    @Override
    public void queueProduction(String key, Object val) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            CacheEntry entry = cache.get(key);
            Object parent = entry != null && !entry.isExpired() ? entry.value : null;

            if (parent == null || !(parent instanceof List)) {
                List<Object> newList = new LinkedList<>();
                newList.add(val);
                set(key, newList);
            } else {
                ((List<Object>) parent).add(val);
            }
            Condition condition = getQueueCondition(key);
            condition.signalAll(); // 通知所有等待的消费者
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object queueConsumption(String key) {
        ReentrantLock lock = getLock(key);
        lock.lock();
        try {
            Object parent = getAndCheck(key);
            if (parent instanceof List && !((List) parent).isEmpty()) {
                Object consumed = ((List) parent).remove(0);
                // 再次更新缓存，以反映列表的变动
                set(key, parent, getRemainingTime(key));
                return consumed;
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    // 辅助方法，用于获取剩余过期时间
    private int getRemainingTime(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.expireTime <= 0) {
            return -1;
        }
        long remaining = entry.expireTime - System.currentTimeMillis();
        return (int) (remaining > 0 ? remaining / 1000 : 0);
    }

    @Override
    public Object queueConsumptionBlock(String key, int second) {
        ReentrantLock lock = getLock(key);
        Condition condition = getQueueCondition(key);
        lock.lock();
        try {
            long deadline = (second > 0) ? System.nanoTime() + TimeUnit.SECONDS.toNanos(second) : 0;
            while (true) {
                Object parent = getAndCheck(key);
                if (parent instanceof List && !((List) parent).isEmpty()) {
                    Object consumed = ((List) parent).remove(0);
                    // 再次更新缓存，以反映列表的变动
                    set(key, parent, getRemainingTime(key));
                    return consumed;
                }
                if (second <= 0) { // 无限期阻塞
                    condition.await();
                } else {
                    long remainingNanos = deadline - System.nanoTime();
                    if (remainingNanos <= 0) {
                        return null; // 超时
                    }
                    condition.awaitNanos(remainingNanos);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T> void queueProductionObject(String key, T val) {
        queueProduction(key, serialize(val));
    }

    @Override
    public <T> T queueConsumptionObject(String key) {
        Object val = queueConsumption(key);
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) obj;
    }

    @Override
    public <T> T queueConsumptionBlockObject(String key) {
        Object val = queueConsumptionBlock(key, 0); // 无限期阻塞
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) obj;
    }

    @Override
    public <T> T queueConsumptionBlockObject(String key, int second) {
        Object val = queueConsumptionBlock(key, second);
        Object obj = deserialize(getByteArrayInternal(val));
        return (T) obj;
    }
}