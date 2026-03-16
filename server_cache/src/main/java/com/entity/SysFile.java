package com.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gzb.tools.*;

public class SysFile {
    private java.lang.Long sysFileId;
    private java.lang.String sysFilePath;
    private java.lang.String sysFileMd5;
    private java.time.LocalDateTime sysFileTime;
    private java.lang.String sysFileType;
    private Object data;

    public SysFile() {
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
            throw new gzb.exception.GzbException0("SysFile.sysFilePath最大长度为:100,实际长度为:" + size0 + ",数据为:" + sysFilePath);
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
            throw new gzb.exception.GzbException0("SysFile.sysFileMd5最大长度为:64,实际长度为:" + size0 + ",数据为:" + sysFileMd5);
        }
        this.sysFileMd5 = sysFileMd5;
        return this;
    }

    public SysFile setSysFileMd5Unsafe(java.lang.String sysFileMd5) {
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

    public java.lang.String getSysFileType() {
        return sysFileType;
    }

    public SysFile setSysFileType(java.lang.String sysFileType) {
        int size0 = Tools.textLength(sysFileType);
        if (size0 > 100) {
            throw new gzb.exception.GzbException0("SysFile.sysFileType最大长度为:100,实际长度为:" + size0 + ",数据为:" + sysFileType);
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
