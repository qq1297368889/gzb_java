package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysOptionDao;
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
@EntityAttribute(name="sys_option",desc="sysOption")
public class SysOption implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_option_id",desc="sysOptionId")
    private java.lang.Long sysOptionId;
    @EntityAttribute(key=false,size = 255,name="sys_option_key",desc="sysOptionKey")
    private java.lang.String sysOptionKey;
    @EntityAttribute(key=false,size = 255,name="sys_option_title",desc="sysOptionTitle")
    private java.lang.String sysOptionTitle;
    @EntityAttribute(key=false,size = 255,name="sys_option_value",desc="sysOptionValue")
    private java.lang.String sysOptionValue;
    @EntityAttribute(key=false,size = 19,name="sys_option_state",desc="sysOptionState")
    private java.lang.Long sysOptionState;
    private List<?> list;
   public SysOption() {}

    public SysOption(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysOption(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysOption(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysOption(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.save(this);
    }
    public int delete(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.delete(this);
    }
    public int update(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.update(this);
    }
    public int saveAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.saveAsync(this);
    }
    public int deleteAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.deleteAsync(this);
    }
    public int updateAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.updateAsync(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.query(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,int page,int size) throws Exception {
        return sysOptionDao.query(this,page,size);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysOptionDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysOption find(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.find(this);
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
        result.set("sysOptionId", sysOptionId);
        result.set("sysOptionKey", sysOptionKey);
        result.set("sysOptionTitle", sysOptionTitle);
        result.set("sysOptionValue", sysOptionValue);
        result.set("sysOptionState", sysOptionState);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysOptionId=result.getLong("sysOptionId", null);
        this.sysOptionKey=result.getString("sysOptionKey", null);
        this.sysOptionTitle=result.getString("sysOptionTitle", null);
        this.sysOptionValue=result.getString("sysOptionValue", null);
        this.sysOptionState=result.getLong("sysOptionState", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_option_id");
            if (temp!=null) {
                this.sysOptionId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_option_key");
            if (temp!=null) {
                this.sysOptionKey=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_option_title");
            if (temp!=null) {
                this.sysOptionTitle=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_option_value");
            if (temp!=null) {
                this.sysOptionValue=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_option_state");
            if (temp!=null) {
                this.sysOptionState=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysOptionId() {
        return sysOptionId;
    }
    public SysOption setSysOptionId(java.lang.Long sysOptionId) {
        int size0 = Tools.textLength(sysOptionId);
        if (size0 > 19) {
            throw new RuntimeException("SysOption.sysOptionId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysOptionId);
        }
        this.sysOptionId = sysOptionId;
        return this;
    }
    public SysOption setSysOptionIdUnsafe(java.lang.Long sysOptionId) {
        this.sysOptionId = sysOptionId;
        return this;
    }
    public java.lang.String getSysOptionKey() {
        return sysOptionKey;
    }
    public SysOption setSysOptionKey(java.lang.String sysOptionKey) {
        int size0 = Tools.textLength(sysOptionKey);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionKey最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionKey);
        }
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public SysOption setSysOptionKeyUnsafe(java.lang.String sysOptionKey) {
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public java.lang.String getSysOptionTitle() {
        return sysOptionTitle;
    }
    public SysOption setSysOptionTitle(java.lang.String sysOptionTitle) {
        int size0 = Tools.textLength(sysOptionTitle);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionTitle);
        }
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public SysOption setSysOptionTitleUnsafe(java.lang.String sysOptionTitle) {
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public java.lang.String getSysOptionValue() {
        return sysOptionValue;
    }
    public SysOption setSysOptionValue(java.lang.String sysOptionValue) {
        int size0 = Tools.textLength(sysOptionValue);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionValue最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionValue);
        }
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public SysOption setSysOptionValueUnsafe(java.lang.String sysOptionValue) {
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public java.lang.Long getSysOptionState() {
        return sysOptionState;
    }
    public SysOption setSysOptionState(java.lang.Long sysOptionState) {
        int size0 = Tools.textLength(sysOptionState);
        if (size0 > 19) {
            throw new RuntimeException("SysOption.sysOptionState最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysOptionState);
        }
        this.sysOptionState = sysOptionState;
        return this;
    }
    public SysOption setSysOptionStateUnsafe(java.lang.Long sysOptionState) {
        this.sysOptionState = sysOptionState;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
