package com.redrover.xoyou.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestEmailAuth;
import com.redrover.xoyou.network.request.ActionRequestFindId;
import com.redrover.xoyou.network.response.EmailAuthResult;
import com.redrover.xoyou.network.response.FindIdResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;

public class FindIdActivity extends CommonActivity {
    private String email = "";
    private String type = "id";
    private String u_id;
    private final int FIND_ID_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        EditText emailEditText = findViewById(R.id.findid_email_edittext);
        Button findIdButton = findViewById(R.id.findid_btn);

        findIdButton.setOnClickListener(v ->{
            email = emailEditText.getText().toString();

            Log.d("!!TEST","hi");
            if(email.equals("")){
                Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.mong_email_find_id),Toast.LENGTH_SHORT).show();
            }else{
                requestEmailAuth();
            }
        });


        ImageView backButton = findViewById(R.id.findid_back_btn);
        backButton.setOnClickListener(v ->{
            finish();
        });


    }



    public void requestEmailAuth(){
        ActionRuler.getInstance().addAction(new ActionRequestEmailAuth(this,type, u_id, email, new ActionResultListener<EmailAuthResult>() {
            @Override
            public void onResponseResult(EmailAuthResult response) {
                try {
                    EmailAuthResult result = response;

                    if(result.isResult()){
                        Intent i = new Intent(FindIdActivity.this,EmailAuthActivity.class);
                        i.putExtra("type",type);
                        i.putExtra("email",email);
                        i.putExtra("code",result.getResp());
                        Log.d("@TEST","code : " + result.getResp());
                        Log.d("@TEST","email : " + email);
                        startActivityForResult(i,FIND_ID_REQUEST_CODE);
                    }else{
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_email_find_id_check),Toast.LENGTH_SHORT).show();
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

    public void requestFindId(){

        ActionRuler.getInstance().addAction(new ActionRequestFindId(this, email, new ActionResultListener<FindIdResult>() {
            @Override
            public void onResponseResult(FindIdResult response) {
                try {
                    FindIdResult result = response;
                    if(result.isResult()){
                        TextView resultTextView = findViewById(R.id.findid_result_textview);
                        resultTextView.setText(CommandUtil.getInstance().getStr(R.string.mong_email_find_id_result)+result.getResp().getU_id());
                        resultTextView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("아이디찾기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK&&requestCode==FIND_ID_REQUEST_CODE){
            requestFindId();
        }
    }
}