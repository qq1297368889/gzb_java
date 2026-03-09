package gzb.frame.factory;

import gzb.frame.netty.tools.TCPTools;
import gzb.tools.NettyTools;
import gzb.tools.Tools;
import gzb.tools.cache.object.ByteBuff;
import gzb.tools.cache.object.ObjectCache;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RPC_TCP {
    public static ThreadLocal<RPC_TCP> RPC_TCP = new ThreadLocal<>();
    public static Map<Integer, RPC_TCP> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        RPC_TCP rpcTcp = new RPC_TCP("127.0.0.1", 2081);
        String res=rpcTcp.call("test/api0/get1", 0, 0, "message=哈哈哈");
        Log.log.i(rpcTcp.socket,sessions,res);
    }

    static {
        ServiceThread.start(new Runnable() {
            @Override
            public void run() {
                List<Integer> list = new ArrayList<>();
                while (true) {
                    Tools.sleep(1000 * 10, 1000 * 20);
                    for (Map.Entry<Integer, RPC_TCP> integerSocketEntry : sessions.entrySet()) {
                        RPC_TCP rpc = integerSocketEntry.getValue();
                        int sid = integerSocketEntry.getKey();
                        try {
                            if (!rpc.socket.isConnected()) {
                                rpc.socket.connect(rpc.socket.getRemoteSocketAddress());
                            }
                            if (!rpc.socket.isConnected()) {
                                list.add(sid);
                                continue;
                            }
                            String res = rpc.call("/ping", 0, 0, "t=" + System.currentTimeMillis());
                            System.out.println(res);
                        } catch (IOException e) {
                            Log.log.e("RPC_TCP-出现错误", e);
                        }
                    }
                    while (list.size() > 0) {
                        sessions.remove(list.remove(0));
                    }
                }
            }
        });
    }

    public String ip;
    public int port;
    public int sid = 0;
    public Socket socket = null;

    public RPC_TCP(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        sid = socket.hashCode();
        sessions.put(sid, this);
        this.port = port;
        this.ip = ip;
    }

    static byte[] state = "1234567890".getBytes();

    public String call(String url, int met, int type, String data) throws IOException {
        byte[] bytes0 = NettyTools.readByteBuf(TCPTools.createDataPacketPromise(
                url, met, type, data));
        socket.getOutputStream().write(bytes0);
        socket.getOutputStream().flush();
        byte[] bytes = new byte[128];
        ObjectCache.Entity entity = ObjectCache.SB_BYTE_BUFF.get();
        int index0 = entity.open();
        try {
            ByteBuff byteBuff = entity.getByteBuff(index0);
            int index = 0;
            List<byte[]> list = null;
            while (true) {
                index = socket.getInputStream().read(bytes);
                byteBuff.write(bytes, 0, index);
                list = TCPTools.readDataPacketByteArray(sid, byteBuff.get());
                if (list != null && list.size() > 0) {
                    break;
                }
            }
            byteBuff.setIndex(0);
            byteBuff.write(list.get(0), 0, list.get(0).length);
            return new String(byteBuff.get());
        } finally {
            entity.close(index0);
        }

    }
}
