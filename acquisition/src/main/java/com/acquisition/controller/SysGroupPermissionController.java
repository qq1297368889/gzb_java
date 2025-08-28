package com.acquisition.controller;

import com.acquisition.entity.SysGroupPermission;
import com.acquisition.dao.SysGroupPermissionDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/sysGroupPermission", header = "content-type:application/json;charset=UTF-8")
public class SysGroupPermissionController{
    @Resource(value = "com.acquisition.dao.impl.SysGroupPermissionDaoImpl")
    SysGroupPermissionDao sysGroupPermissionDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数 
     * /system/v1.0.0/sysGroupPermission/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysGroupPermission sysGroupPermission1 = sysGroupPermissionDao.find(sysGroupPermission);
        if (sysGroupPermission1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(sysGroupPermission1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数 
     * /system/v1.0.0/sysGroupPermission/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,SysGroupPermission sysGroupPermission, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return sysGroupPermissionDao.queryPage(sysGroupPermission, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupPermission的参数 
     * /system/v1.0.0/sysGroupPermission/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysGroupPermissionDao.getDataBase().toSelect("sys_group_permission", field, symbol, value, montage, sortField, sortType);
        return sysGroupPermissionDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysGroupPermission的参数 
     * /system/v1.0.0/sysGroupPermission/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysGroupPermissionDao.save(sysGroupPermission) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysGroupPermission的参数 
     * /system/v1.0.0/sysGroupPermission/update?sysGroupPermissionId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}") || sysGroupPermission.getSysGroupPermissionId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysGroupPermissionDao.update(sysGroupPermission) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysGroupPermissionId 
     * /system/v1.0.0/sysGroupPermission/delete?sysGroupPermissionId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, SysGroupPermission sysGroupPermission) throws Exception {
        if (sysGroupPermission == null || sysGroupPermission.toString().equals("{}") || sysGroupPermission.getSysGroupPermissionId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysGroupPermissionDao.delete(sysGroupPermission) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysGroupPermissionId 
     * /system/v1.0.0/sysGroupPermission/deleteAll?sysGroupPermissionId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] sysGroupPermissionId) throws Exception {
        if (sysGroupPermissionId == null || sysGroupPermissionId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        SysGroupPermission acquisition = new SysGroupPermission();
        for (Long aLong : sysGroupPermissionId) {
            sysGroupPermissionDao.delete(acquisition.setSysGroupPermissionId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
