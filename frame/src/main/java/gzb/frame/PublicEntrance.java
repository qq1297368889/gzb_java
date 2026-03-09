package gzb.frame;

import gzb.entity.HttpMapping;
import gzb.exception.GzbException0;
import gzb.frame.db.EventFactory;
import gzb.frame.db.EventFactoryImpl;
import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassLoadEvent;
import gzb.frame.factory.ClassTools;
import gzb.frame.factory.Factory;
import gzb.frame.factory.v4.FactoryImplV2;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.log.Log;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PublicEntrance {
    //上下文缓存 主要用于传递环境信息
    public static final ThreadLocal<Object[]> context = new ThreadLocal<>();
    //数据库事件 深度配置
    public static final ThreadLocal<Integer> depth = new ThreadLocal<>();
    //数据库事务签名
    public static final ThreadLocal<String> open_transaction_key = new ThreadLocal<>();
    //json模板实现类
    public static final GzbJson gzbJson = new GzbJsonImpl();
    //框架核心实现类
    public static FactoryImplV2 factory = new FactoryImplV2();

    //数据库事件工厂
    public static EventFactory eventFactory = new EventFactoryImpl(PublicEntrance.factory.getMapObject(), Log.log);
    //类加载事件回调
    public static ClassLoadEvent classLoadEvent = null;

    ///  tcp相关操作
    //sessionid  [req,resp]
    private static Map<Integer, ChannelHandlerContext> tcp_session = new ConcurrentHashMap<>();
    //获取会话列表 副本
    public static Map<Integer, ChannelHandlerContext> readTcpSession() {
        return new HashMap<>(tcp_session);
    }
//写入 新会话 框架内部调用
    public static void putTcpSession(Integer sessionId, ChannelHandlerContext ctx) {
        if (ctx==null || !ctx.channel().isOpen() || !ctx.channel().isActive()) {
            throw new GzbException0("会话无效 无法存入缓存");
        }
        tcp_session.put(sessionId, ctx);
    }
//读取一个会话根据session id
    public static ChannelHandlerContext readTcpSession(Integer sessionId) {
        ChannelHandlerContext ctx = tcp_session.get(sessionId);
        if (ctx!=null && ctx.channel().isOpen() && ctx.channel().isActive()) {
            return ctx;
        }
        return null;
    }
    //删除一个会话 根据id
    public static void removeTcpSession(Integer sessionId) {
        ChannelHandlerContext ctx =tcp_session.remove(sessionId);
        if (ctx!=null && ctx.channel().isOpen() && ctx.channel().isActive()) {
            ctx.close();
        }
    }

    //删除一个会话 根据ctx
    public static void removeTcpSession(ChannelHandlerContext ctx) {
        removeTcpSession(ctx.channel().hashCode());
    }

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
