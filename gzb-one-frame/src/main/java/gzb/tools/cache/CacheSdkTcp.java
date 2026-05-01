package gzb.tools.cache;

import gzb.exception.GzbException0;
import gzb.frame.netty.tools.TCPTools;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class CacheSdkTcp {
    public static class Message {
        public long id;
        public String data;
        public Message(long id,String data){
            this.id=id;
            this.data=data;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }


    public static Log log = Log.log;
    private int IO_THREADS = 1;
    /// Runtime.getRuntime().availableProcessors();
    private EventLoopGroup group = null;
    private final Map<Long, Call> requests = new ConcurrentHashMap<>();
    //private final Map<Long, CompletableFuture<byte[]>> requests = new ConcurrentHashMap<>();
    private final AtomicLong sidGenerator = new AtomicLong(1);
    private final byte[] state = "1234567890".getBytes();

    private final Channel channel;
    private final int index;

    public CacheSdkTcp(String host, int port, int index, int IO_THREADS) throws IOException {
        this.index = index;
        if (IO_THREADS < 1) {
            IO_THREADS = Runtime.getRuntime().availableProcessors();
        }
        this.IO_THREADS = IO_THREADS;
        Bootstrap b = new Bootstrap();
        group = new EpollEventLoopGroup(IO_THREADS);
        b.group(group)
                .channel(EpollSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new SdkHandler());
                    }
                });

        try {
            this.channel = b.connect(host, port).sync().channel();
        } catch (InterruptedException e) {
            throw new IOException("Netty 连接失败", e);
        }
    }

    private class SdkHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf byteBuf = (ByteBuf) msg;
            List<byte[]> list = TCPTools.readDataPacketByteArray(ctx.channel().hashCode(), byteBuf);
            if (list != null) {
                for (byte[] bytes : list) {
                    if (bytes != null) {
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
                            /*
                            Call call = requests2.remove(sid);
                            call.run(bytes1);

                            CompletableFuture<byte[]>completableFuture= requests.remove(sid);
                            completableFuture.complete(bytes1);
                            */
                            Call call = requests.remove(sid);
                            call.run(bytes1);
                        }

                    }

                }
            }

        }
    }


    public static class Call {
        public void run(Object obj) {

        }
    }


    private void sendSync(String url, String params, Call call) {
        long sid = sidGenerator.incrementAndGet();
        requests.put(sid, call);
        channel.writeAndFlush(TCPTools.createDataPacketPromise(url, 1, 0, params == null ? "sid=" + sid : params + "&sid=" + sid
        ));
    }

    private byte[] sendSync(String url, String params) {
        Tools.ThreadWakeUp threadWakeUp = new Tools.ThreadWakeUp();
        sendSync(url, params, new Call() {
            @Override
            public void run(Object obj) {
                try {
                    if (obj instanceof Throwable) {
                        threadWakeUp.notifyActivation(new RuntimeException((Throwable) obj));
                    } else {
                        byte[] bytes = (byte[]) obj;
                        threadWakeUp.notifyActivation(bytes);
                    }
                } catch (Exception e) {
                    threadWakeUp.notifyActivation(new GzbException0("Exception", e));
                }
            }
        });
        return threadWakeUp.waitActivationData(1000 * 10);
    }

 /*
    private void sendSync(String url, String params, Call call) {
        long sid = sidGenerator.incrementAndGet();
        requests2.put(sid, call);
        channel.writeAndFlush(TCPTools.createDataPacketPromise(url, 1, 0, params == null ? "sid=" + sid : params + "&sid=" + sid
        ));
    }
       private byte[] sendSync(String url, String params) {
        Tools.ThreadWakeUp threadWakeUp = new Tools.ThreadWakeUp();
        sendSync(url, params, new Call() {
            @Override
            public void run(Object obj) {
                try {
                    if (obj instanceof Throwable) {
                        threadWakeUp.notifyActivation(new RuntimeException((Throwable) obj));
                    } else {
                        byte[] bytes = (byte[]) obj;
                        threadWakeUp.notifyActivation(bytes);
                    }
                } catch (Exception e) {
                    threadWakeUp.notifyActivation(new GzbException0("Exception", e));
                }
            }
        });
        return threadWakeUp.waitActivationData(1000 * 10);
    }
    */

    /*
        private byte[] sendSync(String url, String params) throws IOException {
            long sid = sidGenerator.incrementAndGet();
            CompletableFuture<byte[]> future = new CompletableFuture<>();
            requests.put(sid, future);
            channel.writeAndFlush(TCPTools.createDataPacketPromise(url, 1, 0, params == null ? "sid=" + sid : params + "&sid=" + sid
            ));

            try {
                // 阻塞等待结果，可设置超时防止死锁
                return future.get(10, TimeUnit.SECONDS);
            } catch (Exception e2) {
                requests.remove(sid);
                Log.log.e("请求超时或异常", e2);
                return null;
            }
        }*/
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

            return new String(res, 2, res.length - 2);
        } finally {
            entity.stringBuilderCacheEntity.close(index);
        }
    }

    public boolean produce(String data) throws IOException {
        byte[] res = sendSync("/queue/produce", "d=" + data + "&i=" + index);
        return res != null && res[0] == state[0];
    }

    public static byte split = ",".getBytes()[0];

    public Message consume(int time) throws IOException {
        byte[] res = sendSync("/queue/consume", "s=" + time + "&i=" + index);
        if (res == null || res[0] != state[0]) return null;
        int[] xy = new int[]{0, 0};
        int state = Tools.byteReadInt(res, split, xy);
        long idx = Tools.byteReadLong(res, split, xy);
        if (idx < 1) {
            return null;
        }
        return new Message(idx, new String(res, xy[0], res.length - xy[0]));
    }


    public String confirm(long id) throws IOException {
        byte[] res = sendSync("/queue/confirm", "id=" + id + "&i=" + index);
        if (res == null || res[0] != state[0]) return null;
        return new String(res, 2, res.length - 2);
    }

    public void close() {
        if (channel != null) channel.close();
        group.shutdownGracefully();
    }
}