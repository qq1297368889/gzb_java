package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.OnLineUserCopy1Dao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="on_line_user_copy1",desc="onLineUserCopy1")
public class OnLineUserCopy1 implements Serializable{
    @EntityAttribute(key=true,size = 19,name="on_line_user_id",desc="onLineUserId")
    private java.lang.Long onLineUserId;
    @EntityAttribute(key=false,size = 19,name="on_line_user_time",desc="onLineUserTime")
    private java.lang.String onLineUserTime;
    @EntityAttribute(key=false,size = 19,name="on_line_user_aid",desc="onLineUserAid")
    private java.lang.Long onLineUserAid;
    @EntityAttribute(key=false,size = 100,name="on_line_user_desc",desc="onLineUserDesc")
    private java.lang.String onLineUserDesc;
    @EntityAttribute(key=false,size = 100,name="on_line_user_sex",desc="onLineUserSex")
    private java.lang.String onLineUserSex;
    @EntityAttribute(key=false,size = 100,name="on_line_user_rsex",desc="onLineUserRsex")
    private java.lang.String onLineUserRsex;
    @EntityAttribute(key=false,size = 10,name="on_line_user_price",desc="onLineUserPrice")
    private java.lang.String onLineUserPrice;
    @EntityAttribute(key=false,size = 100,name="on_line_user_nike",desc="onLineUserNike")
    private java.lang.String onLineUserNike;
    @EntityAttribute(key=false,size = 19,name="on_line_user_sec_id",desc="onLineUserSecId")
    private java.lang.Long onLineUserSecId;
    @EntityAttribute(key=false,size = 19,name="on_line_user_uid",desc="onLineUserUid")
    private java.lang.Long onLineUserUid;
    @EntityAttribute(key=false,size = 19,name="on_line_user_ruid",desc="onLineUserRuid")
    private java.lang.Long onLineUserRuid;
    @EntityAttribute(key=false,size = 19,name="on_line_user_read_num",desc="onLineUserReadNum")
    private java.lang.Long onLineUserReadNum;
    private List<?> list;
    public OnLineUserCopy1() {}

    public OnLineUserCopy1(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public OnLineUserCopy1(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("onLineUserId");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserTime");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserAid");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserAid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserDesc");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserSex");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserSex(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserRsex");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserRsex(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserPrice");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserPrice(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserNike");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserNike(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("onLineUserSecId");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserSecId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserUid");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserUid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserRuid");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserRuid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserReadNum");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserReadNum(java.lang.Long.valueOf(str));
        }
    }

    public OnLineUserCopy1(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public OnLineUserCopy1(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.save(this);
    }
    public int delete(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.delete(this);
    }
    public int update(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.update(this);
    }
    public int saveAsync(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.saveAsync(this);
    }
    public int deleteAsync(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.deleteAsync(this);
    }
    public int updateAsync(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.updateAsync(this);
    }
    public List<OnLineUserCopy1> query(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.query(this);
    }
    public List<OnLineUserCopy1> query(OnLineUserCopy1Dao onLineUserCopy1Dao,int page,int size) throws Exception {
        return onLineUserCopy1Dao.query(this,page,size);
    }
    public List<OnLineUserCopy1> query(OnLineUserCopy1Dao onLineUserCopy1Dao,String sortField,String sortType,int page,int size) throws Exception {
        return onLineUserCopy1Dao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(OnLineUserCopy1Dao onLineUserCopy1Dao, String sortField, String sortType, int page, int size) throws Exception {
        return onLineUserCopy1Dao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(OnLineUserCopy1Dao onLineUserCopy1Dao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return onLineUserCopy1Dao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public OnLineUserCopy1 find(OnLineUserCopy1Dao onLineUserCopy1Dao) throws Exception {
        return onLineUserCopy1Dao.find(this);
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
        json.put("onLineUserId", getOnLineUserId());
        json.put("onLineUserTime", getOnLineUserTime());
        json.put("onLineUserAid", getOnLineUserAid());
        json.put("onLineUserDesc", getOnLineUserDesc());
        json.put("onLineUserSex", getOnLineUserSex());
        json.put("onLineUserRsex", getOnLineUserRsex());
        json.put("onLineUserPrice", getOnLineUserPrice());
        json.put("onLineUserNike", getOnLineUserNike());
        json.put("onLineUserSecId", getOnLineUserSecId());
        json.put("onLineUserUid", getOnLineUserUid());
        json.put("onLineUserRuid", getOnLineUserRuid());
        json.put("onLineUserReadNum", getOnLineUserReadNum());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getOnLineUserId() {
        return onLineUserId;
    }
    public OnLineUserCopy1 setOnLineUserId(java.lang.Long onLineUserId) {
        int size0 = Tools.textLength(onLineUserId);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserId最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserId);
        }
        this.onLineUserId = onLineUserId;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserIdUnsafe(java.lang.Long onLineUserId) {
        this.onLineUserId = onLineUserId;
        return this;
    }
    public java.lang.String getOnLineUserTime() {
        return onLineUserTime;
    }
    public OnLineUserCopy1 setOnLineUserTime(java.lang.String onLineUserTime) {
        int size0 = Tools.textLength(onLineUserTime);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserTime);
        }
        this.onLineUserTime = onLineUserTime;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserTimeUnsafe(java.lang.String onLineUserTime) {
        this.onLineUserTime = onLineUserTime;
        return this;
    }
    public java.lang.Long getOnLineUserAid() {
        return onLineUserAid;
    }
    public OnLineUserCopy1 setOnLineUserAid(java.lang.Long onLineUserAid) {
        int size0 = Tools.textLength(onLineUserAid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserAid);
        }
        this.onLineUserAid = onLineUserAid;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserAidUnsafe(java.lang.Long onLineUserAid) {
        this.onLineUserAid = onLineUserAid;
        return this;
    }
    public java.lang.String getOnLineUserDesc() {
        return onLineUserDesc;
    }
    public OnLineUserCopy1 setOnLineUserDesc(java.lang.String onLineUserDesc) {
        int size0 = Tools.textLength(onLineUserDesc);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserDesc);
        }
        this.onLineUserDesc = onLineUserDesc;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserDescUnsafe(java.lang.String onLineUserDesc) {
        this.onLineUserDesc = onLineUserDesc;
        return this;
    }
    public java.lang.String getOnLineUserSex() {
        return onLineUserSex;
    }
    public OnLineUserCopy1 setOnLineUserSex(java.lang.String onLineUserSex) {
        int size0 = Tools.textLength(onLineUserSex);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserSex最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserSex);
        }
        this.onLineUserSex = onLineUserSex;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserSexUnsafe(java.lang.String onLineUserSex) {
        this.onLineUserSex = onLineUserSex;
        return this;
    }
    public java.lang.String getOnLineUserRsex() {
        return onLineUserRsex;
    }
    public OnLineUserCopy1 setOnLineUserRsex(java.lang.String onLineUserRsex) {
        int size0 = Tools.textLength(onLineUserRsex);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserRsex最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserRsex);
        }
        this.onLineUserRsex = onLineUserRsex;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserRsexUnsafe(java.lang.String onLineUserRsex) {
        this.onLineUserRsex = onLineUserRsex;
        return this;
    }
    public java.lang.String getOnLineUserPrice() {
        return onLineUserPrice;
    }
    public OnLineUserCopy1 setOnLineUserPrice(java.lang.String onLineUserPrice) {
        int size0 = Tools.textLength(onLineUserPrice);
        if (size0 > 10) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserPrice最大长度为:10,实际长度为:"+ size0 +",数据为:"+onLineUserPrice);
        }
        this.onLineUserPrice = onLineUserPrice;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserPriceUnsafe(java.lang.String onLineUserPrice) {
        this.onLineUserPrice = onLineUserPrice;
        return this;
    }
    public java.lang.String getOnLineUserNike() {
        return onLineUserNike;
    }
    public OnLineUserCopy1 setOnLineUserNike(java.lang.String onLineUserNike) {
        int size0 = Tools.textLength(onLineUserNike);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserNike最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserNike);
        }
        this.onLineUserNike = onLineUserNike;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserNikeUnsafe(java.lang.String onLineUserNike) {
        this.onLineUserNike = onLineUserNike;
        return this;
    }
    public java.lang.Long getOnLineUserSecId() {
        return onLineUserSecId;
    }
    public OnLineUserCopy1 setOnLineUserSecId(java.lang.Long onLineUserSecId) {
        int size0 = Tools.textLength(onLineUserSecId);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserSecId最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserSecId);
        }
        this.onLineUserSecId = onLineUserSecId;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserSecIdUnsafe(java.lang.Long onLineUserSecId) {
        this.onLineUserSecId = onLineUserSecId;
        return this;
    }
    public java.lang.Long getOnLineUserUid() {
        return onLineUserUid;
    }
    public OnLineUserCopy1 setOnLineUserUid(java.lang.Long onLineUserUid) {
        int size0 = Tools.textLength(onLineUserUid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserUid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserUid);
        }
        this.onLineUserUid = onLineUserUid;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserUidUnsafe(java.lang.Long onLineUserUid) {
        this.onLineUserUid = onLineUserUid;
        return this;
    }
    public java.lang.Long getOnLineUserRuid() {
        return onLineUserRuid;
    }
    public OnLineUserCopy1 setOnLineUserRuid(java.lang.Long onLineUserRuid) {
        int size0 = Tools.textLength(onLineUserRuid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserRuid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserRuid);
        }
        this.onLineUserRuid = onLineUserRuid;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserRuidUnsafe(java.lang.Long onLineUserRuid) {
        this.onLineUserRuid = onLineUserRuid;
        return this;
    }
    public java.lang.Long getOnLineUserReadNum() {
        return onLineUserReadNum;
    }
    public OnLineUserCopy1 setOnLineUserReadNum(java.lang.Long onLineUserReadNum) {
        int size0 = Tools.textLength(onLineUserReadNum);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUserCopy1.onLineUserReadNum最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserReadNum);
        }
        this.onLineUserReadNum = onLineUserReadNum;
        return this;
    }
    public OnLineUserCopy1 setOnLineUserReadNumUnsafe(java.lang.Long onLineUserReadNum) {
        this.onLineUserReadNum = onLineUserReadNum;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
