package gzb.tools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GzbMap implements Serializable {
    public Map<String, Object> map = new HashMap<>();
    public String tableName;
    public String tableId;

    public Object get(String key) {
        return get(key,null);
    }
    public Map<?,?> getMap(String key) {
        return getMap(key, null);
    }
    public Integer getInteger(String key) {
        return getInteger(key, null);
    }

    public Long getLong(String key) {
        return getLong(key, null);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public Double getDouble(String key) {
        return getDouble(key, null);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public DateTime getDateTime(String key) {
        return getDateTime(key, null, null);
    }

    public DateTime getDateTime(String key, String format) {
        return getDateTime(key, format, null);
    }

    public DateTime getDateTime(String key, DateTime def) {
        return getDateTime(key, null, def);
    }

    public <T>T getObject(String key,T def) {
        Object object = get(key);
        if (object != null) {
            return (T)object;
        }
        return def;
    }
    public <T>T getObject(String key) {
        return getObject(key,null);
    }
    public GzbMap getGzbMap(String key, GzbMap def) {
        Object object = get(key);
        if (object != null) {
            return (GzbMap)object;
        }
        return def;
    }
    public Map<?,?> getMap(String key, Map<?,?> def) {
        Object object = get(key);
        if (object != null) {
            return (Map<?,?>)object;
        }
        return def;
    }
    public Integer getInteger(String key, Integer def) {
        String string = getString(key);
        if (string != null && string.length() > 0 && string.length() < 11) {
            return Integer.valueOf(string);
        }
        return def;
    }

    public Long getLong(String key, Long def) {
        String string = getString(key);
        if (string != null && string.length() > 0 && string.length() < 21) {
            return Long.valueOf(string);
        }
        return def;
    }

    public Boolean getBoolean(String key, Boolean def) {
        String string = getString(key);
        if (string != null) {
            if (string.equals("1")) {
                string = "true";
            } else if (string.equals("0")) {
                string = "false";
            }
            return Boolean.valueOf(string);
        }
        return def;
    }

    public Double getDouble(String key, Double def) {
        String string = getString(key);
        if (string != null) {
            return Double.valueOf(string);
        }
        return def;
    }
    public Object get(String key, String def) {
        try {
            Object object = map.get(key);
            if (object != null) {
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public String getString(String key, String def) {
        try {
            Object object = map.get(key);
            if (object != null) {
                return String.valueOf(object.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public DateTime getDateTime(String key, String format, DateTime def) {
        try {
            String string = getString(key);
            if (string != null) {
                if (string.length() == 10) {
                    return new DateTime(Integer.valueOf(string));
                } else if (string.length() == 13) {
                    return new DateTime(Long.valueOf(string));
                } else {
                    if (format == null) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    }
                    return new DateTime(string, format);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public String[] getStringArray(String key, String split, String[] def) {
        try {
            String string = getString(key);
            if (string != null) {
                return string.split(split);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public String[] getStringMid(String key, String q, String h, String[] def) {
        try {
            String string = getString(key);
            if (string != null) {
                return Tools.textMid(string, q, h).toArray(new String[]{});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

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
            e.printStackTrace();
        }
        return def;
    }

    public Object set(String key, Object val) {
        return map.put(key, val);
    }

    public Object remove(String key) {
        return map.remove(key);
    }

    public Object put(String key, Object val) {
        return set(key, val);
    }

    public Object del(String key) {
        return remove(key);
    }

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return Tools.toJson(map);
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public GzbMap setMap(Map<String, Object> map) {
        this.map = map;
        return this;
    }
    public GzbMap setMap(Object map) {
        this.map = (Map<String, Object>)map;
        return this;
    }
}
