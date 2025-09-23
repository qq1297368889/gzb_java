package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.log.Log;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

public class GzbCacheRedis implements GzbCache {
    public static Log log= Config.log;
    public JedisPool jedisPool = null;
    public GzbCacheRedis() {
        this(Config.get("db.cache.key", "cache1"));
    }

    public GzbCacheRedis(String key) {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            String ip = Config.get("db.redis." + key + ".ip", "127.0.0.1");
            Integer port = Config.getInteger("db.redis." + key + ".port", 6379);
            String pwd = Config.get("db.redis." + key + ".pwd", "123456");
            Integer maxIdle = Config.getInteger("db.redis." + key + ".maxIdle", 10);
            Integer maxTotal = Config.getInteger("db.redis." + key + ".maxTotal", 10);
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
        } catch (Exception e) {
            log.e(e, "GzbCacheRedis,初始化失败");
            throw new RuntimeException(e);
        }
    }

    // --- Core Operations ---

    @Override
    public int getIncr(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long result = jedis.incr(key);
            if (result != null) {
                return result.intValue();
            }
        } catch (Exception e) {
            log.e(e);
        }
        return 0;
    }

    @Override
    public int getIncr(String key, String mapKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long result = jedis.hincrBy(key, mapKey, 1);
            if (result != null) {
                return result.intValue();
            }
        } catch (Exception e) {
            log.e(e);
        }
        return 0;
    }

    @Override
    public Object get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public Object get(String key, String mapKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.hget(key, mapKey);
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public Object get(String key, int index) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> list = jedis.lrange(key, index, index);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public void set(String key, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setMap(String key, String mapKey, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, mapKey, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setList(String key, int index, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lset(key, index, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void set(String key, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, second, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setMap(String key, String mapKey, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(key, mapKey, Objects.toString(val));
            jedis.expire(key, second);
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setList(String key, int index, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lset(key, index, Objects.toString(val));
            jedis.expire(key, second);
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void add(String key, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public Object del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Object val = get(key); // 获取值以便返回
            jedis.del(key);
            return val;
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public Object del(String key, String mapKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            Object val = get(key, mapKey); // 获取值以便返回
            jedis.hdel(key, mapKey);
            return val;
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public Object del(String key, int index) {
        try (Jedis jedis = jedisPool.getResource()) {
            Object val = get(key, index); // 获取值以便返回
            // Redis 没有直接的 ldel 命令，需要 Lua 脚本或事务来保证原子性
            String script = "local val = redis.call('LINDEX', KEYS[1], ARGV[1])\n" +
                    "if val then\n" +
                    "  redis.call('LSET', KEYS[1], ARGV[1], '__DEL__')\n" +
                    "  redis.call('LREM', KEYS[1], 1, '__DEL__')\n" +
                    "end\n" +
                    "return val";
            jedis.eval(script, 1, key, String.valueOf(index));
            return val;
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    // --- 类型转换方法 ---
    private Integer getIntegerInternal(Object value) {
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
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.get(key.getBytes());
            return bytes != null ? bytes : defVal;
        } catch (Exception e) {
            log.e(e);
        }
        return defVal;
    }

    @Override
    public byte[] getByteArray(String key, String mapKey, byte[] defVal) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.hget(key.getBytes(), mapKey.getBytes());
            return bytes != null ? bytes : defVal;
        } catch (Exception e) {
            log.e(e);
        }
        return defVal;
    }

    @Override
    public byte[] getByteArray(String key, int index, byte[] defVal) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<byte[]> bytesList = jedis.lrange(key.getBytes(), index, index);
            if (bytesList != null && !bytesList.isEmpty()) {
                return bytesList.get(0);
            }
        } catch (Exception e) {
            log.e(e);
        }
        return defVal;
    }

    // --- 序列化方法 ---

    private byte[] serialize(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            log.e(e);
            return null;
        }
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (Exception e) {
            log.e(e);
            return null;
        }
    }

    @Override
    public void setObject(String key, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = serialize(val);
            if (bytes != null) {
                jedis.setex(key.getBytes(), second, bytes);
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setObject(String key, String mapKey, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = serialize(val);
            if (bytes != null) {
                jedis.hset(key.getBytes(), mapKey.getBytes(), bytes);
                jedis.expire(key, second);
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void setObject(String key, int index, Object val, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = serialize(val);
            if (bytes != null) {
                jedis.lset(key.getBytes(), index, bytes);
                jedis.expire(key, second);
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public void addObject(String key, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = serialize(val);
            if (bytes != null) {
                jedis.rpush(key.getBytes(), bytes);
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public <T> T getObject(String key, T defVal) {
        byte[] bytes = getByteArray(key, null);
        Object obj = deserialize(bytes);
        return obj != null ? (T) obj : defVal;
    }

    @Override
    public <T> T getObject(String key, String mapKey, T defVal) {
        byte[] bytes = getByteArray(key, mapKey, null);
        Object obj = deserialize(bytes);
        return obj != null ? (T) obj : defVal;
    }

    @Override
    public <T> T getObject(String key, int index, T defVal) {
        byte[] bytes = getByteArray(key, index, null);
        Object obj = deserialize(bytes);
        return obj != null ? (T) obj : defVal;
    }

    // --- 队列方法 ---

    @Override
    public void queueProduction(String key, Object val) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(key, Objects.toString(val));
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public Object queueConsumption(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(key);
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public Object queueConsumptionBlock(String key, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> result = jedis.brpop(second, key);
            if (result != null && !result.isEmpty()) {
                // BRPOP 返回一个列表，第一个元素是键名，第二个是值
                return result.get(1);
            }
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public <T> void queueProductionObject(String key, T val) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = serialize(val);
            if (bytes != null) {
                jedis.lpush(key.getBytes(), bytes);
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    @Override
    public <T> T queueConsumptionObject(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] bytes = jedis.rpop(key.getBytes());
            return (T) deserialize(bytes);
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }

    @Override
    public <T> T queueConsumptionBlockObject(String key) {
        return queueConsumptionBlockObject(key, 0); // 0表示无限期阻塞
    }

    @Override
    public <T> T queueConsumptionBlockObject(String key, int second) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<byte[]> bytesList = jedis.brpop(second, key.getBytes());
            if (bytesList != null && !bytesList.isEmpty()) {
                byte[] bytes = bytesList.get(1); // BRPOP 返回一个列表，第一个元素是键名，第二个是值
                return (T) deserialize(bytes);
            }
        } catch (Exception e) {
            log.e(e);
        }
        return null;
    }
}