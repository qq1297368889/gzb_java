package com.action;

import gzb.frame.annotation.*;
import gzb.frame.netty.action.cache.SystemCacheAction;
import gzb.tools.Config;
import gzb.tools.cache.GzbCache;
import gzb.tools.log.Log;
import gzb.tools.thread.GzbThreadLocal;

/// api: cache/ping cache/get cache/set cache/get/all cache/del
@Controller
@RequestMapping("cache")
public class CacheAction extends SystemCacheAction {

}
