package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_mapping_column", desc = "sysMappingColumn")
public class SysMappingColumn implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_mapping_column_id", desc = "sysMappingColumnId", type = "java.lang.Long")
    private java.lang.Long sysMappingColumnId;
    @EntityAttribute(key = false, size = 100, name = "sys_mapping_column_name", desc = "sysMappingColumnName", type = "java.lang.String")
    private java.lang.String sysMappingColumnName;
    @EntityAttribute(key = false, size = 100, name = "sys_mapping_column_title", desc = "sysMappingColumnTitle", type = "java.lang.String")
    private java.lang.String sysMappingColumnTitle;
    @EntityAttribute(key = false, size = 19, name = "sys_mapping_column_width", desc = "sysMappingColumnWidth", type = "java.lang.Long")
    private java.lang.Long sysMappingColumnWidth;
    @EntityAttribute(key = false, size = 255, name = "sys_mapping_column_file", desc = "sysMappingColumnFile", type = "java.lang.String")
    private java.lang.String sysMappingColumnFile;
    @EntityAttribute(key = false, size = 255, name = "sys_mapping_column_image", desc = "sysMappingColumnImage", type = "java.lang.String")
    private java.lang.String sysMappingColumnImage;
    @EntityAttribute(key = false, size = 255, name = "sys_mapping_column_date", desc = "sysMappingColumnDate", type = "java.lang.String")
    private java.lang.String sysMappingColumnDate;
    @EntityAttribute(key = false, size = 19, name = "sys_mapping_column_number", desc = "sysMappingColumnNumber", type = "java.lang.Long")
    private java.lang.Long sysMappingColumnNumber;
    @EntityAttribute(key = false, size = 19, name = "sys_mapping_column_text", desc = "sysMappingColumnText", type = "java.lang.Long")
    private java.lang.Long sysMappingColumnText;
    @EntityAttribute(key = false, size = 19, name = "sys_mapping_column_table", desc = "sysMappingColumnTable", type = "java.lang.Long")
    private java.lang.Long sysMappingColumnTable;
    @EntityAttribute(key = false, size = 100, name = "sys_mapping_column_request", desc = "sysMappingColumnRequest", type = "java.lang.String")
    private java.lang.String sysMappingColumnRequest;
    @EntityAttribute(key = false, size = 100, name = "sys_mapping_column_option", desc = "sysMappingColumnOption", type = "java.lang.String")
    private java.lang.String sysMappingColumnOption;
    @EntityAttribute(key = false, size = 100, name = "sys_mapping_column_sql", desc = "sysMappingColumnSql", type = "java.lang.String")
    private java.lang.String sysMappingColumnSql;
    private Object data;

    public SysMappingColumn() {
    } 

    public java.lang.Long getSysMappingColumnId() {
        return sysMappingColumnId;
    }

    public SysMappingColumn setSysMappingColumnId(java.lang.Long sysMappingColumnId) {
        this.sysMappingColumnId = sysMappingColumnId;
        return this;
    }

    public java.lang.String getSysMappingColumnName() {
        return sysMappingColumnName;
    }

    public SysMappingColumn setSysMappingColumnName(java.lang.String sysMappingColumnName) {
        this.sysMappingColumnName = sysMappingColumnName;
        return this;
    }

    public java.lang.String getSysMappingColumnTitle() {
        return sysMappingColumnTitle;
    }

    public SysMappingColumn setSysMappingColumnTitle(java.lang.String sysMappingColumnTitle) {
        this.sysMappingColumnTitle = sysMappingColumnTitle;
        return this;
    }

    public java.lang.Long getSysMappingColumnWidth() {
        return sysMappingColumnWidth;
    }

    public SysMappingColumn setSysMappingColumnWidth(java.lang.Long sysMappingColumnWidth) {
        this.sysMappingColumnWidth = sysMappingColumnWidth;
        return this;
    }

    public java.lang.String getSysMappingColumnFile() {
        return sysMappingColumnFile;
    }

    public SysMappingColumn setSysMappingColumnFile(java.lang.String sysMappingColumnFile) {
        this.sysMappingColumnFile = sysMappingColumnFile;
        return this;
    }

    public java.lang.String getSysMappingColumnImage() {
        return sysMappingColumnImage;
    }

    public SysMappingColumn setSysMappingColumnImage(java.lang.String sysMappingColumnImage) {
        this.sysMappingColumnImage = sysMappingColumnImage;
        return this;
    }

    public java.lang.String getSysMappingColumnDate() {
        return sysMappingColumnDate;
    }

    public SysMappingColumn setSysMappingColumnDate(java.lang.String sysMappingColumnDate) {
        this.sysMappingColumnDate = sysMappingColumnDate;
        return this;
    }

    public java.lang.Long getSysMappingColumnNumber() {
        return sysMappingColumnNumber;
    }

    public SysMappingColumn setSysMappingColumnNumber(java.lang.Long sysMappingColumnNumber) {
        this.sysMappingColumnNumber = sysMappingColumnNumber;
        return this;
    }

    public java.lang.Long getSysMappingColumnText() {
        return sysMappingColumnText;
    }

    public SysMappingColumn setSysMappingColumnText(java.lang.Long sysMappingColumnText) {
        this.sysMappingColumnText = sysMappingColumnText;
        return this;
    }

    public java.lang.Long getSysMappingColumnTable() {
        return sysMappingColumnTable;
    }

    public SysMappingColumn setSysMappingColumnTable(java.lang.Long sysMappingColumnTable) {
        this.sysMappingColumnTable = sysMappingColumnTable;
        return this;
    }

    public java.lang.String getSysMappingColumnRequest() {
        return sysMappingColumnRequest;
    }

    public SysMappingColumn setSysMappingColumnRequest(java.lang.String sysMappingColumnRequest) {
        this.sysMappingColumnRequest = sysMappingColumnRequest;
        return this;
    }

    public java.lang.String getSysMappingColumnOption() {
        return sysMappingColumnOption;
    }

    public SysMappingColumn setSysMappingColumnOption(java.lang.String sysMappingColumnOption) {
        this.sysMappingColumnOption = sysMappingColumnOption;
        return this;
    }

    public java.lang.String getSysMappingColumnSql() {
        return sysMappingColumnSql;
    }

    public SysMappingColumn setSysMappingColumnSql(java.lang.String sysMappingColumnSql) {
        this.sysMappingColumnSql = sysMappingColumnSql;
        return this;
    }

    public SysMappingColumn setList(List<?> data) {
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

    public SysMappingColumn setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysMappingColumn putMap(String key, Object value) {
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

    public SysMappingColumn setData(Object data) {
        this.data = data;
        return this;
    }
}