package kr.co.genericit.mybase.xoyou2.network.model;

public class DeleteRelationInfo {
    TEMP_DATA us;


    public DeleteRelationInfo(String SEQ) {
        this.us = new TEMP_DATA(SEQ);
    }

    public class TEMP_DATA{
        String SEQ;

        public TEMP_DATA(String SEQ) {
            this.SEQ = SEQ;
        }

        public String getSEQ() {
            return SEQ;
        }

        public void setSEQ(String SEQ) {
            this.SEQ = SEQ;
        }
    }
}
