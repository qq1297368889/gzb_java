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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import gzb.entity.PagingEntity;
import gzb.entity.TableInfo;
import gzb.entity.UploadEntity;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Constant;
import gzb.frame.factory.v4.entity.HttpMapping;
import gzb.frame.netty.entity.Response;
import gzb.start.Application;
import gzb.tools.http.HTTP;
import gzb.tools.img.GifCaptcha;
import gzb.tools.json.JsonSerializable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.Charset;
import java.security.CodeSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Tools {
    private static Map<String, String> humpMap = new HashMap<>();
    public static String[] ss1 = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789".split("|");
    public static Lock lock = new ReentrantLock();
    public static int[] arr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    public static int[] arrCheck = {1, 0, 88, 9, 8, 7, 6, 5, 4, 3, 2};
    public static Random random = new Random(new Date().getTime() + 100);

    public static String getName(String name) {
        if (name == null) {
            return null;
        }
        String nameHump = humpMap.get(name);
        if (nameHump != null) {
            return nameHump;
        }
        String[] arr1 = name.split("_");
        String n = arr1[0];
        for (int i = 1; i < arr1.length; i++) {
            char[] chars = arr1[i].toCharArray();
            for (int i1 = 0; i1 < chars.length; i1++) {
                if (i1 == 0) {
                    n += String.valueOf(chars[i1]).toUpperCase();
                } else {
                    n += String.valueOf(chars[i1]).toLowerCase();
                }
            }
        }
        humpMap.put(name, n);
        return n;
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

    static class ObjectTypeAdapter extends TypeAdapter<Object> {
        private final TypeAdapter<Object> defaultAdapter = new Gson().getAdapter(Object.class);

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            defaultAdapter.write(out, value);
        }

        @Override
        public Object read(JsonReader in) throws IOException {
            JsonToken token = in.peek();
            switch (token) {
                case NUMBER:
                case STRING:
                    return in.nextString();
                case BOOLEAN:
                    return in.nextBoolean();
                case NULL:
                    in.nextNull();
                    return null;
                case BEGIN_ARRAY:
                    in.beginArray();
                    return in;
                case BEGIN_OBJECT:
                    in.beginObject();
                    return in;
                default:
                    return null;
            }
        }
    }

    public static Map<String, Object> jsonToMap(String json) {
        if (json == null) {
            return new HashMap<>();
        }
        // 使用 GsonBuilder 来注册自定义的 TypeAdapter
        return new GsonBuilder()
                .registerTypeAdapter(Object.class, new ObjectTypeAdapter())
                .create().fromJson(json, Map.class);
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
    public static List<Object> createArrayList(Object...objects) {
        if (objects!=null&&objects.length>0) {
            List<Object>list = new ArrayList<>(objects.length);
            for (Object object : objects) {
                list.add(object);
            }
            return list;
        }else{
            return new ArrayList<>();
        }
    }

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        }
        if (obj instanceof Iterable) {
            return iterableToJson((Iterable<?>) obj);
        }
        if (obj.getClass().isArray()) {
            return arrayToJson(obj);
        }
        if (obj instanceof GzbMap) {
            return obj.toString();
        }
        if (obj instanceof Class<?>) {
            return "\"" + obj.toString()+ "\"";
        }
        if (obj instanceof File) {
            return "\"" + obj.toString()+ "\"";
        }
        if (obj instanceof JsonSerializable) {
            return obj.toString();
        }
        if (obj instanceof CharSequence) {
            return "\"" + escapeJsonString(obj.toString()) + "\"";
        }
        if (obj instanceof Character) {
            return "\"" + escapeJsonString(String.valueOf(obj)) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return "\"" + (obj.toString()) + "\"";
        }
        if (obj instanceof Exception) {
            return "\"" + escapeJsonString(getExceptionInfo((Exception) obj)) + "\"";
        }
        try {
            String res = ClassTools.toJsonObject(obj.getClass(), obj, null);
            return res == null ? obj.toString() : res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Helper method to serialize a Map
    public static String mapToJson(Map<?, ?> map) {
        if (map == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append("\"").append(escapeJsonString((entry.getKey().toString()))).append("\":");
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
        StringBuilder sb = new StringBuilder();
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
        int length = Array.getLength(array);
        StringBuilder sb = new StringBuilder();
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

    // Helper method to escape special JSON characters
    public static String escapeJsonString(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
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
        return sb.toString();
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
            ret = new String(out.toByteArray(), Charset.forName(Config.encoding));
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
    public static String textToMd5(String str){
        return toMd5(str.getBytes(Charset.forName(Config.encoding)));
    }

    /**
     * 获取文本MD5
     * 参数1:byte[]
     */
    public static String toMd5(byte[] bytes){
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
     * @param oldFilePath 老文件路径
     * @param newFilePath 新文件路径
     * @return
     */
    public static void fileRename(String oldFilePath, String newFilePath) {
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        if (oldFile.exists() && oldFile.isFile()) {
            oldFile.renameTo(newFile);
        }
    }

    /**
     * 保存字符串数据
     *
     * @param file     文件地址 例如 "d:/a.txt"
     * @param str      要保存的数据
     * @param encoding 是否追加,true追加写入,false覆盖写入
     * @return 默认为 UTF-8
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

    /**
     * 获取目录下文件和目录名
     * 参数1:目录地址 例如 "d:/a.txt"
     * 参数2:retType 为1 返回全部 目录和文件名.为2返回文件名 为3返回目录名
     *
     * @throws Exception
     */
    public static List<String> fileSubNames(File file, int retType, String suffix) throws Exception {
        List<String> list = new ArrayList<String>();
        if (!file.exists() || !file.isDirectory()) {
            return list;
        }
        File[] files = file.listFiles();
        String[] arr1 = null;
        if (suffix != null) {
            arr1 = suffix.split("/");
        }
        if (files == null) {
            return list;
        }
        for (File f : files) {
            if (retType == 2) {
                if (f.isFile()) {
                    if (suffix == null || suffix.length() == 0 || suffix.indexOf("*") > -1) {
                        list.add(f.getName());
                    }
                    if (arr1 != null) {
                        for (String string : arr1) {
                            if (f.getName().endsWith(string)) {
                                list.add(f.getName());
                            }
                        }
                    }

                }
            } else if (retType == 3) {
                if (f.isDirectory()) {
                    list.add(f.getName());
                }
            } else {
                if (arr1 != null) {
                    for (String string : arr1) {
                        if (f.getName().endsWith(string)) {
                            list.add(f.getName());
                        }
                    }
                }
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
        return fileSub(path, retType, 0, 999);
    }

    public static List<File> fileSub(String path, int retType, String suffix) throws Exception {
        return fileSub(path, retType, suffix, 0, 999);
    }

    public static List<File> fileSub(String path, int retType, int page, int size) throws Exception {
        return fileSub(path, retType, null, page, size);
    }

    public static PagingEntity fileSubPaging(String path, int retType, String suffix, int page, int size) throws Exception {
        List<File> list = fileSub(path, retType, suffix, 1, 999);
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

    public static Map<String, String> mapIsReg = new HashMap<>();

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



