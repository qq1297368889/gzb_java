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

package gzb.frame.netty.entity;

import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.thread.GzbThreadLocal;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.text.SimpleDateFormat;
import java.util.*;

public class ResponseHTTPImpl implements Response {
    private HttpResponseStatus status;
    private Map<String, String> headers;
    private String charset;
    private ChannelHandlerContext ctx;
    private Set<Cookie> cookies; // Added cookies field
    private boolean isSendHeaders = false;
    private FullHttpRequest request = null;

    boolean keepAlive = false;

    public ResponseHTTPImpl(ChannelHandlerContext ctx, boolean keepAlive) {
        this.ctx = ctx;
        this.keepAlive = keepAlive;

    }

    /**
     * 发送响应头。这个方法只会被调用一次。
     */
    private Response sendHeaders() {
        if (!isSendHeaders) {
            isSendHeaders = true;
            GzbThreadLocal.Entity entity0 = GzbThreadLocal.context.get();
            int index = entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder data = entity0.stringBuilderCacheEntity.get(index);
                //data.append("Server: ").append(Config.frameName).append("\r\n");
                if (keepAlive) {
                    //data.append("keep-alive: keep-alive\r\n");
                } else {
                    data.append("keep-alive: close\r\n");
                }
                //data.append("Date: ").append(NettyTools.THIS_TIME).append("\r\n");
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        data.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
                    }
                }
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        data.append("Set-Cookie: ").append(NettyTools.encodeSingleCookie(cookie)).append("\r\n");
                    }
                }
                data.append("transfer-encoding: chunked\r\n\r\n");
                ctx.write(Unpooled.wrappedBuffer(data.toString().getBytes()));
            } finally {
                entity0.stringBuilderCacheEntity.close(index);
            }
        }
        return this;
    }

    /**
     * 向响应体写入一个数据块。
     *
     * @param chunk 数据块
     */
    public Response write(byte[] chunk) {
        if (!isSendHeaders) {
            sendHeaders();
        }
        if (chunk != null) {
            ctx.write(chunk);
        }
        return this;
    }

    /**
     * 结束响应流，并发送最后一个数据块和末尾标志。
     */
    public Response flush() {
        if (!isSendHeaders) {
            sendHeaders();
        }
        ChannelFuture future = ctx.writeAndFlush(Unpooled.wrappedBuffer(NettyTools.send_end));
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        return this;
    }

    public Response sendAndFlush(Object chunk) {
        GzbThreadLocal.Entity entity0 = GzbThreadLocal.context.get();
        int index = entity0.stringBuilderCacheEntity.open();
        try {
            StringBuilder data = entity0.stringBuilderCacheEntity.get(index);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    data.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
                }
            }
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    data.append("Set-Cookie: ").append(NettyTools.encodeSingleCookie(cookie)).append("\r\n");
                }
            }
            NettyTools.sendHTTP(ctx, chunk, 200, data.toString(), keepAlive);
            return this;
        } finally {
            entity0.stringBuilderCacheEntity.close(index);
        }

    }

    public Response setStatus(int status) {
        this.status = HttpResponseStatus.valueOf(status);
        return this;
    }

    public Response setCharset(String charset) {
        this.charset = charset;
        return this;
    }


    public Response setCookie(String key, String val, int mm) {
        return setCookie(key, val, mm, null, "/", true, false);
    }

    public Response setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly,
                              boolean secure) {
        if (key == null || val == null) {
            return this;
        }
        if (cookies == null) {
            cookies = new HashSet<>();
        }

        // Check if cookie already exists
        Cookie existingCookie = null;
        for (Cookie cookie : cookies) {
            if (cookie.name().equals(key)) {
                existingCookie = cookie;
                break;
            }
        }

        // Remove existing cookie to replace it
        if (existingCookie != null) {
            cookies.remove(existingCookie);
        }

        // Create a new cookie with the updated parameters
        Cookie newCookie = new DefaultCookie(key, val);
        newCookie.setMaxAge(maxAge);
        if (domain != null) {
            newCookie.setDomain(domain);
        }
        if (path != null) {
            newCookie.setPath(path);
        }
        newCookie.setHttpOnly(httpOnly);
        newCookie.setSecure(secure);

        cookies.add(newCookie);
        return this;
    }

    public Response success(Object chunk) {
        return sendAndFlush(chunk);
    }

    public Response fail() {
        this.status = HttpResponseStatus.valueOf(403);
        return this;
    }

    public Response error() {
        this.status = HttpResponseStatus.valueOf(500);
        return this;
    }

    public Response setHeader(String key, String value, String... keyAndVal) {
        if (key != null) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key.toLowerCase(), value);
            if (keyAndVal != null && keyAndVal.length % 2 == 0) {
                for (int i = 0; i < keyAndVal.length; i = i + 2) {
                    headers.put(keyAndVal[i].toLowerCase(), keyAndVal[i + 1]);
                }
            }
        }
        return this;
    }

    public Response setContentType(String value) {
        String key = "content-type";
        if (value != null) {
            if (headers == null) {
                headers = new HashMap<>();
            }
            headers.put(key.toLowerCase(), value);
        }
        return this;
    }

    public Response setContentLengthLong(int value) {
        String key = "content-length";
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key.toLowerCase(), value + "");
        return this;
    }

    /**
     * 模拟setDateHeader方法
     *
     * @param name 响应头名称
     * @param date 时间戳(自1970-01-01 00:00:00 GMT以来的毫秒数)
     */
    public void setDateHeader(String name, long date) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("响应头名称不能为空");
        }
        SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss zzz",
                Locale.US // 必须使用英文Locale，否则月份和星期会是中文
        );
        HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));

        // 将时间戳转换为Date对象
        Date dateObj = new Date(date);

        // 格式化为HTTP标准日期字符串
        String formattedDate = HTTP_DATE_FORMAT.format(dateObj);

        if (headers == null) {
            headers = new HashMap<>();
        }
        // 存储到响应头中
        headers.put(name, formattedDate);
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public Response setStatus(HttpResponseStatus status) {
        this.status = status;
        return this;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    public Response setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public Response setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        return this;
    }
}
