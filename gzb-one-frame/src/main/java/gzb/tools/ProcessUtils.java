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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows 进程管理工具类
 * 提供启动、停止应用程序及进程名与PID转换功能
 */
public class ProcessUtils {
    // 进程名与PID映射缓存（提高查询效率）
    private static final Map<String, Long> PROCESS_CACHE = new HashMap<>();
    // 进程信息正则表达式（用于解析 tasklist 输出）
    private static final Pattern PROCESS_PATTERN = Pattern.compile("(.*?)\\s+(\\d+)");

    /**
     * 启动应用程序
     * @param appPath 应用程序路径（如 "notepad.exe" 或完整路径）
     * @return 进程对象
     * @throws IOException 启动失败时抛出
     */
    public static Process start(String appPath) throws IOException {
        return Runtime.getRuntime().exec(appPath);
    }

    /**
     * 通过 PID 停止应用程序
     * @param pid 进程ID
     * @param terminateChildren 是否同时终止子进程
     * @return 是否成功
     */
    public static boolean stop(long pid, boolean terminateChildren) {
        try {
            String command = "taskkill /F " +
                    (terminateChildren ? "/T " : "") +
                    "/PID " + pid;
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 通过进程名停止应用程序
     * @param processName 进程名（如 "notepad.exe"）
     * @return 是否成功
     */
    public static boolean stop(String processName, boolean terminateChildren) {
        try {
            String command = "taskkill /F " +
                    (terminateChildren ? "/T " : "") +
                    "/IM " + processName;
            Process process = Runtime.getRuntime().exec(command);
            return process.waitFor() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 通过进程名获取 PID（支持模糊匹配）
     * @param processName 进程名（如 "notepad.exe"）
     * @return 匹配的第一个进程ID，未找到返回 -1
     */
    public static long nameToId(String processName) {
        // 先从缓存获取
        if (PROCESS_CACHE.containsKey(processName)) {
            return PROCESS_CACHE.get(processName);
        }

        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains(processName)) {
                    Matcher matcher = PROCESS_PATTERN.matcher(line);
                    if (matcher.find()) {
                        String name = matcher.group(1).trim();
                        long pid = Long.parseLong(matcher.group(2));
                        PROCESS_CACHE.put(name, pid);
                        if (name.equalsIgnoreCase(processName)) {
                            return pid;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 从 Process 对象获取 PID（兼容 Java 8 及以下）
     * @param process 进程对象
     * @return PID，失败时返回 -1
     */
    public static long getPid(Process process) {
        try {
            // Java 9+ 直接获取
            if (process.getClass().getMethod("pid") != null) {
                return (long) process.getClass().getMethod("pid").invoke(process);
            }
            // Java 8 反射获取
            Field field = process.getClass().getDeclaredField("handle");
            field.setAccessible(true);
            return (long) field.get(process);
        } catch (Exception e) {
            return -1;
        }
    }

    // 测试示例
    public static void main(String[] args) throws Exception {
        // 启动记事本
        Process notepad = start("notepad.exe");
        long pid = getPid(notepad);
        System.out.println("记事本PID: " + pid);

        // 等待3秒
        Thread.sleep(3000);

        // 通过 PID 停止
        if (pid != -1) {
            stop(pid,true);
            System.out.println("已通过 PID 停止记事本");
        }

        // 启动计算器并通过进程名停止
        start("calc.exe");
        Thread.sleep(1000);
        long calcPid = nameToId("calc.exe");
        if (calcPid != -1) {
            stop(calcPid,true);
            System.out.println("已通过进程名停止计算器");
        }
    }
}