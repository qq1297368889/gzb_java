//type="file"
var globalWindow = (function () {
    return this;
})();
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
if (window.hook_json_parse_fun_aray == null) {
    window.hook_json_parse_fun_aray = [];
}
if (window.hook_json_stringify_fun_aray == null) {
    window.hook_json_stringify_fun_aray = [];
}
if (window.hook_http_fun_array == null) {
    window.hook_http_fun_array = [];
}
if (window.activation_fun_array == null) {
    window.activation_fun_array = [];
}
var gzb = {};
gzb.lv = 1002
gzb.tid = 0;
gzb.deBug = true;
gzb.lastX = 0
gzb.lastY = 0
gzb.startMouseDisplay = function () {
    if (document.getElementById("mouseIndicator") != null) {
        return;
    }
    globalWindow.handleMouseMove = function (event) {
        const x = event.pageX;
        const y = event.pageY;
        // 更新指示点的位置
        mouseIndicator.style.left = `${x - 5}px`;
        mouseIndicator.style.top = `${y + 10}px`;
        // 更新上次鼠标位置
        gzb.lastX = x;
        gzb.lastY = y;
    }
    // 创建一个新的 div 元素作为鼠标指示器
    globalWindow.mouseIndicator = document.createElement('div');
    globalWindow.mouseIndicator.id = 'mouseIndicator';
    globalWindow.mouseIndicator.style.width = '10px';
    globalWindow.mouseIndicator.style.height = '10px';
    globalWindow.mouseIndicator.style.backgroundColor = 'red';

    // 正确设置元素的定位方式为 absolute
    globalWindow.mouseIndicator.style.position = 'absolute';
    // 设置元素的初始位置
    globalWindow.mouseIndicator.style.top = '20px';
    globalWindow.mouseIndicator.style.left = '20px';
    // 设置元素的层级
    globalWindow.mouseIndicator.style.zIndex = '9999';

    // 将元素添加到页面中
    document.body.appendChild(globalWindow.mouseIndicator);


    document.addEventListener('mousemove', globalWindow.handleMouseMove);
}
gzb.endMouseDisplay = function () {
    if (globalWindow.mouseIndicator) {
        document.body.removeChild(globalWindow.mouseIndicator);
        // 移除鼠标移动事件监听器
        document.removeEventListener('mousemove', globalWindow.handleMouseMove);
        globalWindow.mouseIndicator = null;
    }
}
gzb.startSimulation = async function (startX, startY, endX, endY, duration, useLastPosition) {
    console.log("移动开始")
    let res = await gzb.simulateMouseMovement(startX, startY, endX, endY, duration, useLastPosition)
    console.log("移动结束")
    return res;
}
gzb.simulateMouseMovement = async function (startX, startY, endX, endY, duration, useLastPosition) {
    return new Promise((resolve) => {
        if (startX == null || startX == null) {
            // 根据开关决定起点位置
            if (useLastPosition && gzb.lastX != null && gzb.lastY != null) {
                startX = gzb.lastX;
                startY = gzb.lastY;
                if (startX < 1) {
                    startX = 1;
                }
                if (startY < 1) {
                    startY = 1;
                }
                console.log("useLastPosition", startX, startY)
            } else {
                console.log("switch")
                const side = Math.floor(Math.random() * 4);
                switch (side) {
                    case 0: // 上边缘
                        startX = Math.random() * globalWindow.innerWidth;
                        startY = 0;
                        break;
                    case 1: // 右边缘
                        startX = globalWindow.innerWidth;
                        startY = Math.random() * globalWindow.innerHeight;
                        break;
                    case 2: // 下边缘
                        startX = Math.random() * globalWindow.innerWidth;
                        startY = globalWindow.innerHeight;
                        break;
                    case 3: // 左边缘
                        startX = 0;
                        startY = Math.random() * globalWindow.innerHeight;
                        break;
                }
            }
        }


        const startTime = performance.now();

        function generateControlPoints() {
            let controlPoint1X = startX + (Math.random() - 0.5) * (endX - startX);
            let controlPoint1Y = startY + (Math.random() - 0.5) * (endY - startY);
            let controlPoint2X = startX + (Math.random() - 0.5) * (endX - startX);
            let controlPoint2Y = startY + (Math.random() - 0.5) * (endY - startY);

            // 对控制点进行边界检查
            controlPoint1X = Math.max(0, Math.min(controlPoint1X, globalWindow.innerWidth));
            controlPoint1Y = Math.max(0, Math.min(controlPoint1Y, globalWindow.innerHeight));
            controlPoint2X = Math.max(0, Math.min(controlPoint2X, globalWindow.innerWidth));
            controlPoint2Y = Math.max(0, Math.min(controlPoint2Y, globalWindow.innerHeight));

            return [controlPoint1X, controlPoint1Y, controlPoint2X, controlPoint2Y];
        }

        const controlPoints = generateControlPoints();

        function bezierPoint(t, x0, y0, x1, y1, x2, y2, x3, y3) {
            const u = 1 - t;
            const tt = t * t;
            const uu = u * u;
            const uuu = uu * u;
            const ttt = tt * t;

            let x = uuu * x0;
            x += 3 * uu * t * x1;
            x += 3 * u * tt * x2;
            x += ttt * x3;

            let y = uuu * y0;
            y += 3 * uu * t * y1;
            y += 3 * u * tt * y2;
            y += ttt * y3;

            return [x, y];
        }

        function step(timestamp) {
            const elapsed = timestamp - startTime;
            const progress = Math.min(elapsed / duration, 1);

            let [x, y] = bezierPoint(progress, startX, startY, ...controlPoints, endX, endY);

            // 边界检查，确保坐标在屏幕范围内
            x = Math.max(0, Math.min(x, globalWindow.innerWidth));
            y = Math.max(0, Math.min(y, globalWindow.innerHeight));
            const event = new MouseEvent('mousemove', {
                view: globalWindow,
                bubbles: true,
                cancelable: true,
                screenX: x,
                screenY: y,
                clientX: x,
                clientY: y
            });
            document.dispatchEvent(event);

            if (progress < 1) {
                requestAnimationFrame(step);
            } else {
                // 移动完成，更新上次位置
                gzb.lastX = x;
                gzb.lastY = y;
                // 解决 Promise，模拟阻塞结束
                resolve();
            }
        }

        requestAnimationFrame(step);
    });
}

gzb.move = function (x, y) {
    // 创建一个鼠标移动事件
    const event = new MouseEvent('mousemove', {
        view: window,
        bubbles: true,
        cancelable: true,
        clientX: x,
        clientY: y
    });

    document.dispatchEvent(event);
}
gzb.bezierCurve = function (points, t) {
    let n = points.length - 1;
    let result = [0, 0];
    for (let i = 0; i <= n; i++) {
        let coefficient = gzb.binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);
        result[0] += coefficient * points[i][0];
        result[1] += coefficient * points[i][1];
    }
    return result;
}
gzb.binomialCoefficient = function (n, k) {
    let result = 1;
    for (let i = 1; i <= k; i++) {
        result *= (n - (k - i)) / i;
    }
    return result;
}
gzb.simulateMouseMove = async function (startX, startY, endX, endY, totalTime, controlPointCount) {
    if (startX == null) {
        startX = gzb.random(window.innerWidth - 20, window.innerWidth)
    }
    if (startY == null) {
        startY = gzb.random(1, window.innerHeight)
    }
    // 生成控制点
    let controlPoints = [];
    controlPoints.push([startX, startY]);

    // 合理生成控制点
    const dx = endX - startX;
    const dy = endY - startY;
    if (controlPointCount == null) {
        if (dx > dy) {
            controlPointCount = dx / 10
        } else {
            controlPointCount = dy / 10
        }
        if (controlPointCount < 3) {
            controlPointCount = gzb.random(3, 6)
        }
        if (controlPointCount > 50) {
            controlPointCount = gzb.random(10, 50)
        }
        if (controlPointCount > 10) {
            controlPointCount = gzb.random(controlPointCount - (controlPointCount / 10), controlPointCount + (controlPointCount / 10))
        } else {
            controlPointCount = gzb.random(controlPointCount - 2, controlPointCount + 5)
        }
    }
    for (let i = 1; i <= controlPointCount; i++) {
        // 计算控制点在 x 和 y 方向上相对于起点到终点的偏移比例
        const ratio = i / (controlPointCount + 1);
        // 在 x 和 y 方向上添加一定的随机偏移
        const randomOffsetX = (Math.random() - 0.5) * Math.abs(dx) * 0.2;
        const randomOffsetY = (Math.random() - 0.5) * Math.abs(dy) * 0.2;
        const controlX = startX + dx * ratio + randomOffsetX;
        const controlY = startY + dy * ratio + randomOffsetY;
        controlPoints.push([controlX, controlY]);
    }

    controlPoints.push([endX, endY]);

    // 计算步数
    let len = gzb.random(6, 12);
    let steps = totalTime / len; // 每 10 毫秒一步
    for (let i = 0; i <= steps; i++) {
        let t = i / steps;
        let [currentX, currentY] = gzb.bezierCurve(controlPoints, t);

        // 创建鼠标移动事件
        let event = new MouseEvent('mousemove', {
            view: window,
            bubbles: true,
            cancelable: true,
            clientX: currentX,
            clientY: currentY
        });

        // 触发鼠标移动事件
        document.dispatchEvent(event);
        let a = gzb.random(3, len - 2)
        gzb.move(currentX, currentY);
        await gzb.sleep(a);
        gzb.move(currentX, currentY);
        if (i < steps / 10 * 7 + gzb.random(1, 50)) {
            await gzb.sleep(gzb.random(len - a - 1, len - a - +1) - 2);
        } else {
            await gzb.sleep(gzb.random(len - a - 1, len - a - +1) + gzb.random(1, 3));
        }
    }
}
gzb.mouseMove = async function (startX, startY, endX, endY, totalTime, controlPointCount) {
    let a = gzb.random(endX / 10 * 8, endX / 10 * 12)
    let b = gzb.random(endY / 10 * 8, endY / 10 * 12)
    if (gzb.random(1, 3) > 1) {
        await gzb.simulateMouseMove(startX, startY, a, b, totalTime / 10 * 7, controlPointCount);
        await gzb.sleep(gzb.random(totalTime / 10 * 0.5, totalTime / 10 * 1.5));
        await gzb.simulateMouseMove(a, b, endX, endY, totalTime / 10 * 2, controlPointCount);
    } else {
        await gzb.simulateMouseMove(startX, startY, endX, endY, totalTime, controlPointCount);
    }

}

//下一个节点（忽略文本节点、注释节点等）
gzb.next = function (node) {
    return node.nextElementSibling;
}
//下一个任意类型节点（包括文本节点、注释节点等）
gzb.nextSibling = function (node) {
    return node.nextSibling;
}
//获取前一个元素节点（忽略文本节点、注释节点等）
gzb.previousElementSibling = function (node) {
    return node.previousElementSibling;
}
//获取前一个任意类型节点（包括文本节点、注释节点等）
gzb.previousSibling = function (node) {
    return node.previousSibling;
}
//获取父元素
gzb.parentElement = function (node) {
    return node.parentElement;
}
//获取子元素
gzb.children = function (node) {
    return node.children;
}


gzb.getXY = function (ele) {
    let rect = ele.getBoundingClientRect();
    let x = rect.left + (rect.width / 2);
    let y = rect.top + (rect.height / 2);
    return {x: parseInt(x), y: parseInt(y)};
}
gzb.find = function (tag, text, textType, index, link, click, inputData) {
    tag = tag == null ? "*" : tag;
    text = text == null ? "" : text;
    textType = textType == null ? "text" : textType;
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
            let data1 = null;
            if (textType == null || textType == "text") {
                data1 = node.innerText;
            } else {
                data1 = node.getAttribute(textType);
            }
            if (data1 != null) {
                data1 = data1.replace(/\n/g, "")
                data1 = data1.replace(/ /g, "")
                data1 = data1.replace(/   /g, "")
            }
            if (data1 == null) {
                continue;
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
                node.getXY = gzb.getXY(node)
                node.XY = node.getXY.x + "," + node.getXY.y
                if (click) {
                    gzb.clickNode(node);
                    console.log("find true", tag, text, textType, index, link, click, inputData)
                }
                if (inputData != null && inputData.length > 0) {
                    gzb.inputText(node, inputData);
                    console.log("find true", tag, text, textType, index, link, click, inputData)
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
            console.log("find false", tag, text, textType, index, link, click, inputData)
            return null;
        } else {
            return resArray;
        }
    } else {
        console.log("find false", tag, text, textType, index, link, click, inputData)
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
        /*        function insertAtCursor(element, content) {
                    const selection = window.getSelection();
                    const range = selection.getRangeAt(0);

                    // 保存原始插入位置
                    const startOffset = range.startOffset;
                    const endOffset = range.endOffset;
                    const parentNode = range.commonAncestorContainer;

                    // 创建临时元素处理内容
                    const tempDiv = document.createElement('div');
                    tempDiv.innerHTML = content;

                    // 插入内容并保持原有 Selection
                    range.deleteContents();
                    const fragment = document.createDocumentFragment();
                    while (tempDiv.firstChild) {
                        fragment.appendChild(tempDiv.firstChild);
                    }
                    range.insertNode(fragment);

                    // 重新构建 Selection
                    const newRange = new Range();
                    newRange.setStart(parentNode, startOffset);
                    newRange.setEnd(parentNode, startOffset + content.length);

                    selection.removeAllRanges();
                    selection.addRange(newRange);
                }*/
        function insertAtCursor(element, content) {
            const selection = window.getSelection();
            if (selection.rangeCount > 0) {
                const range = selection.getRangeAt(0);
                const startOffset = range.startOffset;
                const parentNode = range.commonAncestorContainer;

                // 创建临时元素处理内容
                const tempDiv = document.createElement('div');
                tempDiv.innerHTML = content;

                // 插入内容
                range.deleteContents();
                const fragment = document.createDocumentFragment();
                while (tempDiv.firstChild) {
                    fragment.appendChild(tempDiv.firstChild);
                }
                range.insertNode(fragment);

                // 计算插入内容后的文本长度
                let insertedTextLength = 0;
                const walker = document.createTreeWalker(fragment, NodeFilter.SHOW_TEXT);
                while (walker.nextNode()) {
                    insertedTextLength += walker.currentNode.textContent.length;
                }

                // 找到插入内容后的最后一个文本节点
                let lastTextNode = null;
                let lastOffset = 0;
                const newWalker = document.createTreeWalker(parentNode, NodeFilter.SHOW_TEXT);
                let currentOffset = 0;
                while (newWalker.nextNode()) {
                    if (currentOffset + newWalker.currentNode.textContent.length >= startOffset + insertedTextLength) {
                        lastTextNode = newWalker.currentNode;
                        lastOffset = startOffset + insertedTextLength - currentOffset;
                        break;
                    }
                    currentOffset += newWalker.currentNode.textContent.length;
                }

                // 重新构建 Selection
                const newRange = new Range();
                if (lastTextNode) {
                    newRange.setStart(lastTextNode, lastOffset);
                    newRange.setEnd(lastTextNode, lastOffset);
                } else {
                    // 如果没有找到合适的文本节点，可能是插入到末尾的情况
                    newRange.setStart(parentNode, parentNode.textContent.length);
                    newRange.setEnd(parentNode, parentNode.textContent.length);
                }

                selection.removeAllRanges();
                selection.addRange(newRange);
            }
        }
        insertAtCursor(element,text)
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
gzb.clickNode = async function (element) {
    if (!element) {
        console.error('Element is not provided or does not exist.');
        return;
    }
    try {
        // 获取元素在页面中的位置和尺寸
        const rect = element.getBoundingClientRect();
        // 计算元素中心点的坐标 随机 4/2位置
        let clientX = gzb.random(rect.left + (rect.width / 10 * 1), rect.left + (rect.width / 10 * 1))
        let clientY = gzb.random(rect.top + (rect.height / 10 * 1), rect.top + (rect.height / 10 * 1))
        console.log("clickNode", clientX, clientY, "rect", rect)

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
        //await gzb.startSimulation(null, null, clientX, clientY, gzb.random(100, 200), true)
        await gzb.mouseMove(null, null, clientX, clientY, gzb.random(500, 1000), null)
        // 触发 mousedown 事件
        const mouseDownEvent = createMouseEvent('mousedown');
        element.dispatchEvent(mouseDownEvent);
        await gzb.sleepRandom(50, 100);
        // 触发 mouseup 事件
        const mouseUpEvent = createMouseEvent('mouseup');
        element.dispatchEvent(mouseUpEvent);
        // 触发 click 事件
        const clickEvent = createMouseEvent('click');
        element.dispatchEvent(clickEvent);
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
// 定义 MIME 类型到后缀名的映射
gzb.mimeTypeToExtensionMap = {
    'image/jpg': '.jpg',
    'image/jpeg': '.jpg',
    'image/png': '.png',
    'image/gif': '.gif',
    'application/pdf': '.pdf',
    'text/plain': '.txt',
    'application/msword': '.doc',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document': '.docx',
    // 可以根据需要添加更多映射
};

gzb.uploadLocalFilesBase64 = function (inputSelector, base64Images) {
    const inputElement = document.querySelector(inputSelector);
    if (!inputElement) {
        return;
    }
    const dataTransfer = new DataTransfer();
    let processedFiles = 0;

    function processNextFile() {
        if (processedFiles >= base64Images.length) {
            if (dataTransfer.files.length === 0) {
                console.error('没有有效的文件可供上传');
            } else {
                inputElement.files = dataTransfer.files;
                const changeEvent = new Event('change', {bubbles: true});
                inputElement.dispatchEvent(changeEvent);
            }
            return;
        }

        const base64Image = base64Images[processedFiles];
        let [mimeTypePart, base64Data] = base64Image.split(',');
        const mimeType = mimeTypePart.replace('data:', '').replace(';base64', '');

        const binaryString = atob(base64Data);
        const len = binaryString.length;
        const bytes = new Uint8Array(len);
        for (let i = 0; i < len; i++) {
            bytes[i] = binaryString.charCodeAt(i);
        }
        const blob = new Blob([bytes], {type: mimeType});

        // 根据 MIME 类型查找对应的后缀名
        let fileExtension = gzb.mimeTypeToExtensionMap[mimeType];
        if (!fileExtension) {
            fileExtension = '.unknown';
        }
        let fileName = new Date().getTime() + "_" + processedFiles + fileExtension;
        const file = new File([blob], fileName, {type: mimeType});
        dataTransfer.items.add(file);

        processedFiles++;
        processNextFile();
    }

    processNextFile();
};
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
                    let fileType = blob.type;
                    if (!fileType) {
                        // 如果 Blob.type 为空，从响应头中获取 Content-Type
                        fileType = response.getResponseHeader('Content-Type');
                    }

                    // 根据 MIME 类型查找对应的后缀名
                    let fileExtension = gzb.mimeTypeToExtensionMap[fileType];
                    if (!fileExtension) {
                        fileExtension = '.unknown';
                    }
                    let fileName = new Date().getTime() + "_" + processedFiles + fileExtension;
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
};
gzb.uploadLocalFilesFetch = function (inputSelector, filePaths) {
    const inputElement = document.querySelector(inputSelector);
    if (!inputElement) {
        return;
    }
    const dataTransfer = new DataTransfer();
    let processedFiles = 0;

    async function processNextFile() {
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

        try {
            const response = await fetch(fixedFilePath);
            if (response.ok) {
                const blob = await response.blob();
                let fileType = blob.type;
                if (!fileType) {
                    // 如果 Blob.type 为空，从响应头中获取 Content-Type
                    fileType = response.headers.get('Content-Type');
                }

                // 根据 MIME 类型查找对应的后缀名
                let fileExtension = gzb.mimeTypeToExtensionMap[fileType];
                if (!fileExtension) {
                    fileExtension = '.unknown';
                }
                let fileName = new Date().getTime() + "_" + processedFiles + fileExtension;
                const file = new File([blob], fileName, {type: blob.type});
                dataTransfer.items.add(file);
            } else {
                console.error(`读取文件 ${filePath} 失败，状态码：${response.status}`);
            }
        } catch (error) {
            console.error(`处理文件 ${filePath} 时出错：`, error);
        }

        processedFiles++;
        processNextFile();
    }

    processNextFile();
};

gzb.uploadLocalFilesXHR = function (inputSelector, filePaths) {
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
        const xhr = new XMLHttpRequest();
        xhr.open('GET', fixedFilePath, true);
        xhr.responseType = 'blob';

        xhr.onload = function () {
            if (xhr.status === 200) {
                const blob = xhr.response;
                let fileType = blob.type;
                if (!fileType) {
                    // 如果 Blob.type 为空，从响应头中获取 Content-Type
                    fileType = xhr.getResponseHeader('Content-Type');
                }

                // 根据 MIME 类型查找对应的后缀名
                let fileExtension = gzb.mimeTypeToExtensionMap[fileType];
                if (!fileExtension) {
                    fileExtension = '.unknown';
                }
                let fileName = new Date().getTime() + "_" + processedFiles + fileExtension;
                const file = new File([blob], fileName, {type: blob.type});
                dataTransfer.items.add(file);
            } else {
                console.error(`读取文件 ${filePath} 失败，状态码：${xhr.status}`);
            }
            processedFiles++;
            processNextFile();
        };

        xhr.onerror = function () {
            console.error(`处理文件 ${filePath} 时出错：`, xhr.statusText || '未知错误');
            processedFiles++;
            processNextFile();
        };

        xhr.send();
    }

    processNextFile();
};
gzb.random = function (min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

gzb.request = async function (url, met, data) {
    if (data == null) {
        data = ""
    }
    console.log("gzb.request", url, met, data)
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
gzb.getCanvasFingerprint = function () {
    function bin2crc(bin, crcTable) {
        let crc = 0 ^ (-1);
        for (let i = 0; i < bin.length; i++) {
            crc = (crc >>> 8) ^ crcTable[(crc ^ bin.charCodeAt(i)) & 0xFF];
        }
        return (crc ^ (-1)) >>> 0;
    }

// 定义 CRC 表
    function makeCRCTable() {
        let crcTable = [];
        let c;
        for (let n = 0; n < 256; n++) {
            c = n;
            for (let k = 0; k < 8; k++) {
                c = ((c & 1) ? (0xEDB88320 ^ (c >>> 1)) : (c >>> 1));
            }
            crcTable[n] = c;
        }
        return crcTable;
    }

    let canvas = document.createElement('canvas');
    let ctx = canvas.getContext('2d');
    ctx.textBaseline = "top";
    ctx.font = "14px 'Arial'";
    ctx.textBaseline = "alphabetic";
    ctx.fillStyle = "#f60";
    ctx.fillRect(125, 1, 62, 20);
    ctx.fillStyle = "#069";
    ctx.fillText("Cwm fjordbank glyphs vext quiz, 1234567890", 2, 15);
    ctx.fillStyle = "rgba(102, 204, 0, 0.7)";
    ctx.fillText("Cwm fjordbank glyphs vext quiz, 1234567890", 4, 17);
    let b64 = canvas.toDataURL().replace("data:image/png;base64,", "");
    let bin = atob(b64);
    let crc = bin2crc(bin, makeCRCTable());
    return crc;
}

gzb.getAudioFingerprint = async function () {
    // 创建 AudioContext 对象，兼容不同浏览器
    let audioContext = new (globalWindow.AudioContext || globalWindow.webkitAudioContext)();
    // 创建振荡器
    let oscillator = audioContext.createOscillator();
    // 设置振荡器类型为正弦波
    oscillator.type = 'sine';
    // 设置振荡器频率为 440Hz
    oscillator.frequency.setValueAtTime(440, audioContext.currentTime);
    // 创建音频分析器
    let analyser = audioContext.createAnalyser();
    // 将振荡器连接到分析器
    oscillator.connect(analyser);
    // 将分析器连接到音频输出
    analyser.connect(audioContext.destination);
    // 启动振荡器
    oscillator.start();
    await gzb.sleep(300)
    // 设置一个延迟，等待音频信号稳定
    await setTimeout(() => {
        // 获取分析器的频率数据缓冲区长度
        let bufferLength = analyser.frequencyBinCount;
        // 创建一个 Uint8Array 数组来存储频率数据
        let dataArray = new Uint8Array(bufferLength);
        // 从分析器中获取频率数据
        analyser.getByteFrequencyData(dataArray);
        // 将频率数据数组转换为字符串作为音频指纹
        let audioFingerprint = dataArray.join('');
        console.log('AudioContext 指纹:', audioFingerprint);
        // 停止振荡器
        oscillator.stop();
    }, 100); // 延迟 200 毫秒，可根据实际情况调整
}

gzb.startHookJSON = function () {
    if (window.originalStringify == null) {
        window.originalStringify = JSON.stringify;
    }
    if (window.originalParse == null) {
        window.originalParse = JSON.parse;
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
            console.log(e)
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
            console.log(e)
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
    let arr1 = []
    for (let activationFunArrayElement of window.hook_json_parse_fun_aray) {
        if (activationFunArrayElement == null) {
            continue;
        }
        if (activationFunArrayElement["key"] == key) {
            continue;
        }
        arr1.push(activationFunArrayElement)
    }
    window.hook_json_parse_fun_aray = arr1
}
gzb.delHookJSONStringify = function (key) {
    let arr1 = []
    for (let activationFunArrayElement of window.hook_json_stringify_fun_aray) {
        if (activationFunArrayElement == null) {
            continue;
        }
        if (activationFunArrayElement["key"] == key) {
            continue;
        }
        arr1.push(activationFunArrayElement)
    }
    window.hook_json_stringify_fun_aray = arr1
}

gzb.startHookHTTP = function () {
    if (window.originalXHR == null) {
        window.originalXHR = window.XMLHttpRequest;
    }
    if (window.originalFetch == null) {
        window.originalFetch = window.fetch;
    }
    console.log("window.originalXHR", window.originalXHR)
    console.log("window.originalFetch", window.originalFetch)
    console.log("window.hook_http_fun_array", window.hook_http_fun_array)
    // 拦截 XMLHttpRequest
    window.XMLHttpRequest = function () {
        const xhr = new window.originalXHR();
        const originalOpen = xhr.open;
        const originalSend = xhr.send;

        xhr.open = function (method, url, async, user, password) {
            this._requestUrl = url;
            console.log("url", url)
            console.log(`XHR open called for URL: ${url}`);
            return originalOpen.call(this, method, url, async, user, password);
        };

        xhr.send = function (data) {
            const url = this._requestUrl;
            console.log("url", url)
            const hooks = window.hook_http_fun_array.filter(hook => url.includes(hook.url));
            if (hooks.length > 0) {
                console.log(`XHR request to ${url} is being hooked.`);
                const originalOnLoad = this.onload;
                this.onload = function () {
                    console.log('XHR onload event triggered.');
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
            console.log(`XHR sending data for URL: ${url}`);
            return originalSend.call(this, data);
        };
        return xhr;
    };

    // 拦截 fetch
    window.fetch = async function (input, init) {
        const url = typeof input === 'string' ? input : input.url;
        console.log(`Fetching request to: ${url}`);
        const hooks = window.hook_http_fun_array.filter(hook => url.includes(hook.url));
        const response = await window.originalFetch(input, init);
        if (hooks.length > 0) {
            console.log(`Fetch request to ${url} is being hooked.`);
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
    let arr1 = []
    for (let activationFunArrayElement of window.hook_http_fun_array) {
        if (activationFunArrayElement == null) {
            continue;
        }
        if (activationFunArrayElement["url"] == key) {
            continue;
        }
        arr1.push(activationFunArrayElement)
    }
    window.hook_http_fun_array = arr1
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
                                console.log("循环->this：" + (gzb.startActivationFun_sleep * 100) + "ms", "next：" + (Number(mm) * 100) + "ms")
                            }
                            gzb.startActivationFun_sleep = Number(mm);
                        } else {
                            if (activation_fun["ss"] != null) {
                                gzb.startActivationFun_sleep = Number(activation_fun["ss"]);
                            }
                        }
                    }
                } catch (e) {
                    console.log(e)
                } finally {
                    if (gzb.deBug == false) {
                        console.clear();
                    }
                }
            }
        }, 100);
    } else {
        console.log("重复启动 startActivationFun:" + gzb.tid)
        return;
    }
    console.log("启动 startActivationFun:" + gzb.tid)
}
gzb.addActivationFun = function (url, ss, fun) {
    window.activation_fun_array[window.activation_fun_array.length] = {
        "url": url,
        "ss": ss,
        "fun": fun
    }
}
gzb.delActivationFun = function (key) {
    let arr1 = []
    for (let activationFunArrayElement of window.activation_fun_array) {
        if (activationFunArrayElement == null) {
            continue;
        }
        if (activationFunArrayElement["url"] == key) {
            continue;
        }
        arr1.push(activationFunArrayElement)
    }
    window.activation_fun_array = arr1
}

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
gzb.sleep = async function (ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
gzb.sleepRandom = async function (min, max) {
    return await gzb.sleep(gzb.random(min, max));
}
gzb.log = function (...text) {
    if (gzb.originalLog != null) {
        gzb.originalLog.apply(console, text);
    } else if (gzb.originalInfo != null) {
        gzb.originalInfo.apply(console, text);
    }
}
gzb.init = function () {
    gzb.startMouseDisplay();
}
// 保存原始的 console.log 函数
if (window.originalLog == null) {
    window.originalLog = console.log;
}
if (window.originalInfo == null) {
    window.originalInfo = console.info;
}

// 重写 console.log 函数
console.log = function (...args) {
    if (gzb.deBug) {
        window.originalLog.apply(console, args);
    }
};
console.info = function (...args) {
    if (gzb.deBug) {
        window.originalInfo.apply(console, args);
    }
};
