package com.redrover.xoyou.network.response;

import java.io.Serializable;

/**
 * Base Response
 */
public class BaseResponse implements Serializable {
    public boolean result;
    public String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public String getError() {
//        return error;
//    }
//
//    public void setError(String error) {
//        this.error = error;
//    }
}
