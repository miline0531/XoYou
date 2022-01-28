package kr.co.genericit.mybase.xoyou2.common;


import org.json.JSONObject;

public class Constants {
    public static final String APP_VERSION = "0.11";

    //계정사용자 정보
    public static String RootSEQ = "";
    public static String RootNAME = "";
    public static String RootNICK_NAME = "";
    public static String RootBIRTH = "";
    public static int RootMW = 1;

    public static final boolean isBioMetric = false;

    public static String myCoinValue = "0";

    public static String CardImageName = "mymong_card";
    public static byte[] cardImageData;
    public static String MONG_SRL = "";
    public static JSONObject StoryJob;
    public static JSONObject MainData;
    public static String mongSEQ = "";

    public static String mongTitle = "";
    public static String mongNote = "";

    public static int MongCardPickIndex = -1;
    public static String MongCardPickText = "";

    public static void setRootUserInit(){
        RootSEQ = "";
        RootNAME = "";
        RootNICK_NAME = "";
        RootBIRTH = "";
        RootMW = 1;
    }
    //해소추천 초기화
    public static void setRandomCardPickInit(){
        MongCardPickIndex = -1;
        MongCardPickText = "";
    }


}
