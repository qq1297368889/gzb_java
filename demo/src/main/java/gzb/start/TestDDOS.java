package gzb.start;

import gzb.frame.DDOS;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDDOS {

    public static void main(String[] args) throws Exception {
        //172.24.25.195:2080/test2/get2?msg=m01
        Map<String,String> headers=new HashMap<>();
        Map<String, List<File>> files=new HashMap<>();
        String url="http://127.0.0.1:2081/test2/get2?msg=m01";
        String met="GET";
        String data="";
        int num=10000 * 1000;
        byte[]success="m01".getBytes("UTF-8");
        DDOS.start(
                "正常请求",
                12,
                url
                ,met,
                data,
                headers,
                files,
                num,
                success);

    }
}
