// ==UserScript==
// @name         GZB-框架
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  Load and execute a remote script
// @author       Your Name
// @include      https://y.tuwan.com/*
// @grant        GM_xmlhttpRequest
// @icon         data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==
// ==/UserScript==
var globalWindow = (function () {
    return this;
})();
(async function () {
    'use strict';
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
    if (window.hook_json_parse_fun_aray==null) {
        window.hook_json_parse_fun_aray=[];
    }
    if (window.hook_json_stringify_fun_aray==null) {
        window.hook_json_stringify_fun_aray=[];
    }
    if (window.hook_http_fun_array==null) {
        window.hook_http_fun_array=[];
    }
    if (window.activation_fun_array==null) {
        window.activation_fun_array=[];
    }
    let gzb = {};
    gzb.lv = 1002
    gzb.state = 0;
    gzb.tid = 0;
    gzb.map01 = {}
    gzb.deBug = true;
    gzb.config = {"show": "0"};
    gzb.configPageData = []
    /*    gzb.configPageData = [
            {"type": "input", "title": "设备名", "defValue": "", "desc": ""},
            {"type": "textarea", "title": "图片名称", "defValue": "", "desc": ""},
            {
                "type": "select",
                "title": "脚本开关",
                "options": [
                    {
                        "title": "开启功能",
                        "value": "1",
                        "selected": false
                    },
                    {
                        "title": "停用功能",
                        "value": "0",
                        "selected": true
                    }]
            },
            {"type":"title","title":"配置界面"},
        {"type":"p","title":"授权截止日期:","id":"p-01"},
        {"type":"button","id":"button-01","title":"验证授权卡有效性","click":function() {

        }}
        ]*/

    //nextElementSibling 返回当前元素的下一个元素节点（忽略文本节点、注释节点等）
    //nextSibling 返回当前节点的下一个任意类型节点（包括文本节点、注释节点等）。
    //previousElementSibling：获取前一个元素节点
    //previousSibling：获取前一个任意类型节点。
    //parentElement 获取父元素
    //children获取子元素
    //gzb.find("span", "聊天", 1, false, false, 100, 1000)
    gzb.find = function (tag, text, index, link, click) {
        tag = tag == null ? "*" : tag;
        text = text == null ? "" : text;
        index = index == null ? 1 : index;
        link = link == null ? false : link;
        click = click == null ? false : click;
        let nodes = document.getElementsByTagName(tag);
        let resArray = [];
        if (text != null) {
            text = text.replace(/\n/g, "")
            text = text.replace(/ /g, "")
            text = text.replace(/   /g, "")
        }
        let sucNum = 0;
        for (let node of nodes) {
            if (node) {
                let data1 = node.innerText;
                if (data1 != null) {
                    data1 = data1.replace(/\n/g, "")
                    data1 = data1.replace(/ /g, "")
                    data1 = data1.replace(/   /g, "")
                }
                if (link) {
                    if (data1.indexOf(text) > -1) {
                        sucNum++;
                    }
                } else {
                    if (data1 == text) {
                        sucNum++;
                    }
                }
                if (sucNum == index || index == -1) {
                    if (click) {
                        gzb.clickNode(node);
                        gzb.log("find true", tag, text, index, link, click)
                    }
                    if (index == -1) {
                        resArray.push(node)
                    } else {
                        return node;
                    }
                }

            }
        }
        if (index == -1) {
            if (resArray.length == 0) {
                gzb.log("find false", tag, text, index, link, click)
                return null;
                ;
            } else {
                return resArray;
            }
        } else {
            gzb.log("find false", tag, text, index, link, click)
            return null;
        }
    };
    gzb.inputText = async function (element, text) {
        if (!element) {
            console.error('传入的元素不存在');
            return;
        }

        async function forceUpdateRender() {
            await new Promise(resolve => requestAnimationFrame(resolve));
            await new Promise(resolve => setTimeout(resolve, 200));
        }

        let getEventListeners = function (element) {
            return element._getEventListeners ? element._getEventListeners() : {};
        }
        // 保存原始的事件监听器
        const originalEventListeners = {};
        const eventsToSave = ['mouseover', 'focus'];
        eventsToSave.forEach(event => {
            const listeners = getEventListeners(element)[event];
            if (listeners) {
                originalEventListeners[event] = listeners;
                listeners.forEach(listener => {
                    element.removeEventListener(event, listener.listener);
                });
            }
        });

        // 模拟鼠标悬停和聚焦
        const mouseoverEvent = new Event('mouseover', {bubbles: true});
        element.dispatchEvent(mouseoverEvent);
        element.focus();

        // 等待页面响应聚焦事件
        await new Promise(resolve => setTimeout(resolve, 100));

        const keyCode = text.charCodeAt(0);

        // 触发 compositionstart 事件（模拟输入法开始输入）
        const compositionstartEvent = new Event('compositionstart', {bubbles: true});
        element.dispatchEvent(compositionstartEvent);

        const keydownEvent = new KeyboardEvent('keydown', {
            bubbles: true,
            cancelable: true,
            key: text,
            code: `Key${text.toUpperCase()}`,
            keyCode: keyCode,
            which: keyCode
        });
        element.dispatchEvent(keydownEvent);

        if (element.tagName === 'INPUT' || element.tagName === 'TEXTAREA') {
            element.value += text;
        } else if (element.hasAttribute('contenteditable') && element.getAttribute('contenteditable') === 'true') {
            const textNode = document.createTextNode(text);
            let range = document.createRange();
            const selection = window.getSelection();

            if (selection.rangeCount > 0) {
                range = selection.getRangeAt(0);
            } else {
                range.selectNodeContents(element);
                range.collapse(false);
            }
            range.insertNode(textNode);
            range.collapse(true);
            selection.removeAllRanges();
            selection.addRange(range);
        } else {
            console.log("else001")
        }

        // 触发 compositionupdate 事件（模拟输入法输入过程）
        const compositionupdateEvent = new Event('compositionupdate', {bubbles: true});
        element.dispatchEvent(compositionupdateEvent);

        const inputEvent = new Event('input', {
            bubbles: true,
            cancelable: true
        });
        element.dispatchEvent(inputEvent);

        const keyupEvent = new KeyboardEvent('keyup', {
            bubbles: true,
            cancelable: true,
            key: text,
            code: `Key${text.toUpperCase()}`,
            keyCode: keyCode,
            which: keyCode
        });
        element.dispatchEvent(keyupEvent);

        // 触发 compositionend 事件（模拟输入法输入结束）
        const compositionendEvent = new Event('compositionend', {bubbles: true});
        element.dispatchEvent(compositionendEvent);

        const delay = 0;//Math.random() * 100 + 50;
        await new Promise(resolve => setTimeout(resolve, delay));

        const changeEvent = new Event('change', {
            bubbles: true,
            cancelable: true
        });
        element.dispatchEvent(changeEvent);

        const blurEvent = new Event('blur', {
            bubbles: true,
            cancelable: true
        });
        element.dispatchEvent(blurEvent);

        // 恢复原始的事件监听器
        Object.keys(originalEventListeners).forEach(event => {
            originalEventListeners[event].forEach(listener => {
                element.addEventListener(event, listener.listener);
            });
        });

        // 强制更新页面渲染
        await forceUpdateRender();
        gzb.log("inputText", text)
    }
    gzb.clickNode = function (element) {
        if (!element) {
            console.error('Element is not provided or does not exist.');
            return;
        }
        try {
            // 获取元素在页面中的位置和尺寸
            const rect = element.getBoundingClientRect();
            // 计算元素中心点的坐标 随机 4/2位置
            let a = rect.width / 4;
            let b = rect.height / 4;
            let clientX = gzb.random(parseInt(rect.left + (a * 1)), parseInt(rect.left + (a * 3)))
            let clientY = gzb.random(parseInt(rect.top + (b * 1)), parseInt(rect.top + (b * 3)))
            gzb.log("clickNode", clientX, clientY)

            // 创建一个通用的鼠标事件创建函数
            function createMouseEvent(type) {
                return new MouseEvent(type, {
                    view: globalWindow,
                    bubbles: true,
                    cancelable: true,
                    clientX: clientX,
                    clientY: clientY
                });
            }

            // 模拟按下鼠标的延迟
            let pressDelay = Math.random() * 100 + 50; // 50 - 150 毫秒之间的随机延迟
            setTimeout(() => {
                // 触发 mousedown 事件
                const mouseDownEvent = createMouseEvent('mousedown');
                element.dispatchEvent(mouseDownEvent);

                // 模拟按下和释放之间的延迟
                const holdDelay = Math.random() * 100 + 50; // 50 - 150 毫秒之间的随机延迟
                setTimeout(() => {
                    // 触发 mouseup 事件
                    const mouseUpEvent = createMouseEvent('mouseup');
                    element.dispatchEvent(mouseUpEvent);
                    // 触发 click 事件
                    const clickEvent = createMouseEvent('click');
                    element.dispatchEvent(clickEvent);
                }, holdDelay);
            }, pressDelay);

        } catch (e) {
            console.log(e)
            element.click();
        }
    }
    gzb.findArray = function (arr, data) {
        for (let i = 0; i < arr.length; i++) {
            if (arr[i] == data) {
                return i;
            }
        }
        return -1;
    }
    gzb.uploadLocalFiles = function (inputSelector, filePaths) {
        const inputElement = document.querySelector(inputSelector);
        if (!inputElement) {
            return;
        }
        const dataTransfer = new DataTransfer();
        let processedFiles = 0;

        function processNextFile() {
            if (processedFiles >= filePaths.length) {
                if (dataTransfer.files.length === 0) {
                    console.error('没有有效的文件可供上传');
                } else {
                    inputElement.files = dataTransfer.files;
                    const changeEvent = new Event('change', {bubbles: true});
                    inputElement.dispatchEvent(changeEvent);
                }
                return;
            }

            const filePath = filePaths[processedFiles];
            const fixedFilePath = filePath.replace(/\\/g, '/');

            GM_xmlhttpRequest({
                method: 'GET',
                url: `${fixedFilePath}`,
                responseType: 'blob',
                onload: function (response) {
                    if (response.status === 200) {
                        const blob = response.response;
                        const fileName = filePath.split(/[\\/]/).pop();
                        const file = new File([blob], fileName, {type: blob.type});
                        dataTransfer.items.add(file);
                    } else {
                        console.error(`读取文件 ${filePath} 失败，状态码：${response.status}`);
                    }
                    processedFiles++;
                    processNextFile();
                },
                onerror: function (error) {
                    console.error(`处理文件 ${filePath} 时出错：`, error.error || error.statusText || '未知错误');
                    processedFiles++;
                    processNextFile();
                }
            });
        }

        processNextFile();
    }
    gzb.random = function (min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;

    }
    gzb.request = async function (url, met, data) {
        if (data == null) {
            data = ""
        }
        gzb.log("gzb.request", url, met, data)
        //application/x-www-form-urlencoded
        //application/json;charset=UTF-8
        let res = null;
        GM_xmlhttpRequest({
            method: met,
            url: url,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            data: data,
            onload: function (response) {
                if (response.status === 200) {
                    try {
                        let res2 = response.responseText;
                        try {
                            res = JSON.parse(res2)
                        } catch (e) {
                            console.log(e)
                            res = res2;
                        }
                    } catch (error) {
                        console.error("解析响应数据时出错:", error);
                    }
                } else {
                    console.error("POST 请求失败，状态码:", response.status);
                }
            },
            onerror: function (error) {
                console.error("请求发生错误:", error);
            }
        });
        let im = 0;
        while (true) {
            im++
            await gzb.sleep(10)
            if (res != null || im > 1000) {
                break;
            }
        }
        return res;
    };

    gzb.startHookJSON = function () {
        if (window.originalStringify == null) {
            window.originalStringify= JSON.stringify;
        }
        if (window.originalParse == null) {
            window.originalParse= JSON.parse;
        }
        JSON.stringify = function (value, replacer, space) {
            const result = window.originalStringify(value, replacer, space);
            try {
                for (let obj of window.hook_json_stringify_fun_aray) {
                    if (obj == null) {
                        continue;
                    }
                    if (obj.key == null || obj.val == null || value[obj.key] == obj.val) {
                        let res = obj.fun(value, result);
                        if (res != null) {
                            return res;
                        }
                    }
                }
            } catch (e) {
                gzb.log(e)
            }
            return result;
        };
        JSON.parse = function (text, reviver) {
            const result = window.originalParse(text, reviver);
            try {
                for (let obj of window.hook_json_parse_fun_aray) {
                    if (obj == null) {
                        continue;
                    }
                    if (obj.key == null || obj.val == null || result[obj.key] == obj.val) {
                        let res = obj.fun(text, result);
                        if (res != null) {
                            return res;
                        }
                    }
                }
            } catch (e) {
                gzb.log(e)
            }
            return result;
        };
    }
    gzb.addHookJSONParse = function (key, val, fun) {
        window.hook_json_parse_fun_aray.push({
            "key": key,
            "val": val,
            "fun": fun
        });
    }
    gzb.addHookJSONStringify = function (key, val, fun) {
        window.hook_json_stringify_fun_aray.push({
            "key": key,
            "val": val,
            "fun": fun
        });
    }
    gzb.delHookJSONParse = function (key) {
        let arr1=[]
        for (let activationFunArrayElement of window.hook_json_parse_fun_aray) {
            if (activationFunArrayElement==null) {
                continue;
            }
            if (activationFunArrayElement["key"] == key) {
                continue;
            }
            arr1.push(activationFunArrayElement)
        }
        window.hook_json_parse_fun_aray= arr1
    }
    gzb.delHookJSONStringify = function (key) {
        let arr1=[]
        for (let activationFunArrayElement of window.hook_json_stringify_fun_aray) {
            if (activationFunArrayElement==null) {
                continue;
            }
            if (activationFunArrayElement["key"] == key) {
                continue;
            }
            arr1.push(activationFunArrayElement)
        }
        window.hook_json_stringify_fun_aray= arr1
    }
    gzb.startHookHTTP = function () {
        if (window.originalXHR == null) {
            window.originalXHR= window.XMLHttpRequest;
        }
        if (window.originalFetch == null) {
            window.originalFetch= window.fetch;
        }
        console.log("window.originalXHR",window.originalXHR)
        console.log("window.originalFetch",window.originalFetch)
        console.log("window.hook_http_fun_array",window.hook_http_fun_array)
        // 拦截 XMLHttpRequest
        window.XMLHttpRequest = function () {
            const xhr = new window.originalXHR();
            const originalOpen = xhr.open;
            const originalSend = xhr.send;

            xhr.open = function (method, url, async, user, password) {
                this._requestUrl = url;
                console.log("url",url)
                gzb.log(`XHR open called for URL: ${url}`);
                return originalOpen.call(this, method, url, async, user, password);
            };

            xhr.send = function (data) {
                const url = this._requestUrl;
                console.log("url",url)
                const hooks = window.hook_http_fun_array.filter(hook => url.includes(hook.url));
                if (hooks.length > 0) {
                    gzb.log(`XHR request to ${url} is being hooked.`);
                    const originalOnLoad = this.onload;
                    this.onload = function () {
                        gzb.log('XHR onload event triggered.');
                        let response = {
                            status: this.status,
                            statusText: this.statusText,
                            headers: this.getAllResponseHeaders(),
                            body: this.responseText,
                            responseText: this.responseText
                        };
                        hooks.forEach(hook => {
                            if (hook == null) {
                                return;
                            }
                            let res = hook.fun(response); // 仅调用处理函数记录日志等，不修改响应
                            if (res != null) {
                                response = res;
                            }
                        });
                        if (originalOnLoad) {
                            originalOnLoad.call(response);
                        }
                    };
                }
                gzb.log(`XHR sending data for URL: ${url}`);
                return originalSend.call(this, data);
            };
            return xhr;
        };

        // 拦截 fetch
        window.fetch = async function (input, init) {
            const url = typeof input === 'string' ? input : input.url;
            gzb.log(`Fetching request to: ${url}`);
            const hooks = window.hook_http_fun_array.filter(hook => url.includes(hook.url));
            const response = await window.originalFetch(input, init);
            if (hooks.length > 0) {
                gzb.log(`Fetch request to ${url} is being hooked.`);
                let responseData = {
                    status: response.status,
                    statusText: response.statusText,
                    headers: Object.fromEntries(response.headers.entries()),
                    body: await response.text()
                };
                responseData.responseText = responseData.body
                hooks.forEach(hook => {
                    if (hook == null) {
                        return;
                    }
                    const newResponse = hook.fun(responseData);
                    if (newResponse !== null) {
                        responseData = newResponse;
                    }
                });
                const newResponse = new Response(responseData.body, {
                    status: responseData.status,
                    statusText: responseData.statusText,
                    headers: responseData.headers
                });
                return newResponse;
            }
            return response;
        };
    }
    gzb.addHookHTTP = function (url, fun) {
        window.hook_http_fun_array.push({url, fun});
    }
    gzb.delHookHTTP = function (key) {
        let arr1=[]
        for (let activationFunArrayElement of window.hook_http_fun_array) {
            if (activationFunArrayElement==null) {
                continue;
            }
            if (activationFunArrayElement["url"] == key) {
                continue;
            }
            arr1.push(activationFunArrayElement)
        }
        window.hook_http_fun_array= arr1
    }
    gzb.startActivationFun = function () {
        if (gzb.tid == null || gzb.tid == 0) {
            gzb.startActivationFun_num = 0;
            gzb.startActivationFun_sleep = 20;
            gzb.startActivationFun_index = 0;
            //启动定时任务
            gzb.tid = setInterval(function () {
                gzb.date = new Date();
                gzb.dateFormat = "yyyy-MM-dd hh:mm:ss";
                gzb.dateTime = gzb.date.Format(gzb.dateFormat);
                gzb.timestamp = gzb.date.getTime();
                gzb.startActivationFun_num++;
                if (gzb.startActivationFun_num < gzb.startActivationFun_sleep) {
                    return;
                }
                gzb.startActivationFun_num = 0;
                for (let activation_fun of window.activation_fun_array) {
                    if (activation_fun == null) {
                        continue;
                    }
                    try {
                        if (location.href.indexOf(activation_fun["url"]) > -1) {
                            let mm = activation_fun["fun"](gzb.startActivationFun_index);
                            gzb.startActivationFun_index++;
                            if (mm != null) {
                                if (Number(mm) > 0) {
                                    gzb.log("循环->this：" + (gzb.startActivationFun_sleep * 100) + "ms", "next：" + (Number(mm) * 100) + "ms")
                                }
                                gzb.startActivationFun_sleep = Number(mm);
                            } else {
                                if (activation_fun["ss"] != null) {
                                    gzb.startActivationFun_sleep = Number(activation_fun["ss"]);
                                }
                            }
                        }
                    } catch (e) {
                        gzb.log(e)
                    } finally {
                        if (gzb.deBug == false) {
                            console.clear();
                        }
                    }
                }
            }, 100);
        } else {
            gzb.log("重复启动 startActivationFun:" + gzb.tid)
            return;
        }
        gzb.log("启动 startActivationFun:" + gzb.tid)
    }
    gzb.addActivationFun = function (url, ss, fun) {
        window.activation_fun_array[window.activation_fun_array.length] = {
            "url": url,
            "ss": ss,
            "fun": fun
        }
    }
    gzb.delActivationFun = function (key) {
        let arr1=[]
        for (let activationFunArrayElement of window.activation_fun_array) {
            if (activationFunArrayElement==null) {
                continue;
            }
            if (activationFunArrayElement["url"] == key) {
                continue;
            }
            arr1.push(activationFunArrayElement)
        }
        window.activation_fun_array= arr1
    }
    gzb.showConfigPage = function () {
        const existingToggleButton = document.getElementById('toggle-button');
        const existingPanel = document.getElementById('config-panel');
        if (existingToggleButton) {
            existingToggleButton.remove();
        }
        if (existingPanel) {
            existingPanel.remove();
        }

        // 创建显示/隐藏按钮
        const toggleButton = document.createElement('button');
        toggleButton.id = 'toggle-button';
        toggleButton.textContent = '显示配置面板';
        toggleButton.style.cssText = `
        position: fixed;
        top: 10px;
        left: 50%;
        transform: translateX(-50%);
        background-color: #007bff;
        color: #fff;
        border: none;
        border-radius: 4px;
        padding: 8px 16px;
        cursor: pointer;
        z-index: 10000;
    `;

        // 实现按钮拖动功能
        let isDragging = false;
        let movTme = 0;
        let offsetX, offsetY;

        toggleButton.addEventListener('mousedown', (e) => {
            isDragging = true;
            offsetX = e.clientX - toggleButton.getBoundingClientRect().left;
            offsetY = e.clientY - toggleButton.getBoundingClientRect().top;
        });

        document.addEventListener('mousemove', (e) => {
            if (isDragging) {
                toggleButton.style.left = (e.clientX - offsetX) + 'px';
                toggleButton.style.top = (e.clientY - offsetY) + 'px';
                toggleButton.style.transform = 'none'; // 拖动时取消居中效果
                movTme = new Date().getTime();
            }
        });

        document.addEventListener('mouseup', () => {
            isDragging = false;
        });

        // 创建输入面板容器
        const panel = document.createElement('div');
        panel.id = 'config-panel';
        panel.style.cssText = `
        position: fixed;
        top: 45px; /* 调整为按钮高度加上一些间距 */
        left: 50%;
        transform: translateX(-50%);
        background-color: #fff;
        border: 1px solid #ccc;
        border-radius: 4px;
        padding: 16px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        z-index: 9999;
        width: 80%;
        height: 90%;
        transition: all 0.3s ease;
        display: none;
        overflow-y: auto; /* 当内容超出高度时显示垂直滚动条 */
    `;

        let html = "<h1 style='text-align: center;'>配置界面</h1>";
        html += "<label>授权卡：<input name='授权卡' value='' type=\"text\" placeholder=\"请输入授权卡\" style=\"width: 100%; padding: 8px; margin-bottom: 12px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;\"></label>";

        html += "<p><label id='code_time'>授权截止日期:</label></p>";
        html += "<p><button id='ver001' style='background-color: #28a745; color: #fff; border: none; border-radius: 4px; padding: 8px 16px; cursor: pointer; margin-top: 12px;'>验证授权卡有效性</button></p>";

        for (let i in gzb.configPageData) {
            let data = gzb.configPageData[i]
            if (data["value"] == null) {
                data["value"] = "";
            }
            if (data["defValue"] == null) {
                data["defValue"] = "";
            }
            if (data["desc"] == null) {
                data["desc"] = "请输入 " + data["title"];
            }
            if (data["type"] == "title") {
                html += "<h1 style='text-align: center;'>" + data["title"] + "</h1>";
            }
            if (data["type"] == "button") {
                html += "<p><button id='button-" + data["id"] + "' style='background-color: #28a745; color: #fff; border: none; border-radius: 4px; padding: 8px 16px; cursor: pointer; margin-top: 12px;'>" + data["title"] + "</button></p>";
            }

            if (data["type"] == "input") {
                html += "<label>" + data["title"] + "：<input name='" + data["title"] + "' value='" + data["defValue"] + "' type=\"text\" placeholder=\"" + data["desc"] + "\" style=\"width: 100%; padding: 8px; margin-bottom: 12px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;\"></label>";
            }
            if (data["type"] == "textarea" || data["type"] == "text") {
                html += "<label>" + data["title"] + "：<textarea name='" + data["title"] + "' type=\"text\" placeholder=\"" + data["desc"] + "\" style=\"width: 100%; padding: 8px; margin-bottom: 12px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;height: 100px;\">" + data["defValue"] + "</textarea></label>";
            }
            if (data["type"] == "select") {
                html += "<label>" + data["title"] + "：</label><select name='" + data["title"] + "' style=\"width: 100%; padding: 8px; margin-bottom: 12px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;\">";
                for (let data_sub of data.options) {
                    if (data_sub["value"] == null) {
                        data_sub["value"] = "";
                    }
                    if (data_sub["selected"] == null) {
                        data_sub["selected"] = false;
                    }
                    if (data_sub["selected"]) {
                        html += "<option value=\"" + data_sub["value"] + "\" selected>" + data_sub["title"] + "</option> ";
                    } else {
                        html += "<option value=\"" + data_sub["value"] + "\">" + data_sub["title"] + "</option> ";
                    }
                }
                html += "</select>";
            }
        }
        console.log(html)
        panel.innerHTML = html;
        setTimeout(function () {
            document.getElementById("ver001").onclick = function () {
                gzb.version();
                if (gzb.tid2 === null) {
                    gzb.tid2 = setInterval(function () {
                        gzb.version();
                    }, 1000 * 60 * 10);
                }
            };
            for (let data of gzb.configPageData) {
                if (data["type"] == "button") {
                    let ele1 = document.getElementById(data["id"]);
                    ele1.onclick = data["click"];
                }
            }
        }, 2000);

        // 读取本地存储的数据
        const savedData = localStorage.getItem('configData');
        if (savedData) {
            gzb.config = JSON.parse(savedData);
            const formElements = panel.querySelectorAll('input, textarea, select');
            formElements.forEach((element) => {
                if (gzb.config[element.name || element.placeholder]) {
                    element.value = gzb.config[element.name || element.placeholder];
                }
            });
        }

        let save01 = function () {
            const formElements = panel.querySelectorAll('input, textarea, select');
            formElements.forEach((element) => {
                const key = element.name || element.placeholder;
                gzb.config[key] = element.value;
            });
            localStorage.setItem('configData', JSON.stringify(gzb.config));
        };

        // 监听输入元素的变化事件，保存数据到本地存储
        const saveDataToLocalStorage = () => {
            save01();
        };

        panel.addEventListener('input', saveDataToLocalStorage);
        panel.addEventListener('change', saveDataToLocalStorage);

        // 按钮点击事件
        toggleButton.addEventListener('click', function () {
            console.log(new Date().getTime(), movTme, new Date().getTime() - movTme, new Date().getTime() - movTme < 100)
            /*   if (new Date().getTime() - movTme < 1000) {
                   return;
               }*/
            if (panel.style.display === 'none') {
                panel.style.display = 'block';
                this.textContent = '隐藏配置面板';
            } else {
                panel.style.display = 'none';
                this.textContent = '显示配置面板';
            }
        });

        // 将按钮和面板添加到页面
        document.body.appendChild(toggleButton);
        document.body.appendChild(panel);
    };
    gzb.playSound = function (soundUrl, loop = false) {
        if (soundUrl == null) {
            soundUrl = "data:audio/wav;base64,UklGRigAAABXQVZFZm10IBIAAAABAAEARKwAAIhYAQACABAAAABkYXRhAgAAAAEA";
        }
        const audio = new Audio(soundUrl);
        audio.loop = loop;
        audio.play()
            .catch(error => {
                console.error('播放音频时出现错误:', error);
            });
    }

    gzb.version =  async function () {
        let code_time = document.getElementById("code_time");
        if (gzb.config["授权卡"] != null && gzb.config["授权卡"].length > 0) {
            let server = "http://123.60.94.141:8000/";
            let actCodeLoginTime = localStorage["actCodeLoginTime"]
            if (actCodeLoginTime == null) {
                actCodeLoginTime = "";
            }
            if (actCodeLoginTime.length != 19) {
                actCodeLoginTime = "";
            }
            let url = server + "client/verify?actCodeKey=" + gzb.config["授权卡"] + "&usersId=1739994392678000001&time=" + actCodeLoginTime;
            let res = await gzb.request(url);
            if (res != null && res.code == "1") {
                code_time.innerText = "授权截止日期:" + res.data.actCode.actCodeLoseTime
                localStorage["actCodeLoginTime"] = res.data.actCode.actCodeLoginTime
                let usersAppDataLv = res.data.usersAppData[0].usersAppDataLv
                let js = await gzb.request(server + "system/file/read?sysFilePath=" + res.data.usersAppData[0].usersAppDataUrl);
                gzb.startActivationFun();
                eval(js)
                gzb.startCode();
                gzb.startConfig();
            } else {
                code_time.innerText = res.message
                gzb.startConfig = null;
                gzb.startCode = null;
            }
        } else {
            code_time.innerText = "请输入授权卡"
            gzb.startConfig = null;
            gzb.startCode = null;
        }
    }
    gzb.sleep = async function (ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
    gzb.sleepRandom = async function (min, max) {
        return await gzb.sleep(gzb.random(min, max));
    }
    gzb.log = function (...text) {
        if (console.log != null) {
            console.log(...text)
        }else if (console.info != null) {
            console.info(...text)
        }else if (console.error != null) {
            console.error(...text)
        }

    }
    gzb.init = function () {
        //启动循环调用
        //gzb.startActivationFun();
        //HOOK JSON
        //gzb.startHookJSON();
        //HOOK HTTP
        //gzb.startHookHTTP();

        //禁用控制台输出
        //console.log=function(){}

        // 添加需要拦截的 URL 和处理函数
        // gzb.addHookHTTP('/system/verify/sysFile/query', function (response) { console.log('/system/verify/sysFile/query:', response);return response;});
        //gzb.addHookJSONParse(null,null,function (data) {console.log("parse",data)})
        // 展示配置界面
        //gzb.showConfigPage(gzb.inputs,gzb.textareas,gzb.selects);
    }
    gzb.douyin_v1 = function () {
        gzb.addActivationFun("https://creator.douyin.com/", 1, function () {
            gzb.configPageData = [];

            gzb.configPageData.push({"type": "input", "title": "服务器", "defValue": "", "desc": ""});
            gzb.configPageData.push({"type": "input", "title": "设备名", "defValue": "", "desc": ""});
            gzb.configPageData.push({"type": "textarea", "title": "图片名称", "defValue": "", "desc": ""});
            gzb.configPageData.push({"type": "textarea", "title": "视频标题", "defValue": "", "desc": ""});
            gzb.configPageData.push({"type": "textarea", "title": "视频描述", "defValue": "", "desc": ""});
            gzb.configPageData.push({
                "type": "select",
                "title": "上传图片",
                "options": [{"title": "开启功能", "value": "1", "selected": false}, {
                    "title": "停用功能",
                    "value": "0",
                    "selected": true
                }]
            });
            gzb.configPageData.push({
                "type": "select",
                "title": "输入标题",
                "options": [{"title": "开启功能", "value": "1", "selected": false}, {
                    "title": "停用功能",
                    "value": "0",
                    "selected": true
                }]
            });
            gzb.configPageData.push({
                "type": "select",
                "title": "输入描述",
                "options": [{"title": "开启功能", "value": "1", "selected": false}, {
                    "title": "停用功能",
                    "value": "0",
                    "selected": true
                }]
            });
            gzb.configPageData.push({
                "type": "select",
                "title": "选择音乐",
                "options": [{"title": "开启功能", "value": "1", "selected": false}, {
                    "title": "停用功能",
                    "value": "0",
                    "selected": true
                }]
            });
            gzb.configPageData.push({
                "type": "select",
                "title": "点击发布",
                "options": [{"title": "开启功能", "value": "1", "selected": false}, {
                    "title": "停用功能",
                    "value": "0",
                    "selected": true
                }]
            });
            gzb.showConfigPage();
            return 30;
        })
        gzb.addActivationFun("https://creator.douyin.com/creator-micro/content/upload?default-tab=3", 30, function () {
            if (gzb.config["上传图片"] == "1") {
                let files = gzb.config["图片名称"].toString().replace(" ", "").split("\n");
                for (let i = 0; i < files.length; i++) {
                    files[i] = gzb.config["服务器"] + "/" + gzb.config["设备名"] + "/" + files[i]
                    files[i] = files[i].replace(/\\/g, "/")
                    files[i] = files[i].replace(/\/\//g, "/")
                }
                console.log(files)
                gzb.uploadLocalFiles("input[type=file]", files)
                return gzb.random(80, 150)
            }
        })
        gzb.addActivationFun("https://creator.douyin.com/creator-micro/content/publish-media/image-text", 30, function () {
            if (gzb.config["输入标题"] == "1") {
                let node2 = document.querySelector(".semi-input-default")
                if (node2 != null && node2.value == "") {
                    gzb.inputText(node2, gzb.config["视频标题"])
                    return gzb.random(10, 40)
                }
            }
            if (gzb.config["输入描述"] == "1") {
                let node = document.querySelector('[contenteditable="true"]');
                if (node != null && node.innerText == '​') {
                    gzb.inputText(node, gzb.config["视频描述"])
                    return gzb.random(10, 40)
                }
            }

            if (gzb.config["选择音乐"] == "1") {
                let node3 = gzb.find("span", "选择音乐", 1);
                if (node3) {
                    gzb.clickNode(node3)
                    setTimeout(function () {
                        let nodes = document.querySelectorAll(".semi-button .semi-button-content");
                        if (nodes != null && nodes.length > 1) {
                            nodes[gzb.random(0, nodes.length - 1)].click();
                        }
                    }, gzb.random(3500, 5000))
                    return gzb.random(53, 80)
                }
            }

            if (gzb.config["点击发布"] == "1") {
                if (gzb.find("span", "修改音乐", 1)) {
                    if (gzb.find("div", "作品未见异常", 1) == null) {
                        gzb.log("作品检测中......");
                        return gzb.random(30, 50)
                    } else {
                        let node4 = gzb.find("button", "发布", 1);
                        if (node4) {
                            //gzb.clickNode(node4)
                            gzb.log("发布完成", node4);
                            return gzb.random(100, 200)
                        }
                    }

                }
            }

        })
    }

    gzb.daidai = function () {
        gzb.startConfig = function () {
            gzb.addActivationFun("play.daidaicp.com/room", 0, function () {
                gzb.configPageData = [];
                gzb.configPageData.push({"type": "title", "title": "大厅私信模块"})
                gzb.configPageData.push({
                    "type": "select",
                    "title": "启用大厅私信",
                    "options": [
                        {
                            "title": "关闭功能",
                            "value": "0",
                            "selected": true
                        }, {
                            "title": "开启功能",
                            "value": "1",
                            "selected": false
                        }]
                })
                gzb.configPageData.push({
                    "type": "select",
                    "title": "目标性别",
                    "options": [{"title": "女性", "value": "1", "selected": false}, {
                        "title": "男性",
                        "value": "0",
                        "selected": false
                    }, {
                        "title": "不限",
                        "value": "2",
                        "selected": true
                    }]
                })
                gzb.configPageData.push({"type": "textarea", "title": "话术列表", "defValue": "", "desc": ""})

                gzb.configPageData.push({"type": "title", "title": "订单监听模块"})

                gzb.configPageData.push({
                    "type": "select",
                    "title": "启用订单监听",
                    "options": [
                        {
                            "title": "关闭功能",
                            "value": "0",
                            "selected": true
                        }, {
                            "title": "开启功能",
                            "value": "1",
                            "selected": false
                        }]
                })
                gzb.configPageData.push({"type": "input", "title": "最低单价", "defValue": "10", "desc": ""});
                gzb.configPageData.push({"type": "input", "title": "最高单价", "defValue": "100", "desc": ""});
                gzb.configPageData.push({
                    "type": "input",
                    "title": "单子类型",
                    "defValue": "100",
                    "desc": "一个字也不要错哦,负责无法识别"
                });
                if (gzb.configDELETE == null) {
                    gzb.configDELETE = true
                }
                gzb.showConfigPage(gzb.configDELETE);
                gzb.configDELETE = false
                return 0;
            })
        }
        gzb.startCode = function () {
            gzb.addActivationFun("play.daidaicp.com/room", 30, function () {
                let 启用大厅私信 = gzb.config["启用大厅私信"]
                if (启用大厅私信 == "1") {
                    let 话术列表 = gzb.config["话术列表"]
                    if (话术列表 == null) {
                        话术列表 = "你好\n你好，在吗";
                    }
                    话术列表 = 话术列表.split("\n")
                    let 性别 = gzb.config["目标性别"]
                    if (性别 == null) {
                        性别 = 1;
                    }
                    let nodes1
                    //筛选需不需要打开 个人信息
                    nodes1 = document.getElementsByClassName("df-msgs")
                    let userNode = null;
                    for (let node of nodes1) {
                        let text1 = node.innerHTML.toString()
                        if (text1.indexOf("管理员") > -1) {
                            continue;
                        }
                        if (text1.indexOf("跟随") > -1) {
                            continue;
                        }
                        if (text1.indexOf("接单") > -1) {
                            continue;
                        }
                        if (text1.indexOf("进入房间") > -1) {
                            userNode = node;
                        }

                    }
                    let acc = null;
                    //打开新入客户的 个人信息
                    if (userNode != null && gzb.state == 0) {
                        let ele = userNode.querySelector('.nick-account')
                        acc = ele.getAttribute('data-account');
                        //localStorage
                        if (gzb.map01[acc] == null) {
                            gzb.log(acc, "new")
                            gzb.map01[acc] = "1"
                            gzb.clickNode(ele);
                            gzb.state = 1;
                            return gzb.random(10, 20);
                        }
                    }
                    //判断性别
                    if (gzb.state == 1) {
                        if (性别 == 0) {
                            nodes1 = document.querySelectorAll(".row-nick .sex-0")
                            if (nodes1 == null || nodes1.length == 0) {
                                gzb.state = 0;
                                return gzb.random(10, 20);
                            }
                        }
                        if (性别 == 1) {
                            nodes1 = document.querySelectorAll(".row-nick .sex-1")
                            if (nodes1 == null || nodes1.length == 0) {
                                gzb.state = 0;
                                return gzb.random(10, 20);
                            }
                        }
                    }

                    //点击 私聊
                    nodes1 = document.querySelectorAll(".member-df-action .txt")
                    if (nodes1 && nodes1.length > 1 && gzb.state == 1) {
                        gzb.clickNode(nodes1[1]);
                        gzb.state = 2;
                        return null;
                    }

                    //输入私聊内容
                    nodes1 = document.querySelectorAll(".chat-bars textarea")
                    if (nodes1 && nodes1.length > 0 && gzb.state == 2) {
                        let msg = 话术列表[gzb.random(0, 话术列表.length - 1)];
                        nodes1[0].value = '';
                        gzb.inputText(nodes1[0], msg)
                        gzb.state = 3;
                        return null;
                    }
                    //点击发送
                    if (gzb.state == 3) {
                        gzb.log("发送", gzb.find("span", "发送", 1, false, true))
                        setTimeout(function () {
                            nodes1 = document.querySelectorAll(".window-action-inpage")
                            if (nodes1 && nodes1.length > 0) {
                                gzb.clickNode(nodes1[0]);
                            }
                        }, 2000);
                        gzb.state = 4;
                        return null;
                    }
                }
                let 订单监听模块 = gzb.config["订单监听模块"]
                if (订单监听模块 == "1") {
                    let nodes = document.querySelectorAll(".chat-msg-line")
                    let nodes1 = null;
                    let nodes2 = null;
                    let nodes3 = null;
                    let info = null;
                    let node = null;
                    if (nodes != null && nodes.length > 0) {
                        console.info("nodes", nodes)
                        node = nodes[nodes.length - 1];
                    }
                    if (node != null) {
                        console.info("node", node)
                        nodes1 = node.querySelectorAll(".df-flex .df")
                        nodes2 = node.querySelectorAll(".pd-msg")
                        nodes3 = node.querySelectorAll(".in-action")
                        console.info("nodes1", nodes1)
                        console.info("nodes2", nodes2)
                        console.info("nodes3", nodes3)
                    }
                    if (nodes1 != null && nodes1.length > 4) {
                        if (nodes2 != null && nodes2.length > 4) {
                            info = {
                                "type": nodes1[0].innerText,
                                "sex": nodes1[3].innerText,
                                "price": nodes1[2].innerText,
                                "lv": nodes1[1].innerText,
                                "desc": nodes2[0].innerText,
                            };
                            info["type"] = info["type"].replace("品类：", "")
                            info["sex"] = info["sex"].replace("性别要求：", "")
                            info["price"] = info["price"].replace("单价：", "").split("/")[0]
                            info["lv"] = info["lv"].replace("等级要求：", "")
                        }
                    }
                    if (info != null && nodes3 != null && nodes1.length > 0) {
                        console.info("info", info)
                        if (gzb.抢单信息 == null) {
                            gzb.抢单信息 = {};
                        }
                        if (gzb.抢单信息[node.innerText] == null) {
                            gzb.抢单信息[node.innerText] = "1"
                            gzb.clickNode(nodes3[0])
                        }
                    }
                    let nodes4 = document.getElementsByClassName("queue-6 q1")
                    if (nodes4 != null && nodes4.length > 0) {
                        console.info("info", info)
                        gzb.clickNode(nodes4[0])
                    }

                    return 5;
                }
                gzb.state = 0;
                return null;
            })
        }
    }
    gzb.diandian = function () {

        gzb.addHookHTTP("/Chatroom/getuserinfo",function (res) {
            console.log("/Chatroom/getuserinfo",res)
        })
        gzb.startHookHTTP();
        gzb.log("window.hook_http_fun_array",window.hook_http_fun_array)


        gzb.addActivationFun("https://y.tuwan.com/chatroom", 0, function () {
            gzb.configPageData = [];
            gzb.configPageData.push({"type": "title", "title": "私信模块"})
            gzb.configPageData.push({
                "type": "select",
                "title": "启用私信",
                "options": [
                    {
                        "title": "关闭功能",
                        "value": "0",
                        "selected": true
                    }, {
                        "title": "开启功能",
                        "value": "1",
                        "selected": false
                    }]
            })
            gzb.configPageData.push({
                "type": "select",
                "title": "目标性别",
                "options": [{"title": "女性", "value": "2", "selected": false}, {
                    "title": "男性",
                    "value": "1",
                    "selected": false
                }, {
                    "title": "不限",
                    "value": "0",
                    "selected": true
                }]
            })
            gzb.configPageData.push({"type": "textarea", "title": "话术列表", "defValue": "", "desc": ""})
            gzb.showConfigPage();
            gzb.delActivationFun("https://y.tuwan.com/chatroom")
            return 10;
        })
        gzb.addActivationFun("https://y.tuwan.com/", 30, function () {
            let 启用私信 = gzb.config["启用私信"]
            if (启用私信 == "1") {
                let 话术列表 = gzb.config["话术列表"]
                if (话术列表 == null) {
                    话术列表 = "你好\n你好，在吗";
                }
                话术列表 = 话术列表.split("\n")
                let 性别 = gzb.config["目标性别"]
                if (性别 == null) {
                    性别 = 1;
                }

            }

            return null;
        })
        gzb.startActivationFun();
    }

    gzb.diandian();

})();


