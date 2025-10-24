package gzb.frame.factory;

import gzb.frame.annotation.ThreadFactory;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
import gzb.tools.log.Log;
import gzb.tools.thread.ThreadPoolV3;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadFactoryImpl {
    public static class Entity {
        public GzbOneInterface gzbOneInterface;
        public int met_id;
        public String met_sign;
        public ThreadInterval interval;
        public boolean async;
        public int num;
        public long sleep;
        public Class<?> clazz;
        public int state;
        public Thread thread;
        public Result result;
    }

    public static Log log = Log.log;
    public static Map<String, List<Object>> _parar = new HashMap<>();
    public static Object[] objArray = new Object[0];
    Map<String, Map<String, Entity>> threadMap = new ConcurrentHashMap<>();
    ThreadPoolV3 threadPool = new ThreadPoolV3(100.0);

    public ThreadFactoryImpl() {

    }


    public void register(Map<String, Object> objectMap, Class<?> aClass) throws Exception {
        ThreadFactory threadFactory=aClass.getAnnotation(ThreadFactory.class);
        if (threadFactory==null) {
            return;
        }
        Method[]methods = ClassTools.getCombinedMethods(aClass);
        for (int i = 0; i < methods.length; i++) {
            ThreadInterval threadInterval=methods[i].getAnnotation(ThreadInterval.class);
            if (threadInterval==null) {
                continue;
            }
            Entity entity = new Entity();
            entity.met_id=ClassTools.getSingInt(methods[i],aClass);
            entity.met_sign=ClassTools.getSing(methods[i],aClass);
            entity.async=threadInterval.async();
            entity.gzbOneInterface=(GzbOneInterface)objectMap.get(aClass.getName());
            entity.clazz=aClass;
            entity.num=threadInterval.num();
            entity.sleep=threadInterval.value();
            entity.interval=threadInterval;
            entity.state=0;
            entity.result=new ResultImpl();
        }
    }

    public void start(Entity entity, Map<String, Object> objectMap) {
        String className = entity.clazz.getName();
        Thread thread = new Thread(() -> {
            Entity entity1 = entity;
            Map<String, Entity> mapEntity =null;
            while (true) {
                mapEntity = threadMap.get(className);
                if (mapEntity == null) {
                    log.d("线程结束 正常结束 1", entity.thread);
                    break;
                }
                entity1 = mapEntity.get(entity1.met_sign);
                if (entity1 == null) {
                    log.d("线程结束 正常结束 2", entity.thread);
                    break;
                }

                try {
                    Object object = entity1.gzbOneInterface._gzb_call_x01(entity1.met_id, objectMap, null, null, _parar, null, log, objArray);
                    if (object != null) {
                        log.d("线程结束 主动结束", entity.thread);
                        break;
                    }
                    Thread.sleep(entity1.sleep);
                } catch (InterruptedException e) {
                    log.d("线程结束 被中断", entity.thread);
                    break;
                } catch (Exception e) {
                    log.e("线程执行错误,但不会被终止", entity.thread, e);
                }
            }
            if (mapEntity != null) {
                mapEntity.remove(className);
            }
        });
        entity.thread = thread;
        thread.setName(entity.met_sign);
        thread.start();
    }
}
