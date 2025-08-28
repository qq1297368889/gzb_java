package com.frame.entity;
import com.frame.SqlTools;
import gzb.entity.SqlTemplate;
import gzb.tools.*;
import com.frame.dao.SysFileDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name="sys_file",desc="sysFile")
public class SysFile implements Serializable{
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
    private List<?> list;
    public SysFile() {}

    public SysFile(JSON gzbMap) {
        this(new GzbMap().setMap(gzbMap.map));
    }

    public SysFile(GzbMap gzbMap) {
        String str=null;
        str=gzbMap.getString("sysFileId");
        if (str!=null && !str.isEmpty()) {
            setSysFileId(java.lang.Long.valueOf(str));
        }
        str=gzbMap.getString("sysFilePath");
        if (str!=null && !str.isEmpty()) {
            setSysFilePath(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysFileMd5");
        if (str!=null && !str.isEmpty()) {
            setSysFileMd5(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysFileTime");
        if (str!=null && !str.isEmpty()) {
            setSysFileTime(java.lang.String.valueOf(str));
        }
        str=gzbMap.getString("sysFileType");
        if (str!=null && !str.isEmpty()) {
            setSysFileType(java.lang.String.valueOf(str));
        }
    }

    public SysFile(Map<String, Object> map) {
        this(new GzbMap().setMap(map));
    }

    public SysFile(String jsonString) {
        this(new GzbMap().setMap(new JSON().loadMap(jsonString).map));
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
        json.put("sysFileId", getSysFileId());
        json.put("sysFilePath", getSysFilePath());
        json.put("sysFileMd5", getSysFileMd5());
        json.put("sysFileTime", getSysFileTime());
        json.put("sysFileType", getSysFileType());
        json.put("data", getList());
        return json;
    }

    public java.lang.Long getSysFileId() {
        return sysFileId;
    }
    public SysFile setSysFileId(java.lang.Long sysFileId) {
        int size0 = Tools.textLength(sysFileId);
        if (size0 > 19) {
            throw new RuntimeException("SysFile.sysFileId最大长度为:19,实际长度为:"+ size0 +",数据为:"+sysFileId);
        }
        this.sysFileId = sysFileId;
        return this;
    }
    public SysFile setSysFileIdUnsafe(java.lang.Long sysFileId) {
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
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
