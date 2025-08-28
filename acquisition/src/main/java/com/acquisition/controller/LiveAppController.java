package com.acquisition.controller;

import com.acquisition.entity.LiveApp;
import com.acquisition.dao.LiveAppDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/liveApp", header = "content-type:application/json;charset=UTF-8")
public class LiveAppController{
    @Resource(value = "com.acquisition.dao.impl.LiveAppDaoImpl")
    LiveAppDao liveAppDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LiveApp的参数 
     * /system/v1.0.0/liveApp/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, LiveApp liveApp) throws Exception {
        if (liveApp == null || liveApp.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        LiveApp liveApp1 = liveAppDao.find(liveApp);
        if (liveApp1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(liveApp1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LiveApp的参数 
     * /system/v1.0.0/liveApp/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,LiveApp liveApp, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return liveAppDao.queryPage(liveApp, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 LiveApp的参数 
     * /system/v1.0.0/liveApp/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = liveAppDao.getDataBase().toSelect("live_app", field, symbol, value, montage, sortField, sortType);
        return liveAppDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LiveApp的参数 
     * /system/v1.0.0/liveApp/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, LiveApp liveApp) throws Exception {
        if (liveApp == null || liveApp.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (liveAppDao.save(liveApp) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 LiveApp的参数 
     * /system/v1.0.0/liveApp/update?liveAppId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, LiveApp liveApp) throws Exception {
        if (liveApp == null || liveApp.toString().equals("{}") || liveApp.getLiveAppId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (liveAppDao.update(liveApp) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 liveAppId 
     * /system/v1.0.0/liveApp/delete?liveAppId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, LiveApp liveApp) throws Exception {
        if (liveApp == null || liveApp.toString().equals("{}") || liveApp.getLiveAppId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (liveAppDao.delete(liveApp) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 liveAppId 
     * /system/v1.0.0/liveApp/deleteAll?liveAppId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] liveAppId) throws Exception {
        if (liveAppId == null || liveAppId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        LiveApp acquisition = new LiveApp();
        for (Long aLong : liveAppId) {
            liveAppDao.delete(acquisition.setLiveAppId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
