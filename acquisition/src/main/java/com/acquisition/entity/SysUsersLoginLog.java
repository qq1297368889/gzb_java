package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.SysUsersLoginLogDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_users_login_log",desc="sysUsersLoginLog")
public class SysUsersLoginLog implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_users_login_log_id",desc="sysUsersLoginLogId")
    private java.lang.Long sysUsersLoginLogId;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_ip",desc="sysUsersLoginLogIp")
    private java.lang.String sysUsersLoginLogIp;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_desc",desc="sysUsersLoginLogDesc")
    private java.lang.String sysUsersLoginLogDesc;
    @EntityAttribute(key=false,size = 19,name="sys_users_login_log_time",desc="sysUsersLoginLogTime")
    private java.lang.String sysUsersLoginLogTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_login_log_uid",desc="sysUsersLoginLogUid")
    private java.lang.Long sysUsersLoginLogUid;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_token",desc="sysUsersLoginLogToken")
    private java.lang.String sysUsersLoginLogToken;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_mac",desc="sysUsersLoginLogMac")
    private java.lang.String sysUsersLoginLogMac;
    private List<?> list;
    public SysUsersLoginLog() {}

    public SysUsersLoginLog(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysUsersLoginLog(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysUsersLoginLogId");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogIp");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogIp(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogDesc");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogTime");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogUid");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogUid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogToken");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogToken(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysUsersLoginLogMac");
        if (str!=null && !str.isEmpty()) {
            setSysUsersLoginLogMac(java.lang.String.valueOf(str));
        }
    }

    public SysUsersLoginLog(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysUsersLoginLog(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.save(this);
    }
    public int delete(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.delete(this);
    }
    public int update(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.update(this);
    }
    public int saveAsync(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.saveAsync(this);
    }
    public int deleteAsync(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.deleteAsync(this);
    }
    public int updateAsync(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.updateAsync(this);
    }
    public List<SysUsersLoginLog> query(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.query(this);
    }
    public List<SysUsersLoginLog> query(SysUsersLoginLogDao sysUsersLoginLogDao,int page,int size) throws Exception {
        return sysUsersLoginLogDao.query(this,page,size);
    }
    public List<SysUsersLoginLog> query(SysUsersLoginLogDao sysUsersLoginLogDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysUsersLoginLogDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysUsersLoginLogDao sysUsersLoginLogDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysUsersLoginLogDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysUsersLoginLogDao sysUsersLoginLogDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysUsersLoginLogDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysUsersLoginLog find(SysUsersLoginLogDao sysUsersLoginLogDao) throws Exception {
        return sysUsersLoginLogDao.find(this);
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
        json.put("sysUsersLoginLogId", getSysUsersLoginLogId());
        json.put("sysUsersLoginLogIp", getSysUsersLoginLogIp());
        json.put("sysUsersLoginLogDesc", getSysUsersLoginLogDesc());
        json.put("sysUsersLoginLogTime", getSysUsersLoginLogTime());
        json.put("sysUsersLoginLogUid", getSysUsersLoginLogUid());
        json.put("sysUsersLoginLogToken", getSysUsersLoginLogToken());
        json.put("sysUsersLoginLogMac", getSysUsersLoginLogMac());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysUsersLoginLogId() {
        return sysUsersLoginLogId;
    }
    public SysUsersLoginLog setSysUsersLoginLogId(java.lang.Long sysUsersLoginLogId) {
        int size0 = Tools.textLength(sysUsersLoginLogId);
        if (size0 > 19) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogId);
        }
        this.sysUsersLoginLogId = sysUsersLoginLogId;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogIdUnsafe(java.lang.Long sysUsersLoginLogId) {
        this.sysUsersLoginLogId = sysUsersLoginLogId;
        return this;
    }
    public java.lang.String getSysUsersLoginLogIp() {
        return sysUsersLoginLogIp;
    }
    public SysUsersLoginLog setSysUsersLoginLogIp(java.lang.String sysUsersLoginLogIp) {
        int size0 = Tools.textLength(sysUsersLoginLogIp);
        if (size0 > 100) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogIp最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogIp);
        }
        this.sysUsersLoginLogIp = sysUsersLoginLogIp;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogIpUnsafe(java.lang.String sysUsersLoginLogIp) {
        this.sysUsersLoginLogIp = sysUsersLoginLogIp;
        return this;
    }
    public java.lang.String getSysUsersLoginLogDesc() {
        return sysUsersLoginLogDesc;
    }
    public SysUsersLoginLog setSysUsersLoginLogDesc(java.lang.String sysUsersLoginLogDesc) {
        int size0 = Tools.textLength(sysUsersLoginLogDesc);
        if (size0 > 100) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogDesc);
        }
        this.sysUsersLoginLogDesc = sysUsersLoginLogDesc;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogDescUnsafe(java.lang.String sysUsersLoginLogDesc) {
        this.sysUsersLoginLogDesc = sysUsersLoginLogDesc;
        return this;
    }
    public java.lang.String getSysUsersLoginLogTime() {
        return sysUsersLoginLogTime;
    }
    public SysUsersLoginLog setSysUsersLoginLogTime(java.lang.String sysUsersLoginLogTime) {
        int size0 = Tools.textLength(sysUsersLoginLogTime);
        if (size0 > 19) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogTime);
        }
        this.sysUsersLoginLogTime = sysUsersLoginLogTime;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogTimeUnsafe(java.lang.String sysUsersLoginLogTime) {
        this.sysUsersLoginLogTime = sysUsersLoginLogTime;
        return this;
    }
    public java.lang.Long getSysUsersLoginLogUid() {
        return sysUsersLoginLogUid;
    }
    public SysUsersLoginLog setSysUsersLoginLogUid(java.lang.Long sysUsersLoginLogUid) {
        int size0 = Tools.textLength(sysUsersLoginLogUid);
        if (size0 > 19) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogUid最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogUid);
        }
        this.sysUsersLoginLogUid = sysUsersLoginLogUid;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogUidUnsafe(java.lang.Long sysUsersLoginLogUid) {
        this.sysUsersLoginLogUid = sysUsersLoginLogUid;
        return this;
    }
    public java.lang.String getSysUsersLoginLogToken() {
        return sysUsersLoginLogToken;
    }
    public SysUsersLoginLog setSysUsersLoginLogToken(java.lang.String sysUsersLoginLogToken) {
        int size0 = Tools.textLength(sysUsersLoginLogToken);
        if (size0 > 100) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogToken最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogToken);
        }
        this.sysUsersLoginLogToken = sysUsersLoginLogToken;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogTokenUnsafe(java.lang.String sysUsersLoginLogToken) {
        this.sysUsersLoginLogToken = sysUsersLoginLogToken;
        return this;
    }
    public java.lang.String getSysUsersLoginLogMac() {
        return sysUsersLoginLogMac;
    }
    public SysUsersLoginLog setSysUsersLoginLogMac(java.lang.String sysUsersLoginLogMac) {
        int size0 = Tools.textLength(sysUsersLoginLogMac);
        if (size0 > 100) {
            throw new RuntimeException("SysUsersLoginLog.sysUsersLoginLogMac最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogMac);
        }
        this.sysUsersLoginLogMac = sysUsersLoginLogMac;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogMacUnsafe(java.lang.String sysUsersLoginLogMac) {
        this.sysUsersLoginLogMac = sysUsersLoginLogMac;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
