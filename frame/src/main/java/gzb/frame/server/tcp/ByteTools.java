package gzb.frame.server.tcp;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.thread.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.*;
import java.util.concurrent.*;

public class ByteTools {

    public static void main(String[] args) {
        test();
    }
    public static void test() {
        String sid = "gzb_test_session";
        System.out.println("=== 开始 gzb one 框架粘包测试 ===");

        // 准备数据
        String msg1 = "hello";      // 封装后: "5,hello"
        String msg2 = "world_gzb";  // 封装后: "9,world_gzb"
        String msg3 = "number_one"; // 封装后: "10,number_one"

        // 1. 测试：正常多包粘在一起
        System.out.println("\n场景1：正常多包粘连...");
        ByteBuf multiPackets = Unpooled.buffer();
        multiPackets.writeBytes(createDataPacket(msg1));
        multiPackets.writeBytes(createDataPacket(msg2));

        List<String> res1 = readDataPacketString(sid, multiPackets);
        printResult(res1); // 预期：[hello, world_gzb]

        // 2. 测试：极端的半包情况（一个包拆成三次发）
        System.out.println("\n场景2：极其细碎的半包...");
        // 目标包: "10,number_one"
        // 第一次：只发 "1"
        readDataPacketString(sid, Unpooled.wrappedBuffer("1".getBytes()));
        // 第二次：发 "0,num"
        readDataPacketString(sid, Unpooled.wrappedBuffer("0,num".getBytes()));
        // 第三次：发 "ber_one" (补全剩下的)
        List<String> res2 = readDataPacketString(sid, Unpooled.wrappedBuffer("ber_one".getBytes()));
        printResult(res2); // 预期：[number_one]

        // 3. 测试：复杂混合情况 (一个半包 + 一个整包 + 半个包)
        System.out.println("\n场景3：混合包情况...");
        // 构造数据流： "5,he" (半个) + "llo5,apple" (补全前一个+一个整包) + "2,o" (半个)
        readDataPacketString(sid, Unpooled.wrappedBuffer("5,he".getBytes()));
        List<String> res3 = readDataPacketString(sid, Unpooled.wrappedBuffer("llo5,apple2,o".getBytes()));
        printResult(res3); // 预期：[hello, apple] (2,o 还在缓存)

        // 补全最后的 2,ok
        List<String> res4 = readDataPacketString(sid, Unpooled.wrappedBuffer("k".getBytes()));
        printResult(res4); // 预期：[ok]

        System.out.println("\n=== 测试完成 ===");
    }

    private static void printResult(List<String> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("结果：[空] (数据尚未完整，已存入缓存)");
        } else {
            System.out.println("解析结果：" + list);
        }
    }
    private static class PacketEntity {
        long lastActiveTime;
        ByteBuf buffer;

        PacketEntity(ByteBuf data) {
            this.lastActiveTime = System.currentTimeMillis();
            this.buffer = data;
        }
    }

    private static final Map<String, PacketEntity> sessionMap = new ConcurrentHashMap<>();
    private static final byte COMMA = (byte) ',';

    static {
        startCleanupThread();
    }
    // 核心解析逻辑设为私有，不对外暴露 ByteBuf
    private static List<ByteBuf> readDataPacket(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result = null;
        PacketEntity entity = sessionMap.remove(sessionSign);
        ByteBuf cumulation = null;

        try {
            if (entity != null) {
                // 组合旧缓存和新数据
                cumulation = Unpooled.wrappedBuffer(entity.buffer, newData);
                // 关键：wrappedBuffer 会增加引用计数，合并后原来的 entity.buffer 就不需要了
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

            // 4. 剩余部分存入缓存
            if (cumulation.isReadable()) {
                sessionMap.put(sessionSign, new PacketEntity(cumulation.retain()));
            }
        } finally {
            // 如果是 CompositeByteBuf 且不在 Map 里了，这里会正确处理引用计数
            // 注意：newData 通常由 Netty 框架外层释放，所以这里主要针对包装后的 cumulation
        }
        return result;
    }
    public static List<byte[]> readDataPacketByteArray(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0==null) {
            return null;
        }
        List<byte[]> result =new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(Tools.readByteBuf(byteBuf));
            }finally {
                byteBuf.release();
            }
        }
        return result;
    }
    public static List<String> readDataPacketString(String sessionSign, ByteBuf newData) {
        List<ByteBuf> result0 = readDataPacket(sessionSign, newData);
        if (result0==null) {
            return null;
        }
        List<String> result =new ArrayList<>(result0.size());
        for (ByteBuf byteBuf : result0) {
            try {
                result.add(new String(Tools.readByteBuf(byteBuf)));
            }finally {
                byteBuf.release();
            }
        }
        return result;
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

    private static void startCleanupThread() {
        ThreadPool.startService(1, "ByteTools-Cleanup", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long now = System.currentTimeMillis();
                    Iterator<Map.Entry<String, PacketEntity>> it = sessionMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, PacketEntity> entry = it.next();
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