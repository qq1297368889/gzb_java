/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import gzb.entity.PagingEntity;
import gzb.entity.TableInfo;
import gzb.exception.GzbException0;
import gzb.frame.PublicEntrance;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Constant;
import gzb.entity.HttpMapping;
import gzb.frame.netty.entity.Response;
import gzb.tools.http.HTTP;
import gzb.tools.img.GifCaptcha;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.*;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Tools {
    public static void main(String[] args) {
        System.out.println(escapeJsonString("123哈哈哈\"` \n \r\n \t 哈sjsj"));
    }

    private static Map<String, String> humpMap = new HashMap<>();
    public static String[] ss1 = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789".split("|");
    public static Lock lock = new ReentrantLock();
    public static int[] arr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    public static int[] arrCheck = {1, 0, 88, 9, 8, 7, 6, 5, 4, 3, 2};
    public static Random random = new Random(new Date().getTime() + 100);

    public final static DateTimeFormatter[] FORMATTER = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd号HH点mm分ss秒"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy-MM"),
            DateTimeFormatter.ofPattern("yyyy"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("HH"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    };

    public static java.sql.Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static java.sql.Timestamp getTimestamp(long time) {
        return new Timestamp(time);
    }

    public static java.sql.Timestamp getTimestamp(Result result, String key) {
        String str = result.getString(key);
        if (str == null) {
            return null;
        }
        return Timestamp.valueOf(str);
    }


    public static String toKey(String sql, Object[] params) {
        StringBuilder key = new StringBuilder(sql).append("_");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                key.append(params[i].toString());
                if (i < params.length - 1) {
                    key.append("_");
                }
            }
        }
        return key.toString();
    }

    public static int textLength(String str) {
        return (str == null) ? 0 : str.length();
    }

    public static int textLength(Integer i) {
        return (i == null) ? 0 : String.valueOf(i).length();
    }

    public static int textLength(Long l) {
        return (l == null) ? 0 : String.valueOf(l).length();
    }

    // 通用回退方法
    public static int textLength(Object obj) {
        return (obj == null) ? 0 : obj.toString().length();
    }

    public static Map<String, Object> jsonToMap(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }

        // Fastjson2 推荐使用 TypeReference 来进行泛型解析
        // TypeReference<Map<String, Object>> 明确告诉解析器，Map的值类型是 Object
        try {
            return JSON.parseObject(
                    json,
                    new TypeReference<Map<String, Object>>() {
                    });
        } catch (Exception e) {
            System.err.println("Fastjson2 解析失败: " + e.getMessage());
            return null;
        }
    }

    public static Map<Object, Object> createHashMap(String key, String val) {
        Map<Object, Object> map = new HashMap<>();
        if (key != null && val != null) {
            map.put(key, val);
        }
        return map;

    }

    public static Map<Object, Object> createHashMap() {
        return createHashMap(null, null);
    }

    public static List<Object> createArrayList(Object... objects) {
        if (objects != null && objects.length > 0) {
            List<Object> list = new ArrayList<>(objects.length);
            for (Object object : objects) {
                list.add(object);
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    // 预定义数字字符的ASCII值（0-9）
    private static final byte[] DIGITS = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    // 预定义boolean常量字节数组
    private static final byte[] TRUE_BYTES = "true".getBytes();
    private static final byte[] FALSE_BYTES = "false".getBytes();
    // 预定义null字节数组
    private static final byte[] NULL_BYTES = "null".getBytes();

    // ThreadLocal缓冲区复用（减少数组创建开销）
    private static final ThreadLocal<byte[]> BUFFER = ThreadLocal.withInitial(() -> new byte[32]);

    /**
     * boolean转换为byte[]
     */
    public static byte[] toBytes(boolean value) {
        return value ? TRUE_BYTES : FALSE_BYTES;
    }

    /**
     * byte转换为byte[]
     */
    public static byte[] toBytes(byte value) {
        // 直接复用int转换（byte范围小，无需单独实现）
        return toBytes(value);
    }

    /**
     * short转换为byte[]
     */
    public static byte[] toBytes(short value) {
        if (value == 0) {
            return new byte[]{48};
        }

        int len = 0;
        int temp = value;
        boolean negative = value < 0;
        if (negative) {
            len++;
            temp = -temp;
        }

        // 计算数字长度（short最大32767，最多5位）
        while (temp > 0) {
            temp /= 10;
            len++;
        }

        byte[] bytes = new byte[len];
        int pos = len - 1;
        temp = negative ? -value : value;

        // 填充数字字节
        while (temp > 0) {
            bytes[pos--] = DIGITS[temp % 10];
            temp /= 10;
        }

        // 处理负数符号
        if (negative) {
            bytes[0] = '-';
        }

        return bytes;
    }

    /**
     * int转换为byte[]
     */
    public static byte[] toBytes(int value) {
        if (value == 0) {
            return new byte[]{48};
        }

        int len = 0;
        int temp = value;
        boolean negative = value < 0;
        if (negative) {
            len++;
            temp = -temp;
        }

        // 计算数字长度
        while (temp > 0) {
            temp /= 10;
            len++;
        }

        byte[] bytes = new byte[len];
        int pos = len - 1;
        temp = negative ? -value : value;

        // 填充数字字节
        while (temp > 0) {
            bytes[pos--] = DIGITS[temp % 10];
            temp /= 10;
        }

        // 处理负数符号
        if (negative) {
            bytes[0] = '-';
        }

        return bytes;
    }

    /**
     * long转换为byte[]
     */
    public static byte[] toBytes(long value) {
        if (value == 0) {
            return new byte[]{48};
        }

        int len = 0;
        long temp = value;
        boolean negative = value < 0;
        if (negative) {
            len++;
            temp = -temp;
        }

        // 计算数字长度
        while (temp > 0) {
            temp /= 10;
            len++;
        }

        byte[] bytes = new byte[len];
        int pos = len - 1;
        temp = negative ? -value : value;

        // 填充数字字节
        while (temp > 0) {
            bytes[pos--] = DIGITS[(int) (temp % 10)];
            temp /= 10;
        }

        // 处理负数符号
        if (negative) {
            bytes[0] = '-';
        }

        return bytes;
    }

    /**
     * float转换为byte[]（保留6位小数精度）
     */
    public static byte[] toBytes(float value) {
        // 处理特殊值
        if (Float.isNaN(value)) return "NaN".getBytes();
        if (Float.isInfinite(value)) return value > 0 ? "Infinity".getBytes() : "-Infinity".getBytes();

        // 简单处理：四舍五入保留6位小数
        long scaled = Math.round(value * 1000000);
        byte[] intPart = toBytes(scaled / 1000000);
        int decimalPart = Math.abs((int) (scaled % 1000000));

        // 拼接整数和小数部分
        byte[] buffer = BUFFER.get();
        int pos = 0;

        // 复制整数部分
        System.arraycopy(intPart, 0, buffer, pos, intPart.length);
        pos += intPart.length;

        // 添加小数点
        buffer[pos++] = '.';

        // 处理小数部分（补零）
        for (int i = 5; i >= 0; i--) {
            int divisor = (int) Math.pow(10, i);
            buffer[pos++] = DIGITS[(decimalPart / divisor) % 10];
        }

        // 截取有效部分
        byte[] result = new byte[pos];
        System.arraycopy(buffer, 0, result, 0, pos);
        return result;
    }

    /**
     * double转换为byte[]（保留6位小数精度）
     */
    public static byte[] toBytes(double value) {
        // 处理特殊值
        if (Double.isNaN(value)) return "NaN".getBytes();
        if (Double.isInfinite(value)) return value > 0 ? "Infinity".getBytes() : "-Infinity".getBytes();

        // 简单处理：四舍五入保留6位小数
        long scaled = Math.round(value * 1000000);
        byte[] intPart = toBytes(scaled / 1000000);
        int decimalPart = Math.abs((int) (scaled % 1000000));

        // 拼接整数和小数部分
        byte[] buffer = BUFFER.get();
        int pos = 0;

        // 复制整数部分
        System.arraycopy(intPart, 0, buffer, pos, intPart.length);
        pos += intPart.length;

        // 添加小数点
        buffer[pos++] = '.';

        // 处理小数部分（补零）
        for (int i = 5; i >= 0; i--) {
            int divisor = (int) Math.pow(10, i);
            buffer[pos++] = DIGITS[(decimalPart / divisor) % 10];
        }

        // 截取有效部分
        byte[] result = new byte[pos];
        System.arraycopy(buffer, 0, result, 0, pos);
        return result;
    }

    /**
     * char转换为byte[]（UTF-8编码）
     */
    public static byte[] toBytes(char value) {
        if (value < 0x80) {
            // 单字节UTF-8
            return new byte[]{(byte) value};
        } else if (value < 0x800) {
            // 双字节UTF-8
            byte[] bytes = new byte[2];
            bytes[0] = (byte) (0xC0 | (value >> 6));
            bytes[1] = (byte) (0x80 | (value & 0x3F));
            return bytes;
        } else {
            // 三字节UTF-8（基本多文种平面）
            byte[] bytes = new byte[3];
            bytes[0] = (byte) (0xE0 | (value >> 12));
            bytes[1] = (byte) (0x80 | ((value >> 6) & 0x3F));
            bytes[2] = (byte) (0x80 | (value & 0x3F));
            return bytes;
        }
    }

    /**
     * String转换为byte[]（UTF-8编码，提供兼容性）
     */
    public static byte[] toBytes(String value) {
        if (value == null) return NULL_BYTES;
        return value.getBytes();
    }


    public static byte[] toJsonBytes(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(256);
        toJsonBytes(obj, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static void toJsonBytes(Object obj, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        if (obj == null) {
            byteArrayOutputStream.write(NULL_BYTES);
            return;
        }
        if (obj instanceof Map) {
            mapToJsonByte((Map<?, ?>) obj, byteArrayOutputStream);
            return;
        }
        if (obj instanceof Iterable) {
            iterableToJsonByte((Iterable<?>) obj, byteArrayOutputStream);
            return;
        }
        if (obj.getClass().isArray()) {
            arrayToJsonByte(obj, byteArrayOutputStream);
            return;
        }
        if (obj instanceof GzbMap) {
            mapToJsonByte(((GzbMap) obj).getMap(), byteArrayOutputStream);
            return;
        }
        if (obj instanceof Class<?> || obj instanceof File || obj instanceof CharSequence || obj instanceof Character || obj instanceof Number || obj instanceof Boolean) {
            byteArrayOutputStream.write(34);
            escapeJsonStringByte(obj.toString(), byteArrayOutputStream);
            byteArrayOutputStream.write(34);
            return;
        }
        if (obj instanceof JsonSerializable) {
            byteArrayOutputStream.write(obj.toString().getBytes(Config.encoding));
            return;
        }
        if (obj instanceof Exception) {
            byteArrayOutputStream.write(34);
            escapeJsonStringByte(getExceptionInfo((Exception) obj), byteArrayOutputStream);
            byteArrayOutputStream.write(34);
            return;
        }
        if (obj instanceof Timestamp) {
            byteArrayOutputStream.write(34);
            escapeJsonStringByte(new DateTime((Timestamp) obj).toString(), byteArrayOutputStream);
            byteArrayOutputStream.write(34);
            return;
        }
        byteArrayOutputStream.write(Objects.requireNonNull(ClassTools.toJsonObjectByte(obj)));
    }


    // 预定义JSON常用字符的字节常量（避免频繁创建byte[]）
    private static final byte[] JSON_NULL = "null".getBytes();
    private static final byte[] JSON_LBRACE = "{".getBytes();
    private static final byte[] JSON_RBRACE = "}".getBytes();
    private static final byte[] JSON_LBRACKET = "[".getBytes();
    private static final byte[] JSON_RBRACKET = "]".getBytes();
    private static final byte[] JSON_COMMA = ",".getBytes();
    private static final byte[] JSON_COLON = ":".getBytes();
    private static final byte[] JSON_QUOTE = "\"".getBytes();
    // 转义字符字节常量
    private static final byte[] ESC_BACKSLASH = "\\\\".getBytes();
    private static final byte[] ESC_QUOTE = "\\\"".getBytes();
    private static final byte[] ESC_B = "\\b".getBytes();
    private static final byte[] ESC_F = "\\f".getBytes();
    private static final byte[] ESC_N = "\\n".getBytes();
    private static final byte[] ESC_R = "\\r".getBytes();
    private static final byte[] ESC_T = "\\t".getBytes();


    /**
     * Map序列化（字节版）：直接写入ByteArrayOutputStream，不返回内容
     * 完全保留原mapToJson逻辑：空map处理、键值非空判断、逗号分隔规则
     */
    public static void mapToJsonByte(Map<?, ?> map, ByteArrayOutputStream byteArrayOutputStream) {
        if (map == null) {
            byteArrayOutputStream.write(JSON_NULL, 0, JSON_NULL.length);
            return;
        }

        // 写入JSON对象开始符 '{'
        byteArrayOutputStream.write(JSON_LBRACE, 0, JSON_LBRACE.length);
        boolean first = true;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            // 保留原逻辑：仅处理键和值都非空的条目
            if (entry.getKey() != null && entry.getValue() != null) {
                if (!first) {
                    // 非首次条目，先写入逗号
                    byteArrayOutputStream.write(JSON_COMMA, 0, JSON_COMMA.length);
                }
                first = false;

                // 写入键：引号 + 转义后的键字符串 + 引号 + 冒号
                byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
                // 转义键字符串并写入字节流
                escapeJsonStringByte(entry.getKey().toString(), byteArrayOutputStream);
                byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
                byteArrayOutputStream.write(JSON_COLON, 0, JSON_COLON.length);

                // 写入值（调用字节版toJson）
                toJsonByte(entry.getValue(), byteArrayOutputStream);
            }
        }

        // 写入JSON对象结束符 '}'
        byteArrayOutputStream.write(JSON_RBRACE, 0, JSON_RBRACE.length);
    }


    /**
     * Iterable序列化（字节版）：直接写入ByteArrayOutputStream，不返回内容
     * 完全保留原iterableToJson逻辑：空Iterable处理、逗号分隔规则
     */
    public static void iterableToJsonByte(Iterable<?> iterable, ByteArrayOutputStream byteArrayOutputStream) {
        if (iterable == null) {
            byteArrayOutputStream.write(JSON_NULL, 0, JSON_NULL.length);
            return;
        }

        // 写入JSON数组开始符 '['
        byteArrayOutputStream.write(JSON_LBRACKET, 0, JSON_LBRACKET.length);
        boolean first = true;
        Iterator<?> iterator = iterable.iterator();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (!first) {
                // 非首次元素，先写入逗号
                byteArrayOutputStream.write(JSON_COMMA, 0, JSON_COMMA.length);
            }
            first = false;

            // 写入元素（调用字节版toJson）
            toJsonByte(obj, byteArrayOutputStream);
        }

        // 写入JSON数组结束符 ']'
        byteArrayOutputStream.write(JSON_RBRACKET, 0, JSON_RBRACKET.length);
    }


    /**
     * 数组序列化（字节版）：直接写入ByteArrayOutputStream，不返回内容
     * 完全保留原arrayToJson逻辑：空数组处理、索引判断逗号规则
     */
    public static void arrayToJsonByte(Object array, ByteArrayOutputStream byteArrayOutputStream) {
        if (array == null) {
            byteArrayOutputStream.write(JSON_NULL, 0, JSON_NULL.length);
            return;
        }

        // 保留原逻辑：通过Array.getLength获取数组长度
        int length = java.lang.reflect.Array.getLength(array);
        // 写入JSON数组开始符 '['
        byteArrayOutputStream.write(JSON_LBRACKET, 0, JSON_LBRACKET.length);

        for (int i = 0; i < length; i++) {
            if (i > 0) {
                // 索引>0时，先写入逗号（保留原逻辑）
                byteArrayOutputStream.write(JSON_COMMA, 0, JSON_COMMA.length);
            }

            // 写入数组元素（调用字节版toJson）
            Object element = java.lang.reflect.Array.get(array, i);
            toJsonByte(element, byteArrayOutputStream);
        }

        // 写入JSON数组结束符 ']'
        byteArrayOutputStream.write(JSON_RBRACKET, 0, JSON_RBRACKET.length);
    }


    /**
     * JSON字符串转义（字节版）：直接写入ByteArrayOutputStream，不返回内容
     * 完全保留原escapeJsonString逻辑：特殊字符转义、ASCII<32控制字符转义规则
     */
    public static void escapeJsonStringByte(String str, ByteArrayOutputStream byteArrayOutputStream) {
        if (str == null) {
            return; // 原逻辑返回空字符串，此处对应不写入任何内容
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            switch (c) {
                case '"':
                    // 双引号转义：" → \"
                    byteArrayOutputStream.write(ESC_QUOTE, 0, ESC_QUOTE.length);
                    break;
                case '\\':
                    // 反斜杠转义：\ → \\
                    byteArrayOutputStream.write(ESC_BACKSLASH, 0, ESC_BACKSLASH.length);
                    break;
                case '\b':
                    // 退格转义：\b
                    byteArrayOutputStream.write(ESC_B, 0, ESC_B.length);
                    break;
                case '\f':
                    // 换页转义：\f
                    byteArrayOutputStream.write(ESC_F, 0, ESC_F.length);
                    break;
                case '\n':
                    // 换行转义：\n
                    byteArrayOutputStream.write(ESC_N, 0, ESC_N.length);
                    break;
                case '\r':
                    // 回车转义：\r
                    byteArrayOutputStream.write(ESC_R, 0, ESC_R.length);
                    break;
                case '\t':
                    // 制表符转义：\t
                    byteArrayOutputStream.write(ESC_T, 0, ESC_T.length);
                    break;
                default:
                    if (c < ' ') {
                        // 保留原逻辑：ASCII<32的控制字符转义为 \ u XXXX
                        String unicodeEsc = String.format("\\u%04x", (int) c);
                        byte[] unicodeBytes = unicodeEsc.getBytes();
                        byteArrayOutputStream.write(unicodeBytes, 0, unicodeBytes.length);
                    } else {
                        // 普通字符直接写入（转成UTF-8字节）
                        byteArrayOutputStream.write((byte) c);
                    }
                    break;
            }
        }
    }


    /**
     * 核心序列化分发（字节版）：根据对象类型调用对应字节版方法
     * （需根据原toJson逻辑补充完整类型判断，此处保留核心逻辑框架）
     */
    private static void toJsonByte(Object obj, ByteArrayOutputStream byteArrayOutputStream) {
        if (obj == null) {
            byteArrayOutputStream.write(JSON_NULL, 0, JSON_NULL.length);
            return;
        }

        Class<?> objClass = obj.getClass();
        // 处理基本类型包装类（boolean/Byte/Short/Integer/Long/Float/Double/Character）
        if (objClass == Boolean.class) {
            boolean value = (Boolean) obj;
            byte[] boolBytes = value ? "true".getBytes() : "false".getBytes();
            byteArrayOutputStream.write(boolBytes, 0, boolBytes.length);
        } else if (objClass == Byte.class) {
            byte[] byteBytes = toBytes((Byte) obj); // 复用之前的基本类型转byte[]工具
            byteArrayOutputStream.write(byteBytes, 0, byteBytes.length);
        } else if (objClass == Short.class) {
            byte[] shortBytes = toBytes((Short) obj);
            byteArrayOutputStream.write(shortBytes, 0, shortBytes.length);
        } else if (objClass == Integer.class) {
            byte[] intBytes = toBytes((Integer) obj);
            byteArrayOutputStream.write(intBytes, 0, intBytes.length);
        } else if (objClass == Long.class) {
            byte[] longBytes = toBytes((Long) obj);
            byteArrayOutputStream.write(longBytes, 0, longBytes.length);
        } else if (objClass == Float.class) {
            byte[] floatBytes = toBytes((Float) obj);
            byteArrayOutputStream.write(floatBytes, 0, floatBytes.length);
        } else if (objClass == Double.class) {
            byte[] doubleBytes = toBytes((Double) obj);
            byteArrayOutputStream.write(doubleBytes, 0, doubleBytes.length);
        } else if (objClass == Character.class) {
            byte[] charBytes = toBytes((Character) obj);
            byteArrayOutputStream.write(charBytes, 0, charBytes.length);
        }
        // 处理字符串
        else if (objClass == String.class) {
            byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
            escapeJsonStringByte((String) obj, byteArrayOutputStream);
            byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
        }
        // 处理Map
        else if (Map.class.isAssignableFrom(objClass)) {
            mapToJsonByte((Map<?, ?>) obj, byteArrayOutputStream);
        }
        // 处理Iterable（List/Set等）
        else if (Iterable.class.isAssignableFrom(objClass)) {
            iterableToJsonByte((Iterable<?>) obj, byteArrayOutputStream);
        }
        // 处理数组
        else if (objClass.isArray()) {
            arrayToJsonByte(obj, byteArrayOutputStream);
        }
        // 处理其他自定义对象（需根据原toJson逻辑补充，此处暂留默认实现）
        else {
            // 若原逻辑是调用toString，此处保持一致（转成JSON字符串格式）
            byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
            escapeJsonStringByte(obj.toString(), byteArrayOutputStream);
            byteArrayOutputStream.write(JSON_QUOTE, 0, JSON_QUOTE.length);
        }
    }


    public static String toJson0(Object obj) {
        return JSON.toJSONString(obj, JSONWriter.Feature.WriteNonStringValueAsString);
    }
    public static void toJson(Object obj,StringBuilder stringBuilder) {
        stringBuilder.append(toJson(obj));
    }

    //记不住这个api 封装一下
    public static byte[] readByteBuf(io.netty.buffer.ByteBuf buf) {
        // readableBytes() 获取实际有数据的长度
        byte[] bytes = new byte[buf.readableBytes()];
        // getBytes 不会移动读索引(readerIndex)，适合调试打印
        buf.getBytes(buf.readerIndex(), bytes);
        return bytes;
    }
    //记不住这个api 封装一下
    public static ByteBuf loadByteBuf(byte[] data) {
        return Unpooled.wrappedBuffer(data);
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> ac = obj.getClass();
        if (ac.isPrimitive()
                || ac == String.class
                || ac == Long.class
                || ac == Integer.class
                || ac == Double.class
                || ac == Float.class
                || ac == Short.class
                || ac == Boolean.class
                || ac == Byte.class
                || ac == Character.class
        ) {
            return "\""+escapeJsonString(obj.toString())+"\"";
        }
        if (obj instanceof JsonSerializable) {
            return obj.toString();
        }
        if (obj.getClass().isArray()) {
            return arrayToJson(obj);
        }
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>)obj);
        }
        if (obj instanceof Iterable) {
            return iterableToJson((Iterable<?>)obj);
        }
        if (obj instanceof GzbMap) {//已知类处理
            return mapToJson(((GzbMap) obj).getMap());
        }
        if (obj instanceof Class<?> || obj instanceof File || obj instanceof CharSequence) {
            return "\""+escapeJsonString(obj.toString())+"\"";
        }
        if (obj instanceof Exception) {
            return "\""+escapeJsonString(getExceptionInfo((Exception) obj))+"\"";
        }
        if (obj instanceof Timestamp) {
            return "\""+escapeJsonString(new DateTime((Timestamp) obj).toString())+"\"";
        }
        if (obj instanceof LocalDateTime) {
            return "\""+escapeJsonString(new DateTime((LocalDateTime) obj).toString())+"\"";
        }
        if (obj instanceof Date) {
            return "\""+escapeJsonString(new DateTime((Date) obj).toString())+"\"";
        }
        String data=ClassTools.toJsonObject(obj);
        if (data==null) {
            return obj.toString();
        }
        return data;
    }


    // Helper method to serialize a Map
    public static String mapToJson(Map<?, ?> map) {
        if (map == null) {
            return "null";
        }
        StringBuilder sb = PublicEntrance.SB_CACHE.get();
        sb.setLength(0); // 重置StringBuilder长度，复用缓存
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append("\"");
                sb.append(entry.getKey());
                sb.append("\":");
                sb.append(toJson(entry.getValue()));
            }

        }
        sb.append("}");
        return sb.toString();
    }
    // Helper method to serialize an Iterable (e.g., List, Set)
    public static String iterableToJson(Iterable<?> iterable) {
        if (iterable == null) {
            return "null";
        }
        StringBuilder sb = PublicEntrance.SB_CACHE.get();
        sb.setLength(0); // 重置StringBuilder长度，复用缓存
        sb.append("[");
        if (iterable != null) {
            boolean first = true;
            for (Object obj : iterable) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(toJson(obj));
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Helper method to serialize an Array
    public static String arrayToJson(Object array) {
        if (array == null) {
            return "null";
        }
        StringBuilder sb = PublicEntrance.SB_CACHE.get();
        sb.setLength(0); // 重置StringBuilder长度，复用缓存
        int length = Array.getLength(array);
        sb.append("[");
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(toJson(Array.get(array, i)));
        }
        sb.append("]");
        return sb.toString();
    }

    public static String escapeJsonString(String str) {
        if (str == null) {
            return "null";
        }
        //StringBuilder sb = new StringBuilder(str.length() + 10);
        StringBuilder sb = PublicEntrance.SB_CACHE.get();
        sb.setLength(0); // 重置StringBuilder长度，复用缓存
        final int len = str.length();
        int last = 0; // 上一次追加的起点索引

        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);

            // 检查字符是否需要转义
            switch (c) {
                case '"':
                    // 追加上一段普通字符串
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\\"");
                    last = i + 1;
                    break;
                case '\\':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\\\");
                    last = i + 1;
                    break;
                case '\b':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\b");
                    last = i + 1;
                    break;
                case '\f':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\f");
                    last = i + 1;
                    break;
                case '\n':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\n");
                    last = i + 1;
                    break;
                case '\r':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\r");
                    last = i + 1;
                    break;
                case '\t':
                    if (last < i) {
                        sb.append(str, last, i);
                    }
                    sb.append("\\t");
                    last = i + 1;
                    break;
                default:
                    // 检查控制字符 (< ASCII 32)
                    if (c < ' ') {
                        if (last < i) {
                            sb.append(str, last, i);
                        }
                        sb.append("\\u00");
                        // 快速转换 4-bit 为 16 进制字符（需实现高效的 toHexChar 辅助方法）
                        sb.append(HEX_CHARS[(c >> 4) & 0xF]); // 高 4 位
                        sb.append(HEX_CHARS[c & 0xF]);        // 低 4 位
                        last = i + 1;
                    }
                    break;
            }
        }

        // 循环结束后，追加最后一段普通字符串
        if (last < len) {
            sb.append(str, last, len);
        }
        return sb.toString();
    }

    // 辅助常量：用于将 int 0-15 快速转换为 16 进制字符
    private static final char[] HEX_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    // Helper method to escape special JSON characters
    public static void escapeJsonString0(String str, StringBuilder sb) {
        if (str == null) {
            return;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    // Control characters below ASCII 32 need to be escaped
                    if (c < ' ') {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                    break;
            }
        }
    }

    /**
     * 从一个大byte[]中寻找一个小byte[]
     * 大bytes
     * 小arr
     */
    public static boolean bytesContains(byte[] bytes, byte[] arr) {
        if (bytes == null || arr == null || arr.length == 0 || bytes.length < arr.length) {
            return false;
        }
        for (int i = 0; i <= bytes.length - arr.length; i++) {
            boolean found = true;
            for (int j = 0; j < arr.length; j++) {
                if (bytes[i + j] != arr[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return true;
            }
        }
        return false;
    }

    /**
     * Unicode转字符串
     * 例如：输入 "\\u4f60\\u597d" 返回 "你好"
     */
    public static String unicodeToString(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < unicodeStr.length()) {
            char ch = unicodeStr.charAt(i);
            if (ch == '\\' && i + 1 < unicodeStr.length() && unicodeStr.charAt(i + 1) == 'u') {
                StringBuilder hex = new StringBuilder();
                for (int j = 0; j < 4 && i + 2 + j < unicodeStr.length(); j++) {
                    hex.append(unicodeStr.charAt(i + 2 + j));
                }

                try {
                    int codePoint = Integer.parseInt(hex.toString(), 16);
                    result.append((char) codePoint);
                    i += 6;
                    continue;
                } catch (NumberFormatException e) {
                    // 不是有效的Unicode编码，按普通字符处理
                }
            }
            result.append(ch);
            i++;
        }
        return result.toString();
    }

    /**
     * 字符串转Unicode
     * 例如：输入 "你好" 返回 "\\u4f60\\u597d"
     */
    public static String stringToUnicode(String str) {
        if (str == null) {
            return null;
        }

        StringBuilder unicode = new StringBuilder();
        for (char ch : str.toCharArray()) {
            // 只对ASCII码之外的字符进行Unicode编码
            if (ch < 128) {
                unicode.append(ch);
            } else {
                unicode.append("\\u").append(String.format("%04x", (int) ch));
            }
        }
        return unicode.toString();
    }

    public static byte[] httpGetByte(String url) {
        HTTP http = new HTTP();
        for (int i = 0; i < 10; i++) {
            http.httpGet(url);
            if (http.toByte() != null && http.toByte().length > 0) {
                return http.toByte();
            }
        }
        return null;
    }

    public static String httpGetString(String url) {
        byte[] bytes = httpGetByte(url);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(bytes);
    }

    public static int m3u8Download(String m3u8Url, String filePath,
                                   int intervalMin, int intervalMax) throws Exception {
        return m3u8Download(m3u8Url, filePath, intervalMax, intervalMax, new ConcurrentHashMap<>(), "def");
    }

    public static int m3u8Download(String m3u8Url, String filePath,
                                   int intervalMin, int intervalMax, Map<String, String> printLogMap, String key) throws Exception {
        String str1 = httpGetString(m3u8Url);
        if (str1 == null) {
            System.out.println("请求下载 m3u8 失败");
            return -1;
        }
        String pwdUrl = textMid(str1, "#EXT-X-KEY:METHOD=AES-128,URI=\"", "\"", 1);
        byte[] pwd = httpGetByte(pwdUrl);
        if (pwd == null || pwd.length < 16) {
            System.out.println("密码读取失败");
            return -1;
        }
        List<String> list = textMid(str1, ",\n", "\n#");
        for (int i = 0; i < list.size(); i++) {
            File file = new File(filePath.replaceAll("\\\\", "/") + "/" + i + ".ts");
            if (file.exists()) {
                continue;
            }
            String subUrl = list.get(i);
            printLogMap.put(key, (i + 1) + "/" + list.size());
            byte[] bytes = httpGetByte(subUrl);
            if (bytes == null || bytes.length == 0) {
                printLogMap.put(key, "失败");
                System.out.println("请求下载  失败 " + subUrl);
                return i;
            }
            byte[] bytes2 = AES_CBC_128.aesDe(bytes, pwd, null);
            if (bytes2 != null) {
                //System.out.println(desc + " " + (i + 1) + "/" + list.size() + " data " + bytes2.length);
                FileTools.save(file, bytes2);
                if (intervalMin > 0 && intervalMax > 0) {
                    sleep(getRandomInt(intervalMax, intervalMin));
                }
            } else {
                printLogMap.put(key, "失败");
                return i;
            }
        }
        printLogMap.put(key, "完毕");
        return list.size();
    }

    public static File m3u8ToMp4(String path, String mp4Path, int start) throws Exception {
        while (true) {
            File file = new File(path + "/" + start + ".ts");
            start++;
            if (!file.exists()) {
                break;
            }
            FileTools.append(new File(mp4Path), Tools.fileReadByte(file));
        }
        return new File(mp4Path);
    }

    // 工具方法：将字节数组转换为十六进制字符串
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static void sleep(long millis) {
        long nanosToPark = millis * 1_000_000L; // 转换为纳秒
        LockSupport.parkNanos(nanosToPark);
    }

    public static void sleep(long min, long max) {
        sleep(getRandomLong(max, min));
    }

    public static int updateMapping(DataBase dataBase, List<TableInfo> listTableInfo, Long gid) throws Exception {
/*        int num01 = loadApiInfo(dataBase);
        System.out.println("权限数量:" + num01);*/
        if (listTableInfo == null) {
            listTableInfo = dataBase.getTableInfo();
        }
        String dbName = listTableInfo.get(0).dbName;
        List<GzbMap> list1 = dataBase.selectGzbMap("select * from sys_permission where sys_permission_name = '" + dbName + "' and sys_permission_type=1 and sys_permission_sup = 0");
        if (list1.isEmpty()) {
            String sql = "INSERT INTO sys_permission" +
                    "(sys_permission_id, sys_permission_name, sys_permission_data, sys_permission_type, sys_permission_desc, sys_permission_sup, sys_permission_sort) " +
                    "VALUES (" + OnlyId.getDistributed() + ", '" + dbName + "', NULL, '1', NULL, 0, 0)";
            dataBase.runSql(sql);
            list1 = dataBase.selectGzbMap("select * from sys_permission where sys_permission_name = '" + dbName + "' and sys_permission_type=1 and sys_permission_sup = 0");

        }
        long sup = list1.get(0).getLong("sysPermissionId");
        for (TableInfo tableInfo : listTableInfo) {
            List<GzbMap> list2 = dataBase.selectGzbMap("select * from sys_permission where " +
                    "sys_permission_name = '" + tableInfo.name + "' and " +
                    "sys_permission_type = 1 and sys_permission_sup > 0");
            if (list2.isEmpty()) {
                String sql = "INSERT INTO sys_permission" +
                        "(sys_permission_id, sys_permission_name, sys_permission_data, sys_permission_type, sys_permission_desc, sys_permission_sup, sys_permission_sort) " +
                        "VALUES (" + OnlyId.getDistributed() + ", '" + tableInfo.name + "', " +
                        "'list.html?config=" + tableInfo.nameHumpLowerCase + "&edit=" + tableInfo.nameHumpLowerCase + "', " +
                        "'1', NULL, " + sup + ", 0)";
                dataBase.runSql(sql);
            }
        }

        List<GzbMap> list_sys_users = dataBase.selectGzbMap("select * from sys_users where sys_users_acc = 'admin'");
        if (list_sys_users.size() != 1) {
            return -1;
        }
        List<GzbMap> list_sys_group = null;
        if (gid == null) {
            list_sys_group = dataBase.selectGzbMap("select * from sys_group where sys_group_name = '管理员权限组'");
        } else {
            list_sys_group = dataBase.selectGzbMap("select * from sys_group where sys_group_id = " + gid);
        }
        Long sysGroupId = null;
        if (list_sys_group.size() == 1) {
            sysGroupId = list_sys_group.get(0).getLong("sysGroupId");
        } else {
            return -5;
        }
        if (list_sys_group.size() != 1) {
            return -2;
        }
        String sql0 = "INSERT INTO sys_group_table(" +
                "sys_group_table_id, sys_group_table_key, sys_group_table_gid, " +
                "sys_group_table_but_save_open, sys_group_table_but_delete_open, sys_group_table_table_update_open, " +
                "sys_group_table_table_delete_open, sys_group_table_but_query_open,sys_group_table_table_but_width) " +
                "SELECT ?,?,?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_group_table WHERE sys_group_table_key = ? and sys_group_table_gid = ?)";
        String sql1 = "INSERT INTO sys_mapping(" +
                "sys_mapping_id, sys_mapping_key, sys_mapping_title, " +
                "sys_mapping_val, sys_mapping_table_width, sys_mapping_select, " +
                "sys_mapping_image, sys_mapping_file, sys_mapping_date," +
                "sys_mapping_script, sys_mapping_sort) " +
                "SELECT ?,?,?,?,?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_mapping WHERE sys_mapping_val = ?)";
        String sql2 = "INSERT INTO sys_group_column(" +
                "sys_group_column_id, sys_group_column_table, sys_group_column_edit, " +
                "sys_group_column_update, sys_group_column_save, sys_group_column_save_def, " +
                "sys_group_column_update_def, sys_group_column_query_symbol, sys_group_column_query_montage, " +
                "sys_group_column_query_def, sys_group_column_key, sys_group_column_gid" +
                ",sys_group_column_name,sys_group_column_query) " +
                "SELECT ?,?,?,?,?,?,?,?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_group_column WHERE sys_group_column_name = ? and sys_group_column_gid = ?)";

        Object[] objects;
        for (TableInfo tableInfo : listTableInfo) {
            //权限 表信息
            objects = new Object[]{
                    OnlyId.getDistributed(), tableInfo.nameHumpLowerCase, sysGroupId
                    , 1, 1, 1
                    , 1, 1, 120
                    , tableInfo.nameHumpLowerCase, sysGroupId
            };
            dataBase.runSql(sql0, objects);
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                String name1 = tableInfo.columnNames.get(i);


                //基础映射信息
                objects = new Object[]{
                        OnlyId.getDistributed(), tableInfo.nameHumpLowerCase, tableInfo.columnNamesHumpLowerCase.get(i)
                        , tableInfo.columnNames.get(i), 100, null
                        , null, null, null
                        , null, 0
                        , tableInfo.columnNames.get(i)
                };
                if (name1.contains("state") || name1.contains("status")) {
                    objects[5] = "key:def_state";
                }
                if (!dataBase.selectGzbMap("select sys_option_id  from sys_option where sys_option_key = ?", new Object[]{name1}).isEmpty()) {
                    objects[5] = "key:" + name1;
                }
                dataBase.runSql(sql1, objects);

                //权限 列信息
                objects = new Object[]{
                        OnlyId.getDistributed(), 1, 1
                        , 1, 1, null
                        , null, 1, 1
                        , null, tableInfo.nameHumpLowerCase, sysGroupId
                        , tableInfo.columnNames.get(i), 0
                        , tableInfo.columnNames.get(i), sysGroupId
                };
                dataBase.runSql(sql2, objects);
            }

        }
        //只生成 对应权限范围的 映射信息

  /*      List<GzbMap> list_sys_permission = dataBase.selectGzbMap("select * from sys_permission");
        for (GzbMap gzbMap : list_sys_permission) {
            if (sysGroupId != null) {
                dataBase.runSql("INSERT INTO sys_group_permission(" +
                                "sys_group_permission_id, sys_group_permission_pid, sys_group_permission_gid, " +
                                "sys_group_permission_time) " +
                                "SELECT ?,?,?,? " +
                                "WHERE NOT EXISTS (SELECT 1 FROM sys_group_permission WHERE " +
                                "sys_group_permission_pid = ? and sys_group_permission_gid= ?)"
                        , new Object[]{
                                OnlyId.getDistributed(), gzbMap.get("sysPermissionId"), sysGroupId
                                , new DateTime().toString()
                                , gzbMap.get("sysPermissionId"), sysGroupId
                        });
            }
        }
*/
        return 1;
    }

    public static int loadApiInfo(DataBase dataBase, Map<String, Object> mapping) throws Exception {
        String sql = "INSERT INTO sys_permission(" +
                "sys_permission_id, sys_permission_name, sys_permission_data, " +
                "sys_permission_type, sys_permission_desc, sys_permission_sup, " +
                "sys_permission_sort) " +
                "SELECT ?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE sys_permission_name = ?)";
        Object[] objects = null;
        int x = 0;
        if (mapping == null) {
            return 0;
        }
        for (Map.Entry<String, Object> obj : mapping.entrySet()) {
            if (obj.getValue().getClass().getComponentType() == null) {
                objects = new Object[]{OnlyId.getDistributed(), obj.getKey(), null, 2, null, 0, 0, obj.getKey()};
            } else {
                HttpMapping[] objects1 = (HttpMapping[]) obj.getValue();
                if (objects1[0] != null) {
                    objects = new Object[]{OnlyId.getDistributed(), obj.getKey() + "-" + Constant.requestMethod[0], null, 2, null, 0, 0, obj.getKey() + "-" + Constant.requestMethod[0]};
                } else if (objects1[1] != null) {
                    objects = new Object[]{OnlyId.getDistributed(), obj.getKey() + "-" + Constant.requestMethod[1], null, 2, null, 0, 0, obj.getKey() + "-" + Constant.requestMethod[1]};
                } else if (objects1[2] != null) {
                    objects = new Object[]{OnlyId.getDistributed(), obj.getKey() + "-" + Constant.requestMethod[2], null, 2, null, 0, 0, obj.getKey() + "-" + Constant.requestMethod[2]};
                } else if (objects1[3] != null) {
                    objects = new Object[]{OnlyId.getDistributed(), obj.getKey() + "-" + Constant.requestMethod[3], null, 2, null, 0, 0, obj.getKey() + "-" + Constant.requestMethod[3]};
                }
            }
            dataBase.runSqlAsync(sql, objects);
            x++;
        }
        return x;
    }

    public static void autoReadTableInfo(DataBase dataBase) throws Exception {
        int num = 0;
        String sql1 = "select * from sys_mapping where sys_mapping_key = ? and sys_mapping_type = ?";
        String sql2 = "INSERT INTO sys_mapping (" +
                "sys_mapping_key" +
                ",sys_mapping_title" +
                ",sys_mapping_type" +
                ",sys_mapping_show_table_delete" +
                ",sys_mapping_show_table_update" +
                ",sys_mapping_show_query" +
                ",sys_mapping_show_delete" +
                ",sys_mapping_show_save" +
                ",sys_mapping_show_table_width" +
                ",sys_mapping_show_sort" +
                ") " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);";
        String sql3 = "INSERT INTO sys_mapping (" +
                "sys_mapping_key" +
                ",sys_mapping_title" +
                ",sys_mapping_type" +
                ",sys_mapping_show_table_width" +
                ",sys_mapping_val" +
                ",sys_mapping_table_edit_show" +
                ",sys_mapping_table_show" +
                ",sys_mapping_query_show" +
                ",sys_mapping_table_update_show" +
                ",sys_mapping_save_show" +
                ",sys_mapping_query_montage" +
                ",sys_mapping_query_symbol" +
                ",sys_mapping_show_sort" +
                ",sys_mapping_select" +
                ") " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);";
        String sql4 = "select * from sys_mapping where sys_mapping_key = ? and sys_mapping_val = ? and sys_mapping_type = ?";
        Object[] objects = null;
        List<TableInfo> list = dataBase.getTableInfo();
        for (TableInfo tableInfo : list) {
            objects = new Object[]{tableInfo.nameHumpLowerCase, 2L};
            if (dataBase.selectGzbMap(sql1, objects).isEmpty()) {
                num++;
                objects = new Object[]{tableInfo.nameHumpLowerCase, "页面信息", 2, 1, 1, 1, 1, 1, 130L, num + 10L};
                dataBase.runSql(sql2, objects);
            }
            for (int i = 0; i < tableInfo.columnNamesHumpLowerCase.size(); i++) {
                objects = new Object[]{tableInfo.nameHumpLowerCase, tableInfo.columnNames.get(i), 1L};
                if (dataBase.selectGzbMap(sql4, objects).isEmpty()) {
                    num++;
                    String name1 = tableInfo.columnNames.get(i);
                    objects = new Object[]{
                            tableInfo.nameHumpLowerCase, tableInfo.columnNamesHumpLowerCase.get(i), 1,
                            120, tableInfo.columnNames.get(i), 1,
                            1, 0, 1,
                            1, 1, 1,
                            100, null};
                    if (name1.contains("state") || name1.contains("status")) {
                        objects[13] = "key:def_state";
                    }
                    if (!dataBase.selectGzbMap("select sys_option_id  from sys_option where sys_option_key = ?", new Object[]{name1}).isEmpty()) {
                        objects[13] = "key:" + name1;
                    }
                    dataBase.runSql(sql3, objects);
                }

            }

        }
    }

    //首字母转大写
    public static String lowStr_d(String str) {
        return str.substring(0, 1).toUpperCase() + (str.substring(1));
    }

    //首字母转小写
    public static String lowStr_x(String str) {
        return str.substring(0, 1).toLowerCase() + (str.substring(1));
    }

    public static String lowStr_hump(String name) {
        return lowStr_hump(name, true);
    }

    public static String lowStr_hump(String name, boolean d) {
        if (name == null) {
            return null;
        }
        String nameHump = humpMap.get(name);
        if (nameHump != null) {
            return nameHump;
        }
        String[] arr1 = name.split("_");
        StringBuilder n = new StringBuilder(arr1[0]);
        for (int i = 1; i < arr1.length; i++) {
            char[] chars = arr1[i].toCharArray();
            for (int i1 = 0; i1 < chars.length; i1++) {
                if (i1 == 0) {
                    if (d) {
                        n.append(String.valueOf(chars[i1]).toUpperCase());
                    } else {
                        n.append(String.valueOf(chars[i1]).toLowerCase());
                    }

                } else {
                    n.append(String.valueOf(chars[i1]).toLowerCase());
                }
            }
        }
        humpMap.put(name, n.toString());
        return n.toString();
    }


    public static Map<String, Object> mapArrayToNoArray(Map<String, Object> map) {
        Map<String, Object> map0 = new HashMap<>();
        map.forEach((k, v) -> {
            if (v == null || v.toString().isEmpty()) {
                return;
            }
            if (v.getClass().isArray()) {
                map0.put(k, ((Object[]) v)[0]);
            } else {
                map0.put(k, v);
            }
        });
        return map0;
    }

    public static byte[] objectToByte(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T objectRestore(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileExtension(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1);
        }
        return "";
    }

    public static String textDelNoText(String data) {
        if (data == null) {
            return null;
        }
        return data.replaceAll("\\s", "")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("\t", "");
    }

    public static boolean isText(byte[] data) {
        for (byte b : data) {
            if ((b & 0xFF) < 32 && b != '\n' && b != '\r' && b != '\t') {
                return false;
            }
        }
        return true;
    }

    public static Method[] readMethods(Class<?> aClass) {
        List<Method> methodList = new ArrayList<>();
        Class<?> currentClass = aClass;

        // 遍历继承链，直到Object类或null（Object的父类为null）
        while (currentClass != null) {
            //System.out.println("当前类: " + currentClass.getName());

            // 获取当前类的所有声明方法
            Method[] declaredMethods = currentClass.getDeclaredMethods();
            // System.out.println("声明方法数量: " + declaredMethods.length);

            // 添加方法并去重（通过方法签名判断）
            for (Method method : declaredMethods) {
                if (!isMethodDuplicate(methodList, method)) {
                    methodList.add(method);
                }
            }

            // 移动到父类
            currentClass = currentClass.getSuperclass();
            //System.out.println("父类: " + (currentClass != null ? currentClass.getName() : "null"));
            //System.out.println("------------------");
        }

        return methodList.toArray(new Method[0]);
    }

    // 判断方法是否已存在（通过签名：名称+参数类型）
    private static boolean isMethodDuplicate(List<Method> methodList, Method newMethod) {
        String newMethodSignature = getMethodSignature(newMethod);
        for (Method method : methodList) {
            if (newMethodSignature.equals(getMethodSignature(method))) {
                return true;
            }
        }
        return false;
    }

    // 生成方法签名（名称+参数类型列表）
    private static String getMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder(method.getName());
        sb.append("(");
        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(paramTypes[i].getName());
        }
        sb.append(")");
        return sb.toString();
    }


    public static String[] getMethodParameterArray(String allCode, String name, Class<?>[] classes) throws Exception {
        return getMethodParameter(allCode, name, classes).toArray(new String[]{});
    }

    public static List<String> getMethodParameter(String allCode, String name, Class<?>[] classes) throws Exception {
        List<String> list = new ArrayList<>();
        while (allCode.indexOf("  ") > -1) {
            allCode = allCode.replaceAll("  ", " ");
        }
        while (allCode.indexOf(", ") > -1) {
            allCode = allCode.replaceAll(", ", ",");
        }
        allCode = allCode
                .replaceAll("\t", "")
                .replaceAll("\r\n", "")
                .replaceAll("\r", "")
                .replaceAll("\n", "")
                .replaceAll("  ", " ")
                .replaceAll(", ", ",")
                .replaceAll(name + "\\(", name + " (")
                .replaceAll(" \\)", ")")
                .replace("[]", "[] ")
                .replace(" []", "[]");
        while (allCode.indexOf("  ") > -1) {
            allCode = allCode.replaceAll("  ", " ");
        }
        while (allCode.indexOf(", ") > -1) {
            allCode = allCode.replaceAll(", ", ",");
        }
        List<String> list1 = Tools.textMid(allCode, name + " (", ")");
        for (int i = 0; i < list1.size(); i++) {
            String data01 = list1.get(i);
            List<String> list001 = Tools.textMid(data01, "<", ">");
            for (int i1 = 0; i1 < list001.size(); i1++) {
                data01 = data01.replace("<" + list001.get(i) + ">", "");
            }
            String[] ss1 = data01.split(",");
            if (ss1.length + 0 == classes.length + 0) {
                int len = 0;
                for (int i1 = 0; i1 < classes.length; i1++) {
                    String[] ss2 = ss1[i1].split(" ");
                    if (ss2.length > 1) {
                        ss2[0] = ss2[0].replace("[]", "");
                        String tName = null;
                        if (classes[i1].getComponentType() == null) {
                            tName = classes[i1].getName();
                        } else {
                            tName = classes[i1].getComponentType().getName();
                        }
                        if (classes[i1].getName().indexOf(ss2[0]) > -1) {
                            len++;
                        } else if (tName.equals("int")) {
                            len++;
                        } else if (tName.equals("char")) {
                            len++;
                        } else if (tName.equals("long")) {
                            len++;
                        } else if (tName.equals("double")) {
                            len++;
                        } else if (tName.equals("short")) {
                            len++;
                        } else if (tName.equals("float")) {
                            len++;
                        } else if (tName.equals("byte")) {
                            len++;
                        } else if (tName.equals("boolean")) {
                            len++;
                        }
                    }
                }
                if (len + 0 == classes.length + 0) {
                    for (int i1 = 0; i1 < ss1.length; i1++) {
                        String[] ss2 = ss1[i1].split(" ");
                        if (ss2.length > 1) {
                            list.add(ss2[1]);
                        }
                    }
                }
                return list;
            }

        }

        return list;
    }

    public static String getProjectRoot() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length > 0) {
            try {
                return getProjectRoot(Class.forName(getStartClassName()));
            } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
        return getProjectRoot0(Tools.class);
    }

    public static StackTraceElement[] getStackTrace() {
        return new Throwable().getStackTrace();
    }
    public static String getStartClassName() {
        StackTraceElement[] stackTrace = getStackTrace();
        return stackTrace[stackTrace.length - 1].getClassName();
    }

    public static String getProjectRoot(Class<?> aClass) {
        String classPath = getProjectRoot0(aClass);
        return classPath.split("target")[0];
    }
    public static String getProjectRoot0(Class<?> aClass) {
        try {
            CodeSource codeSource = aClass.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return new File(System.getProperty("user.dir")).getAbsolutePath();
            }

            URL location = codeSource.getLocation();
            if (location == null) {
                return new File(System.getProperty("user.dir")).getAbsolutePath();
            }

            // 处理Spring Boot嵌套JAR的URL格式（jar:file:/path/to/app.jar!/BOOT-INF/classes/）
            String locationStr = location.toString();
            File jarFile = null;

            // 1. 识别嵌套JAR的URL（格式：jar:file:/xxx/app.jar!/...）
            if (locationStr.startsWith("jar:file:")) {
                // 截取"jar:file:"之后、"!/"之前的部分（即外部JAR的路径）
                int separatorIndex = locationStr.indexOf("!/");
                if (separatorIndex != -1) {
                    String jarPath = locationStr.substring("jar:file:".length(), separatorIndex);
                    // 处理URL编码（如空格会被编码为%20）
                    jarPath = new URI(jarPath).getPath();
                    jarFile = new File(jarPath);
                }
            }
            // 2. 普通JAR或目录（如IDE调试时的classpath）
            else if (locationStr.startsWith("file:")) {
                String filePath = new URI(locationStr).getPath();
                jarFile = new File(filePath);
            }

            if (jarFile == null) {
                return new File(System.getProperty("user.dir")).getAbsolutePath();
            }

            // 3. 确定项目根目录
            if (jarFile.isFile() && jarFile.getName().endsWith(".jar")) {
                // JAR运行时：返回JAR所在目录
                return jarFile.getParentFile().getAbsolutePath();
            } else {
                // 调试环境（IDE中）：返回classes目录的上层（通常是target/classes -> target -> 项目根）
                return jarFile.getParentFile().getPath().split("target")[0];
            }

        } catch (URISyntaxException e) {
            return new File(System.getProperty("user.dir")).getAbsolutePath();
        }
    }

    /**
     * 获取程序运行目录，支持从JAR包运行和IDE调试环境
     *
     * @return 程序运行目录的绝对路径
     */
    public static String getProjectRoot00(Class<?> aClass) {
        try {
            // 获取当前类的代码源
            CodeSource codeSource = aClass.getProtectionDomain().getCodeSource();

            if (codeSource == null) {
                // 某些特殊环境下可能返回null，此时使用用户目录
                return new File(System.getProperty("user.dir")).getAbsolutePath();
            }

            URL location = codeSource.getLocation();
            if (location == null) {
                return new File(System.getProperty("user.dir")).getAbsolutePath();
            }

            // 处理文件路径中的空格和特殊字符
            String path = location.toURI().getPath();

            // 判断是否从JAR包运行
            if (path.toLowerCase().endsWith(".jar")) {
                // JAR包运行时，获取JAR包所在目录
                return new File(path).getParentFile().getAbsolutePath();
            } else {
                // 调试环境下，获取类路径的根目录
                // 对于Maven项目，通常是target/classes目录
                return new File(path).getParentFile().getParentFile().getAbsolutePath();
            }
        } catch (URISyntaxException e) {
            // 处理URI解析异常
            return new File(System.getProperty("user.dir")).getAbsolutePath();
        }
    }

    public static String pathFormat(String path) {
        String temp = path.replaceAll("\\\\", "/")
                .replaceAll("/{2,}", "/")
                .replaceAll("\\.\\.", "-");
        if (!temp.endsWith("/")) {
            temp = temp + "/";
        }
        return temp;
    }


    public static String getPath(Class<?> aClass, String resourcePath) {
        URL url = aClass.getResource(resourcePath);
        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try {
            return new File(url.toURI()).getAbsolutePath();
        } catch (URISyntaxException e) {
            return url.getPath();
        }
    }

    public static int[] toArrayType(Object[] data, int[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new int[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            Integer kk = Integer.parseInt(data[i].toString().split("\\.")[0]);
            arr[i] = kk;
        }
        return arr;
    }

    public static short[] toArrayType(Object[] data, short[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new short[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Short.parseShort(data[i].toString());
        }
        return arr;
    }

    public static float[] toArrayType(Object[] data, float[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new float[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Float.parseFloat(data[i].toString());
        }
        return arr;
    }

    public static long[] toArrayType(Object[] data, long[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new long[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Long.parseLong(data[i].toString());
        }
        return arr;
    }

    public static boolean[] toArrayType(Object[] data, boolean[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new boolean[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Boolean.parseBoolean(data[i].toString());
        }
        return arr;
    }

    public static double[] toArrayType(Object[] data, double[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new double[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Double.parseDouble(data[i].toString());
        }
        return arr;
    }

    public static char[] toArrayType(Object[] data, char[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new char[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = data[i].toString().toCharArray()[0];
        }
        return arr;
    }

    public static byte[] toArrayType(Object[] data, byte[] arr) {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = new byte[data.length];
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                continue;
            }
            arr[i] = Byte.parseByte(data[i].toString());
        }
        return arr;
    }

    /**
     * 获取指定 Windows 进程的 CPU 占用率。
     *
     * @param processName 进程名，例如 "chrome.exe"
     * @return CPU 占用百分比（0-100），如果未找到进程或发生错误则返回 -1。
     */
    public static double getProcessCpuUsage(String processName) {
        if (processName == null || processName.isEmpty()) {
            return -1;
        }

        try {
            // 构建 wmic 命令
            String command = "wmic path Win32_PerfFormattedData_PerfProc_Process where \"Name='" + processName + "'\" get PercentProcessorTime";
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            builder.redirectErrorStream(true);

            Process process = builder.start();

            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 等待命令执行完成
            process.waitFor();

            String result = output.toString();

            // 解析输出
            String[] lines = result.split("\n");
            if (lines.length > 2) {
                // 找到包含 CPU 数据的行，通常是第二行
                String cpuLine = lines[2].trim();
                if (!cpuLine.isEmpty()) {
                    return Double.parseDouble(cpuLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static int getCPUNum() {
        return getCPUNum(10000);
    }

    public static int getCPUNum(int max) {
        int n = Runtime.getRuntime().availableProcessors();
        if (n > max) {
            n = max;
        }
        return n;
    }


    public static String replaceAll(String str) {
        String[] arr1 = new String[]{"\r", "\n", "\t"};
        String[] arr2 = new String[]{"\\\\r", "\\\\n", "\\\\t"};
        return replaceAll(str, arr1, arr2);
    }

    public static String replaceAll2(String str) {
        String[] arr1 = new String[]{"\r", "\n", "\t", "\""};
        String[] arr2 = new String[]{"\\\\r", "\\\\n", "\\\\t", "\\\\\\\""};
        return replaceAll(str, arr1, arr2);
    }

    public static String replaceAll(String str, String[] arr1, String[] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            str = str.replaceAll(arr1[i], arr2[i]);
        }
        return str;
    }

    public static String replaceAll(String str, String str1, String str2) {
        while (str.contains(str1)) {
            str = str.replaceAll(str1, str2);
        }
        return str;
    }

    public static List<Class<?>> scanClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);

        if (resource != null) {
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                scanDirectory(packageName, directory, classes);
            }
        }

        return classes;
    }

    private static void scanDirectory(String packageName, File directory, List<Class<?>> classes) throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(packageName + "." + file.getName(), file, classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        }
    }

    public static List<Class<?>> getPackageClass(String packageName) {
        //第一个class类的集合

        List<Class<?>> classes = new ArrayList<Class<?>>();

        //是否循环迭代

        boolean recursive = true;

        //获取包的名字 并进行替换

        String packageDirName = packageName.replace('.', '/');

        //定义一个枚举的集合 并进行循环来处理这个目录下的things

        Enumeration<URL> dirs;

        try {

            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            //循环迭代下去

            while (dirs.hasMoreElements()) {

                //获取下一个元素

                URL url = dirs.nextElement();

                //得到协议的名称

                String protocol = url.getProtocol();

                //如果是以文件的形式保存在服务器上

                if ("file".equals(protocol)) {

                    //获取包的物理路径

                    String filePath = URLDecoder.decode(url.getFile(), System.getProperty("file.encoding", "UTF-8"));

                    //以文件的方式扫描整个包下的文件 并添加到集合中

                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);

                } else if ("jar".equals(protocol)) {

                    //如果是jar包文件

                    //定义一个JarFile

                    JarFile jar;

                    try {

                        //获取jar

                        jar = ((JarURLConnection) url.openConnection()).getJarFile();

                        //从此jar包 得到一个枚举类

                        Enumeration<JarEntry> entries = jar.entries();

                        //同样的进行循环迭代

                        while (entries.hasMoreElements()) {

                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件

                            JarEntry entry = entries.nextElement();

                            String name = entry.getName();

                            //如果是以/开头的

                            if (name.charAt(0) == '/') {

                                //获取后面的字符串

                                name = name.substring(1);

                            }

                            //如果前半部分和定义的包名相同

                            if (name.startsWith(packageDirName)) {

                                int idx = name.lastIndexOf('/');

                                //如果以"/"结尾 是一个包

                                if (idx != -1) {

                                    //获取包名 把"/"替换成"."

                                    packageName = name.substring(0, idx).replace('/', '.');

                                }

                                //如果可以迭代下去 并且是一个包

                                if ((idx != -1) || recursive) {

                                    //如果是一个.class文件 而且不是目录

                                    if (name.endsWith(".class") && !entry.isDirectory()) {

                                        //去掉后面的".class" 获取真正的类名

                                        String className = name.substring(packageName.length() + 1, name.length() - 6);

                                        try {

                                            //添加到classes

                                            classes.add(Class.forName(packageName + '.' + className));

                                        } catch (ClassNotFoundException e) {

                                            e.printStackTrace();

                                        }

                                    }

                                }

                            }

                        }

                    } catch (IOException e) {

                        e.printStackTrace();

                    }

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }


        return classes;

    }

    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {

        //获取此包的目录 建立一个File

        File dir = new File(packagePath);

        //如果不存在或者 也不是目录就直接返回

        if (!dir.exists() || !dir.isDirectory()) {

            return;

        }

        //如果存在 就获取包下的所有文件 包括目录

        File[] dirfiles = dir.listFiles(new FileFilter() {

            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)

            public boolean accept(File file) {

                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));

            }

        });

        //循环所有文件

        for (File file : dirfiles) {

            //如果是目录 则继续扫描

            if (file.isDirectory()) {

                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),

                        file.getAbsolutePath(),

                        recursive,

                        classes);

            } else {

                //如果是java类文件 去掉后面的.class 只留下类名

                String className = file.getName().substring(0, file.getName().length() - 6);

                try {

                    //添加到集合中去

                    classes.add(Class.forName(packageName + '.' + className));

                } catch (ClassNotFoundException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    public static boolean isJar() {
        URL url = Tools.class.getResource("");
        String protocol = url.getProtocol();
        return protocol.equals("file") == false;
    }

    public static String joinArray(Object[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);
            if (i < array.length - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String joinArray(Object object) {
        if (object.getClass().getName().substring(1, 2).equals("L")) {
            return joinArray((Object[]) object);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(object);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static boolean StringNotNull(Object... arr) {
        for (Object s : arr) {
            if (s == null || s.toString().length() == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean IdCardVerify(String str) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += (str.charAt(i) - 48) * arr[i];
        }
        if (str.charAt(17) == 88) {
            return arrCheck[sum % 11] == 88;
        } else {
            return arrCheck[sum % 11] == str.charAt(17) - 48;
        }
    }

    public static byte[] readByte(InputStream inputStream) throws Exception {
        return readInputStream(inputStream);
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static byte[] readFileInputStream(FileInputStream fileInputStream) throws Exception {
        return readBufferedInputStream(new BufferedInputStream(fileInputStream));
    }

    public static byte[] readBufferedInputStream(BufferedInputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    //验证码 gif 那种
    public static String getPictureCode2(Response response) {
        GifCaptcha gifCaptcha = new GifCaptcha(140, 45, 5);
        String verCode = gifCaptcha.text().toLowerCase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gifCaptcha.out(outputStream);
        response.setHeader("content-type", "image/gif");
        response.write(outputStream.toByteArray());
        response.flush();
        return verCode;
    }

    //验证码 gif 那种
    public static byte[] getPictureCode2() {
        GifCaptcha gifCaptcha = new GifCaptcha(140, 45, 5);
        String verCode = gifCaptcha.text().toLowerCase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gifCaptcha.out(outputStream);
        return outputStream.toByteArray();
    }


    public static String arrayToString(Object[] objs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                sb.append("\"");
                sb.append(objs[i]);
                sb.append("\"");
                if (i != objs.length - 1) sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static <T> T[] appendArray(T[] ts, Object... objs) {
        List list = new ArrayList();
        if (ts == null) {
            ts = (T[]) new Object[]{};
        }
        for (T t : ts) {
            list.add(t);
        }
        for (Object object : objs) {
            list.add(object);
        }
        return (T[]) list.toArray(ts);
    }

    public static <T> T[] appendArray(T[] ts, T[] def, T... tArray) {
        List<T> list = new ArrayList<>();
        if (ts != null) {
            Collections.addAll(list, ts);
        }
        Collections.addAll(list, tArray);
        return list.toArray(def);
    }


    public static Object[] toArray(Object... objs) {
        return objs;
    }

    public static Object[] toArray() {
        return new Object[0];
    }

    public static List<Object> toList(Object... objs) {
        List<Object> list = new ArrayList<>();
        if (objs == null) {
            return list;
        }
        for (int i = 0; i < objs.length; i++) {
            list.add(objs[i]);
        }
        return list;
    }

    public static String[] toArrayString(Object... objs) {

        String[] ss1 = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (String) objs[i];
        }
        return ss1;
    }

    public static Integer[] toArrayInteger(Object... objs) {
        Integer[] ss1 = new Integer[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Integer) objs[i];
        }
        return ss1;
    }

    public static Short[] toArrayShort(Object... objs) {
        Short[] ss1 = new Short[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Short) objs[i];
        }
        return ss1;
    }

    public static Long[] toArrayLong(Object... objs) {
        Long[] ss1 = new Long[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Long) objs[i];
        }
        return ss1;
    }

    public static Float[] toArrayFloat(Object... objs) {
        Float[] ss1 = new Float[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Float) objs[i];
        }
        return ss1;
    }

    public static Boolean[] toArrayBoolean(Object... objs) {
        Boolean[] ss1 = new Boolean[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Boolean) objs[i];
        }
        return ss1;
    }

    public static Double[] toArrayDouble(Object... objs) {
        Double[] ss1 = new Double[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Double) objs[i];
        }
        return ss1;
    }

    public static Character[] toArrayCharacter(Object... objs) {
        Character[] ss1 = new Character[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Character) objs[i];
        }
        return ss1;
    }

    public static Byte[] toArrayByte(Object... objs) {
        Byte[] ss1 = new Byte[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ss1[i] = (Byte) objs[i];
        }
        return ss1;
    }


    public static Integer[] toArray(Integer... arr) {
        return arr;
    }

    public static Short[] toArray(Short... arr) {
        return arr;
    }

    public static Long[] toArray(Long... arr) {
        return arr;
    }

    public static Float[] toArray(Float... arr) {
        return arr;
    }

    public static Boolean[] toArray(Boolean... arr) {
        return arr;
    }

    public static Double[] toArray(Double... arr) {
        return arr;
    }

    public static Character[] toArray(Character... arr) {
        return arr;
    }

    public static Byte[] toArray(Byte... arr) {
        return arr;
    }

    public static String doubleTo2(double data) {
        return String.format("%.2f", data);
    }

    public static String doubleTo8(double data) {
        return String.format("%.8f", data);
    }

    public static String doubleTo1(double data) {
        return String.format("%.1f", data);
    }


    /**
     * 如果object ==null 或者 =="" 返回true
     */
    public static boolean isNull(Object object) {
        return object == null || object.toString().length() < 1;
    }

    public static boolean isString(String str, int min, int max) {
        return isNull(str) || str.length() < min || str.length() > max;
    }

    /**
     * 小数 保留位数
     */
    public static String doubleSize(double data, int size) {
        return String.format("%." + size + "f", data);
    }

    /**
     * 获取指定长度的随机字符a-z,A-Z,0-9
     */
    public static String getRandomString(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(ss1[getRandomInt(ss1.length - 1, 0)]);
        }
        return sb.toString();
    }


    /**
     * 获取整数随机值
     */
    public static Long getRandomLong(long max, long min) {
        if (max == min) {
            return (long) max;
        }
        if (min > max) {
            long a = max;
            max = min;
            min = a;
        }

        return (long) (random.nextInt((int) (max - min + 1)) + min);
    }

    /**
     * 获取整数随机值
     */
    public static int getRandomInt(int max, int min) {
        if (max == min) {
            return max;
        }
        if (min > max) {
            int a = max;
            max = min;
            min = a;
        }
        return random.nextInt(max - min + 1) + min;
    }

    public static String getUUID() {
        return new Date().getTime() + getRandomString(10);
    }

    public static String getUUID(int len) {
        return new Date().getTime() + getRandomString(len);
    }

    public static String getUUID(String id) {
        return new Date().getTime() + id;
    }


    /**
     * 是否linux服务器
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0;
    }

    /**
     * 获取异常信息 详细
     */
    public static String getExceptionInfo(Exception ex) {
        ByteArrayOutputStream out = null;
        PrintStream pout = null;
        String ret = "";
        try {
            out = new ByteArrayOutputStream();
            pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = out.toString();
            out.close();
        } catch (Exception e) {
            return ex.getMessage();
        } finally {
            if (pout != null) {
                pout.close();
            }
        }
        return ret;
    }

    public static String getExceptionInfo(Throwable throwable) {
        ByteArrayOutputStream out = null;
        PrintStream pout = null;
        String ret = "";
        try {
            out = new ByteArrayOutputStream();
            pout = new PrintStream(out);
            throwable.printStackTrace(pout);
            ret = out.toString();
            out.close();
        } catch (Exception e) {
            return throwable.getMessage();
        } finally {
            if (pout != null) {
                pout.close();
            }
        }
        return ret;
    }

    /**
     * 获取锁ReentrantLock
     */
    public static Lock lockGet() {
        return new ReentrantLock();
    }


    public static String textBase64Encoder(String str) {
        try {
            return textBase64Encoder(str.getBytes(System.getProperty("file.encoding", "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String textBase64Encoder(byte[] strByte) {
        try {
            return new String(Base64.getEncoder().encode(strByte), "UTF-8").replaceAll("=", "_").replaceAll("\\+", "-");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String textBase64Decoder(String str) {
        try {
            str = str.replaceAll("\r", "");
            str = str.replaceAll("\n", "");
            str = str.replaceAll("\t", "");
            str = str.replaceAll("\\s", "");
            return new String(textBase64DecoderByte(str), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] textBase64DecoderByte(String str) {
        try {
            return Base64.getDecoder().decode(str.replaceAll("_", "=").replaceAll("-", "+"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String textAESEncoder(String str, String pwd1, String pwd2) {
        try {
            byte[] raw = pwd1.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
            IvParameterSpec iv = new IvParameterSpec(pwd2.getBytes("UTF-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return textBase64Encoder(cipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String textAESDecoder(String str, String pwd1, String pwd2) {
        try {
            byte[] raw = pwd1.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(pwd2.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return new String(cipher.doFinal(textBase64DecoderByte(str)), "UTF-8");
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


    /**
     * 获取文本MD5
     * 参数1:文本
     * 默认为 UTF-8
     */
    public static String textToMd5(String str) {
        return toMd5(str.getBytes(Config.encoding));
    }

    /**
     * 获取文本MD5
     * 参数1:byte[]
     */
    public static String toMd5(byte[] bytes) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(bytes);
            byte s[] = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            while (result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String textMid(String str, String q, String h, int index) {
        int a = 0;
        int b = 0;
        while (index > 0) {
            index--;
            a = str.indexOf(q, b);
            if (a < 0) {
                return null;
            }
            b = str.indexOf(h, a + q.length());
            if (b < 0) {
                return null;
            }
        }
        return str.substring(a + q.length(), b);
    }


    public static List<String> textMid(String str, String q, String h) {
        int a = 0;
        int b = 0;
        List<String> list = new ArrayList<String>();
        while (true) {
            a = str.indexOf(q, b);
            if (a < 0) {
                break;
            }
            b = str.indexOf(h, a + q.length());
            if (b < 0) {
                break;
            }
            list.add(str.substring(a + q.length(), b));
        }
        return list;
    }


    //###############################################文件操作@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    public static String toMD5Path(String rootPath, String md5) {
        String path = rootPath
                .replaceAll("\\\\", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/");
        path = pathFormat(path);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        path += "/" + toMD5Path(md5);
        return path;
    }

    public static String toMD5Path(String md5) {

        String path = "resources/";
        path += md5.substring(0, 2) + "/";
        path += md5.substring(2, 4) + "/";
        path += md5.substring(4, 6) + "/";
        path += md5 + "/";
        return path;
    }

    public static String fileSaveResources(String file, String rootUrl) throws Exception {
        return fileSaveResources(new File(file), rootUrl);
    }

    /**
     * 文件保存到 某个目录下 会根据文件md5自动新建目录
     */
    public static String fileSaveResources(File file, String rootUrl) throws Exception {
        String md5 = Tools.fileToMd5(file);
        String fileName = file.getName();
        String path = rootUrl + "/resources/";
        path += md5.substring(0, 2) + "/";
        path += md5.substring(2, 4) + "/";
        path += md5.substring(4, 6) + "/";
        path += md5 + "/";
        new File(path).mkdirs();
        path += fileName;
        path = pathFormat(path);
        File file2 = new File(path);
        if (file2.exists()) {
            return path;//重复文件!
        }
        FileTools.save(file2, Tools.fileReadByte(file));
        return path;
    }

    public static String fileSaveResources(byte[] bytes, String rootUrl, String suffix) throws Exception {
        String md5 = Tools.toMd5(bytes);
        String page = rootUrl + "/resources/";
        page += md5.substring(0, 2) + "/";
        page += md5.substring(2, 4) + "/";
        page += md5.substring(4, 6) + "/";
        new File(page).mkdirs();
        page += md5 + "." + suffix;
        File file2 = new File(page);
        if (file2.exists()) {
            return page;//重复文件!
        }
        FileTools.save(file2, bytes);
        return page;
    }

    public static String fileSaveResources(byte[] bytes, String rootUrl) throws Exception {
        return fileSaveResources(bytes, rootUrl, "data");
    }


    /**
     * 读取byte数据
     * 参数1:文件地址 例如 d:/a.txt
     *
     * @throws Exception
     */
    public static byte[] fileReadByte(String file) throws Exception {
        return fileReadByte(new File(file));
    }

    /**
     * 读取byte数据
     * 参数1:文件地址 例如 new File("d:/a.txt")
     *
     * @throws Exception
     */
    public static byte[] fileReadByte(File file) throws Exception {
        byte[] bt = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                bt = new byte[(int) file.length()];
                bis.read(bt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        return bt;
    }

    /**
     * 读取字符串数据
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 参数2:编码
     *
     * @throws Exception
     */
    public static String fileReadString(File file, String encoding) throws Exception {
        byte[] bytes = fileReadByte(file);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, encoding);
    }

    /**
     * 读取字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:编码
     *
     * @throws Exception
     */
    public static String fileReadString(String file, String encoding) throws Exception {
        return fileReadString(new File(file), encoding);
    }

    /**
     * 读取字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 默认编码为"UTF-8"
     *
     * @throws Exception
     */
    public static String fileReadString(String file) throws Exception {
        return fileReadString(new File(file), "UTF-8");
    }

    /**
     * 读取字符串数据
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 默认编码为"UTF-8"
     *
     * @throws Exception
     */
    public static String fileReadString(File file) throws Exception {
        return new String(fileReadByte(file), "UTF-8");
    }

    /**
     * 读取字符串数据 返回数组 一行一个
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 参数2:编码
     *
     * @throws Exception
     */
    public static String[] fileReadArray(File file, String encoding, String split) throws Exception {
        return new String(fileReadByte(file), encoding).split(split);
    }


    static int BUFFER_SIZE = 16 * 1024;

    /**
     * 计算文件的 MD5 哈希值（支持超大文件，分块读取，避免内存溢出）
     *
     * @param file 待计算 MD5 的文件（需确保文件存在且可读取）
     * @return 文件的 MD5 字符串（32位小写）
     * @throws Exception 若文件不存在、不可读或计算过程中出现 IO/加密异常
     */
    public static String fileToMd5(File file) throws Exception {
        // 1. 校验文件合法性（避免无效操作）
        if (file == null) {
            throw new IllegalArgumentException("文件对象不能为 null");
        }
        if (!file.exists()) {
            throw new FileNotFoundException("文件不存在：" + file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("没有文件读取权限：" + file.getAbsolutePath());
        }
        if (file.isDirectory()) {
            throw new IOException("不能计算目录的 MD5：" + file.getAbsolutePath());
        }

        // 2. 初始化 MD5 加密器（线程不安全，需每次创建新实例）
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        // 3. 分块读取文件（使用带缓冲的流，提升读取效率）
        try (InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead; // 每次实际读取的字节数

            // 逐块更新 MD5 哈希值（核心：避免一次性加载文件到内存）
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                // 仅更新实际读取的字节（避免缓冲区剩余的无效数据影响结果）
                md5Digest.update(buffer, 0, bytesRead);
            }
        }

        // 4. 生成最终 MD5 字节数组，并转为 32 位小写字符串
        byte[] md5Bytes = md5Digest.digest();
        StringBuilder md5Sb = new StringBuilder();
        for (byte b : md5Bytes) {
            // 将字节转为 16 进制（不足两位补 0，确保最终是 32 位）
            md5Sb.append(String.format("%02x", b));
        }

        return md5Sb.toString();
    }


    /**
     * 获取文本MD5
     * 参数1:文件地址 例如 "d:/a.txt"
     *
     * @throws Exception
     */
    public static String fileToMd5(String file) throws Exception {
        return fileToMd5(new File(file));
    }

    //压缩 参数1 被压缩文件   参数2 压缩到文件
    public static void fileZipEncoder(String fileUrl, String target) {
        ZipUtil.compress(fileUrl, target);
    }

    //压缩 参数1 被压缩目录   参数2 压缩到文件
    public static void fileZipEncoderDir(String fileUrl, String target) {
        ZipUtil.compressDir(fileUrl, target);
    }

    //解压  参数1 压缩文件    参数2 解压到 目录
    public static void fileZipDecoder(String fileUrl, String target) {
        ZipUtil.decompress(fileUrl, target);
    }
}



