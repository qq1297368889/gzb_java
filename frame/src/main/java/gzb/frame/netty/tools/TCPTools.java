package gzb.frame.netty.tools;

import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Constant;
import gzb.frame.netty.entity.PacketPromise;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TCPTools {
    public static class PacketEntity {
        long lastActiveTime;
        ByteBuf buffer;

        public PacketEntity(ByteBuf data) {
            this.lastActiveTime = System.currentTimeMillis();
            this.buffer = data;
        }
    }

    private static final Map<String, TCPTools.PacketEntity> sessionMap = new ConcurrentHashMap<>();

    static {
        startCleanupThread();
    }

    // 核心解析逻辑设为私有，不对外暴露 ByteBuf
    private static List<ByteBuf> readDataPacket(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result = null;
        TCPTools.PacketEntity entity = sessionMap.remove(sessionSign);
        ByteBuf cumulation = null;
        try {
            if (entity != null) {
                cumulation = Unpooled.wrappedBuffer(entity.buffer, newData);
                entity.buffer.release();
            } else {
                cumulation = newData;
            }

            while (cumulation.isReadable()) {
                cumulation.markReaderIndex();
                int dataSize = 0;
                boolean foundComma = false;

                while (cumulation.isReadable()) {
                    byte b = cumulation.readByte();
                    if (b == ',') {
                        foundComma = true;
                        break;
                    } else if (b >= '0' && b <= '9') {
                        dataSize = dataSize * 10 + (b - '0');
                    }
                }

                if (foundComma && cumulation.readableBytes() >= dataSize) {
                    // readRetainedSlice 会 +1 引用计数，供后续 byteArray 方法使用
                    ByteBuf frame = cumulation.readRetainedSlice(dataSize);
                    if (result == null) result = new ArrayList<>();
                    result.add(frame);
                } else {
                    cumulation.resetReaderIndex();
                    break;
                }
            }
            if (cumulation.isReadable()) {
                sessionMap.put(sessionSign, new TCPTools.PacketEntity(cumulation.retain()));
            }
        } finally {
            // 如果是 CompositeByteBuf 且不在 Map 里了，这里会正确处理引用计数
            // 注意：newData 通常由 Netty 框架外层释放，所以这里主要针对包装后的 cumulation
        }
        return result;
    }

    public static List<byte[]> readDataPacketByteArray(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0 == null) {
            return null;
        }
        List<byte[]> result = new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(Tools.readByteBuf(byteBuf));
            } finally {
                byteBuf.release();
            }
        }
        return result;
    }

    public static List<String> readDataPacketString(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0 == null) {
            return null;
        }
        List<String> result = new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(new String(Tools.readByteBuf(byteBuf)));
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
        byte[] header = sizeStr.getBytes();
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
            packetPromise.type = Integer.parseInt(new String(data, url_index + 1, type_index - met_index - 1, Config.encoding));
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
    public static PacketPromise readPacketPromise(ByteBuf[] data) {
        // 1. 空数组/空Buf直接抛异常
        if (data == null || data.length == 0) {
            return null;
        }

        // 2. 拼接所有ByteBuf为一个完整的byte[]
        int totalLen = 0;
        for (ByteBuf buf : data) {
            if (buf == null || !buf.isReadable()) {
                throw new IllegalArgumentException("解析失败：ByteBuf数组包含空/不可读的Buf");
            }
            totalLen += buf.readableBytes();
        }

        byte[] allData = new byte[totalLen];
        int offset = 0;
        for (ByteBuf buf : data) {
            buf.readBytes(allData, offset, buf.readableBytes());
            offset += buf.readableBytes();
        }

        // 3. 调用核心的byte[]解析方法
        return readPacketPromise(allData);
    }

    private static void startCleanupThread() {
        ServiceThread.start(1, "ByteTools-Cleanup", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long now = System.currentTimeMillis();
                    Iterator<Map.Entry<String, TCPTools.PacketEntity>> it = sessionMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, TCPTools.PacketEntity> entry = it.next();
                        if (now - entry.getValue().lastActiveTime > 30000) {
                            entry.getValue().buffer.release(); // 释放堆外内存
                            it.remove();
                        }
                    }
                    for (int i = 0; i < 60; i++) {
                        Tools.sleep(1000);
                    }
                }
            }
        });
    }

}
