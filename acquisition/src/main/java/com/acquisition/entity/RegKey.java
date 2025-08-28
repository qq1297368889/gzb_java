package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.RegKeyDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="reg_key",desc="regKey")
public class RegKey implements Serializable{
    @EntityAttribute(key=true,size = 19,name="reg_key_id",desc="regKeyId")
    private java.lang.Long regKeyId;
    @EntityAttribute(key=false,size = 100,name="reg_key_text",desc="regKeyText")
    private java.lang.String regKeyText;
    @EntityAttribute(key=false,size = 100,name="reg_key_desc",desc="regKeyDesc")
    private java.lang.String regKeyDesc;
    @EntityAttribute(key=false,size = 19,name="reg_key_start",desc="regKeyStart")
    private java.lang.String regKeyStart;
    @EntityAttribute(key=false,size = 19,name="reg_key_end",desc="regKeyEnd")
    private java.lang.String regKeyEnd;
    @EntityAttribute(key=false,size = 100,name="reg_key_ip",desc="regKeyIp")
    private java.lang.String regKeyIp;
    @EntityAttribute(key=false,size = 20,name="reg_key_fz",desc="regKeyFz")
    private java.lang.String regKeyFz;
    @EntityAttribute(key=false,size = 19,name="reg_key_num",desc="regKeyNum")
    private java.lang.Long regKeyNum;
    private List<?> list;
    public RegKey() {}

    public RegKey(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public RegKey(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("regKeyId");
        if (str!=null && !str.isEmpty()) {
            setRegKeyId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("regKeyText");
        if (str!=null && !str.isEmpty()) {
            setRegKeyText(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyDesc");
        if (str!=null && !str.isEmpty()) {
            setRegKeyDesc(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyStart");
        if (str!=null && !str.isEmpty()) {
            setRegKeyStart(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyEnd");
        if (str!=null && !str.isEmpty()) {
            setRegKeyEnd(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyIp");
        if (str!=null && !str.isEmpty()) {
            setRegKeyIp(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyFz");
        if (str!=null && !str.isEmpty()) {
            setRegKeyFz(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("regKeyNum");
        if (str!=null && !str.isEmpty()) {
            setRegKeyNum(java.lang.Long.valueOf(str));
        }
    }

    public RegKey(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public RegKey(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.save(this);
    }
    public int delete(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.delete(this);
    }
    public int update(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.update(this);
    }
    public int saveAsync(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.saveAsync(this);
    }
    public int deleteAsync(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.deleteAsync(this);
    }
    public int updateAsync(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.updateAsync(this);
    }
    public List<RegKey> query(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.query(this);
    }
    public List<RegKey> query(RegKeyDao regKeyDao,int page,int size) throws Exception {
        return regKeyDao.query(this,page,size);
    }
    public List<RegKey> query(RegKeyDao regKeyDao,String sortField,String sortType,int page,int size) throws Exception {
        return regKeyDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(RegKeyDao regKeyDao, String sortField, String sortType, int page, int size) throws Exception {
        return regKeyDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(RegKeyDao regKeyDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return regKeyDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public RegKey find(RegKeyDao regKeyDao) throws Exception {
        return regKeyDao.find(this);
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
        json.put("regKeyId", getRegKeyId());
        json.put("regKeyText", getRegKeyText());
        json.put("regKeyDesc", getRegKeyDesc());
        json.put("regKeyStart", getRegKeyStart());
        json.put("regKeyEnd", getRegKeyEnd());
        json.put("regKeyIp", getRegKeyIp());
        json.put("regKeyFz", getRegKeyFz());
        json.put("regKeyNum", getRegKeyNum());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getRegKeyId() {
        return regKeyId;
    }
    public RegKey setRegKeyId(java.lang.Long regKeyId) {
        int size0 = Tools.textLength(regKeyId);
        if (size0 > 19) {
            throw new RuntimeException("RegKey.regKeyId最大长度为:19,实际长度为:"+ size0 +",数据为:"+regKeyId);
        }
        this.regKeyId = regKeyId;
        return this;
    }
    public RegKey setRegKeyIdUnsafe(java.lang.Long regKeyId) {
        this.regKeyId = regKeyId;
        return this;
    }
    public java.lang.String getRegKeyText() {
        return regKeyText;
    }
    public RegKey setRegKeyText(java.lang.String regKeyText) {
        int size0 = Tools.textLength(regKeyText);
        if (size0 > 100) {
            throw new RuntimeException("RegKey.regKeyText最大长度为:100,实际长度为:"+ size0 +",数据为:"+regKeyText);
        }
        this.regKeyText = regKeyText;
        return this;
    }
    public RegKey setRegKeyTextUnsafe(java.lang.String regKeyText) {
        this.regKeyText = regKeyText;
        return this;
    }
    public java.lang.String getRegKeyDesc() {
        return regKeyDesc;
    }
    public RegKey setRegKeyDesc(java.lang.String regKeyDesc) {
        int size0 = Tools.textLength(regKeyDesc);
        if (size0 > 100) {
            throw new RuntimeException("RegKey.regKeyDesc最大长度为:100,实际长度为:"+ size0 +",数据为:"+regKeyDesc);
        }
        this.regKeyDesc = regKeyDesc;
        return this;
    }
    public RegKey setRegKeyDescUnsafe(java.lang.String regKeyDesc) {
        this.regKeyDesc = regKeyDesc;
        return this;
    }
    public java.lang.String getRegKeyStart() {
        return regKeyStart;
    }
    public RegKey setRegKeyStart(java.lang.String regKeyStart) {
        int size0 = Tools.textLength(regKeyStart);
        if (size0 > 19) {
            throw new RuntimeException("RegKey.regKeyStart最大长度为:19,实际长度为:"+ size0 +",数据为:"+regKeyStart);
        }
        this.regKeyStart = regKeyStart;
        return this;
    }
    public RegKey setRegKeyStartUnsafe(java.lang.String regKeyStart) {
        this.regKeyStart = regKeyStart;
        return this;
    }
    public java.lang.String getRegKeyEnd() {
        return regKeyEnd;
    }
    public RegKey setRegKeyEnd(java.lang.String regKeyEnd) {
        int size0 = Tools.textLength(regKeyEnd);
        if (size0 > 19) {
            throw new RuntimeException("RegKey.regKeyEnd最大长度为:19,实际长度为:"+ size0 +",数据为:"+regKeyEnd);
        }
        this.regKeyEnd = regKeyEnd;
        return this;
    }
    public RegKey setRegKeyEndUnsafe(java.lang.String regKeyEnd) {
        this.regKeyEnd = regKeyEnd;
        return this;
    }
    public java.lang.String getRegKeyIp() {
        return regKeyIp;
    }
    public RegKey setRegKeyIp(java.lang.String regKeyIp) {
        int size0 = Tools.textLength(regKeyIp);
        if (size0 > 100) {
            throw new RuntimeException("RegKey.regKeyIp最大长度为:100,实际长度为:"+ size0 +",数据为:"+regKeyIp);
        }
        this.regKeyIp = regKeyIp;
        return this;
    }
    public RegKey setRegKeyIpUnsafe(java.lang.String regKeyIp) {
        this.regKeyIp = regKeyIp;
        return this;
    }
    public java.lang.String getRegKeyFz() {
        return regKeyFz;
    }
    public RegKey setRegKeyFz(java.lang.String regKeyFz) {
        int size0 = Tools.textLength(regKeyFz);
        if (size0 > 20) {
            throw new RuntimeException("RegKey.regKeyFz最大长度为:20,实际长度为:"+ size0 +",数据为:"+regKeyFz);
        }
        this.regKeyFz = regKeyFz;
        return this;
    }
    public RegKey setRegKeyFzUnsafe(java.lang.String regKeyFz) {
        this.regKeyFz = regKeyFz;
        return this;
    }
    public java.lang.Long getRegKeyNum() {
        return regKeyNum;
    }
    public RegKey setRegKeyNum(java.lang.Long regKeyNum) {
        int size0 = Tools.textLength(regKeyNum);
        if (size0 > 19) {
            throw new RuntimeException("RegKey.regKeyNum最大长度为:19,实际长度为:"+ size0 +",数据为:"+regKeyNum);
        }
        this.regKeyNum = regKeyNum;
        return this;
    }
    public RegKey setRegKeyNumUnsafe(java.lang.Long regKeyNum) {
        this.regKeyNum = regKeyNum;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
