package gzb.frame.server.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * TCP客户端（适配size,data格式 + 可配置超时）
 * 发送格式：size,data（如5,hello）
 * 超时时间可配置，超时直接失败
 */
public class TCPClient {
    // 可配置参数：服务端地址、端口、读取超时时间（秒）
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 8888;
    private static final int READ_TIMEOUT_SECONDS = 10; // 超时时间，可自定义

    public static void main(String[] args) throws InterruptedException {
        // 测试发送hello（按size,data格式）
        for (int i = 0; i < 10; i++) {
            new TCPClient().sendMessage("hello");
        }
    }

    public void sendMessage(String msg) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加读取超时处理器
                            pipeline.addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS));
                            // 自定义客户端处理器
                            pipeline.addLast(new TcpClientHandler());
                        }
                    });

            // 连接服务端
            ChannelFuture future = bootstrap.connect(SERVER_HOST, SERVER_PORT).sync();
            System.out.println(String.format("客户端已连接到服务端：%s:%d，读取超时时间：%d秒", SERVER_HOST, SERVER_PORT, READ_TIMEOUT_SECONDS));

            // 构造size,data格式的请求数据
            byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
            String requestMsg = msgBytes.length + "," + msg;
            ByteBuf requestBuf = Unpooled.copiedBuffer(requestMsg, StandardCharsets.UTF_8);

            // 发送数据
            future.channel().writeAndFlush(requestBuf);
            System.out.println("客户端发送：" + requestMsg);

            // 等待连接关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("客户端发送/接收数据失败：" + e.getMessage());
        } finally {
            group.shutdownGracefully();
            System.out.println("客户端已断开连接");
        }
    }

    /**
     * 客户端处理器：解析服务端返回的size,data格式数据
     */
    static class TcpClientHandler extends ChannelInboundHandlerAdapter {
        // 缓存未解析的字节
        private final List<Byte> cacheBytes = new ArrayList<>();

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf buf = (ByteBuf) msg;
            try {
                // 1. 存入缓存
                while (buf.isReadable()) {
                    cacheBytes.add(buf.readByte());
                }

                // 2. 解析size
                Integer dataSize = parseSizeFromCache();
                if (dataSize == null) {
                    return;
                }

                // 3. 读取data
                int totalNeedBytes = cacheBytes.size() - (getCommaIndex() + 1);
                if (totalNeedBytes < dataSize) {
                    System.out.println("客户端缓存数据不足，需要" + dataSize + "字节，仅收到" + totalNeedBytes + "字节");
                    return;
                }

                byte[] dataBytes = readDataFromCache(dataSize);
                String dataStr = new String(dataBytes, StandardCharsets.UTF_8);
                System.out.println("客户端解析到服务端返回：size=" + dataSize + ", data=" + dataStr);

                // 4. 关闭连接
                ctx.channel().close();

            } catch (Exception e) {
                System.out.println("客户端解析数据失败：" + e.getMessage());
                ctx.close();
            } finally {
                buf.release();
            }
        }

        // 解析size（同服务端逻辑）
        private Integer parseSizeFromCache() {
            int commaIndex = getCommaIndex();
            if (commaIndex == -1) {
                return null;
            }
            byte[] sizeBytes = new byte[commaIndex];
            for (int i = 0; i < commaIndex; i++) {
                sizeBytes[i] = cacheBytes.get(i);
            }
            String sizeStr = new String(sizeBytes, StandardCharsets.UTF_8);
            try {
                return Integer.parseInt(sizeStr);
            } catch (NumberFormatException e) {
                throw new RuntimeException("size解析失败，非数字格式：" + sizeStr);
            }
        }

        // 查找逗号位置
        private int getCommaIndex() {
            for (int i = 0; i < cacheBytes.size(); i++) {
                if (cacheBytes.get(i) == ',') {
                    return i;
                }
            }
            return -1;
        }

        // 读取data
        private byte[] readDataFromCache(int dataSize) {
            int commaIndex = getCommaIndex();
            byte[] dataBytes = new byte[dataSize];
            for (int i = 0; i < dataSize; i++) {
                dataBytes[i] = cacheBytes.get(commaIndex + 1 + i);
            }
            return dataBytes;
        }

        /**
         * 超时/异常处理
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            if (cause instanceof io.netty.handler.timeout.ReadTimeoutException) {
                System.out.println("客户端读取超时（" + READ_TIMEOUT_SECONDS + "秒），关闭连接");
            } else {
                System.out.println("客户端发生异常：" + cause.getMessage());
            }
            ctx.close();
        }
    }
}