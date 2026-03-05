package gzb.start;

import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbCacheRedis;

public class Test2 {
    public static void main(String[] args) {
        GzbCache gzbCache = new GzbCacheRedis("cache1");
        gzbCache.set("test1","{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\"哈哈哈\"}",20);
        long size,start,end;

        for (int i = 0; i < 10; i++) {
            size=0;
            start=System.nanoTime();
            for (int j = 0; j < 10000 ; j++) {
                if (gzbCache.get("test1").contains("{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\"哈哈哈\"}")) {
                    size++;
                }else{
                    System.err.println("err");
                }
            }
            end=System.nanoTime();
            System.out.println((end-start)/1000/(10000));
        }

    }
}
