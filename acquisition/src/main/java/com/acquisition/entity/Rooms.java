package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.RoomsDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="rooms",desc="rooms")
public class Rooms implements Serializable{
    @EntityAttribute(key=true,size = 19,name="rooms_id",desc="roomsId")
    private java.lang.Long roomsId;
    @EntityAttribute(key=false,size = 32,name="rooms_oid",desc="roomsOid")
    private java.lang.String roomsOid;
    @EntityAttribute(key=false,size = 32,name="rooms_oid2",desc="roomsOid2")
    private java.lang.String roomsOid2;
    @EntityAttribute(key=false,size = 32,name="rooms_oid3",desc="roomsOid3")
    private java.lang.String roomsOid3;
    @EntityAttribute(key=false,size = 19,name="rooms_heat",desc="roomsHeat")
    private java.lang.Long roomsHeat;
    @EntityAttribute(key=false,size = 255,name="rooms_name",desc="roomsName")
    private java.lang.String roomsName;
    @EntityAttribute(key=false,size = 19,name="rooms_sex",desc="roomsSex")
    private java.lang.Long roomsSex;
    @EntityAttribute(key=false,size = 19,name="rooms_type",desc="roomsType")
    private java.lang.Long roomsType;
    @EntityAttribute(key=false,size = 255,name="rooms_tag",desc="roomsTag")
    private java.lang.String roomsTag;
    @EntityAttribute(key=false,size = 255,name="rooms_desc",desc="roomsDesc")
    private java.lang.String roomsDesc;
    @EntityAttribute(key=false,size = 19,name="rooms_time",desc="roomsTime")
    private java.lang.String roomsTime;
    @EntityAttribute(key=false,size = 19,name="rooms_state",desc="roomsState")
    private java.lang.Long roomsState;
    @EntityAttribute(key=false,size = 19,name="rooms_read",desc="roomsRead")
    private java.lang.Long roomsRead;
    @EntityAttribute(key=false,size = 19,name="rooms_aid",desc="roomsAid")
    private java.lang.Long roomsAid;
    private List<?> list;
    public Rooms() {}

    public Rooms(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public Rooms(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("roomsId");
        if (str!=null && !str.isEmpty()) {
            setRoomsId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsOid");
        if (str!=null && !str.isEmpty()) {
            setRoomsOid(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsOid2");
        if (str!=null && !str.isEmpty()) {
            setRoomsOid2(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsOid3");
        if (str!=null && !str.isEmpty()) {
            setRoomsOid3(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsHeat");
        if (str!=null && !str.isEmpty()) {
            setRoomsHeat(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsName");
        if (str!=null && !str.isEmpty()) {
            setRoomsName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsSex");
        if (str!=null && !str.isEmpty()) {
            setRoomsSex(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsType");
        if (str!=null && !str.isEmpty()) {
            setRoomsType(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsTag");
        if (str!=null && !str.isEmpty()) {
            setRoomsTag(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsDesc");
        if (str!=null && !str.isEmpty()) {
            setRoomsDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsTime");
        if (str!=null && !str.isEmpty()) {
            setRoomsTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("roomsState");
        if (str!=null && !str.isEmpty()) {
            setRoomsState(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsRead");
        if (str!=null && !str.isEmpty()) {
            setRoomsRead(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("roomsAid");
        if (str!=null && !str.isEmpty()) {
            setRoomsAid(java.lang.Long.valueOf(str));
        }
    }

    public Rooms(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public Rooms(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(RoomsDao roomsDao) throws Exception {
        return roomsDao.save(this);
    }
    public int delete(RoomsDao roomsDao) throws Exception {
        return roomsDao.delete(this);
    }
    public int update(RoomsDao roomsDao) throws Exception {
        return roomsDao.update(this);
    }
    public int saveAsync(RoomsDao roomsDao) throws Exception {
        return roomsDao.saveAsync(this);
    }
    public int deleteAsync(RoomsDao roomsDao) throws Exception {
        return roomsDao.deleteAsync(this);
    }
    public int updateAsync(RoomsDao roomsDao) throws Exception {
        return roomsDao.updateAsync(this);
    }
    public List<Rooms> query(RoomsDao roomsDao) throws Exception {
        return roomsDao.query(this);
    }
    public List<Rooms> query(RoomsDao roomsDao,int page,int size) throws Exception {
        return roomsDao.query(this,page,size);
    }
    public List<Rooms> query(RoomsDao roomsDao,String sortField,String sortType,int page,int size) throws Exception {
        return roomsDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(RoomsDao roomsDao, String sortField, String sortType, int page, int size) throws Exception {
        return roomsDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(RoomsDao roomsDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return roomsDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public Rooms find(RoomsDao roomsDao) throws Exception {
        return roomsDao.find(this);
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
        json.put("roomsId", getRoomsId());
        json.put("roomsOid", getRoomsOid());
        json.put("roomsOid2", getRoomsOid2());
        json.put("roomsOid3", getRoomsOid3());
        json.put("roomsHeat", getRoomsHeat());
        json.put("roomsName", getRoomsName());
        json.put("roomsSex", getRoomsSex());
        json.put("roomsType", getRoomsType());
        json.put("roomsTag", getRoomsTag());
        json.put("roomsDesc", getRoomsDesc());
        json.put("roomsTime", getRoomsTime());
        json.put("roomsState", getRoomsState());
        json.put("roomsRead", getRoomsRead());
        json.put("roomsAid", getRoomsAid());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getRoomsId() {
        return roomsId;
    }
    public Rooms setRoomsId(java.lang.Long roomsId) {
        int size0 = Tools.textLength(roomsId);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsId最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsId);
        }
        this.roomsId = roomsId;
        return this;
    }
    public Rooms setRoomsIdUnsafe(java.lang.Long roomsId) {
        this.roomsId = roomsId;
        return this;
    }
    public java.lang.String getRoomsOid() {
        return roomsOid;
    }
    public Rooms setRoomsOid(java.lang.String roomsOid) {
        int size0 = Tools.textLength(roomsOid);
        if (size0 > 32) {
            throw new RuntimeException("Rooms.roomsOid最大长度为:32,实际长度为:"+ size0 +",数据为:"+roomsOid);
        }
        this.roomsOid = roomsOid;
        return this;
    }
    public Rooms setRoomsOidUnsafe(java.lang.String roomsOid) {
        this.roomsOid = roomsOid;
        return this;
    }
    public java.lang.String getRoomsOid2() {
        return roomsOid2;
    }
    public Rooms setRoomsOid2(java.lang.String roomsOid2) {
        int size0 = Tools.textLength(roomsOid2);
        if (size0 > 32) {
            throw new RuntimeException("Rooms.roomsOid2最大长度为:32,实际长度为:"+ size0 +",数据为:"+roomsOid2);
        }
        this.roomsOid2 = roomsOid2;
        return this;
    }
    public Rooms setRoomsOid2Unsafe(java.lang.String roomsOid2) {
        this.roomsOid2 = roomsOid2;
        return this;
    }
    public java.lang.String getRoomsOid3() {
        return roomsOid3;
    }
    public Rooms setRoomsOid3(java.lang.String roomsOid3) {
        int size0 = Tools.textLength(roomsOid3);
        if (size0 > 32) {
            throw new RuntimeException("Rooms.roomsOid3最大长度为:32,实际长度为:"+ size0 +",数据为:"+roomsOid3);
        }
        this.roomsOid3 = roomsOid3;
        return this;
    }
    public Rooms setRoomsOid3Unsafe(java.lang.String roomsOid3) {
        this.roomsOid3 = roomsOid3;
        return this;
    }
    public java.lang.Long getRoomsHeat() {
        return roomsHeat;
    }
    public Rooms setRoomsHeat(java.lang.Long roomsHeat) {
        int size0 = Tools.textLength(roomsHeat);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsHeat最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsHeat);
        }
        this.roomsHeat = roomsHeat;
        return this;
    }
    public Rooms setRoomsHeatUnsafe(java.lang.Long roomsHeat) {
        this.roomsHeat = roomsHeat;
        return this;
    }
    public java.lang.String getRoomsName() {
        return roomsName;
    }
    public Rooms setRoomsName(java.lang.String roomsName) {
        int size0 = Tools.textLength(roomsName);
        if (size0 > 255) {
            throw new RuntimeException("Rooms.roomsName最大长度为:255,实际长度为:"+ size0 +",数据为:"+roomsName);
        }
        this.roomsName = roomsName;
        return this;
    }
    public Rooms setRoomsNameUnsafe(java.lang.String roomsName) {
        this.roomsName = roomsName;
        return this;
    }
    public java.lang.Long getRoomsSex() {
        return roomsSex;
    }
    public Rooms setRoomsSex(java.lang.Long roomsSex) {
        int size0 = Tools.textLength(roomsSex);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsSex最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsSex);
        }
        this.roomsSex = roomsSex;
        return this;
    }
    public Rooms setRoomsSexUnsafe(java.lang.Long roomsSex) {
        this.roomsSex = roomsSex;
        return this;
    }
    public java.lang.Long getRoomsType() {
        return roomsType;
    }
    public Rooms setRoomsType(java.lang.Long roomsType) {
        int size0 = Tools.textLength(roomsType);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsType最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsType);
        }
        this.roomsType = roomsType;
        return this;
    }
    public Rooms setRoomsTypeUnsafe(java.lang.Long roomsType) {
        this.roomsType = roomsType;
        return this;
    }
    public java.lang.String getRoomsTag() {
        return roomsTag;
    }
    public Rooms setRoomsTag(java.lang.String roomsTag) {
        int size0 = Tools.textLength(roomsTag);
        if (size0 > 255) {
            throw new RuntimeException("Rooms.roomsTag最大长度为:255,实际长度为:"+ size0 +",数据为:"+roomsTag);
        }
        this.roomsTag = roomsTag;
        return this;
    }
    public Rooms setRoomsTagUnsafe(java.lang.String roomsTag) {
        this.roomsTag = roomsTag;
        return this;
    }
    public java.lang.String getRoomsDesc() {
        return roomsDesc;
    }
    public Rooms setRoomsDesc(java.lang.String roomsDesc) {
        int size0 = Tools.textLength(roomsDesc);
        if (size0 > 255) {
            throw new RuntimeException("Rooms.roomsDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+roomsDesc);
        }
        this.roomsDesc = roomsDesc;
        return this;
    }
    public Rooms setRoomsDescUnsafe(java.lang.String roomsDesc) {
        this.roomsDesc = roomsDesc;
        return this;
    }
    public java.lang.String getRoomsTime() {
        return roomsTime;
    }
    public Rooms setRoomsTime(java.lang.String roomsTime) {
        int size0 = Tools.textLength(roomsTime);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsTime);
        }
        this.roomsTime = roomsTime;
        return this;
    }
    public Rooms setRoomsTimeUnsafe(java.lang.String roomsTime) {
        this.roomsTime = roomsTime;
        return this;
    }
    public java.lang.Long getRoomsState() {
        return roomsState;
    }
    public Rooms setRoomsState(java.lang.Long roomsState) {
        int size0 = Tools.textLength(roomsState);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsState最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsState);
        }
        this.roomsState = roomsState;
        return this;
    }
    public Rooms setRoomsStateUnsafe(java.lang.Long roomsState) {
        this.roomsState = roomsState;
        return this;
    }
    public java.lang.Long getRoomsRead() {
        return roomsRead;
    }
    public Rooms setRoomsRead(java.lang.Long roomsRead) {
        int size0 = Tools.textLength(roomsRead);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsRead最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsRead);
        }
        this.roomsRead = roomsRead;
        return this;
    }
    public Rooms setRoomsReadUnsafe(java.lang.Long roomsRead) {
        this.roomsRead = roomsRead;
        return this;
    }
    public java.lang.Long getRoomsAid() {
        return roomsAid;
    }
    public Rooms setRoomsAid(java.lang.Long roomsAid) {
        int size0 = Tools.textLength(roomsAid);
        if (size0 > 19) {
            throw new RuntimeException("Rooms.roomsAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+roomsAid);
        }
        this.roomsAid = roomsAid;
        return this;
    }
    public Rooms setRoomsAidUnsafe(java.lang.Long roomsAid) {
        this.roomsAid = roomsAid;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
