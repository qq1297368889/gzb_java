package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysPermissionDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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
    private Object data;
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

    public SysPermission(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysPermissionId != null) {
            sb.append("\"sysPermissionId\":\"").append(sysPermissionId).append("\",");
        }
        if (this.sysPermissionName != null) {
            sb.append("\"sysPermissionName\":\"").append(sysPermissionName).append("\",");
        }
        if (this.sysPermissionData != null) {
            sb.append("\"sysPermissionData\":\"").append(sysPermissionData).append("\",");
        }
        if (this.sysPermissionType != null) {
            sb.append("\"sysPermissionType\":\"").append(sysPermissionType).append("\",");
        }
        if (this.sysPermissionDesc != null) {
            sb.append("\"sysPermissionDesc\":\"").append(sysPermissionDesc).append("\",");
        }
        if (this.sysPermissionSup != null) {
            sb.append("\"sysPermissionSup\":\"").append(sysPermissionSup).append("\",");
        }
        if (this.sysPermissionSort != null) {
            sb.append("\"sysPermissionSort\":\"").append(sysPermissionSort).append("\",");
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
        result.set("sysPermissionId", sysPermissionId);
        result.set("sysPermissionName", sysPermissionName);
        result.set("sysPermissionData", sysPermissionData);
        result.set("sysPermissionType", sysPermissionType);
        result.set("sysPermissionDesc", sysPermissionDesc);
        result.set("sysPermissionSup", sysPermissionSup);
        result.set("sysPermissionSort", sysPermissionSort);
        result.set(dataName, data);
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
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_permission_id")) {
            temp=resultSet.getString("sys_permission_id");
            if (temp!=null) {
                this.sysPermissionId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_name")) {
            temp=resultSet.getString("sys_permission_name");
            if (temp!=null) {
                this.sysPermissionName=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_data")) {
            temp=resultSet.getString("sys_permission_data");
            if (temp!=null) {
                this.sysPermissionData=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_type")) {
            temp=resultSet.getString("sys_permission_type");
            if (temp!=null) {
                this.sysPermissionType=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_desc")) {
            temp=resultSet.getString("sys_permission_desc");
            if (temp!=null) {
                this.sysPermissionDesc=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_sup")) {
            temp=resultSet.getString("sys_permission_sup");
            if (temp!=null) {
                this.sysPermissionSup=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_permission_sort")) {
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
        this.sysPermissionSup = sysPermissionSup;
        return this;
    }
    public java.lang.Long getSysPermissionSort() {
        return sysPermissionSort;
    }
    public SysPermission setSysPermissionSort(java.lang.Long sysPermissionSort) {
        this.sysPermissionSort = sysPermissionSort;
        return this;
    }
    public SysPermission setList(List<?> data) {
        this.data = data;
        return this;
    }

    public List<?> getList() {
          if (data instanceof List) {
                return (List<?>) data;
          }
        return null;
    }
    public Map<String, Object> getMap() {
          if (data instanceof Map) {
                return (Map<String, Object>) data;
          }
        return null;
    }

    public SysPermission setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysPermission putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysPermission setData(Object data) {
        this.data = data;
        return this;
    }}
