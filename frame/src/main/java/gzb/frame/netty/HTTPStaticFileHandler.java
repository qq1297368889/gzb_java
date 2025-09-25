package gzb.frame.netty;

import gzb.tools.Config;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

public class HTTPStaticFileHandler {
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        try {
            // 1. 验证请求
            if (!req.decoderResult().isSuccess()) {
                sendError(ctx, HttpResponseStatus.BAD_REQUEST);
                return;
            }
            if (req.method() != HttpMethod.GET) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                return;
            }

            // 2. 解析请求路径
            String uri = req.uri();
            String path = sanitizeUri(uri);
            if (path == null) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
                return;
            }

            // 3. 检查文件是否存在
            File file = new File(Config.staticDir, path);
            if (!file.exists()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            if (file.isDirectory()) {
                // 目录请求：尝试返回index.html
                file = new File(file, "index.html");
                if (!file.exists()) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
            }

            // 4. 读取文件并发送响应
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            long fileLength = raf.length();

            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            HttpUtil.setContentLength(response, fileLength);
            setContentTypeHeader(response, file);

            if (HttpUtil.isKeepAlive(req)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            ctx.write(response);

            // 5. 发送文件内容（零拷贝）
            ChannelFuture sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
           /*
            sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                @Override
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                    // 进度回调（调试用）
                    if (total < 0) {
                        System.err.println("Transfer progress: " + progress);
                    } else {
                        System.err.println("Transfer progress: " + progress + " / " + total);
                    }
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture future) {
                    System.err.println("Transfer complete.");
                }
            });
*/
            // 6. 发送响应结束标记
            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!HttpUtil.isKeepAlive(req)) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 清理请求路径，防止路径遍历攻击
     */
    private String sanitizeUri(String uri) throws URISyntaxException {
        try {
            uri = URLDecoder.decode(uri, Config.encoding);
        } catch (Exception e) {
            return null;
        }

        // 转换为文件路径
        URI normalized = new URI(uri);
        String path = normalized.getPath();
        if (path == null || path.contains("..") || !path.startsWith("/")) {
            return null;
        }
        return path;
    }

    /**
     * 设置Content-Type响应头
     */
    private void setContentTypeHeader(HttpResponse response, File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".html")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset="+Config.encoding);
        } else if (fileName.endsWith(".css")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css; charset="+Config.encoding);
        } else if (fileName.endsWith(".js")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/javascript");
        } else if (fileName.endsWith(".png")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/png");
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "image/jpeg");
        } else {
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        }
    }

    /**
     * 发送错误响应
     */
    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status + "\r\n", Charset.forName(Config.encoding)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset="+Config.encoding);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}