package com.redrover.xoyou.network.model;

public class MongStorePrice {

    private String userId;
    private String seq;
    private String MongStoreColor;
    private String MongStoreSoRi;
    //private String MongStoreyear;
    private String MongStorePyeongGa;

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

    public String getMongStoreColor() {
        return MongStoreColor;
    }

    public void setMongStoreColor(String mongStoreColor) {
        MongStoreColor = mongStoreColor;
    }

    public String getMongStoreSoRi() {
        return MongStoreSoRi;
    }

    public void setMongStoreSoRi(String mongStoreSoRi) {
        MongStoreSoRi = mongStoreSoRi;
    }

   /* public String getMongStoreyear() {
        return MongStoreyear;
    }

    public void setMongStoreyear(String mongStoreyear) {
        MongStoreyear = mongStoreyear;
    }*/

    public String getMongStorePyeongGa() {
        return MongStorePyeongGa;
    }

    public void setMongStorePyeongGa(String mongStorePyeongGa) {
        MongStorePyeongGa = mongStorePyeongGa;
    }
}
