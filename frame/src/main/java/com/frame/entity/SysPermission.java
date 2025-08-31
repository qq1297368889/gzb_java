package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysPermissionDao;
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
@EntityAttribute(name="sys_permission",desc="sysPermission")
public class SysPermission implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_permission_id",desc="sysPermissionId")
    private java.lang.Long sysPermissionId;
    @EntityAttribute(key=false,size = 255,name="sys_permission_name",desc="sysPermissionName")
    private java.lang.String sysPermissionName;
    @EntityAttribute(key=false,size = 255,name="sys_permission_data",desc="sysPermissionData")
    private java.lang.String sysPermissionData;
    @EntityAttribute(key=false,size = 19,name="sys_permission_type",desc="sysPermissionType")
    private java.lang.Long sysPermissionType;
    @EntityAttribute(key=false,size = 255,name="sys_permission_desc",desc="sysPermissionDesc")
    private java.lang.String sysPermissionDesc;
    @EntityAttribute(key=false,size = 19,name="sys_permission_sup",desc="sysPermissionSup")
    private java.lang.Long sysPermissionSup;
    @EntityAttribute(key=false,size = 19,name="sys_permission_sort",desc="sysPermissionSort")
    private java.lang.Long sysPermissionSort;
    private List<?> list;
   public SysPermission() {}

    public SysPermission(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysPermission(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysPermission(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysPermission(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.save(this);
    }
    public int delete(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.delete(this);
    }
    public int update(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.update(this);
    }
    public int saveAsync(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.saveAsync(this);
    }
    public int deleteAsync(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.deleteAsync(this);
    }
    public int updateAsync(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.updateAsync(this);
    }
    public List<SysPermission> query(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.query(this);
    }
    public List<SysPermission> query(SysPermissionDao sysPermissionDao,int page,int size) throws Exception {
        return sysPermissionDao.query(this,page,size);
    }
    public List<SysPermission> query(SysPermissionDao sysPermissionDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysPermissionDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysPermissionDao sysPermissionDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysPermissionDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysPermissionDao sysPermissionDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysPermissionDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysPermission find(SysPermissionDao sysPermissionDao) throws Exception {
        return sysPermissionDao.find(this);
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
        result.set("sysPermissionId", sysPermissionId);
        result.set("sysPermissionName", sysPermissionName);
        result.set("sysPermissionData", sysPermissionData);
        result.set("sysPermissionType", sysPermissionType);
        result.set("sysPermissionDesc", sysPermissionDesc);
        result.set("sysPermissionSup", sysPermissionSup);
        result.set("sysPermissionSort", sysPermissionSort);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysPermissionId=result.getLong("sysPermissionId", null);
        this.sysPermissionName=result.getString("sysPermissionName", null);
        this.sysPermissionData=result.getString("sysPermissionData", null);
        this.sysPermissionType=result.getLong("sysPermissionType", null);
        this.sysPermissionDesc=result.getString("sysPermissionDesc", null);
        this.sysPermissionSup=result.getLong("sysPermissionSup", null);
        this.sysPermissionSort=result.getLong("sysPermissionSort", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_permission_id");
            if (temp!=null) {
                this.sysPermissionId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_name");
            if (temp!=null) {
                this.sysPermissionName=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_data");
            if (temp!=null) {
                this.sysPermissionData=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_type");
            if (temp!=null) {
                this.sysPermissionType=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_desc");
            if (temp!=null) {
                this.sysPermissionDesc=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_sup");
            if (temp!=null) {
                this.sysPermissionSup=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_permission_sort");
            if (temp!=null) {
                this.sysPermissionSort=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysPermissionId() {
        return sysPermissionId;
    }
    public SysPermission setSysPermissionId(java.lang.Long sysPermissionId) {
        int size0 = Tools.textLength(sysPermissionId);
        if (size0 > 19) {
            throw new RuntimeException("SysPermission.sysPermissionId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysPermissionId);
        }
        this.sysPermissionId = sysPermissionId;
        return this;
    }
    public SysPermission setSysPermissionIdUnsafe(java.lang.Long sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
        return this;
    }
    public java.lang.String getSysPermissionName() {
        return sysPermissionName;
    }
    public SysPermission setSysPermissionName(java.lang.String sysPermissionName) {
        int size0 = Tools.textLength(sysPermissionName);
        if (size0 > 255) {
            throw new RuntimeException("SysPermission.sysPermissionName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionName);
        }
        this.sysPermissionName = sysPermissionName;
        return this;
    }
    public SysPermission setSysPermissionNameUnsafe(java.lang.String sysPermissionName) {
        this.sysPermissionName = sysPermissionName;
        return this;
    }
    public java.lang.String getSysPermissionData() {
        return sysPermissionData;
    }
    public SysPermission setSysPermissionData(java.lang.String sysPermissionData) {
        int size0 = Tools.textLength(sysPermissionData);
        if (size0 > 255) {
            throw new RuntimeException("SysPermission.sysPermissionData最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionData);
        }
        this.sysPermissionData = sysPermissionData;
        return this;
    }
    public SysPermission setSysPermissionDataUnsafe(java.lang.String sysPermissionData) {
        this.sysPermissionData = sysPermissionData;
        return this;
    }
    public java.lang.Long getSysPermissionType() {
        return sysPermissionType;
    }
    public SysPermission setSysPermissionType(java.lang.Long sysPermissionType) {
        int size0 = Tools.textLength(sysPermissionType);
        if (size0 > 19) {
            throw new RuntimeException("SysPermission.sysPermissionType最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysPermissionType);
        }
        this.sysPermissionType = sysPermissionType;
        return this;
    }
    public SysPermission setSysPermissionTypeUnsafe(java.lang.Long sysPermissionType) {
        this.sysPermissionType = sysPermissionType;
        return this;
    }
    public java.lang.String getSysPermissionDesc() {
        return sysPermissionDesc;
    }
    public SysPermission setSysPermissionDesc(java.lang.String sysPermissionDesc) {
        int size0 = Tools.textLength(sysPermissionDesc);
        if (size0 > 255) {
            throw new RuntimeException("SysPermission.sysPermissionDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionDesc);
        }
        this.sysPermissionDesc = sysPermissionDesc;
        return this;
    }
    public SysPermission setSysPermissionDescUnsafe(java.lang.String sysPermissionDesc) {
        this.sysPermissionDesc = sysPermissionDesc;
        return this;
    }
    public java.lang.Long getSysPermissionSup() {
        return sysPermissionSup;
    }
    public SysPermission setSysPermissionSup(java.lang.Long sysPermissionSup) {
        int size0 = Tools.textLength(sysPermissionSup);
        if (size0 > 19) {
            throw new RuntimeException("SysPermission.sysPermissionSup最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysPermissionSup);
        }
        this.sysPermissionSup = sysPermissionSup;
        return this;
    }
    public SysPermission setSysPermissionSupUnsafe(java.lang.Long sysPermissionSup) {
        this.sysPermissionSup = sysPermissionSup;
        return this;
    }
    public java.lang.Long getSysPermissionSort() {
        return sysPermissionSort;
    }
    public SysPermission setSysPermissionSort(java.lang.Long sysPermissionSort) {
        int size0 = Tools.textLength(sysPermissionSort);
        if (size0 > 19) {
            throw new RuntimeException("SysPermission.sysPermissionSort最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysPermissionSort);
        }
        this.sysPermissionSort = sysPermissionSort;
        return this;
    }
    public SysPermission setSysPermissionSortUnsafe(java.lang.Long sysPermissionSort) {
        this.sysPermissionSort = sysPermissionSort;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
