package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysRoleDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_role",desc="sysRole")
public class SysRole implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_role_id",desc="",type="java.lang.Long")
    private java.lang.Long sysRoleId;
    @EntityAttribute(key=false,size = 100,name="sys_role_name",desc="",type="java.lang.String")
    private java.lang.String sysRoleName;
    @EntityAttribute(key=false,size = 19,name="sys_role_state",desc="",type="java.lang.Long")
    private java.lang.Long sysRoleState;
    @EntityAttribute(key=false,size = 19,name="sys_role_time",desc="",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysRoleTime;
    private Object data;
   public SysRole() {}

    public SysRole(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysRole(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysRole(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.save(this);
    }
    public int delete(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.delete(this);
    }
    public int update(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.update(this);
    }
    public int saveAsync(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.saveAsync(this);
    }
    public int deleteAsync(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.deleteAsync(this);
    }
    public int updateAsync(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.updateAsync(this);
    }
    public List<SysRole> query(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.query(this);
    }
    public List<SysRole> query(SysRoleDao sysRoleDao,int page,int size) throws Exception {
        return sysRoleDao.query(this,page,size);
    }
    public List<SysRole> query(SysRoleDao sysRoleDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysRoleDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysRoleDao sysRoleDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysRoleDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysRoleDao sysRoleDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysRoleDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysRole find(SysRoleDao sysRoleDao) throws Exception {
        return sysRoleDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(83);
       boolean app01=false;
        sb.append("{");
        if (this.sysRoleId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleId\":\"").append(sysRoleId).append("\"");
        }
        if (this.sysRoleName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleName\":");
            sb.append(Tools.toJson(sysRoleName));        }
        if (this.sysRoleState != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleState\":\"").append(sysRoleState).append("\"");
        }
        if (this.sysRoleTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTime\":");
            sb.append(Tools.toJson(sysRoleTime));        }
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
        result.set("sysRoleId", sysRoleId);
        result.set("sysRoleName", sysRoleName);
        result.set("sysRoleState", sysRoleState);
        result.set("sysRoleTime", sysRoleTime);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysRoleId=result.getLong("sysRoleId", null);
        this.sysRoleName=result.getString("sysRoleName", null);
        this.sysRoleState=result.getLong("sysRoleState", null);
        this.sysRoleTime=result.getLocalDateTime("sysRoleTime", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysRoleId() {
        return sysRoleId;
    }
    public SysRole setSysRoleId(java.lang.Long sysRoleId) {
        this.sysRoleId = sysRoleId;
        return this;
    }
    public java.lang.String getSysRoleName() {
        return sysRoleName;
    }
    public SysRole setSysRoleName(java.lang.String sysRoleName) {
        int size0 = Tools.textLength(sysRoleName);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysRole.sysRoleName最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysRoleName);
        }
        this.sysRoleName = sysRoleName;
        return this;
    }
    public SysRole setSysRoleNameUnsafe(java.lang.String sysRoleName) {
        this.sysRoleName = sysRoleName;
        return this;
    }
    public java.lang.Long getSysRoleState() {
        return sysRoleState;
    }
    public SysRole setSysRoleState(java.lang.Long sysRoleState) {
        this.sysRoleState = sysRoleState;
        return this;
    }
    public java.time.LocalDateTime getSysRoleTime() {
        return sysRoleTime;
    }
    public SysRole setSysRoleTime(java.time.LocalDateTime sysRoleTime) {
        this.sysRoleTime = sysRoleTime;
        return this;
    }
    public SysRole setList(List<?> data) {
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

    public SysRole setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRole putMap(String key, Object value) {
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
    public SysRole setData(Object data) {
        this.data = data;
        return this;
    }
}
