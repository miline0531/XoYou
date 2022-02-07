package com.redrover.xoyou.network.model;

import com.google.gson.annotations.SerializedName;

public class AddLocationInfo {
    @SerializedName("us")
    public TEMP_DATA us;

    public AddLocationInfo(String IN_SEQ, String USER_ID, String GUBUN,String BZGUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR){
        this.us = new TEMP_DATA(IN_SEQ, USER_ID, GUBUN, BZGUBUN, NAME, POST_CODE, ADDR1, ADDR2, LAT, LNG, FLOOR);
    }

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String IN_SEQ;
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

        public TEMP_DATA(String IN_SEQ, String USER_ID, String GUBUN,String BZGUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR) {
            this.IN_SEQ = IN_SEQ;
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

        public String getIN_SEQ() {
            return IN_SEQ;
        }

        public void setIN_SEQ(String IN_SEQ) {
            this.IN_SEQ = IN_SEQ;
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
    }

}
