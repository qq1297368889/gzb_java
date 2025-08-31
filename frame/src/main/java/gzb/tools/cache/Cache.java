package gzb.tools.cache;

import gzb.tools.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static GzbCache gzbCache;
    public static GzbCache session;
    public static Map<String, Object> gzbMap;

    static {
        gzbMap=new ConcurrentHashMap<>();
        String type = Config.get("gzb.system.cache.type", "map");
        if (type.equals("mysql")) {
            try {
                throw new Exception("mysql 缓存暂时不支持");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("redis")) {
            gzbCache = new GzbCacheRedis();
        } else {
            gzbCache = new GzbCacheMap();
        }
        type = Config.get("gzb.system.http.session.type", "map");
        if (type.equals("mysql")) {
            try {
                throw new Exception("mysql 缓存暂时不支持");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("redis")) {
            session = new GzbCacheRedis();
        } else {
            session = new GzbCacheMap();
        }
    }
}
