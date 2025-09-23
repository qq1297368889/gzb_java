package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysGroupPermissionDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_group_permission",desc="sysGroupPermission")
public class SysGroupPermission implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_group_permission_id",desc="sysGroupPermissionId")
    private java.lang.Long sysGroupPermissionId;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_pid",desc="sysGroupPermissionPid")
    private java.lang.Long sysGroupPermissionPid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_gid",desc="sysGroupPermissionGid")
    private java.lang.Long sysGroupPermissionGid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_time",desc="sysGroupPermissionTime")
    private java.lang.String sysGroupPermissionTime;
    private Object data;
   public SysGroupPermission() {}

    public SysGroupPermission(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysGroupPermission(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysGroupPermission(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysGroupPermission(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysGroupPermissionId != null) {
            sb.append("\"sysGroupPermissionId\":\"").append(sysGroupPermissionId).append("\",");
        }
        if (this.sysGroupPermissionPid != null) {
            sb.append("\"sysGroupPermissionPid\":\"").append(sysGroupPermissionPid).append("\",");
        }
        if (this.sysGroupPermissionGid != null) {
            sb.append("\"sysGroupPermissionGid\":\"").append(sysGroupPermissionGid).append("\",");
        }
        if (this.sysGroupPermissionTime != null) {
            sb.append("\"sysGroupPermissionTime\":\"").append(sysGroupPermissionTime).append("\",");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":").append(Tools.toJson(entry.getValue())).append(",");
            }
        }else if(this.data != null){
            sb.append("\"").append(dataName).append("\":").append(Tools.toJson(this.data)).append(",");
        }
        if (sb.length()>1) {
            sb.delete(sb.length()-1, sb.length());
        }
       return sb.append("}").toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysGroupPermissionId", sysGroupPermissionId);
        result.set("sysGroupPermissionPid", sysGroupPermissionPid);
        result.set("sysGroupPermissionGid", sysGroupPermissionGid);
        result.set("sysGroupPermissionTime", sysGroupPermissionTime);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysGroupPermissionId=result.getLong("sysGroupPermissionId", null);
        this.sysGroupPermissionPid=result.getLong("sysGroupPermissionPid", null);
        this.sysGroupPermissionGid=result.getLong("sysGroupPermissionGid", null);
        this.sysGroupPermissionTime=result.getString("sysGroupPermissionTime", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_group_permission_id")) {
            temp=resultSet.getString("sys_group_permission_id");
            if (temp!=null) {
                this.sysGroupPermissionId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_permission_pid")) {
            temp=resultSet.getString("sys_group_permission_pid");
            if (temp!=null) {
                this.sysGroupPermissionPid=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_permission_gid")) {
            temp=resultSet.getString("sys_group_permission_gid");
            if (temp!=null) {
                this.sysGroupPermissionGid=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_permission_time")) {
            temp=resultSet.getString("sys_group_permission_time");
            if (temp!=null) {
                this.sysGroupPermissionTime=java.lang.String.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysGroupPermissionId() {
        return sysGroupPermissionId;
    }
    public SysGroupPermission setSysGroupPermissionId(java.lang.Long sysGroupPermissionId) {
        this.sysGroupPermissionId = sysGroupPermissionId;
        return this;
    }
    public java.lang.Long getSysGroupPermissionPid() {
        return sysGroupPermissionPid;
    }
    public SysGroupPermission setSysGroupPermissionPid(java.lang.Long sysGroupPermissionPid) {
        this.sysGroupPermissionPid = sysGroupPermissionPid;
        return this;
    }
    public java.lang.Long getSysGroupPermissionGid() {
        return sysGroupPermissionGid;
    }
    public SysGroupPermission setSysGroupPermissionGid(java.lang.Long sysGroupPermissionGid) {
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
        return (List<?>) data;
    }

    public SysGroupPermission setList(List<?> data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) data;
    }

    public SysGroupPermission setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroupPermission putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysGroupPermission setData(Object data) {
        this.data = data;
        return this;
    }}
