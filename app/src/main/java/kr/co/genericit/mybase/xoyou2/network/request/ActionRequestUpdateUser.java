package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.UpdateUserInfo;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateUserResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestUpdateUser extends Action {

    public ActionResultListener mActionResultInterface;
    private String SEQ,NICK_NAME,NAME,MW,BIRTH_DATE,IMAGE_URL;

    public ActionRequestUpdateUser(Activity context, String SEQ, String NICK_NAME, String NAME, String MW, String BIRTH_DATE,
                                   String IMAGE_URL, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.SEQ = SEQ;
        this.NICK_NAME = NICK_NAME;
        this.NAME = NAME;
        this.MW = MW;
        this.BIRTH_DATE = BIRTH_DATE;
        this.IMAGE_URL = IMAGE_URL;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<UpdateUserResult> mCallback = new Callback<UpdateUserResult>(){
        @Override
        public void onResponse(Call<UpdateUserResult> call, Response<UpdateUserResult> response) {
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
                    UpdateUserResult body = response.body();


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
        public void onFailure(Call<UpdateUserResult> call, Throwable t) {
            LogUtil.LogD("!responseCode:: onFailure" + t.getMessage());
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

        setBaseUrl(NetInfo.SERVER_BASE_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        //SetRelationshipInfo info = new SetRelationshipInfo("",relationship,firstName,lastName,birth,gender);

        UpdateUserInfo info = new UpdateUserInfo(SEQ,NICK_NAME,NAME,MW,BIRTH_DATE,IMAGE_URL);
        Log.d("TEST@",SEQ+NICK_NAME+NAME+MW+BIRTH_DATE+IMAGE_URL);

       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestUpdateUser(info).enqueue(mCallback);
    }
}
