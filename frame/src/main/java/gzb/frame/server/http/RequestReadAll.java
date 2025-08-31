package gzb.frame.server.http;

import com.google.gson.Gson;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RequestReadAll {
    static Log log = new LogImpl();
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final int BUFFER_SIZE = 8192; // 8KB buffer size for reading chunks
    private static final String tempDir= Tools.pathFormat(Config.get("gzb.system.server.http.tmp.dir",Config.tmpPath()));
    // 优化：Gson是线程安全的，使用静态常量可以避免每次请求都重复创建
    private static final Gson GSON = new Gson();

    public static List<String> read(HttpServletRequest request, Object requestDataObject) {
        Map<String, Object[]> requestData = (Map<String, Object[]>) requestDataObject;
        List<String> paramNames = new ArrayList<>();
        String charset = DEFAULT_CHARSET; // Use a local variable for clarity
        try {
            // 设置请求的字符编码
            request.setCharacterEncoding(charset);
            // 检查请求是否为 multipart/form-data 类型
            boolean isMultipart = request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");

            if (isMultipart) {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    log.d("参数名", part.getName(),
                            "文件名", part.getSubmittedFileName(),
                            "文件类型", part.getContentType(),
                            "字节流长度", part.getInputStream().available());
                    String partName = part.getName();
                    String contentType = part.getContentType();

                    if (contentType != null && !contentType.isEmpty()) {
                        File finalFile = new File(tempDir + part.getSubmittedFileName());

                        // 确保目标目录存在
                        Files.createDirectories(finalFile.getParentFile().toPath());

                        try (InputStream inputStream = part.getInputStream();
                             OutputStream outputStream = new FileOutputStream(finalFile)) {
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        } catch (Exception e) {
                            log.e(e);
                            // 如果写入失败，清理不完整的文件
                            if (finalFile.exists()) {
                                Files.delete(finalFile.toPath());
                            }
                            continue; // 跳过此部分，继续处理下一个
                        }

                        // 将文件对象放入 requestData
                        Object existingValue = requestData.get(partName);
                        if (existingValue instanceof File[]) {
                            File[] newValues = Arrays.copyOf((File[])existingValue, ((File[])existingValue).length + 1);
                            newValues[newValues.length-1] = finalFile;
                            requestData.put(partName, newValues);
                        } else {
                            requestData.put(partName, new File[]{finalFile});
                        }
                        paramNames.add(partName);
                    } else {
                        // 处理常规表单参数
                        String value = getPartValue(part);
                        if (requestData.containsKey(partName)) {
                            Object existingValue = requestData.get(partName);
                            if (existingValue instanceof String[]) {
                                String[] existingValues = (String[]) existingValue;
                                String[] newValues = Arrays.copyOf(existingValues, existingValues.length + 1);
                                newValues[existingValues.length] = value;
                                requestData.put(partName, newValues);
                            } else if (existingValue instanceof String) {
                                // Promote single string to String array
                                requestData.put(partName, new String[]{ (String) existingValue, value });
                            }
                        } else {
                            requestData.put(partName, new String[]{value});
                        }
                        paramNames.add(partName);
                    }
                }
            }
            // 尝试读取 JSON 数据
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                // 注意：这里保留了将整个JSON字符串读入内存的逻辑，以便兼容原始代码行为。
                // 对于非常大的JSON，这仍有内存溢出风险。
                StringBuilder stringBuffer = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), charset))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    String jsonString = stringBuffer.toString();
                    // 优化：使用静态Gson实例
                    Map<String, Object> map=Tools.jsonToMap(jsonString);
                    requestData.put("body", new Object[]{map});
                    for (Map.Entry<String, Object> stringObjectEntry : map.entrySet()) {
                        Object[]objs=requestData.get(stringObjectEntry.getKey());
                        if (objs==null) {
                            objs=new Object[]{stringObjectEntry.getValue()};
                            requestData.put(stringObjectEntry.getKey(),objs);
                        }else{
                            Object[] newValues = Arrays.copyOf(objs, objs.length + 1);
                            newValues[objs.length] = stringObjectEntry.getValue();
                            requestData.put(stringObjectEntry.getKey(),newValues);
                        }
                    }
                } catch (IOException e) {
                   log.e(e);
                }
            }
        } catch (IOException | ServletException e) {
            log.e(e);
        }
        return paramNames;
    }

    // 优化：使用更高效的方式读取Part的文本值
    private static String getPartValue(Part part) throws IOException {
        StringBuilder value = new StringBuilder();
        try (InputStream inputStream = part.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            char[] buffer = new char[BUFFER_SIZE];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                value.append(buffer, 0, charsRead);
            }
        }
        return value.toString();
    }
}
