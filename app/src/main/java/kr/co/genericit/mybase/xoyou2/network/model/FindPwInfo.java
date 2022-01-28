package kr.co.genericit.mybase.xoyou2.network.model;

public class FindPwInfo {
    String user_id;
    String user_email;
    String user_pw;

    public FindPwInfo(String user_id, String user_email, String user_pw) {
        this.user_id = user_id;
        this.user_email = user_email;
        this.user_pw = user_pw;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_pw() {
        return user_pw;
    }
}
