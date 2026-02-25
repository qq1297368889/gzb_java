package gzb.frame;

import gzb.entity.HttpMapping;
import gzb.frame.db.EventFactory;
import gzb.frame.db.EventFactoryImpl;
import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassLoadEvent;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Factory;
import gzb.frame.factory.v4.FactoryImplV2;
import gzb.frame.factory.v4.entity.CacheObject;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.log.Log;

import java.util.HashMap;
import java.util.Map;

public class PublicEntrance {
    //这个不用担心泄露 泄露也没关系 省的下次 == null
    public static final ThreadLocal<Object[]> context = new ThreadLocal<>();
    public static final ThreadLocal<Integer> depth = new ThreadLocal<>();

    public static final ThreadLocal<String> open_transaction_key = new ThreadLocal<>();

    public static final GzbJson gzbJson = new GzbJsonImpl();
    public static final FactoryImplV2 factory;

    static {
        try {
            factory = new FactoryImplV2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static EventFactory eventFactory = new EventFactoryImpl(PublicEntrance.factory.getMapObject(), Log.log);

    public static ClassLoadEvent classLoadEvent = null;


    /// 注册类加载事件
    public static void registerClassLoadEvent(ClassLoadEvent event) {
        classLoadEvent = event;
    }

    /// 注册数据库事件
    public static void registerDataBaseEvent(Class<?> clazz, String javaCode) throws Exception {
        EventFactory eventFactory = (EventFactory) factory.mapObject0.get(EventFactory.class.getName());
        eventFactory.register(clazz, javaCode);
    }

    /// 注册类到容器 会自动识别类型
    public static void registerClass(String className, String javaCode, String aes_cbc_128_pwd, String aes_cbc_128_iv) throws Exception {
        Map<String, String> sourcesMap = new HashMap<>();
        sourcesMap.put(className, javaCode);
        PublicEntrance.factory.load(sourcesMap, aes_cbc_128_pwd, aes_cbc_128_iv);
    }

    /// 注册类到容器 会自动识别类型  自动获取类名
    public static void registerClass(String javaCode, String aes_cbc_128_pwd, String aes_cbc_128_iv) throws Exception {
        String className = ClassTools.extractPublicClassName(javaCode);
        registerClass(className, javaCode, aes_cbc_128_pwd, aes_cbc_128_iv);
    }

    /// 获取内部映射信息副本  避免外部修改导致线程安全问题
    public static Map<String, HttpMapping[]> readMapping() {
        return new HashMap<>(factory.mapHttpMapping0);
    }

}
