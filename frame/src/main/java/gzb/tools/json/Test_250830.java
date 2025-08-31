package gzb.tools.json;

import com.frame.entity.SysUsers;
import gzb.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test_250830 {
    public static void main(String[] args) {

        class zuobiao{
            int x=0;
            int y=0;
            String name="def";
        }
        System.setProperty("this.dir", Tools.getProjectRoot(ResultImpl.class));
        Result result = new ResultImpl();
        result.success("成功", new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", 2);
            put("key3", true);
        }});
        List<String>list=new ArrayList<>();
        list.add("list001");
        list.add("list002");
        list.add("list003");
        result.set("listData",list);
        result.set("page",1);
        result.set("size","1");
        result.set("zuobiao",new zuobiao());
        result.set("sysUsers",new SysUsers().setSysUsersAcc("123").setSysUsersPwd("p001"));
        SysUsers sysUsers = result.getObject("sysUsers");
        System.out.println(result);
        System.out.println(sysUsers);
        System.out.println(sysUsers.getSysUsersAcc());
        System.out.println(result.getInteger("page"));
        System.out.println(result.getString("page"));
        System.out.println(result.getInteger("size"));
        System.out.println(result.getString("size"));
    }

}
