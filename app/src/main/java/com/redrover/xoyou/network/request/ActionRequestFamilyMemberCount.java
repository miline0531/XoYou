package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.response.FamilyMemberCountResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestFamilyMemberCount extends Action {

    public ActionResultListener mActionResultInterface;


    //1.Request Params
    public ActionRequestFamilyMemberCount(Activity context, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
    }


    private final Callback<FamilyMemberCountResult> mCallback = new Callback<FamilyMemberCountResult>() {
        @Override
        public void onResponse(Call<FamilyMemberCountResult> call, Response<FamilyMemberCountResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {

                case 200:
                    if (response.headers() == null) {
                        Log.d("!TEST","header null");
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        mActionResultInterface.onResponseError(String.valueOf(R.string.str_error));
                        return;
                    }

                    FamilyMemberCountResult body = response.body();

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
        public void onFailure(Call<FamilyMemberCountResult> call, Throwable t) {
            CommandUtil.getInstance().dismissLoadingDialog();

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

        setBaseUrl(NetInfo.SERVER_RELATION_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        mRetrofitAPIService.requestFamilyMemberCount().enqueue(mCallback);
    }
}
