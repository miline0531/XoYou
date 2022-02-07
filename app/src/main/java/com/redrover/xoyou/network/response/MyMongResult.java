package com.redrover.xoyou.network.response;

public class MyMongResult {
    boolean success;
    int error;
    String message;
    String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }






}
/*
{\"SEQ\":0,
\"USER_ID\":null,
\"TITLE\":null,
\"NOTE\":null,
\"STATUS_CODE\":0,
\"STORE_ID\":\"389\",
\"SELL_TYPE\":null,
\"BUY_TYPE\":null,
\"MONG_TYPE\":null,
\"THEME_TYPE\":null,
\"START_DATE\":\"2022-01-08 오전 5:53:09\",
\"END_DATE\":\"2022-01-08 오전 5:53:09\",
\"MAX_PRICE\":36.0,
\"MIN_PRICE\":0.0,
\"MONG_PRICE\":2.0,
\"BID_VALUE\":0.0,
\"BID_COUNT\":0.0,
\"MONG_DATE\":null,
\"IMAGE_URL\":\"https://soulway-upload-2.s3.ap-southeast-1.amazonaws.com/soulway_1639992675580_imgBig7.png\",
\"JUSEO_SEQ\":0,
\"USE_YN\":null,
\"REG_DATE\":\"2022-01-08 14:52:18\",
\"UPD_DATE\":null,
\"JUSEO_DATA\":null,
\"MONG_JST\":0.0,
\"VIEW_TYPE\":null,
\"CURRENT_PAGE\":0}
*/
