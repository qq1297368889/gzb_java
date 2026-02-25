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

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.frame.netty.entity.Response;
import gzb.entity.RunRes;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPool;
import gzb.tools.thread.ThreadPoolV3;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class HTTPRequestHandlerV4 extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static final byte[] def = "{\"code\":\"2\",\"message\":\"Server Busy / 服务器 繁忙\"}".getBytes();
    public static Map<Long, AtomicInteger> reqInfo = new ConcurrentHashMap<>();



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Log.log.e("netty 抛出错误", ctx, cause);
        ctx.close();
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        // handler(ctx,req);
        NettyServer.factory.start(ctx,req);
    }

}