package com.redrover.xoyou.network.model;

public class LocationRegist {
    String loca_relationship;
    String loca_name;
    String loca_address;
    String loca_floor;

    public LocationRegist(String loca_relationship, String loca_name, String loca_address, String loca_floor) {
        this.loca_relationship = loca_relationship;
        this.loca_name = loca_name;
        this.loca_address = loca_address;
        this.loca_floor = loca_floor;
    }

    public String getLoca_relationship() {
        return loca_relationship;
    }

    public void setLoca_relationship(String loca_relationship) {
        this.loca_relationship = loca_relationship;
    }

    public String getLoca_name() {
        return loca_name;
    }

    public void setLoca_name(String loca_name) {
        this.loca_name = loca_name;
    }

    public String getLoca_address() {
        return loca_address;
    }

    public void setLoca_address(String loca_address) {
        this.loca_address = loca_address;
    }

    public String getLoca_floor() {
        return loca_floor;
    }

    public void setLoca_floor(String loca_floor) {
        this.loca_floor = loca_floor;
    }
}

