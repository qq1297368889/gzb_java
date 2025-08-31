package com.authorizationSystem.entity;
import com.authorizationSystem.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.authorizationSystem.dao.ApplicationUiDao;
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
@EntityAttribute(name="application_ui",desc="applicationUi")
public class ApplicationUi implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="application_ui_id",desc="applicationUiId")
    private java.lang.Long applicationUiId;
    @EntityAttribute(key=false,size = 19,name="application_ui_aid",desc="applicationUiAid")
    private java.lang.Long applicationUiAid;
    @EntityAttribute(key=false,size = 19,name="application_ui_time",desc="applicationUiTime")
    private java.lang.String applicationUiTime;
    @EntityAttribute(key=false,size = 19,name="application_ui_file",desc="applicationUiFile")
    private java.lang.Long applicationUiFile;
    private List<?> list;
   public ApplicationUi() {}

    public ApplicationUi(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public ApplicationUi(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public ApplicationUi(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public ApplicationUi(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.save(this);
    }
    public int delete(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.delete(this);
    }
    public int update(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.update(this);
    }
    public int saveAsync(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.saveAsync(this);
    }
    public int deleteAsync(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.deleteAsync(this);
    }
    public int updateAsync(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.updateAsync(this);
    }
    public List<ApplicationUi> query(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.query(this);
    }
    public List<ApplicationUi> query(ApplicationUiDao applicationUiDao,int page,int size) throws Exception {
        return applicationUiDao.query(this,page,size);
    }
    public List<ApplicationUi> query(ApplicationUiDao applicationUiDao,String sortField,String sortType,int page,int size) throws Exception {
        return applicationUiDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(ApplicationUiDao applicationUiDao, String sortField, String sortType, int page, int size) throws Exception {
        return applicationUiDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(ApplicationUiDao applicationUiDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return applicationUiDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public ApplicationUi find(ApplicationUiDao applicationUiDao) throws Exception {
        return applicationUiDao.find(this);
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
        result.set("applicationUiId", applicationUiId);
        result.set("applicationUiAid", applicationUiAid);
        result.set("applicationUiTime", applicationUiTime);
        result.set("applicationUiFile", applicationUiFile);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.applicationUiId=result.getLong("applicationUiId", null);
        this.applicationUiAid=result.getLong("applicationUiAid", null);
        this.applicationUiTime=result.getString("applicationUiTime", null);
        this.applicationUiFile=result.getLong("applicationUiFile", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("application_ui_id");
            if (temp!=null) {
                this.applicationUiId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_ui_aid");
            if (temp!=null) {
                this.applicationUiAid=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_ui_time");
            if (temp!=null) {
                this.applicationUiTime=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_ui_file");
            if (temp!=null) {
                this.applicationUiFile=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getApplicationUiId() {
        return applicationUiId;
    }
    public ApplicationUi setApplicationUiId(java.lang.Long applicationUiId) {
        int size0 = Tools.textLength(applicationUiId);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationUi.applicationUiId最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUiId);
        }
        this.applicationUiId = applicationUiId;
        return this;
    }
    public ApplicationUi setApplicationUiIdUnsafe(java.lang.Long applicationUiId) {
        this.applicationUiId = applicationUiId;
        return this;
    }
    public java.lang.Long getApplicationUiAid() {
        return applicationUiAid;
    }
    public ApplicationUi setApplicationUiAid(java.lang.Long applicationUiAid) {
        int size0 = Tools.textLength(applicationUiAid);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationUi.applicationUiAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUiAid);
        }
        this.applicationUiAid = applicationUiAid;
        return this;
    }
    public ApplicationUi setApplicationUiAidUnsafe(java.lang.Long applicationUiAid) {
        this.applicationUiAid = applicationUiAid;
        return this;
    }
    public java.lang.String getApplicationUiTime() {
        return applicationUiTime;
    }
    public ApplicationUi setApplicationUiTime(java.lang.String applicationUiTime) {
        int size0 = Tools.textLength(applicationUiTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationUi.applicationUiTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUiTime);
        }
        this.applicationUiTime = applicationUiTime;
        return this;
    }
    public ApplicationUi setApplicationUiTimeUnsafe(java.lang.String applicationUiTime) {
        this.applicationUiTime = applicationUiTime;
        return this;
    }
    public java.lang.Long getApplicationUiFile() {
        return applicationUiFile;
    }
    public ApplicationUi setApplicationUiFile(java.lang.Long applicationUiFile) {
        int size0 = Tools.textLength(applicationUiFile);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationUi.applicationUiFile最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationUiFile);
        }
        this.applicationUiFile = applicationUiFile;
        return this;
    }
    public ApplicationUi setApplicationUiFileUnsafe(java.lang.Long applicationUiFile) {
        this.applicationUiFile = applicationUiFile;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
