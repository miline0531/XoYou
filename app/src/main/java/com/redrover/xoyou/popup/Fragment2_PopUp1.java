package com.redrover.xoyou.popup;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.adapter.popup.FragmentStep1ListAdapter;
import com.redrover.xoyou.adapter.popup.FragmentStep2ListAdapter;
import com.redrover.xoyou.common.ActivityEx;
import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.QaListObj;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;


public class Fragment2_PopUp1 extends ActivityEx {
    CommonUtil dataSet = CommonUtil.getInstance();
    private String[] YouQA_Name ={"기본", "건강", "결혼", "명예", "사고", "이동", "인연","재능", "재물", "직업", "집안"};
    private ListView list;
    private TextView title_txt;
    private FragmentStep1ListAdapter m_Adapter;
    private FragmentStep2ListAdapter m_Step2Adapter;
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ArrayList<QaListObj> qrList = new ArrayList<QaListObj>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.activity_allstep);

        title_txt = (TextView)findViewById(R.id.title_txt);
        list = (ListView)findViewById(R.id.list);
        findViewById(R.id.btn_close).setOnClickListener(btnListener);


        stepInit();
    }

    private void stepInit(){
        if(getIntent().getIntExtra("STEP",0) == 1){
            title_txt.setText("질문 항목 선택");
            m_Adapter = new FragmentStep1ListAdapter( this, YouQA_Name);
            list.setOnItemClickListener(mItemClickListener);
            list.setAdapter(m_Adapter);
        }else if(getIntent().getIntExtra("STEP",0) == 2){
            title_txt.setText("건강 관련 질문");
            getQaList();
        }
    }

    private void getQaList(){
        CommandUtil.getInstance().showLoadingDialog(MainActivity.mainAc);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_QA_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        map.put("qaItem", getIntent().getStringExtra("data"));

        //스레드 생성
        mThread = new AccumThread(MainActivity.mainAc, mAfterAccum, map, 5, 1, null);
        mThread.start();        //스레드 시작!!
    }


    Handler mAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CommandUtil.getInstance().dismissLoadingDialog();
            if(msg.arg1 == 1) {
                String res  = (String)msg.obj;
                SkyLog.d("res 1: " + res);

                try {
                    JSONObject jsonObject_succes = new JSONObject(res);                     //SUCCESS
                    if(jsonObject_succes.getString("success").equals("true")){
                        JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_succes.getString("data"));

                        qrList.clear();
                        for (int i = 0; i < jsonObject_listSimRi.length(); i++) {
                            JSONObject jsonObject = jsonObject_listSimRi.getJSONObject(i);
                            qrList.add(new QaListObj(
                                    jsonObject.getString("No") ,
                                    jsonObject.getString("Menu") ,
                                    jsonObject.getString("MenuGuBun") ,
                                    jsonObject.getString("MenuName") ,
                                    jsonObject.getString("MenuSeo")));
                        }
                        SkyLog.d("qrList COUNT :: " + qrList.size());

                        m_Step2Adapter = new FragmentStep2ListAdapter( Fragment2_PopUp1.this, qrList);
                        list.setOnItemClickListener(mItemClickListener);
                        list.setAdapter(m_Step2Adapter);
                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDialog(MainActivity.mainAc,
                                jsonObject_succes.getString("error") + getClass().toString(),
                                MainActivity.mainAc.getResources().getString(R.string.str_cofirm),
                                CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,
                                null);
                    }
                }catch (Exception e){
                    SkyLog.d("e :: " + e);
                }
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
            }
        }
    };



    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.e("SKY" , "position :: " + position);
            if(getIntent().getIntExtra("STEP",0) == 1){
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("data", YouQA_Name[position]);
                setResult(RESULT_OK, intent);
                finish();
            }else if(getIntent().getIntExtra("STEP",0) == 2){
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("data", qrList.get(position).getMenuSeo());
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    };

}





