package gzb.frame;

import gzb.entity.Context;
import gzb.frame.db.DataBase;
import gzb.frame.db.EventFactory;
import gzb.frame.db.EventFactoryImpl;
import gzb.frame.db.entity.TransactionEntity;
import gzb.frame.factory.Factory;
import gzb.frame.factory.v4.FactoryImplV2;
import gzb.frame.netty.entity.Request;
import gzb.frame.netty.entity.Response;
import gzb.tools.Config;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.log.Log;
import io.netty.util.concurrent.FastThreadLocal;

import java.util.List;
import java.util.Map;

public class PublicData {
    //这个不用担心泄露 泄露也没关系 省的下次 == null  用于数据库事件
    public static final ThreadLocal<Object[]> context = new ThreadLocal<>();
    public static final ThreadLocal<Integer> depth = new ThreadLocal<>();

    public static final ThreadLocal<String> open_transaction_key = new ThreadLocal<>();

    public static final GzbJson gzbJson = new GzbJsonImpl();
    public static final Factory factory;

    static {
        try {
            factory = new FactoryImplV2();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final EventFactory eventFactory = new EventFactoryImpl(PublicData.factory.getMapObject(), Log.log);

}
