package gzb.frame.server.tcp;

import gzb.tools.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * TCP服务端（size,data格式 + 可配置超时）
 * 协议格式：size,data（如5,hello）
 * 超时时间可配置，超时直接关闭连接
 */
public class TCPServer {
    // 可配置参数：监听端口、读取超时时间（秒）
    private static final int PORT = 8888;

    public static void main(String[] args) throws InterruptedException {
        new TCPServer().start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 自定义业务处理器（解析size,data格式）
                            pipeline.addLast(new TCPServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();
            Log.log.i(String.format("TCP服务端已启动，监听端口：%d", PORT));
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("TCP服务端已停止");
        }
    }

}