package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_permission", desc = "sysPermission")
public class SysPermission implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_permission_id", desc = "sysPermissionId", type = "java.lang.Long")
    private Long sysPermissionId;
    @EntityAttribute(key = false, size = 255, name = "sys_permission_name", desc = "sysPermissionName", type = "java.lang.String")
    private String sysPermissionName;
    @EntityAttribute(key = false, size = 255, name = "sys_permission_data", desc = "sysPermissionData", type = "java.lang.String")
    private String sysPermissionData;
    @EntityAttribute(key = false, size = 19, name = "sys_permission_type", desc = "sysPermissionType", type = "java.lang.Long")
    private Long sysPermissionType;
    @EntityAttribute(key = false, size = 255, name = "sys_permission_desc", desc = "sysPermissionDesc", type = "java.lang.String")
    private String sysPermissionDesc;
    @EntityAttribute(key = false, size = 19, name = "sys_permission_sup", desc = "sysPermissionSup", type = "java.lang.Long")
    private Long sysPermissionSup;
    @EntityAttribute(key = false, size = 19, name = "sys_permission_sort", desc = "sysPermissionSort", type = "java.lang.Long")
    private Long sysPermissionSort;
    private Object data;

    public SysPermission() {
    } 

    public Long getSysPermissionId() {
        return sysPermissionId;
    }

    public SysPermission setSysPermissionId(Long sysPermissionId) {
        this.sysPermissionId = sysPermissionId;
        return this;
    }

    public String getSysPermissionName() {
        return sysPermissionName;
    }

    public SysPermission setSysPermissionName(String sysPermissionName) {
        this.sysPermissionName = sysPermissionName;
        return this;
    }

    public String getSysPermissionData() {
        return sysPermissionData;
    }

    public SysPermission setSysPermissionData(String sysPermissionData) {
        this.sysPermissionData = sysPermissionData;
        return this;
    }

    public Long getSysPermissionType() {
        return sysPermissionType;
    }

    public SysPermission setSysPermissionType(Long sysPermissionType) {
        this.sysPermissionType = sysPermissionType;
        return this;
    }

    public String getSysPermissionDesc() {
        return sysPermissionDesc;
    }

    public SysPermission setSysPermissionDesc(String sysPermissionDesc) {
        this.sysPermissionDesc = sysPermissionDesc;
        return this;
    }

    public Long getSysPermissionSup() {
        return sysPermissionSup;
    }

    public SysPermission setSysPermissionSup(Long sysPermissionSup) {
        this.sysPermissionSup = sysPermissionSup;
        return this;
    }

    public Long getSysPermissionSort() {
        return sysPermissionSort;
    }

    public SysPermission setSysPermissionSort(Long sysPermissionSort) {
        this.sysPermissionSort = sysPermissionSort;
        return this;
    }

    public SysPermission setList(List<?> data) {
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

    public SysPermission setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysPermission putMap(String key, Object value) {
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

    public SysPermission setData(Object data) {
        this.data = data;
        return this;
    }
}