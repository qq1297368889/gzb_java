package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_users_login_log", desc = "sysUsersLoginLog")
public class SysUsersLoginLog implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_users_login_log_id", desc = "sysUsersLoginLogId", type = "java.lang.Long")
    private Long sysUsersLoginLogId;
    @EntityAttribute(key = false, size = 100, name = "sys_users_login_log_ip", desc = "sysUsersLoginLogIp", type = "java.lang.String")
    private String sysUsersLoginLogIp;
    @EntityAttribute(key = false, size = 100, name = "sys_users_login_log_desc", desc = "sysUsersLoginLogDesc", type = "java.lang.String")
    private String sysUsersLoginLogDesc;
    @EntityAttribute(key = false, size = 19, name = "sys_users_login_log_time", desc = "sysUsersLoginLogTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersLoginLogTime;
    @EntityAttribute(key = false, size = 19, name = "sys_users_login_log_uid", desc = "sysUsersLoginLogUid", type = "java.lang.Long")
    private Long sysUsersLoginLogUid;
    @EntityAttribute(key = false, size = 100, name = "sys_users_login_log_token", desc = "sysUsersLoginLogToken", type = "java.lang.String")
    private String sysUsersLoginLogToken;
    @EntityAttribute(key = false, size = 100, name = "sys_users_login_log_mac", desc = "sysUsersLoginLogMac", type = "java.lang.String")
    private String sysUsersLoginLogMac;
    private Object data;

    public SysUsersLoginLog() {
    } 

    public Long getSysUsersLoginLogId() {
        return sysUsersLoginLogId;
    }

    public SysUsersLoginLog setSysUsersLoginLogId(Long sysUsersLoginLogId) {
        this.sysUsersLoginLogId = sysUsersLoginLogId;
        return this;
    }

    public String getSysUsersLoginLogIp() {
        return sysUsersLoginLogIp;
    }

    public SysUsersLoginLog setSysUsersLoginLogIp(String sysUsersLoginLogIp) {
        this.sysUsersLoginLogIp = sysUsersLoginLogIp;
        return this;
    }

    public String getSysUsersLoginLogDesc() {
        return sysUsersLoginLogDesc;
    }

    public SysUsersLoginLog setSysUsersLoginLogDesc(String sysUsersLoginLogDesc) {
        this.sysUsersLoginLogDesc = sysUsersLoginLogDesc;
        return this;
    }

    public java.time.LocalDateTime getSysUsersLoginLogTime() {
        return sysUsersLoginLogTime;
    }

    public SysUsersLoginLog setSysUsersLoginLogTime(java.time.LocalDateTime sysUsersLoginLogTime) {
        this.sysUsersLoginLogTime = sysUsersLoginLogTime;
        return this;
    }

    public Long getSysUsersLoginLogUid() {
        return sysUsersLoginLogUid;
    }

    public SysUsersLoginLog setSysUsersLoginLogUid(Long sysUsersLoginLogUid) {
        this.sysUsersLoginLogUid = sysUsersLoginLogUid;
        return this;
    }

    public String getSysUsersLoginLogToken() {
        return sysUsersLoginLogToken;
    }

    public SysUsersLoginLog setSysUsersLoginLogToken(String sysUsersLoginLogToken) {
        this.sysUsersLoginLogToken = sysUsersLoginLogToken;
        return this;
    }

    public String getSysUsersLoginLogMac() {
        return sysUsersLoginLogMac;
    }

    public SysUsersLoginLog setSysUsersLoginLogMac(String sysUsersLoginLogMac) {
        this.sysUsersLoginLogMac = sysUsersLoginLogMac;
        return this;
    }

    public SysUsersLoginLog setList(List<?> data) {
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

    public SysUsersLoginLog setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysUsersLoginLog putMap(String key, Object value) {
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

    public SysUsersLoginLog setData(Object data) {
        this.data = data;
        return this;
    }
}