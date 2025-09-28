
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPV2 {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36";
    private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9";
    private static final String CONNECTION_KEEP_ALIVE = "keep-alive";//keep-alive
    private static final String COOKIE_HEADER = "cookie";
    private static final String SET_COOKIE_HEADER = "set-cookie";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    private static final String CRLF = "\r\n";
    private static final String PREFIX = "--";
    private static final String BOUNDARY = "#";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String FILENAME_ATTRIBUTE = "filename";
    private static final String ACCEPT_ALL = "*/*";
    private static final String ACCEPT_ENCODING = "gzip, deflate";
    private static final String CACHE_CONTROL = "no-cache";

    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_DELETE = "DELETE";

    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    public Map<String, String> cookies = new HashMap<>();
    public Map<String, String> headers = new HashMap<>();
    public Map<String, List<String>> resHeader = new HashMap<>();
    private byte[] bytes;

    static {
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "10000");
    }

    public HTTPV2() {
        initHeaders();
    }

    public HTTPV2(Map<String, String> cookies, Map<String, String> headers) {
        initHeaders();
        if (headers != null) {
            this.headers.putAll(headers);
        }
        if (cookies != null) {
            this.cookies.putAll(cookies);
        }
    }

    public HTTPV2(String cookieStr, Map<String, String> headers) {
        initHeaders();
        if (cookieStr != null) {
            addCookies(cookieStr);
        }
        if (headers != null) {
            this.headers.putAll(headers);
        }
    }

    private HTTPV2 initHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("user-agent", USER_AGENT);
        headers.put("Accept", ACCEPT);
        headers.put("Connection", CONNECTION_KEEP_ALIVE);
        return this;
    }

    public String cookieToString() {
        StringBuilder sb = new StringBuilder();
        if (cookies != null) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
        }
        return sb.toString();
    }

    public HTTPV2 addCookies(String cookieStr) {
        cookieStr = cookieStr.replaceAll("; ", ";");
        String[] ss1 = cookieStr.split(";");
        if (cookies == null) {
            cookies = new HashMap<>();
        }
        for (String s : ss1) {
            String[] ss2 = s.split("=", 2);
            if (ss2.length == 2) {
                cookies.put(ss2[0], ss2[1]);
            }
        }
        return this;
    }

    public HTTPV2 delCookies() {
        cookies = new HashMap<>();
        return this;
    }

    public HTTPV2 addCookies(String k, String v) {
        if (cookies == null) {
            cookies = new HashMap<>();
        }
        cookies.put(k, v);
        return this;
    }

    public HTTPV2 request(String httpUrl, String data, int met) {
        try {
            return request(new URL(httpUrl), data, met, null, null, null);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return this;
        }
    }

    public HTTPV2 request(URL url, String data, int met) throws UnsupportedEncodingException {
        return request(url, data, met, null, null, null);
    }

    /**
     * met:
     * 0 GET
     * 1 POST
     * 11 POST BYTE
     * 12 POST FILE
     * 2 PUT
     * 3 DELETE
     */
    public HTTPV2 request(URL url, String data, int met, String uploadName, List<File> listFiles, Map<String, String[]> map) {
        HttpURLConnection conn = null;
        URLConnection conn0 = null;
        data = data == null ? "" : data;
        try {
            conn0 = url.openConnection();
            if (!(conn0 instanceof HttpURLConnection)) {
                System.err.println("不支持的协议: " + url.getProtocol());
                return this;
            }
            conn = (HttpURLConnection) conn0;
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setDefaultUseCaches(false);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty(CONNECTION_KEEP_ALIVE, "true");
            if (met==11) {
                if (headers==null) {
                    headers=new HashMap<>();
                }
                headers.put("content-type","application/json;charset="+Config.encoding);
            }
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            String cookie = cookieToString();
            if (cookie != null && !cookie.isEmpty()) {
                conn.setRequestProperty(COOKIE_HEADER, cookie);
            }


            switch (met) {
                case 1: // POST
                    conn.setRequestMethod(METHOD_POST);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
                        out.print(data);
                        out.flush();
                    }
                    break;
                case 11: // POST BYTE
                    conn.setRequestMethod(METHOD_POST);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                        out.write(data.getBytes(Config.encoding));
                        out.flush();
                    }
                    break;
                case 12: // POST FILE
                    conn.setRequestMethod(METHOD_POST);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Connection", CONNECTION_KEEP_ALIVE);
                    conn.setRequestProperty("Accept", ACCEPT_ALL);
                    conn.setRequestProperty("Accept-Encoding", ACCEPT_ENCODING);
                    conn.setRequestProperty("Cache-Control", CACHE_CONTROL);
                    conn.setRequestProperty("Content-Type", CONTENT_TYPE_MULTIPART + "; boundary=" + BOUNDARY);
                    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");

                    try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                        if (map != null && !map.isEmpty()) {
                            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                                String key = entry.getKey();
                                String[] values = entry.getValue();
                                for (String value : values) {
                                    dos.writeBytes(PREFIX + BOUNDARY + CRLF);
                                    dos.writeBytes("Content-Disposition: form-data; " + NAME_ATTRIBUTE + "=\"" + key + "\"" + CRLF + CRLF);
                                    dos.write(value.getBytes(Config.encoding));
                                    dos.writeBytes(CRLF);
                                }
                            }
                        }

                        if (listFiles != null) {
                            for (File file : listFiles) {
                                if (!file.exists()) continue;
                                String fileName = file.getName();
                                byte[] body_data = FileTools.readByte(file);
                                if (body_data != null && body_data.length > 0) {
                                    dos.writeBytes(PREFIX + BOUNDARY + CRLF);
                                    dos.writeBytes("Content-Disposition: form-data; " +
                                            NAME_ATTRIBUTE + "=\"" + uploadName + "\"; " + FILENAME_ATTRIBUTE + "=\"" +
                                            URLEncoder.encode(fileName, Config.encoding.name()) + "\"" + CRLF);
                                    dos.writeBytes(CRLF);
                                    dos.write(body_data);
                                    dos.writeBytes(CRLF);
                                }
                            }
                        }
                        dos.writeBytes(PREFIX + BOUNDARY + PREFIX + CRLF);
                        dos.flush();
                    }
                    break;
                case 2: // PUT
                    conn.setRequestMethod(METHOD_PUT);
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                        out.write(data.getBytes(Config.encoding));
                        out.flush();
                    }
                    break;
                case 3: // DELETE
                    conn.setRequestMethod(METHOD_DELETE);
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    break;
                default: // GET
                    conn.setRequestMethod(METHOD_GET);
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setUseCaches(false);
                    break;
            }
            conn.connect();
            long startTime = System.currentTimeMillis();
            int responseCode = conn.getResponseCode();
            long endTime = System.currentTimeMillis();
            //System.out.println(endTime - startTime);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = conn.getInputStream()) {
                    this.bytes = Tools.readInputStream(inputStream);
                }
                String key;
                for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
                    if (key.equalsIgnoreCase(SET_COOKIE_HEADER)) {
                        addCookies(conn.getHeaderField(i));
                    }
                }
                resHeader = conn.getHeaderFields();
            } else {
                this.bytes = null;
                //System.out.println("请求失败，错误码：" + responseCode);
            }
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println((met) + " " + url.getHost() + ":" + url.getPort() + url.getPath() + "?" + (data == null ? "" : data) + " : " + (this.bytes == null ? "" : toString()));
            return this;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public HTTPV2 httpGet(String url) {
        return request(url, null, 0);
    }

    public byte[] httpPost(String url, String data) {
        request(url, data, 1);
        return this.bytes;
    }

    public byte[] httpPostByte(String url, String data) {
        headers.put("content-type", CONTENT_TYPE_JSON);
        request(url, data, 11);
        return this.bytes;
    }

    public String httpGetString(String url) {
        httpGet(url);
        return bytesToString();
    }

    public String httpPostString(String url, String data) {
        httpPost(url, data);
        return bytesToString();
    }

    public String httpPostByteString(String url, String data) {
        httpPostByte(url, data);
        return bytesToString();
    }

    @Override
    public String toString() {
        return bytesToString();
    }

    public byte[] toByte() {
        return this.bytes;
    }

    private String bytesToString() {
        if (this.bytes == null) {
            return null;
        }
        return new String(this.bytes, Config.encoding);
    }
}