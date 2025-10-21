package gzb.start;

import gzb.frame.factory.v4.UpdateServer;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.thread.ThreadPool;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 2082;
        ThreadPool.startService(10, "ddos", new Runnable() {
            @Override
            public void run() {
                HTTP http =new HTTP();
                byte[] bytes;
                long start = System.currentTimeMillis();
                int size=0;
                for (int i = 0; i < 40000; i++) {
                    try {
                        bytes=http.httpGet(host,port,"/test/test1?msg=msg1");
                        if (bytes==null || bytes.length !=52) {
                            size++;
                        }
                    } catch (IOException e) {
                        size++;
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName()+" "+size+" "+(end-start));
            }
        });

    }
}
class HTTP{
    // HTTP 协议常量
    private static final byte CR = 0x0D; // \r
    private static final byte LF = 0x0A; // \n
    private static final byte[] CRLF_BYTES = {CR, LF};
    private static final String CRLF = "\r\n";

    // 连接状态
    private Socket socket;

    // --- 辅助方法：字节级读取一行（用于解析 Header） ---

    /**
     * 手动从 InputStream 中读取一行字节，直到遇到 CRLF 或 LF。
     * @param is 原始 InputStream
     * @return 包含该行内容的 byte 数组 (不包含 CRLF/LF)。
     */
    private byte[] readLineBytes(InputStream is) throws IOException {
        ByteArrayBuilder bab = new ByteArrayBuilder();
        int prev = -1;
        int curr;

        while ((curr = is.read()) != -1) {
            if (curr == LF) {
                // 遇到 LF，行结束
                if (prev == CR) {
                    // 如果前一个是 CR，需要移除它（处理 CRLF）
                    bab.removeLast();
                }
                return bab.toByteArray();
            }
            bab.append((byte) curr);
            prev = curr;
        }
        // 如果流结束
        return bab.size() > 0 ? bab.toByteArray() : null;
    }

    // --- 辅助类：高性能字节数组构建器 (避免频繁 resize) ---
    private static class ByteArrayBuilder {
        private byte[] data = new byte[128];
        private int size = 0;

        public void append(byte b) {
            if (size == data.length) {
                data = Arrays.copyOf(data, data.length * 2);
            }
            data[size++] = b;
        }

        public void removeLast() {
            if (size > 0) {
                size--;
            }
        }

        public byte[] toByteArray() {
            return Arrays.copyOf(data, size);
        }

        public int size() {
            return size;
        }
    }

    // --- 核心 HTTP GET 方法 ---

    /**
     * 执行 HTTP GET 请求并复用连接。
     * @param host 目标主机名
     * @param port 目标端口
     * @param path 请求路径
     * @return 响应 Body 的 byte 数组
     */
    public byte[] httpGet(String host, int port, String path) throws IOException {

        // 1. 构造请求头（使用 byte[] 拼接，避免 String 转换开销）
        byte[] requestBytes = buildRequestBytes(host, path);

        // 2. 连接复用检查
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            socket = new Socket(host, port);
            // 建议设置超时，防止 read 永久阻塞
            // socket.setSoTimeout(5000);
            System.out.println("创建新的 TCP 连接到 " + socket.getInetAddress().getHostAddress());
        }

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        // 3. 发送请求
        os.write(requestBytes);
        os.flush();

        // 4. 读取并解析 HTTP 响应头
        int contentLength = 0;
        byte[] lineBytes;

        // 使用字节级 readLineBytes() 读取 Header
        while ((lineBytes = readLineBytes(is)) != null && lineBytes.length > 0) {
            String line = new String(lineBytes, StandardCharsets.US_ASCII); // 只解析 Header 部分为 String

            // 性能提示：尽管我们使用了 String.startsWith，但由于 Header 行很短，这里的开销非常小。
            if (line.toLowerCase().startsWith("content-length:")) {
                String lengthStr = line.substring("content-length:".length()).trim();
                contentLength = Integer.parseInt(lengthStr);
            }
        }

        // 5. 读取 Body
        if (contentLength > 0) {
            byte[] bodyBytes = new byte[contentLength];
            int totalRead = 0;
            int bytesRead;

            while (totalRead < contentLength) {
                int bytesToRead = contentLength - totalRead;
                bytesRead = is.read(bodyBytes, totalRead, bytesToRead);

                if (bytesRead == -1) {
                    System.err.println("警告: 服务器在发送完所有数据前关闭了连接。");
                    // 意外关闭连接，则关闭 Socket，以便下次重连
                    closeSocket();
                    return null;
                }
                totalRead += bytesRead;
            }

            if (totalRead == contentLength) {
                return bodyBytes;
            }
        }

        // 如果 Body 为空或解析失败，返回 null
        return null;
    }

    // --- 辅助方法：构造请求字节数组 ---
    private byte[] buildRequestBytes(String host, String path) {
        StringBuilder requestBuilder = new StringBuilder();

        requestBuilder.append("GET ").append(path).append(" HTTP/1.1").append(CRLF);
        requestBuilder.append("Host: ").append(host).append(CRLF);
        requestBuilder.append("User-Agent: RawJavaClient/1.0").append(CRLF);
        requestBuilder.append("Connection: keep-alive").append(CRLF);
        requestBuilder.append(CRLF);

        // 将整个请求一次性转换为字节数组
        return requestBuilder.toString().getBytes(StandardCharsets.US_ASCII);
    }

    // --- 连接清理方法 ---
    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket = null;
            }
        }
    }
}