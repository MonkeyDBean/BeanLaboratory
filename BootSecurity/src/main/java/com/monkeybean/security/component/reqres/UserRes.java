package com.monkeybean.security.component.reqres;

/**
 * Created by MonkeyBean on 2019/4/18.
 */
public class UserRes {
    private String userName;
    private String phone;
    private String email;
    private Boolean isDatabase;
    private Boolean enabled;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getDatabase() {
        return isDatabase;
    }

    public void setDatabase(Boolean database) {
        isDatabase = database;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
