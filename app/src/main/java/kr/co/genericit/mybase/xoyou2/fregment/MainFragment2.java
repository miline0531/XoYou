package kr.co.genericit.mybase.xoyou2.fregment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1LeftListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1ListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag2ListAdapter;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.QaListObj;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.model.WeListObj;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class MainFragment2 extends Fragment {
    public Activity ac;

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ListView list;
    private ArrayList<WeListObj> weList = new ArrayList<WeListObj>();
    private MainFrag2ListAdapter m_Adapter;


    @Override
    public void onResume() {
        super.onResume();
        SkyLog.d("-- onResume --");

        getWeList();
    }

    public static MainFragment2 newInstance(int number, Activity _ac) {
        MainFragment2 fragment2 = new MainFragment2(_ac);
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    public MainFragment2(Activity _ac){
        this.ac = _ac;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_top_2, container, false);
        list = (ListView)view.findViewById(R.id.list);

        m_Adapter = new MainFrag2ListAdapter( ac, weList);
        list.setOnItemClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);

        return view;

    }


    private void getWeList(){
        CommandUtil.getInstance().showLoadingDialog(ac);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_WE_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(ac, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            SkyLog.d("mItemClickListener position :: " + position);
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
                        CommandUtil.getInstance().showCommonOneButtonDialog(ac,
                                jsonObject_succes.getString("error") + getClass().toString(),
                                ac.getResources().getString(R.string.str_cofirm),
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