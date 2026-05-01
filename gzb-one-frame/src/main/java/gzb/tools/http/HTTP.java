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

import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTP {
    public Map<String, String> cookies = new HashMap<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    public Map<String, List<String>> resHeader = new HashMap<String, List<String>>();
    byte[] bytes;
static {

    System.setProperty("http.keepAlive", "true");
    System.setProperty("http.maxConnections", "10000");
}
    public HTTP() {
        // 全局启用 Keep-Alive
        init();
    }

    public HTTP(Map<String, String> cookies, Map<String, String> headers) {
        init();
        if (headers != null) {
            this.headers = headers;
        }
        if (cookies != null) {
            this.cookies = cookies;
        }
    }

    public HTTP(String cookieStr, Map<String, String> headers) {
        init();
        if (cookieStr != null) {
            addCookies(cookieStr);
        }
        if (cookies != null) {
            this.cookies = cookies;
        }
    }


    //设置为 谷歌的协议头 ua
    private HTTP init() {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        //headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
        //headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("Connection", "keep-alive");
        return this;
    }

    //cookie 序列化成字符串
    public String cookieToString() {
        StringBuilder sb = new StringBuilder();
        if (cookies != null) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
        }
        return sb.toString();
    }

    //添加cookie 字符串 支持多个
    public HTTP addCookies(String cookieStr) {
        cookieStr = cookieStr.replaceAll("; ", ";");
        String[] ss1 = cookieStr.split(";");
        cookies = cookies == null ? new HashMap<String, String>() : cookies;
        for (int i = 0; i < ss1.length; i++) {
            String[] ss2 = ss1[i].split("=", 2);
            if (ss2.length == 2) {
                cookies.put(ss2[0], ss2[1]);
            }
        }
        return this;
    }

    //添加cookie 字符串 支持多个
    public HTTP delCookies() {
        cookies = new HashMap<>();
        return this;
    }

    //添加一个 k，v形式的  cookie
    public HTTP addCookies(String k, String v) {
        cookies = cookies == null ? new HashMap<String, String>() : cookies;
        cookies.put(k, v);
        return this;
    }


    /**
     * 参数1 请求地址
     * 参数2 请求参数 post模式下 data有效 get模式 无视data
     * 参数3 访问方式 1 post ,11 post byte, 0get
     */
    public HTTP request(String httpUrl, String data, int met) {
        try {
            request(new URL(httpUrl), data, met);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HTTP request(URL url, String data, int met) throws UnsupportedEncodingException {
        return request(url, data, met, null, null, null);
    }

    /**
     * 参数1 请求地址
     * 参数2 请求参数 post模式下 data有效 get模式 无视data
     * 参数3 访问方式 1 post ,11 post byte 12文件上传, 0 get
     * uploadName 上传文件字段名
     * listFiles上传文件列表
     * 随文件提交参数 map
     */

    public HTTP request0(URL url, String data, int met, String uploadName, List<File> listFiles, Map<String, String[]> map){
        HttpURLConnection conn = null;
        data = data == null ? "" : data;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setDefaultUseCaches(false);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("keep-alive","true");
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            String cookie = cookieToString();
            if (cookie != null && cookie.length() > 0) {
                conn.setRequestProperty("cookie", cookie);
            }
            if (met == 1) {
                //POST
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(data);
                out.flush();
            } else if (met == 11) {
                //POST BYTE
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(data.getBytes(Config.encoding));
                out.flush();
            } else if (met == 12) {
                //POST FILE
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                final String NEWLINE = "\r\n";
                final String PREFIX = "--";
                final String BOUNDARY = "#";
                DataOutputStream dos = null;
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);
                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
                dos = new DataOutputStream(conn.getOutputStream());
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, String[]> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String[] values = map.get(key);
                        String data1 = "";
                        for (String value : values) {
                            data1 += PREFIX + BOUNDARY + NEWLINE +
                                    "Content-Disposition: form-data; "
                                    + "name=\"" + key + "\"" + NEWLINE + NEWLINE + value;
                        }
                        data1 += NEWLINE;
                        dos.write(data1.getBytes(Config.encoding));
                    }
                }

                for (File file : listFiles) {
                    if (file.exists() == false) {
                        continue;
                    }
                    String fileName = file.getName();
                    byte[] body_data = FileTools.readByte(file);
                    if (body_data != null && body_data.length > 0) {
                        dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                        dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                                + uploadName + "\"" + "; filename=\"" + URLEncoder.encode(fileName, "UTF-8")
                                + "\"" + NEWLINE);
                        dos.writeBytes(NEWLINE);
                        dos.write(body_data);
                        dos.writeBytes(NEWLINE);
                    }
                }
                dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE);
                dos.flush();

            } else {
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
            }
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                this.bytes = Tools.readInputStream(inputStream);
                String key = null;
                for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        addCookies(conn.getHeaderField(i));
                    }
                }
                resHeader = conn.getHeaderFields();
            } else {
                this.bytes=null;
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println((met) + " " + url.getHost()+":"+url.getPort()+url.getPath() + "?" + (data == null ? "" : data) + " : " + (this.bytes == null ? "" : toString()));
            return this;
        } finally {
            if (conn!=null){
                conn.disconnect();
            }
        }
    }


    /**
     * 参数1 请求地址
     * 参数2 请求参数 post模式下 data有效 get模式 无视data
     * 参数3 访问方式 1 post ,11 post byte 12文件上传, 0 get 2 PUT 3 DELETE
     * uploadName 上传文件字段名
     * listFiles上传文件列表
     * 随文件提交参数 map
     */
    public HTTP request(URL url, String data, int met, String uploadName, List<File> listFiles, Map<String, String[]> map) {
        HttpURLConnection conn = null;
        URLConnection conn0 = null;

        data = data == null ? "" : data;
        try {
            conn0 = url.openConnection();
            assert conn0 instanceof HttpURLConnection;
            conn = (HttpURLConnection) conn0;
            conn.setConnectTimeout(10000);
            conn.setDefaultUseCaches(false);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("keep-alive", "true");
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            String cookie = cookieToString();
            if (cookie != null && cookie.length() > 0) {
                conn.setRequestProperty("cookie", cookie);
            }
            if (met == 1) {
                //POST
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(data);
                out.flush();
            } else if (met == 11) {
                //POST BYTE
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(data.getBytes("UTF-8"));
                out.flush();
            } else if (met == 12) {
                //POST FILE
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                final String NEWLINE = "\r\n";
                final String PREFIX = "--";
                final String BOUNDARY = "#";
                BufferedInputStream bis = null;
                DataOutputStream dos = null;
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Accept", "*/*");
                conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data; boundary=" + BOUNDARY);
                conn.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
                // 调用HttpURLConnection对象的getOutputStream()方法构建输出流对象；
                dos = new DataOutputStream(conn.getOutputStream());
                // 获取表单中上传控件之外的控件数据，写入到输出流对象（根据HttpWatch提示的流信息拼凑字符串）；
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, String[]> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String[] values = map.get(key);
                        String data1 = "";
                        for (String value : values) {
                            data1 += PREFIX + BOUNDARY + NEWLINE +
                                    "Content-Disposition: form-data; "
                                    + "name=\"" + key + "\"" + NEWLINE + NEWLINE + value;
                        }
                        data1 += NEWLINE;
                        dos.write(data1.getBytes("UTF-8"));
                    }
                }

                for (File file : listFiles) {
                    if (file.exists() == false) {
                        continue;
                    }
                    String fileName = file.getName();
                    byte[] body_data = FileTools.readByte(file);
                    if (body_data != null && body_data.length > 0) {
                        dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
                        dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                                + uploadName + "\"" + "; filename=\"" + URLEncoder.encode(fileName, "UTF-8")
                                + "\"" + NEWLINE);
                        dos.writeBytes(NEWLINE);
                        dos.write(body_data);
                        dos.writeBytes(NEWLINE);
                    }
                }
                dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE);
                dos.flush();

            } else if (met == 2) {
                // PUT
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(data.getBytes("UTF-8"));
                out.flush();
            } else if (met == 3) {
                // DELETE
                conn.setRequestMethod("DELETE");
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
            } else {
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setUseCaches(false);
            }
            conn.connect();
            long a = new DateTime().toStampLong();
            int responseCode = conn.getResponseCode();
            long b = new DateTime().toStampLong();
            //System.out.println(b-a);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                this.bytes = Tools.readInputStream(inputStream);
                String key = null;
                for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        addCookies(conn.getHeaderField(i));
                    }
                }
                resHeader = conn.getHeaderFields();
            } else {
                this.bytes = null;
                System.out.println("请求失败，错误码：" + responseCode);
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println((met) + " " + url.getHost() + ":" + url.getPort() + url.getPath() + "?" + (data == null ? "" : data) + " : " + (this.bytes == null ? "" : toString()));
            return this;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public HTTP httpGet(String url) {
        return request(url, null, 0);
    }

    public byte[] httpPost(String url, String data) {
        request(url, data, 1);
        return this.bytes;
    }

    public byte[] httpPostByte(String url, String data) {
        headers.put("content-type", "application/json");
        request(url, data, 11);
        return this.bytes;
    }

    public String httpGetString(String url) {
        httpGet(url);
        if (this.bytes == null) {
            return null;
        }
        return this.toString();
    }

    public String httpPostString(String url, String data) {
        httpPost(url, data);
        if (this.bytes == null) {
            return null;
        }
        return this.toString();
    }

    public String httpPostByteString(String url, String data) {
        httpPostByte(url, data);
        if (this.bytes == null) {
            return null;
        }
        return this.toString();
    }


    @Override
    public String toString() {
        try {
            return new String(this.bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] toByte() {
        return this.bytes;
    }

}
