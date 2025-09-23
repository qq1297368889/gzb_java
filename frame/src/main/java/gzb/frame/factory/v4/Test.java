package gzb.frame.factory.v4;

import gzb.frame.factory.ClassTools;
import gzb.tools.Config;
import gzb.tools.Tools;
import gzb.tools.log.Log;

import java.util.List;
import java.util.Map;

public class Test {
    public static Log log= Config.log;
    public static void main(String[] args) throws Exception {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("this.dir", Tools.getProjectRoot(Test.class));
        String code = Tools.fileReadString("E:\\codes_20220814\\java\\250913_code\\demo\\src\\main\\java\\com\\frame\\api\\TestAction.java");

        Class<?>[]pararType=new Class[]{String.class,int.class,List.class,Double[].class,double[].class, Map.class};
        for (Class<?> aClass : pararType) {
            log.d(aClass.getCanonicalName());
        }
        List<String>names=ClassTools.getParameterNames(code,"test",pararType);
        log.d(names);
    }
}
