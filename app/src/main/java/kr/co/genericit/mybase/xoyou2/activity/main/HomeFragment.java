package kr.co.genericit.mybase.xoyou2.activity.main;

import static kr.co.genericit.mybase.xoyou2.storage.JWSharePreference.PREFERENCE_AUTO_FINGER_LOGIN;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import co.kr.sky.AccumThread;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.CalendarActivity;
import kr.co.genericit.mybase.xoyou2.activity.CardActivity;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.MongStoreSaveActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MyMongRegistActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MymongDetailActivity;
import kr.co.genericit.mybase.xoyou2.activity.xoyou.ChattingRoomActivity;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1Left3ListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1LeftListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag1ListAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag2ListAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.model.QaListObj;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestLogin;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestSelectData;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStorySearch;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.network.response.LoginResult;
import kr.co.genericit.mybase.xoyou2.network.response.MongSelectDataResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;
import kr.co.genericit.mybase.xoyou2.view.PwAuthDialog;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.BottomSheetDefaultListFragment;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;


public class HomeFragment extends Fragment{
    public Context mContext;

    //SKY
    private CommonUtil dataSet = CommonUtil.getInstance();
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private ArrayList<SimRi> listSimRi = new ArrayList<SimRi>();
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

        getUserList();
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
        m_Adapter = new MainFrag1ListAdapter( MainActivity.mainAc, listSimRi);
        m_Adapter.setListOnClickListener(mItemClickListener);
        list.setAdapter(m_Adapter);



//        view.findViewById(R.id.popview).setOnClickListener(btnListener);
//        view.findViewById(R.id.btn_1).setOnClickListener(btnListener);

        return view;
    }

    public MainFrag1ListAdapter.listOnClickListener mItemClickListener = new MainFrag1ListAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            SkyLog.d("CLACIK id :: "  + id);
            SkyLog.d("CLACIK action :: "  + action);
            SkyLog.d("CLACIK getPhone :: "  + listSimRi.get(id).getPhone());
            MainActivity.homeClickPosition = id;
            MainActivity.homeClickObj = listSimRi.get(id);
            ((MainActivity)getContext()).refreshSlide2();
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



    //받은건 flag = 0 , 보낸건 flag = 1
    public int readSMSMessage(String[] arrVal , String name) {
        //내가 받은것!
        Uri allMessage = Uri.parse("content://sms/inbox/");


        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(allMessage,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                "address=?", arrVal,
                "date DESC");

        while (c.moveToNext()) {
            kr.co.genericit.mybase.xoyou2.model.Message msg = new kr.co.genericit.mybase.xoyou2.model.Message(); // 따로 저는 클래스를 만들어서 담아오도록 했습니다.

            long messageId = c.getLong(0);
            msg.setMessageId(String.valueOf(messageId));

            long threadId = c.getLong(1);
            msg.setThreadId(String.valueOf(threadId));

            String address = c.getString(2);
            msg.setAddress(address);

            long contactId = c.getLong(3);
            msg.setContactId(String.valueOf(contactId));

            String contactId_string = String.valueOf(contactId);
            msg.setContactId_string(contactId_string);

            long timestamp = c.getLong(4);
            msg.setTimestamp(String.valueOf(timestamp));

            String body = c.getString(5);
            msg.setBody(body);

            SkyLog.d("==============SMS==============");
            SkyLog.d("messageId :: " + messageId);
            SkyLog.d("threadId :: " + threadId);
            SkyLog.d("address :: " + address);
            SkyLog.d("contactId_string :: " + contactId_string);
            SkyLog.d("timestamp :: " + timestamp);
            SkyLog.d("body :: " + body);

            ContractObj obj = new ContractObj("" + messageId , "" +threadId , address , contactId_string , "" +timestamp , body , "0" , name);
            dataSet.sqlContractInsert(mContext , obj);

            SkyLog.d("==============SMS==============");


            //arrayList.add(msg); //이부분은 제가 arraylist에 담으려고 하기떄문에 추가된부분이며 수정가능합니다.

        }

        //내가 보낸것!
        Uri allMessage2 = Uri.parse("content://sms/sent/");
        Cursor c2 = cr.query(allMessage2,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                "address=?", arrVal,
                "date DESC");

        while (c2.moveToNext()) {
            kr.co.genericit.mybase.xoyou2.model.Message msg = new kr.co.genericit.mybase.xoyou2.model.Message(); // 따로 저는 클래스를 만들어서 담아오도록 했습니다.

            long messageId = c2.getLong(0);
            msg.setMessageId(String.valueOf(messageId));

            long threadId = c2.getLong(1);
            msg.setThreadId(String.valueOf(threadId));

            String address = c2.getString(2);
            msg.setAddress(address);

            long contactId = c2.getLong(3);
            msg.setContactId(String.valueOf(contactId));

            String contactId_string = String.valueOf(contactId);
            msg.setContactId_string(contactId_string);

            long timestamp = c2.getLong(4);
            msg.setTimestamp(String.valueOf(timestamp));

            String body = c2.getString(5);
            msg.setBody(body);

            SkyLog.d("==============SMS==============");
            SkyLog.d("messageId :: " + messageId);
            SkyLog.d("threadId :: " + threadId);
            SkyLog.d("address :: " + address);
            SkyLog.d("contactId_string :: " + contactId_string);
            SkyLog.d("timestamp :: " + timestamp);
            SkyLog.d("body :: " + body);

            ContractObj obj = new ContractObj("" + messageId , "", address , contactId_string , "" +timestamp , body , "1" , name);
            dataSet.sqlContractInsert(mContext , obj);

            SkyLog.d("==============SMS==============");


            //arrayList.add(msg); //이부분은 제가 arraylist에 담으려고 하기떄문에 추가된부분이며 수정가능합니다.

        }
        return 0;
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
                        JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));
                        JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_data.getString("listSimRi"));

                        SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());
                        listSimRi.clear();
                        for (int i = 0; i < jsonObject_listSimRi.length(); i++) {
                            JSONObject jsonObject = jsonObject_listSimRi.getJSONObject(i);
                            String[] phone_Arr = new String[1];

                            if(i == 0){
                                phone_Arr[0] = "01033435914";
                            }else{
                                phone_Arr[0] = "" + i;

                            }
                            listSimRi.add(new SimRi(
                                    //jsonObject.getString("Phone") ,
                                    phone_Arr[0] ,
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
                            readSMSMessage(phone_Arr , jsonObject.getString("Name"));
                        }
                        m_Adapter.notifyDataSetChanged();




                        //전화번호부 저장
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

}