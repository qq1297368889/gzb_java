package gzb.frame.netty.tools;

import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Constant;
import gzb.frame.netty.entity.PacketPromise;
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TCPTools {
    public static void main(String[] args) {
        byte[]bytes1=NettyTools.readByteBuf(createDataPacket(Tools.getRandomString(20)));
        byte[]bytes2=NettyTools.readByteBuf(createDataPacket(Tools.getRandomString(10)));
        byte[]bytes0=new byte[bytes1.length+bytes2.length];
        byte[]bytes3=new byte[bytes1.length+20];
        byte[]bytes4=new byte[bytes2.length-5];
        byte[]bytes5=new byte[5];

        for (int i = 0; i < bytes1.length; i++) {
            bytes0[i]=bytes1[i];
        }
        for (int i = 0; i < bytes2.length; i++) {
            bytes0[bytes1.length+i]=bytes2[i];
        }
        for (int i = 0; i < bytes1.length; i++) {
            bytes3[i]=bytes1[i];
        }
        for (int i = 0; i < bytes2.length; i++) {
            if (i<bytes2.length-5) {
                bytes4[i]=bytes2[i];
            }else{
                bytes5[i-bytes4.length]=bytes2[i];
            }
        }
        List<ByteBuf> list=null;
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes0));
        Log.log.i("bytes0 ",list.get(0).toString(Config.encoding),list.get(1).toString(Config.encoding),new String(bytes0));
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes1));
        Log.log.i("bytes1 ",list.get(0).toString(Config.encoding),new String(bytes1));
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes2));
        Log.log.i("bytes2 ",list.get(0).toString(Config.encoding),new String(bytes2));
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes3));
        Log.log.i("bytes3 ",list.get(0).toString(Config.encoding),new String(bytes3));
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes4));
        list=readDataPacket(1,Unpooled.wrappedBuffer(bytes5));
        Log.log.i("bytes5 ",list.get(0).toString(Config.encoding),new String(bytes5));

    }
    public static class PacketEntity {
        long lastActiveTime;
        ByteBuf buffer;

        public PacketEntity(ByteBuf data) {
            this.lastActiveTime = System.currentTimeMillis();
            this.buffer = data;
        }
    }

    private static final Map<Integer, TCPTools.PacketEntity> sessionMap = new ConcurrentHashMap<>();

    static {
        startCleanupThread();

    }
    // 核心解析逻辑设为私有，不对外暴露 ByteBuf
    private static List<ByteBuf> readDataPacket(int sessionSign, ByteBuf newData) {
        PacketEntity entity = sessionMap.remove(sessionSign);
        ByteBuf cumulation = null;
        if (entity != null) {
            while (newData.refCnt() > 1) {
                newData.release();
            }
            entity.buffer.resetReaderIndex();
            cumulation = Unpooled.wrappedBuffer(entity.buffer, newData);
            entity.buffer=cumulation;
        } else {
            cumulation = newData;
            entity=new PacketEntity(cumulation);
        }
        List<ByteBuf>list=null;
        while (cumulation.isReadable()) {
            int dataSize = 0;
            boolean foundComma = false;
            int digitCount = 0;
            while (cumulation.isReadable()) {
                byte b = cumulation.readByte();
                digitCount++;
                if (b == ','){
                    foundComma = true;
                    break;
                }
                if (b<48 || b>57||digitCount > 10) {
                    break;
                }
                dataSize = dataSize * 10 + (b - 48);
            }
            if (!foundComma || digitCount>10) {
                cumulation.release();
                return list;
            }
            if (Config.maxPostSize>0) {
                if (Config.maxPostSize<dataSize) {
                    cumulation.release();
                    return null;
                }
            }
            int size1=cumulation.readableBytes();
            if (size1 >= dataSize) {
                if (list==null) {
                    list=new ArrayList<>();
                }
                list.add(cumulation.readRetainedSlice(dataSize));
                cumulation.markReaderIndex();
            }else{
                cumulation.resetReaderIndex();
                break;
            }
        }
        if (cumulation.readerIndex()<cumulation.readableBytes()-1) {
            sessionMap.put(sessionSign, entity);
        }else{
            cumulation.release();
        }

        return list;
    }

    public static List<byte[]> readDataPacketByteArray(int sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0 == null) {
            return null;
        }
        List<byte[]> result = new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(NettyTools.readByteBuf(byteBuf));
            } finally {
                byteBuf.release();
            }
        }
        return result;
    }
    public static List<byte[]> readDataPacketByteArray(int sessionSign, byte[] newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, NettyTools.loadByteBuf(newData));
        if (result0 == null) {
            return null;
        }
        List<byte[]> result = new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(NettyTools.readByteBuf(byteBuf));
            } finally {
                byteBuf.release();
            }
        }
        return result;
    }

    public static List<String> readDataPacketString(int sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0 == null) {
            return null;
        }
        List<String> result = new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {

                result.add(byteBuf.toString(Config.encoding));
            } finally {
                byteBuf.release();
            }
        }
        return result;
    }

    /**
     * 封装通讯协议
     *
     * @param url  路由地址
     * @param method 请求方式 为了兼容http格式 {"GET", "POST", "PUT", "DELETE"}
     * @param type 数据类型（0-表单，1-JSON，2-BYTE）
     * @param data 数据内容
     * @return 封装后的 ByteBuf，格式为 size,data，其中 data 由 url,type,data 组成
     */
    public static ByteBuf createDataPacketPromise(String url, int method, int type, byte[] data) {
        byte[] bytes1 = (url + "," + method + "," + type + ",").getBytes(Config.encoding);
        ByteBuf buffer = Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(bytes1), Unpooled.wrappedBuffer(data));
        return createDataPacket(buffer);
    }

    //针对 String 数据的便捷方法，内部调用核心逻辑
    public static ByteBuf createDataPacketPromise(String url, int method, int type, String data) {
        return createDataPacketPromise(url, method, type, data.getBytes(Config.encoding));
    }

    //针对 自动序列化对象的便捷方法，内部调用核心逻辑
    public static ByteBuf createDataPacketPromiseObject(String url, int method, int type, Object data) {
        return createDataPacketPromise(url, method, type, Tools.toJson(data).getBytes(Config.encoding));
    }

    public static ByteBuf createDataPacket(ByteBuf data) {
        String sizeStr = data.readableBytes() + ",";
        byte[] header = sizeStr.getBytes(Config.encoding);
        // 组合头部和数据体，全程无数据物理拷贝
        return Unpooled.wrappedBuffer(Unpooled.wrappedBuffer(header), data.retain());
    }

    /**
     * 扩展方法：支持 byte[] 格式
     * 逻辑：先将 byte[] 包装为 ByteBuf，再调用核心逻辑
     */
    public static ByteBuf createDataPacket(byte[] data) {
        if (data == null) return Unpooled.EMPTY_BUFFER;
        // 使用 Unpooled.wrappedBuffer 零拷贝包装原生数组
        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        return createDataPacket(buffer);
    }

    /**
     * 扩展方法：支持 String 格式
     * 逻辑：将字符串转为 UTF-8 字节后直接处理
     */
    public static ByteBuf createDataPacket(String data) {
        if (data == null || data.isEmpty()) {
            // 返回封装后的 "0,"
            return Unpooled.wrappedBuffer("0,".getBytes(Config.encoding));
        }
        // 注意：这里需要将字符串转为字节数组
        byte[] stringBytes = data.getBytes(Config.encoding);
        return createDataPacket(stringBytes);
    }

    /**
     * 解析byte[]格式的数据包（核心方法）
     * 格式要求：url,type,数据体（如：/users/find,1,哈哈哈哈）
     *
     * @param data 本层原始数据
     * @return 解析后的PacketPromise
     * @throws IllegalArgumentException 格式错误/空数据直接抛异常（暴露逻辑错误）
     */
    public static PacketPromise readPacketPromise(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        int url_index = -1;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == ',') {
                url_index = i;
                break;
            }
        }
        if (url_index == -1) {
            Log.log.d("解析失败：格式异常，格式必须为url,met,type,数据体");
            return null;
        }
        int met_index = -1;
        for (int i = url_index + 1; i < data.length; i++) {
            if (data[i] == ',') {
                met_index = i;
                break;
            }
        }
        if (met_index == -1) {
            Log.log.d("解析失败：格式异常，格式必须为url,met,type,数据体");
            return null;
        }
        int type_index = -1;
        for (int i = met_index + 1; i < data.length; i++) {
            if (data[i] == ',') {
                type_index = i;
                break;
            }
        }
        if (type_index == -1) {
            Log.log.d("解析失败：格式异常，格式必须为url,met,type,数据体");
            return null;
        }
        PacketPromise packetPromise = new PacketPromise();
        packetPromise.url = ClassTools.webPathFormat(new String(data, 0, url_index, Config.encoding));

        try {
            int method = Integer.parseInt(new String(data, url_index + 1, met_index - url_index - 1, Config.encoding));
            if (method > Constant.requestMethod.length - 1) {
                return null;
            }
            packetPromise.method = Constant.requestMethod[method];
            packetPromise.type = Integer.parseInt(new String(data, met_index + 1, type_index - met_index - 1, Config.encoding));
        } catch (NumberFormatException e) {
            Log.log.w("协议解析失败", new String(data, Config.encoding));
            return null;
        }
        // 4.3 解析数据体（剩余全部字节）
        int dataLen = data.length - type_index - 1;
        packetPromise.data = new byte[dataLen];
        System.arraycopy(data, type_index + 1, packetPromise.data, 0, dataLen);
        return packetPromise;
    }

    /**
     * 解析ByteBuf[]格式的数据包（适配Netty场景，内部调用byte[]核心方法）
     *
     * @param data ByteBuf数组（所有Buf拼接后为url,type,数据体格式）
     * @return 解析后的PacketPromise
     * @throws IllegalArgumentException 格式错误/空数据直接抛异常
     */
    public static PacketPromise readPacketPromise(ByteBuf data) {
        // 1. 空数组/空Buf直接抛异常
        if (data == null) {
            return null;
        }
        return readPacketPromise(NettyTools.readByteBuf(data));
    }

    private static void startCleanupThread() {
        ServiceThread.start(1, "ByteTools-Cleanup", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long now = System.currentTimeMillis();
                    Iterator<Map.Entry<Integer, TCPTools.PacketEntity>> it = sessionMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Integer, TCPTools.PacketEntity> entry = it.next();
                        if (now - entry.getValue().lastActiveTime > 10*1000) {
                            entry.getValue().buffer.release(); // 释放堆外内存
                            it.remove();
                        }
                    }
                    for (int i = 0; i < 11; i++) {
                        Tools.sleep(1000);
                    }
                }
            }
        });
    }

}
