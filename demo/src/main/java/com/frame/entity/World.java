package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.WorldDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="world",desc="world")
public class World implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 0,name="id",desc="",type="int")
    private java.lang.Integer worldId;
    @EntityAttribute(key=false,size = 0,name="randomNumber",desc="",type="int")
    private java.lang.Integer worldRandomnumber;
    private Object data;
   public World() {}

    public World(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public World(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public World(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(WorldDao worldDao) throws Exception {
        return worldDao.save(this);
    }
    public int delete(WorldDao worldDao) throws Exception {
        return worldDao.delete(this);
    }
    public int update(WorldDao worldDao) throws Exception {
        return worldDao.update(this);
    }
    public List<World> query(WorldDao worldDao) throws Exception {
        return worldDao.query(this);
    }
    public List<World> query(WorldDao worldDao,int page,int size) throws Exception {
        return worldDao.query(this,page,size);
    }
    public List<World> query(WorldDao worldDao,String sortField,String sortType,int page,int size) throws Exception {
        return worldDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(WorldDao worldDao, String sortField, String sortType, int page, int size) throws Exception {
        return worldDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(WorldDao worldDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return worldDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public World find(WorldDao worldDao) throws Exception {
        return worldDao.find(this);
    }
    @Override
    public String toString() {
        gzb.tools.thread.GzbThreadLocal.Entity entity0 = gzb.tools.thread.GzbThreadLocal.context.get();
        int index0=entity0.stringBuilderCacheEntity.open();
            try {
                StringBuilder sb = entity0.stringBuilderCacheEntity.get(index0);
       boolean app01=false;
        sb.append("{");
        if (this.worldId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"worldId\":\"").append(worldId).append("\"");
        }
        if (this.worldRandomnumber != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"worldRandomnumber\":\"").append(worldRandomnumber).append("\"");
        }
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
        result.set("worldId", worldId);
        result.set("worldRandomnumber", worldRandomnumber);
        result.set(Config.entityDataListName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.worldId=result.getInteger("worldId", null);
        this.worldRandomnumber=result.getInteger("worldRandomnumber", null);
        Object obj = result.get(Config.entityDataListName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Integer getWorldId() {
        return worldId;
    }
    public World setWorldId(java.lang.Integer worldId) {
        this.worldId = worldId;
        return this;
    }
    public java.lang.Integer getWorldRandomnumber() {
        return worldRandomnumber;
    }
    public World setWorldRandomnumber(java.lang.Integer worldRandomnumber) {
        this.worldRandomnumber = worldRandomnumber;
        return this;
    }
    public World setList(List<?> data) {
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

    public World setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public World putMap(String key, Object value) {
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
    public World setData(Object data) {
        this.data = data;
        return this;
    }
}
