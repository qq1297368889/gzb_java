package com.acquisition.service;

import com.acquisition.dao.LogInfoDao;
import com.acquisition.dao.OnLineUserDao;
import com.acquisition.dao.RoomUserDao;
import com.acquisition.dao.SysLogDao;
import com.acquisition.dao.impl.RoomUserDaoImpl;
import com.acquisition.entity.LogInfo;
import com.acquisition.entity.OnLineUser;
import com.acquisition.entity.RoomUser;
import com.acquisition.entity.SysLog;
import com.google.gson.Gson;
import gzb.frame.annotation.Service;
import gzb.frame.annotation.ThreadInterval;
import gzb.start.Application;
import gzb.tools.DateTime;
import gzb.tools.LockFactory;
import gzb.tools.Tools;
import gzb.tools.http.HTTP;
import gzb.tools.log.Log;
import gzb.tools.log.LogImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ThreadImpl {
    static Log log = new LogImpl();
    int max = 1;

    @ThreadInterval(num = 0, async = true, value = 30000)
    public void updateData_duo_mi_del(OnLineUserDao onLineUserDao) throws Exception {
        try {
            onLineUserDao.getDataBase().runSqlAsync("delete from on_line_user where on_line_user_time < ?",
                    new Object[]{new DateTime().operation(-600).toString()});
            onLineUserDao.getDataBase().runSqlAsync("delete from room_user where room_user_time < ? and room_user_aid = ?",
                    new Object[]{new DateTime().operation(-(60 * 60 * 24 * 7)).toString(),13525685});

        } catch (Exception e) {
            log.e(e);
        }
    }

    //num 定时器数量
    //async 是否异步 最好同步 很重要 异步可能引发一些问题 比如未能及时结束导致线程越来越多
    //value 等待多久后开始下次执行
    @ThreadInterval(num = 0, async = true, value = 2000)
    public void updateData_duo_mi_nv(RoomUserDao roomUserDao, LogInfoDao logInfoDao, OnLineUserDao onLineUserDao) throws Exception {
        try {
            updateData_duo_mi(roomUserDao, logInfoDao, 10, 2, 13525685L, onLineUserDao);
        } catch (Exception e) {
            log.e(e);
        }

    }

    @ThreadInterval(num = 0, async = true, value = 2000)
    public void updateData_duo_mi_nan(RoomUserDao roomUserDao, LogInfoDao logInfoDao, OnLineUserDao onLineUserDao) throws Exception {
        try {
            updateData_duo_mi(roomUserDao, logInfoDao, 10, 1, 13525685L, onLineUserDao);
        } catch (Exception e) {
            log.e(e);
        }
    }

    private void saveOnLine(String nid,OnLineUserDao onLineUserDao,RoomUserDao roomUserDao) throws Exception {
        RoomUser roomUser1 = roomUserDao.find(new RoomUser().setRoomUserOuId(nid));
        OnLineUser onLineUser = new OnLineUser();
        onLineUser.setOnLineUserAid(roomUser1.getRoomUserAid())
                .setOnLineUserTime(new DateTime().toString())
                .setOnLineUserRuid(roomUser1.getRoomUserId())
                .setOnLineUserSecId(Long.parseLong(roomUser1.getRoomUserOuId()))
                .setOnLineUserUid(Long.parseLong(roomUser1.getRoomUserOuId2()))
                .setOnLineUserPrice(roomUser1.getRoomUserPrice())
                .setOnLineUserNike(roomUser1.getRoomUserOuName())
                .setOnLineUserSex(roomUser1.getRoomUserSex())
                .setOnLineUserRsex(roomUser1.getRoomUserType())
                .setOnLineUserReadNum(0L)
                .setOnLineUserDesc("当前在线");
        onLineUserDao.saveAsync(onLineUser);
    }

    private List<RoomUser> readRoomUser(RoomUserDao roomUserDao, int size, int sex, Long aid) throws Exception {
        List<RoomUser> list = new ArrayList<>();
        RoomUser roomUser = null;
        long room_user_id = 0L;
        int max = 0;
        while (true) {
            max++;
            if (max > 100) {
                break;
            }
            if (list.size() >= size) {
                break;
            }
            //获取用户
            roomUser = roomUserDao.find("select * from room_user where " +
                            "room_user_id > ? and " +
                            "room_user_state = ? and " +
                            "room_user_price >= ? and " +
                            "room_user_sex = ? and " +
                            "room_user_aid = ? order by room_user_id asc limit ?"
                    , new Object[]{room_user_id, 0, 0, sex, aid, 1});
            if (roomUser == null) {
                roomUserDao.getDataBase().runSql("update room_user set room_user_state = 0 where " +
                        "room_user_state > ? and " +
                        "room_user_price >= ? and " +
                        "room_user_sex = ? and " +
                        "room_user_aid = ?", new Object[]{0, 0, sex, aid});
                continue;
            }
            room_user_id = roomUser.getRoomUserId();
            if (roomUser.getRoomUserOuName() != null) {
                int index = roomUser.getRoomUserOuName().indexOf(".");
                int index2 = roomUser.getRoomUserOuName().indexOf("-");
                if ((index > 0 && index < 3 && index < roomUser.getRoomUserOuName().length() / 2) ||
                                (index2 > 0 && index2 < 3 && index2 < roomUser.getRoomUserOuName().length() / 2) ||
                                roomUser.getRoomUserOuName().contains("服务") ||
                                roomUser.getRoomUserOuName().contains("厅") ||
                                roomUser.getRoomUserOuName().contains("聘") ||
                                roomUser.getRoomUserOuName().contains("管") ||
                                roomUser.getRoomUserOuName().contains("播") ||
                                roomUser.getRoomUserOuName().contains("抖") ||
                                roomUser.getRoomUserOuName().contains("💕") ||
                                roomUser.getRoomUserOuName().contains("诉")) {
                    roomUser.setRoomUserState(1L);
                    roomUserDao.update(roomUser);
                    continue;
                }
            }
            list.add(roomUser);
        }
        return list;
    }

    public void updateData_duo_mi(RoomUserDao roomUserDao, LogInfoDao logInfoDao, int size, int sex, Long aid, OnLineUserDao onLineUserDao) throws Exception {
        List<RoomUser> listRoomUser = readRoomUser(roomUserDao, size, sex, aid);
        if (listRoomUser.isEmpty()) {
            return;
        }
        StringBuilder ids = new StringBuilder();
        String uid = null;
        for (int i = 0; i < listRoomUser.size(); i++) {
            listRoomUser.get(i).setRoomUserState(1L);
            roomUserDao.update(listRoomUser.get(i));
            if (uid == null) {
                uid = listRoomUser.get(i).getRoomUserOuId2();
            }
            ids.append(listRoomUser.get(i).getRoomUserOuId2());
            if (i < listRoomUser.size() - 1) {
                ids.append("%2C");
            }
        }
        if (ids.length() < 1) {
            return;
        }
        String url = "https://papi.enlargemagic.com/user/limit/list?" +
                "ticket=5182efae-904d-4ed4-ac72-15f976e5811a" +
                "&uid=" + uid +
                "&appVersion=1.5.8" +
                "&appVersionCode=158" +
                "&deviceId=" + Tools.toMd5(Tools.getRandomString(100).getBytes())+
                "&uids=" + ids;
        HTTP http = new HTTP();
        String res = http.httpGetString(url);
        List<String> list1 = Tools.textMid(res, "\"erbanNo\":", ",\"");
        List<String> list2 = Tools.textMid(res, "\"nick\":\"", "\",\"");
        List<String> list3 = Tools.textMid(res, "\"online\":", ",\"");
        int sucNum = 0;
        if (list1.size() == list2.size() && list2.size() == list3.size()) {
            for (int i = 0; i < list1.size(); i++) {
                Object erbanNo = list1.get(i);
                Object online = list3.get(i);
                if (online != null && erbanNo != null) {
                    if (Integer.parseInt(online.toString().split("\\.")[0]) == 1) {
                        roomUserDao.getDataBase().runSql(
                                "update room_user set " +
                                        "room_user_time = ?,room_user_state = ? " +
                                        "where room_user_ou_id = ?",
                                new Object[]{new DateTime().toString(), 2, erbanNo});
                        sucNum++;
                        saveOnLine(erbanNo.toString(), onLineUserDao,roomUserDao);
                    }
                }
            }
        }
        max = Tools.getRandomInt(30, 90);
        log.i("等待时间", max,
                "检测性别", sex == 1 ? "男性" : "女性",
                "在线数量", sucNum,
                "检测模板", ids.toString().replace("%2C", ",")
        );
        int jc01 = Tools.getRandomInt(5,10);
        for (int i = 0; i < max; i++) {
            Thread.sleep(1000);
            if (i>0 && i% jc01 ==0) {
                List<OnLineUser>list01=onLineUserDao.query("select * from on_line_user where on_line_user_sex = ? limit 1",new Object[]{sex});
                if (list01.isEmpty()) {
                    break;
                }
            }
        }
        try {
            Thread.sleep(Tools.getRandomInt(2000, 1000));
        } catch (InterruptedException e) {
            log.e(e);
        }
    }

}
