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
let gzb = {};
gzb.status = {"success": "1", "fail": "2", "err": "3", "jump": "4"}

gzb.timeMap={}
gzb.tableHeight = 0;
gzb.events = {}
gzb.table = {
    eleId: "table_001",
    cols: [],
    mapping: {},
    "code": -1,
    "msg": null,
    "count": -1,
    "data": [],
    "limit": 50,
    "limits": [20, 50, 100, 200, 500, 1000]
    , "button": []
}
gzb.button = {
    "colour": {
        "白色": 'layui-btn-primary',
        "蓝色": 'layui-btn-normal',
        "红色": 'layui-btn-danger',
    }
}

gzb.openPage = function (url, title, endFun,w,h) {
    if (w == null) {
        w="80%"
    }
    if (h == null) {
        h="80%"
    }
    layer.open({
        type: 2,
        title: title,
        shadeClose: true,
        shade: 0.5,
        area: [w, h],
        content: url,
        end: endFun
    });
}
gzb.appendNode = function (data) {
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
gzb.loadScript = function (url) {
    console.log("url", url)
    let data = httpGet(url);
    console.log("data", data)
    eval(data);
}
gzb.getUUid = function () {
    const s = [];
    const hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    return s.join("");
}
gzb.msgSuccess = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 1, time: 2000}, fun);
}
gzb.msgErr = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 2, time: 2000}, fun);
}
gzb.msgDoubt = function (msgStr, fun) {
    layer.msg(msgStr, {offset: '10px', icon: 3, time: 2000}, fun);
}
gzb.jsonVerify = function (json, successMsg, sucFun, failFun, errFun, jumpFun) {
    if (json == null) {
        gzb.msgErr("访问服务器失败，请稍后重试[1001]");
        return false;
    }
    let status = null;
    status = json["state"];
    if (status == null) {
        status = json["status"];
    }
    if (status == null) {
        status = json["code"];
    }
    if (status == null) {
        gzb.msgErr("访问服务器失败，请稍后重试[1002]");
        return false;
    }
    let msg = null;
    msg = json["message"];
    if (msg == null) {
        msg = json["msg"];
    }
    let url = null;
    url = json["jump"];
    if (url == null) {
        url = json["path"];
    }
    if (url == null) {
        url = json["url"];
    }
    if (status === gzb.status["success"]) {
        if (successMsg) {
            gzb.msgSuccess(msg);
        }
        if (sucFun != null) {
            sucFun();
        }
        return true;
    }
    if (status === gzb.status["fail"]) {
        gzb.msgErr(msg);
        if (failFun != null) {
            failFun();
        }
        return false;
    }
    if (status === gzb.status["err"]) {
        gzb.msgErr(msg);
        if (errFun != null) {
            errFun();
        }
        return false;
    }
    if (status === gzb.status["jump"]) {
        gzb.msgErr(msg, function () {
            localStorage["主页跳转"] = url;
        });
        if (jumpFun != null) {
            jumpFun();
        }
        return false;
    }
    gzb.msgErr("服务器响应异常，请稍后重试");
    return false;
};
/*gzb.request = function (url, met, data, suc, err, async) {
    let requestData = {};
    if (data == null) {
        data = {};
    }
    for (let key in gzb.requestData) {
        requestData[key] = gzb.requestData[key]
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
        data: requestData,
        type: met,
        async: async,
        success: suc0,
        error: err
    });
};
gzb.get = function (url, data, suc, err) {
    gzb.request(url, "GET", data, suc, err, true);
}
gzb.post = function (url, data, suc, err) {
    gzb.request(url, "POST", data, suc, err, true);
}
gzb.put = function (url, data, suc, err) {
    gzb.request(url, "PUT", data, suc, err, true);
}
gzb.delete = function (url, data, suc, err) {
    gzb.request(url, "DELETE", data, suc, err, true);
}*/
gzb.request = async function (url, met, data, sucFun, errFun) {
    try {
        // 处理数据格式
        let processedData = '';
        let requestUrl = url;

        // 判断 data 是否为 xx=1&xx=2 格式的字符串
        const isFormUrlEncoded = typeof data === 'string' && /^[^=&]+=[^=&]+(&[^=&]+=[^=&]+)*$/.test(data);

        if (isFormUrlEncoded) {
            processedData = data;
        } else if (typeof data === 'object' && data !== null) {
            // 处理对象和数组
            const params = new URLSearchParams();

            const processValue = (key, value) => {
                if (Array.isArray(value)) {
                    value.forEach((item, index) => {
                        processValue(`${key}[${index}]`, item);
                    });
                } else if (typeof value === 'object' && value !== null) {
                    Object.entries(value).forEach(([subKey, subValue]) => {
                        processValue(`${key}[${subKey}]`, subValue);
                    });
                } else {
                    params.append(key, value);
                }
            };

            Object.entries(data).forEach(([key, value]) => {
                processValue(key, value);
            });

            processedData = params.toString();
        } else {
            // 其他类型数据直接转换为字符串
            processedData = String(data);
        }

        // 处理请求方法
        const method = met.toUpperCase();

        if (method === 'GET') {
            // GET 请求将数据添加到 URL
            requestUrl += (url.includes('?') ? '&' : '?') + processedData;
        }

        // 构建请求配置
        const options = {
            method,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        };

        // 非 GET 请求添加 body
        if (method !== 'GET') {
            options.body = processedData;
        }

        // 发送请求
        const response = await fetch(requestUrl, options);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.text();
        // 调用成功回调
        if (typeof sucFun === 'function') {
            let data = null;
            try {
                data = JSON.parse(result)
            } catch (e) {
                data = result
            }
            sucFun(data);
        }

        return result;
    } catch (error) {
        // 调用错误回调
        if (typeof errFun === 'function') {
            errFun(error);
        }

        throw error;
    }
};

// 同步版本的 request 函数（使用 XMLHttpRequest）
gzb.requestSync = function (url, met, data, suc, err) {
    let requestData = {};

    // 合并全局请求数据和局部数据
    if (typeof gzb.requestData === 'object') {
        for (let key in gzb.requestData) {
            requestData[key] = gzb.requestData[key];
        }
    }

    if (data == null) {
        data = {};
    }

    for (let key in data) {
        requestData[key] = data[key];
    }

    if (met == null) {
        met = "GET";
    }

    const xhr = new XMLHttpRequest();
    xhr.open(met, url, false); // 第三个参数为 false 表示同步请求
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            let res = xhr.responseText;

            // 尝试解析 JSON
            try {
                res = JSON.parse(res);
            } catch (e) {
                // 解析失败，使用原始文本
            }

            // 调用成功回调
            if (typeof suc === 'function') {
                suc(res);
            }
        } else {
            // 处理 HTTP 错误
            if (typeof err === 'function') {
                err({
                    status: xhr.status,
                    statusText: xhr.statusText,
                    response: xhr.responseText
                });
            }
        }
    };

    xhr.onerror = function () {
        // 处理网络错误
        if (typeof err === 'function') {
            err({
                status: xhr.status,
                statusText: xhr.statusText,
                response: xhr.responseText
            });
        }
    };

    // 处理请求体（非 GET 请求）
    try {
        const requestBody = (met !== 'GET' && met !== 'HEAD')
            ? JSON.stringify(requestData)
            : null;

        xhr.send(requestBody);
        return xhr.responseText; // 直接返回响应
    } catch (error) {
        console.error('同步请求错误:', error);
        if (typeof err === 'function') {
            err(error);
        }
        return null;
    }
};

// 重新定义 HTTP 方法快捷函数
gzb.get = function (url, data, suc, err) {
    return gzb.request(url, "GET", data, suc, err, true);
};

gzb.post = function (url, data, suc, err) {
    return gzb.request(url, "POST", data, suc, err, true);
};

gzb.put = function (url, data, suc, err) {
    return gzb.request(url, "PUT", data, suc, err, true);
};

gzb.delete = function (url, data, suc, err) {
    return gzb.request(url, "DELETE", data, suc, err, true);
};
gzb.low_hump = function (str, up) {
    if (str == null) {
        return str;
    }
    let arr1 = str.split("_");
    let str2 = "";
    for (let i = 0; i < arr1.length; i++) {
        let str3 = arr1[i];
        if (i === 0) {
            if (up) {
                str2 += str3.substring(0, 1).toUpperCase() + str3.substring(1)
            } else {
                str2 += str3.substring(0, 1).toLowerCase() + str3.substring(1)
            }
        } else {
            str2 += str3.substring(0, 1).toUpperCase() + str3.substring(1)
        }
    }
    return str2
}

gzb.getParas = function () {
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
gzb.findOption = function (option, val) {
    if (option == null) {
        return "text";
    }
    for (let optionElement of option) {
        if (optionElement.sysOptionValue.toString() === val.toString()) {
            return optionElement.sysOptionTitle;
        }
    }
    return val;
}

gzb.getSelect = function (json, id, title, css1, css2) {
    let opHtml = "";
    opHtml += "<option value=\"\">请选择或输入</option>";
    for (let jsonElement of json) {
        opHtml += "<option value=\"" + jsonElement.sysOptionValue + "\">" + jsonElement.sysOptionTitle + "</option>";
    }
    return `<div class="${css1}">
            <label class="layui-form-label">${title}</label>
            <div class="${css2}">
            <select id='${id}' lay-search=''>${opHtml}</select>
            </div>
        </div>`;
}

gzb.getInput = function (json, id, title, css1, css2) {
    return "<div class=\"" + css1 + "\">\n" +
        "            <label class=\"layui-form-label\">" + title + "</label>\n" +
        "            <div class=\"" + css2 + "\">\n" +
        "                <input id=\"" + id + "\" class=\"layui-input\" placeholder='" + title + "'>\n" +
        "            </div>\n" +
        "        </div>";
}
gzb.bindUploadImage = function (id) {
    let id_button_1 = id + "-button-1-image";
    let id_p_1 = id + "-p-1-image";
    let id_img_1 = id + "-img-1-image";
    //普通图片上传
    var uploadInst = upload.render({
        elem: '#' + id_button_1
        , url: gzb.api.upload
        , before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                $('#' + id_img_1).attr('src', result); //图片链接（base64）
            });
        }
        , done: function (res) {
            if (gzb.jsonVerify(res, true)) {
                console.log(res.data, res.data[0].sysFileMd5)
                $("#" + id).val(res.data[0].sysFileMd5);
            }
        }
        , error: function () {
            //演示失败状态，并实现重传
            var demoText = $('#' + id_p_1);
            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
            demoText.find('.demo-reload').on('click', function () {
                uploadInst.upload();
            });
        }
    });
}
gzb.getUploadImage = function (json, id, title, css1, css2, imageUrl) {
    let id_button_1 = id + "-button-1-image";
    let id_p_1 = id + "-p-1-image";
    let id_img_1 = id + "-img-1-image";
    return "<div class=\"" + css1 + "\">\n" +
        "            <label class=\"layui-form-label\">" + title + "</label>\n" +
        "            <div class=\"" + css2 + "\">\n" +
        "                    <div class=\"layui-upload\">\n" +
        "                        <button type=\"button\" class=\"layui-btn layui-btn-danger layui-btn-radius\" id=\"" + id_button_1 + "\">图片上传</button>\n" +
        "                        <div class=\"layui-upload-list\">\n" +
        "                            <img src='" + (imageUrl == null ? "" : imageUrl) + "' class=\"layui-upload-img\" id=\"" + id_img_1 + "\" style='width: 150px;height: 150px;background-color: #4E5465'>\n" +
        "                            <p id=\"" + id_p_1 + "\"></p>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "<input id='" + id + "' value='" + (imageUrl == null ? "" : imageUrl) + "' type='hidden'>" +
        "<script>gzb.bindUploadImage(\"" + id + "\")</script>" +
        "            </div>\n" +
        "        </div>";
}
gzb.getQueryInputId = function (field) {
    field = gzb.low_hump(field)
    return "query_input_gzb_001_" + field;
}
//cssText 1整行 其他短的
//是否显示 isField
//eleId 元素id
gzb.initInput = function (json, eleId, isField, defVal, cssText) {
    if (eleId == null) {
        eleId = "but_gzb_001";
    }
    if (isField == null) {
        isField = "sysMappingQueryShow";
    }
    let css1 = "layui-inline";
    let css2 = "layui-input-inline";
    if (cssText != null || Number(cssText) === 1) {
        css1 = "layui-form-item";
        css2 = "layui-input-block";
    }

    let ele1 = $("#" + eleId);
    let num = 0;
    if (ele1 == null) {
        return null;
    }
    if (json[isField] != null && Number(json[isField]) === 1) {
        let id = gzb.getQueryInputId(json.sysMappingVal);
        let html = "";
        if (json.sysMappingSelect != null && json.sysMappingSelect.toString().length > 0) {
            html = gzb.getSelect(json.sysMappingSelect, id, json.sysMappingTitle, css1, css2);
        } else if (json.sysMappingDate != null && json.sysMappingDate.toString().length > 0) {
            html = gzb.getInput(null, id, json.sysMappingTitle, css1, css2);
        } else if (json.sysMappingImage != null && json.sysMappingImage.toString().length > 0) {
            html = gzb.getUploadImage(null, id, json.sysMappingTitle, css1, css2);
        } else {
            html = gzb.getInput(null, id, json.sysMappingTitle, css1, css2);
        }
        ele1.append(html);
        if (json.sysMappingDate != null && json.sysMappingDate.toString().length > 0) {
            laydate.render({
                elem: '#' + id
                , theme: '#393D49'
                , type: json.sysMappingDate
                //, format: json.sysMappingDate
            });
        }
        let val = json[defVal]
        console.log("def id ",val)
        if (val != null && val.length > 0) {
            $("#" + id).val(val);
        }
        gzb.eleInit();
        num++;

    }
    if (num > 0) {
        ele1.attr("class", "layui-form-item")

    }
}
gzb.eleInit = function () {
    form.render();
}
gzb.getTableImage = function (url, height) {
    if ($("#table-image-height").text() === "") {
        height = height == null ? 30 : height
        $("head").append("<style id='table-image-height'>td > .layui-table-cell{height: " + height + "px;} </style>")
    }
    return "<img style='width: auto;max-height: 100%;' " +
        "src='" + url + "' " +
        "onclick='layer.photos({photos: {\"code\": 0,\"msg\": \"大图\",\"title\": \"大图\",\"id\": 1,\"start\": 0,\"data\": [{\"alt\": \"大图预览\",\"pid\": 1,\"src\": \"" + url + "\",\"thumb\": \"\"}]}});' />";
}

gzb.appendEvent = function (ele, fun, type) {
    if (type == null) {
        type = "click";
    }
    $('body').on(type, ele, function () {
        let a01 = gzb.timeMap[ele];
        let a02 = new Date().getTime();
        if (a01 != null) {
            if (a02 - a01 < 1000) {
                return;
            }
        }
        gzb.timeMap[ele] = a02;
        let funArray = gzb.getEvent0(ele, type);
        console.log('元素事件', "特征", ele, "类型", type, "函数", funArray, gzb.events);
        if (funArray == null) {
            return;
        }
        for (let fun1 of funArray) {
            if (fun1) {
                fun1(this);
            }
        }
    });
    if (ele.startsWith("#")) {
        ele = ele.substring(1, ele.length)
    }
    if (fun == null) {
        return;
    }
    gzb.appendEvent0(ele, fun, type)
}
gzb.appendEvent0 = function (ele, fun, type) {
    if (type == null) {
        type = "click";
    }
    if (ele.startsWith("#")) {
        ele = ele.substring(1, ele.length)
    }
    if (gzb.events[ele + "-" + type] == null) {
        gzb.events[ele + "-" + type] = []
    }
    gzb.events[ele + "-" + type].push(fun);
    // console.log('添加事件', "储存在gzb.events", ele, type, fun);
}
gzb.getEvent0 = function (ele, type, index) {
    if (type == null) {
        type = "click";
    }
    if (ele.startsWith("#")) {
        ele = ele.substring(1, ele.length)
    }
    let array = gzb.events[ele + "-" + type];
    if (array == null) {
        return null;
    }
    if (index == null) {
        return array;
    }
    if (array.length < index + 1) {
        return null;
    }
    return array[index]
}

gzb.readQueryRequestData = function (type, url) {
    let requestData = [];
    let requestDataString = "";
    for (let key in gzb.table.mapping) {
        let json = gzb.table.mapping[key]
        if (json.sysMappingQueryShow != null && Number(json.sysMappingQueryShow) === 1) {
            let id = gzb.getQueryInputId(json.sysMappingVal);
            let data = $("#" + id).val();
            if (data == null || data.length < 1) {
                continue;
            }
            requestData.push({
                field: json.sysMappingVal,
                symbol: json.sysMappingQuerySymbol,
                value: data,
                montage: json.sysMappingQueryMontage,
            })
            requestDataString += `field=${json.sysMappingVal}&symbol=${json.sysMappingQuerySymbol}&value=${encodeURIComponent(data)}&montage=${json.sysMappingQueryMontage}&`;
        }
    }
    let map1=gzb.getParas();
    for (let key in map1) {
        let next=false;
        for (let key2 in gzb.table.mapping) {
            let json = gzb.table.mapping[key2]
            if (json.sysMappingVal == null) {
                continue;
            }
            if (gzb.low_hump(json.sysMappingVal.toString().trim()) === key.trim()) {
                next=true;
                break;
            }
        }
        let data=map1[key];
        if (data == null) {
            continue;
        }
        if (!next) {
            console.log("排除无效参数:",key,data)
            continue;
        }
        requestDataString += `field=${key}&symbol=${1}&value=${encodeURIComponent(data)}&montage=${1}&`;
    }
    if (type == null || type === 1) {
        return requestData;
    } else {
        if (requestDataString.length > 0) {
            if (url.indexOf("?") > -1) {
                if (url.endsWith("?")) {
                    requestDataString = url + requestDataString;
                } else {
                    requestDataString = url + "&" + requestDataString;
                }
            } else {
                requestDataString = url + "?" + requestDataString;
            }
            if (requestDataString.endsWith("&")) {
                requestDataString = requestDataString.substring(0, requestDataString.length - 1)
            }
        } else {
            requestDataString = url
        }
        console.log(requestDataString)
        return requestDataString;
    }
}
gzb.loadTable = function () {
    let table_cols = gzb.table.cols
    if (gzb.tableHeight === 0) {
        gzb.tableHeight = document.getElementsByClassName("layui-card")[0].offsetHeight;
        gzb.tableHeight = window.innerHeight - gzb.tableHeight - 20
    }
    console.log(table_cols)
    table.render({
        elem: '#' + gzb.table.eleId
        , toolbar: '#' + gzb.table.eleId
        , url: gzb.readQueryRequestData(0, gzb.api.query)
        , method: 'get'  //"post"
        , where: null //如果无需传递额外参数，可不加该参数
        , title: '数据'
        , cols: [table_cols]
        , height: gzb.tableHeight
        , cellMinWidth: 100
        , page: true
        , limit: gzb.table.limit
        , limits: gzb.table.limits
        , response: {
            statusCode: 1 //重新规定成功的状态码为 200，table 组件默认为 0
        }
        , parseData: function (res) { //将原始数据解析成 table 组件所规定的数据
            gzb.table.code = res.code
            gzb.table.msg = res.message
            gzb.table.count = res.count
            gzb.table.data = res.data
            return {
                "code": res.code, //解析接口状态
                "msg": res.message, //解析提示文本
                "count": res.total, //解析数据长度
                "data": res.data //解析数据列表
            };
        }
    })

    gzb.initTableEdit();
}
gzb.initTableEdit = function () {
    //监听单元格编辑
    table.on('edit(' + gzb.table.eleId + ')', function (obj) {
        let value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        console.log(field, value, data)
        data[field] = value
        gzb.update_table_gzb_b001(obj, data, field, value);
    });
}

gzb.appendButton = function (supEleId, title, fun, colour, eleId, style) {

    if (eleId == null) {
        eleId = gzb.getUUid();
    }
    let className = null;
    if (colour == null) {
        className = "layui-btn layui-btn-primary layui-btn-radius";
    } else {
        className = "layui-btn " + colour + " layui-btn-radius";
    }
    if (style == null) {
        style = "";
    }
    let html = "<button class='" + className + "' id='" + eleId + "' style='" + style + "' >" + title + "</button>";

    if (supEleId == null) {
        supEleId="#but_gzb_002";
    }
    console.log("appendButton", supEleId, title, fun, className, eleId, style)
    let node = $(supEleId);
    if (node) {
        node.append(html)
    }
    console.log(node)
    gzb.appendEvent("#" + eleId, fun, "click");
    return eleId;
}
gzb.initBut = function (_query, _save, _delete) {
    let ele1 = $("#but_gzb_002");
    let num = 0;
    if (ele1 == null) {
        return null;
    }
    if (_query != null && Number(_query) === 1) {
        ele1.append("<button class='layui-btn layui-btn-primary layui-btn-radius' id='query_gzb_b001'>查询数据</button>");
        num++;
        gzb.appendEvent("#query_gzb_b001", gzb.query_gzb_b001, "click");
    }
    if (_save && Number(_save) === 1) {
        ele1.append("<button class='layui-btn layui-btn-normal layui-btn-radius' id='update_gzb_b001'>添加数据</button>");
        num++;
        gzb.appendEvent("#update_gzb_b001", gzb.save_gzb_b001, "click");
    }
    if (_delete && Number(_delete) === 1) {
        ele1.append("<button class='layui-btn layui-btn-danger layui-btn-radius' id='delete_gzb_b001'>删除选中</button>");
        num++;
        gzb.appendEvent("#delete_gzb_b001", gzb.delete_gzb_b001, "click");
    }
    if (num > 0) {
        ele1.attr("class", "layui-card-header")
    } else {
        ele1.remove();
    }
}
gzb.appendTableButton = function (title, colour, style, fun, name) {
    if (name == null) {
        name = title;
    }

    let className = null;
    if (colour == null) {
        className = "layui-btn layui-btn-primary layui-btn-xs layui-btn-radius";
    } else {
        className = "layui-btn " + colour + " layui-btn-xs layui-btn-radius";
    }
    if (style == null) {
        style = "";
    }
    gzb.table.button.push({
        title: title,
        className: className,
        style: style,
        fun: fun,
        name: name
    })
}
gzb.initTableBut = function (res, table_cols) {
    if (res.data.sysMappingShowTableUpdate != null && Number(res.data.sysMappingShowTableUpdate) === 1) {
        gzb.appendTableButton("修改", gzb.button.colour["蓝色"], "", gzb.update_table_gzb_open_page, "update_table_gzb_open_page");
    }
    if (res.data.sysMappingShowTableDelete != null && Number(res.data.sysMappingShowTableDelete) === 1) {
        gzb.appendTableButton("删除", gzb.button.colour["红色"], "", gzb.delete_table_gzb_b001, "delete_table_gzb_b001");
    }
    let but_table_html = "";
    let num = 0;
    for (let buttonElement of gzb.table.button) {
        but_table_html += "<button " +
            "class='" + buttonElement.className + "' " +
            "name='" + buttonElement.name + "' " +
            "style='" + buttonElement.style + "' " +
            "lay-event='" + buttonElement.name + "'>" + buttonElement.title + "</button>";
        gzb.appendEvent0("#" + buttonElement.name, buttonElement.fun, "click");
        num++;
    }
    if (but_table_html.length > 0) {
        let templet1 = function (data) {
            return but_table_html
        }
        let json1 = {field: "操作按钮", title: "操作按钮", templet: templet1}
        if (res.data.sysMappingTableWidth != null && Number(res.data.sysMappingTableWidth) > 1) {
            json1.width = Number(res.data.sysMappingTableWidth);
        } else {
            json1.width = num * 60;
        }
        table_cols.push(json1)
        //监听工具条
        table.on('tool(' + gzb.table.eleId + ')', function (obj) {
            let a01 = gzb.timeMap[obj.event];
            let a02 = new Date().getTime();
            if (a01 != null) {
                if (a02 - a01 < 1000) {
                    return;
                }
            }
            gzb.timeMap[obj.event] = a02;
            let data = obj.data;
            let fun1 = gzb.getEvent0(obj.event, "click", 0)
            if (fun1 != null) {
                fun1(obj, data)
            }
        });
    }
    return table_cols;
}
gzb.initListPage = function () {
    let table_cols = [];
    gzb.getMappingJSON(function (res) {
        if (gzb.jsonVerify(res, false) === false) {
            return;
        }
        table_cols.push({type: 'checkbox'})
        gzb.initBut(res.data.sysMappingShowQuery, res.data.sysMappingShowSave, res.data.sysMappingShowDelete)
        for (let datum of res.data.data) {
            gzb.initInput(datum,null,null,"sysGroupColumnQueryDef");
            if (datum.sysMappingVal == null || datum.sysMappingTitle == null || datum.sysMappingTableShow == null || Number(datum.sysMappingTableShow) === 0) {
                continue;
            }
            gzb.table.mapping[datum.sysMappingVal] = datum
            let json0 = {};
            json0.field = gzb.low_hump(datum.sysMappingVal);
            json0.title = datum.sysMappingTitle
            if (datum.sysMappingTableEditShow != null) {
                if (Number(datum.sysMappingTableEditShow) === 1) {
                    json0.edit = "text"
                }
                if (Number(datum.sysMappingTableEditShow) === 2) {
                    json0.edit = "select"
                }
            } else {
                json0.edit = null;
            }
            if (datum.sysMappingTableSort != null && Number(datum.sysMappingTableSort) === 1) {
                json0.sort = true;
            } else {
                json0.sort = false;
            }
            if (datum.sysMappingTableWidth != null && Number(datum.sysMappingTableWidth) > 1) {
                json0.width = Number(datum.sysMappingTableWidth);
            } else {
                json0.width = 100;
            }
            if (datum.sysMappingScript != null) {
                json0.templet = sysMappingScript;
            }
            if (datum.sysMappingImage != null) {
                json0.templet = function (data) {
                    let imageUrl = gzb.low_hump(datum.sysMappingImage)
                    let key2 = gzb.low_hump(datum.sysMappingVal)
                    while (imageUrl.indexOf("\${" + key2 + "}") > -1) {
                        imageUrl = imageUrl.replace("\${" + key2 + "}", data[key2])
                    }
                    return gzb.getTableImage(imageUrl, 30);
                };
            }

            if (datum.sysMappingSelect != null) {
                json0.templet = function (data) {
                    let key = gzb.low_hump(datum.sysMappingVal);
                    let str1 = data[key];
                    if (str1 == null) {
                        str1 = "";
                    }
                    for (let jsonElement of datum.sysMappingSelect) {
                        if (jsonElement.sysOptionValue == null || jsonElement.sysOptionTitle == null) {
                            continue;
                        }
                        if (jsonElement.sysOptionValue.toString() === str1.toString()) {
                            return jsonElement.sysOptionTitle;
                        }
                    }
                    return str1;
                };
            }
            table_cols.push(json0)
        }
        table_cols = gzb.initTableBut(res, table_cols)

        gzb.table.cols = table_cols
        gzb.loadTable()
    });

}
gzb.loadLibs = async function () {
    let map = gzb.getParas()
    console.log(map)
    if (map.config != null) {
        let name = map.config
        if (map.edit != null) {
            name = map.edit
        }
        let url1 = "js/" + map.config + ".js"
        let url2 = "js/edit/" + name + ".js"
        eval(await (await fetch(url1)).text())
        eval(await (await fetch(url2)).text())
        gzb.api = {
            query: gzb.base + gzb.entityName + "/query",
            list: gzb.base + gzb.entityName + "/list",
            find: gzb.base + gzb.entityName + "/find",
            update: gzb.base + gzb.entityName + "/update",
            delete: gzb.base + gzb.entityName + "/deleteAll",
            save: gzb.base + gzb.entityName + "/save",
            upload: gzb.base + "upload",
            mapping: gzb.base + "read/mapping",
        }
    }
}

async function initUpdate() {
    await gzb.loadLibs();
    let map = gzb.getParas()
    if (map.config != null) {
        let name = map.config
        if (map.edit != null) {
            map.config = map.edit
        }
        let url1 = "js/" + map.config + ".js"
        let url2 = "js/edit/" + name + ".js"
        eval(await (await fetch(url1)).text())
        eval(await (await fetch(url2)).text())

    }

    gzb.update_init1 = function (res) {
        if (gzb.jsonVerify(res, false)) {
            gzb.initUpdatePage(res.data);
        }
        let id = map[gzb.entityIdName]
        if (id == null) {
            return null;
        }
        gzb.get(gzb.api.find, gzb.entityIdName + "=" + id, gzb.update_init2)
    }
    gzb.update_init2 = function (res) {
        gzb.table.thisData = res;
        if (gzb.jsonVerify(res, false)) {
            for (let key in res.data) {
                for (let datum of gzb.table.mapping.data) {
                    let imageUrl = gzb.low_hump(datum.sysMappingImage)
                    let key2 = gzb.low_hump(datum.sysMappingVal)
                    let eleId = gzb.getQueryInputId(datum.sysMappingVal);
                    if (key2 === key) {
                        if (imageUrl != null) {
                            let id_img_1 = eleId + "-img-1-image";
                            let data = "\${" + key2 + "}"
                            while (imageUrl.indexOf(data) > -1) {
                                imageUrl = imageUrl.replace(data, res.data[key])
                            }
                            $("#" + id_img_1).attr("src", imageUrl);
                            //${sysUsersOpenId}  sysUsersOpenId  save_input_gzb_c001
                            console.log("imageUrl", key2, imageUrl)
                        }
                        $("#" + eleId).val(res.data[key]);
                        gzb.eleInit();
                        break;
                    }
                }
            }
        }
    }
    gzb.getMappingJSON(gzb.update_init1);
    gzb.initUpdatePage = function (mapping) {
        gzb.table.mapping = mapping
        for (let datum of gzb.table.mapping.data) {
            gzb.initInput(datum,
                "save_input_gzb_c001",
                "sysMappingTableUpdateShow",
                "sysMappingTableUpdateDef",
                1)
        }

    }
    gzb.appendEvent("#but_001", eventUpdate, "click")
    gzb.appendEvent("#but_003", function () {
        for (let datum of gzb.table.mapping.data) {
            let id = gzb.getQueryInputId(datum.sysMappingVal);
            $("#" + id).val("");
        }
    }, "click")
    gzb.appendEvent("#but_002", function () {
        location.reload();
    }, "click")
}

async function initSave() {
    await gzb.loadLibs();

    function init1(res) {
        if (gzb.jsonVerify(res, false)) {
            gzb.initSavePage(res.data);
        }
    }

    gzb.getMappingJSON(init1);
    gzb.initSavePage = function (mapping) {
        gzb.table.mapping = mapping
        for (let datum of gzb.table.mapping.data) {
            gzb.initInput(datum, "save_input_gzb_c001", "sysMappingSaveShow", "sysMappingSaveDef", 1)
        }
    }
    gzb.appendEvent("#but_001", eventSave, "click")
    gzb.appendEvent("#but_003", function () {
        for (let datum of gzb.table.mapping.data) {
            let id = gzb.getQueryInputId(datum.sysMappingVal);
            $("#" + id).val("");
        }
    }, "click")
    gzb.appendEvent("#but_002", function () {
        location.reload();
    }, "click")
}

async function initListPage() {
    await gzb.loadLibs();
    gzb.initListPage();
}


gzb.getMappingJSON = function (suc) {
    gzb.get(gzb.api.mapping, {key: gzb.entityName}, function (res) {
        let sysGroup = res.data[0].sysGroup;

        let obj1 = {};
        obj1.sysMappingShowQuery = sysGroup.sysGroupTableButQueryOpen;
        obj1.sysMappingShowSave = sysGroup.sysGroupTableButSaveOpen;
        obj1.sysMappingShowDelete = sysGroup.sysGroupTableButDeleteOpen;
        obj1.sysMappingTableWidth = sysGroup.sysGroupTableTableButWidth;
        obj1.sysMappingShowTableDelete = sysGroup.sysGroupTableTableDeleteOpen;
        obj1.sysMappingShowTableUpdate = sysGroup.sysGroupTableTableUpdateOpen;
        obj1.sysMappingShowTableUpdate = sysGroup.sysGroupTableTableUpdateOpen;
        let array1 = [];
        for (let sysGroupColumn of sysGroup.sysGroupColumn) {
            let mapping1 = sysGroupColumn.mapping;
            mapping1.sysMappingTableShow = sysGroupColumn.sysGroupColumnTable
            mapping1.sysMappingTableUpdateShow = sysGroupColumn.sysGroupColumnUpdate
            mapping1.sysMappingTableEditShow = sysGroupColumn.sysGroupColumnEdit
            mapping1.sysMappingSaveShow = sysGroupColumn.sysGroupColumnSave
            mapping1.sysMappingQueryShow = sysGroupColumn.sysGroupColumnQuery
            mapping1.sysMappingSaveDef = sysGroupColumn.sysGroupColumnSaveDef
            mapping1.sysMappingTableUpdateDef = sysGroupColumn.sysGroupColumnUpdateDef
            mapping1.sysMappingQuerySymbol = sysGroupColumn.sysGroupColumnQuerySymbol
            mapping1.sysMappingQueryMontage = sysGroupColumn.sysGroupColumnQueryMontage
            mapping1.sysGroupColumnQueryDef = sysGroupColumn.sysGroupColumnQueryDef
            array1.push(mapping1)
        }
        obj1.data = array1;
        let json1 = {
            "code": "1",
            "data": obj1
        }
        console.log("json1  1   ", json1)
        suc(json1);
    }, function (res) {
        gzb.jsonVerify(res, false)
    })
}

gzb.delete_table_gzb_b001 = function (obj, data) {
    layer.confirm('真的要删除吗？', function (index) {
        let postData = gzb.entityIdName + "=" + data[gzb.entityIdName];
        gzb.get(gzb.api.delete, postData, function (res) {
            if (gzb.jsonVerify(res, true)) {
                obj.del();
            }
        });
        layer.close(index);
    });
}
gzb.delete_gzb_b001 = function (_this) {

    layer.confirm('真的要删除已选中数据吗？', function (index) {
        let checkStatus = table.checkStatus(gzb.table.eleId)
        let data = checkStatus.data;
        let postData = "";
        for (let datum of data) {
            postData += gzb.entityIdName + "=" + encodeURIComponent(datum[gzb.entityIdName]) + "&";
        }
        gzb.get(gzb.api.delete, postData, function (res) {
            if (gzb.jsonVerify(res, true)) {
                table.reload(gzb.table.eleId);
            }
        });
        layer.close(index);
    });

}
gzb.update_table_gzb_open_page = function (obj, data) {
    localStorage["updateJSONObject"] = JSON.stringify(data);
    layer.open({
        type: 2,
        title: '修改数据',
        shadeClose: true,
        shade: 0.5,
        area: ['60%', '80%'],
        content: 'update.html?config=' + gzb.entityName + '&' + gzb.entityIdName + '=' + data[gzb.entityIdName],
        end: function () {
            console.log("修改数据", "弹窗关闭")
            let json = JSON.parse(localStorage["updateJSONObject"]);
            console.log("json", json)
            obj.update(json);
        }
    });
}
gzb.query_gzb_b001 = function (_this) {
    gzb.loadTable();
}
gzb.save_gzb_b001 = function (_this) {
    layer.open({
        type: 2,
        title: '修改数据',
        shadeClose: true,
        shade: 0.5,
        area: ['60%', '80%'],
        content: 'save.html?config=' + gzb.entityName,
        end: function () {
            console.log("修改数据", "弹窗关闭")
        }
    });
}
gzb.update_table_gzb_b001 = function (obj, data) {
    /// /system/v1.0.0/sysMapping/update?sysMappingId=
    let pdata = "";
    for (let jsonKey in data) {
        if (jsonKey == null) {
            continue;
        }
        console.log(jsonKey, data[jsonKey])
        pdata += jsonKey + "=" + encodeURIComponent(data[jsonKey]) + "&"
    }
    gzb.post(gzb.api.update, pdata, function (res) {
        if (gzb.jsonVerify(res, true)) {

        }
    })
}

function eventSave() {
    let postData = "";
    for (let datum of gzb.table.mapping.data) {
        let key = gzb.low_hump(datum.sysMappingVal)
        let eleId = gzb.getQueryInputId(datum.sysMappingVal);
        let val = $("#" + eleId).val();
        if (key != null && val != null) {
            postData += key + "=" + val + "&";
        }
    }
    if (postData.endsWith("&")) {
        postData = postData.substring(0, postData.length - 1)
    }
    gzb.post(gzb.api.save, postData, function (res) {
        gzb.jsonVerify(res, true)
    });
}

function eventUpdate() {
    let postData = "";
    for (let datum of gzb.table.mapping.data) {
        let key = gzb.low_hump(datum.sysMappingVal)
        let eleId = gzb.getQueryInputId(datum.sysMappingVal);
        let val = $("#" + eleId).val();
        if (val == null || val.length < 1) {
            continue;
        }
        gzb.table.thisData.data[key] = val;
    }
    console.log("gzb.table.thisData.data", gzb.table.thisData.data)
    localStorage["updateJSONObject"] = JSON.stringify(gzb.table.thisData.data)
    for (let key in gzb.table.thisData.data) {
        postData += key + "=" + gzb.table.thisData.data[key] + "&";
    }
    if (postData.endsWith("&")) {
        postData = postData.substring(0, postData.length - 1)
    }
    console.log(gzb.table.mapping.data, gzb.table.thisData.data, postData)
    gzb.post(gzb.api.update, postData, function (res) {
        gzb.jsonVerify(res, true)
    });
}
