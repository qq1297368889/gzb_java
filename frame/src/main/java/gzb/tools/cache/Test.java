package gzb.tools.cache;

import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.cache.entity.Entity;
import gzb.tools.log.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {
    public static class TestObject implements java.io.Serializable {
        private final String name;
        private final int id;

        public TestObject(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() { return name; }
        public int getId() { return id; }

    }
    public static Log log = Log.log;
    // --- 测试方法 ---

    public static void main(String[] args) throws Exception {
        assert false : "1111";
        GzbCache gzbCache;
        gzbCache = new GzbCacheMap("C:\\work\\1.cache", 60);
        testCache(gzbCache);
        gzbCache = new GzbCacheRedis();
        testCache(gzbCache);
    }

    private static void testCache(GzbCache gzbCache) throws InterruptedException {
        TestObject originalObject = new TestObject("TestName", 123);
        String mapKey="sub1";
        String val="val-1";
        // 假设 Config 类已正确配置 Redis 连接信息
        val="val-过期-字符串";
        String key0="key0";
        gzbCache.set(key0,val,1);
        log.d("预期",val,gzbCache.get(key0));
        Tools.sleep(1500);
        log.d("预期",null,gzbCache.get(key0));
        gzbCache.removeMap(key0);


        val="val-不过期-字符串";
        String key1="key1";
        gzbCache.set(key1,val,0);
        log.d("预期",val,gzbCache.get(key1));
        Tools.sleep(1500);
        log.d("预期",val,gzbCache.get(key1));
        gzbCache.removeMap(key1);
        String key2="key2";
        gzbCache.setObject(key2,originalObject,1);
        log.d("预期",originalObject,(TestObject)gzbCache.getObject(key2));
        Tools.sleep(1500);
        log.d("预期",null,(TestObject)gzbCache.getObject(key2));
        gzbCache.removeMap(key2);
        String key3="key3";
        gzbCache.setObject(key3,originalObject,0);
        log.d("预期",originalObject,(TestObject)gzbCache.getObject(key3));
        Tools.sleep(1500);
        log.d("预期",originalObject,(TestObject)gzbCache.getObject(key3));
        gzbCache.removeMap(key3);

        String key4="key4";
        log.d("预期","1",gzbCache.getIncr(key4,1));
        log.d("预期","2",gzbCache.getIncr(key4,1));
        log.d("预期","3",gzbCache.getIncr(key4,1));
        Tools.sleep(1500);
        log.d("预期","1",gzbCache.getIncr(key4,1));
        gzbCache.remove(key4);
        String key5="key5";
        log.d("预期","1",gzbCache.getIncr(key5,0));
        log.d("预期","2",gzbCache.getIncr(key5,0));
        log.d("预期","3",gzbCache.getIncr(key5,0));
        Tools.sleep(1500);
        log.d("预期","4",gzbCache.getIncr(key5,0));
        gzbCache.remove(key5);

        String key6="key6";
        val="val-过期-subval";
        gzbCache.setMap(key6,mapKey,val,1);
        log.d("预期",val,gzbCache.getMap(key6,mapKey));
        Tools.sleep(1500);
        log.d("预期",null,gzbCache.getMap(key6,mapKey));
        gzbCache.removeMap(key6);
        String key7="key7";
        val="val-不过期-subval";
        gzbCache.setMap(key7,mapKey,val,0);
        log.d("预期",val,gzbCache.getMap(key7,mapKey));
        Tools.sleep(1500);
        log.d("预期",val,gzbCache.getMap(key7,mapKey));
        gzbCache.removeMap(key7);


    }
    /**
     * 对任何 GzbQueue 实现运行完整的操作和验证测试。
     */
    public static void testQueue() throws Exception {
        GzbQueue queue = new GzbQueueImpl();
        System.out.println(">>>>>>>> 开始 "+queue.getClass().getSimpleName()+" 功能测试 (操作 + 验证) <<<<<<<<");
        queue.produce("消费 1");
        queue.produce("消费 2");
        queue.produce("消费 3");
        log.d("queue.size()",queue.size());
        Entity entity = queue.consume(-1);
        log.d("entity",entity);
        log.d("confirm",queue.confirm(entity.id));
        log.d("confirm",queue.confirm(entity.id));
        log.d("queue.read()",queue.read());
        log.d("queue.size()",queue.size());
        log.d("consumeAndConfirm 1",queue.consumeAndConfirm(-1));
        log.d("consumeAndConfirm 2",queue.consumeAndConfirm(-1));
        log.d("consumeAndConfirm 3",queue.consumeAndConfirm(-1));
        log.d("queue.size()",queue.size());
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                queue.produce("消费一块");
            }
        }.start();
        log.d("queue.consume(-1)",queue.consume(-1));
         entity = queue.consume(0);
        log.d("queue.consume(0)",entity);
        log.d("queue.consume(-1)",queue.consume(-1));
        log.d("confirm",queue.confirm(entity.id));
        log.d("queue.size()",queue.size());
        System.out.println(">>>>>>>> GzbQueue 测试完成 <<<<<<<<");
    }

}
