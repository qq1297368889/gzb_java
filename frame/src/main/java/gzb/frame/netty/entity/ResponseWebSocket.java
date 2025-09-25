package gzb.frame.netty.entity;

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

public class ResponseWebSocket implements Response {
    public static final Charset encoding = Charset.forName(Config.encoding);
    private String charset;
    private ChannelHandlerContext ctx;
    public ResponseWebSocket(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Response setStatus(int status) {
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

        return this;
    }

    /**
     * 向响应体写入一个数据块。
     *
     * @param chunk 数据块
     */
    public Response write(byte[] chunk) {
        ctx.write(Unpooled.copiedBuffer(chunk));
        return this;
    }

    /**
     * 结束响应流，并发送最后一个数据块和末尾标志。
     */
    public Response flush() {
        ctx.flush();
        return this;
    }

    public Response sendAndFlush(Object chunk) {
        byte[] bytes;
        if (chunk instanceof byte[]) {
            bytes = (byte[]) chunk;
        } else if (chunk instanceof String) {
            bytes = ((String) chunk).getBytes(encoding);
        } else if (chunk == null) {
            bytes = new byte[0];
        } else {
            String str1 = Tools.toJson(chunk);
            if (str1 == null) {
                bytes = new byte[0];
            } else {
                bytes = str1.getBytes(encoding);
            }
        }
        ctx.writeAndFlush(bytes);
        return this;
    }

    public Response setCookie(String key, String val, int mm) {
        return null;
    }

    /**
     * Sets a cookie with all available parameters.
     * If a cookie with the same key already exists, it will be updated.
     */
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

    public Response setHeader(String key, String value, String... keyAndVal) {

        return this;
    }

    public Response setContentType(String value) {

        return this;
    }

    public Response setContentLengthLong(int value) {

        return this;
    }

    /**
     * 模拟setDateHeader方法
     *
     * @param name 响应头名称
     * @param date 时间戳(自1970-01-01 00:00:00 GMT以来的毫秒数)
     */
    public void setDateHeader(String name, long date) {

    }

    public HttpResponseStatus getStatus() {
        return null;
    }

    public Response setStatus(HttpResponseStatus status) {

        return this;
    }

    public Map<String, String> getHeaders() {
        return null;
    }

    public Response setHeaders(Map<String, String> headers) {

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
