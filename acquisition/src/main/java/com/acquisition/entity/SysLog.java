package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.SysLogDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_log",desc="sysLog")
public class SysLog implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_log_id",desc="sysLogId")
    private java.lang.Long sysLogId;
    @EntityAttribute(key=false,size = 19,name="sys_log_time",desc="sysLogTime")
    private java.lang.String sysLogTime;
    @EntityAttribute(key=false,size = 19,name="sys_log_ms",desc="sysLogMs")
    private java.lang.Long sysLogMs;
    @EntityAttribute(key=false,size = 536870911,name="sys_log_sql",desc="sysLogSql")
    private java.lang.String sysLogSql;
    private List<?> list;
    public SysLog() {}

    public SysLog(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysLog(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysLogId");
        if (str!=null && !str.isEmpty()) {
            setSysLogId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysLogTime");
        if (str!=null && !str.isEmpty()) {
            setSysLogTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysLogMs");
        if (str!=null && !str.isEmpty()) {
            setSysLogMs(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysLogSql");
        if (str!=null && !str.isEmpty()) {
            setSysLogSql(java.lang.String.valueOf(str));
        }
    }

    public SysLog(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysLog(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
        json.put("sysLogId", getSysLogId());
        json.put("sysLogTime", getSysLogTime());
        json.put("sysLogMs", getSysLogMs());
        json.put("sysLogSql", getSysLogSql());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysLogId() {
        return sysLogId;
    }
    public SysLog setSysLogId(java.lang.Long sysLogId) {
        int size0 = Tools.textLength(sysLogId);
        if (size0 > 19) {
            throw new RuntimeException("SysLog.sysLogId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysLogId);
        }
        this.sysLogId = sysLogId;
        return this;
    }
    public SysLog setSysLogIdUnsafe(java.lang.Long sysLogId) {
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
        int size0 = Tools.textLength(sysLogMs);
        if (size0 > 19) {
            throw new RuntimeException("SysLog.sysLogMs最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysLogMs);
        }
        this.sysLogMs = sysLogMs;
        return this;
    }
    public SysLog setSysLogMsUnsafe(java.lang.Long sysLogMs) {
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
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
