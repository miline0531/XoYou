package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.UpdateCertifyInfo;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestUpdateCerfify extends Action {

    public ActionResultListener mActionResultInterface;
    public String mImgPath;

    public ActionRequestUpdateCerfify(Activity context, String imgPath, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.mImgPath = imgPath;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<ResponseBody> mCallback = new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    ResponseBody body = response.body();


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
        public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);
//        final JWSharePreference sharePreference = new JWSharePreference();
//        int userId = sharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0);

        UpdateCertifyInfo data = new UpdateCertifyInfo();
        data.setMong_srl(Constants.MONG_SRL);
        data.setMong_image(mImgPath);

        Log.v("ifeelbluu","UpdateCertifyInfo :: setMong_srl " +  Constants.MONG_SRL);
        Log.v("ifeelbluu","UpdateCertifyInfo :: setMong_image " +  mImgPath);

        mRetrofitAPIService.requestUpdateCertify(data).enqueue(mCallback);

    }
}
