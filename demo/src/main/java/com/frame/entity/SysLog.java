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
    @EntityAttribute(key=true,size = 19,name="sys_log_id",desc="日志ID",type="java.lang.Long")
    private java.lang.Long sysLogId;
    @EntityAttribute(key=false,size = 19,name="sys_log_time",desc="时间",type="java.time.LocalDateTime")
    private java.time.LocalDateTime sysLogTime;
    @EntityAttribute(key=false,size = 19,name="sys_log_ms",desc="耗时",type="java.lang.Long")
    private java.lang.Long sysLogMs;
    @EntityAttribute(key=false,size = 536870911,name="sys_log_sql",desc="sql",type="java.lang.String")
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
        StringBuilder sb = new StringBuilder(75);
       boolean app01=false;
        sb.append("{");
        if (this.sysLogId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysLogId\":\"").append(sysLogId).append("\"");
        }
        if (this.sysLogTime != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysLogTime\":");
            sb.append(Tools.toJson(sysLogTime));        }
        if (this.sysLogMs != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysLogMs\":\"").append(sysLogMs).append("\"");
        }
        if (this.sysLogSql != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysLogSql\":");
            sb.append(Tools.toJson(sysLogSql));        }
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
        result.set("sysLogId", sysLogId);
        result.set("sysLogTime", sysLogTime);
        result.set("sysLogMs", sysLogMs);
        result.set("sysLogSql", sysLogSql);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysLogId=result.getLong("sysLogId", null);
        this.sysLogTime=result.getLocalDateTime("sysLogTime", null);
        this.sysLogMs=result.getLong("sysLogMs", null);
        this.sysLogSql=result.getString("sysLogSql", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysLogId() {
        return sysLogId;
    }
    public SysLog setSysLogId(java.lang.Long sysLogId) {
        this.sysLogId = sysLogId;
        return this;
    }
    public java.time.LocalDateTime getSysLogTime() {
        return sysLogTime;
    }
    public SysLog setSysLogTime(java.time.LocalDateTime sysLogTime) {
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
            throw new gzb.exception.GzbException0("SysLog.sysLogSql最大长度为:536870911,实际长度为:"+ size0 +",数据为:"+sysLogSql);
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
    public SysLog setData(Object data) {
        this.data = data;
        return this;
    }
}
