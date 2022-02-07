package com.redrover.xoyou.network.model;

import com.google.gson.annotations.SerializedName;

public class DeleteUserInfo {
    @SerializedName("us")
    public TEMP_DATA us;

    public DeleteUserInfo(String SEQ) {
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
