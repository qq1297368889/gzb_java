package com.acquisition.controller;

import com.acquisition.entity.OnLineUserCopy1;
import com.acquisition.dao.OnLineUserCopy1Dao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/onLineUserCopy1", header = "content-type:application/json;charset=UTF-8")
public class OnLineUserCopy1Controller{
    @Resource(value = "com.acquisition.dao.impl.OnLineUserCopy1DaoImpl")
    OnLineUserCopy1Dao onLineUserCopy1Dao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUserCopy1的参数 
     * /system/v1.0.0/onLineUserCopy1/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, OnLineUserCopy1 onLineUserCopy1) throws Exception {
        if (onLineUserCopy1 == null || onLineUserCopy1.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        OnLineUserCopy1 onLineUserCopy11 = onLineUserCopy1Dao.find(onLineUserCopy1);
        if (onLineUserCopy11 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(onLineUserCopy11.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUserCopy1的参数 
     * /system/v1.0.0/onLineUserCopy1/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,OnLineUserCopy1 onLineUserCopy1, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return onLineUserCopy1Dao.queryPage(onLineUserCopy1, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 OnLineUserCopy1的参数 
     * /system/v1.0.0/onLineUserCopy1/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = onLineUserCopy1Dao.getDataBase().toSelect("on_line_user_copy1", field, symbol, value, montage, sortField, sortType);
        return onLineUserCopy1Dao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 OnLineUserCopy1的参数 
     * /system/v1.0.0/onLineUserCopy1/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, OnLineUserCopy1 onLineUserCopy1) throws Exception {
        if (onLineUserCopy1 == null || onLineUserCopy1.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (onLineUserCopy1Dao.save(onLineUserCopy1) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 OnLineUserCopy1的参数 
     * /system/v1.0.0/onLineUserCopy1/update?onLineUserId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, OnLineUserCopy1 onLineUserCopy1) throws Exception {
        if (onLineUserCopy1 == null || onLineUserCopy1.toString().equals("{}") || onLineUserCopy1.getOnLineUserId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (onLineUserCopy1Dao.update(onLineUserCopy1) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 onLineUserId 
     * /system/v1.0.0/onLineUserCopy1/delete?onLineUserId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, OnLineUserCopy1 onLineUserCopy1) throws Exception {
        if (onLineUserCopy1 == null || onLineUserCopy1.toString().equals("{}") || onLineUserCopy1.getOnLineUserId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (onLineUserCopy1Dao.delete(onLineUserCopy1) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 onLineUserId 
     * /system/v1.0.0/onLineUserCopy1/deleteAll?onLineUserId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] onLineUserId) throws Exception {
        if (onLineUserId == null || onLineUserId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        OnLineUserCopy1 acquisition = new OnLineUserCopy1();
        for (Long aLong : onLineUserId) {
            onLineUserCopy1Dao.delete(acquisition.setOnLineUserId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
