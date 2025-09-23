package gzb.frame.factory;

import java.util.Map;

public interface GzbOneInterface {
    public Object call(String methodName, Map<String, java.util.List<Object>> requestMap, Map<String, Object> mapObject, Object[] arrayObject,boolean openTransaction)throws Exception;
}
