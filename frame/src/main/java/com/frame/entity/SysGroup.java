package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysGroupDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_group",desc="sysGroup")
public class SysGroup implements Serializable{
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

    public SysGroup(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysGroup(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysGroupId");
        if (str!=null && !str.isEmpty()) {
            setSysGroupId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupName");
        if (str!=null && !str.isEmpty()) {
            setSysGroupName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupType");
        if (str!=null && !str.isEmpty()) {
            setSysGroupType(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupDesc");
        if (str!=null && !str.isEmpty()) {
            setSysGroupDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupSup");
        if (str!=null && !str.isEmpty()) {
            setSysGroupSup(java.lang.Long.valueOf(str));
        }
    }

    public SysGroup(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysGroup(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
        json.put("sysGroupId", getSysGroupId());
        json.put("sysGroupName", getSysGroupName());
        json.put("sysGroupType", getSysGroupType());
        json.put("sysGroupDesc", getSysGroupDesc());
        json.put("sysGroupSup", getSysGroupSup());
        json.put("data", getList());
        return json;
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
