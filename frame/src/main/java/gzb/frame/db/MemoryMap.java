package gzb.frame.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMap {
    //表名 = {ID=ENTITY}
    public static Map<String,Map<Object,Object>>cache = new ConcurrentHashMap<>();

}
