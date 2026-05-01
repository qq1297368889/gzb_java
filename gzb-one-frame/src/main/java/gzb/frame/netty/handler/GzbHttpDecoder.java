package gzb.frame.netty.handler;

import gzb.frame.netty.tools.HTTPTools;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class GzbHttpDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() > 0) {
            HTTPTools.Entity entity = HTTPTools.analysis(in);
            if (entity == null) {
                break;
            }
            out.add(entity);
        }

    }
}