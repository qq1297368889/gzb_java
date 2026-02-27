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
import gzb.tools.GzbStringBuilder;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.log.Log;

import java.util.*;

public class PublicEntrance {
    //字符串拼接通用缓存
    //针对于 高频操作做一个对象池  主要是字符串拼接的对象池 其他的对象池暂时没有想到合适的场景 以后有了再说
    public static class Entity {
        int max = 50; //禁止嵌套太多  大概是50kb 内存 或许可以考虑加大 暂定50
        int buff_size = 1024;
        private StringBuilder[] stringBuilders = new StringBuilder[max];
        private int[] states = new int[max];

        public int open() {
            for (int i = 0; i < states.length; i++) {
                if (states[i] == 0) {
                    states[i] = 1;
                    return i;
                }
            }
            return -1;
        }

        public void close(int index) {
            if (index < 0) {//不对大值容错 传递异常值说明使用错误早点暴漏问题并非坏事
                return;
            }
            states[index] = 0;
            stringBuilders[index].setLength(0);//按规则调用他不可能是null 不做容错
        }

        public StringBuilder get(int index) {
            if (index < 0) {//超出max 不在缓存范围内 直接返回新对象
                return new StringBuilder(128);
            }
            if (stringBuilders[index] == null) {
                stringBuilders[index] = new StringBuilder(buff_size);
            }
            return stringBuilders[index];//返回引用
        }

        //框架兜底 考虑是否使用 他是开销 但是或许有必要
        public void closeAll() {
            Arrays.fill(states, 0);
        }
    }

    public static final ThreadLocal<Entity> SB_CACHE0 = ThreadLocal.withInitial(Entity::new);

    //上下文缓存 主要用于传递环境信息
    public static final ThreadLocal<Object[]> context = new ThreadLocal<>();
    //数据库事件 深度配置
    public static final ThreadLocal<Integer> depth = new ThreadLocal<>();
    //数据库事务签名
    public static final ThreadLocal<String> open_transaction_key = new ThreadLocal<>();
    //json模板实现类
    public static final GzbJson gzbJson = new GzbJsonImpl();
    //框架核心实现类
    public static final FactoryImplV2 factory;

    static {
        try {
            factory = new FactoryImplV2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //数据库事件工厂
    public static EventFactory eventFactory = new EventFactoryImpl(PublicEntrance.factory.getMapObject(), Log.log);
    //类加载事件回调
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
