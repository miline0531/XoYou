package com.redrover.xoyou.network.response;

import java.io.Serializable;

/**
 * Base Response
 */
public class VersionCheckResponse implements Serializable {

    public String code;
    public String msg;
    public versionData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public versionData getData() {
        return data;
    }

    public void setData(versionData data) {
        this.data = data;
    }

    public class versionData{
        public String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
