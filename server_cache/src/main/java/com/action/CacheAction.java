package com.action;

import gzb.frame.annotation.*;
import gzb.frame.netty.action.cache.SystemCacheAction;
import gzb.tools.Config;
import gzb.tools.cache.GzbCache;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;

@Controller
@RequestMapping("cache")
public class CacheAction extends SystemCacheAction {

    public static final byte[] BYTES = "Hello, World!".getBytes(Config.encoding);

    @EventLoop
    @RequestMapping("hello")
    public byte[] hello(){
        return BYTES;
    }
}
