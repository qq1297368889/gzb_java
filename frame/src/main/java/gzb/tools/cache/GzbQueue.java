package gzb.tools.cache;

import gzb.tools.cache.entity.Entity;


public interface GzbQueue {
    //生产
    void produce(String val);

    //消费 second小于0 不阻塞 等于0 无限阻塞  大于0为阻塞秒数
    Entity consume(int second);

    //和consume 区别是立刻自动确认
    String consumeAndConfirm(int second);

    //确认
    boolean confirm(long messageId);

    //当前队列长度
    long size();

    //读取 不移除
    Entity read();


}
