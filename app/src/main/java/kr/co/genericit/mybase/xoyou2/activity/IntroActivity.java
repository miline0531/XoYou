package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.genericit.mybase.xoyou2.R;


public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        initView();

    }

    public void initView(){

    }
}