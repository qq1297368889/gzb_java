package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_role_table", desc = "sysRoleTable")
public class SysRoleTable implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_role_table_id", desc = "sysRoleTableId", type = "java.lang.Long")
    private Long sysRoleTableId;
    @EntityAttribute(key = false, size = 255, name = "sys_role_table_name", desc = "sysRoleTableName", type = "java.lang.String")
    private String sysRoleTableName;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_save", desc = "sysRoleTableSave", type = "java.lang.Long")
    private Long sysRoleTableSave;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_query", desc = "sysRoleTableQuery", type = "java.lang.Long")
    private Long sysRoleTableQuery;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_delete", desc = "sysRoleTableDelete", type = "java.lang.Long")
    private Long sysRoleTableDelete;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_update", desc = "sysRoleTableUpdate", type = "java.lang.Long")
    private Long sysRoleTableUpdate;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_delete_sgin", desc = "sysRoleTableDeleteSgin", type = "java.lang.Long")
    private Long sysRoleTableDeleteSgin;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_role", desc = "sysRoleTableRole", type = "java.lang.Long")
    private Long sysRoleTableRole;
    @EntityAttribute(key = false, size = 19, name = "sys_role_table_width", desc = "sysRoleTableWidth", type = "java.lang.Long")
    private Long sysRoleTableWidth;
    private Object data;

    public SysRoleTable() {
    } 

    public Long getSysRoleTableId() {
        return sysRoleTableId;
    }

    public SysRoleTable setSysRoleTableId(Long sysRoleTableId) {
        this.sysRoleTableId = sysRoleTableId;
        return this;
    }

    public String getSysRoleTableName() {
        return sysRoleTableName;
    }

    public SysRoleTable setSysRoleTableName(String sysRoleTableName) {
        this.sysRoleTableName = sysRoleTableName;
        return this;
    }

    public Long getSysRoleTableSave() {
        return sysRoleTableSave;
    }

    public SysRoleTable setSysRoleTableSave(Long sysRoleTableSave) {
        this.sysRoleTableSave = sysRoleTableSave;
        return this;
    }

    public Long getSysRoleTableQuery() {
        return sysRoleTableQuery;
    }

    public SysRoleTable setSysRoleTableQuery(Long sysRoleTableQuery) {
        this.sysRoleTableQuery = sysRoleTableQuery;
        return this;
    }

    public Long getSysRoleTableDelete() {
        return sysRoleTableDelete;
    }

    public SysRoleTable setSysRoleTableDelete(Long sysRoleTableDelete) {
        this.sysRoleTableDelete = sysRoleTableDelete;
        return this;
    }

    public Long getSysRoleTableUpdate() {
        return sysRoleTableUpdate;
    }

    public SysRoleTable setSysRoleTableUpdate(Long sysRoleTableUpdate) {
        this.sysRoleTableUpdate = sysRoleTableUpdate;
        return this;
    }

    public Long getSysRoleTableDeleteSgin() {
        return sysRoleTableDeleteSgin;
    }

    public SysRoleTable setSysRoleTableDeleteSgin(Long sysRoleTableDeleteSgin) {
        this.sysRoleTableDeleteSgin = sysRoleTableDeleteSgin;
        return this;
    }

    public Long getSysRoleTableRole() {
        return sysRoleTableRole;
    }

    public SysRoleTable setSysRoleTableRole(Long sysRoleTableRole) {
        this.sysRoleTableRole = sysRoleTableRole;
        return this;
    }

    public Long getSysRoleTableWidth() {
        return sysRoleTableWidth;
    }

    public SysRoleTable setSysRoleTableWidth(Long sysRoleTableWidth) {
        this.sysRoleTableWidth = sysRoleTableWidth;
        return this;
    }

    public SysRoleTable setList(List<?> data) {
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

    public SysRoleTable setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleTable putMap(String key, Object value) {
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

    public SysRoleTable setData(Object data) {
        this.data = data;
        return this;
    }
}