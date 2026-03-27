package com.frame.test;

import gzb.tools.*;

public class TestAes {

    public static void main(String[] args) throws Exception {
        String url="http://127.0.0.1:2080/hot/update/upload/v1";
        String file0 = "E:\\codes_20220814\\java\\250913_code\\demo\\src\\main\\java\\com\\frame\\test\\test0";
        HotUpdateClient.push(file0,Config.codePwd,Config.codeIv,url);
    }
}
