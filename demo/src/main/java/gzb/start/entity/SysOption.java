package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_option", desc = "sysOption")
public class SysOption implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_option_id", desc = "sysOptionId", type = "java.lang.Long")
    private java.lang.Long sysOptionId;
    @EntityAttribute(key = false, size = 100, name = "sys_option_key", desc = "sysOptionKey", type = "java.lang.String")
    private java.lang.String sysOptionKey;
    @EntityAttribute(key = false, size = 255, name = "sys_option_title", desc = "sysOptionTitle", type = "java.lang.String")
    private java.lang.String sysOptionTitle;
    @EntityAttribute(key = false, size = 255, name = "sys_option_value", desc = "sysOptionValue", type = "java.lang.String")
    private java.lang.String sysOptionValue;
    @EntityAttribute(key = false, size = 19, name = "sys_option_state", desc = "sysOptionState", type = "java.lang.Long")
    private java.lang.Long sysOptionState;
    private Object data;

    public SysOption() {
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
        this.sysOptionKey = sysOptionKey;
        return this;
    }

    public java.lang.String getSysOptionTitle() {
        return sysOptionTitle;
    }

    public SysOption setSysOptionTitle(java.lang.String sysOptionTitle) {
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }

    public java.lang.String getSysOptionValue() {
        return sysOptionValue;
    }

    public SysOption setSysOptionValue(java.lang.String sysOptionValue) {
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

    public SysOption setData(Object data) {
        this.data = data;
        return this;
    }
}