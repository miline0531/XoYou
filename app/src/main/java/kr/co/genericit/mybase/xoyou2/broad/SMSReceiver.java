package kr.co.genericit.mybase.xoyou2.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.LocationListActivity;
import kr.co.genericit.mybase.xoyou2.activity.WebViewActivity;
import kr.co.genericit.mybase.xoyou2.activity.xoyou.ChattingRoomActivity;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestAddLocation;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestUpdateLocation;
import kr.co.genericit.mybase.xoyou2.network.response.AddLocationResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateLocationResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;


public class SMSReceiver extends BroadcastReceiver {
    private CommonUtil dataSet = CommonUtil.getInstance();


    @Override
    public void onReceive(Context context, Intent intent) {
        SkyLog.d("onReceive() called");

        SkyLog.d( "onReceive() called");

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages.length > 0){
            String sender = messages[0].getOriginatingAddress();
            String content = messages[0].getMessageBody().toString();
            Date date = new Date(messages[0].getTimestampMillis());

            SkyLog.d( "sender: " + sender);
            SkyLog.d( "content: " + content);
            SkyLog.d( "date: " + date);
            String[] phone_Arr = new String[1];
            phone_Arr[0] = sender;

            ArrayList<ContractObj> obj = dataSet.sqlSelectContract(context , sender);
            if(obj.size() > 0){
                if(dataSet.readSMSMessage(context , phone_Arr , obj.get(0).getName()) == 0){

                    Message msg2 = dataSet.LIFE_HANDLER.obtainMessage();
                    dataSet.LIFE_HANDLER.sendMessage(msg2);
                }
            }


        }

    }
    private SmsMessage[] parseSmsMessage(Bundle bundle){
        // PDU: Protocol Data Units
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for(int i=0; i<objs.length; i++){
            messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
        }

        return messages;
    }
}