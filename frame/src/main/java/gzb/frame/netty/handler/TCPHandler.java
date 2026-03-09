package gzb.frame.netty.handler;
import gzb.frame.PublicEntrance;
import gzb.frame.netty.Server;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.frame.netty.entity.RequestTcpImpl;
import io.netty.channel.ChannelHandler.Sharable;
import gzb.frame.netty.entity.PacketPromise;
import gzb.frame.netty.tools.TCPTools;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;
@Sharable
public class TCPHandler extends ChannelInboundHandlerAdapter {
    /**
     * 1. 捕获连接打开事件（客户端成功连接到服务端时触发）
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int sessionId = ctx.channel().hashCode();
        PublicEntrance.putTcpSession(sessionId,ctx);
        Log.log.d("TCP连接已建立","sessionId", sessionId);
        super.channelActive(ctx);
    }

    /**
     * 2. 捕获连接关闭事件（TCP连接断开时触发，主动/被动关闭都会触发）
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        int sessionId = ctx.channel().hashCode();
        PublicEntrance.removeTcpSession(sessionId);
        Log.log.d("TCP连接已关闭","sessionId", sessionId);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int sessionId = ctx.channel().hashCode();
        ByteBuf buf = (ByteBuf) msg;
        List<byte[]> list=null;
        try {
           list = TCPTools.readDataPacketByteArray(sessionId, buf);
            if (list != null) {
                for (byte[] bytes : list) {
                    PacketPromise packetPromise = TCPTools.readPacketPromise(bytes);
                    if (packetPromise!=null) {
                        Request request=new RequestTcpImpl(ctx,packetPromise);
                        Server.factory.start(request,request.getResponse());
                    }
                }
            }
        } catch (Exception e) {
            Log.log.e("TCP协议错误","sessionId",sessionId,e);
            ctx.close();
        } finally {
            //必须在 list 不为空才能释放内存 否则 如果是半包 也就是list=null 释放了内存的话 下半部分数据到了 合包会出错 bug已修复
            if (list != null&& buf != null) {
                buf.release();
            }
        }

    }
    /**
     * 超时/异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String sessionId = ctx.channel().id().asLongText();
        Log.log.e("TCP连接错误","sessionId", sessionId,cause);
        ctx.close();
    }
}