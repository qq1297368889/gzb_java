package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysMappingColumnDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_mapping_column",desc="sysMappingColumn")
public class SysMappingColumn implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key=true,size = 19,name="sys_mapping_column_id",desc="映射ID",type="java.lang.Long")
    private java.lang.Long sysMappingColumnId;
    @EntityAttribute(key=false,size = 100,name="sys_mapping_column_name",desc="列名驼峰",type="java.lang.String")
    private java.lang.String sysMappingColumnName;
    @EntityAttribute(key=false,size = 100,name="sys_mapping_column_title",desc="标题",type="java.lang.String")
    private java.lang.String sysMappingColumnTitle;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_column_width",desc="宽度",type="java.lang.Long")
    private java.lang.Long sysMappingColumnWidth;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_column_file",desc="文件模板",type="java.lang.String")
    private java.lang.String sysMappingColumnFile;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_column_image",desc="图片模板",type="java.lang.String")
    private java.lang.String sysMappingColumnImage;
    @EntityAttribute(key=false,size = 255,name="sys_mapping_column_date",desc="时间模板",type="java.lang.String")
    private java.lang.String sysMappingColumnDate;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_column_number",desc="是否数字",type="java.lang.Long")
    private java.lang.Long sysMappingColumnNumber;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_column_text",desc="是否文本",type="java.lang.Long")
    private java.lang.Long sysMappingColumnText;
    @EntityAttribute(key=false,size = 19,name="sys_mapping_column_table",desc="表ID",type="java.lang.Long")
    private java.lang.Long sysMappingColumnTable;
    @EntityAttribute(key=false,size = 100,name="sys_mapping_column_request",desc="引用模板",type="java.lang.String")
    private java.lang.String sysMappingColumnRequest;
    @EntityAttribute(key=false,size = 100,name="sys_mapping_column_option",desc="选项模板",type="java.lang.String")
    private java.lang.String sysMappingColumnOption;
    @EntityAttribute(key=false,size = 100,name="sys_mapping_column_sql",desc="sql模板",type="java.lang.String")
    private java.lang.String sysMappingColumnSql;
    private Object data;
   public SysMappingColumn() {}

    public SysMappingColumn(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysMappingColumn(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysMappingColumn(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }
    public int save(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.save(this);
    }
    public int delete(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.delete(this);
    }
    public int update(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.update(this);
    }
    public int saveAsync(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.saveAsync(this);
    }
    public int deleteAsync(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.deleteAsync(this);
    }
    public int updateAsync(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.updateAsync(this);
    }
    public List<SysMappingColumn> query(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.query(this);
    }
    public List<SysMappingColumn> query(SysMappingColumnDao sysMappingColumnDao,int page,int size) throws Exception {
        return sysMappingColumnDao.query(this,page,size);
    }
    public List<SysMappingColumn> query(SysMappingColumnDao sysMappingColumnDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysMappingColumnDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysMappingColumnDao sysMappingColumnDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysMappingColumnDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysMappingColumnDao sysMappingColumnDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysMappingColumnDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysMappingColumn find(SysMappingColumnDao sysMappingColumnDao) throws Exception {
        return sysMappingColumnDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(398);
       boolean app01=false;
        sb.append("{");
        if (this.sysMappingColumnId != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnId\":\"").append(sysMappingColumnId).append("\"");
        }
        if (this.sysMappingColumnName != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnName\":");
            sb.append(Tools.toJson(sysMappingColumnName));        }
        if (this.sysMappingColumnTitle != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnTitle\":");
            sb.append(Tools.toJson(sysMappingColumnTitle));        }
        if (this.sysMappingColumnWidth != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnWidth\":\"").append(sysMappingColumnWidth).append("\"");
        }
        if (this.sysMappingColumnFile != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnFile\":");
            sb.append(Tools.toJson(sysMappingColumnFile));        }
        if (this.sysMappingColumnImage != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnImage\":");
            sb.append(Tools.toJson(sysMappingColumnImage));        }
        if (this.sysMappingColumnDate != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnDate\":");
            sb.append(Tools.toJson(sysMappingColumnDate));        }
        if (this.sysMappingColumnNumber != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnNumber\":\"").append(sysMappingColumnNumber).append("\"");
        }
        if (this.sysMappingColumnText != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnText\":\"").append(sysMappingColumnText).append("\"");
        }
        if (this.sysMappingColumnTable != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnTable\":\"").append(sysMappingColumnTable).append("\"");
        }
        if (this.sysMappingColumnRequest != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnRequest\":");
            sb.append(Tools.toJson(sysMappingColumnRequest));        }
        if (this.sysMappingColumnOption != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnOption\":");
            sb.append(Tools.toJson(sysMappingColumnOption));        }
        if (this.sysMappingColumnSql != null) {
            if(app01){sb.append(",");}app01=true;
            sb.append("\"sysMappingColumnSql\":");
            sb.append(Tools.toJson(sysMappingColumnSql));        }
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
        result.set("sysMappingColumnId", sysMappingColumnId);
        result.set("sysMappingColumnName", sysMappingColumnName);
        result.set("sysMappingColumnTitle", sysMappingColumnTitle);
        result.set("sysMappingColumnWidth", sysMappingColumnWidth);
        result.set("sysMappingColumnFile", sysMappingColumnFile);
        result.set("sysMappingColumnImage", sysMappingColumnImage);
        result.set("sysMappingColumnDate", sysMappingColumnDate);
        result.set("sysMappingColumnNumber", sysMappingColumnNumber);
        result.set("sysMappingColumnText", sysMappingColumnText);
        result.set("sysMappingColumnTable", sysMappingColumnTable);
        result.set("sysMappingColumnRequest", sysMappingColumnRequest);
        result.set("sysMappingColumnOption", sysMappingColumnOption);
        result.set("sysMappingColumnSql", sysMappingColumnSql);
        result.set(Config.get("json.entity.data","data"), data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysMappingColumnId=result.getLong("sysMappingColumnId", null);
        this.sysMappingColumnName=result.getString("sysMappingColumnName", null);
        this.sysMappingColumnTitle=result.getString("sysMappingColumnTitle", null);
        this.sysMappingColumnWidth=result.getLong("sysMappingColumnWidth", null);
        this.sysMappingColumnFile=result.getString("sysMappingColumnFile", null);
        this.sysMappingColumnImage=result.getString("sysMappingColumnImage", null);
        this.sysMappingColumnDate=result.getString("sysMappingColumnDate", null);
        this.sysMappingColumnNumber=result.getLong("sysMappingColumnNumber", null);
        this.sysMappingColumnText=result.getLong("sysMappingColumnText", null);
        this.sysMappingColumnTable=result.getLong("sysMappingColumnTable", null);
        this.sysMappingColumnRequest=result.getString("sysMappingColumnRequest", null);
        this.sysMappingColumnOption=result.getString("sysMappingColumnOption", null);
        this.sysMappingColumnSql=result.getString("sysMappingColumnSql", null);
        Object obj = result.get(Config.get("json.entity.data","data"),null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public java.lang.Long getSysMappingColumnId() {
        return sysMappingColumnId;
    }
    public SysMappingColumn setSysMappingColumnId(java.lang.Long sysMappingColumnId) {
        this.sysMappingColumnId = sysMappingColumnId;
        return this;
    }
    public java.lang.String getSysMappingColumnName() {
        return sysMappingColumnName;
    }
    public SysMappingColumn setSysMappingColumnName(java.lang.String sysMappingColumnName) {
        int size0 = Tools.textLength(sysMappingColumnName);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnName最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysMappingColumnName);
        }
        this.sysMappingColumnName = sysMappingColumnName;
        return this;
    }
    public SysMappingColumn setSysMappingColumnNameUnsafe(java.lang.String sysMappingColumnName) {
        this.sysMappingColumnName = sysMappingColumnName;
        return this;
    }
    public java.lang.String getSysMappingColumnTitle() {
        return sysMappingColumnTitle;
    }
    public SysMappingColumn setSysMappingColumnTitle(java.lang.String sysMappingColumnTitle) {
        int size0 = Tools.textLength(sysMappingColumnTitle);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnTitle最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysMappingColumnTitle);
        }
        this.sysMappingColumnTitle = sysMappingColumnTitle;
        return this;
    }
    public SysMappingColumn setSysMappingColumnTitleUnsafe(java.lang.String sysMappingColumnTitle) {
        this.sysMappingColumnTitle = sysMappingColumnTitle;
        return this;
    }
    public java.lang.Long getSysMappingColumnWidth() {
        return sysMappingColumnWidth;
    }
    public SysMappingColumn setSysMappingColumnWidth(java.lang.Long sysMappingColumnWidth) {
        this.sysMappingColumnWidth = sysMappingColumnWidth;
        return this;
    }
    public java.lang.String getSysMappingColumnFile() {
        return sysMappingColumnFile;
    }
    public SysMappingColumn setSysMappingColumnFile(java.lang.String sysMappingColumnFile) {
        int size0 = Tools.textLength(sysMappingColumnFile);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnFile最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingColumnFile);
        }
        this.sysMappingColumnFile = sysMappingColumnFile;
        return this;
    }
    public SysMappingColumn setSysMappingColumnFileUnsafe(java.lang.String sysMappingColumnFile) {
        this.sysMappingColumnFile = sysMappingColumnFile;
        return this;
    }
    public java.lang.String getSysMappingColumnImage() {
        return sysMappingColumnImage;
    }
    public SysMappingColumn setSysMappingColumnImage(java.lang.String sysMappingColumnImage) {
        int size0 = Tools.textLength(sysMappingColumnImage);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnImage最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingColumnImage);
        }
        this.sysMappingColumnImage = sysMappingColumnImage;
        return this;
    }
    public SysMappingColumn setSysMappingColumnImageUnsafe(java.lang.String sysMappingColumnImage) {
        this.sysMappingColumnImage = sysMappingColumnImage;
        return this;
    }
    public java.lang.String getSysMappingColumnDate() {
        return sysMappingColumnDate;
    }
    public SysMappingColumn setSysMappingColumnDate(java.lang.String sysMappingColumnDate) {
        int size0 = Tools.textLength(sysMappingColumnDate);
        if (size0 > 255) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnDate最大长度为:255,实际长度为:"+ size0 +",数据为:"+sysMappingColumnDate);
        }
        this.sysMappingColumnDate = sysMappingColumnDate;
        return this;
    }
    public SysMappingColumn setSysMappingColumnDateUnsafe(java.lang.String sysMappingColumnDate) {
        this.sysMappingColumnDate = sysMappingColumnDate;
        return this;
    }
    public java.lang.Long getSysMappingColumnNumber() {
        return sysMappingColumnNumber;
    }
    public SysMappingColumn setSysMappingColumnNumber(java.lang.Long sysMappingColumnNumber) {
        this.sysMappingColumnNumber = sysMappingColumnNumber;
        return this;
    }
    public java.lang.Long getSysMappingColumnText() {
        return sysMappingColumnText;
    }
    public SysMappingColumn setSysMappingColumnText(java.lang.Long sysMappingColumnText) {
        this.sysMappingColumnText = sysMappingColumnText;
        return this;
    }
    public java.lang.Long getSysMappingColumnTable() {
        return sysMappingColumnTable;
    }
    public SysMappingColumn setSysMappingColumnTable(java.lang.Long sysMappingColumnTable) {
        this.sysMappingColumnTable = sysMappingColumnTable;
        return this;
    }
    public java.lang.String getSysMappingColumnRequest() {
        return sysMappingColumnRequest;
    }
    public SysMappingColumn setSysMappingColumnRequest(java.lang.String sysMappingColumnRequest) {
        int size0 = Tools.textLength(sysMappingColumnRequest);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnRequest最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysMappingColumnRequest);
        }
        this.sysMappingColumnRequest = sysMappingColumnRequest;
        return this;
    }
    public SysMappingColumn setSysMappingColumnRequestUnsafe(java.lang.String sysMappingColumnRequest) {
        this.sysMappingColumnRequest = sysMappingColumnRequest;
        return this;
    }
    public java.lang.String getSysMappingColumnOption() {
        return sysMappingColumnOption;
    }
    public SysMappingColumn setSysMappingColumnOption(java.lang.String sysMappingColumnOption) {
        int size0 = Tools.textLength(sysMappingColumnOption);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnOption最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysMappingColumnOption);
        }
        this.sysMappingColumnOption = sysMappingColumnOption;
        return this;
    }
    public SysMappingColumn setSysMappingColumnOptionUnsafe(java.lang.String sysMappingColumnOption) {
        this.sysMappingColumnOption = sysMappingColumnOption;
        return this;
    }
    public java.lang.String getSysMappingColumnSql() {
        return sysMappingColumnSql;
    }
    public SysMappingColumn setSysMappingColumnSql(java.lang.String sysMappingColumnSql) {
        int size0 = Tools.textLength(sysMappingColumnSql);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysMappingColumn.sysMappingColumnSql最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysMappingColumnSql);
        }
        this.sysMappingColumnSql = sysMappingColumnSql;
        return this;
    }
    public SysMappingColumn setSysMappingColumnSqlUnsafe(java.lang.String sysMappingColumnSql) {
        this.sysMappingColumnSql = sysMappingColumnSql;
        return this;
    }
    public SysMappingColumn setList(List<?> data) {
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

    public SysMappingColumn setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysMappingColumn putMap(String key, Object value) {
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
    public SysMappingColumn setData(Object data) {
        this.data = data;
        return this;
    }
}
