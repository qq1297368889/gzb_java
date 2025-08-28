package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.LogInfoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="log_info",desc="logInfo")
public class LogInfo implements Serializable{
    @EntityAttribute(key=true,size = 19,name="log_info_id",desc="logInfoId")
    private java.lang.Long logInfoId;
    @EntityAttribute(key=false,size = 19,name="log_info_type",desc="logInfoType")
    private java.lang.Long logInfoType;
    @EntityAttribute(key=false,size = 19,name="log_info_time",desc="logInfoTime")
    private java.lang.String logInfoTime;
    @EntityAttribute(key=false,size = 100,name="log_info_ip",desc="logInfoIp")
    private java.lang.String logInfoIp;
    @EntityAttribute(key=false,size = 19,name="log_info_uid",desc="logInfoUid")
    private java.lang.Long logInfoUid;
    @EntityAttribute(key=false,size = 715827882,name="log_info_msg",desc="logInfoMsg")
    private java.lang.String logInfoMsg;
    @EntityAttribute(key=false,size = 100,name="log_info_desc",desc="logInfoDesc")
    private java.lang.String logInfoDesc;
    @EntityAttribute(key=false,size = 19,name="log_info_state",desc="logInfoState")
    private java.lang.Long logInfoState;
    private List<?> list;
    public LogInfo() {}

    public LogInfo(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public LogInfo(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("logInfoId");
        if (str!=null && !str.isEmpty()) {
            setLogInfoId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("logInfoType");
        if (str!=null && !str.isEmpty()) {
            setLogInfoType(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("logInfoTime");
        if (str!=null && !str.isEmpty()) {
            setLogInfoTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("logInfoIp");
        if (str!=null && !str.isEmpty()) {
            setLogInfoIp(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("logInfoUid");
        if (str!=null && !str.isEmpty()) {
            setLogInfoUid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("logInfoMsg");
        if (str!=null && !str.isEmpty()) {
            setLogInfoMsg(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("logInfoDesc");
        if (str!=null && !str.isEmpty()) {
            setLogInfoDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("logInfoState");
        if (str!=null && !str.isEmpty()) {
            setLogInfoState(java.lang.Long.valueOf(str));
        }
    }

    public LogInfo(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public LogInfo(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.save(this);
    }
    public int delete(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.delete(this);
    }
    public int update(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.update(this);
    }
    public int saveAsync(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.saveAsync(this);
    }
    public int deleteAsync(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.deleteAsync(this);
    }
    public int updateAsync(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.updateAsync(this);
    }
    public List<LogInfo> query(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.query(this);
    }
    public List<LogInfo> query(LogInfoDao logInfoDao,int page,int size) throws Exception {
        return logInfoDao.query(this,page,size);
    }
    public List<LogInfo> query(LogInfoDao logInfoDao,String sortField,String sortType,int page,int size) throws Exception {
        return logInfoDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(LogInfoDao logInfoDao, String sortField, String sortType, int page, int size) throws Exception {
        return logInfoDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(LogInfoDao logInfoDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return logInfoDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public LogInfo find(LogInfoDao logInfoDao) throws Exception {
        return logInfoDao.find(this);
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
        json.put("logInfoId", getLogInfoId());
        json.put("logInfoType", getLogInfoType());
        json.put("logInfoTime", getLogInfoTime());
        json.put("logInfoIp", getLogInfoIp());
        json.put("logInfoUid", getLogInfoUid());
        json.put("logInfoMsg", getLogInfoMsg());
        json.put("logInfoDesc", getLogInfoDesc());
        json.put("logInfoState", getLogInfoState());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getLogInfoId() {
        return logInfoId;
    }
    public LogInfo setLogInfoId(java.lang.Long logInfoId) {
        int size0 = Tools.textLength(logInfoId);
        if (size0 > 19) {
            throw new RuntimeException("LogInfo.logInfoId最大长度为:19,实际长度为:"+ size0 +",数据为:"+logInfoId);
        }
        this.logInfoId = logInfoId;
        return this;
    }
    public LogInfo setLogInfoIdUnsafe(java.lang.Long logInfoId) {
        this.logInfoId = logInfoId;
        return this;
    }
    public java.lang.Long getLogInfoType() {
        return logInfoType;
    }
    public LogInfo setLogInfoType(java.lang.Long logInfoType) {
        int size0 = Tools.textLength(logInfoType);
        if (size0 > 19) {
            throw new RuntimeException("LogInfo.logInfoType最大长度为:19,实际长度为:"+ size0 +",数据为:"+logInfoType);
        }
        this.logInfoType = logInfoType;
        return this;
    }
    public LogInfo setLogInfoTypeUnsafe(java.lang.Long logInfoType) {
        this.logInfoType = logInfoType;
        return this;
    }
    public java.lang.String getLogInfoTime() {
        return logInfoTime;
    }
    public LogInfo setLogInfoTime(java.lang.String logInfoTime) {
        int size0 = Tools.textLength(logInfoTime);
        if (size0 > 19) {
            throw new RuntimeException("LogInfo.logInfoTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+logInfoTime);
        }
        this.logInfoTime = logInfoTime;
        return this;
    }
    public LogInfo setLogInfoTimeUnsafe(java.lang.String logInfoTime) {
        this.logInfoTime = logInfoTime;
        return this;
    }
    public java.lang.String getLogInfoIp() {
        return logInfoIp;
    }
    public LogInfo setLogInfoIp(java.lang.String logInfoIp) {
        int size0 = Tools.textLength(logInfoIp);
        if (size0 > 100) {
            throw new RuntimeException("LogInfo.logInfoIp最大长度为:100,实际长度为:"+ size0 +",数据为:"+logInfoIp);
        }
        this.logInfoIp = logInfoIp;
        return this;
    }
    public LogInfo setLogInfoIpUnsafe(java.lang.String logInfoIp) {
        this.logInfoIp = logInfoIp;
        return this;
    }
    public java.lang.Long getLogInfoUid() {
        return logInfoUid;
    }
    public LogInfo setLogInfoUid(java.lang.Long logInfoUid) {
        int size0 = Tools.textLength(logInfoUid);
        if (size0 > 19) {
            throw new RuntimeException("LogInfo.logInfoUid最大长度为:19,实际长度为:"+ size0 +",数据为:"+logInfoUid);
        }
        this.logInfoUid = logInfoUid;
        return this;
    }
    public LogInfo setLogInfoUidUnsafe(java.lang.Long logInfoUid) {
        this.logInfoUid = logInfoUid;
        return this;
    }
    public java.lang.String getLogInfoMsg() {
        return logInfoMsg;
    }
    public LogInfo setLogInfoMsg(java.lang.String logInfoMsg) {
        int size0 = Tools.textLength(logInfoMsg);
        if (size0 > 715827882) {
            throw new RuntimeException("LogInfo.logInfoMsg最大长度为:715827882,实际长度为:"+ size0 +",数据为:"+logInfoMsg);
        }
        this.logInfoMsg = logInfoMsg;
        return this;
    }
    public LogInfo setLogInfoMsgUnsafe(java.lang.String logInfoMsg) {
        this.logInfoMsg = logInfoMsg;
        return this;
    }
    public java.lang.String getLogInfoDesc() {
        return logInfoDesc;
    }
    public LogInfo setLogInfoDesc(java.lang.String logInfoDesc) {
        int size0 = Tools.textLength(logInfoDesc);
        if (size0 > 100) {
            throw new RuntimeException("LogInfo.logInfoDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+logInfoDesc);
        }
        this.logInfoDesc = logInfoDesc;
        return this;
    }
    public LogInfo setLogInfoDescUnsafe(java.lang.String logInfoDesc) {
        this.logInfoDesc = logInfoDesc;
        return this;
    }
    public java.lang.Long getLogInfoState() {
        return logInfoState;
    }
    public LogInfo setLogInfoState(java.lang.Long logInfoState) {
        int size0 = Tools.textLength(logInfoState);
        if (size0 > 19) {
            throw new RuntimeException("LogInfo.logInfoState最大长度为:19,实际长度为:"+ size0 +",数据为:"+logInfoState);
        }
        this.logInfoState = logInfoState;
        return this;
    }
    public LogInfo setLogInfoStateUnsafe(java.lang.Long logInfoState) {
        this.logInfoState = logInfoState;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
