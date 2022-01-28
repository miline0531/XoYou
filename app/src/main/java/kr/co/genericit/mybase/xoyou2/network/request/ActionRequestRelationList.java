package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.RelationListInfo;
import kr.co.genericit.mybase.xoyou2.network.response.RelationListResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestRelationList extends Action{
    public ActionResultListener mActionResultInterface;
    private String USER_ID,CURRENT_PAGE;

    public ActionRequestRelationList(Activity context,String USER_ID,String CURRENT_PAGE, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
        this.USER_ID = USER_ID;
        this.CURRENT_PAGE = CURRENT_PAGE;
    }
    private final Callback<RelationListResult> mCallback = new Callback<RelationListResult>(){
        @Override
        public void onResponse(Call<RelationListResult> call, Response<RelationListResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(Action.resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(Action.resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    RelationListResult body = response.body();


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
        public void onFailure(Call<RelationListResult> call, Throwable t) {
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

        RelationListInfo info = new RelationListInfo(USER_ID,CURRENT_PAGE);
        mRetrofitAPIService.requestRelationList(info).enqueue(mCallback);
    }
}
