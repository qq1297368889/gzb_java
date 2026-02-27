# 个别笔误 错别字 请理解 咳咳
# 核心注解篇
#### @Controller 类级注解 表示这是一个控制器 会被扫描
#### @Service 类级注解 表示这是一个实现类火其他类 可以被扫描 

#### @GetMapping("xxx") 类级或方法级注解 表示这是一个 http get 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### @PostMapping("xxx") 类级或方法级注解 表示这是一个 http post 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### @PutMapping("xxx") 类级或方法级注解 表示这是一个 http put 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### @DeleteMapping("xxx") 类级或方法级注解 表示这是一个 http delete 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### @RequestMapping(value = "/test/") 类级或方法级注解 表示这是一个 http 任意类型的 端点 
#### @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")}) 类变量和方法级别 注解 设置协议头
#### @CrossDomain(allowCredentials = false)//开跨域 允许所有默认 可配置详细规则
#### @Resource(value = "com.frame.dao.impl.SysGroupColumnDaoImpl") 类变量专用注解 表示这是一个被注入的 接口或对象 value 可以指定实现类  也可以留空 默认第一个
#### @EventLoop 端点专用注解 表示这个端点将直接在事件循环线程执行 避免了业务线程池的上下文切换开销，但请注意不能阻塞

#### @Decorator 类级注解 表示这是一个装饰器 可以被扫描
#### @DecoratorStart("/system/",sort=1) 方法级注解 表示这是一个装饰器方法在/system/xxx控制器方法被调用  前  调用 可以拦截或修改请求数据  sort用于多个装饰器的触发顺序 可省略 （前提控制器方法注解了#### DecoratorOpen 才会参与）
#### @DecoratorEnd("/system/",sort=1) 方法级注解 表示这是一个装饰器方法 在/system/xxx控制器方法被调用   后 调用 可以拦截或修改响应 sort用于多个装饰器的触发顺序 可省略（前提控制器方法注解了#### DecoratorOpen 才会参与）
#### @DecoratorOpen 方法级注解 表示这是一个需要被装饰器方法挂钩的方法 只有注解了的HTTP端点 方法才会被 装饰器 DecoratorStart 和 DecoratorEnd 处理

#### @ThreadFactory 类级别注解 表示这个类是受框架调度的线程模型，所需参数皆为框架传入
#### @ThreadInterval(num = 0, value = 2000, async = false) 方法级注解 表示这是一个线程函数 会被框架循环调度 num =10表示启动10个线程 value=2000表示每次调度间隔2000毫秒  async=true or false 表示同步或异步调用

#### @Limitation(10) 方法级注解 表示这是一个被限流的端点 当同时运行该端点超过10个时 将会返回 服务器繁忙 （注意只有http端点注解才有效）

#### @Transaction 方法级注解 表示这是一个开启事务的端点 关闭了自动提交 你可以随意回滚 或者出错自动回滚 未出错 结束后自动提交    注意 事务开启时不支持异步执行SQL

#### @DataBaseEventFactory    标注为 数据库事件注册 类      注意参数只要定义 且 有可注入的对象 就会注入
#### @DataBaseEventSave(entity = SysUsers.class, executionBefore = false, depth = 5)     //entity 注册到 某个实体类 数据库 新增事件 //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false //depth 事件传播深度 防止循环传播
#### @DataBaseEventDelete(entity = SysUsers.class, executionBefore = false, depth = 5)     //entity 注册到 某个实体类 数据库 删除事件 //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false //depth 事件传播深度 防止循环传播
#### @DataBaseEventUpdate(entity = SysUsers.class, executionBefore = false, depth = 5)     //entity 注册到 某个实体类 数据库 修改事件 //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false //depth 事件传播深度 防止循环传播
#### @DataBaseEventSelect(entity = SysUsers.class, executionBefore = false, depth = 5)     //entity 注册到 某个实体类 数据库 查询事件 //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false //depth 事件传播深度 防止循环传播
#### @CacheRequest(value={"p1","p2","xxx"},second=10) //缓存接口响应 value={xx,xx} xx对应请求参数  最终会生成 key 两次请求 同key 将会命中缓存(前提未过期) second 缓存时间 单位秒


#### @EntityAttribute(key=true,size = 19,name="sys_file_id",desc="sysFileId") 类级或方法级注解 表示这是一个实体类 和 数据库的映射信息  这个对开发者无感知 知道就行 没有实用机会
 
# Controller Decorator ThreadFactory Service 这些类所在文件中，不要定义多个顶级平级类（如 public class a{} class b{}）；
# public类内部的子类可正常定义，主要是防止框架插入代码时出现问题。

# 参数注入介绍
#### 所有被框架管理的 类 都可以被注入  可以注入到 类变量 和 方法参数  
#### http 请求流程的对象 Request(请求对象 .getSession()可以获取session )  Response(响应对象) Map<List<Object>>(请求参数) Log(框架日志对象) GzbJson(JSON对象) 会被注入
#### 在装饰器 DecoratorStart 中 runRes 注入的对象 也会在后续流程被注入 
#### 有一个例外 线程模型 不会注入http相关的变量 因为他与http请求无关
#### 什么时候会被注入: Controller Decorator ThreadFactory DataBaseEventFactory 这些标注类 内部的 变量 和 注册的方法参数 都会被注入  
#### 需要注意的点 复杂对象是根据类型匹配的 如果两个同类型对象可能会导致意外问题 但是 我认为 为什么要重复呢 避免重复即可 如果为了边缘问题设置一大堆配置方案 那么是否得不偿失?
# http 请求参数解析规则
#### 这是主要差异点 任何请求参数 都会被扁平化封装为 map<string,list<object>> 
#### 因为我不想增加前端工作负担 传参时 还要 obj.xxx=xx  或者 obj{xx:xx}  他们可以 直接 xxx=xx 或者 {xx:xx}
#### 这也就导致了 会有妥协 不支持嵌套对象解析 但是可以定义一个 map<string,list<object>> 接受原始对象
#### 当定义一个 复杂对象的时候 框架会扫描这个对象的各个参数变量 如果参数名 和 请求参数名匹配 那么就会把这个参数封装到这个对象里边  
#### 这种方式 既支持了表单提交 又支持了 json提交 并且统一抽象了兼容层 map 控制器 无需关心 参数来自于表单 还是 json 又或者查询参数  
#### 但是不支持嵌套对象解析到复杂对象 或者说会解析成意外的情况 这就是代价 但我认为这个代价是值得的 只要按照规则 那么你的认知负担将会极低
#### 这是一个权衡问题 和 设计思想问题 我认为 参数不应该嵌套 增加了太多认知负担 而扁平化天生匹配我们人类的大脑思维模式
#### 比如说 {xx:xx,data:[{dataxx:xx},{}....]} 应该怎么扁平化的传递? xx=xx&dataxx=xx&dataxx=xxx..... 这真的复杂吗?  后台接收 fun(string xx,string[]dataxx)
#### 对象怎么匹配 比如传递 {xx:xx} 后端定义 fun(xxobj obj) 只要obj 拥有变量 xx 那么也就能接收到 
#### 有人会说了 重名怎么办?说实在的 这是你的习惯不好 我创建数据库固定是 表名_列名 生成的实体类是 驼峰形式的 表名列名 天生不重名 
#### 接收对象直接用entity 不比定义一堆 DTO要好?? 对不对 而且它拥有强一致规范 返回的数据 和接收的数据 天然匹配 规则即文档 降低对接成本





#### 数据库使用例子
```java

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;
//直接生成代码 基本不需要写
public class AutoCode {
    //根据数据库表信息 逆向 声称 dao entity controller
    public static void main(String[] args) throws Exception {
        //要生成代码到这个目录
        String path = Config.thisPath() + "/src/main/java";
        //要生成代码的包名
        String pkg = "com";
        //数据库名 要和配置文件中 db.数据库名 这里匹配
        String dbKey = "db002";
        //生成代码的函数 感兴趣可以去看看里边 直接调用里边的也可以   参数含义 生成代码的主目录 包名前缀 框架数据库 业务数据库（相同则重复传递即可） 
        GenerateJavaCode.generateCode(path,pkg,dbKey,dbKey);
        //运行后会生成所需代码 entity dao controller 以及每个表对应的 crud网页UI
    }
}

```
#### 日志类怎么使用
#### 怎么控制日志是否输出 去配置文集中寻找这几行 修改（修改运行中实时生效）
```properties
# 0 显示但不保存 1 保存但不显示 2 不显示也不保存 3 显示且保存
gzb.log.trace=2
gzb.log.debug=0
gzb.log.info=0
gzb.log.warn=0
gzb.log.error=0
gzb.log.path=this:logs/
```
#### 使用示例
```java
public class Test{
    public static void main(String[] args) {
        String msg="日志内容";
        SysFile sysFile=new SysFile();
        //可以自动检测所在类 所在方法 所在行 并序列化输出对象
        //对象可以单例使用 不需要创建多个，不需要担心堆栈获取开销 只要不输出 且 不保存 那么逻辑会被忽略 耗时几乎 = 0
        //如果你需要传入当前class的话 可以使用多例 但是没必要  gzb.tools.log.Log.create(Test.class);
        gzb.tools.log.Log log = gzb.tools.log.Log.log; 
        log.t("这是一个日志","msg",msg,"sysFile",sysFile);
        log.d("这是一个日志","msg",msg,"sysFile",sysFile);
        log.i("这是一个日志","msg",msg,"sysFile",sysFile);
        log.w("这是一个日志","msg",msg,"sysFile",sysFile);
        log.e("这是一个日志","msg",msg,"sysFile",sysFile,new Exception());
    }
}
```
# 缓存怎么用
```java
import gzb.tools.GzbMap;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.cache.GzbCacheRedis;

public class Test0 {
    public static void main(String[] args) {
        //参数分别为 刷盘路径 刷盘周期（jvm结束时会触发回调刷盘，所以这个也不用太频繁）
        GzbCache gzbCache1 = new GzbCacheMap("C:/cache1.cache",120);
        //redis 配置 请查看配置文件，配置文件有注释
        //# 数据库地址
        //db.redis.cache1.ip=127.0.0.1
        //# 数据库端口
        //db.redis.cache1.port=6379
        //# 数据库密码 可为空
        //db.redis.cache1.pwd=xxxxxxxxxxx
        //# 数据库 最大空闲连接数
        //db.redis.cache1.max.thread.idle=10
        //# 数据库 最大连接数
        //db.redis.cache1.thread=24
        //# 超时时间
        //db.redis.cache1.overtime=3000
        //# 数据库选择
        //db.redis.cache1.index=0
        GzbCache gzbCache2 = new GzbCacheRedis();

        //GzbCache 有两个实现 redis or map（带持久化）
        GzbCache main = Cache.gzbCache;//主缓存  配置文件可切换redis or map
        GzbCache map = Cache.gzbMap;//内部缓存 重启消失
        GzbCache session = Cache.session;//会话缓存 配置文件可切换redis or map
        GzbCache db = Cache.dataBaseCache;//数据库缓存 配置文件可切换redis or map
        /// 过期时间设置为小于0为永不过期
        //存入一个 键值对  过期时间为 永不过期
        main.set("key", "val", -1);
        //存入一个 键值对  过期时间为 60秒
        main.set("key2", "val2", 60);
        //存入一个 键值对 值为对象 会被序列化储存  过期时间为 60秒
        main.setObject("key3", new String("val3"), 60);
        //将值 存入map.key.sub_key  过期时间为 60秒
        main.setMap("key4", "key4", "val4", 60);
        //将值 存入map.key.sub_key  值为对象 会被序列化储存 过期时间为 60秒
        main.setMapObject("key5", "key5", new String("val5"), 60);
        //获取一个自增数字 从1开始 60秒过期 过期后从1重新开始
        main.getIncr("key6", 60);
        //读取值 同上储存逻辑
        main.get("key");
        //读取值 同上储存逻辑
        main.getObject("key3");
        //读取值 同上储存逻辑
        main.getMap("key4","key4");
        //读取值 同上储存逻辑
        main.getMapObject("key5","key5");
    }
}

```
# 控制器  action Controller 篇
#### 参数映射 可以把表单提交的参数(a=1&b=2)和 json参数({xxx:xxx,xx:xx}) 映射到方法的参数 复杂JSON请定义Map<String,Object>
#### 数据响应 可以直接 return 字符串 byte 或者直接从 HttpServletResponse 手动响应数据 如果手动响应请 return null;
```java
import com.authorizationSystem.dao.ApplicationDao;
import com.authorizationSystem.entity.Application;
import gzb.frame.annotation.*;
import gzb.tools.json.Result;
import gzb.tools.log.Log;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response; 
import java.util.ArrayList;
import java.util.List;
//虽然依然是 Controller命名 但是实际上这只是命名习惯 在我的定义里 这是service 将来会支持被tcp udp ws 等协议调用
@Controller
@RequestMapping(value = "/test/")
public class DemoAction {
    @Resource
    ApplicationDao applicationDao;

    // 请求端点为：http://ip:port/test/get?msg=msg1&applicationName=name1&applicationDesc=desc1    注意 applicationDesc 和 applicationName 是 application的内部参数 最终会映射到 application
    //@Limitation(1)//开启限流
    //@Transaction //开启事务
    //@DecoratorOpen//开启装饰器钩子
    //@EventLoop//表示该端点在事件循环中直接执行
    @GetMapping(value = "get", crossDomain = "*")
    public Object get(GzbJson result, //框架提供的json对象 你定义了就会注入 不定义就不会
                      String msg,//请求参数 msg
                      ApplicationDao applicationDao2, //被扫描的类自动注入
                      Application application//根据请求参数 自动匹配的实体对象 表单数据和 json提交 都会映射
            , Request response //底层的request 和 response定义即可注入
            , Response request //底层的request 和 response定义即可注入
            , Log log //框架自带日志对象
    ) throws Exception
    //参数非必须 所有参数都是可选的 任何被扫描的类都可以在这里被注入
    {
        log.d("get", result, msg, applicationDao, applicationDao2, application, response, request);
        // 开启事务的话 save delete update 都会被框架提交 也可以 applicationDao2.getDataBase().commit(); 手动提交 或回滚
        /*
        log.d("save", applicationDao2.save(application));
        log.d("query", applicationDao2.query(application));
        log.d("delete", applicationDao2.delete(application));
        log.d("query", applicationDao2.query(application));
        */
        /* 异步提交 和批量提交 save update delete 都有对应方法
           applicationDao2.saveAsync(application); //异步提交 耗时几乎为0  但是其实是前台转后台 资源占用没有消失 但是理想状态下 比单挑提交 效率提升5-30倍 并且不用开发者特殊处理  缺点是有一定延迟
        List list= new ArrayList<>();
        list.add(application);
        applicationDao2.saveBatch(list);//就是传统的批量提交，内置了大批量转多个小批量功能 防止长时间锁表 效率很高 效率对比单条 提升5-30 倍
        applicationDao2.saveAsync(application, new Runnable() {
            @Override
            public void run() {
                log.d("保存失败回调");//可以处理补偿措施
            }
        });
           */
        //return applicationDao2.queryPage(application,null,null,1,10,100,100); //这是自带分页方法
        return result.success(msg);
    }
}

```

# 装饰器  Decorator 篇
```java

//标注 这是一个装饰器 
@Decorator
public class RequestDecorator {
//获取数据库对象
    public DataBase dataBase= DataBaseFactory.getDataBase("db001");
    @Resource
    SysUsersDao sysUsersDao0;
    //所有 标注了参与装饰器 且路径为 /system/xxx 的端点 方法被调用后 实际响应前 都会调用这个方法 可以修改一些参数 获取一些参数 或拦截
    @DecoratorEnd("/system/")
    public RunRes testEnd(RunRes runRes, Log log,GzbJson gzbJson,SysUsersDao sysUsersDao) {
        log.d("后验证 开始");
        runRes.setState(200);//放行
        //return runRes.setState(400).setData(gzbJson.fail("请求被拦截"));//拦截某个请求
        //runRes.setData("xxxxx");//修改响应 多个拦截器都修改的话 最后一个为准
        log.d("后验证 通过");
        return runRes;
    }
    //所有 标注了参与装饰器 且路径为 /system/xxx 的端点 方法被 调用前  都会调用这个方法 可以修改一些参数 获取一些参数 或拦截
    @DecoratorStart("/system/")
    public RunRes testEnd(RunRes runRes, Log log,GzbJson gzbJson,SysUsersDao sysUsersDao) {
        log.d("前验证 开始");
        runRes.setState(200);//放行
        //return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"\":\""+Config.get("key.system.login.page")+"\"}"); //拦截未经授权的访问
        log.d("前验证 通过");
        return runRes;
    }
}
```

## 线程模型
### 线程模型主要为了解决热更新 线程持有老对象问题
```java
package com.frame.thread;
import com.frame.dao.SysUsersDao;
import gzb.frame.annotation.ThreadFactory;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.json.Result;
import gzb.tools.log.Log;
import java.util.concurrent.locks.Lock;
//标注 这是一个需要被框架调度的类
@ThreadFactory
public class ThreadClass {
    //注意 这个类 在运行中 不能直接被删除 否则线程失控。（重新创建并有一致签名 即可接着影响线程的行为）
    //之所以提供线程模型是因为 热更新 需要的对象 通过 参数通过  可以防止使用旧版本对象

    /// 修改方法内容 不修改签名的话 可以在不丢失 状态的情况下 热更新
    /// 修改签名的话会丢失状态 重新开始调度
    //定义 这是一个需要被调度的方法 num是启动线程数量  value 是 调度周期 单位毫秒
    @ThreadInterval(num=0 ,value=1000)
    public Object test001(SysUsersDao sysUsersDao, Result result, Log log,Lock lock) {
        //sysUsersDao @service 实现类
        //私有变量 不会有并发问题 但是多次被调用时 会记录状态  专属 result
        //方法专属锁 lock  仅该方法内部使用 启用多个线程的时候 防止并发  和其他被调度的方法 不是同一个锁
        int intx=result.getInteger("intx",0);
        intx++;
        result.set("intx",intx);
        log.d(intx,lock,result.hashCode(),sysUsersDao);
        return null;//继续循环 如果不需要主动停止 可以直接设置为 void方法  返回非null 即可停止被框架调度 本次启动无法再次被调度 除非方法签名出现变法
    }

}


```
## 数据库 事件例子
```java
 package com.frame.component;

import com.frame.dao.SysUsersLoginLogDao;
import com.frame.entity.SysUsers;
import com.frame.entity.SysUsersLoginLog;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.tools.DateTime;
import gzb.tools.log.Log;

//标注为 数据库事件注册 类
@DataBaseEventFactory
public class DataBaseEvent {
    @Resource
    Log log;
    /// 注意 异步操作 和 直接执行SQL不会触发事件
    /// 如果主操作开启事务 本方法一定开启  如果主操作没开启 但是本方法注解事务了 也会开启
    /// 依赖注入规则 和 action 一致 所有框架调度方法注入规则 都是一样的（线程模型例外，因为他没有 req resp）

    //entity 注册到 某个实体类 数据库 新增事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventSave(entity = SysUsers.class, executionBefore = false, depth = 5)
    public void save(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao, Request request) throws Exception {
        log.d("事件触发 DataBaseEventSave", sysUsersLoginLogDao, sysUsers);
        SysUsersLoginLog sysUsersLoginLog = new SysUsersLoginLog();
        sysUsersLoginLog.setSysUsersLoginLogIp(request.getRemoteIp())
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId())
                .setSysUsersLoginLogTime(new DateTime().toString())
                .setSysUsersLoginLogDesc("用户创建")
                .setSysUsersLoginLogToken(request.getSession().getId());
        sysUsersLoginLogDao.save(sysUsersLoginLog);
    }

    //entity 注册到 某个实体类 数据库 删除事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventDelete(entity = SysUsers.class)
    public void delete(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        log.d("事件触发 DataBaseEventDelete", sysUsersLoginLogDao, sysUsers);
        sysUsersLoginLogDao.delete(new SysUsersLoginLog().setSysUsersLoginLogUid(sysUsers.getSysUsersId()));
    }

    //entity 注册到 某个实体类 数据库 修改事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventUpdate(entity = SysUsers.class)
    public void update(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        log.d("事件触发 DataBaseEventUpdate", sysUsersLoginLogDao, sysUsers);
    }

    //entity 注册到 某个实体类 数据库 查询事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventSelect(entity = SysUsers.class)
    public void select(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        log.d("事件触发 DataBaseEventSelect", sysUsersLoginLogDao, sysUsers);
    }
}

```

# 关于异步 数据库操作问题  注意这是同步框架
## 为什么采用了 dao.saveAsync()这种异步方式？
### 异步 SQL 的真实目的（例如 saveAsync）突破系统硬性 I/O 上限：
#### 异步提交并不是为了释放 HTTP 线程，而是将多个离散的 SQL 汇聚到后台的一个或少数几个高吞吐量数据队列中。
### 智能合并与批量写入（SQL Aggregation）： 
#### 框架在后台线程中，可以智能地对同类的 SQL 操作进行聚合和合并。
#### 这意味着 100 次 saveAsync 调用，可能只触发 1 次批量写入，从而大幅减少 JDBC 调用开销
#### 相比于传统的单条同步 SQL 写入，此机制能将数据层的写入吞吐量提升 5 到 30 倍，有效解决在高并发插入的性能问题。
#### 数据库的真正瓶颈不在于网络 I/O，而在于数据库服务器 CPU 的事务处理效率和锁竞争开销。异步无法解决此根本问题。
### 代码示例
```java

@PostMapping("test")
public Object test(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
    SysUsers sysUsers = new SysUsers().setSysUsersAcc("123456");
    //严格原子性要求的数据 要慎重
    sysUsersDao.saveAsync(sysUsers, new Runnable() {
        @Override
        public void run() {
            //这里有完整上下文信息 因为开发者可以自由引用当前可访问区域的内容 可以进行补偿操作  
            log.d("执行失败，回调触发", sysUsers);
        }
    });
    return gzbJson.success("OK",sysUsers);
}

```
