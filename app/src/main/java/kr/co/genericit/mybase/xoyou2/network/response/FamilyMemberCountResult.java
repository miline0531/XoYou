package kr.co.genericit.mybase.xoyou2.network.response;

public class FamilyMemberCountResult {
    String code;
    String msg;
    Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        boolean result;
        String code;
        int dataCnt;
        String message;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getDataCnt() {
            return dataCnt;
        }

        public void setDataCnt(int dataCnt) {
            this.dataCnt = dataCnt;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
