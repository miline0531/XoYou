package com.redrover.xoyou.network.response;

public class UserListResult {
    private boolean success;
    private int error;
    private String message;
   /*@SerializedName("data")
    private ArrayList<User> data = new ArrayList<>();*/
    private String data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
/*

    public ArrayList<User> getData() {
        return data;
    }

    public void setData(ArrayList<User> data) {
        this.data = data;
    }
*/

      public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
