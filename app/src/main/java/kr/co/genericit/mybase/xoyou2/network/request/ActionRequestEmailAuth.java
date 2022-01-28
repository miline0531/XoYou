package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.EmailAuthInfo;
import kr.co.genericit.mybase.xoyou2.network.response.EmailAuthResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 이메일확인 Request
 */
public class ActionRequestEmailAuth extends Action {

    public ActionResultListener mActionResultInterface;

    private String mType;
    private String mId;
    private String mBeforeAuthEmail;

    //1.Request Params
    public ActionRequestEmailAuth(Activity context, String type, String id, String beforeAuthEmail, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context.getBaseContext();
        mBeforeAuthEmail = beforeAuthEmail;
        mType = type;
        mId = id;
        mActionResultInterface = actionResultInterface;
    }


    private final Callback<EmailAuthResult> mCallback = new Callback<EmailAuthResult>() {
        @Override
        public void onResponse(Call<EmailAuthResult> call, Response<EmailAuthResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    EmailAuthResult body = response.body();


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
        public void onFailure(Call<EmailAuthResult> call, Throwable t) {
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


        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        EmailAuthInfo emailAuthInfo = new EmailAuthInfo(mType,mId,mBeforeAuthEmail);

        mRetrofitAPIService.requestEmailAuth(emailAuthInfo).enqueue(mCallback);
    }
}
