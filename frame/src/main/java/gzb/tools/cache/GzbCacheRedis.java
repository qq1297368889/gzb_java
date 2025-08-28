package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.GzbMap;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GzbCacheRedis implements GzbCache {

    static Log log = new LogImpl(GzbCacheRedis.class);

    private static JedisPool jedisPool = null;

    static {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            String ip = Config.get("db.redis.ip", "127.0.0.1");
            Integer port = Config.getInteger("db.redis.port", 6379);
            String pwd = Config.get("db.redis.pwd", "ASHaxf129736#$_*?.");
            Integer maxIdle = Config.getInteger("db.redis.maxIdle", 10);
            Integer maxTotal = Config.getInteger("db.redis.maxTotal", 10);
            Integer overtime = Config.getInteger("db.redis.overtime", 5000);
            Integer dataBase = Config.getInteger("db.redis.dataBase", 0);
            jedisPoolConfig.setMaxTotal(maxTotal);
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPool = new JedisPool(
                    jedisPoolConfig,
                    ip,
                    port,
                    overtime, pwd, dataBase);

            log.i("redis,初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.e(e, "redis,初始化失败");
        }

    }



    public int getIncr(String key) {
        Jedis jedis = null;
        int id = 0;
        try {
            jedis = jedisPool.getResource();
            id = Integer.valueOf(jedis.incr(key).toString());
        } catch (Exception e) {
            log.e(e);
            id = 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return id;
    }

    public String get(String key) {
        return get(key, 0);
    }

    public String get(String key, int mm) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            if (mm > 0) {
                jedis.expire(key, mm);
            }
            return str;
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public <T> T getObject(String key) {
        return getObject(key, 0);
    }

    @Override
    public <T> T getObject(String key, int mm) {
        byte[] bytes = getObjectByte(key.getBytes(), mm);
        if (bytes == null) {
            return null;
        }
        return Tools.objectRestore(bytes);
    }

    public void set(String key, String val) {
        set(key, val, 0);
    }

    public void set(String key, String val, int mm) {
        Jedis jedis = null;
        if (key == null || val == null) {
            return;
        }
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, val);
            if (mm > 0) {
                jedis.expire(key, mm);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void setObject(String key, Object val) {
        setObjectByte(key.getBytes(), Tools.objectToByte(val), 0);
    }

    @Override
    public void setObject(String key, Object val, int mm) {
        setObjectByte(key.getBytes(), Tools.objectToByte(val), mm);
    }

    private void setObjectByte(byte[] k, byte[] v, int mm) {
        Jedis jedis = null;
        if (k == null || v == null) {
            return;
        }
        try {
            jedis = jedisPool.getResource();
            jedis.set(k, v);
            if (mm > 0) {
                jedis.expire(k, mm);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private byte[] deleteObjectByte(byte[] k) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(k);
            jedis.del(k);
            return bytes;
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    private byte[] getObjectByte(byte[] k, int mm) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] bytes = jedis.get(k);
            if (mm > 0) {
                jedis.expire(k, mm);
            }
            return bytes;
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String del(String key) {
        byte[] bytes = deleteObjectByte(key.getBytes());
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    @Override
    public <T> T delObject(String key) {
        byte[] bytes = deleteObjectByte(key.getBytes());
        if (bytes == null) {
            return null;
        }
        return Tools.objectRestore(bytes);
    }

}