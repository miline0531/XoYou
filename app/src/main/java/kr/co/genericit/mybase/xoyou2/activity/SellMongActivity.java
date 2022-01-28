package kr.co.genericit.mybase.xoyou2.activity;

import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestSellMong;
import kr.co.genericit.mybase.xoyou2.network.response.SellMongResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class SellMongActivity extends CommonActivity {

    private TextView btn_fixed,btn_bid,text_startdate,text_enddate,text_selltype,btn_enter;
    private EditText edt_price;
    private int sellType = 1; //1.고정가 2.경매
    private JWSharePreference jwSharePreference;
    private String startDate,endDate,startTime,
            currentYear,currentMonth,currentDay,currentTime,
            endYear,endMonth,endDay,endTime;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sell_mong);
        super.onCreate(savedInstanceState);

        initView();
    }

    LinearLayout btn_edate, btn_sdate;

    public void initView() {
        btn_fixed = findViewById(R.id.btn_fixed);
        btn_bid = findViewById(R.id.btn_bid);
        text_startdate = findViewById(R.id.text_startdate);
        text_enddate = findViewById(R.id.text_enddate);
        text_selltype = findViewById(R.id.text_selltype);
        btn_enter = findViewById(R.id.btn_enter);
        edt_price = findViewById(R.id.edt_price);

        btn_edate = findViewById(R.id.btn_edate);
        btn_sdate = findViewById(R.id.btn_sdate);

        //startTime구하는부분
        long now = System.currentTimeMillis();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


        String arr[] = sdf.format(now).split("-");
        String arr2[] = arr[2].split(" ");
        currentYear = arr[0];
        Log.d("TEST","currentYear : " +currentYear);
        currentMonth = arr[1];
        Log.d("TEST","currentMonth : " +currentMonth);
        currentDay = arr2[0];
        Log.d("TEST","currentDay : " +currentDay);
        currentTime = arr2[1];
        Log.d("TEST","currentTime : " +currentTime);
        startTime = currentTime;

        //endTime구하는부분
        Calendar cal = Calendar.getInstance();
        Date date = null;
        try{
            date = sdf.parse(sdf.format(now));
        }catch (ParseException e){
            Log.d("TEST","ParsesException 발생");
        }
        cal.setTime(date);
        if(sellType==1){ //고정가
            cal.add(Calendar.YEAR, 1);
        }else{ //경매
            cal.add(Calendar.MONTH, 1);
        }
        endTime = sdf.format(cal.getTime());
        endTime = endTime.split(" ")[1];
        Log.d("Test","endTime : " + endTime);


        text_startdate.setText(sdf.format(now));
        text_enddate.setText(sdf.format(cal.getTime()));



        setOnClick();

    }

    public void setOnClick(){
        final int white = ContextCompat.getColor(this, R.color.white);
        final int black = ContextCompat.getColor(this, R.color.app_disable_text_color);

        btn_fixed.setOnClickListener(v ->{
            if(sellType==2){
                btn_fixed.setBackgroundResource(R.color.btn_ok_color);
                btn_fixed.setTextColor(white);
                btn_bid.setBackgroundResource(R.color.txt_disable);
                btn_bid.setTextColor(black);
                sellType = 1;
                text_selltype.setText(CommandUtil.getInstance().getStr(R.string.mong_sell_price));
            }
        });

        btn_bid.setOnClickListener(v ->{
            if(sellType==1){
                btn_bid.setBackgroundResource(R.color.btn_ok_color);
                btn_bid.setTextColor(black);
                btn_fixed.setBackgroundResource(R.color.txt_disable);
                btn_fixed.setTextColor(white);
                sellType = 2;
                text_selltype.setText(CommandUtil.getInstance().getStr(R.string.mong_sell_price_start));
            }
        });


        btn_sdate.setOnClickListener(v ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(SellMongActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    startDate = changeBirthdayToString(year, month, dayOfMonth);
                    text_startdate.setText(startDate + " " + startTime);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(SellMongActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            startTime = changeTimeToString(hourOfDay,minute);
                            text_startdate.setText(startDate + " " + startTime);
                        }
                    },0,0,true);
                    timePickerDialog.show();

                }
            }, Integer.parseInt(currentYear), Integer.parseInt(currentMonth)-1, Integer.parseInt(currentDay));
            datePickerDialog.show();
        });

        btn_edate.setOnClickListener(v ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(SellMongActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    endDate = changeBirthdayToString(year, month, dayOfMonth);
                    text_enddate.setText(endDate + " " + endTime);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(SellMongActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            endTime = changeTimeToString(hourOfDay,minute);
                            text_enddate.setText(endDate + " " + endTime);
                        }
                    },0,0,true);
                    timePickerDialog.show();

                }
            }, Integer.parseInt(currentYear)+1, Integer.parseInt(currentMonth)-1, Integer.parseInt(currentDay));
            datePickerDialog.show();
        });


        btn_enter.setOnClickListener(v ->{
            requestSellMong();
        });
    }
    public String changeTimeToString(int hour, int minute){
        String hourString;
        if(hour<10){
            hourString = "0"+hour;
        }else{
            hourString = ""+hour;
        }
        String minuteString;
        if(minute<10){
            minuteString = "0"+minute;
        }else{
            minuteString = ""+minute;
        }
        String result = hourString + ":" + minuteString + ":00";

        return result;
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

        return ""+year+"-"+monthText+"-"+dayText;
    }

    public String dateToUTC(Date date){
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(date);
        Log.d("TEST","utcTime : " + utcTime);
        return utcTime;
    }

    public Date stringDateToDate(String strDate)
    {
        Date dateToReturn = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try{
            dateToReturn = (Date)dateFormat.parse(strDate);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return dateToReturn;
    }

    public void requestSellMong() {
        /*jwSharePreference = new JWSharePreference();
        int userId = jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0);*/
        int mong_srl = Integer.parseInt(getIntent().getStringExtra("srl"));
        String description = getIntent().getStringExtra("description");
        String title = getIntent().getStringExtra("title");
        Log.d("TEST","description : " + description);
        Log.d("TEST","title : " + title);

        String startText = text_startdate.getText().toString();
        Date startDate = stringDateToDate(startText);
        String start_time = dateToUTC(startDate);

        String endText = text_enddate.getText().toString();
        Date endDate = stringDateToDate(endText);
        String end_time = dateToUTC(endDate);



        Log.d("TEST","mong_srl : " + mong_srl);
        Log.d("TEST","start_time : " + start_time);
        Log.d("TEST","end_time : " + end_time);

        int start_value = Integer.parseInt(edt_price.getText().toString());

        String[] category_code = {};


        ActionRuler.getInstance().addAction(new ActionRequestSellMong(this,mong_srl, start_time, end_time, sellType,
                start_value, description, title,  category_code, new ActionResultListener<SellMongResult>() {

            @Override
            public void onResponseResult(SellMongResult response) {
                try {
                    SellMongResult result = response;

                    Log.d("TEST","Error_desc : " + result.getError_desc());
                    Log.d("TEST","Error : " + result.getError());
                    Log.d("TEST","Msg : " + result.getMsg());

                    if(result.getMsg().equals("success")){
                        CommandUtil.getInstance().showCommonOneButtonDialog(SellMongActivity.this, CommandUtil.getInstance().getStr(R.string.mong_sell_reg_success), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                            @Override
                            public void onClick(int button) {
                                finish();
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                //Toast.makeText(getContext(), "꿈 가져오기 실패.\n잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("꿈 올리기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
}