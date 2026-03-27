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

import gzb.frame.netty.handler.TCPHandler;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class TCPServer{
    public static Log log = Log.log;
    public static void main(String[] args) throws Exception {
        int port = 2001;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new TCPServer().start(port);
    }

    // 负责接收连接
    public EventLoopGroup bossGroup;
    // 负责处理 I/O
    public EventLoopGroup workerGroup;


    public void start(int port) throws Exception {
        start(port, Config.mainThreadNum, Config.ioThreadNum);
    }

    public void start(int port, int main_thread_num, int io_thread_num) throws Exception {
        if (main_thread_num < 1 || io_thread_num < 1) {
            log.e("线程数错误,请重新设置", "main", main_thread_num, "io", io_thread_num);
            return;
        }
        if (bossGroup != null || workerGroup != null) {
            log.e("同一个服务器无法多次启动");
            return;
        }
        ServerBootstrap bootstrap = null;
        int backlog = Config.bizAwaitNum; // 监听队列大小（建议10240+）  
        try {
            if (Tools.isLinux()) {
                bossGroup = new EpollEventLoopGroup(main_thread_num);
                workerGroup = new EpollEventLoopGroup(io_thread_num);
                bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(EpollServerSocketChannel.class);
            } else {
                bossGroup = new NioEventLoopGroup(main_thread_num);
                workerGroup = new NioEventLoopGroup(io_thread_num);
                bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class);
            }
            bootstrap.option(ChannelOption.SO_REUSEADDR, true)        // 允许地址重用
                    .option(ChannelOption.SO_BACKLOG, backlog)  // 监听队列大小
                    // === 客户端Socket参数（Worker线程） ===
                    .childHandler(new TCPHandler())
                    .childOption(ChannelOption.TCP_NODELAY, true)     // Nagle算法
                    .childOption(ChannelOption.SO_KEEPALIVE, true)    // TCP保活，清理空闲连接
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 已配置
            ;
            log.i(
                    "start tcp server", port,
                    "netty main 线程数量", main_thread_num,
                    "netty io 线程数量", io_thread_num,
                    "biz 线程数量", Config.bizThreadNum < 1 ? "动态扩容" : Config.bizThreadNum,
                    "biz 队列最大数量", Config.bizAwaitNum < 1 ? Config.cpu * 2000 : Config.bizAwaitNum);
            // 1. 绑定端口（同步阻塞，确保绑定完成）
            ChannelFuture bindFuture = bootstrap.bind(new InetSocketAddress(port)).sync();
            // 2. 异步监听closeFuture，而非同步阻塞
            Channel serverChannel = bindFuture.channel();
            serverChannel.closeFuture().addListener(future -> {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            });
        } catch (Exception e) {
            log.e("启动服务器过程出现错误", e);
            System.exit(1314520);
        }

    }

}