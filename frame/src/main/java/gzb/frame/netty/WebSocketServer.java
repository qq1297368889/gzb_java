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

package gzb.frame.netty;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestWebSocket;
import gzb.frame.netty.entity.Response;
import gzb.frame.netty.entity.ResponseDefaultImpl;
import gzb.tools.Tools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Arrays;

public final class WebSocketServer {
    static {

        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(WebSocketServer.class));
    }

    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 创建主线程组，用于接受客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 创建工作线程组，用于处理 I/O 读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务器启动引导类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // Http 编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 异步处理大文件
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 聚合 HTTP 消息，使 HttpObject 可以完整接受
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            // WebSocket 协议处理器，握手、心跳、协议处理等
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                            // 自定义的 WebSocket 处理器
                            pipeline.addLast(new MyWebSocketHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();
            System.out.println("WebSocket server started at port " + port);

            // 等待服务器套接字关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅地关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new WebSocketServer(port).run();
    }
}

class MyWebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        ByteBuf content = frame.content();
        if (content.readableBytes() < 6) {
            ctx.close();
        }
        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);
        int len1 = 0;
        int len2 = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 32) {
                len1 = i;
                len2++;
            }
            if (len2 == 3) {
                break;
            }
        }
        if (len2 != 3) {
            ctx.close();
        }

        byte[] bytes2 = new byte[len1];
        System.arraycopy(bytes, 0, bytes2, 0, bytes2.length);
        String uri = new String(bytes2);
        System.out.println("uri=" + uri);
        String[] arr1 = uri.split(" ");
        String method = arr1[0];
        String path = arr1[1];
        int len = Integer.parseInt(arr1[2]);
        System.out.println("method=" + method);
        System.out.println("path=" + path);
        System.out.println("len=" + len);
        if (len > -1 && bytes.length >= len + len1 + 1) {
            byte[] bytes3 = new byte[len];
            System.arraycopy(bytes, bytes2.length + 1, bytes3, 0, bytes3.length);
            System.out.println("body=" + new String(bytes3));

        } else {
            ctx.close();
        }

        ctx.channel().writeAndFlush(new TextWebSocketFrame("hello"));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当有新的客户端连接时
        System.out.println("A new client connected: " + ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 当客户端断开连接时
        System.out.println("A client disconnected: " + ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常处理
        cause.printStackTrace();
        ctx.close();
    }
}
