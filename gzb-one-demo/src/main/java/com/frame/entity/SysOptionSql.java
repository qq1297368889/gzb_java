package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysOptionSqlDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_option_sql",desc="sysOptionSql")
public class SysOptionSql implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 0,name="sys_option_sql_id",desc="唯一索引",type="bigint")
    private Long sysOptionSqlId;
    @EntityAttribute(key=false,size = 255,name="sys_option_sql_sql",desc="sql语句",type="varchar(255)")
    private String sysOptionSqlSql;
    @EntityAttribute(key=false,size = 255,name="sys_option_sql_title_name",desc="标题键名",type="varchar(255)")
    private String sysOptionSqlTitleName;
    @EntityAttribute(key=false,size = 255,name="sys_option_sql_val_name",desc="内容键名",type="varchar(255)")
    private String sysOptionSqlValName;
    @EntityAttribute(key=false,size = 100,name="sys_option_sql_key",desc="助记名称",type="varchar(100)")
    private String sysOptionSqlKey;
    private Object data;
   public SysOptionSql() {}

    public SysOptionSql(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysOptionSql(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysOptionSql(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysOptionSqlDao sysOptionSqlDao) throws Exception {
        return sysOptionSqlDao.save(this);
    }
    public int delete(SysOptionSqlDao sysOptionSqlDao) throws Exception {
        return sysOptionSqlDao.delete(this);
    }
    public int update(SysOptionSqlDao sysOptionSqlDao) throws Exception {
        return sysOptionSqlDao.update(this);
    }
    public List<SysOptionSql> query(SysOptionSqlDao sysOptionSqlDao) throws Exception {
        return sysOptionSqlDao.query(this);
    }
    public List<SysOptionSql> query(SysOptionSqlDao sysOptionSqlDao,int page,int size) throws Exception {
        return sysOptionSqlDao.query(this,page,size);
    }
    public List<SysOptionSql> query(SysOptionSqlDao sysOptionSqlDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysOptionSqlDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysOptionSqlDao sysOptionSqlDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysOptionSqlDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysOptionSqlDao sysOptionSqlDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysOptionSqlDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysOptionSql find(SysOptionSqlDao sysOptionSqlDao) throws Exception {
        return sysOptionSqlDao.find(this);
    }
    @Override
    public String toString() {
        gzb.tools.thread.GzbThreadLocal.Entity entity0 = gzb.tools.thread.GzbThreadLocal.context.get();
        int index0=entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
       boolean app01=false;
        sb.append("{");
        if (this.sysOptionSqlId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionSqlId\":\"").append(sysOptionSqlId).append("\"");
        }
        if (this.sysOptionSqlSql != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionSqlSql\":");
            sb.append(Tools.toJson(sysOptionSqlSql));        }
        if (this.sysOptionSqlTitleName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionSqlTitleName\":");
            sb.append(Tools.toJson(sysOptionSqlTitleName));        }
        if (this.sysOptionSqlValName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionSqlValName\":");
            sb.append(Tools.toJson(sysOptionSqlValName));        }
        if (this.sysOptionSqlKey != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionSqlKey\":");
            sb.append(Tools.toJson(sysOptionSqlKey));        }
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
        result.set("sysOptionSqlId", sysOptionSqlId);
        result.set("sysOptionSqlSql", sysOptionSqlSql);
        result.set("sysOptionSqlTitleName", sysOptionSqlTitleName);
        result.set("sysOptionSqlValName", sysOptionSqlValName);
        result.set("sysOptionSqlKey", sysOptionSqlKey);
        result.set(Config.entityDataListName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysOptionSqlId=result.getLong("sysOptionSqlId", null);
        this.sysOptionSqlSql=result.getString("sysOptionSqlSql", null);
        this.sysOptionSqlTitleName=result.getString("sysOptionSqlTitleName", null);
        this.sysOptionSqlValName=result.getString("sysOptionSqlValName", null);
        this.sysOptionSqlKey=result.getString("sysOptionSqlKey", null);
        Object obj = result.get(Config.entityDataListName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public Long getSysOptionSqlId() {
        return sysOptionSqlId;
    }
    public SysOptionSql setSysOptionSqlId(Long sysOptionSqlId) {
        this.sysOptionSqlId = sysOptionSqlId;
        return this;
    }
    public String getSysOptionSqlSql() {
        return sysOptionSqlSql;
    }
    public SysOptionSql setSysOptionSqlSql(String sysOptionSqlSql) {
        int size0 = Tools.textLength(sysOptionSqlSql);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionSql.sysOptionSqlSql最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionSqlSql);
        }
        this.sysOptionSqlSql = sysOptionSqlSql;
        return this;
    }
    public SysOptionSql setSysOptionSqlSqlUnsafe(String sysOptionSqlSql) {
        this.sysOptionSqlSql = sysOptionSqlSql;
        return this;
    }
    public String getSysOptionSqlTitleName() {
        return sysOptionSqlTitleName;
    }
    public SysOptionSql setSysOptionSqlTitleName(String sysOptionSqlTitleName) {
        int size0 = Tools.textLength(sysOptionSqlTitleName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionSql.sysOptionSqlTitleName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionSqlTitleName);
        }
        this.sysOptionSqlTitleName = sysOptionSqlTitleName;
        return this;
    }
    public SysOptionSql setSysOptionSqlTitleNameUnsafe(String sysOptionSqlTitleName) {
        this.sysOptionSqlTitleName = sysOptionSqlTitleName;
        return this;
    }
    public String getSysOptionSqlValName() {
        return sysOptionSqlValName;
    }
    public SysOptionSql setSysOptionSqlValName(String sysOptionSqlValName) {
        int size0 = Tools.textLength(sysOptionSqlValName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionSql.sysOptionSqlValName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionSqlValName);
        }
        this.sysOptionSqlValName = sysOptionSqlValName;
        return this;
    }
    public SysOptionSql setSysOptionSqlValNameUnsafe(String sysOptionSqlValName) {
        this.sysOptionSqlValName = sysOptionSqlValName;
        return this;
    }
    public String getSysOptionSqlKey() {
        return sysOptionSqlKey;
    }
    public SysOptionSql setSysOptionSqlKey(String sysOptionSqlKey) {
        int size0 = Tools.textLength(sysOptionSqlKey);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOptionSql.sysOptionSqlKey最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionSqlKey);
        }
        this.sysOptionSqlKey = sysOptionSqlKey;
        return this;
    }
    public SysOptionSql setSysOptionSqlKeyUnsafe(String sysOptionSqlKey) {
        this.sysOptionSqlKey = sysOptionSqlKey;
        return this;
    }
    public SysOptionSql setList(List<?> data) {
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

    public SysOptionSql setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysOptionSql putMap(String key, Object value) {
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
    public SysOptionSql setData(Object data) {
        this.data = data;
        return this;
    }
}
