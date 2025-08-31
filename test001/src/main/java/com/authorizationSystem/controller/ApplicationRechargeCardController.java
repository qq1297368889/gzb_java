package com.authorizationSystem.controller;

import com.authorizationSystem.entity.ApplicationRechargeCard;
import com.authorizationSystem.dao.ApplicationRechargeCardDao;
import gzb.frame.db.BaseDao;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/applicationRechargeCard", header = "content-type:application/json;charset=UTF-8")
public class ApplicationRechargeCardController{
    @Resource(value = "com.authorizationSystem.dao.impl.ApplicationRechargeCardDaoImpl")
    ApplicationRechargeCardDao applicationRechargeCardDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ApplicationRechargeCard的参数 
     * /system/v1.0.0/applicationRechargeCard/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, ApplicationRechargeCard applicationRechargeCard) throws Exception {
        if (applicationRechargeCard == null || applicationRechargeCard.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        ApplicationRechargeCard applicationRechargeCard1 = applicationRechargeCardDao.find(applicationRechargeCard);
        if (applicationRechargeCard1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(applicationRechargeCard1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ApplicationRechargeCard的参数 
     * /system/v1.0.0/applicationRechargeCard/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,ApplicationRechargeCard applicationRechargeCard, String sortField, String sortType, Integer page, Integer size) throws Exception {
        return applicationRechargeCardDao.queryPage(applicationRechargeCard, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 ApplicationRechargeCard的参数 
     * /system/v1.0.0/applicationRechargeCard/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value, String[] montage, String sortField, String sortType, Integer page, Integer limit) throws Exception {
        SqlTemplate sqlTemplate = applicationRechargeCardDao.getDataBase().toSelect("application_recharge_card", field, symbol, value, montage, sortField, sortType);
        return applicationRechargeCardDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 ApplicationRechargeCard的参数 
     * /system/v1.0.0/applicationRechargeCard/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, ApplicationRechargeCard applicationRechargeCard) throws Exception {
        if (applicationRechargeCard == null || applicationRechargeCard.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (applicationRechargeCardDao.save(applicationRechargeCard) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 ApplicationRechargeCard的参数 
     * /system/v1.0.0/applicationRechargeCard/update?applicationRechargeCardId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, ApplicationRechargeCard applicationRechargeCard) throws Exception {
        if (applicationRechargeCard == null || applicationRechargeCard.toString().equals("{}") || applicationRechargeCard.getApplicationRechargeCardId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (applicationRechargeCardDao.update(applicationRechargeCard) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 applicationRechargeCardId 
     * /system/v1.0.0/applicationRechargeCard/delete?applicationRechargeCardId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, ApplicationRechargeCard applicationRechargeCard) throws Exception {
        if (applicationRechargeCard == null || applicationRechargeCard.toString().equals("{}") || applicationRechargeCard.getApplicationRechargeCardId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (applicationRechargeCardDao.delete(applicationRechargeCard) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 applicationRechargeCardId 
     * /system/v1.0.0/applicationRechargeCard/deleteAll?applicationRechargeCardId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] applicationRechargeCardId) throws Exception {
        if (applicationRechargeCardId == null || applicationRechargeCardId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        ApplicationRechargeCard authorizationSystem = new ApplicationRechargeCard();
        for (Long aLong : applicationRechargeCardId) {
            applicationRechargeCardDao.delete(authorizationSystem.setApplicationRechargeCardId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
