package gzb.start;

import gzb.frame.server.ServerTest;

import java.io.IOException;

public class TestServerQPS {
    //进行压力测试
    public static void main(String[] args) throws IOException, InterruptedException {

        String url1 = "http://127.0.0.1:2080/test/test1?name=lisi";
        String url2 = "http://127.0.0.1:8080/test/test1?name=lisi";
        ServerTest.action_test(300000, 1,
                0, 0,
                "自研框架", "spring",
                url1, url2);

    }
}
