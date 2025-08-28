package com.acquisition.controller;

import com.acquisition.entity.OnLineUser;
import com.acquisition.dao.OnLineUserDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/onLineUser", header = "content-type:application/json;charset=UTF-8")
public class OnLineUserController{
    @Resource(value = "com.acquisition.dao.impl.OnLineUserDaoImpl")
    OnLineUserDao onLineUserDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUser的参数 
     * /system/v1.0.0/onLineUser/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, OnLineUser onLineUser) throws Exception {
        if (onLineUser == null || onLineUser.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        OnLineUser onLineUser1 = onLineUserDao.find(onLineUser);
        if (onLineUser1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(onLineUser1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUser的参数 
     * /system/v1.0.0/onLineUser/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,OnLineUser onLineUser, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return onLineUserDao.queryPage(onLineUser, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 OnLineUser的参数 
     * /system/v1.0.0/onLineUser/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = onLineUserDao.getDataBase().toSelect("on_line_user", field, symbol, value, montage, sortField, sortType);
        return onLineUserDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUser的参数 
     * /system/v1.0.0/onLineUser/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, OnLineUser onLineUser) throws Exception {
        if (onLineUser == null || onLineUser.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (onLineUserDao.save(onLineUser) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 OnLineUser的参数 
     * /system/v1.0.0/onLineUser/update?onLineUserId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, OnLineUser onLineUser) throws Exception {
        if (onLineUser == null || onLineUser.toString().equals("{}") || onLineUser.getOnLineUserId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (onLineUserDao.update(onLineUser) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 onLineUserId 
     * /system/v1.0.0/onLineUser/delete?onLineUserId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, OnLineUser onLineUser) throws Exception {
        if (onLineUser == null || onLineUser.toString().equals("{}") || onLineUser.getOnLineUserId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (onLineUserDao.delete(onLineUser) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 onLineUserId 
     * /system/v1.0.0/onLineUser/deleteAll?onLineUserId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] onLineUserId) throws Exception {
        if (onLineUserId == null || onLineUserId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        OnLineUser acquisition = new OnLineUser();
        for (Long aLong : onLineUserId) {
            onLineUserDao.delete(acquisition.setOnLineUserId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
