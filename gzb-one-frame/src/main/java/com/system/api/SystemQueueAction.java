package com.system.api;

import gzb.frame.annotation.EventLoop;
import gzb.frame.annotation.PostMapping;
import com.system.tools.QueueData;
import com.system.tools.ResponseData;
import gzb.tools.cache.GzbQueue;

/// 提供 tcp服务 三个接口 /queue/produce  /queue/consume  /queue/confirm

public class SystemQueueAction {
    ///  8080/queue/produce?d=1001&i=10
    /// 返回码说明
    /// 11 生产者 data为空
    /// 12 索引 i为空
    /// 13 索引对应实例不存在
    /// 14 消费者最大等待时间为空  小于0不等待 0无限等待 大于零0为秒数
    /// 15 消费者返回数据为空
    /// 16 消费后确认时 id为空
    @EventLoop
    @PostMapping("/produce")
    public String produce(String d, Integer i,long sid) throws Exception {
        if (i == null) {
            return ResponseData.send(sid, 12);
        }
        if (d == null) {
            return ResponseData.send(sid, 11);
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return ResponseData.send(sid, 13);
        }
        gzbQueue.produce(d);
        return ResponseData.send(sid, 1);
    }
    // @EventLoop //允许 阻塞 所以不能占用事件循环
    @PostMapping("/consume")
    public String consume(Integer i,Integer s,long sid) throws Exception {
        if (i == null) {
            return ResponseData.send(sid, 12);
        }
        if (s == null) {
            return ResponseData.send(sid, 14);
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return ResponseData.send(sid, 13);
        }
        GzbQueue.Entity entity=gzbQueue.consume(s);
        if (entity==null) {
            return ResponseData.send(sid, 15);
        }
        return ResponseData.send(sid, 1,entity.id+","+entity.data);
    }
    @EventLoop
    @PostMapping("/confirm")
    public String confirm(Long id, Integer i,long sid) throws Exception {
        if (i == null) {
            return ResponseData.send(sid, 12);
        }
        if (id<1) {
            return ResponseData.send(sid, 16);
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return ResponseData.send(sid, 13);
        }

        return ResponseData.send(sid, 1,gzbQueue.confirm(id));
    }

}
