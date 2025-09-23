package gzb.frame.netty;

import gzb.tools.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ExceptionHandler());
        // 异常处理器
        if (!NettyServer.allowedDomains.contains("0.0.0.0")) {
            p.addLast(new DomainFilterHandler(NettyServer.allowedDomains));
        }
        // 编解码器：将字节流转换为 HTTP 消息对象
        p.addLast(new HttpServerCodec());
        // 消息聚合器：将分段的 HTTP 消息聚合为一个完整的请求/响应
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        // 请求处理器
        p.addLast(new RequestHandlerV4());
        if (Config.maxPostSize>0) {
            p.addLast(new HttpObjectAggregator(Config.maxPostSize));
        }

        if (Config.compression){
            p.addLast(new HttpContentCompressor(9, 15, 8, Config.compressionMinSize));
        }



    }
}