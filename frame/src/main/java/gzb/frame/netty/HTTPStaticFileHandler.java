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

package gzb.frame.netty;

import gzb.frame.factory.ClassTools;
import gzb.frame.netty.tools.HTTPTools;
import gzb.tools.Config;
import gzb.tools.log.Log;
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
    public void channelRead0(ChannelHandlerContext ctx, HTTPTools.Entity entity) {
        RandomAccessFile raf = null;
        try {
            if (entity.method != 0) {
                sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                return;
            }
            String uri = entity.getURL();
            String path = sanitizeUri(uri);
            if (path == null) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
                return;
            }

            File file = new File(Config.staticDir, path);
            if (!file.exists()) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
            if (file.isDirectory()) {
                file = new File(file, "index.html");
                if (!file.exists()) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
            }

            raf = new RandomAccessFile(file, "r");
            long fileLength = raf.length();

            StringBuilder sb = new StringBuilder(256);
            sb.append("HTTP/1.1 200 OK\r\n");
            sb.append("Content-Length: ").append(fileLength).append("\r\n");
            sb.append("Content-Type: ").append(getContentType(file)).append("\r\n");

            boolean keepAlive = entity.isKeep();
            if (keepAlive) {
                sb.append("Connection: keep-alive\r\n");
            } else {
                sb.append("Connection: close\r\n");
            }
            sb.append("\r\n"); // Header 结束符

            byte[] headerBytes = sb.toString().getBytes(Config.encoding);
            ctx.write(Unpooled.wrappedBuffer(headerBytes));
            ChannelFuture sendFileFuture = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength));

            ChannelFuture lastContentFuture = ctx.writeAndFlush(Unpooled.EMPTY_BUFFER);
            if (!keepAlive) {
                lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }

        } catch (Exception e) {
            Log.log.e(e);
            if (raf != null) { try { raf.close(); } catch (Exception ignored) {} }
            if (ctx.channel().isActive()) {
                sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
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
            String uri =req.uri();

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

    /**
     * 清理请求路径，防止路径遍历攻击
     */
    private String sanitizeUri(String uri) throws URISyntaxException {
        try {
            uri = URLDecoder.decode(uri, Config.encoding.name());
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
    private String getContentType(File file) {
        String fileName = file.getName();
        if (fileName.endsWith(".html")) return "text/html; charset=" + Config.encoding;
        if (fileName.endsWith(".css")) return "text/css; charset=" + Config.encoding;
        if (fileName.endsWith(".js")) return "application/javascript";
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        return "application/octet-stream";
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
                Unpooled.copiedBuffer("Failure: " + status + "\r\n", (Config.encoding)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset="+Config.encoding);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}