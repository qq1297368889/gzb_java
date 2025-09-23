package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysGroupTableDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_group_table",desc="sysGroupTable")
public class SysGroupTable implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
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
    private Object data;
   public SysGroupTable() {}

    public SysGroupTable(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysGroupTable(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysGroupTable(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysGroupTable(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysGroupTableId != null) {
            sb.append("\"sysGroupTableId\":\"").append(sysGroupTableId).append("\",");
        }
        if (this.sysGroupTableKey != null) {
            sb.append("\"sysGroupTableKey\":\"").append(sysGroupTableKey).append("\",");
        }
        if (this.sysGroupTableGid != null) {
            sb.append("\"sysGroupTableGid\":\"").append(sysGroupTableGid).append("\",");
        }
        if (this.sysGroupTableButSaveOpen != null) {
            sb.append("\"sysGroupTableButSaveOpen\":\"").append(sysGroupTableButSaveOpen).append("\",");
        }
        if (this.sysGroupTableButDeleteOpen != null) {
            sb.append("\"sysGroupTableButDeleteOpen\":\"").append(sysGroupTableButDeleteOpen).append("\",");
        }
        if (this.sysGroupTableTableUpdateOpen != null) {
            sb.append("\"sysGroupTableTableUpdateOpen\":\"").append(sysGroupTableTableUpdateOpen).append("\",");
        }
        if (this.sysGroupTableTableDeleteOpen != null) {
            sb.append("\"sysGroupTableTableDeleteOpen\":\"").append(sysGroupTableTableDeleteOpen).append("\",");
        }
        if (this.sysGroupTableButQueryOpen != null) {
            sb.append("\"sysGroupTableButQueryOpen\":\"").append(sysGroupTableButQueryOpen).append("\",");
        }
        if (this.sysGroupTableTableButWidth != null) {
            sb.append("\"sysGroupTableTableButWidth\":\"").append(sysGroupTableTableButWidth).append("\",");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":").append(Tools.toJson(entry.getValue())).append(",");
            }
        }else if(this.data != null){
            sb.append("\"").append(dataName).append("\":").append(Tools.toJson(this.data)).append(",");
        }
        if (sb.length()>1) {
            sb.delete(sb.length()-1, sb.length());
        }
       return sb.append("}").toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysGroupTableId", sysGroupTableId);
        result.set("sysGroupTableKey", sysGroupTableKey);
        result.set("sysGroupTableGid", sysGroupTableGid);
        result.set("sysGroupTableButSaveOpen", sysGroupTableButSaveOpen);
        result.set("sysGroupTableButDeleteOpen", sysGroupTableButDeleteOpen);
        result.set("sysGroupTableTableUpdateOpen", sysGroupTableTableUpdateOpen);
        result.set("sysGroupTableTableDeleteOpen", sysGroupTableTableDeleteOpen);
        result.set("sysGroupTableButQueryOpen", sysGroupTableButQueryOpen);
        result.set("sysGroupTableTableButWidth", sysGroupTableTableButWidth);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysGroupTableId=result.getLong("sysGroupTableId", null);
        this.sysGroupTableKey=result.getString("sysGroupTableKey", null);
        this.sysGroupTableGid=result.getLong("sysGroupTableGid", null);
        this.sysGroupTableButSaveOpen=result.getLong("sysGroupTableButSaveOpen", null);
        this.sysGroupTableButDeleteOpen=result.getLong("sysGroupTableButDeleteOpen", null);
        this.sysGroupTableTableUpdateOpen=result.getLong("sysGroupTableTableUpdateOpen", null);
        this.sysGroupTableTableDeleteOpen=result.getLong("sysGroupTableTableDeleteOpen", null);
        this.sysGroupTableButQueryOpen=result.getLong("sysGroupTableButQueryOpen", null);
        this.sysGroupTableTableButWidth=result.getLong("sysGroupTableTableButWidth", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_group_table_id")) {
            temp=resultSet.getString("sys_group_table_id");
            if (temp!=null) {
                this.sysGroupTableId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_key")) {
            temp=resultSet.getString("sys_group_table_key");
            if (temp!=null) {
                this.sysGroupTableKey=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_gid")) {
            temp=resultSet.getString("sys_group_table_gid");
            if (temp!=null) {
                this.sysGroupTableGid=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_but_save_open")) {
            temp=resultSet.getString("sys_group_table_but_save_open");
            if (temp!=null) {
                this.sysGroupTableButSaveOpen=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_but_delete_open")) {
            temp=resultSet.getString("sys_group_table_but_delete_open");
            if (temp!=null) {
                this.sysGroupTableButDeleteOpen=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_table_update_open")) {
            temp=resultSet.getString("sys_group_table_table_update_open");
            if (temp!=null) {
                this.sysGroupTableTableUpdateOpen=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_table_delete_open")) {
            temp=resultSet.getString("sys_group_table_table_delete_open");
            if (temp!=null) {
                this.sysGroupTableTableDeleteOpen=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_but_query_open")) {
            temp=resultSet.getString("sys_group_table_but_query_open");
            if (temp!=null) {
                this.sysGroupTableButQueryOpen=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_group_table_table_but_width")) {
            temp=resultSet.getString("sys_group_table_table_but_width");
            if (temp!=null) {
                this.sysGroupTableTableButWidth=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysGroupTableId() {
        return sysGroupTableId;
    }
    public SysGroupTable setSysGroupTableId(java.lang.Long sysGroupTableId) {
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
        this.sysGroupTableGid = sysGroupTableGid;
        return this;
    }
    public java.lang.Long getSysGroupTableButSaveOpen() {
        return sysGroupTableButSaveOpen;
    }
    public SysGroupTable setSysGroupTableButSaveOpen(java.lang.Long sysGroupTableButSaveOpen) {
        this.sysGroupTableButSaveOpen = sysGroupTableButSaveOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableButDeleteOpen() {
        return sysGroupTableButDeleteOpen;
    }
    public SysGroupTable setSysGroupTableButDeleteOpen(java.lang.Long sysGroupTableButDeleteOpen) {
        this.sysGroupTableButDeleteOpen = sysGroupTableButDeleteOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableUpdateOpen() {
        return sysGroupTableTableUpdateOpen;
    }
    public SysGroupTable setSysGroupTableTableUpdateOpen(java.lang.Long sysGroupTableTableUpdateOpen) {
        this.sysGroupTableTableUpdateOpen = sysGroupTableTableUpdateOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableDeleteOpen() {
        return sysGroupTableTableDeleteOpen;
    }
    public SysGroupTable setSysGroupTableTableDeleteOpen(java.lang.Long sysGroupTableTableDeleteOpen) {
        this.sysGroupTableTableDeleteOpen = sysGroupTableTableDeleteOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableButQueryOpen() {
        return sysGroupTableButQueryOpen;
    }
    public SysGroupTable setSysGroupTableButQueryOpen(java.lang.Long sysGroupTableButQueryOpen) {
        this.sysGroupTableButQueryOpen = sysGroupTableButQueryOpen;
        return this;
    }
    public java.lang.Long getSysGroupTableTableButWidth() {
        return sysGroupTableTableButWidth;
    }
    public SysGroupTable setSysGroupTableTableButWidth(java.lang.Long sysGroupTableTableButWidth) {
        this.sysGroupTableTableButWidth = sysGroupTableTableButWidth;
        return this;
    }
    public List<?> getList() {
        return (List<?>) data;
    }

    public SysGroupTable setList(List<?> data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) data;
    }

    public SysGroupTable setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroupTable putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysGroupTable setData(Object data) {
        this.data = data;
        return this;
    }}
