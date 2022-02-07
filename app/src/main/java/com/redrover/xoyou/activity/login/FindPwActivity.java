package com.redrover.xoyou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestEmailAuth;
import com.redrover.xoyou.network.request.ActionRequestFindPw;
import com.redrover.xoyou.network.response.EmailAuthResult;
import com.redrover.xoyou.network.response.FindPwResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;

public class FindPwActivity extends CommonActivity {
    private String email = "";
    private String type = "pw";
    private String u_id;
    private ConstraintLayout sendMailLayout;
    private ConstraintLayout changePwLayout;
    private final int FIND_PW_REQUEST_CODE = 100;
    private String pw = "";
    private String checkPw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        sendMailLayout = findViewById(R.id.findpw_sendmail_layout);

        EditText idEditText = findViewById(R.id.findpw_id_edittext);
        EditText emailEditText = findViewById(R.id.findpw_email_edittext);

        Button sendMailButton = findViewById(R.id.findpw_sendmail_btn);

        sendMailButton.setOnClickListener(v -> {
            u_id = idEditText.getText().toString();
            email = emailEditText.getText().toString();

            if(u_id.equals("")||email.equals("")){
                Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.mong_email_find_id_pw_check),Toast.LENGTH_SHORT).show();
            }else{
                requestEmailAuth();
            }
        });

        ImageView backButton = findViewById(R.id.findpw_back_btn);
        backButton.setOnClickListener(v ->{
            finish();
        });

        changePwLayout = findViewById(R.id.findpw_changepw_layout);

        EditText newPwEditText = findViewById(R.id.findpw_newpw_edittext);
        EditText checkNewPwEditText = findViewById(R.id.findpw_checknewpw_edittext);

        Button changePwButton = findViewById(R.id.findpw_changepw_btn);
        changePwButton.setOnClickListener(v->{
            pw = newPwEditText.getText().toString();
            checkPw = checkNewPwEditText.getText().toString();

            if(pw.equals("")||checkPw.equals("")){
                Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_email_find_pw_change),Toast.LENGTH_SHORT).show();
                Log.d("TEST","1");
            }else{
                if(pw.equals(checkPw)){
                    requestChangePassWord();
                    Log.d("TEST","2");
                }else{
                    Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_email_find_pw_error),Toast.LENGTH_SHORT).show();
                    Log.d("TEST","3");
                }
            }
        });


    }
    public void requestEmailAuth(){
        Log.d("TEST","type : " + type);
        Log.d("TEST","u_id : " + u_id);
        Log.d("TEST","email : " + email);
        ActionRuler.getInstance().addAction(new ActionRequestEmailAuth(this,type, u_id, email, new ActionResultListener<EmailAuthResult>() {
            @Override
            public void onResponseResult(EmailAuthResult response) {
                try {
                    EmailAuthResult result = response;

                    if(result.isResult()){
                        Intent i = new Intent(FindPwActivity.this,EmailAuthActivity.class);
                        i.putExtra("type",type);
                        i.putExtra("email",email);
                        i.putExtra("code",result.getResp());
                        Log.d("TEST","code : "+ result.getResp());
                        startActivityForResult(i,FIND_PW_REQUEST_CODE);
                    }else{
//                        if(result.getError().equals("90001")){
//                            Toast.makeText(getApplicationContext(),"이메일과 아이디가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
//                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("이메일을 확인 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();

    }

    public void requestChangePassWord(){
        ActionRuler.getInstance().addAction(new ActionRequestFindPw(this,u_id,email,pw, new ActionResultListener<FindPwResult>() {
            @Override
            public void onResponseResult(FindPwResult response) {
                try {
                    FindPwResult result = response;

                    if(result.isResult()){
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_email_find_pw_change_success),Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("이메일을 확인 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FIND_PW_REQUEST_CODE&&resultCode==RESULT_OK){
            changePwLayout.setVisibility(View.VISIBLE);
            sendMailLayout.setVisibility(View.GONE);
        }
    }
}