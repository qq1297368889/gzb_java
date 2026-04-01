package gzb.frame.netty.entity;

import gzb.frame.factory.ClassTools;
import gzb.frame.netty.tools.HTTPRequestParameters;
import gzb.frame.netty.tools.OptimizedParameterParser;
import gzb.tools.Tools;
import gzb.tools.cache.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;

import java.net.InetSocketAddress;
import java.util.*;

public class RequestTcpImpl implements Request {
    private ChannelHandlerContext ctx;
    private PacketPromise packetPromise;
    private Response response;

    private Map<String,List<Object>> parameters;

    public RequestTcpImpl(ChannelHandlerContext ctx, PacketPromise packetPromise) {
        this.ctx = ctx;
        this.packetPromise = packetPromise;
        parameters = new HashMap<>();
        setParameters(parameters);
        this.response = new ResponseTcpImpl(ctx);
    }

    /**
     * 获取 协议头MAP
     */
    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * 获取日期类型响应头，转换为时间戳
     *
     * @param name 响应头名称
     * @return 时间戳(毫秒)，如果头不存在或解析失败返回-1
     */
    @Override
    public long getDateHeader(String name) {
        return 0;
    }

    /**
     * 获取 协议头 某个
     *
     * @param key
     */
    @Override
    public String getHeader(String key) {
        return "";
    }

    /**
     * 获取 COOKIE
     */
    @Override
    public Set<Cookie> getCookies() {
        return Collections.emptySet();
    }

    /**
     * 获取 底层对象
     */
    @Override
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    /**
     * 获取 参数解析对象
     */
    @Override
    public HTTPRequestParameters getRequestParameters() {
        return null;
    }

    /**
     * 获取 响应对象
     */
    @Override
    public Response getResponse() {
        return response;
    }

    /**
     * 获取 cookie 的 对象
     *
     * @param key
     */
    @Override
    public Cookie getCookie(String key) {
        return null;
    }

    /**
     * 获取 cookie 的 某个值
     *
     * @param key
     */
    @Override
    public String getCookieVal(String key) {
        return null;
    }

    /**
     * 获取 参数 某个元素 数组
     *
     * @param key
     */
    @Override
    public String[] getParameterArray(String key) {
        List<Object> list = parameters.get(key);
        if (list != null && !list.isEmpty()) {
            return list.toArray(new String[0]);
        }
        return null;
    }

    /**
     * 获取 参数 某个元素 字符串
     *
     * @param key
     */
    @Override
    public String getParameterVal(String key) {
        List<Object> list = parameters.get(key);
        if (list != null && !list.isEmpty()) {
            Object value = list.get(0);
            if (value != null) {
                if (value instanceof String) {
                    return (String) value;
                } else {
                    return value.toString();
                }
            }
        }
        return null;
    }


    /**
     * 获取 参数 MAP
     */
    @Override
    public Map<String, List<Object>> getParameter() {
        return parameters;
    }

    private void setParameters(Map<String, List<Object>> parameters) {
        if (packetPromise.type == 0) {
            String data = getBodyString();
            OptimizedParameterParser.parseUrlEncoded(data, parameters, true);
        } else if (packetPromise.type == 1) {
            String data = getBodyString();
            Tools.jsonToMap(data,parameters);
        }
        // body类型不做处理
    }

    /**
     * 获取 协议头 某个 Origin
     */
    @Override
    public String getOrigin() {
        return null;
    }

    /**
     * 从请求中获取真实域名，考虑了反向代理的情况。
     *
     * @return 真实域名，如果无法获取则返回null
     */
    @Override
    public String getDomain() {
        return null;
    }

    /**
     * 获取 session 对象
     */
    @Override
    public Session getSession() {
        return null;
    }

    /**
     * 获取 session 对象
     * sendCookie 是否自动发送cookie  默认 true
     *
     * @param sendCookie
     */
    @Override
    public Session getSession(boolean sendCookie) {
        return null;
    }

    /**
     * 获取 请求 路径
     */
    @Override
    public String getUri() {
        return packetPromise.url;
    }

    @Override
    public String webPathFormat() {
        return ClassTools.webPathFormat(packetPromise.url);
    }

    /**
     * 获取 请求 方法
     */
    @Override
    public String getMethod() {
        return packetPromise.method;
    }


    /**
     * 获取 底层对象 req
     */
    @Override
    public FullHttpRequest getRequest() {
        return null;
    }

    /**
     * 获取 请求数据 原数据 body
     */
    @Override
    public String getBodyString() {
        return new String(packetPromise.data);
    }

    /**
     * 获取 请求数据 原数据 body
     */
    @Override
    public byte[] getBody() {
        return packetPromise.data;
    }


    @Override
    public String getRemoteIp() {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return remoteAddress.getAddress().getHostAddress();
    }

    @Override
    public int getRemotePort() {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        return remoteAddress.getPort();
    }

    @Override
    public String getLocalIp() {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        return localAddress.getAddress().getHostAddress();
    }

    @Override
    public int getLocalPort() {
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        return localAddress.getPort();
    }

    public void close() {
        ctx.close();
    }
    public int getImplType(){
        return 1;
    }
}
