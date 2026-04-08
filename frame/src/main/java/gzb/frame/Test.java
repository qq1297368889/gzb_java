package gzb.frame;

import gzb.tools.Tools;
import gzb.tools.http.HTTP_V3;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String[] args) throws Exception {
        List<File> list1 = new ArrayList<>();
        List<File> list2 = new ArrayList<>();
        list1.add(new File("E:\\codes_20220814\\logo\\1.jpg"));
        list2.add(new File("E:\\codes_20220814\\logo\\2.jpg"));
        list2.add(new File("E:\\codes_20220814\\logo\\3.jpg"));
        Map<String, List<File>> files = new HashMap<>();
        files.put("file", list1);
        files.put("files", list2);
        String path = "http://127.0.0.1:2080/text?a=0&ss=%E8%A7%A3%E6%9E%90";
        HTTP_V3 httpV3 = new HTTP_V3();
        httpV3.post(path, "a=1&bb=2");
        System.out.println(httpV3.asString());
        httpV3.request(path,"POST","a=1&bb=2",null,files,10000L);
        System.out.println(httpV3.asString());
        Map<String, String>h01=new HashMap<>();
        h01.put("content-type","application/json");
        httpV3.request(path,"POST","{\"a\":\"1\",\"bb\":\"2\"}", h01,null,10000L);
        System.out.println(httpV3.asString());
    }

}