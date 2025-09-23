package gzb.frame.factory;

import gzb.entity.SqlTemplate;

public interface GzbEntityInterface {
    String toJson(Object object,gzb.tools.log.Log _gzb_log) throws Exception;
    Object[] loadObject(java.util.Map<String,java.util.List<Object>> map,gzb.tools.log.Log _gzb_log) throws Exception;
    SqlTemplate toDeleteSql(Object obj) throws Exception;
    SqlTemplate toUpdateSql(Object obj) throws Exception;
    SqlTemplate toSaveSql(Object obj) throws Exception;
    SqlTemplate toSelectSql(Object obj) throws Exception;
}
