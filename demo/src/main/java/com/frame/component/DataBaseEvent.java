package com.frame.component;

import com.frame.dao.SysUsersDao;
import com.frame.dao.SysUsersLoginLogDao;
import com.frame.entity.SysUsers;
import com.frame.entity.SysUsersLoginLog;
import gzb.frame.annotation.*;
import gzb.tools.DateTime;
@DataBaseEventFactory
public class DataBaseEvent {

    @Transaction
    @DataBaseEventSave("sys_users")
    public void save(SysUsers sysUsers, SysUsersLoginLogDao sysUsersLoginLogDao, SysUsersDao sysUsersDao) throws Exception {
        sysUsersLoginLogDao.saveAsync(new SysUsersLoginLog()
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId())
                .setSysUsersLoginLogTime(new DateTime().toString())
                .setSysUsersLoginLogDesc("用户创建")
        );
    }

    @Transaction
    @DataBaseEventDelete("sys_users")
    public void delete(SysUsers sysUsers,SysUsersLoginLogDao sysUsersLoginLogDao)throws Exception {
        sysUsersLoginLogDao.deleteAsync(new SysUsersLoginLog()
                .setSysUsersLoginLogUid(sysUsers.getSysUsersId()));
    }
    @Transaction
    @DataBaseEventUpdate("sys_users")
    public void update(SysUsers sysUsers,SysUsersLoginLogDao sysUsersLoginLogDao)throws Exception {

    }
    @DataBaseEventSelect("sys_users")
    public void select(SysUsers sysUsers,SysUsersLoginLogDao sysUsersLoginLogDao)throws Exception {

    }
}
