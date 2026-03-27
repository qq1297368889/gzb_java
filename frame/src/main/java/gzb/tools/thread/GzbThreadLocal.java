package gzb.tools.thread;
import gzb.frame.db.entity.TransactionEntity;
import gzb.tools.Tools;
import gzb.tools.cache.object.ArrayBuffCache;
import gzb.tools.cache.object.ByteBuffCache;
import gzb.tools.cache.object.MapCache;
import gzb.tools.cache.object.StringBuilderCache;
import gzb.tools.log.Log;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GzbThreadLocal {
/*    public static final  AtomicLong atomicLong1 = new AtomicLong(0);
    public static final  AtomicLong atomicLong2 = new AtomicLong(0);
    static{
        new Thread(()->{
            while (true){
                Tools.sleep(1000);
                System.out.println(atomicLong1.get()+"   "+atomicLong2.get());
            }
        }).start();
    }*/
  /*      public StringBuilderCache.Entity stringBuilderCacheEntity=new StringBuilderCache.Entity(){
            @Override
            public int open() {
                atomicLong1.incrementAndGet();
                return super.open();
            }

            @Override
            public void close(int index) {
                atomicLong2.incrementAndGet();
                super.close(index);
            }
        };*/
    public static final ThreadLocal<Entity> context = ThreadLocal.withInitial(Entity::new);
    public static class Entity{
        public Object[] objects=null;
        public int depth=0;
        public String open_transaction_key=null;
        public Map<String, List<Object>>requestMap=null;
        public byte[]byte_buff_32=new byte[32];
        public Connection connection;
        public TransactionEntity transactionEntity;
        public ByteBuffCache.Entity byteBuffCacheEntity=new ByteBuffCache.Entity();
        public StringBuilderCache.Entity stringBuilderCacheEntity=new StringBuilderCache.Entity();
    }
    static Map<Long,Object>data=new ConcurrentHashMap<>();
    static{
        ServiceThread.start(new Runnable() {
            @Override
            public void run() {
                ThreadMXBean threadMXBean=ManagementFactory.getThreadMXBean();
                while (true){
                    Iterator<Map.Entry<Long, Object>> it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Long, Object> ent = it.next();
                        if (threadMXBean.getThreadInfo(ent.getKey())==null) {
                            it.remove();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Log.log.w("线程收到中断信号，但该线程不允许停止...");
                    }

                }
            }
        });
    }
    public static <T>T get(){
        return (T)data.get(Thread.currentThread().getId());
    }
    public static void set(Object t){
        data.put(Thread.currentThread().getId(),t);
    }
    public static void remove(){
        data.remove(Thread.currentThread().getId());
    }
    public static Map<Long,Object> read(){
        return data;
    }
}