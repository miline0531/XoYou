package kr.co.genericit.mybase.xoyou2.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.detail.MymongDetailActivity;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.databinding.ActivityKeywordInputBinding;

public class KeywordInputActivity extends CommonActivity implements View.OnClickListener {
    private ActivityKeywordInputBinding binding;


    private int mCurrentTabIndex = 0;
    private LinearLayout btn_keyword_selectbox, btn_keyword_inputbox;
    private TextView txt_keyword1, txt_keyword2;
    private View view_under_line1, view_under_line2;
    ImageView test;

    private EditText edt_keyword;
    private TextView inputLength;

    private LinearLayout home_content_input, home_content_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityKeywordInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView(){
        btn_keyword_selectbox = findViewById(R.id.btn_keyword_selectbox);
        btn_keyword_inputbox = findViewById(R.id.btn_keyword_inputbox);
        home_content_input = findViewById(R.id.home_content_input);
        home_content_select = findViewById(R.id.home_content_select);
        txt_keyword1 = findViewById(R.id.txt_keyword1);
        txt_keyword2 = findViewById(R.id.txt_keyword2);
        view_under_line1 = findViewById(R.id.view_under_line1);
        view_under_line2 = findViewById(R.id.view_under_line2);
        edt_keyword = findViewById(R.id.edt_keyword);
        inputLength = findViewById(R.id.inputLength);

        btn_keyword_selectbox.setOnClickListener(this);
        btn_keyword_inputbox.setOnClickListener(this);

        test = findViewById(R.id.btn_test);
        test.setOnClickListener( k ->{
            Intent i = new Intent(KeywordInputActivity.this, CardActivity.class);
            startActivity(i);
        });

        findViewById(R.id.btn_mymong_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KeywordInputActivity.this, MymongDetailActivity.class);
                startActivity(i);
            }
        });

        datePickerCreate();
        binding.selectBoxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        setTextWatcher();
    }

    private DatePickerDialog datePickerDialog;
    private String birthday="";

    public void datePickerCreate(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        birthday = changeBirthdayToString(mYear, mMonth, mDay);
        binding.selectBoxData.setText(birthday);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday = changeBirthdayToString(year, month, dayOfMonth);
                binding.selectBoxData.setText(birthday);
            }
        }, mYear, mMonth, mDay);
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
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_keyword_selectbox:

                txt_keyword1.setTextColor(ContextCompat.getColor(this, R.color.white));
                txt_keyword2.setTextColor(ContextCompat.getColor(this, R.color.app_disable_text_color));
                view_under_line1.setVisibility(View.VISIBLE);
                view_under_line2.setVisibility(View.INVISIBLE);
                home_content_select.setVisibility(View.VISIBLE);
                home_content_input.setVisibility(View.GONE);
                break;
            case R.id.btn_keyword_inputbox:

                txt_keyword2.setTextColor(ContextCompat.getColor(this, R.color.white));
                txt_keyword1.setTextColor(ContextCompat.getColor(this, R.color.app_disable_text_color));
                view_under_line2.setVisibility(View.VISIBLE);
                view_under_line1.setVisibility(View.INVISIBLE);
                home_content_input.setVisibility(View.VISIBLE);
                home_content_select.setVisibility(View.GONE);

                break;
        }
    }

    private void setTextWatcher(){
        edt_keyword.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                boolean valid = Util.getInstance(CheckPasswordChangeActivity.this).validPW(arg0.toString());
                String length = arg0.toString().length()+"";
                inputLength.setText(length);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
}