package gzb;

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;
import gzb.tools.Tools;

public class AutoCode {
    //根据数据库表信息 逆向 声称 dao entity controller
    public static void main(String[] args) throws Exception {
        //必要 必须把当前类传过去 用于获取项目目录
        System.setProperty("this.dir", Tools.getProjectRoot(AutoCode.class));
        //要生成代码到这个目录
        String path = Config.thisPath() + "/src/main/java";
        //要生成代码的包名
        String pkg = "com";
        //数据库名 要和 db.mysql.数据库名 这里匹配
        String dbKey = "frame";
        //生成代码的函数 感兴趣可以去看看里边 直接调用里边的也可以
        GenerateJavaCode.generateCode(path,pkg,1,dbKey,dbKey);
        //复制一些 UI文件过来 保障正常运行
        Tools.copyStaticFile(AutoCode.class);
        //结束运行
        System.exit(0);
    }
}
