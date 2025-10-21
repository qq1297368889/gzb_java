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

import gzb.tools.AES_CBC_128;
import gzb.tools.Config;
import gzb.tools.Tools;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class ResponseDefaultImpl implements Response {
    private HttpResponseStatus status;
    private Map<String, String> headers;
    private String charset;
    private ChannelHandlerContext ctx;
    private Set<Cookie> cookies; // Added cookies field
    private boolean isSendHeaders = false;

    public ResponseDefaultImpl(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Response setStatus(int status) {
        this.status = HttpResponseStatus.valueOf(status);
        return this;
    }

    public Response setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 发送响应头。这个方法只会被调用一次。
     */
    private Response sendHeaders() {
        if (!isSendHeaders) {
            isSendHeaders = true;
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);
            //response.headers().set("server", Config.frameName);
            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    response.headers().set(entry.getKey(), entry.getValue());
                }
            }
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    response.headers().set("Set-Cookie", encodeSingleCookie(cookie));
                }
            }
            ctx.writeAndFlush(response);
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
            HttpContent content = new DefaultHttpContent(Unpooled.copiedBuffer(chunk));
            ctx.write(content);
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
        ctx.flush();
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        return this;
    }

    public Response sendAndFlush(Object chunk) {
        byte[] bytes;
        if (chunk instanceof byte[]) {
            bytes = (byte[]) chunk;
        } else if (chunk instanceof String) {
            bytes = ((String) chunk).getBytes(Config.encoding);
        } else if (chunk == null) {
            bytes = new byte[0];
        } else {
            String str1 = Tools.toJson(chunk);
            if (str1 == null) {
                bytes = new byte[0];
            } else {
                bytes = str1.getBytes(Config.encoding);
            }
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.valueOf(200),
                Unpooled.wrappedBuffer(bytes));
        //response.headers().set("server", Config.frameName);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                response.headers().set(entry.getKey(), entry.getValue());
            }
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                response.headers().set("Set-Cookie", encodeSingleCookie(cookie));
            }
        }
        response.headers().set("content-length", bytes.length);
        ctx.writeAndFlush(response);
        return this;
    }

    /**
     * 将单个 Cookie 编码为 "key=value; Path=/; ..." 格式的字符串。
     *
     * @param cookie 要编码的单个 Cookie。
     * @return 编码后的字符串。
     */
    private static String encodeSingleCookie(Cookie cookie) {
        StringBuilder sb = new StringBuilder();
        sb.append(cookie.name()).append('=').append(cookie.value());

        if (cookie.path() != null) {
            sb.append("; Path=").append(cookie.path());
        }
        if (cookie.domain() != null) {
            sb.append("; Domain=").append(cookie.domain());
        }
        if (cookie.maxAge() >= 0) {
            sb.append("; Max-Age=").append(cookie.maxAge());
        }
        if (cookie.isSecure()) {
            sb.append("; Secure");
        }
        if (cookie.isHttpOnly()) {
            sb.append("; HttpOnly");
        }

        // Netty 的 DefaultCookie 还有一个 wrap 参数，
        // 如果你需要支持它，可以在这里添加逻辑
        // if (cookie.isWrapped()) { ... }

        return sb.toString();
    }
    // --- Cookie Management Methods ---

    public Response setCookie(String key, String val, int mm) {
        return setCookie(key, val, mm, null, "/", true, false);
    }

    /**
     * Sets a cookie with all available parameters.
     * If a cookie with the same key already exists, it will be updated.
     */
    public Response setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly, boolean secure) {
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

    public Response success(byte[] body) {
        return sendAndFlush(body);
    }

    public Response success(String body) {
        return sendAndFlush(body.getBytes(Config.encoding));
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
