package gzb.start;

import gzb.tools.Tools;

import java.io.IOException;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws IOException {
        System.out.println(("{\"code\":\"4\",\"time\":\"1774237654314\",\"message\":\"未登录或登录失效\",\"url\":\"login.html\"}").getBytes().length);
        Socket socket = new Socket("127.0.0.1", 2080);

        // 【正确】全部用 \r\n 结尾 + 最后多一个 \r\n 结束请求头
        String request =
                "GET /system/v1.0.0/world/list?id=1 HTTP/1.1\r\n" +
                        "Host: 127.0.0.1\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"; // 必须有空行！

        socket.getOutputStream().write(request.getBytes());
        socket.getOutputStream().flush();
        Tools.sleep(100);
        byte[] bytes = new byte[2048];
        int len = socket.getInputStream().read(bytes);
        System.out.println(new String(bytes, 0, len));

        socket.close();
    }
}