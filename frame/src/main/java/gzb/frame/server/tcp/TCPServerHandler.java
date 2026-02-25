package gzb.frame.server.tcp;

import gzb.frame.netty.OptimizedParameterParser;
import gzb.tools.http.HTTP_V3;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class TCPServerHandler extends ChannelInboundHandlerAdapter {
    public static void main(String[] args) throws Exception {
        HTTP_V3 httpV3 = new HTTP_V3();
        //httpV3.get("http://127.0.0.1:8888/html/index.html?p1=1&p1=2&p2=2");
        httpV3.post("http://127.0.0.1:8888/html/index.html?p1=1","p1=2&p2=2");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        try {
            String sessionId = ctx.channel().id().asLongText();
            List<byte[]> list = HttpDataParser.readHTTPRequestData(sessionId, buf);
            for (byte[] bytes : list) {
                System.out.println(new String(bytes));
                System.out.println(GzbRequestParser.readRequestObject(bytes));

                //ctx.writeAndFlush(ByteTools.createDataPacket("收到了数据：" + new String(bytes)));
            }
        } catch (Exception e) {
            System.out.println("服务端解析数据失败：" + e.getMessage());
            ctx.close();
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }

    /**
     * 超时/异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.close();
    }
}