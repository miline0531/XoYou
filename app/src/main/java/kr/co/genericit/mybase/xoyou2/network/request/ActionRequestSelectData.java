package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.KeywordSearchData;
import kr.co.genericit.mybase.xoyou2.network.response.MongSelectDataResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestSelectData extends Action {

    public ActionResultListener mActionResultInterface;
    public String mKeyword1,mKeyword2,mKeyword3,mKeyword4;
    public int mSearchLevel;

    public ActionRequestSelectData(Activity context, int searchLevel, String keyword1,String keyword2,String keyword3,String keyword4, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mKeyword1 = keyword1;
        mKeyword2 = keyword2;
        mKeyword3 = keyword3;
        mKeyword4 = keyword4;
        mSearchLevel = searchLevel;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<MongSelectDataResult> mCallback = new Callback<MongSelectDataResult>(){
        @Override
        public void onResponse(Call<MongSelectDataResult> call, Response<MongSelectDataResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    MongSelectDataResult body = response.body();


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
        public void onFailure(Call<MongSelectDataResult> call, Throwable t) {
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
        KeywordSearchData data = new KeywordSearchData();
        if(mKeyword1 != null) data.setDaeMong(mKeyword1);
        if(mKeyword2 != null) data.setSoMong(mKeyword2);
        if(mKeyword3 != null) data.setWhatMong(mKeyword3);
        if(mKeyword4 != null) data.setPlayMong(mKeyword4);

        switch (mSearchLevel){
            case 0:
                mRetrofitAPIService.requestKeywordSelectData1().enqueue(mCallback);
                break;
            case 1:
                mRetrofitAPIService.requestKeywordSelectData2(data).enqueue(mCallback);
                break;
            case 2:
                mRetrofitAPIService.requestKeywordSelectData3(data).enqueue(mCallback);
                break;
            case 3:
                mRetrofitAPIService.requestKeywordSelectData4(data).enqueue(mCallback);
                break;
            case 4:
                mRetrofitAPIService.requestKeywordSelectData5(data).enqueue(mCallback);
                break;
        }

    }
}
