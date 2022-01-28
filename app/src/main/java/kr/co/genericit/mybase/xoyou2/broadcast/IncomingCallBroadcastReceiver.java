package kr.co.genericit.mybase.xoyou2.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.service.CallingService;

//브로드 캐스트 리시버

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {
	public static final String TAG = "PHONE STATE";
	private static String mLastState;
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	@SuppressLint("WrongConstant")
	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.d(TAG,"onReceive()");

		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Log.d("ifeelbluu","ACTION_SCREEN_ON");
		}

		try{
			//폰이 부팅될때 서비스등록
			if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
				Log.v("ifeelbluu", " ACTION_BOOT_COMPLETED ::::");
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					if(Constants.isStartService == false)
					Log.v("ifeelbluu", " ACTION_BOOT_COMPLETED :::: OS OREO");
					CallRegistJobService.enqueueWork(context, new Intent());
					Constants.isStartService = true;
				}else{
					Log.v("ifeelbluu", " ACTION_BOOT_COMPLETED :::: OS ELSE");
					Intent serviceIntent = new Intent(context, CallingService.class);
					context.startService(serviceIntent);
				}
				return;
			}

			//발신!!!
			if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
				//전화 걸때 서비스가 비활성화일 경우 서비스 등록
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Constants.isStartService == false){
					Log.v("ifeelbluu","ACTION_NEW_OUTGOING_CALL::::");
					Constants.isStartService = true;
					CallRegistOutGoingJobService.enqueueWork(context, intent);
					return;
				}

				String num = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Intent serviceIntent = new Intent(context, CallingService.class);

				serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, num);
				context.startService(serviceIntent);
				return;
			}


			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

			//RINGING(수신)시 호출이 2번들어옴 첫번째 null 무시 start
			String incomingNumber = null;
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				Log.v("ifeelbluu", "incomingNumber :: " + incomingNumber);
				if(incomingNumber==null){
					return;
				}
			}
			if (state.equals(mLastState)) {
				Log.v("ifeelbluu","mLastState:::: " + mLastState);
				return;
			} else {
				mLastState = state;
			}
			//RINGING(수신)시 호출이 2번들어옴 첫번째 null 무시 end


			//수신!!!
			Log.v("ifeelbluu","EXTRA_STATE_RINGING::::START");
			if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Constants.isStartService == false){
					//전화 수신시 서비스 등록되어있지않을경우 서비스 등록해 줌.
					Log.v("honghong","EXTRA_STATE_RINGING::::OREO");
					Constants.isStartService = true;
					CallRegistOutGoingJobService.enqueueWork(context, intent);
					return;
				}

				final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);

				Intent serviceIntent = new Intent(context, CallingService.class);
				serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, phone_number);
				context.startService(serviceIntent);

			}

			if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)){
				Intent serviceIntent = new Intent(context, CallingService.class);
				serviceIntent.putExtra(CallingService.EXTRA_CALL_NUMBER, "-99");
				context.startService(serviceIntent);
            }


		}catch (Exception e){

		}



	}
}
