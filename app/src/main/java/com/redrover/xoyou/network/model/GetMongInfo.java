package com.redrover.xoyou.network.model;

public class GetMongInfo {
    String userId;
    String userSrl;
    String seq;

    public GetMongInfo(String userId,String userSrl, String seq) {
        this.userId = userId;
        this.userSrl = userSrl;
        this.seq = seq;
    }

    public String getUserSrl() {
        return userSrl;
    }

    public void setUserSrl(String userSrl) {
        this.userSrl = userSrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}

