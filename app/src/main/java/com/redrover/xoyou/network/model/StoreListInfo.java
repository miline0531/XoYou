package com.redrover.xoyou.network.model;

public class StoreListInfo {
    String userId;
    String userSrl;

    public StoreListInfo(String userId, String userSrl) {
        this.userId = userId;
        this.userSrl = userSrl;
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
}

