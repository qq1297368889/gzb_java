package gzb.frame.server.http.entity;

import gzb.frame.annotation.Resource;

import java.lang.reflect.Field;

public class FieldInfo {
    Field field;
    Resource resource;
    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "field=" + field +
                ", resource=" + resource +
                '}';
    }
}
