package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateLocationResult;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestUpdateLocation extends Action {

    public ActionResultListener mActionResultInterface;
    private int id;
    private String seq,userId,gubun,bzgubun, name,post_code,addr1,addr2,lat,lng,floor;
    private int gender;

    public ActionRequestUpdateLocation(Activity context, String seq,String userId,String gubun,String bzgubun,String name,
                                       String post_code,String addr1, String addr2,String lat,String lng,String floor,
                                       ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.seq = seq;
        this.userId = userId;
        this.gubun = gubun;
        this.bzgubun = bzgubun;
        this.name = name;
        this.post_code = post_code;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.lat = lat;
        this.lng = lng;
        this.floor = floor;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<UpdateLocationResult> mCallback = new Callback<UpdateLocationResult>(){
        @Override
        public void onResponse(Call<UpdateLocationResult> call, Response<UpdateLocationResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    UpdateLocationResult body = response.body();


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
        public void onFailure(Call<UpdateLocationResult> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_BASE_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        UpdateLocationInfo info = new UpdateLocationInfo(seq,userId,gubun,bzgubun, name,post_code,addr1,addr2,lat,lng,floor);
        Log.v("CHECK","AddLocationInfo :: " + info.getUs().getFLOOR() );
        mRetrofitAPIService.requestUpdateLocation(info).enqueue(mCallback);
    }
}
