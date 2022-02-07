package com.redrover.xoyou.network.model;

public class BidMongInfo {
    int bid_value;
    int mong_srl;

    public BidMongInfo(int bid_value, int mong_srl) {
        this.bid_value = bid_value;
        this.mong_srl = mong_srl;
    }

    public int getBid_value() {
        return bid_value;
    }

    public void setBid_value(int bid_value) {
        this.bid_value = bid_value;
    }

    public int getMong_srl() {
        return mong_srl;
    }

    public void setMong_srl(int mong_srl) {
        this.mong_srl = mong_srl;
    }

}
