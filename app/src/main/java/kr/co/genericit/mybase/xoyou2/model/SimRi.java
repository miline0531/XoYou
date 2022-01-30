package kr.co.genericit.mybase.xoyou2.model;

import java.io.Serializable;

public class SimRi implements Serializable {

    int Id;
    int No;
    String NickName;
    String Name;
    String UserInfo;
    String GwanInfo;
    String SimRiInfo;
    String Value;
    double iDou;
    boolean XO;

    public SimRi(int id, int no, String nickName, String name, String userInfo, String gwanInfo, String simRiInfo, String value, double iDou, boolean XO) {
        Id = id;
        No = no;
        NickName = nickName;
        Name = name;
        UserInfo = userInfo;
        GwanInfo = gwanInfo;
        SimRiInfo = simRiInfo;
        Value = value;
        this.iDou = iDou;
        this.XO = XO;
    }

    public SimRi(){
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(String userInfo) {
        UserInfo = userInfo;
    }

    public String getGwanInfo() {
        return GwanInfo;
    }

    public void setGwanInfo(String gwanInfo) {
        GwanInfo = gwanInfo;
    }

    public String getSimRiInfo() {
        return SimRiInfo;
    }

    public void setSimRiInfo(String simRiInfo) {
        SimRiInfo = simRiInfo;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public double getiDou() {
        return iDou;
    }

    public void setiDou(double iDou) {
        this.iDou = iDou;
    }

    public boolean isXO() {
        return XO;
    }

    public void setXO(boolean XO) {
        this.XO = XO;
    }
}
