package gzb;

public class TestServerQPS {
    //进行压力测试
    public static void main(String[] args) throws Exception {
        String url1 = "http://127.0.0.1:2081/test/test1?name=x0005"; //运行 test001 模块的 gzb.Start.java 启动服务器
        String url2 = "http://127.0.0.1:8080/test/test1?name=x0005"; //运行 test_spring_mvc 模块的 gzb.test_spring_mvc.TestSpringMvcApplication.java 启动服务器
        gzb.frame.server.ServerTest.action_test(10*10000, 1,
                0, 0,
                "gzb one", "spring",
                url1, url2,
                "\"code\":\"1\"".getBytes("UTF-8"));
    }
}
