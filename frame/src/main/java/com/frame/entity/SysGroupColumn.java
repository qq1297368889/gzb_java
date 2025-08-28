package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysGroupColumnDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_group_column",desc="sysGroupColumn")
public class SysGroupColumn implements Serializable{
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

    public SysGroupColumn(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysGroupColumn(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysGroupColumnId");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnKey");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnKey(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnName");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnTable");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnTable(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnEdit");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnEdit(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnUpdate");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnUpdate(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnSave");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnSave(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnQuery");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnQuery(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnSaveDef");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnSaveDef(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnUpdateDef");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnUpdateDef(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnQuerySymbol");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnQuerySymbol(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnQueryMontage");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnQueryMontage(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnQueryDef");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnQueryDef(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupColumnGid");
        if (str!=null && !str.isEmpty()) {
            setSysGroupColumnGid(java.lang.Long.valueOf(str));
        }
    }

    public SysGroupColumn(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysGroupColumn(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
    public SqlTemplate toSelectSql(String sortField, String sortType, int size, boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave(java.lang.Long actCodeId) {
        return SqlTools.toSave(this,actCodeId);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSON toJson() {
        JSON json = new JSON();
        json.put("sysGroupColumnId", getSysGroupColumnId());
        json.put("sysGroupColumnKey", getSysGroupColumnKey());
        json.put("sysGroupColumnName", getSysGroupColumnName());
        json.put("sysGroupColumnTable", getSysGroupColumnTable());
        json.put("sysGroupColumnEdit", getSysGroupColumnEdit());
        json.put("sysGroupColumnUpdate", getSysGroupColumnUpdate());
        json.put("sysGroupColumnSave", getSysGroupColumnSave());
        json.put("sysGroupColumnQuery", getSysGroupColumnQuery());
        json.put("sysGroupColumnSaveDef", getSysGroupColumnSaveDef());
        json.put("sysGroupColumnUpdateDef", getSysGroupColumnUpdateDef());
        json.put("sysGroupColumnQuerySymbol", getSysGroupColumnQuerySymbol());
        json.put("sysGroupColumnQueryMontage", getSysGroupColumnQueryMontage());
        json.put("sysGroupColumnQueryDef", getSysGroupColumnQueryDef());
        json.put("sysGroupColumnGid", getSysGroupColumnGid());
        json.put("data", getList());
        return json;
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
