package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.GetMongInfo;
import com.redrover.xoyou.network.response.GetMongResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestGetMong extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId,seq;

    public ActionRequestGetMong(Activity context, String userId,String seq,
                                ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.seq = seq;
        this.userId = userId;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<GetMongResult> mCallback = new Callback<GetMongResult>(){
        @Override
        public void onResponse(Call<GetMongResult> call, Response<GetMongResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    GetMongResult body = response.body();


                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseResult(body);
                    }

                    break;

                default:
                    LogUtil.LogD("responseCode:: " + response.message().toString());

                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseError(null);
                    }

                    break;
            }
        }

        @Override
        public void onFailure(Call<GetMongResult> call, Throwable t) {
            LogUtil.LogD("responseCode:: onFailure");
//            actionDone("", null, "");
            CommandUtil.getInstance().dismissLoadingDialog();
        }
    };


    @Override
    public void run() {
        if (!isNetworkCheck()) {
            actionDone(resultType.ACTION_RESULT_ERROR_DISABLE_NETWORK);
            return;
        }

        CommandUtil.getInstance().showLoadingDialog(mBaseActivity);

        setBaseUrl(NetInfo.SERVER_BASE_URL2);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);
       // String IN_SEQ, String USER_ID, String GUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR

        JWSharePreference sharePreference = new JWSharePreference();
        String userId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        String srlId = sharePreference.getInt(JWSharePreference.PREFERENCE_SRL, 0) + "";

        GetMongInfo info = new GetMongInfo(userId,srlId,seq);


       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestGetMong(info).enqueue(mCallback);
    }
}
