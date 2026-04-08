package gzb.tools;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QueueTest {
    public static void main(String[] args) throws Exception {
        testBasic();       // 基础功能测试
        testBackpressure(); // 背压与阻塞唤醒测试
        testPollTimeout();  // 超时功能测试
        //testConcurrency();  // 多线程极限压力测试
    }

    // 1. 基础读写测试
    public static void testBasic() throws Exception {
        System.out.println("=== 开始基础测试 ===");
        Queue<String> queue = new Queue<>(10);
        queue.add("A", "B", "C");

        assert queue.size() == 3 : "Size 应该是 3";
        assert "A".equals(queue.read()) : "读取顺序错误";

        List<String> list = queue.readList(2);
        assert list.size() == 2 && list.get(0).equals("B") : "批量读取失败";

        System.out.println("基础测试通过！");
    }

    // 2. 背压与容量限制测试
    public static void testBackpressure() throws Exception {
        System.out.println("\n=== 开始背压阻塞测试 ===");
        final int capacity = 5;
        Queue<Integer> queue = new Queue<>(capacity);

        // 启动一个线程尝试塞满并溢出
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.add(i);
                System.out.println("生产: " + i + "，当前Size: " + queue.size());
            }
        });
        producer.start();

        Thread.sleep(500); // 等待生产者填满队列
        System.out.println("生产者应已阻塞，Size: " + queue.size());

        assert queue.size() == capacity : "背压失效，队列超限";

        // 消费者开始消费
        for (int i = 0; i < 5; i++) {
            Thread.sleep(200);
            System.out.println("读取并释放空间: " + queue.read());
        }

        producer.join(2000);
        assert queue.size() == 5 : "生产者未成功被唤醒继续生产";
        System.out.println("背压测试通过！");
    }

    // 3. 超时 Poll 测试
    public static void testPollTimeout() throws Exception {
        System.out.println("\n=== 开始超时 Poll 测试 ===");
        Queue<String> queue = new Queue<>(10);

        long start = System.currentTimeMillis();
        String res = queue.poll(1, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();

        assert res == null : "空队列 Poll 应该返回 null";
        assert (end - start) >= 1000 : "超时时间不足";

        System.out.println("超时 Poll 测试通过！");
    }

    // 4. 多线程高并发测试
    public static void testConcurrency() throws Exception {
        System.out.println("\n=== 开始多线程压力测试 ===");
        final int count = 100000; // 每线程生产数量
        final int threadNum = 5;  // 线程数
        final Queue<Integer> queue = new Queue<>(5000);
        final AtomicInteger totalRead = new AtomicInteger(0);

        // 启动 5 个消费者
        for (int i = 0; i < threadNum; i++) {
            new Thread(() -> {
                try {
                    while (totalRead.get() < count * threadNum) {
                        List<Integer> list = queue.readList(100);
                        totalRead.addAndGet(list.size());
                    }
                } catch (InterruptedException e) {}
            }).start();
        }

        long start = System.nanoTime();
        // 启动 5 个生产者
        Thread[] producers = new Thread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            producers[i] = new Thread(() -> {
                for (int j = 0; j < count; j++) {
                    queue.add(j);
                }
            });
            producers[i].start();
        }

        for (Thread t : producers) t.join();

        // 等待消费完成
        while (totalRead.get() < count * threadNum) Thread.sleep(10);

        long end = System.nanoTime();
        System.out.println("多线程测试完成！总消费: " + totalRead.get());
        System.out.println("平均每百万次操作耗时(纳秒): " + (end - start) / (count * threadNum / 1000000.0));
    }
}