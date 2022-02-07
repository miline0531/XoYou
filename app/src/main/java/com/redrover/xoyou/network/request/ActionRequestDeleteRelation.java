package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.DeleteRelationInfo;
import com.redrover.xoyou.network.response.DeleteRelationResult;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestDeleteRelation extends Action{
    public ActionResultListener mActionResultInterface;
    private String SEQ;

    public ActionRequestDeleteRelation(Activity context, String SEQ, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
        this.SEQ = SEQ;
    }

    private final Callback<DeleteRelationResult> mCallback = new Callback<DeleteRelationResult>(){
        @Override
        public void onResponse(Call<DeleteRelationResult> call, Response<DeleteRelationResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(Action.resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(Action.resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    DeleteRelationResult body = response.body();


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
        public void onFailure(Call<DeleteRelationResult> call, Throwable t) {
            CommandUtil.getInstance().dismissLoadingDialog();
        }
    };


    @Override
    public void run() {
        if (!isNetworkCheck()) {
            actionDone(Action.resultType.ACTION_RESULT_ERROR_DISABLE_NETWORK);
            return;
        }

        CommandUtil.getInstance().showLoadingDialog(mBaseActivity);

        setBaseUrl(NetInfo.SERVER_BASE_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        DeleteRelationInfo info = new DeleteRelationInfo(SEQ);
        mRetrofitAPIService.requestDeleteRelationship(info).enqueue(mCallback);
    }
}
