package gzb.frame.netty.entity;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import java.util.*;

public interface Response {

    /**
     * 写入状态码
     */
    public Response setStatus(int status) ;

    /**
     * 写入编码
     */
    public Response setCharset(String charset);

    /**
     * 向响应体写入一个数据块。
     *
     * @param chunk 数据块
     */
    public Response write(byte[] chunk) ;

    /**
     * 结束响应流，并发送最后一个数据块和末尾标志。 和 write 配合
     */
    public Response flush() ;

    /**
     * 写入数据并  提交 响应
     */
    public Response sendAndFlush(Object chunk);

    /**
     * 设置 Cookie
     */
    public Response setCookie(String key, String val, int mm);

    /**
     * 设置 Cookie
     */
    public Response setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly, boolean secure);

    /**
     * 返回 成功
     */
    public Response success(byte[] body);

    /**
     * 返回 成功
     */
    public Response success(String body);

    /**
     * 返回 失败
     */
    public Response fail();

    /**
     * 返回 异常
     */
    public Response error();

    /**
     * 设置 协议头
     */
    public Response setHeader(String key, String value, String... keyAndVal) ;

    /**
     * 设置响应类型
     */
    public Response setContentType(String value);

    /**
     * 设置响应长度
     */
    public Response setContentLengthLong(int value) ;

    /**
     * 发送时间协议头
     */
    public void setDateHeader(String name, long date);

    /**
     * 获取响应状态码
     */
    public HttpResponseStatus getStatus();

    /**
     * 设置响应状态码
     */
    public Response setStatus(HttpResponseStatus status);

    /**
     * 获取 协议头MAP
     */
    public Map<String, String> getHeaders();

    /**
     * 写入 协议头MAP
     */
    public Response setHeaders(Map<String, String> headers) ;

    /**
     * 获取 编码
     */
    public String getCharset();

    /**
     * 获取 底层对象
     */
    public ChannelHandlerContext getCtx();

    /**
     * 置入 底层对象
     */
    public Response setCtx(ChannelHandlerContext ctx);
}
