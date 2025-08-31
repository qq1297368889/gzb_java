package gzb.frame.generate

import gzb.tools.Tools

class DBCode extends Base {
    public DBCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(String dbKey,String dbName, boolean save) {
        String code="package ${this.pkg}.${Tools.lowStr_hump(dbName)};\n" +
                "\n" +
                "import gzb.frame.db.BaseDaoImpl;\n" +
                "public class ${Tools.lowStr_d(Tools.lowStr_hump(dbName))}<T> extends BaseDaoImpl<T> {\n" +
                "    //数据库信息 在这里指定\n" +
                "    public ${Tools.lowStr_d(Tools.lowStr_hump(dbName))}() {\n" +
                "        try {\n" +
                "          init(\"${dbKey}\");\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException(e);\n" +
                "        }\n" +
                "    }\n" +
                "}";
        saveFile(getFilePath("${this.pkg}.${Tools.lowStr_hump(dbName)}",Tools.lowStr_d(Tools.lowStr_hump(dbName))),code,save)
    }

}
