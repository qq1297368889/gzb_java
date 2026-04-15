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
import gzb.tools.thread.GzbThreadLocal;
import gzb.tools.thread.ServiceThread;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogThread {
    public static final LogThread logThread = new LogThread();
    private static final List<ConcurrentLinkedQueue<byte[]>> logQueues = new ArrayList<>();
    public static File[] logFile = new File[]{null, null, null, null, null};
    public static byte[] BYTES_RN = "\r\n".getBytes();
    public static int buff_size = 1024 * 1024;
    public static void installHOOK() {
        //System.setOut(new HOOK(1));
        //System.setErr(new HOOK(4));
    }
    static {
        installHOOK();
        //加载配置
        LogConfig.loadConfig(logFile);
        //初始化 日志队列
        for (int i = 0; i < logFile.length; i++) {
            logQueues.add(new ConcurrentLinkedQueue<>());
        }
        //启动保存线程
        startSave();
        //启动服务线程
        ServiceThread.start("LogThread.read-config", () -> {
            while (true) {
                try {
                    LogConfig.loadConfig(logFile);
                } catch (Exception e) {
                    Log.log.e("LogThread.loadConfig", e);
                }
                try {
                    Thread.sleep(3005);
                } catch (InterruptedException e) {
                    e.printStackTrace();//响应中断
                    break;
                }
            }
        });
    }

    public static void startSave() {
        ServiceThread.start("LogThread.save.file-服务线程", new Runnable() {
            @Override
            public void run() {
                int sleep_sec = 5000;
                while (true) {
                    String time0 = LogConfig.getDay(null);
                    for (int i = 0; i < logFile.length; i++) {
                        byte[] buffer = new byte[(int) (buff_size)];
                        int size = 0;
                        while (true) {
                            byte[] bytes = logQueues.get(i).poll();
                            if (bytes != null) {
                                int remainingToCopy = bytes.length; // 这一块 byte[] 还有多少没处理
                                int srcPos = 0;                     // 在 bytes[] 中的起始读取位置

                                while (remainingToCopy > 0) {

                                    int bufferRemainingSpace = buffer.length - size; // buffer 还能放多少
                                    int copyLength = Math.min(remainingToCopy, bufferRemainingSpace); // 本次能复制多少

                                    System.arraycopy(bytes, srcPos, buffer, size, copyLength);

                                    size += copyLength;
                                    srcPos += copyLength;
                                    remainingToCopy -= copyLength;

                                    if (size == buffer.length) {
                                        save(i, time0, buffer, size);
                                        size = 0;
                                    }
                                }

                                if (size + BYTES_RN.length < buffer.length) {
                                    System.arraycopy(BYTES_RN, 0, buffer, size, BYTES_RN.length);
                                    size += BYTES_RN.length;
                                } else {
                                    save(i, time0, buffer, size);
                                    size = 0;
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
        });
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


    public LogThread() {

    }

    public void addLog(int index, Class<?> aClass, Object[] log) {
         final GzbThreadLocal.Entity ENTITY = GzbThreadLocal.context.get();
        int configValue = LogConfig.lvConfig[index];
        String msg = null;
        //   lvConfig.get(index)0 显示但不保存 1 保存但不显示 2 不显示也不保存 3 显示且保存
        if (configValue == 0 || configValue == 1 || configValue == 3) {
            msg = appendLog(index, aClass, log);
        }
        if (configValue == 0 || configValue == 3) {
            int index0=ENTITY.stringBuilderCacheEntity.open();
            StringBuilder sb=ENTITY.stringBuilderCacheEntity.get(index0);
            try {
                HOOK.out0.println(sb.append(LogConfig.lvColour[index]).append(msg).append(LogConfig.lvColour[LogConfig.lvColour.length - 1]).toString());
            }finally {
                ENTITY.stringBuilderCacheEntity.close(index0);
            }
        }
        if (configValue == 1 || configValue == 3) {
            logQueues.get(index).add(msg.getBytes(Config.encoding));
        }
    }


    private String appendLog(int index, Class<?> aClass, Object[] log) {
        Thread currentThread = Thread.currentThread();
        //String threadName = currentThread.getName();
        long threadId = currentThread.getId();
        GzbThreadLocal.Entity entity0 = GzbThreadLocal.context.get();
        int index0 = entity0.stringBuilderCacheEntity.open();
        StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
        try {
            sb.append(new DateTime().formatDateTime("yyyy-MM-dd HH:mm:ss.SSS"))
                    .append(" ")
                    .append(LogConfig.lvNames[index])
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
        } finally {
            entity0.stringBuilderCacheEntity.close(index0);
        }

    }

    public String deduplication(String msg, int index) {
        if (LogConfig.lvConfig[index] == 0 || LogConfig.lvConfig[index] == 3) {
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
