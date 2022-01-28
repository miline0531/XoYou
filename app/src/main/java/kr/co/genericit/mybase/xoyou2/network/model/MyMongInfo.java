package kr.co.genericit.mybase.xoyou2.network.model;

public class MyMongInfo {
    String userId;
    String viewType;
    String currentPage;
    String userSrl;

    public MyMongInfo(String userId,String userSrl, String viewType, String currentPage) {
        this.userId = userId;
        this.viewType = viewType;
        this.userSrl = userSrl;
        this.currentPage = currentPage;
    }

    public String getUserSrl() {
        return userSrl;
    }

    public void setUserSrl(String userSrl) {
        this.userSrl = userSrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }
}
