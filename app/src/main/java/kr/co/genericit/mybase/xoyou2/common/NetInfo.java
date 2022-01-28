package kr.co.genericit.mybase.xoyou2.common;

/**
 * 공통 API 및 URL 정의 Class
 */
public class NetInfo {
    // PRD
//    public static String SERVER_MAIN_URL = "https://54.238.58.145:3005";
    // DEV
//    public static String SERVER_MAIN_URL = "http://api.soulway.world:3001";
    public static String SERVER_MAIN_URL = "http://api.soulway.world:3100/";
    public static String SERVER_VERSION_CHECK_URL = "http://35.72.101.26/";
    public static String SERVER_RELATION_URL = "http://35.72.101.26/";
    public static String SERVER_BASE_URL = "http://15.165.134.221:7081/";
    public static String SERVER_BASE_URL2 = "http://15.165.134.221:7081/";
//    public static String SERVER_BASE_URL = "http://192.168.0.55:8081/";
//    public static String SERVER_BASE_URL2 = "http://192.168.0.55:8081/";
    public static String SERVER_DOWNLOAD_URL = "http://35.72.101.26/dist/apksvr/";


    public static final String API_SUB_URL = "";

    public static final String URL_GLOBAL_IMAGE_PATH = SERVER_MAIN_URL + API_SUB_URL + "/thumb/"; // 이미지 Path

    // 통신 기본 Timeout
    public static final int DEFAULT_TIMEOUT = 15;

    // API
    public static final String API_VERSION_CHECK = "/dist/apksvr/verchk"; // 버전체크

    public static final String API_LOGIN_URL = "/user/login"; // 로그인
    public static final String API_VARIFY_EMAIL = "/etc/varify_email"; // 이메일 확인
    public static final String API_FIND_ID = "/user/login/find_id"; // 아이디 찾기
    public static final String API_FIND_PW = "/user/login/change_pw"; // 비밀번호 찾기
    public static final String API_SIGN_UP = "/user/join"; // 회원가입



    public static final String API_SET_LOCATION = "/api/locations/create"; // 관계장소 설정

    public static final String API_FAMILYMEMBER_LIST = "/api/familymember/list"; //가족구성 리스트
    public static final String API_FAMILYMEMBER_UPDATE = "/api/familymember/update"; //가족구성 리스트
    public static final String API_FAMILYMEMBER_COUNT = "/api/familymember/getDataCnt"; //가족구성수



    //코인
    public static final String API_COIN_DATA = "/mongapp/store/stat_info"; // 코인정보


   //Manage
    public static final String API_MANAGE_MONGLIST = "/v2/myMongStoreMyList"; // 내 꿈 리스트

    //Store
    public static final String API_STORE_LIST= "/v2/myMongStoreStoreList"; // 스토어 꿈 리스트
    public static final String API_GET_MONG= "/v2/myMongStoreGetMong"; // 꿈 상세보기
    public static final String API_BUY_MONG= "/store_api/mong/buy"; // 꿈 구매하기
    public static final String API_BID_MONG= "/store_api/mong/bid"; // 꿈 입찰하기
    public static final String API_SELL_MONG= "/store_api/transaction_mong/create_mong_transaction"; // 꿈 팔기

    //사용자
    public static final String API_MAIN_USER_INFO = "/v2/myMongMainLoad"; //메인 리스트
    public static final String API_ADD_USER = "/v2/in/c_in"; //사용자 추가
    public static final String API_USER_LIST = "/v2/in/l_in"; //사용자 리스트
    public static final String API_DELETE_USER = "/v2/in/d_in"; //사용자 삭제
    public static final String API_UPDATE_USER = "/v2/in/u_in"; //사용자 수정

    //장소
    public static final String API_ADD_LOCATION = "/v2/te/c_te"; //장소 추가
    public static final String API_LOCATION_LIST = "/v2/te/l_te"; //장소 리스트
    public static final String API_DELETE_LOCATION = "/v2/te/d_te"; //장소 삭제
    public static final String API_UADATE_LOCATION = "/v2/te/u_te"; // 장소 업데이트

    //관계인
    public static final String API_ADD_RELATION = "/v2/yu/c_yu"; // 관계인 설정
    public static final String API_DELETE_RELATION = "/v2/yu/d_yu"; // 관계인 삭제
    public static final String API_RELATION_LIST = "/v2/yu/l_yu"; //관계 리스트
    public static final String API_UPDATE_RELATION = "/v2/yu/u_yu"; // 관계인 업데이트

    //몽 꿈분석
    public static final String API_SELECT_BOX_DATA1 = "/v2/myMongKeywordDaeMong"; // 대분류 조회
    public static final String API_SELECT_BOX_DATA2 = "/v2/myMongKeywordSoMong"; // 중분류 조회
    public static final String API_SELECT_BOX_DATA3 = "/v2/myMongKeywordWhatMong"; // 소분류 조회
    public static final String API_SELECT_BOX_DATA4 = "/v2/myMongKeywordPlayMong"; // 세분류 조회
    public static final String API_SELECT_BOX_DATA_PLAY = "/v2/myMongKeywordValueMong"; // 조회
    public static final String API_SEARCH_STORY_PLAY = "/v2/myMongStoryGetMong"; // 조회
    public static final String API_DETAIL_DATE_CHART = "/v2/myMongStoryGetDateUn"; // 꿈분석 적용기간 그래프
    public static final String API_DETAIL_LOCATION_COLOR = "/v2/myMongStoryGetLocationColor"; // 장소분석 그래프
    public static final String API_GET_REAL_UN = "/v2/GetRealUn"; // 나의운 데이터

    public static final String API_SELECT_MONG_STORE_LOAD = "/v2/myMongStoreLoad"; // 인증등록 셀렉트 메뉴
    public static final String API_SELECT_MONG_STORE_PRICE = "/v2/myMongStorePrice"; //
    public static final String API_SELECT_MONG_STORE_SAVE = "/v2/myMongStoreSave"; //
    public static final String API_CARD_IMAGE_UPLOAD = "/upload/single_file"; // 이미지업로드
    public static final String API_UPDATE_CERTIFY = "/mong/update_certify"; // 이미지업로드

    //xoYou
    public static final String API_SELECT_USER_LIST = "/v2/xoYouUserLoad"; // 나를위한사람들-관계인리스트
    public static final String API_SELECT_QA_LIST = "/v2/xoYouUserQAList"; // 나를위한사람들-질문항목리스트
    public static final String API_SELECT_QA_SIMRI_LIST = "/v2/xoYouUserQASimRiList"; // 나를위한사람들-질문선택 인연순리스트
    public static final String API_SELECT_QA_SIMRI_DETAIL = "/v2/xoYouUserQASimRiDetail"; //나를위한사람들-관계인 질문항목분석
    public static final String API_SELECT_SIMRI_MESSAGE = "/v2/xoYouUserSimRiMessage"; // 나를위한사람들-메시지분석

    public static final String API_SELECT_WE_LIST = "/v2/xoYouWeLoad"; // 함께라면-좋은운/나쁜운 항목별순위리스트
    public static final String API_SELECT_WE_UN_LIST = "/v2/xoYouWeYouUnDataList"; // 함께라면-선택운 관계인순위리스트
    public static final String API_SELECT_WE_UN_DETAIL = "/v2/xoYouWeYouUnDataDetail"; // 함께라면- 관계인 관계분석

    public static final String API_SELECT_MEET_LIST = "/v2/xoYouMeetLoad"; // 나만을위한약속- 강한약속/약한약속순위리스트
    public static final String API_SELECT_MEET_NAME_LIST = "/v2/xoYouMeetNameList"; // 나만을위한약속- 약속항목 리스트
    public static final String API_SELECT_MEET_YOU_MEET_LIST = "/v2/xoYouMeetYouMeetDataList"; // 나만을위한약속- 선택약속 관계인순위리스트

    }
