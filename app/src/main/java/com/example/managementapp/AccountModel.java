package com.example.managementapp;

public class AccountModel {

    private String userId;

    private String userName;
    private String adminid;
    private String groupname;

    public AccountModel(String userId, String userName, String adminid, String groupname) {
        this.userId = userId;
        this.userName = userName;
        this.adminid  = adminid;
        this.groupname = groupname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
