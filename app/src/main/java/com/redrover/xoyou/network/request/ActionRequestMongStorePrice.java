package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.MongStorePrice;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestMongStorePrice extends Action {

    public ActionResultListener mActionResultInterface;
    public String MongStoreColor,MongStoreSoRi,MongStoreYear,MongStorePyeongGa;

    public ActionRequestMongStorePrice(Activity context, String MongStoreColor, String MongStoreSoRi, String MongStoreYear, String MongStorePyeongGa, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.MongStoreColor = MongStoreColor;
        this.MongStoreSoRi = MongStoreSoRi;
        this.MongStoreYear = MongStoreYear;
        this.MongStorePyeongGa = MongStorePyeongGa;
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

        MongStorePrice data = new MongStorePrice();
        JWSharePreference sharePreference = new JWSharePreference();
        String srlId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        data.setUserId(srlId);
        try {
            data.setSeq(Constants.mongSEQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data.setMongStorePyeongGa(MongStorePyeongGa);
        data.setMongStoreColor(MongStoreColor);
        data.setMongStoreSoRi(MongStoreSoRi);
        //data.setMongStoreyear(MongStoreYear);


        Log.v("ifeelbluu", "setSeq :: " + data.getSeq());
        Log.v("ifeelbluu", "MongStorePyeongGa :: " + MongStorePyeongGa);

        mRetrofitAPIService.requestMongStorePrice(data).enqueue(mCallback);

    }
}
