package gzb.tools.cache;

import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    //默认 缓存 具体实现取决于配置文件
    public static GzbCache gzbCache=Config.cache_gzbCache;
    //session 缓存 具体实现取决于配置文件
    public static GzbCache session=Config.cache_session;
    //数据库查询 缓存 具体实现取决于配置文件
    public static GzbCache dataBaseCache=Config.cache_dataBaseCache;
    //全局缓存 是map实现 避免一些对象无法存入redis 并且重启会消失
    public static GzbCache gzbMap=Config.cache_gzbMap;
}
