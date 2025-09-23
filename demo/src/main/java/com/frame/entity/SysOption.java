package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysOptionDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_option",desc="sysOption")
public class SysOption implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_option_id",desc="sysOptionId")
    private java.lang.Long sysOptionId;
    @EntityAttribute(key=false,size = 255,name="sys_option_key",desc="sysOptionKey")
    private java.lang.String sysOptionKey;
    @EntityAttribute(key=false,size = 255,name="sys_option_title",desc="sysOptionTitle")
    private java.lang.String sysOptionTitle;
    @EntityAttribute(key=false,size = 255,name="sys_option_value",desc="sysOptionValue")
    private java.lang.String sysOptionValue;
    @EntityAttribute(key=false,size = 19,name="sys_option_state",desc="sysOptionState")
    private java.lang.Long sysOptionState;
    private Object data;
   public SysOption() {}

    public SysOption(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysOption(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysOption(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysOption(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
    }
    public int save(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.save(this);
    }
    public int delete(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.delete(this);
    }
    public int update(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.update(this);
    }
    public int saveAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.saveAsync(this);
    }
    public int deleteAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.deleteAsync(this);
    }
    public int updateAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.updateAsync(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.query(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,int page,int size) throws Exception {
        return sysOptionDao.query(this,page,size);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysOptionDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysOption find(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysOptionId != null) {
            sb.append("\"sysOptionId\":\"").append(sysOptionId).append("\",");
        }
        if (this.sysOptionKey != null) {
            sb.append("\"sysOptionKey\":\"").append(sysOptionKey).append("\",");
        }
        if (this.sysOptionTitle != null) {
            sb.append("\"sysOptionTitle\":\"").append(sysOptionTitle).append("\",");
        }
        if (this.sysOptionValue != null) {
            sb.append("\"sysOptionValue\":\"").append(sysOptionValue).append("\",");
        }
        if (this.sysOptionState != null) {
            sb.append("\"sysOptionState\":\"").append(sysOptionState).append("\",");
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
        result.set("sysOptionId", sysOptionId);
        result.set("sysOptionKey", sysOptionKey);
        result.set("sysOptionTitle", sysOptionTitle);
        result.set("sysOptionValue", sysOptionValue);
        result.set("sysOptionState", sysOptionState);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysOptionId=result.getLong("sysOptionId", null);
        this.sysOptionKey=result.getString("sysOptionKey", null);
        this.sysOptionTitle=result.getString("sysOptionTitle", null);
        this.sysOptionValue=result.getString("sysOptionValue", null);
        this.sysOptionState=result.getLong("sysOptionState", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_option_id")) {
            temp=resultSet.getString("sys_option_id");
            if (temp!=null) {
                this.sysOptionId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_option_key")) {
            temp=resultSet.getString("sys_option_key");
            if (temp!=null) {
                this.sysOptionKey=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_option_title")) {
            temp=resultSet.getString("sys_option_title");
            if (temp!=null) {
                this.sysOptionTitle=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_option_value")) {
            temp=resultSet.getString("sys_option_value");
            if (temp!=null) {
                this.sysOptionValue=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_option_state")) {
            temp=resultSet.getString("sys_option_state");
            if (temp!=null) {
                this.sysOptionState=java.lang.Long.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysOptionId() {
        return sysOptionId;
    }
    public SysOption setSysOptionId(java.lang.Long sysOptionId) {
        this.sysOptionId = sysOptionId;
        return this;
    }
    public java.lang.String getSysOptionKey() {
        return sysOptionKey;
    }
    public SysOption setSysOptionKey(java.lang.String sysOptionKey) {
        int size0 = Tools.textLength(sysOptionKey);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionKey最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionKey);
        }
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public SysOption setSysOptionKeyUnsafe(java.lang.String sysOptionKey) {
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public java.lang.String getSysOptionTitle() {
        return sysOptionTitle;
    }
    public SysOption setSysOptionTitle(java.lang.String sysOptionTitle) {
        int size0 = Tools.textLength(sysOptionTitle);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionTitle);
        }
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public SysOption setSysOptionTitleUnsafe(java.lang.String sysOptionTitle) {
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public java.lang.String getSysOptionValue() {
        return sysOptionValue;
    }
    public SysOption setSysOptionValue(java.lang.String sysOptionValue) {
        int size0 = Tools.textLength(sysOptionValue);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionValue最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionValue);
        }
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public SysOption setSysOptionValueUnsafe(java.lang.String sysOptionValue) {
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public java.lang.Long getSysOptionState() {
        return sysOptionState;
    }
    public SysOption setSysOptionState(java.lang.Long sysOptionState) {
        this.sysOptionState = sysOptionState;
        return this;
    }
    public List<?> getList() {
        return (List<?>) data;
    }

    public SysOption setList(List<?> data) {
        this.data = data;
        return this;
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) data;
    }

    public SysOption setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysOption putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysOption setData(Object data) {
        this.data = data;
        return this;
    }}
