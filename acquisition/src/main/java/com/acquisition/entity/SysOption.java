package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.SysOptionDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_option",desc="sysOption")
public class SysOption implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_option_id",desc="sysOptionId")
    private java.lang.Long sysOptionId;
    @EntityAttribute(key=false,size = 255,name="sys_option_key",desc="sysOptionKey")
    private java.lang.String sysOptionKey;
    @EntityAttribute(key=false,size = 255,name="sys_option_title",desc="sysOptionTitle")
    private java.lang.String sysOptionTitle;
    @EntityAttribute(key=false,size = 255,name="sys_option_value",desc="sysOptionValue")
    private java.lang.String sysOptionValue;
    @EntityAttribute(key=false,size = 19,name="sys_option_state",desc="sysOptionState")
    private java.lang.Long sysOptionState;
    private List<?> list;
    public SysOption() {}

    public SysOption(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysOption(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysOptionId");
        if (str!=null && !str.isEmpty()) {
            setSysOptionId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysOptionKey");
        if (str!=null && !str.isEmpty()) {
            setSysOptionKey(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysOptionTitle");
        if (str!=null && !str.isEmpty()) {
            setSysOptionTitle(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysOptionValue");
        if (str!=null && !str.isEmpty()) {
            setSysOptionValue(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysOptionState");
        if (str!=null && !str.isEmpty()) {
            setSysOptionState(java.lang.Long.valueOf(str));
        }
    }

    public SysOption(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysOption(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.save(this);
    }
    public int delete(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.delete(this);
    }
    public int update(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.update(this);
    }
    public int saveAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.saveAsync(this);
    }
    public int deleteAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.deleteAsync(this);
    }
    public int updateAsync(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.updateAsync(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.query(this);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,int page,int size) throws Exception {
        return sysOptionDao.query(this,page,size);
    }
    public List<SysOption> query(SysOptionDao sysOptionDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysOptionDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysOptionDao sysOptionDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysOptionDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysOption find(SysOptionDao sysOptionDao) throws Exception {
        return sysOptionDao.find(this);
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
        json.put("sysOptionId", getSysOptionId());
        json.put("sysOptionKey", getSysOptionKey());
        json.put("sysOptionTitle", getSysOptionTitle());
        json.put("sysOptionValue", getSysOptionValue());
        json.put("sysOptionState", getSysOptionState());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysOptionId() {
        return sysOptionId;
    }
    public SysOption setSysOptionId(java.lang.Long sysOptionId) {
        int size0 = Tools.textLength(sysOptionId);
        if (size0 > 19) {
            throw new RuntimeException("SysOption.sysOptionId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysOptionId);
        }
        this.sysOptionId = sysOptionId;
        return this;
    }
    public SysOption setSysOptionIdUnsafe(java.lang.Long sysOptionId) {
        this.sysOptionId = sysOptionId;
        return this;
    }
    public java.lang.String getSysOptionKey() {
        return sysOptionKey;
    }
    public SysOption setSysOptionKey(java.lang.String sysOptionKey) {
        int size0 = Tools.textLength(sysOptionKey);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionKey最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionKey);
        }
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public SysOption setSysOptionKeyUnsafe(java.lang.String sysOptionKey) {
        this.sysOptionKey = sysOptionKey;
        return this;
    }
    public java.lang.String getSysOptionTitle() {
        return sysOptionTitle;
    }
    public SysOption setSysOptionTitle(java.lang.String sysOptionTitle) {
        int size0 = Tools.textLength(sysOptionTitle);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionTitle);
        }
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public SysOption setSysOptionTitleUnsafe(java.lang.String sysOptionTitle) {
        this.sysOptionTitle = sysOptionTitle;
        return this;
    }
    public java.lang.String getSysOptionValue() {
        return sysOptionValue;
    }
    public SysOption setSysOptionValue(java.lang.String sysOptionValue) {
        int size0 = Tools.textLength(sysOptionValue);
        if (size0 > 255) {
            throw new RuntimeException("SysOption.sysOptionValue最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysOptionValue);
        }
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public SysOption setSysOptionValueUnsafe(java.lang.String sysOptionValue) {
        this.sysOptionValue = sysOptionValue;
        return this;
    }
    public java.lang.Long getSysOptionState() {
        return sysOptionState;
    }
    public SysOption setSysOptionState(java.lang.Long sysOptionState) {
        int size0 = Tools.textLength(sysOptionState);
        if (size0 > 19) {
            throw new RuntimeException("SysOption.sysOptionState最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysOptionState);
        }
        this.sysOptionState = sysOptionState;
        return this;
    }
    public SysOption setSysOptionStateUnsafe(java.lang.Long sysOptionState) {
        this.sysOptionState = sysOptionState;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
