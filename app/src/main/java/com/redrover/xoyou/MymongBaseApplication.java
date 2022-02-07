package com.redrover.xoyou;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Application
 */
public class MymongBaseApplication extends Application {

    public static MymongBaseApplication instance;

    public static boolean isBackground = false;

    public MymongBaseApplication getInstance() {
        return instance;
    }

    public MymongBaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

//        // FrescoImage 초기화
//        FrescoImageLoaderUtil.initialize(this);

        // Ver8.0 이상 Push 채널 등록
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            FirebaseMessagingService.createPushChannel(this);
        }

        IntentFilter screenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isBackground) {
                    isBackground = true;
                    notifyBackground();
                }
            }
        }, screenOffFilter);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackground) {
                    isBackground = false;
                    notifyForeground(activity);
                }
            }

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackground = true;
            notifyBackground();
        }
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    private void notifyBackground(){
        Log.d("STATUS", "AppStatusCheck :: APP_STATUS_BACKGROUND");
    }

    private void notifyForeground(Activity activity){
        Log.d("STATUS", "AppStatusCheck :: APP_STATUS_FOREGROUND::");
    }
}
