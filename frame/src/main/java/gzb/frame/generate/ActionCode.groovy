package gzb.frame.generate

import gzb.entity.TableInfo

class ActionCode extends Base {
    public ActionCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list, boolean save) {
        String code=null;
        for (final def tableInfo in list) {
            if (tableInfo.name.equals("sys_users")){
                continue;
            }
            code="package ${this.pkg}.${tableInfo.dbNameLowerCase}.controller;\n" +
                    "\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.entity.${tableInfo.nameHumpUpperCase};\n" +
                    "import ${this.pkg}.${tableInfo.dbNameLowerCase}.dao.${tableInfo.nameHumpUpperCase}Dao;\n" +
                    "import gzb.frame.db.BaseDao;\n" +
                    "import gzb.entity.SqlTemplate;\n" +
                    "import gzb.frame.annotation.*;\n" +
                    "import gzb.tools.JSONResult;\n" +
                    "import gzb.tools.log.Log;\n" +
                    "\n" +
                    "@Controller\n" +
                    "@RequestMapping(value = \"/system/v1.0.0/${tableInfo.nameHumpLowerCase}\", header = \"content-type:application/json;charset=UTF-8\")\n" +
                    "public class ${tableInfo.nameHumpUpperCase}Controller{\n" +
                    "    @Resource(value = \"${this.pkg}.${tableInfo.dbNameLowerCase}.dao.impl.${tableInfo.nameHumpUpperCase}DaoImpl\")\n" +
                    "    ${tableInfo.nameHumpUpperCase}Dao ${tableInfo.nameHumpLowerCase}Dao;\n" +
                    "    @Resource(value = \"gzb.tools.log.LogImpl\")\n" +
                    "    Log log;\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ${tableInfo.nameHumpUpperCase}的参数 \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/find?参数同下,只是仅接受查询结果为1条的情况\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(value = \"find\")\n" +
                    "    public Object find(JSONResult result, ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) throws Exception {\n" +
                    "        if (${tableInfo.nameHumpLowerCase} == null || ${tableInfo.nameHumpLowerCase}.toString().equals(\"{}\")) {\n" +
                    "            return result.fail(\"find 输入参数错误\");\n" +
                    "        }\n" +
                    "\n" +
                    "        ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}1 = ${tableInfo.nameHumpLowerCase}Dao.find(${tableInfo.nameHumpLowerCase});\n" +
                    "        if (${tableInfo.nameHumpLowerCase}1 == null) {\n" +
                    "            return result.fail(\"find 要查询的数据不存在或不适合find\");\n" +
                    "        }\n" +
                    "        return result._data(${tableInfo.nameHumpLowerCase}1.toString()).success(\"查询成功\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ${tableInfo.nameHumpUpperCase}的参数 \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/list?参数同下,只是是根据实体类查询\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(\"list\")\n" +
                    "    public Object list(JSONResult result,${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}, String sortField, String sortType, Integer page, Integer size) throws Exception {\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.queryPage(${tableInfo.nameHumpLowerCase}, sortField, sortType, page, size, 100, 1000);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 修改,根据id修改其他参数 请提供 ${tableInfo.nameHumpUpperCase}的参数 \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式" +
                    "&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(\"query\")\n" +
                    "    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {\n" +
                    "        SqlTemplate sqlTemplate = ${tableInfo.nameHumpLowerCase}Dao.getDataBase().toSelect(\"${tableInfo.name}\", field, symbol, value, montage, sortField, sortType);\n" +
                    "        return ${tableInfo.nameHumpLowerCase}Dao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ${tableInfo.nameHumpUpperCase}的参数 \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/save\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @PostMapping(\"save\")\n" +
                    "    public Object save(JSONResult result, ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) throws Exception {\n" +
                    "        if (${tableInfo.nameHumpLowerCase} == null || ${tableInfo.nameHumpLowerCase}.toString().equals(\"{}\")) {\n" +
                    "            return result.fail(\"save 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (${tableInfo.nameHumpLowerCase}Dao.save(${tableInfo.nameHumpLowerCase}) < 0) {\n" +
                    "            return result.fail(\"save 保存失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"save 成功\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 修改,根据id修改其他参数 请提供 ${tableInfo.nameHumpUpperCase}的参数 \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/update?${tableInfo.idHumpLowerCase}=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @PostMapping(\"update\")\n" +
                    "    public Object update(JSONResult result, ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) throws Exception {\n" +
                    "        if (${tableInfo.nameHumpLowerCase} == null || ${tableInfo.nameHumpLowerCase}.toString().equals(\"{}\") || ${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}() == null) {\n" +
                    "            return result.fail(\"update 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (${tableInfo.nameHumpLowerCase}Dao.update(${tableInfo.nameHumpLowerCase}) < 0) {\n" +
                    "            return result.fail(\"update 失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"update 成功\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 单个删除,根据id 请提供 ${tableInfo.idHumpLowerCase} \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/delete?${tableInfo.idHumpLowerCase}=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @DeleteMapping(\"delete\")\n" +
                    "    public Object delete(JSONResult result, ${tableInfo.nameHumpUpperCase} ${tableInfo.nameHumpLowerCase}) throws Exception {\n" +
                    "        if (${tableInfo.nameHumpLowerCase} == null || ${tableInfo.nameHumpLowerCase}.toString().equals(\"{}\") || ${tableInfo.nameHumpLowerCase}.get${tableInfo.idHumpUpperCase}() == null) {\n" +
                    "            return result.fail(\"delete 输入参数错误\");\n" +
                    "        }\n" +
                    "        if (${tableInfo.nameHumpLowerCase}Dao.delete(${tableInfo.nameHumpLowerCase}) < 0) {\n" +
                    "            return result.fail(\"delete 失败\");\n" +
                    "        }\n" +
                    "        return result.success(\"成功删除[1]条数据\");\n" +
                    "    }\n" +
                    "\n" +
                    "    /**\n" +
                    "     * 批量删除,根据id 删除多个请重复提供 ${tableInfo.idHumpLowerCase} \n" +
                    "     * /system/v1.0.0/${tableInfo.nameHumpLowerCase}/deleteAll?${tableInfo.idHumpLowerCase}=\n" +
                    "     * */\n" +
                    "    @DecoratorOpen\n" +
                    "    @GetMapping(\"deleteAll\")\n" +
                    "    public Object deleteAll(JSONResult result, Long[] ${tableInfo.idHumpLowerCase}) throws Exception {\n" +
                    "        if (${tableInfo.idHumpLowerCase} == null || ${tableInfo.idHumpLowerCase}.length < 1) {\n" +
                    "            return result.fail(\"delete 输入参数错误\");\n" +
                    "        }\n" +
                    "        int num=0;\n" +
                    "        ${tableInfo.nameHumpUpperCase} ${tableInfo.dbNameLowerCase} = new ${tableInfo.nameHumpUpperCase}();\n" +
                    "        for (Long aLong : ${tableInfo.idHumpLowerCase}) {\n" +
                    "            ${tableInfo.nameHumpLowerCase}Dao.delete(${tableInfo.dbNameLowerCase}.set${tableInfo.idHumpUpperCase}(aLong));num++;\n" +
                    "        }\n" +
                    "        return result.success(\"成功删除[\"+num+\"]条数据\");\n" +
                    "    }\n";
                    code+="}\n";
            saveFile(getFilePath("${this.pkg}.${tableInfo.dbNameLowerCase}.controller","${tableInfo.nameHumpUpperCase}Controller"),code,save);
        }

    }

}
