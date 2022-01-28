package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberCountResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
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
