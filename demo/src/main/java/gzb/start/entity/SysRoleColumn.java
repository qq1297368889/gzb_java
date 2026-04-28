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
    private java.lang.Long sysRoleColumnId;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_table", desc = "sysRoleColumnTable", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnTable;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_name", desc = "sysRoleColumnName", type = "java.lang.String")
    private java.lang.String sysRoleColumnName;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_table_show", desc = "sysRoleColumnTableShow", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnTableShow;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query", desc = "sysRoleColumnQuery", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnQuery;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_save", desc = "sysRoleColumnSave", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnSave;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_update", desc = "sysRoleColumnUpdate", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnUpdate;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_edit", desc = "sysRoleColumnEdit", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnEdit;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_query_def", desc = "sysRoleColumnQueryDef", type = "java.lang.String")
    private java.lang.String sysRoleColumnQueryDef;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query_symbol", desc = "sysRoleColumnQuerySymbol", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnQuerySymbol;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_query_montage", desc = "sysRoleColumnQueryMontage", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnQueryMontage;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_save_def", desc = "sysRoleColumnSaveDef", type = "java.lang.String")
    private java.lang.String sysRoleColumnSaveDef;
    @EntityAttribute(key = false, size = 100, name = "sys_role_column_update_def", desc = "sysRoleColumnUpdateDef", type = "java.lang.String")
    private java.lang.String sysRoleColumnUpdateDef;
    @EntityAttribute(key = false, size = 19, name = "sys_role_column_role", desc = "sysRoleColumnRole", type = "java.lang.Long")
    private java.lang.Long sysRoleColumnRole;
    private Object data;

    public SysRoleColumn() {
    } 

    public java.lang.Long getSysRoleColumnId() {
        return sysRoleColumnId;
    }

    public SysRoleColumn setSysRoleColumnId(java.lang.Long sysRoleColumnId) {
        this.sysRoleColumnId = sysRoleColumnId;
        return this;
    }

    public java.lang.Long getSysRoleColumnTable() {
        return sysRoleColumnTable;
    }

    public SysRoleColumn setSysRoleColumnTable(java.lang.Long sysRoleColumnTable) {
        this.sysRoleColumnTable = sysRoleColumnTable;
        return this;
    }

    public java.lang.String getSysRoleColumnName() {
        return sysRoleColumnName;
    }

    public SysRoleColumn setSysRoleColumnName(java.lang.String sysRoleColumnName) {
        this.sysRoleColumnName = sysRoleColumnName;
        return this;
    }

    public java.lang.Long getSysRoleColumnTableShow() {
        return sysRoleColumnTableShow;
    }

    public SysRoleColumn setSysRoleColumnTableShow(java.lang.Long sysRoleColumnTableShow) {
        this.sysRoleColumnTableShow = sysRoleColumnTableShow;
        return this;
    }

    public java.lang.Long getSysRoleColumnQuery() {
        return sysRoleColumnQuery;
    }

    public SysRoleColumn setSysRoleColumnQuery(java.lang.Long sysRoleColumnQuery) {
        this.sysRoleColumnQuery = sysRoleColumnQuery;
        return this;
    }

    public java.lang.Long getSysRoleColumnSave() {
        return sysRoleColumnSave;
    }

    public SysRoleColumn setSysRoleColumnSave(java.lang.Long sysRoleColumnSave) {
        this.sysRoleColumnSave = sysRoleColumnSave;
        return this;
    }

    public java.lang.Long getSysRoleColumnUpdate() {
        return sysRoleColumnUpdate;
    }

    public SysRoleColumn setSysRoleColumnUpdate(java.lang.Long sysRoleColumnUpdate) {
        this.sysRoleColumnUpdate = sysRoleColumnUpdate;
        return this;
    }

    public java.lang.Long getSysRoleColumnEdit() {
        return sysRoleColumnEdit;
    }

    public SysRoleColumn setSysRoleColumnEdit(java.lang.Long sysRoleColumnEdit) {
        this.sysRoleColumnEdit = sysRoleColumnEdit;
        return this;
    }

    public java.lang.String getSysRoleColumnQueryDef() {
        return sysRoleColumnQueryDef;
    }

    public SysRoleColumn setSysRoleColumnQueryDef(java.lang.String sysRoleColumnQueryDef) {
        this.sysRoleColumnQueryDef = sysRoleColumnQueryDef;
        return this;
    }

    public java.lang.Long getSysRoleColumnQuerySymbol() {
        return sysRoleColumnQuerySymbol;
    }

    public SysRoleColumn setSysRoleColumnQuerySymbol(java.lang.Long sysRoleColumnQuerySymbol) {
        this.sysRoleColumnQuerySymbol = sysRoleColumnQuerySymbol;
        return this;
    }

    public java.lang.Long getSysRoleColumnQueryMontage() {
        return sysRoleColumnQueryMontage;
    }

    public SysRoleColumn setSysRoleColumnQueryMontage(java.lang.Long sysRoleColumnQueryMontage) {
        this.sysRoleColumnQueryMontage = sysRoleColumnQueryMontage;
        return this;
    }

    public java.lang.String getSysRoleColumnSaveDef() {
        return sysRoleColumnSaveDef;
    }

    public SysRoleColumn setSysRoleColumnSaveDef(java.lang.String sysRoleColumnSaveDef) {
        this.sysRoleColumnSaveDef = sysRoleColumnSaveDef;
        return this;
    }

    public java.lang.String getSysRoleColumnUpdateDef() {
        return sysRoleColumnUpdateDef;
    }

    public SysRoleColumn setSysRoleColumnUpdateDef(java.lang.String sysRoleColumnUpdateDef) {
        this.sysRoleColumnUpdateDef = sysRoleColumnUpdateDef;
        return this;
    }

    public java.lang.Long getSysRoleColumnRole() {
        return sysRoleColumnRole;
    }

    public SysRoleColumn setSysRoleColumnRole(java.lang.Long sysRoleColumnRole) {
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