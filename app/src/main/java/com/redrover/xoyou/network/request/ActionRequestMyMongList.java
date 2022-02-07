package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.MyMongInfo;
import com.redrover.xoyou.network.response.MyMongResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestMyMongList extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId,viewType,currentPage;

    public ActionRequestMyMongList(Activity context, String userId,String viewType, String currentPage, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.userId = userId;
        this.viewType = viewType;
        this.currentPage = currentPage;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<MyMongResult> mCallback = new Callback<MyMongResult>(){
        @Override
        public void onResponse(Call<MyMongResult> call, Response<MyMongResult> response) {
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
                    MyMongResult body = response.body();


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
        public void onFailure(Call<MyMongResult> call, Throwable t) {
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

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);

        JWSharePreference sharePreference = new JWSharePreference();
        String userId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        String srlId = sharePreference.getInt(JWSharePreference.PREFERENCE_SRL, 0) + "";


        MyMongInfo info = new MyMongInfo(userId,srlId,viewType,currentPage);
        Log.d("MyMongStoreMyList","userId : " + userId);
        Log.d("MyMongStoreMyList","srlId : " + srlId);
        Log.d("MyMongStoreMyList","viewType :: " + viewType );
        Log.d("MyMongStoreMyList","currentPage :: " + currentPage );

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestMyMongList(info).enqueue(mCallback);
    }
}
