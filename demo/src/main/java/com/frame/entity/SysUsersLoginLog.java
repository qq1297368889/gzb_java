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
    @EntityAttribute(key=true,size = 19,name="sys_users_login_log_id",desc="",type="java.lang.Long")
    private java.lang.Long sysUsersLoginLogId;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_ip",desc="",type="java.lang.String")
    private java.lang.String sysUsersLoginLogIp;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_desc",desc="",type="java.lang.String")
    private java.lang.String sysUsersLoginLogDesc;
    @EntityAttribute(key=false,size = 19,name="sys_users_login_log_time",desc="",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersLoginLogTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_login_log_uid",desc="",type="java.lang.Long")
    private java.lang.Long sysUsersLoginLogUid;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_token",desc="",type="java.lang.String")
    private java.lang.String sysUsersLoginLogToken;
    @EntityAttribute(key=false,size = 100,name="sys_users_login_log_mac",desc="",type="java.lang.String")
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
        StringBuilder sb = new StringBuilder(205);
       boolean app01=false;
        sb.append("{");
        if (this.sysUsersLoginLogId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogId\":\"").append(sysUsersLoginLogId).append("\"");
        }
        if (this.sysUsersLoginLogIp != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogIp\":");
            sb.append(Tools.toJson(sysUsersLoginLogIp));        }
        if (this.sysUsersLoginLogDesc != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogDesc\":");
            sb.append(Tools.toJson(sysUsersLoginLogDesc));        }
        if (this.sysUsersLoginLogTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogTime\":");
            sb.append(Tools.toJson(sysUsersLoginLogTime));        }
        if (this.sysUsersLoginLogUid != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogUid\":\"").append(sysUsersLoginLogUid).append("\"");
        }
        if (this.sysUsersLoginLogToken != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogToken\":");
            sb.append(Tools.toJson(sysUsersLoginLogToken));        }
        if (this.sysUsersLoginLogMac != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersLoginLogMac\":");
            sb.append(Tools.toJson(sysUsersLoginLogMac));        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                if(app01){sb.append(",");}app01=true;
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(Tools.toJson(entry.getValue()));
            }
        }else if(this.data != null){
            if(app01){sb.append(",");}app01=true;
            sb.append("\"").append(Config.get("json.entity.data","data")).append("\":");
            sb.append(Tools.toJson(this.data));
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
        result.set(Config.get("json.entity.data","data"), data);
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
        this.sysUsersLoginLogTime=result.getLocalDateTime("sysUsersLoginLogTime", null);
        this.sysUsersLoginLogUid=result.getLong("sysUsersLoginLogUid", null);
        this.sysUsersLoginLogToken=result.getString("sysUsersLoginLogToken", null);
        this.sysUsersLoginLogMac=result.getString("sysUsersLoginLogMac", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
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
            throw new gzb.exception.GzbException0("SysUsersLoginLog.sysUsersLoginLogIp最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogIp);
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
            throw new gzb.exception.GzbException0("SysUsersLoginLog.sysUsersLoginLogDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogDesc);
        }
        this.sysUsersLoginLogDesc = sysUsersLoginLogDesc;
        return this;
    }
    public SysUsersLoginLog setSysUsersLoginLogDescUnsafe(java.lang.String sysUsersLoginLogDesc) {
        this.sysUsersLoginLogDesc = sysUsersLoginLogDesc;
        return this;
    }
    public java.time.LocalDateTime getSysUsersLoginLogTime() {
        return sysUsersLoginLogTime;
    }
    public SysUsersLoginLog setSysUsersLoginLogTime(java.time.LocalDateTime sysUsersLoginLogTime) {
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
            throw new gzb.exception.GzbException0("SysUsersLoginLog.sysUsersLoginLogToken最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogToken);
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
            throw new gzb.exception.GzbException0("SysUsersLoginLog.sysUsersLoginLogMac最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersLoginLogMac);
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
            // 自动初始化
            this.data = new HashMap<>();
        } else if (!(this.data instanceof Map)) {
            // 无法转换，抛出业务异常
            throw new gzb.exception.GzbException0("无法将"+this.data+" 转换为MAP");
        }
        // 安全地进行 put 操作 (可能需要抑制一下警告)
        @SuppressWarnings("unchecked")
        Map<String, Object> mapData = (Map<String, Object>) this.data;
        mapData.put(key, value);
        return this;
    }
    public Object getData() {
        return data;
    }
    public SysUsersLoginLog setData(Object data) {
        this.data = data;
        return this;
    }
}
