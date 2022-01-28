package kr.co.genericit.mybase.xoyou2.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import kr.co.genericit.mybase.xoyou2.service.CallingService;

/**
 * 폰 부팅완료  서비스 활성화
 * 서비스 등록 후 바로 UI호출 할 필요없는경우
 */
public class CallRegistJobService extends JobIntentService {
	private IncomingCallBroadcastReceiver mReceiver = null;
	public static final int JOB_ID = 0x01;

	public static void enqueueWork(Context context, Intent work) {
		enqueueWork(context, CallRegistJobService.class, JOB_ID, work);
	}

	@Override
	protected void onHandleWork(@NonNull Intent intent) {
		// your code
		Log.v("ifeelbluu","BootService:::::::");
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			try{
				mReceiver = new IncomingCallBroadcastReceiver();
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
				filter.addAction(Intent.ACTION_SCREEN_ON);
				filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
				registerReceiver(mReceiver, filter);
			}catch (Exception e){
				Log.e("ifeelbluu",e.getMessage());
			}

			Intent serviceIntent = new Intent(this, CallingService.class);
			startForegroundService(serviceIntent);
		}
	}
}

