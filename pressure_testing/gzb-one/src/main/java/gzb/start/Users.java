package gzb.start;

import java.time.LocalDateTime;

public class Users {
    Long usersId;
    String usersName;
    String usersPassword;
    String usersEmail;
    Integer usersAge;
    LocalDateTime usersTime;

    public Users() {
    }

    public Users(Long usersId, String usersName, String usersPassword, String usersEmail, Integer usersAge, LocalDateTime usersTime) {
        this.usersId = usersId;
        this.usersName = usersName;
        this.usersPassword = usersPassword;
        this.usersEmail = usersEmail;
        this.usersAge = usersAge;
        this.usersTime = usersTime;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getUsersName() {
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public String getUsersEmail() {
        return usersEmail;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
    }

    public Integer getUsersAge() {
        return usersAge;
    }

    public void setUsersAge(Integer usersAge) {
        this.usersAge = usersAge;
    }

    public LocalDateTime getUsersTime() {
        return usersTime;
    }

    public void setUsersTime(LocalDateTime usersTime) {
        this.usersTime = usersTime;
    }
}
