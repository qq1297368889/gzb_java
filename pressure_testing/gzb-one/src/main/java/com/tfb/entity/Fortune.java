package com.tfb.entity;
import gzb.tools.*;
import com.tfb.dao.FortuneDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="fortune",desc="fortune")
public class Fortune implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 10,name="id",desc="",type="java.lang.Integer")
    private java.lang.Integer id;
    @EntityAttribute(key=false,size = 2048,name="message",desc="",type="java.lang.String")
    private java.lang.String message;
    private Object data;
   public Fortune() {}

    public Fortune(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public Fortune(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public Fortune(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.save(this);
    }
    public int delete(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.delete(this);
    }
    public int update(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.update(this);
    }
    public int saveAsync(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.saveAsync(this);
    }
    public int deleteAsync(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.deleteAsync(this);
    }
    public int updateAsync(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.updateAsync(this);
    }
    public List<Fortune> query(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.query(this);
    }
    public List<Fortune> query(FortuneDao fortuneDao,int page,int size) throws Exception {
        return fortuneDao.query(this,page,size);
    }
    public List<Fortune> query(FortuneDao fortuneDao,String sortField,String sortType,int page,int size) throws Exception {
        return fortuneDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(FortuneDao fortuneDao, String sortField, String sortType, int page, int size) throws Exception {
        return fortuneDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(FortuneDao fortuneDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return fortuneDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public Fortune find(FortuneDao fortuneDao) throws Exception {
        return fortuneDao.find(this);
    }
    @Override
    public String toString() {
        gzb.tools.thread.GzbThreadLocal.Entity entity0 = gzb.tools.thread.GzbThreadLocal.context.get();
        int index0=entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
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
            sb.append("\"").append(Config.entityDataListName).append("\":");
            sb.append(Tools.toJson(this.data));
        }
       return sb.append("}").toString();
            }finally {
                entity0.stringBuilderCacheEntity.close(index0);
            }    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("id", id);
        result.set("message", message);
        result.set(Config.entityDataListName, data);
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
        Object obj = result.get(Config.entityDataListName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Integer getId() {
        return id;
    }
    public Fortune setId(java.lang.Integer id) {
        this.id = id;
        return this;
    }
    public java.lang.String getMessage() {
        return message;
    }
    public Fortune setMessage(java.lang.String message) {
        int size0 = Tools.textLength(message);
        if (size0 > 2048) {
            throw new gzb.exception.GzbException0("Fortune.message最大长度为:2048,实际长度为:"+ size0 +",数据为:"+message);
        }
        this.message = message;
        return this;
    }
    public Fortune setMessageUnsafe(java.lang.String message) {
        this.message = message;
        return this;
    }
    public Fortune setList(List<?> data) {
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

    public Fortune setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public Fortune putMap(String key, Object value) {
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
    public Fortune setData(Object data) {
        this.data = data;
        return this;
    }
}
