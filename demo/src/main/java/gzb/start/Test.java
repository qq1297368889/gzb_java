package gzb.start;

import gzb.frame.annotation.EntityAttribute;
import gzb.tools.Config;
import gzb.tools.GzbMap;
import gzb.tools.Tools;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Test {
    static {

        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(Test.class));
    }

    public static void main(String[] args) throws Exception {
        //测试前关闭日志输出  配置文件中 frame.log.xx.lv设置为2
      /*  String url="http://192.168.10.101:2082/test/test1?sysUsersAcc=acc&sysUsersPwd=pwd&sysUsersType=222222222222255555555555555555";
         url="http://192.168.10.101:2082/test/test1?sysUsersAcc=acc&sysUsersPwd=pwd&sysUsersType=2";
        url="http://192.168.10.101:2082/test/test1?sysUsersAcc=acc";
        DDOS.start("框架测试 服务器线程1 压测线程6", 6,
                url, 0, null,
                10000 * 50,
                "code".getBytes(Config.encoding));
        */
        Constructor c = SysFile.class.getDeclaredConstructor();
        for (int n = 0; n < 5; n++) {
            StringBuilder sb = new StringBuilder();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000 * 100; i++) {
                SysFile user = new SysFile();
                user.setSysFileMd5(null);
                user.setSysFilePath(null);
                user.setData(null);
                user.setSysFileId(null);
                user.setSysFileTime(null);
                user.setSysFileType(null);
                sb.append(user.toString());
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            StringBuilder sb2 = new StringBuilder();
            Field[] fields = SysFile.class.getDeclaredFields();
            List<Field> list = new ArrayList<>();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                list.add(field);
            }
            fields = list.toArray(new Field[0]);
            long start2 = System.currentTimeMillis();
            for (int i = 0; i < 10000 * 100; i++) {
                Object obj = c.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    field.set(obj, null);
                }
                sb2.append(obj.toString());
            }
            long end2 = System.currentTimeMillis();
            System.out.println(end2 - start2);
            System.out.println("--------------------");
            Tools.sleep(100);
        }
        System.out.println("*********************");
        for (int n = 0; n < 5; n++) {
            StringBuilder sb = new StringBuilder();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000 * 1000; i++) {
                sb.append(new SysFile());
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);

            StringBuilder sb2 = new StringBuilder();
            long start2 = System.currentTimeMillis();
            for (int i = 0; i < 10000 * 1000; i++) {
                sb2.append(c.newInstance());
            }
            long end2 = System.currentTimeMillis();
            System.out.println(end2 - start2);
            System.out.println("--------------------");
            Tools.sleep(100);

        }
        System.out.println("*********************");
        for (int n = 0; n < 5; n++) {
            StringBuilder sb = new StringBuilder();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000 * 1000; i++) {
                SysFile user = new SysFile();
                user.setSysFileMd5(null);
                sb.append(user);
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);

            StringBuilder sb2 = new StringBuilder();
            long start2 = System.currentTimeMillis();
            Method method = SysFile.class.getDeclaredMethod("setSysFileMd5", String.class);
            Object[]arr=new Object[]{null};
            for (int i = 0; i < 10000 * 1000; i++) {
                Object obj=c.newInstance();
                method.invoke(obj, arr);
                sb2.append(obj);
            }
            long end2 = System.currentTimeMillis();
            System.out.println(end2 - start2);
            System.out.println("--------------------");
            Tools.sleep(100);

        }


    }
}


class SysFile implements Serializable, JsonSerializable {
    private static final long serialVersionUID = 1000L;
    private static final String dataName = Config.get("json.entity.data", "data");
    @EntityAttribute(key = true, size = 19, name = "sys_file_id", desc = "sysFileId")
    private java.lang.Long sysFileId;
    @EntityAttribute(key = false, size = 100, name = "sys_file_path", desc = "sysFilePath")
    private java.lang.String sysFilePath;
    @EntityAttribute(key = false, size = 64, name = "sys_file_md5", desc = "sysFileMd5")
    private java.lang.String sysFileMd5;
    @EntityAttribute(key = false, size = 19, name = "sys_file_time", desc = "sysFileTime")
    private java.lang.String sysFileTime;
    @EntityAttribute(key = false, size = 100, name = "sys_file_type", desc = "sysFileType")
    private java.lang.String sysFileType;
    private Object data;

    public SysFile() {
    }

    public SysFile(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysFile(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysFile(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysFile(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet, names);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysFileId != null) {
            sb.append("\"sysFileId\":\"").append(sysFileId).append("\",");
        }
        if (this.sysFilePath != null) {
            sb.append("\"sysFilePath\":\"").append(sysFilePath).append("\",");
        }
        if (this.sysFileMd5 != null) {
            sb.append("\"sysFileMd5\":\"").append(sysFileMd5).append("\",");
        }
        if (this.sysFileTime != null) {
            sb.append("\"sysFileTime\":\"").append(sysFileTime).append("\",");
        }
        if (this.sysFileType != null) {
            sb.append("\"sysFileType\":\"").append(sysFileType).append("\",");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":").append(Tools.toJson(entry.getValue())).append(",");
            }
        } else if (this.data != null) {
            sb.append("\"").append(dataName).append("\":").append(Tools.toJson(this.data)).append(",");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.append("}").toString();
    }

    public Result toJson() {
        Result result = new ResultImpl();
        result.set("sysFileId", sysFileId);
        result.set("sysFilePath", sysFilePath);
        result.set("sysFileMd5", sysFileMd5);
        result.set("sysFileTime", sysFileTime);
        result.set("sysFileType", sysFileType);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result = new ResultImpl(json);
        loadJson(result);
    }

    public void loadJson(Result result) {
        this.sysFileId = result.getLong("sysFileId", null);
        this.sysFilePath = result.getString("sysFilePath", null);
        this.sysFileMd5 = result.getString("sysFileMd5", null);
        this.sysFileTime = result.getString("sysFileTime", null);
        this.sysFileType = result.getString("sysFileType", null);
        Object obj = result.get(dataName, null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }

    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp = null;
        if (names.contains("sys_file_id")) {
            temp = resultSet.getString("sys_file_id");
            if (temp != null) {
                this.sysFileId = java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_file_path")) {
            temp = resultSet.getString("sys_file_path");
            if (temp != null) {
                this.sysFilePath = java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_md5")) {
            temp = resultSet.getString("sys_file_md5");
            if (temp != null) {
                this.sysFileMd5 = java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_time")) {
            temp = resultSet.getString("sys_file_time");
            if (temp != null) {
                this.sysFileTime = java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_type")) {
            temp = resultSet.getString("sys_file_type");
            if (temp != null) {
                this.sysFileType = java.lang.String.valueOf(temp);
            }
        }
    }

    public java.lang.Long getSysFileId() {
        return sysFileId;
    }

    public SysFile setSysFileId(java.lang.Long sysFileId) {
        int size0 = Tools.textLength(sysFileId);
        if (size0 > 19) {
            throw new RuntimeException("SysFile.sysFileId最大长度为:19,实际长度为:" + size0 + ",数据为:" + sysFileId);
        }
        this.sysFileId = sysFileId;
        return this;
    }

    public SysFile setSysFileIdUnsafe(java.lang.Long sysFileId) {
        this.sysFileId = sysFileId;
        return this;
    }

    public java.lang.String getSysFilePath() {
        return sysFilePath;
    }

    public SysFile setSysFilePath(java.lang.String sysFilePath) {
        int size0 = Tools.textLength(sysFilePath);
        if (size0 > 100) {
            throw new RuntimeException("SysFile.sysFilePath最大长度为:100,实际长度为:" + size0 + ",数据为:" + sysFilePath);
        }
        this.sysFilePath = sysFilePath;
        return this;
    }

    public SysFile setSysFilePathUnsafe(java.lang.String sysFilePath) {
        this.sysFilePath = sysFilePath;
        return this;
    }

    public java.lang.String getSysFileMd5() {
        return sysFileMd5;
    }

    public SysFile setSysFileMd5(java.lang.String sysFileMd5) {
        int size0 = Tools.textLength(sysFileMd5);
        if (size0 > 64) {
            throw new RuntimeException("SysFile.sysFileMd5最大长度为:64,实际长度为:" + size0 + ",数据为:" + sysFileMd5);
        }
        this.sysFileMd5 = sysFileMd5;
        return this;
    }

    public SysFile setSysFileMd5Unsafe(java.lang.String sysFileMd5) {
        this.sysFileMd5 = sysFileMd5;
        return this;
    }

    public java.lang.String getSysFileTime() {
        return sysFileTime;
    }

    public SysFile setSysFileTime(java.lang.String sysFileTime) {
        int size0 = Tools.textLength(sysFileTime);
        if (size0 > 19) {
            throw new RuntimeException("SysFile.sysFileTime最大长度为:19,实际长度为:" + size0 + ",数据为:" + sysFileTime);
        }
        this.sysFileTime = sysFileTime;
        return this;
    }

    public SysFile setSysFileTimeUnsafe(java.lang.String sysFileTime) {
        this.sysFileTime = sysFileTime;
        return this;
    }

    public java.lang.String getSysFileType() {
        return sysFileType;
    }

    public SysFile setSysFileType(java.lang.String sysFileType) {
        int size0 = Tools.textLength(sysFileType);
        if (size0 > 100) {
            throw new RuntimeException("SysFile.sysFileType最大长度为:100,实际长度为:" + size0 + ",数据为:" + sysFileType);
        }
        this.sysFileType = sysFileType;
        return this;
    }

    public SysFile setSysFileTypeUnsafe(java.lang.String sysFileType) {
        this.sysFileType = sysFileType;
        return this;
    }

    public List<?> getList() {
        return (List<?>) data;
    }

    public SysFile setList(List<?> data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) data;
    }

    public SysFile setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysFile putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>) this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysFile setData(Object data) {
        this.data = data;
        return this;
    }
}
