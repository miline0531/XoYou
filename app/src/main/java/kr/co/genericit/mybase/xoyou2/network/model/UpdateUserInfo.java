package kr.co.genericit.mybase.xoyou2.network.model;

import com.google.gson.annotations.SerializedName;

public class UpdateUserInfo {
    @SerializedName("us")
    public TEMP_DATA us;

    public UpdateUserInfo(String SEQ, String NICK_NAME, String NAME, String MW, String BIRTH_DATE, String IMAGE_URL){
        this.us = new TEMP_DATA(SEQ, NICK_NAME, NAME, MW, BIRTH_DATE, IMAGE_URL);
    }

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String SEQ;
        String NICK_NAME;
        String NAME;
        String MW;
        String BIRTH_DATE;
        String IMAGE_URL;

        public TEMP_DATA(String SEQ, String NICK_NAME, String NAME, String MW, String BIRTH_DATE, String IMAGE_URL) {
            this.SEQ = SEQ;
            this.NICK_NAME = NICK_NAME;
            this.NAME = NAME;
            this.MW = MW;
            this.BIRTH_DATE = BIRTH_DATE;
            this.IMAGE_URL = IMAGE_URL;
        }


        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getNICK_NAME() {
            return NICK_NAME;
        }

        public void setNICK_NAME(String NICK_NAME) {
            this.NICK_NAME = NICK_NAME;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getMW() {
            return MW;
        }

        public void setMW(String MW) {
            this.MW = MW;
        }

        public String getBIRTH_DATE() {
            return BIRTH_DATE;
        }

        public void setBIRTH_DATE(String BIRTH_DATE) {
            this.BIRTH_DATE = BIRTH_DATE;
        }

        public String getIMAGE_URL() {
            return IMAGE_URL;
        }

        public void setIMAGE_URL(String IMAGE_URL) {
            this.IMAGE_URL = IMAGE_URL;
        }
    }





}
