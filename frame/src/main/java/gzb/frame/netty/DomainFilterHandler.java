package gzb.frame.netty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.HashSet;
import java.util.Set;

public class DomainFilterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 允许的域名列表
    private final Set<String> allowedDomains;

    public DomainFilterHandler(Set<String> allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        // 从请求头中获取 Host
        String host = request.headers().get(HttpHeaderNames.HOST);

        if (host != null && allowedDomains.contains(host)) {
            // 如果域名在允许列表中，将请求传递给下一个处理器
            ctx.fireChannelRead(request.retain());
        } else {
            // 如果域名不合法，直接返回 403 Forbidden 并关闭连接
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.FORBIDDEN
            );
            ctx.writeAndFlush(response);
            ctx.close();
        }
    }
}