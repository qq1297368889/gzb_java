package gzb.start;

import com.frame.entity.SysUsers;
import gzb.tools.Tools;

public class Test {

    public static void main(String[] args) {
        String path = Tools.getProjectRoot(Test.class);
        System.setProperty("file.encoding","UTF-8");
        System.setProperty("this.dir", path);
        SysUsers sysUsers = new SysUsers();
        sysUsers.setSysUsersAcc("acc001")
                .setSysUsersPwd("pwd001")
                .setSysUsersType(1L);
        String json = sysUsers.toString();
        System.out.println("json  " + json);
        SysUsers sysUsers1 = new SysUsers();
        sysUsers1.loadJson(json);
        System.out.println("getRootMap  " + sysUsers1.toJson().getRootMap());
        System.out.println("sysUsers1  " + sysUsers1);

    }
}
