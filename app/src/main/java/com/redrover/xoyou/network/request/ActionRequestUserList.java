package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.UserListInfo;
import com.redrover.xoyou.network.response.UserListResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestUserList extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId;

    public ActionRequestUserList(Activity context, String userId, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.userId = userId;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<UserListResult> mCallback = new Callback<UserListResult>(){
        @Override
        public void onResponse(Call<UserListResult> call, Response<UserListResult> response) {
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
                    UserListResult body = response.body();


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
        public void onFailure(Call<UserListResult> call, Throwable t) {
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

        Log.v("ifeelbluu", " birth : " + birth);
        UserListInfo info = new UserListInfo(userId,"0",name,name,gender+"",birth.substring(0,4),"");

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestUserList(info).enqueue(mCallback);
    }
}
