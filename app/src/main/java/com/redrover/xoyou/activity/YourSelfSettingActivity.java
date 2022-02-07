package com.redrover.xoyou.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestUpdateUser;
import com.redrover.xoyou.network.response.UpdateUserResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;


public class YourSelfSettingActivity extends CommonActivity {
    private JWSharePreference sharePreference = new JWSharePreference();
    private EditText edt_name,edt_nickname;
    private TextView tv_birthday,btn_cancel,btn_enter;
    private String nickName,name,seq,birth;
    private int gender;
    private RadioGroup gender_radiogroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_self);
        super.onCreate(savedInstanceState);


        initView();

    }

    public void initView(){

        String intentName = getIntent().getStringExtra("name");

        if(intentName != null && intentName.equals("") == false){
            String intentNickName = getIntent().getStringExtra("nickname");
            String intentSEQ = getIntent().getStringExtra("SEQ");
            int intentMW = getIntent().getIntExtra("mw",1);
            String intentBirth = getIntent().getStringExtra("birth");
            name = intentName;
            birth = intentBirth;
            gender = intentMW;
            seq = intentSEQ;
            nickName = intentNickName;
        }else{
            name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NAME,"");
            birth = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_BIRTH,"").substring(0,4);
            gender = sharePreference.getInt(JWSharePreference.PREFERENCE_GENDER,1);
            seq = sharePreference.getString(JWSharePreference.PREFERENCE_USER_SEQ, "");
            nickName = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME, "");
        }

        edt_name = findViewById(R.id.edt_name);
        edt_nickname = findViewById(R.id.edt_nickname);
        tv_birthday = findViewById(R.id.tv_birthday);
        btn_cancel =findViewById(R.id.btn_cancel);
        btn_enter =findViewById(R.id.btn_enter);
        gender_radiogroup = findViewById(R.id.gender_radiogroup);

        gender_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_radiobtn :
                        gender = 1;
                        break;
                    case R.id.female_radiobtn :
                        gender = 0;
                        break;
                }
            }
        });

        edt_name.setText(name);
        edt_nickname.setText(nickName);
        tv_birthday.setText(birth);

        if( gender == 1){
            ((RadioButton)findViewById(R.id.male_radiobtn)).setChecked(true);

        }else{
            ((RadioButton)findViewById(R.id.female_radiobtn)).setChecked(true);
        }

        tv_birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                 DatePickerDialog datePickerDialog = new DatePickerDialog(YourSelfSettingActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        birth = changeBirthdayToString(year, month, dayOfMonth).substring(0,4);
                        tv_birthday.setText(birth);
                    }
                }, 1980, 0, 1);
                datePickerDialog.show();
            }


        });



        btn_cancel.setOnClickListener(v ->{
            finish();
        });

        btn_enter.setOnClickListener(v ->{
            requestUpdateUser();
        });

    }

    public void requestUpdateUser(){
        nickName = edt_nickname.getText().toString();
        name = edt_name.getText().toString();
        birth = tv_birthday.getText().toString();

        Log.d("TEST",seq+"\t"+ nickName+"\t"+ name+"\t"+
                String.valueOf(gender)+"\t"+ birth);
        ActionRuler.getInstance().addAction(new ActionRequestUpdateUser(this,seq, nickName, name,
                String.valueOf(gender), birth, "", new ActionResultListener<UpdateUserResult>() {
            @Override
            public void onResponseResult(UpdateUserResult response) {
                try {
                    UpdateUserResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST","성공");

                        Constants.RootNICK_NAME = nickName;
                        Constants.RootSEQ = seq;
                        Constants.RootNAME = name;
                        Constants.RootMW = gender;
                        Constants.RootBIRTH = birth;

                        Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.mong_yourself_change_succss),Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        Log.d("TEST","실패");

                    }

                } catch (Exception e) {
                    Log.d("TEST","에러");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Log.d("TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    public String changeBirthdayToString(int year, int month, int dayOfMonth){
        String monthText = "";
        String dayText = "";
        if(month<9){
            monthText = "0"+(month+1);
        }else{
            monthText = String.valueOf(month+1);
        }
        if(dayOfMonth<10){
            dayText = "0"+dayOfMonth;
        }else{
            dayText = String.valueOf(dayOfMonth);
        }

        return ""+year+monthText+dayText;
    }
}