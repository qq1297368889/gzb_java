package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysRoleGroupDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_role_group",desc="sysRoleGroup")
public class SysRoleGroup implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_role_group_id",desc="",type="java.lang.Long")
    private java.lang.Long sysRoleGroupId;
    @EntityAttribute(key=false,size = 19,name="sys_role_group_rid",desc="",type="java.lang.Long")
    private java.lang.Long sysRoleGroupRid;
    @EntityAttribute(key=false,size = 19,name="sys_role_group_gid",desc="",type="java.lang.Long")
    private java.lang.Long sysRoleGroupGid;
    private Object data;
   public SysRoleGroup() {}

    public SysRoleGroup(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysRoleGroup(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysRoleGroup(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.save(this);
    }
    public int delete(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.delete(this);
    }
    public int update(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.update(this);
    }
    public int saveAsync(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.saveAsync(this);
    }
    public int deleteAsync(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.deleteAsync(this);
    }
    public int updateAsync(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.updateAsync(this);
    }
    public List<SysRoleGroup> query(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.query(this);
    }
    public List<SysRoleGroup> query(SysRoleGroupDao sysRoleGroupDao,int page,int size) throws Exception {
        return sysRoleGroupDao.query(this,page,size);
    }
    public List<SysRoleGroup> query(SysRoleGroupDao sysRoleGroupDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysRoleGroupDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysRoleGroupDao sysRoleGroupDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysRoleGroupDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysRoleGroupDao sysRoleGroupDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysRoleGroupDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysRoleGroup find(SysRoleGroupDao sysRoleGroupDao) throws Exception {
        return sysRoleGroupDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(74);
       boolean app01=false;
        sb.append("{");
        if (this.sysRoleGroupId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleGroupId\":\"").append(sysRoleGroupId).append("\"");
        }
        if (this.sysRoleGroupRid != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleGroupRid\":\"").append(sysRoleGroupRid).append("\"");
        }
        if (this.sysRoleGroupGid != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleGroupGid\":\"").append(sysRoleGroupGid).append("\"");
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
        result.set("sysRoleGroupId", sysRoleGroupId);
        result.set("sysRoleGroupRid", sysRoleGroupRid);
        result.set("sysRoleGroupGid", sysRoleGroupGid);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysRoleGroupId=result.getLong("sysRoleGroupId", null);
        this.sysRoleGroupRid=result.getLong("sysRoleGroupRid", null);
        this.sysRoleGroupGid=result.getLong("sysRoleGroupGid", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysRoleGroupId() {
        return sysRoleGroupId;
    }
    public SysRoleGroup setSysRoleGroupId(java.lang.Long sysRoleGroupId) {
        this.sysRoleGroupId = sysRoleGroupId;
        return this;
    }
    public java.lang.Long getSysRoleGroupRid() {
        return sysRoleGroupRid;
    }
    public SysRoleGroup setSysRoleGroupRid(java.lang.Long sysRoleGroupRid) {
        this.sysRoleGroupRid = sysRoleGroupRid;
        return this;
    }
    public java.lang.Long getSysRoleGroupGid() {
        return sysRoleGroupGid;
    }
    public SysRoleGroup setSysRoleGroupGid(java.lang.Long sysRoleGroupGid) {
        this.sysRoleGroupGid = sysRoleGroupGid;
        return this;
    }
    public SysRoleGroup setList(List<?> data) {
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

    public SysRoleGroup setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleGroup putMap(String key, Object value) {
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
    public SysRoleGroup setData(Object data) {
        this.data = data;
        return this;
    }
}
