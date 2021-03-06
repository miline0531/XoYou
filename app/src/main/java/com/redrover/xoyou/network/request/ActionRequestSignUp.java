package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.SignUpInfo;
import com.redrover.xoyou.network.response.SignUpResult;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 회원가입 Request
 */
public class ActionRequestSignUp extends Action {

    public ActionResultListener mActionResultInterface;
    private SignUpInfo mData;

    public ActionRequestSignUp(Activity context, SignUpInfo data, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mData = data;
        mActionResultInterface = actionResultInterface;
    }


    private final Callback<SignUpResult> mCallback = new Callback<SignUpResult>() {
        @Override
        public void onResponse(Call<SignUpResult> call, Response<SignUpResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    SignUpResult body = response.body();


                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseResult(body);
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
        public void onFailure(Call<SignUpResult> call, Throwable t) {
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

        SignUpInfo signUpInfo = mData;

        mRetrofitAPIService.requestSignUp(signUpInfo).enqueue(mCallback);
    }
}
