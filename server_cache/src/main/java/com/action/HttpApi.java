package com.action;

import com.entity.SysFile;
import gzb.frame.annotation.*;
import gzb.tools.Config;
import gzb.tools.json.GzbJson;

@Controller
@RequestMapping("http")
public class HttpApi {
    public static final byte[] BYTES = "Hello, World!".getBytes(Config.encoding);

    /// @EventLoop 只有在不涉及任何 阻塞的情况下可用

    /// http://127.0.0.1:6100/http/get0
    @EventLoop
    @GetMapping("get0")
    public byte[] get0() {
        return BYTES;
    }

    /// http/get1?message=msg
    @EventLoop
    @GetMapping("get1")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public String get1(String message) {
        return "{\"code\":\"1\",\"time\":\"1769527173990\",\"message\":\"" + message + "\"}";
    }

    /// http/get2?message=msg
    @EventLoop
    @GetMapping("get2")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public String get1(String message, GzbJson gzbJson) {
        if (message == null) {
            return gzbJson.fail("message 为必填项");
        }
        return gzbJson.success(message);
    }

    /// http/get3?sysFileId=1&sysFilePath=sysFilePath&sysFileMd5=sysFileMd5&sysFileTime=2001-01-01 01:01:01&sysFileType=image
    @EventLoop
    @GetMapping("get3")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public String get3(SysFile sysFile, GzbJson gzbJson) {
        if (sysFile == null) {
            return gzbJson.fail("sysFile == null");
        }
        return gzbJson.success("ok", sysFile);
    }

    /// http/get4?sysFileId=1&sysFilePath=sysFilePath&sysFileMd5=sysFileMd5&sysFileTime=2001-01-01 01:01:01&sysFileType=image
    @EventLoop
    @GetMapping("get4")
    @Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
    public String get4(Long sysFileId, String sysFilePath, String sysFileMd5, String sysFileTime, String sysFileType, GzbJson gzbJson) {
        return gzbJson.success("ok", new Object[]{sysFileId, sysFilePath, sysFileMd5, sysFileTime, sysFileType});
    }
}
