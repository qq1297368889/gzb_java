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
let token="5ba21830-ea9b-42bc-9e78-ac2c5c952720";
let deviceId="bd42e00add4a5df0e704e8ac57c37571";
let tags=[5, 50, 8, 9, 26, 34];
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

async function 获取房间列表_派对房(page, max) {
    for (let number of tags) {
        let jsonArray1 = [];
        for (let i = page - 1; i < max; i++) {
            let url = "https://papi.enlargemagic.com/home/fun/tag/list/v2?" +
                "ticket=" +token+
                "&uid=61971585" +
                "&appVersion=1.5.8" +
                "&appVersionCode=158" +
                "&deviceId=" +deviceId+
                "&pageNum=" + (i + 1) + "" +
                "&pageSize=100" +
                "&os=window&tagId=" + number;

            let data = xhrGet(url)
            data = JSON.parse(data);
            console.log(data)
            if (data.code !== 200 || data.data == null) {
                break;
            }
            let postData = "";
            let puArray = function (obj1) {
                jsonArray1.push({
                    "roomId": obj1.roomId,
                    "uid": obj1.uid,
                    "type": obj1.type,
                    "nick": obj1.nick,
                    "gender": obj1.gender,
                    "roomTag": obj1.roomTag,
                    "erbanNo": obj1.erbanNo,
                    "onlineNum": obj1.onlineNum
                })
                postData += "roomsOid=" + obj1.erbanNo +
                    "&roomsOid2=" + obj1.roomId +
                    "&roomsOid3=" + obj1.uid +
                    "&roomsName=" + encodeURIComponent(obj1.title) +
                    "&roomsSex=" + obj1.gender +
                    "&roomsTag=" + encodeURIComponent(obj1.roomTag) +
                    "&roomsType=" + obj1.type +
                    "&roomsHeat=" + obj1.onlineNum +
                    "&roomsAid=" + aid +
                    "&roomsDesc=" + encodeURIComponent("手机脚本采集") +
                    "&roomsTime=" + new Date().Format("yyyy-MM-dd hh:mm:ss") +
                    "&roomsState=" + 1 +
                    "&roomsRead=" + 0 + "&";

            }

            if (Array.isArray(data.data)) {
                for (let obj1 of data.data) {
                    puArray(obj1);
                }
            }

            if (data.data.top != null && Array.isArray(data.data.top)) {
                for (let obj1 of data.data.top) {
                    puArray(obj1);
                }
            }
            if (data.data.list != null && Array.isArray(data.data.list)) {
                for (let obj1 of data.data.list) {
                    puArray(obj1);
                }
            }
            if (jsonArray1.length === 0) {
                break;
            }
            xhrPost(server + "/system/v1.0.0/script/rooms/save", postData);
            jsonArray1 = [];
            await sleep(5000)
        }
    }
    console.log("抓取完毕")
}

获取房间列表_派对房(1,100);