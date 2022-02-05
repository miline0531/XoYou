package kr.co.genericit.mybase.xoyou2.popup;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.AddRelationActivity;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.adapter.popup.FragmentStep1ListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.popup.FragmentStep2ListAdapter;
import kr.co.genericit.mybase.xoyou2.common.ActivityEx;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.model.Contactobj;
import kr.co.genericit.mybase.xoyou2.model.QaListObj;
import kr.co.genericit.mybase.xoyou2.model.WeListObj;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestAddRelation;
import kr.co.genericit.mybase.xoyou2.network.response.AddRelationResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;


public class ContractInsertPopUp extends ActivityEx {
    CommonUtil dataSet = CommonUtil.getInstance();
    private Contactobj obj;
    private EditText relationEdt, nameEdt, tv_birthday , edt_phone;
    private String gender = "0";
    private RadioGroup radioGroup;
    private String IN_SEQ="",USER_ID="",GWANGYE="",NICK_NAME="",NAME="",MW="1",BIRTH_DATE="",IMAGE_URL="" , callNumber = "";
    private JWSharePreference jwSharePreference;
    private boolean isFinish = false;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.activity_contractpopup);

        jwSharePreference = new JWSharePreference();
        obj = getIntent().getParcelableExtra("obj");

        relationEdt = findViewById(R.id.edt_relation);
        nameEdt = findViewById(R.id.edt_name);
        tv_birthday = findViewById(R.id.tv_birthday);
        radioGroup = findViewById(R.id.radioGroup);
        edt_phone = findViewById(R.id.edt_phone);

        relationEdt.setText(obj.getName());
        nameEdt.setText(obj.getName());
        edt_phone.setText(obj.getNumber());

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);


        findViewById(R.id.btn_close).setOnClickListener(btnListener);
        findViewById(R.id.btn_cancel).setOnClickListener(btnListener);
        findViewById(R.id.btn_setup).setOnClickListener(btnListener);

    }

    //라디오 그룹 클릭 리스너
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if(i == R.id.rg_btn1){
                gender = "0";
            } else if(i == R.id.rg_btn2){
                gender = "1";
            }
        }
    };

    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_close:
                    Log.e("SKY", "-- btn_close --");
                    finish();
                    break;
                case R.id.btn_cancel:
                    Log.e("SKY", "-- btn_cancel --");
                    finish();
                    break;
                case R.id.btn_setup:
                    Log.e("SKY", "-- btn_setup --");
                    SkyLog.d("GWANGYE :: " + relationEdt.getText().toString());
                    SkyLog.d("NICK_NAME :: " + nameEdt.getText().toString());
                    SkyLog.d("NAME :: " + nameEdt.getText().toString());
                    SkyLog.d("BIRTH_DATE :: " + tv_birthday.getText().toString());

                    IN_SEQ = jwSharePreference.getString(JWSharePreference.PREFERENCE_USER_SEQ,"");
                    USER_ID = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
                    GWANGYE = relationEdt.getText().toString();
                    NAME = nameEdt.getText().toString();
                    NICK_NAME = NAME;
                    MW = gender;
                    BIRTH_DATE = tv_birthday.getText().toString();
                    IMAGE_URL = "";
                    callNumber = edt_phone.getText().toString();


                    if(GWANGYE.equals("")||NAME.equals("")||NICK_NAME.equals("")||BIRTH_DATE.equals("")||callNumber.equals("")){
                        //todo 입력오류 얼럿!!
                        Toast.makeText(ContractInsertPopUp.this , "필수항목을 모두 입력해주세요." , Toast.LENGTH_SHORT).show();
                    }else{
                        requestAddRelationship(); //등록

                    }
                    break;
            }
        }
    };

    private void requestAddRelationship(){
        final String year = BIRTH_DATE.substring(0,4);
        CommandUtil.getInstance().showLoadingDialog(ContractInsertPopUp.this);
        SkyLog.d(""+IN_SEQ+"\t"+USER_ID+"\t"+GWANGYE+"\t"+NICK_NAME+"\t"+NAME+"\t"+MW+"\t"+year+"\t"+IMAGE_URL);
        ActionRuler.getInstance().addAction(new ActionRequestAddRelation(this,IN_SEQ,USER_ID,GWANGYE,NICK_NAME,NAME,MW,year,IMAGE_URL,callNumber, new ActionResultListener<AddRelationResult>() {
            @Override
            public void onResponseResult(AddRelationResult response) {
                try {
                    CommandUtil.getInstance().dismissLoadingDialog();
                    AddRelationResult result = response;

                    SkyLog.d("관계인 등록 결과 :: " + result);
                    String message = "";
                    if(result.isSuccess()){
                        //성공
                        message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_success);
                        isFinish = true;
                    }else{
                        message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_fail);
                    }

                    Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
                    if(isFinish) finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("관계인 등록 실패 !!");
            }
        }));
        ActionRuler.getInstance().runNext();

    }
}





