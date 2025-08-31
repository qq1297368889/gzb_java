package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysMappingDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_mapping",desc="sysMapping")
public class SysMapping implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
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

    public SysMapping(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysMapping(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysMapping(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysMapping(ResultSet resultSet) throws SQLException {
        loadJson(resultSet);
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
    public SqlTemplate toSelectSql(String sortField, String sortType, Integer size, Boolean selectId) {
        return SqlTools.toSelectSql(this,sortField, sortType, size, selectId);
    }

    //插入 可以指定id  不指定自动生成
    public SqlTemplate toSave() {
        return SqlTools.toSave(this);
    }

    //根据id修改 高级需求请手动写sql
    public SqlTemplate toUpdate() {
        return SqlTools.toUpdate(this);
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public SqlTemplate toDelete(Boolean selectId) {
        return SqlTools.toDelete(this,selectId);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysMappingId", sysMappingId);
        result.set("sysMappingKey", sysMappingKey);
        result.set("sysMappingTitle", sysMappingTitle);
        result.set("sysMappingVal", sysMappingVal);
        result.set("sysMappingTableWidth", sysMappingTableWidth);
        result.set("sysMappingSelect", sysMappingSelect);
        result.set("sysMappingImage", sysMappingImage);
        result.set("sysMappingFile", sysMappingFile);
        result.set("sysMappingDate", sysMappingDate);
        result.set("sysMappingScript", sysMappingScript);
        result.set("sysMappingSort", sysMappingSort);
        result.set(dataName, list);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
         loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysMappingId=result.getLong("sysMappingId", null);
        this.sysMappingKey=result.getString("sysMappingKey", null);
        this.sysMappingTitle=result.getString("sysMappingTitle", null);
        this.sysMappingVal=result.getString("sysMappingVal", null);
        this.sysMappingTableWidth=result.getLong("sysMappingTableWidth", null);
        this.sysMappingSelect=result.getString("sysMappingSelect", null);
        this.sysMappingImage=result.getString("sysMappingImage", null);
        this.sysMappingFile=result.getString("sysMappingFile", null);
        this.sysMappingDate=result.getString("sysMappingDate", null);
        this.sysMappingScript=result.getString("sysMappingScript", null);
        this.sysMappingSort=result.getLong("sysMappingSort", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof List) {
            this.list=(List<?>)obj;
        }
    }
    public void loadJson(ResultSet resultSet) throws SQLException {
        //ResultSetMetaData rsMetaData = resultSet.getMetaData();
        String temp=null;
        while (resultSet.next()) {
            temp=resultSet.getString("sys_mapping_id");
            if (temp!=null) {
                this.sysMappingId=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_key");
            if (temp!=null) {
                this.sysMappingKey=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_title");
            if (temp!=null) {
                this.sysMappingTitle=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_val");
            if (temp!=null) {
                this.sysMappingVal=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_table_width");
            if (temp!=null) {
                this.sysMappingTableWidth=java.lang.Long.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_select");
            if (temp!=null) {
                this.sysMappingSelect=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_image");
            if (temp!=null) {
                this.sysMappingImage=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_file");
            if (temp!=null) {
                this.sysMappingFile=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_date");
            if (temp!=null) {
                this.sysMappingDate=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_script");
            if (temp!=null) {
                this.sysMappingScript=java.lang.String.valueOf(temp);
            }
            temp=resultSet.getString("sys_mapping_sort");
            if (temp!=null) {
                this.sysMappingSort=java.lang.Long.valueOf(temp);
            }
        }
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
