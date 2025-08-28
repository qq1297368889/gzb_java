package com.acquisition.api;

import com.acquisition.entity.RoomUser;
import com.acquisition.dao.RoomUserDao;
import com.frame.entity.SysUsers;
import gzb.entity.SqlTemplate;
import gzb.frame.annotation.*;
import gzb.tools.JSONResult;
import gzb.tools.Tools;
import gzb.tools.log.Log;

@Controller
@RequestMapping(value = "/system/v1.0.0/roomUser", header = "content-type:application/json;charset=UTF-8")
public class RoomUserController{
    @Resource(value = "com.acquisition.dao.impl.RoomUserDaoImpl")
    RoomUserDao roomUserDao;
    @Resource(value = "gzb.tools.log.LogImpl")
    Log log;

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 RoomUser的参数 
     * /system/v1.0.0/roomUser/find?参数同下,只是仅接受查询结果为1条的情况
     * */
    @DecoratorOpen
    @GetMapping(value = "find")
    public Object find(JSONResult result, RoomUser roomUser) throws Exception {
        if (roomUser == null || roomUser.toString().equals("{}")) {
            return result.fail("find 输入参数错误");
        }

        RoomUser roomUser1 = roomUserDao.find(roomUser);
        if (roomUser1 == null) {
            return result.fail("find 要查询的数据不存在或不适合find");
        }
        return result._data(roomUser1.toString()).success("查询成功");
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 RoomUser的参数 
     * /system/v1.0.0/roomUser/list?参数同下,只是是根据实体类查询
     * */
    @DecoratorOpen
    @GetMapping("list")
    public Object list(JSONResult result,RoomUser roomUser, String sortField, String sortType, Integer page, Integer size) throws Exception {

        return roomUserDao.queryPage(roomUser, sortField, sortType, page, size, 100, 1000);
    }

    /**
     * 修改,根据id修改其他参数 请提供 RoomUser的参数 
     * /system/v1.0.0/roomUser/query?field=字段名&symbol=运算方式&value=值&montage=和下一条件的连接方式&sortField=sortField排序字段&sortField=排序方式&page=页码&limit=本页长度
     * */
    @DecoratorOpen
    @GetMapping("query")
    public Object query(JSONResult result, String[] field, String[] symbol, String[] value,
                        String[] montage, String sortField, String sortType,
                        Integer page, Integer limit, SysUsers sysUsers) throws Exception {
        if (sysUsers.getSysUsersType()!=4L) {
            if (page==null) {
                page=1;
            }
            if (limit==null) {
                limit=10;
            }
            if (page>1) {
                page=1;
            }
            if (limit>50) {
                limit=50;
            }
            field=Tools.appendArray(field,new String[]{},"room_user_aid");
            symbol= Tools.appendArray(symbol,new String[]{},"1");
            value=Tools.appendArray(value,new String[]{},sysUsers.getSysUsersOpenId());
            montage=Tools.appendArray(montage,new String[]{},"1");
        }

        SqlTemplate sqlTemplate = roomUserDao.getDataBase().toSelect("room_user", field, symbol, value, montage, sortField, sortType);
        return roomUserDao.queryPage(sqlTemplate.getSql(), sqlTemplate.getObjects(), sortField, sortType, page, limit, 0, 0);
    }

    /**
     * 插入数据,不能接受空数据,请至少提供一个参数,请提供 RoomUser的参数 
     * /system/v1.0.0/roomUser/save
     * */
    @DecoratorOpen
    @PostMapping("save")
    public Object save(JSONResult result, RoomUser roomUser) throws Exception {
        if (roomUser == null || roomUser.toString().equals("{}")) {
            return result.fail("save 输入参数错误");
        }
        if (roomUserDao.save(roomUser) < 0) {
            return result.fail("save 保存失败");
        }
        return result.success("save 成功");
    }

    /**
     * 修改,根据id修改其他参数 请提供 RoomUser的参数 
     * /system/v1.0.0/roomUser/update?roomUserId=
     * */
    @DecoratorOpen
    @PostMapping("update")
    public Object update(JSONResult result, RoomUser roomUser) throws Exception {
        if (roomUser == null || roomUser.toString().equals("{}") || roomUser.getRoomUserId() == null) {
            return result.fail("update 输入参数错误");
        }
        if (roomUserDao.update(roomUser) < 0) {
            return result.fail("update 失败");
        }
        return result.success("update 成功");
    }

    /**
     * 单个删除,根据id 请提供 roomUserId 
     * /system/v1.0.0/roomUser/delete?roomUserId=
     * */
    @DecoratorOpen
    @DeleteMapping("delete")
    public Object delete(JSONResult result, RoomUser roomUser) throws Exception {
        if (roomUser == null || roomUser.toString().equals("{}") || roomUser.getRoomUserId() == null) {
            return result.fail("delete 输入参数错误");
        }
        if (roomUserDao.delete(roomUser) < 0) {
            return result.fail("delete 失败");
        }
        return result.success("成功删除[1]条数据");
    }

    /**
     * 批量删除,根据id 删除多个请重复提供 roomUserId 
     * /system/v1.0.0/roomUser/deleteAll?roomUserId=
     * */
    @DecoratorOpen
    @GetMapping("deleteAll")
    public Object deleteAll(JSONResult result, Long[] roomUserId) throws Exception {
        if (roomUserId == null || roomUserId.length < 1) {
            return result.fail("delete 输入参数错误");
        }
        int num=0;
        RoomUser acquisition = new RoomUser();
        for (Long aLong : roomUserId) {
            roomUserDao.delete(acquisition.setRoomUserId(aLong));num++;
        }
        return result.success("成功删除["+num+"]条数据");
    }
}
