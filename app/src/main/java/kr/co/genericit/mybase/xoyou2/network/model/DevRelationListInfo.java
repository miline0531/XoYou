package kr.co.genericit.mybase.xoyou2.network.model;

public class DevRelationListInfo {

    US us;

    public DevRelationListInfo(US us) {
        this.us = us;
    }

    public US getUs() {
        return us;
    }

    public void setUs(US us) {
        this.us = us;
    }

    public class US{
        int USER_ID;
        int CURRENT_PAGE;

        public US(int USER_ID, int CURRENT_PAGE) {
            this.USER_ID = USER_ID;
            this.CURRENT_PAGE = CURRENT_PAGE;
        }

        public int getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(int USER_ID) {
            this.USER_ID = USER_ID;
        }

        public int getCURRENT_PAGE() {
            return CURRENT_PAGE;
        }

        public void setCURRENT_PAGE(int CURRENT_PAGE) {
            this.CURRENT_PAGE = CURRENT_PAGE;
        }
    }
}
