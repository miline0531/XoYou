package com.redrover.xoyou.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.detail.MyMongRegistActivity;
import com.redrover.xoyou.activity.detail.MymongDetailActivity;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestLogin;
import com.redrover.xoyou.network.response.LoginResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.view.PwAuthDialog;

public class ManageDetailActivity extends AppCompatActivity {
    /***
     * 세부분석 보기
     * 인증등록
     * 판매하기
     * 닫기
     */
    private TextView txt_analyze;
    private TextView txt_keep;
    private TextView txt_free_reg;
    private TextView txt_certify_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_detail);
        initView();
    }

    private void initView(){
        txt_analyze = findViewById(R.id.txt_analyze);
        txt_keep = findViewById(R.id.txt_keep);
        txt_free_reg = findViewById(R.id.txt_free_reg);
        txt_certify_reg = findViewById(R.id.txt_certify_reg);

//        if(type == 0){
//            Intent i = new Intent(getActivity(), MymongDetailActivity.class);
//            startActivity(i);
//            edt_keyword.setText("");
//        }else if(type == 1){
//            if(Constants.isBioMetric){
//                String isFinger = sharePreference.getString(PREFERENCE_AUTO_FINGER_LOGIN, "N");
//                if(isFinger.equals("Y")){
//                    auth();
//                    return;
//                }
//            }
//            showCustomDialog();
//
//        }else {
//
//        }

//        BottomSheetData item = new BottomSheetData(0,"세부분석 보기",false);
////                mSelectDataStory.add(item);
////                item = new BottomSheetData(1,"인증등록",false);
////                mSelectDataStory.add(item);
////                item = new BottomSheetData(2,"닫기",false);
        txt_analyze.setOnClickListener(v->{
            //원 그래프 로 이동
            Intent i = new Intent(getApplicationContext(), MymongDetailActivity.class);
            startActivity(i);
            finish();
        });
        txt_keep.setOnClickListener(v->{
            // 마이몽하우스 꿈보관
            Toast.makeText(getApplicationContext(), "꿈보관", Toast.LENGTH_SHORT).show();
        });
        txt_free_reg.setOnClickListener(v->{
            //몽스토어 꿈 무료등록
            Toast.makeText(getApplicationContext(), "무료등록", Toast.LENGTH_SHORT).show();
        });
        txt_certify_reg.setOnClickListener(v->{
            //몽스토어 꿈 인증등록
            showCustomDialog();

            // 비밀번호 인증 다이얼로그 -> 생체인증 엑티비티 이동 MymongRegisterActivty
        });
    }
    public void showCustomDialog(){
        PwAuthDialog dialog = new PwAuthDialog(this, new PwAuthDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick(String pw) {
                requestPwAuth(pw);
            }

            @Override
            public void onNegativeClick() {

            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void requestPwAuth(String pw){
        final JWSharePreference sharePreference = new JWSharePreference();
        ActionRuler.getInstance().addAction(new ActionRequestLogin(this, sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,""), pw, new ActionResultListener<LoginResult>() {
            @Override
            public void onResponseResult(LoginResult response) {

                LoginResult result = response;
                if(result.isResult()){
                    Toast.makeText(getApplicationContext(),"비밀번호 인증 완료",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MyMongRegistActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d("!TEST","비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void onResponseError(String message) { }
        }));
        ActionRuler.getInstance().runNext();
    }

}