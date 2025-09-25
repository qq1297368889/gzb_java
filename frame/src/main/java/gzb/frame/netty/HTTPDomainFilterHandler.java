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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Set;

public class HTTPDomainFilterHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 允许的域名列表
    private final Set<String> allowedDomains;

    public HTTPDomainFilterHandler(Set<String> allowedDomains) {
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