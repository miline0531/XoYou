package com.redrover.xoyou.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.interfaces.DialogClickListener;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.view.CommonPopupDialog;

/**
 * 기능별 함수 정의
 * <p>
 */
public class CommandUtil {
    private final int PERMISSION_REQUEST_CODE = 5005;

    private Dialog mProgressDialog;
    private Dialog mSendDialog;

    private boolean mLoadingAnimationStatus = false;

    private boolean mSendLoadingAnimationStatus = false;

    private Activity mCurrentActivity;

    private static CommandUtil instance = new CommandUtil();

    public int findRes(Context context, String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    public static synchronized CommandUtil getInstance() {
        return instance;
    }

    public synchronized void showCommonOneButtonDefaultDialog(final Context context,
                                                       final String message) {
        Activity mActivity = (Activity) context;
        if (mActivity != null && mActivity.isFinishing()) {
            // 이미 액티비티가 종료된 상태에서 팝업을 띄울때 에러 발생 방지
            return;
        }
        CommonPopupDialog commonPopupDialog = new CommonPopupDialog(context, message, "확인", CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);
        commonPopupDialog.setCanceledOnTouchOutside(false);
        commonPopupDialog.setCancelable(false);
        commonPopupDialog.show();
    }

    /**
     * 공통 One Button Dialog 호출 함수
     *
     * @param context             Context
     * @param message             팝업 메시지
     * @param buttonText          팝업 버튼 문구
     * @param buttonOption        팝업 버튼 옵션
     * @param dialogClickListener 다이얼로그 버튼 클릭 리스너(필요 없을 경우 null 처리)
     */
    public synchronized void showCommonOneButtonDialog(final Context context,
                                                       final String message,
                                                       final String buttonText,
                                                       final int buttonOption,
                                                       final DialogClickListener dialogClickListener) {
        Activity mActivity = (Activity) context;
        if (mActivity != null && mActivity.isFinishing()) {
            // 이미 액티비티가 종료된 상태에서 팝업을 띄울때 에러 발생 방지
            return;
        }
        CommonPopupDialog commonPopupDialog = new CommonPopupDialog(context, message, buttonText, buttonOption, dialogClickListener);
        commonPopupDialog.setCanceledOnTouchOutside(false);
        commonPopupDialog.setCancelable(false);
        commonPopupDialog.show();
    }


    /**
     * 네트워크 상태 확인 함수
     */
    public synchronized boolean isEnabledNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return true;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * 문자열 Null, Empty, Length 유효성 확인 함수
     *
     * @param str 확인 문자열
     * @return boolean true : 유효 문자, false : 무효 문자
     */
    public synchronized boolean isValidString(String str) {
        if (str == null || str.equals("") || str.isEmpty()) {
            return false;
        }

        if (str.trim().length() <= 0) {
            return false;
        }

        return true;
    }

    /**
     * 안드로이드 M이상 퍼미션 획득 처리
     *
     * @param activity    Activity
     * @param permissions 퍼미션 리스트
     */
    public void setPermission(Activity activity, List<String> permissions) {
        int needRequestPermissionCount = 0;
        int permissionIndex = 0;
        HashMap<String, Integer> permissionHashMap = new HashMap<String, Integer>();
        List<String> arrPermissions = permissions;

        for (int i = 0; i < arrPermissions.size(); i++) {

            int permissionCheck = ContextCompat.checkSelfPermission(activity, arrPermissions.get(i));

            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                LogUtil.LogD(arrPermissions.get(i) + " : PERMISSION_DENIED");
                permissionHashMap.put(arrPermissions.get(i), PackageManager.PERMISSION_DENIED);
                needRequestPermissionCount++;
            } else {
                LogUtil.LogD(arrPermissions.get(i) + " : PERMISSION_GRANTED");
                permissionHashMap.put(arrPermissions.get(i), PackageManager.PERMISSION_GRANTED);
            }
        }

        String[] permission = new String[needRequestPermissionCount];

        for (int i = 0; arrPermissions.size() > i; i++) {
            if (permissionHashMap.get(arrPermissions.get(i)) == PackageManager.PERMISSION_DENIED) {
                permission[permissionIndex] = arrPermissions.get(i);
                permissionIndex++;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permission.length > 0) {
                activity.requestPermissions(permission, PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * 공통 Two Button Dialog 호출 함수
     * 기본 타입.
     */
    public synchronized void showCommonTwoButtonDialog(final Context context,
                                                       final String message,
                                                       final String leftButtonText,
                                                       final String rightButtonText,
                                                       final DialogClickListener listener) {
        showCommonTwoButtonDialog(context, message, leftButtonText, rightButtonText, CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, listener);
    }

    /**
     * 공통 Two Button Dialog 호출 함수
     *
     * @param context             Context
     * @param leftButtonText      왼쪽 버튼 문구
     * @param rightButtonText     오른쪽 버튼 문구
     * @param leftButtonOption    왼쪽 버튼 옵션
     * @param rightButtonOption   오른쪽 버튼 옵션
     * @param dialogClickListener 다이얼로그 버튼 클릭 리스너(필요 없을 경우 null 처리)
     */
    public synchronized void showCommonTwoButtonDialog(final Context context, final String message, final String leftButtonText, final String rightButtonText, final int leftButtonOption, final int rightButtonOption, final DialogClickListener dialogClickListener) {
        Activity mActivity = (Activity) context;
        if (mActivity != null && mActivity.isFinishing()) {
            // 이미 액티비티가 종료된 상태에서 팝업을 띄울때 에러 발생 방지
            return;
        }
        CommonPopupDialog commonPopupDialog = new CommonPopupDialog(context, message, leftButtonText, rightButtonText, leftButtonOption, rightButtonOption, dialogClickListener);
        commonPopupDialog.setCanceledOnTouchOutside(false);
        commonPopupDialog.setCancelable(false);
        commonPopupDialog.show();
    }

    /**
     * getColor 함수 버전 별 적용
     *
     * @param context Context
     * @param id      Resource ID
     * @return int Resource ID
     */
    public synchronized int getColor(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, null);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Drawable 함수 버전 별 적용
     *
     * @param context Context
     * @param view    target View
     * @param id      Resource ID
     */
    public void setViewDrawable(Context context, View view, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackground(ContextCompat.getDrawable(context, id));
        } else {
            view.setBackgroundDrawable(ContextCompat.getDrawable(context, id));
        }
    }

    /**
     * Drawable 함수 버전 별 적용
     *
     * @param view     Target View
     * @param drawable Drawable
     * @author Jong Hwan Lee
     */
    public void setViewDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public float convertPixelsToDp(Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * 현재 액티비티 설정 함수
     *
     * @param activity Activity
     */
    public synchronized void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }

    /**
     * 현재 액티비티 반환 함수
     *
     * @return Activity 현재 액티비티
     */
    public synchronized Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * Glide 이미지 세팅
     *
     * @param context
     * @param imgPath
     * @param imageView
     */
    public void setGlideImg(Context context, String imgPath, ImageView imageView, int resId) {
        if (imgPath != null && !imgPath.contains("https://") && !imgPath.contains("http://") && !imgPath.contains("youtube")
                && !imgPath.contains("file://")) {
            imgPath = NetInfo.URL_GLOBAL_IMAGE_PATH + imgPath;
        }

        Glide.with(context)
                .load(imgPath)
                .error(resId)
                .placeholder(resId)
                .into(imageView);
    }



    /**
     * Glide Circle 이미지 세팅
     *
     * @param context
     * @param imgPath
     * @param imageView
     */
    public void setGlideCircleImg(Context context, String imgPath, ImageView imageView, int resId) {
        if (imgPath != null && !imgPath.contains("https://") && !imgPath.contains("http://") && !imgPath.contains("youtube")
                && !imgPath.contains("file://")) {
            imgPath = NetInfo.URL_GLOBAL_IMAGE_PATH + imgPath;
        }

        RequestOptions options = new RequestOptions();
        options.circleCrop();

        Glide.with(context)
                .load(imgPath)
                .apply(options)
                .error(resId)
                .placeholder(resId)
                .into(imageView);
    }

    public void setGlideImgCenterCrop(Context context, String imgPath, ImageView imageView, int redId) {
        if(imageView == null) return;

        imageView.setOutlineProvider(new ViewOutlineProvider() {

            @Override
            public void getOutline(View view, Outline outline) {
                //왼쪽만 라운딩
                final int left = 0;
                final int top = 0;
                final int right = view.getWidth();
                final int bottom = view.getHeight();
                final int cornerRadius = 40;
                outline.setRoundRect(left, top, right, bottom, cornerRadius);

            }
        });
        imageView.setClipToOutline(true);

        if (imgPath != null && !imgPath.contains("https://") && !imgPath.contains("http://") && !imgPath.contains("youtube")
                && !imgPath.contains("file://")) {
            imgPath = NetInfo.URL_GLOBAL_IMAGE_PATH + imgPath;
        }

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        Glide.with(context)
                .load(imgPath)
                .error(redId)
                .apply(options)
                .placeholder(redId)
                .into(imageView);
    }

    /**
     * Glide 이미지 세팅
     *
     * @param context
     * @param imageView
     */
    public void setGlideImg(Context context, int redId, ImageView imageView) {
        Glide.with(context)
                .load(redId)
                .error(R.drawable.ball)
                .placeholder(R.drawable.ball)
                .into(imageView);
    }

    /**
     * 공통 프로그래스 다이알로그 노출 함수
     */
    public synchronized void showLoadingDialog(Activity activity) {

        if (activity != null && activity.isFinishing()) {
            return;
        }

        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (IllegalArgumentException ex) {
                    mProgressDialog.cancel();
                    mProgressDialog = null;
                }
            }
        }

        mProgressDialog = new Dialog(activity);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;

        mProgressDialog.getWindow().setAttributes(lpWindow);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setContentView(R.layout.view_common_loading);
        mProgressDialog.show();

//        mLoadingImageView = (FrescoImageView) mProgressDialog.findViewById(R.id.common_loading_image_view);
//        FrescoImageLoaderUtil.with().load(R.drawable.loading_dialog).setLoadingImage(false).setImageScaleType(ScalingUtils.ScaleType.FIT_CENTER).
//                setPlaceHolderRes(R.color.transparent).showImage(mLoadingImageView);

        mLoadingAnimationStatus = true;
    }


    /**
     * 공통 프로그래스 다이알로그 Dismiss 처리 함수
     */
    public synchronized void dismissLoadingDialog() {
        if (mProgressDialog != null) {
/*            if (mFrameAnimation != null && mFrameAnimation.isRunning()) {
                mFrameAnimation.stop();
                mFrameAnimation = null;
            }*/

            if (mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                } catch (IllegalArgumentException ex) {
                    mProgressDialog.cancel();
                    mProgressDialog = null;
                }
            }
            mProgressDialog = null;
            mLoadingAnimationStatus = false;
        }
    }

    /**
     * 앱 종료 처리 함수
     *
     * @param activity Activity
     */
    public synchronized void finishApplication(Activity activity) {
        JWSharePreference sharePreference = new JWSharePreference();
        try {
            LogUtil.LogD("MainActivity resetData finishAffinity!!!!!!! ");

            if (sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_FLAG, "").equals("Y")) {
                sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_FLAG, "N");
                sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_TOKEN, "");
                sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
                sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_TYPE, "");
                sharePreference.setString(JWSharePreference.PREFERENCE_SRL, null);
            }

            ActivityCompat.finishAffinity(activity);

            android.os.Process.killProcess(android.os.Process.myPid());
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

            if (activityManager != null) {
                activityManager.killBackgroundProcesses(activity.getPackageName());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getStr(int id){
        return getCurrentActivity().getResources().getString(id);
    }
}
