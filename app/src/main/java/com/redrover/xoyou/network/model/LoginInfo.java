package com.redrover.xoyou.network.model;

public class LoginInfo {
    String user_id;
    String user_pw;

    public LoginInfo(String user_id, String user_pw) {
        this.user_id = user_id;
        this.user_pw = user_pw;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }
}
