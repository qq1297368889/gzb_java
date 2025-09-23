package gzb.tools.cache.session;

public interface Session {
     Session sendHeader();

    String getId();

    Session put(String key, String val);

    Integer getInt(String key);

    String getString(String key);

    Object getObject(String key);

    Object delete(String key);

    int getIncr(String key);

    Session delete();
}
