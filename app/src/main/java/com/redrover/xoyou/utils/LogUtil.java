package com.redrover.xoyou.utils;

import android.util.Log;

import com.redrover.xoyou.BuildInfo;


/**
 * Debug Log 출력 Class
 */
public class LogUtil {

    public static String TAG = "LogUtil";

    public static void LogD(String str) {
        if (!BuildInfo.FLAG_DEBUG_MODE) {
            return;
        }

        StringBuffer log_tag = new StringBuffer();
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        log_tag.append(TAG);

        if (stackTrace.length > 1) {
            log_tag.append(stackTrace[1].getFileName());
        }

        Log.d(log_tag.toString(), str);
    }


    public static void LogE(String str) {
        if (!BuildInfo.FLAG_DEBUG_MODE) return;

        StringBuffer log_tag = new StringBuffer();
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        log_tag.append(TAG);

        if (stackTrace.length > 1) {
            log_tag.append(stackTrace[1].getFileName());
        }

        Log.e(log_tag.toString(), str);
    }

    private static void LogESub(String str) {
        if (!BuildInfo.FLAG_DEBUG_MODE) return;

        StringBuffer log_tag = new StringBuffer();
        StackTraceElement[] stackTrace = new Exception().getStackTrace();

        log_tag.append(TAG);

        if (stackTrace.length > 2) {
            log_tag.append(stackTrace[2].getFileName());
        }

        Log.e(log_tag.toString(), str);
    }

    public static void LogE(Exception e) {
        if (!BuildInfo.FLAG_DEBUG_MODE) return;

        LogESub(Log.getStackTraceString(e));
    }
}
