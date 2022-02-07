package com.redrover.xoyou.common;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.redrover.xoyou.R;
import com.redrover.xoyou.utils.CommandUtil;

public class CommonActivity extends AppCompatActivity {
//    public CommonBackExit mCommonBackExit;
//    public CommonProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommandUtil.getInstance().setCurrentActivity(this);
        getSupportActionBar().hide();
//        mProgressDialog = new CommonProgressDialog(this);
//        mCommonBackExit = new CommonBackExit(this);

        if(findViewById(R.id.btn_back) != null){
            findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mHandler.removeCallbacksAndMessages(null);
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
////            mProgressDialog.cancel();
//        }
    }

    public void showProgressDialog() {
//        LogUtil.d("CommonActivity showProgressDialog 진입");
//        if (mProgressDialog != null && activity != null && !activity.isFinishing())
//            mProgressDialog.show();
    }

    public void hideProgressDialog() {
//        LogUtil.d("CommonActivity hideProgressDialog 진입 ");
//        if (mProgressDialog != null && activity != null && !activity.isFinishing())
//            mProgressDialog.hide();
    }

    public void handleMessage(Message msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideProgressDialog();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        hideProgressDialog();
    }
}
