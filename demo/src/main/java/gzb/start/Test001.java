package gzb.start;

import com.frame.dao.SysUsersDao;
import com.frame.dao.impl.SysUsersDaoImpl;
import com.frame.entity.SysUsers;
import gzb.frame.db.v2.AsyncFactory;
import gzb.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import com.frame.entity.*;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test001 {
    public static void main(String[] args) throws Exception {
        SysUsersDao sysUsersDao=new SysUsersDaoImpl();
        long start=System.currentTimeMillis();
        AtomicInteger success=new AtomicInteger();

        AtomicInteger fail=new AtomicInteger();
        AtomicInteger all_size=new AtomicInteger();
        for (int i = 0; i < 100; i++) {
            new Thread(){
                @Override
                public void run() {
                    int size=20000;
                    List<SysUsers>list=new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        SysUsers sysUsers = new SysUsers();
                        all_size.incrementAndGet();
                        try {
                            sysUsersDao.saveAsync(sysUsers, new Runnable() {
                                @Override
                                public void run() {
                                    fail.incrementAndGet();
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {
                                    success.incrementAndGet();
                                }
                            });
                            list.add(sysUsers);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }.start();
        }

        while (true) {
            Tools.sleep(1000);
            long end=System.currentTimeMillis();
            Log.log.i("耗时",end-start,
                    "成功回调数量",success.get(),
                    "失败回调数量",fail.get(),
                    "前台执行数量", all_size.get(),
                    "后台执行数量",sysUsersDao.getDataBase().asyncFactory.all.get());
        }


    }
    public static void main0(String[] args) throws Exception {
        SysUsersDao sysUsersDao=new SysUsersDaoImpl();
        for (int n = 0; n < 100; n++) {
            Log.log.i(sysUsersDao.query(new SysUsers()).size());
            List<SysUsers>list=new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                SysUsers sysUsers = new SysUsers();
                sysUsersDao.saveAsync(sysUsers);
                list.add(sysUsers);
            }
            Tools.sleep(2000);
            Log.log.i(sysUsersDao.query(new SysUsers()).size());
            for (SysUsers sysUsers : list) {
                sysUsersDao.deleteAsync(sysUsers);
            }
            Tools.sleep(2000);
            Log.log.i(sysUsersDao.query(new SysUsers()).size());
        }


    }

}