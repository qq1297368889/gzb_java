package gzb.tools.log;

import gzb.frame.annotation.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogImpl implements Log {
    public static void main(String[] args) throws Exception {
        String[] objects1 = new String[]{"GH", "SD", "kk"};
        Character[] objects2 = new Character[]{'1', '2', '3'};
        char[] objects3 = new char[]{'4', '5'};
        Object[] objects4 = new Object[]{objects1, objects2, objects3};
        Object[] objects5 = new Object[]{objects4};
        Object[] objects6 = new Object[]{objects5, 5, 6.5, "ss"};
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key1", 1);
        map1.put("key2", 2);
        map1.put("key3", 3);
        map1.put("key4", 4);
        map1.put("objects1", objects1);
        Object[] objects7 = new Object[]{objects6, "233", 88, 99, 66.22, map1};
        Map<String, Object> map2 = new HashMap<>();
        map2.put("objects7", objects7);
        List<Object> list = new ArrayList<>();
        list.add(map1);
        Exception e = new IOException("抛出异常");
        LogImpl logLv2 = new LogImpl();
        logLv2.d(map2, "哈哈哈", list, e);
    }

    Class aClass;

    public LogImpl() {
    }
    public LogImpl(Class aClass) {
        this.aClass = aClass;
    }


    @Override
    public void print(int index, Object... log) {
        LogThread.thread.addLog(index,this.aClass,log);
    }

    @Override
    public void d(Object... log) {
        print(0, log);
    }

    @Override
    public void i(Object... log) {
        print(1, log);
    }

    @Override
    public void w(Object... log) {
        print(2, log);
    }

    @Override
    public void e(Object... log) {
        print(3, log);
    }

    @Override
    public void s(String sql, long start, long end) {
        if (end - start < 100){
            print(0, sql,end-start);
        }else if (end - start < 1000){
            print(2, sql,start,end-start);
        }else{
            print(3, sql,start,end-start);
        }
    }
}