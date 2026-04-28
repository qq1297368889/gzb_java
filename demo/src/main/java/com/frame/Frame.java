package com.frame;

import com.frame.dao.SysUsersDao;
import com.frame.dao.impl.SysUsersDaoImpl;
import com.frame.entity.SysUsers;
import gzb.frame.db.v2.BaseDaoAsync;
import gzb.tools.log.Log;

public class Frame<T> extends BaseDaoAsync<T> {
    public static void main(String[] args) throws Exception {
        SysUsersDao sysUsersDao=new SysUsersDaoImpl();
        Log.log.i(sysUsersDao.query(new SysUsers()));
    }
    //数据库信息 在这里指定
    public Frame() {
        try {
            //对应配置文件里的 db.mysql.xxx 的 xxx
            init("db2");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}