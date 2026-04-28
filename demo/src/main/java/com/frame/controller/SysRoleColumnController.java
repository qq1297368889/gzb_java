package com.frame.controller;

import com.frame.entity.SysRoleColumn;
import com.frame.dao.SysRoleColumnDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
//@CrossDomain(allowCredentials = false)
@RequestMapping(value = "/system/v1.0.0/sysRoleColumn")
public class SysRoleColumnController{
    @Resource(value = "com.frame.dao.impl.SysRoleColumnDaoImpl")
    SysRoleColumnDao sysRoleColumnDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysRoleColumn的参数 
     * /system/v1.0.0/sysRoleColumn/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(GzbJson result, SysRoleColumn sysRoleColumn) throws Exception {
        if (sysRoleColumn == null || sysRoleColumn.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        SysRoleColumn sysRoleColumn1 = sysRoleColumnDao.find(sysRoleColumn);
        if (sysRoleColumn1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result.success("查询成功",sysRoleColumn1);
    }

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 SysRoleColumn的参数 
     * /system/v1.0.0/sysRoleColumn/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(GzbJson result,SysRoleColumn sysRoleColumn, String sortField, String sortType, Integer page, Integer size) throws Exception {
        if(sysRoleColumn == null){
           sysRoleColumn = new SysRoleColumn();
        }
        return sysRoleColumnDao.queryPage(sysRoleColumn, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 查询,根据id修改其他参数 请提供 SysRoleColumn的参数 
     * /system/v1.0.0/sysRoleColumn/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = sysRoleColumnDao.getDataBase().toSelect("sys_role_column", field, symbol, value, montage, null, null);
        return sysRoleColumnDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysRoleColumn的参数 
     * /system/v1.0.0/sysRoleColumn/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(GzbJson result, SysRoleColumn sysRoleColumn) throws Exception {
        if (sysRoleColumn == null || sysRoleColumn.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (sysRoleColumnDao.save(sysRoleColumn) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysRoleColumn的参数 
     * /system/v1.0.0/sysRoleColumn/update?sysRoleColumnId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(GzbJson result, SysRoleColumn sysRoleColumn) throws Exception {
        if (sysRoleColumn == null || sysRoleColumn.toString().equals("{}") || sysRoleColumn.getSysRoleColumnId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (sysRoleColumnDao.update(sysRoleColumn) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysRoleColumnId 
     * /system/v1.0.0/sysRoleColumn/delete?sysRoleColumnId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(GzbJson result, SysRoleColumn sysRoleColumn) throws Exception {
        if (sysRoleColumn == null || sysRoleColumn.toString().equals("{}") || sysRoleColumn.getSysRoleColumnId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysRoleColumnDao.delete(sysRoleColumn) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysRoleColumnId 
     * /system/v1.0.0/sysRoleColumn/deleteAll?sysRoleColumnId=
     * */
    @DecoratorOpen
    @DeleteMapping("deleteAll")
    public Object deleteAll(GzbJson result, java.lang.Long[] sysRoleColumnId) throws Exception {
        if (sysRoleColumnId == null || sysRoleColumnId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        SysRoleColumn frame = new SysRoleColumn();
        for (java.lang.Long _id : sysRoleColumnId) {
            sysRoleColumnDao.delete(frame.setSysRoleColumnId(_id));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }
};