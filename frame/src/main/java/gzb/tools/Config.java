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

import gzb.tools.thread.ThreadPool;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    public static final String frameName = "gzb.one";
    public static Map<String, String> config = new ConcurrentHashMap<>();
    public static String thisPath = null;
    public static File configFile = null;
    public static long configTime = 0;
    public static Charset encoding = null;

    public static int cpu;
    public static String domain;
    public static int HTTP_PORT;
    public static int TCP_PORT;
    public static int UDP_PORT;
    public static int WS_PORT;
    public static int mainThreadNum;
    public static int ioThreadNum;
    public static int bizThreadNum;
    public static int bizAwaitNum;

    public static int maxPostSize;
    public static int compressionMinSize;
    public static boolean compression;
    public static String codeDir;
    public static String codePwd;
    public static String codeIv;
    public static String staticDir;
    public static String tempDir;
    public static String uploadDir;
    public static String dataBaseCache;
    public static String cacheType;
    public static String sessionType;
    public static Integer sessionTime;

    public static Integer successVal;
    public static Integer failVal;
    public static Integer errorVal;
    public static Integer jumpVal;
    public static String stateName;
    public static String messageName;
    public static String timeName;
    public static String dataName;
    public static String urlName;
    public static String pageName;
    public static String sizeName;
    public static String totalName;

    public static Long version;
    public static Long initTime;

    public static String code_type;
    public static String code_http_ip;
    public static Integer code_http_port;
    public static String code_http_key;
    public static Long code_http_aid;

    public static Long server_name;

    static {
        init();
    }

    public static void update() {

        initTime = Config.getLong("gzb.system.init.time", 0L);
        version = Config.getLong("gzb.system.server.version", 0L);
        server_name = Config.getLong("gzb.system.server.name", 1L);
        code_type = Config.get("gzb.system.code.type", "file");
        code_http_ip = Config.get("gzb.system.code.http.ip", null);
        code_http_port = Config.getInteger("gzb.system.code.http.port", null);
        code_http_key = Config.get("gzb.system.code.http.key", null);
        code_http_aid = Config.getLong("gzb.system.code.http.aid", null);

        domain = Config.get("gzb.system.server.http.domain", "0.0.0.0");
        HTTP_PORT = Config.getInteger("gzb.system.server.http.port", 8080);
        TCP_PORT = Config.getInteger("gzb.system.server.tcp.port", 0);
        UDP_PORT = Config.getInteger("gzb.system.server.udp.port", 0);
        WS_PORT = Config.getInteger("gzb.system.server.ws.port", 0);

         mainThreadNum=getInteger("gzb.system.server.main.thread.num", Math.max(cpu / 10, 1));
        ioThreadNum=getInteger("gzb.system.server.io.thread.num", cpu * 2);
         bizThreadNum=getInteger("gzb.system.server.biz.thread.num", cpu * 2);
        bizAwaitNum = Config.getInteger("gzb.system.server.biz.await.num", cpu * 100);
        maxPostSize = Config.getInteger("gzb.system.server.http.post.size", 1024 * 1024 * 5);
        compressionMinSize = Config.getInteger("gzb.system.server.http.compression.min.size", 1024 * 5);
        compression = Config.getBoolean("gzb.system.server.http.compression", false);
        codeDir = Config.get("gzb.system.code.file.dir", null);
        codePwd = Config.get("gzb.system.code.file.pwd", null);
        codeIv = Config.get("gzb.system.code.file.iv", null);
        staticDir = Config.get("gzb.system.server.http.static.dir", thisPath);
        uploadDir = Config.get("key.system.upload.dir", tempDir);
        cacheType = Config.get("gzb.system.cache.type", "map");
        dataBaseCache = Config.get("gzb.system.db.cache.type", "map");
        sessionType = Config.get("gzb.system.http.session.type", "map");
        sessionTime = Config.getInteger("gzb.system.http.session.time", 3600);

        stateName = Config.get("json.code", "code");
        messageName = Config.get("json.message", "message");
        dataName = Config.get("json.data", "data");
        timeName = Config.get("json.time", "time");
        urlName = Config.get("json.jump", "url");
        pageName = Config.get("json.page", "page");
        sizeName = Config.get("json.size", "size");
        totalName = Config.get("json.total", "total");
        successVal = Config.getInteger("json.success.code", 1);
        failVal = Config.getInteger("json.fail.code", 2);
        errorVal = Config.getInteger("json.error.code", 3);
        jumpVal = Config.getInteger("json.jump.code", 4);
    }

    private static void init() {
        try {
            cpu = Tools.getCPUNum();
            encoding = Charset.forName(System.getProperty("file.encoding", "UTF-8"));
            thisPath = System.getProperty("this.dir", Tools.getProjectRoot());
            tempDir = Config.get("gzb.system.server.http.tmp.dir", System.getProperty("java.io.tmpdir"));
            //更改前提示  虽然不需要了 但是现在也保留吧
            if (thisPath == null || thisPath.isEmpty()) {
                System.err.println("请注意找不到项目根目录,程序已停止,请按照提示操作, \n" +
                        "你必须传递环境变量: this.dir = 你的项目根目录\n" +
                        "或者使用代码获取:\n" +
                        "        System.setProperty(\"file.encoding\", \"" + encoding + "\");\n" +
                        "        System.setProperty(\"this.dir\", Tools.getProjectRoot(你项目中的任意类.class));");
                System.exit(-1314);
            }
            System.out.println(thisPath);
            thisPath = Tools.pathFormat(thisPath);
            tempDir = Tools.pathFormat(tempDir);
            if (configFile == null) {
                File file = new File(thisPath + "/application.properties");
                if (!file.exists()) {
                    file = new File(thisPath + "/src/main/resources/application.properties");
                    if (!file.exists()) {
                        file = new File(thisPath + "/target/classes/application.properties");
                        if (!file.exists()) {
                            System.err.println("配置文件未找到：" + thisPath + "/target/classes/application.properties");
                        }
                    }
                }
                configFile = file;
            }
            load(configFile);
            ThreadPool.startService(new Runnable() {
                @Override
                public void run() {
                    thread = Thread.currentThread();
                    while (true) {
                        try {
                            load(configFile);
                        } catch (Exception e) { //这里不允许出错， 也无法记录日志 因为会循环依赖
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();//响应中断
                            break;
                        }

                    }
                }
            }, "config-auto-update-服务线程");
        } catch (Exception e) {
            System.err.println("程序配置 缓存初始化错误" + Tools.getExceptionInfo(e));
        }
    }

    public static Thread thread = null;
    static String configData;

    public static Map<String, String> toMap(String configFileData) {
        configData = configFileData;
        Map<String, String> map0 = new ConcurrentHashMap<>();
        if (configFileData != null) {
            String[] ss1 = configFileData.split("\n");
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
                        String[] arr1 = n1.split(",");
                        StringBuilder newString = new StringBuilder();
                        for (String string : arr1) {
                            FileTools.mkdir(new File(string.trim()));
                            newString.append(string).append(",");
                        }
                        if (newString.toString().endsWith(",")) {
                            newString.delete(newString.length() - 1, newString.length());
                        }
                        ss2[1] = newString.toString();
                    }
                    if (ss2[1].contains("this:")) {
                        String n1 = ss2[1].replaceAll("this:", thisPath());
                        if (!new File(n1).exists()) {
                            n1 = ss2[1].replaceAll("this:", thisPath());
                        }
                        String[] arr1 = n1.split(",");
                        StringBuilder newString = new StringBuilder();
                        for (String string : arr1) {
                            FileTools.mkdir(new File(string.trim()));
                            newString.append(string).append(",");
                        }
                        if (newString.toString().endsWith(",")) {
                            newString.delete(newString.length() - 1, newString.length());
                        }
                        ss2[1] = newString.toString();
                    }
                    map0.put(ss2[0].trim(), ss2[1]);
                }
            }
        }
        return map0;

    }

    public static void load(File file) throws Exception {
        if (file.exists()) {
            if (configTime == file.lastModified()) {
                return;
            }
            configTime = file.lastModified();
            config = toMap(FileTools.readString(file));
            update();
        } else {
            System.err.println("config 配置文件不存在：" + file.getPath());
        }
    }

    /**
     * 更新配置文件  保留原有格式
     */
    public static void save() {
        Map<String, String> mapConfig = new ConcurrentHashMap<>(config);
        StringBuilder newConfigData = new StringBuilder(configData.length());
        String[] ss1 = configData.split("\n");
        for (String s : ss1) {
            if (s == null || s.length() < 2) {
                continue;
            }
            s = s.trim();
            String[] ss2 = s.split("=", 2);
            if (ss2.length == 2 && ss2[0] != null && ss2[1] != null && !ss2[0].isEmpty() && !ss2[1].isEmpty()) {
                String key = ss2[0];
                String val = mapConfig.remove(key);
                if (val == null) {
                    val = "";
                }
                val = val.trim().replace(thisPath(), "this:");//还原 符号 this: 和
                newConfigData.append(key).append("=").append(val).append("\n");
            } else {
                newConfigData.append(s).append("\n");
            }
        }
        if (mapConfig.size() > 0) {
            newConfigData.append("# 新增配置 -- ").append(new DateTime()).append("\n");
            for (Map.Entry<String, String> stringStringEntry : mapConfig.entrySet()) {
                newConfigData.append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("\n");
            }
        }
        FileTools.save(configFile, newConfigData.toString());
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

    public static Double getDouble(String key, Double defVal) {
        String val = config.get(key);
        if (val == null) {
            return defVal;
        }
        return Double.parseDouble(val);
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
        return tempDir;
    }
}
