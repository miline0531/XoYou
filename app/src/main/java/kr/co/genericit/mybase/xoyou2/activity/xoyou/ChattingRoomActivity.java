package kr.co.genericit.mybase.xoyou2.activity.xoyou;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import co.kr.sky.AccumThread;
import co.kr.sky.Check_Preferences;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.main.HomeFragment;
import kr.co.genericit.mybase.xoyou2.adapter.ChattingRoom_Adapter;
import kr.co.genericit.mybase.xoyou2.adapter.WeUnListAdapter;
import kr.co.genericit.mybase.xoyou2.common.ChatAsyncTask;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.model.WeYouUnDataListObj;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class ChattingRoomActivity extends AppCompatActivity {
    private String obj;
    private CommonUtil dataSet = CommonUtil.getInstance();
    private ArrayList<ContractObj> arrContract = new ArrayList<>();
    private CircularProgressView progressView1,progressView2,progressView3,progressView4;
    private TextView progressTxt1,progressTxt2,progressTxt3,progressTxt4;
    private EditText msg_edit;

    private int apiPosition = 0;

    private ListView list;
    private ChattingRoom_Adapter m_Adapter;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chattingroom);
        getSupportActionBar().hide();
        init();


        apiPosition = arrContract.size()-1;

        ChatAsyncTask asyncTask = new ChatAsyncTask(ChatAfterAccum , arrContract.get(apiPosition) ,apiPosition);
        asyncTask.execute();

        bottomCirclLast();

        SkyLog.d("arrContract size :: " + arrContract.size());


    }
    private void init(){
        list = findViewById(R.id.list);
        progressView1 = findViewById(R.id.progressView1);
        progressView2 = findViewById(R.id.progressView2);
        progressView3 = findViewById(R.id.progressView3);
        progressView4 = findViewById(R.id.progressView4);
        progressTxt1 = findViewById(R.id.progressTxt1);
        progressTxt2 = findViewById(R.id.progressTxt2);
        progressTxt3 = findViewById(R.id.progressTxt3);
        progressTxt4 = findViewById(R.id.progressTxt4);
        msg_edit = findViewById(R.id.msg_edit);

        progressView1.setProgressTextEnabled(false);
        progressView2.setProgressTextEnabled(false);
        progressView3.setProgressTextEnabled(false);
        progressView4.setProgressTextEnabled(false);

        obj = getIntent().getStringExtra("phone");

        arrContract = dataSet.sqlSelectContract(this , obj);
        m_Adapter = new ChattingRoom_Adapter( this, arrContract);
        list.setAdapter(m_Adapter);

        initData();

        findViewById(R.id.common_left_btn).setOnClickListener(btnListener);
        findViewById(R.id.btnSend).setOnClickListener(btnListener);
    }
    private void initData(){
        arrContract = dataSet.sqlSelectContract(this , obj);
        //m_Adapter.notifyDataSetChanged();
        m_Adapter = new ChattingRoom_Adapter( this, arrContract);
        list.setAdapter(m_Adapter);
        list.setSelection(arrContract.size()-1);
        bottomCirclLast();
    }
    private void bottomCirclLast(){
        int i = arrContract.size()-1;
        for (int j = 0; j<arrContract.size(); j++ ){
            if(arrContract.get(j).getSend_Flag().equals("0")){
                i = j;
            }
        }

        float po1val = arrContract.get(i).getPo0Val() == null ? 0 : Float.parseFloat(arrContract.get(i).getPo0Val());
        float po2val = arrContract.get(i).getPo1Val() == null ? 0 : Float.parseFloat(arrContract.get(i).getPo1Val());
        float po3val = arrContract.get(i).getPo2Val() == null ? 0 : Float.parseFloat(arrContract.get(i).getPo2Val());
        float po4val = arrContract.get(i).getPo3Val() == null ? 0 : Float.parseFloat(arrContract.get(i).getPo3Val());

        bottomCircleGraph((int)po1val, (int)po2val, (int)po3val, (int)po4val);
    }
    private void bottomCircleGraph(int progress1 , int progress2 , int progress3 , int progress4){
        progressView1.setProgress(progress1,true);
        progressTxt1.setText("믿음\n"+progress1+"%");

        progressView2.setProgress(progress2,true);
        progressTxt2.setText("호감\n"+progress2+"%");

        progressView3.setProgress(progress3,true);
        progressTxt3.setText("기분\n"+progress3+"%");

        progressView4.setProgress(progress4,true);
        progressTxt4.setText("생각\n"+progress4+"%");
    }
    private void sendSMS(String phoneNumber, String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }
    Handler ChatAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String res  = (String)msg.obj;

            SkyLog.d("ChatAfterAccum  :: " + msg.arg1);
            SkyLog.d("ChatAfterAccum  res:: " + res);
            if(msg.arg1 >= 0){
                try {
                    JSONObject jsonObject_succes = new JSONObject(res);                     //SUCCESS
                    if(jsonObject_succes.getString("success").equals("true")){
                        JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));

                        String po0Val = jsonObject_data.getString("po0Val");
                        String po1Val = jsonObject_data.getString("po1Val");
                        String po2Val = jsonObject_data.getString("po2Val");
                        String po3Val = jsonObject_data.getString("po3Val");
                        String message = jsonObject_data.getString("message");
                        String suggestion = jsonObject_data.getString("suggestion");

                        SkyLog.d("apiPosition :: " + apiPosition);
                        SkyLog.d("po0Val :: " + po0Val);
                        SkyLog.d("po1Val :: " + po1Val);
                        SkyLog.d("po2Val :: " + po2Val);
                        SkyLog.d("po3Val :: " + po3Val);
                        SkyLog.d("message :: " + message);
                        SkyLog.d("suggestion :: " + suggestion);

                        //db 저장
                        dataSet.sqlContractUpdate(ChattingRoomActivity.this ,
                                arrContract.get(msg.arg1) ,
                                suggestion ,
                                po0Val ,
                                po1Val ,
                                po2Val ,
                                po3Val ,
                                message);

                        //리스트뷰 새로고침 & 하단 그래프 새로고침
                        arrContract.get(msg.arg1).setSuggestion(suggestion);
                        arrContract.get(msg.arg1).setPo0Val(po0Val);
                        arrContract.get(msg.arg1).setPo1Val(po1Val);
                        arrContract.get(msg.arg1).setPo2Val(po2Val);
                        arrContract.get(msg.arg1).setPo3Val(po3Val);
                        arrContract.get(msg.arg1).setMessage(message);
                        m_Adapter.notifyDataSetChanged();
                        bottomCirclLast();


                        if(msg.arg1 != 0){
                            apiPosition = apiPosition - 1;
                            ChatAsyncTask asyncTask = new ChatAsyncTask(ChatAfterAccum , arrContract.get(apiPosition) ,apiPosition);
                            asyncTask.execute();
                        }
                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDialog(MainActivity.mainAc,
                                jsonObject_succes.getString("error") + getClass().toString(),
                                MainActivity.mainAc.getResources().getString(R.string.str_cofirm),
                                CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,
                                null);
                    }
                }catch (Exception e){
                    SkyLog.d("e :: " + e);
                    if(msg.arg1 != 0){
                        apiPosition = apiPosition - 1;
                        ChatAsyncTask asyncTask = new ChatAsyncTask(ChatAfterAccum , arrContract.get(apiPosition) ,apiPosition);
                        asyncTask.execute();
                    }else{
                        //새로고침
                        initData();
                    }
                }
            }
        }
    };

    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.common_left_btn:
                    finish();
                    break;
                case R.id.btnSend:
                    SkyLog.d("==btnSend==");
                    if(msg_edit.getText().toString().length()== 0) {
                        Toast.makeText(getApplicationContext() , "문자 내용을 입력해주세요." , Toast.LENGTH_SHORT).show();
                    }else if(msg_edit.getText().toString().length() > 70) {
                        Toast.makeText(getApplicationContext() , "문자 내용을 줄여주세요" , Toast.LENGTH_SHORT).show();
                    }else{
                        sendSMS(getIntent().getStringExtra("phone"), msg_edit.getText().toString());

                        new Handler().postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                msg_edit.setText("");
                                String[] phone_Arr = new String[1];
                                phone_Arr[0] = getIntent().getStringExtra("phone");
                                if(dataSet.readSMSMessage(ChattingRoomActivity.this , phone_Arr , getIntent().getStringExtra("name")) == 0){
                                    initData();
                                }
                            }
                        }, 2000);
                    }
                    break;
            }
        }
    };
}