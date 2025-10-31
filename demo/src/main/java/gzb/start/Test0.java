package gzb.start;

import gzb.tools.GzbMap;
import gzb.tools.cache.Cache;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheMap;
import gzb.tools.cache.GzbCacheRedis;

public class Test0 {
    public static void main(String[] args) {
        //参数分别为 刷盘路径 刷盘周期（jvm结束时会触发回调刷盘，所以这个也不用太频繁）
        GzbCache gzbCache1 = new GzbCacheMap("C:/cache1.cache",120);
        //redis 配置 请查看配置文件，配置文件有注释
        GzbCache gzbCache2 = new GzbCacheRedis();

        //GzbCache 有两个实现 redis or map（带持久化）
        GzbCache main = Cache.gzbCache;//主缓存  配置文件可切换redis or map
        GzbCache map = Cache.gzbMap;//内部缓存 重启消失
        GzbCache session = Cache.session;//会话缓存 配置文件可切换redis or map
        GzbCache db = Cache.dataBaseCache;//数据库缓存 配置文件可切换redis or map
        /// 过期时间设置为小于0为永不过期
        //存入一个 键值对  过期时间为 永不过期
        main.set("key", "val", -1);
        //存入一个 键值对  过期时间为 60秒
        main.set("key2", "val2", 60);
        //存入一个 键值对 值为对象 会被序列化储存  过期时间为 60秒
        main.setObject("key3", new String("val3"), 60);
        //将值 存入map.key.sub_key  过期时间为 60秒
        main.setMap("key4", "key4", "val4", 60);
        //将值 存入map.key.sub_key  值为对象 会被序列化储存 过期时间为 60秒
        main.setMapObject("key5", "key5", new String("val5"), 60);
        //获取一个自增数字 从1开始 60秒过期 过期后从1重新开始
        main.getIncr("key6", 60);
        //读取值 同上储存逻辑
        main.get("key");
        //读取值 同上储存逻辑
        main.getObject("key3");
        //读取值 同上储存逻辑
        main.getMap("key4","key4");
        //读取值 同上储存逻辑
        main.getMapObject("key5","key5");
    }
}
