package gzb.frame.factory.v5;

import gzb.frame.factory.ClassLoad;
import gzb.frame.factory.ClassLoadEvent;
import gzb.frame.factory.ClassTools;
import gzb.tools.FileTools;
import gzb.tools.log.Log;
import gzb.tools.thread.ServiceThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class HotUpdateFactory {
public static class Entity{
    Class<?>aClass;
    String code;
    File file;
}
    public static final LinkedBlockingQueue<Entity> queue = new LinkedBlockingQueue<>();
    public static final ClassLoadEvent classLoadEvent = new ClassLoadEvent();

    //储存文件更新 时间
    public static final Map<String, Long> file_update_time = new ConcurrentHashMap<>();
    public static Log log;
    public static File[] dirs;

    static {
        ServiceThread.start("HotUpdateFactory-file-read", () -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                    List<Class<?>> list1 = compilation(readList(),true);
                    log.d("热更新扫描完成，编译了 " + list1.size() + " 个类");
                } catch (InterruptedException e) {
                    break;
                } catch (Exception e) {
                    log.e("热更新扫描异常", e);
                }
            }
        });
    }

    public static void init(String[] dirs0) {
        log = Log.log;
        if (dirs0 == null) {
            log.w("类加载目录为空不会启动自动扫描");
            return;
        }
        dirs = new File[dirs0.length];
        for (int i = 0; i < dirs0.length; i++) {

            File file = new File(dirs0[i]);
            if (file.exists()) {
                log.w("类加载目录不存在", dirs0[i]);
                continue;
            }
            dirs[i] = file;
        }
    }

    public static List<File> readList() {
        List<File> list = new ArrayList<>();
        for (int i = 0; i < dirs.length; i++) {
            if (!dirs[i].exists()) {
                continue;
            }
            List<File> list1 = FileTools.subFile(dirs[i], 2);
            for (File file : list1) {
                if (file.getName().endsWith(".java")) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    public static List<Class<?>> compilation(List<File> list,boolean append) {
        List<Class<?>> list1 = new ArrayList<>();
        int state = 0;
        for (File file : list) {
            if (file_update_time.get(file.getPath()) == file.lastModified()) {
                continue;
            }
            if (state == 0) {
                classLoadEvent.eventStart();
            }
            state++;
            try {
                String code = FileTools.readString(file);
                String className = ClassTools.extractPublicClassName(code);
                classLoadEvent.eventClassStart(className);
                Class<?> c01 = ClassLoad.compileJavaCode(code, className);
                classLoadEvent.eventClassEnd(className);
                if (c01 == null) {
                    log.w("编译异常 返回类为 null", file.getPath());
                    continue;
                }
                file_update_time.put(file.getPath(), file.lastModified());
                list1.add(c01);
                if (append) {
                    Entity entity = new Entity();
                    entity.aClass = c01;
                    entity.code = code;
                    entity.file = file;
                    queue.add(entity);
                }
            } catch (Exception e) {
                log.e("编译失败", file.getPath(), e);
            }
        }
        if (state > 0) {
            classLoadEvent.eventEnd(null);//后续需要改为实际回调内容
        }
        return list1;
    }
}
