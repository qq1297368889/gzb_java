package gzb.frame.generate

import gzb.entity.TableInfo

class DaoImplCode extends Base {
    public DaoImplCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }
    public void start(List<TableInfo> list, boolean save) {
        String code=null;
        for (final def tableInfo in list) {
            code="package ${this.pkg}.${tableInfo.dbNameLowerCase}.dao.impl;\n" +
                    "import gzb.frame.annotation.Service;\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.dao.${tableInfo.nameHumpUpperCase}Dao;\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.entity.${tableInfo.nameHumpUpperCase};\n" +
                    "import ${this.pkg}.${tableInfo.dbNameHumpLowerCase}.${tableInfo.dbNameHumpUpperCase};\n" +
                    "@Service\n" +
                    "public class ${tableInfo.nameHumpUpperCase}DaoImpl extends ${tableInfo.dbNameHumpUpperCase}<${tableInfo.nameHumpUpperCase}> implements ${tableInfo.nameHumpUpperCase}Dao{\n" +
                    "\n" +
                    "}";
            saveFile(getFilePath("${this.pkg}.${tableInfo.dbNameLowerCase}.dao.impl","${tableInfo.nameHumpUpperCase}DaoImpl"),code,save);
        }

    }


}