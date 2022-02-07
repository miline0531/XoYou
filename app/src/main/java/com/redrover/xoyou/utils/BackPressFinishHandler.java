package com.redrover.xoyou.utils;

import android.app.Activity;
import android.widget.Toast;

import com.redrover.xoyou.R;

public class BackPressFinishHandler {
    private final int BACK_KEY_DURATION = 1300;
    private long backKeyPressedTime = 0;
    private Activity mActivity;

    public BackPressFinishHandler(Activity context) {
        this.mActivity = context;
    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + BACK_KEY_DURATION) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + BACK_KEY_DURATION) {
            CommandUtil.getInstance().finishApplication(mActivity);
            //  CommandUtil.getInstance().cancelToastPopup();
        }
    }

    public void showGuide() {
        //Toast.makeText(mActivity,mActivity.getResources().getString(R.string.str_guide_app_finish),Toast.LENGTH_SHORT).show();
        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.str_guide_app_finish), Toast.LENGTH_SHORT).show();
    }
}
