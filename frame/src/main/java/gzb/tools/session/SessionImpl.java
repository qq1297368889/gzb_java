package gzb.tools.session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gzb.tools.Config;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.cache.GzbCacheRedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class SessionImpl implements Session {
    public static GzbCache gzbCache = null;
    public static String prefix = null;
    public static int mm;
    String id;

    //有效 秒数 不支持 小于0的数值
    public static void main(String[] args) {
        GzbCache gzbCache = new GzbCacheRedis();
        String id = "123456id";
        String key = "123456k";
        String val = "123456v";
        Map<String, Object> map = gzbCache.getObject(id);
        if (map == null) {
            map = new HashMap<String, Object>();
            gzbCache.setObject(id, map);
        }
        map.put(key, val);
        System.out.println(map);
        map = gzbCache.getObject(id);
        System.out.println(map);

    }

    static {
        prefix = "session";
        String sessionType = Config.get("gzb.system.http.session.type", "map");
        mm = Config.getInteger("gzb.system.http.session.time", 3600 * 24);
        if (sessionType != null && sessionType.equals("redis")) {
            gzbCache = new GzbCacheRedis();
        } else if (sessionType != null && sessionType.equals("mysql")) {
            try {
                throw new Exception("mysql session 暂时不支持");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gzbCache = new GzbCacheMap();
        }
    }

    private String getKey(String... arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public SessionImpl(HttpServletRequest request, HttpServletResponse response) {
        if (request == null || response == null) {
            return;
        }
        id = request.getParameter("token");
        if (id == null || id.length() != 32) {
            id = request.getHeader("token");
        }
        if (id == null) {
            id = Tools.CookieGet("token", request);
        }
        if (id == null) {
            id = OnlyId.getDistributed() + "-" + Tools.getRandomString(12);
            Tools.CookieSet("token", id, mm, response, request);
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void put(String key, String val) {
        try {
            String jsonString = gzbCache.get(id);
            if (jsonString == null) {
                jsonString = "{}";
            }
            Map<String, String> map = new Gson().fromJson(jsonString, Map.class);
            if (map.get("_time_end") == null) {
                map.put("_time_start", String.valueOf(System.currentTimeMillis()));
                map.put("_time_end", String.valueOf(System.currentTimeMillis() + (mm * 1000L)));
                map.put("_time_mm", String.valueOf(mm));
            }
            map.put(getKey(prefix, key), val);
            if (mm > 0) {
                gzbCache.set(id, new Gson().toJson(map), (int) (Long.valueOf(map.get("_time_end").toString()) - System.currentTimeMillis()) / 1000);
            } else {
                gzbCache.set(id, new Gson().toJson(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
            gzbCache.del(key);
        }
    }

    @Override
    public Integer getInt(String key) {
        String str = getString(key);
        if (str == null) {
            return null;
        }
        return Integer.valueOf(str);
    }

    @Override
    public String getString(String key) {
        try {
            String jsonString = gzbCache.get(id);
            if (jsonString == null) {
                jsonString = "{}";
            }
            Map<String, Object> map = new Gson().fromJson(jsonString, Map.class);
            Object obj = map.get(getKey(prefix, key));
            if (obj == null) {
                return null;
            }
            return String.valueOf(obj);
        } catch (Exception e) {
            e.printStackTrace();
            gzbCache.del(key);
            return null;
        }
    }

    @Override
    public Object getObject(String key) {
        try {
            String jsonString = gzbCache.getObject(id);
            if (jsonString == null) {
                jsonString = "{}";
            }
            Map<String, Object> map = new Gson().fromJson(jsonString, Map.class);
            return map.get(getKey(prefix, key));
        } catch (Exception e) {
            e.printStackTrace();
            gzbCache.del(key);
            return null;
        }
    }

    @Override
    public Object delete(String key) {
        String jsonString = gzbCache.del(id);
        if (jsonString == null) {
            jsonString = "{}";
        }
        Map<String, Object> map = new Gson().fromJson(jsonString, Map.class);
        return map.remove(getKey(prefix, key));
    }

    @Override
    public int getIncr(String key) {
        Integer a01 = getInt(key);
        if (a01 == null) {
            a01 = 0;
        }
        int a02 = a01 + 1;
        put(key, a02 + "");
        return a02;
    }

    @Override
    public void delete() {
        gzbCache.del(id);
    }


}
