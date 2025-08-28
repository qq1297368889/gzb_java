package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysPermissionDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_permission",desc="sysPermission")
public class SysPermission implements Serializable{
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

    public SysPermission(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysPermission(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysPermissionId");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionName");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionData");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionData(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionType");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionType(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionDesc");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionSup");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionSup(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysPermissionSort");
        if (str!=null && !str.isEmpty()) {
            setSysPermissionSort(java.lang.Long.valueOf(str));
        }
    }

    public SysPermission(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysPermission(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
        json.put("sysPermissionId", getSysPermissionId());
        json.put("sysPermissionName", getSysPermissionName());
        json.put("sysPermissionData", getSysPermissionData());
        json.put("sysPermissionType", getSysPermissionType());
        json.put("sysPermissionDesc", getSysPermissionDesc());
        json.put("sysPermissionSup", getSysPermissionSup());
        json.put("sysPermissionSort", getSysPermissionSort());
        json.put("data", getList());
        return json;
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
