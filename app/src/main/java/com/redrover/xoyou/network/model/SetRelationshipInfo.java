package com.redrover.xoyou.network.model;

public class SetRelationshipInfo {
    String nickName;
    String relationship;
    String firtsName;
    String lastName;
    String birth;
    int gender;

    public SetRelationshipInfo(String nickName, String relationship, String firtsName, String lastName, String birth, int gender) {
        this.nickName = nickName;
        this.relationship = relationship;
        this.firtsName = firtsName;
        this.lastName = lastName;
        this.birth = birth;
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getFirtsName() {
        return firtsName;
    }

    public void setFirtsName(String firtsName) {
        this.firtsName = firtsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
