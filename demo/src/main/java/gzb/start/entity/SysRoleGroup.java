package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_role_group", desc = "sysRoleGroup")
public class SysRoleGroup implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_role_group_id", desc = "sysRoleGroupId", type = "java.lang.Long")
    private java.lang.Long sysRoleGroupId;
    @EntityAttribute(key = false, size = 19, name = "sys_role_group_rid", desc = "sysRoleGroupRid", type = "java.lang.Long")
    private java.lang.Long sysRoleGroupRid;
    @EntityAttribute(key = false, size = 19, name = "sys_role_group_gid", desc = "sysRoleGroupGid", type = "java.lang.Long")
    private java.lang.Long sysRoleGroupGid;
    private Object data;

    public SysRoleGroup() {
    } 

    public java.lang.Long getSysRoleGroupId() {
        return sysRoleGroupId;
    }

    public SysRoleGroup setSysRoleGroupId(java.lang.Long sysRoleGroupId) {
        this.sysRoleGroupId = sysRoleGroupId;
        return this;
    }

    public java.lang.Long getSysRoleGroupRid() {
        return sysRoleGroupRid;
    }

    public SysRoleGroup setSysRoleGroupRid(java.lang.Long sysRoleGroupRid) {
        this.sysRoleGroupRid = sysRoleGroupRid;
        return this;
    }

    public java.lang.Long getSysRoleGroupGid() {
        return sysRoleGroupGid;
    }

    public SysRoleGroup setSysRoleGroupGid(java.lang.Long sysRoleGroupGid) {
        this.sysRoleGroupGid = sysRoleGroupGid;
        return this;
    }

    public SysRoleGroup setList(List<?> data) {
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

    public SysRoleGroup setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleGroup putMap(String key, Object value) {
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

    public SysRoleGroup setData(Object data) {
        this.data = data;
        return this;
    }
}