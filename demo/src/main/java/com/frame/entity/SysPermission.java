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
    @EntityAttribute(key=true,size = 19,name="sys_permission_id",desc="权限名称",type="java.lang.Long")
    private java.lang.Long sysPermissionId;
    @EntityAttribute(key=false,size = 255,name="sys_permission_name",desc="权限名称",type="java.lang.String")
    private java.lang.String sysPermissionName;
    @EntityAttribute(key=false,size = 255,name="sys_permission_data",desc="权限数据",type="java.lang.String")
    private java.lang.String sysPermissionData;
    @EntityAttribute(key=false,size = 19,name="sys_permission_type",desc="权限类型",type="java.lang.Long")
    private java.lang.Long sysPermissionType;
    @EntityAttribute(key=false,size = 255,name="sys_permission_desc",desc="权限备注",type="java.lang.String")
    private java.lang.String sysPermissionDesc;
    @EntityAttribute(key=false,size = 19,name="sys_permission_sup",desc="权限上级",type="java.lang.Long")
    private java.lang.Long sysPermissionSup;
    @EntityAttribute(key=false,size = 19,name="sys_permission_sort",desc="权限排序",type="java.lang.Long")
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
        StringBuilder sb = new StringBuilder(186);
       boolean app01=false;
        sb.append("{");
        if (this.sysPermissionId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionId\":\"").append(sysPermissionId).append("\"");
        }
        if (this.sysPermissionName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionName\":");
            sb.append(Tools.toJson(sysPermissionName));        }
        if (this.sysPermissionData != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionData\":");
            sb.append(Tools.toJson(sysPermissionData));        }
        if (this.sysPermissionType != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionType\":\"").append(sysPermissionType).append("\"");
        }
        if (this.sysPermissionDesc != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionDesc\":");
            sb.append(Tools.toJson(sysPermissionDesc));        }
        if (this.sysPermissionSup != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionSup\":\"").append(sysPermissionSup).append("\"");
        }
        if (this.sysPermissionSort != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysPermissionSort\":\"").append(sysPermissionSort).append("\"");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                if(app01){sb.append(",");}app01=true;
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(Tools.toJson(entry.getValue()));
            }
        }else if(this.data != null){
            if(app01){sb.append(",");}app01=true;
            sb.append("\"").append(Config.get("json.entity.data","data")).append("\":");
            sb.append(Tools.toJson(this.data));
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
        result.set(Config.get("json.entity.data","data"), data);
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
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
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
            throw new gzb.exception.GzbException0("SysPermission.sysPermissionName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionName);
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
            throw new gzb.exception.GzbException0("SysPermission.sysPermissionData最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionData);
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
            throw new gzb.exception.GzbException0("SysPermission.sysPermissionDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysPermissionDesc);
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
            // 自动初始化
            this.data = new HashMap<>();
        } else if (!(this.data instanceof Map)) {
            // 无法转换，抛出业务异常
            throw new gzb.exception.GzbException0("无法将"+this.data+" 转换为MAP");
        }
        // 安全地进行 put 操作 (可能需要抑制一下警告)
        @SuppressWarnings("unchecked")
        Map<String, Object> mapData = (Map<String, Object>) this.data;
        mapData.put(key, value);
        return this;
    }
    public Object getData() {
        return data;
    }
    public SysPermission setData(Object data) {
        this.data = data;
        return this;
    }
}
