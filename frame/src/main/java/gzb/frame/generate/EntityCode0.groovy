package gzb.frame.generate

import gzb.entity.TableInfo

class EntityCode0 extends Base {
    public EntityCode0(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        for (final def tableInfo in list) {
            String code = "";
            code = "package ${this.pkg}.${tableInfo.dbNameLowerCase}.entity;\n" +
                    "\n" +
                    "import gzb.entity.SqlTemplate;\n" +
                    "import gzb.tools.GzbMap;\n" +
                    "import gzb.tools.JSON;\n" +
                    "import gzb.tools.OnlyId;" +
                    "import java.io.Serializable;\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Map;\n" +
                    "import gzb.tools.Tools;\n" +
                    "import gzb.frame.annotation.EntityAttribute;\n" +
                    "\n" +
                    "@EntityAttribute(name=\"${tableInfo.name}\",desc=\"${tableInfo.nameHumpLowerCase}\")\n" +
                    "public class ${tableInfo.nameUpperCase} implements Serializable{\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "    @EntityAttribute(key=${tableInfo.columnNames.get(i).equals(tableInfo.id)},size = ${tableInfo.columnSize.get(i)}," +
                        "name=\"${tableInfo.columnNames.get(i)}\",desc=\"${tableInfo.columnNamesHumpLowerCase.get(i)}\")\n" +
                        "    private ${tableInfo.columnTypes.get(i)} ${tableInfo.columnNamesHumpLowerCase.get(i)};\n";
            }
            code += "    public ${tableInfo.nameUpperCase}() {}\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(JSON gzbMap) {\n" +
                    "        this(new GzbMap().setMap(gzbMap.map));\n" +
                    "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(GzbMap gzbMap) {\n" +
                    "String str=null;\n";

            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        str=gzbMap.getString(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\");\n" +
                        "        if (str!=null) {\n" +
                        "            set${tableInfo.columnNamesHumpUpperCase.get(i)}(${tableInfo.columnTypes.get(i)}.valueOf(str));\n" +
                        "        }\n";
            }
            code += "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(Map<String, Object> map) {\n" +
                    "        this(new GzbMap().setMap(map));\n" +
                    "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(String jsonString) {\n" +
                    "        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));\n" +
                    "    }\n" +
                    "\n" +
                    "    public SqlTemplate toSelectSql() {\n" +
                    "        return toSelectSql(\"${tableInfo.id}\", \"asc\", 0, false);\n" +
                    "    }\n" +
                    "\n" +
                    "    //查询语句 可选项 排序\n" +
                    "    public SqlTemplate toSelectSql(String sortField, String sortType, int size, boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"select * from ${tableInfo.name}\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(true,false, sb, list, selectId);\n" +
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
                    "    public SqlTemplate toSave(${tableInfo.idType} ${tableInfo.idHumpLowerCase}) {\n" +
                    "        if (${tableInfo.idHumpLowerCase} > 0L) {\n" +
                    "            set${tableInfo.idHumpUpperCase}(${tableInfo.idHumpLowerCase});\n" +
                    "        } else {\n" +
                    "            set${tableInfo.idHumpUpperCase}(OnlyId.getDistributed());\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"insert into ${tableInfo.name}(\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";

            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        if (get${tableInfo.columnNamesHumpUpperCase.get(i)}() != null && !get${tableInfo.columnNamesHumpUpperCase.get(i)}().toString().isEmpty()) {\n" +
                        "            sb.append(\"${tableInfo.columnNames.get(i)},\");\n" +
                        "            list.add(get${tableInfo.columnNamesHumpUpperCase.get(i)}());\n" +
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
                    "    public SqlTemplate toUpdate() {\n" +
                    "        if (get${tableInfo.idHumpUpperCase}() == null || get${tableInfo.idHumpUpperCase}().toString().isEmpty()) {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        StringBuilder sb = new StringBuilder(\"update ${tableInfo.name} set\");\n" +
                    "        List<Object> list = new ArrayList<>();\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                if (tableInfo.columnNames.get(i).equals(tableInfo.id)){
                    continue;
                }
                code += "        if (get${tableInfo.columnNamesHumpUpperCase.get(i)}() != null && !get${tableInfo.columnNamesHumpUpperCase.get(i)}().toString().isEmpty()) {\n" +
                        "            sb.append(\" ${tableInfo.columnNames.get(i)} = ?,\");\n" +
                        "            list.add(get${tableInfo.columnNamesHumpUpperCase.get(i)}());\n" +
                        "        }\n"
            }
            code += "        if (list.size() > 0) {\n" +
                    "            sb.delete(sb.length() - 1, sb.length());\n" +
                    "        } else {\n" +
                    "            return null;\n" +
                    "        }\n" +
                    "        sb.append(\" where ${tableInfo.id} = ?\");\n" +
                    "        list.add(get${tableInfo.idHumpUpperCase}());\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题\n" +
                    "    public SqlTemplate toDelete(boolean selectId) {\n" +
                    "        StringBuilder sb = new StringBuilder(\"delete from ${tableInfo.name}\");\n" +
                    "        List<Object> list = new ArrayList<>();\n" +
                    "        getSelectWhere(true,false, sb, list, selectId);\n" +
                    "        if (list.size() < 1) {\n" +
                    "            return null;//风险操作 会删除表\n" +
                    "        }\n" +
                    "        return new SqlTemplate(sb.toString(), list.toArray());\n" +
                    "    }\n" +
                    "\n" +
                    "    private void getSelectWhere(boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {\n" +
                    "        if (selectId){\n" +
                    "            if (appSql(\"${tableInfo.id}\", get${tableInfo.idHumpUpperCase}(), sb, list, where,and)) {\n" +
                    "                return;\n" +
                    "            }\n" +
                    "        }\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        if (appSql(\"${tableInfo.columnNames.get(i)}\", get${tableInfo.columnNamesHumpUpperCase.get(i)}(), sb, list, where,and)) {\n" +
                        "            where = false;\n" +
                        "            and=true;\n" +
                        "        }\n"
            }
            code += "    }\n" +
                    "\n" +
                    "    public boolean appSql(String key, Object val, StringBuilder stringBuilder, List<Object> list, boolean where, boolean and) {\n" +
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
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public String toString() {\n" +
                    "        return toJson().toString();\n" +
                    "    }\n" +
                    "\n" +
                    "    public JSON toJson() {\n" +
                    "        JSON json = new JSON();\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        json.put(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", get${tableInfo.columnNamesHumpUpperCase.get(i)}());\n";
            }
            code += "        return json;\n" +
                    "    }\n" +
                    "\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "    public ${tableInfo.columnTypes.get(i)} get${tableInfo.columnNamesHumpUpperCase.get(i)}() {\n" +
                        "        return ${tableInfo.columnNamesHumpLowerCase.get(i)};\n" +
                        "    }\n"+
                        "    public ${tableInfo.nameHumpUpperCase} set${tableInfo.columnNamesHumpUpperCase.get(i)}(${tableInfo.columnTypes.get(i)} ${tableInfo.columnNamesHumpLowerCase.get(i)}) {\n" +
                        "        int len = Tools.textLength(${tableInfo.columnNamesHumpLowerCase.get(i)});\n" +
                        "        if (len > ${tableInfo.columnSize.get(i)}) {\n" +
                        "            throw new RuntimeException(\"${tableInfo.nameHumpUpperCase}.${tableInfo.columnNamesHumpLowerCase.get(i)}最大长度为:${tableInfo.columnSize.get(i)},实际长度为:\"+(len)+\",数据为:\"+${tableInfo.columnNamesHumpLowerCase.get(i)});\n" +
                        "        }\n" +
                        "        this.${tableInfo.columnNamesHumpLowerCase.get(i)} = ${tableInfo.columnNamesHumpLowerCase.get(i)};\n" +
                        "        return this;\n" +
                        "    }\n";
            }
            code += "}\n" +

                    "" +
                    "\n";
            outCodeEntity(save, code, tableInfo.dbNameHumpLowerCase, tableInfo.nameHumpLowerCase);
        }

    }

}
