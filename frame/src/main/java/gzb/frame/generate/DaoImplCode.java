package gzb.frame.generate;

import gzb.entity.TableInfo;

import java.util.List;

public class DaoImplCode extends Base {
    public DaoImplCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao.impl;\n" +
                    "import gzb.frame.annotation.Service;\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao." + tableInfo.getNameHumpUpperCase() + "Dao;\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".entity." + tableInfo.getNameHumpUpperCase() + ";\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameHumpLowerCase() + "." + tableInfo.getDbNameHumpUpperCase() + ";\n" +
                    "@Service\n" +
                    "public class " + tableInfo.getNameHumpUpperCase() + "DaoImpl extends " + tableInfo.getDbNameHumpUpperCase() + "<" + tableInfo.getNameHumpUpperCase() + "> implements " + tableInfo.getNameHumpUpperCase() + "Dao{\n" +
                    "\n" +
                    "}";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao.impl", tableInfo.getNameHumpUpperCase() + "DaoImpl"), code, save);
        }
    }
}