package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysUsersDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private List<?> list;
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

    public SysUsers(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
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
    public SqlTemplate toSelectSql() {
        return SqlTools.toSelectSql(this,"act_code_id", "asc", 0, false);
    }

    //查询语句 可选项 排序
    public SqlTemplate toSelectSql(String sortField, String sortType, Integer size, Boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave() {
        return SqlTools.toSave(this);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(Boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
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
        result.set(dataName, list);
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
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_users_id");
            if (temp!=null) {
                this.sysUsersId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_acc");
            if (temp!=null) {
                this.sysUsersAcc=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_pwd");
            if (temp!=null) {
                this.sysUsersPwd=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_phone");
            if (temp!=null) {
                this.sysUsersPhone=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_open_id");
            if (temp!=null) {
                this.sysUsersOpenId=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_status");
            if (temp!=null) {
                this.sysUsersStatus=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_type");
            if (temp!=null) {
                this.sysUsersType=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_reg_time");
            if (temp!=null) {
                this.sysUsersRegTime=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_start_time");
            if (temp!=null) {
                this.sysUsersStartTime=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_end_time");
            if (temp!=null) {
                this.sysUsersEndTime=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_price");
            if (temp!=null) {
                this.sysUsersPrice=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_desc");
            if (temp!=null) {
                this.sysUsersDesc=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_sup");
            if (temp!=null) {
                this.sysUsersSup=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_users_mail");
            if (temp!=null) {
                this.sysUsersMail=java.lang.String.valueOf(temp);
            }
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
        int size0 = Tools.textLength(sysUsersId);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersId);
        }
        this.sysUsersId = sysUsersId;
        return this;
    }
    public SysUsers setSysUsersIdUnsafe(java.lang.Long sysUsersId) {
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
        int size0 = Tools.textLength(sysUsersStatus);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersStatus最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersStatus);
        }
        this.sysUsersStatus = sysUsersStatus;
        return this;
    }
    public SysUsers setSysUsersStatusUnsafe(java.lang.Long sysUsersStatus) {
        this.sysUsersStatus = sysUsersStatus;
        return this;
    }
    public java.lang.Long getSysUsersType() {
        return sysUsersType;
    }
    public SysUsers setSysUsersType(java.lang.Long sysUsersType) {
        int size0 = Tools.textLength(sysUsersType);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersType最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersType);
        }
        this.sysUsersType = sysUsersType;
        return this;
    }
    public SysUsers setSysUsersTypeUnsafe(java.lang.Long sysUsersType) {
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
        int size0 = Tools.textLength(sysUsersPrice);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersPrice最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersPrice);
        }
        this.sysUsersPrice = sysUsersPrice;
        return this;
    }
    public SysUsers setSysUsersPriceUnsafe(java.lang.Long sysUsersPrice) {
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
        int size0 = Tools.textLength(sysUsersSup);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersSup最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersSup);
        }
        this.sysUsersSup = sysUsersSup;
        return this;
    }
    public SysUsers setSysUsersSupUnsafe(java.lang.Long sysUsersSup) {
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
        int size0 = Tools.textLength(sysUsersGroup);
        if (size0 > 19) {
            throw new RuntimeException("SysUsers.sysUsersGroup最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysUsersGroup);
        }
        this.sysUsersGroup = sysUsersGroup;
        return this;
    }
    public SysUsers setSysUsersGroupUnsafe(java.lang.Long sysUsersGroup) {
        this.sysUsersGroup = sysUsersGroup;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
