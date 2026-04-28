package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "fortunes", desc = "fortunes")
public class Fortunes implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 10, name = "id", desc = "id", type = "java.lang.Integer")
    private java.lang.Integer id;
    @EntityAttribute(key = false, size = 1024, name = "message", desc = "message", type = "java.lang.String")
    private java.lang.String message;
    private Object data;

    public Fortunes() {
    } 

    public java.lang.Integer getId() {
        return id;
    }

    public Fortunes setId(java.lang.Integer id) {
        this.id = id;
        return this;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public Fortunes setMessage(java.lang.String message) {
        this.message = message;
        return this;
    }

    public Fortunes setList(List<?> data) {
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

    public Fortunes setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public Fortunes putMap(String key, Object value) {
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

    public Fortunes setData(Object data) {
        this.data = data;
        return this;
    }
}