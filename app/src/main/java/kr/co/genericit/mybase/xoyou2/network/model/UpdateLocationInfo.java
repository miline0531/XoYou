package kr.co.genericit.mybase.xoyou2.network.model;

import com.google.gson.annotations.SerializedName;

public class UpdateLocationInfo {
    @SerializedName("us")
    public TEMP_DATA us;

    public UpdateLocationInfo(String SEQ, String USER_ID, String GUBUN,String BZGUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR){
        this.us = new TEMP_DATA(SEQ, USER_ID, GUBUN, BZGUBUN, NAME, POST_CODE, ADDR1, ADDR2, LAT, LNG, FLOOR);
    }

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String SEQ;
        String USER_ID;
        String GUBUN;
        String BZGUBUN;
        String NAME;
        String POST_CODE;
        String ADDR1;
        String ADDR2;
        String LAT;
        String LNG;
        String FLOOR;

        public TEMP_DATA(String SEQ, String USER_ID, String GUBUN,String BZGUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR) {
            this.SEQ = SEQ;
            this.USER_ID = USER_ID;
            this.GUBUN = GUBUN;
            this.BZGUBUN = BZGUBUN;
            this.NAME = NAME;
            this.POST_CODE = POST_CODE;
            this.ADDR1 = ADDR1;
            this.ADDR2 = ADDR2;
            this.LAT = LAT;
            this.LNG = LNG;
            this.FLOOR = FLOOR;
        }

        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getGUBUN() {
            return GUBUN;
        }

        public void setGUBUN(String GUBUN) {
            this.GUBUN = GUBUN;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getPOST_CODE() {
            return POST_CODE;
        }

        public void setPOST_CODE(String POST_CODE) {
            this.POST_CODE = POST_CODE;
        }

        public String getADDR1() {
            return ADDR1;
        }

        public void setADDR1(String ADDR1) {
            this.ADDR1 = ADDR1;
        }

        public String getADDR2() {
            return ADDR2;
        }

        public void setADDR2(String ADDR2) {
            this.ADDR2 = ADDR2;
        }

        public String getLAT() {
            return LAT;
        }

        public void setLAT(String LAT) {
            this.LAT = LAT;
        }

        public String getLNG() {
            return LNG;
        }

        public void setLNG(String LNG) {
            this.LNG = LNG;
        }

        public String getFLOOR() {
            return FLOOR;
        }

        public void setFLOOR(String FLOOR) {
            this.FLOOR = FLOOR;
        }


        @Override
        public String toString() {
            return "TEMP_DATA{" +
                    "SEQ='" + SEQ + '\'' +
                    ", USER_ID='" + USER_ID + '\'' +
                    ", GUBUN='" + GUBUN + '\'' +
                    ", BZGUBUN='" + BZGUBUN + '\'' +
                    ", NAME='" + NAME + '\'' +
                    ", POST_CODE='" + POST_CODE + '\'' +
                    ", ADDR1='" + ADDR1 + '\'' +
                    ", ADDR2='" + ADDR2 + '\'' +
                    ", LAT='" + LAT + '\'' +
                    ", LNG='" + LNG + '\'' +
                    ", FLOOR='" + FLOOR + '\'' +
                    '}';
        }
    }
}
