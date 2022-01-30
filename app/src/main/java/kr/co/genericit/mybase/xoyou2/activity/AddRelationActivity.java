package kr.co.genericit.mybase.xoyou2.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestAddRelation;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestUpdateRelation;
import kr.co.genericit.mybase.xoyou2.network.response.AddRelationResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateRelationResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;


public class AddRelationActivity extends CommonActivity implements View.OnClickListener {
    private int updateId = -1,seq = -1;
    private EditText relationEdt, nameEdt, tv_birthday;
    private TextView setupBtn, btn_cancel;
    private ImageView backBtn;
    private RelativeLayout btn_birthday;
    private String IN_SEQ="",USER_ID="",GWANGYE="",NICK_NAME="",NAME="",MW="1",BIRTH_DATE="",IMAGE_URL="";
    private DatePickerDialog datePickerDialog;
    private JWSharePreference jwSharePreference;
    private Button btn_contract;
    private final int CONTRACT_REQUEST_IDX = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_relation);
        super.onCreate(savedInstanceState);

        initView();


//        Calendar c = Calendar.getInstance();
//        int mYear = c.get(Calendar.YEAR);
//        int mMonth = c.get(Calendar.MONTH);
//        int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                BIRTH_DATE = changeBirthdayToString(year, month, dayOfMonth);
//                birthdayText.setText(BIRTH_DATE);
//            }
//        }, 1970, 0, 1);
//

    }

    private void requestUpdateRelationship(){
        {
            final String YEAR = BIRTH_DATE.substring(0,4);
            final String SEQ = String.valueOf(seq);


            ActionRuler.getInstance().addAction(new ActionRequestUpdateRelation(this,SEQ,IN_SEQ,GWANGYE,NICK_NAME,NAME,MW,YEAR,IMAGE_URL, new ActionResultListener<UpdateRelationResult>() {
                @Override
                public void onResponseResult(UpdateRelationResult response) {
                    try {
                        UpdateRelationResult result = response;

                        String message = "";
                        if(result.isSuccess()){
                            //성공
                            message = CommandUtil.getInstance().getStr(R.string.add_relation_change_success);

                        }else{
                            message = CommandUtil.getInstance().getStr(R.string.add_relation_change_fail);
                        }

                        CommandUtil.getInstance().showCommonOneButtonDialog(AddRelationActivity.this, message, CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                            @Override
                            public void onClick(int button) {
                                finish();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponseError(String message) {
                    LogUtil.LogD("관계인 수정 실패 !!");
                }
            }));
            ActionRuler.getInstance().runNext();

        }
    }


    boolean isFinish = false;
    private void requestAddRelationship(){
        final String year = BIRTH_DATE.substring(0,4);

        SkyLog.d(""+IN_SEQ+"\t"+USER_ID+"\t"+GWANGYE+"\t"+NICK_NAME+"\t"+NAME+"\t"+MW+"\t"+year+"\t"+IMAGE_URL);
        ActionRuler.getInstance().addAction(new ActionRequestAddRelation(this,IN_SEQ,USER_ID,GWANGYE,NICK_NAME,NAME,MW,year,IMAGE_URL, new ActionResultListener<AddRelationResult>() {
            @Override
            public void onResponseResult(AddRelationResult response) {
                try {
                    AddRelationResult result = response;

                    String message = "";
                    if(result.isSuccess()){
                        //성공
                        message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_success);
                        isFinish = true;
                    }else{
                        message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_fail);
                    }

                    CommandUtil.getInstance().showCommonOneButtonDialog(AddRelationActivity.this, message, CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                        @Override
                        public void onClick(int button) {
                            if(isFinish) finish();
                        }
                    });
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


    public void initView(){
        relationEdt = findViewById(R.id.edt_relation);
        nameEdt = findViewById(R.id.edt_name);
        tv_birthday = findViewById(R.id.tv_birthday);
        backBtn = findViewById(R.id.btn_back);
        setupBtn = findViewById(R.id.btn_setup);
        btn_birthday = findViewById(R.id.btn_birthday);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_contract = findViewById(R.id.btn_contract);

        jwSharePreference = new JWSharePreference();

        //수정
        seq = getIntent().getIntExtra("seq",-1);

        if(seq != -1){
            GWANGYE=getIntent().getStringExtra("relation");
            NAME=getIntent().getStringExtra("name");
            NICK_NAME=NAME;
            BIRTH_DATE=getIntent().getStringExtra("birth");

            relationEdt.setText(GWANGYE);
            nameEdt.setText(NAME);
            tv_birthday.setText(BIRTH_DATE);

            setupBtn.setText("수정");

            ((TextView)findViewById(R.id.activity_title)).setText(CommandUtil.getInstance().getStr(R.string.add_relation_change));
        }

        btn_cancel.setOnClickListener(this);
        btn_contract.setOnClickListener(this);
        setupBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_contract:
                Intent it = new Intent(AddRelationActivity.this , ContactActivity.class);
                it.putExtra("" , "");
                startActivityForResult(it , CONTRACT_REQUEST_IDX);
                break;
            case R.id.btn_setup:
                SkyLog.d("GWANGYE :: " + relationEdt.getText().toString());
                SkyLog.d("NICK_NAME :: " + nameEdt.getText().toString());
                SkyLog.d("NAME :: " + nameEdt.getText().toString());
                SkyLog.d("BIRTH_DATE :: " + tv_birthday.getText().toString());

                IN_SEQ = jwSharePreference.getString(JWSharePreference.PREFERENCE_USER_SEQ,"");
                USER_ID = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
                GWANGYE = relationEdt.getText().toString();
                NAME = nameEdt.getText().toString();
                NICK_NAME = NAME;
                MW = "1";
                BIRTH_DATE = tv_birthday.getText().toString();
                IMAGE_URL = "";

                if(GWANGYE.equals("")||NAME.equals("")||NICK_NAME.equals("")||BIRTH_DATE.equals("")){
                    //todo 입력오류 얼럿!!
                    Toast.makeText(AddRelationActivity.this , "필수항목을 모두 입력해주세요." , Toast.LENGTH_SHORT).show();
                }else{
                    if(seq == -1){
                        requestAddRelationship(); //등록
                    }else{
                        requestUpdateRelationship();//수정
                    }

                }
                break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case CONTRACT_REQUEST_IDX:
                    SkyLog.d("name :: " + data.getStringExtra("name"));
                    SkyLog.d("number :: " + data.getStringExtra("number"));
                    relationEdt.setText(data.getStringExtra("name"));

                    break;
            }
        }
    }

}