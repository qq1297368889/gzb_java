package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysOptionRequestDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_option_request",desc="sysOptionRequest")
public class SysOptionRequest implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_option_request_id",desc="",type="java.lang.Long")
    private java.lang.Long sysOptionRequestId;
    @EntityAttribute(key=false,size = 100,name="sys_option_request_url",desc="请求地址",type="java.lang.String")
    private java.lang.String sysOptionRequestUrl;
    @EntityAttribute(key=false,size = 10,name="sys_option_request_met",desc="请求方式",type="java.lang.String")
    private java.lang.String sysOptionRequestMet;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_data",desc="请求参数",type="java.lang.String")
    private java.lang.String sysOptionRequestData;
    @EntityAttribute(key=false,size = 100,name="sys_option_request_title_name",desc="标题键名",type="java.lang.String")
    private java.lang.String sysOptionRequestTitleName;
    @EntityAttribute(key=false,size = 100,name="sys_option_request_val_name",desc="内容键名",type="java.lang.String")
    private java.lang.String sysOptionRequestValName;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_search_url",desc="搜索请求地址",type="java.lang.String")
    private java.lang.String sysOptionRequestSearchUrl;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_search_met",desc="搜索请求方式",type="java.lang.String")
    private java.lang.String sysOptionRequestSearchMet;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_search_data",desc="搜索请求参数",type="java.lang.String")
    private java.lang.String sysOptionRequestSearchData;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_search_title_name",desc="搜索标题键名",type="java.lang.String")
    private java.lang.String sysOptionRequestSearchTitleName;
    @EntityAttribute(key=false,size = 255,name="sys_option_request_search_val_name",desc="搜索内容键名",type="java.lang.String")
    private java.lang.String sysOptionRequestSearchValName;
    @EntityAttribute(key=false,size = 100,name="sys_option_request_key",desc="助记名称",type="java.lang.String")
    private java.lang.String sysOptionRequestKey;
    private Object data;
   public SysOptionRequest() {}

    public SysOptionRequest(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysOptionRequest(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysOptionRequest(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.save(this);
    }
    public int delete(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.delete(this);
    }
    public int update(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.update(this);
    }
    public int saveAsync(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.saveAsync(this);
    }
    public int deleteAsync(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.deleteAsync(this);
    }
    public int updateAsync(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.updateAsync(this);
    }
    public List<SysOptionRequest> query(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.query(this);
    }
    public List<SysOptionRequest> query(SysOptionRequestDao sysOptionRequestDao,int page,int size) throws Exception {
        return sysOptionRequestDao.query(this,page,size);
    }
    public List<SysOptionRequest> query(SysOptionRequestDao sysOptionRequestDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysOptionRequestDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysOptionRequestDao sysOptionRequestDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysOptionRequestDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysOptionRequestDao sysOptionRequestDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysOptionRequestDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysOptionRequest find(SysOptionRequestDao sysOptionRequestDao) throws Exception {
        return sysOptionRequestDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(399);
       boolean app01=false;
        sb.append("{");
        if (this.sysOptionRequestId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestId\":\"").append(sysOptionRequestId).append("\"");
        }
        if (this.sysOptionRequestUrl != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestUrl\":");
            sb.append(Tools.toJson(sysOptionRequestUrl));        }
        if (this.sysOptionRequestMet != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestMet\":");
            sb.append(Tools.toJson(sysOptionRequestMet));        }
        if (this.sysOptionRequestData != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestData\":");
            sb.append(Tools.toJson(sysOptionRequestData));        }
        if (this.sysOptionRequestTitleName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestTitleName\":");
            sb.append(Tools.toJson(sysOptionRequestTitleName));        }
        if (this.sysOptionRequestValName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestValName\":");
            sb.append(Tools.toJson(sysOptionRequestValName));        }
        if (this.sysOptionRequestSearchUrl != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestSearchUrl\":");
            sb.append(Tools.toJson(sysOptionRequestSearchUrl));        }
        if (this.sysOptionRequestSearchMet != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestSearchMet\":");
            sb.append(Tools.toJson(sysOptionRequestSearchMet));        }
        if (this.sysOptionRequestSearchData != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestSearchData\":");
            sb.append(Tools.toJson(sysOptionRequestSearchData));        }
        if (this.sysOptionRequestSearchTitleName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestSearchTitleName\":");
            sb.append(Tools.toJson(sysOptionRequestSearchTitleName));        }
        if (this.sysOptionRequestSearchValName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestSearchValName\":");
            sb.append(Tools.toJson(sysOptionRequestSearchValName));        }
        if (this.sysOptionRequestKey != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysOptionRequestKey\":");
            sb.append(Tools.toJson(sysOptionRequestKey));        }
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
        result.set("sysOptionRequestId", sysOptionRequestId);
        result.set("sysOptionRequestUrl", sysOptionRequestUrl);
        result.set("sysOptionRequestMet", sysOptionRequestMet);
        result.set("sysOptionRequestData", sysOptionRequestData);
        result.set("sysOptionRequestTitleName", sysOptionRequestTitleName);
        result.set("sysOptionRequestValName", sysOptionRequestValName);
        result.set("sysOptionRequestSearchUrl", sysOptionRequestSearchUrl);
        result.set("sysOptionRequestSearchMet", sysOptionRequestSearchMet);
        result.set("sysOptionRequestSearchData", sysOptionRequestSearchData);
        result.set("sysOptionRequestSearchTitleName", sysOptionRequestSearchTitleName);
        result.set("sysOptionRequestSearchValName", sysOptionRequestSearchValName);
        result.set("sysOptionRequestKey", sysOptionRequestKey);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysOptionRequestId=result.getLong("sysOptionRequestId", null);
        this.sysOptionRequestUrl=result.getString("sysOptionRequestUrl", null);
        this.sysOptionRequestMet=result.getString("sysOptionRequestMet", null);
        this.sysOptionRequestData=result.getString("sysOptionRequestData", null);
        this.sysOptionRequestTitleName=result.getString("sysOptionRequestTitleName", null);
        this.sysOptionRequestValName=result.getString("sysOptionRequestValName", null);
        this.sysOptionRequestSearchUrl=result.getString("sysOptionRequestSearchUrl", null);
        this.sysOptionRequestSearchMet=result.getString("sysOptionRequestSearchMet", null);
        this.sysOptionRequestSearchData=result.getString("sysOptionRequestSearchData", null);
        this.sysOptionRequestSearchTitleName=result.getString("sysOptionRequestSearchTitleName", null);
        this.sysOptionRequestSearchValName=result.getString("sysOptionRequestSearchValName", null);
        this.sysOptionRequestKey=result.getString("sysOptionRequestKey", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysOptionRequestId() {
        return sysOptionRequestId;
    }
    public SysOptionRequest setSysOptionRequestId(java.lang.Long sysOptionRequestId) {
        this.sysOptionRequestId = sysOptionRequestId;
        return this;
    }
    public java.lang.String getSysOptionRequestUrl() {
        return sysOptionRequestUrl;
    }
    public SysOptionRequest setSysOptionRequestUrl(java.lang.String sysOptionRequestUrl) {
        int size0 = Tools.textLength(sysOptionRequestUrl);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestUrl最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionRequestUrl);
        }
        this.sysOptionRequestUrl = sysOptionRequestUrl;
        return this;
    }
    public SysOptionRequest setSysOptionRequestUrlUnsafe(java.lang.String sysOptionRequestUrl) {
        this.sysOptionRequestUrl = sysOptionRequestUrl;
        return this;
    }
    public java.lang.String getSysOptionRequestMet() {
        return sysOptionRequestMet;
    }
    public SysOptionRequest setSysOptionRequestMet(java.lang.String sysOptionRequestMet) {
        int size0 = Tools.textLength(sysOptionRequestMet);
        if (size0 > 10) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestMet最大长度为:10,实际长度为:"+ size0 +",数据为:"+sysOptionRequestMet);
        }
        this.sysOptionRequestMet = sysOptionRequestMet;
        return this;
    }
    public SysOptionRequest setSysOptionRequestMetUnsafe(java.lang.String sysOptionRequestMet) {
        this.sysOptionRequestMet = sysOptionRequestMet;
        return this;
    }
    public java.lang.String getSysOptionRequestData() {
        return sysOptionRequestData;
    }
    public SysOptionRequest setSysOptionRequestData(java.lang.String sysOptionRequestData) {
        int size0 = Tools.textLength(sysOptionRequestData);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestData最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestData);
        }
        this.sysOptionRequestData = sysOptionRequestData;
        return this;
    }
    public SysOptionRequest setSysOptionRequestDataUnsafe(java.lang.String sysOptionRequestData) {
        this.sysOptionRequestData = sysOptionRequestData;
        return this;
    }
    public java.lang.String getSysOptionRequestTitleName() {
        return sysOptionRequestTitleName;
    }
    public SysOptionRequest setSysOptionRequestTitleName(java.lang.String sysOptionRequestTitleName) {
        int size0 = Tools.textLength(sysOptionRequestTitleName);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestTitleName最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionRequestTitleName);
        }
        this.sysOptionRequestTitleName = sysOptionRequestTitleName;
        return this;
    }
    public SysOptionRequest setSysOptionRequestTitleNameUnsafe(java.lang.String sysOptionRequestTitleName) {
        this.sysOptionRequestTitleName = sysOptionRequestTitleName;
        return this;
    }
    public java.lang.String getSysOptionRequestValName() {
        return sysOptionRequestValName;
    }
    public SysOptionRequest setSysOptionRequestValName(java.lang.String sysOptionRequestValName) {
        int size0 = Tools.textLength(sysOptionRequestValName);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestValName最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionRequestValName);
        }
        this.sysOptionRequestValName = sysOptionRequestValName;
        return this;
    }
    public SysOptionRequest setSysOptionRequestValNameUnsafe(java.lang.String sysOptionRequestValName) {
        this.sysOptionRequestValName = sysOptionRequestValName;
        return this;
    }
    public java.lang.String getSysOptionRequestSearchUrl() {
        return sysOptionRequestSearchUrl;
    }
    public SysOptionRequest setSysOptionRequestSearchUrl(java.lang.String sysOptionRequestSearchUrl) {
        int size0 = Tools.textLength(sysOptionRequestSearchUrl);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestSearchUrl最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestSearchUrl);
        }
        this.sysOptionRequestSearchUrl = sysOptionRequestSearchUrl;
        return this;
    }
    public SysOptionRequest setSysOptionRequestSearchUrlUnsafe(java.lang.String sysOptionRequestSearchUrl) {
        this.sysOptionRequestSearchUrl = sysOptionRequestSearchUrl;
        return this;
    }
    public java.lang.String getSysOptionRequestSearchMet() {
        return sysOptionRequestSearchMet;
    }
    public SysOptionRequest setSysOptionRequestSearchMet(java.lang.String sysOptionRequestSearchMet) {
        int size0 = Tools.textLength(sysOptionRequestSearchMet);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestSearchMet最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestSearchMet);
        }
        this.sysOptionRequestSearchMet = sysOptionRequestSearchMet;
        return this;
    }
    public SysOptionRequest setSysOptionRequestSearchMetUnsafe(java.lang.String sysOptionRequestSearchMet) {
        this.sysOptionRequestSearchMet = sysOptionRequestSearchMet;
        return this;
    }
    public java.lang.String getSysOptionRequestSearchData() {
        return sysOptionRequestSearchData;
    }
    public SysOptionRequest setSysOptionRequestSearchData(java.lang.String sysOptionRequestSearchData) {
        int size0 = Tools.textLength(sysOptionRequestSearchData);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestSearchData最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestSearchData);
        }
        this.sysOptionRequestSearchData = sysOptionRequestSearchData;
        return this;
    }
    public SysOptionRequest setSysOptionRequestSearchDataUnsafe(java.lang.String sysOptionRequestSearchData) {
        this.sysOptionRequestSearchData = sysOptionRequestSearchData;
        return this;
    }
    public java.lang.String getSysOptionRequestSearchTitleName() {
        return sysOptionRequestSearchTitleName;
    }
    public SysOptionRequest setSysOptionRequestSearchTitleName(java.lang.String sysOptionRequestSearchTitleName) {
        int size0 = Tools.textLength(sysOptionRequestSearchTitleName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestSearchTitleName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestSearchTitleName);
        }
        this.sysOptionRequestSearchTitleName = sysOptionRequestSearchTitleName;
        return this;
    }
    public SysOptionRequest setSysOptionRequestSearchTitleNameUnsafe(java.lang.String sysOptionRequestSearchTitleName) {
        this.sysOptionRequestSearchTitleName = sysOptionRequestSearchTitleName;
        return this;
    }
    public java.lang.String getSysOptionRequestSearchValName() {
        return sysOptionRequestSearchValName;
    }
    public SysOptionRequest setSysOptionRequestSearchValName(java.lang.String sysOptionRequestSearchValName) {
        int size0 = Tools.textLength(sysOptionRequestSearchValName);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestSearchValName最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionRequestSearchValName);
        }
        this.sysOptionRequestSearchValName = sysOptionRequestSearchValName;
        return this;
    }
    public SysOptionRequest setSysOptionRequestSearchValNameUnsafe(java.lang.String sysOptionRequestSearchValName) {
        this.sysOptionRequestSearchValName = sysOptionRequestSearchValName;
        return this;
    }
    public java.lang.String getSysOptionRequestKey() {
        return sysOptionRequestKey;
    }
    public SysOptionRequest setSysOptionRequestKey(java.lang.String sysOptionRequestKey) {
        int size0 = Tools.textLength(sysOptionRequestKey);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysOptionRequest.sysOptionRequestKey最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysOptionRequestKey);
        }
        this.sysOptionRequestKey = sysOptionRequestKey;
        return this;
    }
    public SysOptionRequest setSysOptionRequestKeyUnsafe(java.lang.String sysOptionRequestKey) {
        this.sysOptionRequestKey = sysOptionRequestKey;
        return this;
    }
    public SysOptionRequest setList(List<?> data) {
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

    public SysOptionRequest setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysOptionRequest putMap(String key, Object value) {
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
    public SysOptionRequest setData(Object data) {
        this.data = data;
        return this;
    }
}
