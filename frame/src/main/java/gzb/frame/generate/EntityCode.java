/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.frame.generate;

import gzb.entity.TableInfo;

import java.io.IOException;
import java.util.List;

public class EntityCode extends Base {
    public EntityCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) throws IOException {
        for (final TableInfo tableInfo : list) {
            String code;
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".entity;\n" +
    /*                "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".SqlTools;\n" +
                    "import gzb.entity.SqlTemplate;\n" +
                    "import gzb.frame.factory.ClassTools;\n" +*/
                    "import gzb.tools.*;\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao." + tableInfo.getNameUpperCase() + "Dao;\n" +
                    "import java.io.Serializable;\n" +
                    "import java.sql.ResultSet;\n" +
                    "import java.sql.SQLException;\n" +
                    "import java.util.*;\n" +
                    "import gzb.frame.annotation.EntityAttribute;\n" +
                    "import gzb.tools.json.JsonSerializable;\n" +
                    "import gzb.tools.json.Result;\n" +
                    "import gzb.tools.json.ResultImpl;\n" +
                    "@EntityAttribute(name=\"" + tableInfo.getName() + "\",desc=\"" + tableInfo.getNameHumpLowerCase() + "\")\n" +
                    "public class " + tableInfo.getNameUpperCase() + " implements Serializable, JsonSerializable{\n" +
                    "    private static final long serialVersionUID = 1000L;\n" +
                    "    private static final String dataName= Config.get(\"json.entity.data\",\"data\");\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "    @EntityAttribute(key=" + tableInfo.getColumnNames().get(i).equals(tableInfo.getId()) + ",size = " + tableInfo.getColumnSize().get(i) + "," +
                        "name=\"" + tableInfo.getColumnNames().get(i) + "\",desc=\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\")\n" +
                        "    private " + tableInfo.getColumnTypes().get(i) + " " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n";
            }
            code += "    private Object data;\n" +
                    "   public " + tableInfo.getNameUpperCase() + "() {}\n" +
                    "\n" +
                    "    public " + tableInfo.getNameUpperCase() + "(GzbMap gzbMap) {\n" +
                    "        this(gzbMap.map);\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + tableInfo.getNameUpperCase() + "(Map<String, Object> map) {\n" +
                    "        Result result = new ResultImpl(map);\n" +
                    "        loadJson(result);\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + tableInfo.getNameUpperCase() + "(String jsonString) {\n" +
                    "        Result result = new ResultImpl(jsonString);\n" +
                    "        loadJson(result);\n" +
                    "    }\n" +
                    "\n" +
                    "    public " + tableInfo.getNameUpperCase() + "(ResultSet resultSet, Set<String> names) throws SQLException {\n" +
                    "        loadResultSet(resultSet,names);\n" +
                    "    }\n" +
                    "    public int save(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.save(this);\n" +
                    "    }\n" +
                    "    public int delete(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.delete(this);\n" +
                    "    }\n" +
                    "    public int update(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.update(this);\n" +
                    "    }\n" +
                    "    public int saveAsync(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.saveAsync(this);\n" +
                    "    }\n" +
                    "    public int deleteAsync(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.deleteAsync(this);\n" +
                    "    }\n" +
                    "    public int updateAsync(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.updateAsync(this);\n" +
                    "    }\n" +
                    "    public List<" + tableInfo.getNameUpperCase() + "> query(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.query(this);\n" +
                    "    }\n" +
                    "    public List<" + tableInfo.getNameUpperCase() + "> query(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao,int page,int size) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.query(this,page,size);\n" +
                    "    }\n" +
                    "    public List<" + tableInfo.getNameUpperCase() + "> query(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao,String sortField,String sortType,int page,int size) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.query(this,sortField,sortType,page,size);\n" +
                    "    }\n" +
                    "    public JSONResult queryPage(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao, String sortField, String sortType, int page, int size) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.queryPage(this,sortField,sortType,page,size,100,100);\n" +
                    "    }\n" +
                    "    public JSONResult queryPage(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);\n" +
                    "    }\n" +
                    "    public " + tableInfo.getNameUpperCase() + " find(" + tableInfo.getNameUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.find(this);\n" +
                    "    }\n" +
                   /* "    public SqlTemplate toSelectSql() {\n" +
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
                    "\n" +*/
                    "    @Override\n" +
                    "    public String toString() {\n" +
                    "        StringBuilder sb = new StringBuilder(\"{\");\n" ;
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                //统一标准为 string  不再区分其他类型 因为遇到过各种实现差异导致的问题 比如 1意外转为1.0  长整数被科学计数法表示 等
        /*        if (tableInfo.getColumnTypes().get(i).contains("Boolean") ||tableInfo.getColumnTypes().get(i).contains("boolean") ||
                        tableInfo.getColumnTypes().get(i).contains("Byte") ||tableInfo.getColumnTypes().get(i).contains("byte") ||
                        tableInfo.getColumnTypes().get(i).contains("Short") ||tableInfo.getColumnTypes().get(i).contains("short") ||
                        tableInfo.getColumnTypes().get(i).contains("Integer") ||tableInfo.getColumnTypes().get(i).contains("int") ||
                        tableInfo.getColumnTypes().get(i).contains("Long") ||tableInfo.getColumnTypes().get(i).contains("long") ||
                        tableInfo.getColumnTypes().get(i).contains("Float") ||tableInfo.getColumnTypes().get(i).contains("float") ||
                        tableInfo.getColumnTypes().get(i).contains("Double") ||tableInfo.getColumnTypes().get(i).contains("double")) {
                    code +="        if (this."+tableInfo.getColumnNamesHumpLowerCase().get(i)+" != null) {\n" +
                            "            sb.append(\"\\\""+tableInfo.getColumnNamesHumpLowerCase().get(i)+"\\\":\").append("+tableInfo.getColumnNamesHumpLowerCase().get(i)+").append(\",\");\n" +
                            "        }\n";
                }else{
                    code +="        if (this."+tableInfo.getColumnNamesHumpLowerCase().get(i)+" != null) {\n" +
                            "            sb.append(\"\\\""+tableInfo.getColumnNamesHumpLowerCase().get(i)+"\\\":\\\"\").append("+tableInfo.getColumnNamesHumpLowerCase().get(i)+").append(\"\\\",\");\n" +
                            "        }\n";
                }
*/
                code +="        if (this."+tableInfo.getColumnNamesHumpLowerCase().get(i)+" != null) {\n" +
                        "            sb.append(\"\\\""+tableInfo.getColumnNamesHumpLowerCase().get(i)+"\\\":\\\"\")" +
                        ".append("+tableInfo.getColumnNamesHumpLowerCase().get(i)+").append(\"\\\",\");\n" +
                        "        }\n";


            }

            code +="" +
                    "        if (this.data instanceof Map) {\n" +
                    "            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {\n" +
                    "                sb.append(\"\\\"\").append(entry.getKey()).append(\"\\\":\").append(Tools.toJson(entry.getValue())).append(\",\");\n" +
                    "            }\n" +
                    "        }else if(this.data != null){\n" +
                    "            sb.append(\"\\\"\").append(dataName).append(\"\\\":\").append(Tools.toJson(this.data)).append(\",\");\n" +
                    "        }\n";


            code += "        if (sb.length()>1) {\n" +
                    "            sb.delete(sb.length()-1, sb.length());\n" +
                    "        }\n";
            code += "       return sb.append(\"}\").toString();\n" +
                    "    }\n" +
                    "\n" +
                    "    public Result toJson() {\n" +
                    "        Result result=new ResultImpl();\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code += "        result.set(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ");\n";
            }
            code += "        result.set(dataName, data);\n" +
                    "        return result;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    public void loadJson(String json) {\n" +
                    "        Result result=new ResultImpl(json);\n" +
                    "        loadJson(result);\n" +
                    "    }\n" +
                    "    public void loadJson(Result result) {\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                if (tableInfo.getColumnTypes().get(i).endsWith("Long")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getLong(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("Integer")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getInteger(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("Short")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getShort(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("String")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getString(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("Double")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getDouble(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("Boolean")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getBoolean(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else if (tableInfo.getColumnTypes().get(i).endsWith("Float")) {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getFloat(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                } else {
                    code += "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=result.getObject(\"" + tableInfo.getColumnNamesHumpLowerCase().get(i) + "\", null);\n";
                }

            }
            code += "        Object obj = result.get(dataName,null);\n" +
                    "        if (obj instanceof Map) {\n" +
                    "            this.data = (Map<String, Object>) obj;\n" +
                    "        }\n" +
                    "    }\n" +
                    "    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {\n" +
                    "        String temp=null;\n";
            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                code +="        if (names.contains(\""+ tableInfo.getColumnNames().get(i) +"\")) {\n" +
                        "            temp=resultSet.getString(\"" + tableInfo.getColumnNames().get(i) + "\");\n" +
                        "            if (temp!=null) {\n" +
                        "                this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "=" + tableInfo.getColumnTypes().get(i) + ".valueOf(temp);\n" +
                        "            }\n" +
                        "        }\n";
            }

            code += "    }\n";


            for (int i = 0; i < tableInfo.getColumnNames().size(); i++) {
                if (tableInfo.getColumnTypes().get(i).contains("Boolean") ||tableInfo.getColumnTypes().get(i).contains("boolean") ||
                        tableInfo.getColumnTypes().get(i).contains("Byte") ||tableInfo.getColumnTypes().get(i).contains("byte") ||
                        tableInfo.getColumnTypes().get(i).contains("Short") ||tableInfo.getColumnTypes().get(i).contains("short") ||
                        tableInfo.getColumnTypes().get(i).contains("Integer") ||tableInfo.getColumnTypes().get(i).contains("int") ||
                        tableInfo.getColumnTypes().get(i).contains("Long") ||tableInfo.getColumnTypes().get(i).contains("long") ||
                        tableInfo.getColumnTypes().get(i).contains("Float") ||tableInfo.getColumnTypes().get(i).contains("float") ||
                        tableInfo.getColumnTypes().get(i).contains("Double") ||tableInfo.getColumnTypes().get(i).contains("double")) {
                    code += "    public " + tableInfo.getColumnTypes().get(i) + " get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "() {\n" +
                            "        return " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n" +
                            "    }\n" +
                            "    public " + tableInfo.getNameHumpUpperCase() + " set" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "(" + tableInfo.getColumnTypes().get(i) + " " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ") {\n" +
                            "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + " = " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n" +
                            "        return this;\n" +
                            "    }\n";
                }else{
                    code += "    public " + tableInfo.getColumnTypes().get(i) + " get" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "() {\n" +
                            "        return " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n" +
                            "    }\n" +
                            "    public " + tableInfo.getNameHumpUpperCase() + " set" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "(" + tableInfo.getColumnTypes().get(i) + " " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ") {\n" +
                            "        int size0 = Tools.textLength(" + tableInfo.getColumnNamesHumpLowerCase().get(i) + ");\n" +
                            "        if (size0 > " + tableInfo.getColumnSize().get(i) + ") {\n" +
                            "            throw new RuntimeException(\"" + tableInfo.getNameHumpUpperCase() + "." + tableInfo.getColumnNamesHumpLowerCase().get(i) + "最大长度为:" + tableInfo.getColumnSize().get(i) + ",实际长度为:\"+ size0 +\",数据为:\"+" + tableInfo.getColumnNamesHumpLowerCase().get(i) + ");\n" +
                            "        }\n" +
                            "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + " = " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n" +
                            "        return this;\n" +
                            "    }\n" +
                            "    public " + tableInfo.getNameHumpUpperCase() + " set" + tableInfo.getColumnNamesHumpUpperCase().get(i) + "Unsafe(" + tableInfo.getColumnTypes().get(i) + " " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ") {\n" +
                            "        this." + tableInfo.getColumnNamesHumpLowerCase().get(i) + " = " + tableInfo.getColumnNamesHumpLowerCase().get(i) + ";\n" +
                            "        return this;\n" +
                            "    }\n";
                }

            }
            code += "    public List<?> getList() {\n" +
                    "        return (List<?>) data;\n" +
                    "    }\n" +
                    "\n" +
                    "    public "+ tableInfo.getNameHumpUpperCase() +" setList(List<?> data) {\n" +
                    "        this.data = data;\n" +
                    "        return this;\n" +
                    "    }\n" +
                    "\n" +
                    "    public Map<String, Object> getMap() {\n" +
                    "        return (Map<String, Object>) data;\n" +
                    "    }\n" +
                    "\n" +
                    "    public "+ tableInfo.getNameHumpUpperCase() +" setMap(Map<String, Object> data) {\n" +
                    "        this.data = data;\n" +
                    "        return this;\n" +
                    "    }\n" +
                    "\n" +
                    "    public "+ tableInfo.getNameHumpUpperCase() +" putMap(String key, Object value) {\n" +
                    "        if (this.data == null) {\n" +
                    "            this.data = new HashMap<>();\n" +
                    "        }\n" +
                    "        ((Map<String, Object>)this.data).put(key, value);\n" +
                    "        return this;\n" +
                    "    }\n" +
                    "\n" +
                    "    public Object getData() {\n" +
                    "        return data;\n" +
                    "    }\n" +
                    "\n" +
                    "    public "+ tableInfo.getNameHumpUpperCase() +" setData(Object data) {\n" +
                    "        this.data = data;\n" +
                    "        return this;\n" +
                    "    }" +
                    "}\n";
            outCodeEntity(save, code, tableInfo.getDbNameHumpLowerCase(), tableInfo.getNameHumpLowerCase());
        }

    }

}