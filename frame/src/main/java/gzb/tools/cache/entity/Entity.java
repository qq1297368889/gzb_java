package gzb.tools.cache.entity;

import java.io.Serializable;

public  class Entity implements Serializable {
    public long id;
    public Object data;
    public short retries = 0;
    public long time = 0;
}