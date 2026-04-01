package gzb.start;

import com.frame.dao.SysFileDao;
import com.frame.dao.impl.SysFileDaoImpl;
import gzb.entity.TableInfo;
import gzb.frame.factory.ClassLoad;
import gzb.frame.template.GzbTemplate;
import gzb.tools.FileTools;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class TemTest {
    public static void main(String[] args) throws Exception {
        File file = new File("E:\\codes_20220814\\java\\250913_code\\frame\\src\\main\\java\\gzb\\frame\\generate\\v2\\entity.jsp");
        Class c01= ClassLoad.compileJavaCode(GzbTemplate.generate(file));
        System.out.println(c01);
        SysFileDao sysFileDao = new SysFileDaoImpl();
        List<TableInfo>list=sysFileDao.getDataBase().getTableInfo();
        Method met=c01.getMethod("_gzb_tem_001", TableInfo.class,String.class);
        Object obj=c01.getConstructor().newInstance();
        for (TableInfo tableInfo : list) {
            File file1=new File("E:\\codes_20220814\\java\\250913_code\\demo\\src\\main\\java\\gzb\\start\\entity\\"+tableInfo.nameHumpUpperCase+".java");
            FileTools.createFile(file1);
            FileTools.save(file1,met.invoke(obj,tableInfo,"gzb.start.entity").toString());
        }
    }
}
