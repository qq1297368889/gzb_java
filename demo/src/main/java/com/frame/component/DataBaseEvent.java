package com.frame.component;

import com.frame.dao.*;
import com.frame.entity.*;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.tools.DateTime;
import gzb.tools.log.Log;

//标注为 数据库事件注册 类
@DataBaseEventFactory
public class DataBaseEvent {
    @Resource
    Log log;

    /// 依赖注入规则 和 action 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

    //entity 注册到 某个实体类 数据库 新增事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventSave(entity = SysUsers.class, executionBefore = false, depth = 5)
    public void saveSysUsers(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao, Request request) throws Exception {
        SysUsersLoginLog sysUsersLoginLog = new SysUsersLoginLog();
        sysUsersLoginLog.setSysUsersLoginLogIp(request.getRemoteIp())
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId())
                .setSysUsersLoginLogTime(new DateTime().toString())
                .setSysUsersLoginLogDesc("用户创建")
                .setSysUsersLoginLogToken(request.getSession().getId());
        sysUsersLoginLogDao.saveAsync(sysUsersLoginLog);
    }

    //entity 注册到 某个实体类 数据库 删除事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventDelete(entity = SysUsers.class)
    public void deleteSysUsers(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        sysUsersLoginLogDao.deleteAsync(new SysUsersLoginLog().setSysUsersLoginLogUid(sysUsers.getSysUsersId()));

    }

    @DataBaseEventDelete(entity = SysGroup.class)
    public void deleteSysGroup(SysGroup sysGroup, SysGroupColumnDao sysGroupColumnDao, SysGroupTableDao sysGroupTableDao, SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        sysGroupColumnDao.deleteAsync(new SysGroupColumn().setSysGroupColumnGid(sysGroup.getSysGroupId()));
        sysGroupTableDao.deleteAsync(new SysGroupTable().setSysGroupTableGid(sysGroup.getSysGroupId()));
        sysGroupPermissionDao.deleteAsync(new SysGroupPermission().setSysGroupPermissionGid(sysGroup.getSysGroupId()));

    }

    @DataBaseEventDelete(entity = SysPermission.class)
    public void deleteSysPermission(SysPermission sysPermission, SysGroupColumnDao sysGroupColumnDao, SysGroupTableDao sysGroupTableDao, SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        sysGroupPermissionDao.deleteAsync(new SysGroupPermission().setSysGroupPermissionPid(sysPermission.getSysPermissionId()));

    }


}