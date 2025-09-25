package gzb.frame.netty;

import gzb.frame.netty.entity.FileUploadEntity;
import gzb.tools.Tools;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

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
    private final String rawBody;
    private Map<String, List<Object>> parameters;
    public String path;

    public HTTPRequestParameters(FullHttpRequest request, String rawBody) {
        this.rawBody = rawBody;
        this.request = request;
    }

    /**
     * Lazily parses and returns all parameters from the request.
     * This method is designed for a single-threaded context.
     *
     * @return A map of parameter names to a list of their values.
     */
    public Map<String, List<Object>> getParameters() {
        if (parameters == null) {
            parameters = new HashMap<>();
            QueryStringDecoder urlDecoder = new QueryStringDecoder(request.uri());
            urlDecoder.parameters().forEach((key, valueList) ->
                    parameters.put(key, new ArrayList<>(valueList))
            );
            this.path = urlDecoder.path();

            if (!rawBody.isEmpty()) {
                String contentType = request.headers().get("Content-Type");
                if (contentType != null) {
                    if (contentType.startsWith("application/json")) {
                        parseJson(parameters);
                    } else if (contentType.startsWith("application/x-www-form-urlencoded")) {
                        parseUrlEncoded(parameters);
                    } else if (contentType.startsWith("multipart/form-data")) {
                        parseFormData(parameters);
                    } else {
                        // Default to plain text body
                        parameters.computeIfAbsent("body", k -> new ArrayList<>()).add(rawBody);
                    }
                }
            }
        }
        return parameters;
    }

    public String getRawBody() {
        return rawBody;
    }

    private void parseJson(Map<String, List<Object>> params) {
        try {
            Map<String, Object> jsonMap = Tools.jsonToMap(rawBody);
            for (Map.Entry<String, Object> stringObjectEntry : jsonMap.entrySet()) {
                if (stringObjectEntry.getValue() instanceof Double) {
                    continue;
                } else {

                }
                List<Object> list = params.computeIfAbsent(stringObjectEntry.getKey(), k -> new ArrayList<>());
                String val = stringObjectEntry.getValue().toString();
                int x = val.indexOf(".");

                list.add(stringObjectEntry.getValue());
            }
        } catch (Exception e) {
            System.err.println("Failed to parse JSON body: " + e.getMessage());
            params.clear();
        }
    }

    private void parseUrlEncoded(Map<String, List<Object>> params) {
        try {
            QueryStringDecoder bodyDecoder = new QueryStringDecoder(rawBody, CharsetUtil.UTF_8, false);
            bodyDecoder.parameters().forEach((key, valueList) ->
                    params.computeIfAbsent(key, k -> new ArrayList<>()).addAll(valueList)
            );
        } catch (Exception e) {
            System.err.println("Failed to parse URL-encoded body: " + e.getMessage());
            params.clear();
        }
    }

    private void parseFormData(Map<String, List<Object>> params) {
        HttpPostRequestDecoder bodyDecoder = null;
        try {
            // 使用 HttpDataFactory 和 FullHttpRequest 构造 HttpPostRequestDecoder
            bodyDecoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
            for (InterfaceHttpData data : bodyDecoder.getBodyHttpDatas()) {
                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) data;
                    params.computeIfAbsent(attribute.getName(), k -> new ArrayList<>()).add(attribute.getValue());
                } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUploadEntity fileUploadEntity = new FileUploadEntity((FileUpload) data);
                    tempFile.add(new TempFile(fileUploadEntity.getFile(), System.currentTimeMillis()));
                    params.computeIfAbsent(fileUploadEntity.getName(), k -> new ArrayList<>()).add(fileUploadEntity);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse form data: " + e.getMessage());
            params.clear();
        } finally {
            // 确保释放资源
            if (bodyDecoder != null) {
                bodyDecoder.destroy();
            }
        }
    }
}