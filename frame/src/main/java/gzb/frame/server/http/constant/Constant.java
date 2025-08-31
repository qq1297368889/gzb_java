package gzb.frame.server.http.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static final ContentType contentType = new ContentType();

    // 基本数据类型和包装类的映射
    public static Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_MAP = new HashMap<>();
    public static String[] requestMethod;

    static {
        PRIMITIVE_WRAPPER_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_WRAPPER_MAP.put(byte.class, Byte.class);
        PRIMITIVE_WRAPPER_MAP.put(char.class, Character.class);
        PRIMITIVE_WRAPPER_MAP.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_MAP.put(int.class, Integer.class);
        PRIMITIVE_WRAPPER_MAP.put(long.class, Long.class);
        PRIMITIVE_WRAPPER_MAP.put(float.class, Float.class);
        PRIMITIVE_WRAPPER_MAP.put(double.class, Double.class);
        requestMethod = new String[]{"GET", "POST", "PUT", "DELETE"};
    }
}
