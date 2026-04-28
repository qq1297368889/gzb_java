package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_group_permission", desc = "sysGroupPermission")
public class SysGroupPermission implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_group_permission_id", desc = "sysGroupPermissionId", type = "java.lang.Long")
    private java.lang.Long sysGroupPermissionId;
    @EntityAttribute(key = false, size = 19, name = "sys_group_permission_pid", desc = "sysGroupPermissionPid", type = "java.lang.Long")
    private java.lang.Long sysGroupPermissionPid;
    @EntityAttribute(key = false, size = 19, name = "sys_group_permission_gid", desc = "sysGroupPermissionGid", type = "java.lang.Long")
    private java.lang.Long sysGroupPermissionGid;
    @EntityAttribute(key = false, size = 19, name = "sys_group_permission_time", desc = "sysGroupPermissionTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysGroupPermissionTime;
    private Object data;

    public SysGroupPermission() {
    } 

    public java.lang.Long getSysGroupPermissionId() {
        return sysGroupPermissionId;
    }

    public SysGroupPermission setSysGroupPermissionId(java.lang.Long sysGroupPermissionId) {
        this.sysGroupPermissionId = sysGroupPermissionId;
        return this;
    }

    public java.lang.Long getSysGroupPermissionPid() {
        return sysGroupPermissionPid;
    }

    public SysGroupPermission setSysGroupPermissionPid(java.lang.Long sysGroupPermissionPid) {
        this.sysGroupPermissionPid = sysGroupPermissionPid;
        return this;
    }

    public java.lang.Long getSysGroupPermissionGid() {
        return sysGroupPermissionGid;
    }

    public SysGroupPermission setSysGroupPermissionGid(java.lang.Long sysGroupPermissionGid) {
        this.sysGroupPermissionGid = sysGroupPermissionGid;
        return this;
    }

    public java.time.LocalDateTime getSysGroupPermissionTime() {
        return sysGroupPermissionTime;
    }

    public SysGroupPermission setSysGroupPermissionTime(java.time.LocalDateTime sysGroupPermissionTime) {
        this.sysGroupPermissionTime = sysGroupPermissionTime;
        return this;
    }

    public SysGroupPermission setList(List<?> data) {
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

    public SysGroupPermission setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroupPermission putMap(String key, Object value) {
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

    public SysGroupPermission setData(Object data) {
        this.data = data;
        return this;
    }
}