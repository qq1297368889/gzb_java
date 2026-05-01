package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_role", desc = "sysRole")
public class SysRole implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_role_id", desc = "sysRoleId", type = "java.lang.Long")
    private Long sysRoleId;
    @EntityAttribute(key = false, size = 100, name = "sys_role_name", desc = "sysRoleName", type = "java.lang.String")
    private String sysRoleName;
    @EntityAttribute(key = false, size = 19, name = "sys_role_state", desc = "sysRoleState", type = "java.lang.Long")
    private Long sysRoleState;
    @EntityAttribute(key = false, size = 19, name = "sys_role_time", desc = "sysRoleTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysRoleTime;
    private Object data;

    public SysRole() {
    } 

    public Long getSysRoleId() {
        return sysRoleId;
    }

    public SysRole setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
        return this;
    }

    public String getSysRoleName() {
        return sysRoleName;
    }

    public SysRole setSysRoleName(String sysRoleName) {
        this.sysRoleName = sysRoleName;
        return this;
    }

    public Long getSysRoleState() {
        return sysRoleState;
    }

    public SysRole setSysRoleState(Long sysRoleState) {
        this.sysRoleState = sysRoleState;
        return this;
    }

    public java.time.LocalDateTime getSysRoleTime() {
        return sysRoleTime;
    }

    public SysRole setSysRoleTime(java.time.LocalDateTime sysRoleTime) {
        this.sysRoleTime = sysRoleTime;
        return this;
    }

    public SysRole setList(List<?> data) {
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

    public SysRole setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRole putMap(String key, Object value) {
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

    public SysRole setData(Object data) {
        this.data = data;
        return this;
    }
}