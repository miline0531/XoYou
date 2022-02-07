package com.redrover.xoyou.network.request;

import static com.redrover.xoyou.network.action.Action.resultType.ACTION_RESULT_ERROR_RESPONSE;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.LoginInfo;
import com.redrover.xoyou.network.response.LoginResult;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 로그인 Request
 */
public class ActionRequestLogin extends Action {

    public ActionResultListener mActionResultInterface;
    private String mID;
    private String mPassword;

    public ActionRequestLogin(Activity context, String id, String password, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mID = id;
        mPassword = password;
        mActionResultInterface = actionResultInterface;
    }

    private final Callback<LoginResult> mCallback = new Callback<LoginResult>() {
        @Override
        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    LoginResult body = response.body();
                    if(body.isResult()){
                        //로그인성공
                        if (mActionResultInterface != null) {
                            mActionResultInterface.onResponseResult(body);
                        }
                    }else{
                        //실패
                        String errorMsg = "";
//                      errorMsg = body.getMsg()
                        errorMsg = "로그인에 실패 하였습니다.";
                        actionDone(ACTION_RESULT_ERROR_RESPONSE,errorMsg);
                        return;
                    }



                    break;

                default:
                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseError(null);
                    }

                    break;
            }
        }

        @Override
        public void onFailure(Call<LoginResult> call, Throwable t) {
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



        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        LoginInfo loginInfo = new LoginInfo(mID,mPassword);

        mRetrofitAPIService.requestLogin(loginInfo).enqueue(mCallback);
    }
}
