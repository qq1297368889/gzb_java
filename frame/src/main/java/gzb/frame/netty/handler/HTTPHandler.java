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
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.AttributeKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@ChannelHandler.Sharable
public class HTTPHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause.getMessage().contains("Connection reset")) {
            return;
        }

        Log.log.e("netty error", ctx, cause);
        ctx.close();
    }
/*
    /// 试验性 主题逻辑通了 不过想实用 需要大量优化 目前总耗时大概3微秒
    public static final AttributeKey<ByteBuf> SESSION_BUF = AttributeKey.valueOf("session_buf");
   @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof ByteBuf) {
            ByteBuf data = (ByteBuf) msg;
            try {
                ByteBuf sessionBuf = ctx.channel().attr(SESSION_BUF).get();
                if (sessionBuf == null) {
                    sessionBuf = ctx.alloc().directBuffer(2048);
                    ctx.channel().attr(SESSION_BUF).set(sessionBuf);
                }
                sessionBuf.writeBytes(data);
                while (true) {
                    HTTPTools.Entity entity = HTTPTools.analysis(sessionBuf);
                    if (entity != null) {
                        //entity.print();
                        start(entity,ctx);
                        if (sessionBuf.readableBytes() > 0) {
                            continue;
                        }
                    }
                    break;
                }
                if (!sessionBuf.isReadable()) {
                    sessionBuf.clear();
                } else if (sessionBuf.readerIndex() > 4096) {
                    sessionBuf.discardReadBytes();
                }

            } finally {
                data.release();
            }
        }

    }
    private void start(HTTPTools.Entity entity,ChannelHandlerContext ctx){
        if (entity.getURL().equals("/text")) {
            NettyTools.sendHTTP(ctx, NettyTools.HELLO_WORD, 200,  NettyTools.content_type_text, true);
        } else {
            Request request = new RequestHTTPImpl(ctx, entity);
            Server.factory.startV2(request, request.getResponse(),entity);
        }
    }*/
    public static FullHttpResponse response=null;
    static{
         response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(NettyTools.HELLO_WORD),
                false
        );
        HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, NettyTools.CONTENT_TYPE);
        headers.set(HttpHeaderNames.CONTENT_LENGTH, NettyTools.CONTENT_LENGTH);
        //headers.set(HttpHeaderNames.SERVER, NettyTools.SERVER_NAME);
        //headers.set(HttpHeaderNames.DATE, NettyTools.THIS_TIME);
    }
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.uri().equals("/text")) {
            ctx.write(response.retainedDuplicate());
        } else {
            Request request = new RequestDefaultImpl(ctx, req);
            Server.factory.start(request, request.getResponse());
        }
        ctx.flush();
    }
}