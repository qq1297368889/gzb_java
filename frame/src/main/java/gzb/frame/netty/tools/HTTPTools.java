package gzb.frame.netty.tools;

import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ByteProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HTTPTools {

    public static class Entity {
        public byte[] bytes;
        public int method; // 0GET 1POST 2PUT 3DELETE
        public int[] version; // bytes中 http version [start, end]
        public int[] url; // bytes中 url [start, end]
        public int[][] header; // [[key_s, key_e, val_s, val_e], ...]
        public int[] body; // bytes中 body [start, end]
        public int[] query; // bytes中 query [start, end]
        public int index;
        String version_str = null;
        String url_str = null;
        String body_str = null;
        byte []body_byte = null;
        String query_str = null;
        Map<String, String> header_map = null;
        int is_keep = -1;
        int type = -1;// 0表单 1json 2文件
        String type_string = null;// 0表单 1json 2文件

        public boolean isKeep() {
            if (is_keep==-1) {
                HTTPTools.initEntity(this);
            }
            return is_keep==1;
        }

        public int getType() {
            if (type == -1) {
                HTTPTools.initEntity(this);
            }
            return type;
        }
        public String getTypeString() {
            if (type_string == null) {
                HTTPTools.initEntity(this);
            }
            return type_string;
        }
        public String getVersion() {
            if (version_str == null) {
                version_str = new String(bytes, version[0], version[1] - version[0]);
            }
            return version_str;
        }

        public String getURL() {
            if (url_str == null) {
                url_str = new String(bytes, url[0], url[1] - url[0]);
                url_str= ClassTools.webPathFormat(url_str);
            }

            return url_str;
        }

        public String getBody() {
            if (body_str == null) {
                body_str = new String(bytes, body[0], body[1] - body[0]);
            }
            return body_str;
        }
        public byte[] getBodyByte() {
            if (body_byte == null) {
                body_byte=new byte[body[1] - body[0]];
                System.arraycopy(bytes, 0, body_byte, 0, body_byte.length);
            }
            return body_byte;
        }
        Map<String,List<Object>> parameter=null;
        public Map<String,List<Object>> getParameters() throws IOException {
            if (parameter == null) {
                parameter=new HashMap<>();
                HTTPTools.parsingPostData(parameter, this, Config.uploadDir);
            }
            return parameter;
        }
        public String getQuery() {
            if (query_str == null) {
                query_str = new String(bytes, query[0], query[1] - query[0]);
            }
            return query_str;
        }

        public Map<String, String> getHeader() {
            if (header_map == null) {
                header_map = readHeaderMap(this);
            }
            return header_map;
        }

        public void print() {

            Log.log.i(
                    "getBody", getBody(),
                    "isKeep", isKeep(),
                    "getType", getType(),
                    "getTypeString", getTypeString(),
                    "getURL", getURL(),
                    "getQuery", getQuery(),
                    "getVersion", getVersion(),
                    "method", method,
                    "getHeader", getHeader()
            );
        }
    }

    static byte[][] static_bytes = new byte[][]
            {
                    "GET".getBytes(),
                    "POST".getBytes(),
                    "PUT".getBytes(),
                    "DELETE".getBytes(),
                    "content-length".getBytes(),
                    "connection".getBytes(),
                    "keep-alive".getBytes(),
                    "content-type".getBytes(),
                    "application/json".getBytes(),
                    "multipart/form-data".getBytes(),
            };
    /**
     * 处理流水线粘包，解析出所有的 HTTP 请求实体
     */
    public static Entity analysis0(ByteBuf byteBuf) {
        byteBuf.retain();
        byteBuf.release();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        byteBuf.markReaderIndex();
        Entity entity = null;
        entity = new Entity();
        entity.index = 0;
        entity.bytes = data;

        // 1. 解析 Method (简单的空格分隔判断)
        int methodEnd = findByte(data, entity.index, (byte) ' ');
        if (methodEnd == -1) return null;
        if (entity.index + 3 >= data.length && data[entity.index] == static_bytes[0][0] && data[entity.index + 1] == static_bytes[0][1] && data[entity.index + 2] == static_bytes[0][2]) {
            entity.method = 0;
        } else if (entity.index + 4 >= data.length && data[entity.index] == static_bytes[1][0] && data[entity.index + 1] == static_bytes[1][1] && data[entity.index + 2] == static_bytes[1][2] && data[entity.index + 3] == static_bytes[1][3]) {
            entity.method = 1;
        } else if (entity.index + 3 >= data.length && data[entity.index] == static_bytes[2][0] && data[entity.index + 1] == static_bytes[2][1] && data[entity.index + 2] == static_bytes[2][2]) {
            entity.method = 2;
        } else if (entity.index + 5 >= data.length && data[entity.index] == static_bytes[3][0] && data[entity.index + 1] == static_bytes[3][1] && data[entity.index + 2] == static_bytes[3][2] && data[entity.index + 3] == static_bytes[3][3] && data[entity.index + 4] == static_bytes[3][4]) {
            entity.method = 3;
        }
        int urlStart = methodEnd + 1;
        int urlEnd = findByte(data, urlStart, (byte) '?');
        if (urlEnd > -1) {
            entity.url = new int[]{urlStart, urlEnd};
            int query_end = findByte(data, urlStart, (byte) ' ');
            if (query_end == -1) {
                return null;
            }
            entity.query = new int[]{urlEnd + 1, query_end};
        } else {
            urlEnd = findByte(data, urlStart, (byte) ' ');
            if (urlEnd == -1) {
                return null;
            }
            entity.url = new int[]{urlStart, urlEnd};
            entity.query = new int[]{0, 0};
        }
        int verStart = urlEnd + 1;
        int verEnd = findLineEnd(data, verStart);
        if (verEnd == -1) {
            return null;
        }
        entity.version = new int[]{verStart, verEnd};
        List<int[]> headerList = new ArrayList<>(16);
        entity.index = verEnd + 2;
        while (entity.index < data.length && !(data[entity.index] == '\r' && data[entity.index + 1] == '\n')) {
            int lineEnd = findLineEnd(data, entity.index);
            if (lineEnd == -1) {
                return null;
            }
            int colonPos = findByte(data, entity.index, (byte) ':');
            if (colonPos != -1 && colonPos < lineEnd) {
                toLowerCase(entity.bytes, entity.index, colonPos);
                headerList.add(new int[]{entity.index, colonPos, colonPos + 2, lineEnd});
            }
            entity.index = lineEnd + 2;
        }
        entity.header = headerList.toArray(new int[0][0]);
        entity.index += 2;
        int contentLength = getLengthFromHeader(entity);
        if (contentLength > 0) {
            entity.body = new int[]{entity.index, entity.index + contentLength};
            entity.index += contentLength;
        } else {
            entity.body = new int[]{0, 0};
        }
        if (entity.index >= data.length) {
            byteBuf.clear();
        } else {
            byteBuf.markReaderIndex();
        }
        return entity;
    }

    /**
     * 处理流水线粘包，解析出一个完整的 Entity。
     * 如果数据不足一个整包，返回 null，且 byteBuf 指针保持不动。
     */
    public static Entity analysis(ByteBuf byteBuf) {
        //Unsafe.getUnsafe().getLong()
        int beginReaderIndex = byteBuf.readerIndex();
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes < 10) return null;
        byte[] data = new byte[readableBytes];
        byteBuf.getBytes(beginReaderIndex, data);

        Entity entity = new Entity();
        entity.bytes = data;
        int cursor = 0;
        int methodEnd = findByte(data, cursor, (byte) ' ');
        if (methodEnd == -1) return null;
        int methodLen = methodEnd - cursor;
        if (methodLen == 3 && data[0] == 'G' && data[1] == 'E' && data[2] == 'T') {
            entity.method = 0;
        } else if (methodLen == 4 && data[0] == 'P' && data[1] == 'O' && data[2] == 'S' && data[3] == 'T') {
            entity.method = 1;
        } else if (methodLen == 3 && data[0] == 'P' && data[1] == 'U' && data[2] == 'T') {
            entity.method = 2;
        } else if (methodLen == 6 && data[0] == 'D' && data[1] == 'E' && data[2] == 'L') {
            entity.method = 3;
        }
        int urlStart = methodEnd + 1;
        int spaceAfterUrl = findByte(data, urlStart, (byte) ' ');
        if (spaceAfterUrl == -1) return null; // 半包

        int questionMark = findByteRange(data, urlStart, spaceAfterUrl, (byte) '?');
        if (questionMark != -1) {
            entity.url = new int[]{urlStart, questionMark};
            entity.query = new int[]{questionMark + 1, spaceAfterUrl};
        } else {
            entity.url = new int[]{urlStart, spaceAfterUrl};
            entity.query = new int[]{0, 0};
        }
        int verEnd = findLineEnd(data, spaceAfterUrl + 1);
        if (verEnd == -1) return null; // 半包
        entity.version = new int[]{spaceAfterUrl + 1, verEnd};

        List<int[]> headerList = new ArrayList<>(16);
        cursor = verEnd + 2;
        while (cursor + 1 < data.length && !(data[cursor] == '\r' && data[cursor + 1] == '\n')) {
            int lineEnd = findLineEnd(data, cursor);
            if (lineEnd == -1) return null; // 半包

            int colonPos = findByteRange(data, cursor, lineEnd, (byte) ':');
            if (colonPos != -1) {
                toLowerCase(entity.bytes, cursor, colonPos);
                headerList.add(new int[]{cursor, colonPos, colonPos + 2, lineEnd});
            }
            cursor = lineEnd + 2;
        }

        if (cursor + 1 >= data.length) return null;
        entity.header = headerList.toArray(new int[0][0]);
        cursor += 2;
        int contentLength = getLengthFromHeader(entity);
        if (contentLength > 0) {
            if (data.length - cursor < contentLength) return null; // Body 还没收全
            entity.body = new int[]{cursor, cursor + contentLength};
            cursor += contentLength;
        } else {
            entity.body = new int[]{0, 0};
        }
        byteBuf.readerIndex(beginReaderIndex + cursor);
        return entity;
    }

    // 辅助方法：在指定范围内查找字节
    private static int findByteRange(byte[] data, int start, int end, byte target) {
        for (int i = start; i < end; i++) {
            if (data[i] == target) return i;
        }
        return -1;
    }
    private static int findByte(byte[] data, int start, byte target) {
        for (int i = start; i < data.length; i++) {
            if (data[i] == target) return i;
        }
        return -1;
    }

    private static int findLineEnd(byte[] data, int start) {
        for (int i = start; i < data.length - 1; i++) {
            if (data[i] == '\r' && data[i + 1] == '\n') return i;
        }
        return data.length;
    }

    private static int getLengthFromHeader(Entity entity) {
        for (int[] h : entity.header) {
            boolean next = true;
            for (int i = 0; i < static_bytes[4].length; i++) {
                if (static_bytes[4][i] != entity.bytes[h[0] + i]) {
                    next = false;
                    break;
                }
            }
            if (!next) {
                continue;
            }
            int size = 0;
            int len = 0;
            for (int i = h[2]; i < h[3]; i++) {
                len++;
                if (len > 10) {
                    return 0;
                }
                if (Config.maxPostSize > 0 && len > Config.maxPostSize) {
                    return 0;
                }
                if (entity.bytes[i] < 48 || entity.bytes[i] > 57) {
                    continue;
                }
                size = size * 10 + (entity.bytes[i] - 48);
            }
            return size;
        }
        return 0;
    }
    /**
     * 原地将指定范围的字节转换为小写
     */
    public static void toLowerCase(byte[] bytes, int start, int end) {
        for (int i = start; i < end; i++) {
            if (bytes[i] >= 'A' && bytes[i] <= 'Z') {
                bytes[i] = (byte) (bytes[i] + 32);
            }
        }
    }
    /**
     * 解析协议头为字符串列表
     */
    public static List<String[]> readHeader(Entity entity) {
        List<String[]> result = new ArrayList<>();
        for (int[] h : entity.header) {
            String key = new String(entity.bytes, h[0], h[1] - h[0]);
            String val = new String(entity.bytes, h[2], h[3] - h[2]);
            result.add(new String[]{key, val});
        }
        return result;
    }

    public static Map<String, String> readHeaderMap(Entity entity) {
        Map<String, String> result = new HashMap<>();
        for (int[] h : entity.header) {
            String key = new String(entity.bytes, h[0], h[1] - h[0]);
            String val = new String(entity.bytes, h[2], h[3] - h[2]);
            result.put(key, val);
        }
        return result;
    }

    public static void initEntity(Entity entity) {
        if (entity.is_keep !=-1 || entity.type != -1) {
            return;
        }
        for (int[] h : entity.header) {
            boolean next = true;
            if (static_bytes[5].length == h[1]-h[0] && static_bytes[6].length == h[3]-h[2]) {
                for (int i = 0; i < h[1]-h[0]; i++) {
                    if (entity.bytes[h[0] + i] != static_bytes[5][i]) {
                        next = false;
                        break;
                    }
                }
                if (next) {
                    for (int i = 0; i < static_bytes[6].length; i++) {
                        if (entity.bytes[h[2] + i] != static_bytes[6][i]) {
                            next = false;
                            break;
                        }
                    }
                    entity.is_keep=next?1:0;
                }
            }
            next = true;
            if (static_bytes[7].length == h[1]-h[0]) {
                for (int i = 0; i < h[1]-h[0]; i++) {
                    if (entity.bytes[h[0] + i] != static_bytes[7][i]) {
                        next = false;
                        break;
                    }
                }
                if (next) {
                    if (static_bytes[8].length <= h[3]-h[2]) {
                        for (int i = 0; i < static_bytes[8].length; i++) {
                            if (entity.bytes[h[2] + i] != static_bytes[8][i]) {
                                next = false;
                                break;
                            }
                        }
                        if (next) {
                            entity.type=1;
                        }
                    }
                    next = true;
                    if (static_bytes[9].length <= h[3]-h[2]) {
                        for (int i = 0; i < static_bytes[9].length; i++) {
                            if (entity.bytes[h[2] + i] != static_bytes[9][i]) {
                                next = false;
                                break;
                            }
                        }
                        if (next) {
                            entity.type=2;
                            entity.type_string=new String(entity.bytes, h[2], h[3]-h[2]);
                        }
                    }

                }
            }
            if (entity.is_keep!=-1 &&entity.type !=-1) {
                break;
            }
        }
        if (entity.type==-1) {
            entity.type=0;
        }
        if (entity.is_keep==-1) {
            entity.is_keep=0;
        }
    }


    public static String toLowerCaseString(byte[] bytes, int start, int end) {
        toLowerCase(bytes, start, end);
        return new String(bytes, start, end - start);
    }

    public static void parsingPostData(Map<String, List<Object>> parameters, Entity entity, String tempPath) throws IOException {
        OptimizedParameterParser.parseUrlEncoded(entity.getQuery(), parameters, true);
        int contentType=entity.getType();
        if (contentType == -1 || contentType==0) {
            OptimizedParameterParser.parseUrlEncoded(entity.getBody(), parameters, true);
        } else if (contentType==1) {
            Tools.jsonToMap(entity.getBody(), parameters);
        } else if (contentType==2) {
            parseMultipart(parameters, entity.bytes, entity.body[0], entity.body[1], entity.getTypeString(), tempPath);
        } else {
            parameters.computeIfAbsent("body", k -> new ArrayList<>(1)).add(entity.getBody());
        }
    }

    // 1. 解析常规表单 (a=1&b=2)
    private static void parseUrlEncoded(Map<String, List<Object>> map, byte[] data) {
        String str = new String(data, Config.encoding);
        String[] pairs = str.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                addParam(map, kv[0], kv[1]);
            }
        }
    }

    // 2. 解析文件上传 (Multipart)
    private static void parseMultipart(Map<String, List<Object>> map, byte[] data, int start, int end, String contentType, String tempPath) throws IOException {
        // 获取 boundary 标识
        String boundary = "--" + contentType.split("boundary=")[1];
        byte[] boundaryBytes = boundary.getBytes();

        // 寻找每一个 Part 的起始位置（简单实现：根据 boundary 分割）
        // 在 gzb one 这种追求性能的框架里，建议使用双指针滑动窗口定位 boundary
        int cursor = start;
        while (cursor < end) {
            int start0 = findBytes(data, boundaryBytes, cursor);
            if (start == -1) break;

            int nextBoundary = findBytes(data, boundaryBytes, start0 + boundaryBytes.length);
            if (nextBoundary == -1) break;

            int partStart = start0 + boundaryBytes.length + 2;
            int partEnd = nextBoundary - 2;
            processPart(map, data, partStart, partEnd, tempPath);
            cursor = nextBoundary;
        }
    }
    private static void processPart(Map<String, List<Object>> map, byte[] data, int start, int end, String tempPath) throws IOException {
        byte[] delimiter = "\r\n\r\n".getBytes();
        int boundaryIndex = findBytes(data, delimiter, start);
        if (boundaryIndex == -1) return;

        int bodyStart = boundaryIndex + 4;
        int bodyLen = end - bodyStart;
        String headStr = new String(data, start, bodyStart - start - 4, StandardCharsets.UTF_8);

        String fieldName = extractAttribute(headStr, "name=\"");
        String fileName = extractAttribute(headStr, "filename=\"");

        if (fieldName == null) return;

        if (fileName != null && !fileName.isEmpty()) {
            // 是文件上传：Key 是 fieldName，内容处理后存入
            handleFile(map, data, bodyStart, bodyLen,  fieldName,fileName, tempPath);
        } else {
            // 是普通表单参数
            String value = new String(data, bodyStart, bodyLen, StandardCharsets.UTF_8);
            addParam(map, fieldName, value);
        }
    }

    /**
     * 辅助方法：从 Header 字符串中提取属性值
     * 比如从 'name="user_file"; filename="test.png"' 中提取 user_file
     */
    private static String extractAttribute(String header, String attributeRef) {
        int start = header.indexOf(attributeRef);
        if (start == -1) return null;
        start += attributeRef.length();
        int end = header.indexOf("\"", start);
        if (end == -1) return null;
        return header.substring(start, end);
    }
    private static void handleFile(Map<String, List<Object>> map, byte[] data, int start, int len, String fieldName, String fileName, String tempPath) throws IOException {
        File tempFile = new File(tempPath,  System.currentTimeMillis() + "." + fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(data, start, len);
            addParam(map, fieldName, tempFile);
        }
    }

    private static void addParam(Map<String, List<Object>> map, String key, Object val) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(val);
    }

    // 辅助方法：字节搜索
    private static int findBytes(byte[] data, byte[] target, int start) {
        for (int i = start; i <= data.length - target.length; i++) {
            boolean match = true;
            for (int j = 0; j < target.length; j++) {
                if (data[i + j] != target[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return -1;
    }


}