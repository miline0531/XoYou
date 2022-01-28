package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.StoreListInfo;
import kr.co.genericit.mybase.xoyou2.network.response.StoreListResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestStoreList extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId;

    public ActionRequestStoreList(Activity context, String userId,ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.userId = userId;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<StoreListResult> mCallback = new Callback<StoreListResult>(){
        @Override
        public void onResponse(Call<StoreListResult> call, Response<StoreListResult> response) {
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
                    StoreListResult body = response.body();


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
        public void onFailure(Call<StoreListResult> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_BASE_URL2);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        JWSharePreference sharePreference = new JWSharePreference();
        String userId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        String srlId = sharePreference.getInt(JWSharePreference.PREFERENCE_SRL, 0) + "";

        Log.d("MyMongStoreStoreList","userId :: " + userId);
        Log.d("MyMongStoreStoreList","srlId :: " + srlId);
        StoreListInfo info = new StoreListInfo(userId,srlId);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestStoreList(info).enqueue(mCallback);
    }
}
