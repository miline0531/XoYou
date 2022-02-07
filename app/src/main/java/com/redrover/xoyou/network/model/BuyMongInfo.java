package com.redrover.xoyou.network.model;

public class BuyMongInfo {
    int mong_srl;
    String user_name;


    public BuyMongInfo(int mong_srl, String user_name) {
        this.mong_srl = mong_srl;
        this.user_name = user_name;
    }

    public int getMong_srl() {
        return mong_srl;
    }

    public void setMong_srl(int mong_srl) {
        this.mong_srl = mong_srl;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
