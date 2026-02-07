package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysMappingTableDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_mapping_table",desc="sysMappingTable")
public class SysMappingTable implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_mapping_table_id",desc="",type="java.lang.Long")
    private java.lang.Long sysMappingTableId;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_table_name",desc="",type="java.lang.String")
    private java.lang.String sysMappingTableName;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_table_title",desc="",type="java.lang.String")
    private java.lang.String sysMappingTableTitle;
    private Object data;
   public SysMappingTable() {}

    public SysMappingTable(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysMappingTable(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysMappingTable(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.save(this);
    }
    public int delete(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.delete(this);
    }
    public int update(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.update(this);
    }
    public int saveAsync(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.saveAsync(this);
    }
    public int deleteAsync(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.deleteAsync(this);
    }
    public int updateAsync(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.updateAsync(this);
    }
    public List<SysMappingTable> query(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.query(this);
    }
    public List<SysMappingTable> query(SysMappingTableDao sysMappingTableDao,int page,int size) throws Exception {
        return sysMappingTableDao.query(this,page,size);
    }
    public List<SysMappingTable> query(SysMappingTableDao sysMappingTableDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysMappingTableDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysMappingTableDao sysMappingTableDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysMappingTableDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysMappingTableDao sysMappingTableDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysMappingTableDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysMappingTable find(SysMappingTableDao sysMappingTableDao) throws Exception {
        return sysMappingTableDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(86);
       boolean app01=false;
        sb.append("{");
        if (this.sysMappingTableId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingTableId\":\"").append(sysMappingTableId).append("\"");
        }
        if (this.sysMappingTableName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingTableName\":");
            sb.append(Tools.toJson(sysMappingTableName));        }
        if (this.sysMappingTableTitle != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingTableTitle\":");
            sb.append(Tools.toJson(sysMappingTableTitle));        }
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
        result.set("sysMappingTableId", sysMappingTableId);
        result.set("sysMappingTableName", sysMappingTableName);
        result.set("sysMappingTableTitle", sysMappingTableTitle);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysMappingTableId=result.getLong("sysMappingTableId", null);
        this.sysMappingTableName=result.getString("sysMappingTableName", null);
        this.sysMappingTableTitle=result.getString("sysMappingTableTitle", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysMappingTableId() {
        return sysMappingTableId;
    }
    public SysMappingTable setSysMappingTableId(java.lang.Long sysMappingTableId) {
        this.sysMappingTableId = sysMappingTableId;
        return this;
    }
    public java.lang.String getSysMappingTableName() {
        return sysMappingTableName;
    }
    public SysMappingTable setSysMappingTableName(java.lang.String sysMappingTableName) {
        int size0 = Tools.textLength(sysMappingTableName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysMappingTable.sysMappingTableName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingTableName);
        }
        this.sysMappingTableName = sysMappingTableName;
        return this;
    }
    public SysMappingTable setSysMappingTableNameUnsafe(java.lang.String sysMappingTableName) {
        this.sysMappingTableName = sysMappingTableName;
        return this;
    }
    public java.lang.String getSysMappingTableTitle() {
        return sysMappingTableTitle;
    }
    public SysMappingTable setSysMappingTableTitle(java.lang.String sysMappingTableTitle) {
        int size0 = Tools.textLength(sysMappingTableTitle);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysMappingTable.sysMappingTableTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingTableTitle);
        }
        this.sysMappingTableTitle = sysMappingTableTitle;
        return this;
    }
    public SysMappingTable setSysMappingTableTitleUnsafe(java.lang.String sysMappingTableTitle) {
        this.sysMappingTableTitle = sysMappingTableTitle;
        return this;
    }
    public SysMappingTable setList(List<?> data) {
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

    public SysMappingTable setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysMappingTable putMap(String key, Object value) {
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
    public SysMappingTable setData(Object data) {
        this.data = data;
        return this;
    }
}
