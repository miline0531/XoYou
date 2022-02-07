package com.redrover.xoyou.network.request;

import static com.redrover.xoyou.network.action.Action.resultType.ACTION_RESULT_ERROR_RESPONSE;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.VersionCheckInfo;
import com.redrover.xoyou.network.response.VersionCheckResponse;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 버전체크 Request
 */
public class ActionRequestVersionCheck extends Action {

    public ActionResultListener mActionResultInterface;

    public ActionRequestVersionCheck(Activity context, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
    }

    private final Callback<VersionCheckResponse> mCallback = new Callback<VersionCheckResponse>() {
        @Override
        public void onResponse(Call<VersionCheckResponse> call, Response<VersionCheckResponse> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    VersionCheckResponse body = response.body();

                    if(response.isSuccessful()){
                        if (mActionResultInterface != null) {
                            mActionResultInterface.onResponseResult(body);
                        }
                    }else{
                        //실패
                        String errorMsg = "";
                        errorMsg = "버전 확인중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.";
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
        public void onFailure(Call<VersionCheckResponse> call, Throwable t) {
            CommandUtil.getInstance().dismissLoadingDialog();
        }
    };


    @Override
    public void run() {
        if (!isNetworkCheck()) {
            actionDone(resultType.ACTION_RESULT_ERROR_DISABLE_NETWORK);
            return;
        }

//        CommandUtil.getInstance().showLoadingDialog(mBaseActivity);

        setBaseUrl(NetInfo.SERVER_VERSION_CHECK_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        VersionCheckInfo versionCheck = new VersionCheckInfo();
        versionCheck.setDistCd("DIST03");

        mRetrofitAPIService.requestVersionCheck(versionCheck).enqueue(mCallback);
    }
}
