package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_group", desc = "sysGroup")
public class SysGroup implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_group_id", desc = "sysGroupId", type = "java.lang.Long")
    private java.lang.Long sysGroupId;
    @EntityAttribute(key = false, size = 255, name = "sys_group_name", desc = "sysGroupName", type = "java.lang.String")
    private java.lang.String sysGroupName;
    @EntityAttribute(key = false, size = 19, name = "sys_group_type", desc = "sysGroupType", type = "java.lang.Long")
    private java.lang.Long sysGroupType;
    @EntityAttribute(key = false, size = 255, name = "sys_group_desc", desc = "sysGroupDesc", type = "java.lang.String")
    private java.lang.String sysGroupDesc;
    @EntityAttribute(key = false, size = 19, name = "sys_group_sup", desc = "sysGroupSup", type = "java.lang.Long")
    private java.lang.Long sysGroupSup;
    private Object data;

    public SysGroup() {
    } 

    public java.lang.Long getSysGroupId() {
        return sysGroupId;
    }

    public SysGroup setSysGroupId(java.lang.Long sysGroupId) {
        this.sysGroupId = sysGroupId;
        return this;
    }

    public java.lang.String getSysGroupName() {
        return sysGroupName;
    }

    public SysGroup setSysGroupName(java.lang.String sysGroupName) {
        this.sysGroupName = sysGroupName;
        return this;
    }

    public java.lang.Long getSysGroupType() {
        return sysGroupType;
    }

    public SysGroup setSysGroupType(java.lang.Long sysGroupType) {
        this.sysGroupType = sysGroupType;
        return this;
    }

    public java.lang.String getSysGroupDesc() {
        return sysGroupDesc;
    }

    public SysGroup setSysGroupDesc(java.lang.String sysGroupDesc) {
        this.sysGroupDesc = sysGroupDesc;
        return this;
    }

    public java.lang.Long getSysGroupSup() {
        return sysGroupSup;
    }

    public SysGroup setSysGroupSup(java.lang.Long sysGroupSup) {
        this.sysGroupSup = sysGroupSup;
        return this;
    }

    public SysGroup setList(List<?> data) {
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

    public SysGroup setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysGroup putMap(String key, Object value) {
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

    public SysGroup setData(Object data) {
        this.data = data;
        return this;
    }
}