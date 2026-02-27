package com.frame.event;

import com.frame.dao.*;
import com.frame.entity.*;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.Request;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.List;

// 标注为 数据库事件注册 类
// 这个注释掉 就不会触发事件了 因为可能会影响性能 所以需要手动开启而非自动开启
// 为什么要生成这个类呢 主要是为了提供代码即为文档的功能 让用户更清晰的知道怎么注册事件
// @DataBaseEventFactory
public class FortunesEvent {

    /// 依赖注入规则 和 controller 一致 所有框架调度方法 都是一样的（线程模型例外，因为他没有 req resp）

    @Resource
    Log log;
    @Resource
    FortunesDao fortunesDao;//不仅限于这个对应的dao 其他任何 service都可以被注入

    //entity 注册到 某个实体类 数据库 新增事件
    //executionBefore 事件执行时机，true 主操作执行前 false 主操作执行后 默认false
    //depth 事件传播深度 防止循环传播
    @DataBaseEventUpdate(entity = Fortunes.class, executionBefore = false, depth = 5)
    public void update(
            Fortunes fortunes, //对应的实体类对象
            Request request //来自上下文中的对象 和 controller 注入规则一致
    ) throws Exception {

    }

    @DataBaseEventSelect(entity = Fortunes.class, executionBefore = false, depth = 5)
    public void select(Fortunes fortunes) throws Exception {

    }

    @DataBaseEventSave(entity = Fortunes.class, executionBefore = false, depth = 5)
    public void save(Fortunes fortunes) throws Exception {

    }

    @DataBaseEventDelete(entity = Fortunes.class, executionBefore = false, depth = 5)
    public void delete(Fortunes fortunes) throws Exception {

    }
}