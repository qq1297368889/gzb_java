// inject_hook_tag.js - 改进版

// --- Android 平台通用 Hook 示例 ---
console.log("hook",Java.available,Java.use("android.webkit.WebView"),Java.use("android.webkit.WebViewClient"));
if (Java.available) {
    console.log("1 [+] Frida Script Loaded for Android (Improved).");
    Java.perform(function () {
        console.log("2 [+] Frida Script Loaded for Android (Improved).");

        const WebView = Java.use("android.webkit.WebView");
        const WebViewClient = Java.use("android.webkit.WebViewClient");

        // 我们将使用一个变量来存储 WebView 实例，以便在 WebViewClient 中使用
        // 将其声明在 Java.perform 外部，使其成为全局变量，方便REPL访问
        let targetWebViewInstance = null;
        // isJsPayloadInjected 标志及其外部检查已移除，以便更直接地尝试注入

        // --- 核心注入逻辑函数 ---
        // 这个函数现在可以被多次调用，但内部的jsPayload会确保DOM元素只添加一次
        function injectJsPayload(webviewInstance) {
            // 确保在UI线程执行 JavaScript
            Java.scheduleOnMainThread(function() {
                const jsPayload = `
                    (function() {
                        // 这个内部检查是必须的，防止每次页面跳转都创建新的div
                        if (document.getElementById('frida-hook-tag')) {
                            console.log('Injected JS: Hook tag already exists. Skipping re-injection (DOM check).');
                            return;
                        }
                        // 确保body元素已经可用，否则无法修改背景色或添加元素
                        if (!document.body) {
                            console.log('Injected JS: Document body not yet available, delaying visual injection.');
                            // 为了简化，我们只在body存在时注入视觉元素。如果没有body，可能需要更复杂的等待机制。
                            return;
                        }

                        // 改变背景色，这是一个非常明显的视觉效果，确认JS已执行
                        document.body.style.backgroundColor = 'purple';
                        console.log('Injected JS: Body background color changed to purple!');

                        // 创建新的 div 元素
                        const hookTag = document.createElement('div');
                        hookTag.id = 'frida-hook-tag';
                        hookTag.textContent = '这里是HOOK';

                        // 设置样式
                        hookTag.style.position = 'fixed';
                        hookTag.style.top = '20px';
                        hookTag.style.right = '20px';
                        hookTag.style.backgroundColor = 'rgba(255, 0, 0, 0.8)'; // 更深的红色
                        hookTag.style.color = 'white';
                        hookTag.style.padding = '10px 20px'; // 更大
                        hookTag.style.borderRadius = '8px'; // 更圆润
                        hookTag.style.zIndex = '999999'; // 确保在最顶层
                        hookTag.style.fontSize = '20px'; // 更大字体
                        hookTag.style.fontWeight = 'bold'; // 加粗
                        hookTag.style.fontFamily = 'sans-serif';
                        hookTag.style.boxShadow = '0 4px 8px rgba(0,0,0,0.3)'; // 阴影

                        // 将元素添加到页面的 body 中
                        document.body.appendChild(hookTag);
                        console.log('Injected JS: Successfully added "这里是HOOK" tag to the page.');

                        // 强制弹出提示框，这是确认JS是否执行的最直接方式
                        // 如果alert干扰App，可以将其注释掉
                        alert('Frida JS 注入成功！请查看屏幕右上方和背景颜色！');

                        // 示例：尝试访问Vue应用的全局钩子，如果存在的话
                        if (typeof window.__VUE_DEVTOOLS_GLOBAL_HOOK__ !== 'undefined') {
                            console.log('Injected JS: Vue DevTools hook detected in target app.');
                            // 可以在这里进一步探索Vue实例
                        }
                    })();
                `;
                webviewInstance.evaluateJavascript(jsPayload, null);
            });
        }


        // --- Hook WebView 的各种加载方法，并在调用原始方法前直接注入 JS ---
        // Hook loadUrl(String)
        WebView.loadUrl.overload('java.lang.String').implementation = function (url) {
            console.log("[*] WebView.loadUrl(String) called with URL: " + url);
            targetWebViewInstance = this; // 捕获当前的 WebView 实例
            injectJsPayload(this); // 在加载URL前直接注入JS Payload
            this.loadUrl(url); // 调用原始方法，让页面继续加载
        };

        // Hook loadUrl(String, Map<String, String>)
        WebView.loadUrl.overload('java.lang.String', 'java.util.Map').implementation = function (url, headers) {
            console.log("[*] WebView.loadUrl(String, Map) called with URL: " + url);
            targetWebViewInstance = this;
            injectJsPayload(this); // 在加载URL前直接注入JS Payload
            this.loadUrl(url, headers);
        };

        // Hook loadData (此方法通常用于加载HTML字符串，而非外部URL)
        WebView.loadData.overload('java.lang.String', 'java.lang.String', 'java.lang.String').implementation = function (data, mimeType, encoding) {
            console.log("[*] WebView.loadData called. MimeType: " + mimeType);
            targetWebViewInstance = this;
            injectJsPayload(this); // 在加载数据前直接注入JS Payload
            this.loadData(data, mimeType, encoding);
        };

        // Hook loadDataWithBaseURL
        WebView.loadDataWithBaseURL.overload('java.lang.String', 'java.lang.String', 'java.lang.String', 'java.lang.String', 'java.lang.String').implementation = function (baseUrl, data, mimeType, encoding, historyUrl) {
            console.log("[*] WebView.loadDataWithBaseURL called. BaseURL: " + baseUrl);
            targetWebViewInstance = this;
            injectJsPayload(this); // 在加载数据前直接注入JS Payload
            this.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        };

        // --- Hook WebViewClient 的 onPageFinished 方法 (作为备用注入点) ---
        // 这是一个匿名类 Hook 的例子，因为 App 可能会创建 WebViewClient 的匿名实例
        WebViewClient.$init.implementation = function () {
            console.log("[*] New WebViewClient instance created.");
            this.$init(); // 调用原始构造函数

            // Hook这个新的 WebViewClient 实例的 onPageFinished 方法
            this.onPageFinished.implementation = function (view, url) {
                console.log("[*] WebViewClient.onPageFinished called for URL: " + url);
                this.onPageFinished(view, url); // 调用原始方法

                // 再次尝试注入，以防在loadUrl时DOM未完全准备好。
                // 内部的jsPayload会处理重复注入的DOM元素。
                if (targetWebViewInstance && view && view === targetWebViewInstance) {
                    console.log("[+] Attempting to inject JS into WebView (from onPageFinished fallback)...");
                    injectJsPayload(view); // 传入当前的view实例
                } else {
                    console.log("[-] Warning: targetWebViewInstance not captured or view mismatch for onPageFinished.");
                }
            };
        };


        // --- 暴露 WebView 实例给 REPL，并提供一个辅助函数 ---
        // 将 targetWebViewInstance 暴露到全局变量 (globalThis)
        globalThis.webview = targetWebViewInstance; // 将WebView实例赋值给全局变量webview
        console.log("[+] WebView instance exposed as 'webview' in REPL.");

        // 提供一个辅助函数，方便在 REPL 中执行 JS
        globalThis.runJsInWebview = function(jsCode) {
            if (targetWebViewInstance) {
                Java.scheduleOnMainThread(function() {
                    targetWebViewInstance.evaluateJavascript(jsCode, null);
                    console.log(`[REPL] Executed JS in WebView: ${jsCode.substring(0, 50)}...`);
                });
            } else {
                console.log("[-] WebView instance not yet available. Try navigating within the app.");
            }
        };
        console.log("[+] 'runJsInWebview(jsCode)' function available in REPL.");

        console.log("[+] WebView and WebViewClient Hooking for Android is set up.");
    });
} else {
    console.log("[-] Java runtime not available. This script is for Android/JVM environments.");
}
