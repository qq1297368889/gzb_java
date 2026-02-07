package com.frame.controller;

import com.frame.entity.SysRoleGroup;
import com.frame.dao.SysRoleGroupDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
//@CrossDomain(allowCredentials = false)
@RequestMapping(value = "/system/v1.0.0/sysRoleGroup")
public class SysRoleGroupController{
    @Resource(value = "com.frame.dao.impl.SysRoleGroupDaoImpl")
    SysRoleGroupDao sysRoleGroupDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysRoleGroup的参数 
     * /system/v1.0.0/sysRoleGroup/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(GzbJson result, SysRoleGroup sysRoleGroup) throws Exception {
        if (sysRoleGroup == null || sysRoleGroup.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysRoleGroup sysRoleGroup1 = sysRoleGroupDao.find(sysRoleGroup);
        if (sysRoleGroup1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result.success("查询成功",sysRoleGroup1);
    }

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysRoleGroup的参数 
     * /system/v1.0.0/sysRoleGroup/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(GzbJson result,SysRoleGroup sysRoleGroup, String sortField, String sortType, Integer page, Integer size) throws Exception {
        if(sysRoleGroup == null){
           sysRoleGroup = new SysRoleGroup();
        }
        return sysRoleGroupDao.queryPage(sysRoleGroup, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 查询,根据id修改其他参数 请提供 SysRoleGroup的参数 
     * /system/v1.0.0/sysRoleGroup/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysRoleGroupDao.getDataBase().toSelect("sys_role_group", field, symbol, value, montage, null, null);
        return sysRoleGroupDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysRoleGroup的参数 
     * /system/v1.0.0/sysRoleGroup/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(GzbJson result, SysRoleGroup sysRoleGroup) throws Exception {
        if (sysRoleGroup == null || sysRoleGroup.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysRoleGroupDao.save(sysRoleGroup) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysRoleGroup的参数 
     * /system/v1.0.0/sysRoleGroup/update?sysRoleGroupId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(GzbJson result, SysRoleGroup sysRoleGroup) throws Exception {
        if (sysRoleGroup == null || sysRoleGroup.toString().equals("{}") || sysRoleGroup.getSysRoleGroupId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysRoleGroupDao.update(sysRoleGroup) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysRoleGroupId 
     * /system/v1.0.0/sysRoleGroup/delete?sysRoleGroupId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(GzbJson result, SysRoleGroup sysRoleGroup) throws Exception {
        if (sysRoleGroup == null || sysRoleGroup.toString().equals("{}") || sysRoleGroup.getSysRoleGroupId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysRoleGroupDao.delete(sysRoleGroup) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysRoleGroupId 
     * /system/v1.0.0/sysRoleGroup/deleteAll?sysRoleGroupId=
     * */
    @DecoratorOpen
    @DeleteMapping("deleteAll")
    public Object deleteAll(GzbJson result, java.lang.Long[] sysRoleGroupId) throws Exception {
        if (sysRoleGroupId == null || sysRoleGroupId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysRoleGroup frame = new SysRoleGroup();
        for (java.lang.Long _id : sysRoleGroupId) {
            sysRoleGroupDao.delete(frame.setSysRoleGroupId(_id));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }
};