package gzb.frame;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 【非公开类】标准异常：用于性能基线对比。
 */
class StandardException extends RuntimeException {
    public StandardException(String message) {
        super(message);
    }
}

/**
 * 【非公开类】零栈追踪异常：fillInStackTrace() 覆盖，返回 null，极致性能。
 */
class ZeroStackException extends RuntimeException {

    // 禁用写入栈追踪和启用抑制，实现最快构造
    public ZeroStackException(String message) {
        super(message, null, false, false);
    }

    // 核心优化：覆盖此方法，阻止 JVM 填充栈追踪。
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}

/**
 * 【非公开类】浅层追踪异常：禁用栈追踪，但允许手动记录上下文。
 */
class ShallowTraceException extends RuntimeException {

    private final String context; // 用于存储 "端点:参数" 这样的关键信息

    public ShallowTraceException(String message, String context) {
        // 禁用写入栈追踪和启用抑制
        super(message, null, false, false);
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    // 核心优化：覆盖此方法，阻止 JVM 填充栈追踪。
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
/**
 * 【非公开类】定制栈追踪异常：只保留指定层数的栈信息。
 */
class CustomTraceException extends RuntimeException {

    // 存储手动裁剪后的栈追踪
    private StackTraceElement[] customStack;

    public CustomTraceException(String message) {
        // 禁用写入栈追踪和启用抑制
        super(message, null, false, false);
    }

    // 核心：设置裁剪后的栈追踪
    public void setCustomStackTrace(StackTraceElement[] customStack) {
        this.customStack = customStack;
        // 覆盖父类的 setStackTrace，将定制的栈设置进去
        super.setStackTrace(customStack);
    }

    // 覆盖 fillInStackTrace，阻止 JVM 填充完整栈
    @Override
    public synchronized Throwable fillInStackTrace() {

        return null;
    }

    // 覆盖 getStackTrace，返回定制的栈
    @Override
    public StackTraceElement[] getStackTrace() {
        return customStack != null ? customStack : new StackTraceElement[0];
    }
}


public class ExceptionPerformanceTest {
    private static final int ITERATIONS = 500_000;
    private static final int TRACE_DEPTH = 3; // 你需要的追踪深度

    // -----------------------------------------------------
    // 定制栈追踪的核心逻辑
    // -----------------------------------------------------

    /**
     * 手动获取并裁剪栈追踪信息。
     * @param depth 想要的栈追踪深度。
     * @param skipFrames 要跳过的帧数（即本方法和其调用者，通常是 1-2 帧）。
     */
    private static StackTraceElement[] getPartialStackTrace(int depth, int skipFrames) {
        // 获取当前线程的完整栈追踪。这是耗时操作！
        StackTraceElement[] fullStack = Thread.currentThread().getStackTrace();

        // 计算要开始的索引：跳过 getStackTrace, getPartialStackTrace, 以及其调用者
        int startIndex = 1 + skipFrames; // 至少跳过 1 帧

        if (startIndex >= fullStack.length) {
            return new StackTraceElement[0];
        }

        // 计算实际要复制的长度
        int actualLength = Math.min(depth, fullStack.length - startIndex);

        // 复制裁剪后的数组
        return Arrays.copyOfRange(fullStack, startIndex, startIndex + actualLength);
    }

    // -----------------------------------------------------
    // 模拟业务逻辑调用链
    // -----------------------------------------------------

    public static void deepCall(int type) {
        methodA(type);
    }

    private static void methodA(int type) {
        methodB(type);
    }

    private static void methodB(int type) {
        methodC(type);
    }

    private static void methodC(int type) {
        // 根据类型抛出不同的异常
        switch (type) {
            case 0:
                throw new StandardException("标准异常");
            case 1:
                throw new ZeroStackException("零栈追踪");
            case 2:
                // 模拟在参数适配器层手动附加关键信息
                throw new ShallowTraceException("参数转换失败", "端点:/test1, 参数:userID");
            case 3:
                // 模拟在参数适配器层手动附加关键信息
                throw new CustomTraceException("指定层数");
        }
    }

    // -----------------------------------------------------
    // 性能测试方法
    // -----------------------------------------------------

    public static void runTest(String name, int type) {
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            try {
                deepCall(type);
            } catch (StandardException | ZeroStackException | ShallowTraceException e) {
                // 仅捕获，模拟调度器处理
            }
        }

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        long averageTime = totalTime / ITERATIONS;

        System.out.printf("【%-15s】: %5d 纳秒 (%.3f 微秒)\n",
                name, averageTime, averageTime / 1000.0);
    }

    public static void printTrace(String name, int type) {
        try {
            deepCall(type);
        } catch (Throwable e) {
            System.out.println("\n--- " + name + " 实际追踪输出 ---");
            if (e instanceof ShallowTraceException) {
                System.out.println("  [Context]: " + ((ShallowTraceException) e).getContext());
            }
            e.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- 异常抛出与捕获性能测试 (重复 " + ITERATIONS + " 次) ---");
        System.out.println("-----------------------------------------------------------------");

        // JIT 预热：JVM 第一次运行时会慢，执行多次可达到稳定性能
        for (int i = 0; i < 5; i++) {
            runTest("1. 预热 标准异常", 0);
            runTest("2. 预热 零栈异常", 1);
            runTest("3. 预热 浅层追踪", 2);
            runTest("4. 预热 自定义长度", 3);
        }
        System.out.println("-----------------------------------------------------------------");

        // 正式测试
        runTest("1. 标准异常", 0);
        runTest("2. 零栈异常", 1);
        runTest("3. 浅层追踪", 2);
        runTest("4. 自定义长度", 3);

        System.out.println("\n--- 栈追踪结果对比 (关键：观察栈的深度) ---");
        printTrace("1. 标准异常", 0);
        printTrace("2. 零栈异常", 1);
        printTrace("3. 浅层追踪", 2);
        printTrace("4. 自定义长度", 3);
    }
}