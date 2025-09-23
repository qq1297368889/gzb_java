package gzb.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassInfo {
    public Class aClass;
    public String name;
    public int keyIndex;
    public List<String> attributes = new ArrayList<>();
    public List<String> columnNames = new ArrayList<>();
    public List<String> notes = new ArrayList<>();


    public List<Field> fields = new ArrayList<>();
    public List<Object> values = new ArrayList<>();
    public List<Integer> sizes = new ArrayList<>();
    //public List<Class> types = new ArrayList<>();

    @Override
    public String toString() {
        return "EntityClassInfo{" +
                "aClass=" + aClass +
                ", name='" + name + '\'' +
                ", keyIndex=" + keyIndex +
                ", attributes=" + attributes +
                ", columnNames=" + columnNames +
                ", values=" + values +
                ", sizes=" + sizes +
                //", types=" + types +
                ", fields=" + fields +
                '}';
    }
}
