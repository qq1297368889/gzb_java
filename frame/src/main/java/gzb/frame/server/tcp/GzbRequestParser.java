package gzb.frame.server.tcp;

import gzb.frame.netty.OptimizedParameterParser;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * gzb one 协议解析器
 * 核心目标：高性能、低对象创建、协议自适应
 */
public class GzbRequestParser {

    public static class GzbRequest {
        public String method;
        public String url;
        public String path;
        public String protocol;
        public Map<String, String> headers = new HashMap<>();
        public Map<String, List<Object>> params = new HashMap<>();
        public byte[] body;

        @Override
        public String toString() {
            return "GzbRequest{" +
                    "method='" + method + '\'' +
                    ", path='" + path + '\'' +
                    ", paramsCount=" + params.size() +
                    ", bodyLen=" + (body != null ? body.length : 0) +
                    '}';
        }
    }

    /**
     * 将字节流解析为 GzbRequest 对象
     * 兼容标准 HTTP 和 极简 TCP
     */
    public static GzbRequest readRequestObject(byte[] bytes) {
        GzbRequest req = new GzbRequest();
        int n = bytes.length;
        if (n == 0) return req;

        int i = 0;

        // 1. 解析请求行 (Method URL Protocol)
        // 提取 Method
        int start = i;
        while (i < n && bytes[i] != ' ') i++;
        req.method = new String(bytes, start, i - start, StandardCharsets.US_ASCII);

        // 提取 URL
        i++; start = i;
        while (i < n && bytes[i] != ' ' && bytes[i] != '\r') i++;
        req.url = new String(bytes, start, i - start, StandardCharsets.UTF_8);

        // --- 复用你的核心解析类 ---
        // 解析 URL 参数并获取 Path
        req.path = OptimizedParameterParser.parseUrlEncoded(req.url, req.params, false);

        // 识别协议 (兼容极简模式)
        if (i < n && bytes[i] == ' ') {
            i++; start = i;
            while (i < n && bytes[i] != '\r') i++;
            req.protocol = new String(bytes, start, i - start, StandardCharsets.US_ASCII);
            i += 2; // 跳过 \r\n
        } else {
            req.protocol = "GZB-TCP"; // 极简模式
            if (i < n && bytes[i] == '\r') i += 2;
        }

        // 2. 解析 Headers (标准 HTTP 逻辑)
        while (i < n) {
            // 检查是否到达 Header 结束 (双换行)
            if (bytes[i] == '\r' && (i + 1 < n && bytes[i+1] == '\n')) {
                i += 2;
                break;
            }

            start = i;
            while (i < n && bytes[i] != ':') i++;
            if (i >= n) break;

            String key = new String(bytes, start, i - start, StandardCharsets.US_ASCII).trim().toLowerCase();
            i++; // 跳过 ':'

            start = i;
            while (i < n && bytes[i] != '\r') i++;
            String value = new String(bytes, start, i - start, StandardCharsets.UTF_8).trim();
            req.headers.put(key, value);

            i += 2; // 跳过 \r\n
        }

        // 3. 处理 Body
        int bodyStart = i;
        int bodyLen = n - bodyStart;
        if (bodyLen > 0) {
            req.body = new byte[bodyLen];
            System.arraycopy(bytes, bodyStart, req.body, 0, bodyLen);

            // 如果是 Form 表单，继续复用解析逻辑处理 Body
            String contentType = req.headers.get("content-type");
            if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                String bodyData = new String(req.body, StandardCharsets.UTF_8);
                OptimizedParameterParser.parseUrlEncoded(bodyData, req.params, true);
            }
        }

        return req;
    }
}