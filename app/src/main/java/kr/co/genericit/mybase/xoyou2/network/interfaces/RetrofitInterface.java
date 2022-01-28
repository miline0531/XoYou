package kr.co.genericit.mybase.xoyou2.network.interfaces;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.model.AddLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.AddRelationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.AddUserInfo;
import kr.co.genericit.mybase.xoyou2.network.model.BidMongInfo;
import kr.co.genericit.mybase.xoyou2.network.model.BuyMongInfo;
import kr.co.genericit.mybase.xoyou2.network.model.DeleteLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.DeleteRelationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.DeleteUserInfo;
import kr.co.genericit.mybase.xoyou2.network.model.EmailAuthInfo;
import kr.co.genericit.mybase.xoyou2.network.model.FindIdInfo;
import kr.co.genericit.mybase.xoyou2.network.model.FindPwInfo;
import kr.co.genericit.mybase.xoyou2.network.model.GetMongInfo;
import kr.co.genericit.mybase.xoyou2.network.model.GetRealUnInfo;
import kr.co.genericit.mybase.xoyou2.network.model.KeywordSearchData;
import kr.co.genericit.mybase.xoyou2.network.model.KeywordSearchStory;
import kr.co.genericit.mybase.xoyou2.network.model.LocationColorInfo;
import kr.co.genericit.mybase.xoyou2.network.model.LocationListInfo;
import kr.co.genericit.mybase.xoyou2.network.model.LoginInfo;
import kr.co.genericit.mybase.xoyou2.network.model.MainUserInfo;
import kr.co.genericit.mybase.xoyou2.network.model.MongStorePrice;
import kr.co.genericit.mybase.xoyou2.network.model.MongStoreSave;
import kr.co.genericit.mybase.xoyou2.network.model.MongStoryDateChart;
import kr.co.genericit.mybase.xoyou2.network.model.MyMongInfo;
import kr.co.genericit.mybase.xoyou2.network.model.RelationListInfo;
import kr.co.genericit.mybase.xoyou2.network.model.SellMongInfo;
import kr.co.genericit.mybase.xoyou2.network.model.SetFamilyMemberInfo;
import kr.co.genericit.mybase.xoyou2.network.model.SignUpInfo;
import kr.co.genericit.mybase.xoyou2.network.model.StoreListInfo;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateCertifyInfo;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateRelationInfo;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateUserInfo;
import kr.co.genericit.mybase.xoyou2.network.model.UserListInfo;
import kr.co.genericit.mybase.xoyou2.network.model.VersionCheckInfo;
import kr.co.genericit.mybase.xoyou2.network.modelxo.*;
import kr.co.genericit.mybase.xoyou2.network.response.AddLocationResult;
import kr.co.genericit.mybase.xoyou2.network.response.AddUserResult;
import kr.co.genericit.mybase.xoyou2.network.response.BidMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.BuyMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.CoinDataResult;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.network.response.DeleteLocationResult;
import kr.co.genericit.mybase.xoyou2.network.response.DeleteRelationResult;
import kr.co.genericit.mybase.xoyou2.network.response.DeleteUserResult;
import kr.co.genericit.mybase.xoyou2.network.response.EmailAuthResult;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberCountResult;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberResult;
import kr.co.genericit.mybase.xoyou2.network.response.FindIdResult;
import kr.co.genericit.mybase.xoyou2.network.response.FindPwResult;
import kr.co.genericit.mybase.xoyou2.network.response.GetMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.LocationListResult;
import kr.co.genericit.mybase.xoyou2.network.response.LoginResult;
import kr.co.genericit.mybase.xoyou2.network.response.MongSelectDataResult;
import kr.co.genericit.mybase.xoyou2.network.response.MyMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.RelationListResult;
import kr.co.genericit.mybase.xoyou2.network.response.SellMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.SetFamilyMemberResult;
import kr.co.genericit.mybase.xoyou2.network.response.StoreListResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateLocationResult;
import kr.co.genericit.mybase.xoyou2.network.response.AddRelationResult;
import kr.co.genericit.mybase.xoyou2.network.response.SignUpResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateRelationResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateUserResult;
import kr.co.genericit.mybase.xoyou2.network.response.UserListResult;
import kr.co.genericit.mybase.xoyou2.network.response.VersionCheckResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {

    @POST(NetInfo.API_VERSION_CHECK)
    Call<VersionCheckResponse> requestVersionCheck(@Body VersionCheckInfo versionCheck);

    //RelationURL

    @POST(NetInfo.API_FAMILYMEMBER_COUNT)
    Call<FamilyMemberCountResult> requestFamilyMemberCount();

    @POST(NetInfo.API_FAMILYMEMBER_LIST)
    Call<FamilyMemberResult> requestFamilyMemberList();


    @POST(NetInfo.API_FAMILYMEMBER_UPDATE)
    Call<SetFamilyMemberResult> requestFamilyMemberList(@Body SetFamilyMemberInfo info);





    //BaseURL
        //사용자
    @POST(NetInfo.API_ADD_USER)
    Call<AddUserResult> requestAddUser(@Body AddUserInfo body);

    @POST(NetInfo.API_USER_LIST)
    Call<UserListResult> requestUserList(@Body UserListInfo body);
    @POST(NetInfo.API_MAIN_USER_INFO)
    Call<DefaultResult> requestMainUserInfo(@Body MainUserInfo body);

    @POST(NetInfo.API_DELETE_USER)
    Call<DeleteUserResult> requestDeleteUser(@Body DeleteUserInfo body);

    @POST(NetInfo.API_UPDATE_USER)
    Call<UpdateUserResult> requestUpdateUser(@Body UpdateUserInfo body);

        //장소
    @POST(NetInfo.API_ADD_LOCATION)
    Call<AddLocationResult> requestAddLocation(@Body AddLocationInfo body);

    @POST(NetInfo.API_DELETE_LOCATION)
    Call<DeleteLocationResult> requestDeleteLocation(@Body DeleteLocationInfo info);

    @POST(NetInfo.API_LOCATION_LIST)
    Call<LocationListResult> requestLocationList(@Body LocationListInfo locationListInfo);

    @POST(NetInfo.API_UADATE_LOCATION)
    Call<UpdateLocationResult> requestUpdateLocation(@Body UpdateLocationInfo info);

        //관계인
    @POST(NetInfo.API_ADD_RELATION)
    Call<AddRelationResult> requestAddRelation(@Body AddRelationInfo info);

    @POST(NetInfo.API_DELETE_RELATION)
    Call<DeleteRelationResult> requestDeleteRelationship(@Body DeleteRelationInfo info);

    @POST(NetInfo.API_RELATION_LIST)
    Call<RelationListResult> requestRelationList(@Body RelationListInfo info);

    @POST(NetInfo.API_UPDATE_RELATION)
    Call<UpdateRelationResult> requestUpdateRelation(@Body UpdateRelationInfo info);

        //manage 꿈 리스트
    @POST(NetInfo.API_MANAGE_MONGLIST)
    Call<MyMongResult> requestMyMongList(@Body MyMongInfo info);

        //store 리스트
    @POST(NetInfo.API_STORE_LIST)
    Call<StoreListResult> requestStoreList(@Body StoreListInfo info);

    @POST(NetInfo.API_GET_REAL_UN)
    Call<DefaultResult> requestGetRealUn(@Body GetRealUnInfo info);

        //꿈 상세보기
    @POST(NetInfo.API_GET_MONG)
    Call<GetMongResult> requestGetMong(@Body GetMongInfo info);

        //꿈 사기
    @POST(NetInfo.API_BUY_MONG)
    Call<BuyMongResult> requestBuyMong(@Body BuyMongInfo info);
        //꿈 입찰
    @POST(NetInfo.API_BID_MONG)
    Call<BidMongResult> requestBidMong(@Body BidMongInfo info);
    //꿈 팔기
    @POST(NetInfo.API_SELL_MONG)
    Call<SellMongResult> requestSellMong(@Body SellMongInfo info);

    //코인 정보

    @POST(NetInfo.API_COIN_DATA)
    Call<CoinDataResult> requestCoinData();





    //MainURL
    @POST(NetInfo.API_VARIFY_EMAIL)
    Call<EmailAuthResult> requestEmailAuth(@Body EmailAuthInfo emailAuthInfo);

    @POST(NetInfo.API_FIND_ID)
    Call<FindIdResult> requestFindId(@Body FindIdInfo findIdInfo);

    @POST(NetInfo.API_FIND_PW)
    Call<FindPwResult> requestFindPw(@Body FindPwInfo findPwInfo);

    @POST(NetInfo.API_LOGIN_URL)
    Call<LoginResult> requestLogin(@Body LoginInfo loginInfo);

    @POST(NetInfo.API_SIGN_UP)
    Call<SignUpResult> requestSignUp(@Body SignUpInfo signUpInfo);
    @POST(NetInfo.API_SIGN_UP)
    Call<ResponseBody> requestSignUp2(@Body SignUpInfo signUpInfo);




    //몽 분석
    @POST(NetInfo.API_SELECT_BOX_DATA1)
    Call<MongSelectDataResult> requestKeywordSelectData1();
    @POST(NetInfo.API_SELECT_BOX_DATA2)
    Call<MongSelectDataResult> requestKeywordSelectData2(@Body KeywordSearchData data);
    @POST(NetInfo.API_SELECT_BOX_DATA3)
    Call<MongSelectDataResult> requestKeywordSelectData3(@Body KeywordSearchData data);
    @POST(NetInfo.API_SELECT_BOX_DATA4)
    Call<MongSelectDataResult> requestKeywordSelectData4(@Body KeywordSearchData data);
    @POST(NetInfo.API_SELECT_BOX_DATA_PLAY)
    Call<MongSelectDataResult> requestKeywordSelectData5(@Body KeywordSearchData data);
    @POST(NetInfo.API_SEARCH_STORY_PLAY)
    Call<DefaultResult> requestSearchStory(@Body KeywordSearchStory data);
    @POST(NetInfo.API_DETAIL_DATE_CHART) //꿈분석 적용기간 차트
    Call<DefaultResult> ActionRequestMyMongStoryGetDateUn(@Body MongStoryDateChart data);
    @POST(NetInfo.API_DETAIL_LOCATION_COLOR)
    Call<DefaultResult> requestLocationColor(@Body LocationColorInfo info);

    //인증등록
    @POST(NetInfo.API_SELECT_MONG_STORE_LOAD) //셀렉트 박스
    Call<DefaultResult> requestKeywordSelectData();
    @POST(NetInfo.API_SELECT_MONG_STORE_PRICE) //가격 검증
    Call<DefaultResult> requestMongStorePrice(@Body MongStorePrice data);
    @POST(NetInfo.API_SELECT_MONG_STORE_SAVE) //몽스토어저장(MONG_SRL 받아옴)
    Call<DefaultResult> requestMongStoreSave(@Body MongStoreSave data);
    @POST(NetInfo.API_UPDATE_CERTIFY) //인증 등록
    Call<ResponseBody> requestUpdateCertify(@Body UpdateCertifyInfo data);


    @Multipart
    @POST(NetInfo.API_CARD_IMAGE_UPLOAD) //카드 이미지업로드 박스
    Call<ResponseBody> requestCardImageUpload(@Part MultipartBody.Part file);

    //xoYou
    @POST(NetInfo.API_SELECT_USER_LIST)
    Call<DefaultResult> requestXoYouUserLoad(@Body ReqXoYouUserLoad data);
    @POST(NetInfo.API_SELECT_QA_LIST)
    Call<DefaultResult> requestXoYouUserQAList(@Body ReqXoYouUserQAList data);
    @POST(NetInfo.API_SELECT_QA_SIMRI_LIST)
    Call<DefaultResult> requestXoYouUserQASimRiList(@Body ReqXoYouUserQASimRiList data);
    @POST(NetInfo.API_SELECT_QA_SIMRI_DETAIL)
    Call<DefaultResult> requestXoYouUserQASimRiDetail(@Body ReqXoYouUserQASimRiDetail data);
    @POST(NetInfo.API_SELECT_SIMRI_MESSAGE)
    Call<DefaultResult> requestXoYouUserSimRiMessage(@Body ReqXoYouUserSimRiMessage data);
    @POST(NetInfo.API_SELECT_WE_LIST)
    Call<DefaultResult> requestXoYouWeLoad(@Body ReqXoYouWeLoad data);
    @POST(NetInfo.API_SELECT_WE_UN_LIST) //꿈분석 적용기간 차트
    Call<DefaultResult> requestXoYouWeYouUnDataList(@Body ReqXoYouWeYouUnDataList data);
    @POST(NetInfo.API_SELECT_WE_UN_DETAIL)
    Call<DefaultResult> requestXoYouWeYouUnDataDetail(@Body ReqXoYouWeYouUnDataDetail info);
    @POST(NetInfo.API_SELECT_MEET_LIST)
    Call<DefaultResult> requestXoYouMeetLoad(@Body ReqXoYouMeetLoad info);
    @POST(NetInfo.API_SELECT_MEET_NAME_LIST)
    Call<DefaultResult> requestXoYouMeetNameList(@Body ReqXoYouMeetNameList info);
    @POST(NetInfo.API_SELECT_MEET_YOU_MEET_LIST)
    Call<DefaultResult> requestXoYouMeetYouMeetDataList(@Body ReqXoYouMeetYouMeetDataList info);

}
