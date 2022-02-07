package com.redrover.xoyou.network.requestxo;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.modelxo.ReqXoYouUserSimRiMessage;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestXoYouUserSimRiMessage extends Action {

    public ActionResultListener mActionResultInterface;
    public String mUserId,mCallNumber,mMessage;

    public ActionRequestXoYouUserSimRiMessage(Activity context, String userId, String callNumber, String message, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mUserId = userId;
        mCallNumber = callNumber;
        mMessage = message;

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

        int keywordCount = 0;
        ReqXoYouUserSimRiMessage data = new ReqXoYouUserSimRiMessage();
        if(mUserId != null) data.setUserId(mUserId);
        if(mCallNumber != null) data.setCallNumber(mCallNumber);
        if(mMessage != null) data.setMessage(mMessage);

        mRetrofitAPIService.requestXoYouUserSimRiMessage(data).enqueue(mCallback);
    }
}