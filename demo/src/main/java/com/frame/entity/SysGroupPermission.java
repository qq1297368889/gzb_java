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
    @EntityAttribute(key=true,size = 19,name="sys_group_permission_id",desc="",type="java.lang.Long")
    private java.lang.Long sysGroupPermissionId;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_pid",desc="",type="java.lang.Long")
    private java.lang.Long sysGroupPermissionPid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_gid",desc="",type="java.lang.Long")
    private java.lang.Long sysGroupPermissionGid;
    @EntityAttribute(key=false,size = 19,name="sys_group_permission_time",desc="",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysGroupPermissionTime;
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
        StringBuilder sb = new StringBuilder(124);
       boolean app01=false;
        sb.append("{");
        if (this.sysGroupPermissionId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupPermissionId\":\"").append(sysGroupPermissionId).append("\"");
        }
        if (this.sysGroupPermissionPid != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupPermissionPid\":\"").append(sysGroupPermissionPid).append("\"");
        }
        if (this.sysGroupPermissionGid != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupPermissionGid\":\"").append(sysGroupPermissionGid).append("\"");
        }
        if (this.sysGroupPermissionTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupPermissionTime\":");
            sb.append(Tools.toJson(sysGroupPermissionTime));        }
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
        result.set("sysGroupPermissionId", sysGroupPermissionId);
        result.set("sysGroupPermissionPid", sysGroupPermissionPid);
        result.set("sysGroupPermissionGid", sysGroupPermissionGid);
        result.set("sysGroupPermissionTime", sysGroupPermissionTime);
        result.set(Config.get("json.entity.data","data"), data);
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
        this.sysGroupPermissionTime=result.getLocalDateTime("sysGroupPermissionTime", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
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
    public java.time.LocalDateTime getSysGroupPermissionTime() {
        return sysGroupPermissionTime;
    }
    public SysGroupPermission setSysGroupPermissionTime(java.time.LocalDateTime sysGroupPermissionTime) {
        this.sysGroupPermissionTime = sysGroupPermissionTime;
        return this;
    }
    public SysGroupPermission setList(List<?> data) {
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

    public SysGroupPermission setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroupPermission putMap(String key, Object value) {
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
    public SysGroupPermission setData(Object data) {
        this.data = data;
        return this;
    }
}
