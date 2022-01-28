package kr.co.genericit.mybase.xoyou2.network.request;

import android.app.Activity;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.network.action.Action;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.interfaces.RetrofitInterface;
import kr.co.genericit.mybase.xoyou2.network.model.KeywordSearchStory;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionRequestStorySearch extends Action {

    public ActionResultListener mActionResultInterface;
    private String seq,note,date;

    public ActionRequestStorySearch(Activity context,String seq, String note, String date,
                                    ActionResultListener actionResultInterface) {
        mBaseActivity = context;
        mBaseContext = context;
        this.seq = seq;
        this.note = note;
        this.date = date;
        mActionResultInterface = actionResultInterface;
    }
    private final Callback<DefaultResult> mCallback = new Callback<DefaultResult>(){
        @Override
        public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
            CommandUtil.getInstance().dismissLoadingDialog();
            actionDone(resultType.ACTION_RESULT_RUNNEXT);
            LogUtil.LogD("responseCode:: " + response.code());

            switch (response.code()) {
                case 200:
                    if (response.headers() == null) {
                        actionDone(resultType.ACTION_RESULT_ERROR_SKIP);
                        return;
                    }
                    DefaultResult body = response.body();


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
        public void onFailure(Call<DefaultResult> call, Throwable t) {
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



        JWSharePreference jwSharePreference = new JWSharePreference();
        KeywordSearchStory data = new KeywordSearchStory();
        JWSharePreference sharePreference = new JWSharePreference();
        String srlId = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        data.setUserId(srlId);
//        data.setUserId("" +jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL, 0));
        data.setSeq(seq);
        data.setNote(note);
        data.setDate(date);
//        data.setDate(date);

        Log.v("ifeelbluu","getUserId == " + data.getUserId() );
        Log.v("ifeelbluu","getSeq == " + data.getSeq() );
        Log.v("ifeelbluu","getNote == " + data.getNote() );
        Log.v("ifeelbluu","getDate == " + data.getDate() );
       // us =addUserInfo.US(userId,5,nickName,name,gender,birth,"");
        mRetrofitAPIService.requestSearchStory(data).enqueue(mCallback);
    }
}
