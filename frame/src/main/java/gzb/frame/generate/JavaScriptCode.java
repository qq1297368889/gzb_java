package gzb.frame.generate;

import gzb.entity.TableInfo;
import gzb.tools.Tools;

import java.io.File;
import java.util.List;

public class JavaScriptCode extends Base {


    public JavaScriptCode(String path, String pkg, String table) {
        this.path = path;
        this.pkg = pkg;
        this.table = table;
    }

    public void start(List<TableInfo> list) {

        for (TableInfo tableInfo : list) {
            gen_jsCode(tableInfo.getNameHumpLowerCase(), tableInfo.getIdHumpLowerCase());
        }

    }

    public void gen_jsCode(String name, String id) {
        String path = getHtmlPath();
        path += "/js/entity/";
        new File(path).mkdirs();
        path = Tools.pathFormat(path);
        String code = "/////////////////////////////// 开始加载\n" +
                "console.log(\"" + name + ".js\", location.href)\n" +
                "/////////////////////////////// 初始化 必要数据\n" +
                "/////////////////////////////// 绑定表信息\n" +
                "gzb.entity_name = \"" + name + "\";\n" +
                "gzb.entity_id = \"" + id + "\";\n" +
                "let base = \"/system\";\n" +
                "/////////////////////////////// 设置API\n" +
                "gzb.api = {\n" +
                "    option: base + \"/v1.0.0/sysOption/list\",//查询api 根据实体对象\n" +
                "    mapping: base + \"/v2/read/mapping\",//读取元数据\n" +
                "    upload: base + \"/v2/upload\",//上传文件api\n" +
                "    user_info: base + \"/v2/read/user/info\",//上传文件api\n" +
                "    user_exit:base + \"/v2/exit/user\",\n" +
                "    read_permission:base + \"/v2/read/permission\",\n" +
                "    query_data: {},//废弃\n" +
                "    query_met: \"GET\",//表格查询的 请求类型\n" +
                "    query: base + \"/v1.0.0/\" + gzb.entity_name + \"/query\",\n" +
                "    list: base + \"/v1.0.0/\" + gzb.entity_name + \"/list\",\n" +
                "    save: base + \"/v1.0.0/\" + gzb.entity_name + \"/save\",\n" +
                "    update: base + \"/v1.0.0/\" + gzb.entity_name + \"/update\",\n" +
                "    delete: base + \"/v1.0.0/\" + gzb.entity_name + \"/deleteAll\"\n" +
                "}\n" +
                "/////////////////////////////// 添加自定义按钮\n" +
                "//注册 表格上方按钮\n" +
                "//gzb.registerTableButton(\"测试按钮1\", 1, 2, function (data, obj) {});\n" +
                "//注册表格行级 工具类按钮\n" +
                "//gzb.registerButton(\"测试按钮2\", 1, 2, null, function () {});\n" +
                "\n" +
                "/////////////////////////////// hook 请求数据 请注意 只接受 string参数\n" +
                "gzb.registerGetDataHook(function (data) {\n" +
                "    console.log(\"hook getData\", data)\n" +
                "    return data;\n" +
                "});\n" +
                "gzb.registerPostDataHook(function (data) {\n" +
                "    console.log(\"hook postData\", data)\n" +
                "    return data;\n" +
                "});\n" +
                "gzb.registerPutDataHook(function (data) {\n" +
                "    console.log(\"hook putData\", data)\n" +
                "    return data;\n" +
                "});\n" +
                "gzb.registerDeleteDataHook(function (data) {\n" +
                "    console.log(\"hook deleteData\", data)\n" +
                "    return data;\n" +
                "});";
        if (new File(path + "/" + name + ".js").exists()) {
            return;
        }
        saveFile(path + "/" + name + ".js", code, true);
    }
}
