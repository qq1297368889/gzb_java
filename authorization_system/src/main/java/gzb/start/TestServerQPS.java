package gzb.start;

public class TestServerQPS {
    //进行压力测试
    public static void main(String[] args) throws Exception {
        String url1 = "http://127.0.0.1:2080/test/test1?name=x0005";
        String url2 = "http://127.0.0.1:8080/test/test1?name=x0005";
        gzb.frame.server.ServerTest.action_test(200*10000, 100,
                7424, 9340,
                "gzb one", "spring",
                url1, url2,"\"code\":\"1\"".getBytes("UTF-8"));
    }
}
