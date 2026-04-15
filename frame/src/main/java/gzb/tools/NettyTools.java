package gzb.tools;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import gzb.tools.cache.object.ByteBuff;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import gzb.tools.thread.ServiceThread;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.AsciiString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NettyTools {

    public static final byte[] HELLO_WORD = "Hello, World!".getBytes(Config.encoding);

    public static final AsciiString SERVER_NAME = AsciiString.cached(Config.frameName);
    public static final AsciiString CONTENT_TYPE = AsciiString.cached("text/plain");
    public static final AsciiString CONTENT_LENGTH = AsciiString.cached(String.valueOf(HELLO_WORD.length));

    public static AsciiString THIS_TIME = null;
    public static byte[] THIS_TIME_BYTE = null;
    public static long THIS_TIME_MILLIS = 0;
    private static final SimpleDateFormat HTTP_DATE_FORMAT = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss zzz",
            Locale.US
    );
    static {
        HTTP_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
/*
        getServerTime();
        ServiceThread.start("NettyTools-time-update-1s-loop", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //时间每秒更新
                    try {
                       getServerTime();
                    } catch (Exception e) {
                        Log.log.e("NettyTools.static{}", e);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();//响应中断
                        break;
                    }

                }
            }
        });*/
    }

    /// header0+code+header1+date+header2+size+header3
    public static byte[] header0 = ("HTTP/1.1 ").getBytes(Config.encoding);
    public static byte[] STATE_200 = "200".getBytes(Config.encoding);
    //public static byte[] header1 = (" OK\r\nServer: " + Config.frameName + "\r\nDate: ").getBytes(Config.encoding);
    //public static byte[] header2 = ("\r\nContent-Length: ").getBytes(Config.encoding);
    public static byte[] header1 = (" OK\r\nContent-Length: ").getBytes(Config.encoding);
    public static byte[] header3 = ("\r\n").getBytes(Config.encoding);


    public static byte[] keepAlive_true = "Connection: keep-alive\r\n".getBytes(Config.encoding);
    public static byte[] keepAlive_false = "Connection: close\r\n".getBytes(Config.encoding);
    public static byte[] send_start="transfer-encoding: chunked\r\n".getBytes();
    public static byte[] send_end =new byte[]{ '0', '\r', '\n', '\r', '\n' };
    public static String content_type_text ="content-type: text/plain; charset="+Config.encoding+"\r\n";

    public static void sendHTTP(ChannelHandlerContext ctx, Object body, int code, String header, boolean keepAlive) {
        byte[] bytes = toByte(body);
        GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
        int index = entity.byteBuffCacheEntity.open();
        try {
            ByteBuff byteBuff = entity.byteBuffCacheEntity.get(index);
            byteBuff.write(header0);
            if (code == 200) {
                byteBuff.write(STATE_200);
            } else {
                byteBuff.write((code + "").getBytes(Config.encoding));
            }
            byteBuff.write(header1);
            byteBuff.write((bytes.length + "").getBytes(Config.encoding));
            byteBuff.write(header3);
            if (keepAlive) {
                //byteBuff.write(keepAlive_true);
            } else {
                byteBuff.write(keepAlive_false);
            }
            if (header !=null) {
                byteBuff.write(header.getBytes(Config.encoding));//确保\r\n结尾的
            }

            byteBuff.write(header3);
            byteBuff.write(bytes);
            //Log.log.i(new String(byteBuff.get()));
            ChannelFuture future = ctx.channel().write(Unpooled.wrappedBuffer(byteBuff.get()));
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } finally {
            entity.byteBuffCacheEntity.close(index);
        }
    }

    public static void getServerTime() {
        Date dateObj = new Date();
        THIS_TIME_MILLIS = dateObj.getTime();
        THIS_TIME = AsciiString.cached(HTTP_DATE_FORMAT.format(dateObj));
        THIS_TIME_BYTE = THIS_TIME.toByteArray();
    }

    public static ByteBuf toByteBuf(Object chunk) {
        if (chunk instanceof String) {
            return Unpooled.wrappedBuffer(((String) chunk).getBytes(Config.encoding));
        } else if (chunk instanceof byte[]) {
            return Unpooled.wrappedBuffer((byte[]) chunk);
        } else if (chunk == null) {
            return Unpooled.EMPTY_BUFFER;
        } else {
            return Unpooled.wrappedBuffer(JSON.toJSONBytes(chunk, "yyyy-MM-dd HH:mm:ss", JSONWriter.Feature.WriteNonStringValueAsString));
        }
    }

    public static byte[] toByte(Object chunk) {
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
        return bytes;
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
        GzbThreadLocal.Entity entity0 = GzbThreadLocal.context.get();
        int index0 = entity0.stringBuilderCacheEntity.open();
        try {
            StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
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
            entity0.stringBuilderCacheEntity.close(index0);
        }
    }
}
