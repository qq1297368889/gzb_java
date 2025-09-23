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
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_users_id",desc="sysUsersId")
    private java.lang.Long sysUsersId;
    @EntityAttribute(key=false,size = 32,name="sys_users_acc",desc="sysUsersAcc")
    private java.lang.String sysUsersAcc;
    @EntityAttribute(key=false,size = 32,name="sys_users_pwd",desc="sysUsersPwd")
    private java.lang.String sysUsersPwd;
    @EntityAttribute(key=false,size = 11,name="sys_users_phone",desc="sysUsersPhone")
    private java.lang.String sysUsersPhone;
    @EntityAttribute(key=false,size = 100,name="sys_users_open_id",desc="sysUsersOpenId")
    private java.lang.String sysUsersOpenId;
    @EntityAttribute(key=false,size = 19,name="sys_users_status",desc="sysUsersStatus")
    private java.lang.Long sysUsersStatus;
    @EntityAttribute(key=false,size = 19,name="sys_users_type",desc="sysUsersType")
    private java.lang.Long sysUsersType;
    @EntityAttribute(key=false,size = 19,name="sys_users_reg_time",desc="sysUsersRegTime")
    private java.lang.String sysUsersRegTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_start_time",desc="sysUsersStartTime")
    private java.lang.String sysUsersStartTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_end_time",desc="sysUsersEndTime")
    private java.lang.String sysUsersEndTime;
    @EntityAttribute(key=false,size = 19,name="sys_users_price",desc="sysUsersPrice")
    private java.lang.Long sysUsersPrice;
    @EntityAttribute(key=false,size = 255,name="sys_users_desc",desc="sysUsersDesc")
    private java.lang.String sysUsersDesc;
    @EntityAttribute(key=false,size = 19,name="sys_users_sup",desc="sysUsersSup")
    private java.lang.Long sysUsersSup;
    @EntityAttribute(key=false,size = 255,name="sys_users_mail",desc="sysUsersMail")
    private java.lang.String sysUsersMail;
    @EntityAttribute(key=false,size = 19,name="sys_users_group",desc="sysUsersGroup")
    private java.lang.Long sysUsersGroup;
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

    public SysUsers(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
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
        StringBuilder sb = new StringBuilder("{");
        if (this.sysUsersId != null) {
            sb.append("\"sysUsersId\":\"").append(sysUsersId).append("\",");
        }
        if (this.sysUsersAcc != null) {
            sb.append("\"sysUsersAcc\":\"").append(sysUsersAcc).append("\",");
        }
        if (this.sysUsersPwd != null) {
            sb.append("\"sysUsersPwd\":\"").append(sysUsersPwd).append("\",");
        }
        if (this.sysUsersPhone != null) {
            sb.append("\"sysUsersPhone\":\"").append(sysUsersPhone).append("\",");
        }
        if (this.sysUsersOpenId != null) {
            sb.append("\"sysUsersOpenId\":\"").append(sysUsersOpenId).append("\",");
        }
        if (this.sysUsersStatus != null) {
            sb.append("\"sysUsersStatus\":\"").append(sysUsersStatus).append("\",");
        }
        if (this.sysUsersType != null) {
            sb.append("\"sysUsersType\":\"").append(sysUsersType).append("\",");
        }
        if (this.sysUsersRegTime != null) {
            sb.append("\"sysUsersRegTime\":\"").append(sysUsersRegTime).append("\",");
        }
        if (this.sysUsersStartTime != null) {
            sb.append("\"sysUsersStartTime\":\"").append(sysUsersStartTime).append("\",");
        }
        if (this.sysUsersEndTime != null) {
            sb.append("\"sysUsersEndTime\":\"").append(sysUsersEndTime).append("\",");
        }
        if (this.sysUsersPrice != null) {
            sb.append("\"sysUsersPrice\":\"").append(sysUsersPrice).append("\",");
        }
        if (this.sysUsersDesc != null) {
            sb.append("\"sysUsersDesc\":\"").append(sysUsersDesc).append("\",");
        }
        if (this.sysUsersSup != null) {
            sb.append("\"sysUsersSup\":\"").append(sysUsersSup).append("\",");
        }
        if (this.sysUsersMail != null) {
            sb.append("\"sysUsersMail\":\"").append(sysUsersMail).append("\",");
        }
        if (this.sysUsersGroup != null) {
            sb.append("\"sysUsersGroup\":\"").append(sysUsersGroup).append("\",");
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
        result.set("sysUsersGroup", sysUsersGroup);
        result.set(dataName, data);
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
        this.sysUsersRegTime=result.getString("sysUsersRegTime", null);
        this.sysUsersStartTime=result.getString("sysUsersStartTime", null);
        this.sysUsersEndTime=result.getString("sysUsersEndTime", null);
        this.sysUsersPrice=result.getLong("sysUsersPrice", null);
        this.sysUsersDesc=result.getString("sysUsersDesc", null);
        this.sysUsersSup=result.getLong("sysUsersSup", null);
        this.sysUsersMail=result.getString("sysUsersMail", null);
        this.sysUsersGroup=result.getLong("sysUsersGroup", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_users_id")) {
            temp=resultSet.getString("sys_users_id");
            if (temp!=null) {
                this.sysUsersId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_acc")) {
            temp=resultSet.getString("sys_users_acc");
            if (temp!=null) {
                this.sysUsersAcc=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_pwd")) {
            temp=resultSet.getString("sys_users_pwd");
            if (temp!=null) {
                this.sysUsersPwd=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_phone")) {
            temp=resultSet.getString("sys_users_phone");
            if (temp!=null) {
                this.sysUsersPhone=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_open_id")) {
            temp=resultSet.getString("sys_users_open_id");
            if (temp!=null) {
                this.sysUsersOpenId=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_status")) {
            temp=resultSet.getString("sys_users_status");
            if (temp!=null) {
                this.sysUsersStatus=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_type")) {
            temp=resultSet.getString("sys_users_type");
            if (temp!=null) {
                this.sysUsersType=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_reg_time")) {
            temp=resultSet.getString("sys_users_reg_time");
            if (temp!=null) {
                this.sysUsersRegTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_start_time")) {
            temp=resultSet.getString("sys_users_start_time");
            if (temp!=null) {
                this.sysUsersStartTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_end_time")) {
            temp=resultSet.getString("sys_users_end_time");
            if (temp!=null) {
                this.sysUsersEndTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_price")) {
            temp=resultSet.getString("sys_users_price");
            if (temp!=null) {
                this.sysUsersPrice=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_desc")) {
            temp=resultSet.getString("sys_users_desc");
            if (temp!=null) {
                this.sysUsersDesc=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_sup")) {
            temp=resultSet.getString("sys_users_sup");
            if (temp!=null) {
                this.sysUsersSup=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_users_mail")) {
            temp=resultSet.getString("sys_users_mail");
            if (temp!=null) {
                this.sysUsersMail=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_users_group")) {
            temp=resultSet.getString("sys_users_group");
            if (temp!=null) {
                this.sysUsersGroup=java.lang.Long.valueOf(temp);
            }
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
            throw new RuntimeException("SysUsers.sysUsersAcc最大长度为:32,实际长度为:"+ size0 +",数据为:"+sysUsersAcc);
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
            throw new RuntimeException("SysUsers.sysUsersPwd最大长度为:32,实际长度为:"+ size0 +",数据为:"+sysUsersPwd);
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
            throw new RuntimeException("SysUsers.sysUsersPhone最大长度为:11,实际长度为:"+ size0 +",数据为:"+sysUsersPhone);
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
            throw new RuntimeException("SysUsers.sysUsersOpenId最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysUsersOpenId);
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
    public java.lang.String getSysUsersRegTime() {
        return sysUsersRegTime;
    }
    public SysUsers setSysUsersRegTime(java.lang.String sysUsersRegTime) {
        int size0 = Tools.textLength(sysUsersRegTime);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersRegTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersRegTime);
        }
        this.sysUsersRegTime = sysUsersRegTime;
        return this;
    }
    public SysUsers setSysUsersRegTimeUnsafe(java.lang.String sysUsersRegTime) {
        this.sysUsersRegTime = sysUsersRegTime;
        return this;
    }
    public java.lang.String getSysUsersStartTime() {
        return sysUsersStartTime;
    }
    public SysUsers setSysUsersStartTime(java.lang.String sysUsersStartTime) {
        int size0 = Tools.textLength(sysUsersStartTime);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersStartTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersStartTime);
        }
        this.sysUsersStartTime = sysUsersStartTime;
        return this;
    }
    public SysUsers setSysUsersStartTimeUnsafe(java.lang.String sysUsersStartTime) {
        this.sysUsersStartTime = sysUsersStartTime;
        return this;
    }
    public java.lang.String getSysUsersEndTime() {
        return sysUsersEndTime;
    }
    public SysUsers setSysUsersEndTime(java.lang.String sysUsersEndTime) {
        int size0 = Tools.textLength(sysUsersEndTime);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersEndTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersEndTime);
        }
        this.sysUsersEndTime = sysUsersEndTime;
        return this;
    }
    public SysUsers setSysUsersEndTimeUnsafe(java.lang.String sysUsersEndTime) {
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
            throw new RuntimeException("SysUsers.sysUsersDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysUsersDesc);
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
            throw new RuntimeException("SysUsers.sysUsersMail最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysUsersMail);
        }
        this.sysUsersMail = sysUsersMail;
        return this;
    }
    public SysUsers setSysUsersMailUnsafe(java.lang.String sysUsersMail) {
        this.sysUsersMail = sysUsersMail;
        return this;
    }
    public java.lang.Long getSysUsersGroup() {
        return sysUsersGroup;
    }
    public SysUsers setSysUsersGroup(java.lang.Long sysUsersGroup) {
        this.sysUsersGroup = sysUsersGroup;
        return this;
    }
    public List<?> getList() {
        return (List<?>) data;
    }

    public SysUsers setList(List<?> data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) data;
    }

    public SysUsers setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysUsers putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysUsers setData(Object data) {
        this.data = data;
        return this;
    }}
