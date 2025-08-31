//yyyy-MM-dd hh:mm:ss
Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 鏈堜唤
        "d+": this.getDate(), // 鏃�
        "h+": this.getHours(), // 灏忔椂
        "m+": this.getMinutes(), // 鍒�
        "s+": this.getSeconds(), // 绉�
        "q+": Math.floor((this.getMonth() + 3) / 3), // 瀛ｅ害
        "S": this.getMilliseconds() // 姣
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
console.log(location.href)
String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

function getUUid() {
    let s = [];
    let hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    let uuid = s.join("");
    return "UUID-" + uuid + "-" + new Date().getTime();
}

function loadScript(url) {
    console.log("url", url)
    let data = httpGet(url);
    console.log("data",data)
    eval(data);
}

function httpGet(url) {
    let xml = new XMLHttpRequest();
    xml.open("GET", url, false)
    xml.send();
    console.log(xml.responseText)
    return (xml.responseText);
}

function httpRequest(url, met, data, async) {
    list.requestData = list.getParas();
    for (let key2 in list.requestData) {
        url = url.toString().replaceAll("\\${" + key2 + "}", list.requestData[key2])
    }
    let appendUrl = function (url, data1) {
        if (url.indexOf("?") == -1) {
            url += "?";
        }
        let a01 = url.substring(url.length - 1, url.length);
        if (a01 != "&" && a01 != "?") {
            url += "&";
        }
        url += data1 + "&";
        return url;
    }
    if (data != null && Array.isArray(data)) {
        for (let i in data) {
            let data1 = data[i];
            for (let key in data1) {
                let val = data1[key];
                for (let key2 in list.requestData) {
                    if ("${" + key2 + "}" == val) {
                        val = list.requestData[key2];
                    }
                }
                if (met.toLowerCase() == "get") {
                    url = appendUrl(url, i + "[" + key + "]=" + val);
                }
            }
        }
    } else if (data != null && typeof data == "object") {
        let i = 0;
        for (let key in data) {
            let val = data[key];
            for (let key2 in list.requestData) {
                if ("${" + key2 + "}" == val) {
                    val = list.requestData[key2];
                }
            }
            if (met.toLowerCase() == "get") {
                url = appendUrl(url, i + "[" + key + "]=" + val);
            }
        }
    } else if (data != null) {
        for (let key2 in list.requestData) {
            data = data.toString().replaceAll("\\${" + key2 + "}", list.requestData[key2])
        }
        if (met.toLowerCase() == "get") {
            url = appendUrl(url, data);
        }
    }
    met = met == null ? "get" : met;
    data = data == null ? "" : data;
    async = async == null ? false : async;
    let xhr = new XMLHttpRequest();
    xhr.open(met, url, async); // 第三个参数为是否开启异步请求
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(data);
    let result;
    try {
        result = JSON.parse(xhr.responseText);
    } catch (e) {
        try {
            result = JSON.parse(xhr.response);
        } catch (e) {

        }
    }
    return result;
}

function getSelectDataPublicDef(key) {
    if (key == null) {
        key = "def_status";
    }
    return getSelectDataPublic("/system/sysStatus", "GET", {
        "sysStatusKey": key,
        "page": "1",
        "limit": "0"
    }, "sysStatusTitle", "sysStatusVal");
}

function getSelectDataPublic(api, met, data, titleField, valueField) {
    let map = list.getParas();
    for (let mapKey in map) {
        let str1 = "$";
        str1 += "{" + mapKey + "}"
        api = api.replaceAll(str1, map[mapKey]);
        data = data.replaceAll(str1, map[mapKey]);
    }
    if (api == null || valueField == null || titleField == null) {
        return null;
    }
    let res = httpRequest(api, met, data, false);
    let arr = [{"title": "请选择", "value": ""}];
    if (list.jsonVerify(res) && res.data != null) {
        for (let data of res.data) {
            let title = "";
            let value = "";
            let ss1 = titleField.toString().split("-");
            for (let i in ss1) {
                title += data[ss1[i]];
                if (i < ss1.length - 1) {
                    title += ">";
                }
            }
            let ss2 = valueField.toString().split("-");
            for (let i in ss2) {
                value += data[ss2[i]];
                if (i < ss2.length - 1) {
                    value += ">";
                }
            }
            arr[arr.length] = {"title": title, "value": value}
        }
    }
    return arr;
}
let loadOk=function(){
    if (list.ButArray[0]==null) {
        list.ButArray[0] = {"title": "查询", "colour": "原始", "clickFun": list.tableInit}
    }
    if (list.ButArray[1]==null) {
        list.ButArray[1] = {"title": "添加", "colour": "蓝色", "clickFun": list.openAddPage}
    }
    if (list.ButArray[2]==null) {
        list.ButArray[2] = {"title": "删除", "colour": "红色", "clickFun": list.deleteAll}
    }


    if (list.tableButArray[0]==null) {
        list.tableButArray[0] = {"title": "编辑", "colour": "蓝色", "clickFun": list.tableUpdate}
    }
    if (list.tableButArray[1]==null) {
        list.tableButArray[1] = {"title": "删除", "colour": "红色", "clickFun": list.tableDelete}
    }
    console.log("load:lib.js")
}

function getMappingData(key) {
    let res = httpGet("/system/sysMapping?field=sysMappingKey&value=" + key + "&symbol=1&montage=0&page=1&limit=100")
    res = JSON.parse(res)
    if (list.jsonVerify(res) == false) {
        console.log("err " + JSON.stringify(res))
        return null;
    }
    let titles = {};
    for (let data of res.data) {
        let key = data["sysMappingValue"];
        if (key == null) {
            continue;
        }
        let tf = "";
        let ss1 = key.split("_");
        for (let i = 0; i < ss1.length; i++) {
            if (ss1[i] != null && ss1[i].length > 0) {
                let s2 = ss1[i].substring(0, 1);
                let s3 = ss1[i].substring(1, ss1[i].length);
                if (i === 0) {
                    tf += s2 + s3;
                } else {
                    tf += s2.toUpperCase() + s3;
                }
            }
        }
        data["sysMappingValue"] = tf
        titles[tf] = data;
        titles[key] = data;
        //console.log(tf, key)
    }
    //console.log("titles " + JSON.stringify(titles))
    return titles;
}

let list = {};

//展示顺序
list.sort = [];
//按钮颜色 映射
list.colourData = {
    "红色": "layui-btn-danger",
    "黄色": "layui-btn-warm",
    "蓝色": "layui-btn-normal",
    "原始": "layui-btn-primary",
    "禁用": "layui-btn-disabled",
};
//元素字段映射 id是key  val是字段名
list.eleMapping = {};
//表格 默认分页的集合
list.tableDefLimits = [10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 500, 1000]
//表格默认分页条数
list.tableDefLimit = 50
//表格右侧按钮信息
list.tableButArray = [];
//表格上边按钮信息
list.ButArray = [];
//表格右侧按钮占用长度PX
list.tableButArraySize = 55;
//默认带上的请求参数
list.requestData = {}
//防止连续操作的 计时 时间戳
list.lao_time = 0;
//当前被修改的数据缓存在这里方便存取  使用
list.thisUpdateData = null;
//事件保存。内部使用 无需关注
list.addTableButtonClickFun = {};
//请求API
list.restfulApi = null;
//复杂查询专用 api
list.queryApi = null;
//文件上传接口 对接数据库
list.uploadApi = null;
//主键
list.fieldId = null;
//api的状态 定义
list.status = {"success": "1", "fail": "2", "err": "3", "jump": "4"}
//所有字段
list.fields = [];
//字段的文本标题
list.titles = [];
//多个查询条件的 连接符  1 and ，  2or
list.queryFieldType = [];
//查询类型 1 = ,2 > ,3>= ,4< ,5<= ,6 <> ,7 %like% ,8 like% ,9 %like
list.queryFieldSymbol = [];
//是否显示在查询条件里  开启true  关闭false
list.queryFields = [];
//最小宽度 tableWidths==null 最小宽度有效
list.tableMinWidths = [];
// 如果不为null 最小宽度就无效
list.tableWidths = [];
//表格编辑 开启true  关闭false
list.edits = [];
//table里的脚本模板  直接写函数
list.tableScripts = [];
//如果type 是select  就来这里读 选项数据   select的数据 [{"title":"title","value":"value"}]
list.selects = [];
//默认值
list.defValQuery = [];
list.defValAdd = [];
list.defValUpdate = [];
//各个字段的类型  //input date time datetime image select
list.types = [];
//是否显示在表格中 true 显示在表格  false不显示
list.table = [];
//true 显示在修改界面 false不显示
list.update = [];
//true 显示在修改界面 false不显示
list.add = [];
//这是图片模板 如果type = image  此字段有效
list.imageTemplates = [];
list.thisTimeString = new Date().Format("yyyy-MM-dd hh:mm:ss");
list.imageTemplate = "/${fieldName}";
list.cache = {};
list.temp_index = 0;
/*111
*
*
*
     {
            "sysMappingId": "72",
            "sysMappingKey": "gameGrant",
            "sysMappingTableUpdate": "1",
            "sysMappingQuery": "1",
            "sysMappingTableShow": "1",
            "sysMappingTableEdit": "1",
            "sysMappingTime": "2024-01-07 11:30:54",
            "sysMappingState": "1",
            "sysMappingValue": "game_grant_time_ok",
            "sysMappingTitle": "完成",
            "sysMappingQuerySymbol": "1",
            "sysMappingTableWidth": "100",
            "sysMappingQueryType": "1",
            "sysMappingTableMinWidth": "100",
            "sysMappingAdd": "1"
        },
* */
list.getIndex = function (field) {
    for (let i in list.fields) {
        if (list.fields[i] == field) {
            return i;
        }
    }
}
list.setConfigAllData = function (data) {
    if (data == null) {
        //console.log("list.setConfigAllData.data==null")
        return;
    }
    let field = data["sysMappingValue"];
    let title = data["sysMappingTitle"];
    let type = data["sysMappingType"] == null ? "input" : data["sysMappingType"];
    let query = data["sysMappingQuery"] != null && data["sysMappingQuery"] == "1" ? true : false;
    let queryType = data["sysMappingQueryMontage"];
    let querySymbol = data["sysMappingQuerySymbol"];
    let add = data["sysMappingAdd"] != null && data["sysMappingAdd"] == "1" ? true : false;
    let update = data["sysMappingTableUpdate"] != null && data["sysMappingTableUpdate"] == "1" ? true : false;
    let image = data["sysMappingImage"];
    let minWidth = data["sysMappingTableMinWidth"];
    let width = data["sysMappingTableWidth"];
    let edit = data["sysMappingTableEdit"] != null && data["sysMappingTableEdit"] == "1" ? true : false;
    let table = data["sysMappingTableShow"] != null && data["sysMappingTableShow"] == "1" ? true : false;
    let select = data["sysMappingSelectUrl"];
    let sysMappingQueryDef = data["sysMappingQueryDef"];
    let sysMappingUpdateDef = data["sysMappingUpdateDef"];
    let sysMappingAddDef = data["sysMappingAddDef"];
    let script = data["sysMappingScript"];
    ///system/sysStatus?sysStatusKey=def_status&page=1&limit=0
    if (type != null && type == "select") {
        let key = field;
        if (select == null) {
            if (list.cache[key] == null) {
                list.cache[key] = getSelectDataPublicDef(key)
            }
            select = list.cache[key]
        } else {
            let arr1 = select.split(";");
            if (arr1.length != 5) {
                console.log("无效 sysMappingSelectUrl：[" + data["sysMappingSelectUrl"] + "]")
                type = "input"
            }
            key = arr1[0] + "-" + arr1[1] + "-" + arr1[2] + "-" + arr1[3] + "-" + arr1[4]
            if (list.cache[key] == null) {
                list.cache[key] = getSelectDataPublic(arr1[0], arr1[1], arr1[2], arr1[3], arr1[4])
            }
            select = list.cache[key]
        }
    }
    if (type != null && type == "image") {
        if (image == null) {
            image = "/${" + field + "}";
        }
        image = image.toString().replace(/thisField/g, field);
    }
    if (image != null) {
        type = "image";
    }
    if (select != null) {
        type = "select";
    }
    list.setConfigAll(field, title, type, query, queryType, querySymbol, table, add, update, select, image, script, minWidth, width, edit, sysMappingQueryDef, sysMappingAddDef, sysMappingUpdateDef);
}
list.setConfigAll = function (field, title, type, query, queryType, querySymbol, table, add, update, select, image, script, minWidth, width, edit, defQuery, defAdd, defUpdate) {
    let index = -1;
    for (let i in list.fields) {
        if (list.fields[i] == field) {
            index = i;
            break;
        }
    }
    if (index == -1) {
        list.fields[list.fields.length] = field;
        index = list.fields.length - 1;
    }
    list.titles[index] = title == null ? list.fields[index] : title;
    list.types[index] = type;
    list.queryFields[index] = query;
    list.queryFieldType[index] = queryType;
    list.queryFieldSymbol[index] = querySymbol;
    list.add[index] = add;
    list.table[index] = table;
    list.update[index] = update;
    list.selects[index] = select;
    list.imageTemplates[index] = image;
    list.tableScripts[index] = script;
    list.tableMinWidths[index] = minWidth;
    list.tableWidths[index] = width;
    list.edits[index] = edit;
    list.defValQuery[index] = defQuery;
    list.defValAdd[index] = defAdd;
    list.defValUpdate[index] = defUpdate;
}

list.sortField = function (arr1) {
    list.sort = [];
    for (let i in arr1) {
        for (let k in list.fields) {
            if (list.fields[k].toString() == arr1[i].toString()) {
                list.sort[list.sort.length] = k;
            }
        }
    }
}
let $ = layui.$;
let admin = layui.admin;
let element = layui.element;
let layer = layui.layer;
let laydate = layui.laydate;
let form = layui.form;
let table = layui.table;
let upload = layui.upload;
let setter = layui.setter;
let router = layui.router();
let search = router.search;
list.tableHeight = 0;
// 拦截请求 的保存
list.postDataInitFun = {}
list.postDataInitFun.query = function (data) {
    return data;
};
list.postDataInitFun.deleteAll = function (data) {
    return data;
};
list.postDataInitFun.deleteSign = function (data) {
    return data;
};
list.postDataInitFun.save = function (data) {
    return data;
};
list.postDataInitFun.update = function (data) {
    return data;
};
list.postDataInitFun.def = function (data) {
    return data;
};
//请求前 会把请求参数传进来。可以自定义处理
//type  1查询 2批量删 3单个删 4插入 5修改  其他值为默认
list.postDataInit = function (postData, type) {
    if (type != null && type.toString() == "1") {
        postData = list.postDataInitFun.query(postData)
        console.log("查询,data:" + JSON.stringify(postData))
    } else if (type != null && type.toString() == "2") {
        postData = list.postDataInitFun.deleteAll(postData)
        console.log("删除-批量,data:" + JSON.stringify(postData))
    } else if (type != null && type.toString() == "3") {
        postData = list.postDataInitFun.deleteSign(postData)
        console.log("删除-单个,data:" + JSON.stringify(postData))
    } else if (type != null && type.toString() == "4") {
        postData = list.postDataInitFun.save(postData)
        console.log("插入,data:" + JSON.stringify(postData))
    } else if (type != null && type.toString() == "5") {
        postData = list.postDataInitFun.update(postData)
        console.log("修改,data:" + JSON.stringify(postData))
    } else {
        postData = list.postDataInitFun.def(postData)
        console.log("默认,data:" + JSON.stringify(postData))
    }
    return postData;
};

list.deleteAll = function () {
    let thisTime = new Date().getTime();
    if (thisTime - list.lao_time < 1000) {
        return;
    }
    list.lao_time = thisTime;
    let data = table.checkStatus('tableId001').data;
    let postData = "";
    for (let json of data) {
        postData += list.fieldId + "=" + json[list.fieldId] + "&"
    }
    layer.confirm('真的要删除已选中[' + data.length + ']条数据吗？', function (index) {
        postData = list.postDataInit(postData, 2);
        list.delete(list.restfulApi, postData, function (res) {
            if (list.jsonVerify(res, true)) {
                table.reload("tableId001");
            }
        });
        layer.close(index);
    });
}
list.tableDelete = function (obj, data) {
    let thisTime = new Date().getTime();
    if (thisTime - list.lao_time < 1000) {
        return;
    }
    list.lao_time = thisTime;
    layer.confirm('真的要删除已选中数据吗？', function (index) {
        let postData = list.fieldId + "=" + data[list.fieldId];
        postData = list.postDataInit(postData, 3);
        list.delete(list.restfulApi, postData, function (res) {
            if (list.jsonVerify(res, true)) {
                obj.del();
            }
        });
        layer.close(index);
    });
}
list.addData = function () {
    let thisTime = new Date().getTime();
    if (thisTime - list.lao_time < 1000) {
        return;
    }
    list.lao_time = thisTime;
    let postData = {}
    for (let i = 0; i < list.fields.length; i++) {
        if (list.add[i]) {
            let node = $("#from-002 #" + list.eleMapping[i + "-" + list.fields[i] + "-add-page"]);
            if (node) {
                let tmp = node.val();
                if (tmp != null && tmp.length > 0) {
                    postData[list.fields[i]] = node.val();
                }
            }
        }
    }
    postData = list.postDataInit(postData, 4);
    list.post(list.restfulApi, postData, function (res) {
        list.jsonVerify(res, true);
    })
}
list.updateData = function (obj, data) {
    let thisTime = new Date().getTime();
    if (thisTime - list.lao_time < 1000) {
        return;
    }
    list.lao_time = thisTime;
    let postData = {}
    for (let i = 0; i < list.fields.length; i++) {
        if (list.update[i]) {
            let node = $("#from-003 #" + list.eleMapping[i + "-" + list.fields[i] + "-update-page"]);
            if (node) {
                /* let tmp = node.val();
                 if (tmp != null) {
                     postData[list.fields[i]] = node.val();
                 } else {
                     postData[list.fields[i]] = list.thisUpdateData[list.fields[i]]
                 }*/
                postData[list.fields[i]] = node.val();
            } else {
                postData[list.fields[i]] = list.thisUpdateData[list.fields[i]]
            }
        } else {
            postData[list.fields[i]] = list.thisUpdateData[list.fields[i]]
        }
    }
    console.log(postData)
    postData = list.postDataInit(postData, 5);
    list.put(list.restfulApi, postData, function (res) {
        if (list.jsonVerify(res, true)) {
            list.newUpdata = postData;
        }
    })
}
list.editUpdate = function (obj) {
    let field = obj.field;
    let value = obj.value;
    let data = obj.data;
    let postData = {}
    for (let key in data) {
        postData[key] = data[key];
    }
    list.put(list.restfulApi, postData, function (res) {
        list.jsonVerify(res, true);
    })
}
list.upload = function () {

}
list.init = function () {
    loadOk();
    list.requestData = list.getParas();
    list.initHtml();
}
list.tableInit = function () {
    if (list.sort == null || list.sort.length != list.fields.length) {
        list.sortField(list.fields);
    }
    let thisTime = new Date().getTime();
    if (thisTime - list.lao_time < 1000) {
        return;
    }
    list.lao_time = thisTime;
    let listCols = [{type: 'checkbox', fixed: 'left'}]
    let k = 1;
    for (let i0 in list.sort) {
        let i = list.sort[i0];
        if (list.table[i]) {
            listCols[k] = {
                edit: list.edits[i] ? true : false,
                field: list.fields[i],
                title: list.titles[i],
                minWidth: list.tableMinWidths[i] == null ? 100 : list.tableMinWidths[i],
                //Width: list.tableWidths[i],
            }
            if (list.tableScripts[i] != null && list.tableScripts[i].length > 0) {
                eval("listCols[k]['templet']=" + list.tableScripts[i])
            }
            if (list.types[i] == null) {
                list.types[i] = "input";
            }
            if (list.types[i] == "select") {
                listCols[k]["templet"] = function (data) {
                    let nodes = document.getElementsByClassName("table_edit_select_003")
                    for (let i = 0; i < nodes.length; i++) {
                        nodes[i].remove();
                    }
                    let text = list.convertState(list.selects[i], data[list.fields[i]], list.fields[i]);
                    text = "<span class='gzb_table_temp_002_text' data_id='" + data[list.fieldId] + "'>" + text + "</span>";
                    list.cache["data_id_" + data[list.fieldId]] = data
                    return text;
                }
                listCols[k].edit = false
            } else if (list.types[i] == "image") {
                eval("listCols[k][\"templet\"] = function (data) {" +
                    "return list.addTableImage(" +
                    "\"" + list.imageTemplates[i] + "\".replace(\"${" + list.fields[i] + "}\",data[\"" + list.fields[i] + "\"])," +
                    "\"50\")}")
            }
            k++;
        }
    }
    if (list.tableButArray.length > 0) {
        listCols[k] = {
            edit: false,
            field: "cao_zuo",
            title: "操作",
            minWidth: list.tableButArray.length * list.tableButArraySize,
            templet: list.tableBut
        }
    }
    let postData = [];
    for (let i in list.fields) {
        let val = $("#" + list.eleMapping[i + "-" + list.fields[i]]).val();
        if (postData[postData.length] != null) {
            continue;
        }
        if (val == null || val == "") {
            continue;
        }
        let json = {
            "field": list.fields[i],
            "symbol": list.queryFieldSymbol[i],
            "value": val,
            "montage": list.queryFieldType[i]
        }
        postData[postData.length] = json;
    }
    console.log("postData", postData)
    postData = list.postDataInit(postData, 1);
    /*    if (postData.length > 0) {
            postData[postData.length - 1]["montage"] = "0";
        }*/
    console.log("postData", postData);
    if (list.tableHeight == 0) {
        list.tableHeight = document.getElementsByClassName("layui-card")[0].offsetHeight;
        list.tableHeight = window.innerHeight - list.tableHeight - 20
    }
    table.render({
        elem: '#tableId001'
        , url: list.queryApi
        , method: 'get'  //"post"
        , where: postData //如果无需传递额外参数，可不加该参数
        , toolbar: true
        , title: '数据'
        , totalRow: true
        , cellMinWidth: 150
        , cols: [listCols]
        , page: true
        , limits: list.tableDefLimits
        , limit: list.tableDefLimit
        , height: list.tableHeight
        , response: {
            statusCode: 1 //重新规定成功的状态码 res.code
        }
        , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据

            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.total, //解析数据长度
                "data": res.data //解析数据列表
            };
        }
    });

    //监听单元格编辑
    table.on('edit(tableId001)', list.editUpdate);
//监听工具条
    table.on('tool(tableId001)', function (obj) {
        let data = obj.data;
        for (let key in list.addTableButtonClickFun) {
            if (key.toString() == obj.event.toString()) {
                list.addTableButtonClickFun[key](obj, data);
            }
        }
    });
    let delEle = function (ele, arr1) {
        try {
            let node = document.getElementById(ele.id + "_div");
            let data_id = node.querySelector(".gzb_table_temp_002_text").getAttribute("data_id")
            let field = ele.getAttribute("field")

            let obj = {
                field: field,
                value: ele.value,
                data: list.cache["data_id_" + data_id]
            };
            let lao = obj.data[obj.field]
            obj.data[obj.field] = ele.value
            obj.value = ele.value

            console.log(obj.value, obj.field, obj.data[obj.field], obj.data)
            if (obj.value != null && obj.value != lao) {
                list.editUpdate(obj)
                let data01 = list.convertState(arr1, ele.value)
                let text = data01;
                text = "<span class='gzb_table_temp_002_text' data_id='" + obj.data[list.fieldId] + "'>" + text + "</span>";
                document.getElementById(ele.id + "_div").innerHTML = text;
            }
        } catch (e) {
            //console.log(e)
        } finally {
            ele.remove();
        }

    }
    $('body').on('click', 'tbody tr td div', function () {
        let ele = this.parentElement;
        const rect = ele.getBoundingClientRect();
        let arr1;
        let field = ele.getAttribute("data-field");
        for (let i in list.fields) {
            if (list.fields[i] != null && list.fields[i] === field && list.selects[i] != null) {
                arr1 = list.selects[i];
                break
            }
        }
        if (arr1 == null) {
            return;
        }
        list.temp_index++;
        let select = document.createElement("select");
        select.setAttribute("field", field)
        select.className = "table_edit_select_003"
        select.id = "edit_gzb_table_select_001_" + list.temp_index
        this.id = "edit_gzb_table_select_001_" + list.temp_index + "_div"
        let htm = "";
        let thisIndex = 0;
        for (let j = 0; j < arr1.length; j++) {
            if (arr1[j].title === this.innerText) {
                htm += "<option value='" + arr1[j].value + "' selected>" + arr1[j].title + "</option>"
                thisIndex = j;
            } else {
                htm += "<option value='" + arr1[j].value + "'>" + arr1[j].title + "</option>"
            }
        }
        select.innerHTML = htm;
        // 设置 select 元素的样式，使其与点击元素的位置和大小一致
        select.style.position = 'absolute';
        select.style.left = rect.left + 'px';
        select.style.top = rect.top + 'px';
        select.style.width = rect.width + 'px';
        select.style.height = rect.height + 'px';
        select.style.border = "1px solid #5FB878"
        document.body.appendChild(select);
        //select.value=thisIndex;
        select.focus();
        // 创建一个 mousedown 事件
        const event = new MouseEvent('mousedown', {
            view: window,
            bubbles: true,
            cancelable: true
        });
        // 触发 mousedown 事件
        select.dispatchEvent(event);
        select.addEventListener('blur', function () {
            console.log("blur", this)
            delEle(this, arr1);
        });
    });
}
list.initHtml = function () {
    for (let i = 0; i < list.fields.length; i++) {
        if (list.queryFields[i]) {
            let eleId = getUUid();
            list.eleMapping[eleId] = i + "-" + list.fields[i];
            list.eleMapping[i + "-" + list.fields[i]] = eleId;
            if (list.types[i] == null || list.types[i] == "input") {
                let html = list.addInput("from-1", eleId, list.titles[i], list.defValQuery[i], "请输入 " + list.titles[i]);
            } else if (list.types[i] == "date") {
                let html = list.addInput("from-1", eleId, list.titles[i], list.defValQuery[i], "请选择 " + list.titles[i]);
                laydate.render({
                    elem: '#' + eleId
                    , type: 'date'
                });
            } else if (list.types[i] == "time") {
                let html = list.addInput("from-1", eleId, list.titles[i], list.defValQuery[i], "请选择 " + list.titles[i]);
                laydate.render({
                    elem: '#' + eleId
                    , type: 'time'
                });
            } else if (list.types[i] == "datetime") {
                let html = list.addInput("from-1", eleId, list.titles[i], list.defValQuery[i], "请选择 " + list.titles[i]);
                laydate.render({
                    elem: '#' + eleId
                    , type: 'datetime'
                });
            } else if (list.types[i] == "select") {
                let html = list.addSelect("from-1", eleId, list.titles[i], list.selects[i], list.defValQuery[i], null, null);
            } else if (list.types[i] == "textarea") {
                let html = list.addTextarea("from-1", eleId, list.titles[i], list.defValQuery[i], "请输入 " + list.titles[i]);
            }

        }
    }
    for (let i in list.ButArray) {
        list.addButton("but-1", "but-100-" + i, list.ButArray[i]["title"], list.ButArray[i]["colour"], list.ButArray[i]["clickFun"])
    }
    setTimeout(function () {
        list.tableInit();
    }, 100)
    $("body").on("click", "#saveData001", list.addData)
    $("body").on("click", "#updateData001", list.updateData)
}

list.openAddPage = function (obj, data) {
    let html = "<div class=\"layui-card-body\" style=\"padding: 15px;\">\n" +
        "    <form class=\"layui-form\" action=\"\" lay-filter=\"component-form-group\" id='from-002'>\n";
    for (let i = 0; i < list.fields.length; i++) {
        if (list.add[i]) {
            let eleId = getUUid();
            list.eleMapping[eleId] = i + "-" + list.fields[i] + "-add-page";
            list.eleMapping[i + "-" + list.fields[i] + "-add-page"] = eleId;
            if (list.types[i] == null || list.types[i] == "input") {
                html += list.addInputSingle(null, eleId, list.titles[i], list.defValAdd[i], "请输入 " + list.titles[i]);
            } else if (list.types[i] == "date") {
                html += list.addInputSingle(null, eleId, list.titles[i], list.defValAdd[i], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "time") {
                html += list.addInputSingle(null, eleId, list.titles[i], list.defValAdd[i], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "datetime") {
                html += list.addInputSingle(null, eleId, list.titles[i], list.defValAdd[i], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "select") {
                html += list.addSelectSingle(null, eleId, list.titles[i], list.selects[i], list.defValAdd[i], null);
            } else if (list.types[i] == "image") {
                html += list.addUploadSingle(null, eleId, list.titles[i], "选择文件", list.uploadApi, null, list.defValAdd[i], list.upload)
            } else if (list.types[i] == "file") {
                html += list.addUploadFileSingle(null, eleId, list.titles[i], "选择文件", list.uploadApi, null, list.defValAdd[list.fields[i]], list.upload)
            } else if (list.types[i] == "textarea") {
                html += list.addTextareaSingle(null, eleId, list.titles[i], list.defValAdd[i], "请输入 " + list.titles[i]);
            }
        }
    }


    html += "        <div class=\"layui-form-item\">\n" +
        "            <label class=\"layui-form-label\">操作：</label>\n" +
        "            <div class=\"layui-input-block\">\n" +
        "               <button id='saveData001' type='button' class=\"layui-btn layui-btn-danger\">添加/保存</button> " +
        "            </div>\n" +
        "        </div>\n";
    html += "    </form>\n" +
        "</div>";
    layer.open({
        title: '添加数据'
        , type: 1
        //,skin: 'layui-layer-rim'
        , shadeClose: true
        , area: ["60%", "80%"]
        , content: html
        , end: function () {
            layui.table.reload("tableId001");
            console.log("添加数据界面关闭")
        }
    });
    for (let i = 0; i < list.fields.length; i++) {
        if (list.add[i]) {
            if (list.types[i] == "date") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-add-page"]
                    , type: 'date'
                });
            } else if (list.types[i] == "time") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-add-page"]
                    , type: 'time'
                });
            } else if (list.types[i] == "datetime") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-add-page"]
                    , type: 'datetime'
                });
            }
        }
    }
    form.render();
}
list.tableUpdate = function (obj, data) {
    let data1 = list.cache["data_id_" + data[list.fieldId]];
    if (data1 != null) {
        data = data1
    }
    list.thisUpdateData = data;
    console.log(list.thisUpdateData)
    let html = "<div class=\"layui-card-body\" style=\"padding: 15px;\">\n" +
        "    <form class=\"layui-form\" action=\"\" lay-filter=\"component-form-group\" id='from-003'>\n";
    for (let i = 0; i < list.fields.length; i++) {
        if (list.update[i]) {
            let eleId = getUUid();
            list.eleMapping[eleId] = i + "-" + list.fields[i] + "-update-page";
            list.eleMapping[i + "-" + list.fields[i] + "-update-page"] = eleId;
            if (list.types[i] == null || list.types[i] == "input") {
                html += list.addInputSingle(null, eleId, list.titles[i], data[list.fields[i]], "请输入 " + list.titles[i]);
            } else if (list.types[i] == "date") {
                html += list.addInputSingle(null, eleId, list.titles[i], data[list.fields[i]], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "time") {
                html += list.addInputSingle(null, eleId, list.titles[i], data[list.fields[i]], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "datetime") {
                html += list.addInputSingle(null, eleId, list.titles[i], data[list.fields[i]], "请选择 " + list.titles[i]);
            } else if (list.types[i] == "select") {
                html += list.addSelectSingle(null, eleId, list.titles[i], list.selects[i], data[list.fields[i]]);
            } else if (list.types[i] == "image") {
                html += list.addUploadSingle(null, eleId, list.titles[i], "选择文件", list.uploadApi, list.imageTemplates[i].replace("${" + list.fields[i] + "}", data[list.fields[i]]), data[list.fields[i]], list.upload)
            } else if (list.types[i] == "file") {
                console.log(eleId, list.titles[i], "选择文件", list.uploadApi, data[list.fields[i]], data[list.fields[i]], list.upload)
                html += list.addUploadFileSingle(null, eleId, list.titles[i], "选择文件", list.uploadApi, data[list.fields[i]], data[list.fields[i]], list.upload)
            } else if (list.types[i] == "textarea") {
                html += list.addTextareaSingle(null, eleId, list.titles[i], data[list.fields[i]], "请输入 " + list.titles[i]);
            } else {
                console.log("未知的type：" + list.types[i])
            }
        }
    }
    html += "<input id='" + list.fieldId + "' type='hidden' value='" + data[list.fieldId] + "'>"
    html += "        <div class=\"layui-form-item\">\n" +
        "            <label class=\"layui-form-label\">操作：</label>\n" +
        "            <div class=\"layui-input-block\">\n" +
        "               <button id='updateData001' type='button' class=\"layui-btn layui-btn-danger\">修改/更新</button> " +
        "            </div>\n" +
        "        </div>\n";
    html += "    </form>\n" +
        "</div>";
    layer.open({
        title: '数据修改'
        , type: 1
        //,skin: 'layui-layer-rim'
        , shadeClose: true
        , area: ["60%", "80%"]
        , content: html
        , end: function () {
            console.log("数据修改", list.newUpdata)
            obj.update(list.newUpdata);
        }
    });
    for (let i = 0; i < list.fields.length; i++) {
        if (list.update[i]) {
            if (list.types[i] == "date") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-update-page"]
                    , type: 'date'
                });
            } else if (list.types[i] == "time") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-update-page"]
                    , type: 'time'
                });
            } else if (list.types[i] == "datetime") {
                laydate.render({
                    elem: '#' + list.eleMapping[i + "-" + list.fields[i] + "-update-page"]
                    , type: 'datetime'
                });
            }
        }
    }
    form.render();
}
list.tableBut = function (data) {
    let html = "";
    for (let json of list.tableButArray) {
        html += list.addTableButton(json["title"], json["colour"], json["clickFun"]);
    }
    return html;
}
list.uploadEvent = function (inputId, butId, showId, msgId, api, successCallback) {
    let uploadInst = upload.render({
        elem: '#' + butId, url: api,
        before: function (obj) {
            obj.preview(function (index, file, result) {
                $('#' + showId).attr('src', result);
            });
        },
        done: function (res) {
            if (list.jsonVerify(res)) {
                if (res.data == null && res.data[0] == null) {
                    list.msgErr("上传失败，服务器返回数据为空");
                    return;
                }
                let url = null;
                if (url == null && res["data"] != null) {
                    url = res["data"]["src"];
                }
                if (url == null && res["data"] != null) {
                    url = res["data"]["path"];
                }
                if (url == null && res["data"] != null) {
                    url = res["data"]["url"];
                }
                if (url == null && res["data"][0] != null) {
                    url = res["data"][0]["url"];
                }
                if (url == null && res["data"][0] != null) {
                    url = res["data"][0]["path"];
                }
                if (url == null && res["data"][0] != null) {
                    url = res["data"][0]["url"];
                }
                let node = $("#" + inputId)
                if (node != null) {
                    node.val(url);
                }
                if (successCallback != null) {
                    successCallback(res)
                }
            }
        }
        , error: function () {
            let demoText = $('#' + msgId);
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadInst.upload();
            });
        }
    });
}

list.addUploadSingle = function (fid, id, title, buttonTitle, api, imageUrl, defVal, successCallback) {
    return list.addUpload(fid, id, buttonTitle, title, api, imageUrl, defVal, successCallback, "layui-form-item")
}
list.addUpload = function (fid, id, buttonTitle, title, api, imageUrl, defVal, successCallback, rowClass) {
    rowClass = rowClass == null ? "layui-inline" : rowClass; //layui-inline  layui-form-item
    let html = "<div class=\"" + rowClass + "\">\n" + (title == null ? "" : "<label class=\"layui-form-label\">" + title + "</label>\n") +
        "                    <div class=\"layui-input-block\">\n" +
        "                        <div class=\"layui-upload\">\n" +
        (api == null || successCallback == null ? "" : "<button type=\"button\" class=\"layui-btn layui-btn-primary layui-btn-radius\" id=\"but-" + id + "\">" + buttonTitle + "</button>\n") +
        "                            <div class=\"layui-upload-list\">\n" +
        "                                <img " + (imageUrl == null || imageUrl == "" ? "" : "src='" + imageUrl + "'") + " class=\"layui-upload-img\" id=\"show-" + id + "\" style='width: 100px;height: 100px;margin: 0 10px 10px 0;'>\n" +
        "                                <p id=\"msg-" + id + "\"></p>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>" +
        "<input id='" + id + "' type='hidden' value='" + (defVal == null ? "" : defVal) + "'>";
    if (fid != null) {
        $("#" + fid).append(html);
        list.uploadEvent(id, "but-" + id, "show-" + id, "msg-" + id, api, successCallback);
    } else {
        html += "<script>\n" +
            " let uploadInst = upload.render({elem: '#but-" + id + "', url: '" + api + "',\n" +
            "             auto: true, exts:'jpg|jpeg|png|bmp|gif|svg|tiff|tif|webp',\n" +
            "        before: function (obj) {\n" +
            "            obj.preview(function (index, file, result) {$('#show-" + id + "').attr('src', result);});\n" +
            "        },\n" +
            "        done: function (res) {\n" +
            "            if (list.jsonVerify(res)) {\n" +
            "                if (res.data == null && res.data[0]==null) {\n" +
            "                    list.msgErr(\"上传失败，服务器返回数据为空\");\n" +
            "                    return;\n" +
            "                }\n" +
            "                let url = null;\n" +
            "                if (url == null && res[\"data\"]!=null) {\n" +
            "                    url = res[\"data\"][\"src\"];\n" +
            "                }" +
            " if (url == null && res[\"data\"]!=null) {\n" +
            "                    url = res[\"data\"][\"path\"];\n" +
            "                }" +
            " if (url == null && res[\"data\"]!=null) {\n" +
            "                    url = res[\"data\"][\"url\"];\n" +
            "                }" +
            " if (url == null && res[\"data\"][0]!=null) {\n" +
            "                    url = res[\"data\"][0][\"url\"];\n" +
            "                }" +
            " if (url == null && res[\"data\"][0]!=null) {\n" +
            "                    url = res[\"data\"][0][\"path\"];\n" +
            "                }" +
            " if (url == null && res[\"data\"][0]!=null) {\n" +
            "                    url = res[\"data\"][0][\"url\"];\n" +
            "                }\n" +
            "                let node =$(\"#" + id + "\")\n" +
            "                if (node!=null) {\n" +
            "                    node.val(url);\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        , error: function () {\n" +
            "            let demoText = $('#msg-" + id + "');\n" +
            "            demoText.html('<span style=\"color: #FF5722;\">上传失败</span> <a class=\"layui-btn layui-btn-mini demo-reload\">重试</a>');\n" +
            "            demoText.find('.demo-reload').on('click', function () {\n" +
            "                uploadInst.upload();\n" +
            "            });\n" +
            "        }\n" +
            "    });\n" +
            "</script>";
    }
    return html;
}

list.addUploadFileSingle = function (fid, id, title, buttonTitle, api, defVal, successCallback) {
    return list.addUploadFile(fid, id, buttonTitle, title, api, defVal, successCallback, "layui-form-item")
}
list.addUploadFile = function (fid, id, buttonTitle, title, api, defVal, successCallback, rowClass) {
    console.log(fid, id, buttonTitle, title, api, defVal, successCallback, rowClass)
    rowClass = rowClass == null ? "layui-inline" : rowClass; //layui-inline  layui-form-item
    let html = "<div class=\"" + rowClass + "\">\n" + (title == null ? "" : "<label class=\"layui-form-label\">" + title + "</label>\n") +
        "                    <div class=\"layui-input-block\">\n" +
        "                        <div class=\"layui-upload\">\n" +
        ("<button type=\"button\" class=\"layui-btn layui-btn-primary layui-btn-radius\" id=\"but-" + id + "\">" + buttonTitle + "</button>\n") +
        "                            <div class=\"layui-upload-list\">\n" +
        "<input id='" + id + "' value='" + (defVal == null ? "" : defVal) + "' class='layui-input'>" +
        "                                <p id=\"msg-" + id + "\"></p>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>";
    html += "<script>\n" +
        "let uploadInst = upload.render({elem: '#but-" + id + "', url: '" + api + "'" +
        "            , accept: 'file'\n" +
        "            , done: function (res) {\n" +
        "            if (list.jsonVerify(res)) {\n" +
        "                if (res.data == null && res.data[0]==null) {\n" +
        "                    list.msgErr(\"上传失败，服务器返回数据为空\");\n" +
        "                    return;\n" +
        "                }\n" +
        "                let url = null;\n" +
        "                if (url == null && res[\"data\"]!=null) {\n" +
        "                    url = res[\"data\"][\"src\"];\n" +
        "                }" +
        " if (url == null && res[\"data\"]!=null) {\n" +
        "                    url = res[\"data\"][\"path\"];\n" +
        "                }" +
        " if (url == null && res[\"data\"]!=null) {\n" +
        "                    url = res[\"data\"][\"url\"];\n" +
        "                }" +
        " if (url == null && res[\"data\"][0]!=null) {\n" +
        "                    url = res[\"data\"][0][\"url\"];\n" +
        "                }" +
        " if (url == null && res[\"data\"][0]!=null) {\n" +
        "                    url = res[\"data\"][0][\"path\"];\n" +
        "                }" +
        " if (url == null && res[\"data\"][0]!=null) {\n" +
        "                    url = res[\"data\"][0][\"url\"];\n" +
        "                }\n" +
        "                let node =$(\"#" + id + "\")\n" +
        "                if (node!=null) {\n" +
        "                    node.val(url);\n" +
        "                }\n" +
        "            }\n" +
        "        }\n" +
        "        });" +
        "</script>";
    if (fid != null) {
        $("#" + fid).append(html);
    }
    return html;
}
list.addTableButton = function (title, colour, click) {
    colour = colour == null ? list.colourData["原始"] : colour
    if (list.colourData[colour] != null) {
        colour = list.colourData[colour];
    }
    let layEvent = list.getUUid();
    list.addTableButtonClickFun[layEvent] = click;
    return "<button lay-event='" + layEvent + "' class=\"layui-btn " + colour + " layui-btn-xs layui-btn-radius\">" + title + "</button>";
}
list.addSelect = function (fid, id, title, options, defVal, selectedEvent, width, disable) {
    width = width == null ? "200px" : width;
    let className = title == null ? "layui-inline" : "layui-input-block";
    let html = "<div class=\"layui-inline\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" style='width: " + width + ";'>\n" +
        "                                    <select " + (disable != null && disable == true ? "disable" : "") + " id=\"" + id + "\" lay-filter=\"" + id + "\" style='width: " + width + ";' lay-search=\"\">\n";
    if (options != null) {
        for (let i = 0; i < options.length; i++) {
            let selected = false;
            if (defVal == null) {
                if (i == 0) {
                    selected = true;
                }
            } else {
                if (options[i]["value"].toString() == defVal.toString()) {
                    selected = true;
                }
            }
            html += "<option value=\"" + options[i]["value"] + "\" " + (selected ? "selected" : "") + ">" + options[i]["title"] + "</option>";
        }
    }

    html += "                                    </select>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.on("select(" + id + ")", selectedEvent);
        if (defVal != null) {
            $("#" + id).val(defVal);
        }
        form.render();
    }
    return html;
}
list.addSelectSingle = function (fid, id, title, options, defVal, selectedEvent, disable) {
    defVal = defVal == null ? "" : defVal
    let html = "<div class=\"layui-form-item\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\">\n" +
        "                                    <select " + (disable != null && disable == true ? "disable" : "") + " id=\"" + id + "\" lay-filter=\"" + id + "\" lay-search=\"\">\n";
    if (options != null) {
        for (let i = 0; i < options.length; i++) {
            let selected = false;
            if (defVal == null || defVal.length == 0) {
                if (i == 0) {
                    selected = true;
                }
            } else {
                if (options[i]["value"].toString() == defVal.toString()) {
                    selected = true;
                }
            }
            console.log(id, defVal, selected, options[i])
            html += "<option value=\"" + options[i]["value"] + "\" " + (selected ? "selected" : "") + ">" + options[i]["title"] + "</option>";
        }
    }
    html += "                                    </select>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.on("select(" + id + ")", selectedEvent);
        form.render();
    }
    return html;
}
list.addTextarea = function (fid, id, title, defVal, desc, width, disable) {
    defVal = defVal == null ? "" : defVal
    width = width == null ? "200px" : width;
    let html = "<div class=\"layui-inline\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" style='width: " + width + ";'>\n" +
        "                        <textarea class=\"layui-textarea\"  " + (disable != null && disable == true ? "disable" : "") + " style='width: " + width + ";' id='" + id + "' " + (disable != null && disable == true ? "disable" : "") + " placeholder=\"" + desc + "\">" + defVal + "</textarea>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}
list.addTextareaSingle = function (fid, id, title, defVal, desc, disable) {
    defVal = defVal == null ? "" : defVal
    let html = "<div class=\"layui-form-item\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" >\n" +
        "                        <textarea class=\"layui-textarea\"  " + (disable != null && disable == true ? "disable" : "") + " id='" + id + "' " + (disable != null && disable == true ? "disable" : "") + " placeholder=\"" + desc + "\">" + defVal + "</textarea>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}
list.addInput = function (fid, id, title, defVal, desc, width, disable) {
    defVal = defVal == null ? "" : defVal
    if (defVal.toLowerCase() == "thisdate") {
        defVal = new Date().Format("yyyy-MM-dd")
    }
    if (defVal.toLowerCase() == "thistime") {
        defVal = new Date().Format("hh:mm:ss")
    }
    if (defVal.toLowerCase() == "thisdatetime") {
        defVal = new Date().Format("yyyy-MM-dd hh:mm:ss")
    }
    width = width == null ? "200px" : width;
    let html = "<div class=\"layui-inline\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" style='width: " + width + ";'>\n" +
        "                                    <input " + (disable != null && disable == true ? "disable" : "") + " style='width: " + width + ";' id='" + id + "' value='" + defVal + "' lay-com.verify=\"required\" placeholder=\"" + desc + "\" autocomplete=\"off\" class=\"layui-input\">\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}
list.addInputSingle = function (fid, id, title, defVal, desc, disable) {
    defVal = defVal == null ? "" : defVal
    if (defVal.toLowerCase() == "thisdate") {
        defVal = new Date().Format("yyyy-MM-dd")
    }
    if (defVal.toLowerCase() == "thistime") {
        defVal = new Date().Format("hh:mm:ss")
    }
    if (defVal.toLowerCase() == "thisdatetime") {
        defVal = new Date().Format("yyyy-MM-dd hh:mm:ss")
    }
    console.log(defVal)
    let html = "<div class=\"layui-form-item\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" >\n" +
        "                                    <input " + (disable != null && disable == true ? "disable" : "") + " value='" + defVal + "' id='" + id + "' lay-com.verify=\"required\" placeholder=\"" + desc + "\" autocomplete=\"off\" class=\"layui-input\">\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}
list.addButton = function (fid, id, title, colour, click) {
    id = id == null ? getUUid() : id;
    colour = colour == null ? list.colourData["原始"] : colour
    if (list.colourData[colour] != null) {
        colour = list.colourData[colour];
    }
    let html = "<button class=\"layui-btn " + colour + " layui-btn-radius\" type='button' id='" + id + "'>" + title + "</button>";
    if (fid != null) {
        if (fid.substring(0, 1) == "#") {
            $(fid).append(html);
            $(id).click(click);
        } else {
            $("#" + fid).append(html);
            $("#" + id).click(click);
        }
        form.render();
    }
    return html;
}
list.addButtonSingle = function (fid, id, title, colour, click) {
    id = id == null ? getUUid() : id;
    colour = colour == null ? list.colourData["原始"] : colour
    if (list.colourData[colour] != null) {
        colour = list.colourData[colour];
    }
    let html = "<div class=\"layui-form-item\">\n<label class=\"layui-form-label\">确认操作</label>" +
        "<button class=\"layui-btn " + colour + " layui-btn-radius\" type='button' id='" + id + "'>" + title + "</button>" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        $("#" + id).click(click);
        form.render();
    }
    return html;
}
list.addTableImage = function (url, height) {
    if ($("#table-image-height").text() == "") {
        height = height == null ? 30 : height
        //$("head").append("<style id='table-image-height'>td > .layui-table-cell{height: "+height+"px;} </style>")
    }
    return "<img style='width: auto;max-height: 100%;' src='" + url + "' onclick='layer.photos({photos: {\"code\": 0,\"msg\": \"大图\",\"title\": \"大图\",\"id\": 1,\"start\": 0,\"data\": [{\"alt\": \"大图预览\",\"pid\": 1,\"src\": \"" + url + "\",\"thumb\": \"\"}]}});' />";
}
list.openPages = function (obj, data, url, title, w, h, end) {
    console.log(url)
    if (end == null) {
        end = function () {

            if (localStorage["update-page-ok"] == "1") {
                localStorage["update-page-ok"] = "0"
                table.reload("tableId001");
            }
        }
    }
    w = w == null ? 80 : w;
    h = h == null ? 90 : h;
    title = title == null ? "弹出页" : title
    if (url.toString().indexOf("?") == -1 && url.toString().substring(0, url.toString().length - 1) != "?") {
        url += "?";
    }
    if (data != null) {
        for (let key in data) {
            if (url.toString().substring(0, url.toString().length - 1) == "&") {
                url += key + "=" + data[key];
            } else {
                url += "&" + key + "=" + data[key];
            }
        }
    }
    layer.open({
        type: 2,
        title: title,
        shadeClose: true,
        shade: 0.5,
        area: [w + '%', h + '%'],
        content: url,
        end: end
    });
}

list.convertState = function (selects, dataVal, field) {
    dataVal = dataVal == null ? "" : dataVal;
    let data = "";
    if (dataVal == null || dataVal == "") {
        return "";
    }
    for (let i in selects) {
        if (selects[i]["value"].toString() == dataVal.toString()) {
            data = selects[i]["title"].toString()
        }
    }
    return data;
}
list.getUUid = function () {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}
list.msgSuccess = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 1, time: 2000}, fun);
}
list.msgErr = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 2, time: 2000}, fun);
}
list.msgDoubt = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 3, time: 2000}, fun);
}
list.jsonVerify = function (json, successMsg, sucFun, failFun, errFun, jumpFun) {
    if (json == null) {
        list.msgErr("访问服务器失败，请稍后重试[1001]");
        return false;
    }
    let status = null;
    if (status == null) {
        status = json["state"];
    }
    if (status == null) {
        status = json["status"];
    }
    if (status == null) {
        status = json["code"];
    }
    if (status == null) {
        list.msgErr("访问服务器失败，请稍后重试[1002]");
        return false;
    }
    let msg = null;
    if (msg == null) {
        msg = json["message"];
    }
    if (msg == null) {
        msg = json["msg"];
    }
    let url = null;
    if (url == null) {
        url = json["jump"];
    }
    if (url == null) {
        url = json["path"];
    }
    if (url == null) {
        url = json["url"];
    }
    if (status == list.status["success"]) {
        if (successMsg) {
            list.msgSuccess(msg);
        }
        if (sucFun != null) {
            sucFun();
        }
        return true;
    }
    if (status == list.status["fail"]) {
        list.msgErr(msg);
        if (failFun != null) {
            failFun();
        }
        return false;
    }
    if (status == list.status["err"]) {
        list.msgErr(msg);
        if (errFun != null) {
            errFun();
        }
        return false;
    }
    if (status == list.status["jump"]) {
        list.msgErr(msg, function () {
            localStorage["主页跳转"] = url;
        });
        if (jumpFun != null) {
            jumpFun();
        }
        return false;
    }
    list.msgErr("服务器响应异常，请稍后重试");
    return false;
};
list.request = function (url, met, data, suc, err, async) {
    let requestData = {};
    if (data == null) {
        data = {};
    }
    for (let key in list.requestData) {
        requestData[key] = list.requestData[key]
    }
    for (let key in data) {
        requestData[key] = data[key]
    }
    if (met == null) {
        met = "GET";
    }
    if (async == null) {
        async = true;
    }
    let suc0 = function (res) {
        try {
            res = JSON.parse(res);
        } catch (e) {
        }
        try {
            suc(res)
        } catch (e) {
            console.log(e)
        }
    }
    $.ajax({
        url: url,
        data: data,
        type: met,
        ansy: async,
        success: suc0,
        error: err
    });
};
list.get = function (url, data, suc, err) {
    list.request(url, "GET", data, suc, err, true);
}
list.post = function (url, data, suc, err) {
    list.request(url, "POST", data, suc, err, true);
}
list.put = function (url, data, suc, err) {
    list.request(url, "PUT", data, suc, err, true);
}
list.delete = function (url, data, suc, err) {
    list.request(url, "DELETE", data, suc, err, true);
}
list.base64Encode = function (str) {
    return Base64.encode(str).replace(/\+/g, '-').replace(/\=/g, '_');
}
list.base64Decode = function (str) {
    return Base64.decode(str).replace(/-/g, '+').replace(/_/g, '=');
}
list.getParas = function () {
    let retArr = {}
    let url = location.href;
    let ss1 = url.split("?", 2);
    if (ss1.length < 2) {
        return null;
    }
    let p = ss1[1];
    let ss2 = p.split("&");
    for (let ss2_1 of ss2) {
        let ss3 = ss2_1.split("=", 2);
        if (ss3.length == 2) {
            retArr[ss3[0]] = ss3[1]
        }
    }
    return retArr
}

function appendNode(data) {
    let html = "";
    let i = 0;
    for (let key in data) {
        let json = data[key]
        html += "<li class=\"layui-nav-item " + (i == 0 ? "layui-nav-itemed" : "") + "\" data-name='" + key + "'>" +
            " <a href=\"javascript:;\" lay-tips=\"" + key + "\" lay-direction=\"2\">" +
            " <i class=\"layui-icon\"></i>" +
            " <cite>" + key + "</cite>" +
            " <span class=\"layui-nav-more\"></span>" +
            " </a>" +
            " <dl class=\"layui-nav-child\">";
        for (let list of json) {
            if (i == 0) {
                html += "<dd class=\"layui-this\">" +
                    "<a lay-href=\"" + list.frameAuthorityPath + "\">" + list.frameAuthorityName + "</a>" +
                    "</dd>"
            } else {
                html += "<dd >" +
                    "<a lay-href=\"" + list.frameAuthorityPath + "\">" + list.frameAuthorityName + "</a>" +
                    "</dd>"
            }
            i++;
        }
        html += " </dl>" +
            " </li>"
    }
    $("#LAY-system-side-menu").html(html);
    element.init();
}
