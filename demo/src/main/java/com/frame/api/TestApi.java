package com.frame.api;

import com.frame.dao.SysUsersDao;
import com.frame.entity.SysFile;
import com.frame.entity.SysUsers;
import gzb.frame.DDOS;
import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("test")
public class TestApi {
    @GetMapping("start")
    public Object start(GzbJson gzbJson) throws Exception {
        new Thread() {
            @Override
            public void run() {
                DDOS.start("正常请求", 1,
                        "http://127.0.0.1:20004/test/test2?msg=msg1"
                        , "GET",
                        null,
                        null,
                        null,
                        10000 * 10,
                        "msg1".getBytes(Config.encoding));
                System.out.println("end");
            }
        }.start();

        return gzbJson.success("suc");//由于没有byid方法 通过动态生成sql方式
    }

    @GetMapping("/test0")
    public Object test0(String msg, GzbJson gzbJson) throws Exception {
        return gzbJson.success(msg);
    }

    @GetMapping("/test1")
    public Object test1(String msg, SysUsersDao sysUsersDao) throws Exception {
        List<SysUsers> list = new ArrayList<>();
        SysUsers sysUsers = new SysUsers().setSysUsersId(1L);
        for (int i = 0; i < 100; i++) {
            list.add(sysUsersDao.find("select * from sys_users where sys_users_id = ?",new Object[]{1L}));

        }
        return list;
    }

    @GetMapping("/test2")
    public Object test2(String msg, GzbJson gzbJson) throws Exception {
        return gzbJson.success(msg, new SysFile().setSysFileId(1L).setData("data").setSysFileMd5("def").setSysFilePath("def p")
                .setSysFileTime(Tools.getTimestamp().toLocalDateTime())
                .setSysFileType("def t"));
    }
}
