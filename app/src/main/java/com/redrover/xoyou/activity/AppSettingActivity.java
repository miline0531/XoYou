package com.redrover.xoyou.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.redrover.xoyou.R;


public class AppSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initView();

    }

    public void initView(){

    }
}