package com.redrover.xoyou.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.view.RecordDialog;

public class ADevTempActivity extends CommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_adev_temp);
        super.onCreate(savedInstanceState);

        initView();
        permCheck();
    }
    private CheckBox cb_1_1,cb_1_2,cb_1_3;


    public void initView() {
        cb_1_1 = findViewById(R.id.cb_1_1);
        cb_1_2 = findViewById(R.id.cb_1_2);
        cb_1_3 = findViewById(R.id.cb_1_3);
        findViewById(R.id.btn_cb_1_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.btn_cb_1_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.btn_cb_1_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_1_3.isChecked()){
                    cb_1_3.setChecked(false);
                }else{

                }
            }
        });
    }

    private String recordFile1,recordFile2 = null;
    public void showRecordDialog(String title, String path, int index){
        RecordDialog dialog = new RecordDialog(this,title,path,index, new RecordDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick(int index, String path) {
                if(index == 1){
                    recordFile1 = path;
                    ((CheckBox)findViewById(R.id.cb_1_1)).setChecked(true);
                }else{
                    recordFile2 = path;
                    ((CheckBox)findViewById(R.id.cb_1_2)).setChecked(true);
                }
            }

            @Override
            public void onNegativeClick() {

            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }



    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    public static boolean Prem = false;
    public static boolean isPerm(){
        return Prem;
    }
    private void permCheck(){
        Prem = grantExternalStoragePermission();
    }
    /**********************
     * Permission Check
     **********************/
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                Log.v("ifeelbluu","Permission is granted");
                return true;
            }else{
                Log.v("ifeelbluu","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.RECORD_AUDIO}, 1);

                return false;
            }
        }else{
            Toast.makeText(this, "External Storage Permission is Grant",
                    Toast.LENGTH_SHORT).show();
            Log.d("ifeelbluu", "External Storage Permission is Grant ");

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("ifeelbluu","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
        }

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // You have permission

                    // 오버레이 설정창 이동 후 이벤트 처리합니다.

                }
            }
        }
    }
}