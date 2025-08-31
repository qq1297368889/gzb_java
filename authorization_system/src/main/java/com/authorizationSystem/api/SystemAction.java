package com.authorizationSystem.api;

import com.authorizationSystem.dao.ApplicationDao;
import com.authorizationSystem.entity.Application;
import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import com.frame.system.BaseSystemAction;
import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.frame.annotation.Transaction;
import gzb.tools.log.Log;

//继承 内部实现类 实现 权限管理用户管理 等内置功能 前提必须数据库中包含内置表 sys_xxx
@Controller
@RequestMapping(value = "/system/v1.0.0/", header = "content-type:application/json;charset=UTF-8")
public class SystemAction extends BaseSystemAction {

    ///system/v1.0.0/test/test001
    @GetMapping("test/test001")
    @Transaction
    public Object test001(ApplicationDao applicationDao, Log log) throws Exception {
        log.d("插入前 事务开启",applicationDao.count("select * from application",null));
        applicationDao.save(new Application().setApplicationName("acc0000x1").setApplicationDesc("pwd0000x1"));
        log.d("插入后 事务开启",applicationDao.count("select * from application",null));
        applicationDao.commit();
        log.d("提交后 事务开启",applicationDao.count("select * from application",null));
        applicationDao.save(new Application().setApplicationName("acc0000x1").setApplicationDesc("pwd0000x1"));
        log.d("插入 诱发出错 手动检查数据库验证",applicationDao.count("select * from application",null));

        return applicationDao.query(new Application());
    }
}

