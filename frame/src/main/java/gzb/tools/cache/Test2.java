package gzb.tools.cache;

import com.alibaba.fastjson2.JSON;
import gzb.entity.ClassEntity;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.io.*;

public class Test2 {

    public static void main(String[] args) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.sign="1";
        classEntity.code="2";
        classEntity.pwd="4";
        classEntity.filePath="5";
        classEntity.iv="6";
        GzbCache gzbCache = new GzbCacheMap();
        long start,end,size = 0;
        for (int i = 0; i < 1000; i++) {
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 10000; i1++) {
                gzbCache.set("key_str_"+i1,"val_"+i1,60);
                gzbCache.setObject("key_obj_"+i1,classEntity,60);
            }
            end=System.currentTimeMillis();
            Log.log.i("set",end-start,size);
            start=System.currentTimeMillis();
            for (int i1 = 0; i1 < 10000; i1++) {
               size+= gzbCache.get("key_str_"+i1).length();
                ClassEntity classEntity2 =gzbCache.getObject("key_obj_"+i1);
                size+= classEntity2.toString().length();
            }
            end=System.currentTimeMillis();
            Log.log.i("get",end-start,size);
        }
    }
    public static void main0(String[] args) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.sign="1";
        classEntity.code="2";
        classEntity.pwd="4";
        classEntity.filePath="5";
        classEntity.iv="6";
        for (int n = 0; n < 100; n++) {
            long size=0;
            long start=System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                byte[]bytes=serialize(classEntity);
                ClassEntity classEntity2 = deserialize(bytes);
                size+=classEntity2.toString().length();
            }
            long end=System.currentTimeMillis();
            Log.log.i("byte",end-start,size);
            size=0;
            start=System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                byte[]bytes=Tools.serialize(classEntity);
                ClassEntity classEntity2 = Tools.deserialize(bytes);
                size+=classEntity2.toString().length();
            }
            end=System.currentTimeMillis();
            Log.log.i("JSON",end-start,size);
        }

    }

    /**
     * 将 Object 序列化为 byte 数组。
     *
     * @param obj 要序列化的对象。
     * @return 序列化后的 byte 数组，失败返回 null。
     */
    private static byte[] serialize(Object obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将 byte 数组反序列化为 Object。
     *
     * @param bytes 序列化后的 byte 数组。
     * @return 反序列化后的 Object，失败返回 null。
     */
    @SuppressWarnings("unchecked")
    private static <T> T deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
