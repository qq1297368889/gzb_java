package gzb.frame.server.udp;

import gzb.frame.server.udp.entity.Packet;
import gzb.tools.thread.ThreadPool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UDP {
    static {
        ThreadPool threadPool = new ThreadPool(1, 1);
        threadPool.execute(() -> {
            while (true) {
                try {
                    List<String> delList = new ArrayList<>();
                    long time = System.currentTimeMillis() - (3000);
                    UDP.mapData.forEach((k, v) -> {
                        AtomicInteger atomicInteger = new AtomicInteger();
                        AtomicLong min = new AtomicLong();
                        AtomicLong max = new AtomicLong();
                        v.forEach((k1, v1) -> {
                            if (min.get() > v1.getTime()) {
                                min.set(v1.getTime());
                            }
                            if (max.get() < v1.getTime()) {
                                max.set(v1.getTime());
                            }
                            if (v1.getTime() < time) {
                                atomicInteger.getAndIncrement();
                            }
                        });
                        if (atomicInteger.get() == v.size()) {
                            delList.add(k);
                        } else if (max.get() - min.get() > 30 * 1000) {
                            delList.add(k);
                        }
                    });
                    for (String s : delList) {
                        System.out.println("delete:" + s);
                        UDP.mapData.remove(s);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static final int HEADER_SIZE = 11;
    public static final int MAX_PACKET_SIZE = 1200;

    // 使用ConcurrentHashMap提升并发性能
    public static final Map<String, Map<Integer, Packet>> mapData = new ConcurrentHashMap<>();
    public static final Map<String, AtomicInteger> mapDataSize = new ConcurrentHashMap<>();
    public static final AtomicInteger idGenerator = new AtomicInteger(0);
    public static Packet read(DatagramSocket server) throws IOException {
        byte[] buffer = new byte[MAX_PACKET_SIZE + HEADER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            server.receive(packet);
            String ip = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            String baseKey = ip + ":" + port;

            // 解析Header
            int totalFragments = bytesToInt(buffer[0], buffer[1]);
            int currentFragment = bytesToInt(buffer[2], buffer[3]);
            int fragmentSize = bytesToInt(buffer[4], buffer[5]);
            int totalDataSize = bytesToInt(buffer[6], buffer[7]);
            int type = buffer[10] & 0xFF; // 读取 type
            int sessionId = bytesToInt(buffer[11], buffer[12]); // 注意偏移量现在是 11 和 12

            String sessionKey = baseKey + ":" + sessionId;
            if (fragmentSize + HEADER_SIZE != packet.getLength()) {
                continue; // 无效数据包
            }

            // 提取数据部分
            byte[] fragmentData = new byte[fragmentSize];
            System.arraycopy(buffer, HEADER_SIZE, fragmentData, 0, fragmentSize);

            // 原子操作更新分片数据
            mapData.computeIfAbsent(sessionKey, k -> new ConcurrentHashMap<>())
                    .put(currentFragment, createUDPBytes(ip, port, fragmentData, sessionId, type)); // 传递 type

            mapDataSize.computeIfAbsent(sessionKey, k -> new AtomicInteger(0))
                    .addAndGet(fragmentSize);

            // 检查是否收集完所有分片
            Map<Integer, Packet> fragments = mapData.get(sessionKey);
            if (fragments != null && fragments.size() == totalFragments
                    && mapDataSize.get(sessionKey).get() == totalDataSize) {

                // 合并数据
                byte[] fullData = new byte[totalDataSize];
                int offset = 0;
                for (int i = 1; i <= totalFragments; i++) {
                    Packet fragment = fragments.get(i);
                    if (fragment == null) break;
                    System.arraycopy(fragment.getBytes(), 0, fullData, offset, fragment.getBytes().length);
                    offset += fragment.getBytes().length;
                }

                // 清理会话数据
                mapData.remove(sessionKey);
                mapDataSize.remove(sessionKey);

                return createUDPBytes(ip, port, fullData, sessionId, type); // 传递 type
            }
        }
    }
    public static Packet read0(DatagramSocket server) throws IOException {
        byte[] buffer = new byte[MAX_PACKET_SIZE + HEADER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            server.receive(packet);
            String ip = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            String baseKey = ip + ":" + port;

            // 解析Header
            int totalFragments = bytesToInt(buffer[0], buffer[1]);
            int currentFragment = bytesToInt(buffer[2], buffer[3]);
            int fragmentSize = bytesToInt(buffer[4], buffer[5]);
            int totalDataSize = bytesToInt(buffer[6], buffer[7]);
            int sessionId = bytesToInt(buffer[8], buffer[9]);
            String sessionKey = baseKey + ":" + sessionId;

            if (fragmentSize + HEADER_SIZE != packet.getLength()) {
                continue; // 无效数据包
            }

            // 提取数据部分
            byte[] fragmentData = new byte[fragmentSize];
            System.arraycopy(buffer, HEADER_SIZE, fragmentData, 0, fragmentSize);

            // 原子操作更新分片数据
            mapData.computeIfAbsent(sessionKey, k -> new ConcurrentHashMap<>())
                    .put(currentFragment, createUDPBytes(ip, port, fragmentData, sessionId));

            mapDataSize.computeIfAbsent(sessionKey, k -> new AtomicInteger(0))
                    .addAndGet(fragmentSize);

            // 检查是否收集完所有分片
            Map<Integer, Packet> fragments = mapData.get(sessionKey);
            if (fragments != null && fragments.size() == totalFragments
                    && mapDataSize.get(sessionKey).get() == totalDataSize) {

                // 合并数据
                byte[] fullData = new byte[totalDataSize];
                int offset = 0;
                for (int i = 1; i <= totalFragments; i++) {
                    Packet fragment = fragments.get(i);
                    if (fragment == null) break;
                    System.arraycopy(fragment.getBytes(), 0, fullData, offset, fragment.getBytes().length);
                    offset += fragment.getBytes().length;
                }

                // 清理会话数据
                mapData.remove(sessionKey);
                mapDataSize.remove(sessionKey);

                return createUDPBytes(ip, port, fullData, sessionId);
            }
        }
    }

    public static void send(DatagramSocket socket, Packet udpBytes) throws IOException {
        send(socket, udpBytes, 0);
    }

    public static void send(DatagramSocket socket, Packet udpBytes, int fragmentIndexThis) throws IOException {
        send(socket, udpBytes.getIp(), udpBytes.getPort(), udpBytes.getBytes(), udpBytes.getSessionId(), fragmentIndexThis, udpBytes.getType());
    }

    public static void send(DatagramSocket socket, String ip, int port, byte[] data) throws IOException {
        send(socket, ip, port, data, idGenerator.getAndUpdate(i -> (i >= 65535) ? 0 : i + 1), 0, 1); // 默认 type 为 0
    }

    public static void send(DatagramSocket socket, String ip, int port, byte[] data, int sessionId, int fragmentIndexThis, int type) throws IOException {
        final int fragmentSize = MAX_PACKET_SIZE;
        final int totalFragments = (data.length + fragmentSize - 1) / fragmentSize;
        if (fragmentIndexThis < 1) {
            fragmentIndexThis = 1;
        }
        for (int fragmentIndex = fragmentIndexThis; fragmentIndex <= totalFragments; fragmentIndex++) {
            int offset = (fragmentIndex - 1) * fragmentSize;
            int length = Math.min(fragmentSize, data.length - offset);

            byte[] packetBuffer = new byte[HEADER_SIZE + length];
            // 设置Header
            System.arraycopy(intToBytes(totalFragments), 0, packetBuffer, 0, 2);
            System.arraycopy(intToBytes(fragmentIndex), 0, packetBuffer, 2, 2);
            System.arraycopy(intToBytes(length), 0, packetBuffer, 4, 2);
            System.arraycopy(intToBytes(data.length), 0, packetBuffer, 6, 2);
            packetBuffer[10] = (byte) type; // 设置 type
            System.arraycopy(intToBytes(sessionId), 0, packetBuffer, 11, 2); // 注意偏移量现在是 11
            // 填充数据
            System.arraycopy(data, offset, packetBuffer, HEADER_SIZE, length);
            // 发送数据包
            DatagramPacket packet = new DatagramPacket(
                    packetBuffer,
                    packetBuffer.length,
                    InetAddress.getByName(ip),
                    port
            );
            socket.send(packet);
        }
    }

    // 工具方法优化
    private static Packet createUDPBytes(String ip, int port, byte[] data, int sessionId) {
        return createUDPBytes(ip, port, data, sessionId, 0); // 默认 type 为 0
    }

    private static Packet createUDPBytes(String ip, int port, byte[] data, int sessionId, int type) {
        Packet udpBytes = new Packet();
        udpBytes.setIp(ip);
        udpBytes.setPort(port);
        udpBytes.setBytes(data);
        udpBytes.setSessionId(sessionId);
        udpBytes.setTime(System.currentTimeMillis());
        udpBytes.setType(type); // 设置 type
        return udpBytes;
    }

    private static byte[] intToBytes(int value) {
        return new byte[]{(byte) (value >>> 8), (byte) value};
    }

    private static int bytesToInt(byte high, byte low) {
        return ((high & 0xFF) << 8) | (low & 0xFF);
    }
}