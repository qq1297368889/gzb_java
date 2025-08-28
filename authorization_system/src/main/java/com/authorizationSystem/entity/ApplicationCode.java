package com.authorizationSystem.entity;
import com.authorizationSystem.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.authorizationSystem.dao.ApplicationCodeDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="application_code",desc="applicationCode")
public class ApplicationCode implements Serializable{
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

    public ApplicationCode(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public ApplicationCode(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("applicationCodeId");
        if (str!=null && !str.isEmpty()) {
            setApplicationCodeId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("applicationCodeAid");
        if (str!=null && !str.isEmpty()) {
            setApplicationCodeAid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("applicationCodeTime");
        if (str!=null && !str.isEmpty()) {
            setApplicationCodeTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("applicationCodeFile");
        if (str!=null && !str.isEmpty()) {
            setApplicationCodeFile(java.lang.Long.valueOf(str));
        }
    }

    public ApplicationCode(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public ApplicationCode(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
        json.put("applicationCodeId", getApplicationCodeId());
        json.put("applicationCodeAid", getApplicationCodeAid());
        json.put("applicationCodeTime", getApplicationCodeTime());
        json.put("applicationCodeFile", getApplicationCodeFile());
        json.put("data", getList());
        return json;
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
