package com.frame.thread.hot;
import gzb.frame.annotation.ThreadFactory;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.json.Result;
import gzb.tools.log.Log;
import java.util.concurrent.locks.Lock;

//标注 这是一个需要被框架调度的类(后台线程)
@ThreadFactory
public class HotBackgroundThread {
    /// 框架提供的对象  和 @service对象 都可以被注入到类变量和参数
    /// 注意 这个类 在运行中 不能直接被删除 否则线程失控。（重新创建并有一致签名 即可接着影响线程的行为）
    /// 之所以提供线程模型是因为 热更新 需要的对象 通过 参数通过  可以防止使用旧版本对象

    /// 修改方法内容 不修改签名的话 可以在不丢失 状态的情况下 热更新
    /// 修改签名的话会丢失状态 重新开始调度
    /// 私有变量 不会有并发问题 但是多次被调用时 会记录状态  专属 result
    /// 方法专属锁 lock  仅该方法内部使用 启用多个线程的时候 防止并发  和其他被调度的方法 不是同一个锁
    /// 定义 这是一个需要被调度的方法 num是启动线程数量  value 是调度周期 单位毫秒
    @ThreadInterval(num=0 ,value=1000)
    public Object test001(Result result, Log log, Lock lock) throws Exception {
        //sysUsersDao @service 实现类
        int intx=result.getInteger("intx",0);
        intx++;
        result.set("intx",intx);
        log.d(intx,lock,result.hashCode());
        return null;
        /// 继续循环 如果不需要主动停止 可以直接设置为 void方法  返回非null 即可停止被框架调度 本次启动无法再次被调度 除非方法签名出现变法
    }

}
