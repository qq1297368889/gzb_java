package gzb.frame.netty;

import gzb.tools.Config;
import gzb.tools.log.Log;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高性能、顺序解析 URL 编码参数的方法。
 * 专注于 Char 级操作，最小化 String/Byte 转换和对象创建。
 */
public class OptimizedParameterParser {
    public static void main(String[] args) throws InterruptedException {
        for (int n = 0; n < 100000; n++) {
            Map<String, List<Object>> map = new HashMap<>();
            long t01 = System.nanoTime();
            parseUrlEncoded("/test/api0/get1?message=message001", map, false);
            long t02 = System.nanoTime();
            System.out.println(((t02 - t01) ) +"  "+map);
        }
        Thread.sleep(1000);
        for (int n = 0; n < 100000; n++) {
            Map<String, List<Object>> map = new HashMap<>();
            long t01 = System.nanoTime();
            parseUrlEncoded("/test/api0/get1?message=message001", map, false);
            long t02 = System.nanoTime();
            System.out.println(((t02 - t01) ) +"  "+map);
        }


    }

    private static final byte[] HEX_TABLE = new byte[128];

    static {
        // 初始化十六进制查找表，用于快速解码 %xx
        for (int i = 0; i < 10; i++) {
            HEX_TABLE['0' + i] = (byte) i;
        }
        for (int i = 0; i < 6; i++) {
            HEX_TABLE['A' + i] = (byte) (10 + i);
            HEX_TABLE['a' + i] = (byte) (10 + i);
        }
    }
    /**
     * 顺序解析 URL 或 POST Body 中的键值对。
     * 例如：?k1=v1&k2=v2 或 k1=v1&k2=v2
     * 目标：尽可能减少对 String.substring() 的依赖，并优化 URL 解码。
     *
     * @param data  要解析的参数字符串。
     * @param parar 存储解析结果的 Map<String, List<Object>>。
     */
    public static String parseUrlEncoded(String data, Map<String, List<Object>> parar, boolean post) {

        if (data == null || data.isEmpty()) {
            return data;
        }
        int len = data.length();
        int keyStart = 0; // 跳过开头的 '?'
        int keyEnd = -1;
        int valueStart = -1;
        int urlEnd = 0;
        if (post) {
            valueStart = 0;
        } else {
            valueStart = data.indexOf('?');
            if (valueStart > -1) {
                urlEnd = valueStart;
                keyStart = valueStart + 1;
            } else {
                urlEnd = data.length();
            }

        }

        // 使用一个可重用的解码器，避免在每次循环中创建对象
        Decoder decoder = new Decoder(len);

        for (int i = keyStart; i < len; i++) {
            char c = data.charAt(i);

            if (keyEnd == -1) {
                // 阶段 1: 寻找 Key 的结束 (字符 '=')
                if (c == '=') {
                    keyEnd = i;
                    valueStart = i + 1;
                } else if (c == '&') {
                    // 发现分隔符，但 Key 没有 Value (例如: "k1&k2=v2")
                    // Key: [keyStart, i], Value: ""
                    String key = decoder.decode(data, keyStart, i);
                    addParameter(parar, key, "");
                    keyStart = i + 1;
                }
            } else {
                // 阶段 2: 寻找 Value 的结束 (字符 '&')
                if (c == '&' || i == len - 1) {
                    int valueEnd = i;
                    if (i == len - 1) {
                        valueEnd = len; // 最后一个字符，包含在 Value 内
                    }

                    // 提取并解码 Key 和 Value
                    String key = decoder.decode(data, keyStart, keyEnd);
                    String value = decoder.decode(data, valueStart, valueEnd);
                    addParameter(parar, key, value);

                    // 重置状态，开始寻找下一个 Key
                    keyEnd = -1;
                    keyStart = i + 1;
                }
            }
        }

        // 处理最后一个没有 Value 的 Key (例如: "k1=v1&k2")
        if (keyEnd != -1) {
            String key = decoder.decode(data, keyStart, keyEnd);
            addParameter(parar, key, "");
        }
        // 如果只有一个 Key 且没有 = 和 & (例如: "k1")
        else if (keyStart < len) {
            String key = decoder.decode(data, keyStart, len);
            addParameter(parar, key, "");
        }
        if (urlEnd > 0) {
            return data.substring(0, urlEnd);
        } else {
            return null;
        }
    }

    /**
     * 辅助方法：添加参数到 Map
     */
    private static void addParameter(Map<String, List<Object>> parar, String key, String value) {
        List<Object> values = parar.computeIfAbsent(key, k -> new ArrayList<>(1));
        // 注意：这里存储的是 String，如果需要 Object，请自行转换
        values.add(value);
    }

    /**
     * 内部高性能 URL 解码器 (处理 %xx 和 +)
     * 使用 char 数组和位操作，以减少堆分配。
     */
    private static class Decoder {
        private final char[] chars;
        private final byte[] bytes;

        public Decoder(int maxLen) {
            // 预分配最大可能的长度，以便重用
            this.chars = new char[maxLen];
            this.bytes = new byte[maxLen];
        }

        /**
         * 解码指定范围的字符串，并处理 %xx 和 +
         */
        public String decode(String data, int start, int end) {
            if (start >= end) {
                return "";
            }

            int charIndex = 0;
            int byteIndex = 0;
            boolean needsDecoding = false;

            for (int i = start; i < end; i++) {
                char c = data.charAt(i);

                if (c == '%') {
                    needsDecoding = true;
                    if (i + 2 >= end) {
                        // 格式错误，直接跳过或抛出异常
                        break;
                    }

                    // 快速解码 %xx
                    byte b1 = HEX_TABLE[data.charAt(i + 1)];
                    byte b2 = HEX_TABLE[data.charAt(i + 2)];
                    bytes[byteIndex++] = (byte) ((b1 << 4) | b2);
                    i += 2;

                } else if (c == '+') {
                    needsDecoding = true;
                    bytes[byteIndex++] = ' '; // + 替换为空格

                } else {
                    // 非编码字符，直接存储为 char，并假设它是单字节字符，同时存储到 bytes 数组
                    chars[charIndex++] = c;
                    bytes[byteIndex++] = (byte) c;
                }
            }

            if (needsDecoding) {
                return new String(bytes, 0, byteIndex, StandardCharsets.UTF_8);
            } else {
                return data.substring(start, end);
            }
        }
    }
}