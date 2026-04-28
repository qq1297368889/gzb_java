package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_log", desc = "sysLog")
public class SysLog implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_log_id", desc = "sysLogId", type = "java.lang.Long")
    private java.lang.Long sysLogId;
    @EntityAttribute(key = false, size = 19, name = "sys_log_time", desc = "sysLogTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysLogTime;
    @EntityAttribute(key = false, size = 19, name = "sys_log_ms", desc = "sysLogMs", type = "java.lang.Long")
    private java.lang.Long sysLogMs;
    @EntityAttribute(key = false, size = 536870911, name = "sys_log_sql", desc = "sysLogSql", type = "java.lang.String")
    private java.lang.String sysLogSql;
    private Object data;

    public SysLog() {
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

    public SysLog setData(Object data) {
        this.data = data;
        return this;
    }
}