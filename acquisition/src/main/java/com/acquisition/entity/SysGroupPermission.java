package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.SysGroupPermissionDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_group_permission",desc="sysGroupPermission")
public class SysGroupPermission implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_group_permission_id",desc="sysGroupPermissionId")
    private java.lang.Long sysGroupPermissionId;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_pid",desc="sysGroupPermissionPid")
    private java.lang.Long sysGroupPermissionPid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_gid",desc="sysGroupPermissionGid")
    private java.lang.Long sysGroupPermissionGid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_time",desc="sysGroupPermissionTime")
    private java.lang.String sysGroupPermissionTime;
    private List<?> list;
    public SysGroupPermission() {}

    public SysGroupPermission(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysGroupPermission(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysGroupPermissionId");
        if (str!=null && !str.isEmpty()) {
            setSysGroupPermissionId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupPermissionPid");
        if (str!=null && !str.isEmpty()) {
            setSysGroupPermissionPid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupPermissionGid");
        if (str!=null && !str.isEmpty()) {
            setSysGroupPermissionGid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupPermissionTime");
        if (str!=null && !str.isEmpty()) {
            setSysGroupPermissionTime(java.lang.String.valueOf(str));
        }
    }

    public SysGroupPermission(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysGroupPermission(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.save(this);
    }
    public int delete(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.delete(this);
    }
    public int update(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.update(this);
    }
    public int saveAsync(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.saveAsync(this);
    }
    public int deleteAsync(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.deleteAsync(this);
    }
    public int updateAsync(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.updateAsync(this);
    }
    public List<SysGroupPermission> query(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.query(this);
    }
    public List<SysGroupPermission> query(SysGroupPermissionDao sysGroupPermissionDao,int page,int size) throws Exception {
        return sysGroupPermissionDao.query(this,page,size);
    }
    public List<SysGroupPermission> query(SysGroupPermissionDao sysGroupPermissionDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysGroupPermissionDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysGroupPermissionDao sysGroupPermissionDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysGroupPermissionDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysGroupPermissionDao sysGroupPermissionDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysGroupPermissionDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysGroupPermission find(SysGroupPermissionDao sysGroupPermissionDao) throws Exception {
        return sysGroupPermissionDao.find(this);
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
        json.put("sysGroupPermissionId", getSysGroupPermissionId());
        json.put("sysGroupPermissionPid", getSysGroupPermissionPid());
        json.put("sysGroupPermissionGid", getSysGroupPermissionGid());
        json.put("sysGroupPermissionTime", getSysGroupPermissionTime());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysGroupPermissionId() {
        return sysGroupPermissionId;
    }
    public SysGroupPermission setSysGroupPermissionId(java.lang.Long sysGroupPermissionId) {
        int size0 = Tools.textLength(sysGroupPermissionId);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupPermission.sysGroupPermissionId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupPermissionId);
        }
        this.sysGroupPermissionId = sysGroupPermissionId;
        return this;
    }
    public SysGroupPermission setSysGroupPermissionIdUnsafe(java.lang.Long sysGroupPermissionId) {
        this.sysGroupPermissionId = sysGroupPermissionId;
        return this;
    }
    public java.lang.Long getSysGroupPermissionPid() {
        return sysGroupPermissionPid;
    }
    public SysGroupPermission setSysGroupPermissionPid(java.lang.Long sysGroupPermissionPid) {
        int size0 = Tools.textLength(sysGroupPermissionPid);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupPermission.sysGroupPermissionPid最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupPermissionPid);
        }
        this.sysGroupPermissionPid = sysGroupPermissionPid;
        return this;
    }
    public SysGroupPermission setSysGroupPermissionPidUnsafe(java.lang.Long sysGroupPermissionPid) {
        this.sysGroupPermissionPid = sysGroupPermissionPid;
        return this;
    }
    public java.lang.Long getSysGroupPermissionGid() {
        return sysGroupPermissionGid;
    }
    public SysGroupPermission setSysGroupPermissionGid(java.lang.Long sysGroupPermissionGid) {
        int size0 = Tools.textLength(sysGroupPermissionGid);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupPermission.sysGroupPermissionGid最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupPermissionGid);
        }
        this.sysGroupPermissionGid = sysGroupPermissionGid;
        return this;
    }
    public SysGroupPermission setSysGroupPermissionGidUnsafe(java.lang.Long sysGroupPermissionGid) {
        this.sysGroupPermissionGid = sysGroupPermissionGid;
        return this;
    }
    public java.lang.String getSysGroupPermissionTime() {
        return sysGroupPermissionTime;
    }
    public SysGroupPermission setSysGroupPermissionTime(java.lang.String sysGroupPermissionTime) {
        int size0 = Tools.textLength(sysGroupPermissionTime);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupPermission.sysGroupPermissionTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupPermissionTime);
        }
        this.sysGroupPermissionTime = sysGroupPermissionTime;
        return this;
    }
    public SysGroupPermission setSysGroupPermissionTimeUnsafe(java.lang.String sysGroupPermissionTime) {
        this.sysGroupPermissionTime = sysGroupPermissionTime;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
