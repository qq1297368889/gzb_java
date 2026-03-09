package gzb.frame.netty;

import gzb.frame.netty.tools.TCPTools;
import gzb.tools.DateTime;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import gzb.tools.http.HTTPV2;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class UDPClient {
    // 统计总请求数
    private static AtomicLong totalRequestCount = new AtomicLong(0);
    // 统计每秒请求数
    private static AtomicLong secondRequestCount = new AtomicLong(0);
    // QPS统计线程
    private static ScheduledExecutorService qpsMonitor;

    /**
     * UDP压测核心方法
     * @param threadCount 线程数量
     * @param requestPerThread 每个线程请求数量
     * @param route 请求路由
     * @param params 请求参数
     * @param serverIp 服务端IP
     * @param serverPort 服务端端口
     * @throws Exception 异常
     */
    public static void udpPressureTest(int threadCount,
                                       int requestPerThread,
                                       String route,
                                       String params,
                                       String serverIp,
                                       int serverPort) throws Exception {
        // 初始化计数器
        totalRequestCount.set(0);
        secondRequestCount.set(0);

        // 启动QPS监控线程，每秒输出一次QPS
        startQpsMonitor();

        // 创建线程池执行压测任务
        ExecutorService executor = new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private int count = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "UDP-Client-Thread-" + (++count));
                    }
                }
        );

        // 准备请求数据
        byte[] requestBytes = NettyTools.readByteBuf(TCPTools.createDataPacketPromise(route, 0, 0, params));

        // 提交压测任务
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new UdpRequestTask(requestPerThread, requestBytes, serverIp, serverPort));
        }

        // 关闭线程池并等待任务完成
        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.HOURS);
        if (!finished) {
            executor.shutdownNow();
            System.err.println("压测任务超时强制终止");
        }

        // 停止QPS监控
        stopQpsMonitor();

        System.out.println("===== 压测完成 =====");
        System.out.println("总请求数: " + totalRequestCount.get());
        System.out.println("总线程数: " + threadCount);
        System.out.println("每个线程请求数: " + requestPerThread);
    }

    /**
     * UDP请求任务
     */
    static class UdpRequestTask implements Runnable {
        private final int requestCount;
        private final byte[] requestBytes;
        private final String serverIp;
        private final int serverPort;

        public UdpRequestTask(int requestCount, byte[] requestBytes, String serverIp, int serverPort) {
            this.requestCount = requestCount;
            this.requestBytes = requestBytes;
            this.serverIp = serverIp;
            this.serverPort = serverPort;
        }

        @Override
        public void run() {
            NioEventLoopGroup group = new NioEventLoopGroup(1);
            Channel ch = null;

            try {
                // 创建UDP客户端
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioDatagramChannel.class)
                        .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
                                // 接收响应（如果不需要处理响应，可以注释掉）
                                String response = packet.content().toString(CharsetUtil.UTF_8);
                                // System.out.println("【客户端收到响应】: " + response);
                            }

                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                // 捕获异常，避免线程退出
                                System.err.println("请求异常: " + cause.getMessage());
                            }
                        });

                // 绑定端口并创建通道
                ch = b.bind(0).sync().channel();

                // 发送指定数量的请求
                InetSocketAddress serverAddress = new InetSocketAddress(serverIp, serverPort);
                for (int i = 0; i < requestCount; i++) {
                    try {
                        // 发送UDP数据包
                        ch.writeAndFlush(new DatagramPacket(
                                Unpooled.copiedBuffer(requestBytes),
                                serverAddress)).sync();

                        // 计数累加
                        totalRequestCount.incrementAndGet();
                        secondRequestCount.incrementAndGet();

                    } catch (Exception e) {
                        System.err.println("发送请求失败: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ch != null) {
                    ch.close();
                }
                group.shutdownGracefully();
            }
        }
    }

    /**
     * 启动QPS监控
     */
    private static void startQpsMonitor() {
        qpsMonitor = Executors.newSingleThreadScheduledExecutor();
        qpsMonitor.scheduleAtFixedRate(() -> {
            long qps = secondRequestCount.getAndSet(0);
            System.out.println(String.format("[%s] 当前秒QPS: %d, 累计请求数: %d",
                    new DateTime().toString(), qps, totalRequestCount.get()));
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 停止QPS监控
     */
    private static void stopQpsMonitor() {
        if (qpsMonitor != null) {
            qpsMonitor.shutdown();
            try {
                qpsMonitor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                qpsMonitor.shutdownNow();
            }
        }
    }

    // 测试主方法
    public static void main(String[] args) throws Exception {
        // 示例调用：10个线程，每个线程发送1000个请求，路由test/api0/get1，参数message=哈哈哈
        udpPressureTest(
                10,                  // 线程数量
                10000*100,                // 每个线程请求数量
                "test/api0/get1",    // 请求路由
                "message=哈哈哈",     // 请求参数
                "127.0.0.1",         // 服务端IP
                2082                 // 服务端端口
        );
    }
}