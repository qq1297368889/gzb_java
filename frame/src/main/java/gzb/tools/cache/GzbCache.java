package gzb.tools.cache;

/**
 * 通用缓存接口。
 * <p>
 * 此缓存接口设计为容错型。所有方法都不会抛出异常。
 * 成功调用返回对应数据，失败调用返回 {@code null} 或默认值。
 */
public interface GzbCache {

    /// 统一解释
    /// key      : 缓存键
    /// val      : 缓存值
    /// mapKey   : 哈希表中的键
    /// index    : 列表中的索引
    /// second   : 缓存过期时间（秒）或最大阻塞时间（秒）
    /// defVal   : 当缓存值不存在时返回的默认值

    /**
     * 对 key 对应的整数值进行自增操作。
     * 如果 key 不存在，则将其初始化为 1 并返回。
     *
     * @param key 要自增的键。
     * @return 自增后的新值；如果操作失败，则返回 0。
     */
    int getIncr(String key);

    /**
     * 对哈希表中的指定键进行自增操作。
     * 如果哈希表或 mapKey 不存在，则将其初始化为 1 并返回。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @return 自增后的新值；如果操作失败，则返回 0。
     */
    int getIncr(String key, String mapKey);

    /**
     * 获取 key 对应的元素。
     *
     * @param key 要获取的键。
     * @return 键对应的元素；如果键不存在或操作失败，则返回 {@code null}。
     */
    Object get(String key);

    /**
     * 获取哈希表中指定键对应的元素。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @return 哈希表中的元素；如果哈希表或 mapKey 不存在，则返回 {@code null}。
     */
    Object get(String key, String mapKey);

    /**
     * 获取列表中指定索引的元素。
     *
     * @param key   列表的键。
     * @param index 要获取的元素的索引。
     * @return 列表中指定索引的元素；如果列表或索引不存在，则返回 {@code null}。
     */
    Object get(String key, int index);

    /**
     * 插入 key 对应的元素，不设置过期时间。
     *
     * @param key 要存储的键。
     * @param val 要存储的值。
     */
    void set(String key, Object val);

    /**
     * 插入哈希表中指定键对应的元素，不设置过期时间。
     * 如果哈希表不存在，则会创建。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param val    要存储的值。
     */
    void setMap(String key, String mapKey, Object val);

    /**
     * 替换列表中指定索引的元素，不设置过期时间。
     *
     * @param key   列表的键。
     * @param index 要替换的元素的索引。
     * @param val   要设置的新值。
     */
    void setList(String key, int index, Object val);

    /**
     * 插入 key 对应的元素，并设置过期时间。
     *
     * @param key    要存储的键。
     * @param val    要存储的值。
     * @param second 过期时间，单位为秒。
     */
    void set(String key, Object val, int second);

    /**
     * 插入哈希表中指定键对应的元素，并设置过期时间。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param val    要存储的值。
     * @param second 过期时间，单位为秒。
     */
    void setMap(String key, String mapKey, Object val, int second);

    /**
     * 替换列表中指定索引的元素，并设置过期时间。
     *
     * @param key    列表的键。
     * @param index  要替换的元素的索引。
     * @param val    要设置的新值。
     * @param second 过期时间，单位为秒。
     */
    void setList(String key, int index, Object val, int second);

    /**
     * 向列表的末尾追加一个元素。
     *
     * @param key 列表的键。
     * @param val 要追加的值。
     */
    void add(String key, Object val);

    /**
     * 删除 key 对应的元素。
     *
     * @param key 要删除的键。
     * @return 被删除的元素；如果键不存在或操作失败，则返回 {@code null}。
     */
    Object del(String key);

    /**
     * 删除哈希表中指定键对应的元素。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @return 被删除的元素；如果哈希表或 mapKey 不存在，则返回 {@code null}。
     */
    Object del(String key, String mapKey);

    /**
     * 删除列表中指定索引的元素。
     *
     * @param key   列表的键。
     * @param index 要删除的元素的索引。
     * @return 被删除的元素；如果列表或索引不存在，则返回 {@code null}。
     */
    Object del(String key, int index);

    /**
     * 获取 key 对应的整数值。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的整数值；如果失败，则返回 {@code defVal}。
     */
    Integer getInteger(String key, Integer defVal);

    /**
     * 获取哈希表中指定键对应的整数值。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的整数值；如果失败，则返回 {@code defVal}。
     */
    Integer getInteger(String key, String mapKey, Integer defVal);

    /**
     * 获取列表中指定索引的整数值。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的整数值；如果失败，则返回 {@code defVal}。
     */
    Integer getInteger(String key, int index, Integer defVal);

    /**
     * 获取 key 对应的字符串值。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字符串值；如果失败，则返回 {@code defVal}。
     */
    String getString(String key, String defVal);

    /**
     * 获取哈希表中指定键对应的字符串值。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字符串值；如果失败，则返回 {@code defVal}。
     */
    String getString(String key, String mapKey, String defVal);

    /**
     * 获取列表中指定索引的字符串值。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字符串值；如果失败，则返回 {@code defVal}。
     */
    String getString(String key, int index, String defVal);

    /**
     * 获取 key 对应的布尔值。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的布尔值；如果失败，则返回 {@code defVal}。
     */
    Boolean getBoolean(String key, Boolean defVal);

    /**
     * 获取哈希表中指定键对应的布尔值。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的布尔值；如果失败，则返回 {@code defVal}。
     */
    Boolean getBoolean(String key, String mapKey, Boolean defVal);

    /**
     * 获取列表中指定索引的布尔值。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的布尔值；如果失败，则返回 {@code defVal}。
     */
    Boolean getBoolean(String key, int index, Boolean defVal);

    /**
     * 获取 key 对应的长整型值。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的长整型值；如果失败，则返回 {@code defVal}。
     */
    Long getLong(String key, Long defVal);

    /**
     * 获取哈希表中指定键对应的长整型值。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的长整型值；如果失败，则返回 {@code defVal}。
     */
    Long getLong(String key, String mapKey, Long defVal);

    /**
     * 获取列表中指定索引的长整型值。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的长整型值；如果失败，则返回 {@code defVal}。
     */
    Long getLong(String key, int index, Long defVal);

    /**
     * 获取 key 对应的双精度浮点型值。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的双精度浮点型值；如果失败，则返回 {@code defVal}。
     */
    Double getDouble(String key, Double defVal);

    /**
     * 获取哈希表中指定键对应的双精度浮点型值。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的双精度浮点型值；如果失败，则返回 {@code defVal}。
     */
    Double getDouble(String key, String mapKey, Double defVal);

    /**
     * 获取列表中指定索引的双精度浮点型值。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的双精度浮点型值；如果失败，则返回 {@code defVal}。
     */
    Double getDouble(String key, int index, Double defVal);

    /**
     * 获取 key 对应的字节数组。
     *
     * @param key    要获取的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字节数组；如果失败，则返回 {@code defVal}。
     */
    byte[] getByteArray(String key, byte[] defVal);

    /**
     * 获取哈希表中指定键对应的字节数组。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字节数组；如果失败，则返回 {@code defVal}。
     */
    byte[] getByteArray(String key, String mapKey, byte[] defVal);

    /**
     * 获取列表中指定索引的字节数组。
     *
     * @param key    列表的键。
     * @param index  要获取的元素的索引。
     * @param defVal 当值不存在或类型不匹配时返回的默认值。
     * @return 转换后的字节数组；如果失败，则返回 {@code defVal}。
     */
    byte[] getByteArray(String key, int index, byte[] defVal);

    /**
     * 插入 key 对应的可序列化对象，并设置过期时间。
     *
     * @param key    要存储的键。
     * @param val    要存储的对象（必须实现 {@link java.io.Serializable}）。
     * @param second 过期时间，单位为秒。
     */
    void setObject(String key, Object val, int second);

    /**
     * 插入哈希表中指定键对应的可序列化对象，并设置过期时间。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param val    要存储的对象（必须实现 {@link java.io.Serializable}）。
     * @param second 过期时间，单位为秒。
     */
    void setObject(String key, String mapKey, Object val, int second);

    /**
     * 替换列表中指定索引的可序列化对象，并设置过期时间。
     *
     * @param key    列表的键。
     * @param index  要替换的元素的索引。
     * @param val    要设置的新对象（必须实现 {@link java.io.Serializable}）。
     * @param second 过期时间，单位为秒。
     */
    void setObject(String key, int index, Object val, int second);

    /**
     * 向列表的末尾追加一个可序列化对象。
     *
     * @param key 列表的键。
     * @param val 要追加的对象（必须实现 {@link java.io.Serializable}）。
     */
    void addObject(String key, Object val);

    /**
     * 获取 key 对应的可序列化对象。
     *
     * @param key 要获取的键。
     * @param defVal 当值不存在时返回的默认值。
     * @param <T> 要转换的类型。
     * @return 反序列化后的对象；如果键不存在或反序列化失败，则返回 {@code null}。
     */
    <T> T getObject(String key,T defVal);

    /**
     * 获取哈希表中指定键对应的可序列化对象。
     *
     * @param key    哈希表的主键。
     * @param mapKey 哈希表中的键。
     * @param defVal 当值不存在时返回的默认值。
     * @param <T>    要转换的类型。
     * @return 反序列化后的对象；如果哈希表或 mapKey 不存在或反序列化失败，则返回 {@code null}。
     */
    <T> T getObject(String key, String mapKey,T defVal);

    /**
     * 获取列表中指定索引的可序列化对象。
     *
     * @param key   列表的键。
     * @param index 要获取的元素的索引。
     * @param defVal 当值不存在时返回的默认值。
     * @param <T>   要转换的类型。
     * @return 反序列化后的对象；如果列表或索引不存在或反序列化失败，则返回 {@code null}。
     */
    <T> T getObject(String key, int index,T defVal);


    /**
     * 向队列的末尾追加一个元素。这是一个非阻塞的生产者操作。
     *
     * @param key 队列的键。
     * @param val 要追加的元素。
     */
    void queueProduction(String key, Object val);

    /**
     * 获取并移除队列头部的第一个元素。这是一个非阻塞的消费者操作。
     *
     * @param key 队列的键。
     * @return 队列中的第一个元素；如果队列为空，则返回 {@code null}。
     */
    Object queueConsumption(String key);

    /**
     * 获取并移除队列头部的第一个元素，如果队列为空则阻塞。
     *
     * @param key    队列的键。
     * @param second 最大阻塞时间，单位为秒。如果为 0，则无限期阻塞。
     * @return 队列中的第一个元素；如果超时，则返回 {@code null}。
     */
    Object queueConsumptionBlock(String key, int second);


    /**
     * 向队列的末尾追加一个可序列化对象。这是一个非阻塞的生产者操作。
     *
     * @param key 队列的键。
     * @param val 要追加的对象（必须实现 {@link java.io.Serializable}）。
     * @param <T> 元素的类型。
     */
    <T> void queueProductionObject(String key, T val);

    /**
     * 获取并移除队列头部的第一个可序列化对象。这是一个非阻塞的消费者操作。
     *
     * @param key 队列的键。
     * @param <T> 要转换的类型。
     * @return 队列中的第一个反序列化后的对象；如果队列为空或反序列化失败，则返回 {@code null}。
     */
    <T> T queueConsumptionObject(String key);

    /**
     * 获取并移除队列头部的第一个可序列化对象，如果队列为空则无限期阻塞。
     *
     * @param key 队列的键。
     * @param <T> 要转换的类型。
     * @return 队列中的第一个反序列化后的对象；如果操作失败，则返回 {@code null}。
     */
    <T> T queueConsumptionBlockObject(String key);

    /**
     * 获取并移除队列头部的第一个可序列化对象，如果队列为空则阻塞。
     *
     * @param key    队列的键。
     * @param second 最大阻塞时间，单位为秒。如果为 0，则无限期阻塞。
     * @param <T>    要转换的类型。
     * @return 队列中的第一个反序列化后的对象；如果超时，则返回 {@code null}。
     */
    <T> T queueConsumptionBlockObject(String key, int second);
}