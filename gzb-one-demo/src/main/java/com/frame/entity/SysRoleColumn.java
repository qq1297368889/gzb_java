package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysRoleColumnDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_role_column",desc="sysRoleColumn")
public class SysRoleColumn implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 0,name="sys_role_column_id",desc="ID",type="bigint")
    private Long sysRoleColumnId;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_table",desc="表ID",type="bigint")
    private Long sysRoleColumnTable;
    @EntityAttribute(key=false,size = 100,name="sys_role_column_name",desc="列名 驼峰",type="varchar(100)")
    private String sysRoleColumnName;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_table_show",desc="是否显示在表格",type="bigint")
    private Long sysRoleColumnTableShow;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_query",desc="是否参与查询",type="bigint")
    private Long sysRoleColumnQuery;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_save",desc="是否参与新增",type="bigint")
    private Long sysRoleColumnSave;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_update",desc="是否参与修改",type="bigint")
    private Long sysRoleColumnUpdate;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_edit",desc="是否参与编辑",type="bigint")
    private Long sysRoleColumnEdit;
    @EntityAttribute(key=false,size = 100,name="sys_role_column_query_def",desc="查询默认值",type="varchar(100)")
    private String sysRoleColumnQueryDef;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_query_symbol",desc="查询运算符",type="bigint")
    private Long sysRoleColumnQuerySymbol;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_query_montage",desc="查询连接符",type="bigint")
    private Long sysRoleColumnQueryMontage;
    @EntityAttribute(key=false,size = 100,name="sys_role_column_save_def",desc="新增默认值",type="varchar(100)")
    private String sysRoleColumnSaveDef;
    @EntityAttribute(key=false,size = 100,name="sys_role_column_update_def",desc="修改默认值",type="varchar(100)")
    private String sysRoleColumnUpdateDef;
    @EntityAttribute(key=false,size = 0,name="sys_role_column_role",desc="角色",type="bigint")
    private Long sysRoleColumnRole;
    private Object data;
   public SysRoleColumn() {}

    public SysRoleColumn(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysRoleColumn(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysRoleColumn(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysRoleColumnDao sysRoleColumnDao) throws Exception {
        return sysRoleColumnDao.save(this);
    }
    public int delete(SysRoleColumnDao sysRoleColumnDao) throws Exception {
        return sysRoleColumnDao.delete(this);
    }
    public int update(SysRoleColumnDao sysRoleColumnDao) throws Exception {
        return sysRoleColumnDao.update(this);
    }
    public List<SysRoleColumn> query(SysRoleColumnDao sysRoleColumnDao) throws Exception {
        return sysRoleColumnDao.query(this);
    }
    public List<SysRoleColumn> query(SysRoleColumnDao sysRoleColumnDao,int page,int size) throws Exception {
        return sysRoleColumnDao.query(this,page,size);
    }
    public List<SysRoleColumn> query(SysRoleColumnDao sysRoleColumnDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysRoleColumnDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysRoleColumnDao sysRoleColumnDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysRoleColumnDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysRoleColumnDao sysRoleColumnDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysRoleColumnDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysRoleColumn find(SysRoleColumnDao sysRoleColumnDao) throws Exception {
        return sysRoleColumnDao.find(this);
    }
    @Override
    public String toString() {
        gzb.tools.thread.GzbThreadLocal.Entity entity0 = gzb.tools.thread.GzbThreadLocal.context.get();
        int index0=entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
       boolean app01=false;
        sb.append("{");
        if (this.sysRoleColumnId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnId\":\"").append(sysRoleColumnId).append("\"");
        }
        if (this.sysRoleColumnTable != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnTable\":\"").append(sysRoleColumnTable).append("\"");
        }
        if (this.sysRoleColumnName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnName\":");
            sb.append(Tools.toJson(sysRoleColumnName));        }
        if (this.sysRoleColumnTableShow != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnTableShow\":\"").append(sysRoleColumnTableShow).append("\"");
        }
        if (this.sysRoleColumnQuery != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnQuery\":\"").append(sysRoleColumnQuery).append("\"");
        }
        if (this.sysRoleColumnSave != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnSave\":\"").append(sysRoleColumnSave).append("\"");
        }
        if (this.sysRoleColumnUpdate != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnUpdate\":\"").append(sysRoleColumnUpdate).append("\"");
        }
        if (this.sysRoleColumnEdit != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnEdit\":\"").append(sysRoleColumnEdit).append("\"");
        }
        if (this.sysRoleColumnQueryDef != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnQueryDef\":");
            sb.append(Tools.toJson(sysRoleColumnQueryDef));        }
        if (this.sysRoleColumnQuerySymbol != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnQuerySymbol\":\"").append(sysRoleColumnQuerySymbol).append("\"");
        }
        if (this.sysRoleColumnQueryMontage != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnQueryMontage\":\"").append(sysRoleColumnQueryMontage).append("\"");
        }
        if (this.sysRoleColumnSaveDef != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnSaveDef\":");
            sb.append(Tools.toJson(sysRoleColumnSaveDef));        }
        if (this.sysRoleColumnUpdateDef != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnUpdateDef\":");
            sb.append(Tools.toJson(sysRoleColumnUpdateDef));        }
        if (this.sysRoleColumnRole != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysRoleColumnRole\":\"").append(sysRoleColumnRole).append("\"");
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
        result.set("sysRoleColumnId", sysRoleColumnId);
        result.set("sysRoleColumnTable", sysRoleColumnTable);
        result.set("sysRoleColumnName", sysRoleColumnName);
        result.set("sysRoleColumnTableShow", sysRoleColumnTableShow);
        result.set("sysRoleColumnQuery", sysRoleColumnQuery);
        result.set("sysRoleColumnSave", sysRoleColumnSave);
        result.set("sysRoleColumnUpdate", sysRoleColumnUpdate);
        result.set("sysRoleColumnEdit", sysRoleColumnEdit);
        result.set("sysRoleColumnQueryDef", sysRoleColumnQueryDef);
        result.set("sysRoleColumnQuerySymbol", sysRoleColumnQuerySymbol);
        result.set("sysRoleColumnQueryMontage", sysRoleColumnQueryMontage);
        result.set("sysRoleColumnSaveDef", sysRoleColumnSaveDef);
        result.set("sysRoleColumnUpdateDef", sysRoleColumnUpdateDef);
        result.set("sysRoleColumnRole", sysRoleColumnRole);
        result.set(Config.entityDataListName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysRoleColumnId=result.getLong("sysRoleColumnId", null);
        this.sysRoleColumnTable=result.getLong("sysRoleColumnTable", null);
        this.sysRoleColumnName=result.getString("sysRoleColumnName", null);
        this.sysRoleColumnTableShow=result.getLong("sysRoleColumnTableShow", null);
        this.sysRoleColumnQuery=result.getLong("sysRoleColumnQuery", null);
        this.sysRoleColumnSave=result.getLong("sysRoleColumnSave", null);
        this.sysRoleColumnUpdate=result.getLong("sysRoleColumnUpdate", null);
        this.sysRoleColumnEdit=result.getLong("sysRoleColumnEdit", null);
        this.sysRoleColumnQueryDef=result.getString("sysRoleColumnQueryDef", null);
        this.sysRoleColumnQuerySymbol=result.getLong("sysRoleColumnQuerySymbol", null);
        this.sysRoleColumnQueryMontage=result.getLong("sysRoleColumnQueryMontage", null);
        this.sysRoleColumnSaveDef=result.getString("sysRoleColumnSaveDef", null);
        this.sysRoleColumnUpdateDef=result.getString("sysRoleColumnUpdateDef", null);
        this.sysRoleColumnRole=result.getLong("sysRoleColumnRole", null);
        Object obj = result.get(Config.entityDataListName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public Long getSysRoleColumnId() {
        return sysRoleColumnId;
    }
    public SysRoleColumn setSysRoleColumnId(Long sysRoleColumnId) {
        this.sysRoleColumnId = sysRoleColumnId;
        return this;
    }
    public Long getSysRoleColumnTable() {
        return sysRoleColumnTable;
    }
    public SysRoleColumn setSysRoleColumnTable(Long sysRoleColumnTable) {
        this.sysRoleColumnTable = sysRoleColumnTable;
        return this;
    }
    public String getSysRoleColumnName() {
        return sysRoleColumnName;
    }
    public SysRoleColumn setSysRoleColumnName(String sysRoleColumnName) {
        int size0 = Tools.textLength(sysRoleColumnName);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysRoleColumn.sysRoleColumnName最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysRoleColumnName);
        }
        this.sysRoleColumnName = sysRoleColumnName;
        return this;
    }
    public SysRoleColumn setSysRoleColumnNameUnsafe(String sysRoleColumnName) {
        this.sysRoleColumnName = sysRoleColumnName;
        return this;
    }
    public Long getSysRoleColumnTableShow() {
        return sysRoleColumnTableShow;
    }
    public SysRoleColumn setSysRoleColumnTableShow(Long sysRoleColumnTableShow) {
        this.sysRoleColumnTableShow = sysRoleColumnTableShow;
        return this;
    }
    public Long getSysRoleColumnQuery() {
        return sysRoleColumnQuery;
    }
    public SysRoleColumn setSysRoleColumnQuery(Long sysRoleColumnQuery) {
        this.sysRoleColumnQuery = sysRoleColumnQuery;
        return this;
    }
    public Long getSysRoleColumnSave() {
        return sysRoleColumnSave;
    }
    public SysRoleColumn setSysRoleColumnSave(Long sysRoleColumnSave) {
        this.sysRoleColumnSave = sysRoleColumnSave;
        return this;
    }
    public Long getSysRoleColumnUpdate() {
        return sysRoleColumnUpdate;
    }
    public SysRoleColumn setSysRoleColumnUpdate(Long sysRoleColumnUpdate) {
        this.sysRoleColumnUpdate = sysRoleColumnUpdate;
        return this;
    }
    public Long getSysRoleColumnEdit() {
        return sysRoleColumnEdit;
    }
    public SysRoleColumn setSysRoleColumnEdit(Long sysRoleColumnEdit) {
        this.sysRoleColumnEdit = sysRoleColumnEdit;
        return this;
    }
    public String getSysRoleColumnQueryDef() {
        return sysRoleColumnQueryDef;
    }
    public SysRoleColumn setSysRoleColumnQueryDef(String sysRoleColumnQueryDef) {
        int size0 = Tools.textLength(sysRoleColumnQueryDef);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysRoleColumn.sysRoleColumnQueryDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysRoleColumnQueryDef);
        }
        this.sysRoleColumnQueryDef = sysRoleColumnQueryDef;
        return this;
    }
    public SysRoleColumn setSysRoleColumnQueryDefUnsafe(String sysRoleColumnQueryDef) {
        this.sysRoleColumnQueryDef = sysRoleColumnQueryDef;
        return this;
    }
    public Long getSysRoleColumnQuerySymbol() {
        return sysRoleColumnQuerySymbol;
    }
    public SysRoleColumn setSysRoleColumnQuerySymbol(Long sysRoleColumnQuerySymbol) {
        this.sysRoleColumnQuerySymbol = sysRoleColumnQuerySymbol;
        return this;
    }
    public Long getSysRoleColumnQueryMontage() {
        return sysRoleColumnQueryMontage;
    }
    public SysRoleColumn setSysRoleColumnQueryMontage(Long sysRoleColumnQueryMontage) {
        this.sysRoleColumnQueryMontage = sysRoleColumnQueryMontage;
        return this;
    }
    public String getSysRoleColumnSaveDef() {
        return sysRoleColumnSaveDef;
    }
    public SysRoleColumn setSysRoleColumnSaveDef(String sysRoleColumnSaveDef) {
        int size0 = Tools.textLength(sysRoleColumnSaveDef);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysRoleColumn.sysRoleColumnSaveDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysRoleColumnSaveDef);
        }
        this.sysRoleColumnSaveDef = sysRoleColumnSaveDef;
        return this;
    }
    public SysRoleColumn setSysRoleColumnSaveDefUnsafe(String sysRoleColumnSaveDef) {
        this.sysRoleColumnSaveDef = sysRoleColumnSaveDef;
        return this;
    }
    public String getSysRoleColumnUpdateDef() {
        return sysRoleColumnUpdateDef;
    }
    public SysRoleColumn setSysRoleColumnUpdateDef(String sysRoleColumnUpdateDef) {
        int size0 = Tools.textLength(sysRoleColumnUpdateDef);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysRoleColumn.sysRoleColumnUpdateDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysRoleColumnUpdateDef);
        }
        this.sysRoleColumnUpdateDef = sysRoleColumnUpdateDef;
        return this;
    }
    public SysRoleColumn setSysRoleColumnUpdateDefUnsafe(String sysRoleColumnUpdateDef) {
        this.sysRoleColumnUpdateDef = sysRoleColumnUpdateDef;
        return this;
    }
    public Long getSysRoleColumnRole() {
        return sysRoleColumnRole;
    }
    public SysRoleColumn setSysRoleColumnRole(Long sysRoleColumnRole) {
        this.sysRoleColumnRole = sysRoleColumnRole;
        return this;
    }
    public SysRoleColumn setList(List<?> data) {
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

    public SysRoleColumn setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleColumn putMap(String key, Object value) {
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
    public SysRoleColumn setData(Object data) {
        this.data = data;
        return this;
    }
}
