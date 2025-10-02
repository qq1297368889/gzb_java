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

import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.frame.factory.Factory;
import gzb.frame.factory.v4.FactoryImpl;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class NettyServer {
    public static Log log;
    public static Factory factory;
    public static HTTPStaticFileHandler HTTPStaticFileHandler = new HTTPStaticFileHandler();
    public static Set<String> allowedDomains;


    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(NettyServer.class));
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
        log = Config.log;
        try {
            allowedDomains = new HashSet<String>() {{
                String[] arr1 = Config.domain.split(",");
                for (String string : arr1) {
                    add(string.trim());
                }
            }};

            factory = new FactoryImpl();
            long start = System.currentTimeMillis();
            factory.loadJavaDir(Config.codeDir, Config.codePwd, Config.codeIv);
            long end = System.currentTimeMillis();
            log.i("类编译加载耗时", end - start);

            if (Config.permissionsOpen) {
                DataBase dataBase = null;
                try {
                    dataBase = DataBaseFactory.getDataBase(Config.frameDbKey);
                } catch (Exception e) {
                    log.e("默认数据连接失败", e);
                }
                log.i(Config.frameDbKey, "加载权限信息", Tools.loadApiInfo(dataBase, factory.getMappingMap()), "个");
            }
        } catch (Exception e) {
            log.e("服务器启动失败", e);
        }
    }

    public void startHTTPServer(int port) throws Exception {
        startHTTPServer(port, Math.max(Config.cpu / 10, 1), Config.threadNum);
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
        bossGroup = new NioEventLoopGroup(main_thread_num);
        workerGroup = new NioEventLoopGroup(io_thread_num);//,new TestExecutor()
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, Config.maxAwaitNum)         // 待处理连接队列大小
                    .option(ChannelOption.SO_REUSEADDR, true)        // 允许地址重用
                    .childHandler(new HTTPServerInitializer())       // 自定义处理器
                    .childOption(ChannelOption.TCP_NODELAY, true)    // 禁用Nagle算法
                    .childOption(ChannelOption.SO_KEEPALIVE, true)   // 启用TCP心跳保活
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
            log.i("server", port, "main", main_thread_num, "io", io_thread_num);
            // 绑定端口并启动服务器
            bootstrap.bind(new InetSocketAddress(port)).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}