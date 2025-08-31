package gzb.frame.generate

import gzb.entity.TableInfo

class DaoCode extends Base {
    public DaoCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code=null;
        for (final def tableInfo in list) {
            code="package ${this.pkg}.${tableInfo.dbNameLowerCase}.dao;\n" +
                    "\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.entity.${tableInfo.nameHumpUpperCase};\n" +
                    "import gzb.frame.db.BaseDao;\n" +
                    "\n" +
                    "public interface ${tableInfo.nameHumpUpperCase}Dao extends BaseDao<${tableInfo.nameHumpUpperCase}> {\n" +
                    "}\n";
            saveFile(getFilePath("${this.pkg}.${tableInfo.dbNameLowerCase}.dao","${tableInfo.nameHumpUpperCase}Dao"),code,save);
        }

    }


}
