try {
    if (hook == null) {
        hook = {}
        hook.fun = {}
    }
} catch (e) {
    hook = {}
    hook.fun = {}
}
hook.add = function (url, fun) {
    hook.fun[url] = fun
}
hook.init = function () {
    // 保存原始的 fetch 和 XMLHttpRequest
    if (hook.originalFetch == null || hook.originalXHR == null) {
        hook.originalFetch = window.fetch;
        hook.originalXHR = XMLHttpRequest.prototype.send;
    }
    window.fetch=function(url){
        return hook.originalFetch.apply(window,arguments).then((response) => {
            const reader = response.body.getReader();
            let text="";
            const stream = new ReadableStream({
                start(controller) {
                    function push() {
                        // "done"是一个布尔型，"value"是一个Unit8Array
                        reader.read().then((e) => {
                            let { done, value }=e;
                            // 判断是否还有可读的数据？
                            text+=new TextDecoder("utf-8").decode(value);
                            if (done) {
                                //console.log(response.url,text);
                                controller.close();
                                for (let key in hook.fun){
                                    //console.log(key,response.url.toString().indexOf(key))
                                    if (response.url.toString().indexOf(key) > -1) {
                                        hook.fun[key](text,response.headers,response.url)
                                    }
                                }
                                return;
                            }
                            // 取得数据并将它通过controller发送给浏览器
                            controller.enqueue(value);
                            push();
                        });
                    }
                    push();
                }
            });
            let ret=new Response(stream, { headers: response.headers })
            return ret;
        });
    };

    XMLHttpRequest.prototype.send = function (body) {
        this.addEventListener('load', function () {
            let data = this.response;
            if (this.response != null) {
                data = this.response;
            } else if (this.responseText != null) {
                data = this.responseText;
            } else {
                console.log("null return")
                return;
            }
            for (let key in hook.fun){
                if (this.responseURL.toString().indexOf(key) > -1) {
                    hook.fun[key](data,this.headers,this.responseURL)
                }
            }
        });
        hook.originalXHR.apply(this, [body]);
    };
}
hook.init();
hook.add("lhkeyword/getlhkeywordlist",function (data,headers,url) {
    console.log(data,headers,url)
});
hook.add("system/",function (data,headers,url) {
    console.log(data,headers,url)
});

