package com.redrover.xoyou.model;


public class CheckFamilyData {
    private int id;
    private boolean check_family;
    private String txt_family;

    private String info1;
    private String info2;


    public CheckFamilyData(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck_family() {
        return check_family;
    }

    public void setCheck_family(boolean check_family) {
        this.check_family = check_family;
    }

    public String getTxt_family() {
        return txt_family;
    }

    public void setTxt_family(String txt_family) {
        this.txt_family = txt_family;
    }

    public String getInfo1() {
        return info1;
    }

    public void setInfo1(String info1) {
        this.info1 = info1;
    }

    public String getInfo2() {
        return info2;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }
}