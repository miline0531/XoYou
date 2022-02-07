package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.BuyMongInfo;
import com.redrover.xoyou.network.response.BuyMongResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestBuyMong extends Action {

    public ActionResultListener mActionResultInterface;
    private int mong_srl;
    private String user_name;

    public ActionRequestBuyMong(Activity context,int mong_srl, String user_name, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.mong_srl = mong_srl;
        this.user_name = user_name;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<BuyMongResult> mCallback = new Callback<BuyMongResult>(){
        @Override
        public void onResponse(Call<BuyMongResult> call, Response<BuyMongResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    BuyMongResult body = response.body();


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
        public void onFailure(Call<BuyMongResult> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        BuyMongInfo info = new BuyMongInfo(mong_srl,user_name);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestBuyMong(info).enqueue(mCallback);
    }
}
