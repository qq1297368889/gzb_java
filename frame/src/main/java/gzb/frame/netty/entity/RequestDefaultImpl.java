package gzb.frame.netty.entity;

import gzb.frame.netty.RequestParameters;
import gzb.tools.Config;
import gzb.tools.cache.session.Session;
import gzb.tools.cache.session.SessionImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RequestDefaultImpl implements Request {
    private ChannelHandlerContext ctx;
    private String method;
    private String remoteIp;
    private int remotePort;
    private String localIp;
    private int localPort;
    private Map<String, String> headers;
    private byte[] body;
    private Set<Cookie> cookies;
    private RequestParameters requestParameters;
    private FullHttpRequest request;
    private Response response;
    private Session session;


    public RequestDefaultImpl(ChannelHandlerContext ctx, FullHttpRequest request) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        this.ctx = ctx;
        this.request = request;
        this.response = new ResponseDefaultImpl(ctx);
        this.method = request.method().name();
        this.remoteIp = remoteAddress.getAddress().getHostAddress();
        this.remotePort = remoteAddress.getPort();
        this.localIp = localAddress.getAddress().getHostAddress();
        this.localPort = localAddress.getPort();
        ByteBuf content = request.content();
        if (content.hasArray()) {
            this.body = content.array();
        } else {
            this.body = new byte[content.readableBytes()];
            content.readBytes(this.body);
        }

        getParameter();
        //this.response.setContentType(ContentType.html);
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
            for (Map.Entry<String, String> entry : request.headers().entries()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                headers.put(entry.getKey().toLowerCase(), entry.getValue());
            }
        }
        return headers;
    }

    public long getDateHeader(String name) {
        if (name == null || name.isEmpty()) {
            return -1;
        }

        String headerValue = headers.get(name);
        if (headerValue == null || headerValue.isEmpty()) {
            return -1;
        }
// HTTP协议标准日期格式及常见变体格式
        List<SimpleDateFormat> DATE_FORMATTERS;
        // 初始化支持的日期格式列表
        DATE_FORMATTERS = new ArrayList<>();

        // 标准HTTP日期格式 (RFC 1123)
        SimpleDateFormat rfc1123 = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss zzz",
                Locale.US
        );
        rfc1123.setTimeZone(TimeZone.getTimeZone("GMT"));

        // 旧版HTTP日期格式 (RFC 1036)
        SimpleDateFormat rfc1036 = new SimpleDateFormat(
                "EEEE, dd-MMM-yy HH:mm:ss zzz",
                Locale.US
        );
        rfc1036.setTimeZone(TimeZone.getTimeZone("GMT"));

        // ANSI C asctime()格式
        SimpleDateFormat ansiC = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss yyyy",
                Locale.US
        );
        ansiC.setTimeZone(TimeZone.getTimeZone("GMT"));

        DATE_FORMATTERS.add(rfc1123);
        DATE_FORMATTERS.add(rfc1036);
        DATE_FORMATTERS.add(ansiC);
        // 尝试用所有支持的格式解析
        for (SimpleDateFormat formatter : DATE_FORMATTERS) {
            try {
                Date date = formatter.parse(headerValue);
                return date.getTime(); // 转换为时间戳
            } catch (ParseException e) {
                // 解析失败继续尝试下一种格式
                continue;
            }
        }

        // 所有格式都解析失败
        return -1;
    }

    public String getHeader(String key) {
        if (headers == null) {
            getHeaders();//初始化 协议头数据
        }
        return headers.get(key.toLowerCase());
    }

    public Set<Cookie> getCookies() {
        if (cookies == null) {
            if (headers == null) {
                getHeaders();//初始化 协议头数据
            }
            String cookieString = headers.get("cookie");
            if (cookieString != null) {
                cookies = ServerCookieDecoder.STRICT.decode(cookieString);
            }
            if (cookies == null) {
                cookies = new HashSet<>();
            }
        }
        return cookies;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public Response getResponse() {
        return response;
    }

    public Cookie getCookie(String key) {
        if (cookies == null) {
            getCookies();//初始化 协议头数据 然后cookie
        }
        for (Cookie cookie : cookies) {
            if (cookie.name().equals(key)) {
                return cookie;
            }
        }
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
        if (requestParameters == null) {
            getParameter();
        }
        if (requestParameters == null) {
            return null;
        }
        List<Object> list = requestParameters.getParameters().get(key);
        if (list != null && !list.isEmpty()) {
            return list.toArray(new String[0]);
        }
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
        if (requestParameters == null) {
            requestParameters = new RequestParameters(request, getBodyString());
        }
        return requestParameters.getParameters();
    }

    public String getOrigin() {
        if (headers == null) {
            getHeaders();
        }
        if (headers != null) {
            return headers.get("origin");
        }
        return null;
    }

    public String getDomain() {
        if (headers == null) {
            getHeaders();
        }
        // 1. 检查反向代理头: X-Forwarded-Host
        // 这是最常用的头，由大多数负载均衡器和代理设置
        String host = headers.get("x-forwarded-host");
        if (host != null && !host.isEmpty()) {
            return host.split(":")[0].trim(); // 移除端口号
        }

        // 2. 检查反向代理头: Forwarded
        // 这是较新的RFC 7239标准，但使用不如X-Forwarded-Host广泛
        host = headers.get("forwarded");
        if (host != null && !host.isEmpty()) {
            // 解析host参数，例如 "for=192.0.2.60;proto=http;host=example.com"
            for (String param : host.split(";")) {
                if (param.trim().toLowerCase().startsWith("host=")) {
                    return param.split("=")[1].trim();
                }
            }
        }

        // 3. 回退到标准HTTP Host头
        // 这是最基础的获取方式
        host = headers.get("host");
        if (host != null && !host.isEmpty()) {
            return host.split(":")[0].trim(); // 移除端口号
        }

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
        return requestParameters.path;
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

    public String getBodyString() {
        return new String(body, Charset.forName(Config.encoding));
    }

    public byte[] getBody() {
        return body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }
}
