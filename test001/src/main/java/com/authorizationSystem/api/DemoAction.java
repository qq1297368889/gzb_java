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
