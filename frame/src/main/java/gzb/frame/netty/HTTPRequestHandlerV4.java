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
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class HTTPRequestHandlerV4 extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static final byte[] def = "{\"code\":\"2\",\"message\":\"Server Busy / 服务器 繁忙\"}".getBytes();
    public static final byte[] hello = "Hello, World!".getBytes();
    public static final ThreadPool THREAD_POOL = new ThreadPool(Config.bizThreadNum, Config.bizAwaitNum);

//ThreadPoolV3    ThreadPool
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        Request request = new RequestDefaultImpl(ctx, req);
        Response response = request.getResponse();
        if (!THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                handler(request, response, ctx, req);
            }
        })) {
            response.setContentType("application/json;charset=UTF-8");
            response.sendAndFlush(def);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Log.log.e("netty 抛出错误", ctx, cause);
        ctx.close();
    }

    private void handler(Request request, Response response, ChannelHandlerContext ctx, FullHttpRequest req) {
        RunRes runRes = NettyServer.factory.request(request, response);//内部有错误捕捉 不可能出错
        if (runRes == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.sendAndFlush("{\"code\":\"2\",\"message\":\"服务器出现意料外的情况\"}");
        } else if (runRes.getState() == 200) {
            response.sendAndFlush(runRes.getData());
        } else if (runRes.getState() == 404) {
            NettyServer.HTTPStaticFileHandler.channelRead0(ctx, req);
        } else {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(runRes.getState()).sendAndFlush(runRes.getData());
        }
    }
}