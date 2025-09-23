package gzb.tools.log;

import gzb.frame.annotation.Service;


@Service
public class LogImpl implements Log {
    public static LogThread thread = new LogThread();
    Class<?>aClass;

    public LogImpl() {
    }
    public LogImpl(Class<?>aClass) {
        this.aClass = aClass;
    }


    @Override
    public void print(int index, Object... log) {
       thread.addLog(index,this.aClass,log);
    }

    @Override
    public void t(Object... log) {
        print(0, log);
    }
    @Override
    public void d(Object... log) {
        print(1, log);
    }

    @Override
    public void i(Object... log) {
        print(2, log);
    }

    @Override
    public void w(Object... log) {
        print(3, log);
    }

    @Override
    public void e(Object... log) {
        print(4, log);
    }

    @Override
    public void s(String sql, long start, long end) {
        if (end - start < 100){
            print(1, sql,"耗时",end-start,"毫秒");
        }else if (end - start < 1000){
            print(3, sql,"耗时",end-start,"毫秒");
        }else{
            print(4, sql,"耗时",end-start,"毫秒");
        }
    }
}