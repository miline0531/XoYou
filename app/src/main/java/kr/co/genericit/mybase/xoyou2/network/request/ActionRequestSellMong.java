package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.SellMongInfo;
import kr.co.genericit.mybase.xoyou2.network.response.SellMongResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestSellMong extends Action {

    public ActionResultListener mActionResultInterface;
    private int mong_srl,transaction_type,start_value;
    String start_time,end_time,description,title;
    String[] category_code;

    public ActionRequestSellMong(Activity context, int mong_srl, String start_time, String end_time, int transaction_type, int start_value,
            String description, String title, String[] category_code, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.mong_srl = mong_srl;
        this.start_time = start_time;
        this.end_time = end_time;
        this.transaction_type = transaction_type;
        this.start_value = start_value;
        this.description = description;
        this.title = title;
        this.category_code = category_code;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<SellMongResult> mCallback = new Callback<SellMongResult>(){
        @Override
        public void onResponse(Call<SellMongResult> call, Response<SellMongResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    Log.d("TEST","여기구나");
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    SellMongResult body = response.body();


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
        public void onFailure(Call<SellMongResult> call, Throwable t) {
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

        //Log.d("TEST@",userId);
        SellMongInfo info = new SellMongInfo(mong_srl, start_time, end_time, transaction_type, start_value, description, title,  category_code);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestSellMong(info).enqueue(mCallback);
    }
}
