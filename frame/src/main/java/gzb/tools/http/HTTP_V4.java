/*
package gzb.tools.http;
import okhttp3.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

*/
/**
 * 基于JDK8 NIO的异步HTTP客户端
 * 支持GET、POST、PUT、DELETE等方法，以及文件上传
 *//*

public class HTTP_V4 {
    // 单例OkHttpClient实例，内部使用NIO
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    */
/**
     * 异步发送HTTP请求
     * @param url 请求URL
     * @param method HTTP方法
     * @param data 请求体数据
     * @param headers 请求头
     * @param files 要上传的文件
     * @return Call对象，可用于取消请求
     *//*

    public static void requestAsync(String url, String method, String data,
                        Map<String, String> headers, Map<String, List<File>> files,Callback callback) {
        // 构建请求体
        RequestBody requestBody = buildRequestBody(data, files);

        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);

        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 设置请求方法
        switch (method.toUpperCase()) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(requestBody);
                break;
            case "PUT":
                requestBuilder.put(requestBody);
                break;
            case "DELETE":
                if (requestBody != null) {
                    requestBuilder.delete(requestBody);
                } else {
                    requestBuilder.delete();
                }
                break;
            case "PATCH":
                requestBuilder.method("PATCH", requestBody);
                break;
            default:
                throw new IllegalArgumentException("不支持的HTTP方法: " + method);
        }
        client.newCall(requestBuilder.build()).enqueue(callback);
    }

    */
/**
     * 构建请求体，支持普通数据和文件上传
     *//*

    private static RequestBody buildRequestBody(String data, Map<String, List<File>> files) {
        // 没有数据也没有文件
        if ((data == null || data.isEmpty()) && (files == null || files.isEmpty())) {
            return RequestBody.create(null, new byte[0]);
        }

        // 有文件需要上传
        if (files != null && !files.isEmpty()) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            // 添加普通数据
            if (data != null && !data.isEmpty()) {
                builder.addFormDataPart("data", data);
            }

            // 添加文件
            for (Map.Entry<String, List<File>> entry : files.entrySet()) {
                String fieldName = entry.getKey();
                for (File file : entry.getValue()) {
                    builder.addFormDataPart(
                            fieldName,
                            file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file)
                    );
                }
            }

            return builder.build();
        }
        // 只有普通数据
        else {
            return RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    data != null ? data : ""
            );
        }
    }

}
*/
