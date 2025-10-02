package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysLogDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_log",desc="sysLog")
public class SysLog implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_log_id",desc="sysLogId")
    private java.lang.Long sysLogId;
    @EntityAttribute(key=false,size = 19,name="sys_log_time",desc="sysLogTime")
    private java.lang.String sysLogTime;
    @EntityAttribute(key=false,size = 19,name="sys_log_ms",desc="sysLogMs")
    private java.lang.Long sysLogMs;
    @EntityAttribute(key=false,size = 536870911,name="sys_log_sql",desc="sysLogSql")
    private java.lang.String sysLogSql;
    private Object data;
   public SysLog() {}

    public SysLog(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysLog(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysLog(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysLog(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
    }
    public int save(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.save(this);
    }
    public int delete(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.delete(this);
    }
    public int update(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.update(this);
    }
    public int saveAsync(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.saveAsync(this);
    }
    public int deleteAsync(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.deleteAsync(this);
    }
    public int updateAsync(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.updateAsync(this);
    }
    public List<SysLog> query(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.query(this);
    }
    public List<SysLog> query(SysLogDao sysLogDao,int page,int size) throws Exception {
        return sysLogDao.query(this,page,size);
    }
    public List<SysLog> query(SysLogDao sysLogDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysLogDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysLogDao sysLogDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysLogDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysLogDao sysLogDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysLogDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysLog find(SysLogDao sysLogDao) throws Exception {
        return sysLogDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysLogId != null) {
            sb.append("\"sysLogId\":\"").append(sysLogId).append("\",");
        }
        if (this.sysLogTime != null) {
            sb.append("\"sysLogTime\":\"").append(sysLogTime).append("\",");
        }
        if (this.sysLogMs != null) {
            sb.append("\"sysLogMs\":\"").append(sysLogMs).append("\",");
        }
        if (this.sysLogSql != null) {
            sb.append("\"sysLogSql\":\"").append(sysLogSql).append("\",");
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
        result.set("sysLogId", sysLogId);
        result.set("sysLogTime", sysLogTime);
        result.set("sysLogMs", sysLogMs);
        result.set("sysLogSql", sysLogSql);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysLogId=result.getLong("sysLogId", null);
        this.sysLogTime=result.getString("sysLogTime", null);
        this.sysLogMs=result.getLong("sysLogMs", null);
        this.sysLogSql=result.getString("sysLogSql", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_log_id")) {
            temp=resultSet.getString("sys_log_id");
            if (temp!=null) {
                this.sysLogId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_log_time")) {
            temp=resultSet.getString("sys_log_time");
            if (temp!=null) {
                this.sysLogTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_log_ms")) {
            temp=resultSet.getString("sys_log_ms");
            if (temp!=null) {
                this.sysLogMs=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_log_sql")) {
            temp=resultSet.getString("sys_log_sql");
            if (temp!=null) {
                this.sysLogSql=java.lang.String.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysLogId() {
        return sysLogId;
    }
    public SysLog setSysLogId(java.lang.Long sysLogId) {
        this.sysLogId = sysLogId;
        return this;
    }
    public java.lang.String getSysLogTime() {
        return sysLogTime;
    }
    public SysLog setSysLogTime(java.lang.String sysLogTime) {
        int size0 = Tools.textLength(sysLogTime);
        if (size0 > 19) {
            throw new RuntimeException("SysLog.sysLogTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysLogTime);
        }
        this.sysLogTime = sysLogTime;
        return this;
    }
    public SysLog setSysLogTimeUnsafe(java.lang.String sysLogTime) {
        this.sysLogTime = sysLogTime;
        return this;
    }
    public java.lang.Long getSysLogMs() {
        return sysLogMs;
    }
    public SysLog setSysLogMs(java.lang.Long sysLogMs) {
        this.sysLogMs = sysLogMs;
        return this;
    }
    public java.lang.String getSysLogSql() {
        return sysLogSql;
    }
    public SysLog setSysLogSql(java.lang.String sysLogSql) {
        int size0 = Tools.textLength(sysLogSql);
        if (size0 > 536870911) {
            throw new RuntimeException("SysLog.sysLogSql最大长度为:536870911,实际长度为:"+ size0 +",数据为:"+sysLogSql);
        }
        this.sysLogSql = sysLogSql;
        return this;
    }
    public SysLog setSysLogSqlUnsafe(java.lang.String sysLogSql) {
        this.sysLogSql = sysLogSql;
        return this;
    }
    public SysLog setList(List<?> data) {
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

    public SysLog setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysLog putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysLog setData(Object data) {
        this.data = data;
        return this;
    }}
