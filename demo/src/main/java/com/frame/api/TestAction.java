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
import com.frame.entity.SysFile;
import com.frame.entity.SysGroup;
import com.frame.entity.SysUsers;
import gzb.frame.DDOS;
import gzb.frame.annotation.*;
import gzb.frame.netty.entity.FileUploadEntity;
import gzb.tools.Tools;
import gzb.tools.http.HTTP_V3;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller//标注为控制器
@RequestMapping("/test")//类级别请求前缀
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")}) //设置协议头
public class TestAction {
    public static void main(String[] args) throws Exception {
        List<File> list1 = new ArrayList<>();
        list1.add(new File("E:\\BaiduNetdiskDownload\\002\\DNF模拟器最新使用教学\\1.txt"));
        list1.add(new File("E:\\BaiduNetdiskDownload\\002\\DNF模拟器最新使用教学\\2.txt"));
        List<File> list2 = new ArrayList<>();
        list2.add(new File("E:\\BaiduNetdiskDownload\\002\\DNF模拟器最新使用教学\\3.txt"));
        Map<String, List<File>> files = new HashMap<>();
        files.put("file", list2);
        files.put("files", list1);
String url="http://127.0.0.1:2082/test/api/test";
String data="sysFileId=100" +
        "&sysGroupId=1000&sysGroupId=100" +
        "&name=100" +
        "&names=1000&names=100" +
        "&age1=100" +
        "&ages1=1000&ages1=100" +
        "&num1=100" +
        "&nums1=1000&nums1=100" +
        "&isOk1=true" +
        "&isOks1=false&isOks1=true" +
        "&price1=100" +
        "&prices1=1000&prices1=100" +
        "&age2=100" +
        "&ages2=1000&ages2=100" +
        "&num2=100" +
        "&nums2=1000&nums2=100" +
        "&isOk2=true" +
        "&isOks2=false&isOks2=true" +
        "&price2=100" +
        "&prices2=1000&prices2=100";
        HTTP_V3 http = new HTTP_V3();
        http.request(url, "POST",data, null, files, 1000L);
        System.out.println(http.asString());
        url="http://127.0.0.1:8081/test/api/test2";
        http.request(url, "POST",data, null, files, 1000L);
        System.out.println(http.asString());


        data="sysUsersId=1";

        url="http://127.0.0.1:2082/test/api/test/find";
        DDOS.start("压力测试 gzb one",1,url,"GET",data,null,null,10000 * 5 ,"sysUsersId".getBytes());
//http://127.0.0.1:8081/test/api/test/find?sysUsersId=1
        url="http://127.0.0.1:8081/test/api/test/find";
        DDOS.start("压力测试 spring",1,url,"GET",data,null,null,10000 * 5 ,"sysUsersId".getBytes());

        //DDOS.start("压力测试 gzb one",6,url,"POST",data,null,null,10000 * 100 ,"true".getBytes());


    }

    @Resource("com.frame.dao.impl.SysUsersDaoImpl")//不指定实现类 默认匹配第一个
    SysUsersDao sysUsersDao;

    @GetMapping("/api/test/json01")
    public Object api_test2(String name,GzbJson json) throws Exception {
        return json.success(name);
    }
    @GetMapping("/api/test/find")
    public Object api_test2(SysUsers sysUsers) throws Exception {
        return sysUsersDao.find(sysUsers);
    }
    @PostMapping("/api/test2")
    public Object api_test2(
            SysFile sysFile,
            String name, String[] names,
            int age1, int[] ages1,
            long num1, long[] nums1,
            boolean isOk1, boolean[] isOks1
            , double price1, double[] prices1,
            Integer age2, Integer[] ages2,
            Long num2, Long[] nums2,
            Boolean isOk2, Boolean[] isOks2,
            Double price2, Double[] prices2
    ) throws Exception {
        Map<String, Boolean> res = new HashMap<>();
        res.put("sysUsersDao", sysUsersDao != null);
        res.put("sysFile", sysFile != null && sysFile.getSysFileId() != null && sysFile.getSysFileId() == 100);
        res.put("name", name != null && name.equals("100"));
        res.put("age1", age1 == 100);
        res.put("num1", num1 == 100);
        res.put("isOk1", isOk1);
        res.put("price1", Tools.doubleTo2(price1).equals("100.00"));
        res.put("age2", age2 != null && age2 == 100);
        res.put("num2", num2 != null && num2 == 100);
        res.put("isOk2", isOk2 != null && isOk2);
        res.put("price2", price2 != null && Tools.doubleTo2(price2).equals("100.00"));
        res.put("names", names != null && names.length == 2 && names[1].equals("100"));
        res.put("ages1", ages1 != null && ages1.length == 2 && ages1[1] == 100);
        res.put("nums1", nums1 != null && nums1.length == 2 && nums1[1] == 100);
        res.put("isOks1", isOks1 != null && isOks1.length == 2 && isOks1[1]);
        res.put("prices1", prices1 != null && prices1.length == 2 && Tools.doubleTo2(prices1[1]).equals("100.00"));
        res.put("ages2", ages2 != null && ages2.length == 2 && ages2[1] == 100);
        res.put("nums2", nums2 != null && nums2.length == 2 && nums2[1] == 100);
        res.put("isOks2", isOks2 != null && isOks2.length == 2 && isOks2[1]);
        res.put("prices2", prices2 != null && prices2.length == 2 && Tools.doubleTo2(prices2[1]).equals("100.00"));

        return res.size();
    }
    @PostMapping("/api/test")
    public Object api_test(
            Log log,SysUsersDao sysUsersDao0,
            //固定 数字长度等于2 否则验证不通过  syFileId作为参数   boolean值为 true 其他值都为100
            SysFile sysFile, SysGroup[] sysGroups,
            String name, String[] names,
            FileUploadEntity file, FileUploadEntity[] files,
            int age1, int[] ages1,
            long num1, long[] nums1,
            boolean isOk1, boolean[] isOks1
            , double price1, double[] prices1,
            Integer age2, Integer[] ages2,
            Long num2, Long[] nums2,
            Boolean isOk2, Boolean[] isOks2,
            Double price2, Double[] prices2
    ) throws Exception {
        SysUsers sysUsers0 = new SysUsers().setSysUsersAcc(Tools.getRandomString(12));
        Map<String, Boolean> res = new HashMap<>();
        res.put("sysUsersDao", sysUsersDao != null);
        res.put("sysUsersDao0", sysUsersDao0 != null);
        res.put("log", log != null);
        res.put("添加结果", sysUsersDao.save(sysUsers0) > -1);
        res.put("添加验证", sysUsersDao.query(sysUsers0).size() > 0);
        res.put("修改结果", sysUsersDao.update(sysUsers0.setSysUsersPwd("1002")) > -1);
        res.put("修改验证", sysUsersDao.query(new SysUsers().setSysUsersPwd("1002").setSysUsersId(sysUsers0.getSysUsersId())).size() > 0);
        res.put("删除结果", sysUsersDao.delete(sysUsers0) > -1);
        res.put("删除验证", sysUsersDao.query(new SysUsers().setSysUsersId(sysUsers0.getSysUsersId())).size() == 0);
        res.put("sysFile", sysFile != null && sysFile.getSysFileId() != null && sysFile.getSysFileId() == 100);
        res.put("file", file != null && file.getFile().exists());//文件会被自动清理
        res.put("name", name != null && name.equals("100"));
        res.put("age1", age1 == 100);
        res.put("num1", num1 == 100);
        res.put("isOk1", isOk1);
        res.put("price1", Tools.doubleTo2(price1).equals("100.00"));
        res.put("age2", age2 != null && age2 == 100);
        res.put("num2", num2 != null && num2 == 100);
        res.put("isOk2", isOk2 != null && isOk2);
        res.put("price2", price2 != null && Tools.doubleTo2(price2).equals("100.00"));
        res.put("sysGroups", sysGroups != null && sysGroups.length == 2 && sysGroups[1].getSysGroupId() != null && sysGroups[1].getSysGroupId() == 100);
        res.put("names", names != null && names.length == 2 && names[1].equals("100"));
        res.put("files", files != null && files.length == 2 && files[1].getFile().exists());
        res.put("ages1", ages1 != null && ages1.length == 2 && ages1[1] == 100);
        res.put("nums1", nums1 != null && nums1.length == 2 && nums1[1] == 100);
        res.put("isOks1", isOks1 != null && isOks1.length == 2 && isOks1[1]);
        res.put("prices1", prices1 != null && prices1.length == 2 && Tools.doubleTo2(prices1[1]).equals("100.00"));
        res.put("ages2", ages2 != null && ages2.length == 2 && ages2[1] == 100);
        res.put("nums2", nums2 != null && nums2.length == 2 && nums2[1] == 100);
        res.put("isOks2", isOks2 != null && isOks2.length == 2 && isOks2[1]);
        res.put("prices2", prices2 != null && prices2.length == 2 && Tools.doubleTo2(prices2[1]).equals("100.00"));

        return res;
    }

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
        sysUsers = sysUsersDao.find(sysUsers); //mybatis 没对应方法 我懒得写了 我吃点亏 我这个查询是动态生成的
        return sysUsers;//springmvc 似乎没有内置对象 就这样吧 让spring占点便宜
    }

    @GetMapping("/test5")
    public Object test5(SysUsers sysUsers, GzbJson gzbJson) throws Exception {
        return gzbJson.success("查询成功", sysUsersDao.find(sysUsers));
    }
}
