package com.frame.api;

import com.frame.dao.SysFileDao;
import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.cache.session.Session;
import gzb.tools.json.GzbJson;

@Controller//标注为控制器
@RequestMapping("/test")//类级别请求前缀
public class TestAction {

    //@Resource("com.frame.dao.impl.SysUsersDaoImpl")//不指定实现类 默认匹配第一个
    //SysUsersDao sysUsersDao;

    /**
     * 演示HTTP端点
     *
     * @param sysUsersAcc 客户端请求参数
     */
    @Limitation(100)//限制 最大同时 被100个线程调用
    //@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")}) //这个不解释....
    //@DecoratorOpen//开启一个前验证 和 后验证
    @GetMapping("/test1")//方法级别后缀  类级别+方法级别  SysUsers
    public Object test(String sysUsersAcc) throws Exception {
        return new TestEntity("1", sysUsersAcc);
    }

}
