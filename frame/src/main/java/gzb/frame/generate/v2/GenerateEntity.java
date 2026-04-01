package gzb.frame.generate.v2;

import gzb.entity.TableInfo;
import gzb.frame.db.DataBase;
import gzb.frame.db.DataBaseImpl;
import gzb.frame.factory.ClassLoad;
import gzb.frame.template.GzbTemplate;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.Tools;
import gzb.tools.thread.GzbThreadLocal;

import java.io.File;

public class GenerateEntity {
    public static void main(String[] args) throws Exception {
        File file = new File("E:\\codes_20220814\\java\\250913_code\\frame\\src\\main\\java\\gzb\\frame\\generate\\v2\\entity.jsp");
        Class c01= ClassLoad.compileJavaCode(GzbTemplate.generate(file));
        System.out.println(c01);
        c01.getMethod("_gzb_tem_001", TableInfo.class).invoke(null,"gzb");
    }

}
