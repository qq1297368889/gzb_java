package gzb.start.entity;
import gzb.tools.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import gzb.frame.annotation.EntityAttribute;

@EntityAttribute(name = "sys_users", desc = "sysUsers")
public class SysUsers implements Serializable{
    private static final long serialVersionUID = 1000L;
    @EntityAttribute(key = true, size = 19, name = "sys_users_id", desc = "sysUsersId", type = "java.lang.Long")
    private Long sysUsersId;
    @EntityAttribute(key = false, size = 32, name = "sys_users_acc", desc = "sysUsersAcc", type = "java.lang.String")
    private String sysUsersAcc;
    @EntityAttribute(key = false, size = 32, name = "sys_users_pwd", desc = "sysUsersPwd", type = "java.lang.String")
    private String sysUsersPwd;
    @EntityAttribute(key = false, size = 11, name = "sys_users_phone", desc = "sysUsersPhone", type = "java.lang.String")
    private String sysUsersPhone;
    @EntityAttribute(key = false, size = 100, name = "sys_users_open_id", desc = "sysUsersOpenId", type = "java.lang.String")
    private String sysUsersOpenId;
    @EntityAttribute(key = false, size = 19, name = "sys_users_status", desc = "sysUsersStatus", type = "java.lang.Long")
    private Long sysUsersStatus;
    @EntityAttribute(key = false, size = 19, name = "sys_users_type", desc = "sysUsersType", type = "java.lang.Long")
    private Long sysUsersType;
    @EntityAttribute(key = false, size = 19, name = "sys_users_reg_time", desc = "sysUsersRegTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersRegTime;
    @EntityAttribute(key = false, size = 19, name = "sys_users_start_time", desc = "sysUsersStartTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersStartTime;
    @EntityAttribute(key = false, size = 19, name = "sys_users_end_time", desc = "sysUsersEndTime", type = "java.time.LocalDateTime")
    private java.time.LocalDateTime sysUsersEndTime;
    @EntityAttribute(key = false, size = 19, name = "sys_users_price", desc = "sysUsersPrice", type = "java.lang.Long")
    private Long sysUsersPrice;
    @EntityAttribute(key = false, size = 255, name = "sys_users_desc", desc = "sysUsersDesc", type = "java.lang.String")
    private String sysUsersDesc;
    @EntityAttribute(key = false, size = 19, name = "sys_users_sup", desc = "sysUsersSup", type = "java.lang.Long")
    private Long sysUsersSup;
    @EntityAttribute(key = false, size = 255, name = "sys_users_mail", desc = "sysUsersMail", type = "java.lang.String")
    private String sysUsersMail;
    @EntityAttribute(key = false, size = 19, name = "sys_users_role", desc = "sysUsersRole", type = "java.lang.Long")
    private Long sysUsersRole;
    private Object data;

    public SysUsers() {
    } 

    public Long getSysUsersId() {
        return sysUsersId;
    }

    public SysUsers setSysUsersId(Long sysUsersId) {
        this.sysUsersId = sysUsersId;
        return this;
    }

    public String getSysUsersAcc() {
        return sysUsersAcc;
    }

    public SysUsers setSysUsersAcc(String sysUsersAcc) {
        this.sysUsersAcc = sysUsersAcc;
        return this;
    }

    public String getSysUsersPwd() {
        return sysUsersPwd;
    }

    public SysUsers setSysUsersPwd(String sysUsersPwd) {
        this.sysUsersPwd = sysUsersPwd;
        return this;
    }

    public String getSysUsersPhone() {
        return sysUsersPhone;
    }

    public SysUsers setSysUsersPhone(String sysUsersPhone) {
        this.sysUsersPhone = sysUsersPhone;
        return this;
    }

    public String getSysUsersOpenId() {
        return sysUsersOpenId;
    }

    public SysUsers setSysUsersOpenId(String sysUsersOpenId) {
        this.sysUsersOpenId = sysUsersOpenId;
        return this;
    }

    public Long getSysUsersStatus() {
        return sysUsersStatus;
    }

    public SysUsers setSysUsersStatus(Long sysUsersStatus) {
        this.sysUsersStatus = sysUsersStatus;
        return this;
    }

    public Long getSysUsersType() {
        return sysUsersType;
    }

    public SysUsers setSysUsersType(Long sysUsersType) {
        this.sysUsersType = sysUsersType;
        return this;
    }

    public java.time.LocalDateTime getSysUsersRegTime() {
        return sysUsersRegTime;
    }

    public SysUsers setSysUsersRegTime(java.time.LocalDateTime sysUsersRegTime) {
        this.sysUsersRegTime = sysUsersRegTime;
        return this;
    }

    public java.time.LocalDateTime getSysUsersStartTime() {
        return sysUsersStartTime;
    }

    public SysUsers setSysUsersStartTime(java.time.LocalDateTime sysUsersStartTime) {
        this.sysUsersStartTime = sysUsersStartTime;
        return this;
    }

    public java.time.LocalDateTime getSysUsersEndTime() {
        return sysUsersEndTime;
    }

    public SysUsers setSysUsersEndTime(java.time.LocalDateTime sysUsersEndTime) {
        this.sysUsersEndTime = sysUsersEndTime;
        return this;
    }

    public Long getSysUsersPrice() {
        return sysUsersPrice;
    }

    public SysUsers setSysUsersPrice(Long sysUsersPrice) {
        this.sysUsersPrice = sysUsersPrice;
        return this;
    }

    public String getSysUsersDesc() {
        return sysUsersDesc;
    }

    public SysUsers setSysUsersDesc(String sysUsersDesc) {
        this.sysUsersDesc = sysUsersDesc;
        return this;
    }

    public Long getSysUsersSup() {
        return sysUsersSup;
    }

    public SysUsers setSysUsersSup(Long sysUsersSup) {
        this.sysUsersSup = sysUsersSup;
        return this;
    }

    public String getSysUsersMail() {
        return sysUsersMail;
    }

    public SysUsers setSysUsersMail(String sysUsersMail) {
        this.sysUsersMail = sysUsersMail;
        return this;
    }

    public Long getSysUsersRole() {
        return sysUsersRole;
    }

    public SysUsers setSysUsersRole(Long sysUsersRole) {
        this.sysUsersRole = sysUsersRole;
        return this;
    }

    public SysUsers setList(List<?> data) {
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

    public SysUsers setMap(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public SysUsers putMap(String key, Object value) {
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

    public SysUsers setData(Object data) {
        this.data = data;
        return this;
    }
}