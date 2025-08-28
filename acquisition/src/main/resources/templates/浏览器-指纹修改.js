function spoofProperty(target, property, value) {
    if (target == null || property == null || value == null) {
        return;
    }
    console.log(`Object.defineProperty${target} ${property}: ${value}`);

    try {
        Object.defineProperty(target, property, {
            get: () => value,
            configurable: true,
        });
    } catch (e) {
        // 捕获可能因属性不可配置而抛出的错误
        console.log(e)
    }
}

/**
 * 伪造浏览器的核心指纹属性。
 * @param {object} [options={}] - 包含指纹信息的配置对象。
 * @param {string} [options.userAgent] - 要伪造的 User-Agent 字符串。
 * @param {string[]} [options.languages] - 语言列表。
 * @param {string} [options.language] - 当前首选语言。
 * @param {number} [options.screenWidth] - 屏幕宽度。
 * @param {number} [options.screenHeight] - 屏幕高度。
 * @param {number} [options.deviceMemory] - 设备内存 (GB)。
 * @param {number} [options.hardwareConcurrency] - CPU 核心数。
 * @param {number} [options.timezoneOffset] - 时区偏移量（分钟）。
 * @param {object} [options.geolocation] - 伪造的地理位置对象。
 * @param {number} [options.geolocation.latitude] - 纬度。
 * @param {number} [options.geolocation.longitude] - 经度。
 */
function spoofBrowserFingerprint(options = {}) {

    const {
        userAgent,
        languages,
        language,
        screenWidth,
        screenHeight,
        deviceMemory,
        hardwareConcurrency,
        timezoneOffset,
        geolocation,
        timezoneOffsetName
    } = options;


    if (userAgent != null) {
        const appVersion = userAgent ? userAgent.replace("Mozilla/", "") : '5.0';
        const browserVendor = userAgent && userAgent.includes('Chrome') ? 'Google Inc.' : '';
        const platform = userAgent ? (userAgent.includes('Win') ? 'Win32' : 'Linux') : 'Win32';
        spoofProperty(navigator, 'userAgent', userAgent);
        spoofProperty(navigator, 'appCodeName', 'Mozilla');
        spoofProperty(navigator, 'appName', 'Netscape');
        spoofProperty(navigator, 'product', 'Gecko');
        spoofProperty(navigator, 'productSub', '20030107');
        spoofProperty(navigator, 'appVersion', appVersion);
        spoofProperty(navigator, 'vendor', browserVendor);
        spoofProperty(navigator, 'platform', platform);
        const brands = [];
        const browserMatch = userAgent.match(/(Chrome|Edg|Firefox)\/([0-9.]+)/i);

        if (browserMatch) {
            const browserName = browserMatch[1];
            const browserVersion = browserMatch[2].split('.')[0];

            brands.push({brand: 'Chromium', version: browserVersion});

            if (browserName.toLowerCase() === 'edg') {
                brands.push({brand: 'Microsoft Edge', version: browserVersion});
            } else if (browserName.toLowerCase() === 'chrome') {
                brands.push({brand: 'Google Chrome', version: browserVersion});
            }
            brands.push({brand: "Not)A;Brand", version: Math.floor(Math.random() * 10).toString()});
        }

        spoofProperty(navigator, 'userAgentData', {
            brands: brands,
            mobile: userAgent.includes('Mobile'),
            platform: userAgent.includes('Win') ? 'Windows' : (userAgent.includes('Mac') ? 'macOS' : 'Linux')
        });
    }
    if (language != null) {
        spoofProperty(navigator, 'language', language);
    }
    if (languages != null) {
        spoofProperty(navigator, 'languages', languages);
    }
    if (deviceMemory != null) {
        spoofProperty(navigator, 'deviceMemory', deviceMemory);
    }
    if (hardwareConcurrency != null) {
        spoofProperty(navigator, 'hardwareConcurrency', hardwareConcurrency);
    }
    if (screenWidth != null) {
        spoofProperty(screen, 'width', screenWidth);
        spoofProperty(screen, 'availWidth', screenWidth);
    }
    if (screenHeight != null) {
        spoofProperty(screen, 'height', screenHeight);
        spoofProperty(screen, 'availHeight', screenHeight);
    }

    // --- 伪造时区 ---
    if (timezoneOffset != null) {
        console.log("正在伪装时区...");

        // 获取原始的 getTimezoneOffset 方法
        const originalGetTimezoneOffset = Date.prototype.getTimezoneOffset;
        Date.prototype.getTimezoneOffset=function (){
            console.log("伪造 getTimezoneOffset 已被调用。");
            return timezoneOffset;
        }

        // 伪造 Intl API 中的时区
        const originalResolvedOptions = Intl.DateTimeFormat.prototype.resolvedOptions;
        Object.defineProperty(Intl.DateTimeFormat.prototype, 'resolvedOptions', {
            value: function() {
                // 确保原始方法在正确的上下文中执行
                const options = originalResolvedOptions.call(this);
                options.timeZone = timezoneOffsetName;
                console.log("伪造 Intl.DateTimeFormat.resolvedOptions 已被调用。");
                return options;
            },
            configurable: true,
        });

        console.log("时区伪装成功！");
    }

    // --- 伪造地理位置 ---
    if (geolocation && geolocation.latitude !== undefined && geolocation.longitude !== undefined) {
        Object.defineProperty(navigator, 'geolocation', {
            get: () => ({
                getCurrentPosition: (success, error, options) => {
                    const coords = {
                        latitude: geolocation.latitude,
                        longitude: geolocation.longitude,
                        accuracy: 20,
                        altitude: null,
                        altitudeAccuracy: null,
                        heading: null,
                        speed: null
                    };
                    const timestamp = Date.now();
                    success({coords, timestamp});
                },
                watchPosition: (success, error, options) => {
                    return 1;
                }
            })
        });
    }
}

/**
 * 伪造 Canvas 指纹，为每个会话创建一个独特的、可重现的指纹。
 *
 * @param {string} session_seed - 用于生成伪随机噪声的唯一种子。
 * @param {object} [options={}] - 包含 Canvas 伪造参数的对象。
 * @param {number} [options.noise_level=0.01] - 噪声强度，介于 0 和 1 之间。
 */
function spoofCanvasFingerprint(session_seed, options = {}) {

    const {
        noise_level = 0.01
    } = options;

    const originalGetImageData = CanvasRenderingContext2D.prototype.getImageData;

    // 负责在每次需要时创建一个新的、可重现的 PRNG 实例
    const createPrng = (seed) => {
        let x = 123456789;
        let y = 362436069;
        let z = 521288629;
        let w = 88675123;

        // 使用会话种子初始化生成器
        let s = seed.split('').reduce((sum, char) => sum + char.charCodeAt(0), 0);
        x += s;

        return function () {
            let t = (x ^ (x << 11));
            x = y;
            y = z;
            z = w;
            w = (w ^ (w >> 19)) ^ (t ^ (t >> 8));
            return (w >>> 0) / 4294967295;
        };
    };

    const noisify = function (canvas, context) {
        if (!context || !canvas.width || !canvas.height) {
            return;
        }

        // 关键修正：每次都重新创建一个 PRNG 实例，确保从同一起点开始
        const prng = createPrng(session_seed);

        const imageData = originalGetImageData.apply(context, [0, 0, canvas.width, canvas.height]);
        const data = imageData.data;

        for (let i = 0; i < data.length; i += 4) {
            const randomFactor = prng() * noise_level * 2 - noise_level;

            data[i] = Math.max(0, Math.min(255, data[i] + data[i] * randomFactor));
            data[i + 1] = Math.max(0, Math.min(255, data[i + 1] + data[i + 1] * randomFactor));
            data[i + 2] = Math.max(0, Math.min(255, data[i + 2] + data[i + 2] * randomFactor));
            data[i + 3] = Math.max(0, Math.min(255, data[i + 3] + data[i + 3] * randomFactor));
        }

        context.putImageData(imageData, 0, 0);
    };

    // --- Hooking the Canvas methods ---
    HTMLCanvasElement.prototype.toBlob = new Proxy(HTMLCanvasElement.prototype.toBlob, {
        apply(target, self, args) {
            noisify(self, self.getContext('2d'));
            return Reflect.apply(target, self, args);
        }
    });

    HTMLCanvasElement.prototype.toDataURL = new Proxy(HTMLCanvasElement.prototype.toDataURL, {
        apply(target, self, args) {
            noisify(self, self.getContext('2d'));
            return Reflect.apply(target, self, args);
        }
    });

    CanvasRenderingContext2D.prototype.getImageData = new Proxy(CanvasRenderingContext2D.prototype.getImageData, {
        apply(target, self, args) {
            noisify(self.canvas, self);
            return Reflect.apply(target, self, args);
        }
    });
}


/**
 * 钩住 AudioContext 以伪造音频指纹。
 * 在会话开始时生成一个基于种子的固定随机值，并将其用于所有音频指纹方法，
 * 确保指纹在会话内的一致性。
 *
 * @param {string} session_seed - 用于生成伪随机噪声的唯一种子。
 * @param {object} [options={}] - 一个包含音频伪造参数的对象。
 * @param {number} [options.noise_level=0.001] - 噪声强度，介于 0 和 1 之间。
 */
function spoofAudioFingerprint(session_seed, options = {}) {

    const {
        noise_level = 0.001
    } = options;

    // 核心修正：使用一个全局对象来缓存伪随机数，确保在同一会话中只生成一次
    const cache = {};
    const getCachedNoise = (key) => {
        if (!cache[key]) {
            // 使用简单的、可重现的 PRNG 来生成初始种子
            const prng = (function (seed) {
                let x = 123456789;
                let y = 362436069;
                let z = 521288629;
                let w = 88675123;
                let s = seed.split('').reduce((sum, char) => sum + char.charCodeAt(0), 0);
                x += s;
                return function () {
                    let t = (x ^ (x << 11));
                    x = y;
                    y = z;
                    z = w;
                    w = (w ^ (w >> 19)) ^ (t ^ (t >> 8));
                    return (w >>> 0) / 4294967295;
                };
            })(session_seed);
            cache[key] = prng() * noise_level * 2 - noise_level;
        }
        return cache[key];
    };

    const noisify = (data, key) => {
        // 使用缓存的噪声值
        const randomFactor = getCachedNoise(key);
        for (let i = 0; i < data.length; i++) {
            // 确保数据修改在 -1 到 1 的范围内
            data[i] = data[i] + data[i] * randomFactor;
        }
    };

    // --- 钩住 getChannelData 方法 ---
    const originalGetChannelData = AudioBuffer.prototype.getChannelData;
    Object.defineProperty(AudioBuffer.prototype, 'getChannelData', {
        value: function (...args) {
            const result = originalGetChannelData.apply(this, args);
            noisify(result, 'getChannelData');
            return result;
        },
        configurable: true,
        writable: true
    });

    // --- 钩住 createAnalyser 方法 ---
    const createAnalyserHook = (target) => {
        const originalCreateAnalyser = target.prototype.createAnalyser;
        Object.defineProperty(target.prototype, 'createAnalyser', {
            value: function (...args) {
                const analyserNode = originalCreateAnalyser.apply(this, args);

                const originalGetFloatFrequencyData = analyserNode.__proto__.getFloatFrequencyData;
                Object.defineProperty(analyserNode.__proto__, 'getFloatFrequencyData', {
                    value: function (...args) {
                        originalGetFloatFrequencyData.apply(this, args);
                        const result = args[0];
                        noisify(result, 'getFloatFrequencyData');
                    },
                    configurable: true,
                    writable: true
                });
                return analyserNode;
            },
            configurable: true,
            writable: true
        });
    };

    // 应用钩子到 AudioContext 和 OfflineAudioContext
    if (typeof AudioContext !== 'undefined') {
        createAnalyserHook(AudioContext);
    }
    if (typeof OfflineAudioContext !== 'undefined') {
        createAnalyserHook(OfflineAudioContext);
    }
}

/**
 * 钩住 DOMRect 和 DOMRectReadOnly 对象的属性，为它们添加微小的、基于种子的噪声，
 * 以对抗基于几何属性的浏览器指纹识别。确保指纹在会话内的一致性。
 *
 * @param {string} session_seed - 用于生成伪随机噪声的唯一字符串种子。
 * @param {object} [options={}] - 一个包含 DOMRect 伪造参数的对象。
 * @param {number} [options.domRectNoise=0.00000001] - DOMRect 属性的噪声强度。
 * @param {number} [options.domRectReadOnlyNoise=0.000001] - DOMRectReadOnly 属性的噪声强度。
 */
function spoofDOMRect(session_seed, options = {}) {
    // 使用一个简单的哈希函数将字符串种子转换为数字
    const stringToSeed = (str) => {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            const char = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash |= 0; // 转换为32位整数
        }
        return hash;
    };

    // 线性同余生成器 (LCG)
    let seed = stringToSeed(session_seed);
    const m = 0x80000000; // 2^31
    const a = 1103515245;
    const c = 12345;
    const lcg = () => (seed = (a * seed + c) % m) / m;

    const config = {
        "noise": {
            "DOMRect": options.domRectNoise || 0.00000001,
            "DOMRectReadOnly": options.domRectReadOnlyNoise || 0.000001
        },
        "metrics": {
            "DOMRect": ['x', 'y', "width", "height"],
            "DOMRectReadOnly": ["top", "right", "bottom", "left"]
        },
        "method": {
            "DOMRect": function (e) {
                try {
                    Object.defineProperty(DOMRect.prototype, e, {
                        "get": new Proxy(Object.getOwnPropertyDescriptor(DOMRect.prototype, e).get, {
                            apply(target, self, args) {
                                const result = Reflect.apply(target, self, args);
                                const rect_ran = lcg() - 0.5; // 使用固定的伪随机值
                                return result * (1 + rect_ran * config.noise.DOMRect);
                            }
                        })
                    });
                } catch (e) {
                    // console.error(e);
                }
            },
            "DOMRectReadOnly": function (e) {
                try {
                    Object.defineProperty(DOMRectReadOnly.prototype, e, {
                        "get": new Proxy(Object.getOwnPropertyDescriptor(DOMRectReadOnly.prototype, e).get, {
                            apply(target, self, args) {
                                const result = Reflect.apply(target, self, args);
                                const rect_ran = lcg() - 0.5; // 使用固定的伪随机值
                                return result * (1 + rect_ran * config.noise.DOMRectReadOnly);
                            }
                        })
                    });
                } catch (e) {
                    // console.error(e);
                }
            }
        }
    };

    config.metrics.DOMRect.forEach(prop => config.method.DOMRect(prop));
    config.metrics.DOMRectReadOnly.forEach(prop => config.method.DOMRectReadOnly(prop));
}


/**
 * 钩住 HTMLElement 对象的 offsetHeight 和 offsetWidth 属性，为它们添加微小的、
 * 基于种子的噪声，以对抗基于元素尺寸的浏览器指纹识别。
 *
 * 在会话开始时生成一个基于种子的固定随机值，并将其用于所有元素尺寸伪造，
 * 确保指纹在会话内的一致性。
 *
 * @param {string} session_seed - 用于生成伪随机噪声的唯一字符串种子。
 * @param {object} [options={}] - 一个包含尺寸伪造参数的对象。
 * @param {number} [options.noiseLevel=0.1] - 噪声强度，应为极小值。
 */
function spoofElementDimensions(session_seed, options = {}) {
    // 使用一个简单的哈希函数将字符串种子转换为数字
    const stringToSeed = (str) => {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            const char = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash |= 0; // 转换为32位整数
        }
        return hash;
    };

    // 线性同余生成器 (LCG)
    let seed = stringToSeed(session_seed);
    const m = 0x80000000; // 2^31
    const a = 1103515245;
    const c = 12345;
    const lcg = () => (seed = (a * seed + c) % m) / m;

    // 获取传入的噪声强度，如果没有则使用默认值
    const noiseLevel = options.noiseLevel || 0.1;

    // 封装劫持属性 getter 方法的函数
    function hookProperty(proto, propertyName) {
        const originalDescriptor = Object.getOwnPropertyDescriptor(proto, propertyName);
        if (!originalDescriptor || typeof originalDescriptor.get !== 'function') {
            return;
        }

        Object.defineProperty(proto, propertyName, {
            "get": new Proxy(originalDescriptor.get, {
                apply(target, self, args) {
                    try {
                        const originalValue = Reflect.apply(target, self, args);
                        // 使用固定的伪随机值生成微小噪声
                        const noise = (lcg() - 0.5) * noiseLevel;
                        return originalValue + noise;
                    } catch (e) {
                        // 如果出错，则返回原始值
                        console.error(`获取 ${propertyName} 时出错:`, e);
                        return Reflect.apply(target, self, args);
                    }
                }
            })
        });
    }

    // 钩住 offsetHeight 和 offsetWidth 属性
    hookProperty(HTMLElement.prototype, "offsetHeight");
    hookProperty(HTMLElement.prototype, "offsetWidth");
}

/**
 * Hooks the WebGL rendering context to spoof graphics card and driver information.
 * This is a highly effective method to counter WebGL-based fingerprinting.
 *
 * @param {string} session_seed - A unique string seed for ensuring consistent spoofing within a session.
 */
/**
 * 这个脚本用于 WebGL 指纹伪装。它通过代理（Proxy）浏览器原生的 WebGL 方法，
 * 在网页请求指纹相关信息时，返回伪装过的值，从而扰乱指纹识别。
 * * @param {string} sessionId - 用于保持指纹一致性的会话 ID。
 */
function spoofWebGLFingerprint(sessionId) {
    'use strict';

    /**
     * 根据会话 ID 生成一个简单的伪随机数生成器（PRNG）。
     * 这确保了在同一会话中，所有随机值都是可重现和一致的。
     */
    const stringToSeed = (str) => {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            const char = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash |= 0; // 转换为32位整数
        }
        return hash;
    };
    let seed = stringToSeed(sessionId);
    const m = 0x80000000;
    const a = 1103515245;
    const c = 12345;
    const lcg = () => (seed = (a * seed + c) % m) / m;

    const config = {
        "random": {
            // 使用基于种子的 PRNG 生成一个随机值。
            "value": function () {
                return lcg();
            },
            // 从数组中随机选择一个元素。
            "item": function (e) {
                const rand = e.length * config.random.value();
                return e[Math.floor(rand)];
            },
            // 从给定的一系列幂中生成一个随机的2的幂次方。
            "number": function (power) {
                const tmp = [];
                for (let i = 0; i < power.length; i++) {
                    tmp.push(Math.pow(2, power[i]));
                }
                return config.random.item(tmp);
            },
            // 生成一个随机的 Int32Array。
            "int": function (power) {
                const tmp = [];
                for (let i = 0; i < power.length; i++) {
                    const n = Math.pow(2, power[i]);
                    tmp.push(new Int32Array([n, n]));
                }
                return config.random.item(tmp);
            },
            // 生成一个随机的 Float32Array。
            "float": function (power) {
                const tmp = [];
                for (let i = 0; i < power.length; i++) {
                    const n = Math.pow(2, power[i]);
                    tmp.push(new Float32Array([1, n]));
                }
                return config.random.item(tmp);
            }
        },
        "spoof": {
            "webgl": {
                // 代理 bufferData 方法，为数据添加噪音。
                "buffer": function (target) {
                    const proto = target.prototype ? target.prototype : target.__proto__;
                    proto.bufferData = new Proxy(proto.bufferData, {
                        apply(target, self, args) {
                            const index = Math.floor(config.random.value() * args[1].length);
                            const noise = args[1][index] !== undefined ? 0.1 * config.random.value() * args[1][index] : 0;
                            // 为数据添加微小噪音。
                            args[1][index] = args[1][index] + noise;
                            return Reflect.apply(target, self, args);
                        }
                    });
                },
                // 代理 getParameter 方法，返回伪装过的值。
                "parameter": function (target) {
                    const proto = target.prototype ? target.prototype : target.__proto__;
                    proto.getParameter = new Proxy(proto.getParameter, {
                        apply(target, self, args) {
                            if (args[0] === 3415) return 0;
                            else if (args[0] === 3414) return 24;
                            else if (args[0] === 36348) return 30;
                            else if (args[0] === 7936) return "WebKit";
                            else if (args[0] === 37445) return "Google Inc.";
                            else if (args[0] === 7937) return "WebKit WebGL";
                            else if (args[0] === 3379) return config.random.number([14, 15]);
                            else if (args[0] === 36347) return config.random.number([12, 13]);
                            else if (args[0] === 34076) return config.random.number([14, 15]);
                            else if (args[0] === 34024) return config.random.number([14, 15]);
                            else if (args[0] === 3386) return config.random.int([13, 14, 15]);
                            else if (args[0] === 3413) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 3412) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 3411) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 3410) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 34047) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 34930) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 34921) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 35660) return config.random.number([1, 2, 3, 4]);
                            else if (args[0] === 35661) return config.random.number([4, 5, 6, 7, 8]);
                            else if (args[0] === 36349) return config.random.number([10, 11, 12, 13]);
                            else if (args[0] === 33902) return config.random.float([0, 10, 11, 12, 13]);
                            else if (args[0] === 33901) return config.random.float([0, 10, 11, 12, 13]);
                            else if (args[0] === 37446) return config.random.item(["Graphics", "HD Graphics", "Intel(R) HD Graphics"]);
                            else if (args[0] === 7938) return config.random.item(["WebGL 1.0", "WebGL 1.0 (OpenGL)", "WebGL 1.0 (OpenGL Chromium)"]);
                            else if (args[0] === 35724) return config.random.item(["WebGL", "WebGL GLSL", "WebGL GLSL ES", "WebGL GLSL ES (OpenGL Chromium"]);
                            return Reflect.apply(target, self, args);
                        }
                    });
                }
            }
        }
    };

    // 应用代理到 WebGLRenderingContext 和 WebGL2RenderingContext
    config.spoof.webgl.buffer(WebGLRenderingContext);
    config.spoof.webgl.buffer(WebGL2RenderingContext);
    config.spoof.webgl.parameter(WebGLRenderingContext);
    config.spoof.webgl.parameter(WebGL2RenderingContext);
}


/**
 * 对经纬度应用小的随机偏移，模拟位置变化。
 *
 * @param {number} latitude - 原始纬度。
 * @param {number} longitude - 原始经度。
 * @returns {{latitude: number, longitude: number}} 带有随机偏移的新经纬度。
 */
function spoofIpGeolocation(latitude) {
    // 设置一个随机偏移的范围（例如，0.1 度，大约 11 公里）
    const maxOffset = 0.05;
    const latOffset = (Math.random() - 0.5) * maxOffset * 2;
    return (parseFloat(latitude) + latOffset).toFixed(6);
}

async function start(sessionId, userAgent, languages, language, screenWidth, screenHeight, deviceMemory, hardwareConcurrency) {
let queryApi1="https://ipapi.co/json/";
    if (userAgent == null) {
        userAgent=navigator.userAgent
    }
    // 以 id 为键，值为包含 id、offset、location 的对象
    const GLOBAL_TIMEZONES_MAP = {
        "Pacific/Midway":   {id: "Pacific/Midway",   offset: -660, location: "美属萨摩亚"},
        "Pacific/Niue":     {id: "Pacific/Niue",     offset: -660, location: "纽埃"},
        "Pacific/Pago_Pago":{id: "Pacific/Pago_Pago",offset: -660, location: "美属萨摩亚"},
        "America/Adak":     {id: "America/Adak",     offset: -600, location: "阿留申群岛"},
        "Pacific/Honolulu": {id: "Pacific/Honolulu", offset: -600, location: "夏威夷"},
        "America/Anchorage":{id: "America/Anchorage",offset: -540, location: "阿拉斯加"},
        "America/Juneau":   {id: "America/Juneau",   offset: -540, location: "阿拉斯加"},
        "America/Los_Angeles":{id: "America/Los_Angeles",offset: -480, location: "洛杉矶"},
        "America/Denver":   {id: "America/Denver",   offset: -420, location: "丹佛"},
        "America/Phoenix":  {id: "America/Phoenix",  offset: -420, location: "亚利桑那州"},
        "America/Chicago":  {id: "America/Chicago",  offset: -360, location: "芝加哥"},
        "America/New_York": {id: "America/New_York", offset: -300, location: "纽约"},
        "America/Havana":   {id: "America/Havana",   offset: -300, location: "哈瓦那"},
        "America/Caracas":  {id: "America/Caracas",  offset: -240, location: "加拉加斯"},
        "America/Halifax":  {id: "America/Halifax",  offset: -240, location: "哈利法克斯"},
        "America/Argentina/Buenos_Aires":{id: "America/Argentina/Buenos_Aires",offset: -180, location: "布宜诺斯艾利斯"},
        "America/Sao_Paulo":{id: "America/Sao_Paulo",offset: -180, location: "圣保罗"},
        "Atlantic/Cape_Verde":{id: "Atlantic/Cape_Verde",offset: -60, location: "佛得角"},
        "Europe/London":    {id: "Europe/London",    offset: 0, location: "伦敦"},
        "Europe/Dublin":    {id: "Europe/Dublin",    offset: 0, location: "都柏林"},
        "Africa/Algiers":   {id: "Africa/Algiers",   offset: 60, location: "阿尔及尔"},
        "Europe/Paris":     {id: "Europe/Paris",     offset: 60, location: "巴黎"},
        "Europe/Berlin":    {id: "Europe/Berlin",    offset: 60, location: "柏林"},
        "Europe/Rome":      {id: "Europe/Rome",      offset: 60, location: "罗马"},
        "Europe/Athens":    {id: "Europe/Athens",    offset: 120, location: "雅典"},
        "Africa/Cairo":     {id: "Africa/Cairo",     offset: 120, location: "开罗"},
        "Asia/Beirut":      {id: "Asia/Beirut",      offset: 120, location: "贝鲁特"},
        "Europe/Moscow":    {id: "Europe/Moscow",    offset: 180, location: "莫斯科"},
        "Asia/Baghdad":     {id: "Asia/Baghdad",     offset: 180, location: "巴格达"},
        "Asia/Dubai":       {id: "Asia/Dubai",       offset: 240, location: "迪拜"},
        "Asia/Karachi":     {id: "Asia/Karachi",     offset: 300, location: "卡拉奇"},
        "Asia/Kolkata":     {id: "Asia/Kolkata",     offset: 330, location: "加尔各答"},
        "Asia/Dhaka":       {id: "Asia/Dhaka",       offset: 360, location: "达卡"},
        "Asia/Bangkok":     {id: "Asia/Bangkok",     offset: 420, location: "曼谷"},
        "Asia/Shanghai":    {id: "Asia/Shanghai",    offset: 480, location: "上海/北京"},
        "Asia/Hong_Kong":   {id: "Asia/Hong_Kong",   offset: 480, location: "香港"},
        "Asia/Singapore":   {id: "Asia/Singapore",   offset: 480, location: "新加坡"},
        "Asia/Tokyo":       {id: "Asia/Tokyo",       offset: 540, location: "东京"},
        "Asia/Seoul":       {id: "Asia/Seoul",       offset: 540, location: "首尔"},
        "Australia/Sydney": {id: "Australia/Sydney", offset: 600, location: "悉尼"},
        "Pacific/Guam":     {id: "Pacific/Guam",     offset: 600, location: "关岛"},
        "Pacific/Auckland": {id: "Pacific/Auckland", offset: 720, location: "奥克兰"},
        "Pacific/Fiji":     {id: "Pacific/Fiji",     offset: 720, location: "斐济"}
    };
    let json;
    try {
        let str2= sessionStorage["_this_ip_info_001_x"];
        if (str2 == null) {
            str2=await (await fetch(queryApi1)).text();
            sessionStorage["_this_ip_info_001_x"]=str2;
        }
        json = JSON.parse(str2);
    } catch (e) {
        json = {
            "timezone": "Asia/Seoul",
            "latitude": (37.5658),
            "longitude": (126.978)
        }
        console.error("Error in start function https://ipapi.co/json/:", e);
    }
    console.log("json",json)
    json.latitude = spoofIpGeolocation(json.latitude);
    json.longitude = spoofIpGeolocation(json.longitude);

    if (sessionId == null) {
        sessionId=json.ip;
    }
    if (sessionId == null) {
        sessionId = userAgent;
    }
    try {
        spoofWebGLFingerprint(sessionId);
    } catch (e) {
        console.error("Error in start function spoofWebGLFingerprint:", e);
    }
    try {
        spoofElementDimensions(sessionId, {noiseLevel: 0.1});
    } catch (e) {
        console.error("Error in start function spoofElementDimensions:", e);
    }
    try {
        spoofDOMRect(sessionId, {domRectNoise: 0.00000001, domRectReadOnlyNoise: 0.000001});
    } catch (e) {
        console.error("Error in start function spoofDOMRect:", e);
    }
    try {
        spoofCanvasFingerprint(sessionId, {noise_level: 0.01});
    } catch (e) {
        console.error("Error in start function spoofCanvasFingerprint:", e);
    }
    try {
        spoofBrowserFingerprint({
            userAgent: userAgent,
            languages: languages,
            language: language,
            screenWidth: screenWidth,
            screenHeight: screenHeight,
            deviceMemory: deviceMemory,
            hardwareConcurrency: hardwareConcurrency,
            timezoneOffset: Number(GLOBAL_TIMEZONES_MAP[json.timezone]["offset"]),
            timezoneOffsetName: (json.timezone),
            geolocation: {
                latitude: json.latitude,
                longitude: json.longitude
            }
        });
    } catch (e) {
        console.error("Error in start function spoofBrowserFingerprint:", e);
    }
    try {
        spoofAudioFingerprint(sessionId, {noise_level: 0.01});
    } catch (e) {
        console.error("Error in start function spoofAudioFingerprint:", e);
    }
}

console.log("start ...... ")
start(
    "x000003",
    null,
    ["zh", "zh-CN", "en", "en-US", "en-GB"],
    "zh",
    1920,
    1080,
    4,
    8
)


