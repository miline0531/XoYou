package com.redrover.xoyou.network.model;

public class DeleteLocationInfo {
    TEMP_DATA us;


    public DeleteLocationInfo(String SEQ){
        us = new TEMP_DATA(SEQ);
    }

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String SEQ;

        public TEMP_DATA(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }
    }

}
