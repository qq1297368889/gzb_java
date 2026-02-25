package gzb.frame.server.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class HandlerV2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        try {
            String sessionId = ctx.channel().id().asLongText();
            List<String> list = ByteTools.readDataPacketString(sessionId, buf);
            if (list!=null) {
                for (String string : list) {
                    ctx.writeAndFlush(ByteTools.createDataPacket("收到了数据：" +string));
                }
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