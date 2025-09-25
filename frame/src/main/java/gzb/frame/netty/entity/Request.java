package gzb.frame.netty.entity;

import gzb.frame.netty.HTTPRequestParameters;
import gzb.tools.cache.session.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;

import java.util.*;

public interface Request {

    /**
     * 获取 协议头MAP
     */
    public Map<String, String> getHeaders();

    /**
     * 获取日期类型响应头，转换为时间戳
     *
     * @param name 响应头名称
     * @return 时间戳(毫秒)，如果头不存在或解析失败返回-1
     */
    public long getDateHeader(String name);

    /**
     * 获取 协议头 某个
     */
    public String getHeader(String key);

    /**
     * 获取 COOKIE
     */
    public Set<Cookie> getCookies();

    /**
     * 获取 底层对象
     */
    public ChannelHandlerContext getCtx();

    /**
     * 获取 参数解析对象
     */
    public HTTPRequestParameters getRequestParameters();

    /**
     * 获取 响应对象
     */
    public Response getResponse();

    /**
     * 获取 cookie 的 对象
     */
    public Cookie getCookie(String key) ;

    /**
     * 获取 cookie 的 某个值
     */
    public String getCookieVal(String key);

    /**
     * 获取 参数 某个元素 数组
     */
    public String[] getParameterArray(String key) ;

    /**
     * 获取 参数 某个元素 字符串
     */
    public String getParameterVal(String key) ;

    /**
     * 获取 参数 MAP
     */
    public Map<String, List<Object>> getParameter() ;

    /**
     * 获取 协议头 某个 Origin
     */
    public String getOrigin() ;
    /**
     * 从请求中获取真实域名，考虑了反向代理的情况。
     *
     * @return 真实域名，如果无法获取则返回null
     */
    public String getDomain() ;


    /**
     * 获取 session 对象
     */
    public Session getSession();
    /**
     * 获取 session 对象
     * sendCookie 是否自动发送cookie  默认 true
     */
    public Session getSession(boolean sendCookie);


    /**
     * 获取 请求 路径
     */
    public String getUri();

    /**
     * 获取 请求 方法
     */
    public String getMethod();

    /**
     * 获取 来源 IP
     */
    public String getRemoteIp();

    /**
     * 获取 来源 端口
     */
    public int getRemotePort();

    /**
     * 获取 当前 IP
     */
    public String getLocalIp();

    /**
     * 获取 当前 端口
     */
    public int getLocalPort();

    /**
     * 获取 底层对象 req
     */
    public FullHttpRequest getRequest();

    /**
     * 获取 请求数据 原数据 body
     */
    public String getBodyString();
    /**
     * 获取 请求数据 原数据 body
     */
    public byte[] getBody();
}