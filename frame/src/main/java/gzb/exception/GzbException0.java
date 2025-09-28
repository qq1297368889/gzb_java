package gzb.exception;

public class GzbException0 extends RuntimeException {
    // 构造函数：禁用写入栈追踪和启用抑制
    public GzbException0(String message) {
        super(message, null, false, false);
    }

    // 覆盖 fillInStackTrace，阻止 JVM 填充栈追踪。
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null; // 或者 return this;
    }
}
