package kr.co.genericit.mybase.xoyou2.activity.xoyou;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import co.kr.sky.AccumThread;
import co.kr.sky.Check_Preferences;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.ChattingRoom_Adapter;
import kr.co.genericit.mybase.xoyou2.adapter.WeUnListAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.model.WeYouUnDataListObj;

public class ChattingRoomActivity extends AppCompatActivity {
    private String obj;
    private CommonUtil dataSet = CommonUtil.getInstance();
    private ArrayList<ContractObj> arrContract = new ArrayList<>();


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
        list = findViewById(R.id.list);

        obj = getIntent().getStringExtra("phone");

        arrContract = dataSet.sqlSelectContract(this , obj);

        m_Adapter = new ChattingRoom_Adapter( this, arrContract);
//        list.setOnItemClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);


        SkyLog.d("arrContract size :: " + arrContract.size());
        findViewById(R.id.common_left_btn).setOnClickListener(btnListener);

    }

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