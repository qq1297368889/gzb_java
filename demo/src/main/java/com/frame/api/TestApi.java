package com.frame.api;

import com.frame.dao.SysFileDao;
import com.frame.dao.SysPermissionDao;
import com.frame.dao.SysUsersDao;
import com.frame.entity.SysFile;
import com.frame.entity.SysPermission;
import com.frame.entity.SysUsers;
import gzb.entity.FileUploadEntity;
import gzb.exception.GzbException0;
import gzb.frame.DDOS;
import gzb.frame.annotation.*;
import gzb.frame.factory.GzbOneInterface;
import gzb.tools.Config;
import gzb.tools.DateTime;
import gzb.tools.Tools;
import gzb.tools.http.HTTPV2;
import gzb.tools.http.HTTP_V3;
import gzb.tools.json.GzbJson;
import gzb.tools.json.GzbJsonImpl;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/// 功能测试  依赖注入web解析  文件上传  dao功能   事务处理
@Controller
@RequestMapping("test/api")
public class TestApi {
    public static void main(String[] args) throws Exception {
        HTTP_V3 httpV3 = new HTTP_V3();
        List<File> list1 = new ArrayList<>();
        List<File> list2 = new ArrayList<>();
        list1.add(new File("E:\\codes_20220814\\logo\\1.jpg"));
        list2.add(new File("E:\\codes_20220814\\logo\\2.jpg"));
        list2.add(new File("E:\\codes_20220814\\logo\\3.jpg"));
        String postData = "b=1&c=1&d=1&e=1.11&f=1.12&g=true" +
                "&b1=1&c1=1&d1=1&e1=1.13&f1=1.14&g1=true" +
                "&b2=1&b2=1&c2=1&c2=1&d2=1&d2=1&e2=1.15&e2=1.16&f2=1.17&f2=1.18&g2=true&g2=false" +
                "&b3=1&b3=1&c3=1&c3=1&d3=1&d3=1&e3=1.19&e3=1.20&f3=1.21&f3=1.22&g3=true&g3=false" +
                "&a=1&a2=1&a2=1&sysFileId=1&sysFileId=2&sysUsersId=1";
        Map<String, List<File>> files = new HashMap<>();
        files.put("file", list1);
        files.put("files", list2);
        //统一删除
        httpV3.request("http://127.0.0.1:2080/test/api/del1?t=1", "DELETE", "", null, null, 10000L);
        System.out.println("del 1 " + httpV3.asString());

        //检查数量是不是0
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=0", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());

        //事务 失败
        httpV3.request("http://127.0.0.1:2080/test/api/post1", "POST", "", null, null, 10000L);
        System.out.println("post 1 预期内错误 " + httpV3.asString());

        //检查数量是不是0
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=0", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());

        //事务 成功
        httpV3.request("http://127.0.0.1:2080/test/api/post2", "POST", "", null, null, 10000L);
        System.out.println("post 2 " + httpV3.asString());

        //检查数量是不是1
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=1", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());

        //模拟事务 失败
        httpV3.request("http://127.0.0.1:2080/test/api/post3", "POST", "", null, null, 10000L);
        System.out.println("post 3 预期内错误 " + httpV3.asString());

        //检查数量是不是1
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=1", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());

        //模拟事务 成功
        httpV3.request("http://127.0.0.1:2080/test/api/post4", "POST", "", null, null, 10000L);
        System.out.println("post 4 " + httpV3.asString());

        //检查数量是不是2
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=2", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());

        //综合测试
        httpV3.request("http://127.0.0.1:2080/test/api/post5", "POST", postData, null, files, 10000L);
        System.out.println("post 5 " + httpV3.asString());

//传参测试 时间对象+ 时间数组对象
        httpV3.request("http://127.0.0.1:2080/test/api/get3?" +
                        "localDateTime=2020-01-01%2001:01:01" +
                        "&localDateTimes=2020-01-01%2001:01:01" +
                        "&timestamp=2020-01-01%2001:01:01" +
                        "&timestamps=2020-01-01%2001:01:01" +
                        "&dateTime=2020-01-01%2001:01:01" +
                        "&dateTimes=2020-01-01%2001:01:01" +
                        "&date=2020-01-01%2001:01:01" +
                        "&dates=2020-01-01%2001:01:01"
                , "GET", "", null, null, 10000L);
        System.out.println("get 3 " + httpV3.asString());

//传参测试 复杂对象数组 内部元素是时间 key等于复杂对象的类变量名称
        httpV3.request("http://127.0.0.1:2080/test/api/get2?" +
                        "localDateTime=2020-01-01%2001:01:01" +
                        "&timestamp=2020-01-01%2001:01:01" +
                        "&dateTime=2020-01-01%2001:01:01" +
                        "&date=2020-01-01%2001:01:01" +
                        "&localDateTime=2020-01-01%2001:01:01" +
                        "&timestamp=2020-01-01%2001:01:01" +
                        "&dateTime=2020-01-01%2001:01:01" +
                        "&date=2020-01-01%2001:01:01"
                , "GET", "", null, null, 10000L);
        System.out.println("get 2 " + httpV3.asString());
//检查类变量 注入 是否正常
        httpV3.request("http://127.0.0.1:2080/test/api/get1", "GET", "", null, null, 10000L);
        System.out.println("get 1 " + httpV3.asString());


        //统一删除
        httpV3.request("http://127.0.0.1:2080/test/api/del1?t=1", "DELETE", "", null, null, 10000L);
        System.out.println("del 1 " + httpV3.asString());

        //检查数量是不是0
        httpV3.request("http://127.0.0.1:2080/test/api/find1?num=0", "GET", "", null, null, 10000L);
        System.out.println("find 1 " + httpV3.asString());


        DateTime dateTime = new DateTime();
        TestEntity testEntity = new TestEntity();
        testEntity.setDate(dateTime.toDate());
        testEntity.setDateTime(dateTime);
        testEntity.setTimestamp(dateTime.toTimestamp());
        testEntity.setLocalDateTime(dateTime.toLocalDateTime());
        // {"localDateTime":"2026-03-20 13:32:44","timestamp":"2026-03-20 13:32:44","date":"2026-03-20 13:32:44","dateTime":"2026-03-20 13:32:44"}
        String json = Tools.toJson(testEntity);
        Map<String, String> map0 = new HashMap<>();
        map0.put("content-type", "application/json");
        httpV3.request("http://127.0.0.1:2080/test/api/post6"
                , "POST", "[" + json + "," + json + "]", map0, null, 10000L);
        System.out.println("post 6 " + httpV3.asString());

    }


    @Resource
    SysPermissionDao sysPermissionDao;

    //http://127.0.0.1:2080/test/api/get3?localDateTime=2020-01-01%2001:01:01&localDateTimes=2020-01-01%2001:01:01&timestamp=2020-01-01%2001:01:01&timestamps=2020-01-01%2001:01:01&dateTime=2020-01-01%2001:01:01&dateTimes=2020-01-01%2001:01:01
    //普通参数和数组
    @GetMapping("get3")
    public Object get3(Log log, GzbJson gzbJson
            , LocalDateTime localDateTime, LocalDateTime[] localDateTimes
            , Timestamp timestamp, Timestamp[] timestamps
            , Date date, Date[] dates
            , DateTime dateTime, DateTime[] dateTimes) throws Exception {
        if (localDateTime == null) {
            return gzbJson.fail("localDateTime == null");
        }
        if (localDateTimes == null || localDateTimes.length != 1) {
            return gzbJson.fail("localDateTimes == null");
        }
        if (timestamp == null) {
            return gzbJson.fail("timestamp == null");
        }
        if (timestamps == null || timestamps.length != 1) {
            return gzbJson.fail("timestamps == null");
        }
        if (date == null) {
            return gzbJson.fail("date == null");
        }
        if (dates == null || dates.length != 1) {
            return gzbJson.fail("dates == null");
        }
        if (dateTime == null) {
            return gzbJson.fail("dateTime == null");
        }
        if (dateTimes == null || dateTimes.length != 1) {
            return gzbJson.fail("dateTimes == null");
        }
        return gzbJson.success("OK");
    }

    //数组对象
    @GetMapping("get2")
    public Object get2(TestEntity[] testEntitys, Log log, GzbJson gzbJson) throws Exception {
        if (testEntitys == null || testEntitys.length != 2) {
            return gzbJson.fail("testEntitys == null");
        }
        for (TestEntity testEntity : testEntitys) {
            if (testEntity.getDate() == null) {
                return gzbJson.fail("testEntity.getDate() == null");
            }
            if (testEntity.getDateTime() == null) {
                return gzbJson.fail("testEntity.getDateTime() == null");
            }
            if (testEntity.getTimestamp() == null) {
                return gzbJson.fail("testEntity.getTimestamp() == null");
            }
            if (testEntity.getLocalDateTime() == null) {
                return gzbJson.fail("testEntity.getLocalDateTime() == null");
            }
        }

        return gzbJson.success("OK");
    }

    //检查类变量
    @GetMapping("get1")
    public Object get1(Log log, GzbJson gzbJson) throws Exception {
        if (sysPermissionDao == null) {
            return gzbJson.fail("类变量未注入 sysPermissionDao");
        }

        return gzbJson.success("OK");
    }

    //事务失败
    @Transaction(simulate = false)
    @PostMapping("post1")
    public Object post1(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
        sysUsersDao.save(new SysUsers().setSysUsersAcc("acc_001x"));
        throw new GzbException0("抛出错误");
    }

    //事务成功
    @Transaction(simulate = false)
    @PostMapping("post2")
    public Object post2(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
        sysUsersDao.save(new SysUsers().setSysUsersAcc("acc_001x"));
        return gzbJson.success("OK");
    }

    //事务失败 模拟
    @Transaction(simulate = true)
    @PostMapping("post3")
    public Object post3(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
        sysUsersDao.save(new SysUsers().setSysUsersAcc("acc_001x"));
        throw new GzbException0("抛出错误");
    }

    //事务成功 模拟
    @Transaction(simulate = true)
    @PostMapping("post4")
    public Object post4(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
        sysUsersDao.save(new SysUsers().setSysUsersAcc("acc_001x"));
        return gzbJson.success("OK");
    }

    @DeleteMapping("del1")
    public Object del1(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson) throws Exception {
        sysUsersDao.delete(new SysUsers().setSysUsersAcc("acc_001x"));
        return gzbJson.success("OK");
    }

    @GetMapping("find1")
    public Object find1(SysUsersDao sysUsersDao, Log log, GzbJson gzbJson, int num) throws Exception {
        List<SysUsers> list = sysUsersDao.query(new SysUsers().setSysUsersAcc("acc_001x"));
        if (list.size() != num) {
            return gzbJson.success("acc_001x 数量不是预期的 " + num + "而是：" + list.size());
        }
        return gzbJson.success("OK");
    }

    //综合测试
    @PostMapping("post5")
    public Object post5(
            Integer b, Long c, Short d, Float e, Double f, Boolean g
            , int b1, long c1, short d1, float e1, double f1, boolean g1,
            Integer[] b2, Long[] c2, Short[] d2, Float[] e2, Double[] f2, Boolean[] g2
            , int[] b3, long[] c3, short[] d3, float[] e3, double[] f3, boolean[] g3
            , FileUploadEntity file, FileUploadEntity[] files
            , String a, String[] a2, SysUsers sysUsers, SysFile[] sysFiles
            //对象注入
            , GzbJson gzbJson, Log log, SysUsersDao sysUsersDao, SysFileDao sysFileDao) throws Exception {
        if (sysFileDao == null) {
            return gzbJson.fail("sysFileDao 传值错误，@service 对象注入失败");
        }
        if (sysUsersDao == null) {
            return gzbJson.fail("sysUsersDao 传值错误，@service 对象注入失败");
        }
        if (log == null) {
            return gzbJson.fail("log 传值错误，框架内置对象注入失败");
        }
        if (sysUsers == null || sysUsers.getSysUsersId() != 1L) {
            return gzbJson.fail("sysUsers对象 传值错误");
        }
        if (sysFiles == null || sysFiles.length != 2 || sysFiles[0].getSysFileId() != 1L || sysFiles[1].getSysFileId() != 2L) {
            return gzbJson.fail("sysFile对象数组 传值错误");
        }
        if (file == null || !file.getFile().exists()) {
            return gzbJson.fail("file 传值错误，请确认为 文件");
        }
        if (files == null || files.length != 2 || !files[0].getFile().exists() || !files[1].getFile().exists()) {
            return gzbJson.fail("file 传值错误，请确认为 [文件,文件]");
        }
        if (a == null || !a.toString().equals("1")) {
            return gzbJson.fail("a 传值错误，请确认为 \"1\"");
        }

        if (a2 == null || a2.length != 2 || !a2[0].toString().equals("1") || !a2[1].toString().equals("1")) {
            return gzbJson.fail("a2 传值错误，请确认为 [\"1\",\"1\"]");
        }

        //自动装箱类型 数组
        if (b2 == null || b2.length != 2 || !b2[0].toString().equals("1") || !b2[1].toString().equals("1")) {
            return gzbJson.fail("b2 传值错误，请确认为 [1,1]");
        }
        if (c2 == null || c2.length != 2 || !c2[0].toString().equals("1") || !c2[1].toString().equals("1")) {
            return gzbJson.fail("c2 传值错误，请确认为 [1,1]");
        }
        if (d2 == null || d2.length != 2 || !d2[0].toString().equals("1") || !d2[1].toString().equals("1")) {
            return gzbJson.fail("d2 传值错误，请确认为 [1,1]");
        }
        if (e2 == null || e2.length != 2 || !Tools.doubleTo2(e2[0]).toString().equals("1.15") || !Tools.doubleTo2(e2[1]).toString().equals("1.16")) {
            return gzbJson.fail("e2 传值错误，请确认为 [1.15,1.16]");
        }
        if (f2 == null || f2.length != 2 || !Tools.doubleTo2(f2[0]).toString().equals("1.17") || !Tools.doubleTo2(f2[1]).toString().equals("1.18")) {
            return gzbJson.fail("f2 传值错误，请确认为 [1.17,1.18]");
        }
        if (g2 == null || g2.length != 2 || !g2[0].toString().equals("true") || !g2[1].toString().equals("false")) {
            return gzbJson.fail("g2 传值错误，请确认为 [true,false]");
        }
//基本类型 数组
        if (b3 == null || b3.length != 2 || b3[0] != 1 || b3[1] != 1) {
            return gzbJson.fail("b2 传值错误，请确认为 [1,1]");
        }
        if (c3 == null || c3.length != 2 || c3[0] != 1 || c3[1] != 1) {
            return gzbJson.fail("c2 传值错误，请确认为 [1,1]");
        }
        if (d3 == null || d3.length != 2 || d3[0] != 1 || d3[1] != 1) {
            return gzbJson.fail("d3 传值错误，请确认为 [1,1]");
        }
        if (e3 == null || e3.length != 2 || !Tools.doubleTo2(e3[0]).toString().equals("1.19") || !Tools.doubleTo2(e3[1]).toString().equals("1.20")) {
            return gzbJson.fail("e3 传值错误，请确认为 [1.19,1.20]");
        }
        if (f3 == null || f3.length != 2 || !Tools.doubleTo2(f3[0]).toString().equals("1.21") || !Tools.doubleTo2(f3[1]).toString().equals("1.22")) {
            return gzbJson.fail("f3 传值错误，请确认为 [1.21,1.22]");
        }
        if (g3 == null || g3.length != 2 || !g3[0] || g3[1]) {
            return gzbJson.fail("g3 传值错误，请确认为 [true,false]");
        }


        //自动装箱类型
        if (b == null || !b.toString().equals("1")) {
            return gzbJson.fail("b 传值错误，请确认为 1");
        }
        if (c == null || !c.toString().equals("1")) {
            return gzbJson.fail("c 传值错误，请确认为 1");
        }
        if (d == null || !d.toString().equals("1")) {
            return gzbJson.fail("d 传值错误，请确认为 1");
        }
        if (e == null || !Tools.doubleTo2(e).equals("1.11")) {
            return gzbJson.fail("e 传值错误，请确认为 1.11");
        }
        if (f == null || !Tools.doubleTo2(f).equals("1.12")) {
            return gzbJson.fail("f 传值错误，请确认为 1.12");
        }
        if (g == null || !g.toString().equals("true")) {
            return gzbJson.fail("h 传值错误，请确认为 true");
        }
//基本类型
        if (b1 != 1) {
            return gzbJson.fail("b1 传值错误，请确认为 1");
        }
        if (c1 != 1) {
            return gzbJson.fail("c1 传值错误，请确认为 1");
        }
        if (d1 != 1) {
            return gzbJson.fail("d1 传值错误，请确认为 1");
        }
        if (!Tools.doubleTo2(e1).equals("1.13")) {
            return gzbJson.fail("e1 传值错误，请确认为 1.13");
        }
        if (!Tools.doubleTo2(f1).equals("1.14")) {
            return gzbJson.fail("f1 传值错误，请确认为 1.14");
        }
        if (!g1) {
            return gzbJson.fail("g1 传值错误，请确认为 true");
        }
        sysUsers.setSysUsersId(null);//重置ID 否则框架不会自动填入
        sysUsers.setSysUsersAcc(Tools.getRandomString(12));
        if (sysUsersDao.save(sysUsers) < 0) {
            return gzbJson.fail("数据库插入失败");
        }
        //必然失败，不出错就是错  因为ID重复了
        try {
            sysUsersDao.save(sysUsers);
            return gzbJson.fail("重复ID插入成功，数据库操作异常");
        } catch (Exception e0) {
            log.e("预期中 的错误", e0);
        }
        Semaphore semaphore = new Semaphore(0);
        sysUsersDao.saveAsync(sysUsers, new Runnable() {
            @Override
            public void run() {
                log.d("执行失败了，触发回调", sysUsers);
                //这里有完整上下文信息 因为这是开发者可以自由引用当前可访问区域的内容 可以进行补偿操作  这不是异步思维 而是补偿操作
                semaphore.release();
            }
        }, null);
        if (!semaphore.tryAcquire(2000L, TimeUnit.MILLISECONDS)) {
            // 如果 2000 毫秒内未能获取到许可（回调未触发）
            return gzbJson.fail("异步插入回调超时");
        }
        if (sysUsersDao.find(sysUsers) == null) {
            return gzbJson.fail("数据库查询失败");
        }
        //acc已被修改
        sysUsers.setSysUsersAcc(Tools.getRandomString(10));
        if (sysUsersDao.update(sysUsers) < 0) {
            return gzbJson.fail("数据库修改失败");
        }
        if (sysUsersDao.find(sysUsers) == null) {
            return gzbJson.fail("数据库更改后查询失败");
        }
        if (sysUsersDao.delete(sysUsers) < 0) {
            return gzbJson.fail("数据库删除失败");
        }
        //开启真实事务
        sysUsersDao.getDataBase().openTransaction(false);
        sysUsersDao.save(sysUsers);
        sysUsersDao.getDataBase().rollback();
        sysUsersDao.getDataBase().endTransaction();
        if (sysUsersDao.find(sysUsers) != null) {
            return gzbJson.fail("数据库事务回滚 失败");
        }

        //开启模拟事务 只保证一起成功或失败 本质是收集 统一提交
        sysUsersDao.getDataBase().openTransaction(true);
        sysUsersDao.save(sysUsers);
        sysUsersDao.getDataBase().rollback();
        sysUsersDao.getDataBase().endTransaction();
        if (sysUsersDao.find(sysUsers) != null) {
            return gzbJson.fail("数据库事务回滚 失败 模拟");
        }

        //开启真实事务
        sysUsersDao.getDataBase().openTransaction(false);
        sysUsersDao.save(sysUsers);
        sysUsersDao.getDataBase().commit();
        sysUsersDao.getDataBase().endTransaction();
        if (sysUsersDao.find(sysUsers) == null) {
            return gzbJson.fail("数据库事务提交 失败");
        }
        sysUsersDao.delete(sysUsers);
        //开启模拟事务 只保证一起成功或失败 本质是收集 统一提交
        sysUsersDao.getDataBase().openTransaction(true);
        sysUsersDao.save(sysUsers);
        sysUsersDao.getDataBase().commit();
        sysUsersDao.getDataBase().endTransaction();
        if (sysUsersDao.find(sysUsers) == null) {
            return gzbJson.fail("数据库事务提交 失败 模拟");
        }
        sysUsersDao.delete(sysUsers);

        /// 测试结束
        return gzbJson.success("OK");
    }


    @PostMapping("post6")
    public Object post6(TestEntity[] testEntitys, Log log, GzbJson gzbJson) throws Exception {
        if (testEntitys == null || testEntitys.length != 2) {
            return gzbJson.fail("testEntitys == null");
        }
        for (TestEntity testEntity : testEntitys) {
            if (testEntity.getDate() == null) {
                return gzbJson.fail("testEntity.getDate() == null");
            }
            if (testEntity.getDateTime() == null) {
                return gzbJson.fail("testEntity.getDateTime() == null");
            }
            if (testEntity.getTimestamp() == null) {
                return gzbJson.fail("testEntity.getTimestamp() == null");
            }
            if (testEntity.getLocalDateTime() == null) {
                return gzbJson.fail("testEntity.getLocalDateTime() == null");
            }
        }

        return gzbJson.success("OK");
    }
}
