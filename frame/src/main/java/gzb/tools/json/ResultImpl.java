package gzb.tools.json;

import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultImpl implements Result, java.io.Serializable {

    private static final long serialVersionUID = 1256544882241L;
    public static final Log log = new LogImpl();
    Map<String, Object> rootMap;
    static Integer successVal;
    static Integer failVal;
    static Integer errorVal;
    static Integer jumpVal;
    static String stateName;
    static String messageName;
    static String timeName;
    static String dataName;
    static String urlName;
    static String pageName;
    static String sizeName;
    static String totalName;

    static {
        stateName = Config.get("json.code", "code");
        messageName = Config.get("json.message", "message");
        dataName = Config.get("json.data", "data");
        timeName = Config.get("json.time", "time");
        urlName = Config.get("json.jump", "url");
        pageName = Config.get("json.page", "page");
        sizeName = Config.get("json.size", "size");
        totalName = Config.get("json.total", "total");
        successVal = Config.getInteger("json.code.success", 1);
        failVal = Config.getInteger("json.code.fail", 2);
        errorVal = Config.getInteger("json.code.error", 3);
        jumpVal = Config.getInteger("json.code.jump", 4);
    }


    public ResultImpl() {
        this(new HashMap<String, Object>());
    }
    public ResultImpl(String jsonString) {
        this(Tools.jsonToMap(jsonString));
    }
    public ResultImpl(Map<String, Object> rootMap) {
        this.rootMap = rootMap;
    }

    @Override
    public Map<String, Object> getRootMap() {
        return rootMap;
    }

    @Override
    public Result setRootMap(Map<String, Object> rootMap) {
        this.rootMap = rootMap;
        return this;
    }

    @Override
    public Object get(String key) {
        return get(key, null);
    }

    @Override
    public String getString(String key) {
        return getString(key, null);
    }

    @Override
    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    @Override
    public Long getLong(String key) {
        return getLong(key, null);
    }

    @Override
    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    @Override
    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    @Override
    public Float getFloat(String key) {
        return getFloat(key,null);
    }

    @Override
    public Short getShort(String key) {
        return getShort(key,null);
    }

    @Override
    public <T> T getObject(String key) {
        return getObject(key, null);
    }

    @Override
    public Object get(String key, Object def) {
        Object obj = rootMap.get(key);
        if (obj == null) {
            return def;
        }
        return obj;
    }

    @Override
    public String getString(String key, String def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return obj.toString();
        } catch (Exception e) {
            log.e(e, "Result getString error ", key, def);
            return def;
        }
    }

    @Override
    public Integer getInteger(String key, Integer def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Integer.parseInt(obj.toString().split("\\.")[0]);
        } catch (Exception e) {
            log.e(e, "Result getInteger error ", key, def);
            return def;
        }
    }

    @Override
    public Long getLong(String key, Long def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Long.parseLong(obj.toString().split("\\.")[0]);
        } catch (Exception e) {
            log.e(e, "Result getLong error ", key, def);
            return def;
        }
    }

    @Override
    public Double getDouble(String key, Double def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result Double error ", key, def);
            return def;
        }
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Boolean.parseBoolean(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getBoolean error ", key, def);
            return def;
        }
    }

    @Override
    public Float getFloat(String key, Float def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getFloat error ", key, def);
            return def;
        }
    }

    @Override
    public Short getShort(String key, Short def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return Short.parseShort(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getShort error ", key, def);
            return def;
        }
    }

    @Override
    public <T> T getObject(String key, T def) {
        try {
            Object obj = get(key);
            if (obj == null) {
                return def;
            }
            return (T) obj;
        } catch (Exception e) {
            log.e(e, "Result getObject error ", key, def);
            return def;
        }
    }

    @Override
    public Result set(String key, Object value) {
        rootMap.put(key, value);
        return this;
    }

    @Override
    public Result response(Integer code, String msg, Object data) {
        set(stateName, code);
        set(messageName, msg);
        set(dataName, data);
        return this;
    }

    @Override
    public Result success() {
        response(successVal, "成功", null);
        return this;
    }

    @Override
    public Result fail() {
        response(failVal, "失败", null);
        return this;
    }

    @Override
    public Result error() {
        response(errorVal, "错误", null);
        return this;
    }

    @Override
    public Result jump() {
        response(jumpVal, "跳转", null);
        return this;
    }

    @Override
    public Result success(String msg) {
        response(successVal, msg, null);
        return this;
    }

    @Override
    public Result success(String msg, Object data) {
        response(successVal, msg, data);
        return this;
    }

    @Override
    public Result fail(String msg) {
        response(failVal, msg, null);
        return this;
    }

    @Override
    public Result error(String msg) {
        response(errorVal, msg, null);
        return this;
    }

    @Override
    public Result jump(String msg) {
        response(jumpVal, msg, null);
        return this;
    }

    @Override
    public Result jump(String msg, String url) {
        response(jumpVal, msg, url);
        return this;
    }

    @Override
    public boolean isSuccess() {
        return isEquals(successVal);
    }

    @Override
    public boolean isFail() {
        return isEquals(failVal);
    }

    @Override
    public boolean isError() {
        return isEquals(errorVal);
    }

    @Override
    public boolean isJump() {
        return isEquals(jumpVal);
    }

    @Override
    public boolean isEquals(int val) {
        return Objects.equals(val, getInteger(stateName, -1));
    }

    @Override
    public DateTime getDateTime(String key, String format, DateTime def) {
        try {
            String string = getString(key);
            if (string != null) {
                if (string.length() == 10) {
                    return new DateTime(Integer.parseInt(string));
                } else if (string.length() == 13) {
                    return new DateTime(Long.parseLong(string));
                } else {
                    if (format == null) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    }
                    return new DateTime(string, format);
                }

            }
        } catch (Exception e) {
         log.e("getDateTime",e);
        }
        return def;
    }

    @Override
    public String[] getStringArray(String key, String split, String[] def) {
        try {
            String string = getString(key);
            if (string != null) {
                return string.split(split);
            }
        } catch (Exception e) {
            log.e("getStringArray",e);
        }
        return def;
    }

    @Override
    public String[] getStringMid(String key, String q, String h, String[] def) {
        try {
            String string = getString(key);
            if (string != null) {
                return Tools.textMid(string, q, h).toArray(new String[]{});
            }
        } catch (Exception e) {
            log.e("getStringMid",e);
        }
        return def;
    }

    @Override
    public String getStringMidIndex(String key, String q, String h, Integer index, String def) {
        try {
            String string = getString(key);
            if (string != null) {
                List<String> list = Tools.textMid(string, q, h);
                if (index < list.size()) {
                    return list.get(index);
                }
            }
        } catch (Exception e) {
            log.e("getStringMidIndex",e);
        }
        return def;
    }

    @Override
    public String toJson() {
        return Tools.toJson(rootMap);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
