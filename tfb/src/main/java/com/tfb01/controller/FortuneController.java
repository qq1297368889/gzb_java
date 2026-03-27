package com.tfb01.controller;

import com.tfb01.entity.Fortune;
import com.tfb01.dao.FortuneDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.json.GzbJson;
import gzb.tools.log.Log;

@Controller
@Header(item = {@HeaderItem(key = "content-type", val = "application/json;charset=UTF-8")})
//@CrossDomain(allowCredentials = false)
@RequestMapping(value = "/system/v1.0.0/fortune")
public class FortuneController{
    @Resource(value = "com.tfb01.dao.impl.FortuneDaoImpl")
    FortuneDao fortuneDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 Fortune的参数 
     * /system/v1.0.0/fortune/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(GzbJson result, Fortune fortune) throws Exception {
        if (fortune == null || fortune.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        Fortune fortune1 = fortuneDao.find(fortune);
        if (fortune1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result.success("查询成功",fortune1);
    }

    /**
     * 查询,不能接受空数据,请至少提供一个参数,请提供 Fortune的参数 
     * /system/v1.0.0/fortune/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(GzbJson result,Fortune fortune, String sortField, String sortType, Integer page, Integer size) throws Exception {
        if(fortune == null){
           fortune = new Fortune();
        }
        return fortuneDao.queryPage(fortune, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 查询,根据id修改其他参数 请提供 Fortune的参数 
     * /system/v1.0.0/fortune/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(GzbJson result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = fortuneDao.getDataBase().toSelect("fortune", field, symbol, value, montage, null, null);
        return fortuneDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 Fortune的参数 
     * /system/v1.0.0/fortune/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(GzbJson result, Fortune fortune) throws Exception {
        if (fortune == null || fortune.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (fortuneDao.save(fortune) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 Fortune的参数 
     * /system/v1.0.0/fortune/update?id=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(GzbJson result, Fortune fortune) throws Exception {
        if (fortune == null || fortune.toString().equals("{}") || fortune.getId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (fortuneDao.update(fortune) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 id 
     * /system/v1.0.0/fortune/delete?id=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(GzbJson result, Fortune fortune) throws Exception {
        if (fortune == null || fortune.toString().equals("{}") || fortune.getId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (fortuneDao.delete(fortune) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 id 
     * /system/v1.0.0/fortune/deleteAll?id=
     * */
    @DecoratorOpen
    @DeleteMapping("deleteAll")
    public Object deleteAll(GzbJson result, Integer[] id) throws Exception {
        if (id == null || id.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num = 0;
        Fortune tfb01 = new Fortune();
        for (Integer _id : id) {
            fortuneDao.delete(tfb01.setId(_id));
            num++;
        }
        return result.success("成功删除[" + num + "]条数据");
    }
};