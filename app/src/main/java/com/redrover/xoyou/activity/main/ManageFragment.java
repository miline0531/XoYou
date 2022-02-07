package com.redrover.xoyou.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.activity.xoyou.WeUnListActivity;
import com.redrover.xoyou.adapter.MainFrag2ListAdapter;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.WeListObj;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;


public class ManageFragment extends Fragment {
    public Context mContext;

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private RecyclerView list;
    private ArrayList<WeListObj> weList = new ArrayList<WeListObj>();
    private MainFrag2ListAdapter m_Adapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onResume() {
        super.onResume();
        SkyLog.d("-- onResume --");


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        list = (RecyclerView)view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(mContext)) ;

        m_Adapter = new MainFrag2ListAdapter( mContext, weList);
        m_Adapter.setListOnClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);
        getWeList();
        return view;
    }

    public MainFrag2ListAdapter.listOnClickListener mItemClickListener = new MainFrag2ListAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            SkyLog.d("CLACIK id :: "  + id);
            SkyLog.d("CLACIK action :: "  + action);

            Intent it = new Intent(mContext , WeUnListActivity.class);
            it.putExtra("obj" , weList.get(id));
            startActivity(it);
        }
    };

    private void getWeList(){
        CommandUtil.getInstance().showLoadingDialog(MainActivity.mainAc);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_WE_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(mContext, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }


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
                        weList.clear();
                        for (int i = 0; i < jsonObject_listWe.length(); i++) {
                            JSONObject jsonObject = jsonObject_listWe.getJSONObject(i);
                            weList.add(new WeListObj(
                                    jsonObject.getString("No") ,
                                    jsonObject.getString("UnName") ,
                                    jsonObject.getString("MaxName") ,
                                    jsonObject.getString("MinName")));
                        }
                        m_Adapter.notifyDataSetChanged();
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
}