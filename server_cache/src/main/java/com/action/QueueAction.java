package com.action;

import gzb.frame.annotation.*;
import gzb.frame.netty.action.cache.SystemQueueAction;
import gzb.tools.Tools;
import gzb.tools.cache.GzbCache;
import gzb.tools.cache.GzbQueue;
import gzb.tools.cache.GzbQueueImpl;
import gzb.tools.cache.entity.Entity;
import gzb.tools.http.HTTP_V3;
import gzb.tools.log.Log;

import java.io.UnsupportedEncodingException;

/// api: /queue/produce  /queue/consume  /queue/confirm
@Controller
@RequestMapping("queue")
public class QueueAction extends SystemQueueAction {


}
