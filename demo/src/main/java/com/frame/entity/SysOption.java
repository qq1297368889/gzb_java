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
    @EntityAttribute(key=true,size = 19,name="sys_option_id",desc="选项id",type="java.lang.Long")
    private java.lang.Long sysOptionId;
    @EntityAttribute(key=false,size = 100,name="sys_option_key",desc="选项名称",type="java.lang.String")
    private java.lang.String sysOptionKey;
    @EntityAttribute(key=false,size = 255,name="sys_option_title",desc="选项标题",type="java.lang.String")
    private java.lang.String sysOptionTitle;
    @EntityAttribute(key=false,size = 255,name="sys_option_value",desc="选项值",type="java.lang.String")
    private java.lang.String sysOptionValue;
    @EntityAttribute(key=false,size = 19,name="sys_option_state",desc="选项状态",type="java.lang.Long")
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
        StringBuilder sb = new StringBuilder(115);
       boolean app01=false;
        sb.append("{");
        if (this.sysOptionId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionId\":\"").append(sysOptionId).append("\"");
        }
        if (this.sysOptionKey != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionKey\":");
            sb.append(Tools.toJson(sysOptionKey));        }
        if (this.sysOptionTitle != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionTitle\":");
            sb.append(Tools.toJson(sysOptionTitle));        }
        if (this.sysOptionValue != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionValue\":");
            sb.append(Tools.toJson(sysOptionValue));        }
        if (this.sysOptionState != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionState\":\"").append(sysOptionState).append("\"");
        }
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
        result.set("sysOptionId", sysOptionId);
        result.set("sysOptionKey", sysOptionKey);
        result.set("sysOptionTitle", sysOptionTitle);
        result.set("sysOptionValue", sysOptionValue);
        result.set("sysOptionState", sysOptionState);
        result.set(Config.get("json.entity.data","data"), data);
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
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
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
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOption.sysOptionKey最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionKey);
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
            throw new gzb.exception.GzbException0("SysOption.sysOptionTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionTitle);
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
            throw new gzb.exception.GzbException0("SysOption.sysOptionValue最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionValue);
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
    public SysOption setList(List<?> data) {
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

    public SysOption setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysOption putMap(String key, Object value) {
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
    public SysOption setData(Object data) {
        this.data = data;
        return this;
    }
}
