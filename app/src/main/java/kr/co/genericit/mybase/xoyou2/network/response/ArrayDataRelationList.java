package kr.co.genericit.mybase.xoyou2.network.response;

import java.util.ArrayList;

public class ArrayDataRelationList{
    boolean result;
    int totalCnt;
    int code;
    ArrayList<Data> dataList = new ArrayList<>();

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(int totalCnt) {
        this.totalCnt = totalCnt;
    }

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    public class Data{
        int relationshipId;
            String loginId;
            String userId;
            String nickName;
            String relationship;
            String firtsName;
            String lastName;
            String birth;
            int gender;
            String regDate;
            String updDate;
            String delYn;

            public String getLoginId() {
                return loginId;
            }

            public void setLoginId(String loginId) {
                this.loginId = loginId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
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

        public int getRelationshipId() {
            return relationshipId;
        }

        public void setRelationshipId(int relationshipId) {
            this.relationshipId = relationshipId;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getRegDate() {
                return regDate;
            }

            public void setRegDate(String regDate) {
                this.regDate = regDate;
            }

            public String getUpdDate() {
                return updDate;
            }

            public void setUpdDate(String updDate) {
                this.updDate = updDate;
            }

            public String getDelYn() {
                return delYn;
            }

            public void setDelYn(String delYn) {
                this.delYn = delYn;
            }
    }
}
