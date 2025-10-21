package gzb.start;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.frame.dao.SysUsersDao;
import com.frame.dao.impl.SysUsersDaoImpl;
import com.frame.entity.SysUsers;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.lang.management.ThreadMXBean;
import java.sql.Timestamp;
import java.util.List;

public class test21 {
    public static void main(String[] args) throws Exception {
        Log log =new LogImpl();
        SysUsers sysUsers = new SysUsers();
        SysUsersDao sysUsersDao=new SysUsersDaoImpl();
        sysUsers.setSysUsersId(1L).setSysUsersAcc(Tools.getRandomString(12));
        sysUsersDao.saveAsync(sysUsers,()->{
            log.w("保存失败回调执行了",sysUsers);
        });
        Tools.sleep(1000);
        sysUsersDao.saveAsync(sysUsers);

    }
}
