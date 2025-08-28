package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.LiveAppDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="live_app",desc="liveApp")
public class LiveApp implements Serializable{
    @EntityAttribute(key=true,size = 19,name="live_app_id",desc="liveAppId")
    private java.lang.Long liveAppId;
    @EntityAttribute(key=false,size = 255,name="live_app_name",desc="liveAppName")
    private java.lang.String liveAppName;
    @EntityAttribute(key=false,size = 255,name="live_app_state",desc="liveAppState")
    private java.lang.String liveAppState;
    private List<?> list;
    public LiveApp() {}

    public LiveApp(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public LiveApp(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("liveAppId");
        if (str!=null && !str.isEmpty()) {
            setLiveAppId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("liveAppName");
        if (str!=null && !str.isEmpty()) {
            setLiveAppName(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("liveAppState");
        if (str!=null && !str.isEmpty()) {
            setLiveAppState(java.lang.String.valueOf(str));
        }
    }

    public LiveApp(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public LiveApp(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.save(this);
    }
    public int delete(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.delete(this);
    }
    public int update(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.update(this);
    }
    public int saveAsync(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.saveAsync(this);
    }
    public int deleteAsync(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.deleteAsync(this);
    }
    public int updateAsync(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.updateAsync(this);
    }
    public List<LiveApp> query(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.query(this);
    }
    public List<LiveApp> query(LiveAppDao liveAppDao,int page,int size) throws Exception {
        return liveAppDao.query(this,page,size);
    }
    public List<LiveApp> query(LiveAppDao liveAppDao,String sortField,String sortType,int page,int size) throws Exception {
        return liveAppDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(LiveAppDao liveAppDao, String sortField, String sortType, int page, int size) throws Exception {
        return liveAppDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(LiveAppDao liveAppDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return liveAppDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public LiveApp find(LiveAppDao liveAppDao) throws Exception {
        return liveAppDao.find(this);
    }
    public SqlTemplate toSelectSql() {
        return SqlTools.toSelectSql(this,"act_code_id", "asc", 0, false);
    }

    //查询语句 可选项 排序
    public SqlTemplate toSelectSql(String sortField, String sortType, int size, boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave(java.lang.Long actCodeId) {
        return SqlTools.toSave(this,actCodeId);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public JSON toJson() {
        JSON json = new JSON();
        json.put("liveAppId", getLiveAppId());
        json.put("liveAppName", getLiveAppName());
        json.put("liveAppState", getLiveAppState());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getLiveAppId() {
        return liveAppId;
    }
    public LiveApp setLiveAppId(java.lang.Long liveAppId) {
        int size0 = Tools.textLength(liveAppId);
        if (size0 > 19) {
            throw new RuntimeException("LiveApp.liveAppId最大长度为:19,实际长度为:"+ size0 +",数据为:"+liveAppId);
        }
        this.liveAppId = liveAppId;
        return this;
    }
    public LiveApp setLiveAppIdUnsafe(java.lang.Long liveAppId) {
        this.liveAppId = liveAppId;
        return this;
    }
    public java.lang.String getLiveAppName() {
        return liveAppName;
    }
    public LiveApp setLiveAppName(java.lang.String liveAppName) {
        int size0 = Tools.textLength(liveAppName);
        if (size0 > 255) {
            throw new RuntimeException("LiveApp.liveAppName最大长度为:255,实际长度为:"+ size0 +",数据为:"+liveAppName);
        }
        this.liveAppName = liveAppName;
        return this;
    }
    public LiveApp setLiveAppNameUnsafe(java.lang.String liveAppName) {
        this.liveAppName = liveAppName;
        return this;
    }
    public java.lang.String getLiveAppState() {
        return liveAppState;
    }
    public LiveApp setLiveAppState(java.lang.String liveAppState) {
        int size0 = Tools.textLength(liveAppState);
        if (size0 > 255) {
            throw new RuntimeException("LiveApp.liveAppState最大长度为:255,实际长度为:"+ size0 +",数据为:"+liveAppState);
        }
        this.liveAppState = liveAppState;
        return this;
    }
    public LiveApp setLiveAppStateUnsafe(java.lang.String liveAppState) {
        this.liveAppState = liveAppState;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
