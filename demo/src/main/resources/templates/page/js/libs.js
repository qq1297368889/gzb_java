// 全局禁止表单提交的函数
function preventFormSubmit() {
    // 处理所有表单：阻止submit事件的默认行为
    document.querySelectorAll('form').forEach(form => {
        // 先移除已有的事件监听（避免重复绑定）
        form.removeEventListener('submit', handleFormSubmit);
        form.addEventListener('submit', handleFormSubmit);
    });

    // 处理所有submit类型的按钮：改为button类型（避免触发提交）
    document.querySelectorAll('button[type="submit"]').forEach(btn => {
        // 记录原始类型（可选：如需恢复时使用）
        if (!btn.dataset.originalType) {
            btn.dataset.originalType = btn.type;
        }
        btn.type = 'button';
    });
}

// 表单submit事件的处理函数（阻止默认提交）
function handleFormSubmit(e) {
    e.preventDefault(); // 阻止表单默认提交
    e.stopPropagation(); // 阻止事件冒泡
    console.log('表单提交已被全局禁止', e.target);
}

// 监听DOM变化，处理动态创建的表单和按钮
const observer = new MutationObserver((mutations) => {
    mutations.forEach(mutation => {
        // 检查新增的节点中是否包含表单或submit按钮
        mutation.addedNodes.forEach(node => {
            if (node.nodeType === 1) { // 元素节点
                // 如果新增的是表单，直接处理
                if (node.tagName === 'FORM') {
                    node.removeEventListener('submit', handleFormSubmit);
                    node.addEventListener('submit', handleFormSubmit);
                }
                // 如果新增的是submit按钮，改为button类型
                if (node.tagName === 'BUTTON' && node.type === 'submit') {
                    node.dataset.originalType = node.type;
                    node.type = 'button';
                }
                // 检查子节点中是否包含表单或按钮
                if (node.querySelector('form') || node.querySelector('button[type="submit"]')) {
                    preventFormSubmit(); // 重新扫描处理
                }
            }
        });
    });
});

// 启动DOM监听（监视整个文档的变化）
observer.observe(document.documentElement, {
    childList: true,
    subtree: true,
    attributes: false,
    characterData: false
});

// 初始加载时处理已存在的表单和按钮
document.addEventListener('DOMContentLoaded', preventFormSubmit);
Date.prototype.Format = function (fmt) {
    if (fmt === null) {
        fmt = "yyyy-MM-dd hh:mm:ss";
    }
    if (fmt === "date") {
        fmt = "hh:mm:ss";
    }
    if (fmt === "time") {
        fmt = "hh:mm:ss";
    }
    if (fmt === "datetime") {
        fmt = "yyyy-MM-dd hh:mm:ss";
    }
    let o = {
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
    for (let k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }

    return fmt;
}

String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

/**
 * 深度扫描对象或数组，将所有属性或元素中的空字符串 ("") 替换为 null。
 *
 * @param {Object|Array} data 要扫描和修改的变量 (Map 或 List 类型)
 * @returns {Object|Array|null} 返回修改后的变量本身
 */
function replaceEmptyStringsWithNull(data) {
    // 1. 检查数据是否为 null 或非对象类型
    // null 和基本类型（字符串，数字，布尔值等）直接返回
    if (data === null || typeof data !== 'object') {
        return data;
    }

    // 2. 处理数组 (List) 类型
    if (Array.isArray(data)) {
        for (let i = 0; i < data.length; i++) {
            // 递归处理数组元素
            data[i] = replaceEmptyStringsWithNull(data[i]);
        }
    }

    // 3. 处理对象 (Map) 类型
    else {
        // 使用 for...in 循环遍历对象的所有可枚举属性
        for (const key in data) {
            // 确保只处理对象自身的属性，排除继承的属性
            if (Object.prototype.hasOwnProperty.call(data, key)) {
                let value = data[key];

                // 检查当前值是否为目标类型 ("" 空字符串)
                if (value === "") {
                    // 如果是空字符串，替换为 null
                    data[key] = null;
                }
                // 检查当前值是否为对象或数组（需要进一步递归处理）
                else if (typeof value === 'object' && value !== null) {
                    // 递归调用处理子属性/子元素
                    data[key] = replaceEmptyStringsWithNull(value);
                }
            }
        }
    }

    // 返回修改后的原始变量
    return data;
}

function loadScript(url) {
    let data = httpGet(url);
    eval(data);
}

function httpGet(url) {
    let xml = new XMLHttpRequest();
    xml.open("GET", url, false)
    xml.send();
    let data = xml.responseText;
    try {
        return JSON.parse(data);
    } catch (e) {
        return data;
    }
}

function httpRequest(url, met, data, headers) {
    //防止请求中断 不允许失败
    try {
        met = met == null ? "GET" : met;
        data = data == null ? "" : data;
        met = met.toUpperCase();
        let finalUrl = url;
        let finalData = data;
        // 【修复点 1】：处理 GET/DELETE 方法的数据：必须拼接到 URL 上
        if (met === 'GET' || met === 'DELETE') {
            if (data && typeof data === 'string') {
                finalUrl += (finalUrl.includes('?') ? '&' : '?') + data;
                finalData = null; // GET/DELETE 禁传 Body
            }
        }
        let xhr = new XMLHttpRequest();
        xhr.open(met, finalUrl, false); // 第三个参数为是否开启异步请求
        if (headers == null) {
            headers = {"content-type": "application/x-www-form-urlencoded"}
        }
        for (let key in headers) {
            xhr.setRequestHeader(key, headers[key]);
        }
        xhr.send(data);
        //增加容错 不管是不是json都尝试
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
    } catch (e) {
        console.log(e.message)
        return null;
    }

}

/**
 * 判断当前页面是否被任何形式的框架内嵌。
 * @returns {boolean} 如果被内嵌则返回 true，否则返回 false。
 */
function isFramed() {
    try {
        // window.self 指向当前窗口，window.top 指向最顶层窗口。
        // 如果两者不相等，说明当前窗口不是最顶层窗口，即被内嵌了。
        return window.self !== window.top;
    } catch (e) {
        // 跨域访问可能导致安全错误（SecurityError），
        // 如果捕获到错误，我们假定它被内嵌在不同源（Cross-Origin）的框架中。
        return true;
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

let gzb = {};

gzb.update_data_key = "save_gzb_one_001";
gzb.init = {}//初始化调用入口
//所有选项
gzb.options = {};
//url中的参数
gzb.params = {};
//按钮 css
gzb.but_css = ["layui-btn-primary", "", "layui-btn-normal", "layui-btn-warm", "layui-btn-danger", "layui-btn-disabled"]
//按钮大小
gzb.but_size = ["", "layui-btn-sm", "layui-btn-xs"]

gzb.mapping = null;//映射信息保存

gzb.api = {}
//响应结果映射
gzb.resp = {
    "codeName": "code",
    "messageName": "message",
    "jumpName": "url",
    "successVal": "1",
    "failVal": "2",
    "errorVal": "3",
    "jumpVal": "4",
}
//事件 按钮等信息保存
gzb.events = {
    "table_query": null,//查询按钮事件
    "table_save": null,//删除按钮事件
    "table_delete": null,//删除按钮事件
    "table_tool": {},//行级 工具栏按钮事件
    "table_edit": null,//列级 编辑事件
    "buttons": [],
    "table_tool_buttons": [],
    "input_event": {}
}

//表格元数据
gzb.table = {
    eleId: "table_001",
    cols: [],
    height: 0,
    "limit": 30,
    "limits": [10, 20, 30, 40, 50, 100, 200, 500, 1000],
    "parseData": function (res) { //将原始数据解析成 table 组件所规定的数据
        gzb.jsonVerify(res, false)
        return {
            "code": res.code, //解析接口状态
            "msg": res.message, //解析提示文本
            "count": res.total, //解析数据长度
            "data": res.data //解析数据列表
        };
    },
    "statusCodeSuccess": "1"
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
gzb.toastSuccess = function (msg) {
    layer.msg(msg, {
        offset: '15px'
        , icon: 6
    });
}
gzb.toastFail = function (msg) {
    layer.msg(msg, {
        offset: '15px'
        , icon: 7
    });
}
gzb.toastError = function (msg) {
    layer.msg(msg, {
        offset: '15px'
        , icon: 5
    });
}
gzb.jump = function (url, time) {
    setTimeout(function () {
        //location.href=url;
        if (isFramed()) {
            localStorage["跳转地址"] = url;
        } else {
            location.href = url;
        }

    }, time);
}
gzb.jsonVerify = function (json, successPrint) {
    if (json == null) {
        gzb.toastFail("访问服务器失败,请稍后重试[-1]")
        return false;
    }
    if (json[gzb.resp.codeName] == null) {
        gzb.toastFail("访问服务器失败,请稍后重试[-2]")
        return false;
    }
    if (json[gzb.resp.messageName] == null) {
        json[gzb.resp.messageName] = "访问服务器失败,请稍后重试[-3]";
    }
    if (json[gzb.resp.codeName] == gzb.resp.successVal) {
        if (successPrint) {
            gzb.toastSuccess(json[gzb.resp.messageName])
        }
        return true;
    }
    if (json[gzb.resp.codeName] == gzb.resp.failVal) {
        gzb.toastFail(json[gzb.resp.messageName])
        return false;
    }
    if (json[gzb.resp.codeName] == gzb.resp.errorVal) {
        gzb.toastError(json[gzb.resp.messageName])
        return false;
    }
    if (json[gzb.resp.codeName] == gzb.resp.jumpVal) {
        gzb.toastFail(json[gzb.resp.messageName])
        gzb.jump(json[gzb.resp.jumpName], 1000);
        return true;
    } else {
        gzb.toastError(json[gzb.resp.messageName])
        return false;
    }
}
gzb.hook = {
    GET: [],
    POST: [],
    PUT: [],
    DELETE: []
}

gzb.joinParar = function (url, data) {
    let _DATA = "";
    let _URL = "";
    let arr001 = url.split("?", 2);
    if (arr001.length === 2) {
        _DATA = arr001[1];
        _URL = arr001[0];
    } else {
        _URL = url;
    }
    let data0 = "";
    if (typeof data !== 'string') {
        if (Array.isArray(data)) {
            for (let map of data) {
                for (let key in map) {
                    let val=encodeURIComponent(map[key]);
                    data0 += `${key}=${val}&`
                }
            }
        } else {
            for (let key in data) {
                let val=encodeURIComponent(data[key]);
                data0 += `${key}=${val}&`
            }
        }
    } else {
        data0 = data;
    }
    if (data0.length > 0) {
        if (!_DATA.endsWith("&")) {
            _DATA += "&";
        }
        if (data0.startsWith("&")) {
            data0 = data0.substring(1)
        }
        _DATA += data0;
    }
    if (_DATA.endsWith("&")) {
        _DATA = _DATA.substring(0, _DATA.length - 1)
    }
    if (_DATA.startsWith("&")) {
        _DATA = _DATA.substring(1)
    }
    return [_URL, _DATA];
}
/**
 * 通用 HTTP 请求函数，基于 fetch API。
 * @param {string} url 请求的 URL。
 * @param {string} [met='GET'] 请求方法（GET, POST, PUT, DELETE）。
 * @param {Object} [data={}] 请求体数据（如果是 GET，则作为 URL 参数）。
 * @param {Object} [headers={}] 自定义请求头。
 * @param {File[]|FileList} [files=[]] 需要上传的文件列表（如果有文件，请求方法将强制为 POST，内容类型为 multipart/form-data）。
 * @returns {Promise<Object>} 返回一个包含 'data' 和 'status' 的 Promise 对象。
 */
gzb.request = async function (url, met = 'GET', data = "", headers = {}) {

    let req_0 = gzb.joinParar(url, data);
    url = req_0[0];
    data = req_0[1];
    if (gzb.hook[met] != null) {
        for (let fun of gzb.hook[met]) {
            data = fun(data);
        }
    }
    console.log("request ", url, met, data, headers)
    // 2. 构造请求选项 (options)
    const options = {
        method: met,
        headers: new Headers(headers)
    };
    let finalUrl = url;

    options.headers.set('content-type', 'application/x-www-form-urlencoded');
    // 3. 处理文件上传 (multipart/form-data)
    if (met === 'GET' || met === 'HEAD') {
        finalUrl += "?" + data;
    } else {
        try {
            options.body = data;
        } catch (e) {
            console.error('Error: Failed to stringify request data for body:', e);
            throw new Error('Invalid request data format.');
        }
    }

    // 6. 执行 fetch 请求
    try {
        const response = await fetch(finalUrl, options);
        // 7. 处理响应状态码
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP Error ${response.status}: ${response.statusText}. Response: ${errorText.substring(0, 100)}...`);
        }

        // 8. 尝试解析 JSON 响应
        try {
            const responseData = await response.json();
            return {
                data: responseData,
                status: response.status
            };
        } catch (e) {
            // 如果响应不是 JSON (如 204 No Content)，返回原始文本
            const rawText = await response.text();
            return {
                data: rawText || null,
                status: response.status
            };
        }

    } catch (error) {
        console.error('Request failed:', finalUrl, error);
        throw error;
    }
}

gzb.requestCallBack = function (url, met, data, sucFun, errFun, headers) {
    if (errFun == null) {
        errFun = async function (error) {
            console.log("默认 错误 回调", error)
        }
    }
    if (sucFun == null) {
        sucFun = async function (data, status, res) {
            console.log("默认 错误 回调", data, status, res)
        }
    }
    gzb.request(url, met, data, headers)
        .then(result => {
            sucFun(result.data, result.status, result)
        })
        .catch(error => {
            errFun(error)
        });
}
// 重新定义 HTTP 方法快捷函数
gzb.get = function (url, data, suc, err) {
    return gzb.requestCallBack(url, "GET", data, suc, err);
};

gzb.post = function (url, data, suc, err) {
    return gzb.requestCallBack(url, "POST", data, suc, err);
};

gzb.put = function (url, data, suc, err) {
    return gzb.requestCallBack(url, "PUT", data, suc, err);
};

gzb.delete = function (url, data, suc, err) {
    return gzb.requestCallBack(url, "DELETE", data, suc, err);
};
//转驼峰
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
//获取URL参数
gzb.getUrlParar = function (url) {
    let res = {};
    if (url == null) {
        url = location.href;
    }
    let arr1 = url.split("?", 2);
    if (arr1.length < 2) {
        return res;
    }
    for (let str of arr1) {
        if (str == null) {
            continue;
        }
        let arr2 = str.split("&");
        for (let string of arr2) {
            if (string == null) {
                continue;
            }
            let arr3 = string.split("=", 2);
            if (arr3.length === 2) {
                if (arr3[0] == null || arr3[1] == null) {
                    continue;
                }
                let key = arr3[0].trim();
                let val = arr3[1].trim();
                if (res[key] == null) {
                    res[key] = [];
                }
                res[key].push(val);
            }
        }
    }
    return res;
}

gzb.init_ele = function () {
    form.render(null, "component-form-group");
    element.render('breadcrumb', 'breadcrumb');
}


//原始按钮  默认按钮 百搭按钮 暖色按钮 警告按钮 禁用按钮

gzb.createButtonHTML = function (title, index, size, ele_id, event, parent_id, append) {
    if (ele_id == null) {
        ele_id = gzb.getUUid();
    }
    let html = `<button class="layui-btn ${gzb.but_size[size]} ${gzb.but_css[index]} layui-btn-radius" id="${ele_id}" lay-event='${title}'>${title}</button>`;
    if (parent_id != null) {
        if (append) {
            $("#" + parent_id).append(html);
        } else {
            $("#" + parent_id).html(html);
        }
    }
    if (event != null) {
        gzb.registerEvent(ele_id, event, "click");
    }
    return html;
}

gzb.notNull = function (obj) {
    if (obj == null || obj.toString().length == 0) {
        return false;
    }
    return true;
}
//type 1查询 2修改 3新增
gzb.createFrom = function (col, type) {
    let html = "";
    if (col.data == null || col.data.sysMappingColumnTitle == null || col.data.sysMappingColumnName == null) {
        return html;
    }
    let this_data;
    if (type == 2) {
        this_data = JSON.parse(localStorage[gzb.update_data_key])
        if (this_data == null) {
            gzb.toastFail("预读数据失败");
            return;
        }
    }
    let css = "";
    let def0 = null;
    let open = 0;
    if (type == 1) {
        def0 = col.sysRoleColumnQueryDef;
        css = "layui-inline";
        open = col.sysRoleColumnQuery;
    } else if (type == 2) {
        def0 = this_data[col.data.sysMappingColumnName];
        if (def0 == null || def0.length == 0) {
            def0 = col.sysRoleColumnUpdateDef;
        }
        css = "layui-form-item";
        open = col.sysRoleColumnUpdate;
    } else if (type == 3) {
        def0 = col.sysRoleColumnSaveDef;
        css = "layui-form-item";
        open = col.sysRoleColumnSave;
    }
    if (def0 == null) {
        def0 = "";
    }
    if (open == null || open.toString() != "1") {
        html += "<input value='" + def0 + "' type='hidden' id='" + (col.data.sysMappingColumnName) + "_query_where_0' style='height: 0;width: 0;'/>";
    } else {
        if (col.data.sysMappingColumnOption != null && col.data.sysMappingColumnOption.length > 0) {
            html += gzb.createSelectHTML(
                col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName,
                col.data.sysMappingColumnOption,
                null, def0, css);
        } else if (col.data.sysMappingColumnFile != null && type != 1) {
            html += gzb.createFileHTML(col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName, col.data.sysMappingColumnFile, def0);
        } else if (col.data.sysMappingColumnImage != null && type != 1) {
            html += gzb.createImageHTML(col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName, col.data.sysMappingColumnImage, def0);
        } else if (col.data.sysMappingColumnText != null && col.data.sysMappingColumnText == "2" && type != 1) {
            html += gzb.createTextareaHTML(
                col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName,
                col.data.sysMappingColumnDate,
                col.data.sysMappingColumnNumber,
                null, def0, css);
        } else if (col.data.sysMappingColumnRequest != null) {
            //总体思路 保留一个input 然后绑定事件 打开一个弹窗 处理后 值回显
            let id = col.data.sysMappingColumnName + "_query_where_0";
            html += gzb.createInputHTML(
                col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName,
                col.data.sysMappingColumnDate,
                col.data.sysMappingColumnNumber,
                null, def0, css,id);
            gzb.registerEvent(id,function (){
                let req01=col.data.sysMappingColumnRequest;
                let data=this_data==null?{}:this_data;
                let _field=col.data.sysMappingColumnName;
                data[_field]=$("#"+id).val();
                localStorage["编辑选择项-请求数据"]=JSON.stringify(req01);
                localStorage["编辑选择项-本行数据"]=JSON.stringify(data);
                localStorage["编辑选择项-当前列"]=JSON.stringify(_field);
                gzb.openPage("option.html?config="+gzb.entity_name+"&no_update=1","编辑选项","70%","80%",function (){
                    console.log("end")
                    let newData=JSON.parse(localStorage["编辑选择项-本行数据"]);
                    $("#"+id).val(newData[_field]);
                    localStorage.removeItem("编辑选择项-请求数据")
                    localStorage.removeItem("编辑选择项-本行数据")
                    localStorage.removeItem("编辑选择项-当前列")
                })
            },"click");
        } else {
            html += gzb.createInputHTML(
                col.data.sysMappingColumnTitle,
                col.data.sysMappingColumnName,
                col.data.sysMappingColumnDate,
                col.data.sysMappingColumnNumber,
                null, def0, css);
        }
    }
    return html;
}
gzb.createImageHTML = function (title, id, imageTem, def0) {
    if (imageTem == null) {
        return "";
    }
    let def1 = "";
    if (def0 != null && def0.length > 0) {
        def1 = imageTem.toString().replace("\${" + id + "}", def0);
    }
    let html = gzb.createInputHTML(title, id, null, null, null, def0, "layui-form-item");
    html += `<div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-block">
                            <button type="button"  class="layui-btn  layui-btn-primary layui-btn-radius" id="image_upload_${id}">选择文件</button>
                             <strong>下载地址:</strong><a  target="_blank" href="${def1}" > ${def1}</a>
                        <div class="layui-upload-list">
                            <img ${def1.length > 0 ? `src='${def1}' ` : "src='data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7'"}class="layui-upload-img" id="image_show_${id}" style="width: 150px;height: 150px;background-color: #737383"  alt="图片">
                            <p id="image_text_${id}"></p>
                        </div>
                        </div>
              </div>
              <script>
    setTimeout(function (){
        let uploadInst = upload.render({
            elem: '#image_upload_${id}'
            , url: '${gzb.api.upload}'
            , before: function (obj) {
                obj.preview(function (index, file, result) {
                    $('#image_show_${id}').attr('src', result); 
                });
            }
            , done: function (res) { 
                if(gzb.jsonVerify(res,true)) { 
                    $("#${id}_query_where_0").val(res.data[0].sysFileId);
                 }
            }
            , error: function () {
                let demoText = $('#image_text_${id}');
                demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function () {
                    uploadInst.upload();
                });
            }
        });
    },300)
</script>`;
    html += "<script></script>";

    return html;
}


gzb.createFileHTML = function (title, id, fileTem, def0) {
    if (fileTem == null) {
        return "";
    }
    let def1 = "";
    if (def0 != null) {
        def1 = fileTem.toString().replace("\${" + id + "}", def0);
    }
    let html = gzb.createInputHTML(title, id, null, null, null, def0, "layui-form-item");
    html += `<div class="layui-form-item">
                        <label class="layui-form-label"></label>
                        <div class="layui-input-block">
                        <button type="button" class="layui-btn" id="image_upload_${id}"><i class="layui-icon"></i>选择文件</button>
                        <a href="${def1}">下载地址：${def1}</a>
                        </div>
              </div>
              <script>
    setTimeout(function (){ 
        let uploadInst = upload.render({
            elem: '#image_upload_${id}'
            , url: '${gzb.api.upload}'
            , accept: 'file' 
            , before: function (obj) {
             console.log(obj)
            }
            , done: function (res) { 
                if(gzb.jsonVerify(res,true)) { 
                    $("#${id}_query_where_0").val(res.data[0].sysFileId);
                }
            }
            , error: function () {
                let demoText = $('#image_text_${id}');
                demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function () {
                    uploadInst.upload();
                });
            }
        });
    },300)
</script>`;
    html += "<script></script>";

    return html;
}


gzb.createSelectHTML = function (title, field, option, width, def, sign, ele_id, select_event) {
    let id = ele_id == null ? field + "_query_where_0" : ele_id;
    if (def == null) {
        def = "";
    }
    let html = `<div class="${sign == null ? 'layui-inline' : sign}" >
                        <label class="layui-form-label">${title}</label>
                        
                        <div class="${sign == null || sign == "layui-inline" ? 'layui-input-inline' : "layui-input-block"}" style="${width == null ? '' : 'width:' + width + 'px'}">`;
    html += `<select id="${id}" lay-search="" lay-filter="${id}">`
    html += ` <option value="" selected>请选择...</option>`;
    if (option.toString().startsWith("req:")) {
        //引用类型
        let op_req = JSON.parse(option.toString().substring(4));
        op_req.data = gzb.replaceAll(op_req.data, `\${${field}}`, def);
        op_req.url = gzb.replaceAll(op_req.url, `\${${field}}`, def);
        if (op_req.met == null) {
            op_req.met = "GET";
        }
        let res = gzb.sysOptionUrl(op_req.url, op_req.met, op_req.data);

        for (let op of res) {
            html += ` <option value="${op[op_req.val]}" ${def.toString() == op[op_req.val].toString() ? 'selected' : ''}>${op[op_req.title]}</option>`;
        }
    } else {
        //选项
        for (let op of option) {
            html += ` <option value="${op['sysOptionValue']}" ${def.toString() == op['sysOptionValue'].toString() ? 'selected' : ''}>${op['sysOptionTitle']}</option>`;
        }
    }
    html += "</select></div></div>";
    if (select_event != null) {
        form.on('select(' + id + ')', function (data) {
            select_event(data.value, data.elem[data.elem.selectedIndex].text, data.elem);
        });
    }
    return html;
}
//_query_where_0
gzb.createInputHTML = function (title, field, date, number, width, def, sign, ele_id, input_event) {
    let id = ele_id == null ? field + "_query_where_0" : ele_id;
    let html = "<script>setTimeout(function (){";
    if (date != null && date.length > 0) {
        html += `laydate.render({
                elem: '#${id}'
                , theme: '#393D49'
                , type: '${date}'
            });`;

        if (def != null) {
            def = new Date().Format(def)
        }
    }
    if (def == null) {
        def = "";
    }
    html += "},100);</script>";

    html += `<div class="${sign == null ? 'layui-inline' : sign}">
                        <label class="layui-form-label">${title}</label>
                        <div class="${sign == null || sign == "layui-inline" ? 'layui-input-inline' : "layui-input-block"}" style="${width == null ? '' : 'width:' + width + 'px'}">
                            <input type="${number != null && number == "1" ? 'number' : 'text'}" id="${id}" placeholder="请输入 ${title}" class="layui-input" value="${def}">
                        </div>
                    </div>
<script>
$('#${id}').on('input', function() {
let fun1=gzb.events.input_event['${id}'];
if(fun1!=null) { 
    fun1($(this).val(),'${title}',$(this));
 }
});
</script>`
    let tid = null;
    gzb.events.input_event[id] = function (val, title, ele) {
        console.log('事件  input input ', id, val);
        if (tid != null) {
            clearTimeout(tid)
        }
        tid = setTimeout(function () {
            if (input_event != null) {
                input_event(val, title, ele);
            }
        }, 1000);
    }
    return html;
}
gzb.createTextareaHTML = function (title, field, date, number, width, def, sign, ele_id, input_event) {
    let id = ele_id == null ? field + "_query_where_0" : ele_id;
    let html = "<script>setTimeout(function (){";
    if (date != null && date.length > 0) {
        html += `laydate.render({
                elem: '#${id}_query_where_0'
                , theme: '#393D49'
                , type: '${date}'
            });`;

        if (def != null) {
            def = new Date().Format(def)
        }
    }
    if (def == null) {
        def = "";
    }
    html += "},100);</script>";

    html += `<div class="${sign == null ? 'layui-inline' : sign}">
                        <label class="layui-form-label">${title}</label>
                        <div class="${sign == null || sign == "layui-inline" ? 'layui-input-inline' : "layui-input-block"}" style="${width == null ? '' : 'width:' + width + 'px'}">
                        <textarea name="text" placeholder="请输入 ${title}" class="layui-textarea" id="${id}">${def}</textarea>
                        </div>
                    </div>
        <script>
        $('#${id}').on('input', function() {
            let fun1=gzb.events.input_event['${id}'];
            if(fun1!=null) {
                fun1($(this).val(),'${title}',$(this));
            }
        });
</script>`
    let tid = null;
    gzb.events.input_event[id] = function (val, title, ele) {
        console.log('事件  textarea input ', id, val, gzb.events.input_event[id]);
        if (tid != null) {
            clearTimeout(tid)
        }
        tid = setTimeout(function () {
            if (input_event != null) {
                input_event(val, title, ele);
            }
        }, 1000);
    }
    return html;
}

gzb.getTableImage = function (url, height) {
    if ($("#table-image-height").text() === "") {
        height = height == null ? 30 : height
        $("head").append("<style id='table-image-height'>td > .layui-table-cell{height: " + height + "px;} </style>")
    }
    return "<img style='width: auto;max-height: 100%;' src='" + url + "' alt=\"图片\"" +
        "onclick='layer.photos({photos: {\"code\": 0,\"msg\": \"大图\",\"title\": \"大图\",\"id\": 1,\"start\": 0,\"data\": [{\"alt\": \"大图预览\",\"pid\": 1,\"src\": \"" + url + "\",\"thumb\": \"\"}]}});' />";
}
gzb.appendButton = function (html) {
    $("#but_list_gzb_one_01").append(html)
}

// 获取选中项
gzb.readTableSelectData = function (id) {
    let checkStatus = table.checkStatus(id);
    return checkStatus.data;
}
//////////////////////  获取映射信息 并且存入gzb.mapping 会缓存 /////////////////////////
gzb.readMapping = function () {
    if (gzb.mapping == null) {
        let res = httpGet(gzb.api.mapping + "?sysRoleTableName=" + gzb.entity_name)

        if (!gzb.jsonVerify(res, false)) {
            return;
        }
        gzb.mapping = replaceEmptyStringsWithNull(res.data);
    }
    console.log("gzb.readMapping", gzb.mapping)
    return gzb.mapping;
}


//////////////////////  获取选项 /////////////////////////
gzb.sysOptionUrl = function (url, met, data, titleName, valName) {
    let key = url + "-" + met + "-" + data+ "-" + titleName+ "-" + valName;
    if (gzb.options[key] == null) {
        let res = httpRequest(url, met, data);
        if (!gzb.jsonVerify(res, false)) {
            console.log("sys OptionUrl 请求异常：", url, met, data, res)
            gzb.options[key] = [];
        } else {
            //{"sysOptionTitle": "请选择.....", "sysOptionValue": ""}
            let data0 = [];
            for (let json of res.data) {
                if (titleName == null || valName == null || json[titleName] == null) {
                    continue;
                }
                data0.push({"sysOptionTitle": json[titleName], "sysOptionValue": json[valName]})
            }
            gzb.options[key] = data0;
            console.log("sys OptionUrl 请求",key, gzb.options[key])
        }
    } else {
        console.log("sys OptionUrl 缓存",key, gzb.options[key])
    }
    return gzb.options[key];
}
gzb.replaceAll = function (str, src, desc) {
    let str1 = str.trim();
    while (str1.indexOf(src) > -1) {
        str1 = str1.replace(src, desc);
    }
    return str1;
}
gzb.selectRequest = function (req, data, search,field) {
    if (req == null) {
        return [];
    }
    if (data[field] == null) {
        data[field]="";
    }
    let req_url_sec=req.sysOptionRequestSearchUrl;
    let req_data_sec=req.sysOptionRequestSearchData;
    let req_url=req.sysOptionRequestUrl.toString();
    let req_data=req.sysOptionRequestData;
    //替换对象
    if (data != null) {
        for (let key in data) {
            let str1 = "${"+key+"}";
            let val01=data[key];
            if (val01 == null) {
                val01 = "";
            }
            if (req.sysOptionRequestUrl != null) {
                req_url = req_url.replace(str1,val01);
            }
            if (req.sysOptionRequestData != null) {
                req_data = req_data.replace(str1, val01);
            }
            if (req.sysOptionRequestSearchUrl != null) {
                req_url_sec= req_url_sec.replace(str1,val01);
            }
            if (req.sysOptionRequestSearchData != null) {
                req_data_sec = req_data_sec.replace(str1, val01);
            }
        }
    }
    let res;
    if (search) {
        res = gzb.sysOptionUrl(req_url_sec, req.sysOptionRequestSearchMet, req_data_sec, req.sysOptionRequestSearchTitleName, req.sysOptionRequestSearchValName);
    } else {
        res = gzb.sysOptionUrl(req_url, req.sysOptionRequestMet,req_data, req.sysOptionRequestTitleName, req.sysOptionRequestValName);
    }
    return res;
}
gzb.initTable = function () {
    if (gzb.table.height === 0) {
        gzb.table.height = window.innerHeight - document.getElementsByClassName("layui-card")[0].offsetHeight - 30
    }
    console.log("gzb.table.height", gzb.table.height)
    gzb.table.cols = [{type: 'checkbox'}];
    for (let res of gzb.mapping.data) {
        let _edit = res.sysRoleColumnEdit == null || res.sysRoleColumnEdit.toString() != "1" ? null : "text";
        let _width = res.data.sysMappingColumnWidth == null ? 100 : Number(res.data.sysMappingColumnWidth);
        let _title = res.data.sysMappingColumnTitle;
        let _field = res.data.sysMappingColumnName;
        let _templet = null;
        let _is_select = res.data.sysMappingColumnOption;
        let _is_text = res.data.sysMappingColumnText;
        let req01=null;
        console.log(_field,res.data)
        if (res.data.sysMappingColumnRequest != null) {
            req01=res.data.sysMappingColumnRequest;
            _is_select=[{"sysOptionTitle": "请选择.....", "sysOptionValue": ""}];
            _templet = function (d) {
                let arr1 = gzb.selectRequest(req01, d,false,_field);
                if (arr1 == null) {
                    gzb.toastFail("获取选项失败 "+_field+" "+_title);
                    return "";
                }
                if (d[_field] == null) {
                    return "";
                }
                //选项
                for (let op of arr1) {
                    if (d[_field] == op["sysOptionValue"]) {
                        return op["sysOptionTitle"]
                    }
                }
                return "";
            }
        }else if (_is_select != null && _is_select.length > 0) {
            _templet = function (d) {
                if (d[_field] == null) {
                    return "";
                }
                //选项
                for (let op of _is_select) {
                    console.log(_field,op)
                    console.log(_field,d[_field] , op["sysOptionValue"],d[_field] == op["sysOptionValue"])
                    if (d[_field] == op["sysOptionValue"]) {
                        return op["sysOptionTitle"]
                    }
                }
                return "";
            }
        } else if (res.data.sysMappingColumnImage != null) {
            _templet = function (d) {
                let def1 = "";
                if (d[res.data.sysMappingColumnName] != null) {
                    def1 = res.data.sysMappingColumnImage.toString().replace("\${" + res.data.sysMappingColumnName + "}", d[res.data.sysMappingColumnName]);
                }
                return gzb.getTableImage(def1);
            }
        }
        let _click_event = "table_col_click-" + _field
        if (_edit != null) {
            if (_is_select != null && _is_select.length > 0) {
                _edit = null;
                if (req01 == null) {
                    gzb.events.table_tool[_click_event] = function (data, obj) {
                        let event1 = function (val, title, ele) {
                            console.log(val, title, ele)
                            gzb.events.table_edit(data, _field, val, obj)
                        }
                        let html = `<div style="padding: 20px;"><div class="layui-form-item"><form class="layui-form" lay-filter="component-form-group">
                        ${gzb.createSelectHTML(_title, _field, _is_select, "30%", data[_field], "layui-form-item", _field + "_select_gzb_one", event1)}   </form></dic>     <script>gzb.init_ele()</script>  </div>`
                        layer.open({
                            title: _title + " 编辑",
                            type: 1,
                            closeBtn: 0,
                            shadeClose: true,
                            area: ['60%', '40%'],
                            content: html
                        });
                    }
                }else{
                    gzb.events.table_tool[_click_event] = function (data, obj) {
                        localStorage["编辑选择项-请求数据"]=JSON.stringify(req01);
                        localStorage["编辑选择项-本行数据"]=JSON.stringify(data);
                        localStorage["编辑选择项-当前列"]=JSON.stringify(_field);
                        gzb.openPage("option.html?config="+gzb.entity_name,"编辑选项","70%","80%",function (){
                            console.log("end")
                            obj.update(JSON.parse(localStorage["编辑选择项-本行数据"]))
                            localStorage.removeItem("编辑选择项-请求数据")
                            localStorage.removeItem("编辑选择项-本行数据")
                            localStorage.removeItem("编辑选择项-当前列")
                        })

                    }
                }

            } else if (_is_text != null && _is_text.toString() == "2") {
                _edit = null;
                gzb.events.table_tool[_click_event] = function (data, obj) {
                    let event1 = function (val, title, ele) {
                        console.log(val, title, ele)
                        gzb.events.table_edit(data, _field, val, obj)
                    }
                    let html = `<div style="padding: 20px;"><div class="layui-form-item"><form class="layui-form" lay-filter="component-form-group">
                    ${gzb.createTextareaHTML(_title, _field, null, null, "30%", data[_field], "layui-form-item", _field + "_select_gzb_one", event1)}   </form></dic>     <script>gzb.init_ele()</script>  </div>`
                    layer.open({
                        title: _title + " 编辑",
                        type: 1,
                        closeBtn: 0,
                        shadeClose: true,
                        area: ['60%', '40%'],
                        content: html
                    });
                }

            }
        }
        gzb.table.cols.push({
            field: _field,
            title: _title,
            minWidth: _width,
            sort: res.data.sysMappingColumnNumber != null && res.data.sysMappingColumnNumber == "1",
            event: _click_event,
            edit: _edit,
            templet: _templet
        })
    }
    let _width = gzb.mapping.sysRoleTableWidth;
    let _templet = function () {
        let html = "";
        if (gzb.mapping.sysRoleTableUpdate != null && gzb.mapping.sysRoleTableUpdate.toString() == "1") {
            html += (gzb.createButtonHTML("修改", 2, 2, null, null, null, false))
        }
        if (gzb.mapping.sysRoleTableDeleteSgin != null && gzb.mapping.sysRoleTableDeleteSgin.toString() == "1") {
            html += (gzb.createButtonHTML("删除", 4, 2, null, null, null, false))
        }
        //扩展 自定义按钮

        //补充按钮  table_tool_buttons  buttons
        for (let html0 of gzb.events.table_tool_buttons) {
            html += (html0)
        }
        return html;
    }
    gzb.table.cols.push({field: "but_table_gzb_one_01", title: "操作按钮", minWidth: _width, templet: _templet})

    //sysRoleColumnName
    let req_data = "";
    let url = "";
    if (gzb.api.query_data != null) {
        let req_0 = gzb.joinParar(gzb.api.query, gzb.api.query_data)
        url = req_0[0];
        req_data = req_0[1];
    } else {
        url = gzb.api.query;
    }

    for (let col of gzb.mapping.data) {
        if (col.sysRoleColumnName == null || col.sysRoleColumnName.length == 0 || col.sysRoleColumnQuery == null || col.sysRoleColumnQuery != "1") {
            continue;
        }
        let val = $("#" + col.sysRoleColumnName + "_query_where_0").val();
        if (val == null || val.length == 0) {
            continue;
        }
        req_data += `field=${col.sysRoleColumnName}&value=${val}&symbol=${col.sysRoleColumnQuerySymbol}&montage=${col.sysRoleColumnQueryMontage}&`
    }
    let met = gzb.api.query_met.toUpperCase();
    let req_0 = gzb.joinParar(url, req_data);
    req_data = req_0[1];
    if (gzb.hook[met] != null) {
        for (let fun of gzb.hook[met]) {
            req_data = fun(req_data);
        }
    }
    table.render({
        elem: '#' + gzb.table_id
        , url: req_data != null && req_data.length > 0 ? url + "?" + req_data : url
        , method: gzb.api.query_met
        , where: null //大垃圾 连字符串拼接都不支持
        , cols: [gzb.table.cols]
        , height: gzb.table.height
        , title: '数据表'
        , toolbar: true
        , page: true
        , limit: gzb.table.limit
        , limits: gzb.table.limits
        , response: {
            statusCode: gzb.table.statusCodeSuccess
        }
        , parseData: gzb.table.parseData
    })
}
gzb.init.public = function () {
    gzb.table_id = "table_gzb_one_01";
    gzb.page = {
        list: "list.html",//表格界面
        save: "save.html",//新增界面
        update: "update.html",//修改编辑界面
    }
    gzb.params = gzb.getUrlParar();
    if (gzb.params["config"] != null) {
        for (let con of gzb.params["config"]) {
            loadScript("js/entity/" + con + ".js");//加载js文件
        }
    }
}
//初始化 list页面
gzb.init.list = function () {
    gzb.init.public();

    let data = gzb.readMapping();

    let ele = $("#gzb_one_ui_main");
    ele.html(`<br/>`);

    //生成查询条件
    let html = `<div class="layui-form-item">
                    <form class="layui-form" lay-filter="component-form-group">`;
    console.log("data 1 ",data)
    for (let col of data.data) {
        if (col == null) {
            continue;
        }
        //添加引用或者input
        html += gzb.createFrom(col, 1);
    }
    html += "</form></div>"
    ele.append(html)
    //装载按钮信息
    ele.append(`<div class="layui-card-body"><div class="layui-form-item" id="but_list_gzb_one_01"><div class="layui-input-inline">`)
    if (data.sysRoleTableQuery != null && data.sysRoleTableQuery.toString() == "1") {
        gzb.appendButton(gzb.createButtonHTML("查 询", 0, 0, "but_query_gzb_one_01", gzb.events.table_query, null, false))
    }
    if (data.sysRoleTableSave != null && data.sysRoleTableSave.toString() == "1") {
        gzb.appendButton(gzb.createButtonHTML("新 增", 2, 0, "but_save_gzb_one_01", gzb.events.table_save, null, false))
    }
    if (data.sysRoleTableDelete != null && data.sysRoleTableDelete.toString() == "1") {
        gzb.appendButton(gzb.createButtonHTML("删 除", 4, 0, "but_delete_gzb_one_01", gzb.events.table_delete, null, false))
    }
    //补充按钮  table_tool_buttons  buttons
    for (let html of gzb.events.buttons) {
        gzb.appendButton(html)
    }
    ele.append(`</div></div></div>`)

    //装载 table
    ele.append(`                <div class="layui-card-body">
                    <table class="layui-hide" id="${gzb.table_id}" lay-filter="${gzb.table_id}"></table>
                </div>`);
    //重新渲染
    gzb.init_ele();
    //渲染表格元数据
    gzb.initTable();
    //监听单元格编辑
    table.on("edit(" + gzb.table_id + ")", function (obj) {
        let value = obj.value //得到修改后的值
            , data = obj.data //得到所在行所有键值
            , field = obj.field; //得到字段
        console.log("表格编辑", field, value)
        gzb.events.table_edit(data, field, value, obj);
    });
    //监听工具条
    table.on('tool(' + gzb.table_id + ')', function (obj) {
        let data = obj.data;
        console.log("表格事件", obj.event, data)
        gzb.click_time_ver();
        let fun = gzb.events.table_tool[obj.event];
        if (fun != null) {
            fun(data, obj);
        }

    });
}

gzb.init.update = function () {
    gzb.init.public();
    let data = gzb.readMapping();
    let ele = $("#gzb_one_ui_main");
    ele.html(`<br/>`);
    //生成查询条件
    let html = `<br/><div class="layui-form-item">
                    <form class="layui-form" lay-filter="component-form-group">`;
    for (let col of data.data) {
        if (col == null) {
            continue;
        }
        //添加引用或者input
        html += gzb.createFrom(col, 2);
    }
    html += "</form></div>"
    ele.append(html)
    //装载按钮信息
    ele.append(`<div class="layui-card-body"><div class="layui-form-item" id="but_list_gzb_one_01"><div class="layui-input-inline">`)

    gzb.appendButton(gzb.createButtonHTML("重置", 2, 0, "but_gzb_one_02", gzb.update_page_reset, null, false))
    gzb.appendButton(gzb.createButtonHTML("修改", 4, 0, "but_gzb_one_03", gzb.update_page_update, null, false))
    gzb.appendButton(gzb.createButtonHTML("复制并保存", 3, 0, "but_gzb_one_04", gzb.update_page_copy, null, false))

    ele.append(`</div></div></div>`)

    gzb.init_ele();
}
gzb.init.save = function () {
    gzb.init.public();
    let data = gzb.readMapping();
    let ele = $("#gzb_one_ui_main");
    ele.html(`<br/>`);
    //生成查询条件
    let html = `<br/><div class="layui-form-item">
                    <form class="layui-form" lay-filter="component-form-group">`;
    for (let col of data.data) {
        if (col == null) {
            continue;
        }
        //添加引用或者input
        html += gzb.createFrom(col, 3);
    }
    html += "</form></div>"
    ele.append(html)
    //装载按钮信息
    ele.append(`<div class="layui-card-body"><div class="layui-form-item" id="but_list_gzb_one_01"><div class="layui-input-inline">`)

    gzb.appendButton(gzb.createButtonHTML("清空", 2, 0, "but_gzb_one_02", gzb.save_page_clear, null, false))
    gzb.appendButton(gzb.createButtonHTML("保存", 4, 0, "but_gzb_one_03", gzb.save_page_save, null, false))

    ele.append(`</div></div></div>`)
    gzb.init_ele();
}
//////////////////////  打开子页面 /////////////////////////

gzb.openPage = function (url, title, w, h, endFun) {
    if (w == null) {
        w = "80%"
    }
    if (h == null) {
        h = "80%"
    }
    layer.open({
        type: 2,
        title: title,
        shadeClose: true,
        closeBtn: 0,
        shade: 0.5,
        area: [w, h],
        content: url,
        end: endFun
    });
}

//////////////////////  注册事件 /////////////////////////
gzb.events.table_query = function () {
    gzb.initTable();
}
gzb.events.table_save = function () {
    localStorage.removeItem(gzb.update_data_key)
    gzb.openPage(gzb.page.save + "?config=" + gzb.entity_name, "添加", "80%", "80%", function () {
        console.log("添加页面 关闭");
        localStorage.removeItem(gzb.update_data_key);
        location.reload();
    })
}
gzb.events.table_delete = function () {
    let data = gzb.readTableSelectData(gzb.table_id);
    if (data.length < 1) {
        return;
    }
    layer.confirm('确定删除选中的<span style="color: #cc0000;font-size: 20px">[ ' + data.length + ' ]</span>条数据吗？', function () {
        let path = gzb.api.delete + "?"
        for (let obj of data) {
            let id = obj[gzb.entity_id];
            if (id == null) {
                gzb.toastFail("没检测到ID：" + gzb.entity_id)
            }
            path += `${gzb.entity_id}=${id}&`;
        }
        gzb.delete(path, null, function (res) {
            //根据响应数据自动弹出提示 或跳转页面
            if (gzb.jsonVerify(res, true)) {
                table.reload(gzb.table_id);
            }
        });
    });
}
gzb.events.table_tool["修改"] = function (data, obj) {
    localStorage[gzb.update_data_key] = JSON.stringify(data);//需要在 修改界面更改值 才会更新
    gzb.openPage(gzb.page.update + "?config=" + gzb.entity_name, "编辑", "80%", "80%", function () {
        let data2 = JSON.parse(localStorage[gzb.update_data_key]);
        localStorage.removeItem(gzb.update_data_key)
        console.log("修改页面 关闭", data2)
        obj.update(data2);
    })
}
gzb.events.table_tool["删除"] = function (data, obj) {
    layer.confirm('确定删除该行数据吗？', function () {
        let path = gzb.api.delete + "?"
        path += `${gzb.entity_id}=${encodeURIComponent(data[gzb.entity_id])}&`;

        gzb.delete(path, null, function (res) {
            //根据响应数据自动弹出提示 或跳转页面
            if (gzb.jsonVerify(res, true)) {
                obj.del();
            }
        });
    });
}
gzb.events.table_edit = function (data, key, val, obj) {
    console.log("编辑", key, val, obj, data)
    data[key] = (val);
    gzb.post(gzb.api.update, data, function (res) {
        if (gzb.jsonVerify(res, true)) {
            data[key] = val;
            if (obj != null) {
                obj.update(data)
            }
        }
    });
}
//修改界面按钮
gzb.update_page_update = function () {
    let req_data = "";
    for (let col of gzb.mapping.data) {
        let val = $("#" + col.sysRoleColumnName + "_query_where_0").val();
        if (val == null || val.length === 0) {
            continue;
        }
        req_data += `${col.sysRoleColumnName}=${encodeURIComponent(val)}&`
    }
    gzb.post(gzb.api.update, req_data, function (res) {
        if (gzb.jsonVerify(res, true)) {
            let new_data = {};
            for (let col of gzb.mapping.data) {
                let val = $("#" + col.sysRoleColumnName + "_query_where_0").val();
                if (val == null || val.length === 0) {
                    new_data[col.sysRoleColumnName] = null;
                    continue;
                }
                new_data[col.sysRoleColumnName] = val
            }
            localStorage[gzb.update_data_key] = JSON.stringify(new_data);
        }
    })
}
gzb.update_page_reset = function () {
    location.reload();
}
//插入界面按钮
gzb.save_page_save = function () {
    let req_data = "";
    for (let col of gzb.mapping.data) {
        let val = $("#" + col.sysRoleColumnName + "_query_where_0").val();
        if (val == null || val.length === 0) {
            continue;
        }

        req_data += `${col.sysRoleColumnName}=${encodeURIComponent(val)}&`
    }
    gzb.post(gzb.api.save, req_data, function (res) {
        gzb.jsonVerify(res, true);
    })
}

gzb.update_page_copy = function () {
    layer.confirm('确定要将本行数据复制到一个新创建的行吗？', function () {
        let req_data = "";
        for (let col of gzb.mapping.data) {
            if (col.sysRoleColumnName == gzb.entity_id) {
                continue;
            }
            let val = $("#" + col.sysRoleColumnName + "_query_where_0").val();
            if (val == null || val.length === 0) {
                continue;
            }

            req_data += `${col.sysRoleColumnName}=${encodeURIComponent(val)}&`
        }
        gzb.post(gzb.api.save, req_data, function (res) {
            gzb.jsonVerify(res, true);
        })

    });

}

gzb.save_page_clear = function () {
    location.reload();
}
//////////////////////  注册 自定义按钮 /////////////////////////
gzb.registerButton = function (title, color, size, id, event) {
    gzb.events.buttons.push(gzb.createButtonHTML(title, color, size, id, event, null, false))
}
gzb.registerTableButton = function (title, color, size, event) {
    gzb.events.table_tool[title] = event;
    gzb.events.table_tool_buttons.push(gzb.createButtonHTML(title, color, size, null, null, null, false))
}

//////////////////////// 注册 HOOK范围 gzb.get post put delete request 如需hook更多请调用 HOOK.js
gzb.registerGetDataHook = function (callBack) {
    gzb.hook["GET"].push(callBack);
}
gzb.registerPostDataHook = function (callBack) {
    gzb.hook["POST"].push(callBack);
}
gzb.registerPutDataHook = function (callBack) {
    gzb.hook["PUT"].push(callBack);
}
gzb.registerDeleteDataHook = function (callBack) {
    gzb.hook["DELETE"].push(callBack);
}
gzb.click_time = 0;
gzb.click_time_ver = function () {
    let time = new Date().getTime();
    if (time - gzb.click_time < 500) {
        throw new Error("点击频率过高");
    }
    gzb.click_time = time;
}
gzb.registerEvent = function (ele_id, event, type) {
    if (ele_id == null) {
        ele_id = gzb.getUUid();
    }
    if (type == null) {
        type = "click";
    }
    console.log("注册 事件", type, ele_id)
    $("body").on(type, '#' + ele_id, function () {
        console.log("触发 事件", type, ele_id)
        gzb.click_time_ver();
        event(this);
    });
}