package gzb.frame.server.tcp;

import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.RequestDefaultImpl;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.thread.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * gzb one 框架 - 极致优化版 HTTP 多包拆解工具类
 */
public class HttpDataParser {

    // 内部类封装，将两次 Hash 寻址合二为一
    private static class SessionContext {
        CompositeByteBuf cumulation;
        long lastAccessTime;

        SessionContext() {
            this.cumulation = Unpooled.compositeBuffer();
            this.lastAccessTime = System.currentTimeMillis();
        }

        void release() {
            if (cumulation != null && cumulation.refCnt() > 0) {
                cumulation.release();
            }
        }
    }

    private static final Map<String, SessionContext> SESSION_CACHE = new ConcurrentHashMap<>();
    private static final long EXPIRE_TIME = 30 * 1000; // 30秒无活动则清理

    static {
        // 独立线程定期清理泄露或超时的会话数据
        ThreadPool.startService(1, "gzb-http-cleaner", () -> {
            while (true) {
                Tools.sleep(10000); // 每10秒检查一次
                long now = System.currentTimeMillis();
                Iterator<Map.Entry<String, SessionContext>> it = SESSION_CACHE.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, SessionContext> entry = it.next();
                    if (now - entry.getValue().lastAccessTime > EXPIRE_TIME) {
                        entry.getValue().release();
                        it.remove();
                        System.out.println("gzb one 清理了超时会话: " + entry.getKey());
                    }
                }
            }
        });
    }

    public static class GzbRequest {
        public String method;
        public String url;
        public String path; // 不含参数的部分
        public String protocol;
        public Map<String, String> headers = new HashMap<>();
        public Map<String, String> queryParams = new HashMap<>();
        public byte[] body;

        @Override
        public String toString() {
            return "GzbRequest{" +
                    "method='" + method + '\'' +
                    ", url='" + url + '\'' +
                    ", path='" + path + '\'' +
                    ", protocol='" + protocol + '\'' +
                    ", headers=" + headers +
                    ", queryParams=" + queryParams +
                    ", body=" + Arrays.toString(body) +
                    '}';
        }
    }

    public static GzbRequest readRequestObject(byte[] bytes) {
        GzbRequest req = new GzbRequest();
        int i = 0;
        int n = bytes.length;

        // --- 1. 解析请求行 (Method, URL, Protocol) ---
        // 获取 Method (直到空格)
        int start = i;
        while (i < n && bytes[i] != ' ') i++;
        req.method = new String(bytes, start, i - start, CharsetUtil.US_ASCII);

        // 获取 URL (直到下一个空格)
        i++; // 跳过空格
        start = i;
        while (i < n && bytes[i] != ' ') i++;
        req.url = new String(bytes, start, i - start, CharsetUtil.UTF_8);

        // 顺便解析 URL 中的 Query 参数 (例如 /index?id=1)
        parseUrlParams(req);

        // 获取 Protocol (直到换行)
        i++; // 跳过空格
        start = i;
        while (i < n && bytes[i] != '\r') i++;
        req.protocol = new String(bytes, start, i - start, CharsetUtil.US_ASCII);
        i += 2; // 跳过 \r\n

        // --- 2. 循环解析 Headers (直到遇到双换行) ---
        while (i < n) {
            // 检查是否到了 Header 的尽头 (\r\n\r\n)
            if (bytes[i] == '\r' && (i + 1 < n && bytes[i + 1] == '\n')) {
                i += 2; // 跳过最后的 \r\n
                break;
            }

            // 解析 Key
            start = i;
            while (i < n && bytes[i] != ':') i++;
            String key = new String(bytes, start, i - start, CharsetUtil.US_ASCII).trim();

            // 解析 Value
            i++; // 跳过冒号
            start = i;
            while (i < n && bytes[i] != '\r') i++;
            String value = new String(bytes, start, i - start, CharsetUtil.UTF_8).trim();

            req.headers.put(key, value);
            i += 2; // 跳过 \r\n
        }

        // --- 3. 提取 Body ---
        int bodyLen = n - i;
        if (bodyLen > 0) {
            req.body = new byte[bodyLen];
            System.arraycopy(bytes, i, req.body, 0, bodyLen);
        }

        // 打印结果
        System.out.println("gzb one 解析完成: " + req.method + " " + req.path);
        System.out.println("参数: " + req.queryParams);
        return req;
    }

    /**
     * 内部解析 URL 路径和参数
     */
    private static void parseUrlParams(GzbRequest req) {
        String url = req.url;
        int qIdx = url.indexOf('?');
        if (qIdx == -1) {
            req.path = url;
            return;
        }
        req.path = url.substring(0, qIdx);
        String query = url.substring(qIdx + 1);
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int eqIdx = pair.indexOf('=');
            if (eqIdx != -1) {
                req.queryParams.put(pair.substring(0, eqIdx), pair.substring(eqIdx + 1));
            }
        }
    }

    public static List<byte[]> readHTTPRequestData(String sessionSign, ByteBuf newData) {
        List<byte[]> packages = new ArrayList<>();

        // 1. 一次 Hash 操作获取上下文
        SessionContext context = SESSION_CACHE.computeIfAbsent(sessionSign, k -> new SessionContext());

        // 2. 更新数据与时间
        context.lastAccessTime = System.currentTimeMillis();
        CompositeByteBuf cumulation = context.cumulation;
        cumulation.addComponent(true, newData.retain());

        try {
            while (true) {
                cumulation.markReaderIndex();

                int headerEndIndex = findDoubleCrlf(cumulation);
                if (headerEndIndex == -1) {
                    cumulation.resetReaderIndex();
                    break;
                }

                int readerIdx = cumulation.readerIndex();
                int headerLen = headerEndIndex + 4 - readerIdx;

                // 解析 Content-Length (内部逻辑已针对性能优化)
                int bodyLength = getContentLength(cumulation, headerEndIndex);
                int totalExpectedLength = headerLen + bodyLength;

                if (cumulation.readableBytes() < totalExpectedLength) {
                    cumulation.resetReaderIndex();
                    break;
                }

                // 3. 提取完整数据包
                byte[] fullData = new byte[totalExpectedLength];
                cumulation.readBytes(fullData);
                packages.add(fullData);

                if (!cumulation.isReadable()) {
                    // 数据读完了，为了节省内存，可以考虑清理，但保留 Context 对象
                    // 或者直接移除等待下次重新创建，这里选择移除以保证内存最净
                    context.release();
                    SESSION_CACHE.remove(sessionSign);
                    break;
                }
            }
        } catch (Exception e) {
            context.release();
            SESSION_CACHE.remove(sessionSign);
        }

        return packages;
    }

    private static int findDoubleCrlf(ByteBuf buf) {
        int start = buf.readerIndex();
        int end = buf.writerIndex();
        // 针对字节搜索的循环优化
        for (int i = start; i <= end - 4; i++) {
            if (buf.getByte(i) == '\r' && buf.getByte(i + 1) == '\n' &&
                    buf.getByte(i + 2) == '\r' && buf.getByte(i + 3) == '\n') {
                return i;
            }
        }
        return -1;
    }

    private static int getContentLength(ByteBuf buf, int headerEnd) {
        int readerIdx = buf.readerIndex();
        // 仅在 Header 区域进行字符串转换
        String headerPart = buf.toString(readerIdx, headerEnd - readerIdx, CharsetUtil.US_ASCII);

        // 使用更高效的查找方式
        int start = indexOfIgnoreCase(headerPart, "content-length:");
        if (start == -1) return 0;

        int valueStart = start + 15; // "content-length:".length()
        int valueEnd = headerPart.indexOf("\r\n", valueStart);
        if (valueEnd == -1) valueEnd = headerPart.length();

        try {
            return Integer.parseInt(headerPart.substring(valueStart, valueEnd).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // 自实现简单的忽略大小写查找，避免 toLowerCase() 产生新字符串
    private static int indexOfIgnoreCase(String src, String target) {
        int n = src.length();
        int m = target.length();
        for (int i = 0; i <= n - m; i++) {
            if (src.regionMatches(true, i, target, 0, m)) {
                return i;
            }
        }
        return -1;
    }
}