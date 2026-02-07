package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysUsersDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_users",desc="sysUsers")
public class SysUsers implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_users_id",desc="ID",type="java.lang.Long")
    private java.lang.Long sysUsersId;
    @EntityAttribute(key=false,size = 32,name="sys_users_acc",desc="账号",type="java.lang.String")
    private java.lang.String sysUsersAcc;
    @EntityAttribute(key=false,size = 32,name="sys_users_pwd",desc="密码",type="java.lang.String")
    private java.lang.String sysUsersPwd;
    @EntityAttribute(key=false,size = 11,name="sys_users_phone",desc="手机",type="java.lang.String")
    private java.lang.String sysUsersPhone;
    @EntityAttribute(key=false,size = 100,name="sys_users_open_id",desc="三方",type="java.lang.String")
    private java.lang.String sysUsersOpenId;
    @EntityAttribute(key=false,size = 19,name="sys_users_status",desc="状态",type="java.lang.Long")
    private java.lang.Long sysUsersStatus;
    @EntityAttribute(key=false,size = 19,name="sys_users_type",desc="类型",type="java.lang.Long")
    private java.lang.Long sysUsersType;
    @EntityAttribute(key=false,size = 19,name="sys_users_reg_time",desc="注册",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersRegTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_start_time",desc="开始",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersStartTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_end_time",desc="结束",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersEndTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_price",desc="金额",type="java.lang.Long")
    private java.lang.Long sysUsersPrice;
    @EntityAttribute(key=false,size = 255,name="sys_users_desc",desc="备注",type="java.lang.String")
    private java.lang.String sysUsersDesc;
    @EntityAttribute(key=false,size = 19,name="sys_users_sup",desc="上级",type="java.lang.Long")
    private java.lang.Long sysUsersSup;
    @EntityAttribute(key=false,size = 255,name="sys_users_mail",desc="邮箱",type="java.lang.String")
    private java.lang.String sysUsersMail;
    @EntityAttribute(key=false,size = 19,name="sys_users_role",desc="角色",type="java.lang.Long")
    private java.lang.Long sysUsersRole;
    private Object data;
   public SysUsers() {}

    public SysUsers(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysUsers(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysUsers(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.save(this);
    }
    public int delete(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.delete(this);
    }
    public int update(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.update(this);
    }
    public int saveAsync(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.saveAsync(this);
    }
    public int deleteAsync(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.deleteAsync(this);
    }
    public int updateAsync(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.updateAsync(this);
    }
    public List<SysUsers> query(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.query(this);
    }
    public List<SysUsers> query(SysUsersDao sysUsersDao,int page,int size) throws Exception {
        return sysUsersDao.query(this,page,size);
    }
    public List<SysUsers> query(SysUsersDao sysUsersDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysUsersDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysUsersDao sysUsersDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysUsersDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysUsersDao sysUsersDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysUsersDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysUsers find(SysUsersDao sysUsersDao) throws Exception {
        return sysUsersDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(342);
       boolean app01=false;
        sb.append("{");
        if (this.sysUsersId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersId\":\"").append(sysUsersId).append("\"");
        }
        if (this.sysUsersAcc != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersAcc\":");
            sb.append(Tools.toJson(sysUsersAcc));        }
        if (this.sysUsersPwd != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersPwd\":");
            sb.append(Tools.toJson(sysUsersPwd));        }
        if (this.sysUsersPhone != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersPhone\":");
            sb.append(Tools.toJson(sysUsersPhone));        }
        if (this.sysUsersOpenId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersOpenId\":");
            sb.append(Tools.toJson(sysUsersOpenId));        }
        if (this.sysUsersStatus != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersStatus\":\"").append(sysUsersStatus).append("\"");
        }
        if (this.sysUsersType != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersType\":\"").append(sysUsersType).append("\"");
        }
        if (this.sysUsersRegTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersRegTime\":");
            sb.append(Tools.toJson(sysUsersRegTime));        }
        if (this.sysUsersStartTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersStartTime\":");
            sb.append(Tools.toJson(sysUsersStartTime));        }
        if (this.sysUsersEndTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersEndTime\":");
            sb.append(Tools.toJson(sysUsersEndTime));        }
        if (this.sysUsersPrice != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersPrice\":\"").append(sysUsersPrice).append("\"");
        }
        if (this.sysUsersDesc != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersDesc\":");
            sb.append(Tools.toJson(sysUsersDesc));        }
        if (this.sysUsersSup != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersSup\":\"").append(sysUsersSup).append("\"");
        }
        if (this.sysUsersMail != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersMail\":");
            sb.append(Tools.toJson(sysUsersMail));        }
        if (this.sysUsersRole != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysUsersRole\":\"").append(sysUsersRole).append("\"");
        }
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
        result.set("sysUsersId", sysUsersId);
        result.set("sysUsersAcc", sysUsersAcc);
        result.set("sysUsersPwd", sysUsersPwd);
        result.set("sysUsersPhone", sysUsersPhone);
        result.set("sysUsersOpenId", sysUsersOpenId);
        result.set("sysUsersStatus", sysUsersStatus);
        result.set("sysUsersType", sysUsersType);
        result.set("sysUsersRegTime", sysUsersRegTime);
        result.set("sysUsersStartTime", sysUsersStartTime);
        result.set("sysUsersEndTime", sysUsersEndTime);
        result.set("sysUsersPrice", sysUsersPrice);
        result.set("sysUsersDesc", sysUsersDesc);
        result.set("sysUsersSup", sysUsersSup);
        result.set("sysUsersMail", sysUsersMail);
        result.set("sysUsersRole", sysUsersRole);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysUsersId=result.getLong("sysUsersId", null);
        this.sysUsersAcc=result.getString("sysUsersAcc", null);
        this.sysUsersPwd=result.getString("sysUsersPwd", null);
        this.sysUsersPhone=result.getString("sysUsersPhone", null);
        this.sysUsersOpenId=result.getString("sysUsersOpenId", null);
        this.sysUsersStatus=result.getLong("sysUsersStatus", null);
        this.sysUsersType=result.getLong("sysUsersType", null);
        this.sysUsersRegTime=result.getLocalDateTime("sysUsersRegTime", null);
        this.sysUsersStartTime=result.getLocalDateTime("sysUsersStartTime", null);
        this.sysUsersEndTime=result.getLocalDateTime("sysUsersEndTime", null);
        this.sysUsersPrice=result.getLong("sysUsersPrice", null);
        this.sysUsersDesc=result.getString("sysUsersDesc", null);
        this.sysUsersSup=result.getLong("sysUsersSup", null);
        this.sysUsersMail=result.getString("sysUsersMail", null);
        this.sysUsersRole=result.getLong("sysUsersRole", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysUsersId() {
        return sysUsersId;
    }
    public SysUsers setSysUsersId(java.lang.Long sysUsersId) {
        this.sysUsersId = sysUsersId;
        return this;
    }
    public java.lang.String getSysUsersAcc() {
        return sysUsersAcc;
    }
    public SysUsers setSysUsersAcc(java.lang.String sysUsersAcc) {
        int size0 = Tools.textLength(sysUsersAcc);
        if (size0 > 32) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersAcc最大长度为:32,实际长度为:"+ size0 +",数据为:"+sysUsersAcc);
        }
        this.sysUsersAcc = sysUsersAcc;
        return this;
    }
    public SysUsers setSysUsersAccUnsafe(java.lang.String sysUsersAcc) {
        this.sysUsersAcc = sysUsersAcc;
        return this;
    }
    public java.lang.String getSysUsersPwd() {
        return sysUsersPwd;
    }
    public SysUsers setSysUsersPwd(java.lang.String sysUsersPwd) {
        int size0 = Tools.textLength(sysUsersPwd);
        if (size0 > 32) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersPwd最大长度为:32,实际长度为:"+ size0 +",数据为:"+sysUsersPwd);
        }
        this.sysUsersPwd = sysUsersPwd;
        return this;
    }
    public SysUsers setSysUsersPwdUnsafe(java.lang.String sysUsersPwd) {
        this.sysUsersPwd = sysUsersPwd;
        return this;
    }
    public java.lang.String getSysUsersPhone() {
        return sysUsersPhone;
    }
    public SysUsers setSysUsersPhone(java.lang.String sysUsersPhone) {
        int size0 = Tools.textLength(sysUsersPhone);
        if (size0 > 11) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersPhone最大长度为:11,实际长度为:"+ size0 +",数据为:"+sysUsersPhone);
        }
        this.sysUsersPhone = sysUsersPhone;
        return this;
    }
    public SysUsers setSysUsersPhoneUnsafe(java.lang.String sysUsersPhone) {
        this.sysUsersPhone = sysUsersPhone;
        return this;
    }
    public java.lang.String getSysUsersOpenId() {
        return sysUsersOpenId;
    }
    public SysUsers setSysUsersOpenId(java.lang.String sysUsersOpenId) {
        int size0 = Tools.textLength(sysUsersOpenId);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersOpenId最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersOpenId);
        }
        this.sysUsersOpenId = sysUsersOpenId;
        return this;
    }
    public SysUsers setSysUsersOpenIdUnsafe(java.lang.String sysUsersOpenId) {
        this.sysUsersOpenId = sysUsersOpenId;
        return this;
    }
    public java.lang.Long getSysUsersStatus() {
        return sysUsersStatus;
    }
    public SysUsers setSysUsersStatus(java.lang.Long sysUsersStatus) {
        this.sysUsersStatus = sysUsersStatus;
        return this;
    }
    public java.lang.Long getSysUsersType() {
        return sysUsersType;
    }
    public SysUsers setSysUsersType(java.lang.Long sysUsersType) {
        this.sysUsersType = sysUsersType;
        return this;
    }
    public java.time.LocalDateTime getSysUsersRegTime() {
        return sysUsersRegTime;
    }
    public SysUsers setSysUsersRegTime(java.time.LocalDateTime sysUsersRegTime) {
        this.sysUsersRegTime = sysUsersRegTime;
        return this;
    }
    public java.time.LocalDateTime getSysUsersStartTime() {
        return sysUsersStartTime;
    }
    public SysUsers setSysUsersStartTime(java.time.LocalDateTime sysUsersStartTime) {
        this.sysUsersStartTime = sysUsersStartTime;
        return this;
    }
    public java.time.LocalDateTime getSysUsersEndTime() {
        return sysUsersEndTime;
    }
    public SysUsers setSysUsersEndTime(java.time.LocalDateTime sysUsersEndTime) {
        this.sysUsersEndTime = sysUsersEndTime;
        return this;
    }
    public java.lang.Long getSysUsersPrice() {
        return sysUsersPrice;
    }
    public SysUsers setSysUsersPrice(java.lang.Long sysUsersPrice) {
        this.sysUsersPrice = sysUsersPrice;
        return this;
    }
    public java.lang.String getSysUsersDesc() {
        return sysUsersDesc;
    }
    public SysUsers setSysUsersDesc(java.lang.String sysUsersDesc) {
        int size0 = Tools.textLength(sysUsersDesc);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysUsersDesc);
        }
        this.sysUsersDesc = sysUsersDesc;
        return this;
    }
    public SysUsers setSysUsersDescUnsafe(java.lang.String sysUsersDesc) {
        this.sysUsersDesc = sysUsersDesc;
        return this;
    }
    public java.lang.Long getSysUsersSup() {
        return sysUsersSup;
    }
    public SysUsers setSysUsersSup(java.lang.Long sysUsersSup) {
        this.sysUsersSup = sysUsersSup;
        return this;
    }
    public java.lang.String getSysUsersMail() {
        return sysUsersMail;
    }
    public SysUsers setSysUsersMail(java.lang.String sysUsersMail) {
        int size0 = Tools.textLength(sysUsersMail);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysUsers.sysUsersMail最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysUsersMail);
        }
        this.sysUsersMail = sysUsersMail;
        return this;
    }
    public SysUsers setSysUsersMailUnsafe(java.lang.String sysUsersMail) {
        this.sysUsersMail = sysUsersMail;
        return this;
    }
    public java.lang.Long getSysUsersRole() {
        return sysUsersRole;
    }
    public SysUsers setSysUsersRole(java.lang.Long sysUsersRole) {
        this.sysUsersRole = sysUsersRole;
        return this;
    }
    public SysUsers setList(List<?> data) {
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

    public SysUsers setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysUsers putMap(String key, Object value) {
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
    public SysUsers setData(Object data) {
        this.data = data;
        return this;
    }
}
