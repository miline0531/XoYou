package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.FindPwInfo;
import com.redrover.xoyou.network.response.FindPwResult;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 비밀번호 찾기 Request
 */
public class ActionRequestFindPw extends Action {

    public ActionResultListener mActionResultInterface;
    private String mEmail;
    private String mId;
    private String mPw;

    public ActionRequestFindPw(Activity context, String id, String email, String pw, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mEmail = email;
        mId = id;
        mPw = pw;
        mActionResultInterface = actionResultInterface;
    }

    private final Callback<FindPwResult> mCallback = new Callback<FindPwResult>() {
        @Override
        public void onResponse(Call<FindPwResult> call, Response<FindPwResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    FindPwResult body = response.body();


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
        public void onFailure(Call<FindPwResult> call, Throwable t) {
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

        FindPwInfo findPwInfo = new FindPwInfo(mId,mEmail,mPw);

        mRetrofitAPIService.requestFindPw(findPwInfo).enqueue(mCallback);
    }
}
