package com.frame.api;

import gzb.frame.DDOS;

public class Test {

    public static void main(String[] args) throws Exception {
//http://192.168.10.101:8081/test/test1?sysUsersAcc=acc
/*        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                DDOS.start("错误请求",2,
                        "http://192.168.10.101:2082/test/test2?sysUsersId=100100100100100100100100100100100100100100100",0,null,
                        10000 * 50,
                        "100".getBytes());
            }
        });
        thread.start();
        http://192.168.10.101:2082/test/test3?sysUsersId=100&sysUsersAcc=acc1&sysUsersPwd=100&sysUsersPhone=100&sysUsersOpenId=100&sysUsersStatus=100&sysUsersType=100&sysUsersRegTime=100&sysUsersStartTime=100&sysUsersEndTime=100&sysUsersPrice=100&sysUsersDesc=100&sysUsersSup=100&sysUsersMail=100&sysUsersGroup=100
        */
        //2082  8081
        DDOS.start("正常请求",6,
                "http://192.168.10.101:8081/test/test4?sysUsersId=100"
                ,0,null,
                10000 * 10,
                "100".getBytes());


    }
}
