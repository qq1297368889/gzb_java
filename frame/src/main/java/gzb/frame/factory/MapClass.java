package gzb.frame.factory;

import gzb.entity.ThreadInfo;
import gzb.frame.server.http.entity.ClassFileInfo;
import gzb.frame.server.http.entity.ClassInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class MapClass {
    //储存所有类  类名->类
    public Map<String, Semaphore> semaphoreMap = new ConcurrentHashMap<String, Semaphore>(){
        @Override
        public Semaphore get(Object key) {
            Semaphore semaphore = super.get(key);
            if (semaphore==null) {

            }
            return super.get(key);
        }
    };
    //储存 创建对象的时候 需要的公共对象类名  需要保证在 mapObject已有
    public List<String> listClassName0 = new ArrayList<>();
    //储存所有类  类名->类
    public Map<String, Class> mapClass0 = new ConcurrentHashMap<String, Class>() {
        @Override
        public Class put(String key, Class value) {
            mapClass1.remove(key);
            return super.put(key, value);
        }
    };
    //储存所有对象  类名或接口名 ->对象
    public Map<String, Object> mapObjectAll0 = new ConcurrentHashMap<String, Object>() {
        @Override
        public Object put(String key, Object value) {
            mapObjectAll1.remove(key);
            return super.put(key, value);
        }
    };
    //储存所有类信息  类名->对象
    public Map<String, ClassInfo> mapClassInfo0 = new ConcurrentHashMap<String, ClassInfo>() {
        @Override
        public ClassInfo put(String key, ClassInfo value) {
            mapClassInfo1.remove(key);
            return super.put(key, value);
        }
    };
    //储存映射信息 请求路径 -> [类名,方法名]
    public Map<String, Object[]> mapping0 = new ConcurrentHashMap<String, Object[]>() {
        @Override
        public Object[] put(String key, Object[] value) {
            mapping1.remove(key);
            return super.put(key, value);
        }
    };
    //储存所有装饰器对象在mapClassInfo的KEY
    public Map<String, String> mapDecorator0 = new ConcurrentHashMap<String, String>() {
        @Override
        public String put(String key, String value) {
            mapDecorator1.remove(key);
            return super.put(key, value);
        }
    };
    //所有从文件编译的类
    public Map<String, ClassFileInfo> mapClassFile0 = new ConcurrentHashMap<String, ClassFileInfo>() {
        @Override
        public ClassFileInfo put(String key, ClassFileInfo value) {
            mapClassFile1.remove(key);
            return super.put(key, value);
        }
    };
    //所有从文件编译的类
    public Map<String, ThreadInfo> mapThread0 = new ConcurrentHashMap<String, ThreadInfo>() {
        @Override
        public ThreadInfo put(String key, ThreadInfo value) {
            maThread1.remove(key);
            return super.put(key, value);
        }
    };

    //储存 创建对象的时候 需要的公共对象类名  需要保证在 mapObject已有
    List<String> listClassName1 = new ArrayList<>();
    //储存所有类  类名->类
    Map<String, Class> mapClass1 = new ConcurrentHashMap<>();
    //储存所有对象  类名或接口名 ->对象
    Map<String, Object> mapObjectAll1 = new ConcurrentHashMap<>();
    //储存所有类信息  类名->对象
    Map<String, ClassInfo> mapClassInfo1 = new ConcurrentHashMap<>();
    //储存映射信息 请求路径 -> [类名,方法名]
    Map<String, Object[]> mapping1 = new ConcurrentHashMap<>();
    //储存所有装饰器对象在mapClassInfo的KEY
    Map<String, String> mapDecorator1 = new ConcurrentHashMap<>();
    //所有从文件编译的类
    Map<String, ClassFileInfo> mapClassFile1 = new ConcurrentHashMap<>();
    //所有从文件编译的类
    Map<String, ThreadInfo> maThread1 = new ConcurrentHashMap<>();

    public void delete_0() {
        // System.out.println("delete_0 - start "+ this.toString());
        mapClass1.forEach((k, v) -> {
            mapClass0.remove(k);
        });
        mapObjectAll1.forEach((k, v) -> {
            mapObjectAll0.remove(k);
        });
        mapClassInfo1.forEach((k, v) -> {
            mapClassInfo0.remove(k);
        });
        mapping1.forEach((k, v) -> {
            mapping0.remove(k);
        });
        mapDecorator1.forEach((k, v) -> {
            mapDecorator0.remove(k);
        });
        mapClassFile1.forEach((k, v) -> {
            mapClassFile0.remove(k);
        });
        maThread1.forEach((k, v) -> {
            mapThread0.remove(k);
        });
    }

    public void to_0_1() {
        //System.out.println("to_0_1 - start "+ this.toString());
        mapClass1 = new ConcurrentHashMap<>(mapClass0);
        mapObjectAll1 = new ConcurrentHashMap<>(mapObjectAll0);
        mapClassInfo1 = new ConcurrentHashMap<>(mapClassInfo0);
        mapping1 = new ConcurrentHashMap<>(mapping0);
        mapDecorator1 = new ConcurrentHashMap<>(mapDecorator0);
        mapClassFile1 = new ConcurrentHashMap<>(mapClassFile0);
        maThread1 = new ConcurrentHashMap<>(mapThread0);
        //System.out.println("to_0_1 - end "+ this.toString());
    }

    @Override
    public String toString() {
        return "MapClass{" + "\n" +
                "listClassName0=" + listClassName0 + "\n" +
                ", mapClass0=" + mapClass0 + "\n" +
                ", mapObjectAll0=" + mapObjectAll0 + "\n" +
                ", mapClassInfo0=" + mapClassInfo0 + "\n" +
                ", mapping0=" + mapping0 + "\n" +
                ", mapDecorator0=" + mapDecorator0 + "\n" +
                ", mapClassFile0=" + mapClassFile0 + "\n" +
/*                ", listClassName1=" + listClassName1 +"\n"+
                ", mapClass1=" + mapClass1 +"\n"+
                ", mapObjectAll1=" + mapObjectAll1 +"\n"+
                ", mapClassInfo1=" + mapClassInfo1 +"\n"+
                ", mapping1=" + mapping1 +"\n"+
                ", mapDecorator1=" + mapDecorator1 +"\n"+
                ", mapClassFile1=" + mapClassFile1 +"\n"+*/
                '}';
    }
}
