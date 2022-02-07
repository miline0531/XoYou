package com.redrover.xoyou.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.lang.reflect.Method;

import com.redrover.xoyou.service.CallingService;

/**
 * 수신 및 발신상태에서 서비스가 비활성화일 경우 사용
 * 등록 후 바로 UI호출
 */
public class CallRegistOutGoingJobService extends JobIntentService {
	public static final int JOB_ID = 0x01;

	private IncomingCallBroadcastReceiver mReceiver = null;

	public static void enqueueWork(Context context, Intent work) {
		enqueueWork(context, CallRegistOutGoingJobService.class, JOB_ID, work);
	}

	@Override
	protected void onHandleWork(@NonNull Intent intent) {
		// your code
		Log.v("honghong","CallingService_CallingService_");
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

			//수신!!!
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Log.v("ifeelbluu", "incomingNumber :: " + incomingNumber);
				if(incomingNumber==null){
					return;
				}
				final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);

				Intent serviceIntent = new Intent(this, CallingService.class);
				serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
				startForegroundService(serviceIntent);
				return;
			}

			//발신!!!
			if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){

				TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

				Log.v("ifeelbluu", "telephonyManager :: " + telephonyManager.getCallState());

				String num = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.v("ifeelbluu", "ACTION_NEW_OUTGOING_CALL :: " + num);
				Intent serviceIntent = new Intent(this, CallingService.class);
				serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, num);
				startForegroundService(serviceIntent);
				return;
			}

			Intent serviceIntent = new Intent(this, CallingService.class);
			startForegroundService(serviceIntent);

		}
	}

	private Boolean isVideoCall(Context context) {
		//ITelephony 체크
		try{
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			Class c = Class.forName(tm.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");
			m.setAccessible(true);
			Object telephonyService = m.invoke(tm); // Get the internal ITelephony object
			c = Class.forName(telephonyService.getClass().getName()); // Get its class
			m = c.getDeclaredMethod("endCall"); // Get the "endCall()" method
			m.setAccessible(true); // Make it accessible
			m.invoke(telephonyService); // invoke endCall()

		}catch (Exception e){

		}
		return false;
	}
}

