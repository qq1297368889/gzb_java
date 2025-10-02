package com.frame.entity;
import gzb.tools.*;
import com.frame.dao.SysFileDao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;
import gzb.tools.json.JsonSerializable;
import gzb.tools.json.Result;
import gzb.tools.json.ResultImpl;
@EntityAttribute(name="sys_file",desc="sysFile")
public class SysFile implements Serializable, JsonSerializable{
    private static final long serialVersionUID = 1000L;
    private static final String dataName= Config.get("json.entity.data","data");
    @EntityAttribute(key=true,size = 19,name="sys_file_id",desc="sysFileId")
    private java.lang.Long sysFileId;
    @EntityAttribute(key=false,size = 100,name="sys_file_path",desc="sysFilePath")
    private java.lang.String sysFilePath;
    @EntityAttribute(key=false,size = 64,name="sys_file_md5",desc="sysFileMd5")
    private java.lang.String sysFileMd5;
    @EntityAttribute(key=false,size = 19,name="sys_file_time",desc="sysFileTime")
    private java.lang.String sysFileTime;
    @EntityAttribute(key=false,size = 100,name="sys_file_type",desc="sysFileType")
    private java.lang.String sysFileType;
    private Object data;
   public SysFile() {}

    public SysFile(GzbMap gzbMap) {
        this(gzbMap.map);
    }

    public SysFile(Map<String, Object> map) {
        Result result = new ResultImpl(map);
        loadJson(result);
    }

    public SysFile(String jsonString) {
        Result result = new ResultImpl(jsonString);
        loadJson(result);
    }

    public SysFile(ResultSet resultSet, Set<String> names) throws SQLException {
        loadResultSet(resultSet,names);
    }
    public int save(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.save(this);
    }
    public int delete(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.delete(this);
    }
    public int update(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.update(this);
    }
    public int saveAsync(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.saveAsync(this);
    }
    public int deleteAsync(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.deleteAsync(this);
    }
    public int updateAsync(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.updateAsync(this);
    }
    public List<SysFile> query(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.query(this);
    }
    public List<SysFile> query(SysFileDao sysFileDao,int page,int size) throws Exception {
        return sysFileDao.query(this,page,size);
    }
    public List<SysFile> query(SysFileDao sysFileDao,String sortField,String sortType,int page,int size) throws Exception {
        return sysFileDao.query(this,sortField,sortType,page,size);
    }
    public JSONResult queryPage(SysFileDao sysFileDao, String sortField, String sortType, int page, int size) throws Exception {
        return sysFileDao.queryPage(this,sortField,sortType,page,size,100,100);
    }
    public JSONResult queryPage(SysFileDao sysFileDao, String sortField, String sortType, int page, int size, int maxPage, int maxSize) throws Exception {
        return sysFileDao.queryPage(this,sortField,sortType,page,size,maxPage,maxSize);
    }
    public SysFile find(SysFileDao sysFileDao) throws Exception {
        return sysFileDao.find(this);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        if (this.sysFileId != null) {
            sb.append("\"sysFileId\":\"").append(sysFileId).append("\",");
        }
        if (this.sysFilePath != null) {
            sb.append("\"sysFilePath\":\"").append(sysFilePath).append("\",");
        }
        if (this.sysFileMd5 != null) {
            sb.append("\"sysFileMd5\":\"").append(sysFileMd5).append("\",");
        }
        if (this.sysFileTime != null) {
            sb.append("\"sysFileTime\":\"").append(sysFileTime).append("\",");
        }
        if (this.sysFileType != null) {
            sb.append("\"sysFileType\":\"").append(sysFileType).append("\",");
        }
        if (this.data instanceof Map) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) this.data).entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":").append(Tools.toJson(entry.getValue())).append(",");
            }
        }else if(this.data != null){
            sb.append("\"").append(dataName).append("\":").append(Tools.toJson(this.data)).append(",");
        }
        if (sb.length()>1) {
            sb.delete(sb.length()-1, sb.length());
        }
       return sb.append("}").toString();
    }

    public Result toJson() {
        Result result=new ResultImpl();
        result.set("sysFileId", sysFileId);
        result.set("sysFilePath", sysFilePath);
        result.set("sysFileMd5", sysFileMd5);
        result.set("sysFileTime", sysFileTime);
        result.set("sysFileType", sysFileType);
        result.set(dataName, data);
        return result;
    }

    @Override
    public void loadJson(String json) {
        Result result=new ResultImpl(json);
        loadJson(result);
    }
    public void loadJson(Result result) {
        this.sysFileId=result.getLong("sysFileId", null);
        this.sysFilePath=result.getString("sysFilePath", null);
        this.sysFileMd5=result.getString("sysFileMd5", null);
        this.sysFileTime=result.getString("sysFileTime", null);
        this.sysFileType=result.getString("sysFileType", null);
        Object obj = result.get(dataName,null);
        if (obj instanceof Map) {
            this.data = (Map<String, Object>) obj;
        }
    }
    public void loadResultSet(ResultSet resultSet, Set<String> names) throws SQLException {
        String temp=null;
        if (names.contains("sys_file_id")) {
            temp=resultSet.getString("sys_file_id");
            if (temp!=null) {
                this.sysFileId=java.lang.Long.valueOf(temp);
            }
        }
        if (names.contains("sys_file_path")) {
            temp=resultSet.getString("sys_file_path");
            if (temp!=null) {
                this.sysFilePath=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_md5")) {
            temp=resultSet.getString("sys_file_md5");
            if (temp!=null) {
                this.sysFileMd5=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_time")) {
            temp=resultSet.getString("sys_file_time");
            if (temp!=null) {
                this.sysFileTime=java.lang.String.valueOf(temp);
            }
        }
        if (names.contains("sys_file_type")) {
            temp=resultSet.getString("sys_file_type");
            if (temp!=null) {
                this.sysFileType=java.lang.String.valueOf(temp);
            }
        }
    }
    public java.lang.Long getSysFileId() {
        return sysFileId;
    }
    public SysFile setSysFileId(java.lang.Long sysFileId) {
        this.sysFileId = sysFileId;
        return this;
    }
    public java.lang.String getSysFilePath() {
        return sysFilePath;
    }
    public SysFile setSysFilePath(java.lang.String sysFilePath) {
        int size0 = Tools.textLength(sysFilePath);
        if (size0 > 100) {
            throw new RuntimeException("SysFile.sysFilePath最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysFilePath);
        }
        this.sysFilePath = sysFilePath;
        return this;
    }
    public SysFile setSysFilePathUnsafe(java.lang.String sysFilePath) {
        this.sysFilePath = sysFilePath;
        return this;
    }
    public java.lang.String getSysFileMd5() {
        return sysFileMd5;
    }
    public SysFile setSysFileMd5(java.lang.String sysFileMd5) {
        int size0 = Tools.textLength(sysFileMd5);
        if (size0 > 64) {
            throw new RuntimeException("SysFile.sysFileMd5最大长度为:64,实际长度为:"+ size0 +",数据为:"+sysFileMd5);
        }
        this.sysFileMd5 = sysFileMd5;
        return this;
    }
    public SysFile setSysFileMd5Unsafe(java.lang.String sysFileMd5) {
        this.sysFileMd5 = sysFileMd5;
        return this;
    }
    public java.lang.String getSysFileTime() {
        return sysFileTime;
    }
    public SysFile setSysFileTime(java.lang.String sysFileTime) {
        int size0 = Tools.textLength(sysFileTime);
        if (size0 > 19) {
            throw new RuntimeException("SysFile.sysFileTime最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysFileTime);
        }
        this.sysFileTime = sysFileTime;
        return this;
    }
    public SysFile setSysFileTimeUnsafe(java.lang.String sysFileTime) {
        this.sysFileTime = sysFileTime;
        return this;
    }
    public java.lang.String getSysFileType() {
        return sysFileType;
    }
    public SysFile setSysFileType(java.lang.String sysFileType) {
        int size0 = Tools.textLength(sysFileType);
        if (size0 > 100) {
            throw new RuntimeException("SysFile.sysFileType最大长度为:100,实际长度为:"+ size0 +",数据为:"+sysFileType);
        }
        this.sysFileType = sysFileType;
        return this;
    }
    public SysFile setSysFileTypeUnsafe(java.lang.String sysFileType) {
        this.sysFileType = sysFileType;
        return this;
    }
    public SysFile setList(List<?> data) {
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

    public SysFile setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysFile putMap(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        ((Map<String, Object>)this.data).put(key, value);
        return this;
    }

    public Object getData() {
        return data;
    }

    public SysFile setData(Object data) {
        this.data = data;
        return this;
    }}
