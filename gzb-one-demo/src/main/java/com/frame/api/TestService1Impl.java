package com.frame.api;

import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.Resource;
import gzb.frame.annotation.Service;
import gzb.tools.log.Log;

@Service
public class TestService1Impl implements TestService1{
    @Resource
    SysUsersDao sysUsersDao;
    @Resource
    Log log;

    @Override
    public void test1() throws Exception {
        log.d("test1",sysUsersDao.query(new SysUsers()));
    }
}
