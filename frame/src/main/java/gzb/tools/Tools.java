package gzb.tools;

import gzb.entity.PagingEntity;
import gzb.entity.TableInfo;
import gzb.entity.UploadEntity;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassFactory;
import gzb.frame.server.http.Handle;
import gzb.start.Application;
import gzb.tools.http.HTTP;
import gzb.tools.img.DrawmageUtil;
import gzb.tools.img.GifCaptcha;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.file.Files;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Tools {
    public static String[] ss1 = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789".split("|");
    public static Lock lock = new ReentrantLock();
    public static int[] arr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    public static int[] arrCheck = {1, 0, 88, 9, 8, 7, 6, 5, 4, 3, 2};
    static Random random = new Random(new Date().getTime() + 100);

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
                fileSaveByte(file, bytes2, true);
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
            Tools.fileSaveByte(mp4Path, Tools.fileReadByte(file), true);
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
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<GzbMap> list_sys_permission = dataBase.selectGzbMap("select * from sys_permission");
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

        return 1;
    }

    public static int loadApiInfo(DataBase dataBase) throws Exception {
        String sql = "INSERT INTO sys_permission(" +
                "sys_permission_id, sys_permission_name, sys_permission_data, " +
                "sys_permission_type, sys_permission_desc, sys_permission_sup, " +
                "sys_permission_sort) " +
                "SELECT ?,?,?,?,?,?,? " +
                "WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE sys_permission_name = ?)";
        Object[] objects = null;
        int x = 0;
        if (Handle.classFactory.mapClass.mapping0==null || Handle.classFactory.mapClass.mapping0.isEmpty()) {
            return 0;
        }
        for (Map.Entry<String, Object[]> stringEntry : Handle.classFactory.mapClass.mapping0.entrySet()) {
            objects = new Object[]{OnlyId.getDistributed(), stringEntry.getKey(), null, 2, null, 0, 0, stringEntry.getKey()};
            dataBase.runSql(sql, objects);
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

    public static String toJson(Object obj) {
        if (obj == null) {
            return "{}";
        }
        Class<?> clazz = obj.getClass();
        if (isPrimitiveOrWrapper(clazz) || clazz == String.class) {
            return formatValue(obj);
        }
        if (clazz.isArray()) {
            return arrayToJson(obj);
        }
        return objectToJson(obj);
    }

    public static int textLength(Object obj) {
        if (obj == null) {
            return 0;
        }
        int size = obj.toString().length();
        if (obj instanceof Double || obj instanceof Float) {
            return size - 1;
        }
        return size;
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Boolean.class ||
                clazz == Byte.class ||
                clazz == Character.class ||
                clazz == Short.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Float.class ||
                clazz == Double.class;
    }

    private static String formatValue(Object value) {
        if (value instanceof String) {
            return "\"" + value.toString().replace("\"", "\\\"") + "\"";
        }
        return value.toString();
    }

    private static String arrayToJson(Object array) {
        List<String> elements = new ArrayList<>();
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            Object element = Array.get(array, i);
            elements.add(toJson(element));
        }
        return "[" + String.join(",", elements) + "]";
    }

    private static String objectToJson(Object obj) {
        List<String> keyValuePairs = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                String key = "\"" + field.getName() + "\"";
                String jsonValue = toJson(value);
                keyValuePairs.add(key + ":" + jsonValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "{" + String.join(",", keyValuePairs) + "}";
    }

    public static void jsonObjectArrayToJson(Object[] log, StringBuilder sb, String spx) {
        if (log == null) {
            sb.append("null"); // 添加 null 占位符
            return;
        }
        for (int i = 0; i < log.length; i++) {
            if (log[i] == null) {
                sb.append("null"); // 添加 null 占位符
            } else if (log[i].getClass().isArray()) {
                jsonArrayToJson(log[i], sb);
            } else {
                if (log[i] instanceof Exception) {
                    String errMsg = Tools.getExceptionInfo((Exception) log[i]);
                    sb.append(errMsg);
                } else if (log[i] instanceof Map) {
                    jsonMapToJson((Map<?, ?>) log[i], sb);
                } else if (log[i] instanceof List) {
                    jsonArrayToJson(((List) log[i]).toArray(), sb);
                } else if (log[i] instanceof String) {
                    sb.append("\"");
                    sb.append(log[i]);
                    sb.append("\"");
                } else {
                    sb.append(log[i]);
                }
            }


            if (i < log.length - 1) {
                sb.append(spx);
            }
        }
    }

    public static void jsonArrayToJson(Object array, StringBuilder sb) {
        sb.append("[");
        if (array instanceof Object[]) {
            Object[] objArray = (Object[]) array;
            jsonObjectArrayToJson(objArray, sb, ", ");
        } else if (array instanceof int[]) {
            int[] intArray = (int[]) array;
            for (int i = 0; i < intArray.length; i++) {
                sb.append(intArray[i]);
                if (i < intArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof long[]) {
            long[] longArray = (long[]) array;
            for (int i = 0; i < longArray.length; i++) {
                sb.append(longArray[i]);
                if (i < longArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof double[]) {
            double[] doubleArray = (double[]) array;
            for (int i = 0; i < doubleArray.length; i++) {
                sb.append(doubleArray[i]);
                if (i < doubleArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof float[]) {
            float[] floatArray = (float[]) array;
            for (int i = 0; i < floatArray.length; i++) {
                sb.append(floatArray[i]);
                if (i < floatArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof boolean[]) {
            boolean[] booleanArray = (boolean[]) array;
            for (int i = 0; i < booleanArray.length; i++) {
                sb.append(booleanArray[i]);
                if (i < booleanArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof char[]) {
            char[] charArray = (char[]) array;
            for (int i = 0; i < charArray.length; i++) {
                sb.append("'");
                sb.append(charArray[i]);
                sb.append("'");
                if (i < charArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof byte[]) {
            byte[] byteArray = (byte[]) array;
            for (int i = 0; i < byteArray.length; i++) {
                sb.append(byteArray[i]);
                if (i < byteArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else if (array instanceof short[]) {
            short[] shortArray = (short[]) array;
            for (int i = 0; i < shortArray.length; i++) {
                sb.append(shortArray[i]);
                if (i < shortArray.length - 1) {
                    sb.append(", ");
                }
            }
        } else {
            sb.append(array);
        }
        sb.append("]");
    }

    public static String jsonMapToJson(Map<?, ?> map, StringBuilder sb) {
        if (map == null) {
            return "null";
        }
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            Object key = entry.getKey();
            Object value = entry.getValue();

            // 处理键，确保键用双引号包裹
            sb.append("\"");
            escapeJson(key.toString(), sb);
            sb.append("\":");
            // 处理值
            if (value == null) {
                sb.append("null");
            } else if (value instanceof String) {
                sb.append("\"");
                escapeJson(value.toString(), sb);
                sb.append("\"");
            } else if (value instanceof List) {
                jsonArrayToJson(((List) value).toArray(), sb);
            } else if (value instanceof Map) {
                sb.append(jsonMapToJson((Map<?, ?>) value, sb));
            } else if (value.getClass().isArray()) {
                jsonArrayToJson(value, sb);
            } else {
                sb.append(value.toString());
            }
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    public static String escapeJson(String input, StringBuilder escaped) {
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                default:
                /*    if (c < 32 || c > 126) {
                        // 处理非 ASCII 字符
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }*/
                    escaped.append(c);
            }
        }
        return escaped.toString();
    }

    //首字母转大写
    public static String lowStr_d(String s) {
        return s.substring(0, 1).toUpperCase() + (s.substring(1, s.length()));
    }

    //首字母转小写
    public static String lowStr_x(String s) {
        return s.substring(0, 1).toLowerCase() + (s.substring(1, s.length()));
    }

    public static String lowStr_hump(String str) {
        String[] ss1 = str.split("_");
        String newString = "";
        for (int i = 0; i < ss1.length; i++) {
            if (i == 0) {
                newString += (ss1[i]).toLowerCase();
            } else {
                newString += lowStr_d(ss1[i]);
            }
        }
        return newString;
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

    public static Method[] readMethods0(Class<?> aClass) {
        Class<?> class1 = aClass;
        List<Method> list = new ArrayList<>();
        while (class1 != null) {
            System.out.println(class1);
            Method[] methods = class1.getDeclaredMethods();
            list.addAll(Arrays.asList(methods));
            Class<?> neClass = class1.getSuperclass();
            System.out.println(class1);
            System.out.println(neClass);
            System.out.println(Arrays.toString(methods));
            System.out.println("------------------");
            if (class1.getName().equals(neClass.getName())) {
                break;
            }
            class1 = neClass;
        }
        return list.toArray(new Method[0]);
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
        return getProjectRoot(Tools.class);
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
                return jarFile.getParentFile().getParentFile().getAbsolutePath();
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

    public static String webPathFormat(String path) {
        String temp = pathFormat(path);
        if (temp.substring(0, 1).equals("/") == false) {
            temp = "/" + temp;
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

    public static <T> T[] toArrayType(Object[] data, T[] arr) throws Exception {
        if (data == null) {
            return null;
        }
        if (arr == null) {
            return null;
        }
        if (data.length > arr.length) {
            arr = (T[]) Array.newInstance(arr.getClass().getComponentType(), data.length);
        }
        Method method = null;
        int type01 = 0;
        Class aClass = arr.getClass().getComponentType();
        if (aClass.getComponentType() != null) {
            aClass = aClass.getComponentType();
        }
        if (aClass.getName().equals("java.lang.Character")) {
            method = aClass.getMethod("valueOf", char.class);
            type01 = 1;
        } else if (aClass.getName().equals("java.lang.String")) {
            method = null;
            type01 = 2;
        } else if (aClass.getName().equals("gzb.frame.entity.UploadEntity") || aClass.getName().equals("java.io.File")) {
            method = null;
            type01 = 4;
        } else {
            method = aClass.getMethod("valueOf", String.class);
            type01 = 3;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                arr[i] = null;
                continue;
            }
            if (data.length == 0) {
                if (type01 == 2) {
                    arr[i] = arr[i];
                } else {
                    arr[i] = null;
                }
                continue;
            }
            if (type01 == 1) {
                arr[i] = (T) method.invoke(null, data[i].toString().toCharArray()[0]);
            } else if (type01 == 2) {
                arr[i] = (T) data[i].toString();
            } else if (type01 == 4 && data[i].getClass().getName().equals("gzb.frame.entity.UploadEntity")) {
                if (aClass.getName().equals("gzb.frame.entity.UploadEntity")) {
                    arr[i] = (T) ((UploadEntity) data[i]);
                }
                if (aClass.getName().equals("java.io.File")) {
                    arr[i] = (T) ((UploadEntity) data[i]).getFile();
                }
            } else {
                if (method != null) {
                    arr[i] = (T) method.invoke(null, data[i].toString());
                }
            }
        }
        return arr;
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

    public static String replaceJsonString(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        escapeJson(input, sb);
        return sb.toString();
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

    public static List<Class<?>> getClasses(String packageName) {
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

                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");

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

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */

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

    //验证码 老式
    public static String getPictureCode1(HttpServletResponse response, int type) throws IOException {
        return new DrawmageUtil().verification(response, type);
    }

    //验证码 老式
    public static String getPictureCode1(HttpServletResponse response) throws IOException {
        return getPictureCode1(response, 2);
    }

    //验证码 gif 那种
    public static String getPictureCode2(HttpServletResponse response) throws IOException {
        GifCaptcha gifCaptcha = new GifCaptcha(140, 45, 5);
        String verCode = gifCaptcha.text().toLowerCase();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        gifCaptcha.out(outputStream);
        response.setContentType("image/gif");
        response.getOutputStream().write(outputStream.toByteArray());
        response.getOutputStream().flush();
        return verCode;
    }

/*
    //m3u8链接 转 mp4文件
    public static File m3u8ToMp4(String m3u8Url, String filePath, String fileName, int intervalMin, int intervalMax) throws Exception {
        File file = new File(filePath.replaceAll("\\\\", "/") + "/" + fileName);
        String str1 = new HTTP().httpGet(m3u8Url).toString();
        String pwdUrl = textMid(str1, "#EXT-X-KEY:METHOD=AES-128,URI=\"", "\"", 1);
        String pwd = new HTTP().httpGet(pwdUrl).toString();
        List<String> list = textMid(str1, ",\n", "\n#");
        for (int i = 0; i < list.size(); i++) {
            String subUrl = list.get(i);
            fileSaveByte(file, AES_ECB_128.aesDe(new HTTP().httpGet(subUrl).toByte(), pwd, null), true);
            if (intervalMin > 0 && intervalMax > 0) {
                Thread.sleep(getRandomInt(intervalMax, intervalMin));
            }
        }
        System.out.println(file.getParent() + "/" + file.getName());
        return file;
    }

    //m3u8链接 转 mp4文件
    public static File m3u8ToMp4(String m3u8Url, String filePath, String fileName) throws Exception {
        return m3u8ToMp4(m3u8Url, filePath, fileName, 0, 0);
    }
*/

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

    public static <T> T[] appendArray(T[] ts, T[] def, Object... objs) {
        List list = new ArrayList();
        if (ts == null) {
            ts = def;
        }
        for (T t : ts) {
            list.add(t);
        }
        for (Object object : objs) {
            list.add(object);
        }
        return (T[]) list.toArray(ts);
    }


    public static <T> T[] appendArrayDef(T[] ts, T[] def, Object... objs) {
        List list = new ArrayList();
        if (ts == null) {
            return def;
        }
        for (T t : ts) {
            list.add(t);
        }
        for (Object object : objs) {
            list.add(object);
        }
        return (T[]) list.toArray(ts);
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

    public static void CookieSet(String key, String value, int mm, HttpServletResponse response, HttpServletRequest request) {

        if (key == null || value == null) {
            return;
        }
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(mm);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    public static String CookieGet(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        return ip;
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
            ret = new String(out.toByteArray());
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

    /**
     * 获取锁ReentrantLock
     */
    public static Lock lockGet() {
        return new ReentrantLock();
    }


    public static String textBase64Encoder(String str) {
        try {
            return textBase64Encoder(str.getBytes("UTF-8"));
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
    public static String textToMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return toMd5(str.getBytes("UTF8"));
    }

    /**
     * 获取文本MD5
     * 参数1:byte[]
     */
    public static String toMd5(byte[] bytes) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
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
    public static boolean copyFile(String oldFileName, String newFileName) {
        return copyFile(new File(oldFileName), new File(newFileName));
    }

    public static boolean copyFile(File oldFile, File newFile) {
        if (oldFile.exists() && oldFile.isFile() && !newFile.exists()) {
            try {
                byte[] bytes = Tools.fileReadByte(oldFile);
                Tools.fileSaveByte(newFile, bytes, false);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static List<String> copyStaticFile(Class<?> clazz) throws Exception {
        List<String> list = new ArrayList<String>();
        String staticDir = "/src/main/resources/templates/static/";
        String path0_1 = Tools.getProjectRoot(Application.class);
        String path0_2 = Tools.getProjectRoot(clazz);
        String path1 = Tools.pathFormat(path0_1 + staticDir);
        String path2 = Tools.pathFormat(path0_2 + staticDir);

        byte[] bytes = Tools.fileReadByte(path0_1 + "/src/main/resources/templates/page/permission.html");
        Tools.fileSaveByte(path0_2 + "/src/main/resources/templates/page/permission.html", bytes, false);
        List<File> list1 = Tools.fileSub(path1, 2);

        new File(path2).mkdirs();
        for (File file : list1) {
            String path3 = Tools.pathFormat(file.getPath());
            path3 = path3.replaceAll(path1, "");
            File file1 = new File(path2 + path3);
            copyFile(file, file1);
        }

        String path01 = Tools.pathFormat(path0_1 + "/src/main/resources/templates/page/js/");
        String path02 = Tools.pathFormat(path0_2 + "/src/main/resources/templates/page/js/");
        List<File> list2 = Tools.fileSub(path01, 2);
        new File(path02).mkdirs();
        for (File file : list2) {
            String path3 = Tools.pathFormat(file.getPath());
            path3 = path3.replaceAll(path01, "");
            File file1 = new File(path02 + path3);
            copyFile(file, file1);
        }
        return list;
    }

    /**
     * 重命名文件
     *
     * @param
     * @return
     */
    public static void fileRename(String oldFileName, String newFileName) {
        File oldFile = new File(oldFileName);
        File newFile = new File(newFileName);
        if (oldFile.exists() && oldFile.isFile()) {
            oldFile.renameTo(newFile);
        }
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 默认为 UTF-8
     */
    public static void fileSaveArray(File file, Object[] str, String encoding) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            for (Object object : str) {
                fos.write((object + "\r\n").getBytes(encoding));
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 默认为 UTF-8
     */
    public static void fileSaveList(File file, List<?> str, String encoding) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            for (Object object : str) {
                fos.write((object + "\r\n").getBytes(encoding));
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 默认为 UTF-8
     *
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void fileSaveString(String file, String str, boolean add) throws UnsupportedEncodingException, IOException {
        fileSaveString(file, str, add, "UTF-8");
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如new File("d:/a.txt")
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 默认为 UTF-8
     *
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void fileSaveString(File file, String str, boolean add) throws UnsupportedEncodingException, IOException {
        fileSaveString(file, str, add, "UTF-8");
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 参数4:编码
     *
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void fileSaveString(String file, String str, boolean add, String encoding) throws UnsupportedEncodingException, IOException {
        fileSaveByte(new File(file), str.getBytes(encoding), add);
    }

    /**
     * 保存字符串数据
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     * 参数4:编码
     *
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public static void fileSaveString(File file, String str, boolean add, String encoding) throws UnsupportedEncodingException, IOException {
        fileSaveByte(file, str.getBytes(encoding), add);
    }


    /**
     * 保存byte数据
     * 参数1:文件地址 例如 "d:/a.txt"
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     *
     * @throws IOException
     */
    public static void fileSaveByte(String file, byte[] bytes, boolean add) throws IOException {
        fileSaveByte(new File(file), bytes, add);
    }

    /**
     * 保存byte数据
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 参数2:要保存的数据
     * 参数3:是否追加,true追加写入,false覆盖写入
     *
     * @throws IOException
     */
    public static void fileSaveByte(File file, byte[] bytes, boolean add) throws IOException {
        FileOutputStream fos = null;
        try {
            fileNew(file);
            fos = new FileOutputStream(file, add);
            fos.write(bytes);
            fos.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }

    }

    /**
     * 文件不存在会重新创建目录并创建文件,如果存在则不作操作,
     * 参数1:文件地址 例如 new File("d:/a.txt")
     */
    public static void fileNew(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    public static void fileMkdirsPath(String path) throws IOException {
        fileMkdirs(new File(path));
    }

    public static void fileMkdirs(File file) throws IOException {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                fileMkdirs(file.getParentFile());
            }
            file.mkdirs();
        }
    }

    public static String toMD5Path(String rootPath, String md5) {
        String path = rootPath
                .replaceAll("\\\\", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/")
                .replaceAll("//", "/");
        path += pathFormat(path);
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
        Tools.fileSaveByte(file2, Tools.fileReadByte(file), false);
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
        Tools.fileSaveByte(file2, bytes, false);
        return page;
    }

    public static String fileSaveResources(byte[] bytes, String rootUrl) throws Exception {
        return fileSaveResources(bytes, rootUrl, "data");
    }

    /**
     * 文件不存在会重新创建目录并创建文件,如果存在则不作操作,
     * 参数1:文件地址 例如 "d:/a.txt"
     */
    public static void fileNew(String file) throws IOException {
        fileNew(new File(file));
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

    /**
     * 读取字符串数据 返回数组 一行一个
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 参数2:编码
     *
     * @throws Exception
     */
    public static String[] fileReadArray(String file, String encoding) throws Exception {
        return fileReadArray(new File(file), encoding, "\r\n");
    }

    /**
     * 读取字符串数据 返回数组 一行一个
     * 参数1:文件地址 例如 new File("d:/a.txt")
     * 默认编码为"UTF-8"
     *
     * @throws Exception
     */
    public static String[] fileReadArray(File file) throws Exception {
        return fileReadArray(file, "UTF-8", "\r\n");
    }

    /**
     * 获取文件MD5
     * 获取文件MD5
     * 参数1:文件地址 例如 new File("d:/a.txt")
     */
    public static String fileToMd5(File file) throws Exception {
        return toMd5(fileReadByte(file));
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

    /**
     * 获取目录下文件和目录名
     * 参数1:目录地址 例如 "d:/a.txt"
     * 参数2:retType 为1 返回全部 目录和文件名.为2返回文件名 为3返回目录名
     *
     * @throws Exception
     */
    public static List<String> fileSubNames(File file, int retType, String suffix) throws Exception {
        List<String> list = new ArrayList<String>();
        File[] files = file.listFiles();
        if (files == null) {
            return list;
        }
        for (File f : files) {
            if (retType == 2) {
                if (f.isFile()) {
                    if (suffix == null || suffix.length() == 0 || suffix.indexOf("*") > -1 || (f.getName().endsWith(suffix))) {
                        list.add(f.getName());
                    }
                }
            } else if (retType == 3) {
                if (f.isDirectory()) {
                    list.add(f.getName());
                }
            } else {
                list.add(f.getName());
            }
        }
        return list;
    }

    public static List<String> fileSubNames(File file, int retType) throws Exception {
        return fileSubNames(file, retType, null);
    }

    public static PagingEntity fileSubNames(String path, int retType, String suffix, int page, int size) throws Exception {
        List<String> list = fileSubNames(new File(path), retType, suffix);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setList(list, page, size);
        return pagingEntity;
    }

    /**
     * 获取目录下文件和目录名  子目录下 也会获取
     * 参数1:目录地址 例如 "d:/a.txt"
     * 参数2:retType 为1 返回全部 目录和文件名.为2返回文件名 为3返回目录名
     *
     * @throws Exception
     */
    public static List<File> fileSub(String path, int retType) throws Exception {
        return fileSub(path, retType, 0, 999999999);
    }

    public static List<File> fileSub(String path, int retType, String suffix) throws Exception {
        return fileSub(path, retType, suffix, 0, 999999999);
    }

    public static List<File> fileSub(String path, int retType, int page, int size) throws Exception {
        return fileSub(path, retType, null, page, size);
    }

    public static PagingEntity fileSubPaging(String path, int retType, String suffix, int page, int size) throws Exception {
        List<File> list = fileSub(path, retType, suffix, 1, 9999);
        PagingEntity pagingEntity = new PagingEntity();
        pagingEntity.setList(list, page, size);
        return pagingEntity;
    }

    public static List<File> fileSub(String path, int retType, String suffix, int page, int size) throws Exception {
        List<String> path_list = new ArrayList<String>();
        List<File> ret_list = new ArrayList<File>();
        path_list.add(path);
        int start = 0;
        if (page >= 1) {
            start = (page - 1) * size;
        }
        int thisNum = 0;
        while (path_list.size() > 0) {
            String path1 = path_list.remove(0);
            File file = new File(path1);
            List<String> list = fileSubNames(file, 2, suffix);
            for (String s : list) {
                if (retType == 2 || retType == 1) {
                    thisNum++;
                    if (thisNum > start) {
                        ret_list.add(new File(path1, s));
                        if (ret_list.size() >= size) {
                            break;
                        }
                    }
                }
            }

            if (ret_list.size() < size) {
                list = fileSubNames(file, 3);
                for (String s : list) {
                    if (retType == 3 || retType == 1) {
                        thisNum++;
                        if (thisNum > start) {
                            ret_list.add(new File(path1, s));
                            if (ret_list.size() >= size) {
                                break;
                            }
                        }
                    }
                    path_list.add(path1 + "/" + s);
                }
            }
            if (ret_list.size() >= size) {
                break;
            }
        }
        return ret_list;
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



