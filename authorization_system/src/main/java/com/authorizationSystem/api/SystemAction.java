package com.authorizationSystem.api;

import com.frame.controller.system.BaseSystemAction;
import gzb.frame.annotation.Controller;
import gzb.frame.annotation.RequestMapping;
//继承 内部实现类 实现 权限管理用户管理 等内置功能 前提必须数据库中包含内置表 sys_xxx
@Controller
@RequestMapping(value = "/system/v1.0.0/", header = "content-type:application/json;charset=UTF-8")
public class SystemAction extends BaseSystemAction {

}
