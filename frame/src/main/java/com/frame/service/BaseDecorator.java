package com.frame.service;

import com.frame.system.BaseAction;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.*;
import gzb.frame.server.http.entity.RunRes;
import gzb.tools.*;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Decorator
public class BaseDecorator {
    @DecoratorEnd("/system/")
    public RunRes testEnd(RunRes runRes) {
        runRes.setState(200);
        return runRes;
    }

    //验证例子
    @DecoratorStart("/system/")
    public RunRes authorization(Session session, RunRes runRes, HttpServletRequest request) throws Exception {
        String key = Config.get("key.system.login.info");
        String data = session.getString(key);
        if (data == null) {
            runRes.setState(400);
            runRes.setData(new JSONResult().jump("未登录或登录失效-1001", Config.get("key.system.login.page")));
            return runRes;
        }
        SysUsers sysUsers = new SysUsers(data);
        if (sysUsers.getSysUsersId() == null) {
            runRes.setState(400);
            runRes.setData(new JSONResult().jump("未登录或登录失效-1002", Config.get("key.system.login.page")));
            return runRes;
        }

        List<GzbMap> list = BaseAction.dataBase.selectGzbMap("select * from sys_users where " +
                "sys_users_id = ?", new Object[]{sysUsers.getSysUsersId()});
        if (list.size() != 1) {
            runRes.setState(400);
            runRes.setData(new JSONResult().fail("未登录或登录失效-1003", Config.get("key.system.login.page")));
            return runRes;
        }
        sysUsers = new SysUsers(list.get(0));
        //超级管理员 无需校验
        if (sysUsers.getSysUsersType() != 4L) {
            if (sysUsers.getSysUsersStatus() < 1L) {
                session.delete();
                runRes.setState(400);
                runRes.setData(new JSONResult().fail("账号状态异常-1004", Config.get("key.system.login.page")));
                return runRes;
            }
            if (sysUsers.getSysUsersType().equals(5L)) {
                if (sysUsers.getSysUsersStartTime() == null || sysUsers.getSysUsersEndTime() == null) {
                    session.delete();
                    runRes.setState(400);
                    runRes.setData(new JSONResult().fail("账号授权日期未指定-1005", Config.get("key.system.login.page")));
                    return runRes;
                }
                DateTime dateTime = new DateTime();
                DateTime dateTime1 = new DateTime(sysUsers.getSysUsersStartTime());
                DateTime dateTime2 = new DateTime(sysUsers.getSysUsersEndTime());
                if (dateTime.toStampInt() < dateTime1.toStampInt()) {
                    session.delete();
                    runRes.setState(400);
                    runRes.setData(new JSONResult().fail("账号授权还未开始-1006", Config.get("key.system.login.page")));
                    return runRes;
                }
                if (dateTime.toStampInt() > dateTime2.toStampInt()) {
                    session.delete();
                    runRes.setState(400);
                    runRes.setData(new JSONResult().fail("账号授权已过期-1007", Config.get("key.system.login.page")));
                    return runRes;
                }
            }
            //授权验证 基于已登录
            String path = Tools.webPathFormat(request.getRequestURI()) + "-" + (request.getMethod().toUpperCase());
            if (!verPermission(path, sysUsers.getSysUsersId())) {
                System.out.println(path);
                runRes.setState(400);
                runRes.setData(new JSONResult().fail("无权限访问该地址:" + path));
                return runRes;
            }
        }
        runRes.setState(200);
        runRes.setData(sysUsers);
        return runRes;
    }


    public boolean verPermission(String url, Long uid) throws Exception {
        String sql = "select sys_permission.* from sys_permission where " +
                "sys_permission.sys_permission_name = ? and " +
                //"sys_permission.sys_permission_type = ? and " +
                "sys_permission.sys_permission_id in (select sys_group_permission_pid from sys_group_permission where " +
                "sys_group_permission_gid = (select sys_users.sys_users_group from sys_users where " +
                "sys_users_id = ?))";
        List<GzbMap> list = BaseAction.dataBase.selectGzbMap(sql, new Object[]{url, uid});
        return list.size() == 1;
    }

}
