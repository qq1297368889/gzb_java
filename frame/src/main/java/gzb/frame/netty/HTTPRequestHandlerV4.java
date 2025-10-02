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
import gzb.frame.server.http.entity.RunRes;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;


public class HTTPRequestHandlerV4 extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

        //long startTime = System.nanoTime();
        Request request = new RequestDefaultImpl(ctx, req);
        //long endTime1 = System.nanoTime();
        Response response = request.getResponse();
        //long endTime2 = System.nanoTime();
        response.setStatus(200);
        //这个方法不会出错 有try包裹
        RunRes runRes = null;
        try {
            runRes = NettyServer.factory.request(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //你说的意思必须在这里释放  而我放在了后边 这没区别啊
            //long endTime3 = System.nanoTime();
            if (runRes.getState() == 200) {
                response.sendAndFlush(runRes.getData());
            } else {
                if (runRes.getState() == 404) {
                    NettyServer.HTTPStaticFileHandler.channelRead0(ctx, req);
                } else {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setStatus(runRes.getState()).sendAndFlush(runRes.getData());
                }
            }
            //System.out.println("req.content().refCnt()  "+req.content().refCnt());
            //log.d("耗时",endTime3-endTime2,endTime2-endTime1,endTime1-startTime,endTime3-startTime,runRes);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}