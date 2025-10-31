package gzb.start;

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;

public class AutoCode {
    //根据数据库表信息 逆向 声称 dao entity controller
    public static void main(String[] args) throws Exception {
        //要生成代码到这个目录
        String path = Config.thisPath() + "/src/main/java";
        //要生成代码的包名
        String pkg = "com";
        //数据库名 要和 db.mysql.数据库名 这里匹配
        String dbKey = "db002";
        //生成代码的函数 感兴趣可以去看看里边 直接调用里边的也可以
        GenerateJavaCode.generateCode(path,pkg,dbKey,dbKey);

    }
}
