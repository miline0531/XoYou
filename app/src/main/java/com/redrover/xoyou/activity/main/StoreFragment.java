package com.redrover.xoyou.activity.main;

import android.content.Context;
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
import com.redrover.xoyou.adapter.StoreFagmentListAdapter;
import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.SimRiUser;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;


public class StoreFragment extends Fragment {
    public Context mContext;

    //SKY
    private CommonUtil dataSet = CommonUtil.getInstance();
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ArrayList<SimRiUser> listSimRi = new ArrayList<SimRiUser>();
    private RecyclerView list;
    private StoreFagmentListAdapter m_Adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //context가져오기
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        list = (RecyclerView)view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(mContext)) ;


        m_Adapter = new StoreFagmentListAdapter( MainActivity.mainAc, listSimRi);
        m_Adapter.setListOnClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);
        getUserList();
        return view;
    }

    public StoreFagmentListAdapter.listOnClickListener mItemClickListener = new StoreFagmentListAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            SkyLog.d("CLACIK id :: "  + id);
            SkyLog.d("CLACIK action :: "  + action);
            SkyLog.d("CLACIK getPhone :: "  + listSimRi.get(id).getPhone());
            MainActivity.homeClickPosition = id;
            MainActivity.storeClickObj = listSimRi.get(id);
            MainActivity.fragmentPosionFlag = 1;
            //((MainActivity)getContext()).storeFragmentLiskClick();
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
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_USER_SIMLI_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        map.put("date", dataSet.FullPatternDate("yyyyMMddHHmmss"));

        //스레드 생성
        mThread = new AccumThread(MainActivity.mainAc, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }



    Handler mEndAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            m_Adapter.notifyDataSetChanged();
            CommandUtil.getInstance().dismissLoadingDialog();
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
            }
        }
    };

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
                    JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_succes.getString("data"));

                    SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());
                    listSimRi.clear();
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
                        listSimRi.add(new SimRiUser(
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
                                jsonObject.getString("Image") ,
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