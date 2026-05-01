package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysRoleTableDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_role_table",desc="sysRoleTable")
public class SysRoleTable implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 0,name="sys_role_table_id",desc="ID",type="bigint")
    private Long sysRoleTableId;
    @EntityAttribute(key=false,size = 255,name="sys_role_table_name",desc="表名-驼峰",type="varchar(255)")
    private String sysRoleTableName;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_save",desc="按钮新增",type="bigint")
    private Long sysRoleTableSave;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_query",desc="按钮查询",type="bigint")
    private Long sysRoleTableQuery;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_delete",desc="按钮删除",type="bigint")
    private Long sysRoleTableDelete;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_update",desc="按钮单改",type="bigint")
    private Long sysRoleTableUpdate;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_delete_sgin",desc="按钮单删",type="bigint")
    private Long sysRoleTableDeleteSgin;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_role",desc="角色",type="bigint")
    private Long sysRoleTableRole;
    @EntityAttribute(key=false,size = 0,name="sys_role_table_width",desc="表格按钮宽度",type="bigint")
    private Long sysRoleTableWidth;
    private Object data;
   public SysRoleTable() {}

    public SysRoleTable(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysRoleTable(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysRoleTable(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysRoleTableDao sysRoleTableDao) throws Exception {
        return sysRoleTableDao.save(this);
    }
    public int delete(SysRoleTableDao sysRoleTableDao) throws Exception {
        return sysRoleTableDao.delete(this);
    }
    public int update(SysRoleTableDao sysRoleTableDao) throws Exception {
        return sysRoleTableDao.update(this);
    }
    public List<SysRoleTable> query(SysRoleTableDao sysRoleTableDao) throws Exception {
        return sysRoleTableDao.query(this);
    }
    public List<SysRoleTable> query(SysRoleTableDao sysRoleTableDao,int page,int size) throws Exception {
        return sysRoleTableDao.query(this,page,size);
    }
    public List<SysRoleTable> query(SysRoleTableDao sysRoleTableDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysRoleTableDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysRoleTableDao sysRoleTableDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysRoleTableDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysRoleTableDao sysRoleTableDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysRoleTableDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysRoleTable find(SysRoleTableDao sysRoleTableDao) throws Exception {
        return sysRoleTableDao.find(this);
    }
    @Override
    public String toString() {
        gzb.tools.thread.GzbThreadLocal.Entity entity0 = gzb.tools.thread.GzbThreadLocal.context.get();
        int index0=entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
       boolean app01=false;
        sb.append("{");
        if (this.sysRoleTableId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableId\":\"").append(sysRoleTableId).append("\"");
        }
        if (this.sysRoleTableName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableName\":");
            sb.append(Tools.toJson(sysRoleTableName));        }
        if (this.sysRoleTableSave != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableSave\":\"").append(sysRoleTableSave).append("\"");
        }
        if (this.sysRoleTableQuery != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableQuery\":\"").append(sysRoleTableQuery).append("\"");
        }
        if (this.sysRoleTableDelete != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableDelete\":\"").append(sysRoleTableDelete).append("\"");
        }
        if (this.sysRoleTableUpdate != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableUpdate\":\"").append(sysRoleTableUpdate).append("\"");
        }
        if (this.sysRoleTableDeleteSgin != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableDeleteSgin\":\"").append(sysRoleTableDeleteSgin).append("\"");
        }
        if (this.sysRoleTableRole != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableRole\":\"").append(sysRoleTableRole).append("\"");
        }
        if (this.sysRoleTableWidth != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleTableWidth\":\"").append(sysRoleTableWidth).append("\"");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                if(app01){sb.append(",");}app01=true;
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(Tools.toJson(entry.getValue()));
            }
        }else if(this.data != null){
            if(app01){sb.append(",");}app01=true;
            sb.append("\"").append(Config.entityDataListName).append("\":");
            sb.append(Tools.toJson(this.data));
        }
       return sb.append("}").toString();
            }finally {
                entity0.stringBuilderCacheEntity.close(index0);
            }    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysRoleTableId", sysRoleTableId);
        result.set("sysRoleTableName", sysRoleTableName);
        result.set("sysRoleTableSave", sysRoleTableSave);
        result.set("sysRoleTableQuery", sysRoleTableQuery);
        result.set("sysRoleTableDelete", sysRoleTableDelete);
        result.set("sysRoleTableUpdate", sysRoleTableUpdate);
        result.set("sysRoleTableDeleteSgin", sysRoleTableDeleteSgin);
        result.set("sysRoleTableRole", sysRoleTableRole);
        result.set("sysRoleTableWidth", sysRoleTableWidth);
        result.set(Config.entityDataListName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysRoleTableId=result.getLong("sysRoleTableId", null);
        this.sysRoleTableName=result.getString("sysRoleTableName", null);
        this.sysRoleTableSave=result.getLong("sysRoleTableSave", null);
        this.sysRoleTableQuery=result.getLong("sysRoleTableQuery", null);
        this.sysRoleTableDelete=result.getLong("sysRoleTableDelete", null);
        this.sysRoleTableUpdate=result.getLong("sysRoleTableUpdate", null);
        this.sysRoleTableDeleteSgin=result.getLong("sysRoleTableDeleteSgin", null);
        this.sysRoleTableRole=result.getLong("sysRoleTableRole", null);
        this.sysRoleTableWidth=result.getLong("sysRoleTableWidth", null);
        Object obj = result.get(Config.entityDataListName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public Long getSysRoleTableId() {
        return sysRoleTableId;
    }
    public SysRoleTable setSysRoleTableId(Long sysRoleTableId) {
        this.sysRoleTableId = sysRoleTableId;
        return this;
    }
    public String getSysRoleTableName() {
        return sysRoleTableName;
    }
    public SysRoleTable setSysRoleTableName(String sysRoleTableName) {
        int size0 = Tools.textLength(sysRoleTableName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysRoleTable.sysRoleTableName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysRoleTableName);
        }
        this.sysRoleTableName = sysRoleTableName;
        return this;
    }
    public SysRoleTable setSysRoleTableNameUnsafe(String sysRoleTableName) {
        this.sysRoleTableName = sysRoleTableName;
        return this;
    }
    public Long getSysRoleTableSave() {
        return sysRoleTableSave;
    }
    public SysRoleTable setSysRoleTableSave(Long sysRoleTableSave) {
        this.sysRoleTableSave = sysRoleTableSave;
        return this;
    }
    public Long getSysRoleTableQuery() {
        return sysRoleTableQuery;
    }
    public SysRoleTable setSysRoleTableQuery(Long sysRoleTableQuery) {
        this.sysRoleTableQuery = sysRoleTableQuery;
        return this;
    }
    public Long getSysRoleTableDelete() {
        return sysRoleTableDelete;
    }
    public SysRoleTable setSysRoleTableDelete(Long sysRoleTableDelete) {
        this.sysRoleTableDelete = sysRoleTableDelete;
        return this;
    }
    public Long getSysRoleTableUpdate() {
        return sysRoleTableUpdate;
    }
    public SysRoleTable setSysRoleTableUpdate(Long sysRoleTableUpdate) {
        this.sysRoleTableUpdate = sysRoleTableUpdate;
        return this;
    }
    public Long getSysRoleTableDeleteSgin() {
        return sysRoleTableDeleteSgin;
    }
    public SysRoleTable setSysRoleTableDeleteSgin(Long sysRoleTableDeleteSgin) {
        this.sysRoleTableDeleteSgin = sysRoleTableDeleteSgin;
        return this;
    }
    public Long getSysRoleTableRole() {
        return sysRoleTableRole;
    }
    public SysRoleTable setSysRoleTableRole(Long sysRoleTableRole) {
        this.sysRoleTableRole = sysRoleTableRole;
        return this;
    }
    public Long getSysRoleTableWidth() {
        return sysRoleTableWidth;
    }
    public SysRoleTable setSysRoleTableWidth(Long sysRoleTableWidth) {
        this.sysRoleTableWidth = sysRoleTableWidth;
        return this;
    }
    public SysRoleTable setList(List<?> data) {
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

    public SysRoleTable setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleTable putMap(String key, Object value) {
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
    public SysRoleTable setData(Object data) {
        this.data = data;
        return this;
    }
}
