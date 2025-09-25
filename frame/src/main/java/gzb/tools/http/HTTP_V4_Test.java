/*
package gzb.tools.http;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
public class HTTP_V4_Test {

    public static void main(String[] args) {
        HTTP_V4 example = new HTTP_V4();

        // 测试GET请求
        String getUrl = "http://192.168.10.101:2082/test/test1?sysUsersId=100";
        Map<String, String> headers = new HashMap<>();
        System.out.println("发送GET请求到: " + getUrl);
        HTTP_V4.requestAsync(getUrl, "GET", null, headers, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("请求失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("响应码: " + response.code());
                    System.out.println("响应体: " + response.body().string());
                } else {
                    System.out.println("请求未成功，响应码: " + response.code());
                }
            }
        });

        // 等待异步请求完成
        try {
            // 实际应用中不需要这样，这里只是为了演示等待异步结果
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 示例：测试POST请求（如需测试可取消注释）
        */
/*
        String postUrl = "http://192.168.10.101:2082/test/post";
        String jsonData = "{\"name\":\"test\",\"value\":123}";
        System.out.println("\n发送POST请求到: " + postUrl);
        example.request(postUrl, "POST", jsonData, headers, null);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        *//*

    }
}*/
