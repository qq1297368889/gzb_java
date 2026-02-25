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

import gzb.frame.PublicEntrance;
import gzb.frame.factory.Factory;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ResourceLeakDetector;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class NettyServer {
    public static Log log = Log.log;
    public static Factory factory;
    public static HTTPStaticFileHandler HTTPStaticFileHandler = new HTTPStaticFileHandler();
    public static Set<String> allowedDomains;

    public static void main(String[] args) throws Exception {
        int port = 3080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new NettyServer().startHTTPServer(port);
    }

    // 负责接收连接
    public EventLoopGroup bossGroup;
    // 负责处理 I/O
    public EventLoopGroup workerGroup;

    public void init() throws Exception {
        try {
            allowedDomains = new HashSet<String>() {{
                String[] arr1 = Config.domain.split(",");
                for (String string : arr1) {
                    add(string.trim());
                }
            }};

            factory = PublicEntrance.factory;
            long start = System.currentTimeMillis();
            if (Config.code_type.startsWith("file")) {
                factory.loadJavaDir(Config.codeDir, Config.codePwd, Config.codeIv);
            }

            long end = System.currentTimeMillis();
            log.i("初始化耗时", end - start);
        } catch (Exception e) {
            log.e("服务器初始化失败", e);
            System.exit(1314520);
        }
    }

    public void startHTTPServer(int port) throws Exception {
        startHTTPServer(port, Config.mainThreadNum, Config.ioThreadNum);
    }

    public void startHTTPServer(int port, int main_thread_num, int io_thread_num) throws Exception {
        init();
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
        int bufferSize = 1024 * Math.max(Config.cpu * 8, 64); // 缓冲区大小：CPU核心数*8K 或 64K（取大值）
        int lowWaterMark = 8 * 1024; // 写缓冲区低水位（固定8K，避免过小）
        int highWaterMark = 32 * 1024; // 写缓冲区高水位（固定32K）


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
                    .childHandler(new HTTPServerInitializer())
                    .childOption(ChannelOption.TCP_NODELAY, true)     // Nagle算法
                    .childOption(ChannelOption.SO_KEEPALIVE, true)    // TCP保活，清理空闲连接
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 已配置
                    ; // 写缓冲区水位，避免OOM
            log.i(
                    "start server", port,
                    "netty main 线程数量", main_thread_num,
                    "netty io 线程数量", io_thread_num,
                    "biz 线程数量", Config.bizThreadNum,
                    "biz 队列最大数量", Config.bizAwaitNum);
            // 绑定端口并启动服务器
            bootstrap.bind(new InetSocketAddress(port)).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            log.e("启动服务器过程出现错误", e);
            System.exit(1314520);
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }

    }

}