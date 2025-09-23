package gzb.frame.netty;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.frame.netty.entity.Response;
import gzb.frame.server.http.entity.RunRes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;


public class RequestHandlerV4 extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.uri().startsWith("/text")) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer("{\"code\":\"1\",\"data\":{\"sysFileMd5\":\"101\"},\"message\":\"n01\"}".getBytes()));
            response.headers().set("content-length", response.content().readableBytes());
            ctx.writeAndFlush(response);
            return;
        }
        //long startTime = System.nanoTime();
        Request request = new RequestDefaultImpl(ctx, req);
        //long endTime1 = System.nanoTime();
        Response response = request.getResponse();
        //long endTime2 = System.nanoTime();
        response.setStatus(200);
        RunRes runRes = NettyServer.factory.request(request, response);
        //long endTime3 = System.nanoTime();
        if (runRes.getState() == 200) {
            response.sendAndFlush(runRes.getData());
        } else {
            if (runRes.getState() == 404) {
                NettyServer.staticFileHandler.channelRead0(ctx, req);
            } else {
                response.setStatus(runRes.getState()).sendAndFlush(runRes.getData());
            }
        }
        //log.d("耗时",endTime3-endTime2,endTime2-endTime1,endTime1-startTime,endTime3-startTime,runRes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}