package com.frame.component;
import com.frame.dao.SysPermissionDao;
import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.Decorator;
import gzb.frame.annotation.DecoratorEnd;
import gzb.frame.annotation.DecoratorStart;
import gzb.frame.annotation.Resource;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseFactory;
import gzb.frame.factory.ClassTools;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.frame.server.http.entity.RunRes;
import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.GzbMap;
import gzb.tools.cache.session.Session;
import gzb.tools.log.Log;

import java.util.List;

//标注 这是一个装饰器
@Decorator
public class RequestDecorator {

    @Resource
    Log log;
    /// 依赖注入规则 和 控制器 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

    //http请求进入后事件
    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    @DecoratorEnd("/test/")
    public RunRes testEnd(RunRes runRes) {
        //log.d("后验证 开始");
        runRes.setState(200);
        //runRes.setData(gzbJson.error("拦截并修改"));
        //log.d("后验证 通过");
        return runRes;
    }
    //http请求进入前事件
    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    @DecoratorStart("/test/")
    public RunRes testStart(RunRes runRes) {
        //log.d("前验证 开始");
        runRes.setState(200);
        //data.put("sysUsersAcc",new ArrayList<>(Collections.singleton("调用前被修改了")));
        //log.d("前验证 通过");
        return runRes;
    }

    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    //登陆验证
    @DecoratorStart(value = "/system/",sort = 0)
    public RunRes authorization(Request request, RunRes runRes,SysUsersDao sysUsersDao) throws Exception {
        Session session=request.getSession();
        log.d("登陆验证 开始");
        String data = session.getString(Config.get("key.system.login.info"));
        if (data == null) {
            return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\""+Config.get("key.system.login.page")+"\"}");
        }
        SysUsers sysUsers = new SysUsers(data);
        if (sysUsers.getSysUsersId() == null) {
            return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\""+Config.get("key.system.login.page")+"\"}");
        }

        List<SysUsers> list = sysUsersDao.query(new SysUsers().setSysUsersId(sysUsers.getSysUsersId()));;
        if (list.size() != 1) {
            return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\""+Config.get("key.system.login.page")+"\"}");
        }
        sysUsers = list.get(0);
        //超级管理员 无需校验
        if (sysUsers.getSysUsersType() != 4L) {
            if (sysUsers.getSysUsersStatus() < 1L) {
                session.delete();
                return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\""+Config.get("key.system.login.page")+"\"}");
            }
            if (sysUsers.getSysUsersType().equals(5L)) {
                if (sysUsers.getSysUsersStartTime() == null || sysUsers.getSysUsersEndTime() == null) {
                    session.delete();
                    return runRes.setState(400).setData("{\"code\":\"2\",\"message\":\"账号授权日期未指定\"}");
                }
                DateTime dateTime = new DateTime();
                DateTime dateTime1 = new DateTime(sysUsers.getSysUsersStartTime());
                DateTime dateTime2 = new DateTime(sysUsers.getSysUsersEndTime());
                if (dateTime.toStampInt() < dateTime1.toStampInt()) {
                    session.delete();
                    return runRes.setState(400).setData("{\"code\":\"2\",\"message\":\"账号授权还未开始\"}");
                }
                if (dateTime.toStampInt() > dateTime2.toStampInt()) {
                    session.delete();
                    return runRes.setState(400).setData("{\"code\":\"2\",\"message\":\"账号授权已过期\"}");
                }
            }
        }
        runRes.setState(200);
        runRes.setData(sysUsers);
        log.d("登录验证 通过");
        return runRes;
    }

    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    //权限验证
    @DecoratorStart(value = "/system/",sort = 1)
    public RunRes verPermission(RunRes runRes, Request request, SysPermissionDao sysPermissionDao, SysUsers sysUsers) throws Exception {
        log.d("权限验证 开始");
        if (sysUsers.getSysUsersType()!=4) {
            String path = ClassTools.webPathFormat(request.getUri()) + "-" + (request.getMethod().toUpperCase());
            String sql = "select sys_permission.* from sys_permission where " +
                    "sys_permission.sys_permission_name = ? and " +
                    //"sys_permission.sys_permission_type = ? and " +
                    "sys_permission.sys_permission_id in (select sys_group_permission_pid from sys_group_permission where " +
                    "sys_group_permission_gid = (select sys_users.sys_users_group from sys_users where " +
                    "sys_users_id = ?))";
            if (sysPermissionDao.query(sql, new Object[]{path, sysUsers.getSysUsersId()},null,null,1,1,60).size() != 1) {
                return runRes.setState(400).setData("{\"code\":\"2\",\"message\":\"无权限访问该地址\"}");
            }
        }

        runRes.setState(200);
        log.d("权限验证 通过");

        return runRes;
    }

}
