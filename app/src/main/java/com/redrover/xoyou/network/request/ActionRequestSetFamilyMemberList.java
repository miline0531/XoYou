package com.redrover.xoyou.network.request;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.network.action.Action;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.SetFamilyMemberInfo;
import com.redrover.xoyou.network.response.FamilyMemberResult;
import com.redrover.xoyou.network.response.SetFamilyMemberResult;
import com.redrover.xoyou.network.interfaces.RetrofitInterface;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestSetFamilyMemberList extends Action {

    public ActionResultListener mActionResultInterface;
    public ArrayList<ArrayList<FamilyMemberResult.Data.FamilyInfo>> arrayListSet;

    //1.Request Params
    public ActionRequestSetFamilyMemberList(Activity context,ArrayList<ArrayList<FamilyMemberResult.Data.FamilyInfo>> arrayListSet, ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        mActionResultInterface = actionResultInterface;
        this.arrayListSet = arrayListSet;
    }



    private final Callback<SetFamilyMemberResult> mCallback = new Callback<SetFamilyMemberResult>() {
        @Override
        public void onResponse(Call<SetFamilyMemberResult> call, Response<SetFamilyMemberResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();

            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());
            switch (response.code()) {

                case 200:
                    if (response.headers() == null) {
                        Log.d("!TEST","header null");
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }

                    SetFamilyMemberResult body = response.body();

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
        public void onFailure(Call<SetFamilyMemberResult> call, Throwable t) {
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


        setBaseUrl(NetInfo.SERVER_RELATION_URL);
        RetrofitInterface mRetrofitAPIService = mRetrofit.create(RetrofitInterface.class);

        SetFamilyMemberInfo info = new SetFamilyMemberInfo(
                arrayListSet.get(0), arrayListSet.get(1), arrayListSet.get(2), arrayListSet.get(3), arrayListSet.get(4)
        );

        mRetrofitAPIService.requestFamilyMemberList(info).enqueue(mCallback);
    }
}
