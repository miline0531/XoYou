package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.MainUserInfo;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestMainUserInfo extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId;

    public ActionRequestMainUserInfo(Activity context, String userId, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.userId = userId;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<DefaultResult> mCallback = new Callback<DefaultResult>(){
        @Override
        public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    Log.d("TEST","여기구나");
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    DefaultResult body = response.body();


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
        public void onFailure(Call<DefaultResult> call, Throwable t) {
            LogUtil.LogD("!responseCode:: onFailure");
            t.printStackTrace();
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

        setBaseUrl(NetInfo.SERVER_BASE_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);

        JWSharePreference sharePreference = new JWSharePreference();
        String userId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        String name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NAME, "");
        int gender = sharePreference.getInt(JWSharePreference.PREFERENCE_GENDER, 0);
        String birth = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_BIRTH, "");

        MainUserInfo info = new MainUserInfo();
        info.setUs(userId);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestMainUserInfo(info).enqueue(mCallback);
    }
}
