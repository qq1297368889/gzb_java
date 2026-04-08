## 前言
#### 前几天发布了一个帖子，向大家请求怎么压测才能获取更可靠的数据
#### 很高兴 我收到了很多建议  我答应了 我会去做
### 今天 我做完了这些事
#### 1.本次压测 来自于真机 Linux（我给自己的电脑装了双系统）释放真正的性能极限
#### 2.压测工具采用 wrk2  获取更精确的数据
#### 3.进程分别都绑定了不同的核心 减少了 压测数据波动
#### 4.提供了及其详细的系统信息
#### 5.确保本次结论可以被大家复现，提供了 代码+可执行程序+压测脚本
* 这是我无法做到的，并非不愿意去做：
* 有人建议我采用两台服务器压测，但是我的网络无法支撑，因为这是框架吞吐量测试而非业务性能测试，PPS 完全不在同一个量级
---
## Introduction
#### A few days ago, I posted a request for advice on how to obtain more reliable data from load testing.
#### I'm glad I received many suggestions, and I promised to do them.
### Today, I completed the following:
#### 1. This load test was conducted on a real Linux machine (I installed a dual-boot system on my computer) to unleash its true performance limits.
#### 2. The load testing tool used was wrk2 to obtain more accurate data.
#### 3. Processes were bound to different cores to reduce fluctuations in load test data.
#### 4. Extremely detailed system information was provided.
#### 5. To ensure that the results can be reproduced, code, an executable program, and a load testing script were provided.
* This is something I couldn't do, not because I was unwilling:
* Some suggested using two servers for load testing, but my network couldn't support it because this is a framework throughput test, not a business performance test; the PPS are completely different.

---
#### 感谢大家的建议 本次测试可靠性 应该会大幅度提升
---
#### Thank you all for your suggestions. The reliability of this test should be significantly improved.
---

## Java 框架性能压测汇总报告 (gzb-one  WebFlux  Quarkus)
## Java Framework Performance Testing Summary Report (gzb-one WebFlux Quarkus)
> #### **数据全部公开透明，欢迎自行验证和复现**
> #### **All data is publicly available and transparent; you are welcome to verify and reproduce it yourself.**
> #### **以下测试均位非流水线测试**
> #### **The following tests are all non-pipeline tests.**
## 1. 极限吞吐量对比 (Max Load QPS)
## 1. Maximum Throughput Comparison (Max Load QPS)
#### 非 HTTP 流水线测试
#### Non-HTTP Pipeline Testing
| Scene              | gzb-one | Quarkus | Spring WebFlux | Leading margin（Quarkus） |
|:------------------| :--- | :--- | :--- |:--------------|
| Hello World       | **473,229** | 180,162 | 167,012 | +162%         |
| POJO to json      | **383,704** | 129,585 | 119,948 | +196%         |
| parameter mapping | **406,143** | 114,368 | 83,552 | +255%         |
| array to json     | **305,047** | 87,933 | 83,973 | +246%         |

### 性能压测峰值对比汇总 (Max_Load 阶段)

| Scene (测试场景) | gzb-one | Quarkus | Spring WebFlux | Leading margin (vs Quarkus) |
| :--- | :--- | :--- | :--- | :--- |
| **Hello World** | **490,434** | 179,030 | 151,494 | **+173.9%** |
| **POJO to JSON** | **361,864** | 179,147 | 115,170 | **+101.9%** |
| **Parameter Mapping** | **391,682** | 163,356 | 76,696 | **+139.7%** |
| **Array to JSON** | **269,067** | 175,942 | 78,127 | **+52.9%** |

---

#### 统计口径说明：
1. **数据来源**：以上数据均取自各框架对应场景报告中的 `Max_Load` 阶段的 **实际 QPS**。
2. **计算公式**：`Leading margin = (gzb-one - Quarkus) / Quarkus * 100%`。
3. **性能简评**：
    - **gzb-one** 在纯文本（Hello World）场景表现极其强悍，逼近 50W QPS。
    - 在复杂参数映射（Parameter Mapping）中，**gzb-one** 领先 Quarkus 接近 1.4 倍，领先 WebFlux 超过 4 倍。
    - 内存表现上，虽然 **Quarkus** 基础占用极低（~60MB），但 **gzb-one** 在高并发下的吞吐效能优势巨大。

# 低压稳定性阶段  资源占用核心对比表 (20,000 QPS)

| 测试框架 | 路径类型 | 实际 QPS | P50 | P99 | P99.9 | 内存 Avg | 效率评价 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **gzb-one** | /test/hello | 19,883 | **809.00us** | **2.01ms** | **2.40ms** | 409.8MB | nice |
| | /test/users/1 | 19,857 | **870.00us** | **2.23ms** | **2.96ms** | 431.2MB | nice |
| | /test/users/2 | 19,830 | **900.00us** | **2.29ms** | **2.78ms** | 526.0MB | nice |
| | /test/users/array | 19,856 | **880.00us** | **2.25ms** | **3.17ms** | 541.2MB | nice |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **WebFlux** | /test/hello | 19,829 | 1.03ms | 3.50ms | 5.41ms | 526.2MB | nice |
| | /test/users/1 | 19,883 | 880.00us | 2.41ms | 6.03ms | 853.1MB | nice |
| | /test/users/2 | 19,883 | 950.00us | 2.68ms | 10.09ms | 885.2MB | nice |
| | /test/users/array | 19,883 | 960.00us | 2.90ms | 9.20ms | 898.9MB | nice |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **Quarkus** | /test/hello | 19,883 | 880.00us | 5.62ms | 10.25ms | **52.5MB** | - |
| | /test/users/1 | 19,829 | 1.03ms | 6.98ms | 10.74ms | **51.6MB** | - |
| | /test/users/2 | 19,855 | 940.00us | 6.09ms | 10.14ms | **50.5MB** | - |
| | /test/users/array | 19,883 | 880.00us | 4.59ms | 9.60ms | **50.7MB** | - |

---

### 1. 为什么 执行效率相差这么大？ 
- **动态织入** 比如装饰器（框架专用AOP） 当gzb.one发现你需要装饰器是才会置入 装饰器调用代码，如果不需要 那么连一个if都没有
- **扁平处理** 火焰图发现 [gzb.one-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/gzb-one.html) [quarkus-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/quarkus.html) [spring-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/spring.html)
- **编译增强** 有些处理和判断 gzb.one 选择在编译期 处理判断 核心链路 是一条直线大马路
- **拒绝反射** 核心链路无反射 全靠编译增强续命 启动时和检测到需要 **无缝热更新** 时，框架会使用编译增强处理你的类
- **池化内存** 其实这点在压测没体现，因为我没有使用我实现的序列化而是采用了三方库，因为这个库快的离谱想超越很困难 我懒....
- 其实并不在于代码优劣 差距是架构代差 我其实并没有极端优化框架，目前我知道且没优化的部分一个请求至少可以节省100纳秒
 
### 2. 框架完整性说明
-  gzb one 不是一个跑分玩具 他功能完备 性能卓越 唯一美中不足的是只经历过少量真实web项目洗礼
-  全栈能力：内置 IOC, AOP, ORM, Cache, Log, Hot Update。
-  无依赖运行：无需引入臃肿的外部库，自身即是一个高性能宇宙。
-  多协议统一：同一个 Action，同时提供 HTTP / TCP / UDP（UDP 已通 Demo）服务。
-  这只是部分功能 详细参考 [开源仓库文档](https://github.com/qq1297368889/gzb_java/blob/main/doc.md)
-  现实意义上 我完全可以称呼gzb one为 无性能开销
-  其他说明 本次压测额外测试了gzb.one的流水线请求 :
-  test/hello (QPS:220W+) 
-  裸写 netty(/text 框架内置) (QPS:230w+) 框架损耗反映在qps上仅有 < 10%
-  备注：框架只是支持流水线压测 不支持http标准流水线，支持的话风险太大，遇到攻击就GG
 
---
## gzb-one 关于性能与取舍的思考
### 核心哲学：轻装前行，极致爆发
Spring 与 Quarkus 作为先驱者，背负了太多的**历史包袱**以维持庞大的生态兼容；而 **gzb-one** 选择在 2026 年**轻装前行**。
> **他们的目标是兼容一切，而我的理念是再快一点。**
---
### ⚖️ 坦诚的权衡 
在架构设计中，没有免费的午餐。我们必须诚实地面对取舍：
* **性能并非他们的唯一追求**，正如**极端兼容性也并非我的强项**。
* 为了压榨出每一滴硬件性能，我**主动牺牲**了部分通用性与繁琐的抽象。
* **这值得吗？** 答案是肯定的。在毫秒必争的生产环境下，这种“牺牲”是通往极致的必经之路。
---
### 🛠️ 性能与生产力的“平衡点”
**gzb-one** 并非难以触碰的“跑分玩具”，而是一套成体系的**现代化开发平台**：
* **熟悉的开发体验**：延续类 Spring 的编程风格，实现**低门槛迁移**。
* **开发者低代码引擎**：内置轻量级驱动，在极致性能的基础上兼顾开发效率。
* **真·无感热更新**：支持在高 QPS 极压环境下进行**动态逻辑热更**，且 QPS 无明显波动。
---
## gzb-one's Thoughts on Performance and Trade-offs
### Core Philosophy: Lightweight Development, Ultimate Explosive Growth
Spring and Quarkus, as pioneers, carried a heavy **historical burden** to maintain their massive ecosystem compatibility; while **gzb-one** chose to **move forward lightweight** in 2026.
> **Their goal is to be compatible with everything, while my philosophy is to be faster.**
---
### ⚖️ The Trade-off
In architectural design, there is no free lunch. We must honestly face the trade-offs:
* **Performance is not their only pursuit**, just as **extreme compatibility is not my strength**.
* To squeeze every drop of hardware performance, I **voluntarily sacrificed** some generality and cumbersome abstractions.
* **Is it worth it?** The answer is yes. In a production environment where milliseconds matter, this "sacrifice" is the necessary path to ultimate performance.
---
### 🛠️ The "Balance" Between Performance and Productivity
**gzb-one** is not an unattainable "benchmark toy," but a comprehensive **modern development platform**:
* **Familiar Development Experience**: Continuing the Spring-like programming style, enabling **low-barrier migration**.
* **Low-Code Engine for Developers**: Built-in lightweight drivers balance extreme performance with development efficiency.
* **Truly Seamless Hot Updates**: Supports **dynamic logic hot updates** under high QPS stress environments, with no significant QPS fluctuations.
---
## 关于环境信息
* 系统 Ubuntu 20.04.6 LTS（纯净内核，无补丁，有些补丁会导致性能下降，他不是针对gzb.one 而是系统整体性能下降，但是计算密度越高受影响越大，据我测试，安装补丁 qps会下降20%，其他框架下降15%）
* 关于调优：无任何激进调优，仅做了基本的性能优化（比如关闭交换分区，调整内核参数），并且所有框架都在同一环境下进行测试，确保公平对比。
* 压测工具 wrk2 -t9 -c900 -d20s -R5000000 --latency "http://xxxxx"
* 资源限制 server端分配核心 2-5（2h4c）压测端分配核心 6-11(3h6c) 系统预留 0-1(1h2c) ;内存充裕 不存在瓶颈
* 可复现环境：test/ 目录   (包含可执行程序 启动脚本 压测脚本 等完整信息...)
* 内部包含上述统计数据 压测时的原始命令输出
* 系统环境： [system info](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/系统环境.txt)（系统设置 cpu 内存 内核 JVM 系统参数等详细信息）
* 服务端程序源码：test/xxx/src/*（xxx 每个目录下各一个）
* 原始压测日志：test/0406-B.txt （来自于/test/start.sh的直接输出） [log text](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/0406-B.md)
* 原始压测脚本：[ddos script](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/start.sh)（压测脚本，更换环境 可能无法直接运行，没做兼容处理， 可手动压测）
* 完整test 目录下载： [download-server-program-address](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/download-server-program-address.txt)
#### ⚠️ 关于脚本兼容性（/test/start.sh） ：
#### 本文的自动化压测脚本（监控 CPU/内存并生成 Markdown 报表）主要是为了我自己方便。
#### 说实话，我不擅长写 Shell 脚本，这逻辑大半也是让 Gemini 帮我撸出来的。
#### 如果你在 Kali、CentOS 或其他系统上跑不起来，那太正常了。
#### 建议直接启动服务端，手动起 wrk2 压测。
---
## Environment Information
* System: Ubuntu 20.04.6 LTS (Clean kernel, no patches. Some patches can cause performance degradation; this isn't specific to gzb.one but rather a decrease in overall system performance, but the higher the compute density, the greater the impact. According to my tests, installing patches reduces QPS by 20%, and other frameworks by 15%)
* Regarding tuning: No aggressive tuning was performed; only basic performance optimizations were done (such as disabling swap and adjusting kernel parameters). All frameworks were tested in the same environment to ensure fair comparison.
* Load testing tool: wrk2 -t9 -c900 -d20s -R5000000 --latency "http://xxxxx"
* Resource limitations: Server-side allocation: 2-5 cores (2h4c); Load testing end allocation: 6-11 cores (3h6c); System reserved: 0-1 cores (1h2c); Ample memory, no bottlenecks.
* Reproducible environment: test/ directory (contains executable program, startup script, load testing script, etc.)
* Contains the above statistics and raw command output during load testing.
* System environment: [system info](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/system environment.txt) (detailed information on system settings, CPU, memory, kernel, JVM, system parameters, etc.)
* Server-side program source code: test/xxx/src/* (one in each of the xxx directories)
* Raw load testing log: test/0406-B.txt (direct output from /test/start.sh) [log] [text](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/0406-B.md)
* Original load testing script: [ddos script](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/start.sh) (Load testing script; may not run directly in different environments due to lack of compatibility handling. Manual load testing is recommended.)
* Download the complete test directory: [download-server-program-address](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/download-server-program-address.txt)
#### ⚠️ Regarding script compatibility (/test/start.sh):
#### The automated load testing script in this article (monitoring CPU/memory and generating Markdown reports) is mainly for my own convenience.
#### To be honest, I'm not good at writing shell scripts; most of the logic was written by Gemini.
#### If it doesn't run on Kali, CentOS, or other systems, that's perfectly normal.
#### It's recommended to start the server directly and manually start wrk2 for load testing.
---

## 关于测试方式：为什么采用环回测试而非跨机测试
#### 跨机测试，网络成为了主要瓶颈，尤其是在QPS达到如此水平时，网络的延迟和带宽问题将显著影响测试结果。
#### 因此，为了更加准确地评估框架的真实吞吐量，我没有采用跨机器的真实网络环境测试。
#### 设备限制：我测试中未采用云端设备，主要原因是大多数云设备对网络有严格的限制，而这种限制会影响高QPS性能测试的真实性和可靠性。
#### 云环境的网络带宽和延迟因素较为复杂，并不能完全代表本地环境下的性能。
#### 环回测试的优势：环回测试 是最能反映框架真实吞吐量的方式。环回模式下，我们可以有效地屏蔽网络噪音，
#### 确保所有的性能瓶颈都集中在框架本身，避免了网络层面的不确定性，从而更准确地评估框架的计算能力和吞吐量。
## About Testing Methodology: Why Use Loopback Testing Instead of Cross-Machine Testing
#### In cross-machine testing, the network becomes the main bottleneck, especially at such QPS levels. Network latency and bandwidth issues will significantly affect the test results.
#### Therefore, to more accurately evaluate the framework's true throughput, I did not use cross-machine real-world network environment testing.
#### Device Limitations: I did not use cloud devices in my tests, primarily because most cloud devices have strict network limitations, which would affect the realism and reliability of high QPS performance tests.
#### Network bandwidth and latency factors in cloud environments are more complex and cannot fully represent performance in local environments.
#### Advantages of Loopback Testing: Loopback testing is the most accurate way to reflect the framework's true throughput. In loopback mode, we can effectively shield network noise,
#### ensuring that all performance bottlenecks are concentrated within the framework itself, avoiding network-level uncertainties, thus more accurately evaluating the framework's computing power and throughput.


---
# http test URL
#### '/test/hello'
#### '/test/users/1'
#### '/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
#### '/test/users/array/1?size=10'

---
#### 被测端点代码展示  各个框架 端点代码一致（仅各个框架注解规则不同）以下为 gzb.one 被测端点代码
#### 总之，这个代码是我写的，性能数据也是我测的，欢迎大家验证和复现。
#### 最后，我想说的是，性能测试是一个非常复杂的过程，涉及到很多因素，
#### 所以即使是同样的代码，在不同的环境下也可能会有不同的性能表现。所以，如果你想要验证我的数据，请务必在相同的环境下进行测试。谢谢！ [system info](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/系统环境.txt)

#### Tested Endpoint Code Display The endpoint code is consistent across frameworks (only the annotation rules differ). Below is the tested endpoint code for gzb.one.
#### In short, this code is mine, and the performance data is mine. Everyone is welcome to verify and reproduce it.
#### Finally, I want to say that performance testing is a very complex process involving many factors.
#### Therefore, even the same code may exhibit different performance in different environments. So, if you want to verify my data, please be sure to test it in the same environment. Thank you! [system info](https://github.com/qq1297368889/gzb_java/tree/main/pressure_testing/2026-04-06/test/系统环境.txt)

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
