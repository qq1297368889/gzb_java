package gzb.tools.session;

public interface Session {
    String getId();

    void put(String key, String val);

    Integer getInt(String key);

    String getString(String key);

    Object getObject(String key);

    Object delete(String key);

    int getIncr(String key);
    void delete();
}
