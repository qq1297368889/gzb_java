package com.action;

import gzb.frame.annotation.*;
import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbQueue;
import gzb.tools.cache.GzbQueueImpl;
import gzb.tools.cache.entity.Entity;
import gzb.tools.cache.object.ObjectCache;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;
/// 提供 tcp服务 三个接口 /queue/produce  /queue/consume  /queue/confirm
/// 写个小玩具 2
@Controller
@RequestMapping("queue")
public class QueueAction {
    /// 返回码说明
    /// 11 生产者 data为空
    /// 12 索引 i为空
    /// 13 索引对应实例不存在
    /// 14 消费者最大等待时间为空  小于0不等待 0无限等待 大于零0为秒数
    /// 15 消费者返回数据为空
    /// 16 消费后确认时 id为空
    @EventLoop
    @PostMapping("/produce")
    public String produce(String d, Integer i) throws Exception {
        if (i == null) {
            return "12";
        }
        if (d == null) {
            return "11";
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return "13";
        }
        gzbQueue.produce(d);
        return "1";
    }
    // @EventLoop //允许 阻塞 所以不能占用事件循环
    @PostMapping("/consume")
    public String consume(Integer i,Integer s) throws Exception {
        if (i == null) {
            return "12";
        }
        if (s == null) {
            return "14";
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return "13";
        }
        Entity entity=gzbQueue.consume(s);
        if (entity==null) {
            return "15";
        }
        return "1 "+ entity.id+" "+entity.data;
    }
    @EventLoop
    @PostMapping("/confirm")
    public String confirm(Long id, Integer i) throws Exception {
        if (i == null) {
            return "12";
        }
        if (id<1) {
            return "16";
        }
        GzbQueue gzbQueue= QueueData.get(i);
        if (gzbQueue == null) {
            return "13";
        }
        return "1 "+gzbQueue.confirm(id);
    }

}
