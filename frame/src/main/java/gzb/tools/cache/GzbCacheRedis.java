package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.log.Log;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisNoScriptException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GzbCacheRedis implements GzbCache {
    public static Log log = Log.log;
    public JedisPool jedisPool = null;

    private static final java.nio.charset.Charset UTF_8 =Config.encoding;

    // --- Lua 脚本定义 (保持不变) ---
    private static final String ATOMIC_INCR_EXPIRE_LUA =
            "local current = redis.call('INCR', KEYS[1]); " +
                    "if (current == 1 and tonumber(ARGV[1]) > 0) then " +
                    "    redis.call('EXPIRE', KEYS[1], ARGV[1]); " +
                    "end; " +
                    "return current;";

    private static final String ATOMIC_HINCR_EXPIRE_LUA =
            "local current = redis.call('HINCRBY', KEYS[1], ARGV[1], 1); " +
                    "if (tonumber(ARGV[2]) > 0) then " +
                    "    redis.call('EXPIRE', KEYS[1], ARGV[2]); " +
                    "end; " +
                    "return current;";

    // --- 脚本 SHA1 缓存字段 (保持不变) ---
    private volatile String incrSha = null;
    private volatile String hIncrSha = null;


    // --- 构造函数和初始化方法 (保持不变) ---
    public GzbCacheRedis() {
        this(Config.get("db.cache.key", "cache1"));
    }

    public GzbCacheRedis(String key) {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            String ip = Config.get("db.redis." + key + ".ip", "127.0.0.1");
            Integer port = Config.getInteger("db.redis." + key + ".port", 6379);
            String pwd = Config.get("db.redis." + key + ".pwd", "123456");
            Integer maxIdle = Config.getInteger("db.redis." + key + ".max.thread.idle", 10);
            Integer maxTotal = Config.getInteger("db.redis." + key + ".thread", 10);
            Integer overtime = Config.getInteger("db.redis." + key + ".overtime", 5000);
            Integer index = Config.getInteger("db.redis." + key + ".index", 0);
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMaxIdle(maxIdle);

            if (pwd == null || pwd.trim().isEmpty() || "null".equalsIgnoreCase(pwd)) {
                jedisPool = new JedisPool(jedisPoolConfig, ip, port, overtime);
            } else {
                jedisPool = new JedisPool(jedisPoolConfig, ip, port, overtime, pwd, index);
            }
            log.d("GzbCacheRedis,初始化成功");

            // *** 性能优化点：预加载 Lua 脚本 ***
            try (Jedis jedis = jedisPool.getResource()) {
                incrSha = jedis.scriptLoad(ATOMIC_INCR_EXPIRE_LUA);
                hIncrSha = jedis.scriptLoad(ATOMIC_HINCR_EXPIRE_LUA);
                log.d("Lua 脚本预加载成功.");
            } catch (Exception e) {
                log.e(e, "GzbCacheRedis, Lua 脚本预加载失败，将使用 EVAL 模式。");
            }
        } catch (Exception e) {
            log.e(e, "GzbCacheRedis,初始化失败");
            throw new RuntimeException(e);
        }
    }

    // --- 核心执行逻辑封装 (evaluateScript) ---
    private Long evaluateScript(String script, String sha, List<String> keys, List<String> args) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (sha != null) {
                try {
                    return (Long) jedis.evalsha(sha, keys, args);
                } catch (JedisNoScriptException e) {
                    Object result = jedis.eval(script, keys, args);
                    String newSha = jedis.scriptLoad(script);
                    if (script.equals(ATOMIC_INCR_EXPIRE_LUA)) {
                        incrSha = newSha;
                    } else if (script.equals(ATOMIC_HINCR_EXPIRE_LUA)) {
                        hIncrSha = newSha;
                    }
                    return (Long) result;
                }
            } else {
                return (Long) jedis.eval(script, keys, args);
            }
        } catch (Exception e) {
            return null;
        }
    }

    // --- GzbCache 接口实现方法 (保持不变) ---
    @Override
    public Long getIncrLong(String key, int second) {
        if (key == null || key.isEmpty()) return 0L;
        List<String> keys = Collections.singletonList(key);
        List<String> args = Collections.singletonList(String.valueOf(second));
        Long result = evaluateScript(ATOMIC_INCR_EXPIRE_LUA, incrSha, keys, args);
        if (result == null) {
            log.e(new Exception("Redis operation failed"), "getIncrLong error for key: " + key);
            return 0L;
        }
        return result;
    }

    @Override
    public Long getIncrLong(String key, String subKey, int second) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return 0L;
        List<String> keys = Collections.singletonList(key);
        List<String> args = Arrays.asList(subKey, String.valueOf(second));
        Long result = evaluateScript(ATOMIC_HINCR_EXPIRE_LUA, hIncrSha, keys, args);
        if (result == null) {
            log.e(new Exception("Redis operation failed"), "getIncrLong error for key: " + key + ", subKey: " + subKey);
            return 0L;
        }
        return result;
    }

    @Override
    public Integer getIncr(String key, int second) {
        Long result = getIncrLong(key, second);
        return result.intValue();
    }

    @Override
    public Integer getIncr(String key, String subKey, int second) {
        Long result = getIncrLong(key, subKey, second);
        return result.intValue();
    }

    @Override
    public void set(String key, String val, int second) {
        if (key == null || val == null || key.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] valBytes = val.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            if (second > 0) {
                jedis.setex(keyBytes, second, valBytes);
            } else {
                jedis.set(keyBytes, valBytes);
            }
        } catch (Exception e) {
            log.e(e, "set error for key: " + key);
        }
    }

    @Override
    public String get(String key) {
        if (key == null || key.isEmpty()) return null;
        byte[] keyBytes = key.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(keyBytes);
            if (bytes != null) {
                return new String(bytes, UTF_8);
            }
            return null;
        } catch (Exception e) {
            log.e(e, "get error for key: " + key);
            return null;
        }
    }

    @Override
    public void setMap(String key, String subKey, String val, int second) {
        if (key == null || subKey == null || val == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] subKeyBytes = subKey.getBytes(UTF_8);
        byte[] valBytes = val.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(keyBytes, subKeyBytes, valBytes);
            if (second > 0) {
                jedis.expire(keyBytes, second);
            }
        } catch (Exception e) {
            log.e(e, "setMap error for key: " + key + ", subKey: " + subKey);
        }
    }

    @Override
    public String getMap(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return null;
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] subKeyBytes = subKey.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.hget(keyBytes, subKeyBytes);
            if (bytes != null) {
                return new String(bytes, UTF_8);
            }
            return null;
        } catch (Exception e) {
            log.e(e, "getMap error for key: " + key + ", subKey: " + subKey);
            return null;
        }
    }

    @Override
    public void remove(String key) {
        if (key == null || key.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(keyBytes);
        } catch (Exception e) {
            log.e(e, "remove error for key: " + key);
        }
    }

    @Override
    public void removeMap(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] subKeyBytes = subKey.getBytes(UTF_8);
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hdel(keyBytes, subKeyBytes);
        } catch (Exception e) {
            log.e(e, "removeMap error for key: " + key + ", subKey: " + subKey);
        }
    }

    @Override
    public void removeMap(String key) {
        remove(key);
    }

    /// 下列各种方法 带序列化和反序列化 其他规则和上述同名方法一致   删除方法共享
    /**
     * 将 Object 序列化为 byte 数组。
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

// --- 新增 Object 存取方法 (使用 byte[] 进行序列化) ---

    @Override
    public void setObject(String key, Object val, int second) {
        if (key == null || val == null || key.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] valBytes = serialize(val);
        if (valBytes == null) return;

        try (Jedis jedis = jedisPool.getResource()) {
            if (second > 0) {
                // 使用 SETEX 原子地设置值和过期时间
                jedis.setex(keyBytes, second, valBytes);
            } else {
                jedis.set(keyBytes, valBytes);
            }
        } catch (Exception e) {
            log.e(e, "setObject error for key: " + key);
        }
    }

    @Override
    public <T> T getObject(String key) {
        if (key == null || key.isEmpty()) return null;
        byte[] keyBytes = key.getBytes(UTF_8);

        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(keyBytes);
            if (bytes != null) {
                // 反序列化 byte[] 为 Object
                return deserialize(bytes);
            }
            return null;
        } catch (Exception e) {
            log.e(e, "getObject error for key: " + key);
            return null;
        }
    }

    @Override
    public void setMapObject(String key, String subKey, Object val, int second) {
        if (key == null || subKey == null || val == null || key.isEmpty() || subKey.isEmpty()) {
            return;
        }
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] subKeyBytes = subKey.getBytes(UTF_8);
        byte[] valBytes = serialize(val);
        if (valBytes == null) return;

        try (Jedis jedis = jedisPool.getResource()) {
            // HSET 是原子操作
            jedis.hset(keyBytes, subKeyBytes, valBytes);
            if (second > 0) {
                // EXPIRE 不是原子操作，但在客户端层面通常可以接受
                jedis.expire(keyBytes, second);
            }
        } catch (Exception e) {
            log.e(e, "setMapObject error for key: " + key + ", subKey: " + subKey);
        }
    }

    @Override
    public <T> T getMapObject(String key, String subKey) {
        if (key == null || subKey == null || key.isEmpty() || subKey.isEmpty()) return null;
        byte[] keyBytes = key.getBytes(UTF_8);
        byte[] subKeyBytes = subKey.getBytes(UTF_8);

        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.hget(keyBytes, subKeyBytes);
            if (bytes != null) {
                // 反序列化 byte[] 为 Object
                return deserialize(bytes);
            }
            return null;
        } catch (Exception e) {
            log.e(e, "getMapObject error for key: " + key + ", subKey: " + subKey);
            return null;
        }
    }

}