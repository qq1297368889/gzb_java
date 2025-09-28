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

import gzb.frame.netty.HTTPRequestParameters;
import gzb.tools.Config;
import gzb.tools.cache.session.Session;
import gzb.tools.cache.session.SessionImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RequestWebSocket implements Request {
    private ChannelHandlerContext ctx;
    private String uri;
    private String method;
    private String remoteIp;
    private int remotePort;
    private String localIp;
    private int localPort;
    private byte[] body;
    private Response response;
    private Session session;


    public RequestWebSocket(ChannelHandlerContext ctx, String method,String uri, byte[] body) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        this.uri = uri;
        this.method = method;
        this.ctx = ctx;
        this.response = new ResponseDefaultImpl(ctx);
        this.remoteIp = remoteAddress.getAddress().getHostAddress();
        this.remotePort = remoteAddress.getPort();
        this.localIp = localAddress.getAddress().getHostAddress();
        this.localPort = localAddress.getPort();
        this.body=body;

    }

    public Map<String, String> getHeaders() {

        return null;
    }

    public long getDateHeader(String name) {

        return -1;
    }

    public String getHeader(String key) {
        return null;
    }

    public Set<Cookie> getCookies() {
        return null;
    }

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

    public Response getResponse() {
        return response;
    }

    public Cookie getCookie(String key) {

        return null;
    }

    public String getCookieVal(String key) {
        Cookie cookie = getCookie(key);
        if (cookie == null) {
            return null;
        }
        return cookie.value();
    }

    public String[] getParameterArray(String key) {

        return null;
    }

    public String getParameterVal(String key) {
        String[] arr1 = getParameterArray(key);
        if (arr1 == null || arr1.length < 1) {
            return null;
        }
        return arr1[0];
    }

    public Map<String, List<Object>> getParameter() {

        return null;
    }

    public String getOrigin() {

        return null;
    }

    public String getDomain() {

        return null;
    }


    public Session getSession() {
        return getSession(true);
    }

    public Session getSession(boolean sendCookie) {
        if (session == null) {
            String token = getHeader("token");
            if (token == null) {
                token = getParameterVal("token");
            }
            if (token == null) {
                token = getCookieVal("token");
            }
            session = new SessionImpl(token, response, sendCookie);
        }
        return session;
    }


    public String getUri() {

        return null;
    }

    public String getMethod() {
        return method;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getLocalIp() {
        return localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    /**
     * 获取 底层对象 req
     */
    @Override
    public FullHttpRequest getRequest() {
        return null;
    }

    public String getBodyString() {
        return new String(body, Config.encoding);
    }

    public byte[] getBody() {
        return body;
    }

}
