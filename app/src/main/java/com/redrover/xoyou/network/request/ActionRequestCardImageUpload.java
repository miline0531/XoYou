package com.redrover.xoyou.network.request;

import android.app.Activity;

import java.io.File;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestCardImageUpload extends Action {

    public File imgeFile;
    public ActionResultListener mActionResultInterface;

    public ActionRequestCardImageUpload(Activity context,File imgeFile, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.imgeFile = imgeFile;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<ResponseBody> mCallback = new Callback<ResponseBody>(){
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            LogUtil.LogD("responseCode:: " + response.message());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }

                    ResponseBody body = response.body();


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
        public void onFailure(Call<ResponseBody> call, Throwable t) {
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

        setBaseUrl(NetInfo.SERVER_MAIN_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);
//
//        AddUserInfo addUserInfo = new AddUserInfo(userId,""+5,nickName,name,""+gender,birth,"");
//        Log.v("CHECK","DDDDD :: " + addUserInfo.getUs().toString() );

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");

        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imgeFile);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", imgeFile.getName(), fileRequestBody);
//        MultipartBody.Part textPart = MultipartBody.Part.createFormData("ParameterKey", "Value");

        mRetrofitAPIService.requestCardImageUpload(filePart).enqueue(mCallback);
    }
}
