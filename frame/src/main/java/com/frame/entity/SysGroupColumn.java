package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysGroupColumnDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_group_column",desc="sysGroupColumn")
public class SysGroupColumn implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_group_column_id",desc="sysGroupColumnId")
    private java.lang.Long sysGroupColumnId;
    @EntityAttribute(key=false,size = 255,name="sys_group_column_key",desc="sysGroupColumnKey")
    private java.lang.String sysGroupColumnKey;
    @EntityAttribute(key=false,size = 255,name="sys_group_column_name",desc="sysGroupColumnName")
    private java.lang.String sysGroupColumnName;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_table",desc="sysGroupColumnTable")
    private java.lang.Long sysGroupColumnTable;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_edit",desc="sysGroupColumnEdit")
    private java.lang.Long sysGroupColumnEdit;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_update",desc="sysGroupColumnUpdate")
    private java.lang.Long sysGroupColumnUpdate;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_save",desc="sysGroupColumnSave")
    private java.lang.Long sysGroupColumnSave;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_query",desc="sysGroupColumnQuery")
    private java.lang.Long sysGroupColumnQuery;
    @EntityAttribute(key=false,size = 100,name="sys_group_column_save_def",desc="sysGroupColumnSaveDef")
    private java.lang.String sysGroupColumnSaveDef;
    @EntityAttribute(key=false,size = 100,name="sys_group_column_update_def",desc="sysGroupColumnUpdateDef")
    private java.lang.String sysGroupColumnUpdateDef;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_query_symbol",desc="sysGroupColumnQuerySymbol")
    private java.lang.Long sysGroupColumnQuerySymbol;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_query_montage",desc="sysGroupColumnQueryMontage")
    private java.lang.Long sysGroupColumnQueryMontage;
    @EntityAttribute(key=false,size = 100,name="sys_group_column_query_def",desc="sysGroupColumnQueryDef")
    private java.lang.String sysGroupColumnQueryDef;
    @EntityAttribute(key=false,size = 19,name="sys_group_column_gid",desc="sysGroupColumnGid")
    private java.lang.Long sysGroupColumnGid;
    private List<?> list;
   public SysGroupColumn() {}

    public SysGroupColumn(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysGroupColumn(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysGroupColumn(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysGroupColumn(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
    }
    public int save(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.save(this);
    }
    public int delete(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.delete(this);
    }
    public int update(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.update(this);
    }
    public int saveAsync(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.saveAsync(this);
    }
    public int deleteAsync(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.deleteAsync(this);
    }
    public int updateAsync(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.updateAsync(this);
    }
    public List<SysGroupColumn> query(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.query(this);
    }
    public List<SysGroupColumn> query(SysGroupColumnDao sysGroupColumnDao,int page,int size) throws Exception {
        return sysGroupColumnDao.query(this,page,size);
    }
    public List<SysGroupColumn> query(SysGroupColumnDao sysGroupColumnDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysGroupColumnDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysGroupColumnDao sysGroupColumnDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysGroupColumnDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysGroupColumnDao sysGroupColumnDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysGroupColumnDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysGroupColumn find(SysGroupColumnDao sysGroupColumnDao) throws Exception {
        return sysGroupColumnDao.find(this);
    }
    public SqlTemplate toSelectSql() {
        return SqlTools.toSelectSql(this,"act_code_id", "asc", 0, false);
    }

    //查询语句 可选项 排序
    public SqlTemplate toSelectSql(String sortField, String sortType, Integer size, Boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave() {
        return SqlTools.toSave(this);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(Boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysGroupColumnId", sysGroupColumnId);
        result.set("sysGroupColumnKey", sysGroupColumnKey);
        result.set("sysGroupColumnName", sysGroupColumnName);
        result.set("sysGroupColumnTable", sysGroupColumnTable);
        result.set("sysGroupColumnEdit", sysGroupColumnEdit);
        result.set("sysGroupColumnUpdate", sysGroupColumnUpdate);
        result.set("sysGroupColumnSave", sysGroupColumnSave);
        result.set("sysGroupColumnQuery", sysGroupColumnQuery);
        result.set("sysGroupColumnSaveDef", sysGroupColumnSaveDef);
        result.set("sysGroupColumnUpdateDef", sysGroupColumnUpdateDef);
        result.set("sysGroupColumnQuerySymbol", sysGroupColumnQuerySymbol);
        result.set("sysGroupColumnQueryMontage", sysGroupColumnQueryMontage);
        result.set("sysGroupColumnQueryDef", sysGroupColumnQueryDef);
        result.set("sysGroupColumnGid", sysGroupColumnGid);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysGroupColumnId=result.getLong("sysGroupColumnId", null);
        this.sysGroupColumnKey=result.getString("sysGroupColumnKey", null);
        this.sysGroupColumnName=result.getString("sysGroupColumnName", null);
        this.sysGroupColumnTable=result.getLong("sysGroupColumnTable", null);
        this.sysGroupColumnEdit=result.getLong("sysGroupColumnEdit", null);
        this.sysGroupColumnUpdate=result.getLong("sysGroupColumnUpdate", null);
        this.sysGroupColumnSave=result.getLong("sysGroupColumnSave", null);
        this.sysGroupColumnQuery=result.getLong("sysGroupColumnQuery", null);
        this.sysGroupColumnSaveDef=result.getString("sysGroupColumnSaveDef", null);
        this.sysGroupColumnUpdateDef=result.getString("sysGroupColumnUpdateDef", null);
        this.sysGroupColumnQuerySymbol=result.getLong("sysGroupColumnQuerySymbol", null);
        this.sysGroupColumnQueryMontage=result.getLong("sysGroupColumnQueryMontage", null);
        this.sysGroupColumnQueryDef=result.getString("sysGroupColumnQueryDef", null);
        this.sysGroupColumnGid=result.getLong("sysGroupColumnGid", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_group_column_id");
            if (temp!=null) {
                this.sysGroupColumnId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_key");
            if (temp!=null) {
                this.sysGroupColumnKey=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_name");
            if (temp!=null) {
                this.sysGroupColumnName=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_table");
            if (temp!=null) {
                this.sysGroupColumnTable=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_edit");
            if (temp!=null) {
                this.sysGroupColumnEdit=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_update");
            if (temp!=null) {
                this.sysGroupColumnUpdate=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_save");
            if (temp!=null) {
                this.sysGroupColumnSave=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_query");
            if (temp!=null) {
                this.sysGroupColumnQuery=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_save_def");
            if (temp!=null) {
                this.sysGroupColumnSaveDef=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_update_def");
            if (temp!=null) {
                this.sysGroupColumnUpdateDef=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_query_symbol");
            if (temp!=null) {
                this.sysGroupColumnQuerySymbol=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_query_montage");
            if (temp!=null) {
                this.sysGroupColumnQueryMontage=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_query_def");
            if (temp!=null) {
                this.sysGroupColumnQueryDef=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_group_column_gid");
            if (temp!=null) {
                this.sysGroupColumnGid=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysGroupColumnId() {
        return sysGroupColumnId;
    }
    public SysGroupColumn setSysGroupColumnId(java.lang.Long sysGroupColumnId) {
        int size0 = Tools.textLength(sysGroupColumnId);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnId);
        }
        this.sysGroupColumnId = sysGroupColumnId;
        return this;
    }
    public SysGroupColumn setSysGroupColumnIdUnsafe(java.lang.Long sysGroupColumnId) {
        this.sysGroupColumnId = sysGroupColumnId;
        return this;
    }
    public java.lang.String getSysGroupColumnKey() {
        return sysGroupColumnKey;
    }
    public SysGroupColumn setSysGroupColumnKey(java.lang.String sysGroupColumnKey) {
        int size0 = Tools.textLength(sysGroupColumnKey);
        if (size0 > 255) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnKey最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupColumnKey);
        }
        this.sysGroupColumnKey = sysGroupColumnKey;
        return this;
    }
    public SysGroupColumn setSysGroupColumnKeyUnsafe(java.lang.String sysGroupColumnKey) {
        this.sysGroupColumnKey = sysGroupColumnKey;
        return this;
    }
    public java.lang.String getSysGroupColumnName() {
        return sysGroupColumnName;
    }
    public SysGroupColumn setSysGroupColumnName(java.lang.String sysGroupColumnName) {
        int size0 = Tools.textLength(sysGroupColumnName);
        if (size0 > 255) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysGroupColumnName);
        }
        this.sysGroupColumnName = sysGroupColumnName;
        return this;
    }
    public SysGroupColumn setSysGroupColumnNameUnsafe(java.lang.String sysGroupColumnName) {
        this.sysGroupColumnName = sysGroupColumnName;
        return this;
    }
    public java.lang.Long getSysGroupColumnTable() {
        return sysGroupColumnTable;
    }
    public SysGroupColumn setSysGroupColumnTable(java.lang.Long sysGroupColumnTable) {
        int size0 = Tools.textLength(sysGroupColumnTable);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnTable最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnTable);
        }
        this.sysGroupColumnTable = sysGroupColumnTable;
        return this;
    }
    public SysGroupColumn setSysGroupColumnTableUnsafe(java.lang.Long sysGroupColumnTable) {
        this.sysGroupColumnTable = sysGroupColumnTable;
        return this;
    }
    public java.lang.Long getSysGroupColumnEdit() {
        return sysGroupColumnEdit;
    }
    public SysGroupColumn setSysGroupColumnEdit(java.lang.Long sysGroupColumnEdit) {
        int size0 = Tools.textLength(sysGroupColumnEdit);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnEdit最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnEdit);
        }
        this.sysGroupColumnEdit = sysGroupColumnEdit;
        return this;
    }
    public SysGroupColumn setSysGroupColumnEditUnsafe(java.lang.Long sysGroupColumnEdit) {
        this.sysGroupColumnEdit = sysGroupColumnEdit;
        return this;
    }
    public java.lang.Long getSysGroupColumnUpdate() {
        return sysGroupColumnUpdate;
    }
    public SysGroupColumn setSysGroupColumnUpdate(java.lang.Long sysGroupColumnUpdate) {
        int size0 = Tools.textLength(sysGroupColumnUpdate);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnUpdate最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnUpdate);
        }
        this.sysGroupColumnUpdate = sysGroupColumnUpdate;
        return this;
    }
    public SysGroupColumn setSysGroupColumnUpdateUnsafe(java.lang.Long sysGroupColumnUpdate) {
        this.sysGroupColumnUpdate = sysGroupColumnUpdate;
        return this;
    }
    public java.lang.Long getSysGroupColumnSave() {
        return sysGroupColumnSave;
    }
    public SysGroupColumn setSysGroupColumnSave(java.lang.Long sysGroupColumnSave) {
        int size0 = Tools.textLength(sysGroupColumnSave);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnSave最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnSave);
        }
        this.sysGroupColumnSave = sysGroupColumnSave;
        return this;
    }
    public SysGroupColumn setSysGroupColumnSaveUnsafe(java.lang.Long sysGroupColumnSave) {
        this.sysGroupColumnSave = sysGroupColumnSave;
        return this;
    }
    public java.lang.Long getSysGroupColumnQuery() {
        return sysGroupColumnQuery;
    }
    public SysGroupColumn setSysGroupColumnQuery(java.lang.Long sysGroupColumnQuery) {
        int size0 = Tools.textLength(sysGroupColumnQuery);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnQuery最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnQuery);
        }
        this.sysGroupColumnQuery = sysGroupColumnQuery;
        return this;
    }
    public SysGroupColumn setSysGroupColumnQueryUnsafe(java.lang.Long sysGroupColumnQuery) {
        this.sysGroupColumnQuery = sysGroupColumnQuery;
        return this;
    }
    public java.lang.String getSysGroupColumnSaveDef() {
        return sysGroupColumnSaveDef;
    }
    public SysGroupColumn setSysGroupColumnSaveDef(java.lang.String sysGroupColumnSaveDef) {
        int size0 = Tools.textLength(sysGroupColumnSaveDef);
        if (size0 > 100) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnSaveDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysGroupColumnSaveDef);
        }
        this.sysGroupColumnSaveDef = sysGroupColumnSaveDef;
        return this;
    }
    public SysGroupColumn setSysGroupColumnSaveDefUnsafe(java.lang.String sysGroupColumnSaveDef) {
        this.sysGroupColumnSaveDef = sysGroupColumnSaveDef;
        return this;
    }
    public java.lang.String getSysGroupColumnUpdateDef() {
        return sysGroupColumnUpdateDef;
    }
    public SysGroupColumn setSysGroupColumnUpdateDef(java.lang.String sysGroupColumnUpdateDef) {
        int size0 = Tools.textLength(sysGroupColumnUpdateDef);
        if (size0 > 100) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnUpdateDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysGroupColumnUpdateDef);
        }
        this.sysGroupColumnUpdateDef = sysGroupColumnUpdateDef;
        return this;
    }
    public SysGroupColumn setSysGroupColumnUpdateDefUnsafe(java.lang.String sysGroupColumnUpdateDef) {
        this.sysGroupColumnUpdateDef = sysGroupColumnUpdateDef;
        return this;
    }
    public java.lang.Long getSysGroupColumnQuerySymbol() {
        return sysGroupColumnQuerySymbol;
    }
    public SysGroupColumn setSysGroupColumnQuerySymbol(java.lang.Long sysGroupColumnQuerySymbol) {
        int size0 = Tools.textLength(sysGroupColumnQuerySymbol);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnQuerySymbol最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnQuerySymbol);
        }
        this.sysGroupColumnQuerySymbol = sysGroupColumnQuerySymbol;
        return this;
    }
    public SysGroupColumn setSysGroupColumnQuerySymbolUnsafe(java.lang.Long sysGroupColumnQuerySymbol) {
        this.sysGroupColumnQuerySymbol = sysGroupColumnQuerySymbol;
        return this;
    }
    public java.lang.Long getSysGroupColumnQueryMontage() {
        return sysGroupColumnQueryMontage;
    }
    public SysGroupColumn setSysGroupColumnQueryMontage(java.lang.Long sysGroupColumnQueryMontage) {
        int size0 = Tools.textLength(sysGroupColumnQueryMontage);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnQueryMontage最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnQueryMontage);
        }
        this.sysGroupColumnQueryMontage = sysGroupColumnQueryMontage;
        return this;
    }
    public SysGroupColumn setSysGroupColumnQueryMontageUnsafe(java.lang.Long sysGroupColumnQueryMontage) {
        this.sysGroupColumnQueryMontage = sysGroupColumnQueryMontage;
        return this;
    }
    public java.lang.String getSysGroupColumnQueryDef() {
        return sysGroupColumnQueryDef;
    }
    public SysGroupColumn setSysGroupColumnQueryDef(java.lang.String sysGroupColumnQueryDef) {
        int size0 = Tools.textLength(sysGroupColumnQueryDef);
        if (size0 > 100) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnQueryDef最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysGroupColumnQueryDef);
        }
        this.sysGroupColumnQueryDef = sysGroupColumnQueryDef;
        return this;
    }
    public SysGroupColumn setSysGroupColumnQueryDefUnsafe(java.lang.String sysGroupColumnQueryDef) {
        this.sysGroupColumnQueryDef = sysGroupColumnQueryDef;
        return this;
    }
    public java.lang.Long getSysGroupColumnGid() {
        return sysGroupColumnGid;
    }
    public SysGroupColumn setSysGroupColumnGid(java.lang.Long sysGroupColumnGid) {
        int size0 = Tools.textLength(sysGroupColumnGid);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupColumn.sysGroupColumnGid最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupColumnGid);
        }
        this.sysGroupColumnGid = sysGroupColumnGid;
        return this;
    }
    public SysGroupColumn setSysGroupColumnGidUnsafe(java.lang.Long sysGroupColumnGid) {
        this.sysGroupColumnGid = sysGroupColumnGid;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
