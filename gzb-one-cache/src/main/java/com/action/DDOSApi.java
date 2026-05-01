package com.action;

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.RequestMapping;
import com.system.api.SystemTestApi;

@Controller
@RequestMapping("/")
public class DDOSApi extends SystemTestApi {
    /// SystemTestApi -> http api: http://ip:port
    ///  hello
    ///  json
    ///  json/a?gzbUserId=1001&gzbUserAcc=acc&gzbUserPwd=pwd&gzbUserState=1&gzbUserNike=nike
    ///  json/b?gzbUserId=1001&gzbUserAcc=acc&gzbUserPwd=pwd&gzbUserState=1&gzbUserNike=nike
    ///  json/a/array?num=2&gzbUserId=1001&gzbUserAcc=acc&gzbUserPwd=pwd&gzbUserState=1&gzbUserNike=nike
    ///  json/b/array?num=1&gzbUserId=1001&gzbUserAcc=acc&gzbUserPwd=pwd&gzbUserState=1&gzbUserNike=nike

}
