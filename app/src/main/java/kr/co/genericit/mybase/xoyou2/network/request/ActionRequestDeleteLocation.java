package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.DeleteLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.response.DeleteLocationResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestDeleteLocation extends Action {
    public ActionResultListener mActionResultInterface;
    private int seq;

    public ActionRequestDeleteLocation(Activity context, int seq, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
        this.seq = seq;
    }

    private final Callback<DeleteLocationResult> mCallback = new Callback<DeleteLocationResult>() {
        @Override
        public void onResponse(Call<DeleteLocationResult> call, Response<DeleteLocationResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(Action.resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(Action.resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    DeleteLocationResult body = response.body();


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
        public void onFailure(Call<DeleteLocationResult> call, Throwable t) {
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

        DeleteLocationInfo info = new DeleteLocationInfo(String.valueOf(seq));
        mRetrofitAPIService.requestDeleteLocation(info).enqueue(mCallback);
    }
}
