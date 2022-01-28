package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.BidMongInfo;
import kr.co.genericit.mybase.xoyou2.network.response.BidMongResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestBidMong extends Action {

    public ActionResultListener mActionResultInterface;
    private int bid_value;
    private int mong_srl;

    public ActionRequestBidMong(Activity context, int bid_value, int mong_srl, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.bid_value = bid_value;
        this.mong_srl = mong_srl;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<BidMongResult> mCallback = new Callback<BidMongResult>(){
        @Override
        public void onResponse(Call<BidMongResult> call, Response<BidMongResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    BidMongResult body = response.body();


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
        public void onFailure(Call<BidMongResult> call, Throwable t) {
            LogUtil.LogD("!responseCode:: onFailure");
            t.printStackTrace();
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

        BidMongInfo info = new BidMongInfo(bid_value,mong_srl);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestBidMong(info).enqueue(mCallback);
    }
}
