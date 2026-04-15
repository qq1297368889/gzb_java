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

package gzb.frame.netty.handler;

import gzb.frame.netty.Server;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.frame.netty.entity.RequestHTTPImpl;
import gzb.frame.netty.tools.HTTPTools;
import gzb.tools.NettyTools;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;


@ChannelHandler.Sharable
public class HTTPHandlerV2 extends SimpleChannelInboundHandler<HTTPTools.Entity> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HTTPTools.Entity entity) {
        if (entity.getURL().equals("/text")) {
            NettyTools.sendHTTP(ctx, NettyTools.HELLO_WORD, 200, NettyTools.content_type_text, true);
        } else {
            Request request = new RequestHTTPImpl(ctx, entity);
            Server.factory.start(request, request.getResponse(), entity);
        }
        ctx.flush();
    }
}