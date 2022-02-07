package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.model.AddUserInfo;
import com.redrover.xoyou.network.response.AddUserResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestAddUser extends Action {

    public ActionResultListener mActionResultInterface;
    private String userId,nickName, name,birth,imageUrl, type;
    private int gender;

    public ActionRequestAddUser(Activity context,String userId,String type, String nickName, String name, int gender, String birth,String imageUrl,
                                ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.userId = userId;
        this.nickName = nickName;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.type = type;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<AddUserResult> mCallback = new Callback<AddUserResult>(){
        @Override
        public void onResponse(Call<AddUserResult> call, Response<AddUserResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    AddUserResult body = response.body();


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
        public void onFailure(Call<AddUserResult> call, Throwable t) {
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

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);
        birth = birth.substring(0,4);
        AddUserInfo addUserInfo = new AddUserInfo(userId,type,nickName,name,""+gender,birth,"");
        Log.v("CHECK","DDDDD :: " + addUserInfo.getUs().toString() );

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestAddUser(addUserInfo).enqueue(mCallback);
    }
}
