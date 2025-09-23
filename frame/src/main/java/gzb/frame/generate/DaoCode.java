package gzb.frame.generate;

import gzb.entity.TableInfo;

import java.util.List;

public class DaoCode extends Base {
    public DaoCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao;\n" +
                    "\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".entity." + tableInfo.getNameHumpUpperCase() + ";\n" +
                    "import gzb.frame.db.BaseDao;\n" +
                    "\n" +
                    "public interface " + tableInfo.getNameHumpUpperCase() + "Dao extends BaseDao<" + tableInfo.getNameHumpUpperCase() + "> {\n" +
                    "}\n";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao", tableInfo.getNameHumpUpperCase() + "Dao"), code, save);
        }
    }
}