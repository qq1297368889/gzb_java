package gzb.start;

import gzb.frame.generate.GenerateJavaCode;
import gzb.tools.Config;

public class AutoCode extends Auto{
    //根据数据库表信息 逆向 生成 dao entity controller （可选 webui）
    public static void main(String[] args) throws Exception {
        //要生成代码到这个目录
        String path = Config.thisPath() + "/src/main/java";
        //要生成代码的包名
        String pkg = "com";
        //数据库名 要和 db.mysql.数据库名 这里匹配
        String dbKey = "db002";
        //生成代码的函数 感兴趣可以去看看里边 直接调用里边的也可以 甚至可以使用三方生成器 自己生成
        GenerateJavaCode. generateCode(
                path,
                pkg,
                0, //0生成全部表  1排除系统表   2生成系统表
                false, //如果不使用框架权限管理 则填写为false 反之为  true
                dbKey, //系统数据库名   如果不使用框架权限管理 则填写为业务数据库即可
                dbKey //业务数据库名
        );
    }
}
