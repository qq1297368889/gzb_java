package com.acquisition.api;

import com.acquisition.SqlTools;
import com.acquisition.dao.*;
import com.acquisition.entity.OnLineUser;
import com.acquisition.entity.RegKey;
import com.acquisition.entity.RoomUser;
import com.acquisition.entity.Rooms;
import gzb.frame.annotation.*;
import gzb.frame.db.DataBase;
import gzb.frame.factory.ClassFactory;
import gzb.tools.*;
import gzb.tools.cache.Cache;
import gzb.tools.log.Log;
import gzb.tools.session.Session;
import org.relaxng.datatype.Datatype;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

@Controller
@RequestMapping(value = "/system/v1.0.0/script", header = "content-type:application/json;charset=UTF-8")
public class ScriptApi {

    @Resource
    RoomsDao roomsDao;
    @Resource
    RoomUserDao roomUserDao;
    @Resource
    LiveAppDao liveAppDao;
    @Resource
    OnLineUserDao onLineUserDao;
    @Resource
    RegKeyDao regKeyDao;
    @Resource
    Log log;
    // /system/v1.0.0/script/cookie/save
    @GetMapping("test001")
    public Object test001(JSONResult result) throws Exception {
        return result.success("001");
    }
    @RequestMapping("m3u8/save")
    public Object m3u8_save(JSONResult result,String path,String title) throws Exception {
        log.d("m3u8_save",title,path);
        String path1="E:\\00\\maomi\\2025\\"+ Tools.toMd5(title.getBytes());
        File file=new File(path1);
        if(file.exists()){
            return result.fail("已存在");
        }
        file.mkdirs();
        Tools.fileSaveString(path1+"/title.txt",title,false);
        Tools.fileSaveString(path1+"/m3u8.txt",path,false);
        return result.success("数据已保存");
    }
    @PostMapping("test002")
    public Object test002(JSONResult result,String name) throws Exception {
        return result.success(name);
    }
    @PostMapping("cookie/save")
    public Object cookieSave(JSONResult result, String cookie,String token,String uid) throws Exception {
        String path = Config.get("gzb.system.server.http.static.dir");
        File file = new File(path + "/cookies/");
        file.mkdirs();
        if (new File(path + "/cookies/" + token + ".json").exists()) {
            return null;
        }
        Tools.fileSaveString(path + "/cookies/" + token + ".json", cookie, false);
        log.d("token",token);
        return null;
    }
    @GetMapping("cookie/read")
    public Object cookieRead(JSONResult result, String name) throws Exception {
        String path = Config.get("gzb.system.server.http.static.dir");
        if (name==null) {
           List<File> list= Tools.fileSub(path,2,".json");
            if (!list.isEmpty()) {
                name=list.get(0).getName();
            }
        }
        if (name==null) {
            return result.fail("文件不存在");
        }
        if (!name.endsWith(".json")) {
            name += ".json";
        }
        if (!new File(path + "/cookies/" + name).exists()) {
            return result.fail("文件不存在");
        }
        return Tools.fileReadString(path + "/cookies/" + name);
    }

    /// http://127.0.0.1:2081/system/v1.0.0/script/room/user/read/sign?roomsAid=13525685&sex=1&code=def001
    @GetMapping("room/user/read/sign")
    public Object rooms_user_read_sign(JSONResult result, Long roomsAid, Long sex, String code) throws Exception {
        int 每个用户可以被获取几次 = 1;
        int 每个授权卡每日配额 = 500;
        OnLineUser onLineUser = null;
        Lock lock = LockFactory.getLock("rooms_user_read_sign");
        lock.lock();
        try {
            if (code == null) {
                return result.fail("请验证权限");
            }
            RegKey regKey = regKeyDao.find(new RegKey().setRegKeyText(code));
            if (regKey == null) {
                return result.fail("无授权");
            }
            if (sex==11 || sex == 12) {
                RoomUser roomUser = roomUserDao.find("select * from room_user where room_user_aid = ? and room_user_state = ? and room_user_sex = ? order by room_user_id asc limit ?",new Object[]{roomsAid,0,sex,1});
                if (roomUser==null) {
                    return result.fail("无数据");
                }
                roomUser.setRoomUserState(1L);
                roomUserDao.update(roomUser);
                onLineUser=new OnLineUser();
                onLineUser.setOnLineUserSecId(Long.valueOf(roomUser.getRoomUserOuId())) ;
            }else{
                DateTime dateTime = new DateTime();
                DateTime dateTime1 = new DateTime(regKey.getRegKeyStart());
                DateTime dateTime2 = new DateTime(regKey.getRegKeyEnd());
                if (dateTime1.toStampInt() > dateTime.toStampInt() || dateTime2.toStampInt() < dateTime.toStampInt()) {
                    return result.fail("授权过期了");
                }
                String day = new DateTime().formatDateTime("yyyy-MM-dd");
                if (regKey.getRegKeyFz() == null || !regKey.getRegKeyFz().equals(day)) {
                    regKey.setRegKeyNum(0L);
                    regKey.setRegKeyFz(day);
                }
                if (regKey.getRegKeyNum() > 每个授权卡每日配额) {
                    return result.fail("获取UID数量 超过每日配额，请明天0点后重试");
                }
                onLineUser = onLineUserDao.find("select * from on_line_user where " +
                        "on_line_user_aid = ? and " +
                        "on_line_user_sex = ? " +
                        "order by on_line_user_time " +
                        "limit ?", new Object[]{roomsAid, sex, 1});
                if (onLineUser == null) {
                    return result.fail("暂无数据请稍等");
                }
                if (onLineUser.getOnLineUserReadNum() == null) {
                    onLineUser.setOnLineUserReadNum(0L);
                }
                onLineUser.setOnLineUserReadNum(onLineUser.getOnLineUserReadNum() + 1);
                if (onLineUser.getOnLineUserReadNum() >= 每个用户可以被获取几次) {
                    onLineUserDao.delete(onLineUser);
                }
                regKey.setRegKeyNum(regKey.getRegKeyNum() + 1);
                regKeyDao.update(regKey);
            }

            return result.success("获取成功", onLineUser.toString());
        } finally {
            lock.unlock();
        }

    }

    /// /system/v1.0.0/script/rooms/save
    /// roomsOid=&roomsName=&roomsSex=&roomsTag=&roomsDesc=&roomsTime=&roomsState=&roomsRead=

    @PostMapping("rooms/save")
    public Object rooms_save(JSONResult result, Map<String, String[]> req) throws Exception {
        List<Rooms> listRooms = SqlTools.asListRooms(req, null);
        log.d("listRooms", listRooms);
        int size = 0;
        for (Rooms listRoom : listRooms) {
            Rooms rooms = roomsDao.find(new Rooms().setRoomsOid(listRoom.getRoomsOid()));
            if (rooms != null) {
                roomsDao.update(listRoom.setRoomsId(rooms.getRoomsId()));
            } else {
                roomsDao.save(listRoom);
            }
            size++;

        }
        return result.success("保存成功[" + size + "]");
    }

    @GetMapping("rooms/read")
    public Object rooms_read(JSONResult result, Long roomsAid, String tableName) throws Exception {
        //rooms_send
        Lock lock = LockFactory.getLock("rooms_read");
        lock.lock();
        String[] arr1 = "点唱/交友/男声/女声".split("/");
        String tag = arr1[Tools.getRandomInt(3, 0)];
        try {
            String sql = "select * from " + tableName + " where " +
                    "rooms_aid = " + roomsAid +
                    " and rooms_read = 0 and " +
                    "rooms_tag = '" + tag + "' " +
                    "order by rooms_heat desc " +
                    "limit 1";
            List<GzbMap> list = roomsDao.getDataBase().selectGzbMap(sql);
            if (list.isEmpty()) {
                for (String string : arr1) {
                    sql = "select * from " + tableName + " where " +
                            "rooms_aid = " + roomsAid +
                            " and rooms_read = 0 and " +
                            "rooms_tag = '" + string + "' " +
                            "order by rooms_heat desc " +
                            "limit 1";
                    list = roomsDao.getDataBase().selectGzbMap(sql);
                    if (!list.isEmpty()) {
                        break;
                    }
                }
            }
            if (list.isEmpty()) {
                //roomsDao.getDataBase().runSql("update "+tableName+" set rooms_read = 0 where rooms_aid = " + roomsAid);
                //list = roomsDao.getDataBase().selectGzbMap(sql);
            }
            if (list.isEmpty()) {
                return result.fail("不存在对应数据");
            }
            roomsDao.getDataBase().runSql("update " + tableName + " set rooms_read = 1 where rooms_id = " + list.get(0).get("roomsId"));
            return result.success("查询成功", list);
        } finally {
            lock.unlock();
        }
    }

    /// /system/v1.0.0/script/rooms/user/save
    /// roomsOid=&roomsName=&roomsSex=&roomsTag=&roomsDesc=&roomsTime=&roomsState=&roomsRead=
    @PostMapping("rooms/user/save")
    public Object rooms_user_save(JSONResult result, Map<String, String[]> req) throws Exception {
        log.d(req);
        List<RoomUser> listRoomUser = SqlTools.asListRoomUser(req, null);
        int size = 0;
        for (RoomUser listRoom : listRoomUser) {
            RoomUser roomUser = roomUserDao.find(new RoomUser().setRoomUserOuId(listRoom.getRoomUserOuId()));
            if (roomUser != null) {
                roomUserDao.update(listRoom.setRoomUserId(roomUser.getRoomUserId()));
            } else {
                roomUserDao.save(listRoom);
            }
            size++;
        }
        return result.success("保存成功[" + size + "]");
    }


    /// /system/v1.0.0/script/rooms/read?roomsAid=xx

    @GetMapping("room/user/update")
    public Object rooms_user_update(JSONResult result, Long uid) throws Exception {
        RoomUser roomUser = null;
        roomUser = roomUserDao.find("select * from room_user where " +
                "room_user_ou_id = ? " +
                "limit ?", new Object[]{uid, 1});
        if (roomUser == null) {
            return result.fail("无对应UID");
        }
        roomUser.setRoomUserOuId(uid.toString());
        roomUser.setRoomUserTime(new DateTime().toString());
        roomUser.setRoomUserState(2L);
        roomUserDao.update(roomUser);
        return result.success("完成任务");
    }

    /// http://127.0.0.1:2081/system/v1.0.0/script/room/user/read?aid=568532145&sex=1&tid=2
    @GetMapping("room/user/read")
    public Object rooms_user_read(JSONResult result, Long roomsAid, Long tid, Long sex) throws Exception {
        Lock lock = LockFactory.getLock("rooms_user_read");
        lock.lock();
        RoomUser roomUser = null;
        try {
            roomUser = roomUserDao.find("select * from room_user where " +
                    "room_user_aid = ? and " +
                    "room_user_sex = ? and " +
                    "room_user_type = ? and " +
                    "room_user_state = ? " +
                    "limit ?", new Object[]{roomsAid, sex, tid, 0, 1});
            if (roomUser == null) {
                roomsDao.getDataBase().runSql("update room_user set room_user_state = 0 where room_user_aid = " + roomsAid);
                roomUser = roomUserDao.find("select * from room_user where " +
                        "room_user_aid = ? and " +
                        "room_user_sex = ? and " +
                        "room_user_type = ? and " +
                        "room_user_state = ? " +
                        "limit ?", new Object[]{roomsAid, sex, tid, 0, 1});
            }
            if (roomUser == null) {
                return result.fail("不存在对应数据");
            }

            roomsDao.getDataBase().runSql("update room_user set room_user_state = 1 where room_user_id = " + roomUser.getRoomUserId());
            return result.success("查询成功", roomUser.toString());
        } finally {
            lock.unlock();
        }
    }


}
