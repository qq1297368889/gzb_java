package com.frame.api;

import com.frame.dao.SysUsersDao;
import com.frame.dao.impl.SysUsersDaoImpl;
import com.frame.entity.SysFile;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.*;
import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassTools;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;
import gzb.tools.log.LogThread;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/test2")
public class Test2 {
    private static final ClassLoadingMXBean classLoadingMXBean= ManagementFactory.getClassLoadingMXBean();
    public static void main(String[] args) throws Exception {
        String code =FileTools.readString(new File("E:\\codes_20220814\\java\\250913_code\\demo\\src\\main\\java\\com\\frame\\api\\Test2.java"));
        ClassLoad.compileJavaCode(code);
        System.out.println("GC前类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
        System.gc();
        Tools.sleep(1000);
        System.out.println("GC1秒后类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
        while (true) {
            Tools.sleep(1000);
            System.out.println("----------------");
            for (int i = 0; i < 100; i++) {
                ClassLoad.compileJavaCode(code);
            }

            for (int i = 0; i < 5; i++) {
                System.out.println("GC前类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
                System.gc();
                Tools.sleep(1000);
                System.out.println("GC1秒后类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
            }
        }




    }
    //test2/get1?msg=hello
    //@Header(item = {@HeaderItem(key = "content-type", val = "text/html;charset=UTF-8")})
    @GetMapping("get1")
    public Object get1(String msg, GzbJson gzbJson) throws Exception {
        System.out.println("GC前类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
        System.gc();
        Tools.sleep(1000);
        System.out.println("GC1秒后类数量"+classLoadingMXBean.getLoadedClassCount()+"  被卸载类数量"+classLoadingMXBean.getUnloadedClassCount());
        return gzbJson.success(msg);
    }
    @GetMapping("/get2")
    public Object get2(String msg) {
        SysFile sysFile = new SysFile();
        sysFile.setSysFileId(100L);
        sysFile.setSysFileMd5("md5_"+msg);
        sysFile.setSysFilePath("path_"+msg);
        sysFile.setSysFileType("type_"+msg);
        return sysFile;
    }
    //调用 service对象
    @GetMapping("/get3")
    public Object get3(String msg, TestService1 testService1, SysUsersDao sysUsersDao, Log log) throws Exception {
        SysFile sysFile = new SysFile();
        sysFile.setSysFileId(100L);
        sysFile.setSysFileMd5("md5_"+msg);
        sysFile.setSysFilePath("path_"+msg);
        sysFile.setSysFileType("type_"+msg);
        testService1.test1();
        String sql="";
        List<Object[]> list_parameter=new ArrayList<>();
        sysUsersDao.getDataBase().runSqlBatch(sql,list_parameter);
        SysUsers sysUsers=new SysUsers().setSysUsersAcc("a01");
        sysUsersDao.saveAsync(sysUsers, new Runnable() {
            @Override
            public void run() {
                log.w("执行失败，触发回调",sysUsers);
            }
        });

        return sysFile;
    }





}
