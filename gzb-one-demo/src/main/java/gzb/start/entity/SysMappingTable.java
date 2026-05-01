package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_mapping_table", desc = "sysMappingTable")
public class SysMappingTable implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_mapping_table_id", desc = "sysMappingTableId", type = "java.lang.Long")
    private Long sysMappingTableId;
    @EntityAttribute(key = false, size = 255, name = "sys_mapping_table_name", desc = "sysMappingTableName", type = "java.lang.String")
    private String sysMappingTableName;
    @EntityAttribute(key = false, size = 255, name = "sys_mapping_table_title", desc = "sysMappingTableTitle", type = "java.lang.String")
    private String sysMappingTableTitle;
    private Object data;

    public SysMappingTable() {
    } 

    public Long getSysMappingTableId() {
        return sysMappingTableId;
    }

    public SysMappingTable setSysMappingTableId(Long sysMappingTableId) {
        this.sysMappingTableId = sysMappingTableId;
        return this;
    }

    public String getSysMappingTableName() {
        return sysMappingTableName;
    }

    public SysMappingTable setSysMappingTableName(String sysMappingTableName) {
        this.sysMappingTableName = sysMappingTableName;
        return this;
    }

    public String getSysMappingTableTitle() {
        return sysMappingTableTitle;
    }

    public SysMappingTable setSysMappingTableTitle(String sysMappingTableTitle) {
        this.sysMappingTableTitle = sysMappingTableTitle;
        return this;
    }

    public SysMappingTable setList(List<?> data) {
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

    public SysMappingTable setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysMappingTable putMap(String key, Object value) {
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

    public SysMappingTable setData(Object data) {
        this.data = data;
        return this;
    }
}