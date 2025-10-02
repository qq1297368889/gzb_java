package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysUsersLoginLogDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_users_login_log",desc="sysUsersLoginLog")
public class SysUsersLoginLog implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
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
    private Object data;
   public SysUsersLoginLog() {}

    public SysUsersLoginLog(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysUsersLoginLog(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysUsersLoginLog(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysUsersLoginLog(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysUsersLoginLogId != null) {
            sb.append("\"sysUsersLoginLogId\":\"").append(sysUsersLoginLogId).append("\",");
        }
        if (this.sysUsersLoginLogIp != null) {
            sb.append("\"sysUsersLoginLogIp\":\"").append(sysUsersLoginLogIp).append("\",");
        }
        if (this.sysUsersLoginLogDesc != null) {
            sb.append("\"sysUsersLoginLogDesc\":\"").append(sysUsersLoginLogDesc).append("\",");
        }
        if (this.sysUsersLoginLogTime != null) {
            sb.append("\"sysUsersLoginLogTime\":\"").append(sysUsersLoginLogTime).append("\",");
        }
        if (this.sysUsersLoginLogUid != null) {
            sb.append("\"sysUsersLoginLogUid\":\"").append(sysUsersLoginLogUid).append("\",");
        }
        if (this.sysUsersLoginLogToken != null) {
            sb.append("\"sysUsersLoginLogToken\":\"").append(sysUsersLoginLogToken).append("\",");
        }
        if (this.sysUsersLoginLogMac != null) {
            sb.append("\"sysUsersLoginLogMac\":\"").append(sysUsersLoginLogMac).append("\",");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":").append(Tools.toJson(entry.getValue())).append(",");
            }
        }else if(this.data != null){
            sb.append("\"").append(dataName).append("\":").append(Tools.toJson(this.data)).append(",");
        }
        if (sb.length()>1) {
            sb.delete(sb.length()-1, sb.length());
        }
       return sb.append("}").toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysUsersLoginLogId", sysUsersLoginLogId);
        result.set("sysUsersLoginLogIp", sysUsersLoginLogIp);
        result.set("sysUsersLoginLogDesc", sysUsersLoginLogDesc);
        result.set("sysUsersLoginLogTime", sysUsersLoginLogTime);
        result.set("sysUsersLoginLogUid", sysUsersLoginLogUid);
        result.set("sysUsersLoginLogToken", sysUsersLoginLogToken);
        result.set("sysUsersLoginLogMac", sysUsersLoginLogMac);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysUsersLoginLogId=result.getLong("sysUsersLoginLogId", null);
        this.sysUsersLoginLogIp=result.getString("sysUsersLoginLogIp", null);
        this.sysUsersLoginLogDesc=result.getString("sysUsersLoginLogDesc", null);
        this.sysUsersLoginLogTime=result.getString("sysUsersLoginLogTime", null);
        this.sysUsersLoginLogUid=result.getLong("sysUsersLoginLogUid", null);
        this.sysUsersLoginLogToken=result.getString("sysUsersLoginLogToken", null);
        this.sysUsersLoginLogMac=result.getString("sysUsersLoginLogMac", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_users_login_log_id")) {
            temp=resultSet.getString("sys_users_login_log_id");
            if (temp!=null) {
                this.sysUsersLoginLogId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_ip")) {
            temp=resultSet.getString("sys_users_login_log_ip");
            if (temp!=null) {
                this.sysUsersLoginLogIp=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_desc")) {
            temp=resultSet.getString("sys_users_login_log_desc");
            if (temp!=null) {
                this.sysUsersLoginLogDesc=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_time")) {
            temp=resultSet.getString("sys_users_login_log_time");
            if (temp!=null) {
                this.sysUsersLoginLogTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_uid")) {
            temp=resultSet.getString("sys_users_login_log_uid");
            if (temp!=null) {
                this.sysUsersLoginLogUid=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_token")) {
            temp=resultSet.getString("sys_users_login_log_token");
            if (temp!=null) {
                this.sysUsersLoginLogToken=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_login_log_mac")) {
            temp=resultSet.getString("sys_users_login_log_mac");
            if (temp!=null) {
                this.sysUsersLoginLogMac=java.lang.String.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysUsersLoginLogId() {
        return sysUsersLoginLogId;
    }
    public SysUsersLoginLog setSysUsersLoginLogId(java.lang.Long sysUsersLoginLogId) {
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
    public SysUsersLoginLog setList(List<?> data) {
        this.data = data;
        return this;
    }

    public List<?> getList() {
          if (data instanceof List) {
                return (List<?>) data;
          }
        return null;
    }
    public Map<String, Object> getMap() {
          if (data instanceof Map) {
                return (Map<String, Object>) data;
          }
        return null;
    }

    public SysUsersLoginLog setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysUsersLoginLog putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysUsersLoginLog setData(Object data) {
        this.data = data;
        return this;
    }}
