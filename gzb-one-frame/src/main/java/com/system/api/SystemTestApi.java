package com.system.api;

import com.system.entity.GzbUser;
import gzb.frame.annotation.*;
import gzb.frame.factory.ContentType;
import gzb.tools.Config;
import gzb.tools.Tools;

import java.util.ArrayList;
import java.util.List;
@Controller
@Header(item = @HeaderItem(key = ContentType.name,val = ContentType.json))
public class SystemTestApi {
/// /system/gzb/user/hello
    public static final byte[] BYTES = "Hello, World!".getBytes(Config.encoding);

    @EventLoop
    @GetMapping("hello")
    @Header(item = @HeaderItem(key = ContentType.name,val = ContentType.text))
    public byte[] hello() {
        return BYTES;
    }
    @EventLoop
    @GetMapping("json")
    public GzbUser json() {
        GzbUser gzbUser=new GzbUser();
        gzbUser.setGzbUserId(100L);
        gzbUser.setGzbUserAcc("acc");
        gzbUser.setGzbUserPwd("pwd");
        gzbUser.setGzbUserState(1);
        gzbUser.setGzbUserNike("nike");
        gzbUser.setGzbUserId(gzbUser.getGzbUserId()+ Tools.getRandomLong(99999, 10000));
        return gzbUser;
    }
    @EventLoop
    @GetMapping("json/a")
    public Object json(GzbUser gzbUser) {
        if (gzbUser==null) {
            return "gzb user == null";
        }
        gzbUser.setGzbUserId(gzbUser.getGzbUserId()+ Tools.getRandomLong(99999, 10000));
        return gzbUser;
    }
    @EventLoop
    @GetMapping("json/b")
    public GzbUser json(Long gzbUserId,String gzbUserACC,String gzbUserPwd,Integer gzbUserState,String gzbUserNike) {
        if (gzbUserId==null) {
            gzbUserId=100L;
        }
        if (gzbUserACC==null) {
            gzbUserACC="acc";
        }
        if (gzbUserPwd==null) {
            gzbUserPwd="pwd";
        }
        if (gzbUserState==null) {
            gzbUserState=1;
        }
        if (gzbUserNike==null) {
            gzbUserNike="nike";
        }
        GzbUser gzbUser=new GzbUser();
        gzbUser.setGzbUserId(gzbUserId);
        gzbUser.setGzbUserAcc(gzbUserACC);
        gzbUser.setGzbUserPwd(gzbUserPwd);
        gzbUser.setGzbUserState(gzbUserState);
        gzbUser.setGzbUserNike(gzbUserNike);
        gzbUser.setGzbUserId(gzbUser.getGzbUserId()+ Tools.getRandomLong(99999, 10000));
        return gzbUser;
    }
    @EventLoop
    @GetMapping("json/a/array")
    public Object json(int num,GzbUser gzbUser) {
        if (gzbUser==null) {
            return "gzb user == null";
        }
        List<GzbUser>list=new ArrayList<>(num);
        gzbUser.setGzbUserId(gzbUser.getGzbUserId()+ Tools.getRandomLong(99999, 10000));
        for (int i = 0; i < num; i++) {
            list.add(gzbUser);
        }
        return list;
    }
    @EventLoop
    @GetMapping("json/b/array")
    public Object json(int num,Long gzbUserId,String gzbUserACC,String gzbUserPwd,Integer gzbUserState,String gzbUserNike) {
        if (gzbUserId==null) {
            gzbUserId=100L;
        }
        if (gzbUserACC==null) {
            gzbUserACC="acc";
        }
        if (gzbUserPwd==null) {
            gzbUserPwd="pwd";
        }
        if (gzbUserState==null) {
            gzbUserState=1;
        }
        if (gzbUserNike==null) {
            gzbUserNike="nike";
        }
        List<GzbUser>list=new ArrayList<>(num);
        GzbUser gzbUser=new GzbUser();
        gzbUser.setGzbUserId(gzbUserId);
        gzbUser.setGzbUserAcc(gzbUserACC);
        gzbUser.setGzbUserPwd(gzbUserPwd);
        gzbUser.setGzbUserState(gzbUserState);
        gzbUser.setGzbUserNike(gzbUserNike);
        gzbUser.setGzbUserId(gzbUser.getGzbUserId()+ Tools.getRandomLong(99999, 10000));
        for (int i = 0; i < num; i++) {
            list.add(gzbUser);
        }
        return list;
    }



}
