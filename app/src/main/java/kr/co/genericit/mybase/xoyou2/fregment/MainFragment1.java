package kr.co.genericit.mybase.xoyou2.fregment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import co.kr.sky.Check_Preferences;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1Left3ListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1LeftListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1ListAdapter;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.QaListObj;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class MainFragment1 extends Fragment {
    public Activity ac;

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ArrayList<SimRi> listSimRi = new ArrayList<SimRi>();
    private ArrayList<QaListObj> qrList = new ArrayList<QaListObj>();
    private ListView list , list_left , list_left3;
    private MainFrag1ListAdapter m_Adapter;
    private MainFrag1LeftListAdapter m_LeftAdapter;
    private MainFrag1Left3ListAdapter m_Left3Adapter;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawer2;
    private String[] YouQA_Name ={"기본", "건강", "결혼", "명예", "사고", "이동", "인연","재능", "재물", "직업", "집안"};
    private RelativeLayout lb_slide;
    private RelativeLayout lb_slide3;
    private LinearLayout popview;

    public static MainFragment1 newInstance(int number, Activity _ac) {

        MainFragment1 fragment2 = new MainFragment1(_ac);
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    public MainFragment1(Activity _ac){
        this.ac = _ac;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_main_top_1, container, false);

        list = (ListView)view.findViewById(R.id.list);
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

        m_Adapter = new MainFrag1ListAdapter( ac, listSimRi);
        list.setOnItemClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);

        lb_slide3.setVisibility(View.GONE);

        getUserList();

        view.findViewById(R.id.popview).setOnClickListener(btnListener);
        view.findViewById(R.id.btn_1).setOnClickListener(btnListener);

        return view;

    }

    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popview:
                    SkyLog.d("-- btn_close --");
                    popview.setVisibility(View.GONE);
                    break;
                case R.id.btn_1:
                    SkyLog.d("-- btn_1 --");
                    popview.setVisibility(View.GONE);

                    break;
            }
        }
    };

    private void getUserList(){
        CommandUtil.getInstance().showLoadingDialog(ac);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_USER_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(ac, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }

    private void getQaList(){
        CommandUtil.getInstance().showLoadingDialog(ac);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_QA_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(ac, mAfterAccum, map, 5, 1, null);
        mThread.start();        //스레드 시작!!
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            SkyLog.d("position :: " + position);
            //MainActivity.viewSlide2();
            lb_slide3.setVisibility(View.GONE);
            lb_slide.setVisibility(View.VISIBLE);

            drawerLayout.openDrawer(drawer2);
            //API 호출(API_SELECT_QA_LIST)

        }
    };


    AdapterView.OnItemClickListener mItemClickListener2 = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            SkyLog.d("position :: " + position);
            //MainActivity.viewSlide2();
            drawerLayout.close();
            getQaList();
        }
    };


    AdapterView.OnItemClickListener mItemClickListener3 = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            SkyLog.d("position :: " + position);
            drawerLayout.close();
            popview.setVisibility(View.VISIBLE);
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
                        JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));
                        JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_data.getString("listSimRi"));

                        SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());

                        for (int i = 0; i < jsonObject_listSimRi.length(); i++) {
                            JSONObject jsonObject = jsonObject_listSimRi.getJSONObject(i);

                            listSimRi.add(new SimRi(
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
            } else if(msg.arg1 == 1) {
                String res  = (String)msg.obj;
                SkyLog.d("res 1: " + res);
                lb_slide3.setVisibility(View.GONE);
                lb_slide.setVisibility(View.GONE);




                try {
                    JSONObject jsonObject_succes = new JSONObject(res);                     //SUCCESS
                    if(jsonObject_succes.getString("success").equals("true")){
                        JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_succes.getString("data"));

                        SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());

                        for (int i = 0; i < jsonObject_listSimRi.length(); i++) {
                            JSONObject jsonObject = jsonObject_listSimRi.getJSONObject(i);
                            qrList.add(new QaListObj(
                                    jsonObject.getString("No") ,
                                    jsonObject.getString("Menu") ,
                                    jsonObject.getString("MenuGuBun") ,
                                    jsonObject.getString("MenuName") ,
                                    jsonObject.getString("MenuSeo")));
                        }

                        m_Adapter.notifyDataSetChanged();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lb_slide3.setVisibility(View.VISIBLE);
                                lb_slide.setVisibility(View.GONE);
                                drawerLayout.openDrawer(drawer2);
                            }
                        }, 1000); //딜레이 타임 조절
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