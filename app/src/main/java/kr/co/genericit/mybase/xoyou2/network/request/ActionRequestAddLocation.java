package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.AddLocationInfo;
import kr.co.genericit.mybase.xoyou2.network.response.AddLocationResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestAddLocation extends Action {

    public ActionResultListener mActionResultInterface;
    private String in_seq,userId,gubun,bzgubun, name,post_code,addr1,addr2,lat,lng,floor;

    public ActionRequestAddLocation(Activity context, String in_seq,String userId,String gubun,String bzgubun,String name,
                                    String post_code,String addr1, String addr2,String lat,String lng,String floor,
                                    ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.in_seq = in_seq;
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
    private final Callback<AddLocationResult> mCallback = new Callback<AddLocationResult>(){
        @Override
        public void onResponse(Call<AddLocationResult> call, Response<AddLocationResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    AddLocationResult body = response.body();


                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseResult(body);
                    }

                    break;

                default:
                    LogUtil.LogD("responseCode:: " + response.message().toString());
                    LogUtil.LogD("responseCode:: " +in_seq+"\t"+userId+"\t"+gubun+"\t"+bzgubun+"\t"+name+"\t"+post_code+"\t"+addr1+"\t"+addr2+"\t"+lat+"\t"+lng+"\t"+floor);
                    if (mActionResultInterface != null) {
                        mActionResultInterface.onResponseError(null);
                    }

                    break;
            }
        }

        @Override
        public void onFailure(Call<AddLocationResult> call, Throwable t) {
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

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);
       // String IN_SEQ, String USER_ID, String GUBUN, String NAME, String POST_CODE, String ADDR1, String ADDR2, String LAT, String LNG, String FLOOR
        AddLocationInfo info = new AddLocationInfo(in_seq,userId,gubun,bzgubun, name,post_code,addr1,addr2,lat,lng,floor);
        Log.v("CHECK","AddLocationInfo :: " + info.getUs().toString() );

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestAddLocation(info).enqueue(mCallback);
    }
}
