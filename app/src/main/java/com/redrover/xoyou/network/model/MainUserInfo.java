package com.redrover.xoyou.network.model;

import com.google.gson.annotations.SerializedName;

public class MainUserInfo {
    @SerializedName("us")
    public TEMP_DATA us;

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(String USER_ID) {
        TEMP_DATA data = new TEMP_DATA();
        data.setUSER_ID(USER_ID);
        this.us = data;
    }

    public class TEMP_DATA{
        String USER_ID;

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
            this.USER_ID = USER_ID;
        }
    }
}

