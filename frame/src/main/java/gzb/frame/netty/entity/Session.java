package gzb.frame.netty.entity;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/// 搁置 http响应头问题 还是无法统一解决
public interface Session {

    void setCookie(String key, String val, int mm);

    void setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly, boolean secure);

    void setHeaders(Map<String, String> headers);

    void setHeaders(String key, String val);

    /// 适用于 tcp udp ws
    void send(Object chunk);

    /// 自动封装单次响应 http格式
    void sendHttp(int code, Object chunk);

    /// 适用于流式发送
    void sendHttpStream(byte[] data);

    void sendHttpStream(ByteBuf data);

    /// 流式响应结束
    void flushHttp();

    /// 获取客户端信息 和 本地临时端口信息
    String getClientIp();

    int getClientPort();

    String getLocalIp();

    int getLocalPort();

    /// 会话签名 同会话唯一
    String getSign();

    /// 统一抽象 http的协议头 cookie 表单 json 都视为 参数统一抽象为该格式 http tcp ws udp 统一 需要注意重名问题 并且规避
    Map<String, List<Object>> getParameter();

    List<Object> getParameterList(String key);

    Object getParameterObject(String key);

    String getParameterString(String key);

    //获取服务端参数

}
