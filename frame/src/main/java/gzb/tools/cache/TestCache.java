package gzb.tools.cache;

import gzb.tools.Tools;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class TestCache {

    // 自定义可序列化对象，用于测试
    static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "User{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(TestCache.class));
        System.out.println("--- 正在初始化---");
        GzbCache cache = new GzbCacheMap("E:\\codes_20220814\\java\\250913_code\\frame\\cacheMap.data");
        //GzbCache cache = new GzbCacheMap();  new GzbCacheRedis();

        // --- SECTION 1: 基本键值对操作 ---
        System.out.println("\n### 1. 测试基本键值对操作");
        System.out.println("--------------------------------------------------");
        cache.set("string_key", "Hello, World!", 60);
        System.out.println("设置 'string_key' = 'Hello, World!'");
        System.out.println("获取 'string_key' 的值: " + cache.get("string_key"));
        System.out.println("将 'string_key' 的值转换为字符串: " + cache.getString("string_key", "默认值"));

        cache.set("integer_key", 123);
        System.out.println("设置 'integer_key' = 123");
        System.out.println("获取 'integer_key' 的值: " + cache.getInteger("integer_key", 0));
        System.out.println("将 'integer_key' 的值转换为字符串 (不匹配): " + cache.getString("integer_key", "默认值"));

        cache.set("double_key", 99.9);
        System.out.println("设置 'double_key' = 99.9");
        System.out.println("获取 'double_key' 的值: " + cache.getDouble("double_key", 0.0));

        cache.set("boolean_key", true);
        System.out.println("设置 'boolean_key' = true");
        System.out.println("获取 'boolean_key' 的值: " + cache.getBoolean("boolean_key", false));

        // --- SECTION 2: 哈希表操作 ---
        System.out.println("\n### 2. 测试哈希表操作");
        System.out.println("--------------------------------------------------");
        cache.setMap("user:1", "name", "Alice");
        cache.setMap("user:1", "age", 30, 300); // 带有过期时间
        System.out.println("设置 'user:1' 哈希表，name='Alice', age=30");
        System.out.println("获取 'user:1' 的 name: " + cache.getString("user:1", "name", null));
        System.out.println("获取 'user:1' 的 age: " + cache.getInteger("user:1", "age", 0));

        System.out.println("删除 'user:1' 中的 'name'...");
        cache.del("user:1", "name");
        System.out.println("再次获取 'user:1' 的 'name' (应为 null): " + cache.getString("user:1", "name", null));

        // 测试哈希表的类型不匹配
        System.out.println("将 'user:1' 转换为列表，但它是一个 Map...");
        cache.setList("user:1", 0, "test"); // 尝试将 Map 类型的键设置为 List
        System.out.println("结果: " + cache.get("user:1", 0)); // 应该为 null

        // --- SECTION 3: 列表操作 ---
        System.out.println("\n### 3. 测试列表操作");
        System.out.println("--------------------------------------------------");
        cache.add("my_list", "Apple");
        cache.add("my_list", "Banana");
        cache.add("my_list", "Cherry");
        System.out.println("向 'my_list' 添加 'Apple', 'Banana', 'Cherry'");
        System.out.println("获取 'my_list' 索引 0 的值: " + cache.get("my_list", 0));
        System.out.println("获取 'my_list' 索引 1 的值: " + cache.get("my_list", 1));

        System.out.println("删除 'my_list' 索引 1 的值 ('Banana')...");
        cache.del("my_list", 1);
        System.out.println("删除后，获取 'my_list' 索引 1 的值 (应为 'Cherry'): " + cache.get("my_list", 1));

        System.out.println("替换 'my_list' 索引 0 的值 ('Apple') 为 'Orange'...");
        cache.setList("my_list", 0, "Orange");
        System.out.println("替换后，获取 'my_list' 索引 0 的值: " + cache.get("my_list", 0));

        // 测试越界索引
        System.out.println("获取 'my_list' 索引 100 (越界): " + cache.get("my_list", 100));

        // --- SECTION 4: 自增操作 ---
        System.out.println("\n### 4. 测试自增操作");
        System.out.println("--------------------------------------------------");
        System.out.println("对 'counter' 进行自增: " + cache.getIncr("counter")); // 1
        System.out.println("对 'counter' 再次自增: " + cache.getIncr("counter")); // 2

        System.out.println("对 'web_visits' 的 'home' 进行自增: " + cache.getIncr("web_visits", "home")); // 1
        System.out.println("对 'web_visits' 的 'home' 再次自增: " + cache.getIncr("web_visits", "home")); // 2

        // 测试对非整数值进行自增
        cache.set("string_to_incr", "not_a_number");
        System.out.println("对非整数值 'string_to_incr' 进行自增 (应返回 0): " + cache.getIncr("string_to_incr"));

        // --- SECTION 5: 过期时间测试 ---
        System.out.println("\n### 5. 测试过期时间");
        System.out.println("--------------------------------------------------");
        cache.set("expiring_key", "I will be gone", 2);
        System.out.println("设置 'expiring_key'，2秒后过期。立即获取: " + cache.get("expiring_key"));
        System.out.println("等待 3 秒...");
        TimeUnit.SECONDS.sleep(3);
        System.out.println("3 秒后再次获取 'expiring_key' (应为 null): " + cache.get("expiring_key"));

        // --- SECTION 6: 对象序列化测试 ---
        System.out.println("\n### 6. 测试对象序列化和反序列化");
        System.out.println("--------------------------------------------------");
        User alice = new User("Alice", 25);
        cache.setObject("user_object", alice, 60);
        System.out.println("存储一个 User 对象: " + alice);
        User retrievedUser = cache.getObject("user_object", null);
        System.out.println("获取并反序列化对象: " + retrievedUser);
        if (retrievedUser != null) {
            System.out.println("验证对象属性 - 姓名: " + retrievedUser.getName() + ", 年龄: " + retrievedUser.getAge());
        }

        // --- SECTION 7: 队列操作测试 ---
        System.out.println("\n### 7. 测试队列操作 (非阻塞)");
        System.out.println("--------------------------------------------------");
        cache.queueProduction("my_queue", "消息A");
        cache.queueProduction("my_queue", "消息B");
        System.out.println("向 'my_queue' 放入 '消息A', '消息B'");
        System.out.println("消费队列头部元素 (应为 '消息A'): " + cache.queueConsumption("my_queue"));
        System.out.println("消费队列头部元素 (应为 '消息B'): " + cache.queueConsumption("my_queue"));
        System.out.println("队列为空时消费 (应为 null): " + cache.queueConsumption("my_queue"));

        // --- SECTION 8: 阻塞式队列消费测试 ---
        System.out.println("\n### 8. 测试阻塞式队列消费");
        System.out.println("--------------------------------------------------");
        Thread producer = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("  [生产者] 2秒后放入新消息。");
                cache.queueProduction("blocking_queue", "新消息");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            System.out.println("  [消费者] 正在阻塞等待 'blocking_queue'，最多等待5秒...");
            Object result = cache.queueConsumptionBlock("blocking_queue", 5);
            System.out.println("  [消费者] 获取到的消息: " + result);
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 再次测试阻塞消费，但这次超时
        System.out.println("\n### 9. 测试阻塞式队列消费 (超时)");
        System.out.println("--------------------------------------------------");
        System.out.println("  [消费者] 正在阻塞等待一个空队列，最多等待2秒...");
        Object resultTimeout = cache.queueConsumptionBlock("empty_blocking_queue", 2);
        System.out.println("  [消费者] 获取到的消息 (应为 null): " + resultTimeout);

        System.out.println("\n--- 所有测试完成 ---");


       //cache.set("stst0001", "Hello, World!", 60);
        System.out.println("获取 'stst0001' 的值: " + cache.get("stst0001"));
    }
}