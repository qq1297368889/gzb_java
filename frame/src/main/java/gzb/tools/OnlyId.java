package gzb.tools;

import gzb.frame.db.BaseDao;
import gzb.frame.db.DataBase;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class OnlyId {
    // 起始时间戳（UTC 2023-01-01 00:00:00）
    private static long EPOCH;

    // 服务器ID位数（支持2^8=256台服务器）
    private static final int SERVER_ID_BITS = 8;
    // 序列号位数（每毫秒最多生成2^12=4096个ID）
    private static final int SEQUENCE_BITS = 12;

    // 服务器ID最大值
    private static final long MAX_SERVER_ID = (1L << SERVER_ID_BITS) - 1;
    // 序列号最大值
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // 时间戳左移位数
    private static final int TIMESTAMP_SHIFT = SERVER_ID_BITS + SEQUENCE_BITS;
    // 服务器ID左移位数
    private static final int SERVER_ID_SHIFT = SEQUENCE_BITS;

    // 当前服务器ID（全局唯一）
    private static final long serverId;

    // 核心状态变量（使用单独的对象锁确保同步）
    private static final Object lock = new Object();
    private static long lastTimestamp = -1L;
    private static long sequence = 0L;  // 普通变量+锁 比 AtomicLong 更可靠

    static {
        serverId = Config.server_name;
        EPOCH = Config.initTime;
        if (EPOCH < 1L) {
            EPOCH = System.currentTimeMillis();
            Config.set("gzb.system.init.time", EPOCH + "");
            Config.save();
        }
        // 严格校验服务器ID
        if (serverId < 0 || serverId > MAX_SERVER_ID) {
            throw new IllegalArgumentException(String.format(
                    "服务器ID必须在0-%d之间，当前值: %d", MAX_SERVER_ID, serverId));
        }
    }

    public static void main(String[] args) throws Exception {
        final int THREAD_COUNT = 100;
        final long ID_PER_THREAD = 100000;
        final long TOTAL_IDS = THREAD_COUNT * ID_PER_THREAD;

        Set<Long> generatedIds = Collections.synchronizedSet(new HashSet<>((int) TOTAL_IDS));
        AtomicLong successCount = new AtomicLong(0);

        System.out.println("--- ID生成器并发测试开始 ---");
        System.out.printf("配置: 线程数=%d, 单线程ID数=%d, 总数=%d\n",
                THREAD_COUNT, ID_PER_THREAD, TOTAL_IDS);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (long j = 0; j < ID_PER_THREAD; j++) {
                    try {
                        Long id = getDistributed();
                        if (!generatedIds.add(id)) {
                            System.err.println("🚨 发现重复ID: " + id);
                            System.exit(1);
                        }
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        System.err.println("❌ 错误: " + e.getMessage());
                    }
                }
            });
        }

        executor.shutdown();
        System.out.println("等待所有线程完成...");

        if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
            System.err.println("超时未完成");
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        System.out.println("\n--- 测试结果 ---");
        System.out.printf("总耗时: %dms\n", totalTime);
        if (totalTime > 0) {
            long qps = (successCount.get() * 1000L) / totalTime;
            System.out.printf("吞吐量: %,d次/秒\n", qps);
        }
        System.out.printf("生成ID总数: %,d\n", successCount.get());
        System.out.printf("去重后总数: %,d\n", generatedIds.size());

        if (successCount.get() == TOTAL_IDS && generatedIds.size() == TOTAL_IDS) {
            System.out.println("✅ 测试通过，无重复ID");
        } else {
            System.err.println("❌ 测试失败，存在重复或丢失");
        }
    }

    /**
     * 生成分布式唯一ID（核心修复版）
     */
    public static long getDistributed() {
        synchronized (lock) {  // 全局唯一锁，确保所有状态变量操作原子性
            long currentTimestamp = System.currentTimeMillis();

            // 处理时钟回拨（绝对禁止生成可能重复的ID）
            if (currentTimestamp < lastTimestamp) {
                long backTime = lastTimestamp - currentTimestamp;
                throw new IllegalStateException("时钟回拨异常: " + backTime + "ms，拒绝生成ID");
            }

            // 同一毫秒内序列号自增
            if (currentTimestamp == lastTimestamp) {
                sequence++;
                // 序列号溢出，等待下一毫秒
                if (sequence > MAX_SEQUENCE) {
                    currentTimestamp = waitUntilNextMillis(lastTimestamp);
                    sequence = 0;  // 重置序列号
                }
            } else {
                // 新的毫秒，重置序列号
                sequence = 0;
            }

            // 必须先更新时间戳，再返回ID（关键修复）
            lastTimestamp = currentTimestamp;

            // 组合ID
            return (currentTimestamp - EPOCH) << TIMESTAMP_SHIFT
                    | (serverId << SERVER_ID_SHIFT)
                    | sequence;
        }
    }

    /**
     * 自旋等待到下一毫秒（确保时间戳严格递增）
     */
    private static long waitUntilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    public static String getDistributedString() {
        return String.valueOf(getDistributed());
    }
}
