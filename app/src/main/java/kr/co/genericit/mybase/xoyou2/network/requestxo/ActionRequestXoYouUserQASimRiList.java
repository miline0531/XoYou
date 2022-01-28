package kr.co.genericit.mybase.xoyou2.network.requestxo;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.modelxo.ReqXoYouUserQASimRiList;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestXoYouUserQASimRiList extends Action {

    public ActionResultListener mActionResultInterface;
    public String mUserId,mDate,mMenuSeo;

    public ActionRequestXoYouUserQASimRiList(Activity context, String userId, String date, String menuSeo,ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mUserId = userId;
        mDate = date;
        mMenuSeo = menuSeo;
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

        int keywordCount = 0;
        ReqXoYouUserQASimRiList data = new ReqXoYouUserQASimRiList();
        if(mUserId != null) data.setUserId(mUserId);
        if(mDate != null) data.setDate(mDate);
        if(mMenuSeo != null) data.setMenuSeo(mMenuSeo);

        mRetrofitAPIService.requestXoYouUserQASimRiList(data).enqueue(mCallback);
    }
}
