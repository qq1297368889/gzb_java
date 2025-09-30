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

    /// 依赖注入规则 和 action 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

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