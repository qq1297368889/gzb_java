package gzb.frame.server.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class StaticResourceHandler {

    /**
     * 过滤路径，防止目录遍历攻击
     *
     * @param requestedPath 用户请求的路径
     * @return 安全的路径，如果存在攻击风险则返回 null
     */
    public static String filterPath(String rootPath, String requestedPath) {
        try {
            // 创建 File 对象并获取规范化路径
            File file = new File(rootPath, requestedPath);
            String canonicalPath = file.getCanonicalPath();

            // 检查规范化后的路径是否在允许的根目录内
            File rootDir = new File(rootPath);
            String rootCanonicalPath = rootDir.getCanonicalPath();

            if (canonicalPath.startsWith(rootCanonicalPath)) {
                return canonicalPath;
            } else {
                return null;
            }
        } catch (IOException e) {
            System.err.println("处理路径时发生错误：" + e.getMessage());
            return null;
        }
    }

    public static boolean handleStaticResource(
            HttpServletRequest req,
            HttpServletResponse res,
            String resourceDir,
            String relativePath) throws IOException {
        if (relativePath==null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        // 1. 构建安全路径
        String path = filterPath(resourceDir, relativePath);
        // 防止路径遍历攻击
        if (path == null) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return false;
        }
        Path path1 = file.toPath();

        // 3. 获取文件元数据
        long fileLength = Files.size(path1);
        Instant lastModified = Files.getLastModifiedTime(path1).toInstant();
        String etag = fileLength + "-" + lastModified.toEpochMilli() ;

        // 4. 缓存验证 (304 Not Modified)
        String ifNoneMatch = req.getHeader("If-None-Match");
        if (ifNoneMatch != null && ifNoneMatch.equals(etag)) {
            long ifModifiedSince = req.getDateHeader("If-Modified-Since");
            if (ifModifiedSince != -1 && lastModified.toEpochMilli() <= ifModifiedSince) {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return true;
            }
        }


        // 5. 准备响应头
        res.setHeader("Cache-Control", "public, max-age=3600");
        res.setHeader("Keep-Alive", "timeout=20");
        res.setHeader("ETag", etag);
        res.setDateHeader("Last-Modified", lastModified.toEpochMilli());
        res.setHeader("Accept-Ranges", "bytes");
        res.setContentType(getContentType(path1));
        // 6. 处理范围请求 (断点续传)
        String rangeHeader = req.getHeader("Range");
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            handleRangeRequest(path1, fileLength, rangeHeader, res);
        } else {
            sendFullResource(path1, fileLength, res);
        }
        return true;
    }

    private static String getContentType(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                // 补充常见类型识别
                String fileName = path.getFileName().toString().toLowerCase();
                if (fileName.endsWith(".js")) return "application/javascript";
                if (fileName.endsWith(".css")) return "text/css";
                if (fileName.endsWith(".html")) return "text/html";

            }
            return contentType != null ? contentType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private static void handleRangeRequest(Path filePath, long fileLength, String rangeHeader, HttpServletResponse res)
            throws IOException {
        // 解析范围请求
        String range = rangeHeader.substring(6);
        String[] ranges = range.split("-");
        long start = 0, end = fileLength - 1;
        try {
            if (ranges.length >= 1) start = Long.parseLong(ranges[0]);
            if (ranges.length == 2) end = Long.parseLong(ranges[1]);
        } catch (NumberFormatException e) {
            res.setHeader("Content-Range", "bytes */" + fileLength);
            res.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }

        // 验证范围有效性
        if (start > end || end >= fileLength) {
            res.setHeader("Content-Range", "bytes */" + fileLength);
            res.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            return;
        }

        // 设置范围响应头
        res.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        res.setHeader("Content-Range",
                String.format("bytes %d-%d/%d", start, end, fileLength));
        res.setContentLengthLong(end - start + 1);

        // 发送部分内容
        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ);
             OutputStream out = res.getOutputStream()) {

            channel.position(start);
            long remaining = end - start + 1;
            ByteBuffer buffer = ByteBuffer.allocate(8192);

            while (remaining > 0) {
                int read = channel.read(buffer);
                if (read <= 0) break;

                buffer.flip();
                out.write(buffer.array(), 0, Math.min(read, (int) remaining));
                buffer.clear();

                remaining -= read;
            }
        }
    }

    private static void sendFullResource(Path filePath, long fileLength,
                                         HttpServletResponse res) throws IOException {
        if (filePath.toFile().isDirectory()){
            return;
        }
        res.setContentLengthLong(fileLength);
        try (InputStream in = Files.newInputStream(filePath);
             OutputStream out = res.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

}