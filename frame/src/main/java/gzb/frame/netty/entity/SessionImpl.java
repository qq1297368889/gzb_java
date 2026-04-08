package gzb.frame.netty.entity;

import gzb.frame.netty.tools.HTTPRequestParameters;
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.net.InetSocketAddress;
import java.util.*;

public class SessionImpl implements Session {
    private ChannelHandlerContext ctx;
    private static final HttpResponseStatus[] httpResponseStatuses = new HttpResponseStatus[]{
            HttpResponseStatus.valueOf(200),
            HttpResponseStatus.valueOf(400),
            HttpResponseStatus.valueOf(403),
            HttpResponseStatus.valueOf(404),
            HttpResponseStatus.valueOf(500)
    };


    private boolean isSendHeaders = false;
    private Map<String, String> sendHeaders;
    private Set<Cookie> sendCookies;
    private final Map<String, List<Object>> parameter;


    private String clientIp = null;
    private int clientPort = 0;
    private String localIp = null;
    private int localPort = 0;
    private String sign = null;

    private void sendHeaders(HttpResponseStatus state) {
        if (!isSendHeaders) {
            isSendHeaders = true;
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, state);
            response.headers().set("server", Config.frameName);
            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            if (sendHeaders != null) {
                for (Map.Entry<String, String> entry : sendHeaders.entrySet()) {
                    response.headers().set(entry.getKey(), entry.getValue());
                }
            }
            if (sendCookies != null) {
                for (Cookie cookie : sendCookies) {
                    response.headers().set("Set-Cookie", NettyTools.encodeSingleCookie(cookie));
                }
            }
            ctx.writeAndFlush(response);
        }
    }

    private HttpResponseStatus getHttpResponseStatus(int code) {
        if (code == 200) {
            return httpResponseStatuses[0];
        } else if (code == 400) {
            return httpResponseStatuses[1];
        } else if (code == 403) {
            return httpResponseStatuses[2];
        } else if (code == 404) {
            return httpResponseStatuses[3];
        } else if (code == 500) {
            return httpResponseStatuses[4];
        } else {
            return HttpResponseStatus.valueOf(code);
        }
    }

    public SessionImpl(ChannelHandlerContext ctx, Map<String, String> sendHeaders, Map<String, List<Object>> parameter) {
        this.ctx = ctx;
        this.sendHeaders = sendHeaders;
        this.parameter = parameter;
    }

    public void setCookie(String key, String val, int mm) {
        setCookie(key, val, mm, null, "/", true, false);
    }

    public void setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly, boolean secure) {
        if (key == null || val == null) {
            return;
        }
        if (sendCookies == null) {
            sendCookies = new HashSet<>();
        }
        Cookie existingCookie = null;
        for (Cookie cookie : sendCookies) {
            if (cookie.name().equals(key)) {
                existingCookie = cookie;
                break;
            }
        }

        if (existingCookie != null) {
            sendCookies.remove(existingCookie);
        }

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
        sendCookies.add(newCookie);
    }

    public void setHeaders(Map<String, String> headers) {
        this.sendHeaders = headers;
    }

    public void setHeaders(String key, String val) {
        if (sendHeaders == null) {
            setHeaders(new HashMap<>());
        }
        sendHeaders.put(key, val);
    }

    /// 适用于 tcp udp ws
    @Override
    public void send(Object chunk) {
        ctx.writeAndFlush(NettyTools.toByteBuf(chunk));
    }

    /// 自动封装单次响应 http格式
    @Override
    public void sendHttp(int code, Object chunk) {
        HttpResponseStatus state = getHttpResponseStatus(code);
        ByteBuf buf = NettyTools.toByteBuf(chunk);
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                state, buf);
        response.headers().set("content-length", buf.readableBytes());
        response.headers().set("server", Config.frameName);
        if (sendHeaders != null) {
            for (Map.Entry<String, String> entry : sendHeaders.entrySet()) {
                response.headers().set(entry.getKey(), entry.getValue());
            }
        }
        if (sendCookies != null) {
            for (Cookie cookie : sendCookies) {
                response.headers().set("Set-Cookie", NettyTools.encodeSingleCookie(cookie));
            }
        }
        ctx.writeAndFlush(response);
    }

    /// 适用于流式发送
    @Override
    public void sendHttpStream(byte[] data) {
        HttpResponseStatus state = httpResponseStatuses[0];
        sendHeaders(state);
        sendHttpStream(Unpooled.copiedBuffer(data));
    }

    @Override
    public void sendHttpStream(ByteBuf data) {
        ctx.writeAndFlush(new DefaultHttpContent(data));
    }

    /// 流式响应结束
    @Override
    public void flushHttp() {
        ctx.flush();
        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    /// 获取客户端信息 和 本地临时端口信息
    @Override
    public String getClientIp() {
        if (clientIp == null) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            clientIp = address.getAddress().getHostAddress();
        }
        return clientIp;
    }

    @Override
    public int getClientPort() {
        if (clientPort < 1) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
            clientPort = address.getPort();
        }
        return clientPort;
    }

    /// 获取客户端信息 和 本地临时端口信息
    @Override
    public String getLocalIp() {
        if (localIp == null) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
            localIp = address.getAddress().getHostAddress();
        }
        return localIp;
    }

    @Override
    public int getLocalPort() {
        if (localPort < 1) {
            InetSocketAddress address = (InetSocketAddress) ctx.channel().localAddress();
            localPort = address.getPort();
        }
        return localPort;
    }

    /// 会话签名 同会话唯一
    @Override
    public String getSign() {
        if (sign == null) {
            sign = ctx.channel().id().asLongText();
        }
        return sign;
    }

    /// 统一抽象 http的协议头 cookie 表单 json 都视为 参数统一抽象为该格式 http tcp ws udp 统一 需要注意重名问题 并且规避
    @Override
    public Map<String, List<Object>> getParameter() {
        return parameter;
    }

    @Override
    public List<Object> getParameterList(String key) {
        Map<String, List<Object>> map = getParameter();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    @Override
    public Object getParameterObject(String key) {
        List<Object> list = getParameterList(key);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public String getParameterString(String key) {
        List<Object> list = getParameterList(key);
        if (list == null || list.size() == 0) {
            return null;
        }
        Object obj = getParameterObject(key);
        if (obj == null) {
            return null;
        }
        return obj instanceof String ? (String) obj : obj.toString();
    }
}
