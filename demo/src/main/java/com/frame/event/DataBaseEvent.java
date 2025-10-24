package com.frame.event;

import com.frame.dao.SysGroupPermissionDao;
import com.frame.dao.SysPermissionDao;
import com.frame.dao.SysRoleGroupDao;
import com.frame.dao.SysUsersLoginLogDao;
import com.frame.entity.*;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.List;

//标注为 数据库事件注册 类
@DataBaseEventFactory
public class DataBaseEvent {

    /// 依赖注入规则 和 action 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

    @Resource
    Log log;
    @Resource
    SysGroupPermissionDao sysGroupPermissionDao;
    @Resource
    SysPermissionDao sysPermissionDao;
    @Resource
    SysRoleGroupDao sysRoleGroupDao;

    @DataBaseEventDelete(entity = SysRole.class, executionBefore = false, depth = 5)
    public void deleteSysRole(SysRole sysRole) throws Exception {
        log.i("deleteSysRole", sysRole);
        //删除该角色全部权限关联
        new SysRoleGroup().setSysRoleGroupRid(sysRole.getSysRoleId()).deleteAsync(sysRoleGroupDao);
    }
    @DataBaseEventSave(entity = SysGroupPermission.class, executionBefore = false, depth = 5)
    public void saveSysGroupPermission(SysGroupPermission sysGroupPermission) throws Exception {
        log.i("saveSysGroupPermission", sysGroupPermission);
        SysPermission sysPermission = sysPermissionDao.find(new SysPermission().setSysPermissionId(sysGroupPermission.getSysGroupPermissionPid()));
        if (sysPermission == null) {
            return;
        }
        log.i("saveSysGroupPermission", sysPermission);
        //如果是子权限 则自动添加父权限
        if (sysPermission.getSysPermissionSup() > 0) {
            SysPermission permission = sysPermissionDao.find(new SysPermission().setSysPermissionId(sysPermission.getSysPermissionSup()));
            if (permission == null) {
                return;
            }
            //防止重复
            SysGroupPermission sysGroupPermission1 = sysGroupPermissionDao.find(new SysGroupPermission()
                    .setSysGroupPermissionGid(sysGroupPermission.getSysGroupPermissionGid())
                    .setSysGroupPermissionPid(permission.getSysPermissionId()));
            if (sysGroupPermission1 != null) {
                return;
            }
            //是否已存在
            sysGroupPermissionDao.saveAsync(sysGroupPermission.setSysGroupPermissionId(null).setSysGroupPermissionPid(permission.getSysPermissionId()));
        }
        //如果是API权限 自动把子权限跟随过去
        if (sysPermission.getSysPermissionType() == 2L) {
            List<SysPermission> list = sysPermissionDao.query(new SysPermission().setSysPermissionSup(sysPermission.getSysPermissionId()));
            for (SysPermission permission : list) {
                if (permission == null) {
                    continue;
                }
                sysGroupPermissionDao.saveAsync(sysGroupPermission.setSysGroupPermissionId(null).setSysGroupPermissionPid(permission.getSysPermissionId()));
            }
        }

    }

    @DataBaseEventDelete(entity = SysGroupPermission.class, executionBefore = false, depth = 5)
    public void deleteSysGroupPermission(SysGroupPermission sysGroupPermission) throws Exception {
        log.i("deleteSysGroupPermission", sysGroupPermission);
        SysPermission sysPermission = sysPermissionDao.find(new SysPermission().setSysPermissionId(sysGroupPermission.getSysGroupPermissionPid()));
        if (sysPermission == null) {
            return;
        }
        //如果是父权限 则自动删除子权限
        if (sysPermission.getSysPermissionId() > 0) {
            //获取子权限
            List<SysPermission> list = sysPermissionDao.query(new SysPermission().setSysPermissionSup(sysPermission.getSysPermissionId()));
            for (SysPermission permission : list) {
                if (permission == null) {
                    continue;
                }
                sysGroupPermissionDao.deleteAsync(new SysGroupPermission().setSysGroupPermissionGid(sysGroupPermission.getSysGroupPermissionGid()).setSysGroupPermissionPid(permission.getSysPermissionId()));
            }

        }
    }

    //entity 注册到 某个实体类 数据库 新增事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventSave(entity = SysUsers.class, executionBefore = false, depth = 5)
    public void save(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao, Request request) throws Exception {
        log.d("事件触发 DataBaseEventSave", sysUsersLoginLogDao, sysUsers);
        SysUsersLoginLog sysUsersLoginLog = new SysUsersLoginLog();
        sysUsersLoginLog.setSysUsersLoginLogIp(request.getRemoteIp())
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId())
                .setSysUsersLoginLogTime(Tools.getTimestamp().toLocalDateTime())
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

}