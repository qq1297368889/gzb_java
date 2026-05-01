package com.system.entity;

public class GzbUser {
    private Long gzbUserId;
    private String gzbUserAcc;
    private String gzbUserPwd;
    private Integer gzbUserState;
    private String gzbUserNike;
public GzbUser(){}
    public GzbUser(Long gzbUserId, String gzbUserAcc, String gzbUserPwd, Integer gzbUserState, String gzbUserNike) {
        this.gzbUserId = gzbUserId;
        this.gzbUserAcc = gzbUserAcc;
        this.gzbUserPwd = gzbUserPwd;
        this.gzbUserState = gzbUserState;
        this.gzbUserNike = gzbUserNike;
    }

    public Long getGzbUserId() {
        return gzbUserId;
    }

    public void setGzbUserId(Long gzbUserId) {
        this.gzbUserId = gzbUserId;
    }

    public String getGzbUserAcc() {
        return gzbUserAcc;
    }

    public void setGzbUserAcc(String gzbUserAcc) {
        this.gzbUserAcc = gzbUserAcc;
    }

    public String getGzbUserPwd() {
        return gzbUserPwd;
    }

    public void setGzbUserPwd(String gzbUserPwd) {
        this.gzbUserPwd = gzbUserPwd;
    }

    public Integer getGzbUserState() {
        return gzbUserState;
    }

    public void setGzbUserState(Integer gzbUserState) {
        this.gzbUserState = gzbUserState;
    }

    public String getGzbUserNike() {
        return gzbUserNike;
    }

    public void setGzbUserNike(String gzbUserNike) {
        this.gzbUserNike = gzbUserNike;
    }
}
