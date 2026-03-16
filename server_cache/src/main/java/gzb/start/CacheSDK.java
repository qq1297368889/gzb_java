package gzb.start;

import gzb.frame.netty.entity.PacketPromise;
import gzb.frame.netty.tools.TCPTools;
import gzb.tools.Config;
import gzb.tools.NettyTools;
import gzb.tools.OnlyId;
import gzb.tools.Tools;
import gzb.tools.cache.object.ByteBuff;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
// 极简实现 只为验证主体逻辑 边缘情况没处理
public class CacheSDK {
    Socket socket;
    int sid=0;
    int index = 0;

    public static void main(String[] args) throws IOException {
        CacheSDK cacheSDK = new CacheSDK("127.0.0.1", 6666, 0);
        System.out.println(cacheSDK.put("test1", "123456789", 2));
        System.out.println(cacheSDK.get("test1"));
        System.out.println(cacheSDK.del("test1"));
        System.out.println(cacheSDK.get("test1"));
    }

    public CacheSDK(String host, int port, int index) throws IOException {
        socket = new Socket(host, port);
        this.index = index;
    }

    static byte[] state = "1234567890".getBytes();

    public void close() throws IOException {
        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }

    //简易版实现 不过滤边缘情况 实际使用需要过滤 正常需要转意 = &
    public boolean put(String key, String val, int sec) throws IOException {
        byte[] data = NettyTools.readByteBuf(TCPTools.createDataPacketPromise(
                "/cache/set", 1, 0, "k=" + key + "&v=" + val +
                        "&s=" + sec + "&i=" + index
        ));
        socket.getOutputStream().write(data);
        socket.getOutputStream().flush();

        byte[] bytes = new byte[1024];
        ByteBuff byteBuff = new ByteBuff();
        int index0 = 0;
        List<byte[]> list = null;
        while (true) {
            byteBuff.setIndex(0);
            index0 = socket.getInputStream().read(bytes);
            if (index0==-1) {
                break;
            }
            byteBuff.write(bytes, 0, index0);
            list = TCPTools.readDataPacketByteArray(sid, byteBuff.get());
            if (list != null && list.size() > 0) {
                break;
            }
        }
        if (list==null) {
            return false;
        }
        return list.get(0)[0] == state[0];
    }

    public boolean del(String key) throws IOException {
        byte[] data = NettyTools.readByteBuf(TCPTools.createDataPacketPromise(
                "/cache/del", 1, 0, "k=" + key + "&i=" + index
        ));
        socket.getOutputStream().write(data);
        socket.getOutputStream().flush();

        byte[] bytes = new byte[1024];
        ByteBuff byteBuff = new ByteBuff();
        int index0 = 0;
        List<byte[]> list = null;
        while (true) {
            byteBuff.setIndex(0);
            index0 = socket.getInputStream().read(bytes);
            if (index0==-1) {
                break;
            }
            byteBuff.write(bytes, 0, index0);
            list = TCPTools.readDataPacketByteArray(sid, byteBuff.get());
            if (list != null && list.size() > 0) {
                break;
            }
        }
        return list.get(0)[0] == state[0];
    }

    public String get(String key) throws IOException {
        // 1. 构建并发送请求数据包
        byte[] requestData = NettyTools.readByteBuf(TCPTools.createDataPacketPromise(
                "/cache/get", 1, 0, "k=" + key + "&i=" + index
        ));
        socket.getOutputStream().write(requestData);
        socket.getOutputStream().flush();

        byte[] bytes = new byte[1024];
        ByteBuff byteBuff = new ByteBuff();
        int index = 0;
        List<byte[]> list = null;
        while (true) {
            byteBuff.setIndex(0);
            index = socket.getInputStream().read(bytes);
            byteBuff.write(bytes, 0, index);
            list = TCPTools.readDataPacketByteArray(sid, byteBuff.get());
            if (list != null && list.size() > 0) {
                break;
            }
        }
        if (list.get(0)[0] != state[0]) {
            return null;
        }
        byteBuff.setIndex(0);
        byteBuff.write(list.get(0), 2, list.get(0).length - 2);
        return new String(byteBuff.get());
    }

}
