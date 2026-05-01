let suc="1";
let fail="2";
let err="3";
let jump="4";
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
let data={};

function openPages(obj, data, url,title,w,h,end) {
    if (end==null) {
        end=function () {
            if (clz.localStorageGet("update-page-ok") == "1") {
                clz.localStorageSet("update-page-ok", "0")
                table.reload("tableId001");
            }
        }
    }
    w=w==null?80:w;
    h=h==null?90:h;
    title=title==null?"弹出页":title
    console.log("openPages:" + JSON.stringify(data))
    if (url.toString().indexOf("?") == -1 && url.toString().substring(0,url.toString().length-1) != "?") {
        url+="?";
    }
    if (data!=null) {
        for (let key in data){
            if (url.toString().substring(0,url.toString().length-1) == "&") {
                url+=key+"="+data[key];
            }else{
                url+="&"+key+"="+data[key];
            }
        }
    }
    openPage(url , title, w, h,end );
}

function openPage(url,title,w,h,endFun) {
    layer.open({
        type: 2,
        title: title,
        shadeClose: true,
        shade: 0.5,
        area: [w+'%', h+'%'],
        content: url
        , end:endFun
    });
}
function getSelectDataPublic(api,met,data,titleField,valueField){
    if (api==null || valueField==null || titleField==null) {
        return null;
    }
    met=met==null ? "get" : met;
    data=data==null ? "" : data;
    if (api.toString().indexOf("?") == -1 && api.toString().substring(0,api.toString().length-1) != "?") {
        api+="?";
    }
    if (met.toString().toLowerCase()  == "get") {
        if (typeof data == "string") {
            if (api.toString().indexOf("?") >-1) {
                if (api.toString().substring(0,api.toString().length-1) == "&") {
                    api+=data;
                }else{
                    api+="&"+data;
                }
            }
        }else{
            for (let key in data){
                if (api.toString().substring(0,api.toString().length-1) == "&") {
                    api+=key+"="+data[key]+"&";
                }else{
                    api+="&"+key+"="+data[key]+"&";
                }
            }
        }
    }
    if (api.toString().substring(0,api.toString().length-1) == "&") {
        api+="page=1&limit=1000&";
    }else{
        api+="&page=1&limit=1000&";
    }

    let res=request(api,met,data,false);
    let arr=[["请选择",""]];
    for (let data of res.data){
        arr[arr.length] = [data[titleField],data[valueField]]
    }
    return arr;
}
function request(url,met,data,async) {
    met=met==null ?"get":met;
    data=data==null ?"":data;
    async=async==null ?false:async;
    const xhr = new XMLHttpRequest();
    xhr.open(met, url, async); // 第三个参数为是否开启异步请求
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(data);
    const result = JSON.parse(xhr.responseText);
    console.log(result)
    return result;
}
function getUUid() {
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
let colourData = {
    "红色": "layui-btn-danger",
    "黄色": "layui-btn-warm",
    "蓝色": "layui-btn-normal",
    "原始": "layui-btn-primary",
    "禁用": "layui-btn-disabled",
};
let addTableButtonClickFun = {};
let addTableCheckboxClickFun = {};

function inputEve(type, field, title, defValue, width, selectArr, event, disabled,uploadApi, imageTemplate, uploadCallBack,sign) {
    defValue=defValue==null ? "":defValue
    selectArr = selectArr == null ? {} : selectArr;
    if (sign==null) {
        sign=true;
    }
    if (type == "input") {
        if (sign) {
            addInputSingle("from-1", field, title, defValue, title);
        }else{
            addInput("from-1", field, title, defValue, title, width);
        }
    } else if (type == "select") {
        if (sign) {
            addSelectSingle("from-1", field, title, selectArr, defValue, event);
        }else{
            addSelect("from-1", field, title, selectArr, defValue, event, width);
        }

    } else if (type.toString().toLowerCase() == "datetime") {
        if (sign) {
            addInputSingle("from-1", field, title, defValue, title);
        }else{
            addInput("from-1", field, title, defValue, title, width);
        }
        laydate.render({
            elem: '#'+field ,type: 'datetime'
        });
    } else if (type == "time") {
        if (sign) {
            addInputSingle("from-1", field, title, defValue, title);
        }else{
            addInput("from-1", field, title, defValue, title, width);
        }
        laydate.render({
            elem: '#'+field ,type: 'time'
        });
    } else if (type == "date") {
        if (sign) {
            addInputSingle("from-1", field, title, defValue, title);
        }else{
            addInput("from-1", field, title, defValue, title, width);
        }
        laydate.render({
            elem: '#'+field ,type: 'date'
        });
    } else if (type == "image") {
        if (imageTemplate == null || imageTemplate == "") {
            imageTemplate = "${" + field + "}"
        }
        let val = defValue
        let url = imageTemplate.replace("${" + field + "}", val);
        if (val == null || val == "") {
            url = "";
        }
        if (sign) {
            addUploadSingle("from-1", field, title, "选择文件", uploadApi,
                url, val, uploadCallBack)
        }else{
            addUpload("from-1", field, title, "选择文件",uploadApi,
                url, val, uploadCallBack)
        }

    } else {
        console.log("type err : " + type)
    }

    if (disabled) {
        $("#" + field).attr("disabled", "disabled");
    }

}
let convertState = function (state, title, dataVal) {
    dataVal=dataVal==null?"":dataVal;
    if (dataVal == null || dataVal == "") {
        return "";
    }
    for (let i = 0; i < state.length; i++) {
        if (state[i].toString() == dataVal.toString()) {
            return title[i];
        }
    }
    return dataVal;
}
function getTableData() {//获取已选中的数据 json
    return table.checkStatus('tableId001').data;
}

function getTableIsAll() {//是否全选  true是  false否
    return table.checkStatus('tableId001').isAll;
}

function addTableImage(url,height) {
    if ($("#table-image-height").text()=="") {
        height=height==null?30:height
        //$("head").append("<style id='table-image-height'>td > .layui-table-cell{height: "+height+"px;} </style>")
    }
    return "<img style='width: auto;max-height: 100%;' src='" + url + "' onclick='layer.photos({photos: {\"code\": 0,\"msg\": \"大图\",\"title\": \"大图\",\"id\": 1,\"start\": 0,\"data\": [{\"alt\": \"大图预览\",\"pid\": 1,\"src\": \"" + url + "\",\"thumb\": \"\"}]}});' />";
}

function addTableCheckbox(selection,text,layEvent,data,click) {
    addTableCheckboxClickFun[layEvent]=click
    return "<input lay-filter='tableCheckbox' data-data='"+data+"' data-eve='"+layEvent+"' type=\"checkbox\" lay-skin=\"switch\" lay-text=\"" +text+ "\" " + (selection == null || selection != true ? "" : "checked") + ">";
}
function addTableButton( title, colour, click) {
    let layEvent =getUUid();
    colour = colour == null ? colourData["原始"] : colour;
    addTableButtonClickFun[layEvent] = click;
    return "<button lay-event='" + layEvent + "' class=\"layui-btn " + colour + " layui-btn-xs layui-btn-radius\">" + title + "</button>";
}


function addUploadSingle(fid,id, title, buttonTitle, api,imageUrl,defVal,successCallback) {
    addUpload(fid,id, buttonTitle, title, api,imageUrl,defVal,successCallback,"layui-form-item")
}
function addUpload(fid,id,buttonTitle,title, api,imageUrl,defVal,successCallback,rowClass) {
    rowClass=rowClass==null? "layui-inline" : rowClass //layui-inline  layui-form-item
    let html = "<div class=\""+rowClass+"\">\n" +(title==null?"":"<label class=\"layui-form-label\">"+title+"</label>\n")+
        "                    <div class=\"layui-input-block\">\n" +
        "                        <div class=\"layui-upload\">\n" +
        (api==null || successCallback==null ?"":"                            <button type=\"button\" class=\"layui-btn layui-btn-primary layui-btn-radius\" id=\"uploadImage-"+id+"\">"+buttonTitle+"</button>\n") +
        "                            <div class=\"layui-upload-list\">\n" +
        "                                <img "+(imageUrl==null||imageUrl==""?"":"src='"+imageUrl+"'")+" class=\"layui-upload-img\" id=\"uploadImageShow-"+id+"\" style='width: 100px;height: 100px;margin: 0 10px 10px 0;'>\n" +
        "                                <p id=\"test-upload-demoText-"+id+"\"></p>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>" +
        "<input id='"+id+"' type='hidden' value='"+defVal+"'>";
    if (fid != null) {
        $("#" + fid).append(html);

        if (successCallback!=null) {
            let uploadInst = upload.render({
                elem: '#uploadImage-'+id
                , url: api
                , before: function (obj) {
                    obj.preview(function (index, file, result) {
                        $('#uploadImageShow-'+id).attr('src', result); //图片链接（base64）
                    });
                }
                , done: function (res) {
                    if (clz.jsonVerify(res)) {
                        let url=res.data.url;
                        if (url==null) {
                            url=res.data.src;
                        }
                        if (url==null && res.data[0]!=null) {
                            url=res.data[0].src;
                        }
                        if (url==null && res.data[0]!=null) {
                            url=res.data[0].url;
                        }
                        $("#" + id).val(url);
                        if (successCallback!=null) {
                            successCallback(res)
                        }
                    }
                }
                , error: function () {
                    let demoText = $('#test-upload-demoText-'+id);
                    demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            });
        }
        form.render();
    }
    return html;
}
function addButton(fid, id, title, colour, click) {
    id=id==null?getUUid():id;
    colour = colour == null ? colourData["原始"] : colour
    if (colourData[colour]!=null) {
        colour=colourData[colour];
    }
    let html = "<button class=\"layui-btn " + colour + " layui-btn-radius\" type='button' id='" + id + "'>" + title + "</button>";
    if (fid != null) {
        $("#" + fid).append(html);
        $("#" + id).click(click);
        form.render();
    }
    return html;
}
//layui-form-item
function addButtonSingle (fid, id, title, colour, click) {
    id=id==null?getUUid():id;
    colour = colour == null ? colourData["原始"] : colour
    if (colourData[colour]!=null) {
        colour=colourData[colour];
    }
    let html = "<div class=\"layui-form-item\">\n<label class=\"layui-form-label\">确认操作</label>" +
        "<button class=\"layui-btn " + colour + " layui-btn-radius\" type='button' id='" + id + "'>" + title + "</button>"+
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        $("#" + id).click(click);
        form.render();
    }
    return html;
}
function addInput(fid, id, title, defVal, desc,width) {
    defVal=defVal==null?"":defVal
    width=width==null?"200px":width;
    let html = "<div class=\"layui-inline\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" style='width: "+width+";'>\n" +
        "                                    <input style='width: "+width+";' id='"+id+"' value='"+defVal+"' lay-com.verify=\"required\" placeholder=\"" + desc + "\" autocomplete=\"off\" class=\"layui-input\">\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}
function addInputSingle (fid, id, title, defVal, desc) {
    defVal=defVal==null?"":defVal
    let html = "<div class=\"layui-form-item\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" >\n" +
        "                                    <input value='"+defVal+"' id='"+id+"' lay-verify=\"required\" placeholder=\"" + desc + "\" autocomplete=\"off\" class=\"layui-input\">\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.render();
    }
    return html;
}

//id=xxxId
//options=[["普通员工","1001"],["经理","1001"]]
//selected=selected(data)
function addSelect(fid, id, title, options,defVal,selected,width) {
    width=width==null?"200px":width;
    let className=title == null? "layui-inline" : "layui-input-block";
    let html = "<div class=\"layui-inline\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\" style='width: "+width+";'>\n" +
        "                                    <select  id=\"" + id + "\" lay-filter=\"" + id + "\" style='width: "+width+";'>\n";
    for (let i = 0; i < options.length; i++) {
        html += "<option value=\"" + options[i][1] + "\" " + (i == 0 ? "selected" : "") + ">" + options[i][0] + "</option>";
    }
    html += "                                    </select>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.on("select(" + id + ")", selected);
        if (defVal!=null) {
            $("#" + id).val(defVal);
        }
        form.render();
    }
    return html;
}
function addSelectSingle(fid, id, title, options,defVal, selected) {
    defVal=defVal==null?"":defVal
    let html = "<div class=\"layui-form-item\">\n" +
        (title == null ? "" : "                                <label class=\"layui-form-label\">" + title + "</label>\n") +
        "                                <div class=\"layui-input-block\">\n" +
        "                                    <select  id=\"" + id + "\" lay-filter=\"" + id + "\">\n";
    for (let i = 0; i < options.length; i++) {
        html += "<option value=\"" + options[i][1] + "\" " + (i == 0 ? "selected" : "") + ">" + options[i][0] + "</option>";
    }
    html += "                                    </select>\n" +
        "                                </div>\n" +
        "                            </div>";
    if (fid != null) {
        $("#" + fid).append(html);
        form.on("select(" + id + ")", selected);
        $("#" + id).val(defVal);
        form.render();
    }
    return html;
}


function apiNotList(api,data,trueFun){
    clz.post(api,data,function (res) {
        if (clz.jsonVerify(res,true)) {
            if (trueFun) {
                trueFun(res);
            }
        }
    },function (res) {
        clz.msgErr("连接服务器失败！")
    },true);
}
let clz = new Object();
clz.rootUrl = "../";

clz.textMid = function (str, q, h, index) {
    let a = str.indexOf(q);
    if (a < 0) {
        return null;
    }
    a = a + q.length;
    let b = str.indexOf(h, a);
    console.log(a)
    console.log(b)
    console.log(str.substring(a, b))
}
clz.msgSuccess = function (msgStr, fun) {
    console.log(msgStr)
    layer.msg(msgStr, {offset: '10px', icon: 1, time: 1000}, fun);
}
clz.msgErr = function (msgStr, fun) {
    console.log(msgStr)
    layer.msg(msgStr, {offset: '10px', icon: 2, time: 1000}, fun);
}
clz.msgDoubt = function (msgStr, fun) {
    console.log(msgStr)
    layer.msg(msgStr, {offset: '10px', icon: 3, time: 1000}, fun);
}

clz.jsonVerify = function (json, successMsg, sucFun, failFun, errFun, jumpFun) {
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
        clz.msgErr("访问服务器失败，请稍后重试");
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
    if (status == clz.status["success"]) {
        if (successMsg) {
            clz.msgSuccess(msg);
        }
        if (sucFun != null) {
            sucFun();
        }
        return true;
    }
    if (status == clz.status["fail"]) {
        clz.msgErr(msg);
        if (failFun != null) {
            failFun();
        }
        return false;
    }
    if (status == clz.status["err"]) {
        clz.msgErr(msg);
        if (errFun != null) {
            errFun();
        }
        return false;
    }
    if (status == clz.status["jump"]) {
        clz.msgErr(msg, function () {
            location.href = url;
        });
        if (jumpFun != null) {
            jumpFun();
        }
        return false;
    }
    clz.msgErr("服务器响应异常，请稍后重试");
    return false;
};
clz.jsonVerify = function (json, successMsg,sucFun,failFun,errFun,jumpFun) {
    let type=0;
    let status=json.code
    suc="1";
    fail="2";
    err="3";
    jump="4";
    if (status==null) {
        status=json.status
    }
    if (status==null) {
        status=json.state
    }
    let msg=json.message
    let url=json.jump;
    url=url==null?json.url:url;
    url=url==null?json.path:url;
    console.log(status)
    console.log(msg)
    if (status == suc) {
        if (successMsg) {
            clz.msgSuccess(msg);
        }
        if (sucFun!=null) {
            sucFun();
        }
        return true;
    }
    if (status == fail) {
        clz.msgErr(msg);
        if (failFun!=null) {
            failFun();
        }
        return false;
    }
    if (status == err) {
        clz.msgErr(msg);
        if (errFun!=null) {
            errFun();
        }
        return false;
    }
    if (status == jump) {
        clz.msgErr(msg, function () {
            location.href =url;
        });
        if (jumpFun!=null) {
            jumpFun();
        }
        return false;
    }
    clz.msgErr("服务器响应异常，请稍后重试");
    return false;
};
/*

clz.jsonVerify = function (json, successMsg,sucFun,failFun,errFun,jumpFun) {
    let type=0;
    let status;
    if (json.state != null) {
          suc="1";
          fail="2";
          err="3";
          jump="4";
        status=json.state
    }else if (json.status != null) {
        suc="1";
        fail="2";
        err="3";
        jump="4";
        type=0;
        status=json.status
    }else{
        clz.msgErr("访问服务器失败，请稍后重试");
        return false;
    }
    let msg=json.msg;
    let url=json.jump;
    msg = json.msg == null ? json.message : msg
    url=url==null?json.url:url;
    url=url==null?json.path:url;
    if (status == suc) {
        if (successMsg) {
            clz.msgSuccess(msg);
        }
        if (sucFun!=null) {
            sucFun();
        }
        return true;
    }
    if (status == fail) {
        clz.msgErr(msg);
        if (failFun!=null) {
            failFun();
        }
        return false;
    }
    if (status == err) {
        clz.msgErr(msg);
        if (errFun!=null) {
            errFun();
        }
        return false;
    }
    if (status == jump) {
        clz.msgErr(msg, function () {
            location.href =url;
        });
        if (jumpFun!=null) {
            jumpFun();
        }
        return false;
    }
    clz.msgErr("服务器响应异常，请稍后重试");
    return false;
};
*/


clz.request=function(url,met,data,suc,err,async){
    met=met==null?"GET":met
    async=async==null?true:async
    let suc0=function(res){
        try {
            res=JSON.parse(res);
        }catch (e) {

        }
        suc(res)
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
clz.get=function(url,data,suc,err){
    clz.request(url,"GET",data,suc,err,true);
}
clz.post=function(url,data,suc,err){
    clz.request(url,"POST",data,suc,err,true);
}
clz.put=function(url,data,suc,err){
    clz.request(url,"PUT",data,suc,err,true);
}
clz.delete=function(url,data,suc,err){
    clz.request(url,"DELETE",data,suc,err,true);
}
clz.base64Encode = function (str) {
    return Base64.encode(str).replace(/\+/g, '-').replace(/\=/g, '_');
}
clz.base64Decode = function (str) {
    return Base64.decode(str).replace(/\+/g, '-').replace(/\=/g, '_');
}

clz.getPara = function (key) {
    let url = location.href;
    let ss1 = url.split("?", 2);
    if (ss1.length < 2) {
        return null;
    }
    let p = ss1[1];
    let ss2 = p.split("&");
    for (let ss2_1 of ss2) {
        let ss3 = ss2_1.split("=", 2);
        if (ss3[0] == key) {
            return ss3[1]
        }
    }
}
clz.getParaArr = function (key) {
    let res = [];
    let res_i = 0;
    let url = location.href;
    let ss1 = url.split("?", 2);
    if (ss1.length < 2) {
        return null;
    }
    let p = ss1[1];
    let ss2 = p.split("&");
    for (let ss2_1 of ss2) {
        let ss3 = ss2_1.split("=", 2);
        if (ss3[0] == key) {
            res[res_i] = ss3[1];
            res_i++;
        }
    }
    return res;
}

clz.cookieSet = function (name, value) {
    let Days = 30;
    let exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";
}
clz.cookieGet = function (name) {
    let arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg)) return unescape(arr[2]);
    else return null;
}
clz.cookieDelete = function (name) {
    let exp = new Date();
    exp.setTime(exp.getTime() - 1);
    let cval = getCookie(name);
    if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}
clz.localStorageSet = function (name, value) {
    localStorage[name] = value;
}
clz.localStorageGet = function (name) {
    return localStorage[name];
}

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

//监听表格复选框选择
table.on('checkbox(tableId001)', function (obj) {
    console.log(obj)
});
//监听工具条
table.on('tool(tableId001)', function (obj) {
    let data = obj.data;
    for (let key in addTableButtonClickFun) {
        if (key.toString() == obj.event.toString()) {
            addTableButtonClickFun[key](obj, data);
        }
    }
});

form.on('switch(tableCheckbox)', function (data) {
    for (let key in addTableCheckboxClickFun) {
        if (key.toString() == $(this).attr("data-eve")) {
            addTableCheckboxClickFun[key](this.checked,$(this).attr("data-data"),data);
        }
    }
});

