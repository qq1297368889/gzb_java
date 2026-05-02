package gzb.tools.thread;


import gzb.frame.factory.ClassLoad;
import gzb.tools.Config;
import gzb.tools.log.Log;

import java.lang.reflect.InvocationTargetException;

public class GzbVT {
    public static GzbThread threadPool = null;
    public static boolean vt = false;

    public static Thread start(String name, Runnable runnable) {
        if (threadPool == null) {
            throw new RuntimeException(name);
        }
        return threadPool.start(name, runnable);
    }

    public static boolean execute(Runnable runnable) {
        if (threadPool == null) {
            throw new RuntimeException();
        }
        threadPool.start("gzb-thread", runnable);
        return true;
    }

    static {
        String code1 = "package gzb.tools.thread;\n" +
                "public class ThreadImpl implements gzb.tools.thread.GzbVT.GzbThread {\n" +
                "    public java.lang.Thread start(String name,Runnable runnable){\n" +
                "        return java.lang.Thread.ofVirtual()\n" +
                "                .name(name, 1) // 设置名称前缀和起始编号\n" +
                "                .start(runnable);\n" +
                "    }\n" +
                "}";
        String code2 = "package gzb.tools.thread;\n" +
                "public class ThreadImpl implements gzb.tools.thread.GzbVT.GzbThread {\n" +
                "    public java.lang.Thread start(String name, Runnable runnable) {\n" +
                "        java.lang.Thread thread = new java.lang.Thread(runnable);\n" +
                "        thread.setName(name);\n" +
                "        thread.start();\n" +
                "        return thread;\n" +
                "    }\n" +
                "}";
        Class<?> aClass = null;
        try {
            aClass = ClassLoad.compileJavaCode(code1);
            vt = Config.getBoolean("gzb.system.vt.open");
        } catch (Exception e) {
            try {
                aClass = ClassLoad.compileJavaCode(code2);
                vt = false;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }
        try {
            threadPool = (GzbThread) aClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Log.log.d("vt is open",vt);
    }


    public static interface GzbThread {
        Thread start(String name, Runnable runnable);
    }
}
