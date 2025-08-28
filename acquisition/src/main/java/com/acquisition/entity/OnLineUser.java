package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.OnLineUserDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="on_line_user",desc="onLineUser")
public class OnLineUser implements Serializable{
    @EntityAttribute(key=true,size = 19,name="on_line_user_id",desc="onLineUserId")
    private java.lang.Long onLineUserId;
    @EntityAttribute(key=false,size = 19,name="on_line_user_time",desc="onLineUserTime")
    private java.lang.String onLineUserTime;
    @EntityAttribute(key=false,size = 19,name="on_line_user_aid",desc="onLineUserAid")
    private java.lang.Long onLineUserAid;
    @EntityAttribute(key=false,size = 100,name="on_line_user_desc",desc="onLineUserDesc")
    private java.lang.String onLineUserDesc;
    @EntityAttribute(key=false,size = 19,name="on_line_user_sex",desc="onLineUserSex")
    private java.lang.Long onLineUserSex;
    @EntityAttribute(key=false,size = 19,name="on_line_user_rsex",desc="onLineUserRsex")
    private java.lang.Long onLineUserRsex;
    @EntityAttribute(key=false,size = 19,name="on_line_user_price",desc="onLineUserPrice")
    private java.lang.Long onLineUserPrice;
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
    public OnLineUser() {}

    public OnLineUser(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public OnLineUser(GzbMap gzbMap) {
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
            setOnLineUserSex(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserRsex");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserRsex(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("onLineUserPrice");
        if (str!=null && !str.isEmpty()) {
            setOnLineUserPrice(java.lang.Long.valueOf(str));
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

    public OnLineUser(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public OnLineUser(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.save(this);
    }
    public int delete(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.delete(this);
    }
    public int update(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.update(this);
    }
    public int saveAsync(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.saveAsync(this);
    }
    public int deleteAsync(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.deleteAsync(this);
    }
    public int updateAsync(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.updateAsync(this);
    }
    public List<OnLineUser> query(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.query(this);
    }
    public List<OnLineUser> query(OnLineUserDao onLineUserDao,int page,int size) throws Exception {
        return onLineUserDao.query(this,page,size);
    }
    public List<OnLineUser> query(OnLineUserDao onLineUserDao,String sortField,String sortType,int page,int size) throws Exception {
        return onLineUserDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(OnLineUserDao onLineUserDao, String sortField, String sortType, int page, int size) throws Exception {
        return onLineUserDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(OnLineUserDao onLineUserDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return onLineUserDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public OnLineUser find(OnLineUserDao onLineUserDao) throws Exception {
        return onLineUserDao.find(this);
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
    public OnLineUser setOnLineUserId(java.lang.Long onLineUserId) {
        int size0 = Tools.textLength(onLineUserId);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserId最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserId);
        }
        this.onLineUserId = onLineUserId;
        return this;
    }
    public OnLineUser setOnLineUserIdUnsafe(java.lang.Long onLineUserId) {
        this.onLineUserId = onLineUserId;
        return this;
    }
    public java.lang.String getOnLineUserTime() {
        return onLineUserTime;
    }
    public OnLineUser setOnLineUserTime(java.lang.String onLineUserTime) {
        int size0 = Tools.textLength(onLineUserTime);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserTime);
        }
        this.onLineUserTime = onLineUserTime;
        return this;
    }
    public OnLineUser setOnLineUserTimeUnsafe(java.lang.String onLineUserTime) {
        this.onLineUserTime = onLineUserTime;
        return this;
    }
    public java.lang.Long getOnLineUserAid() {
        return onLineUserAid;
    }
    public OnLineUser setOnLineUserAid(java.lang.Long onLineUserAid) {
        int size0 = Tools.textLength(onLineUserAid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserAid);
        }
        this.onLineUserAid = onLineUserAid;
        return this;
    }
    public OnLineUser setOnLineUserAidUnsafe(java.lang.Long onLineUserAid) {
        this.onLineUserAid = onLineUserAid;
        return this;
    }
    public java.lang.String getOnLineUserDesc() {
        return onLineUserDesc;
    }
    public OnLineUser setOnLineUserDesc(java.lang.String onLineUserDesc) {
        int size0 = Tools.textLength(onLineUserDesc);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUser.onLineUserDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserDesc);
        }
        this.onLineUserDesc = onLineUserDesc;
        return this;
    }
    public OnLineUser setOnLineUserDescUnsafe(java.lang.String onLineUserDesc) {
        this.onLineUserDesc = onLineUserDesc;
        return this;
    }
    public java.lang.Long getOnLineUserSex() {
        return onLineUserSex;
    }
    public OnLineUser setOnLineUserSex(java.lang.Long onLineUserSex) {
        int size0 = Tools.textLength(onLineUserSex);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserSex最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserSex);
        }
        this.onLineUserSex = onLineUserSex;
        return this;
    }
    public OnLineUser setOnLineUserSexUnsafe(java.lang.Long onLineUserSex) {
        this.onLineUserSex = onLineUserSex;
        return this;
    }
    public java.lang.Long getOnLineUserRsex() {
        return onLineUserRsex;
    }
    public OnLineUser setOnLineUserRsex(java.lang.Long onLineUserRsex) {
        int size0 = Tools.textLength(onLineUserRsex);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserRsex最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserRsex);
        }
        this.onLineUserRsex = onLineUserRsex;
        return this;
    }
    public OnLineUser setOnLineUserRsexUnsafe(java.lang.Long onLineUserRsex) {
        this.onLineUserRsex = onLineUserRsex;
        return this;
    }
    public java.lang.Long getOnLineUserPrice() {
        return onLineUserPrice;
    }
    public OnLineUser setOnLineUserPrice(java.lang.Long onLineUserPrice) {
        int size0 = Tools.textLength(onLineUserPrice);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserPrice最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserPrice);
        }
        this.onLineUserPrice = onLineUserPrice;
        return this;
    }
    public OnLineUser setOnLineUserPriceUnsafe(java.lang.Long onLineUserPrice) {
        this.onLineUserPrice = onLineUserPrice;
        return this;
    }
    public java.lang.String getOnLineUserNike() {
        return onLineUserNike;
    }
    public OnLineUser setOnLineUserNike(java.lang.String onLineUserNike) {
        int size0 = Tools.textLength(onLineUserNike);
        if (size0 > 100) {
            throw new RuntimeException("OnLineUser.onLineUserNike最大长度为:100,实际长度为:"+ size0 +",数据为:"+onLineUserNike);
        }
        this.onLineUserNike = onLineUserNike;
        return this;
    }
    public OnLineUser setOnLineUserNikeUnsafe(java.lang.String onLineUserNike) {
        this.onLineUserNike = onLineUserNike;
        return this;
    }
    public java.lang.Long getOnLineUserSecId() {
        return onLineUserSecId;
    }
    public OnLineUser setOnLineUserSecId(java.lang.Long onLineUserSecId) {
        int size0 = Tools.textLength(onLineUserSecId);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserSecId最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserSecId);
        }
        this.onLineUserSecId = onLineUserSecId;
        return this;
    }
    public OnLineUser setOnLineUserSecIdUnsafe(java.lang.Long onLineUserSecId) {
        this.onLineUserSecId = onLineUserSecId;
        return this;
    }
    public java.lang.Long getOnLineUserUid() {
        return onLineUserUid;
    }
    public OnLineUser setOnLineUserUid(java.lang.Long onLineUserUid) {
        int size0 = Tools.textLength(onLineUserUid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserUid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserUid);
        }
        this.onLineUserUid = onLineUserUid;
        return this;
    }
    public OnLineUser setOnLineUserUidUnsafe(java.lang.Long onLineUserUid) {
        this.onLineUserUid = onLineUserUid;
        return this;
    }
    public java.lang.Long getOnLineUserRuid() {
        return onLineUserRuid;
    }
    public OnLineUser setOnLineUserRuid(java.lang.Long onLineUserRuid) {
        int size0 = Tools.textLength(onLineUserRuid);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserRuid最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserRuid);
        }
        this.onLineUserRuid = onLineUserRuid;
        return this;
    }
    public OnLineUser setOnLineUserRuidUnsafe(java.lang.Long onLineUserRuid) {
        this.onLineUserRuid = onLineUserRuid;
        return this;
    }
    public java.lang.Long getOnLineUserReadNum() {
        return onLineUserReadNum;
    }
    public OnLineUser setOnLineUserReadNum(java.lang.Long onLineUserReadNum) {
        int size0 = Tools.textLength(onLineUserReadNum);
        if (size0 > 19) {
            throw new RuntimeException("OnLineUser.onLineUserReadNum最大长度为:19,实际长度为:"+ size0 +",数据为:"+onLineUserReadNum);
        }
        this.onLineUserReadNum = onLineUserReadNum;
        return this;
    }
    public OnLineUser setOnLineUserReadNumUnsafe(java.lang.Long onLineUserReadNum) {
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
