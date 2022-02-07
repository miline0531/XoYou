package com.redrover.xoyou.view.bottomsheet.model;


import java.io.Serializable;

public class BottomSheetData implements Serializable {
    private int id;
    private String itemTitle;
    private boolean check = false;

    private String info1;
    private String info2;

    public BottomSheetData(){

    }

    public BottomSheetData(int id, String itemTitle, boolean check){
        this.id = id;
        this.itemTitle = itemTitle;
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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