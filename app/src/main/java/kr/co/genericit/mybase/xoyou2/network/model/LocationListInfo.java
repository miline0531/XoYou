package kr.co.genericit.mybase.xoyou2.network.model;

public class LocationListInfo {
    TEMP_DATA us;


    public LocationListInfo(String USER_ID, String CURRENT_PAGE) {
        us = new TEMP_DATA(USER_ID,CURRENT_PAGE);
    }

    public TEMP_DATA getUs() {
        return us;
    }

    public void setUs(TEMP_DATA us) {
        this.us = us;
    }

    public class TEMP_DATA{
        String USER_ID;
        String CURRENT_PAGE;

       public TEMP_DATA(String USER_ID, String CURRENT_PAGE) {
           this.USER_ID = USER_ID;
           this.CURRENT_PAGE = CURRENT_PAGE;
       }

       public String getUSER_ID() {
           return USER_ID;
       }

       public void setUSER_ID(String USER_ID) {
           this.USER_ID = USER_ID;
       }

       public String getCURRENT_PAGE() {
           return CURRENT_PAGE;
       }

       public void setCURRENT_PAGE(String CURRENT_PAGE) {
           this.CURRENT_PAGE = CURRENT_PAGE;
       }
   }
}
