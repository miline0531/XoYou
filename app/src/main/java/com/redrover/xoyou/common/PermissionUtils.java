package com.redrover.xoyou.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * Created by suwun on 2017-01-09.
 * 권한 요청 클래스
 * 안드로이드 OS 6.0 부터는 사용자가 설정에서 권한을 자유롭게 변경 할 수 있으므로 ,해당 클래스를 통해서 권한 체크 및 요청을 한다.
 * @since 0, 1
 */
public class PermissionUtils {

    // 위치기반 + 폰 권한
    public static final String[] NECESSARY_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
    };

    // 폰 권한
    public static final String[] PHONE_PERMS={
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
    };

    // 카메라 권한
    public static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };

    // 연락처 권한
    public static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };

    // 위치기반 권한
    public static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // 저장소 권한
    public static final String[] STORAGE_PERMS={
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // 구글 계정
    public static final String[] GOOGLEACCOUNT_PERMS={
            Manifest.permission.GET_ACCOUNTS
    };

    // 저장소 권한 및 사진
    public static final String[] STORAGECAREMA_PERMS={
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };



    /**
     * 폰 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */

    public static boolean canAccessPhone(Context context){
        return(hasPermission(context, Manifest.permission.READ_PHONE_STATE));
    }

    public static boolean canReadPhone(Context context){
        return(hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE));
    }
    public static boolean canWhitePhone(Context context){
        return(hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }
    public static boolean canReadContacts(Context context){
        return(hasPermission(context, Manifest.permission.READ_CONTACTS));
    }
    public static boolean canWhiteRecord(Context context){
        return(hasPermission(context, Manifest.permission.RECORD_AUDIO));
    }

    /**
     * 카메라 사용 가능 여무
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canAccessCamera(Context context) {
        return(hasPermission(context, Manifest.permission.CAMERA));
    }

    /**
     * 연락처 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canAccessContacts(Context context) {
        return(hasPermission(context, Manifest.permission.READ_CONTACTS));
    }

    /**
     * 위치 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canAccessLocation(Context context) {
        return(hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION));
    }

    /**
     * 위치 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canAccessLocation2(Context context) {
        return(hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION));
    }


    /**
     * 저장소 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canAccessStorage(Context context){
        return(hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    /**
     * 구글 계정 사용 가능 여부
     * @param context
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean canGoogleAccount(Context context){
        return(hasPermission(context, Manifest.permission.GET_ACCOUNTS));
    }

    /**
     * 퍼미션 사용 가능한지 체크
     * @param context
     * @param perm  퍼미션 종류
     * @return bool ( true - 사용가능, false - 사용 불가 )
     */
    public static boolean hasPermission(Context context, String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(context, perm));
    }


}
