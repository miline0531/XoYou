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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import co.kr.sky.Check_Preferences;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class MainFragment1 extends Fragment {
    public Activity ac;

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();

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

        getUserList();
        return view;

    }
    private void getUserList(){
        CommandUtil.getInstance().showLoadingDialog(ac);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_USER_LIST);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));

        //스레드 생성
        mThread = new AccumThread(ac, mAfterAccum, map, 5, 0, null);
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
                        JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));
                        JSONArray jsonObject_listSimRi = new JSONArray(jsonObject_data.getString("listSimRi"));

                        SkyLog.d("COUNT :: " + jsonObject_listSimRi.length());
//                        for (int i = 0; i < jsonObject_arr_reslut.length(); i++) {
//                            JSONObject jsonObject = jsonObject_arr_reslut.getJSONObject(i);
//                            totalCount = Integer.parseInt(jsonObject.getString("CNT"));
//                            arr_material.add(new MaterialListObj(
//                                    jsonObject.getString("MTL_IDX") ,
//                                    jsonObject.getString("PJT_GUID") ,
//                                    jsonObject.getString("PJT_HEAD_COM_IDX") ,
//                                    jsonObject.getString("COM_IDX") ,
//                                    jsonObject.getString("COM_NM") ,
//                                    jsonObject.getString("HIRAGANA") ,
//                                    jsonObject.getString("MTL_ID") ,
//                                    jsonObject.getString("AC_ID") ,
//                                    jsonObject.getString("MTL_NM") ,
//                                    jsonObject.getString("SPEC") ,
//                                    jsonObject.getString("LOGI_TRADE_GUID") ,
//                                    jsonObject.getString("LOGI_TRADE_NM") ,
//                                    jsonObject.getString("MTL_CLS_COD") ,
//                                    jsonObject.getString("MTL_CLS_NM") ,
//                                    jsonObject.getString("MTL_CLS_TYP") ,
//                                    jsonObject.getString("MTL_TYP_COD") ,
//                                    jsonObject.getString("MTL_TYP_NM") ,
//                                    jsonObject.getString("WEIGHT") ,
//                                    jsonObject.getString("LEN") ,
//                                    jsonObject.getString("BIM_MTL_ID") ,
//                                    jsonObject.getString("BLDG_NM") ,
//                                    jsonObject.getString("FLR_NM") ,
//                                    jsonObject.getString("GROUP_NM") ,
//                                    jsonObject.getString("DWG_GUID") ,
//                                    jsonObject.getString("CONVERT_STATUS") ,
//                                    jsonObject.getString("MTL_SN") ,
//                                    jsonObject.getString("CNT")));
//                        }
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