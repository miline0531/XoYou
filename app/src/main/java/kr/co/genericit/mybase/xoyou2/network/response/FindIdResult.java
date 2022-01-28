package kr.co.genericit.mybase.xoyou2.network.response;

import com.google.gson.annotations.SerializedName;

public class FindIdResult {
    boolean result;
    String msg;

    @SerializedName("resp")
    Resp resp;

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public class Resp{
        String u_id;

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }
    }
}

