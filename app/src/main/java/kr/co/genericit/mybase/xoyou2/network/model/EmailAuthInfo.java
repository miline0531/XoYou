package kr.co.genericit.mybase.xoyou2.network.model;

public class EmailAuthInfo {
    String type;
    String u_id;
    String verify_email;

    public EmailAuthInfo(String type, String u_id, String verify_email) {
        this.type = type;
        this.u_id = u_id;
        this.verify_email = verify_email;
    }

    public String getType() {
        return type;
    }

    public String getU_id() {
        return u_id;
    }

    public String getVerify_email() {
        return verify_email;
    }
}
