package com.action;

import gzb.frame.annotation.*;
import com.system.api.SystemCacheAction;

/// api: cache/ping cache/get cache/set cache/get/all cache/del
@Controller
@RequestMapping("cache")
public class CacheAction extends SystemCacheAction {

}
