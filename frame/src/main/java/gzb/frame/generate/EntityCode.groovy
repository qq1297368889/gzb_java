package gzb.frame.generate

import gzb.entity.TableInfo

class EntityCode extends Base {
    public EntityCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        for (final def tableInfo in list) {
            String code = "";
            code = "package ${this.pkg}.${tableInfo.dbNameLowerCase}.entity;\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.SqlTools;\n" +
                    "import gzb.entity.SqlTemplate;\n" +
                    "import gzb.tools.*;\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.dao.${tableInfo.nameUpperCase}Dao;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.sql.ResultSet;\n" +
                    "import java.sql.SQLException;\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.List;\n" +
                    "import java.util.Map;\n" +
                    "import gzb.frame.annotation.EntityAttribute;\n" +
                    "import gzb.tools.json.JsonSerializable;\n" +
                    "import gzb.tools.json.Result;\n" +
                    "import gzb.tools.json.ResultImpl;\n" +
                    "@EntityAttribute(name=\"${tableInfo.name}\",desc=\"${tableInfo.nameHumpLowerCase}\")\n" +
                    "public class ${tableInfo.nameUpperCase} implements Serializable, JsonSerializable{\n" +
                    "    private static final long serialVersionUID = 1000L;\n" +
                    "    private static final String dataName= Config.get(\"json.entity.data\",\"data\");\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "    @EntityAttribute(key=${tableInfo.columnNames.get(i).equals(tableInfo.id)},size = ${tableInfo.columnSize.get(i)}," +
                        "name=\"${tableInfo.columnNames.get(i)}\",desc=\"${tableInfo.columnNamesHumpLowerCase.get(i)}\")\n" +
                        "    private ${tableInfo.columnTypes.get(i)} ${tableInfo.columnNamesHumpLowerCase.get(i)};\n";
            }
            code += "    private List<?> list;\n" +
                    "   public ${tableInfo.nameUpperCase}() {}\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(GzbMap gzbMap) {\n" +
                    "        this(gzbMap.map);\n" +
                    "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(Map<String, Object> map) {\n" +
                    "        Result result = new ResultImpl(map);\n" +
                    "        loadJson(result);\n" +
                    "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(String jsonString) {\n" +
                    "        Result result = new ResultImpl(jsonString);\n" +
                    "        loadJson(result);\n" +
                    "    }\n" +
                    "\n" +
                    "    public ${tableInfo.nameUpperCase}(ResultSet resultSet) throws SQLException {\n" +
                    "        loadJson(resultSet);\n" +
                    "    }\n" +
                    "    public int save(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.save(this);\n" +
                    "    }\n" +
                    "    public int delete(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.delete(this);\n" +
                    "    }\n" +
                    "    public int update(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.update(this);\n" +
                    "    }\n" +
                    "    public int saveAsync(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.saveAsync(this);\n" +
                    "    }\n" +
                    "    public int deleteAsync(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.deleteAsync(this);\n" +
                    "    }\n" +
                    "    public int updateAsync(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.updateAsync(this);\n" +
                    "    }\n" +
                    "    public List<${tableInfo.nameUpperCase}> query(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.query(this);\n" +
                    "    }\n" +
                    "    public List<${tableInfo.nameUpperCase}> query(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao,int page,int size) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.query(this,page,size);\n" +
                    "    }\n" +
                    "    public List<${tableInfo.nameUpperCase}> query(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao,String sortField,String sortType,int page,int size) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.query(this,sortField,sortType,page,size);\n" +
                    "    }\n" +
                    "    public JSONResult queryPage(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao, String sortField, String sortType, int page, int size) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.queryPage(this,sortField,sortType,page,size,100,100);\n" +
                    "    }\n" +
                    "    public JSONResult queryPage(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);\n" +
                    "    }\n" +
                    "    public ${tableInfo.nameUpperCase} find(${tableInfo.nameUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.find(this);\n" +
                    "    }\n" +
                    "    public SqlTemplate toSelectSql() {\n" +
                    "        return SqlTools.toSelectSql(this,\"act_code_id\", \"asc\", 0, false);\n" +
                    "    }\n" +
                    "\n" +
                    "    //查询语句 可选项 排序\n" +
                    "    public SqlTemplate toSelectSql(String sortField, String sortType, Integer size, Boolean selectId) {\n" +
                    "        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);\n" +
                    "    }\n" +
                    "\n" +
                    "    //插入 可以指定id  不指定自动生成\n" +
                    "    public SqlTemplate toSave() {\n" +
                    "        return SqlTools.toSave(this);\n" +
                    "    }\n" +
                    "\n" +
                    "    //根据id修改 高级需求请手动写sql\n" +
                    "    public SqlTemplate toUpdate() {\n" +
                    "        return SqlTools.toUpdate(this);\n" +
                    "    }\n" +
                    "\n" +
                    "    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题\n" +
                    "    public SqlTemplate toDelete(Boolean selectId) {\n" +
                    "        return SqlTools.toDelete(this,selectId);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public String toString() {\n" +
                    "        return toJson().toString();\n" +
                    "    }\n" +
                    "\n" +
                    "    public Result toJson() {\n" +
                    "        Result result=new ResultImpl();\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "        result.set(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", ${tableInfo.columnNamesHumpLowerCase.get(i)});\n";
            }
            code += "        result.set(dataName, list);\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void loadJson(String json) {\n" +
                    "        Result result=new ResultImpl(json);\n" +
                    "         loadJson(result);\n" +
                    "    }\n" +
                    "    public void loadJson(Result result) {\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                if (tableInfo.columnTypes.get(i).endsWith("Long")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getLong(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("Integer")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getInteger(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("Short")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getShort(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("String")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getString(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("Double")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getDouble(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("Boolean")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getBoolean(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else if (tableInfo.columnTypes.get(i).endsWith("Float")) {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getFloat(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                } else {
                    code += "        this.${tableInfo.columnNamesHumpLowerCase.get(i)}=result.getObject(\"${tableInfo.columnNamesHumpLowerCase.get(i)}\", null);\n";
                }

            }
            code += "        Object obj = result.get(dataName,null);\n" +
                    "        if (obj instanceof List) {\n" +
                    "            this.list=(List<?>)obj;\n" +
                    "        }\n" +
                    "    }\n" +
                    "    public void loadJson(ResultSet resultSet) throws SQLException {\n" +
                    "        //ResultSetMetaData rsMetaData = resultSet.getMetaData();\n" +
                    "        String temp=null;\n" +
                    "        while (resultSet.next()) {\n";
            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "            temp=resultSet.getString(\"" + tableInfo.columnNames[i] + "\");\n" +
                        "            if (temp!=null) {\n" +
                        "                this." + tableInfo.columnNamesHumpLowerCase[i] + "=" + tableInfo.columnTypes[i] + ".valueOf(temp);\n" +
                        "            }\n";
            }

            code += "        }\n" +
                    "    }\n";


            for (int i = 0; i < tableInfo.columnNames.size(); i++) {
                code += "    public ${tableInfo.columnTypes.get(i)} get${tableInfo.columnNamesHumpUpperCase.get(i)}() {\n" +
                        "        return ${tableInfo.columnNamesHumpLowerCase.get(i)};\n" +
                        "    }\n" +
                        "    public ${tableInfo.nameHumpUpperCase} set${tableInfo.columnNamesHumpUpperCase.get(i)}(${tableInfo.columnTypes.get(i)} ${tableInfo.columnNamesHumpLowerCase.get(i)}) {\n" +
                        "        int size0 = Tools.textLength(${tableInfo.columnNamesHumpLowerCase.get(i)});\n" +
                        "        if (size0 > ${tableInfo.columnSize.get(i)}) {\n" +
                        "            throw new RuntimeException(\"${tableInfo.nameHumpUpperCase}.${tableInfo.columnNamesHumpLowerCase.get(i)}最大长度为:${tableInfo.columnSize.get(i)},实际长度为:\"+ size0 +\",数据为:\"+${tableInfo.columnNamesHumpLowerCase.get(i)});\n" +
                        "        }\n" +
                        "        this.${tableInfo.columnNamesHumpLowerCase.get(i)} = ${tableInfo.columnNamesHumpLowerCase.get(i)};\n" +
                        "        return this;\n" +
                        "    }\n" +
                        "    public ${tableInfo.nameHumpUpperCase} set${tableInfo.columnNamesHumpUpperCase.get(i)}Unsafe(${tableInfo.columnTypes.get(i)} ${tableInfo.columnNamesHumpLowerCase.get(i)}) {\n" +
                        "        this.${tableInfo.columnNamesHumpLowerCase.get(i)} = ${tableInfo.columnNamesHumpLowerCase.get(i)};\n" +
                        "        return this;\n" +
                        "    }\n";
            }
            code += "    public List<?> getList() {\n" +
                    "        return list;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setList(List<?> list) {\n" +
                    "        this.list = list;\n" +
                    "    }\n" +
                    "}\n";
            outCodeEntity(save, code, tableInfo.dbNameHumpLowerCase, tableInfo.nameHumpLowerCase);
        }

    }

}
