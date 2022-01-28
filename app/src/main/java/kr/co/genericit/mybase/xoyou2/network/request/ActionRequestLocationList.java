package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.LocationListInfo;
import kr.co.genericit.mybase.xoyou2.network.response.LocationListResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestLocationList extends Action {

    public ActionResultListener mActionResultInterface;

    private String user_id,current_page;

    //1.Request Params
    public ActionRequestLocationList(Activity context,String user_id, String current_page, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context.getBaseContext();
        mActionResultInterface = actionResultInterface;
        this.user_id = user_id;
        this.current_page = current_page;
    }


    private final Callback<LocationListResult> mCallback = new Callback<LocationListResult>() {
        @Override
        public void onResponse(Call<LocationListResult> call, Response<LocationListResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    LocationListResult body = response.body();


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
        public void onFailure(Call<LocationListResult> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_BASE_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        LocationListInfo locationListInfo = new LocationListInfo(user_id,current_page);

        mRetrofitAPIService.requestLocationList(locationListInfo).enqueue(mCallback);
    }
}
