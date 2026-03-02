package gzb.tools.thread;

import gzb.tools.OnlyId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceThread {

    public static Map<String, List<Thread>> serviceThread = new ConcurrentHashMap<>();

    public static List<Thread> readService(String name) {
        return serviceThread.get(name);
    }
    public static void start(int num, String name,Runnable runnable) {
        for (int i = 0; i < num; i++) {
            start(name+"-"+i,runnable);
        }
    }
    public static void start(Runnable runnable) {
       start(null,runnable);
    }
    public static void start(String name,Runnable runnable) {
        if (name == null) {
            name = "ServiceThread." + OnlyId.getDistributed();
        }
        Thread thread = new Thread(runnable, name);
        List<Thread> list = serviceThread.get(name);
        if (list == null) {
            list = new ArrayList<>();
            serviceThread.put(name, list);
        }
        list.add(thread);
        thread.start();
    }

}
