package com.redrover.xoyou.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.Date;

import com.redrover.xoyou.common.CommonUtil;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.ContractObj;


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