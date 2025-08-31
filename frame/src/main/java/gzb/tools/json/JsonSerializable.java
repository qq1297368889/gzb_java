package gzb.tools.json;

public interface JsonSerializable {
    Result toJson();
    void loadJson(String json);
}
