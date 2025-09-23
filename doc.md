# 注解篇
#### Controller 类级注解 表示这是一个控制器 会被扫描
#### Service 类级注解 表示这是一个实现类火其他类 可以被扫描 

#### GetMapping("xxx") 类级或方法级注解 表示这是一个 http get 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### PostMapping("xxx") 类级或方法级注解 表示这是一个 http post 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### PutMapping("xxx") 类级或方法级注解 表示这是一个 http put 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### DeleteMapping("xxx") 类级或方法级注解 表示这是一个 http delete 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### RequestMapping(value = "/test/") 类级或方法级注解 表示这是一个 http 任意类型的 端点 
#### @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")}) 类变量和方法级别 注解 设置协议头
#### @CrossDomain(allowCredentials = false)//开跨域 允许所有默认 可配置详细
#### Resource(value = "com.frame.dao.impl.SysGroupColumnDaoImpl") 类变量专用注解 表示这是一个被注入的 接口或对象 value 可以指定实现类  也可以留空 默认第一个


#### Decorator 类级注解 表示这是一个装饰器 可以被扫描
#### DecoratorStart("/system/") 方法级注解 表示这是一个装饰器方法在/system/xxx控制器方法被调用  前  调用 可以拦截或修改请求数据  （前提控制器方法注解了#### DecoratorOpen 才会参与）
#### DecoratorEnd("/system/") 方法级注解 表示这是一个装饰器方法 在/system/xxx控制器方法被调用   后 调用 可以拦截或修改响应 （前提控制器方法注解了#### DecoratorOpen 才会参与）
#### DecoratorOpen 方法级注解 表示这是一个需要被装饰器方法挂钩的方法 只有注解了的HTTP端点 方法才会被 装饰器 DecoratorStart DecoratorEnd 处理

#### ThreadFactory 类级别注解 表示这个类是受框架调度的线程模型，所需参数皆为框架传入
#### ThreadInterval(num = 0, value = 2000, async = false) 方法级注解 表示这是一个线程函数 会被框架循环调度 num =10表示启动10个线程 value=2000表示每次调度间隔2000毫秒  async=true or false 表示同步或异步调用

#### Limitation(10) 方法级注解 表示这是一个被限流的端点 当同时运行该端点超过10个时 将会返回 服务器繁忙 （注意只有http端点注解才有效）

#### Transaction 方法级注解 表示这是一个开启事务的端点 关闭了自动提交 你可以随意回滚 或者出错自动回滚 未出错 结束后自动提交

#### EntityAttribute(key=true,size = 19,name="sys_file_id",desc="sysFileId") 类级或方法级注解 表示这是一个实体类 和 数据库的映射信息  这个对开发者无感知 知道就行 没有实用机会

# 注意一个点  Controller Decorator ThreadFactory Service 这些类 不要有内部类 防止框架插入代码的时候出现问题


# 参数注入介绍
### 在参数或者类变量  定义了 下方 的 接口 或 类 都可以自动注入 
#### 所有注解了都实现类 或单类 都可以被注入
#### http 请求流程的对象 Request(请求对象 .getSession()可以获取session )  Response(响应对象) Map<List<Object>>(请求参数) Log(框架日志对象) GzbJson(JSO对象) 会被注入
#### 在装饰器 DecoratorStart 中 runRes.setData(xx)的对象 也会在后续流程被注入 

#### 什么时候会被注入: Controller Decorator ThreadFactory 这些类内部的类变量 和 注册的方法都会被注入  



# 控制器  action Controller 篇
#### 参数映射 可以把表单提交的参数(a=1&b=2)和 json参数({xxx:xxx,xx:xx}) 映射到方法的参数 复杂JSON请定义Map<String,Object>
#### 数据响应 可以直接 return 字符串 byte 或者直接从 HttpServletResponse 手动响应数据 如果手动响应请 return null;
```java
package com.authorizationSystem.api;
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
        // 开启事务的话 save delete update 都会被框架提交 也可以 applicationDao2.commit(); 手动提交 或回滚
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

    public RequestDecorator() throws Exception {
    }

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

//标注 这是一个需要被框架调度的类
@ThreadFactory
public class ThreadClass {
    //需要注意 如果更改了类 或 方法签名 或 删除类 前 一定要修改num为0 这时候框架会终止线程 否则的话可能会出现失控线程
    //之所以提供线程模型是因为 热更新 需要的对象 通过 参数通过  可以防止使用旧版本对象
    
    //定义 这是一个需要被调度的方法 num是启动线程数量 ; value 是 调度周期 单位毫秒 限制最小值为100毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test001(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        int intx=result.getInteger("intx",0);
        if (intx>20){
            //return false;//非null代表停止
        }
        intx++;
        result.set("intx",intx);
        log.d("3.00",Thread.currentThread().getName(),intx,result.hashCode(),sysUsersDao);
        return null;//继续循环 如果不需要主动停止 可以直接设置为 void方法 
    }
    //定义 这是一个需要被调度的方法 num是启动线程数量 ; value 是 调度周期 单位毫秒 限制最小值为100毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test002(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        return test001(sysUsersDao,result,log,lock);
    }
    //定义 这是一个需要被调度的方法 num是启动线程数量 ; value 是 调度周期 单位毫秒 限制最小值为100毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test003(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        return test001(sysUsersDao,result,log,lock);
    }
}

```
