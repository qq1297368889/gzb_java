package com.frame.decorator;

import com.frame.dao.*;
import com.frame.entity.*;
import gzb.frame.annotation.Decorator;
import gzb.frame.annotation.DecoratorStart;
import gzb.frame.annotation.Resource;
import gzb.frame.factory.ClassTools;
import gzb.frame.netty.entity.Request;
import gzb.entity.RunRes;
import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.cache.session.Session;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.util.ArrayList;
import java.util.List;

//标注 这是一个装饰器
@Decorator
public class RequestDecorator {

    private static final String sql = "select * from sys_permission where " +
            "sys_permission.sys_permission_name = ? and " +
            "sys_permission.sys_permission_id in (" +
            "select sys_group_permission.sys_group_permission_pid from sys_group_permission where sys_group_permission.sys_group_permission_gid in (";
    @Resource
    Log log;

    @Resource
    SysRoleDao sysRoleDao;
    @Resource
    SysRoleGroupDao sysRoleGroupDao;
    @Resource
    SysPermissionDao sysPermissionDao;
    @Resource
    SysGroupDao sysGroupDao;
    @Resource
    SysUsersDao sysUsersDao;
    /// 依赖注入规则 和 控制器 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

    //登陆验证
    @DecoratorStart(value = "/system/", sort = 2)
    public RunRes shabi2(Request request, RunRes runRes, GzbJson gzbJson) throws Exception {

        runRes.setState(200);
        runRes.setData("自定义对象");
        return runRes;
    }
    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    //登陆验证
    @DecoratorStart(value = "/system/", sort = 0)
    public RunRes authorization(Request request, RunRes runRes, GzbJson gzbJson) throws Exception {
        Session session = request.getSession();
        log.d("登陆验证 开始");
        String data = session.getString(Config.get("key.system.login.info"));
        if (data == null) {
            return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\"" + Config.get("key.system.login.page") + "\"}");
        }
        SysUsers sysUsers = new SysUsers(data);
        if (sysUsers.getSysUsersId() == null) {
            return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\"" + Config.get("key.system.login.page") + "\"}");
        }
        sysUsers = sysUsersDao.find(new SysUsers().setSysUsersId(sysUsers.getSysUsersId()));
        if (sysUsers == null) {
            return runRes.setState(400).setData(gzbJson.jump("未登录或登录失效", Config.get("key.system.login.page")));
        }
        if (sysUsers.getSysUsersStatus() < 0) {
            return runRes.setState(400).setData(gzbJson.jump("未登录或登录失效", Config.get("key.system.login.page")));
        }
        //超级管理员 无需校验
        if (sysUsers.getSysUsersType() != 4L) {
            if (sysUsers.getSysUsersStatus() < 1L) {
                session.delete();
                return runRes.setState(400).setData("{\"code\":\"4\",\"message\":\"未登录或登录失效\",\"url\":\"" + Config.get("key.system.login.page") + "\"}");
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
        return runRes;
    }


    //value 指定拦截的链接 默认为全部拦截 比如  请求链接 /a/b/c/d/e  value=/a/b/c/ 那么会拦截 只要从头开始 包含就会拦截
    //type 指定为 true  只要链接 包含就会拦截 不再只匹配开头
    //turn 指定为 true  原包含匹配 改为不包含匹配
    //method 要匹配的方法 GET POST PUT DELETE
    //sort排序 越小越先执行
    //权限验证
    @DecoratorStart(value = "/system/", sort = 1)
    public RunRes verPermission(RunRes runRes, Request request
            , SysUsers sysUsers, GzbJson gzbJson) throws Exception {
        log.d("权限验证 开始",sysUsers.getSysUsersType()==4L);
        if (sysUsers.getSysUsersType()==4L) {
            return runRes.setState(200);
        }
        SysRole sysRole = sysRoleDao.find(new SysRole().setSysRoleId(sysUsers.getSysUsersRole()));
        if (sysRole == null) {
            return runRes.setState(400).setData(gzbJson.fail("无权限访问-用户权限未分配"));
        }
        List<Object> listData = new ArrayList<>();
        String path = ClassTools.webPathFormat(request.getUri()) + "-" + (request.getMethod().toUpperCase());
        listData.add(path);
        List<SysRoleGroup>listSysRoleGroup=sysRoleGroupDao.query(new SysRoleGroup().setSysRoleGroupRid(sysUsers.getSysUsersRole()));
        if (listSysRoleGroup.size()<1) {
            return runRes.setState(400).setData(gzbJson.fail("无权限访问-用户权限错误"));
        }
        StringBuilder sql_p = new StringBuilder(listSysRoleGroup.size() * 2);
        for (int i = 0; i < listSysRoleGroup.size(); i++) {
            listData.add(listSysRoleGroup.get(i).getSysRoleGroupGid());
            sql_p.append("?");
            if (i < listSysRoleGroup.size() - 1) {
                sql_p.append(",");
            }
        }
        log.d(sql + sql_p + "))", listData.toArray());
        List<SysPermission> listSysPermissionDao = sysPermissionDao.query(sql + sql_p + "))", listData.toArray(), null, null, 1, 1, 5);
        if (listSysPermissionDao.size() != 1) {
            return runRes.setState(400).setData(gzbJson.fail("无权限访问-非法访问"));
        }
        return runRes;
    }
}
