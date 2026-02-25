package gzb.frame;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    // 模拟2000个URL（和你的业务场景一致）
    private static final int URL_COUNT = 2000;
    private static final String TEST_URL = "test/api1000/get1000"; // 中间位置的URL，避免极端情况

    public static void main(String[] args) throws InterruptedException {
        // ========== 第一步：测试HashMap ==========
        System.out.println("=== 测试HashMap ===");
        Map<String, Object> hashMap = new HashMap<>(URL_COUNT * 2); // 预设置容量，避免扩容
        // 1. 加载2000个URL
        loadUrls(hashMap);
        // 2. 多轮热身（触发JIT完全编译，至少3轮）
        warmUp(hashMap);
        // 3. 正式测试（3次取平均，避免波动）
        testPerformance(hashMap);
        testPerformance(hashMap);
        testPerformance(hashMap);
        testPerformance(hashMap);
        testPerformance(hashMap);

        // ========== 第二步：测试ConcurrentHashMap ==========
        System.out.println("\n=== 测试ConcurrentHashMap ===");
        Map<String, Object> chm = new ConcurrentHashMap<>(URL_COUNT * 2);
        // 1. 加载2000个URL
        loadUrls(chm);
        // 2. 多轮热身
        warmUp(chm);
        // 3. 正式测试
        for (int i = 0; i < 10; i++) {
            new Thread(()->{

                testPerformance(chm);
            }).start();
        }
        Thread.sleep(10000);
    }

    // 加载2000个URL（模拟真实场景）
    private static void loadUrls(Map<String, Object> map) {
        for (int i = 0; i < URL_COUNT; i++) {
            String url = "test/api" + i + "/get" + i;
            map.put(url, "处理器-" + i);
        }
    }

    // 多轮热身（触发JIT完全编译）
    private static void warmUp(Map<String, Object> map) {
        int size=0;
        //  第一轮：触发解释执行 → C1编译
        for (int i = 0; i < 1000000; i++) {
            size+=map.get(TEST_URL).toString().length();
        }
        size=0;
        // 第二轮：触发C1 → C2编译（极致优化）
        for (int i = 0; i < 5000000; i++) {
            size+=map.get(TEST_URL).toString().length();
        }
        size=0;
        // 第三轮：稳定编译状态
        for (int i = 0; i < 10000000; i++) {
            size+=map.get(TEST_URL).toString().length();
        }
        System.out.println("热身完成 "+size);
    }

    // 正式性能测试（3次取平均，减少波动）
    private static void testPerformance(Map<String, Object> map) {
        long totalTime = 0;
        int testRound = 3; // 3轮测试取平均
        for (int round = 1; round <= testRound; round++) {
            long start = System.nanoTime();
            int size=0;
            for (int i = 0; i < 10000000; i++) { // 1000万次，放大耗时差异
                map.get(TEST_URL);
                size+=map.get(TEST_URL).toString().length();
            }
            long end = System.nanoTime();
            long cost = end - start;
            long avg = cost / 10000000;
            totalTime += cost;
            System.out.println("第" + round + "轮：1000万次耗时=" + cost + "ns，单次平均=" + avg + "ns "+size);
        }
        long avgTotal = totalTime / (testRound * 10000000);
        System.out.println("最终平均：单次耗时=" + avgTotal + "ns");
    }
}