package gzb.start;

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;
import gzb.tools.Tools;

public class Auto {
    public static void main(String[] args) throws Exception {
        System.setProperty("this.dir", Tools.getProjectRoot(Auto.class));
        String path = Config.thisPath() + "/src/main/java";
        String pkg = "com";
        String dbName = "frame";
        GenerateJavaCode.generateCode(path,pkg,0,dbName,dbName);
    }
}
