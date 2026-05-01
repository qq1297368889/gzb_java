package gzb.start;

import gzb.frame.factory.v5.HotUpdateFactory;

public class TestClassLoad {
    public static void main(String[] args) throws Exception {
        HotUpdateFactory hotUpdateFactory = new HotUpdateFactory();
        hotUpdateFactory.init("/media/gzb/文档/codes_20220814/java/250913_code/demo/src/main/java/com/frame");
        HotUpdateFactory.Entity entity = hotUpdateFactory.awaitClassLoad(1000);

        System.out.println(entity.aClass);
    }

}
