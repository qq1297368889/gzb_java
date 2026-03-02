package gzb.frame.netty.entity;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Collections;
import java.util.Map;

public class ResponseTcpImpl implements Response {
    private ChannelHandlerContext ctx;

    public ResponseTcpImpl(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 写入状态码
     *
     * @param status
     */
    @Override
    public Response setStatus(int status) {
        return null;
    }

    /**
     * 写入编码
     *
     * @param charset
     */
    @Override
    public Response setCharset(String charset) {
        return null;
    }

    /**
     * 向响应体写入一个数据块。
     *
     * @param chunk 数据块
     */
    @Override
    public Response write(byte[] chunk) {
        ctx.write(chunk);
        return this;
    }

    /**
     * 结束响应流，并发送最后一个数据块和末尾标志。 和 write 配合
     */
    @Override
    public Response flush() {
        ctx.flush();
        return this;
    }

    /**
     * 写入数据并  提交 响应
     *
     * @param chunk
     */
    @Override
    public Response sendAndFlush(Object chunk) {
        ctx.writeAndFlush(chunk);
        return this;
    }

    /**
     * 设置 Cookie
     *
     * @param key
     * @param val
     * @param mm
     */
    @Override
    public Response setCookie(String key, String val, int mm) {
        return null;
    }

    /**
     * 设置 Cookie
     *
     * @param key
     * @param val
     * @param maxAge
     * @param domain
     * @param path
     * @param httpOnly
     * @param secure
     */
    @Override
    public Response setCookie(String key, String val, long maxAge, String domain, String path, boolean httpOnly, boolean secure) {
        return null;
    }

    /**
     * 返回 成功
     *
     * @param body
     */
    @Override
    public Response success(byte[] body) {
        return null;
    }

    /**
     * 返回 成功
     *
     * @param body
     */
    @Override
    public Response success(String body) {
        return null;
    }

    /**
     * 返回 失败
     */
    @Override
    public Response fail() {
        return null;
    }

    /**
     * 返回 异常
     */
    @Override
    public Response error() {
        return null;
    }

    /**
     * 设置 协议头
     *
     * @param key
     * @param value
     * @param keyAndVal
     */
    @Override
    public Response setHeader(String key, String value, String... keyAndVal) {
        return null;
    }

    /**
     * 设置响应类型
     *
     * @param value
     */
    @Override
    public Response setContentType(String value) {
        return null;
    }

    /**
     * 设置响应长度
     *
     * @param value
     */
    @Override
    public Response setContentLengthLong(int value) {
        return null;
    }

    /**
     * 发送时间协议头
     *
     * @param name
     * @param date
     */
    @Override
    public void setDateHeader(String name, long date) {

    }

    /**
     * 获取响应状态码
     */
    @Override
    public HttpResponseStatus getStatus() {
        return null;
    }

    /**
     * 设置响应状态码
     *
     * @param status
     */
    @Override
    public Response setStatus(HttpResponseStatus status) {
        return null;
    }

    /**
     * 获取 协议头MAP
     */
    @Override
    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    /**
     * 写入 协议头MAP
     *
     * @param headers
     */
    @Override
    public Response setHeaders(Map<String, String> headers) {
        return null;
    }

    /**
     * 获取 编码
     */
    @Override
    public String getCharset() {
        return null;
    }

    /**
     * 获取 底层对象
     */
    @Override
    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    /**
     * 置入 底层对象
     *
     * @param ctx
     */
    @Override
    public Response setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        return this;
    }
}
