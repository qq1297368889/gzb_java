package gzb.tools;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.cookie.Cookie;

public class NettyTools {


    public static ByteBuf toByteBuf(Object chunk) {
        if (chunk instanceof byte[]) {
            return Unpooled.copiedBuffer((byte[]) chunk);
        } else if (chunk instanceof String) {
            return Unpooled.copiedBuffer((String) chunk, Config.encoding);
        } else if (chunk == null) {
            return Unpooled.EMPTY_BUFFER;
        } else {
            String str1 = Tools.toJson(chunk);
            if (str1 == null) {
                return Unpooled.EMPTY_BUFFER;
            } else {
                return Unpooled.copiedBuffer(str1, Config.encoding);
            }
        }
    }
    public static ByteBuf toByteBuf0(Object chunk) {
        byte[] bytes;
        if (chunk instanceof byte[]) {
            bytes = (byte[]) chunk;
        } else if (chunk instanceof String) {
            bytes = ((String) chunk).getBytes(Config.encoding);
        } else if (chunk == null) {
            bytes = new byte[0];
        } else {
            String str1 = Tools.toJson(chunk);
            if (str1 == null) {
                bytes = new byte[0];
            } else {
                bytes = str1.getBytes(Config.encoding);
            }
        }
        return Unpooled.copiedBuffer(bytes);
    }


    //记不住这个api 封装一下
    public static byte[] readByteBuf(io.netty.buffer.ByteBuf buf) {
        // readableBytes() 获取实际有数据的长度
        byte[] bytes = new byte[buf.readableBytes()];
        // getBytes 不会移动读索引(readerIndex)，适合调试打印
        buf.getBytes(buf.readerIndex(), bytes);
        return bytes;
    }

    //记不住这个api 封装一下
    public static ByteBuf loadByteBuf(byte[] data) {
        return Unpooled.wrappedBuffer(data);
    }

    public static String encodeSingleCookie(Cookie cookie) {
        gzb.tools.cache.object.ObjectCache.Entity entity0 = gzb.tools.cache.object.ObjectCache.SB_CACHE0.get();
        int index0 = entity0.open();
        try {
            StringBuilder sb = entity0.get(index0);
            sb.append(cookie.name()).append('=').append(cookie.value());

            if (cookie.path() != null) {
                sb.append("; Path=").append(cookie.path());
            }
            if (cookie.domain() != null) {
                sb.append("; Domain=").append(cookie.domain());
            }
            if (cookie.maxAge() >= 0) {
                sb.append("; Max-Age=").append(cookie.maxAge());
            }
            if (cookie.isSecure()) {
                sb.append("; Secure");
            }
            if (cookie.isHttpOnly()) {
                sb.append("; HttpOnly");
            }
            return sb.toString();

        } finally {
            entity0.close(index0);
        }
    }
}
