package com.acquisition.controller;

import com.acquisition.entity.LogInfo;
import com.acquisition.dao.LogInfoDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/logInfo", header = "content-type:application/json;charset=UTF-8")
public class LogInfoController{
    @Resource(value = "com.acquisition.dao.impl.LogInfoDaoImpl")
    LogInfoDao logInfoDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LogInfo的参数 
     * /system/v1.0.0/logInfo/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, LogInfo logInfo) throws Exception {
        if (logInfo == null || logInfo.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        LogInfo logInfo1 = logInfoDao.find(logInfo);
        if (logInfo1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(logInfo1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LogInfo的参数 
     * /system/v1.0.0/logInfo/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,LogInfo logInfo, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return logInfoDao.queryPage(logInfo, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 LogInfo的参数 
     * /system/v1.0.0/logInfo/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = logInfoDao.getDataBase().toSelect("log_info", field, symbol, value, montage, sortField, sortType);
        return logInfoDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 LogInfo的参数 
     * /system/v1.0.0/logInfo/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, LogInfo logInfo) throws Exception {
        if (logInfo == null || logInfo.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (logInfoDao.save(logInfo) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 LogInfo的参数 
     * /system/v1.0.0/logInfo/update?logInfoId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, LogInfo logInfo) throws Exception {
        if (logInfo == null || logInfo.toString().equals("{}") || logInfo.getLogInfoId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (logInfoDao.update(logInfo) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 logInfoId 
     * /system/v1.0.0/logInfo/delete?logInfoId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, LogInfo logInfo) throws Exception {
        if (logInfo == null || logInfo.toString().equals("{}") || logInfo.getLogInfoId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (logInfoDao.delete(logInfo) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 logInfoId 
     * /system/v1.0.0/logInfo/deleteAll?logInfoId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] logInfoId) throws Exception {
        if (logInfoId == null || logInfoId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        LogInfo acquisition = new LogInfo();
        for (Long aLong : logInfoId) {
            logInfoDao.delete(acquisition.setLogInfoId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
