package gzb.frame.generate

import gzb.entity.TableInfo
import gzb.tools.Tools;

public class SqlToolsCode extends Base {
    public SqlToolsCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code = "";
        code="package ${this.pkg}.${list.get(0).dbNameLowerCase};\n" +
                "\n" +
                "import gzb.entity.SqlTemplate;\n";

        code+="import ${this.pkg}.${list.get(0).dbNameLowerCase}.entity.*;\n";
        code+= "import gzb.tools.OnlyId;\n" +
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
            code += "    public static SqlTemplate toSelectSql(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) {\n" +
                    "        return toSelectSql(${tableInfo.nameHumpLowerCase},\"${tableInfo.id}\", \"asc\", 0, false);\n" +
                    "    }\n" +
                    "    /**\n" +
                    "     * 实体类[${tableInfo.nameHumpUpperCase}]的sql生成\n" +
                    "     * */\n" +
                    "    //查询语句 可选项 排序\n" +
                    "    public static SqlTemplate toSelectSql(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase},String sortField, String sortType, int size, boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"select * from ${tableInfo.name}\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(${tableInfo.nameHumpLowerCase},true,false, sb, list, selectId);\n" +
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
                    "    public static SqlTemplate toSave(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) {\n" +
                    "        if (${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}()==null || ${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}() < 1L) {\n" +
                    "            ${tableInfo.nameHumpLowerCase}.set${tableInfo.idHumpUpperCase}(OnlyId.getDistributed());\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"insert into ${tableInfo.name}(\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";

            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        if (${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}() != null && !${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}().toString().isEmpty()) {\n" +
                        "            sb.append(\"${tableInfo.columnNames.get(i)},\");\n" +
                        "            list.add(${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}());\n" +
                        "        }\n"
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
                    "    public static SqlTemplate toUpdate(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) {\n" +
                    "        if (${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}() == null || ${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}().toString().isEmpty()) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"update ${tableInfo.name} set\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                if (tableInfo.columnNames.get(i).equals(tableInfo.id)) {
                    continue;
                }
                code += "        if (${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}() != null && !${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}().toString().isEmpty()) {\n" +
                        "            sb.append(\" ${tableInfo.columnNames.get(i)} = ?,\");\n" +
                        "            list.add(${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}());\n" +
                        "        }\n"
            }
            code += "        if (list.size() > 0) {\n" +
                    "            sb.delete(sb.length() - 1, sb.length());\n" +
                    "        } else {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        sb.append(\" where ${tableInfo.id} = ?\");\n" +
                    "        list.add(${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}());\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题\n" +
                    "    public static SqlTemplate toDelete(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase},boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"delete from ${tableInfo.name}\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(${tableInfo.nameHumpLowerCase},true,false, sb, list, selectId);\n" +
                    "        if (list.size() < 1) {\n" +
                    "            return null;//风险操作 会删除表\n" +
                    "        }\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    public static void getSelectWhere(${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase},boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {\n" +
                    "        if (selectId){\n" +
                    "            if (appSql(\"${tableInfo.id}\", ${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}(), sb, list, where,and)) {\n" +
                    "                return;\n" +
                    "            }\n" +
                    "        }\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        if (appSql(\"${tableInfo.columnNames.get(i)}\", ${tableInfo.nameHumpLowerCase}.get${tableInfo.columnNamesHumpUpperCase.get(i)}(), sb, list, where,and)) {\n" +
                        "            where = false;\n" +
                        "            and=true;\n" +
                        "        }\n"
            }
            code += "    }\n" +
                    "    public static ${tableInfo.nameHumpUpperCase} as${tableInfo.nameHumpUpperCase}(Map<String,String[]> requestData){\n" +
                    "        List<${tableInfo.nameHumpUpperCase}> list=asList${tableInfo.nameHumpUpperCase}(requestData,1);\n" +
                    "        if (list.isEmpty()) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        return list.get(0);\n" +
                    "    }\n" +
                    "    public static List<${tableInfo.nameHumpUpperCase}> asList${tableInfo.nameHumpUpperCase}(Map<String,String[]> requestData,Integer max){\n" +
                    "        int size=0;\n";

            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code +="        String[] ${tableInfo.columnNamesHumpLowerCase.get(i)}Array = requestData.get(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\");\n" +
                        "        if (${tableInfo.columnNamesHumpLowerCase.get(i)}Array!=null && ${tableInfo.columnNamesHumpLowerCase.get(i)}Array.length>0) {\n" +
                        "            if (size<${tableInfo.columnNamesHumpLowerCase.get(i)}Array.length) {\n" +
                        "                size=${tableInfo.columnNamesHumpLowerCase.get(i)}Array.length;\n" +
                        "            }\n" +
                        "        }\n";
            }
            code +=
                    "        if (max!=null) {\n" +
                    "            if (size>max) {\n" +
                    "                size=max;\n" +
                    "            }\n" +
                    "        }\n" +
                    "        List<${tableInfo.nameHumpUpperCase}> list=new ArrayList<>(size);\n" +
                    "        for (int i = 0; i < size; i++) {\n" +
                    "            ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}=new ${tableInfo.nameHumpUpperCase}();\n";

            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code +="            if (${tableInfo.columnNamesHumpLowerCase.get(i)}Array!=null && i <= ${tableInfo.columnNamesHumpLowerCase.get(i)}Array.length-1) {\n" +
                        "                ${tableInfo.nameHumpLowerCase}.set${tableInfo.columnNamesHumpUpperCase.get(i)}(${tableInfo.columnTypes.get(i)}.valueOf(${tableInfo.columnNamesHumpLowerCase.get(i)}Array[i]));\n" +
                        "            }\n";
            }
            code +="            list.add(${tableInfo.nameHumpLowerCase});\n" +
                    "        }\n" +
                    "        return list;\n" +
                    "    }" +
                    "\n";
        }
        code += "\n" +
                "}\n";
        saveFile(getFilePath("${this.pkg}.${list.get(0).dbNameLowerCase}", Tools.lowStr_d(Tools.lowStr_d("SqlTools"))), code, save)
    }
}

