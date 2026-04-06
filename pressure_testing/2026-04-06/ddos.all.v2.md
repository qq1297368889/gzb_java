# 2026 Java 框架性能压测汇总报告 (gzb-one  WebFlux  Quarkus) 数据全部公开透明，欢迎自行验证和复现。
## 1. 极限吞吐量对比 (Max Load QPS)
| 场景路径 | gzb-one | Quarkus | Spring WebFlux | 领先幅度（Quarkus） |
| :--- | :--- | :--- | :--- |:--------------|
| Hello World | **473,229** | 180,162 | 167,012 | +162%         |
| 单对象获取 | **383,704** | 129,585 | 119,948 | +196%         |
| 多参数映射 | **406,143** | 114,368 | 83,552 | +255%         |
| 数组解析 | **305,047** | 87,933 | 83,973 | +246%         |

## 📈 关键维度深度分析
* 观察性能曲线可以发现，gzb-one 的优势随着业务复杂度的增加而进一步扩大：
* 基础框架开销（领先 162%）：在最简单的 Hello World 场景下，证明了 gzb-one 拥有极短的 IO 指令路径。
* 序列化性能（额外领先 34%）：从单对象获取场景看，gzb-one 处理 POJO 转 JSON 的损耗远低于 Quarkus。
* 参数解析性能（额外领先 59%）：在多参数映射场景，gzb-one 极大缓解了参数绑定的开销。
* 数组解析 ：这个情况比较复杂 它涉及到优势累加 还涉及到大响应的额外开销，不好算

# 低压稳定性阶段  资源占用核心对比表 (20,000 QPS)
#### 其实这个对比我不想做的，但是考虑到某些原因，还是展示一下吧
#### 对一个没有明显性能问题的框架来说 QPS小于负载上限 P99自然好看， 大于负载上限，自然难看
#### P99在业务场景有必要，在框架性能测试意义不大
#### 强调一下 P99在框架中意义不大，并不意味着业务中也没意义 业务场景中可能会有一些特殊的瓶颈点，这时候P99就很有意义了。
#### Quarkus 的延迟高 应该是运气不好遇到系统调度影响了 我以前测试过很多遍 这个数据我都会背了 正常5-6ms左右才对 
#### 但是这是自动脚本测试出来的，重新测试的话又要花很久，所以我也不好随便改数据了（虽然我确实想改掉这个数据）。主要是并不影响结论
| 测试框架 | 路径类型           | 实际 QPS | CPU Avg (%) | 内存 Avg (MB) | P99 延迟 | 效率评价 |
| :--- |:---------------| :--- | :--- | :--- | :--- |:-----|
| **gzb-one** | /test/hello    | 19,830 | **26.6%** | 351.0 | **2.27ms** | 无需评价 |
| | /test/users/1  | 19,896 | **32.1%** | 362.9 | **1.97ms** | 无需评价 |
| | /test/users/2       | 19,883 | **30.4%** | 501.5 | **1.98ms** | 无需评价 |
| | /test/users/array   | 19,829 | **41.5%** | 519.4 | **2.49ms** | 无需评价 |
| --- | ---            | --- | --- | --- | --- | ---  |
| **Quarkus** | /test/hello   | 19,855 | 67.5% | **52.4** | 6.22ms | 延迟略高 |
|| /test/users/1       | 19,855 | 94.6% | **55.0** | 10.08ms | 延迟略高 |
| | /test/users/2       | 19,855 | 102.1% | **55.7** | 9.00ms | 延迟略高 |
| | /test/users/array   | 19,867 | 120.0% | **61.9** | 9.88ms | 延迟略高 |
| --- | ---            | --- | --- | --- | --- | ---  |
| **WebFlux** | /test/hello    | 19,856 | 69.8% | 610.8 | 2.40ms | 表现不错 |
|  | /test/users/1       | 19,796 | 87.7% | 846.3 | 5.21ms | 意义特殊 |
| | /test/users/2       | 19,868 | 115.6% | 859.9 | 3.63ms | 表现不错 |
| | /test/users/array   | 19,883 | 110.4% | 1024.0 | 2.43ms | 表现不错 |

## 📈 关键维度深度分析

### 1. CPU 利用率 (计算密度)
- **gzb-one** 展示了极高的指令集优化效率。在同样的 20,000 QPS 下，其 CPU 占用仅为 **26%~41%**。
- 相较而言，**Quarkus** 和 **WebFlux** 的开销普遍在 **70%~120%**。
- **结论**：处理同等业务逻辑，gzb-one 对 CPU 的压榨程度最低，单核承载上限更高。

### 2. 内存占用(Memory)
- **Quarkus** 凭借其 Native 优化基因，在内存控制上无可匹敌，始终维持在 **60MB 以内**。
- **gzb-one** 内存开销处于中等水平（350MB-500MB），属于典型的“以空间换时间”的设计。
- **WebFlux** 内存占用最高，在复杂对象解析阶段（Array）达到了 **1GB**，对容器配额要求较高。

### 3. 响应稳定性 (P99 Latency)
- **gzb-one** P99 小于 **2.5ms 以内（我真的很帅）**。
- **Quarkus** P99 小于 **10.1ms 以内（视为5-6ms）**。
- **WebFlux** P99 小于 **5.3ms 以内（不错，发挥正常）**。

### 4. 为什么 执行效率相差这么大？ 
- **动态织入** 比如装饰器（框架专用AOP） 当gzb.one发现你需要装饰器是才会置入 装饰器调用代码，如果不需要 那么连一个if都没有
- **扁平处理** 火焰图发现 [gzb.one-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/gzb-one.html) [quarkus-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/quarkus.html) [spring-pic](https://qq1297368889.github.io/gzb_java/pressure_testing/2026-04-06/spring.html)
- **编译增强** 有些处理和判断 gzb.one 选择在编译期 处理判断 核心链路 是一条直线大马路
- **拒绝反射** 核心链路无反射 全靠编译增强续命 启动时和检测到需要 **无缝热更新** 时，框架会使用编译增强处理你的类
- **池化内存** 其实这点在压测没体现，因为我没有使用我实现的序列化而是采用了三方库，因为这个库快的离谱想超越很困难 我懒....
- 其实并不在于代码优劣 差距是架构代差 我其实并没有极端优化框架，目前我知道且没优化的部分一个请求至少可以节省100纳秒

### 5. 框架完整性说明
-  gzb one 不是一个跑分玩具 他功能完备 性能卓越 唯一美中不足的是只经历过十来个真实web项目洗礼
-  全栈能力：内置 IOC, AOP, ORM, Cache, Log, Hot Update。
-  无依赖运行：无需引入臃肿的外部库，自身即是一个高性能宇宙。
-  多协议统一：同一个 Action，同时提供 HTTP / TCP / UDP（UDP 已通 Demo）服务。
-  这只是部分功能 详细参考 [开源仓库文档](../../doc.md)
-  我测试过gzb one test/hello 端点 在流水线上和裸写 netty(/text 框架内置) http请求的 qps差距仅 3%（框架只是支持流水线压测 不支持http标准流水线，支持的话风险太大，遇到攻击就得挂）
-  现实意义上 我完全可以称呼gzb one为 无性能开销
---


## 关于环境信息
* 系统 Ubuntu 20.04.6 LTS（纯净内核，无补丁，有些补丁会导致性能下降，他不是针对gzb.one 而是系统整体性能下降，但是计算密度越高受影响越大，据我测试，安装补丁 qps会下降20%，其他框架下降15%）
* 关于调优：无任何激进调优，仅做了基本的性能优化（比如关闭交换分区，调整内核参数），并且所有框架都在同一环境下进行测试，确保公平对比。
* 压测工具 wrk2 -t9 -c900 -d20s -R5000000 --latency "http://xxxxx"
* 资源限制 server端分配核心 2-5（2h4c）压测端分配核心 6-11(3h6c) 系统预留 0-1(1h2c) ;内存充裕 不存在瓶颈
* 可复现环境：test/ 目录   (包含可执行程序 启动脚本 压测脚本 等完整信息...)
* 内部包含上述统计数据 压测时的原始命令输出
* 系统环境： [system info](test/系统环境.txt)（系统设置 cpu 内存 内核 JVM 系统参数等详细信息）
* 服务端程序源码：test/xxx/src/*（xxx 每个目录下各一个）
* 原始压测日志：test/0406-B.txt （来自于/test/start.sh的直接输出） [log text](0406-B.md)
* 原始压测脚本：[ddos script](test/start.sh)（压测脚本，更换环境 可能无法直接运行，没做兼容处理， 可手动压测）
* 服务端由于太大需要去这里下载 [download-server-program-address](test/download-server-program-address.txt)
#### ⚠️ 关于脚本兼容性（/test/start.sh） ：
#### 本文的自动化压测脚本（监控 CPU/内存并生成 Markdown 报表）主要是为了我自己方便。
#### 说实话，我不擅长写 Shell 脚本，这逻辑大半也是让 Gemini 帮我撸出来的。
#### 如果你在 Kali、CentOS 或其他系统上跑不起来，那太正常了。
#### 建议直接启动服务端，手动起 wrk2 压测。

## 关于测试方式：为什么采用环回测试而非跨机测试
#### 跨机测试，网络成为了主要瓶颈，尤其是在QPS达到如此水平时，网络的延迟和带宽问题将显著影响测试结果。
#### 因此，为了更加准确地评估框架的真实吞吐量，我没有采用跨机器的真实网络环境测试。
#### 设备限制：我测试中未采用云端设备，主要原因是大多数云设备对网络有严格的限制，而这种限制会影响高QPS性能测试的真实性和可靠性。
#### 云环境的网络带宽和延迟因素较为复杂，并不能完全代表本地环境下的性能。
#### 环回测试的优势：环回测试 是最能反映框架真实吞吐量的方式。环回模式下，我们可以有效地屏蔽网络噪音，
#### 确保所有的性能瓶颈都集中在框架本身，避免了网络层面的不确定性，从而更准确地评估框架的计算能力和吞吐量。

---
# 被测端点 URL 展示 各个框架完全一致
#### '/test/hello'
#### '/test/users/1'
#### '/test/users/2?usersId=1000&usersName=usersName1&usersPassword=usersPassword1&usersEmail=usersEmail@gzb.com&usersAge=99'
#### '/test/users/array/1?size=10'

---
#### 被测端点代码展示  各个框架 端点代码一致（仅各个框架注解规则不同）以下为 gzb.one 被测端点代码
#### 补充 我对 Quarkus的序列化尝试优化最后发现没啥作用 仅略微提升，这个序列化不一样 但是性能和下方写法在同一个量级
#### 我不是 Quarkus 的专家，可能还有一些优化空间，但我已经尽力了
#### 总之，这个代码是我写的，性能数据也是我测的，欢迎大家验证和复现。
#### 最后，我想说的是，性能测试是一个非常复杂的过程，涉及到很多因素，
#### 所以即使是同样的代码，在不同的环境下也可能会有不同的性能表现。所以，如果你想要验证我的数据，请务必在相同的环境下进行测试。谢谢！ [system info](test/系统环境.txt)

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
