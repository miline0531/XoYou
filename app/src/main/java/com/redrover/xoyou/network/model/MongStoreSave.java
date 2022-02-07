package com.redrover.xoyou.network.model;

public class MongStoreSave {
    private String userId;
    private String userSrl;
    private String mongSeq;

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

    public String getMongSeq() {
        return mongSeq;
    }

    public void setMongSeq(String mongSeq) {
        this.mongSeq = mongSeq;
    }
}
