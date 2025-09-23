package gzb.frame.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import java.net.SocketException;

public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 统一处理底层IO异常
        if (isIgnorableNetworkError(cause)) {
            // 关闭异常通道，避免资源泄漏
            if (ctx.channel().isActive()) {
                ctx.close();
            }
            return; // 不再传播
        }

        // 其他异常继续传播（让上层处理器处理）
        ctx.fireExceptionCaught(cause);
    }

    /**
     * 判断是否为可忽略的网络错误（连接重置、断连等）
     */
    private boolean isIgnorableNetworkError(Throwable cause) {
        // 穿透包装异常（如ExecutionException可能包装着底层IO异常）
        Throwable rootCause = cause;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }

        // 处理SocketException及其子类（包含Connection reset等）
        if (rootCause instanceof SocketException) {
            String msg = rootCause.getMessage();
            return msg != null && (
                    msg.contains("Connection reset") ||
                            msg.contains("Broken pipe") ||
                            msg.contains("Connection aborted")
            );
        }

        // 处理其他IO异常（如EOFException等）
        if (rootCause instanceof IOException) {
            String msg = rootCause.getMessage();
            return msg != null && (
                    msg.contains("Connection closed by peer") ||
                            msg.contains("Connection reset by peer")
            );
        }

        return false;
    }
}
