package com.authorizationSystem.api;

import com.frame.system.BaseSystemAction;
import gzb.frame.annotation.Controller;
import gzb.frame.annotation.GetMapping;
import gzb.frame.annotation.RequestMapping;
import gzb.tools.session.Session;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/system/v1.0.0/", header = "content-type:application/json;charset=UTF-8")
public class SystemAction extends BaseSystemAction {

}
