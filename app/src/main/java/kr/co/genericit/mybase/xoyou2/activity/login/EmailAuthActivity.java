package kr.co.genericit.mybase.xoyou2.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class EmailAuthActivity extends CommonActivity {
    private String email;
    private String type;
    private String u_id;
    private String code="";
    private String authCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        Intent i = getIntent();
        type = i.getStringExtra("type");
        u_id = i.getStringExtra("u_id");
        authCode = i.getStringExtra("code");



        EditText emailAuthCodeEditText = findViewById(R.id.emailauth_authcode_edittext);
        Button emailAuthButton = findViewById(R.id.emailauth_btn);

        emailAuthButton.setOnClickListener(v ->{
            code = emailAuthCodeEditText.getText().toString();

            if(code.equals("")){
                Toast.makeText(EmailAuthActivity.this, CommandUtil.getInstance().getStr(R.string.mong_email_auth), Toast.LENGTH_SHORT).show();
            }else{
                checkEmailAuthCode();
            }
        });

        ImageView backButton = findViewById(R.id.emailauth_back_btn);
        backButton.setOnClickListener(v ->{
            finish();
        });

    }


    public void checkEmailAuthCode(){
        if(code.equals(authCode)){
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(EmailAuthActivity.this, CommandUtil.getInstance().getStr(R.string.mong_email_auth_error), Toast.LENGTH_SHORT).show();
        }
    }
}