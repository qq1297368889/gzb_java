/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.frame.api;

import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;

@Controller//标注为控制器
@RequestMapping("/test")//类级别请求前缀
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")}) //设置协议头
public class TestAction {
    @Resource("com.frame.dao.impl.SysUsersDaoImpl")//不指定实现类 默认匹配第一个
    SysUsersDao sysUsersDao;

    //请求路径  /test/test0
    @GetMapping("/test0")
    public Object test0() throws Exception {
        return "ok";
    }
    //请求路径  /test/test1?name=xxxx
    @GetMapping("/test1")
    public Object test1(String name) throws Exception {
        return name;
    }
    //请求路径  /test/test3?sysUsersId=100
    @GetMapping("/test3")
    public Object test3(SysUsers sysUsers) throws Exception {
        return sysUsers;
    }
    //请求路径  /test/test4?sysUsersId=100
    @GetMapping("/test4")
    public Object test4(SysUsers sysUsers) throws Exception {
        sysUsers=sysUsersDao.find(sysUsers); //mybatis 没对应方法 我懒得写了 我吃点亏 我这个查询是动态生成的
        return sysUsers;//springmvc 似乎没有内置对象 就这样吧 让spring占点便宜
    }
    @GetMapping("/test5")
    public Object test5(SysUsers sysUsers,GzbJson gzbJson) throws Exception {
        return gzbJson.success("查询成功",sysUsersDao.find(sysUsers));
    }
}
