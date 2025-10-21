package gzb.tools.cache;

/// key 是主key  subKey 是主key的val中的子键 index同理也是
/// 注意 所有方法不允许抛出异常 只能返回null
/// 注意 对子键的操作 需要确保原子性
public interface GzbCache {
    /// map set后返回被替换的值 如果不存在 则返回null
    /// list set后返回被替换的值 如果不存在 则返回null
    /// list add后返回位置索引


    /// 获取自增数字 基于 Long getIncrLong(String key, int second)
    public Integer getIncr(String key,int second);

    /// 获取自增数字 基于 Long getIncrLong(String key, String subKey,int second)
    public Integer getIncr(String key, String subKey,int second);
    /// 获取自增数字  并且需要设置过期时间
    public Long getIncrLong(String key,int second);
    /// 获取自增数字 要在主key下的子key对应 并且需要设置过期时间
    public Long getIncrLong(String key, String subKey,int second);

    ///不带序列化 反序列化
    public void set(String key, String val, int second);

    /// 获取 key对应的 val
    public String get(String key);

    /// put 主key下 的 子key对应的val 替换或新建
    public void setMap(String key, String subKey, String val, int second);
    /// 获取 主key下 的 子key对应的val
    public String getMap(String key, String subKey);

    /// 删除 主key对应的key和val
    public void remove(String key);

    /// 删除主 key下 的 子key和val
    public void removeMap(String key, String subKey);

    /// 直接删除 整个 map
    public void removeMap(String key);


    /// 下列各种方法 带序列化和反序列化 其他规则和上述同名方法一致   删除方法共享

    public void setObject(String key, Object val, int second);

    public <T>T getObject(String key);

    public void setMapObject(String key, String subKey, Object val, int second);


    public <T>T getMapObject(String key, String subKey);
}
