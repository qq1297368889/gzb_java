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

import gzb.tools.DateTime;

import java.sql.Timestamp;
import java.util.Map;

public interface Result {
    Map<String,Object> getRootMap();
    Result setRootMap(Map<String,Object> rootMap);

    Object get(String key);
    String getString(String key);
    Integer getInteger(String key);
    Long getLong(String key);
    Double getDouble(String key);
    Boolean getBoolean(String key);
    Float getFloat(String key);
    Short getShort(String key);
    <T> T getObject(String key);

    Object get(String key,Object def);
    String getString(String key,String def);
    Integer getInteger(String key,Integer def);
    Long getLong(String key,Long def);
    Double getDouble(String key,Double def);
    Boolean getBoolean(String key,Boolean def);
    Float getFloat(String key,Float def);
    Short getShort(String key,Short def);


    <T> T getObject(String key,T t);

    Result set(String key,Object value);

    Result response(Integer code,String msg,Object data);

    Result success();
    Result fail();
    Result error();
    Result jump();

    Result success(String msg);
    Result success(String msg, Object data);
    Result fail(String msg);
    Result error(String msg);
    Result jump(String msg);
    Result jump(String msg,String url);

    boolean isSuccess();
    boolean isFail();
    boolean isError();
    boolean isJump();
    boolean isEquals(int val);
    DateTime getDateTime(String key, String format, DateTime def);
    String[] getStringArray(String key, String split, String[] def);
    String[] getStringMid(String key, String q, String h, String[] def);
    String getStringMidIndex(String key, String q, String h, Integer index, String def);
    String toJson();

    public java.sql.Timestamp getTimestamp(String key);
    public java.sql.Timestamp getTimestamp(String key,java.sql.Timestamp def);

}
