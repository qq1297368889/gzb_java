package gzb.frame.netty.handler;

import gzb.frame.netty.Server;
import gzb.frame.netty.entity.PacketPromise;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestTcpImpl;
import gzb.frame.netty.entity.RequestUdpImpl;
import gzb.frame.netty.tools.TCPTools;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.List;

public class UDPHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket packet = (DatagramPacket) msg;
        ByteBuf buf = packet.content();
        int sessionId = ctx.channel().hashCode();
        List<byte[]> list = null;
        try {
            list = TCPTools.readDataPacketByteArray(sessionId, buf);
            if (list != null) {
                for (byte[] bytes : list) {
                    PacketPromise packetPromise = TCPTools.readPacketPromise(bytes);
                    if (packetPromise != null) {
                        Request request = new RequestUdpImpl(ctx, packetPromise, packet.sender());
                        Server.factory.start(request, request.getResponse());
                    }
                }
            }
        } catch (Exception e) {
            Log.log.e("UDP 协议错误", "sessionId", sessionId, e);
            ctx.close();
        }
    }

    /**
     * 超时/异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String sessionId = ctx.channel().id().asLongText();
        Log.log.e("UDP连接错误", "sessionId", sessionId, cause);
        ctx.close();
    }
}