package gzb.start;

import gzb.frame.DDOS;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDDOS {

    public static void main(String[] args) throws Exception {
        //172.24.25.195:2080/test2/get2?msg=m01  8080
        Map<String, String> headers = new HashMap<>();
        Map<String, List<File>> files = new HashMap<>();

        // 请求地址
        String url = "http://127.0.0.1:2090/test/api/get1?message=111";
        //url="http://127.0.0.1:2080/test/api0/get1?message=message001";
        //请求方式
        String met = "GET";
        //请求数据
        String data = "";
        //请求数量
        int num = 10000 * 1000;
        //包含这个内容即为响应成功  message001  sysLogId
        byte[] success = "message".getBytes("UTF-8");
        //启动压力测试
        DDOS.start(
                "正常请求",//测试名称
                1,//线程数
                url,// 请求地址
                met,//请求方式
                data,//请求数据
                headers,//请求头
                files,//请求文件 文件上传
                num,//请求数量
                success//成功标志
        );

    }
}
