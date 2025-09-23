package com.frame.component;
import com.frame.dao.SysUsersDao;
import gzb.frame.annotation.ThreadFactory;
import gzb.frame.annotation.ThreadInterval;
import gzb.tools.json.Result;
import gzb.tools.log.Log;
import java.util.concurrent.locks.Lock;
//标注 这是一个需要被框架调度的类
@ThreadFactory
public class ThreadClass {
    //需要注意 如果更改了类 或 方法签名 前 一定要修改num为0 这时候框架会终止线程 否则的话可能会出现失控线程
    //之所以提供线程模型是因为 热更新 需要的对象 通过 参数通过  可以防止使用旧版本对象


    //定义 这是一个需要被调度的方法 num是启动线程数量  value 是 调度周期 单位毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test001(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        int intx=result.getInteger("intx",0);
        if (intx>20){
            //return false;//非null代表停止
        }
        intx++;
        result.set("intx",intx);
        log.d("3.00",Thread.currentThread().getName(),intx,result.hashCode(),sysUsersDao);
        return null;//继续循环 如果不需要主动停止 可以直接设置为 void方法
    }
    //定义 这是一个需要被调度的方法 num是启动线程数量  value 是 调度周期 单位毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test002(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        return test001(sysUsersDao,result,log,lock);
    }
    //定义 这是一个需要被调度的方法 num是启动线程数量  value 是 调度周期 单位毫秒
    @ThreadInterval(num=0,value=1000)
    public Object test003(SysUsersDao sysUsersDao, Result result, Log log, Lock lock) {
        return test001(sysUsersDao,result,log,lock);
    }
}
