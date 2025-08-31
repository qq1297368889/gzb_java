# 注解篇
#### Controller 类级注解 表示这是一个控制器 会被扫描
#### Service 类级注解 表示这是一个实现类火其他类 可以被扫描  value='xxx'
#### GetMapping("xxx") 类级或方法级注解 表示这是一个 http get 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### PostMapping("xxx") 类级或方法级注解 表示这是一个 http post 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### PutMapping("xxx") 类级或方法级注解 表示这是一个 http put 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### DeleteMapping("xxx") 类级或方法级注解 表示这是一个 http delete 端点  value = ['xx','xxx'] 映射路径 可支持多个
#### RequestMapping(value = "/test/", header = "content-type:application/json;charset=UTF-8",crossDomain = "*") 类级或方法级注解 表示这是一个 http 任意类型的 端点   header=[{},{}] 指定协议头 当前作用域及以下作用域有效 crossDomain允许某个域名跨域 默认 * 全部允许
#### Resource(value = "com.frame.dao.impl.SysGroupColumnDaoImpl") 类变量专用注解 表示这是一个被注入的 接口或对象 value 可以指定实现类  也可以留空 默认第一个
#### Decorator 类级注解 表示这是一个装饰器 可以被扫描
#### DecoratorStart("/system/") 方法级注解 表示这是一个装饰器方法在/system/xxx控制器方法被调用  前  调用 可以拦截或修改请求数据  （前提控制器方法注解了#### DecoratorOpen 才会参与）
#### DecoratorEnd("/system/") 方法级注解 表示这是一个装饰器方法 在/system/xxx控制器方法被调用   后 调用 可以拦截或修改响应 （前提控制器方法注解了#### DecoratorOpen 才会参与）
#### DecoratorOpen 方法级注解 表示这是一个需要被装饰器方法挂钩的方法 只有注解了的方法才会被装饰器处理
#### ThreadInterval(num = 0, value = 2000, async = false) 方法级注解 表示这是一个线程函数 会被框架循环调度 num =10表示启动10个线程 value=2000表示每次调度间隔2000毫秒  async=true or false 表示同步或异步调用
#### Limitation(10) 方法级注解 表示这是一个被限流的端点 当同时运行该端点超过10个时 将会返回 服务器繁忙 （注意只有http端点注解才有效）
#### Transaction 方法级注解 表示这是一个开启事务的端点 关闭了自动提交 你可以随意回滚 或者出错自动回滚 未出错 结束后自动提交
#### EntityAttribute(key=true,size = 19,name="sys_file_id",desc="sysFileId") 类级或方法级注解 表示这是一个实体类 和 数据库的映射信息  这个对开发者无感知 知道就用 没有实用机会

# 控制器  action Controller 篇
#### 参数映射 可以把表单提交的参数(a=1&b=2)和 json参数({xxx:xxx,xx:xx}) 映射到方法的参数 复杂JSON请定义Map<String,Object>
#### 数据响应 可以直接 return 字符串 byte 或者直接从 HttpServletResponse 手动响应数据 如果手动响应请 return null;
```java
package com.authorizationSystem.api;
import com.authorizationSystem.dao.ApplicationDao;
import com.authorizationSystem.entity.Application;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/test/", header = "content-type:application/json;charset=UTF-8")
public class DemoAction {
    @Resource
    ApplicationDao applicationDao;

    // 请求端点为：http://ip:port/test/get?msg=msg1&applicationName=name1&applicationDesc=desc1    注意 applicationDesc 和 applicationName 是 application的内部参数 最终会映射到 application
    //@Limitation(1)//开启限流
    //@Transaction //开启事务
    //@DecoratorOpen//开启装饰器钩子
    @GetMapping(value = "get", crossDomain = "*")
    public Object get(JSONResult result, //框架提供的json对象 你定义了就会注入 不定义就不会
                      String msg,//请求参数 msg
                      ApplicationDao applicationDao2, //被扫描的类自动注入
                      Application application//根据请求参数 自动匹配的实体对象 表单数据和 json提交 都会映射
            , HttpServletResponse response //servlet的request 和 response定义即可注入
            , HttpServletRequest request //servlet的request 和 response定义即可注入
            , Log log //框架自带日志对象
    ) throws Exception
    //参数非必须 所有参数都是可选的 任何被扫描的类都可以在这里被注入
    {
        log.d("get", result, msg, applicationDao, applicationDao2, application, response, request);
        //这个测试例子 开启事务的话 save delete 需要手动提交 applicationDao2.commit();
        /*
        log.d("save", applicationDao2.save(application));
        log.d("query", applicationDao2.query(application));
        log.d("delete", applicationDao2.delete(application));
        log.d("query", applicationDao2.query(application));
        */
        /* 异步提交 和批量提交 save update delete 都有对应方法
           applicationDao2.saveAsync(application); //异步提交 耗时几乎为0  但是其实是前台转后台 资源占用没有消失 但是理想状态下 比单挑提交 效率提升5-30倍 并且不用开发者特殊处理  缺点是有一定延迟 0.3-1秒左右
        List list= new ArrayList<>();
        list.add(application);
        applicationDao2.saveBatch(list);//就是传统的批量提交，内置了大批量转多个小批量功能 防止长时间锁表 效率很高 效率对比单条 提升5-30 倍
           */
        //return applicationDao2.queryPage(application,null,null,1,10,100,100); //这是自带分页方法
        return result.success(msg);
    }
}

```