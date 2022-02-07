package com.redrover.xoyou.network.request;

import android.app.Activity;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.UpdateRelationInfo;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.network.response.UpdateRelationResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestUpdateRelation extends Action {

    public ActionResultListener mActionResultInterface;
    private int relationId;
    private  String SEQ,IN_SEQ,GWANGYE,NICK_NAME,NAME,MW,BIRTH_DATE,IMAGE_URL,CALL_NUMBER;
    private int gender;

    public ActionRequestUpdateRelation(Activity context,String SEQ,String IN_SEQ,String GWANGYE,String NICK_NAME,String NAME,
                                       String MW,String BIRTH_DATE,String IMAGE_URL,String CALL_NUMBER,
                                       ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.SEQ = SEQ;
        this.IN_SEQ = IN_SEQ;
        this.GWANGYE = GWANGYE;
        this.NICK_NAME = NICK_NAME;
        this.NAME = NAME;
        this.MW = MW;
        this.BIRTH_DATE = BIRTH_DATE;
        this.IMAGE_URL = IMAGE_URL;
        this.CALL_NUMBER = CALL_NUMBER;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<UpdateRelationResult> mCallback = new Callback<UpdateRelationResult>(){
        @Override
        public void onResponse(Call<UpdateRelationResult> call, Response<UpdateRelationResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    UpdateRelationResult body = response.body();


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
        public void onFailure(Call<UpdateRelationResult> call, Throwable t) {
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

        UpdateRelationInfo info = new UpdateRelationInfo(SEQ,IN_SEQ,GWANGYE,NICK_NAME,NAME,MW,BIRTH_DATE,IMAGE_URL,CALL_NUMBER);

        mRetrofitAPIService.requestUpdateRelation(info).enqueue(mCallback);
    }
}
