package com.redrover.xoyou.network.interfaces;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.model.AddLocationInfo;
import com.redrover.xoyou.network.model.AddRelationInfo;
import com.redrover.xoyou.network.model.AddUserInfo;
import com.redrover.xoyou.network.model.BidMongInfo;
import com.redrover.xoyou.network.model.BuyMongInfo;
import com.redrover.xoyou.network.model.DeleteLocationInfo;
import com.redrover.xoyou.network.model.DeleteRelationInfo;
import com.redrover.xoyou.network.model.DeleteUserInfo;
import com.redrover.xoyou.network.model.EmailAuthInfo;
import com.redrover.xoyou.network.model.FindIdInfo;
import com.redrover.xoyou.network.model.FindPwInfo;
import com.redrover.xoyou.network.model.GetMongInfo;
import com.redrover.xoyou.network.model.GetRealUnInfo;
import com.redrover.xoyou.network.model.KeywordSearchData;
import com.redrover.xoyou.network.model.KeywordSearchStory;
import com.redrover.xoyou.network.model.LocationColorInfo;
import com.redrover.xoyou.network.model.LocationListInfo;
import com.redrover.xoyou.network.model.LoginInfo;
import com.redrover.xoyou.network.model.MainUserInfo;
import com.redrover.xoyou.network.model.MongStorePrice;
import com.redrover.xoyou.network.model.MongStoreSave;
import com.redrover.xoyou.network.model.MongStoryDateChart;
import com.redrover.xoyou.network.model.MyMongInfo;
import com.redrover.xoyou.network.model.RelationListInfo;
import com.redrover.xoyou.network.model.SellMongInfo;
import com.redrover.xoyou.network.model.SetFamilyMemberInfo;
import com.redrover.xoyou.network.model.SignUpInfo;
import com.redrover.xoyou.network.model.StoreListInfo;
import com.redrover.xoyou.network.model.UpdateCertifyInfo;
import com.redrover.xoyou.network.model.UpdateLocationInfo;
import com.redrover.xoyou.network.model.UpdateRelationInfo;
import com.redrover.xoyou.network.model.UpdateUserInfo;
import com.redrover.xoyou.network.model.UserListInfo;
import com.redrover.xoyou.network.model.VersionCheckInfo;
import com.redrover.xoyou.network.modelxo.*;
import com.redrover.xoyou.network.response.AddLocationResult;
import com.redrover.xoyou.network.response.AddUserResult;
import com.redrover.xoyou.network.response.BidMongResult;
import com.redrover.xoyou.network.response.BuyMongResult;
import com.redrover.xoyou.network.response.CoinDataResult;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.network.response.DeleteLocationResult;
import com.redrover.xoyou.network.response.DeleteRelationResult;
import com.redrover.xoyou.network.response.DeleteUserResult;
import com.redrover.xoyou.network.response.EmailAuthResult;
import com.redrover.xoyou.network.response.FamilyMemberCountResult;
import com.redrover.xoyou.network.response.FamilyMemberResult;
import com.redrover.xoyou.network.response.FindIdResult;
import com.redrover.xoyou.network.response.FindPwResult;
import com.redrover.xoyou.network.response.GetMongResult;
import com.redrover.xoyou.network.response.LocationListResult;
import com.redrover.xoyou.network.response.LoginResult;
import com.redrover.xoyou.network.response.MongSelectDataResult;
import com.redrover.xoyou.network.response.MyMongResult;
import com.redrover.xoyou.network.response.RelationListResult;
import com.redrover.xoyou.network.response.SellMongResult;
import com.redrover.xoyou.network.response.SetFamilyMemberResult;
import com.redrover.xoyou.network.response.StoreListResult;
import com.redrover.xoyou.network.response.UpdateLocationResult;
import com.redrover.xoyou.network.response.AddRelationResult;
import com.redrover.xoyou.network.response.SignUpResult;
import com.redrover.xoyou.network.response.UpdateRelationResult;
import com.redrover.xoyou.network.response.UpdateUserResult;
import com.redrover.xoyou.network.response.UserListResult;
import com.redrover.xoyou.network.response.VersionCheckResponse;
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

    //마이콜
    @POST(NetInfo.API_SELECT_MYCALL_GET_DATA)
    Call<DefaultResult> requestMycallGetData(@Body ReqMycallGetData info);
    @POST(NetInfo.API_SELECT_MYCALL_QA_LIST)
    Call<DefaultResult> requestMycallQaList(@Body ReqMycallQaList info);
}
