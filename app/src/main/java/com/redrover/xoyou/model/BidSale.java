package com.redrover.xoyou.model;

public class BidSale {
    String MongName;
    String Base_iDou;
    String Hap_iDou;
    String Val_iDou;

    public String getMongName() {
        return MongName;
    }

    public void setMongName(String mongName) {
        MongName = mongName;
    }

    public String getBase_iDou() {
        return Base_iDou;
    }

    public void setBase_iDou(String base_iDou) {
        Base_iDou = base_iDou;
    }

    public String getHap_iDou() {
        return Hap_iDou;
    }

    public void setHap_iDou(String hap_iDou) {
        Hap_iDou = hap_iDou;
    }

    public String getVal_iDou() {
        return Val_iDou;
    }

    public void setVal_iDou(String val_iDou) {
        Val_iDou = val_iDou;
    }

    @Override
    public String toString() {
        return "BidSale{" +
                "MongName='" + MongName + '\'' +
                ", Base_iDou='" + Base_iDou + '\'' +
                ", Hap_iDou='" + Hap_iDou + '\'' +
                ", Val_iDou='" + Val_iDou + '\'' +
                '}';
    }
}
