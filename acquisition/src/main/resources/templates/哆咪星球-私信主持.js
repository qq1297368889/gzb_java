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
let server = "https://sid.0505.fun/";
let aid = 13525685


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
    // 第三个参数设为 false 表示同步请求
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
function random(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
function sleep(min,max) {
    let ms = min;
    if (ms==null) {
        ms=1;
    }
    if (max != null) {
        ms = random(min,max)
    }
    return new Promise(resolve => setTimeout(resolve, ms));
}

function 重启哆咪(){
    xhrGet("http://127.0.0.1:38001/stopAndRun?name="+localStorage["_box_name"]);
}

let sendMsg=false;
/**
 * 模拟触发 DOM 元素的回车键事件
 * @param {HTMLElement} element - 要触发事件的 DOM 元素
 * @param {Object} [options] - 可选配置项
 * @param {number} [options.keyCode=13] - 键码，默认为 13 (Enter)
 * @param {boolean} [options.bubbles=true] - 事件是否冒泡
 * @param {boolean} [options.cancelable=true] - 事件是否可取消
 */
function simulateEnterKey(element, options = {}) {
    const {
        keyCode = 13,
        bubbles = true,
        cancelable = true
    } = options;

    // 创建键盘事件
    const event = new KeyboardEvent('keydown', {
        key: 'Enter',
        keyCode: keyCode,
        code: 'Enter',
        bubbles: bubbles,
        cancelable: cancelable,
        view: window
    });

    // 触发事件
    element.dispatchEvent(event);
}
async function 私信主持(msg) {
    let nodes=null;
    for (let i = 0; i < 20; i++) {
        await sleep(1000);
        nodes=document.getElementsByClassName("magic-animation magic");
        if (nodes != null) {
            break;
        }
    }
    if (nodes==null || nodes.length === 0) {
        return false;
    }
    for (let node of nodes) {
        if (node.offsetHeight<1) {
            continue;
        }
        console.log(node)
        //麦上八个人
        node.parentElement.parentElement.children[0].click();
        await sleep(1500);
        //点击打招呼
        document.getElementsByClassName("send-gift")[0].parentElement.children[1].click()
        await sleep(1500);
        await 发送消息(msg, sendMsg, sendMsg);
    }
}

async function 发送消息(msg, upload, click01) {
    let node0 = null;
    node0 = document.getElementById("emojiText");
    if (node0 == null) {
        return false;
    }
    node0.innerText = "你好";
    await sleep(500);
    if (click01) {
        simulateEnterKey(node0);
        await sleep(500);
    }
    if (upload) {
        console.log("开始上传")
        let res = await (await fetch("http://127.0.0.1:38001/uploadImage")).text();
        console.log("完毕",res)
        await sleep(1500);
    }
    node0 = document.getElementById("emojiText");
    if (node0 == null) {
        return false;
    }
    node0.innerText = msg;
    await sleep(500);
    if (click01) {
        simulateEnterKey(node0);
        await sleep(1500);
    }
    node0=document.getElementsByClassName("back-icon")
    if (node0 == null) {
        return false;
    }
    node0=node0[0];
    if (node0 != null) {
        node0.click()
    }
    await sleep(500);
}

async function 循环_私信主持(msg, max) {
    let node = null;
    let thisNum = 0;
    while (true) {
        thisNum++;
        if (thisNum > max) {
            break;
        }
        console.log("thisNum", thisNum, max)
        let data1 = xhrGet(server + "/system/v1.0.0/script/rooms/read?roomsAid=" + aid+"&tableName=rooms_send")
        data1 = JSON.parse(data1);
        console.log(data1)
        if (data1.code == "1") {
            let url = 'index.html#/roomPage?' +
                'roomUid=' + data1.data[0].roomsOid3 + '&' +
                'roomId=' + data1.data[0].roomsOid2 + '&' +
                'type=' + data1.data[0].roomsType + '&' +
                'source=1';
            location.href = url
            console.log(data1.data[0].roomsName, url)

            for (let i = 0; i < 10; i++) {
                let nodes=document.getElementsByClassName("member-list")
                if (nodes == null || nodes.length == 0) {
                    continue;
                }
                node = nodes[0];
                if (node != null) {
                    node = node.children;
                    if (node.length > 0) {
                        break;
                    }
                }
                await sleep(1000)
            }
            await sleep(1000)
            try {
                await 私信主持(msg)
            } catch (e) {
                console.log(e.message)
            }

            await sleep(1000)
            for (let i = 0; i < 5; i++) {
                node = document.querySelector(".quit");
                console.log(".quit", node)
                if (node) {
                    await sleep(2000)
                    node.click();
                    break;
                }
                await sleep(1000)
            }
        }
    }
    重启哆咪()
    console.log("停止运行")
}
let msg="自动写作业程序，实时老板ID 需要吗？\n" +
    "这里不方便说.\n" +
    "看图片上的网站可以连洗哦";
//启动入口();
async function 启动入口() {
    console.log("启动入口.sleep")
    await sleep(1000*10);
    console.log("启动入口.循环_私信主持")
    await 循环_私信主持(msg, 15);
}
//启动入口();
//发送消息(msg,true,true);
循环_私信主持(msg, 1);

