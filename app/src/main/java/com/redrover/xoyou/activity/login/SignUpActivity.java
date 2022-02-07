package com.redrover.xoyou.activity.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.SignUpInfo;
import com.redrover.xoyou.network.request.ActionRequestAddUser;
import com.redrover.xoyou.network.request.ActionRequestEmailAuth;
import com.redrover.xoyou.network.request.ActionRequestSignUp;
import com.redrover.xoyou.network.request.ActionRequestSignUp2;
import com.redrover.xoyou.network.response.AddUserResult;
import com.redrover.xoyou.network.response.EmailAuthResult;
import com.redrover.xoyou.network.response.SignUpResult;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import com.redrover.xoyou.view.CommonPopupDialog;
import okhttp3.ResponseBody;

public class SignUpActivity extends CommonActivity {
    private final String TAG = "!SignUpActivity";
    private String id = "";
    private String beforeCheckId = "";
    private String birth = "";
    private String pw = "";
    private String checkPw = "";
    private String email = "";
    private String beforeAuthEmail = "";
    private String name = "";
    private String emailAuthCode = "";
    private String code = "";
    private int gender = 0;
    private int boxAll = 0;
    private int box1 = 0;
    private int box2 = 0;
    private int box3 = 0;
    private int box4 = 0;
    private CheckBox allCheckBox;
    private CheckBox termCheckBox;
    private CheckBox privacyCheckBox;
    private CheckBox locationCheckBox;
    private CheckBox adCheckBox;
    private LinearLayout emailAuthLayout;
    private EditText idEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        idEditText = findViewById(R.id.id_edittext);
        EditText pwEditText = findViewById(R.id.pw_edittext);
        EditText checkPwEditText = findViewById(R.id.check_pw_edittext);
        EditText emailEditText = findViewById(R.id.email_edittext);
        EditText nameEditText = findViewById(R.id.name_edittext);
        EditText codeEditText = findViewById(R.id.code_edittext);

        emailAuthLayout = findViewById(R.id.email_auth_layout);


        TextView checkIdButton = findViewById(R.id.check_id_exist_btn);
        checkIdButton.setOnClickListener(v ->{
            beforeCheckId = idEditText.getText().toString();
            if(beforeCheckId.equals("")){
            }else{
                checkIdExist();
            }
        });

        TextView dateTextView = findViewById(R.id.birth_textview);
        TextView dateButton = findViewById(R.id.birth_btn);

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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

                dateTextView.setText(""+year+monthText+dayText);
            }
        }, 1970, 0, 1);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });


        allCheckBox = findViewById(R.id.user_all_checkbox);
        termCheckBox = findViewById(R.id.user_terms_need_checkbox);
        privacyCheckBox = findViewById(R.id.user_privacy_need_checkbox);
        locationCheckBox = findViewById(R.id.user_location_need_checkbox);
        adCheckBox = findViewById(R.id.user_ad_optional_checkbox);

        allCheckBox.setOnClickListener(v -> {
            if(allCheckBox.isChecked()){
                boxAll = 1;
                box1 = 1;
                box2 = 1;
                box3 = 1;
                box4 = 1;
                termCheckBox.setChecked(true);
                privacyCheckBox.setChecked(true);
                locationCheckBox.setChecked(true);
                adCheckBox.setChecked(true);
            }else{
                boxAll = 0;
                box1 = 0;
                box2 = 0;
                box3 = 0;
                box4 = 0;
                termCheckBox.setChecked(false);
                privacyCheckBox.setChecked(false);
                locationCheckBox.setChecked(false);
                adCheckBox.setChecked(false);
            }
        });

        termCheckBox.setOnClickListener(v -> {
            if (((CheckBox)v).isChecked()) {
                box1 = 1;
            } else {
                box1 = 0;
            }
            checkAllCheckBox();
        });

        privacyCheckBox.setOnClickListener(v -> {
            if (((CheckBox)v).isChecked()) {
                box2 = 1;
            } else {
                box2 = 0;
            }
            checkAllCheckBox();
        });

        locationCheckBox.setOnClickListener(v -> {
            if (((CheckBox)v).isChecked()) {
                box3 = 1;
            } else {
                box3 = 0;
            }
            checkAllCheckBox();
        });

        adCheckBox.setOnClickListener(v -> {
            if (((CheckBox)v).isChecked()) {
                box4 = 1;
            } else {
                box4 = 0;
            }

            checkAllCheckBox();
        });




        //성별
        RadioGroup rg = findViewById(R.id.gender_radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.male_radiobtn :
                        gender = 1;
                        break;
                    case R.id.female_radiobtn:
                        gender = 0;
                        break;
                }
            }
        });


        TextView emailAuthButton = findViewById(R.id.email_auth_btn);
        TextView codeAuthButton = findViewById(R.id.code_auth_btn);

        emailAuthButton.setOnClickListener(v ->{
            beforeAuthEmail = emailEditText.getText().toString();
            if(beforeAuthEmail.equals("")){

            }else{
                if(isValidEmail(beforeAuthEmail)){
                    requestEmailAuth();
                }else{
                    Toast.makeText(SignUpActivity.this,CommandUtil.getInstance().getStr(R.string.mong_signup_email),Toast.LENGTH_SHORT).show();
                }
            }
        });

        codeAuthButton.setOnClickListener(v -> {
            code = codeEditText.getText().toString();
            if(code.equals("")){
                Toast.makeText(SignUpActivity.this,CommandUtil.getInstance().getStr(R.string.mong_signup_email_auth),Toast.LENGTH_SHORT).show();
            }else{
                if(code.equals(emailAuthCode)){
                    email = beforeAuthEmail;
                    Toast.makeText(SignUpActivity.this,CommandUtil.getInstance().getStr(R.string.mong_signup_email_auth_success),Toast.LENGTH_SHORT).show();
                    emailAuthLayout.setVisibility(View.GONE);
                    emailEditText.setFocusable(false);
                    emailEditText.setClickable(true);
                    emailAuthButton.setClickable(false);
                }
            }

        });


        TextView signUpButton = findViewById(R.id.try_signup_btn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birth = dateTextView.getText().toString();
                pw = pwEditText.getText().toString();
                name = nameEditText.getText().toString();
                checkPw = checkPwEditText.getText().toString();
                email = emailEditText.getText().toString();

                String validPassword = validPW(checkPw);
                if(validPassword != null){
                    Toast.makeText(getApplicationContext(),validPassword,Toast.LENGTH_SHORT).show();

                    return;
                }
                if(id.equals("")||birth.equals("")||pw.equals("")||email.equals("")||name.equals("")){
                    CommandUtil.getInstance().showCommonOneButtonDialog(SignUpActivity.this, CommandUtil.getInstance().getStr(R.string.mong_signup_error_essential), getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);

                }else if(name.length()==1){
                    CommandUtil.getInstance().showCommonOneButtonDialog(SignUpActivity.this, CommandUtil.getInstance().getStr(R.string.mong_signup_error_name), getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);

                }else if(!pw.equals(checkPw)){
                    CommandUtil.getInstance().showCommonOneButtonDialog(SignUpActivity.this, CommandUtil.getInstance().getStr(R.string.mong_signup_error_pw), getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);

                } else {
                    if(box1==0||box2==0){
                        CommandUtil.getInstance().showCommonOneButtonDialog(SignUpActivity.this, CommandUtil.getInstance().getStr(R.string.mong_signup_error_essential_check), getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);

                    }else{
                        sendSignUpRequest();
                    }
                }
            }
        });
    }

    public String validPW(String pw) {
        boolean chk1 = Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?~`]+$", pw);
        boolean chk2 = hasSpecialCharacter(pw);

        if (!chk1 || pw.length() < 8 || !chk2) {
            return getResources().getString(R.string.password_valid);
        }

        return null;
    }

    public boolean hasSpecialCharacter(String string) {
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isLetterOrDigit(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public void checkIdExist(){
        SignUpInfo signUpInfo = new SignUpInfo("search_id",beforeCheckId);
        ActionRuler.getInstance().addAction(new ActionRequestSignUp(this, signUpInfo, new ActionResultListener<SignUpResult>() {
            @Override
            public void onResponseResult(SignUpResult response) {
                try {
                    SignUpResult result = response;
                    if(result.isResult()){
                        if(result.getResp().equals("duple")){
                            CommandUtil.getInstance().showCommonOneButtonDialog(SignUpActivity.this, CommandUtil.getInstance().getStr(R.string.mong_signup_error_id_dupl), getResources().getString(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, null);

                        }else if(result.getResp().equals("usable")){
                            Toast.makeText(SignUpActivity.this,CommandUtil.getInstance().getStr(R.string.mong_signup_error_id_pass),Toast.LENGTH_SHORT).show();
                            id = beforeCheckId;
                        }else{
                            Log.d(TAG,result.getResp() + "\t1");
                        }
                    }else{
                        Log.d(TAG,result.getError()+ "\t2");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this,CommandUtil.getInstance().getStr(R.string.txt_common_error_exception),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("아이디 체크 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void checkAllCheckBox(){
        if(box1==1&&box2==1&&box3==1&&box4==1){
            boxAll = 1;
            allCheckBox.setChecked(true);
        }else{
            boxAll = 0;
            allCheckBox.setChecked(false);
        }
    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/" + day_string + "/" + year_string);

        Toast.makeText(this,"Date: "+dateMessage,Toast.LENGTH_SHORT).show();
    }


    public void requestEmailAuth(){

        ActionRuler.getInstance().addAction(new ActionRequestEmailAuth(this,"","", beforeAuthEmail, new ActionResultListener<EmailAuthResult>() {
            @Override
            public void onResponseResult(EmailAuthResult response) {
                try {
                    EmailAuthResult result = response;

                    if(result.isResult()){
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_signup_auth_code),Toast.LENGTH_SHORT).show();
                        emailAuthCode = result.getResp();
                        emailAuthLayout.setVisibility(View.VISIBLE);
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

    public boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true;
        }
        return err;
    }



    public void sendSignUpRequest(){
        SignUpInfo signUpInfo = new SignUpInfo("join",
                id,
                name,
                birth,
                pw,
                email,
                "default",
                box1,
                box2,
                box3,
                box4,
                gender);
        Log.d("TEST","id : " + id
                + "\nname : " + name
                + "\nbirth : " + birth
                + "\npw : " + pw
                + "\nemail : " + email
                + "\nbox1 : " + box1
                + "\nbox2 : " + box2
                + "\nbox3 : " + box3
                + "\nbox4 : " + box4
                + "\ngender : " + gender
        );

        ActionRuler.getInstance().addAction(new ActionRequestSignUp2(this, signUpInfo, new ActionResultListener<ResponseBody>() {
            @Override
            public void onResponseResult(ResponseBody response) {
                try {
                    ResponseBody result = response;
                    String rstString = result.string();
                    if(rstString.contains("회원가입 처리 완료")){
                        setUserInfo(id , "0", name, name, gender, birth , "");
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.mong_signup_success),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDefaultDialog(SignUpActivity.this,rstString);
                    }
//                    JSONObject job = new JSONObject(result.string());
//                    boolean resultSuccess = job.getBoolean("result");
//                    if(resultSuccess){
//
//                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("회원가입 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void setUserInfo(String id, String type, String nickname, String name, int gender, String birth, String url ){
        //
        ActionRuler.getInstance().addAction(new ActionRequestAddUser(this,id,type, nickname, name, gender, birth,url, new ActionResultListener<AddUserResult>() {
            @Override
            public void onResponseResult(AddUserResult response) {
                try {
                    AddUserResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST","성공");
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