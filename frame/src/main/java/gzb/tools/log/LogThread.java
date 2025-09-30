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

package gzb.tools.log;

import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.thread.ThreadPool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogThread {
    public static File rootPathFile;
    public static String[] lvNames = new String[]{"trace", "debug", "info ", "warn ", "error"};
    public static List<ConcurrentLinkedQueue<String>> logQueues = new ArrayList<>();
    public static File[] logFile = new File[]{null, null, null, null, null};
    public static Integer[] lvConfig = new Integer[]{0, 0, 0, 0, 0};
    public static String[] lvColour = new String[]{"\u001B[90m", "\u001B[32m", "\u001B[94m", "\u001B[33m", "\u001B[31m", "\u001B[0m"};

    static {
        rootPathFile = new File(Config.thisPath + "/logs/");
        if (!rootPathFile.exists()) {
            if (!rootPathFile.mkdirs()) {
                System.out.println("创建日志目录失败:" + rootPathFile.getPath());
            }
        }
        for (int i = 0; i < logFile.length; i++) {
            logQueues.add(new ConcurrentLinkedQueue<>());
        }
        for (int i = 0; i < logFile.length; i++) {
            if (logQueues.get(i) == null) {
                logQueues.set(i, new ConcurrentLinkedQueue<>());
            }
            int finalI = i;
            ThreadPool.pool.startThread(1, "LogThread." + lvNames[i], new Runnable() {
                @Override
                public void run() {
                    StringBuilder stringBuilder = new StringBuilder();
                    int sleep_sec = 1000;
                    while (true) {
                        try {
                            if (logFile[finalI] != null && logFile[finalI].exists()) {
                                String str = logQueues.get(finalI).peek();
                                if (str != null) {
                                    stringBuilder.append(str).append("\r\n");
                                    logQueues.get(finalI).poll();
                                }
                                if ((str == null && stringBuilder.length() > 0) || stringBuilder.length() >= 1024 * 512) {
                                    String file = logFile[finalI].getPath().trim()
                                            + "/"
                                            + new DateTime().formatDateTime("yyyy-MM-dd_HH")
                                            + ".log";
                                    FileTools.append(new File(file), stringBuilder.toString());
                                    stringBuilder = new StringBuilder();
                                }
                                if (str != null) {
                                    continue;
                                }
                            }
                            Tools.sleep(sleep_sec);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Tools.sleep(1000);
                        }
                    }

                }
            });
        }
        ThreadPool.pool.startThread(1, "LogThread.update.config", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    loadConfig();
                    Tools.sleep(1000);
                }

            }
        });

    }

    public LogThread() {

    }

    public static void loadConfig() {
        for (int i = 0; i < lvNames.length; i++) {
            lvConfig[i] = Config.getInteger("frame.log." + lvNames[i] + ".lv", 0);
            if (logFile[i] == null) {
                logFile[i] = new File(rootPathFile.getPath() + "/" + lvNames[i]);
            }
            //System.out.println(logFile[i]);
            if (!logFile[i].exists()) {
                if (!logFile[i].mkdirs()) {
                    System.out.println("创建日志目录失败:" + logFile[i].getPath());
                }
            }
        }
    }

    public void addLog(int index, Class<?> aClass, Object[] log) {
        int configValue = lvConfig[index];
        String msg = null;
        //   lvConfig.get(index)0 显示但不保存 1 保存但不显示 2 不显示也不保存 3 显示且保存
        if (configValue == 0 || configValue == 1 || configValue == 3) {
            msg = appendLog(index, aClass, log);
        }
        if (configValue == 0 || configValue == 3) {
            System.out.println(lvColour[index] + msg + lvColour[lvColour.length - 1]);
        }
        if (configValue == 1 || configValue == 3) {
            logQueues.get(index).add(msg);
        }
    }


    private String appendLog(int index, Class<?> aClass, Object[] log) {
        Thread currentThread = Thread.currentThread();
        //String threadName = currentThread.getName();
        long threadId = currentThread.getId();
        StringBuilder sb = new StringBuilder();
        sb.append(new DateTime().formatDateTime("yyyy-MM-dd HH:mm:ss.SSS"))
                .append(" ")
                .append(lvNames[index])
                //.append(" ")
                //.append(threadName)
                .append(" ")
                .append(threadId);
        if (aClass != null) {
            sb.append(" ");
            sb.append(aClass.getName());
        } else {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            if (stackTrace.length > 4) {
                int index2 = 4;
                for (int i = index2; i < stackTrace.length; i++) {
                    if (stackTrace[i].getClassName().startsWith("org.codehaus.groovy.vmplugin.v8")) {
                        continue;
                    }
                    if (stackTrace[i].getClassName().startsWith("gzb.tools.log")) {
                        continue;
                    }
                    index2 = i;
                    break;
                }
                sb.append(" ");
                sb.append(stackTrace[index2].getClassName());
                sb.append(".");
                sb.append(stackTrace[index2].getMethodName());
                sb.append("(");
                sb.append(stackTrace[index2].getFileName());
                sb.append(":");
                sb.append(stackTrace[index2].getLineNumber());
                sb.append(")");
                //"gzb.frame.factory.ClassFactory.main(ClassFactory.java:47)"
            }
        }
        sb.append(": ");
        if (log!=null) {
            for (int i = 0; i < log.length; i++) {
                if (log[i] instanceof Exception) {
                    String errMsg = Tools.getExceptionInfo((Exception) log[i]);
                    if (lvConfig[index] == 0 || lvConfig[index] == 3) {
                        //控制台输出开启的话 说明是调试目的 就直接输出  避免清空控制台后 不知道发生了什么错误
                    } else {
                        //不输出的话 说明是 记录到文件 开启归类
                        String md5 = Tools.textToMd5(errMsg);
                        int num = Cache.gzbMap.getInteger("异常去重", md5, 1);
                        Cache.gzbMap.setMap("异常去重", md5, num + 1, 10 * 60);//计次十分钟过期
                        if (num > 1) {
                            errMsg = "异常已出现 " + num + " 次,异常MD5:" + md5;
                        } else {
                            errMsg = "异常MD5:" + md5 + "\r\n" + errMsg;
                        }
                    }
                    sb.append(errMsg);
                } else {
                    sb.append(Tools.toJson(log[i]));
                }
                if (i < log.length - 1) {
                    sb.append(" | ");
                }
            }
        }else{

            sb.append(": null");
        }


  /*      System.out.println("类名: " + element.getClassName() +
                ", 方法名: " + element.getMethodName() +
                ", 文件名: " + element.getFileName() +
                ", 行号: " + element.getLineNumber());*/
        return sb.toString();
    }


}
