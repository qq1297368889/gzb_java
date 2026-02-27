package com.frame.decorator;

import gzb.entity.RunRes;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;


//标注 这是一个装饰器
@Decorator
public class ControllerDecorator {

    /// 依赖注入规则 和 控制器 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）
    /// 装饰器 @DecoratorStart(value = "/xxx/", sort = 0, type = false, method = {"GET", "POST", "PUT", "DELETE"}) 说明
    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()
    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接包含就会拦截 不再只进行前缀匹配
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    /// @DecoratorEnd 和 @DecoratorStart 规则基本一致 可以看下方代码示例的描述
    @Resource
    Log log;
    //可以定义多个对象 被自动注入
    //@Resource
    //XxxDao xxDao;

    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()
    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接包含就会拦截 不再只进行前缀匹配
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    @DecoratorStart(value = "/system/", sort = 0, type = false, method = {"GET", "POST", "PUT", "DELETE"})
    public RunRes start0(Request request, Response response, RunRes runRes, GzbJson gzbJson) {
        log.i("start0");
        /// runRes.intercept(); 拦截 且无输出
        //runRes.intercept(gzbJson.fail("调用被拦截"));
        //runRes.release(new SysUsers());//直接放行  且输入自定义对象到上下文 这个对象可以被后续处理器 控制器 等一系列 组件获取
        //runRes.release();//直接放行不向后续处理器传递对象
        // 这个对象可以被后续处理器 哦不对 装饰器 在参数中接收到
        // 注意 是和service一样的类型匹配不要有重复的类型避免混淆
        return runRes;
    }

    //这个装饰器因为 顺序是1 所以会在上一个装饰器 0 之后执行
    // 也就是说 这个装饰器会在上一个装饰器放行之后执行  如果上一个装饰器没有放行 那么这个装饰器就不会执行
    @DecoratorStart(value = "/system/", sort = 1, type = false, method = {"GET", "POST", "PUT", "DELETE"})
    public RunRes start1(Request request, Response response, RunRes runRes, GzbJson gzbJson) {
        log.i("start1");
        /// runRes.intercept(); 拦截 且无输出
        //runRes.intercept(gzbJson.fail("调用被拦截"));
        //runRes.release(new SysUsers());//直接放行  且输入自定义对象到上下文 这个对象可以被后续处理器 控制器 等一系列 组件获取
        // 这个对象可以被后续处理器 哦不对 装饰器 在参数中接收到
        // 注意 是和service一样的类型匹配不要有重复的类型避免混淆
        return runRes;
    }

    //在请求进入前执行装饰器 如需生效条件为 1. 路径匹配正确 2.对应端点有标注 @DecoratorOpen()
    //规则和 @DecoratorStart 一样 只是这个是在请求处理完成后执行的 也就是说 这个装饰器会在控制器方法执行完成后执行  这个时候 runRes 中就有了控制器方法的返回值了
    @DecoratorEnd(value = "/system/", sort = 1, type = false, method = {"GET", "POST", "PUT", "DELETE"})
    public RunRes end0(Request request, Response response, RunRes runRes, GzbJson gzbJson) {
        log.i("end0");
        // runRes.getData(); //这是响应数据 可以修改 修改后返回即可
        /// runRes.intercept(); 拦截 且无输出
        //runRes.intercept(gzbJson.fail("调用被拦截"));//拦截 修改响应
        return runRes;
    }

    //规则和 @DecoratorStart 一样 只是这个是在请求处理完成后执行的 也就是说 这个装饰器会在控制器方法执行完成后执行  这个时候 runRes 中就有了控制器方法的返回值了
    @DecoratorEnd(value = "/system/", sort = 1, type = false, method = {"GET", "POST", "PUT", "DELETE"})
    public RunRes end1(Request request, Response response, RunRes runRes, GzbJson gzbJson) {
        log.i("end1");
        // runRes.getData(); //这是响应数据 可以修改 修改后返回即可
        /// runRes.intercept(); 拦截 且无输出
        //runRes.intercept(gzbJson.fail("调用被拦截"));//拦截 修改响应
        return runRes;
    }
}
