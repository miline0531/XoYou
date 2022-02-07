package com.redrover.xoyou.network.model;

public class UpdateRelationInfo {
    TEMP_DATA us;
    public UpdateRelationInfo(String IN_SEQ, String USER_ID, String GWANGYE, String NICK_NAME, String NAME, String MW, String BIRTH_DATE, String IMAGE_URL, String CALL_NUMBER) {
        this.us = new TEMP_DATA(IN_SEQ, USER_ID, GWANGYE, NICK_NAME, NAME, MW, BIRTH_DATE, IMAGE_URL, CALL_NUMBER);
    }


    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String SEQ;
        String IN_SEQ;
        String GWANGYE;
        String NICK_NAME;
        String NAME;
        String MW;
        String BIRTH_DATE;
        String IMAGE_URL;
        String CALL_NUMBER;

        public TEMP_DATA( String SEQ,String IN_SEQ, String GWANGYE, String NICK_NAME, String NAME, String MW, String BIRTH_DATE, String IMAGE_URL, String CALL_NUMBER) {
            this.SEQ = SEQ;
            this.IN_SEQ = IN_SEQ;
            this.GWANGYE = GWANGYE;
            this.NICK_NAME = NICK_NAME;
            this.NAME = NAME;
            this.MW = MW;
            this.BIRTH_DATE = BIRTH_DATE;
            this.IMAGE_URL = IMAGE_URL;
            this.CALL_NUMBER = CALL_NUMBER;
        }

        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getIN_SEQ() {
            return IN_SEQ;
        }

        public void setIN_SEQ(String IN_SEQ) {
            this.IN_SEQ = IN_SEQ;
        }

        public String getGWANGYE() {
            return GWANGYE;
        }

        public void setGWANGYE(String GWANGYE) {
            this.GWANGYE = GWANGYE;
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


        public String getCALL_NUMBER() {
            return CALL_NUMBER;
        }

        public void setCALL_NUMBER(String CALL_NUMBER) {
            this.CALL_NUMBER = CALL_NUMBER;
        }

    }
}
