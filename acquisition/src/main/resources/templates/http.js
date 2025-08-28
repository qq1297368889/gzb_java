// 创建 http 对象
let http = {};
// 查询字符串处理方法
http.processQueryData = function(data) {
    if (typeof data === 'string') return data;
    if (!data || typeof data !== 'object') return '';

    return Object.entries(data)
        .map(([key, value]) => {
            if (Array.isArray(value)) {
                return value.map(v => `${encodeURIComponent(key)}=${encodeURIComponent(v)}`).join('&');
            }
            return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
        })
        .join('&');
};
// 通用请求方法  同步加 await  异步这样用 http.get('/api/data').then(data => console.log(data)).catch(err => console.error(err));
http.request = async function(url, method, data = '', headers = {}) {
    try {
        let fullUrl = url;
        let options = {
            method,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                ...headers
            }
        };

        // 处理GET请求的数据
        if (method === 'GET' && data) {
            const queryString = this.processQueryData(data);
            fullUrl += (fullUrl.includes('?') ? '&' : '?') + queryString;
        }
        // 处理非GET请求的数据
        else if (['POST', 'PUT', 'DELETE'].includes(method) && data) {
            const contentType = options.headers['Content-Type'];

            if (contentType === 'application/x-www-form-urlencoded') {
                options.body = this.processQueryData(data);
            } else if (contentType === 'application/json') {
                options.body = JSON.stringify(data);
            } else if (contentType === 'multipart/form-data') {
                options.body = data;
                delete options.headers['Content-Type'];
            }
        }

        const response = await fetch(fullUrl, options);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // 根据响应头判断如何解析响应
        const contentType = response.headers.get('content-type');
        let result;

        if (contentType && contentType.includes('application/json')) {
            result = await response.json();
        } else if (contentType && contentType.includes('text/')) {
            result = await response.text();
        } else if (contentType && contentType.includes('application/xml') || contentType.includes('text/xml')) {
            result = await response.text(); // 可以进一步解析为XML
        } else {
            // 未知类型，返回原始响应对象
            result = response;
            console.warn('Unknown content type:', contentType);
        }

        return result;
    } catch (error) {
        console.error('Request failed:', error);
        throw error;
    }
};
// GET 请求
http.get = function(url, data = '', headers = {}) {
    return this.request(url, 'GET', data, headers);
};
// POST 请求
http.post = function(url, data = '', headers = {}) {
    return this.request(url, 'POST', data, headers);
};
// PUT 请求
http.put = function(url, data = '', headers = {}) {
    return this.request(url, 'PUT', data, headers);
};
// DELETE 请求
http.delete = function(url, data = '', headers = {}) {
    return this.request(url, 'DELETE', data, headers);
};
http.test= async function (){
    let getUrl="http://192.168.10.101:2081/system/v1.0.0/script/test001";
    let postUrl="http://192.168.10.101:2081/system/v1.0.0/script/test002";
    console.log("getUrl",await http.get(getUrl, 'id=1&name=test', {}));
    console.log("postUrl",await http.post(postUrl, 'id=1&name=test', {}));
}

