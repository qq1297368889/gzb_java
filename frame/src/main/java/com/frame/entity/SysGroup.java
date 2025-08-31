package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysGroupDao;
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
@EntityAttribute(name="sys_group",desc="sysGroup")
public class SysGroup implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_group_id",desc="sysGroupId")
    private java.lang.Long sysGroupId;
    @EntityAttribute(key=false,size = 255,name="sys_group_name",desc="sysGroupName")
    private java.lang.String sysGroupName;
    @EntityAttribute(key=false,size = 19,name="sys_group_type",desc="sysGroupType")
    private java.lang.Long sysGroupType;
    @EntityAttribute(key=false,size = 255,name="sys_group_desc",desc="sysGroupDesc")
    private java.lang.String sysGroupDesc;
    @EntityAttribute(key=false,size = 19,name="sys_group_sup",desc="sysGroupSup")
    private java.lang.Long sysGroupSup;
    private List<?> list;
   public SysGroup() {}

    public SysGroup(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysGroup(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysGroup(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysGroup(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.save(this);
    }
    public int delete(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.delete(this);
    }
    public int update(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.update(this);
    }
    public int saveAsync(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.saveAsync(this);
    }
    public int deleteAsync(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.deleteAsync(this);
    }
    public int updateAsync(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.updateAsync(this);
    }
    public List<SysGroup> query(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.query(this);
    }
    public List<SysGroup> query(SysGroupDao sysGroupDao,int page,int size) throws Exception {
        return sysGroupDao.query(this,page,size);
    }
    public List<SysGroup> query(SysGroupDao sysGroupDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysGroupDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysGroupDao sysGroupDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysGroupDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysGroupDao sysGroupDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysGroupDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysGroup find(SysGroupDao sysGroupDao) throws Exception {
        return sysGroupDao.find(this);
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
        result.set("sysGroupId", sysGroupId);
        result.set("sysGroupName", sysGroupName);
        result.set("sysGroupType", sysGroupType);
        result.set("sysGroupDesc", sysGroupDesc);
        result.set("sysGroupSup", sysGroupSup);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysGroupId=result.getLong("sysGroupId", null);
        this.sysGroupName=result.getString("sysGroupName", null);
        this.sysGroupType=result.getLong("sysGroupType", null);
        this.sysGroupDesc=result.getString("sysGroupDesc", null);
        this.sysGroupSup=result.getLong("sysGroupSup", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_group_id");
            if (temp!=null) {
                this.sysGroupId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_name");
            if (temp!=null) {
                this.sysGroupName=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_type");
            if (temp!=null) {
                this.sysGroupType=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_desc");
            if (temp!=null) {
                this.sysGroupDesc=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_sup");
            if (temp!=null) {
                this.sysGroupSup=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }
    public SysGroup setSysGroupId(java.lang.Long sysGroupId) {
        int size0 = Tools.textLength(sysGroupId);
        if (size0 > 19) {
            throw new RuntimeException("SysGroup.sysGroupId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupId);
        }
        this.sysGroupId = sysGroupId;
        return this;
    }
    public SysGroup setSysGroupIdUnsafe(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
        return this;
    }
    public java.lang.String getSysGroupName() {
        return sysGroupName;
    }
    public SysGroup setSysGroupName(java.lang.String sysGroupName) {
        int size0 = Tools.textLength(sysGroupName);
        if (size0 > 255) {
            throw new RuntimeException("SysGroup.sysGroupName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupName);
        }
        this.sysGroupName = sysGroupName;
        return this;
    }
    public SysGroup setSysGroupNameUnsafe(java.lang.String sysGroupName) {
        this.sysGroupName = sysGroupName;
        return this;
    }
    public java.lang.Long getSysGroupType() {
        return sysGroupType;
    }
    public SysGroup setSysGroupType(java.lang.Long sysGroupType) {
        int size0 = Tools.textLength(sysGroupType);
        if (size0 > 19) {
            throw new RuntimeException("SysGroup.sysGroupType最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupType);
        }
        this.sysGroupType = sysGroupType;
        return this;
    }
    public SysGroup setSysGroupTypeUnsafe(java.lang.Long sysGroupType) {
        this.sysGroupType = sysGroupType;
        return this;
    }
    public java.lang.String getSysGroupDesc() {
        return sysGroupDesc;
    }
    public SysGroup setSysGroupDesc(java.lang.String sysGroupDesc) {
        int size0 = Tools.textLength(sysGroupDesc);
        if (size0 > 255) {
            throw new RuntimeException("SysGroup.sysGroupDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupDesc);
        }
        this.sysGroupDesc = sysGroupDesc;
        return this;
    }
    public SysGroup setSysGroupDescUnsafe(java.lang.String sysGroupDesc) {
        this.sysGroupDesc = sysGroupDesc;
        return this;
    }
    public java.lang.Long getSysGroupSup() {
        return sysGroupSup;
    }
    public SysGroup setSysGroupSup(java.lang.Long sysGroupSup) {
        int size0 = Tools.textLength(sysGroupSup);
        if (size0 > 19) {
            throw new RuntimeException("SysGroup.sysGroupSup最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupSup);
        }
        this.sysGroupSup = sysGroupSup;
        return this;
    }
    public SysGroup setSysGroupSupUnsafe(java.lang.Long sysGroupSup) {
        this.sysGroupSup = sysGroupSup;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
