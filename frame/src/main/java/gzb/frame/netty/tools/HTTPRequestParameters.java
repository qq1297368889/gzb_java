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

package gzb.frame.netty.tools;

import gzb.entity.FileUploadEntity;
import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

class TempFile {
    public File file;
    public Long time;

    public TempFile(File file, Long time) {
        this.file = file;
        this.time = time;
    }
}

public class HTTPRequestParameters {
    private static final ConcurrentLinkedQueue<TempFile> tempFile = new ConcurrentLinkedQueue<>();
    private static final Logger log = LoggerFactory.getLogger(HTTPRequestParameters.class);

    static {
        Thread thread = new Thread(() -> {
            int sleep0 = 1000 * 10;
            int mm = 120 * 1000;
            long max = 0;
            while (true) {
                try {
                    TempFile tempFile1 = tempFile.peek();
                    if (tempFile1 != null) {
                        max = System.currentTimeMillis() - tempFile1.time;
                        if (max >= mm) {
                            tempFile.poll();
                            //System.out.println("delete" + tempFile1.file.getPath());
                            if (tempFile1.file.exists()) {
                                tempFile1.file.delete();
                            }
                        } else {
                            Tools.sleep(mm - max);
                            continue;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();//这玩意不允许出错
                }
                Tools.sleep(sleep0);
            }
        });
        thread.setName("HTTPRequestParameters.TempFile-Cleaner");
        thread.setDaemon(true);
        thread.start();
    }

    private final FullHttpRequest request;
    private byte[] body;
    private Map<String, List<Object>> parameters;
    public String path;

    public HTTPRequestParameters(FullHttpRequest request) {
        this.request = request;
        getParameters();
    }


    public byte[] readByte() {
        if (body != null) {
            return body;
        }
        ByteBuf content = request.content(); //FullHttpRequest
        this.body = new byte[content.readableBytes()];
        content.getBytes(content.readerIndex(), this.body);
        return body;
    }

    public String readString() {
        if (this.body == null) {
            readByte();
        }
        return new String(this.body, Config.encoding);
    }

    public String webPathFormat() {
        this.path = ClassTools.webPathFormat(this.path);
        return this.path;
    }

    public Map<String, List<Object>> getParameters() {
        if (parameters == null) {
            GzbThreadLocal.Entity entity = GzbThreadLocal.context.get();
            if (parameters == null) {
                parameters = new HashMap<>();
                entity.requestMap = parameters;
            } else {
                parameters.clear();
            }
            String url = request.uri();
            this.path = OptimizedParameterParser.parseUrlEncoded(url, parameters, false);
            String contentType = request.headers().get("Content-Type");
            if (contentType != null) {
                if (contentType.startsWith("application/json")) {
                    parseJson(parameters);
                } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
                    OptimizedParameterParser.parseUrlEncoded(readString(), parameters, true);
                } else if (contentType.startsWith("multipart/form-data")) {
                    parseFormData(parameters);
                } else {
                    // Default to plain text body
                    parameters.computeIfAbsent("body", k -> new ArrayList<>(1)).add(readString());
                }
            }
        }
        return parameters;
    }

    private void parseJson(Map<String, List<Object>> params) {
        Tools.jsonToMap(readString(), params);
    }

    private void parseUrlEncoded(Map<String, List<Object>> params) {
        try {
            QueryStringDecoder bodyDecoder = new QueryStringDecoder(readString(), Config.encoding, false);
            bodyDecoder.parameters().forEach((key, valueList) ->
                    params.computeIfAbsent(key, k -> new ArrayList<>(2)).addAll(valueList)
            );
        } catch (Exception e) {
            System.err.println("Failed to parse URL-encoded body: " + e.getMessage());
            params.clear();
        }
    }

    private void parseFormData(Map<String, List<Object>> params) {
        HttpPostRequestDecoder bodyDecoder = null;
        try {
            bodyDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(true), request);
            for (InterfaceHttpData data : bodyDecoder.getBodyHttpDatas()) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) data;
                    params.computeIfAbsent(attribute.getName(), k -> new ArrayList<>(2)).add(attribute.getValue());
                } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUploadEntity fileUploadEntity = new FileUploadEntity((FileUpload) data);
                    tempFile.add(new TempFile(fileUploadEntity.getFile(), System.currentTimeMillis()));
                    params.computeIfAbsent(fileUploadEntity.getName(), k -> new ArrayList<>(2)).add(fileUploadEntity);
                }
            }
        } catch (Exception e) {
            Log.log.e("parseFormData 出现错误", e);
            params.clear();
        } finally {
            // 确保释放资源
            if (bodyDecoder != null) {
                bodyDecoder.destroy();
            }
        }
    }
}