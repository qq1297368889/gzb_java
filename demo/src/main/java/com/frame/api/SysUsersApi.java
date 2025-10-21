package com.frame.api;

import com.frame.dao.SysUsersDao;
import com.frame.entity.SysUsers;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.Tools;
import gzb.tools.json.GzbJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
@RequestMapping(value = "/system/v1.0.0/sysUsers")
public class SysUsersApi {

    public static Map<String, Object> mapArrayToNoArray(Map<String, List<Object>> map) {
        Map<String, Object> map0 = new HashMap<>();
        for (Map.Entry<String, List<Object>> stringListEntry : map.entrySet()) {
            if (stringListEntry.getValue() == null || stringListEntry.getValue().toString().isEmpty()) {
                continue;
            }
            map0.put(stringListEntry.getKey(), stringListEntry.getValue().get(0));
        }
        return map0;
    }

    @Resource
    SysUsersDao sysUsersDao;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/find?参数同下,只是仅接受查询结果为1条的情况
     */
    @DecoratorOpen
    @GetMapping(value = "/find")
    public Object find(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers0.getSysUsersType()!=4) {
            sysUsers.setSysUsersSup(sysUsers0.getSysUsersId());
        }
        if (sysUsers.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }
        return result.success("查询成功", sysUsersDao.find(sysUsers));
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/list?参数同下,只是是根据实体类查询
     */
    @DecoratorOpen
    @GetMapping("/list")
    public Object list(GzbJson result, SysUsers sysUsers0, String sortField, String sortType, Integer page, Integer size, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers0.getSysUsersType()!=4) {
            sysUsers.setSysUsersSup(sysUsers0.getSysUsersId());
        }
        return result.paging(sysUsersDao.query(sysUsers, sortField, sortType, page, size), page, size);
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     */
    @DecoratorOpen
    @GetMapping("/query")
    public Object query(SysUsersDao sysUsersDao, SysUsers sysUsers0,GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        if (sysUsers0.getSysUsersType()!=4) {
            field= Tools.appendArray(field,new String[]{},"sys_users_sup");
            symbol= Tools.appendArray(symbol,new String[]{},"1");
            value= Tools.appendArray(value,new String[]{},sysUsers0.getSysUsersId().toString());
            montage= Tools.appendArray(montage,new String[]{},"1");
        }
        SqlTemplate sqlTemplate = sysUsersDao.getDataBase().toSelect("sys_users", field, symbol, value, montage, sortField, sortType);
        List<SysUsers> listGzbMap1 = sysUsersDao.query(sqlTemplate.getSql(), sqlTemplate.getObjects(),null,null,0,0,-1);
        return result.paging(listGzbMap1, page, limit);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/save
     */
    @DecoratorOpen
    @PostMapping("/save")
    public Object save(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        sysUsers.setSysUsersSup(sysUsers0.getSysUsersId());
        if (sysUsersDao.save(sysUsers) < 0) {
            return result.fail("update 失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 SysUsers的参数
     * /system/v1.0.0/sysUsers/update?sysUsersId=
     */
    @DecoratorOpen
    @PostMapping("/update")
    public Object update(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("update 输入参数错误");
        }
        sysUsers.setSysUsersSup(sysUsers0.getSysUsersId());
        if (sysUsersDao.update(sysUsers) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 sysUsersId
     * /system/v1.0.0/sysUsers/delete?sysUsersId=
     */
    @DecoratorOpen
    @DeleteMapping("/delete")
    public Object delete(GzbJson result, SysUsers sysUsers0, Map<String, List<Object>> req) throws Exception {
        Map<String, Object> re2 = mapArrayToNoArray(req);
        SysUsers sysUsers = new SysUsers(re2);
        if (sysUsers.toString().equals("{}") || sysUsers.getSysUsersId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (sysUsers0.getSysUsersType()!=4) {
            if (sysUsersDao.find(new SysUsers().setSysUsersId(sysUsers.getSysUsersId()).setSysUsersSup(sysUsers0.getSysUsersId()))==null) {
                return result.fail("delete 用户不存在");
            }
        }
        if (sysUsersDao.delete(sysUsers) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 sysUsersId
     * /system/v1.0.0/sysUsers/deleteAll?sysUsersId=
     */
    @DecoratorOpen
    @DeleteMapping("/deleteAll")
    public Object deleteAll(GzbJson result, Long[] sysUsersId, SysUsers sysUsers0) throws Exception {
        if (sysUsersId == null || sysUsersId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        for (Long aLong : sysUsersId) {
            if (sysUsers0.getSysUsersType()!=4) {
                if (sysUsersDao.find(new SysUsers().setSysUsersId(aLong).setSysUsersSup(sysUsers0.getSysUsersId()))==null) {
                    return result.fail("delete 用户不存在");
                }
            }
            if (sysUsersDao.delete(new SysUsers().setSysUsersId(aLong)) > -1) {
                num++;
            }
        }
        return result.success("成功删除[" + num + "]条数据");
    }

}
