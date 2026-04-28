package gzb.frame.generate.v2;

import gzb.entity.TableInfo;
import gzb.frame.factory.ClassLoad;
import gzb.frame.template.GzbTemplate;
import gzb.tools.Config;
import gzb.tools.FileTools;
import gzb.tools.Tools;

import java.io.File;
import java.util.List;

public class Generate {
    String path0 = null;
    List<TableInfo> tableInfos = null;
    String pkg0 = null;
    String dbName = null;
    String dbNameHumpLowerCase = null;

    String dbNameHumpUpperCase = null;

    public Generate(String path, List<TableInfo> tableInfos, String pkg) {
        if (tableInfos == null || tableInfos.size() < 1) {
            throw new RuntimeException("List<TableInfo> tableInfos == null");
        }
        this.tableInfos = tableInfos;
        this.path0 = path;
        this.dbName = tableInfos.get(0).getDbName();
        this.dbNameHumpLowerCase = tableInfos.get(0).getDbNameHumpLowerCase();
        this.dbNameHumpUpperCase = tableInfos.get(0).getDbNameHumpUpperCase();
        this.pkg0 = pkg+ "." + dbNameHumpLowerCase;
    }

    public void entity() throws Exception {
        entity(false);
    }

    public void entity(boolean cover) throws Exception {
        String pkg = pkg0  + ".entity";
        Class<?> c01 = ClassLoad.compileJavaCode(GzbTemplate.generate(Tools.getResourceString(Generate.class, "entity.jsp", Config.encoding)));
        String path = path0 + "/" + (pkg.replaceAll("\\.", "/"));
        path = path.replaceAll("\\\\", "/").replaceAll("\\\\", "/")
                .replace("//", "/").replace("//", "/");
        Object obj = c01.getConstructor().newInstance();
        for (TableInfo info : tableInfos) {
            String code = c01.getMethod("_gzb_tem_001", TableInfo.class, String.class).invoke(obj, info, pkg).toString();
            File file = new File(path, info.nameHumpUpperCase + ".java");
            if (!cover) {
                if (file.exists()) {
                    System.out.println("exists:" + file.getAbsolutePath());
                    continue;
                }
            }
            FileTools.createFile(file);
            FileTools.save(file, code);
            System.out.println("save:" + file.getAbsolutePath());
        }
    }

}
