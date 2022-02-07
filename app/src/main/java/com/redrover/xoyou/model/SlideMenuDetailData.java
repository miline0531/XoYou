package com.redrover.xoyou.model;


public class SlideMenuDetailData {
    private int id;
    private String txt_menu_title;
    private String txt_menu_sub_title;

    private String info1;
    private String info2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTxt_menu_title() {
        return txt_menu_title;
    }

    public void setTxt_menu_title(String txt_menu_title) {
        this.txt_menu_title = txt_menu_title;
    }

    public String getTxt_menu_sub_title() {
        return txt_menu_sub_title;
    }

    public void setTxt_menu_sub_title(String txt_menu_sub_title) {
        this.txt_menu_sub_title = txt_menu_sub_title;
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