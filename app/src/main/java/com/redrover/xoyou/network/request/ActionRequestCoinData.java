package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.response.CoinDataResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestCoinData extends Action {

    public ActionResultListener mActionResultInterface;



    //1.Request Params
    public ActionRequestCoinData(Activity context, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context.getBaseContext();
        mActionResultInterface = actionResultInterface;

    }


    private final Callback<CoinDataResult> mCallback = new Callback<CoinDataResult>() {
        @Override
        public void onResponse(Call<CoinDataResult> call, Response<CoinDataResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    CoinDataResult body = response.body();


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
        public void onFailure(Call<CoinDataResult> call, Throwable t) {
            CommandUtil.getInstance().dismissLoadingDialog();
            Log.d("CHECK",t.toString());

        }
    };


    //2.Request
    @Override
    public void run() {
        if (!isNetworkCheck()) {
            actionDone(resultType.ACTION_RESULT_ERROR_DISABLE_NETWORK);
            return;
        }

        CommandUtil.getInstance().showLoadingDialog(mBaseActivity);

        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);


        mRetrofitAPIService.requestCoinData().enqueue(mCallback);
    }
}
