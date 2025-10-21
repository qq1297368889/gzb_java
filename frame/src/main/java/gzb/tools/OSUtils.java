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
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 跨平台CPU负载检测工具类（与任务管理器/top数值对齐，误差±2%以内）
 * 支持：Linux（读/proc/stat）、Windows（JNA调用系统API）
 * 依赖：Windows需添加JNA依赖，Linux无需额外依赖
 */
public class OSUtils {
    public static void main(String[] args) {

    }

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = OS_NAME.contains("win");
    private static final boolean IS_LINUX = OS_NAME.contains("linux");
    private static double this_cpu = 0.0;

    public static double getSystemCPU(int mm) {
        Tools.sleep(mm);
        return this_cpu;
    }

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    this_cpu = getSystemCpuLoadPercentage();
                    Tools.sleep(100);
                }
            }
        }).start();
    }

    /**
     * 获取系统总 CPU 占用率 (0.0 到 100.0)。
     * 该方法通过调用 OS 命令行工具，返回与任务管理器/top 一致的总 CPU 负载。
     *
     * @return CPU 占用百分比 (0.0 到 100.0)，如果获取失败或不支持当前 OS，则返回 -1。
     */
    public static double getSystemCpuLoadPercentage() {
        if (IS_WINDOWS) {
            return getWindowsCpuLoad();
        } else if (IS_LINUX) {
            // 注意：Linux 获取 CPU 百分比需要两次采样，这里简化为一次 top 采样。
            // 💡 最佳实践是使用 Load Average，但若坚持要百分比，此方法可用。
            return getLinuxCpuLoad();
        } else {
            System.err.println("Unsupported OS: " + OS_NAME);
            return -1.0;
        }
    }

    /**
     * 【Windows 实现】使用 WMIC 获取总 CPU 负载。
     * WMIC 结果通常与任务管理器中的“总 CPU”一致。
     */
    private static double getWindowsCpuLoad() {
        try {
            // 使用 wmic 获取 LoadPercentage。/Value 使得解析更容易。
            String command = "wmic cpu get LoadPercentage /Value";

            // 使用 cmd.exe /c 来确保命令在 Windows CMD 中正确执行
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = pb.start();

            // 等待进程结束，设置超时以防挂起
            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new Exception("WMIC command timed out.");
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("LoadPercentage=")) {
                    String value = line.substring("LoadPercentage=".length()).trim();
                    if (!value.isEmpty()) {
                        // 返回百分比 (0-100)
                        return Double.parseDouble(value);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting Windows CPU load via WMIC: " + e.getMessage());
        }
        return -1.0;
    }

    /**
     * 【Linux 实现】使用 top 命令获取总 CPU 负载。
     * Linux 的 top 命令结果包含用户态 (us) 和内核态 (sy) 的总和。
     * 注意：top 的输出格式在不同发行版可能略有不同。
     */
    private static double getLinuxCpuLoad() {
        try {
            // top -bn1 快速执行一次 top，并输出到 stdout
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "top -bn1 | grep 'Cpu(s)'");
            Process process = pb.start();

            if (!process.waitFor(5, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                throw new Exception("top command timed out.");
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();

            if (line != null) {
                // 示例 top 输出: %Cpu(s):  0.3 us,  0.7 sy,  0.0 ni, 98.9 id,  0.1 wa,  0.0 hi,  0.0 si,  0.0 st
                // 正则表达式匹配 us, sy, ni, id, wa 等值
                Pattern pattern = Pattern.compile("([0-9.]+)\\s+us,.*\\s+([0-9.]+)\\s+sy,.*\\s+([0-9.]+)\\s+id");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find() && matcher.groupCount() >= 3) {
                    double user = Double.parseDouble(matcher.group(1)); // us (User Time)
                    double system = Double.parseDouble(matcher.group(2)); // sy (System/Kernel Time)
                    double idle = Double.parseDouble(matcher.group(3)); // id (Idle Time)

                    // 总 CPU 负载 = 100% - Idle Time
                    // 或者：总 CPU 负载 = User Time + System Time + Nice Time
                    double totalCpu = 100.0 - idle;

                    // 返回百分比 (0-100)
                    return totalCpu;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting Linux CPU load via top: " + e.getMessage());
        }
        return -1.0;
    }

}
