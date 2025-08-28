package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.RoomUserDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="room_user",desc="roomUser")
public class RoomUser implements Serializable{
    @EntityAttribute(key=true,size = 19,name="room_user_id",desc="roomUserId")
    private java.lang.Long roomUserId;
    @EntityAttribute(key=false,size = 19,name="room_user_sex",desc="roomUserSex")
    private java.lang.Long roomUserSex;
    @EntityAttribute(key=false,size = 19,name="room_user_rid",desc="roomUserRid")
    private java.lang.Long roomUserRid;
    @EntityAttribute(key=false,size = 19,name="room_user_type",desc="roomUserType")
    private java.lang.Long roomUserType;
    @EntityAttribute(key=false,size = 19,name="room_user_price",desc="roomUserPrice")
    private java.lang.Long roomUserPrice;
    @EntityAttribute(key=false,size = 20,name="room_user_ou_id",desc="roomUserOuId")
    private java.lang.String roomUserOuId;
    @EntityAttribute(key=false,size = 20,name="room_user_ou_id2",desc="roomUserOuId2")
    private java.lang.String roomUserOuId2;
    @EntityAttribute(key=false,size = 20,name="room_user_ou_id3",desc="roomUserOuId3")
    private java.lang.String roomUserOuId3;
    @EntityAttribute(key=false,size = 255,name="room_user_ou_name",desc="roomUserOuName")
    private java.lang.String roomUserOuName;
    @EntityAttribute(key=false,size = 19,name="room_user_state",desc="roomUserState")
    private java.lang.Long roomUserState;
    @EntityAttribute(key=false,size = 19,name="room_user_time",desc="roomUserTime")
    private java.lang.String roomUserTime;
    @EntityAttribute(key=false,size = 536870911,name="room_user_data",desc="roomUserData")
    private java.lang.String roomUserData;
    @EntityAttribute(key=false,size = 19,name="room_user_aid",desc="roomUserAid")
    private java.lang.Long roomUserAid;
    private List<?> list;
    public RoomUser() {}

    public RoomUser(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public RoomUser(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("roomUserId");
        if (str!=null && !str.isEmpty()) {
            setRoomUserId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserSex");
        if (str!=null && !str.isEmpty()) {
            setRoomUserSex(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserRid");
        if (str!=null && !str.isEmpty()) {
            setRoomUserRid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserType");
        if (str!=null && !str.isEmpty()) {
            setRoomUserType(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserPrice");
        if (str!=null && !str.isEmpty()) {
            setRoomUserPrice(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserOuId");
        if (str!=null && !str.isEmpty()) {
            setRoomUserOuId(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserOuId2");
        if (str!=null && !str.isEmpty()) {
            setRoomUserOuId2(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserOuId3");
        if (str!=null && !str.isEmpty()) {
            setRoomUserOuId3(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserOuName");
        if (str!=null && !str.isEmpty()) {
            setRoomUserOuName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserState");
        if (str!=null && !str.isEmpty()) {
            setRoomUserState(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomUserTime");
        if (str!=null && !str.isEmpty()) {
            setRoomUserTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserData");
        if (str!=null && !str.isEmpty()) {
            setRoomUserData(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomUserAid");
        if (str!=null && !str.isEmpty()) {
            setRoomUserAid(java.lang.Long.valueOf(str));
        }
    }

    public RoomUser(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public RoomUser(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.save(this);
    }
    public int delete(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.delete(this);
    }
    public int update(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.update(this);
    }
    public int saveAsync(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.saveAsync(this);
    }
    public int deleteAsync(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.deleteAsync(this);
    }
    public int updateAsync(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.updateAsync(this);
    }
    public List<RoomUser> query(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.query(this);
    }
    public List<RoomUser> query(RoomUserDao roomUserDao,int page,int size) throws Exception {
        return roomUserDao.query(this,page,size);
    }
    public List<RoomUser> query(RoomUserDao roomUserDao,String sortField,String sortType,int page,int size) throws Exception {
        return roomUserDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(RoomUserDao roomUserDao, String sortField, String sortType, int page, int size) throws Exception {
        return roomUserDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(RoomUserDao roomUserDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return roomUserDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public RoomUser find(RoomUserDao roomUserDao) throws Exception {
        return roomUserDao.find(this);
    }
    public SqlTemplate toSelectSql() {
        return SqlTools.toSelectSql(this,"act_code_id", "asc", 0, false);
    }

    //查询语句 可选项 排序
    public SqlTemplate toSelectSql(String sortField, String sortType, int size, boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave(java.lang.Long actCodeId) {
        return SqlTools.toSave(this,actCodeId);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSON toJson() {
        JSON json = new JSON();
        json.put("roomUserId", getRoomUserId());
        json.put("roomUserSex", getRoomUserSex());
        json.put("roomUserRid", getRoomUserRid());
        json.put("roomUserType", getRoomUserType());
        json.put("roomUserPrice", getRoomUserPrice());
        json.put("roomUserOuId", getRoomUserOuId());
        json.put("roomUserOuId2", getRoomUserOuId2());
        json.put("roomUserOuId3", getRoomUserOuId3());
        json.put("roomUserOuName", getRoomUserOuName());
        json.put("roomUserState", getRoomUserState());
        json.put("roomUserTime", getRoomUserTime());
        json.put("roomUserData", getRoomUserData());
        json.put("roomUserAid", getRoomUserAid());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getRoomUserId() {
        return roomUserId;
    }
    public RoomUser setRoomUserId(java.lang.Long roomUserId) {
        int size0 = Tools.textLength(roomUserId);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserId最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserId);
        }
        this.roomUserId = roomUserId;
        return this;
    }
    public RoomUser setRoomUserIdUnsafe(java.lang.Long roomUserId) {
        this.roomUserId = roomUserId;
        return this;
    }
    public java.lang.Long getRoomUserSex() {
        return roomUserSex;
    }
    public RoomUser setRoomUserSex(java.lang.Long roomUserSex) {
        int size0 = Tools.textLength(roomUserSex);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserSex最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserSex);
        }
        this.roomUserSex = roomUserSex;
        return this;
    }
    public RoomUser setRoomUserSexUnsafe(java.lang.Long roomUserSex) {
        this.roomUserSex = roomUserSex;
        return this;
    }
    public java.lang.Long getRoomUserRid() {
        return roomUserRid;
    }
    public RoomUser setRoomUserRid(java.lang.Long roomUserRid) {
        int size0 = Tools.textLength(roomUserRid);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserRid最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserRid);
        }
        this.roomUserRid = roomUserRid;
        return this;
    }
    public RoomUser setRoomUserRidUnsafe(java.lang.Long roomUserRid) {
        this.roomUserRid = roomUserRid;
        return this;
    }
    public java.lang.Long getRoomUserType() {
        return roomUserType;
    }
    public RoomUser setRoomUserType(java.lang.Long roomUserType) {
        int size0 = Tools.textLength(roomUserType);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserType最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserType);
        }
        this.roomUserType = roomUserType;
        return this;
    }
    public RoomUser setRoomUserTypeUnsafe(java.lang.Long roomUserType) {
        this.roomUserType = roomUserType;
        return this;
    }
    public java.lang.Long getRoomUserPrice() {
        return roomUserPrice;
    }
    public RoomUser setRoomUserPrice(java.lang.Long roomUserPrice) {
        int size0 = Tools.textLength(roomUserPrice);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserPrice最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserPrice);
        }
        this.roomUserPrice = roomUserPrice;
        return this;
    }
    public RoomUser setRoomUserPriceUnsafe(java.lang.Long roomUserPrice) {
        this.roomUserPrice = roomUserPrice;
        return this;
    }
    public java.lang.String getRoomUserOuId() {
        return roomUserOuId;
    }
    public RoomUser setRoomUserOuId(java.lang.String roomUserOuId) {
        int size0 = Tools.textLength(roomUserOuId);
        if (size0 > 20) {
            throw new RuntimeException("RoomUser.roomUserOuId最大长度为:20,实际长度为:"+ size0 +",数据为:"+roomUserOuId);
        }
        this.roomUserOuId = roomUserOuId;
        return this;
    }
    public RoomUser setRoomUserOuIdUnsafe(java.lang.String roomUserOuId) {
        this.roomUserOuId = roomUserOuId;
        return this;
    }
    public java.lang.String getRoomUserOuId2() {
        return roomUserOuId2;
    }
    public RoomUser setRoomUserOuId2(java.lang.String roomUserOuId2) {
        int size0 = Tools.textLength(roomUserOuId2);
        if (size0 > 20) {
            throw new RuntimeException("RoomUser.roomUserOuId2最大长度为:20,实际长度为:"+ size0 +",数据为:"+roomUserOuId2);
        }
        this.roomUserOuId2 = roomUserOuId2;
        return this;
    }
    public RoomUser setRoomUserOuId2Unsafe(java.lang.String roomUserOuId2) {
        this.roomUserOuId2 = roomUserOuId2;
        return this;
    }
    public java.lang.String getRoomUserOuId3() {
        return roomUserOuId3;
    }
    public RoomUser setRoomUserOuId3(java.lang.String roomUserOuId3) {
        int size0 = Tools.textLength(roomUserOuId3);
        if (size0 > 20) {
            throw new RuntimeException("RoomUser.roomUserOuId3最大长度为:20,实际长度为:"+ size0 +",数据为:"+roomUserOuId3);
        }
        this.roomUserOuId3 = roomUserOuId3;
        return this;
    }
    public RoomUser setRoomUserOuId3Unsafe(java.lang.String roomUserOuId3) {
        this.roomUserOuId3 = roomUserOuId3;
        return this;
    }
    public java.lang.String getRoomUserOuName() {
        return roomUserOuName;
    }
    public RoomUser setRoomUserOuName(java.lang.String roomUserOuName) {
        int size0 = Tools.textLength(roomUserOuName);
        if (size0 > 255) {
            throw new RuntimeException("RoomUser.roomUserOuName最大长度为:255,实际长度为:"+ size0 +",数据为:"+roomUserOuName);
        }
        this.roomUserOuName = roomUserOuName;
        return this;
    }
    public RoomUser setRoomUserOuNameUnsafe(java.lang.String roomUserOuName) {
        this.roomUserOuName = roomUserOuName;
        return this;
    }
    public java.lang.Long getRoomUserState() {
        return roomUserState;
    }
    public RoomUser setRoomUserState(java.lang.Long roomUserState) {
        int size0 = Tools.textLength(roomUserState);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserState最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserState);
        }
        this.roomUserState = roomUserState;
        return this;
    }
    public RoomUser setRoomUserStateUnsafe(java.lang.Long roomUserState) {
        this.roomUserState = roomUserState;
        return this;
    }
    public java.lang.String getRoomUserTime() {
        return roomUserTime;
    }
    public RoomUser setRoomUserTime(java.lang.String roomUserTime) {
        int size0 = Tools.textLength(roomUserTime);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserTime);
        }
        this.roomUserTime = roomUserTime;
        return this;
    }
    public RoomUser setRoomUserTimeUnsafe(java.lang.String roomUserTime) {
        this.roomUserTime = roomUserTime;
        return this;
    }
    public java.lang.String getRoomUserData() {
        return roomUserData;
    }
    public RoomUser setRoomUserData(java.lang.String roomUserData) {
        int size0 = Tools.textLength(roomUserData);
        if (size0 > 536870911) {
            throw new RuntimeException("RoomUser.roomUserData最大长度为:536870911,实际长度为:"+ size0 +",数据为:"+roomUserData);
        }
        this.roomUserData = roomUserData;
        return this;
    }
    public RoomUser setRoomUserDataUnsafe(java.lang.String roomUserData) {
        this.roomUserData = roomUserData;
        return this;
    }
    public java.lang.Long getRoomUserAid() {
        return roomUserAid;
    }
    public RoomUser setRoomUserAid(java.lang.Long roomUserAid) {
        int size0 = Tools.textLength(roomUserAid);
        if (size0 > 19) {
            throw new RuntimeException("RoomUser.roomUserAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomUserAid);
        }
        this.roomUserAid = roomUserAid;
        return this;
    }
    public RoomUser setRoomUserAidUnsafe(java.lang.Long roomUserAid) {
        this.roomUserAid = roomUserAid;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
