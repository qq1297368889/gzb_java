package gzb.start.entity;
import java.io.Serializable;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_file", desc = "sysFile")
public class SysFile implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_file_id", desc = "sysFileId", type = "java.lang.Long")
    private Long sysFileId;
    @EntityAttribute(key = false, size = 100, name = "sys_file_path", desc = "sysFilePath", type = "java.lang.String")
    private String sysFilePath;
    @EntityAttribute(key = false, size = 64, name = "sys_file_md5", desc = "sysFileMd5", type = "java.lang.String")
    private String sysFileMd5;
    @EntityAttribute(key = false, size = 19, name = "sys_file_time", desc = "sysFileTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysFileTime;
    @EntityAttribute(key = false, size = 100, name = "sys_file_type", desc = "sysFileType", type = "java.lang.String")
    private String sysFileType;
    private Object data;

    public SysFile() {
    } 

    public Long getSysFileId() {
        return sysFileId;
    }

    public SysFile setSysFileId(Long sysFileId) {
        this.sysFileId = sysFileId;
        return this;
    }

    public String getSysFilePath() {
        return sysFilePath;
    }

    public SysFile setSysFilePath(String sysFilePath) {
        this.sysFilePath = sysFilePath;
        return this;
    }

    public String getSysFileMd5() {
        return sysFileMd5;
    }

    public SysFile setSysFileMd5(String sysFileMd5) {
        this.sysFileMd5 = sysFileMd5;
        return this;
    }

    public java.time.LocalDateTime getSysFileTime() {
        return sysFileTime;
    }

    public SysFile setSysFileTime(java.time.LocalDateTime sysFileTime) {
        this.sysFileTime = sysFileTime;
        return this;
    }

    public String getSysFileType() {
        return sysFileType;
    }

    public SysFile setSysFileType(String sysFileType) {
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
            // 自动初始化
            this.data = new HashMap<>();
        } else if (!(this.data instanceof Map)) {
            // 无法转换，抛出业务异常
            throw new gzb.exception.GzbException0("无法将" + this.data + " 转换为MAP");
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

    public SysFile setData(Object data) {
        this.data = data;
        return this;
    }
}