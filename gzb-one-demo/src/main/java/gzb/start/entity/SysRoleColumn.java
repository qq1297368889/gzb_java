package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_role_column", desc = "sysRoleColumn")
public class SysRoleColumn implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_role_column_id", desc = "sysRoleColumnId", type = "java.lang.Long")
    private Long sysRoleColumnId;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_table", desc = "sysRoleColumnTable", type = "java.lang.Long")
    private Long sysRoleColumnTable;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_name", desc = "sysRoleColumnName", type = "java.lang.String")
    private String sysRoleColumnName;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_table_show", desc = "sysRoleColumnTableShow", type = "java.lang.Long")
    private Long sysRoleColumnTableShow;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query", desc = "sysRoleColumnQuery", type = "java.lang.Long")
    private Long sysRoleColumnQuery;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_save", desc = "sysRoleColumnSave", type = "java.lang.Long")
    private Long sysRoleColumnSave;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_update", desc = "sysRoleColumnUpdate", type = "java.lang.Long")
    private Long sysRoleColumnUpdate;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_edit", desc = "sysRoleColumnEdit", type = "java.lang.Long")
    private Long sysRoleColumnEdit;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_query_def", desc = "sysRoleColumnQueryDef", type = "java.lang.String")
    private String sysRoleColumnQueryDef;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query_symbol", desc = "sysRoleColumnQuerySymbol", type = "java.lang.Long")
    private Long sysRoleColumnQuerySymbol;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query_montage", desc = "sysRoleColumnQueryMontage", type = "java.lang.Long")
    private Long sysRoleColumnQueryMontage;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_save_def", desc = "sysRoleColumnSaveDef", type = "java.lang.String")
    private String sysRoleColumnSaveDef;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_update_def", desc = "sysRoleColumnUpdateDef", type = "java.lang.String")
    private String sysRoleColumnUpdateDef;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_role", desc = "sysRoleColumnRole", type = "java.lang.Long")
    private Long sysRoleColumnRole;
    private Object data;

    public SysRoleColumn() {
    } 

    public Long getSysRoleColumnId() {
        return sysRoleColumnId;
    }

    public SysRoleColumn setSysRoleColumnId(Long sysRoleColumnId) {
        this.sysRoleColumnId = sysRoleColumnId;
        return this;
    }

    public Long getSysRoleColumnTable() {
        return sysRoleColumnTable;
    }

    public SysRoleColumn setSysRoleColumnTable(Long sysRoleColumnTable) {
        this.sysRoleColumnTable = sysRoleColumnTable;
        return this;
    }

    public String getSysRoleColumnName() {
        return sysRoleColumnName;
    }

    public SysRoleColumn setSysRoleColumnName(String sysRoleColumnName) {
        this.sysRoleColumnName = sysRoleColumnName;
        return this;
    }

    public Long getSysRoleColumnTableShow() {
        return sysRoleColumnTableShow;
    }

    public SysRoleColumn setSysRoleColumnTableShow(Long sysRoleColumnTableShow) {
        this.sysRoleColumnTableShow = sysRoleColumnTableShow;
        return this;
    }

    public Long getSysRoleColumnQuery() {
        return sysRoleColumnQuery;
    }

    public SysRoleColumn setSysRoleColumnQuery(Long sysRoleColumnQuery) {
        this.sysRoleColumnQuery = sysRoleColumnQuery;
        return this;
    }

    public Long getSysRoleColumnSave() {
        return sysRoleColumnSave;
    }

    public SysRoleColumn setSysRoleColumnSave(Long sysRoleColumnSave) {
        this.sysRoleColumnSave = sysRoleColumnSave;
        return this;
    }

    public Long getSysRoleColumnUpdate() {
        return sysRoleColumnUpdate;
    }

    public SysRoleColumn setSysRoleColumnUpdate(Long sysRoleColumnUpdate) {
        this.sysRoleColumnUpdate = sysRoleColumnUpdate;
        return this;
    }

    public Long getSysRoleColumnEdit() {
        return sysRoleColumnEdit;
    }

    public SysRoleColumn setSysRoleColumnEdit(Long sysRoleColumnEdit) {
        this.sysRoleColumnEdit = sysRoleColumnEdit;
        return this;
    }

    public String getSysRoleColumnQueryDef() {
        return sysRoleColumnQueryDef;
    }

    public SysRoleColumn setSysRoleColumnQueryDef(String sysRoleColumnQueryDef) {
        this.sysRoleColumnQueryDef = sysRoleColumnQueryDef;
        return this;
    }

    public Long getSysRoleColumnQuerySymbol() {
        return sysRoleColumnQuerySymbol;
    }

    public SysRoleColumn setSysRoleColumnQuerySymbol(Long sysRoleColumnQuerySymbol) {
        this.sysRoleColumnQuerySymbol = sysRoleColumnQuerySymbol;
        return this;
    }

    public Long getSysRoleColumnQueryMontage() {
        return sysRoleColumnQueryMontage;
    }

    public SysRoleColumn setSysRoleColumnQueryMontage(Long sysRoleColumnQueryMontage) {
        this.sysRoleColumnQueryMontage = sysRoleColumnQueryMontage;
        return this;
    }

    public String getSysRoleColumnSaveDef() {
        return sysRoleColumnSaveDef;
    }

    public SysRoleColumn setSysRoleColumnSaveDef(String sysRoleColumnSaveDef) {
        this.sysRoleColumnSaveDef = sysRoleColumnSaveDef;
        return this;
    }

    public String getSysRoleColumnUpdateDef() {
        return sysRoleColumnUpdateDef;
    }

    public SysRoleColumn setSysRoleColumnUpdateDef(String sysRoleColumnUpdateDef) {
        this.sysRoleColumnUpdateDef = sysRoleColumnUpdateDef;
        return this;
    }

    public Long getSysRoleColumnRole() {
        return sysRoleColumnRole;
    }

    public SysRoleColumn setSysRoleColumnRole(Long sysRoleColumnRole) {
        this.sysRoleColumnRole = sysRoleColumnRole;
        return this;
    }

    public SysRoleColumn setList(List<?> data) {
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

    public SysRoleColumn setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysRoleColumn putMap(String key, Object value) {
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

    public SysRoleColumn setData(Object data) {
        this.data = data;
        return this;
    }
}