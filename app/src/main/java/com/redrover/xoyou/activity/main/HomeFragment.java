package com.redrover.xoyou.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.redrover.xoyou.adapter.MainFrag1ListAdapter;
import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.SimRi;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;


public class HomeFragment extends Fragment{
    public Context mContext;

    //SKY
    private CommonUtil dataSet = CommonUtil.getInstance();
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private RecyclerView list;

    private MainFrag1ListAdapter m_Adapter;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawer2;
    private RelativeLayout lb_slide;
    private RelativeLayout lb_slide3;
    private LinearLayout popview;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        SkyLog.d("-- onResume --");
//        lb_slide3.setVisibility(View.GONE);


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        list = (RecyclerView)view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(mContext)) ;        /*
        drawerLayout = view.findViewById(R.id.drawerLayout);
        drawer2 = (RelativeLayout) view.findViewById(R.id.drawer2);
        lb_slide = view.findViewById(R.id.lb_slide);
        list_left = view.findViewById(R.id.list_left);
        list_left3 = view.findViewById(R.id.list_left3);
        lb_slide = view.findViewById(R.id.lb_slide);
        lb_slide3 = view.findViewById(R.id.lb_slide3);
        popview = view.findViewById(R.id.popview);



        //왼쪽 슬라이드 리스트뷰--1
        m_LeftAdapter = new MainFrag1LeftListAdapter( ac, YouQA_Name);
        list_left.setOnItemClickListener(mItemClickListener2);
        list_left.setAdapter(m_LeftAdapter);

        //왼쪽 슬라이드 리스트뷰--2
        m_Left3Adapter = new MainFrag1Left3ListAdapter( ac, qrList);
        list_left3.setOnItemClickListener(mItemClickListener3);
        list_left3.setAdapter(m_Left3Adapter);
        */
        m_Adapter = new MainFrag1ListAdapter( MainActivity.mainAc, MainActivity.listSimRi);
        m_Adapter.setListOnClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);



//        view.findViewById(R.id.popview).setOnClickListener(btnListener);
//        view.findViewById(R.id.btn_1).setOnClickListener(btnListener);
        getUserList();
        return view;
    }

    public void getQaSimRiList(String data){
        CommandUtil.getInstance().showLoadingDialog(MainActivity.mainAc);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_USER_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        map.put("date", dataSet.FullPatternDate("yyyyMMddHHmmss"));
        map.put("menuSeo", data);

        //스레드 생성
        mThread = new AccumThread(MainActivity.mainAc, mAfterAccum, map, 5, 1, null);
        mThread.start();        //스레드 시작!!
    }
    public MainFrag1ListAdapter.listOnClickListener mItemClickListener = new MainFrag1ListAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            SkyLog.d("CLACIK id :: "  + id);
            SkyLog.d("CLACIK action :: "  + action);
            SkyLog.d("CLACIK getPhone :: "  + MainActivity.listSimRi.get(id).getPhone());
            MainActivity.homeClickPosition = id;
            MainActivity.homeClickObj = MainActivity.listSimRi.get(id);
            MainActivity.fragmentPosionFlag = 0;

            ((MainActivity)getContext()).homeFragmentLiskClick();

            //test
//            Intent it = new Intent(mContext , ChattingRoomActivity.class);
//            it.putExtra("phone" , listSimRi.get(id).getPhone());
//            it.putExtra("name" , listSimRi.get(id).getName());
//            startActivity(it);

        }
    };

    private void getUserList(){
        CommandUtil.getInstance().showLoadingDialog(MainActivity.mainAc);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_USER_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(MainActivity.mainAc, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }



    AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            SkyLog.d("mItemClickListener2 position :: " + position);
            //MainActivity.viewSlide2();
            drawerLayout.close();
        }
    };



    Handler mAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if(msg.arg1 == 0) {
                String res  = (String)msg.obj;
                SkyLog.d("res 0: " + res);

                ContractThread thread2 = new ContractThread(res);
                thread2.start();
                SkyLog.d("end0 :: ");
            }else if(msg.arg1 == 1) {
                String res  = (String)msg.obj;
                SkyLog.d("res 1: " + res);

                ContractThread thread2 = new ContractThread(res);
                thread2.start();
                SkyLog.d("end1 :: ");

            }
        }
    };


    Handler mEndAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            m_Adapter.notifyDataSetChanged();
            CommandUtil.getInstance().dismissLoadingDialog();
            MainActivity.setHomeLeftTopTxt();
        }
    };

    class ContractThread extends Thread {
        private String jsonData = "";
        public ContractThread(String jsonData){
            this.jsonData = jsonData;
        }

        @Override
        public void run() {
            SkyLog.d("ContractThread start :: " + jsonData);

            try {
                JSONObject jsonObject_succes = new JSONObject(jsonData);                     //SUCCESS
                if(jsonObject_succes.getString("success").equals("true")){
                    JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));
                    JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_data.getString("listSimRi"));

                    SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());
                    MainActivity.listSimRi.clear();
                    for (int i = 0; i < jsonObject_listSimRi.length(); i++) {
                        JSONObject jsonObject = jsonObject_listSimRi.getJSONObject(i);
                        String[] phone_Arr = new String[1];

                        //NMR 수정해야할곳!!
//                            if(i == 0){
//                                phone_Arr[0] = "01033435914";
//                            }else{
//                                phone_Arr[0] = "" + i;
//
//                            }
                        phone_Arr[0] = "" + jsonObject.getString("Phone");
                        MainActivity.listSimRi.add(new SimRi(
                                jsonObject.getString("Phone") ,
                                //phone_Arr[0] ,
                                jsonObject.getInt("Id") ,
                                jsonObject.getInt("No") ,
                                jsonObject.getString("NickName") ,
                                jsonObject.getString("Name") ,
                                jsonObject.getString("UserInfo") ,
                                jsonObject.getString("GwanInfo") ,
                                jsonObject.getString("SimRiInfo") ,
                                jsonObject.getString("Value") ,
                                jsonObject.getDouble("iDou") ,
                                jsonObject.getBoolean("XO")));
                        dataSet.readSMSMessage(mContext , phone_Arr , jsonObject.getString("Name"));
                    }
                    SkyLog.d("readSMSMessage end :: ");

                    //전화번호부 저장
                    Message msg2 = mEndAfterAccum.obtainMessage();
                    mEndAfterAccum.sendMessage(msg2);
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

}