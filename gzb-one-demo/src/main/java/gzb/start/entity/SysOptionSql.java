package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_option_sql", desc = "sysOptionSql")
public class SysOptionSql implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_option_sql_id", desc = "sysOptionSqlId", type = "java.lang.Long")
    private Long sysOptionSqlId;
    @EntityAttribute(key = false, size = 255, name = "sys_option_sql_sql", desc = "sysOptionSqlSql", type = "java.lang.String")
    private String sysOptionSqlSql;
    @EntityAttribute(key = false, size = 255, name = "sys_option_sql_title_name", desc = "sysOptionSqlTitleName", type = "java.lang.String")
    private String sysOptionSqlTitleName;
    @EntityAttribute(key = false, size = 255, name = "sys_option_sql_val_name", desc = "sysOptionSqlValName", type = "java.lang.String")
    private String sysOptionSqlValName;
    @EntityAttribute(key = false, size = 100, name = "sys_option_sql_key", desc = "sysOptionSqlKey", type = "java.lang.String")
    private String sysOptionSqlKey;
    private Object data;

    public SysOptionSql() {
    } 

    public Long getSysOptionSqlId() {
        return sysOptionSqlId;
    }

    public SysOptionSql setSysOptionSqlId(Long sysOptionSqlId) {
        this.sysOptionSqlId = sysOptionSqlId;
        return this;
    }

    public String getSysOptionSqlSql() {
        return sysOptionSqlSql;
    }

    public SysOptionSql setSysOptionSqlSql(String sysOptionSqlSql) {
        this.sysOptionSqlSql = sysOptionSqlSql;
        return this;
    }

    public String getSysOptionSqlTitleName() {
        return sysOptionSqlTitleName;
    }

    public SysOptionSql setSysOptionSqlTitleName(String sysOptionSqlTitleName) {
        this.sysOptionSqlTitleName = sysOptionSqlTitleName;
        return this;
    }

    public String getSysOptionSqlValName() {
        return sysOptionSqlValName;
    }

    public SysOptionSql setSysOptionSqlValName(String sysOptionSqlValName) {
        this.sysOptionSqlValName = sysOptionSqlValName;
        return this;
    }

    public String getSysOptionSqlKey() {
        return sysOptionSqlKey;
    }

    public SysOptionSql setSysOptionSqlKey(String sysOptionSqlKey) {
        this.sysOptionSqlKey = sysOptionSqlKey;
        return this;
    }

    public SysOptionSql setList(List<?> data) {
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

    public SysOptionSql setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysOptionSql putMap(String key, Object value) {
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

    public SysOptionSql setData(Object data) {
        this.data = data;
        return this;
    }
}