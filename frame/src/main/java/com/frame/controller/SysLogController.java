package com.frame.controller;

import com.frame.entity.SysLog;
import com.frame.dao.SysLogDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/sysLog", header = "content-type:application/json;charset=UTF-8")
public class SysLogController{
    @Resource(value = "com.frame.dao.impl.SysLogDaoImpl")
    SysLogDao sysLogDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数 
     * /system/v1.0.0/sysLog/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysLog sysLog1 = sysLogDao.find(sysLog);
        if (sysLog1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysLog1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数 
     * /system/v1.0.0/sysLog/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,SysLog sysLog, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysLogDao.queryPage(sysLog, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysLog的参数 
     * /system/v1.0.0/sysLog/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysLogDao.getDataBase().toSelect("sys_log", field, symbol, value, montage, sortField, sortType);
        return sysLogDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysLog的参数 
     * /system/v1.0.0/sysLog/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysLogDao.save(sysLog) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysLog的参数 
     * /system/v1.0.0/sysLog/update?sysLogId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}") || sysLog.getSysLogId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysLogDao.update(sysLog) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysLogId 
     * /system/v1.0.0/sysLog/delete?sysLogId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, SysLog sysLog) throws Exception {
        if (sysLog == null || sysLog.toString().equals("{}") || sysLog.getSysLogId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysLogDao.delete(sysLog) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysLogId 
     * /system/v1.0.0/sysLog/deleteAll?sysLogId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] sysLogId) throws Exception {
        if (sysLogId == null || sysLogId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        SysLog frame = new SysLog();
        for (Long aLong : sysLogId) {
            sysLogDao.delete(frame.setSysLogId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
