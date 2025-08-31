console.log("load:js/edit/sysGroup.js")
let initListPage2 = gzb.initListPage
gzb.initListPage = function () {
    console.log("gzb.initListPage  hook ")
    initListPage2();
    gzb.appendTableButton("更新映射",gzb.button.colour["红色"],null,function (obj,data){
        gzb.get("/system/v1.0.0/update/updatePermissionAndMapping?sysGroupId="+data.sysGroupId, null, function (res) {
            gzb.jsonVerify(res, true)
        })
    },"open_page_updatePermissionAndMapping")
    gzb.appendTableButton("表配置",null,null,function (obj,data){
        gzb.openPage("list.html?config=sysGroupTable&sysGroupTableGid="+data.sysGroupId
            ,"权限组-页面配置"+data.sysGroupName,function (){
                console.log("end")
            },"100%","100%");
    },"open_page_sysGroupTable")

    gzb.appendTableButton("列配置",null,null,function (obj,data){
        gzb.openPage("list.html?config=sysGroupColumn&sysGroupColumnGid="+data.sysGroupId,"权限组-页面配置-"+data.sysGroupName,function (){
            console.log("end")
        },"100%","100%");
    },"open_page_sysGroupColumn")

    gzb.appendTableButton("授权",gzb.button.colour["蓝色"],null,function (obj,data){
        gzb.openPage("permission.html?sysGroupId="+data.sysGroupId,"权限组-页面配置-"+data.sysGroupName,function (){
            console.log("end")
        },"100%","100%");
    },"open_page_sysGroupId")

}



