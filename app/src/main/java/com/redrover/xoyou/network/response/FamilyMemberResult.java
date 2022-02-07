package com.redrover.xoyou.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FamilyMemberResult {
    String code;
    String msg;

    @SerializedName("data")
    Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        boolean result;
        String code;

        @SerializedName("dataList")
        ArrayList<FamilyInfoList> dataList;

        String message;


        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public ArrayList<FamilyInfoList> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<FamilyInfoList> dataList) {
            this.dataList = dataList;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public class FamilyInfoList{
            ArrayList<FamilyInfo> afterMarry;
            ArrayList<FamilyInfo> family;
            ArrayList<FamilyInfo> motherFamily;
            ArrayList<FamilyInfo> job;
            ArrayList<FamilyInfo> fatherFamily;

            public ArrayList<FamilyInfo> getAfterMarry() {
                return afterMarry;
            }

            public void setAfterMarry(ArrayList<FamilyInfo> afterMarry) {
                this.afterMarry = afterMarry;
            }

            public ArrayList<FamilyInfo> getFamily() {
                return family;
            }

            public void setFamily(ArrayList<FamilyInfo> family) {
                this.family = family;
            }

            public ArrayList<FamilyInfo> getMotherFamily() {
                return motherFamily;
            }

            public void setMotherFamily(ArrayList<FamilyInfo> motherFamily) {
                this.motherFamily = motherFamily;
            }

            public ArrayList<FamilyInfo> getJob() {
                return job;
            }

            public void setJob(ArrayList<FamilyInfo> job) {
                this.job = job;
            }

            public ArrayList<FamilyInfo> getFatherFamily() {
                return fatherFamily;
            }

            public void setFatherFamily(ArrayList<FamilyInfo> fatherFamily) {
                this.fatherFamily = fatherFamily;
            }
        }
        public class FamilyInfo{
            String familyCode;
            String familyName;
            String familyYn;


            public FamilyInfo(String familyCode, String familyName, String familyYn) {
                this.familyCode = familyCode;
                this.familyName = familyName;
                this.familyYn = familyYn;
            }

            public String getFamilyCode() {
                return familyCode;
            }

            public void setFamilyCode(String familyCode) {
                this.familyCode = familyCode;
            }

            public String getFamilyName() {
                return familyName;
            }

            public void setFamilyName(String familyName) {
                this.familyName = familyName;
            }

            public String getFamilyYn() {
                return familyYn;
            }

            public void setFamilyYn(String familyYn) {
                this.familyYn = familyYn;
            }
        }

    }
}
