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

import java.io.*;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogThread {

    public static String[] lvNames = new String[]{"trace", "debug", "info ", "warn ", "error"};
    private static final List<ConcurrentLinkedQueue<byte[]>> logQueues = new ArrayList<>();
    public static File[] logFile = new File[]{null, null, null, null, null};
    public static Integer[] lvConfig = new Integer[]{0, 0, 0, 0, 0};
    public static String[] lvColour = new String[]{"\u001B[90m", "\u001B[32m", "\u001B[94m", "\u001B[33m", "\u001B[31m", "\u001B[0m"};
    public static byte[] BYTES_RN = "\r\n".getBytes();
    public static int buff_size = 1024 * 1024;
    public static String lastDeleteDateStr = "";
    public static int days = 30;


    static {
        //加载配置
        loadConfig();
        //初始化 日志队列
        for (int i = 0; i < logFile.length; i++) {
            logQueues.add(new ConcurrentLinkedQueue<>());
        }
        //启动保存线程
        startSave();
        //自动更新线程
        ThreadPool.startService(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    loadConfig();
                    Tools.sleep(3100);
                }

            }
        }, "LogThread.update.config-服务线程");
    }


    public static void deleteOldLogFiles() throws Exception {
        DateTime dateTime = new DateTime();
        String dateStr = dateTime.formatDateTime("yyyy/MM/dd");
        //确保每天运行一次
        if (!lastDeleteDateStr.equals(dateStr)) {
            lastDeleteDateStr = dateStr;
            long time0 = 1000L * 60 * 60 * 24 * days;
            dateTime.operation(-time0);
            for (File logDir : logFile) {
                //只删除 前一天的 如果前边有 说明程序运行中断 不管删除
                String path = logDir.getPath().trim() + File.separator + getDay(dateTime) + ".log";
                File file = new File(path).getParentFile();
                if (file.exists()) {
                    List<File> list =FileTools.subFileAll(file,3);
                    for (File file1 : list) {
                        file1.delete();
                    }
                    list = FileTools.subFileAll(file, 2);
                    for (File file1 : list) {
                        file1.delete();
                    }
                    file.delete();
                }
            }
        }
    }

    public static String getDay(DateTime dateTime) {
        if (dateTime == null) {
            dateTime = new DateTime();
        }
        return dateTime.formatDateTime("yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator + "HH");
    }

    public static void startSave() {
        ThreadPool.startService(new Runnable() {
            @Override
            public void run() {
                int sleep_sec = 5000;
                while (true) {
                    String time0 = getDay(null);
                    for (int i = 0; i < logFile.length; i++) {
                        byte[] buffer = new byte[(int) (buff_size)];
                        int size = 0;
                        while (true) {
                            /// 实现目的 不阻塞读取并删除 如果存在则一直读 或 读到一定长度
                            byte[] bytes = logQueues.get(i).poll();
                            if (bytes != null) {
                                int remainingToCopy = bytes.length; // 这一块 byte[] 还有多少没处理
                                int srcPos = 0;                     // 在 bytes[] 中的起始读取位置

                                while (remainingToCopy > 0) {

                                    int bufferRemainingSpace = buffer.length - size; // buffer 还能放多少
                                    int copyLength = Math.min(remainingToCopy, bufferRemainingSpace); // 本次能复制多少

                                    // 1. 执行复制
                                    System.arraycopy(bytes, srcPos, buffer, size, copyLength);

                                    // 2. 更新 size 和 剩余长度
                                    size += copyLength;
                                    srcPos += copyLength;
                                    remainingToCopy -= copyLength;

                                    // 3. 检查缓冲区是否写满 (或刚好写满)
                                    if (size == buffer.length) {
                                        // 缓冲区满了，写入文件
                                        save(i, time0, buffer, size);
                                        size = 0;
                                    }
                                }

                                // 4. 处理换行符 (只有在 bytes 被完整处理后才添加)
                                // 仅在 buffer 还有空间容纳 BYTES_RN 时添加
                                if (size + BYTES_RN.length < buffer.length) {
                                    System.arraycopy(BYTES_RN, 0, buffer, size, BYTES_RN.length);
                                    size += BYTES_RN.length;
                                } else {
                                    // 如果连换行符都放不下了，先保存当前 buffer，然后在新 buffer 里放换行符
                                    save(i, time0, buffer, size);
                                    size = 0;
                                    // 将换行符放入新的 buffer
                                    System.arraycopy(BYTES_RN, 0, buffer, size, BYTES_RN.length);
                                    size += BYTES_RN.length;
                                }

                            }
                            //跳出循环
                            if (bytes == null) {
                                break;
                            }
                        }
                        save(i, time0, buffer, size);
                    }

                    Tools.sleep(sleep_sec);
                }
            }
        }, "LogThread.save.file-服务线程");
    }

    public static void save(int i, String time0, byte[] data, int size) {
        if (size > 0 && data != null && data.length > 0) {
            String path = logFile[i].getPath().trim() + File.separator + time0;
            File dir = new File(path).getParentFile();

            if (!FileTools.mkdir(dir)) {
                System.err.println("save 创建日志目录失败:" + dir);
            } else {
                path += ".log";
                try (FileOutputStream fos = new FileOutputStream(path, true)) { // true 表示追加模式
                    fos.write(data, 0, size);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void loadConfig() {
        for (int i = 0; i < lvNames.length; i++) {
            lvConfig[i] = Config.getInteger("gzb.log." + lvNames[i].trim(), 0);
            if (logFile[i] == null) {
                logFile[i] = new File(Config.get("gzb.log.path") + lvNames[i].trim());
            }
            if (!logFile[i].exists()) {
                if (!FileTools.mkdir(logFile[i])) {
                    System.err.println("loadConfig 创建日志目录失败:" + logFile[i].getPath());
                }
            }
        }
        days = Config.getInteger("gzb.log.save.days", 30);
        try {
            deleteOldLogFiles();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public LogThread() {

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
            logQueues.get(index).add(msg.getBytes(Config.encoding));
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
        if (log != null) {
            for (int i = 0; i < log.length; i++) {
                if (log[i] == null) {
                    sb.append("null");
                } else if (log[i] instanceof Exception) {
                    sb.append(deduplication(Tools.getExceptionInfo((Exception) log[i]), index));
                } else if (log[i] instanceof Throwable) {
                    sb.append(deduplication(Tools.getExceptionInfo((Throwable) log[i]), index));
                } else {
                    sb.append(Tools.toJson(log[i]));
                }
                if (i < log.length - 1) {
                    sb.append(" | ");
                }
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    public String deduplication(String msg, int index) {
        if (lvConfig[index] == 0 || lvConfig[index] == 3) {
            //控制台输出开启的话 说明是调试目的 就直接输出  避免清空控制台后 不知道发生了什么错误
            return msg;
        } else {
            //不输出的话 说明是 记录到文件 开启归类
            String md5 = Tools.textToMd5(msg);
            int num = Cache.gzbMap.getIncr("异常去重", md5, 60);
            if (num > 1) {
                msg = "异常已出现 " + num + " 次,异常MD5:" + md5;
            } else {
                msg = "异常MD5:" + md5 + "\r\n" + msg;
            }
        }
        return msg;
    }

}
