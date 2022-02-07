package com.redrover.xoyou.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.LongSerializationPolicy;

import java.util.ArrayList;
import java.util.List;

import com.redrover.xoyou.MymongBaseApplication;
import com.redrover.xoyou.utils.LogUtil;

/**
 * SharedPreference Class
 */
public class JWSharePreference {
    public static final String PREFERENCE_SRL = "preference_login_srl"; //user id(고유값)
    public static final String PREFERENCE_LOGIN_ID = "preference_login_id";
    public static final String PREFERENCE_USER_ID = "preference_user_id";
    public static final String PREFERENCE_USER_SEQ = "preference_user_seq"; //사용자 seq
    public static final String PREFERENCE_LOGIN_NAME = "preference_login_name";
    public static final String PREFERENCE_LOGIN_BIRTH = "preference_login_birth";
    public static final String PREFERENCE_GENDER = "preference_login_gender";
    public static final String PREFERENCE_LOGIN_TOKEN = "preference_login_token";

    public static final String PREFERENCE_AUTO_LOGIN_ID = "preference_auto_login_id";
    public static final String PREFERENCE_AUTO_LOGIN_PASS = "preference_auto_login_pass";
    public static final String PREFERENCE_AUTO_FINGER_LOGIN = "preference_auto_login_finger";

    // SharePreference Key 정의 [START]
    public static final String PREFERENCE_LOGIN_FLAG = "preference_login_flag";

    public static final String PREFERENCE_LOGIN_NICKNAME = "preference_login_nickname"; // 닉네임
    public static final String PREFERENCE_LOGIN_REFRESH_TOKEN = "preference_login_refresh_token"; // 로그인 refresh 토큰
    public static final String PREFERENCE_LOGIN_TYPE = "preference_login_type"; // AGENCY : 기관, NORMAL : 학부모
    public static final String PREFERENCE_LOGIN_TEMPORARY = "preference_login_tempory_flag"; // 임시비밀번호 사용 여부
    public static final String PREFERENCE_SEX_TYPE = "preference_sex_type"; // 회원가입 성별 여부
    public static final String PREFERENCE_SIGN_USER_KEY = "preference_sign_user_key"; // 회원가입 시 사용자 키
    public static final String PREFERENCE_POST_NUM = "preference_post_num";
    public static final String PREFERENCE_ADDRESS_STREETE = "preference_address_street";
    public static final String PREFERENCE_ADDRESS = "preference_address";
    public static final String PREFERENCE_FCM_TOKEN = "preference_fcm_token"; // fcm 토큰
    public static final String PREFERENCE_DEVICE_NUMBER = "preference_device_number"; // device num
    public static final String PREFERENCE_PERMISSION_CONFIRM = "preference_permission_confirm"; // 권한 확인 여부
    public static final String PREFERENCE_AGENCY_KEY = "preference_agency_key"; // 기관 키
    public static final String PREFERENCE_PUSH_AGREE_KEY = "preference_push_agree"; // 푸시알림 키
    public static final String PREFERENCE_JOIN_TYPE = "preference_join_type"; // 조인타입 키
    public static final String PREFERENCE_AGENCY_NAME = "preference_agency_name"; // 기관명
    public static final String PREFERENCE_MY_LOCATION_ADDRESS = "preference_my_address_location"; // 현재 나의 위치 주소
    // SharePreference Key 정의 [END]

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    private final Gson GSON;

    public JWSharePreference() {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(MymongBaseApplication.instance.getBaseContext());
        mEditor = mSharedPreference.edit();

        GsonBuilder gsonGsonBuilder = new GsonBuilder();
        gsonGsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        gsonGsonBuilder.setPrettyPrinting();
        GSON = gsonGsonBuilder.create();
    }

    /**
     * Preference에 String형태로 저장하는 함수
     *
     * @param key   저장 String Key
     * @param value 저장 String Value
     */
    public void setString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.apply();
    }

    /**
     * Preference에서 key에 맞는 value 반환 함수
     *
     * @param key          저장 String Key
     * @param defalutValue 반환 기본값
     * @return String 반환값
     */
    public String getString(String key, String defalutValue) {
        return mSharedPreference.getString(key, defalutValue);
    }

    /**
     * Preference에 Int형태로 저장하는 함수
     *
     * @param key   저장 Int Key
     * @param value 저장 Int Value
     */
    public void setInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    /**
     * Preference에서 key에 맞는 value 반환 함수
     *
     * @param key          저장 Int Key
     * @param defalutValue 반환 기본값
     * @return Int 반환값
     */
    public int getInt(String key, int defalutValue) {
        return mSharedPreference.getInt(key, defalutValue);
    }

    /**
     * Preference에 Boolean형태로 저장하는 함수
     *
     * @param key   저장 Boolean Key
     * @param value 저장 Boolean Value
     */
    public void setBoolean(String key, Boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    /**
     * Preference에서 key에 맞는 value 반환 함수
     *
     * @param key          저장 Boolean Key
     * @param defalutValue 반환 기본값
     * @return Boolean 반환값
     */
    public Boolean getBoolean(String key, Boolean defalutValue) {
        return mSharedPreference.getBoolean(key, defalutValue);
    }

    /**
     * 리스트 형태 저장
     *
     * @param key
     * @param valueList
     * @param <T>
     */
    public <T> void putList(String key, List<T> valueList) {
        if (key == null || key.isEmpty()) {
            return;
        }
        if (valueList == null) {
            mEditor.putString(key, "");
        } else {
            mEditor.putString(key, GSON.toJson(valueList));
        }
        mEditor.commit();
    }

    /**
     * 리스트 가져오기
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String key, final Class<T> cls) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        List<T> list = new ArrayList<T>();
        String gson = mSharedPreference.getString(key, "");

        if (gson.isEmpty()) {
            return list;
        } else {
            try {
                JsonArray arry = new JsonParser().parse(gson).getAsJsonArray();
                for (JsonElement jsonElement : arry) {
                    list.add(GSON.fromJson(jsonElement, cls));
                }
            } catch (Exception e) {
                LogUtil.LogD(e.toString());
            }
            return list;
        }
    }
}
