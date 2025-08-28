// XHR请求工具函数
function xhrPost(url, data) {
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    try {
        xhr.send(data);
        if (xhr.status === 200) {
            return xhr.responseText;
        } else {
            throw new Error('请求失败，状态码: ' + xhr.status);
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}

function xhrGet(url) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', url, false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    try {
        xhr.send();
        if (xhr.status === 200) {
            return xhr.responseText;
        } else {
            throw new Error('请求失败，状态码: ' + xhr.status);
        }
    } catch (error) {
        console.error(error);
        return null;
    }
}

// XHR和Fetch拦截工具类
let hook = {};

// 存储回调和状态的内部变量
hook._xhrCallbacks = new Map();
hook._fetchCallbacks = new Map();
hook._originalXHR = XMLHttpRequest;
hook._originalFetch = window.fetch;
hook._isXhrHooked = false;
hook._isFetchHooked = false;
hook._xhrProxy = null;
hook._fetchProxy = null;

// 辅助函数：创建响应对象
hook.createResponse_xhr = function(data, status = 200, headers = {}) {
    return {
        response: data,
        status: status,
        headers: headers
    };
};

hook.createResponse_fetch = function(data, status = 200, headers = {}) {
    // 确保响应数据可以多次读取
    const body = typeof data === 'string' ? data : JSON.stringify(data);
    const headersObj = new Headers(headers);

    // 创建可重复读取的响应
    const response = new Response(body, {
        status: status,
        headers: headersObj
    });

    // 缓存响应数据以便多次读取
    const cachedBody = body;
    response.clone = function() {
        return new Response(cachedBody, {
            status: status,
            headers: headersObj
        });
    };

    return response;
};

// 启动XHR拦截
hook.start_xhr = function() {
    if (this._isXhrHooked) return;

    const self = this;

    // 创建XHR代理类
    this._xhrProxy = class extends XMLHttpRequest {
        constructor() {
            super();
            this._requestInfo = {
                url: '',
                headers: {},
                method: '',
                data: null
            };
            this._responseInfo = {
                url: '',
                requestHeaders: {},
                requestData: null,
                responseHeaders: '',
                response: null,
                status: 200,
                statusText: 'OK'
            };
            this._matchedCallbacks = [];
            this._isAborted = false; // 标记请求是否被取消
            this._customResponse = null; // 存储自定义响应
        }

        open(method, url, async = true, username = null, password = null) {
            this._requestInfo.url = url;
            this._requestInfo.method = method;
            this._responseInfo.url = url;

            // 查找匹配的回调
            for (const [key, callbacks] of self._xhrCallbacks) {
                if (url.includes(key)) {
                    this._matchedCallbacks.push(callbacks);
                }
            }

            // 应用请求前回调
            for (const { before } of this._matchedCallbacks) {
                if (before) {
                    const newInfo = before({...this._requestInfo});

                    // 检查是否有自定义响应
                    if (newInfo.response !== null && newInfo.response !== undefined) {
                        this._customResponse = newInfo.response;
                        this._isAborted = true;
                        this._requestInfo = {...newInfo};

                        // 设置响应信息
                        this._responseInfo.response = this._customResponse.response || null;
                        this._responseInfo.status = this._customResponse.status || 200;
                        this._responseInfo.statusText = this._getStatusText(this._responseInfo.status);

                        // 处理响应头
                        const headers = this._customResponse.headers || {};
                        this._responseInfo.responseHeaders = Object.entries(headers)
                            .map(([key, value]) => `${key}: ${value}`)
                            .join('\r\n');

                        return;
                    }

                    if (newInfo.url !== this._requestInfo.url) {
                        this._requestInfo.url = newInfo.url;
                        this._responseInfo.url = newInfo.url;
                    }

                    this._requestInfo = {...newInfo};
                }
            }

            return super.open(
                this._requestInfo.method,
                this._requestInfo.url,
                async,
                username,
                password
            );
        }

        setRequestHeader(header, value) {
            if (!this._isAborted) {
                this._requestInfo.headers[header] = value;
                return super.setRequestHeader(header, value);
            }
        }

        send(data) {
            if (this._isAborted) {
                // 触发load事件返回自定义响应
                this._triggerLoadEvent();
                return;
            }

            this._requestInfo.data = data;
            this._responseInfo.requestData = data;

            // 应用请求前回调
            for (const { before } of this._matchedCallbacks) {
                if (before) {
                    const newInfo = before({...this._requestInfo});

                    // 检查是否有自定义响应
                    if (newInfo.response !== null && newInfo.response !== undefined) {
                        this._customResponse = newInfo.response;
                        this._isAborted = true;
                        this._requestInfo = {...newInfo};

                        // 设置响应信息
                        this._responseInfo.response = this._customResponse.response || null;
                        this._responseInfo.status = this._customResponse.status || 200;
                        this._responseInfo.statusText = this._getStatusText(this._responseInfo.status);

                        // 处理响应头
                        const headers = this._customResponse.headers || {};
                        this._responseInfo.responseHeaders = Object.entries(headers)
                            .map(([key, value]) => `${key}: ${value}`)
                            .join('\r\n');

                        // 触发load事件
                        this._triggerLoadEvent();
                        return;
                    }

                    if (newInfo.data !== this._requestInfo.data) {
                        this._requestInfo.data = newInfo.data;
                    }

                    this._requestInfo = {...newInfo};
                }
            }

            return super.send(this._requestInfo.data);
        }

        // 触发load事件返回自定义响应
        _triggerLoadEvent() {
            // 设置XHR状态
            Object.defineProperty(this, 'status', {
                value: this._responseInfo.status,
                writable: false
            });

            Object.defineProperty(this, 'statusText', {
                value: this._responseInfo.statusText,
                writable: false
            });

            // 触发load事件
            const loadEvent = new Event('load');
            this.dispatchEvent(loadEvent);
        }

        // 根据状态码获取状态文本
        _getStatusText(status) {
            const statusTexts = {
                200: 'OK',
                201: 'Created',
                400: 'Bad Request',
                401: 'Unauthorized',
                403: 'Forbidden',
                404: 'Not Found',
                500: 'Internal Server Error'
            };

            return statusTexts[status] || 'Unknown Status';
        }

        get response() {
            if (this._isAborted && this._customResponse) {
                return this._customResponse.response;
            }

            let finalResponse = {
                url: this._responseInfo.url,
                requestHeaders: this._requestInfo.headers,
                requestData: this._requestInfo.data,
                responseHeaders: this.getAllResponseHeaders(),
                response: super.response,
                status: this.status,
                statusText: this.statusText
            };

            for (const { after } of this._matchedCallbacks) {
                if (after) {
                    finalResponse = after(finalResponse);
                }
            }

            return finalResponse.response;
        }

        get responseText() {
            const response = this.response;
            return response ? (typeof response === 'string' ? response : JSON.stringify(response)) : '';
        }

        get responseXML() {
            const response = this.response;
            if (response && typeof response === 'string') {
                const parser = new DOMParser();
                return parser.parseFromString(response, 'text/xml');
            }
            return super.responseXML;
        }

        // 重写getAllResponseHeaders方法
        getAllResponseHeaders() {
            if (this._isAborted && this._customResponse) {
                return this._responseInfo.responseHeaders;
            }
            return super.getAllResponseHeaders();
        }

        // 重写getResponseHeader方法
        getResponseHeader(header) {
            if (this._isAborted && this._customResponse) {
                const headers = this._responseInfo.responseHeaders;
                const regex = new RegExp(`^${header}: (.*)$`, 'im');
                const match = headers.match(regex);
                return match ? match[1] : null;
            }
            return super.getResponseHeader(header);
        }
    };

    // 替换原生XHR
    window.XMLHttpRequest = this._xhrProxy;
    this._isXhrHooked = true;
};

// 启动Fetch拦截
hook.start_fetch = function() {
    if (this._isFetchHooked) return;

    const self = this;

    // 创建Fetch代理函数
    this._fetchProxy = async function(input, init = {}) {
        const request = new Request(input, init);
        const url = request.url;
        let requestBody = null;

        // 保存请求体以便多次读取
        if (request.body) {
            requestBody = await request.clone().text();
        }

        const requestInfo = {
            url: url,
            headers: Object.fromEntries(request.headers.entries()),
            method: request.method,
            data: requestBody
        };

        let matchedCallbacks = [];

        // 查找匹配的回调
        for (const [key, callbacks] of self._fetchCallbacks) {
            if (url.includes(key)) {
                matchedCallbacks.push(callbacks);
            }
        }

        let modifiedRequest = { ...requestInfo };

        // 应用请求前回调
        for (const { before } of matchedCallbacks) {
            if (before) {
                modifiedRequest = before(modifiedRequest);

                // 检查是否有自定义响应
                if (modifiedRequest.response !== null && modifiedRequest.response !== undefined) {
                    // 确保响应可以多次读取
                    const customResponse = modifiedRequest.response;
                    if (customResponse.clone) {
                        return customResponse.clone();
                    } else {
                        return self.createResponse_fetch(customResponse.response || {}, customResponse.status || 200, customResponse.headers || {});
                    }
                }
            }
        }

        // 创建修改后的请求
        const modifiedInit = {
            ...init,
            method: modifiedRequest.method,
            headers: new Headers(modifiedRequest.headers),
            body: modifiedRequest.data
        };

        // 关键修复：使用apply方法确保fetch在正确的上下文中调用
        let response = await self._originalFetch.apply(window, [modifiedRequest.url, modifiedInit]);

        // 保存响应以便多次读取
        const responseClone = response.clone();
        const responseBody = await responseClone.text();

        const responseInfo = {
            url: url,
            requestHeaders: modifiedRequest.headers,
            requestData: modifiedRequest.data,
            responseHeaders: Object.fromEntries(response.headers.entries()),
            response: responseBody,
            status: response.status,
            statusText: response.statusText
        };

        // 应用响应后回调
        let finalResponseInfo = { ...responseInfo };
        for (const { after } of matchedCallbacks) {
            if (after) {
                finalResponseInfo = after(finalResponseInfo);
            }
        }

        // 创建最终响应
        const finalResponse = new Response(
            finalResponseInfo.response,
            {
                status: finalResponseInfo.status,
                statusText: finalResponseInfo.statusText,
                headers: new Headers(finalResponseInfo.responseHeaders)
            }
        );

        // 缓存响应数据以便多次读取
        const cachedBody = finalResponseInfo.response;
        finalResponse.clone = function() {
            return new Response(cachedBody, {
                status: finalResponseInfo.status,
                statusText: finalResponseInfo.statusText,
                headers: new Headers(finalResponseInfo.responseHeaders)
            });
        };

        return finalResponse;
    };

    // 替换原生Fetch
    window.fetch = this._fetchProxy;
    this._isFetchHooked = true;
};

// 停止XHR拦截
hook.end_xhr = function() {
    if (!this._isXhrHooked) return;
    window.XMLHttpRequest = this._originalXHR;
    this._isXhrHooked = false;
};

// 停止Fetch拦截
hook.end_fetch = function() {
    if (!this._isFetchHooked) return;
    window.fetch = this._originalFetch;
    this._isFetchHooked = false;
};

// 删除指定key的XHR回调
hook.del_xhr = function(key) {
    this._xhrCallbacks.delete(key);
};

// 删除指定key的Fetch回调
hook.del_fetch = function(key) {
    this._fetchCallbacks.delete(key);
};

// 添加XHR拦截规则
hook.add_xhr = function(key, before = null, after = null) {
    this._xhrCallbacks.set(key, { before, after });
};
// 添加Fetch拦截规则
hook.add_fetch = function(key, before = null, after = null) {
    this._fetchCallbacks.set(key, { before, after });
};
// 测试XHR拦截
hook.test_xhr = function(getUrl,postUrl,postData) {
    this.start_xhr();
    this.add_xhr(
        "/system/v1.0.0/script/",
        (request) => {
            // 设置自定义响应
            request.response = this.createResponse_xhr(
                { message: '这是一个自定义XHR响应 test_xhr' },
                200,
                { 'Content-Type': 'application/json' }
            );
            console.log("[XHR 请求拦截] 拦截到目标请求:", request.url,request);
            return request;
        },
        (response) => {
            // 响应后修改
            response.response = { message: '这是修改后的XHR响应' };
            console.log("[XHR 请求拦截] 拦截到目标请求:", response.url,response);
            return response;
        }
    );

    console.log("XHR拦截器已启动，正在测试目标地址...");
    console.log("xhrGet结果:", xhrGet(getUrl));

    console.log("xhrPost结果:", xhrPost(postUrl, postData));

    this.end_xhr();
};

// 测试Fetch拦截
hook.test_fetch = function(getUrl,postUrl,postData) {
    this.start_fetch();
    this.add_fetch(
        "/system/v1.0.0/",
        (request) => {
            request.response = this.createResponse_fetch(
                { message: '这是一个自定义XHR响应 test_fetch' },
                200,
                { 'Content-Type': 'application/json' }
            );
            console.log("[Fetch 请求拦截] 拦截到目标请求:", request.url,request);
            return request;
        },
        (response) => {
            console.log("[Fetch 响应拦截] 拦截到目标响应:", response.url,response);

            // 修改响应数据
            try {
                const data = JSON.parse(response.response);
                data.hooked = true;
                data.timestamp = new Date().toISOString();
                response.response = JSON.stringify(data);
            } catch (e) {
                console.log("响应不是JSON格式，追加标记");
                response.response += "\n// Modified by Fetch Hook";
            }

            return response;
        }
    );

    console.log("Fetch拦截器已启动，正在测试目标地址...");

    // 测试GET请求
    fetch(getUrl)
        .then(res => res.text())
        .then(data => console.log("fetchGet结果:", data))
        .catch(err => console.error("fetchGet错误:", err));

    // 测试POST请求
    fetch(postUrl, {
        method: "POST",
        body: postData
    })
        .then(res => res.text())
        .then(data => console.log("fetchPost结果:", data))
        .catch(err => console.error("fetchPost错误:", err));

    // 延迟结束拦截器，确保请求完成
    setTimeout(() => {
        this.end_fetch();
    }, 2000);
};

//开始HOOK
hook.start=function (){
    hook.start_fetch();
    hook.start_xhr();
}

// 添加 HOOK 规则
hook.add=function (key, before = null, after = null){
    hook.add_fetch(key,before,after);
    hook.add_xhr(key,before,after);
}
// 删除 HOOK 规则
hook.del=function (key){
    hook.del_fetch(key);
    hook.del_xhr(key);
}
// 关闭 HOOK
hook.end=function (){
    hook.end_fetch();
    hook.end_xhr();
}
// 测试 HOOK
hook.test = function() {
    let getUrl="http://192.168.10.101:2081/system/v1.0.0/script/test001";
    let postUrl="http://192.168.10.101:2081/system/v1.0.0/script/test002";
    let postData="name=1";
    this.test_xhr(getUrl,postUrl,postData);
    this.test_fetch(getUrl,postUrl,postData);
};
// 执行默认测试
hook.start();
hook.add(
    "index.m3u8",
    (request) => {
        // 设置自定义响应
        /*request.response = this.createResponse_xhr(
            { message: '这是一个自定义XHR响应 test_xhr' },
            200,
            { 'Content-Type': 'application/json' }
        );*/
        console.log("[XHR 请求拦截] 拦截到目标请求:", request.url,request);
        let tid=setInterval(function (){
            let nodes=document.getElementsByClassName("_7a58688c68 cabd0c9f78 mt20");
            if (nodes != null && nodes.length >= 1 && nodes[0].innerText!=null && nodes[0].innerText.length > 0) {
                let title =nodes[0].innerText.trim();
                clearInterval(tid)
                xhrPost("http://127.0.0.1:2081/system/v1.0.0/script/m3u8/save",
                    "path=" + encodeURI(request.url)+"&title="+title)
            }
        },1000);
        return request;
    },
    (response) => {
        // 响应后修改
        //response.response = { message: '这是修改后的XHR响应' };
        //console.log("[XHR 请求拦截] 拦截到目标请求:", response.url,response);
        return response;
    }
);

