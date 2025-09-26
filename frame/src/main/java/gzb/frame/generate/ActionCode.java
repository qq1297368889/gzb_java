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
import gzb.tools.Config;

import java.util.List;

public class ActionCode extends Base {
    public ActionCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code;
        for (final TableInfo tableInfo : list) {
            if (tableInfo.getName().equals("sys_users")) {
                continue;
            }
            code = "package " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".controller;\n" +
                    "\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".entity." + tableInfo.getNameHumpUpperCase() + ";\n" +
                    "import " + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao." + tableInfo.getNameHumpUpperCase() + "Dao;\n" +
                    "import gzb.frame.db.BaseDao;\n" +
                    "import gzb.entity.SqlTemplate;\n" +
                    "import gzb.frame.annotation.*;\n" +
                    "import gzb.tools.json.GzbJson;\n" +
                    "import gzb.tools.log.Log;\n" +
                    "\n" +
                    "@Controller\n" +
                    "@Header(item = {@HeaderItem(key = \"content-type\", val = \"application/json;charset="+ Config.encoding+"\")})\n" +
                    "//@CrossDomain(allowCredentials = false)\n" +
                    "@RequestMapping(value = \"/system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "\")\n" +
                    "public class " + tableInfo.getNameHumpUpperCase() + "Controller{\n" +
                    "    @Resource(value = \"" + this.pkg + "." + tableInfo.getDbNameLowerCase() + ".dao.impl." + tableInfo.getNameHumpUpperCase() + "DaoImpl\")\n" +
                    "    " + tableInfo.getNameHumpUpperCase() + "Dao " + tableInfo.getNameHumpLowerCase() + "Dao;\n" +
                    "    @Resource(value = \"gzb.tools.log.LogImpl\")\n" +
                    "    Log log;\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 查询,不能接受空数据,请至少提供一个参数,请提供 " + tableInfo.getNameHumpUpperCase() + "的参数 \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/find?参数同下,只是仅接受查询结果为1条的情况\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(value = \"find\")\n" +
                    "    public Object find(GzbJson result, " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") throws Exception {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + " == null || " + tableInfo.getNameHumpLowerCase() + ".toString().equals(\"{}\")) {\n" +
                    "            return result.fail(\"find 输入参数错误\");\n" +
                    "        }\n" +
                    "\n" +
                    "        " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + "1 = " + tableInfo.getNameHumpLowerCase() + "Dao.find(" + tableInfo.getNameHumpLowerCase() + ");\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + "1 == null) {\n" +
                    "            return result.fail(\"find 要查询的数据不存在或不适合find\");\n" +
                    "        }\n" +
                    "        return result.success(\"查询成功\"," + tableInfo.getNameHumpLowerCase() + "1);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 查询,不能接受空数据,请至少提供一个参数,请提供 " + tableInfo.getNameHumpUpperCase() + "的参数 \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/list?参数同下,只是是根据实体类查询\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(\"list\")\n" +
                    "    public Object list(GzbJson result," + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ", String sortField, String sortType, Integer page, Integer size) throws Exception {\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.queryPage(" + tableInfo.getNameHumpLowerCase() + ", sortField, sortType, page, size, 100, 1000);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 查询,根据id修改其他参数 请提供 " + tableInfo.getNameHumpUpperCase() + "的参数 \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式" +
                    "&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(\"query\")\n" +
                    "    public Object query(GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {\n" +
                    "        SqlTemplate sqlTemplate = " + tableInfo.getNameHumpLowerCase() + "Dao.getDataBase().toSelect(\"" + tableInfo.getName() + "\", field, symbol, value, montage, sortField, sortType);\n" +
                    "        return " + tableInfo.getNameHumpLowerCase() + "Dao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 " + tableInfo.getNameHumpUpperCase() + "的参数 \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/save\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @PostMapping(\"save\")\n" +
                    "    public Object save(GzbJson result, " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") throws Exception {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + " == null || " + tableInfo.getNameHumpLowerCase() + ".toString().equals(\"{}\")) {\n" +
                    "            return result.fail(\"save 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + "Dao.save(" + tableInfo.getNameHumpLowerCase() + ") < 0) {\n" +
                    "            return result.fail(\"save 保存失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"save 成功\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 修改,根据id修改其他参数 请提供 " + tableInfo.getNameHumpUpperCase() + "的参数 \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/update?" + tableInfo.getIdHumpLowerCase() + "=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @PostMapping(\"update\")\n" +
                    "    public Object update(GzbJson result, " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") throws Exception {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + " == null || " + tableInfo.getNameHumpLowerCase() + ".toString().equals(\"{}\") || " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "() == null) {\n" +
                    "            return result.fail(\"update 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + "Dao.update(" + tableInfo.getNameHumpLowerCase() + ") < 0) {\n" +
                    "            return result.fail(\"update 失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"update 成功\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 单个删除,根据id 请提供 " + tableInfo.getIdHumpLowerCase() + " \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/delete?" + tableInfo.getIdHumpLowerCase() + "=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @DeleteMapping(\"delete\")\n" +
                    "    public Object delete(GzbJson result, " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getNameHumpLowerCase() + ") throws Exception {\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + " == null || " + tableInfo.getNameHumpLowerCase() + ".toString().equals(\"{}\") || " + tableInfo.getNameHumpLowerCase() + ".get" + tableInfo.getIdHumpUpperCase() + "() == null) {\n" +
                    "            return result.fail(\"delete 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (" + tableInfo.getNameHumpLowerCase() + "Dao.delete(" + tableInfo.getNameHumpLowerCase() + ") < 0) {\n" +
                    "            return result.fail(\"delete 失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"成功删除[1]条数据\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 批量删除,根据id 删除多个请重复提供 " + tableInfo.getIdHumpLowerCase() + " \n" +
                    "     * /system/v1.0.0/" + tableInfo.getNameHumpLowerCase() + "/deleteAll?" + tableInfo.getIdHumpLowerCase() + "=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @DeleteMapping(\"deleteAll\")\n" +
                    "    public Object deleteAll(GzbJson result, Long[] " + tableInfo.getIdHumpLowerCase() + ") throws Exception {\n" +
                    "        if (" + tableInfo.getIdHumpLowerCase() + " == null || " + tableInfo.getIdHumpLowerCase() + ".length < 1) {\n" +
                    "            return result.fail(\"delete 输入参数错误\");\n" +
                    "        }\n" +
                    "        int num = 0;\n" +
                    "        " + tableInfo.getNameHumpUpperCase() + " " + tableInfo.getDbNameLowerCase() + " = new " + tableInfo.getNameHumpUpperCase() + "();\n" +
                    "        for (Long aLong : " + tableInfo.getIdHumpLowerCase() + ") {\n" +
                    "            " + tableInfo.getNameHumpLowerCase() + "Dao.delete(" + tableInfo.getDbNameLowerCase() + ".set" + tableInfo.getIdHumpUpperCase() + "(aLong));\n" +
                    "            num++;\n" +
                    "        }\n" +
                    "        return result.success(\"成功删除[\" + num + \"]条数据\");\n" +
                    "    }\n" +
                    "};";
            saveFile(getFilePath(this.pkg + "." + tableInfo.getDbNameLowerCase() + ".controller", tableInfo.getNameHumpUpperCase() + "Controller"), code, save);
        }
    }
}