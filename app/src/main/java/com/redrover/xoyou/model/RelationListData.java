package com.redrover.xoyou.model;

public class RelationListData {
    private int id;
    private String relationship;
    private String firstName;
    private String lastName;
    private String birth;
    private String info;


    public RelationListData(int id,String relationship,String firstName, String lastname,String birth, String info) {
        this.id = id;
        this.relationship = relationship;
        this.firstName = firstName;
        this.lastName = lastname;
        this.birth = birth;
        this.info = info;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
