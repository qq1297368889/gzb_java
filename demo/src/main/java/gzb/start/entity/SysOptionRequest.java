package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_option_request", desc = "sysOptionRequest")
public class SysOptionRequest implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_option_request_id", desc = "sysOptionRequestId", type = "java.lang.Long")
    private java.lang.Long sysOptionRequestId;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_url", desc = "sysOptionRequestUrl", type = "java.lang.String")
    private java.lang.String sysOptionRequestUrl;
    @EntityAttribute(key = false, size = 10, name = "sys_option_request_met", desc = "sysOptionRequestMet", type = "java.lang.String")
    private java.lang.String sysOptionRequestMet;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_data", desc = "sysOptionRequestData", type = "java.lang.String")
    private java.lang.String sysOptionRequestData;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_title_name", desc = "sysOptionRequestTitleName", type = "java.lang.String")
    private java.lang.String sysOptionRequestTitleName;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_val_name", desc = "sysOptionRequestValName", type = "java.lang.String")
    private java.lang.String sysOptionRequestValName;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_url", desc = "sysOptionRequestSearchUrl", type = "java.lang.String")
    private java.lang.String sysOptionRequestSearchUrl;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_met", desc = "sysOptionRequestSearchMet", type = "java.lang.String")
    private java.lang.String sysOptionRequestSearchMet;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_data", desc = "sysOptionRequestSearchData", type = "java.lang.String")
    private java.lang.String sysOptionRequestSearchData;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_title_name", desc = "sysOptionRequestSearchTitleName", type = "java.lang.String")
    private java.lang.String sysOptionRequestSearchTitleName;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_val_name", desc = "sysOptionRequestSearchValName", type = "java.lang.String")
    private java.lang.String sysOptionRequestSearchValName;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_key", desc = "sysOptionRequestKey", type = "java.lang.String")
    private java.lang.String sysOptionRequestKey;
    private Object data;

    public SysOptionRequest() {
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
        this.sysOptionRequestUrl = sysOptionRequestUrl;
        return this;
    }

    public java.lang.String getSysOptionRequestMet() {
        return sysOptionRequestMet;
    }

    public SysOptionRequest setSysOptionRequestMet(java.lang.String sysOptionRequestMet) {
        this.sysOptionRequestMet = sysOptionRequestMet;
        return this;
    }

    public java.lang.String getSysOptionRequestData() {
        return sysOptionRequestData;
    }

    public SysOptionRequest setSysOptionRequestData(java.lang.String sysOptionRequestData) {
        this.sysOptionRequestData = sysOptionRequestData;
        return this;
    }

    public java.lang.String getSysOptionRequestTitleName() {
        return sysOptionRequestTitleName;
    }

    public SysOptionRequest setSysOptionRequestTitleName(java.lang.String sysOptionRequestTitleName) {
        this.sysOptionRequestTitleName = sysOptionRequestTitleName;
        return this;
    }

    public java.lang.String getSysOptionRequestValName() {
        return sysOptionRequestValName;
    }

    public SysOptionRequest setSysOptionRequestValName(java.lang.String sysOptionRequestValName) {
        this.sysOptionRequestValName = sysOptionRequestValName;
        return this;
    }

    public java.lang.String getSysOptionRequestSearchUrl() {
        return sysOptionRequestSearchUrl;
    }

    public SysOptionRequest setSysOptionRequestSearchUrl(java.lang.String sysOptionRequestSearchUrl) {
        this.sysOptionRequestSearchUrl = sysOptionRequestSearchUrl;
        return this;
    }

    public java.lang.String getSysOptionRequestSearchMet() {
        return sysOptionRequestSearchMet;
    }

    public SysOptionRequest setSysOptionRequestSearchMet(java.lang.String sysOptionRequestSearchMet) {
        this.sysOptionRequestSearchMet = sysOptionRequestSearchMet;
        return this;
    }

    public java.lang.String getSysOptionRequestSearchData() {
        return sysOptionRequestSearchData;
    }

    public SysOptionRequest setSysOptionRequestSearchData(java.lang.String sysOptionRequestSearchData) {
        this.sysOptionRequestSearchData = sysOptionRequestSearchData;
        return this;
    }

    public java.lang.String getSysOptionRequestSearchTitleName() {
        return sysOptionRequestSearchTitleName;
    }

    public SysOptionRequest setSysOptionRequestSearchTitleName(java.lang.String sysOptionRequestSearchTitleName) {
        this.sysOptionRequestSearchTitleName = sysOptionRequestSearchTitleName;
        return this;
    }

    public java.lang.String getSysOptionRequestSearchValName() {
        return sysOptionRequestSearchValName;
    }

    public SysOptionRequest setSysOptionRequestSearchValName(java.lang.String sysOptionRequestSearchValName) {
        this.sysOptionRequestSearchValName = sysOptionRequestSearchValName;
        return this;
    }

    public java.lang.String getSysOptionRequestKey() {
        return sysOptionRequestKey;
    }

    public SysOptionRequest setSysOptionRequestKey(java.lang.String sysOptionRequestKey) {
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
            throw new gzb.exception.GzbException0("无法将" + this.data + " 转换为MAP");
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