package gzb.entity;

import java.util.Arrays;

public class SqlTemplate {
    String sql;
    Object[] objects;

    public SqlTemplate(String sql, Object[] objects) {
        this.sql = sql;
        this.objects = objects;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getObjects() {
        return objects;
    }

    public void setObjects(Object[] objects) {
        this.objects = objects;
    }

    @Override
    public String toString() {

        return "SqlTemplate{" +
                "sql='" + sql + '\'' +
                ", objects=" + Arrays.toString(objects) +
                '}';
    }
}
