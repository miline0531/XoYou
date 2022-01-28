package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.genericit.mybase.xoyou2.R;


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