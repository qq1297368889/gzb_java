package com.authorizationSystem.entity;
import com.authorizationSystem.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.authorizationSystem.dao.ApplicationDao;
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
@EntityAttribute(name="application",desc="application")
public class Application implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="application_id",desc="applicationId")
    private java.lang.Long applicationId;
    @EntityAttribute(key=false,size = 100,name="application_name",desc="applicationName")
    private java.lang.String applicationName;
    @EntityAttribute(key=false,size = 100,name="application_desc",desc="applicationDesc")
    private java.lang.String applicationDesc;
    @EntityAttribute(key=false,size = 19,name="application_state",desc="applicationState")
    private java.lang.Long applicationState;
    @EntityAttribute(key=false,size = 19,name="application_type",desc="applicationType")
    private java.lang.Long applicationType;
    @EntityAttribute(key=false,size = 100,name="application_pwd",desc="applicationPwd")
    private java.lang.String applicationPwd;
    @EntityAttribute(key=false,size = 100,name="application_iv",desc="applicationIv")
    private java.lang.String applicationIv;
    @EntityAttribute(key=false,size = 19,name="application_uid",desc="applicationUid")
    private java.lang.Long applicationUid;
    @EntityAttribute(key=false,size = 19,name="application_cid",desc="applicationCid")
    private java.lang.Long applicationCid;
    @EntityAttribute(key=false,size = 19,name="application_uiid",desc="applicationUiid")
    private java.lang.Long applicationUiid;
    @EntityAttribute(key=false,size = 19,name="application_sell",desc="applicationSell")
    private java.lang.Long applicationSell;
    private List<?> list;
   public Application() {}

    public Application(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public Application(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public Application(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public Application(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(ApplicationDao applicationDao) throws Exception {
        return applicationDao.save(this);
    }
    public int delete(ApplicationDao applicationDao) throws Exception {
        return applicationDao.delete(this);
    }
    public int update(ApplicationDao applicationDao) throws Exception {
        return applicationDao.update(this);
    }
    public int saveAsync(ApplicationDao applicationDao) throws Exception {
        return applicationDao.saveAsync(this);
    }
    public int deleteAsync(ApplicationDao applicationDao) throws Exception {
        return applicationDao.deleteAsync(this);
    }
    public int updateAsync(ApplicationDao applicationDao) throws Exception {
        return applicationDao.updateAsync(this);
    }
    public List<Application> query(ApplicationDao applicationDao) throws Exception {
        return applicationDao.query(this);
    }
    public List<Application> query(ApplicationDao applicationDao,int page,int size) throws Exception {
        return applicationDao.query(this,page,size);
    }
    public List<Application> query(ApplicationDao applicationDao,String sortField,String sortType,int page,int size) throws Exception {
        return applicationDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(ApplicationDao applicationDao, String sortField, String sortType, int page, int size) throws Exception {
        return applicationDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(ApplicationDao applicationDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return applicationDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public Application find(ApplicationDao applicationDao) throws Exception {
        return applicationDao.find(this);
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
        result.set("applicationId", applicationId);
        result.set("applicationName", applicationName);
        result.set("applicationDesc", applicationDesc);
        result.set("applicationState", applicationState);
        result.set("applicationType", applicationType);
        result.set("applicationPwd", applicationPwd);
        result.set("applicationIv", applicationIv);
        result.set("applicationUid", applicationUid);
        result.set("applicationCid", applicationCid);
        result.set("applicationUiid", applicationUiid);
        result.set("applicationSell", applicationSell);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.applicationId=result.getLong("applicationId", null);
        this.applicationName=result.getString("applicationName", null);
        this.applicationDesc=result.getString("applicationDesc", null);
        this.applicationState=result.getLong("applicationState", null);
        this.applicationType=result.getLong("applicationType", null);
        this.applicationPwd=result.getString("applicationPwd", null);
        this.applicationIv=result.getString("applicationIv", null);
        this.applicationUid=result.getLong("applicationUid", null);
        this.applicationCid=result.getLong("applicationCid", null);
        this.applicationUiid=result.getLong("applicationUiid", null);
        this.applicationSell=result.getLong("applicationSell", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("application_id");
            if (temp!=null) {
                this.applicationId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_name");
            if (temp!=null) {
                this.applicationName=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_desc");
            if (temp!=null) {
                this.applicationDesc=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_state");
            if (temp!=null) {
                this.applicationState=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_type");
            if (temp!=null) {
                this.applicationType=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_pwd");
            if (temp!=null) {
                this.applicationPwd=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_iv");
            if (temp!=null) {
                this.applicationIv=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_uid");
            if (temp!=null) {
                this.applicationUid=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_cid");
            if (temp!=null) {
                this.applicationCid=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_uiid");
            if (temp!=null) {
                this.applicationUiid=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_sell");
            if (temp!=null) {
                this.applicationSell=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getApplicationId() {
        return applicationId;
    }
    public Application setApplicationId(java.lang.Long applicationId) {
        int size0 = Tools.textLength(applicationId);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationId最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationId);
        }
        this.applicationId = applicationId;
        return this;
    }
    public Application setApplicationIdUnsafe(java.lang.Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }
    public java.lang.String getApplicationName() {
        return applicationName;
    }
    public Application setApplicationName(java.lang.String applicationName) {
        int size0 = Tools.textLength(applicationName);
        if (size0 > 100) {
            throw new RuntimeException("Application.applicationName最大长度为:100,实际长度为:"+ size0 +",数据为:"+applicationName);
        }
        this.applicationName = applicationName;
        return this;
    }
    public Application setApplicationNameUnsafe(java.lang.String applicationName) {
        this.applicationName = applicationName;
        return this;
    }
    public java.lang.String getApplicationDesc() {
        return applicationDesc;
    }
    public Application setApplicationDesc(java.lang.String applicationDesc) {
        int size0 = Tools.textLength(applicationDesc);
        if (size0 > 100) {
            throw new RuntimeException("Application.applicationDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+applicationDesc);
        }
        this.applicationDesc = applicationDesc;
        return this;
    }
    public Application setApplicationDescUnsafe(java.lang.String applicationDesc) {
        this.applicationDesc = applicationDesc;
        return this;
    }
    public java.lang.Long getApplicationState() {
        return applicationState;
    }
    public Application setApplicationState(java.lang.Long applicationState) {
        int size0 = Tools.textLength(applicationState);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationState最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationState);
        }
        this.applicationState = applicationState;
        return this;
    }
    public Application setApplicationStateUnsafe(java.lang.Long applicationState) {
        this.applicationState = applicationState;
        return this;
    }
    public java.lang.Long getApplicationType() {
        return applicationType;
    }
    public Application setApplicationType(java.lang.Long applicationType) {
        int size0 = Tools.textLength(applicationType);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationType最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationType);
        }
        this.applicationType = applicationType;
        return this;
    }
    public Application setApplicationTypeUnsafe(java.lang.Long applicationType) {
        this.applicationType = applicationType;
        return this;
    }
    public java.lang.String getApplicationPwd() {
        return applicationPwd;
    }
    public Application setApplicationPwd(java.lang.String applicationPwd) {
        int size0 = Tools.textLength(applicationPwd);
        if (size0 > 100) {
            throw new RuntimeException("Application.applicationPwd最大长度为:100,实际长度为:"+ size0 +",数据为:"+applicationPwd);
        }
        this.applicationPwd = applicationPwd;
        return this;
    }
    public Application setApplicationPwdUnsafe(java.lang.String applicationPwd) {
        this.applicationPwd = applicationPwd;
        return this;
    }
    public java.lang.String getApplicationIv() {
        return applicationIv;
    }
    public Application setApplicationIv(java.lang.String applicationIv) {
        int size0 = Tools.textLength(applicationIv);
        if (size0 > 100) {
            throw new RuntimeException("Application.applicationIv最大长度为:100,实际长度为:"+ size0 +",数据为:"+applicationIv);
        }
        this.applicationIv = applicationIv;
        return this;
    }
    public Application setApplicationIvUnsafe(java.lang.String applicationIv) {
        this.applicationIv = applicationIv;
        return this;
    }
    public java.lang.Long getApplicationUid() {
        return applicationUid;
    }
    public Application setApplicationUid(java.lang.Long applicationUid) {
        int size0 = Tools.textLength(applicationUid);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationUid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUid);
        }
        this.applicationUid = applicationUid;
        return this;
    }
    public Application setApplicationUidUnsafe(java.lang.Long applicationUid) {
        this.applicationUid = applicationUid;
        return this;
    }
    public java.lang.Long getApplicationCid() {
        return applicationCid;
    }
    public Application setApplicationCid(java.lang.Long applicationCid) {
        int size0 = Tools.textLength(applicationCid);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationCid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationCid);
        }
        this.applicationCid = applicationCid;
        return this;
    }
    public Application setApplicationCidUnsafe(java.lang.Long applicationCid) {
        this.applicationCid = applicationCid;
        return this;
    }
    public java.lang.Long getApplicationUiid() {
        return applicationUiid;
    }
    public Application setApplicationUiid(java.lang.Long applicationUiid) {
        int size0 = Tools.textLength(applicationUiid);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationUiid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUiid);
        }
        this.applicationUiid = applicationUiid;
        return this;
    }
    public Application setApplicationUiidUnsafe(java.lang.Long applicationUiid) {
        this.applicationUiid = applicationUiid;
        return this;
    }
    public java.lang.Long getApplicationSell() {
        return applicationSell;
    }
    public Application setApplicationSell(java.lang.Long applicationSell) {
        int size0 = Tools.textLength(applicationSell);
        if (size0 > 19) {
            throw new RuntimeException("Application.applicationSell最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationSell);
        }
        this.applicationSell = applicationSell;
        return this;
    }
    public Application setApplicationSellUnsafe(java.lang.Long applicationSell) {
        this.applicationSell = applicationSell;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
