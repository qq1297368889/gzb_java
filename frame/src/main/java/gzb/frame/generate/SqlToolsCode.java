package gzb.frame.generate;

import gzb.entity.TableInfo;
import gzb.tools.Tools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.entity.SqlTemplate;
import gzb.tools.OnlyId;

public class SqlToolsCode extends Base {
    public SqlToolsCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public String start(List<TableInfo> list, boolean save) {
        String code = "";
        code = "package " + this.pkg + "." + list.get(0).getDbNameLowerCase() + ";\n" +
                "\n" +
                "import gzb.entity.SqlTemplate;\n";

        code += "import " + this.pkg + "." + list.get(0).getDbNameLowerCase() + ".entity.*;\n";
        code += "import gzb.tools.OnlyId;\n" +
                "\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class SqlTools {\n" +
                "    public static boolean appSql(String key, Object val, StringBuilder stringBuilder, List<Object> list, boolean where, boolean and) {\n" +
                "        if (val != null) {\n" +
                "            if (where) {\n" +
                "                stringBuilder.append(\" where\");\n" +
                "            }\n" +
                "            if (and){\n" +
                "                stringBuilder.append(\" and\");\n" +
                "            }\n" +
                "            stringBuilder.append(\" \").append(key).append(\" = ?\");\n" +
                "            list.add(val);\n" +
                "            return true;\n" +
                "        }\n" +
                "        return false;\n" +
                "    }\n";
        for (TableInfo tableInfo : list) {
            code += "    public static SqlTemplate toSelectSql(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") {\n" +
                    "        return toSelectSql(" + tableInfo.getNameHumpLowerCase() + ",\"" + tableInfo.getId() + "\", \"asc\", 0, false);\n" +
                    "    }\n" +
                    "    /**\n" +
                    "     * 实体类[" + tableInfo.getNameHumpUpperCase() + "]的sql生成\n" +
                    "     * */\n" +
                    "    //查询语句 可选项 排序\n" +
                    "    public static SqlTemplate toSelectSql(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ",String sortField, String sortType, int size, boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"select * from " + tableInfo.getName() + "\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(" + tableInfo.getNameHumpLowerCase() + ",true,false, sb, list, selectId);\n" +
                    "        if (sortField != null && sortType != null) {\n" +
                    "            sb.append(\" order by\");\n" +
                    "            sb.append(\" \");\n" +
                    "            sb.append(sortField);\n" +
                    "            sb.append(\" \");\n" +
                    "            sb.append(sortType);\n" +
                    "        }\n" +
                    "        if (size > 0) {\n" +
                    "            sb.append(\" limit ?,?\");\n" +
                    "            list.add(0);\n" +
                    "            list.add(size);\n" +
                    "        }\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    //插入 可以指定id  不指定自动生成\n" +
                    "    public static SqlTemplate toSave(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "()==null || " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "() < 1L) {\n" +
                    "            " + tableInfo.getNameHumpLowerCase() + ".set" + tableInfo.getIdHumpUpperCase() + "(OnlyId.getDistributed());\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"insert into " + tableInfo.getName() + "(\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";

            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "        if (" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "() != null && !" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "().toString().isEmpty()) {\n" +
                        "            sb.append(\"" + tableInfo.getColumnNames().get(i) + ",\");\n" +
                        "            list.add(" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "());\n" +
                        "        }\n";
            }
            code += "        if (list.size() > 0) {\n" +
                    "            sb.delete(sb.length() - 1, sb.length());\n" +
                    "        } else {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        sb.append(\") values (\");\n" +
                    "        for (int i = 0; i < list.size(); i++) {\n" +
                    "            sb.append(\"?\");\n" +
                    "            if (i < (list.size() - 1)) {\n" +
                    "                sb.append(\",\");\n" +
                    "            }\n" +
                    "\n" +
                    "        }\n" +
                    "        sb.append(\")\");\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    //根据id修改 高级需求请手动写sql\n" +
                    "    public static SqlTemplate toUpdate(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "() == null || " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "().toString().isEmpty()) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"update " + tableInfo.getName() + " set\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                if (tableInfo.getColumnNames().get(i).equals(tableInfo.getId())) {
                    continue;
                }
                code += "        if (" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "() != null && !" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "().toString().isEmpty()) {\n" +
                        "            sb.append(\" " + tableInfo.getColumnNames().get(i) + " = ?,\");\n" +
                        "            list.add(" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "());\n" +
                        "        }\n";
            }
            code += "        if (list.size() > 0) {\n" +
                    "            sb.delete(sb.length() - 1, sb.length());\n" +
                    "        } else {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        sb.append(\" where " + tableInfo.getId() + " = ?\");\n" +
                    "        list.add(" + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "());\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题\n" +
                    "    public static SqlTemplate toDelete(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ",boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"delete from " + tableInfo.getName() + "\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(" + tableInfo.getNameHumpLowerCase() + ",true,false, sb, list, selectId);\n" +
                    "        if (list.size() < 1) {\n" +
                    "            return null;//风险操作 会删除表\n" +
                    "        }\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    public static void getSelectWhere(" + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ",boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {\n" +
                    "        if (selectId){\n" +
                    "            if (appSql(\"" + tableInfo.getId() + "\", " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "(), sb, list, where,and)) {\n" +
                    "                return;\n" +
                    "            }\n" +
                    "        }\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "        if (appSql(\"" + tableInfo.getColumnNames().get(i) + "\", " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "(), sb, list, where,and)) {\n" +
                        "            where = false;\n" +
                        "            and=true;\n" +
                        "        }\n";
            }
            code += "    }\n" +
                    "    public static " + tableInfo.getNameHumpUpperCase() + " as" + tableInfo.getNameHumpUpperCase() + "(Map<String,String[]> requestData){\n" +
                    "        List<" + tableInfo.getNameHumpUpperCase() + "> list=asList" + tableInfo.getNameHumpUpperCase() + "(requestData,1);\n" +
                    "        if (list.isEmpty()) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        return list.get(0);\n" +
                    "    }\n" +
                    "    public static List<" + tableInfo.getNameHumpUpperCase() + "> asList" + tableInfo.getNameHumpUpperCase() + "(Map<String,String[]> requestData,Integer max){\n" +
                    "        int size=0;\n";

            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "        String[] " + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array = requestData.get(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\");\n" +
                        "        if (" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array!=null && " + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array.length>0) {\n" +
                        "            if (size<" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array.length) {\n" +
                        "                size=" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array.length;\n" +
                        "            }\n" +
                        "        }\n";
            }
            code +=
                    "        if (max!=null) {\n" +
                            "            if (size>max) {\n" +
                            "                size=max;\n" +
                            "            }\n" +
                            "        }\n" +
                            "        List<" + tableInfo.getNameHumpUpperCase() + "> list=new ArrayList<>(size);\n" +
                            "        for (int i = 0; i < size; i++) {\n" +
                            "            " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + "=new " + tableInfo.getNameHumpUpperCase() + "();\n";

            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "            if (" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array!=null && i <= " + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array.length-1) {\n" +
                        "                " + tableInfo.getNameHumpLowerCase() + ".set" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "(" + tableInfo.getColumnTypes().get(i) + ".valueOf(" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "Array[i]));\n" +
                        "            }\n";
            }
            code += "            list.add(" + tableInfo.getNameHumpLowerCase() + ");\n" +
                    "        }\n" +
                    "        return list;\n" +
                    "    }" +
                    "\n";
        }
        code += "\n" +
                "}\n";
        saveFile(getFilePath(this.pkg + "." + list.get(0).getDbNameLowerCase(), Tools.lowStr_d(Tools.lowStr_d("SqlTools"))), code, save);

        return code;
    }
}