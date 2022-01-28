package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestAddUser;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestUpdateUser;
import kr.co.genericit.mybase.xoyou2.network.response.AddUserResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateUserResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class AddUserActivity extends CommonActivity {
    private EditText edt_nickname,edt_name,edt_birth;
    private RadioGroup gender_radiogroup;
    private TextView btn_cancel,btn_setup;
    private ImageView btn_back;
    private int gender, type=-1,SEQ; //type이 1일경우 수정, -1이면 추가
    private String nickname,name,birth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_user);
        super.onCreate(savedInstanceState);


        setRadiogroup();

        initView();

        userId = new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");

        Log.v("ifeelbluu","userId :: " + userId);

        btn_cancel.setOnClickListener(v ->{
            finish();
        });
        btn_setup.setOnClickListener(v ->{
            if(type==-1){
                requestAddUser();
            }else{
                requestUpdateUser();
            }
        });

    }

    public void setRadiogroup(){
        gender_radiogroup = findViewById(R.id.gender_radiogroup);
        gender_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_radiobtn:
                        gender = 1;
                        break;
                    case R.id.female_radiobtn:
                        gender = 0;
                        break;
                }
            }
        });
    }

    public void initView(){
        edt_nickname = findViewById(R.id.edt_nickname);
        edt_name = findViewById(R.id.edt_name);
        edt_birth = findViewById(R.id.edt_birth);

        btn_back = findViewById(R.id.btn_back);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_setup = findViewById(R.id.btn_setup);
        //수정
        boolean isUpdate = getIntent().getBooleanExtra("update", false);
        if(isUpdate){

            nickname = getIntent().getStringExtra("nickname");
            name = getIntent().getStringExtra("name");
            birth = getIntent().getStringExtra("birth");
            int mw = getIntent().getIntExtra("mw",0);
            SEQ = getIntent().getIntExtra("SEQ",0);

            edt_nickname.setText(nickname);
            edt_name.setText(name);
            edt_birth.setText(birth);
            if(mw==1){
                gender_radiogroup.check(R.id.male_radiobtn);
            }else{
                gender_radiogroup.check(R.id.female_radiobtn);
            }

            ((TextView)findViewById(R.id.activity_title)).setText(CommandUtil.getInstance().getStr(R.string.add_user_change_success));
            btn_setup.setText(CommandUtil.getInstance().getStr(R.string.add_user_change_btn));

            type = 1;
        }

    }

    private void requestUpdateUser(){
        nickname = edt_nickname.getText().toString();
        name = edt_name.getText().toString();
        birth = edt_birth.getText().toString();

        ActionRuler.getInstance().addAction(new ActionRequestUpdateUser(this,String.valueOf(SEQ), nickname, name,
                String.valueOf(gender), birth, "", new ActionResultListener<UpdateUserResult>() {
            @Override
            public void onResponseResult(UpdateUserResult response) {
                try {
                    UpdateUserResult result = response;

                    if(result.isSuccess()){
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.add_user_change),Toast.LENGTH_SHORT).show();
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

    private void requestAddUser(){
        nickname = edt_nickname.getText().toString();
        name = edt_name.getText().toString();
        birth = edt_birth.getText().toString();

        Log.d("TEST",nickname+"\t"+userId);
        String url = "";
        ActionRuler.getInstance().addAction(new ActionRequestAddUser(this,userId,"5", nickname, name,gender, birth,url, new ActionResultListener<AddUserResult>() {
            @Override
            public void onResponseResult(AddUserResult response) {
                try {
                    AddUserResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST","성공");
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.add_user_reg_success),Toast.LENGTH_SHORT).show();
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
}