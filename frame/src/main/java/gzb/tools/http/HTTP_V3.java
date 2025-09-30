/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools.http;

import gzb.tools.FileTools;
import gzb.tools.Tools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HTTP_V3 {
    public static void main(String[] args) throws Exception {
        HTTP_V3 tools = new HTTP_V3();
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
        tools.get("http://127.0.0.1:3080/test/test1?sysFileMd5=12345678901234567890123456789001", null);
        System.out.println(tools.asString());
    }

    // 默认配置（保留原配置，仅移除自定义连接池）
    public static final long DEF_TIMEOUT = 10000L;
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36";

    // 【关键】JDK 原生 Keep-Alive 配置（静态代码块，全局生效）
/*
    static {
        System.setProperty("http.keepAlive", "true"); // 启用原生 Keep-Alive
        System.setProperty("http.maxConnections", "1000"); // 原生连接池最大连接数（按需求调整）
        System.setProperty("http.keepAlive.timeout", "30000"); // 连接闲置30秒后关闭（避免无效连接）
        System.setProperty("https.keepAlive", "true"); // 若需支持 HTTPS，补充配置
        System.setProperty("https.maxConnections", "1000");
    }
*/

    static {
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "10000");
    }
    // 响应数据（完全保留）
    private byte[] responseBytes;
    private int responseCode;
    private String responseMessage;
    private Map<String, String> responseHeaders = new HashMap<>();

    // 实例配置（完全保留）
    private long timeout = DEF_TIMEOUT;
    private String charset = "UTF-8";
    private Map<String, String> headers = new HashMap<>();
    private boolean keepAlive = true; // 默认启用 Keep-Alive

    public HTTP_V3() {
        // 初始化默认头（保留原逻辑，仅确保 Connection: Keep-Alive）
        headers.put("User-Agent", USER_AGENT);
        headers.put("Accept", "*/*");
        headers.put("Connection", keepAlive ? "Keep-Alive" : "Close");
        headers.put("Keep-Alive", "timeout=30, max=100"); // 告知服务端保持连接的期望
    }

    /**
     * 设置是否启用Keep-Alive（保留原方法签名）
     */
    public HTTP_V3 setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        headers.put("Connection", keepAlive ? "Keep-Alive" : "Close");
        return this;
    }

    /**
     * 核心HTTP请求方法（移除自定义连接池，改用JDK原生连接）
     */
    public HTTP_V3 request(String url, String method, String data, Map<String, String> headers,
                           Map<String, List<File>> files, Long customTimeout) throws Exception {
        // 重置响应数据（保留原逻辑）
        responseBytes = null;
        responseCode = 0;
        responseMessage = null;
        responseHeaders.clear();
        if (url.startsWith("file:///")) {
            responseBytes= FileTools.readByte(new File(url.substring(8)));
            return this;
        }
        long reqTimeout = customTimeout != null ? customTimeout : this.timeout;
        Map<String, String> requestHeaders = mergeHeaders(headers);
        String finalUrl = buildFinalUrl(url, method, data);

        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(finalUrl);
            connection = (HttpURLConnection) requestUrl.openConnection();
            initConnectionSettings(connection, method, reqTimeout, requestHeaders);
            handleRequestBody(connection, method, data, files, requestHeaders);
            processResponse(connection);

        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                //connection.disconnect();
            }
        }

        return this;
    }

    // ------------------------------ 以下方法仅移除自定义连接池相关逻辑，核心逻辑完全保留 ------------------------------

    /**
     * 合并请求头（完全保留原逻辑）
     */
    private Map<String, String> mergeHeaders(Map<String, String> customHeaders) {
        Map<String, String> result = new HashMap<>(this.headers);
        if (customHeaders != null && !customHeaders.isEmpty()) {
            result.putAll(customHeaders);
        }
        return result;
    }

    /**
     * 构建最终URL（处理GET参数，完全保留原逻辑）
     */
    private String buildFinalUrl(String url, String method, String data) {
        if ("GET".equalsIgnoreCase(method) && data != null && !data.isEmpty()) {
            return url.contains("?") ? url + "&" + data : url + "?" + data;
        }
        return url;
    }

    /**
     * 初始化新连接设置（完全保留原逻辑，移除复用相关分支）
     */
    private void initConnectionSettings(HttpURLConnection connection, String method,
                                        long timeout, Map<String, String> headers) throws ProtocolException {
        connection.setRequestMethod(method);
        connection.setConnectTimeout((int) timeout);
        connection.setReadTimeout((int) timeout);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        // 设置请求头（完全保留）
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 处理请求体（完全保留原逻辑）
     */
    private void handleRequestBody(HttpURLConnection connection, String method,
                                   String data, Map<String, List<File>> files,
                                   Map<String, String> headers) throws Exception {
        boolean hasBody = !"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method);
        if (!hasBody) return;

        connection.setDoOutput(true);

        if (files != null && !files.isEmpty()) {
            handleMultipartFormData(connection, data, files);
        } else {
            handleRegularFormData(connection, data, headers);
        }
    }

    /**
     * 处理响应（完全保留原逻辑）
     */
    private void processResponse(HttpURLConnection connection) throws IOException {
        // 获取响应状态
        responseCode = connection.getResponseCode();
        responseMessage = connection.getResponseMessage();

        // 获取响应头
        Map<String, List<String>> connHeaders = connection.getHeaderFields();
        responseHeaders.clear();
        for (Map.Entry<String, List<String>> entry : connHeaders.entrySet()) {
            if (entry.getKey() != null) {
                responseHeaders.put(entry.getKey(), String.join(";", entry.getValue()));
            }
        }

        // 读取响应内容
        try (InputStream inputStream = responseCode >= 200 && responseCode < 300
                ? connection.getInputStream()
                : connection.getErrorStream()) {

            if (inputStream != null) {
                responseBytes = readInputStream(inputStream);
            } else {
                responseBytes = new byte[0];
            }
        }
    }

    /**
     * 处理multipart/form-data类型的请求（文件上传，完全保留原逻辑）
     */
    private void handleMultipartFormData(HttpURLConnection connection, String data,
                                         Map<String, List<File>> files) throws Exception {
        String boundary = "----WebKitFormBoundary" + UUID.randomUUID().toString().replace("-", "");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream out = connection.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, Charset.forName(charset)), true)) {

            // 写入普通表单数据
            if (data != null && !data.isEmpty()) {
                String[] params = data.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length == 2) {
                        writer.append("--").append(boundary).append("\r\n");
                        writer.append("Content-Disposition: form-data; name=\"")
                                .append(URLDecoder.decode(keyValue[0], charset)).append("\"\r\n\r\n");
                        writer.append(URLDecoder.decode(keyValue[1], charset)).append("\r\n");
                    }
                }
            }

            // 写入文件数据
            for (Map.Entry<String, List<File>> entry : files.entrySet()) {
                String fieldName = entry.getKey();
                for (File file : entry.getValue()) {
                    if (file.exists() && file.isFile()) {
                        writer.append("--").append(boundary).append("\r\n");
                        writer.append("Content-Disposition: form-data; name=\"")
                                .append(fieldName).append("\"; filename=\"")
                                .append(file.getName()).append("\"\r\n");
                        writer.append("Content-Type: application/octet-stream\r\n\r\n");
                        writer.flush();

                        // 写入文件内容
                        try (FileInputStream fis = new FileInputStream(file)) {
                            byte[] buffer = new byte[8192]; // 增大缓冲区
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                out.write(buffer, 0, bytesRead);
                            }
                            out.flush();
                        }
                        writer.append("\r\n");
                    }
                }
            }

            // 写入结束分隔符
            writer.append("--").append(boundary).append("--\r\n");
            writer.flush();
        }
    }

    /**
     * 处理普通表单数据（完全保留原逻辑）
     */
    private void handleRegularFormData(HttpURLConnection connection, String data,
                                       Map<String, String> headers) throws Exception {
        String contentType = headers.getOrDefault("content-type",
                "application/x-www-form-urlencoded; charset=" + charset);
        connection.setRequestProperty("Content-Type", contentType);

        if (data != null && !data.isEmpty()) {
            try (OutputStream out = connection.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(out, charset)) {
                writer.write(data);
                writer.flush();
            }
        }
    }

    /**
     * 高效读取输入流（完全保留原逻辑）
     */
    private byte[] readInputStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(inputStream)) {

            byte[] buffer = new byte[8192]; // 增大缓冲区到8KB
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    // ------------------------------ 工具方法、便捷请求方法（完全保留原签名和逻辑） ------------------------------

    public HTTP_V3 setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public HTTP_V3 setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public HTTP_V3 addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public Map<String, String> getResponseHeaders() {
        return new HashMap<>(responseHeaders);
    }

    public String asString() {
        if (responseBytes == null) {
            return null;
        }
        try {
            return new String(responseBytes, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(responseBytes, Charset.forName(charset));
        }
    }

    public byte[] asByte() {
        return responseBytes != null ? responseBytes.clone() : null;
    }

    // 【移除自定义池的 cleanAllConnections 方法（原生池无需手动清理）】

    // 便捷请求方法（完全保留原签名和逻辑）
    public HTTP_V3 get(String url) throws Exception {
        return get(url, null);
    }

    public HTTP_V3 get(String url, String data) throws Exception {
        return request(url, "GET", data, null, null, null);
    }

    public HTTP_V3 post(String url, String data) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/x-www-form-urlencoded; charset=" + charset);
        return request(url, "POST", data, headers, null, null);
    }

    public HTTP_V3 postJson(String url, String data) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json; charset=" + charset);
        return request(url, "POST", data, headers, null, null);
    }

    public HTTP_V3 postUpload(String url, String data, Map<String, List<File>> files) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "multipart/form-data");
        return request(url, "POST", data, headers, files, null);
    }

    public HTTP_V3 put(String url, String data) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/x-www-form-urlencoded; charset=" + charset);
        return request(url, "PUT", data, headers, null, null);
    }

    public HTTP_V3 delete(String url, String data) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/x-www-form-urlencoded; charset=" + charset);
        return request(url, "DELETE", data, headers, null, null);
    }
}