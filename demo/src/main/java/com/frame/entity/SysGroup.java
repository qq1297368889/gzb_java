package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysGroupDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_group",desc="sysGroup")
public class SysGroup implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_group_id",desc="权限组ID",type="java.lang.Long")
    private java.lang.Long sysGroupId;
    @EntityAttribute(key=false,size = 255,name="sys_group_name",desc="权限组名称",type="java.lang.String")
    private java.lang.String sysGroupName;
    @EntityAttribute(key=false,size = 19,name="sys_group_type",desc="权限组类型",type="java.lang.Long")
    private java.lang.Long sysGroupType;
    @EntityAttribute(key=false,size = 255,name="sys_group_desc",desc="权限组描述",type="java.lang.String")
    private java.lang.String sysGroupDesc;
    @EntityAttribute(key=false,size = 19,name="sys_group_sup",desc="权限组上级",type="java.lang.Long")
    private java.lang.Long sysGroupSup;
    private Object data;
   public SysGroup() {}

    public SysGroup(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysGroup(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysGroup(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(107);
       boolean app01=false;
        sb.append("{");
        if (this.sysGroupId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupId\":\"").append(sysGroupId).append("\"");
        }
        if (this.sysGroupName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupName\":");
            sb.append(Tools.toJson(sysGroupName));        }
        if (this.sysGroupType != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupType\":\"").append(sysGroupType).append("\"");
        }
        if (this.sysGroupDesc != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupDesc\":");
            sb.append(Tools.toJson(sysGroupDesc));        }
        if (this.sysGroupSup != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysGroupSup\":\"").append(sysGroupSup).append("\"");
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
        result.set("sysGroupId", sysGroupId);
        result.set("sysGroupName", sysGroupName);
        result.set("sysGroupType", sysGroupType);
        result.set("sysGroupDesc", sysGroupDesc);
        result.set("sysGroupSup", sysGroupSup);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysGroupId=result.getLong("sysGroupId", null);
        this.sysGroupName=result.getString("sysGroupName", null);
        this.sysGroupType=result.getLong("sysGroupType", null);
        this.sysGroupDesc=result.getString("sysGroupDesc", null);
        this.sysGroupSup=result.getLong("sysGroupSup", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }
    public SysGroup setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
        return this;
    }
    public java.lang.String getSysGroupName() {
        return sysGroupName;
    }
    public SysGroup setSysGroupName(java.lang.String sysGroupName) {
        int size0 = Tools.textLength(sysGroupName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysGroup.sysGroupName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupName);
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
        this.sysGroupType = sysGroupType;
        return this;
    }
    public java.lang.String getSysGroupDesc() {
        return sysGroupDesc;
    }
    public SysGroup setSysGroupDesc(java.lang.String sysGroupDesc) {
        int size0 = Tools.textLength(sysGroupDesc);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysGroup.sysGroupDesc最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupDesc);
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
        this.sysGroupSup = sysGroupSup;
        return this;
    }
    public SysGroup setList(List<?> data) {
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

    public SysGroup setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroup putMap(String key, Object value) {
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
    public SysGroup setData(Object data) {
        this.data = data;
        return this;
    }
}
