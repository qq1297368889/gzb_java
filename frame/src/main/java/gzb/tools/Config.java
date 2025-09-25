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

import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.cache.GzbCacheRedis;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    public static Map<String, String> config = new ConcurrentHashMap<>();
    public static String thisPath = null;
    public static File configFile = null;
    public static long configTime = 0;
    public static String encoding = null;

    public static int cpu;
    public static String domain;
    public static int HTTP_PORT;
    public static int TCP_PORT;
    public static int UDP_PORT;
    public static int WS_PORT;
    public static int threadNum;
    public static int maxAwaitNum;
    public static int maxPostSize;
    public static int compressionMinSize;
    public static boolean compression;
    public static boolean permissionsOpen;
    public static String codeDir;
    public static String codePwd;
    public static String codeIv;
    public static String staticDir;
    public static String tempDir;
    public static String frameDbKey;
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


    public static Log log;
    //默认 缓存 具体实现取决于配置文件
    public static GzbCache cache_gzbCache;
    //session 缓存 具体实现取决于配置文件
    public static GzbCache cache_session;
    //数据库查询 缓存 具体实现取决于配置文件
    public static GzbCache cache_dataBaseCache;
    //全局缓存 是map实现 避免一些对象无法存入redis 并且重启会消失
    public static GzbCache cache_gzbMap;
static{
    init();
}

    public static void update() {
        domain = Config.get("gzb.system.server.http.domain", "0.0.0.0");
        HTTP_PORT = Config.getInteger("gzb.system.server.http.port", 8080);
        TCP_PORT = Config.getInteger("gzb.system.server.tcp.port", 0);
        UDP_PORT = Config.getInteger("gzb.system.server.udp.port", 0);
        WS_PORT = Config.getInteger("gzb.system.server.ws.port", 0);
        threadNum = Config.getInteger("gzb.system.server.http.thread.num", cpu * 2);
        maxAwaitNum = Config.getInteger("gzb.system.server.http.await.num", cpu * 1000);
        maxPostSize = Config.getInteger("gzb.system.server.http.post.size", 1024 * 1024 * 10);
        compressionMinSize = Config.getInteger("gzb.system.server.http.compression.min.size", 1024 * 5);
        compression = Config.getBoolean("gzb.system.server.http.compression", false);
        codeDir = Config.get("gzb.system.code.dir", null);
        codePwd = Config.get("gzb.system.code.pwd", null);
        codeIv = Config.get("gzb.system.code.iv", null);
        permissionsOpen = Config.getBoolean("gzb.system.permissions.open", false);
        staticDir = Config.get("gzb.system.server.http.static.dir", thisPath);
        frameDbKey = Config.get("db.frame.key", "frame");
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
            encoding = System.getProperty("file.encoding", "UTF-8");
            thisPath = System.getProperty("this.dir");
            tempDir = Config.get("gzb.system.server.http.tmp.dir", System.getProperty("java.io.tmpdir"));
            if (thisPath == null || thisPath.isEmpty()) {
                System.err.println("请注意找不到项目根目录,程序已停止,请按照提示操作, \n" +
                        "你必须传递环境变量: this.dir = 你的项目根目录\n" +
                        "或者使用代码获取:\n" +
                        "        System.setProperty(\"file.encoding\", \"" + encoding + "\");\n" +
                        "        System.setProperty(\"this.dir\", Tools.getProjectRoot(你项目中的任意类.class));");
                System.exit(-1314);
            }
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
            load();
            log = new LogImpl();
            //配置读取完毕
            cache_gzbMap = new GzbCacheMap();
            if (Config.cacheType.equals("redis")) {
                cache_gzbCache = new GzbCacheRedis();
            } else {
                cache_gzbCache = new GzbCacheMap(Tools.pathFormat(Config.thisPath() + "/session.cache"));
            }
            if (Config.sessionType.equals("redis")) {
                cache_session = new GzbCacheRedis();
            } else {
                cache_session = new GzbCacheMap();
            }
            if (Config.dataBaseCache.equals("redis")) {
                cache_dataBaseCache = new GzbCacheRedis();
            } else {
                cache_dataBaseCache = new GzbCacheMap();
            }
            new Thread(() -> {
                while (true) {
                    try {
                        Tools.sleep(3000);
                        load();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } catch (Exception e) {
            System.err.println("cache 缓存初始化错误" + Tools.getExceptionInfo(e));
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
            if (configTime == file.lastModified()) {
                return;
            }
            configTime = file.lastModified();
            String[] ss1 = Tools.fileReadArray(file, encoding, "\n");
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
                        Tools.fileMkdirs(new File(n1));
                        ss2[1] = n1;

                    }
                    if (ss2[1].contains("this:")) {
                        String n1 = ss2[1].replaceAll("this:", thisPath());
                        if (!new File(n1).exists()) {
                            n1 = ss2[1].replaceAll("this:", thisPath());
                        }
                        Tools.fileMkdirs(new File(n1));
                        ss2[1] = n1;
                    }
                    if (config.get(ss2[0]) == null) {
                        config.put(ss2[0], ss2[1]);
                        //log.d("config","add",ss2[0] + "=" + config.get(ss2[0]));
                    } else if (!config.get(ss2[0]).equals(ss2[1])) {
                        config.put(ss2[0], ss2[1]);
                        //log.d("config","upd",ss2[0] + "=" + config.get(ss2[0]));
                    }
                }
            }
            update();
        } else {
            System.err.println("config 配置文件不存在：" + file.getPath());
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
        return tempDir;
    }
}
