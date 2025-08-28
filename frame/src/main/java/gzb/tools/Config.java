package gzb.tools;

import gzb.tools.thread.ThreadPool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    public static String thisPath = null;
    public static File configFile = null;
    public static Map<String, String> config = new LinkedHashMap<>();
    public static String encoding = null;

    static {
        try {
            thisPath = System.getProperty("this.dir");
            if (thisPath == null || thisPath.isEmpty()) {
                System.err.println("请注意找不到项目根目录,程序已停止,请按照提示操作, \n" +
                        "你必须传递环境变量: this.dir = 你的项目根目录\n" +
                        "或者使用代码获取:\n" +
                        "        System.setProperty(\"file.encoding\", \"UTF-8\");\n" +
                        "        System.setProperty(\"this.dir\", Tools.getProjectRoot(你项目中的任意类.class));");
                System.exit(-1314);
            }
            encoding = System.getProperty("file.encoding", "UTF-8");
            System.out.println("system encoding :" + encoding);

            thisPath = Tools.pathFormat(thisPath);
            if (configFile == null) {
                File file = new File(thisPath + "/application.properties");
                if (!file.exists()) {
                    file = new File(thisPath + "/src/main/resources/application.properties");
                    if (!file.exists()) {
                        file = new File(thisPath + "/target/classes/application.properties");
                        if (!file.exists()) {
                            System.out.println("配置文件未找到：" + thisPath + "/target/classes/application.properties");
                        }
                    }
                }
                configFile = file;
            }
            load();
            ThreadPool.pool.execute(new Runnable() {
                public void run() {
                    try {
                        Tools.sleep(1000);
                        load();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void load() throws Exception {
        load(configFile);
    }

    public static void load(String configFilePath) throws Exception {
        load(new File(configFilePath));
    }

    public static void load(File file) throws Exception {
        if (file.exists()) {
            String[] ss1 =Tools.fileReadArray(file,"UTF-8","\n");
            for (String s : ss1) {
                if (s == null || s.length() < 2) {
                    continue;
                }
                s = s.trim();
                s = s.replaceAll(" ", "");
                s = s.replaceAll("\r", "");
                s = s.replaceAll("\t", "");
                s = s.replaceAll("\n", "");
                String[] ss2 = s.split("=", 2);
                if (ss2.length == 2 && ss2[0] != null && ss2[1] != null && !ss2[0].isEmpty() && !ss2[1].isEmpty()) {
                    if (ss2[1].contains("classpath:")) {
                        String n1 = ss2[1].replaceAll("classpath:", thisPath());
                        if (!new File(n1).exists()) {
                            n1 = ss2[1].replaceAll("classpath:", thisPath());
                        }
                        ss2[1] = n1;

                    }
                    if (ss2[1].contains("this:")) {
                        String n1 = ss2[1].replaceAll("this:", thisPath());
                        if (!new File(n1).exists()) {
                            n1 = ss2[1].replaceAll("this:", thisPath());
                        }
                        ss2[1] = n1;
                    }
                    if (config.get(ss2[0]) == null){
                        config.put(ss2[0], ss2[1]);
                        System.out.println("config new :" + ss2[0] + "=" + config.get(ss2[0]));
                    }else if (!config.get(ss2[0]).equals(ss2[1])) {
                        config.put(ss2[0], ss2[1]);
                        System.out.println("config upd :" + ss2[0] + "=" + config.get(ss2[0]));
                    }
                }
            }
        } else {
            System.out.println("配置文件不存在：" + file.getPath());
        }
    }

    public static void save() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> en : config.entrySet()) {
            sb.append(en.getKey()).append("=").append(en.getValue()).append("\n");
        }
        System.out.println(sb.toString());
        Tools.fileSaveString(configFile, sb.toString(), false);
    }

    public static String get(String key, String defVal) {
        String val = config.get(key);
        if (val == null) {
            return defVal;
        }
        return val;
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static Integer getInteger(String key, Integer defVal) {
        String val = config.get(key);
        if (val == null) {
            return defVal;
        }
        return Integer.valueOf(val);
    }

    public static Long getLong(String key) {
        return getLong(key, null);
    }

    public static Long getLong(String key, Long defVal) {
        String val = config.get(key);
        if (val == null) {
            return defVal;
        }
        return Long.valueOf(val);
    }

    public static Boolean getBoolean(String key, Boolean defVal) {
        String val = config.get(key);
        if (val == null) {
            return defVal;
        }
        return Boolean.valueOf(val);
    }

    public static Boolean getBoolean(String key) {
        return getBoolean(key, null);
    }

    public static void set(String key, String val) {
        config.put(key, val);
    }

    public static String del(String key) {
        return config.remove(key);
    }

    public static String thisPath() {
        return thisPath;
    }

    public static String tmpPath() {
        return System.getProperty("java.io.tmpdir") + "/";
    }
}
