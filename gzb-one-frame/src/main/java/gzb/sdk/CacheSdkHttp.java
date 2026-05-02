package gzb.sdk;

import gzb.exception.GzbException0;
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CacheSdkHttp implements CacheSdk{

    public static Log log = Log.log;
    private EventLoopGroup group = null;
    private final Map<Long, BaseSdk.Call> requests = new ConcurrentHashMap<>();
    private final AtomicLong sidGenerator = new AtomicLong(1);
    private final byte[] state = "1234567890".getBytes();

    private final Channel channel;
    private final int index;
    private final String host;
    private final int port;

    public CacheSdkHttp(String host, int port, int index, int IO_THREADS) throws IOException {
        this.host = host;
        this.port = port;
        this.index = index;
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
            byte[] bytes = NettyTools.readByteBuf(content);
            int end = -1;
            long sid = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                if (b == ',') {
                    end = i;
                    break;
                }
                if (b < 48 || b > 57 || i > 18) {
                    sid = 0;
                    break;
                }
                sid = sid * 10 + (b - 48);
            }
            if (sid > 0) {
                byte[] bytes1 = Arrays.copyOfRange(bytes, end + 1, bytes.length);
                BaseSdk.Call call = requests.remove(sid);
                if (call != null) {
                    call.run(bytes1);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            log.e("HTTP请求异常", cause);
            ctx.close();
            // 异常关闭时，如果还有挂起的请求，可能需要根据业务逻辑处理信号量，
            // 但通常连接断开意味着当前 pipeline 全部失效。
        }
    }


    public byte[] sendSync(String url, String params) {
        Tools.ThreadWakeUp threadWakeUp = new Tools.ThreadWakeUp();
        sendSync(url, params, new BaseSdk.Call() {
            @Override
            public void run(Object obj) {
                try {
                    if (obj instanceof Throwable) {
                        threadWakeUp.notifyActivation(new RuntimeException((Throwable) obj));
                    } else {
                        threadWakeUp.notifyActivation((byte[]) obj);
                    }
                } catch (Exception e) {
                    threadWakeUp.notifyActivation(new GzbException0("Exception", e));
                }
            }
        });
        return threadWakeUp.waitActivationData(1000 * 3);
    }

    public void sendSync(String url, String params, BaseSdk.Call call) {
        sendSync(url,params,call,true);
    }

    public void sendSync(String url, String params, BaseSdk.Call call, boolean flush) {
        long sid = sidGenerator.incrementAndGet();
        requests.put(sid, call);

        String fullUri = params == null ? url + "?sid=" + sid : url + "?" + params + "&sid=" + sid;

        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, fullUri);

        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        if (flush) {
            channel.writeAndFlush(request);
        }else{
            channel.write(request);
        }

    }
    public void request(String url,String met, String params, BaseSdk.Call call, boolean flush) {
        long sid = sidGenerator.incrementAndGet();
        requests.put(sid, call);
        FullHttpRequest request = null;
        if (met==null || met.toLowerCase().equals("get")) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        }else if(met.toLowerCase().equals("post")){
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url,Unpooled.wrappedBuffer(params.getBytes(Config.encoding)));
        }else if(met.toLowerCase().equals("put")){
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url,Unpooled.wrappedBuffer(params.getBytes(Config.encoding)));
        }else if(met.toLowerCase().equals("delete")){
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        }
        request.headers().set(HttpHeaderNames.HOST, host);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        if (flush) {
            channel.writeAndFlush(request);
        }else{
            channel.write(request);
        }

    }
    public List<byte[]> sendSync(String url, String params,int pip) {
        List<byte[]> list=new ArrayList<>(pip);
        Tools.ThreadWakeUp threadWakeUp = new Tools.ThreadWakeUp(pip);
        for (int i = 0; i < pip; i++) {
            sendSync(url, params, new BaseSdk.Call() {
                @Override
                public void run(Object obj) {
                    try {
                        if (obj instanceof Throwable) {
                            threadWakeUp.notifyActivation();
                            throw new RuntimeException((Throwable) obj);
                        } else {
                            byte[] bytes = (byte[]) obj;
                            list.add(bytes);
                            threadWakeUp.notifyActivation();
                        }
                    } catch (Exception e) {
                        threadWakeUp.notifyActivation();
                        throw e;
                    }
                }
            },false);
        }
        channel.flush();
        threadWakeUp.waitActivation(1000 * 10);

        return list;
    }
    public boolean ping() throws IOException {
        byte[] res = sendSync("/cache/ping", null);
        return res != null && res[0] == state[0];
    }

    public boolean put(String key, String val, int sec) throws IOException {
        byte[] res = sendSync("/cache/set", "k=" + key + "&v=" + val + "&s=" + sec + "&i=" + index);
        return res != null && res[0] == state[0];
    }

    public boolean del(String... key) throws IOException {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder stringBuilder = entity.stringBuilderCacheEntity.get(index);
            stringBuilder.append("i=").append(index);
            for (String s : key) {
                stringBuilder.append("&k=").append(s);
            }
            byte[] res = sendSync("/cache/del", stringBuilder.toString());

            return res != null && res[0] == state[0];
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public String get(String... key) throws IOException {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder stringBuilder = entity.stringBuilderCacheEntity.get(index);
            stringBuilder.append("i=").append(index);
            for (String s : key) {
                stringBuilder.append("&k=").append(s);
            }
            byte[] res = sendSync("/cache/get", stringBuilder.toString());
            if (res == null || res[0] != state[0]) return null;
            int[] xy = new int[]{2, 2};
            return Tools.byteReadSizeString(res, split, xy);
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public List<String> getAll(String... key) throws IOException {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder stringBuilder = entity.stringBuilderCacheEntity.get(index);
            stringBuilder.append("i=").append(index);
            for (String s : key) {
                stringBuilder.append("&k=").append(s);
            }
            byte[] res = sendSync("/cache/get/all", stringBuilder.toString());
            if (res == null || res[0] != state[0]) return null;
            List<String> list = new ArrayList<>(key.length);
            int[] xy = new int[]{2, 2};
            String val = Tools.byteReadSizeString(res, split, xy);
            while (val != null) {
                list.add(val);
                val = Tools.byteReadSizeString(res, split, xy);
            }
            return list;
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public List<String> get(int pip, String key) throws IOException {
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.stringBuilderCacheEntity.open();
        try {
            StringBuilder stringBuilder = entity.stringBuilderCacheEntity.get(index);
            stringBuilder.append("i=").append(index);
            stringBuilder.append("&k=").append(key);
            List<byte[]> res0 = sendSync("/cache/get", stringBuilder.toString(), pip);
            List<String> res_str = new ArrayList<>(res0.size());
            for (int i = 0; i < res0.size(); i++) {
                byte[] res = res0.get(i);
                if (res == null || res[0] != state[0]) {
                    res_str.add(null);
                } else {
                    int[] xy = new int[]{2, 2};
                    res_str.add(Tools.byteReadSizeString(res, split, xy));
                }
            }
            return res_str;
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public boolean produce(String data) throws IOException {
        byte[] res = sendSync("/queue/produce", "d=" + data + "&i=" + index);
        return res != null && res[0] == state[0];
    }

    public static byte split = ",".getBytes()[0];

    public BaseSdk.Message consume(int time) throws IOException {
        byte[] res = sendSync("/queue/consume", "s=" + time + "&i=" + index);
        if (res == null || res[0] != state[0]) return null;
        int[] xy = new int[]{2, 2};
        long idx = Tools.byteReadLong(res, split, xy);
        if (idx < 1) {
            return null;
        }
        String val = Tools.byteReadSizeString(res, split, xy);
        return new BaseSdk.Message(idx, val);
    }


    public String confirm(long id) throws IOException {
        byte[] res = sendSync("/queue/confirm", "id=" + id + "&i=" + index);
        if (res == null || res[0] != state[0]) return null;
        int[] xy = new int[]{2, 2};
        return Tools.byteReadSizeString(res, split, xy);
    }

    public void close() {
        if (channel != null) channel.close();
        group.shutdownGracefully();
    }
}