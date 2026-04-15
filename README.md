#  gzb one

## 简介 / Introduction

这是一个**全栈/全功能框架**、**高性能**的 Java Web 框架。它的核心理念是用最少的概念，解决开发者 90% 的问题，同时保留 10% 的底层控制力，以应对**极致性能**需求。它从头开始构建，旨在消除传统框架的臃肿和性能开销。

This is a **full-stack / full-featured**, **high-performance** Java Web framework. Its core philosophy is to solve 90% of developer problems with the least concepts, while retaining 10% of low-level control to meet **extreme performance** requirements. It is built from scratch to eliminate the bloat and performance overhead of traditional frameworks.

这不仅是一个 Web 框架，它还是一个**一站式解决方案**，一个强大的**低代码平台**。

It’s not just a Web framework, but also an **all-in-one solution**, a powerful **low-code platform**.

### 告别重启！框架允许 您在系统满负载下实现无缝热更新，无需停机。

Say goodbye to restarts! The framework allows seamless hot updates under full load, with no downtime.

## 关于性能 / Performance Report
#### [性能报告-单线程服务端 QPS 70万+](pressure_testing/2026-03-31-A.md)  
#### [Performance Report - Single-threaded Server QPS 700,000+](pressure_testing/2026-03-31-A.md)
#### 性能报告-gzb.one vs spring vs quarkus  [中文](pressure_testing/2026-04-08/v1.md) - [English](pressure_testing/2026-04-08/v1.en.md)

### gzb-one 的诞生史 (The Birth of gzb-one)

> * **gzb-one 是我从 2017 年开始，从初学者一步一步走过来的 (Started in 2017 as a beginner, progressing step by step).**
> * **一边进行业务开发，一边进行封装，逐步演化。几乎它的每一个功能，都是为了解决某个业务问题 (Evolved through real-world business development to solve practical problems).**
> **gzb-one 的进化史 (The Evolution Path):**
> * 基于 **Struts2 + Spring + Hibernate** （"框架"雏形出现，初步封装 (Prototype emerged)）
> * 基于 **Spring MVC + Spring + Hibernate** （进行改进，听说 Spring MVC 更好用 (Improved with Spring MVC)）
> * 基于 **Spring Boot + Spring MVC + Spring + Hibernate** （可以打包成 JAR 了，可以独立运行了 (Standalone JAR packaging)）
> * 基于 **Spring Boot + Spring MVC + Spring + MyBatis** （听说 MyBatis 更好用 (Switching to MyBatis)）
> * **我开始关注性能，因为爱好。写代码是我的娱乐活动 (Focusing on performance as a hobby/entertainment).**
> * 基于 **Spring Boot + 自研 WEB 核心 + 自研 ORM + 自研 IOC** （感觉 MyBatis 不好用，但 Hibernate 太重，我选择自研 ORM (Built my own Web/ORM/IOC)）
> * **写 Entity/DAO 这些模板代码太痛苦了** （新增根据数据库逆向生成全套代码 (Added reverse engineering/code generation)）
> * 基于 **Spring Boot + Servlet + Groovy + 自研 WEB 核心 + 自研 ORM** （我可以热更新了！完美应对客户改来改去的烦恼 (Hot-swapping achieved for changing requirements)）
> * **我意外的发现，热更新更大的好处在于开发过程中。以前无法想象的便利，所见即所得，代码写下去立马看到执行结果 (Accidentally discovered the WYSIWYG development experience).**
> * 基于 **Tomcat.jar + Groovy + 自研 WEB 核心 + 自研 ORM** （我长大了，要独立了。再见 Spring (Independent phase, Goodbye Spring)）
> * **有性能提升，但是差别不大 (Performance improved, but < 100%)**
> * 基于 **Netty + Groovy + 自研 WEB 核心 + 自研 ORM** （似乎 Netty 性能更好一点 (Switching to Netty for better performance)）
> * **巨大的性能提升，我开始研究性能优化 (Massive performance boost, > 100%)**
> * 基于 **Netty + 自定义类加载器 + 自研 WEB 核心 + 自研 ORM** （Groovy 性能损耗有点不能接受，虽然它已经很接近 Java 了 (Removing Groovy overhead)）
> * 基于 **Netty + 自定义类加载器 + 自研 WEB 核心 + 自研 ORM + 自研 LOG + 等等...** （**gzb-one 完全体已经出现了 (The complete form of gzb-one)**）
> * **逐渐的，在优化的过程中，计时单位逐渐变成了：纳秒 (The unit of measurement became: Nanoseconds).**
> * **非常多我想做的功能和优化，但是精力不足，毕竟这是我独立开发的 (Many ideas, limited energy for solo development).**
> * **持续中... 但是迟早那些想做还没做的，都会出现在 gzb-one 中 (Ongoing... everything envisioned will eventually be built).**
 
## 核心特性 / Key Features

* **性能至上** / **Performance First**
  * **动态编译**：支持对 `.java` 文件的动态编译，代码修改后**实时生效**，无需重启应用。
  * **Dynamic Compilation**: Supports dynamic compilation of `.java` files, with **real-time effect** after code modification, without restarting the application.
  * **远程动态更新**：支持框架运行时从外部调用框架API进行热更新（框架内置HTTP API 或 自定义开发者实现）
  * **Remote Dynamic Updates**: Supports remote hot updates via framework API (built-in HTTP API or custom developer implementation).
  * **极致自实现**：核心仅依赖少量类库，避免了不必要的依赖。
  * **Extreme Self-Implementation**: The core relies on only a few libraries, avoiding unnecessary dependencies.
  * **异步 SQL 智能合并**：持久层支持**异步 JDBC**，并能智能合并 SQL 操作，显著提升数据库交互效率。
  * **Asynchronous SQL Smart Merging**: Persistence layer supports **asynchronous JDBC**, and intelligently merges SQL operations, significantly improving database interaction efficiency.
  * **查询 SQL 折叠**：在高并发环境下，针对相同的 SQL 指令与参数，可以开启“防击穿”保护。在一定时间内，相同的查询只会产生 1 次 真实的数据库调用。
  * **SQL Query Folding**: In high concurrency environments, similar SQL instructions with identical parameters can trigger “protection against overload”. The same query will result in only **one real database call** within a specified time.

* **功能大一统** / **Unified Functionality**
  * **持久层（ORM）**：既是一个完全面向对象的 ORM 框架，也支持手写 SQL，让开发者可以自由选择。
  * **Persistence Layer (ORM)**: It is both a fully object-oriented ORM framework and supports handwritten SQL, giving developers the freedom to choose.
  * **控制器（Controller）**：可以无缝地将 Java 函数映射到 HTTP 端点。
  * **Controller**: Seamlessly maps Java functions to HTTP endpoints.
  * **依赖注入（IOC）**：支持将 `@Service` 组件动态注入到 `@Resource` 注解的字段，提供了简洁的依赖管理。
  * **Dependency Injection (IOC)**: Supports dynamic injection of `@Service` components into `@Resource` annotated fields, providing simple dependency management.
  * **装饰器（AOP）**：可以轻松拦截或修改 HTTP 请求前后，实现日志、权限等横切关注点。
  * **Decorators (AOP)**: Easily intercept or modify HTTP requests before and after, implementing cross-cutting concerns like logging and permission management.

* **低代码能力** / **Low-Code Capabilities**
  * **全自动代码生成**：根据数据库表信息，一键生成所有实体类、DAO 层代码及对应的 Web 界面。
  * **Automatic Code Generation**: Automatically generates all entity classes, DAO layer code, and corresponding web interfaces based on database table information.
  * **开箱即用**：提供了一整套工具，让你不仅可以快速开发，更能快速部署和运行。
  * **Out-of-the-box**: Provides a full set of tools to quickly develop, deploy, and run your applications.

* **多模态端点支持** / **Multi-modal Endpoint Support**
  * **HTTP 支持**：基于注解扫描，服务接口，适配标准 Web访问。
  * **HTTP Support**: Service interfaces are adapted to standard Web access through annotation scanning.
  * **TCP 支持**：自研极简协议栈，支持 size,data 格式，彻底解决跨语言（易语言、C#、Node.js 等）对接中的字节序与粘包痛点。
  * **TCP Support**: Custom-built lightweight protocol stack, supporting size/data format, solving byte-order and packet-stickiness issues for cross-language integrations (e.g., Easy Language, C#, Node.js).
  * **UDP 支持**：(Coming Soon) 预留高性能 UDP 映射接口，支持高频、低延迟状态包分发。
  * **UDP Support**: (Coming Soon) Reserved high-performance UDP mapping interface, supporting high-frequency, low-latency state packet distribution.
  * **极致解耦**：协议层仅作为“流量入口”，真正做到协议无感。
  * **Extreme Decoupling**: The protocol layer serves only as a “traffic entry”, truly achieving protocol transparency.
  * **逻辑归一**：开发者只需编写一套 Controller 代码，即可通过注解配置，让该逻辑同时在 HTTP、TCP、UDP 端口生效。
  * **Unified Logic**: Developers only need to write a single set of Controller code, which can be configured via annotations to work on HTTP, TCP, and UDP ports simultaneously.
  * **无感切换**：客户端可根据网络状况灵活切换协议（如：瞬时指令走 UDP，复杂数据走 TCP），后端逻辑层完全无感知，共享同一套内存池与缓存状态。
  * **Seamless Switching**: Clients can switch protocols flexibly based on network conditions (e.g., instant commands via UDP, complex data via TCP), with the backend logic layer completely unaware, sharing the same memory pool and cache state.

---

## 理念与愿景 / Philosophy & Vision

#### 传统框架虽然提供了便利，但往往以牺牲性能和控制力为代价。
#### 本框架则代表了一种不同的理念：通过消除不必要的运行时抽象，优先考虑原始速度和效率 。
#### 它不仅仅是一个框架，更是你应用程序的**高性能引擎**。
#### Traditional frameworks provide convenience, but often at the cost of performance and control.
#### This framework represents a different philosophy: eliminating unnecessary runtime abstractions, prioritizing raw speed and efficiency.
#### It is not just a framework, but the **high-performance engine** of your application.

---

## 快速上手指南 / Quick Start Guide

### 本指南将帮助你从零开始，快速启动并运行本框架。
#### This guide will help you quickly get started with the framework from scratch.

#### 项目模板：项目根目录的 gzb.one.zip  项目模板包含一切依赖无需网络下载
#### Project Template: The gzb.one.zip template at the project root contains all dependencies, no need for online downloads.

### 创建基础项目 / Create Basic Project
1. **创建项目**：创建maven项目  
   **Create Project**: Create a Maven project
2. **获取项目模板**：下载项目模板 覆盖到maven项目  
   **Get Project Template**: Download and overwrite the template in your Maven project
3. **初始化项目**：更新 pom 依赖  
   **Initialize Project**: Update pom dependencies
4. **配置文件**：在 application.properties 修改服务器端口 或保持默认 gzb.system.server.http.port=2080  
   **Configuration File**: Modify server port in application.properties or keep default gzb.system.server.http.port=2080
5. **启动程序**：运行 gzb.start.Start 如无报错 显示 port xx 说明启动成功  
   **Start Program**: Run gzb.start.Start. If no errors appear and port xx is displayed, the startup is successful.
6. **HTTP**：ip:port/text 显示 hello word  
   **HTTP**: ip:port/text shows hello word
7. **TCP**：参考提供的cache sdk 内部实现了基于 TCP的私有协议通讯  
   **TCP**: Refer to the provided cache SDK which implements private TCP protocol communication
8. **UDP**：组包逻辑未开发完成 不过可以根据tcp私有协议 转化为udp 即可访问  
   **UDP**: Packet packing logic is not developed yet, but it can be converted from TCP private protocol to UDP for access.
9. **WS**：暂未实现  
   **WS**: Not implemented yet.

### 添加ORM支持 / Add ORM Support
1. **配置数据库信息**：去application.properties 配置数据库信息 配置文件内有例子和注释 根据需求改  
   **Configure Database Info**: Configure the database info in application.properties. There are examples and comments in the file for modification based on your needs.
2. **自动生成代码**：创建类Auto.Java 并写入以下代码 并运行  
   **Automatic Code Generation**: Create the class Auto.Java, write the following code, and run:
```java

//根据数据库表信息 逆向 生成 dao entity controller （可选 webui）
public static void main(String[] args) throws Exception {
  //要生成代码到这个目录
  String path = Config.thisPath() + "/src/main/java";
  //要生成代码的包名
  String pkg = "com";
  //数据库名 要和 db.mysql.数据库名 这里匹配
  String dbKey = "db002";
  //生成代码的函数 感兴趣可以去看看里边 直接调用里边的也可以 甚至可以使用三方生成器 自己生成
  GenerateJavaCode. generateCode(
          path,
          pkg,
          0,     //0生成全部表  1排除系统表   2生成系统表  系统表指的是 权限管理系统需要的表
          false, //如果不使用框架权限管理 则填写为false 反之为  true
          dbKey, //系统数据库名   如果不使用框架权限管理 则填写为业务数据库即可
          dbKey  //业务数据库名
  );
}
``` 
3. **扩展功能** / **Extended Features**：
   请参考demo项目 可以自动生成 用户权限管理系统相关支持  
   Please refer to the demo project which can automatically generate support for user permission management system.

### 温馨提示 / Friendly Reminder

* **JDK**：本框架与 JDK 8 以上版本兼容。  
  **JDK**: This framework is compatible with JDK 8 and above.
* **数据库**：目前只支持 mysql 和 postgresql。  
  **Database**: Currently only supports MySQL and PostgreSQL.
* 你无需关注 `entity` 和 `dao` 层，只需专注于 `service` 和 `controller` 的业务逻辑开发即可。  
  You don’t need to worry about `entity` and `dao` layers, just focus on the `service` and `controller` layers for business logic development.
* 有一个问题 现在web服务器 真的还有视图层吗？ 其实 直接把 service 注解为端点 提供给框架即可  
  One question: Does the web server still need a view layer? Actually, just annotate the service as an endpoint and provide it to the framework.

---

## 文档 / Documentation
* 持续更新中...框架为作者独立开发 精力有限 文档不够正式 请见谅  
  **Ongoing Updates**... The framework is developed independently by the author, with limited time and resources, so the documentation may not be formal. Please forgive any shortcomings.
* 文档不够齐全，但是和spring主流环境 差异很小 参考经验 和 示例项目 即可  
  The documentation is not complete, but it’s very similar to the mainstream Spring environment, and you can refer to the examples and experience.
* [文档在这里，持续更新中......](doc.md)  
  [Documentation here, continuously updated......](doc.md)