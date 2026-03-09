package gzb.frame.netty;

import gzb.frame.netty.handler.UDPHandler;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPServer {
    public static Log log = Log.log;
    public EventLoopGroup group;

    public static void main(String[] args) throws Exception {
        int port = 2001;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new UDPServer().start(port);
    }
    public void start(int port) throws Exception {
        start(port,Config.mainThreadNum,Config.ioThreadNum);
    }
    public void start(int port, int main_thread_num, int io_thread_num) throws Exception {
        if (group != null) {
            log.e("同一个服务器无法多次启动");
            return;
        }
        Bootstrap bootstrap = new Bootstrap();
        try {
            if (Tools.isLinux()) {
                group = new EpollEventLoopGroup(io_thread_num);
                bootstrap.group(group).channel(EpollDatagramChannel.class);
            } else {
                group = new NioEventLoopGroup(io_thread_num);
                bootstrap.group(group).channel(NioDatagramChannel.class);
            }

            bootstrap.option(ChannelOption.SO_BROADCAST, true) // 允许广播
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .handler(new UDPHandler());

            log.i(
                    "start udp server", port,
                    "netty main 线程数量", main_thread_num,
                    "netty io 线程数量", io_thread_num,
                    "biz 线程数量", Config.bizThreadNum,
                    "biz 队列最大数量", Config.bizAwaitNum);
            Channel channel = bootstrap.bind(port).sync().channel();

            channel.closeFuture().addListener(future -> {
                log.i("UDP 服务关闭...");
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.e("启动UDP服务器失败", e);
            System.exit(1);
        }
    }
}