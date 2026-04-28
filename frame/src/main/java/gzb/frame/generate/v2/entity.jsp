<%@ page import="gzb.entity.TableInfo" %><jsp:useBean id="tableInfo" class="gzb.entity.TableInfo"/><jsp:useBean id="pkg" class="java.lang.String"/>
package <%=pkg%>;
import java.io.Serializable;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "<%=tableInfo.name%>", desc = "<%=tableInfo.nameHumpLowerCase%>")
public class <%=tableInfo.nameHumpUpperCase%> implements Serializable{
    private static final long serialVersionUID = 1000L;<%for (int i = 0; i < tableInfo.columnNames.size(); i++) {boolean isId=tableInfo.columnNames.get(i).equals(tableInfo.getId());%>
    @EntityAttribute(key = <%=isId%>, size = <%=tableInfo.columnSize.get(i)%>, name = "<%=tableInfo.columnNames.get(i)%>", desc = "<%=tableInfo.columnNamesHumpLowerCase.get(i)%>", type = "<%=tableInfo.getColumnTypes().get(i)%>")
    private <%=tableInfo.getColumnTypes().get(i)%> <%=tableInfo.getColumnNamesHumpLowerCase().get(i)%>;<%}%>
    private Object data;

    public <%=tableInfo.nameHumpUpperCase%>() {
    } <%for (int i = 0; i < tableInfo.columnNames.size(); i++) {boolean isId=tableInfo.columnNames.get(i).equals(tableInfo.getId());%>

    public <%=tableInfo.getColumnTypes().get(i)%> get<%=tableInfo.getColumnNamesHumpUpperCase().get(i)%>() {
        return <%=tableInfo.getColumnNamesLowerCase().get(i)%>;
    }

    public <%=tableInfo.nameHumpUpperCase%> set<%=tableInfo.getColumnNamesHumpUpperCase().get(i)%>(<%=tableInfo.getColumnTypes().get(i)%> <%=tableInfo.getColumnNamesLowerCase().get(i)%>) {
        this.<%=tableInfo.getColumnNamesLowerCase().get(i)%> = <%=tableInfo.getColumnNamesLowerCase().get(i)%>;
        return this;
    }<%}%>

    public <%=tableInfo.nameHumpUpperCase%> setList(List<?> data) {
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

    public <%=tableInfo.nameHumpUpperCase%> setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public <%=tableInfo.nameHumpUpperCase%> putMap(String key, Object value) {
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

    public <%=tableInfo.nameHumpUpperCase%> setData(Object data) {
        this.data = data;
        return this;
    }
}
