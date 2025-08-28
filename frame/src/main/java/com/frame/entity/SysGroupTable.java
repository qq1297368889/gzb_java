package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysGroupTableDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_group_table",desc="sysGroupTable")
public class SysGroupTable implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_group_table_id",desc="sysGroupTableId")
    private java.lang.Long sysGroupTableId;
    @EntityAttribute(key=false,size = 100,name="sys_group_table_key",desc="sysGroupTableKey")
    private java.lang.String sysGroupTableKey;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_gid",desc="sysGroupTableGid")
    private java.lang.Long sysGroupTableGid;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_but_save_open",desc="sysGroupTableButSaveOpen")
    private java.lang.Long sysGroupTableButSaveOpen;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_but_delete_open",desc="sysGroupTableButDeleteOpen")
    private java.lang.Long sysGroupTableButDeleteOpen;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_table_update_open",desc="sysGroupTableTableUpdateOpen")
    private java.lang.Long sysGroupTableTableUpdateOpen;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_table_delete_open",desc="sysGroupTableTableDeleteOpen")
    private java.lang.Long sysGroupTableTableDeleteOpen;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_but_query_open",desc="sysGroupTableButQueryOpen")
    private java.lang.Long sysGroupTableButQueryOpen;
    @EntityAttribute(key=false,size = 19,name="sys_group_table_table_but_width",desc="sysGroupTableTableButWidth")
    private java.lang.Long sysGroupTableTableButWidth;
    private List<?> list;
    public SysGroupTable() {}

    public SysGroupTable(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysGroupTable(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysGroupTableId");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableKey");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableKey(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableGid");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableGid(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableButSaveOpen");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableButSaveOpen(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableButDeleteOpen");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableButDeleteOpen(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableTableUpdateOpen");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableTableUpdateOpen(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableTableDeleteOpen");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableTableDeleteOpen(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableButQueryOpen");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableButQueryOpen(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysGroupTableTableButWidth");
        if (str!=null && !str.isEmpty()) {
            setSysGroupTableTableButWidth(java.lang.Long.valueOf(str));
        }
    }

    public SysGroupTable(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysGroupTable(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.save(this);
    }
    public int delete(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.delete(this);
    }
    public int update(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.update(this);
    }
    public int saveAsync(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.saveAsync(this);
    }
    public int deleteAsync(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.deleteAsync(this);
    }
    public int updateAsync(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.updateAsync(this);
    }
    public List<SysGroupTable> query(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.query(this);
    }
    public List<SysGroupTable> query(SysGroupTableDao sysGroupTableDao,int page,int size) throws Exception {
        return sysGroupTableDao.query(this,page,size);
    }
    public List<SysGroupTable> query(SysGroupTableDao sysGroupTableDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysGroupTableDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysGroupTableDao sysGroupTableDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysGroupTableDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysGroupTableDao sysGroupTableDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysGroupTableDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysGroupTable find(SysGroupTableDao sysGroupTableDao) throws Exception {
        return sysGroupTableDao.find(this);
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
        json.put("sysGroupTableId", getSysGroupTableId());
        json.put("sysGroupTableKey", getSysGroupTableKey());
        json.put("sysGroupTableGid", getSysGroupTableGid());
        json.put("sysGroupTableButSaveOpen", getSysGroupTableButSaveOpen());
        json.put("sysGroupTableButDeleteOpen", getSysGroupTableButDeleteOpen());
        json.put("sysGroupTableTableUpdateOpen", getSysGroupTableTableUpdateOpen());
        json.put("sysGroupTableTableDeleteOpen", getSysGroupTableTableDeleteOpen());
        json.put("sysGroupTableButQueryOpen", getSysGroupTableButQueryOpen());
        json.put("sysGroupTableTableButWidth", getSysGroupTableTableButWidth());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysGroupTableId() {
        return sysGroupTableId;
    }
    public SysGroupTable setSysGroupTableId(java.lang.Long sysGroupTableId) {
        int size0 = Tools.textLength(sysGroupTableId);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableId);
        }
        this.sysGroupTableId = sysGroupTableId;
        return this;
    }
    public SysGroupTable setSysGroupTableIdUnsafe(java.lang.Long sysGroupTableId) {
        this.sysGroupTableId = sysGroupTableId;
        return this;
    }
    public java.lang.String getSysGroupTableKey() {
        return sysGroupTableKey;
    }
    public SysGroupTable setSysGroupTableKey(java.lang.String sysGroupTableKey) {
        int size0 = Tools.textLength(sysGroupTableKey);
        if (size0 > 100) {
            throw new RuntimeException("SysGroupTable.sysGroupTableKey最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysGroupTableKey);
        }
        this.sysGroupTableKey = sysGroupTableKey;
        return this;
    }
    public SysGroupTable setSysGroupTableKeyUnsafe(java.lang.String sysGroupTableKey) {
        this.sysGroupTableKey = sysGroupTableKey;
        return this;
    }
    public java.lang.Long getSysGroupTableGid() {
        return sysGroupTableGid;
    }
    public SysGroupTable setSysGroupTableGid(java.lang.Long sysGroupTableGid) {
        int size0 = Tools.textLength(sysGroupTableGid);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableGid最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableGid);
        }
        this.sysGroupTableGid = sysGroupTableGid;
        return this;
    }
    public SysGroupTable setSysGroupTableGidUnsafe(java.lang.Long sysGroupTableGid) {
        this.sysGroupTableGid = sysGroupTableGid;
        return this;
    }
    public java.lang.Long getSysGroupTableButSaveOpen() {
        return sysGroupTableButSaveOpen;
    }
    public SysGroupTable setSysGroupTableButSaveOpen(java.lang.Long sysGroupTableButSaveOpen) {
        int size0 = Tools.textLength(sysGroupTableButSaveOpen);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableButSaveOpen最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableButSaveOpen);
        }
        this.sysGroupTableButSaveOpen = sysGroupTableButSaveOpen;
        return this;
    }
    public SysGroupTable setSysGroupTableButSaveOpenUnsafe(java.lang.Long sysGroupTableButSaveOpen) {
        this.sysGroupTableButSaveOpen = sysGroupTableButSaveOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableButDeleteOpen() {
        return sysGroupTableButDeleteOpen;
    }
    public SysGroupTable setSysGroupTableButDeleteOpen(java.lang.Long sysGroupTableButDeleteOpen) {
        int size0 = Tools.textLength(sysGroupTableButDeleteOpen);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableButDeleteOpen最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableButDeleteOpen);
        }
        this.sysGroupTableButDeleteOpen = sysGroupTableButDeleteOpen;
        return this;
    }
    public SysGroupTable setSysGroupTableButDeleteOpenUnsafe(java.lang.Long sysGroupTableButDeleteOpen) {
        this.sysGroupTableButDeleteOpen = sysGroupTableButDeleteOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableUpdateOpen() {
        return sysGroupTableTableUpdateOpen;
    }
    public SysGroupTable setSysGroupTableTableUpdateOpen(java.lang.Long sysGroupTableTableUpdateOpen) {
        int size0 = Tools.textLength(sysGroupTableTableUpdateOpen);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableTableUpdateOpen最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableTableUpdateOpen);
        }
        this.sysGroupTableTableUpdateOpen = sysGroupTableTableUpdateOpen;
        return this;
    }
    public SysGroupTable setSysGroupTableTableUpdateOpenUnsafe(java.lang.Long sysGroupTableTableUpdateOpen) {
        this.sysGroupTableTableUpdateOpen = sysGroupTableTableUpdateOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableDeleteOpen() {
        return sysGroupTableTableDeleteOpen;
    }
    public SysGroupTable setSysGroupTableTableDeleteOpen(java.lang.Long sysGroupTableTableDeleteOpen) {
        int size0 = Tools.textLength(sysGroupTableTableDeleteOpen);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableTableDeleteOpen最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableTableDeleteOpen);
        }
        this.sysGroupTableTableDeleteOpen = sysGroupTableTableDeleteOpen;
        return this;
    }
    public SysGroupTable setSysGroupTableTableDeleteOpenUnsafe(java.lang.Long sysGroupTableTableDeleteOpen) {
        this.sysGroupTableTableDeleteOpen = sysGroupTableTableDeleteOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableButQueryOpen() {
        return sysGroupTableButQueryOpen;
    }
    public SysGroupTable setSysGroupTableButQueryOpen(java.lang.Long sysGroupTableButQueryOpen) {
        int size0 = Tools.textLength(sysGroupTableButQueryOpen);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableButQueryOpen最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableButQueryOpen);
        }
        this.sysGroupTableButQueryOpen = sysGroupTableButQueryOpen;
        return this;
    }
    public SysGroupTable setSysGroupTableButQueryOpenUnsafe(java.lang.Long sysGroupTableButQueryOpen) {
        this.sysGroupTableButQueryOpen = sysGroupTableButQueryOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableButWidth() {
        return sysGroupTableTableButWidth;
    }
    public SysGroupTable setSysGroupTableTableButWidth(java.lang.Long sysGroupTableTableButWidth) {
        int size0 = Tools.textLength(sysGroupTableTableButWidth);
        if (size0 > 19) {
            throw new RuntimeException("SysGroupTable.sysGroupTableTableButWidth最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysGroupTableTableButWidth);
        }
        this.sysGroupTableTableButWidth = sysGroupTableTableButWidth;
        return this;
    }
    public SysGroupTable setSysGroupTableTableButWidthUnsafe(java.lang.Long sysGroupTableTableButWidth) {
        this.sysGroupTableTableButWidth = sysGroupTableTableButWidth;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
