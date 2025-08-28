package com.acquisition.entity;
import com.acquisition.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.acquisition.dao.SysMappingDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_mapping",desc="sysMapping")
public class SysMapping implements Serializable{
    @EntityAttribute(key=true,size = 19,name="sys_mapping_id",desc="sysMappingId")
    private java.lang.Long sysMappingId;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_key",desc="sysMappingKey")
    private java.lang.String sysMappingKey;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_title",desc="sysMappingTitle")
    private java.lang.String sysMappingTitle;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_val",desc="sysMappingVal")
    private java.lang.String sysMappingVal;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_table_width",desc="sysMappingTableWidth")
    private java.lang.Long sysMappingTableWidth;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_select",desc="sysMappingSelect")
    private java.lang.String sysMappingSelect;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_image",desc="sysMappingImage")
    private java.lang.String sysMappingImage;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_file",desc="sysMappingFile")
    private java.lang.String sysMappingFile;
    @EntityAttribute(key=false,size = 20,name="sys_mapping_date",desc="sysMappingDate")
    private java.lang.String sysMappingDate;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_script",desc="sysMappingScript")
    private java.lang.String sysMappingScript;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_sort",desc="sysMappingSort")
    private java.lang.Long sysMappingSort;
    private List<?> list;
    public SysMapping() {}

    public SysMapping(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysMapping(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysMappingId");
        if (str!=null && !str.isEmpty()) {
            setSysMappingId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysMappingKey");
        if (str!=null && !str.isEmpty()) {
            setSysMappingKey(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingTitle");
        if (str!=null && !str.isEmpty()) {
            setSysMappingTitle(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingVal");
        if (str!=null && !str.isEmpty()) {
            setSysMappingVal(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingTableWidth");
        if (str!=null && !str.isEmpty()) {
            setSysMappingTableWidth(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysMappingSelect");
        if (str!=null && !str.isEmpty()) {
            setSysMappingSelect(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingImage");
        if (str!=null && !str.isEmpty()) {
            setSysMappingImage(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingFile");
        if (str!=null && !str.isEmpty()) {
            setSysMappingFile(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingDate");
        if (str!=null && !str.isEmpty()) {
            setSysMappingDate(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingScript");
        if (str!=null && !str.isEmpty()) {
            setSysMappingScript(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysMappingSort");
        if (str!=null && !str.isEmpty()) {
            setSysMappingSort(java.lang.Long.valueOf(str));
        }
    }

    public SysMapping(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysMapping(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
    }


    public int save(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.save(this);
    }
    public int delete(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.delete(this);
    }
    public int update(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.update(this);
    }
    public int saveAsync(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.saveAsync(this);
    }
    public int deleteAsync(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.deleteAsync(this);
    }
    public int updateAsync(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.updateAsync(this);
    }
    public List<SysMapping> query(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.query(this);
    }
    public List<SysMapping> query(SysMappingDao sysMappingDao,int page,int size) throws Exception {
        return sysMappingDao.query(this,page,size);
    }
    public List<SysMapping> query(SysMappingDao sysMappingDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysMappingDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysMappingDao sysMappingDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysMappingDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysMappingDao sysMappingDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysMappingDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysMapping find(SysMappingDao sysMappingDao) throws Exception {
        return sysMappingDao.find(this);
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
        json.put("sysMappingId", getSysMappingId());
        json.put("sysMappingKey", getSysMappingKey());
        json.put("sysMappingTitle", getSysMappingTitle());
        json.put("sysMappingVal", getSysMappingVal());
        json.put("sysMappingTableWidth", getSysMappingTableWidth());
        json.put("sysMappingSelect", getSysMappingSelect());
        json.put("sysMappingImage", getSysMappingImage());
        json.put("sysMappingFile", getSysMappingFile());
        json.put("sysMappingDate", getSysMappingDate());
        json.put("sysMappingScript", getSysMappingScript());
        json.put("sysMappingSort", getSysMappingSort());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysMappingId() {
        return sysMappingId;
    }
    public SysMapping setSysMappingId(java.lang.Long sysMappingId) {
        int size0 = Tools.textLength(sysMappingId);
        if (size0 > 19) {
            throw new RuntimeException("SysMapping.sysMappingId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysMappingId);
        }
        this.sysMappingId = sysMappingId;
        return this;
    }
    public SysMapping setSysMappingIdUnsafe(java.lang.Long sysMappingId) {
        this.sysMappingId = sysMappingId;
        return this;
    }
    public java.lang.String getSysMappingKey() {
        return sysMappingKey;
    }
    public SysMapping setSysMappingKey(java.lang.String sysMappingKey) {
        int size0 = Tools.textLength(sysMappingKey);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingKey最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingKey);
        }
        this.sysMappingKey = sysMappingKey;
        return this;
    }
    public SysMapping setSysMappingKeyUnsafe(java.lang.String sysMappingKey) {
        this.sysMappingKey = sysMappingKey;
        return this;
    }
    public java.lang.String getSysMappingTitle() {
        return sysMappingTitle;
    }
    public SysMapping setSysMappingTitle(java.lang.String sysMappingTitle) {
        int size0 = Tools.textLength(sysMappingTitle);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingTitle最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingTitle);
        }
        this.sysMappingTitle = sysMappingTitle;
        return this;
    }
    public SysMapping setSysMappingTitleUnsafe(java.lang.String sysMappingTitle) {
        this.sysMappingTitle = sysMappingTitle;
        return this;
    }
    public java.lang.String getSysMappingVal() {
        return sysMappingVal;
    }
    public SysMapping setSysMappingVal(java.lang.String sysMappingVal) {
        int size0 = Tools.textLength(sysMappingVal);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingVal最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingVal);
        }
        this.sysMappingVal = sysMappingVal;
        return this;
    }
    public SysMapping setSysMappingValUnsafe(java.lang.String sysMappingVal) {
        this.sysMappingVal = sysMappingVal;
        return this;
    }
    public java.lang.Long getSysMappingTableWidth() {
        return sysMappingTableWidth;
    }
    public SysMapping setSysMappingTableWidth(java.lang.Long sysMappingTableWidth) {
        int size0 = Tools.textLength(sysMappingTableWidth);
        if (size0 > 19) {
            throw new RuntimeException("SysMapping.sysMappingTableWidth最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysMappingTableWidth);
        }
        this.sysMappingTableWidth = sysMappingTableWidth;
        return this;
    }
    public SysMapping setSysMappingTableWidthUnsafe(java.lang.Long sysMappingTableWidth) {
        this.sysMappingTableWidth = sysMappingTableWidth;
        return this;
    }
    public java.lang.String getSysMappingSelect() {
        return sysMappingSelect;
    }
    public SysMapping setSysMappingSelect(java.lang.String sysMappingSelect) {
        int size0 = Tools.textLength(sysMappingSelect);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingSelect最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingSelect);
        }
        this.sysMappingSelect = sysMappingSelect;
        return this;
    }
    public SysMapping setSysMappingSelectUnsafe(java.lang.String sysMappingSelect) {
        this.sysMappingSelect = sysMappingSelect;
        return this;
    }
    public java.lang.String getSysMappingImage() {
        return sysMappingImage;
    }
    public SysMapping setSysMappingImage(java.lang.String sysMappingImage) {
        int size0 = Tools.textLength(sysMappingImage);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingImage最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingImage);
        }
        this.sysMappingImage = sysMappingImage;
        return this;
    }
    public SysMapping setSysMappingImageUnsafe(java.lang.String sysMappingImage) {
        this.sysMappingImage = sysMappingImage;
        return this;
    }
    public java.lang.String getSysMappingFile() {
        return sysMappingFile;
    }
    public SysMapping setSysMappingFile(java.lang.String sysMappingFile) {
        int size0 = Tools.textLength(sysMappingFile);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingFile最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingFile);
        }
        this.sysMappingFile = sysMappingFile;
        return this;
    }
    public SysMapping setSysMappingFileUnsafe(java.lang.String sysMappingFile) {
        this.sysMappingFile = sysMappingFile;
        return this;
    }
    public java.lang.String getSysMappingDate() {
        return sysMappingDate;
    }
    public SysMapping setSysMappingDate(java.lang.String sysMappingDate) {
        int size0 = Tools.textLength(sysMappingDate);
        if (size0 > 20) {
            throw new RuntimeException("SysMapping.sysMappingDate最大长度为:20,实际长度为:"+ size0 +",数据为:"+sysMappingDate);
        }
        this.sysMappingDate = sysMappingDate;
        return this;
    }
    public SysMapping setSysMappingDateUnsafe(java.lang.String sysMappingDate) {
        this.sysMappingDate = sysMappingDate;
        return this;
    }
    public java.lang.String getSysMappingScript() {
        return sysMappingScript;
    }
    public SysMapping setSysMappingScript(java.lang.String sysMappingScript) {
        int size0 = Tools.textLength(sysMappingScript);
        if (size0 > 255) {
            throw new RuntimeException("SysMapping.sysMappingScript最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingScript);
        }
        this.sysMappingScript = sysMappingScript;
        return this;
    }
    public SysMapping setSysMappingScriptUnsafe(java.lang.String sysMappingScript) {
        this.sysMappingScript = sysMappingScript;
        return this;
    }
    public java.lang.Long getSysMappingSort() {
        return sysMappingSort;
    }
    public SysMapping setSysMappingSort(java.lang.Long sysMappingSort) {
        int size0 = Tools.textLength(sysMappingSort);
        if (size0 > 19) {
            throw new RuntimeException("SysMapping.sysMappingSort最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysMappingSort);
        }
        this.sysMappingSort = sysMappingSort;
        return this;
    }
    public SysMapping setSysMappingSortUnsafe(java.lang.Long sysMappingSort) {
        this.sysMappingSort = sysMappingSort;
        return this;
    }
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
