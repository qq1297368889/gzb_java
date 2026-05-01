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
    private Long sysOptionRequestId;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_url", desc = "sysOptionRequestUrl", type = "java.lang.String")
    private String sysOptionRequestUrl;
    @EntityAttribute(key = false, size = 10, name = "sys_option_request_met", desc = "sysOptionRequestMet", type = "java.lang.String")
    private String sysOptionRequestMet;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_data", desc = "sysOptionRequestData", type = "java.lang.String")
    private String sysOptionRequestData;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_title_name", desc = "sysOptionRequestTitleName", type = "java.lang.String")
    private String sysOptionRequestTitleName;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_val_name", desc = "sysOptionRequestValName", type = "java.lang.String")
    private String sysOptionRequestValName;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_url", desc = "sysOptionRequestSearchUrl", type = "java.lang.String")
    private String sysOptionRequestSearchUrl;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_met", desc = "sysOptionRequestSearchMet", type = "java.lang.String")
    private String sysOptionRequestSearchMet;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_data", desc = "sysOptionRequestSearchData", type = "java.lang.String")
    private String sysOptionRequestSearchData;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_title_name", desc = "sysOptionRequestSearchTitleName", type = "java.lang.String")
    private String sysOptionRequestSearchTitleName;
    @EntityAttribute(key = false, size = 255, name = "sys_option_request_search_val_name", desc = "sysOptionRequestSearchValName", type = "java.lang.String")
    private String sysOptionRequestSearchValName;
    @EntityAttribute(key = false, size = 100, name = "sys_option_request_key", desc = "sysOptionRequestKey", type = "java.lang.String")
    private String sysOptionRequestKey;
    private Object data;

    public SysOptionRequest() {
    } 

    public Long getSysOptionRequestId() {
        return sysOptionRequestId;
    }

    public SysOptionRequest setSysOptionRequestId(Long sysOptionRequestId) {
        this.sysOptionRequestId = sysOptionRequestId;
        return this;
    }

    public String getSysOptionRequestUrl() {
        return sysOptionRequestUrl;
    }

    public SysOptionRequest setSysOptionRequestUrl(String sysOptionRequestUrl) {
        this.sysOptionRequestUrl = sysOptionRequestUrl;
        return this;
    }

    public String getSysOptionRequestMet() {
        return sysOptionRequestMet;
    }

    public SysOptionRequest setSysOptionRequestMet(String sysOptionRequestMet) {
        this.sysOptionRequestMet = sysOptionRequestMet;
        return this;
    }

    public String getSysOptionRequestData() {
        return sysOptionRequestData;
    }

    public SysOptionRequest setSysOptionRequestData(String sysOptionRequestData) {
        this.sysOptionRequestData = sysOptionRequestData;
        return this;
    }

    public String getSysOptionRequestTitleName() {
        return sysOptionRequestTitleName;
    }

    public SysOptionRequest setSysOptionRequestTitleName(String sysOptionRequestTitleName) {
        this.sysOptionRequestTitleName = sysOptionRequestTitleName;
        return this;
    }

    public String getSysOptionRequestValName() {
        return sysOptionRequestValName;
    }

    public SysOptionRequest setSysOptionRequestValName(String sysOptionRequestValName) {
        this.sysOptionRequestValName = sysOptionRequestValName;
        return this;
    }

    public String getSysOptionRequestSearchUrl() {
        return sysOptionRequestSearchUrl;
    }

    public SysOptionRequest setSysOptionRequestSearchUrl(String sysOptionRequestSearchUrl) {
        this.sysOptionRequestSearchUrl = sysOptionRequestSearchUrl;
        return this;
    }

    public String getSysOptionRequestSearchMet() {
        return sysOptionRequestSearchMet;
    }

    public SysOptionRequest setSysOptionRequestSearchMet(String sysOptionRequestSearchMet) {
        this.sysOptionRequestSearchMet = sysOptionRequestSearchMet;
        return this;
    }

    public String getSysOptionRequestSearchData() {
        return sysOptionRequestSearchData;
    }

    public SysOptionRequest setSysOptionRequestSearchData(String sysOptionRequestSearchData) {
        this.sysOptionRequestSearchData = sysOptionRequestSearchData;
        return this;
    }

    public String getSysOptionRequestSearchTitleName() {
        return sysOptionRequestSearchTitleName;
    }

    public SysOptionRequest setSysOptionRequestSearchTitleName(String sysOptionRequestSearchTitleName) {
        this.sysOptionRequestSearchTitleName = sysOptionRequestSearchTitleName;
        return this;
    }

    public String getSysOptionRequestSearchValName() {
        return sysOptionRequestSearchValName;
    }

    public SysOptionRequest setSysOptionRequestSearchValName(String sysOptionRequestSearchValName) {
        this.sysOptionRequestSearchValName = sysOptionRequestSearchValName;
        return this;
    }

    public String getSysOptionRequestKey() {
        return sysOptionRequestKey;
    }

    public SysOptionRequest setSysOptionRequestKey(String sysOptionRequestKey) {
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