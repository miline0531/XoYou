package com.redrover.xoyou.popup;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.AddLocationActivity;
import com.redrover.xoyou.activity.WebViewActivity;
import com.redrover.xoyou.activity.xoyou.WeUnDetailActivity;
import com.redrover.xoyou.activity.xoyou.WeUnListActivity;
import com.redrover.xoyou.adapter.WeUnListAdapter;
import com.redrover.xoyou.adapter.WeUnListPopupAdapter;
import com.redrover.xoyou.common.ActivityEx;
import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.Contactobj;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestAddRelation;
import com.redrover.xoyou.network.response.AddRelationResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;


public class WeUnSelectPopUp extends ActivityEx {
    CommonUtil dataSet = CommonUtil.getInstance();
    private ListView list;
    private WeUnListPopupAdapter m_Adapter;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.activity_weunselectpopup);
        list = (ListView)findViewById(R.id.list);

        m_Adapter = new WeUnListPopupAdapter( WeUnSelectPopUp.this, WeUnListActivity.weYouUnDataList);
        list.setOnItemClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);


        findViewById(R.id.btn_close).setOnClickListener(btnListener);

    }


    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.putExtra("obj", WeUnListActivity.weYouUnDataList.get(position));
            setResult(RESULT_OK, intent);
            finish();
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

}





