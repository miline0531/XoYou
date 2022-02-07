package com.redrover.xoyou.model;

public class LocationListData {
    private int id;
    private String title;
    private String sub_title;
    private String floor;
    private String addr;

    public LocationListData(int id, String title, String sub_title, String floor, String addr) {
        this.id = id;
        this.title = title;
        this.sub_title = sub_title;
        this.floor = floor;
        this.addr = addr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
