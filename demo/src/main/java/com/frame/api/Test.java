package com.frame.api;

import gzb.frame.DDOS;
import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.lang.reflect.Method;
import java.util.List;

public class Test {

    public static void main(String[] args) throws Exception {
        DDOS.start("springmvc 框架测试 服务器线程1 压测线程6",6,
                "http://192.168.10.101:8081/test/test1?sysUsersAcc=x001",0,null,
                10000 * 50,
                "x001".getBytes());
        DDOS.start("gzb框架测试 服务器线程1 压测线程6",6,
                "http://192.168.10.101:2082/test/test1?sysUsersAcc=x001",0,null,
                10000 * 50,
                "x001".getBytes());

        //http://192.168.10.101:8081/test/test1?sysUsersAcc=
    }
}
