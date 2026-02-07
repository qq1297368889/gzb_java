package com.frame.dao.impl;
import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.Service;
import com.frame.dao.SysFileDao;
import com.frame.entity.SysFile;
import com.frame.Frame;
import gzb.frame.factory.ClassTools;

import java.sql.SQLException;

@Service
public class SysFileDaoImpl extends Frame<SysFile> implements SysFileDao{
    public static void main(String[] args) throws Exception {
        new SysFileDaoImpl().test1();
    }
    public void test1() throws Exception {
        log.d(getDataBase());
        log.d(getDataBase().getDataBaseConfig());
        ClassTools.readObject(getDataBase().getDataBaseConfig().getClass());
        log.d(getDataBase().getConnection());
        log.d(query(new SysFile()));
        SysUsersDao sysUsersDao = new SysUsersDaoImpl();
        log.d(sysUsersDao.query(new SysUsers()));
    }
}