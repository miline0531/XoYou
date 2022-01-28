package kr.co.genericit.mybase.xoyou2.network.response;


public class LoginResult extends BaseResponse  {
    private loginData resp;

    public loginData getData() {
        return resp;
    }

    public void setData(loginData data) {
        this.resp = data;
    }

    public class loginData {
        int u_srl;
        String u_id;
        String u_name;
        String u_email;
        String u_birth;
        String u_phone;
        int u_gender;
        int u_type;
        String access_token;
        boolean u_pro;

        public int getU_srl() {
            return u_srl;
        }

        public void setU_srl(int u_srl) {
            this.u_srl = u_srl;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getU_name() {
            return u_name;
        }

        public void setU_name(String u_name) {
            this.u_name = u_name;
        }

        public String getU_email() {
            return u_email;
        }

        public void setU_email(String u_email) {
            this.u_email = u_email;
        }

        public String getU_birth() {
            return u_birth;
        }

        public void setU_birth(String u_birth) {
            this.u_birth = u_birth;
        }

        public String getU_phone() {
            return u_phone;
        }

        public void setU_phone(String u_phone) {
            this.u_phone = u_phone;
        }

        public int getU_gender() {
            return u_gender;
        }

        public void setU_gender(int u_gender) {
            this.u_gender = u_gender;
        }

        public int getU_type() {
            return u_type;
        }

        public void setU_type(int u_type) {
            this.u_type = u_type;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public boolean isU_pro() {
            return u_pro;
        }

        public void setU_pro(boolean u_pro) {
            this.u_pro = u_pro;
        }
    }
}
