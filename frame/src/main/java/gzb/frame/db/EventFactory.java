package gzb.frame.db;

public interface EventFactory {

    boolean eventSelect(Object entity_obj, boolean before)throws Exception;
    boolean eventSave(Object entity_obj, boolean before)throws Exception;
    boolean eventDelete(Object entity_obj, boolean before)throws Exception;
    boolean eventUpdate(Object entity_obj, boolean before)throws Exception;
    boolean event(Object entity_obj, int type, boolean before) throws Exception;
    void register(Class<?> aClass, String code) throws Exception;

}
