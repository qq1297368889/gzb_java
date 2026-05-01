package gzb.tools.cache.object;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ArrayBuff{
    long a1,a2,a3,a4,a5,a6,a7,s8;
    Object[] cache;
    int this_index = 0;
    public ArrayBuff(int size) {
        cache = new Object[size];
    }
    public ArrayBuff() {
        this(16);
    }

    public void expansion() {
        Object[] cache1 = new Object[cache.length + (Math.min(cache.length, 1024))];
        System.arraycopy(cache, 0, cache1, 0, cache.length);
        cache = cache1;
    }
    public boolean isEmpty() {
      return this_index>0;
    }

    public void addAll(Object[]t) {
        for (int i = 0; i < t.length; i++) {
            append(t[i]);
        }
    }
    public void addAll(List<?> t) {
        for (int i = 0; i < t.size(); i++) {
            append(t.get(i));
        }
    }
    public void add(Object t) {
        append(t);
    }

    public void append(Object t) {
        if (this_index >= cache.length) {
            expansion();
        }
        cache[this_index] = t;
        this_index++;
    }

    public Object get(int index) {
        return cache[index];
    }
    public String getString(int index) {
        return cache[index] instanceof String ? (String) cache[index] : cache[index].toString();
    }
    public Integer getInteger(int index) {
        return getDouble(index).intValue();
    }
    public Long getLong(int index) {
        return getDouble(index).longValue();
    }
    public Double getDouble(int index) {
        return cache[index] instanceof Double ? (Double) cache[index] : Double.parseDouble(getString(index));
    }
    public Boolean getBoolean(int index) {
        Object obj = cache[index];
        if (obj instanceof Boolean) {
            return (Boolean) cache[index];
        }
        String val=getString(index);
        if (val.length()==4) {
            return val.equals("true");
        }
        if (val.length()==1) {
            return val.equals("1");
        }
        return false;
    }

    //下标越界调用者保证
    public void setLength(int index) {
        for (int i = index; i < this_index; i++) {
            cache[i] = null;
        }
        this.this_index = index;
    }

    public int length() {
        return this.this_index;
    }
    public int size() {
        return this.this_index;
    }

    public int memoryLength() {
        return cache.length;
    }

    public Object[] toObjectArray() {
        int size = this_index;
        Object[] cache1 = new Object[size];
        System.arraycopy(cache, 0, cache1, 0, size);
        return cache1;
    }
    public String[] toStringArray() {
        String[]arr=new String[this_index];
        for (int i = 0; i < this_index; i++) {
            Object obj = cache[i];
            arr[i]=obj instanceof  String ? (String)obj : obj.toString();
        }
        return arr;
    }

    //需要结合 this_index 慎用
    public Object[] toObjectArrayNoCopy() {
        return cache;
    }
}
