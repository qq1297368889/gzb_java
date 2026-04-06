# 这个废弃了 这是第一次压测的数据  因为系统噪音比较多 我重启机器 重新测试了一遍
### gzb-one 性能压测报告汇总
| 阶段 | 时长 | 目标QPS | 实际QPS | P50 | P99 | P99.9 | CPU (Min / Max / Avg) | 内存 (Min / Max / Avg) |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| Warmup | 10s | 50000 | 47176.03 | 1.09ms | 2.64ms | 17.95ms | 60.0 / 73.3 / 65.8 | 441.8MB / 441.8MB / 441.8MB |
| Max_Load | 10s | 600000 | 456172.19 | 740.35ms | 2.68s | 2.84s | 366.7 / 406.7 / 390.8 | 441.8MB / 444.3MB / 443.3MB |
| 0.9_Load | 10s | 410554 | 383220.69 | 1.31ms | 41.92ms | 64.48ms | 300.0 / 333.3 / 321.7 | 444.3MB / 446.8MB / 445.6MB |
| 0.8_Load | 10s | 364937 | 340344.87 | 1.25ms | 26.96ms | 44.77ms | 280.0 / 306.7 / 290.0 | 446.8MB / 449.1MB / 447.7MB |
| 0.7_Load | 10s | 319320 | 301117.96 | 1.21ms | 9.72ms | 32.00ms | 246.7 / 266.7 / 260.0 | 449.1MB / 451.4MB / 449.8MB |
| 0.1_Load | 10s | 45617 | 43415.69 | 0.93ms | 2.45ms | 14.19ms | 66.7 / 73.3 / 69.2 | 451.4MB / 451.4MB / 451.4MB |




# 框架性能极限对比汇总 (Final Summary)
## 测试环境非极端优化环境，Linux 桌面系统默认环境 即可复现
### 1. 极限吞吐量 (High Load Peak)
*测试条件：500 并发 / 60s，展示框架处理能力的上限*

| 框架 | Hello (QPS) | 较 gzb-one 差距 | Array 序列化 (QPS) | 序列化性能损失比 |
| :--- | :--- | :--- | :--- | :--- |
| **gzb-one** | **399,788** | **-** | **219,744** | 45.1% |
| **Quarkus** | 128,278 | ↓ 67.9% | 72,153 | 43.8% |
| **Webflux** | 119,012 | ↓ 70.2% | 70,285 | 41.0% |

### 2. 核心能效比 (Efficiency @ 1.2w QPS)
*测试条件：目标 QPS 12,345，展示处理相同任务消耗的资源*

| 框架 | CPU 占用 (Hello) | CPU 占用 (Array) | 内存 (RSS) | 稳定性 |
| :--- | :--- | :--- | :--- | :--- |
| **gzb-one** | **~26%** | **~38%** | 356MB | **稳定** |
| **Quarkus** | ~62% | ~105% | **55MB** | 波动 |
| **Webflux** | ~63% | ~99% | 720MB | 稳定 |

### 3. 响应时延深度 (Latency Profile @ Low Load)
*单位：毫秒 (ms)，展示框架底噪对延迟的影响*

| 框架 | P50 (Hello) | P99 (Hello) | P99.99 (Hello) | 单位     |
| :--- | :--- | :--- | :--- |:-------|
| **gzb-one** | **0.139** | **0.277** | **0.635** | **毫秒** |
| **Quarkus** | 0.168 | 1.168 | 5.171 | **毫秒** |
| **Webflux** | 0.167 | 0.378 | 4.663 | **毫秒** |

---

### 📊 量化对比结论：

1. **吞吐量维度**：
   在 CPU 同样跑满（~390%）的情况下，**gzb-one** 的有效产出是 Quarkus/Webflux 的 **3.1 - 3.3 倍**。这证实了 gzb-one 的指令执行路径极短，没有无效的算子链调度。

2. **资源成本维度**：
   处理同样的万级流量，gzb-one 仅需 **26%** 的 CPU 动力，而其他框架需要 **60% 以上**。这意味着在云原生环境中，使用 gzb-one 可以节省约 **60% 的计算成本**。

3. **内存确定性**：
   Quarkus 虽然初始内存极低，但随压力增长有 15MB+ 的动态波动；Webflux 内存基数过大（720MB）；只有 **gzb-one** 无论在何种压力下都钉死在 **356MB**。这体现了底层对内存的极致控制，完全消灭了高并发下的 GC 抖动。

4. **架构本质**：
   正如测试数据所示，JSON 序列化的性能损耗比例在各框架中基本一致（40%~45%），这说明 JSON 库是常数瓶颈，而 **框架调度能力** 才是决定 QPS 是 10w 还是 40w 的关键变量。

---

# gzb-one 性能测试报告
*
### 1. 低压场景 (目标：12,345; 并发：240; 时间：60s)
* *内存特征：355MB - 356MB，极致稳定，单核效能极高（仅需 ~26% CPU 即可抗住万级 QPS）*
*
---

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS) |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/test/users/array/1` | 12342.05 | 549.78 | 14806.85 | 0.149 | 0.286 | 3.088 | ~38% | 356MB |
| `/test/users/2`       | 12342.09 | 527.12 | 14627.83 | 0.142 | 0.279 | 2.554 | ~33% | 356MB |
| `/test/users/1`       | 12341.82 | 543.33 | 14914.28 | 0.142 | 0.282 | 1.535 | ~30% | 355MB |
| `/test/hello`         | 12341.91 | 521.49 | 14793.03 | 0.139 | 0.277 | 0.635 | ~26% | 355MB |

### 2. 高压场景 (目标：500,000; 并发：500; 时间：60s)
* *内存特征：352MB - 356MB，无论压力如何增加，内存曲线始终保持直线，几乎无任何波动*

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS) |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/test/users/array/1` | 219744.49 | 30658.36 | 312894.43 | 1.083 | 25.754 | 61.136 | ~390% | 356MB |
| `/test/users/2`       | 328206.40 | 43160.60 | 373070.21 | 0.731 | 23.722 | 52.779 | ~390% | 356MB |
| `/test/users/1`       | 297008.15 | 34461.77 | 364403.90 | 0.811 | 24.878 | 52.915 | ~390% | 353MB |
| `/test/hello`         | 399788.65 | 31890.74 | 422794.45 | 0.611 | 20.689 | 48.548 | ~390% | 352MB |
---
* # Quarkus 性能测试报告

### 1. 低压场景 (目标：12,345; 并发：240; 时间：60s)
* *内存特征：约 55MB，初始占用极低*

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS) |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/test/users/array/1` | 12356.43 | 782.80 | 19270.25 | 0.259 | 3.160 | 5.648 | ~105% | 54MB |
| `/test/users/2`       | 12342.74 | 602.66 | 18969.67 | 0.225 | 2.026 | 5.663 | ~87% | 55MB |
| `/test/users/1`       | 12340.00 | 529.65 | 20253.10 | 0.213 | 1.496 | 5.598 | ~78% | 55MB |
| `/test/hello`         | 12362.36 | 720.83 | 17298.36 | 0.168 | 1.168 | 5.171 | ~62% | 55MB |
*
### 2. 高压场景 (目标：500,000; 并发：500; 时间：60s)
* *内存特征：61MB - 70MB，高压下内存随请求量出现明显波动*

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS) |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/test/users/array/1` | 72153.04 | 8980.67 | 117642.95 | 6.822 | 11.925 | 23.908 | ~390% | 70MB 波动 |
| `/test/users/2`       | 87636.22 | 11908.46 | 149049.25 | 5.606 | 10.568 | 21.473 | ~390% | 65MB 波动 |
| `/test/users/1`       | 94569.09 | 13392.95 | 166121.56 | 5.189 | 10.046 | 17.948 | ~390% | 63MB 波动 |
| `/test/hello`         | 128278.39 | 15773.14 | 214821.85 | 3.751 | 8.349 | 15.648 | ~390% | 61MB 波动 |
---
* ## Spring Webflux 性能测试报告
*
### 1. 低压场景 (目标：12,345; 并发：240; 时间：60s)
* *内存特征：约 720MB，内存基数大*

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS) |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/test/users/array/1` | 12359.88 | 589.81 | 16727.92 | 0.239 | 0.610 | 5.621 | ~99% | 720MB |
| `/test/users/2`       | 12356.85 | 583.55 | 16658.78 | 0.245 | 0.621 | 4.692 | ~99% | 720MB |
| `/test/users/1`       | 12343.28 | 371.61 | 16515.98 | 0.196 | 0.445 | 4.842 | ~73% | 720MB |
| `/test/hello`         | 12364.97 | 730.97 | 17067.86 | 0.167 | 0.378 | 4.663 | ~63% | 720MB |

## 2. 高压场景 (目标：500,000; 并发：500; 时间：60s)
* *内存特征：约 720MB，高压下 整体稳定 users/1 接口 轻微波动*

| 接口路径                  | Mean QPS | Stddev | Max QPS | P50 (ms) | P99 (ms) | P99.99 (ms) | CPU 占用 | 内存 (RSS)   |
|:----------------------| :--- | :--- | :--- | :--- | :--- | :--- | :--- |:-----------|
| `/test/users/array/1` | 70285.49 | 10239.78 | 119249.87 | 6.945 | 18.407 | 37.242 | ~390% | 720MB      |
| `/test/users/2`       | 67826.95 | 10562.88 | 112740.60 | 7.081 | 19.534 | 53.720 | ~390% | 720MB      |
| `/test/users/1`       | 93888.86 | 15318.48 | 163122.69 | 5.156 | 16.254 | 36.749 | ~390% | 720MB 轻微波动 |
| `/test/hello`         | 119012.58 | 14984.35 | 209081.06 | 4.119 | 11.009 | 27.693 | ~390% | 719MB      |

---
# 被测端点 URL 展示 各个框架完全一致
#### '/test/hello'
#### '/test/users/1'
#### '/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
#### '/test/users/array/1?size=10'

---
### 被测端点代码展示  各个框架 端点代码 完全一致，没有任何差别（仅各个框架注解规则不同）
```java
package gzb.start.test;
import gzb.frame.annotation.*;
import gzb.tools.Config;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("test")
@Header(item = {@HeaderItem(key = "Content-Type", val = "application/json")})
public class Test {
    public static final Users users = new Users();
    static {
        users.setUsersId(8215438L);
        users.setUsersName("vpeGb2SNo8Xk");
        users.setUsersPassword("az1Amb2aVJsP");
        users.setUsersEmail("nWV3qO6qW0zZ@gzb.com");
        users.setUsersTime(LocalDateTime.now());
        users.setUsersAge(12);
    }
    public static final byte[] HELLO_WORD = "Hello, World!".getBytes(Config.encoding);
    /// 测试基本性能
    @EventLoop
    @RequestMapping("hello")
    @Header(item = {@HeaderItem(key = "Content-Type", val = "text/html")})
    public Object hello() {
        return HELLO_WORD;
    }
    /// 测试传参性能  + 序列化性能
    @EventLoop
    @RequestMapping("users/1")
    public Object users() {
        return users;
    }
    @EventLoop
    @RequestMapping("users/2")
    public Object users(Long usersId, String usersName, String usersPassword, String usersEmail, Integer usersAge) {
        return new Users(
                usersId == null ? 8215438L : usersId,
                usersName == null ?"vpeGb2SNo8Xk" : usersName,
                usersPassword == null ? "az1Amb2aVJsP": usersPassword,
                usersEmail == null ? "nWV3qO6qW0zZ@gzb.com" : usersEmail,
                usersAge == null ? 12: usersAge,
                null //not time
        );
    }
    /// 序列化性能
    @EventLoop
    @RequestMapping("users/array/1")
    public Object usersArray1(int size) {
        List<Users> usersList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            usersList.add(users);
        }
        return usersList;
    }
}
```
* 资源限制 server端分配核心 2-5（2h4c）压测端分配核心 6-11(3h6c) 系统预留 0-1(1h2c) ;内存不存在瓶颈32G 共享
* 可复现环境：/test/   (包含可执行程序 启动脚本 压测脚本 系统环境详情 JDK CPU 内存 系统 内核 等...)
* 内部包含上述统计数据 压测时的原始命令输出
* cpu 内存负载：/test/xxx/perf_stats.log （xxx 每个目录下各一个）
* oha压测工具原始输出：/test/xxx/result/*.json（xxx 每个目录下各一个）
* 服务端程序源码：/test/xxx/src/*（xxx 每个目录下各一个）
* 粗略统计：/test/xxx/ddos_all （根据压测输出结合cpu内存负载的初步汇总）（xxx 每个目录下各一个）
* 原始压测脚本：/test/start.sh （压测脚本，更换环境 可能无法直接运行，没做兼容处理，复现 可手动压测）

### 复现环境关键约束 (System Environment)
*确保对比在完全公平的物理资源下进行*

* **CPU 分配**：
    * **Server 端**：2 核 (核心 2-5, 2h4c) 绑定，避免核间漂移。
    * **压测端 (oha)**：3 核 (核心 6-11, 3h6c) 确保发压无瓶颈。
    * **系统预留**：1 核 (核心 0-1) 负责内核中断与监控。
* **JVM 参数**：未指定参数，确保 C2 编译完整触发。
* **网络栈**：各框架端点逻辑、数据结构、JSON 序列化对象完全对齐。
