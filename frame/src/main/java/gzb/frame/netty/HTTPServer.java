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
import gzb.frame.language.Template;
import gzb.frame.netty.handler.HTTPHandler;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class HTTPServer{
    public static Log log = Log.log;
    public static HTTPStaticFileHandler HTTPStaticFileHandler = new HTTPStaticFileHandler();
    public static Set<String> allowedDomains;

    public static void main(String[] args) throws Exception {
        int port = 3080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new HTTPServer().start(port);
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

        } catch (Exception e) {
            log.e("服务器初始化失败", e);
            System.exit(1314520);
        }
    }

    public void start(int port) throws Exception {
        start(port, Config.mainThreadNum, Config.ioThreadNum);
    }
    HTTPServerInitializer httpServerInitializer=new HTTPServerInitializer();
    public void start(int port, int main_thread_num, int io_thread_num) throws Exception {
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
        int backlog = Config.bizAwaitNum;
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

            bootstrap.option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .childHandler(httpServerInitializer)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

            log.i(
                    Template.THIS_LANGUAGE[72],
            Template.THIS_LANGUAGE[70], port,
                    Template.THIS_LANGUAGE[66], main_thread_num,
                    Template.THIS_LANGUAGE[67], io_thread_num,
                    Template.THIS_LANGUAGE[68], Config.bizThreadNum < 1 ? Template.THIS_LANGUAGE[71] : Config.bizThreadNum,
                    Template.THIS_LANGUAGE[69], Config.bizAwaitNum < 1 ? Config.cpu * 2000 : Config.bizAwaitNum);
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