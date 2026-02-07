package com.frame.controller;

import com.frame.entity.SysOptionRequest;
import com.frame.dao.SysOptionRequestDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
//@CrossDomain(allowCredentials = false)
@RequestMapping(value = "/system/v1.0.0/sysOptionRequest")
public class SysOptionRequestController{
    @Resource(value = "com.frame.dao.impl.SysOptionRequestDaoImpl")
    SysOptionRequestDao sysOptionRequestDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysOptionRequest的参数 
     * /system/v1.0.0/sysOptionRequest/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(GzbJson result, SysOptionRequest sysOptionRequest) throws Exception {
        if (sysOptionRequest == null || sysOptionRequest.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysOptionRequest sysOptionRequest1 = sysOptionRequestDao.find(sysOptionRequest);
        if (sysOptionRequest1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result.success("查询成功",sysOptionRequest1);
    }

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysOptionRequest的参数 
     * /system/v1.0.0/sysOptionRequest/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(GzbJson result,SysOptionRequest sysOptionRequest, String sortField, String sortType, Integer page, Integer size) throws Exception {
        if(sysOptionRequest == null){
           sysOptionRequest = new SysOptionRequest();
        }
        return sysOptionRequestDao.queryPage(sysOptionRequest, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 查询,根据id修改其他参数 请提供 SysOptionRequest的参数 
     * /system/v1.0.0/sysOptionRequest/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysOptionRequestDao.getDataBase().toSelect("sys_option_request", field, symbol, value, montage, null, null);
        return sysOptionRequestDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysOptionRequest的参数 
     * /system/v1.0.0/sysOptionRequest/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(GzbJson result, SysOptionRequest sysOptionRequest) throws Exception {
        if (sysOptionRequest == null || sysOptionRequest.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysOptionRequestDao.save(sysOptionRequest) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysOptionRequest的参数 
     * /system/v1.0.0/sysOptionRequest/update?sysOptionRequestId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(GzbJson result, SysOptionRequest sysOptionRequest) throws Exception {
        if (sysOptionRequest == null || sysOptionRequest.toString().equals("{}") || sysOptionRequest.getSysOptionRequestId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysOptionRequestDao.update(sysOptionRequest) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysOptionRequestId 
     * /system/v1.0.0/sysOptionRequest/delete?sysOptionRequestId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(GzbJson result, SysOptionRequest sysOptionRequest) throws Exception {
        if (sysOptionRequest == null || sysOptionRequest.toString().equals("{}") || sysOptionRequest.getSysOptionRequestId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysOptionRequestDao.delete(sysOptionRequest) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysOptionRequestId 
     * /system/v1.0.0/sysOptionRequest/deleteAll?sysOptionRequestId=
     * */
    @DecoratorOpen
    @DeleteMapping("deleteAll")
    public Object deleteAll(GzbJson result, java.lang.Long[] sysOptionRequestId) throws Exception {
        if (sysOptionRequestId == null || sysOptionRequestId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysOptionRequest frame = new SysOptionRequest();
        for (java.lang.Long _id : sysOptionRequestId) {
            sysOptionRequestDao.delete(frame.setSysOptionRequestId(_id));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }
};