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

package gzb.tools.json;

import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResultImpl implements Result, java.io.Serializable {

    public static Log log= Config.log;
    private static final long serialVersionUID = 1256544882241L;
    Map<String, Object> rootMap;

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
        Object obj = get(key);
        try {
            if (obj == null) {
                return def;
            }
            return obj.toString();
        } catch (Exception e) {
            log.e(e, "Result getString error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Integer getInteger(String key, Integer def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getInteger error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Long getLong(String key, Long def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getLong error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Double getDouble(String key, Double def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result Double error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Boolean.parseBoolean(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getBoolean error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Float getFloat(String key, Float def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getFloat error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Short getShort(String key, Short def) {
        Object obj = get(key);
        try {
            if (obj == null || obj.toString().isEmpty()) {
                return def;
            }
            return Short.parseShort(obj.toString());
        } catch (Exception e) {
            log.e(e, "Result getShort error ", key,obj, def);
            return def;
        }
    }

    @Override
    public <T> T getObject(String key, T def) {
        Object obj = get(key);
        try {
            if (obj == null) {
                return def;
            }
            return (T) obj;
        } catch (Exception e) {
            log.e(e, "Result getObject error ", key,obj, def);
            return def;
        }
    }

    @Override
    public Result set(String key, Object value) {
        if (key!=null&&value!=null) {
            rootMap.put(key, value);
        }
        return this;
    }

    @Override
    public Result response(Integer code, String msg, Object data) {
        set(Config.stateName, code);
        set(Config.messageName, msg);
        set(Config.dataName, data);
        return this;
    }

    @Override
    public Result success() {
        response(Config.successVal, "成功", null);
        return this;
    }

    @Override
    public Result fail() {
        response(Config.failVal, "失败", null);
        return this;
    }

    @Override
    public Result error() {
        response(Config.errorVal, "错误", null);
        return this;
    }

    @Override
    public Result jump() {
        response(Config.jumpVal, "跳转", null);
        return this;
    }

    @Override
    public Result success(String msg) {
        response(Config.successVal, msg, null);
        return this;
    }

    @Override
    public Result success(String msg, Object data) {
        response(Config.successVal, msg, data);
        return this;
    }

    @Override
    public Result fail(String msg) {
        response(Config.failVal, msg, null);
        return this;
    }

    @Override
    public Result error(String msg) {
        response(Config.errorVal, msg, null);
        return this;
    }

    @Override
    public Result jump(String msg) {
        response(Config.jumpVal, msg, null);
        return this;
    }

    @Override
    public Result jump(String msg, String url) {
        response(Config.jumpVal, msg, url);
        return this;
    }

    @Override
    public boolean isSuccess() {
        return isEquals(Config.successVal);
    }

    @Override
    public boolean isFail() {
        return isEquals(Config.failVal);
    }

    @Override
    public boolean isError() {
        return isEquals(Config.errorVal);
    }

    @Override
    public boolean isJump() {
        return isEquals(Config.jumpVal);
    }

    @Override
    public boolean isEquals(int val) {
        return Objects.equals(val, getInteger(Config.stateName, -1));
    }

    @Override
    public DateTime getDateTime(String key, String format, DateTime def) {
        String string = getString(key);
        try {
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
            log.e(e, "Result getDateTime error ", key,string, def);
        }
        return def;
    }

    @Override
    public String[] getStringArray(String key, String split, String[] def) {
        String string = getString(key);
        try {
            if (string != null) {
                return string.split(split);
            }
        } catch (Exception e) {
            log.e(e, "Result getStringArray error ", key,string, def);
        }
        return def;
    }

    @Override
    public String[] getStringMid(String key, String q, String h, String[] def) {
        String string = getString(key);
        try {
            if (string != null) {
                return Tools.textMid(string, q, h).toArray(new String[]{});
            }
        } catch (Exception e) {
            log.e(e, "Result getStringMid error ", key,string, def);
        }
        return def;
    }

    @Override
    public String getStringMidIndex(String key, String q, String h, Integer index, String def) {
        String string = getString(key);
        try {
            if (string != null) {
                List<String> list = Tools.textMid(string, q, h);
                if (index < list.size()) {
                    return list.get(index);
                }
            }
        } catch (Exception e) {
            log.e(e, "Result getStringMidIndex error ", key,string, def);
        }
        return def;
    }

    @Override
    public String toJson() {
        rootMap.put(Config.timeName,System.currentTimeMillis());
        return Tools.toJson(rootMap);
    }

    @Override
    public String toString() {
        return toJson();
    }
}
