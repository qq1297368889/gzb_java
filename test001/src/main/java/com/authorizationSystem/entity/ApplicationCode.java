package com.authorizationSystem.entity;
import com.authorizationSystem.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.authorizationSystem.dao.ApplicationCodeDao;
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
@EntityAttribute(name="application_code",desc="applicationCode")
public class ApplicationCode implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="application_code_id",desc="applicationCodeId")
    private java.lang.Long applicationCodeId;
    @EntityAttribute(key=false,size = 19,name="application_code_aid",desc="applicationCodeAid")
    private java.lang.Long applicationCodeAid;
    @EntityAttribute(key=false,size = 19,name="application_code_time",desc="applicationCodeTime")
    private java.lang.String applicationCodeTime;
    @EntityAttribute(key=false,size = 19,name="application_code_file",desc="applicationCodeFile")
    private java.lang.Long applicationCodeFile;
    private List<?> list;
   public ApplicationCode() {}

    public ApplicationCode(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public ApplicationCode(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public ApplicationCode(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public ApplicationCode(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.save(this);
    }
    public int delete(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.delete(this);
    }
    public int update(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.update(this);
    }
    public int saveAsync(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.saveAsync(this);
    }
    public int deleteAsync(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.deleteAsync(this);
    }
    public int updateAsync(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.updateAsync(this);
    }
    public List<ApplicationCode> query(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.query(this);
    }
    public List<ApplicationCode> query(ApplicationCodeDao applicationCodeDao,int page,int size) throws Exception {
        return applicationCodeDao.query(this,page,size);
    }
    public List<ApplicationCode> query(ApplicationCodeDao applicationCodeDao,String sortField,String sortType,int page,int size) throws Exception {
        return applicationCodeDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(ApplicationCodeDao applicationCodeDao, String sortField, String sortType, int page, int size) throws Exception {
        return applicationCodeDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(ApplicationCodeDao applicationCodeDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return applicationCodeDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public ApplicationCode find(ApplicationCodeDao applicationCodeDao) throws Exception {
        return applicationCodeDao.find(this);
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
        result.set("applicationCodeId", applicationCodeId);
        result.set("applicationCodeAid", applicationCodeAid);
        result.set("applicationCodeTime", applicationCodeTime);
        result.set("applicationCodeFile", applicationCodeFile);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.applicationCodeId=result.getLong("applicationCodeId", null);
        this.applicationCodeAid=result.getLong("applicationCodeAid", null);
        this.applicationCodeTime=result.getString("applicationCodeTime", null);
        this.applicationCodeFile=result.getLong("applicationCodeFile", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("application_code_id");
            if (temp!=null) {
                this.applicationCodeId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_code_aid");
            if (temp!=null) {
                this.applicationCodeAid=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("application_code_time");
            if (temp!=null) {
                this.applicationCodeTime=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("application_code_file");
            if (temp!=null) {
                this.applicationCodeFile=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getApplicationCodeId() {
        return applicationCodeId;
    }
    public ApplicationCode setApplicationCodeId(java.lang.Long applicationCodeId) {
        int size0 = Tools.textLength(applicationCodeId);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationCode.applicationCodeId最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationCodeId);
        }
        this.applicationCodeId = applicationCodeId;
        return this;
    }
    public ApplicationCode setApplicationCodeIdUnsafe(java.lang.Long applicationCodeId) {
        this.applicationCodeId = applicationCodeId;
        return this;
    }
    public java.lang.Long getApplicationCodeAid() {
        return applicationCodeAid;
    }
    public ApplicationCode setApplicationCodeAid(java.lang.Long applicationCodeAid) {
        int size0 = Tools.textLength(applicationCodeAid);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationCode.applicationCodeAid最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationCodeAid);
        }
        this.applicationCodeAid = applicationCodeAid;
        return this;
    }
    public ApplicationCode setApplicationCodeAidUnsafe(java.lang.Long applicationCodeAid) {
        this.applicationCodeAid = applicationCodeAid;
        return this;
    }
    public java.lang.String getApplicationCodeTime() {
        return applicationCodeTime;
    }
    public ApplicationCode setApplicationCodeTime(java.lang.String applicationCodeTime) {
        int size0 = Tools.textLength(applicationCodeTime);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationCode.applicationCodeTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationCodeTime);
        }
        this.applicationCodeTime = applicationCodeTime;
        return this;
    }
    public ApplicationCode setApplicationCodeTimeUnsafe(java.lang.String applicationCodeTime) {
        this.applicationCodeTime = applicationCodeTime;
        return this;
    }
    public java.lang.Long getApplicationCodeFile() {
        return applicationCodeFile;
    }
    public ApplicationCode setApplicationCodeFile(java.lang.Long applicationCodeFile) {
        int size0 = Tools.textLength(applicationCodeFile);
        if (size0 > 19) {
            throw new RuntimeException("ApplicationCode.applicationCodeFile最大长度为:19,实际长度为:"+ size0 +",数据为:"+applicationCodeFile);
        }
        this.applicationCodeFile = applicationCodeFile;
        return this;
    }
    public ApplicationCode setApplicationCodeFileUnsafe(java.lang.Long applicationCodeFile) {
        this.applicationCodeFile = applicationCodeFile;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
