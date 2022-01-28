package kr.co.genericit.mybase.xoyou2.network.model;

public class SignUpInfo {
    String type;
    String user_id;
    String user_name;
    String user_birth;
    String user_pw;
    String user_email;
    String user_join_type;
    String search_id;
    int user_terms_need;
    int user_privacy_need;
    int user_privacy_optional;
    int user_ad_optional;
    int user_gender;

    public SignUpInfo(String type,String search_id){
        this.type = type;
        this.search_id = search_id;
    }

    public SignUpInfo(String type,
                      String user_id,
                      String user_name,
                      String user_birth,
                      String user_pw,
                      String user_email,
                      String user_join_type,
                      int user_terms_need,
                      int user_privacy_need,
                      int user_privacy_optional,
                      int user_ad_optional,
                      int user_gender) {
        this.type = type;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_birth = user_birth;
        this.user_pw = user_pw;
        this.user_email = user_email;
        this.user_join_type = user_join_type;
        this.user_terms_need = user_terms_need;
        this.user_privacy_need = user_privacy_need;
        this.user_privacy_optional = user_privacy_optional;
        this.user_ad_optional = user_ad_optional;
        this.user_gender = user_gender;
    }

    public String getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_birth() {
        return user_birth;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_join_type() {
        return user_join_type;
    }

    public int getUser_terms_need() {
        return user_terms_need;
    }

    public int getUser_privacy_need() {
        return user_privacy_need;
    }

    public int getUser_privacy_optional() {
        return user_privacy_optional;
    }

    public int getUser_ad_optional() {
        return user_ad_optional;
    }

    public int getUser_gender() {
        return user_gender;
    }

    public String getSearch_id() { return search_id; }
}
