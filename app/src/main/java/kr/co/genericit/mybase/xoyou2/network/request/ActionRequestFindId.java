package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.FindIdInfo;
import kr.co.genericit.mybase.xoyou2.network.response.FindIdResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 아이디 찾기 Request
 */
public class ActionRequestFindId extends Action {

    public ActionResultListener mActionResultInterface;
    private String mEmail;

    public ActionRequestFindId(Activity context, String email, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mEmail = email;
        mActionResultInterface = actionResultInterface;
    }


    private final Callback<FindIdResult> mCallback = new Callback<FindIdResult>() {
        @Override
        public void onResponse(Call<FindIdResult> call, Response<FindIdResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    FindIdResult body = response.body();


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
        public void onFailure(Call<FindIdResult> call, Throwable t) {
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

        FindIdInfo findIdInfo = new FindIdInfo(mEmail);

        mRetrofitAPIService.requestFindId(findIdInfo).enqueue(mCallback);
    }
}
