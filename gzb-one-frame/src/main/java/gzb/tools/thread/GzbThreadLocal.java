package gzb.tools.thread;

import gzb.entity.RunRes;
import gzb.frame.db.entity.TransactionEntity;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.Tools;
import gzb.tools.cache.object.ArrayBuffCache;
import gzb.tools.cache.object.ByteBuffCache;
import gzb.tools.cache.object.MapCache;
import gzb.tools.cache.object.StringBuilderCache;
import gzb.tools.log.Log;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GzbThreadLocal {
    public static final ThreadLocal<Entity> context = ThreadLocal.withInitial(Entity::new);

    public static class Entity {
        public Integer transaction_state_async;
        public SqlConnection connection_async;
        public Transaction transaction_async;
        public Map<String, List<Object[]>> transaction_simulate= null;


        public ByteBuffCache.Entity byteBuffCacheEntity = new ByteBuffCache.Entity();
        public StringBuilderCache.Entity stringBuilderCacheEntity = new StringBuilderCache.Entity();
        public RunRes runRes = new RunRes();
        public Object[] objects = new Object[]{runRes};
        public byte[] byte_buff_32 = new byte[32];
        public int depth = 0;
        public Map<String, List<Object>> requestMap = null;
        public Request request;
        public Response response;

    }

    static Map<Long, Object> data = new ConcurrentHashMap<>();

    static {
        ServiceThread.start(new Runnable() {
            @Override
            public void run() {
                ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
                while (true) {
                    Iterator<Map.Entry<Long, Object>> it = data.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Long, Object> ent = it.next();
                        if (threadMXBean.getThreadInfo(ent.getKey()) == null) {
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

    public static <T> T get() {
        return (T) data.get(Thread.currentThread().getId());
    }

    public static void set(Object t) {
        data.put(Thread.currentThread().getId(), t);
    }

    public static void remove() {
        data.remove(Thread.currentThread().getId());
    }

    public static Map<Long, Object> read() {
        return data;
    }
}