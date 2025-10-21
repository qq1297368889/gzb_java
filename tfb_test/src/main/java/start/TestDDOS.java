package start;

import gzb.frame.DDOS;
import gzb.tools.Config;

public class TestDDOS {

    // 示例用法
    public static void main(String[] args) throws InterruptedException {
// 2082   8081  20004  10003  http://127.0.0.1:20004/plaintext  9001
        //json  plaintext  172.24.25.195  127.0.0.1
        DDOS.start("正常请求",48,
                "http://127.0.0.1:9001/test/test1?msg=m1"
                ,"GET",
                null,
                null,
                null,
                10000 * 200,
                "1".getBytes(Config.encoding));

    }


}
