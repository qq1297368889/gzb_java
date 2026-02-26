package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.FortunesDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="fortunes",desc="fortunes")
public class Fortunes implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 10,name="id",desc="",type="java.lang.Integer")
    private java.lang.Integer id;
    @EntityAttribute(key=false,size = 1024,name="message",desc="",type="java.lang.String")
    private java.lang.String message;
    private Object data;
   public Fortunes() {}

    public Fortunes(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public Fortunes(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public Fortunes(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.save(this);
    }
    public int delete(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.delete(this);
    }
    public int update(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.update(this);
    }
    public int saveAsync(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.saveAsync(this);
    }
    public int deleteAsync(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.deleteAsync(this);
    }
    public int updateAsync(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.updateAsync(this);
    }
    public List<Fortunes> query(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.query(this);
    }
    public List<Fortunes> query(FortunesDao fortunesDao,int page,int size) throws Exception {
        return fortunesDao.query(this,page,size);
    }
    public List<Fortunes> query(FortunesDao fortunesDao,String sortField,String sortType,int page,int size) throws Exception {
        return fortunesDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(FortunesDao fortunesDao, String sortField, String sortType, int page, int size) throws Exception {
        return fortunesDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(FortunesDao fortunesDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return fortunesDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public Fortunes find(FortunesDao fortunesDao) throws Exception {
        return fortunesDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(29);
       boolean app01=false;
        sb.append("{");
        if (this.id != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"id\":\"").append(id).append("\"");
        }
        if (this.message != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"message\":");
            sb.append(Tools.toJson(message));        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                if(app01){sb.append(",");}app01=true;
                sb.append("\"").append(entry.getKey()).append("\":");
                sb.append(Tools.toJson(entry.getValue()));
            }
        }else if(this.data != null){
            if(app01){sb.append(",");}app01=true;
            sb.append("\"").append(Config.get("json.entity.data","data")).append("\":");
            sb.append(Tools.toJson(this.data));
        }
       return sb.append("}").toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("id", id);
        result.set("message", message);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.id=result.getInteger("id", null);
        this.message=result.getString("message", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Integer getId() {
        return id;
    }
    public Fortunes setId(java.lang.Integer id) {
        this.id = id;
        return this;
    }
    public java.lang.String getMessage() {
        return message;
    }
    public Fortunes setMessage(java.lang.String message) {
        int size0 = Tools.textLength(message);
        if (size0 > 1024) {
            throw new gzb.exception.GzbException0("Fortunes.message最大长度为:1024,实际长度为:"+ size0 +",数据为:"+message);
        }
        this.message = message;
        return this;
    }
    public Fortunes setMessageUnsafe(java.lang.String message) {
        this.message = message;
        return this;
    }
    public Fortunes setList(List<?> data) {
        this.data = data;
        return this;
    }

    public List<?> getList() {
          if (data instanceof List) {
                return (List<?>) data;
          }
        return null;
    }
    public Map<String, Object> getMap() {
          if (data instanceof Map) {
                return (Map<String, Object>) data;
          }
        return null;
    }

    public Fortunes setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public Fortunes putMap(String key, Object value) {
        if (this.data == null) {
            // 自动初始化
            this.data = new HashMap<>();
        } else if (!(this.data instanceof Map)) {
            // 无法转换，抛出业务异常
            throw new gzb.exception.GzbException0("无法将"+this.data+" 转换为MAP");
        }
        // 安全地进行 put 操作 (可能需要抑制一下警告)
        @SuppressWarnings("unchecked")
        Map<String, Object> mapData = (Map<String, Object>) this.data;
        mapData.put(key, value);
        return this;
    }
    public Object getData() {
        return data;
    }
    public Fortunes setData(Object data) {
        this.data = data;
        return this;
    }
}
