package kr.co.genericit.mybase.xoyou2.network.action;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kr.co.genericit.mybase.xoyou2.BuildInfo;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 공통 Action Class
 */
public abstract class Action implements Runnable {
    public static Activity mBaseActivity;
    public static Context mBaseContext;
    public static Retrofit mRetrofit;
    public static String url;

    public void setBaseUrl(String baseUrl){
        if (mRetrofit == null || !baseUrl.equals(url)) {
            url = baseUrl;
            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .client(getDefaultHttpClient()).build();
        }
    }

    public void actionDone(resultType type) {
        actionDone("", type, "");
    }

    public void actionDone(resultType type, String errorCode) {
        actionDone("", type, errorCode);
    }

    public void actionDone(String message, resultType type, String errorCode) {

        if (TextUtils.isEmpty(errorCode)) {
            errorCode = mBaseContext.getResources().getString(R.string.str_error);
        }

        switch (type) {
            case ACTION_RESULT_RUNNEXT:
                ActionRuler.getInstance().runNext();
                break;
            case ACTION_RESULT_ERROR_DISABLE_NETWORK:
                ActionRuler.getInstance().finish();
                if (BuildInfo.FLAG_DEBUG_MODE) {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, mBaseContext.getResources().getString(R.string.str_network_server_error) + getClass().toString(), mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_APP, null);
                } else {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, mBaseContext.getResources().getString(R.string.str_network_server_error) + "\n" + "", mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_APP, null);
                }
                break;
            case ACTION_RESULT_ERROR_NOT_RESPONSE:
                ActionRuler.getInstance().finish();
                if (BuildInfo.FLAG_DEBUG_MODE) {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + getClass().toString(), mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                } else {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + "\n" + "", mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                }
                break;
            case ACTION_RESULT_ERROR_RESPONSE:
                ActionRuler.getInstance().finish();
                if (BuildInfo.FLAG_DEBUG_MODE) {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + getClass().toString(), mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                } else {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + "\n" + "", mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                }
                break;
            case ACTION_RESULT_ERROR_INTRO:
                ActionRuler.getInstance().runNext();
                if (BuildInfo.FLAG_DEBUG_MODE) {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + getClass().toString(), mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                } else {
                    CommandUtil.getInstance().showCommonOneButtonDialog(mBaseContext, errorCode + "\n" + "", mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
                }
                break;
            case ACTION_RESULT_ERROR_SKIP:
                ActionRuler.getInstance().runNext();
                break;
        }
    }

    /**
     * 응답 타입 구분
     */
    public enum resultType {
        ACTION_RESULT_RUNNEXT,
        ACTION_RESULT_ERROR_DISABLE_NETWORK,
        ACTION_RESULT_ERROR_NOT_RESPONSE,
        ACTION_RESULT_ERROR_RESPONSE,
        ACTION_RESULT_ERROR_SKIP,
        ACTION_RESULT_ERROR_INTRO
    }

    /**
     * interceptor 세부 세팅
     */
    private class ForbiddenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            Log.v("CHECK","response.code() === " + response.code());
            if (response.code() == 510) {
                // 서버 에러상황일때 코드 삽입
                // pms API 통해 노출될지,,여부
            }

            return response;
        }
    }
    /**
     * interceptor 세팅
     */
    public OkHttpClient getDefaultHttpClient() {
        final JWSharePreference sharePreference = new JWSharePreference();
        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();
        okHttpClient.retryOnConnectionFailure(true);
        okHttpClient.readTimeout(NetInfo.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(NetInfo.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                if (sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_FLAG, "N").equals("Y")) {
//                    //로그인아이디
//                    builder.addHeader("loginId", sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID, ""));
//                    //사용자아이디
//                    builder.addHeader("userId", sharePreference.getString(JWSharePreference.PREFERENCE_USER_ID, ""));
                    //유저토큰
                    builder.addHeader("Authorization", "Bearer " + sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_TOKEN, ""));
                }
                builder.addHeader("Content-Type", "application/json; charset=utf-8");
                builder.method(original.method(), original.body());

                Request request = builder.build();

                LogUtil.LogD("[Request Interceptor] url > " + request.url());
                LogUtil.LogD("[Request Interceptor] headers > " + request.headers());
                LogUtil.LogD("[Request Interceptor] body > " + original.body().toString());
                LogUtil.LogD("[Request Interceptor] connection > " + chain.connection());

                if (BuildInfo.FLAG_DEBUG_MODE) {
                    LogUtil.LogD("===== Action Request URL ====== : " + request.url());
                }

                Response response = chain.proceed(request);
                LogUtil.LogD("[Response Interceptor] url > " + response.toString());
                return response;
            }
        });

        return okHttpClient.build();
    }

    /**
     * 현재 네트워크 상태 체크
     */
    public boolean isNetworkCheck() {
        return CommandUtil.getInstance().isEnabledNetwork(mBaseContext);
    }

    public void showLoadingImage(){
        //todo loading show
//        CommandUtil.getInstance().showLoadingDialog(mBaseContext);
    }
    public void dismissLoadingImage(){
        //todo loading dismiss
//        CommandUtil.getInstance().dismissActionRulerLoadingDialog();
    }

    public boolean isServiceDenied(int code, String message){
        if(code == 401){
            if(message == null || message.isEmpty()) {
                message = mBaseContext.getResources().getString(R.string.error_login_text);
            }
//            CommandUtil.getInstance().dismissLoadingDialog();
//            CommandUtil.getInstance().initLoginInfo();
            CommandUtil.getInstance().showCommonOneButtonDialog(CommandUtil.getInstance().getCurrentActivity(), message, mBaseContext.getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                @Override
                public void onClick(int button) {

                }
            });

            return true;
        }

        return false;
    }



    /**
     * error 메세지 반환
     * @param msg
     * @return
     */
    public String getErrorMsg(String msg) {
        String errorMsg = "";
        if (!TextUtils.isEmpty(msg)) {
            errorMsg = msg;
        }
        return errorMsg;
    }

}
