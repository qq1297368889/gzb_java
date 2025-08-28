package com.authorizationSystem.entity;
import com.authorizationSystem.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.authorizationSystem.dao.ApplicationRechargeCardDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="application_recharge_card",desc="applicationRechargeCard")
public class ApplicationRechargeCard implements Serializable{
    @EntityAttribute(key=true,size = 19,name="application_recharge_card_id",desc="applicationRechargeCardId")
    private java.lang.Long applicationRechargeCardId;
    @EntityAttribute(key=false,size = 100,name="application_recharge_card_key",desc="applicationRechargeCardKey")
    private java.lang.String applicationRechargeCardKey;
    @EntityAttribute(key=false,size = 64,name="application_recharge_card_val",desc="applicationRechargeCardVal")
    private java.lang.String applicationRechargeCardVal;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_time",desc="applicationRechargeCardTime")
    private java.lang.String applicationRechargeCardTime;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_use_time",desc="applicationRechargeCardUseTime")
    private java.lang.String applicationRechargeCardUseTime;
    @EntityAttribute(key=false,size = 255,name="application_recharge_card_use_ip",desc="applicationRechargeCardUseIp")
    private java.lang.String applicationRechargeCardUseIp;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_state",desc="applicationRechargeCardState")
    private java.lang.Long applicationRechargeCardState;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_aid",desc="applicationRechargeCardAid")
    private java.lang.Long applicationRechargeCardAid;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_start_time",desc="applicationRechargeCardStartTime")
    private java.lang.String applicationRechargeCardStartTime;
    @EntityAttribute(key=false,size = 19,name="application_recharge_card_end_time",desc="applicationRechargeCardEndTime")
    private java.lang.String applicationRechargeCardEndTime;
    private List<?> list;
    public ApplicationRechargeCard() {}

    public ApplicationRechargeCard(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public ApplicationRechargeCard(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("applicationRechargeCardId");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardKey");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardKey(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardVal");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardVal(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardTime");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardUseTime");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardUseTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardUseIp");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardUseIp(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardState");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardState(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardAid");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardAid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardStartTime");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardStartTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationRechargeCardEndTime");
        if (str!=null && !str.isEmpty()) {
            setApplicationRechargeCardEndTime(java.lang.String.valueOf(str));
        }
    }

    public ApplicationRechargeCard(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public ApplicationRechargeCard(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.save(this);
    }
    public int delete(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.delete(this);
    }
    public int update(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.update(this);
    }
    public int saveAsync(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.saveAsync(this);
    }
    public int deleteAsync(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.deleteAsync(this);
    }
    public int updateAsync(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.updateAsync(this);
    }
    public List<ApplicationRechargeCard> query(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.query(this);
    }
    public List<ApplicationRechargeCard> query(ApplicationRechargeCardDao applicationRechargeCardDao,int page,int size) throws Exception {
        return applicationRechargeCardDao.query(this,page,size);
    }
    public List<ApplicationRechargeCard> query(ApplicationRechargeCardDao applicationRechargeCardDao,String sortField,String sortType,int page,int size) throws Exception {
        return applicationRechargeCardDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(ApplicationRechargeCardDao applicationRechargeCardDao, String sortField, String sortType, int page, int size) throws Exception {
        return applicationRechargeCardDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(ApplicationRechargeCardDao applicationRechargeCardDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return applicationRechargeCardDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public ApplicationRechargeCard find(ApplicationRechargeCardDao applicationRechargeCardDao) throws Exception {
        return applicationRechargeCardDao.find(this);
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
        json.put("applicationRechargeCardId", getApplicationRechargeCardId());
        json.put("applicationRechargeCardKey", getApplicationRechargeCardKey());
        json.put("applicationRechargeCardVal", getApplicationRechargeCardVal());
        json.put("applicationRechargeCardTime", getApplicationRechargeCardTime());
        json.put("applicationRechargeCardUseTime", getApplicationRechargeCardUseTime());
        json.put("applicationRechargeCardUseIp", getApplicationRechargeCardUseIp());
        json.put("applicationRechargeCardState", getApplicationRechargeCardState());
        json.put("applicationRechargeCardAid", getApplicationRechargeCardAid());
        json.put("applicationRechargeCardStartTime", getApplicationRechargeCardStartTime());
        json.put("applicationRechargeCardEndTime", getApplicationRechargeCardEndTime());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getApplicationRechargeCardId() {
        return applicationRechargeCardId;
    }
    public ApplicationRechargeCard setApplicationRechargeCardId(java.lang.Long applicationRechargeCardId) {
        int size0 = Tools.textLength(applicationRechargeCardId);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardId最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardId);
        }
        this.applicationRechargeCardId = applicationRechargeCardId;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardIdUnsafe(java.lang.Long applicationRechargeCardId) {
        this.applicationRechargeCardId = applicationRechargeCardId;
        return this;
    }
    public java.lang.String getApplicationRechargeCardKey() {
        return applicationRechargeCardKey;
    }
    public ApplicationRechargeCard setApplicationRechargeCardKey(java.lang.String applicationRechargeCardKey) {
        int size0 = Tools.textLength(applicationRechargeCardKey);
        if (size0 > 100) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardKey最大长度为:100,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardKey);
        }
        this.applicationRechargeCardKey = applicationRechargeCardKey;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardKeyUnsafe(java.lang.String applicationRechargeCardKey) {
        this.applicationRechargeCardKey = applicationRechargeCardKey;
        return this;
    }
    public java.lang.String getApplicationRechargeCardVal() {
        return applicationRechargeCardVal;
    }
    public ApplicationRechargeCard setApplicationRechargeCardVal(java.lang.String applicationRechargeCardVal) {
        int size0 = Tools.textLength(applicationRechargeCardVal);
        if (size0 > 64) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardVal最大长度为:64,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardVal);
        }
        this.applicationRechargeCardVal = applicationRechargeCardVal;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardValUnsafe(java.lang.String applicationRechargeCardVal) {
        this.applicationRechargeCardVal = applicationRechargeCardVal;
        return this;
    }
    public java.lang.String getApplicationRechargeCardTime() {
        return applicationRechargeCardTime;
    }
    public ApplicationRechargeCard setApplicationRechargeCardTime(java.lang.String applicationRechargeCardTime) {
        int size0 = Tools.textLength(applicationRechargeCardTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardTime);
        }
        this.applicationRechargeCardTime = applicationRechargeCardTime;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardTimeUnsafe(java.lang.String applicationRechargeCardTime) {
        this.applicationRechargeCardTime = applicationRechargeCardTime;
        return this;
    }
    public java.lang.String getApplicationRechargeCardUseTime() {
        return applicationRechargeCardUseTime;
    }
    public ApplicationRechargeCard setApplicationRechargeCardUseTime(java.lang.String applicationRechargeCardUseTime) {
        int size0 = Tools.textLength(applicationRechargeCardUseTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardUseTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardUseTime);
        }
        this.applicationRechargeCardUseTime = applicationRechargeCardUseTime;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardUseTimeUnsafe(java.lang.String applicationRechargeCardUseTime) {
        this.applicationRechargeCardUseTime = applicationRechargeCardUseTime;
        return this;
    }
    public java.lang.String getApplicationRechargeCardUseIp() {
        return applicationRechargeCardUseIp;
    }
    public ApplicationRechargeCard setApplicationRechargeCardUseIp(java.lang.String applicationRechargeCardUseIp) {
        int size0 = Tools.textLength(applicationRechargeCardUseIp);
        if (size0 > 255) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardUseIp最大长度为:255,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardUseIp);
        }
        this.applicationRechargeCardUseIp = applicationRechargeCardUseIp;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardUseIpUnsafe(java.lang.String applicationRechargeCardUseIp) {
        this.applicationRechargeCardUseIp = applicationRechargeCardUseIp;
        return this;
    }
    public java.lang.Long getApplicationRechargeCardState() {
        return applicationRechargeCardState;
    }
    public ApplicationRechargeCard setApplicationRechargeCardState(java.lang.Long applicationRechargeCardState) {
        int size0 = Tools.textLength(applicationRechargeCardState);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardState最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardState);
        }
        this.applicationRechargeCardState = applicationRechargeCardState;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardStateUnsafe(java.lang.Long applicationRechargeCardState) {
        this.applicationRechargeCardState = applicationRechargeCardState;
        return this;
    }
    public java.lang.Long getApplicationRechargeCardAid() {
        return applicationRechargeCardAid;
    }
    public ApplicationRechargeCard setApplicationRechargeCardAid(java.lang.Long applicationRechargeCardAid) {
        int size0 = Tools.textLength(applicationRechargeCardAid);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardAid);
        }
        this.applicationRechargeCardAid = applicationRechargeCardAid;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardAidUnsafe(java.lang.Long applicationRechargeCardAid) {
        this.applicationRechargeCardAid = applicationRechargeCardAid;
        return this;
    }
    public java.lang.String getApplicationRechargeCardStartTime() {
        return applicationRechargeCardStartTime;
    }
    public ApplicationRechargeCard setApplicationRechargeCardStartTime(java.lang.String applicationRechargeCardStartTime) {
        int size0 = Tools.textLength(applicationRechargeCardStartTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardStartTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardStartTime);
        }
        this.applicationRechargeCardStartTime = applicationRechargeCardStartTime;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardStartTimeUnsafe(java.lang.String applicationRechargeCardStartTime) {
        this.applicationRechargeCardStartTime = applicationRechargeCardStartTime;
        return this;
    }
    public java.lang.String getApplicationRechargeCardEndTime() {
        return applicationRechargeCardEndTime;
    }
    public ApplicationRechargeCard setApplicationRechargeCardEndTime(java.lang.String applicationRechargeCardEndTime) {
        int size0 = Tools.textLength(applicationRechargeCardEndTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationRechargeCard.applicationRechargeCardEndTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationRechargeCardEndTime);
        }
        this.applicationRechargeCardEndTime = applicationRechargeCardEndTime;
        return this;
    }
    public ApplicationRechargeCard setApplicationRechargeCardEndTimeUnsafe(java.lang.String applicationRechargeCardEndTime) {
        this.applicationRechargeCardEndTime = applicationRechargeCardEndTime;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
