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

package gzb.tools.cache.session;

import gzb.frame.netty.entity.Response;
import gzb.tools.Config;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;

public class SessionImpl implements Session {
    public static GzbCache gzbCache = Cache.session;
    private String id;
    private Response response;
    private boolean sendCookie;

    /**
     * 用于创建新会话的构造函数。
     * 此时没有传入令牌，ID 将在第一次需要时生成。
     *
     * @param response HTTP 响应对象
     */
    public SessionImpl(Response response) {
        this.id = null;
        this.response = response;
    }

    /**
     * 用于加载现有会话的构造函数。
     * 传入令牌，会话 ID 即为该令牌。
     *
     * @param token    传入的会话令牌
     * @param response HTTP 响应对象
     */
    public SessionImpl(String token, Response response, Boolean sendCookie) {
        this.id = token;
        this.response = response;
        if (sendCookie == null) {
            this.sendCookie = true;
        } else {
            this.sendCookie = sendCookie;
        }
    }

    /**
     * 生成一个新的唯一的会话 ID。
     *
     * @return 新生成的会话 ID 字符串
     */
    private String getNewSessionId() {
        return OnlyId.getDistributedString() + Tools.getRandomString(15);
    }

    public Session sendHeader() {
        response.setHeader("token", getId());
        return this;
    }

    /**
     * 获取当前会话的 ID。如果 ID 不存在，则生成一个新 ID 并设置到响应的 Cookie 中。
     *
     * @return 会话 ID
     */
    @Override
    public String getId() {
        if (this.id == null || this.id.length() != 32) {
            // 修复了逻辑错误：先生成 ID，再使用它设置 Cookie。
            this.id = getNewSessionId();
            if (sendCookie) {
                response.setCookie("token", this.id, Config.sessionTime);
            }
        }
        return this.id;
    }

    /**
     * 向会话中存入一个键值对。
     *
     * @param key 要存入的键
     * @param val 要存入的值
     */
    @Override
    public Session put(String key, String val) {
        gzbCache.setMap(getId(), key, val, Config.sessionTime);
        return this;
    }

    /**
     * 从会话中获取一个键对应的整数值。
     *
     * @param key 要获取的键
     * @return 键对应的整数值，如果不存在或转换失败则返回 null
     */
    @Override
    public Integer getInt(String key) {
        String str = getString(key);
        if (str == null) {
            return null;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            // 如果值不是有效的数字，返回 null
            return null;
        }
    }

    /**
     * 从会话中获取一个键对应的字符串值。
     *
     * @param key 要获取的键
     * @return 键对应的字符串值，如果不存在则返回 null
     */
    @Override
    public String getString(String key) {
        return gzbCache.getString(getId(), key, null);
    }

    /**
     * 从会话中获取一个键对应的对象值。
     *
     * @param key 要获取的键
     * @return 键对应的对象，如果不存在则返回 null
     */
    @Override
    public Object getObject(String key) {
        return gzbCache.get(getId(), key);
    }

    /**
     * 从会话中删除一个键值对。
     *
     * @param key 要删除的键
     * @return 被删除的对象
     */
    @Override
    public Object delete(String key) {
        return gzbCache.del(getId(), key);
    }

    /**
     * 对会话中的一个键执行原子自增操作。
     * 注意：此方法依赖于底层缓存实现是否提供原子操作。
     * 如果 GzbCache 有 getIncr 方法，应直接调用。
     *
     * @param key 要自增的键
     * @return 自增后的值
     */
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

    /**
     * 删除整个会话。
     */
    @Override
    public Session delete() {
        gzbCache.del(getId());
        return this;
    }
}