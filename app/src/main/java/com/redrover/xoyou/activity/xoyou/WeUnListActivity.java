package com.redrover.xoyou.activity.xoyou;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.WeUnListAdapter;
import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.WeListObj;
import com.redrover.xoyou.model.WeYouUnDataListObj;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;

public class WeUnListActivity extends AppCompatActivity {
    private WeListObj obj;
    public Context mContext;
    CommonUtil dataSet = CommonUtil.getInstance();

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ListView list;
    public static ArrayList<WeYouUnDataListObj> weYouUnDataList = new ArrayList<WeYouUnDataListObj>();
    private WeUnListAdapter m_Adapter;


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
        setContentView(R.layout.activity_weunlist);
        getSupportActionBar().hide();
        obj = getIntent().getParcelableExtra("obj");
        mContext = this;
        list = findViewById(R.id.list);


        getUnData();

        findViewById(R.id.common_left_btn).setOnClickListener(btnListener);

    }
    private void getUnData(){
        CommandUtil.getInstance().showLoadingDialog(WeUnListActivity.this);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_WE_UN_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        map.put("date", dataSet.FullPatternDate("yyyyMMddHHmmss"));
        map.put("unName", obj.getUnName());

        //스레드 생성
        mThread = new AccumThread(WeUnListActivity.this, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Intent it = new Intent(mContext , WeUnDetailActivity.class);
            it.putExtra("obj" , weYouUnDataList.get(position));
            startActivity(it);
        }
    };

    Handler mAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CommandUtil.getInstance().dismissLoadingDialog();
            if(msg.arg1 == 0) {
                String res  = (String)msg.obj;
                SkyLog.d("res 0: " + res);
                try {
                    JSONObject jsonObject_succes = new JSONObject(res);                     //SUCCESS
                    if(jsonObject_succes.getString("success").equals("true")){
                        JSONArray jsonObject_listWe = new JSONArray(jsonObject_succes.getString("data"));

                        SkyLog.d("COUNT :: " + jsonObject_listWe.length());
                        weYouUnDataList.clear();

                        for (int i = 0; i < jsonObject_listWe.length(); i++) {
                            JSONObject jsonObject = jsonObject_listWe.getJSONObject(i);
                            weYouUnDataList.add(new WeYouUnDataListObj(
                                    jsonObject.getString("No") ,
                                    jsonObject.getString("Name") ,
                                    jsonObject.getString("Info") ,
                                    jsonObject.getString("UnName") ,
                                    jsonObject.getString("UnSimRi") ,
                                    jsonObject.getString("Image") ,
                                    jsonObject.getString("Doui")));
                        }


                        m_Adapter = new WeUnListAdapter( WeUnListActivity.this, weYouUnDataList);
                        list.setOnItemClickListener(mItemClickListener);
                        list.setAdapter(m_Adapter);

                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDialog(mContext,
                                jsonObject_succes.getString("error") + getClass().toString(),
                                mContext.getResources().getString(R.string.str_cofirm),
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
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.common_left_btn:
                    finish();
                    break;
            }
        }
    };
}