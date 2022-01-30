package kr.co.genericit.mybase.xoyou2.model;

public class WeListObj {
    String No;
    String UnName;
    String MaxName;
    String MinName;

    public WeListObj(String no, String unName, String maxName, String minName) {
        No = no;
        UnName = unName;
        MaxName = maxName;
        MinName = minName;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getUnName() {
        return UnName;
    }

    public void setUnName(String unName) {
        UnName = unName;
    }

    public String getMaxName() {
        return MaxName;
    }

    public void setMaxName(String maxName) {
        MaxName = maxName;
    }

    public String getMinName() {
        return MinName;
    }

    public void setMinName(String minName) {
        MinName = minName;
    }
}
