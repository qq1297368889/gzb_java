/////////////////////////////// 开始加载
console.log("sysGroup.js", location.href)
/////////////////////////////// 初始化 必要数据
/////////////////////////////// 绑定表信息
gzb.entity_name = "sysGroup";
gzb.entity_id = "sysGroupId";
let base = "/system";
/////////////////////////////// 设置API
gzb.api = {
    option: base + "/v1.0.0/sysOption/list",//查询api 根据实体对象
    mapping: base + "/v2/read/mapping",//读取元数据
    upload: base + "/v2/upload",//上传文件api
    user_info: base + "/v2/read/user/info",//上传文件api
    user_exit:base + "/v2/exit/user",
    read_permission:base + "/v2/read/permission",
    query_data: {},//废弃
    query_met: "GET",//表格查询的 请求类型
    query: base + "/v1.0.0/" + gzb.entity_name + "/query",
    list: base + "/v1.0.0/" + gzb.entity_name + "/list",
    save: base + "/v1.0.0/" + gzb.entity_name + "/save",
    update: base + "/v1.0.0/" + gzb.entity_name + "/update",
    delete: base + "/v1.0.0/" + gzb.entity_name + "/deleteAll"
}
/////////////////////////////// 添加自定义按钮
//注册 表格上方按钮
//gzb.registerTableButton("测试按钮1", 1, 2, function (data, obj) {});
//注册表格行级 工具类按钮
//gzb.registerButton("测试按钮2", 1, 2, null, function () {});

/////////////////////////////// hook 请求数据 请注意 只接受 string参数
gzb.registerGetDataHook(function (data) {
    console.log("hook getData", data)
    return data;
});
gzb.registerPostDataHook(function (data) {
    console.log("hook postData", data)
    return data;
});
gzb.registerPutDataHook(function (data) {
    console.log("hook putData", data)
    return data;
});
gzb.registerDeleteDataHook(function (data) {
    console.log("hook deleteData", data)
    return data;
});