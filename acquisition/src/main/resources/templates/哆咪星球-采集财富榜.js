let msg = "自动写作业程序，实时老板ID 需要吗？\n" +
    "这里不方便说.\n" +
    "看图片上的网站可以连洗哦";
let sendMsg = false;
let zhou = true;
let yue = false;
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

let awaitRes = async function (max) {
    for (let i = 0; i < max; i++) {
        if (window._caifu_ok === 1) {
            return true;
        }
        await sleep(1000)
    }
    return false;
}

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

function sleep(min, max) {
    let ms = min;
    if (ms == null) {
        ms = 1;
    }
    if (max != null) {
        ms = random(min, max)
    }
    return new Promise(resolve => setTimeout(resolve, ms));
}

function 重启哆咪() {
    let name = localStorage["_box_name"];
    if (name == null) {
        name = "";
    }
    xhrGet("http://127.0.0.1:38001/stopAndRun?name=" + name);
}

function hook_json_parse_财富榜() {
    if (!window._originalJSONParse) {
        window._originalJSONParse = JSON.parse;
    }
    if (window._map00x01 == null) {
        window._map00x01 = {};
    }
    // 定义新的 Hook 函数
    JSON.parse = function (jsonString) {
        const result = window._originalJSONParse(jsonString);
        try {
            /*
            if (Array.isArray(result) && result[0]!=null && result[0].jsonExt != null) {
                let postData = "";
                for (let json_01 of result) {
                    let erbanNo = json_01.jsonExt.erbanNo
                    let uid = json_01.jsonExt.uid
                    let nick = json_01.nick_
                    let gender = json_01.jsonExt.gender
                    let vip_id = json_01.jsonExt.vip_id
                    if (window._map00x01[erbanNo] != null) {
                        continue;
                    }
                    window._map00x01[erbanNo] = erbanNo
                    postData+="roomUserOuId=" +erbanNo.toString()+
                        "&roomUserOuId2=" +uid.toString()+
                        "&roomUserOuId3=" +
                        "&roomUserOuName=" +nick.toString()+
                        "&roomUserSex=" +(Number(gender)+10).toString()+
                        "&roomUserRid=" +window._ting.duoMiListNid+
                        "&roomUserType=" +(Number(window._ting_type)+10).toString()+
                        "&roomUserPrice=" +-1+
                        "&roomUserState=" +0+
                        "&roomUserTime=" +new Date().Format("yyyy-MM-dd hh:mm:ss")+
                        "&roomUserAid=" +aid+
                        "&roomUserData="+(vip_id==null?"":vip_id)+"&";
                }
                if (postData.length > 0) {
                    let res=xhrPost(server + "/system/v1.0.0/script/rooms/user/save", postData);
                    console.log("request 在线 ",res)
                }
            }
            */


            if (result != null && result.data != null && result.data.me != null && result.data.rankings != null) {
                if (Array.isArray(result.data.rankings)) {
                    let postData = "";
                    for (let ranking of result.data.rankings) {
                        if (window._map00x01[ranking.erbanNo] != null) {
                            continue;
                        }
                        window._map00x01[ranking.erbanNo] = ranking.erbanNo;
                        //newTime.Format("yyyy-MM-dd hh:mm:ss")
                        const now = new Date();
                        const oneHour = 60 * 60 * 1000; // 1小时对应的毫秒数
                        const newTime = new Date(now.getTime() - oneHour); // 用时间戳相减
                        let time1 = newTime.Format("yyyy-MM-dd hh:mm:ss");
                        postData += "roomUserOuId=" + ranking.erbanNo.toString() +
                            "&roomUserOuId2=" + ranking.uid.toString() +
                            "&roomUserOuId3=" +
                            "&roomUserOuName=" + ranking.nick.toString() +
                            "&roomUserSex=" + ranking.gender.toString() +
                            "&roomUserRid=" + window._ting.duoMiListNid +
                            "&roomUserType=" + window._ting_type +
                            "&roomUserPrice=" + ranking.amount.toString() +
                            "&roomUserState=" + 0 +
                            "&roomUserTime=" + time1 +
                            "&roomUserAid=" + aid +
                            "&roomUserData=" + encodeURIComponent(ranking.userDesc) + "&";
                    }
                    if (postData.length > 0) {
                        let res = xhrPost(server + "/system/v1.0.0/script/rooms/user/save", postData);
                        console.log("request 财富榜 ", res)
                        window._caifu_ok = 1;
                    }
                }
            }
        } catch (error) {
            console.log(error.message)
        }
        return result;
    };
}

async function 开始爬数据_财富榜(maxNum, zhou, yue, send, quit, stopApp) {
    hook_json_parse_财富榜()
    let node = null;
    let num = 0;
    while (true) {
        num++;
        console.log("num", num, maxNum)
        if (num > maxNum) {
            break;
        }
        let data1 = xhrGet(server + "/system/v1.0.0/script/rooms/read?roomsAid=" + aid + "&tableName=rooms")
        data1 = JSON.parse(data1);
        if (data1.code === "1") {
            let url = 'index.html#/roomPage?' +
                'roomUid=' + data1.data[0].roomsOid3 + '&' +
                'roomId=' + data1.data[0].roomsOid2 + '&' +
                'type=' + data1.data[0].roomsType + '&' +
                'source=1';
            console.log(data1.data[0].roomsName, url)
            window._ting = data1.data[0]
            window._ting_type = data1.data[0].roomsSex
            window._ting.duoMiListNid = data1.data[0].roomsOid

            location.href = url;
            await sleep(2000)
            window._caifu_ok = 0;
            //采集今日  必须采集
            for (let i = 0; i < 6; i++) {
                let node1 = document.getElementById("tab-wealth")
                if (node1 != null && node1.innerText != null && node1.innerText.indexOf("财富") > -1) {
                    node1.click();
                }
                await sleep(2000)
                node1 = document.getElementsByClassName("el-radio-button__inner")
                if (node1 != null && node1.length > 0) {
                    break;
                }
            }
            //等待数据传输
            await awaitRes(5)
            let nodes1 = document.getElementsByClassName("el-radio-button__inner")
            if (nodes1.length !== 3) {
                continue;
            }
            if (zhou) {
                //点击周榜
                window._caifu_ok = 0;
                nodes1[1].click();
                await sleep(1000)
                await awaitRes(5)
            }
            if (yue) {
                //点击周榜
                window._caifu_ok = 0;
                nodes1[2].click();
                await sleep(1000)
                await awaitRes(5)
            }
            if (send) {
                //私信她们
                await 私信主持(msg);
            }
            await sleep(random(5000, 10000))
            if (quit) {
                //退出房间
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

        } else {
            break;
        }
        await sleep(random(5000, 10000))
    }
    console.log("停止运行")
    if (stopApp) {
        重启哆咪()
    }
}

async function 房间内采集财富榜数据() {
    window._caifu_ok = 0;
    //采集今日  必须采集
    for (let i = 0; i < 6; i++) {
        let node1 = document.getElementById("tab-wealth")
        if (node1 != null && node1.innerText != null && node1.innerText.indexOf("财富") > -1) {
            node1.click();
        }
        await sleep(2000)
        node1 = document.getElementsByClassName("el-radio-button__inner")
        if (node1 != null && node1.length > 0) {
            break;
        }
    }
    //等待数据传输
    await awaitRes(5)
    let nodes1 = document.getElementsByClassName("el-radio-button__inner")
    if (nodes1.length !== 3) {
        return;
    }
    if (zhou) {
        //点击周榜
        window._caifu_ok = 0;
        nodes1[1].click();
        await sleep(1000)
        await awaitRes(5)
    }
    if (yue) {
        //点击周榜
        window._caifu_ok = 0;
        nodes1[2].click();
        await sleep(1000)
        await awaitRes(5)
    }
    await sleep(1000);
}

async function 进入房间框架流程(进入后回调, 是否退出) {
    hook_json_parse_财富榜()
    let node = null;
    while (true) {
        let data1 = xhrGet(server + "/system/v1.0.0/script/rooms/read?roomsAid=" + aid + "&tableName=rooms")
        data1 = JSON.parse(data1);
        if (data1.code === "1") {
            let url = 'index.html#/roomPage?' +
                'roomUid=' + data1.data[0].roomsOid3 + '&' +
                'roomId=' + data1.data[0].roomsOid2 + '&' +
                'type=' + data1.data[0].roomsType + '&' +
                'source=1';
            console.log(data1.data[0].roomsName, url);
            window._ting = data1.data[0];
            window._ting_type = data1.data[0].roomsSex;
            window._ting.duoMiListNid = data1.data[0].roomsOid;
            location.href = url;
            window._caifu_ok = 0;
            await 进入后回调();
            if (是否退出) {
                //退出房间
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
        } else {
            break;
        }
        await sleep(random(2000, 4000))
    }
    console.log("停止运行")
    重启哆咪()
}

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
    let nodes = null;
    for (let i = 0; i < 20; i++) {
        await sleep(1000);
        nodes = document.getElementsByClassName("magic-animation magic");
        if (nodes != null) {
            break;
        }
    }
    if (nodes == null || nodes.length === 0) {
        return false;
    }
    for (let node of nodes) {
        if (node.offsetHeight < 1) {
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
        console.log("完毕", res)
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
    node0 = document.getElementsByClassName("back-icon")
    if (node0 == null) {
        return false;
    }
    node0 = node0[0];
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
        let data1 = xhrGet(server + "/system/v1.0.0/script/rooms/read?roomsAid=" + aid + "&tableName=rooms_send")
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
                let nodes = document.getElementsByClassName("member-list")
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

async function 启动入口() {
    console.log("启动入口.sleep")
    await sleep(1000 * 10);
    console.log("启动入口.开始爬数据_财富榜")
    //参数含义  打开房间次数  周榜 月榜 发送信息 退出房间
    let num = random(1, 2);
    await 开始爬数据_财富榜(num, true, false, false, true, false);
}

async function read_users(type) {
    if (window._map00x01 == null) {
        window._map00x01 = {};
    }
    let uid0 = 123456
    let arr1 = [];
    let rooms = xhrGet(server + "/system/v1.0.0/script/rooms/read?roomsAid=" + aid + "&tableName=rooms")
    if (rooms == null) {
        return arr1;
    }
    rooms = JSON.parse(rooms);
    if (rooms.code !== "1") {
        return arr1;
    }
    for (let typeElement of type) {
        if (typeElement[2] == null) {
            typeElement[2]=aid;
        }
        let rooms_user = await window._getRoomRankList({
            "uid": uid0,
            "roomUid": rooms.data[0].roomsOid3,
            "type": typeElement[0],
            "subType": typeElement[1],
            "pageNum": 1,
            "pageSize": 100
        });
        if (rooms_user != null && rooms_user.code.toString() === "200") {
            postData1(rooms_user, rooms,typeElement[2])
        }
        await sleep(1000 * 5, 1000 * 15);
    }
}

function postData1(rooms_user, rooms,aid) {
    let rooms_id = rooms.data[0].roomsOid;
    let rooms_sex = -1;
    if (rooms.data[0]["roomsTag"] === "男声") {
        rooms_sex = 1;
    } else if (rooms.data[0]["roomsTag"] === "女声") {
        rooms_sex = 2;
    } else {
        rooms_sex = rooms.data[0]["roomsSex"];
    }
    let postData = "";
    const now = new Date();
    const oneHour = 60 * 60 * 1000; // 1小时对应的毫秒数
    const newTime = new Date(now.getTime() - oneHour); // 用时间戳相减
    let time1 = newTime.Format("yyyy-MM-dd hh:mm:ss");
    for (let ranking of rooms_user.data.rankings) {
        if (window._map00x01[ranking.erbanNo] != null) {
            continue;
        }
        window._map00x01[ranking.erbanNo] = ranking.erbanNo;
        uid0 = ranking.uid.toString();
        postData += "roomUserOuId=" + ranking.erbanNo.toString() +
            "&roomUserOuId2=" + ranking.uid.toString() +
            "&roomUserOuId3=" +
            "&roomUserOuName=" + ranking.nick.toString() +
            "&roomUserSex=" +  ranking.gender.toString() +
            "&roomUserRid=" + rooms_id +
            "&roomUserType=" + rooms_sex +
            "&roomUserPrice=" + ranking.amount.toString() +
            "&roomUserState=" + 0 +
            "&roomUserTime=" + time1 +
            "&roomUserAid=" + aid +
            "&roomUserData=" + encodeURIComponent(ranking.userDesc) + "&";
    }

    if (postData.length > 0) {
        let res = xhrPost(server + "/system/v1.0.0/script/rooms/user/save", postData);
        console.log("request 财富榜 ", res)
    }
}

//type 0魅力  1财富
//subType 0 1 2 今天 本周 总榜
async function start002(max) {
    let num01 = 0;
    while (true) {
        await read_users([
            [1, 0,13525685],
            [1, 1,13525685],
            [0, 0,13525686],
            [0, 1,13525686]
        ]);
        await sleep(1000 * 10, 1000 * 30);
        num01++;
        if (max != null && num01 >= max) {
            console.log("运行结束", num01, "/", max)
            break;
        } else {
            console.log("次数", num01, "/", max == null ? "不限制" : max)
        }
    }
}

start002(500)