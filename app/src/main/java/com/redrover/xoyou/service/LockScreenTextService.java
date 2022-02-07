package com.redrover.xoyou.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.redrover.xoyou.R;

/**
 * 잠금화면 상태에서 서비스
 */
public class LockScreenTextService extends Service {
	private BroadcastReceiver mReceiver;
	private boolean isShowing = false;
	protected View rootView;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private WindowManager windowManager;
	WindowManager.LayoutParams params;

	@Override
	public void onCreate() {
		super.onCreate();

//		//set parameters for the textview
//		params = new WindowManager.LayoutParams(
//				WindowManager.LayoutParams.WRAP_CONTENT,
//				WindowManager.LayoutParams.WRAP_CONTENT,
//				WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
//				PixelFormat.TRANSLUCENT);

		//Register receiver for determining screen off and if user is present
		mReceiver = new LockScreenStateReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);
		filter.addAction(Intent.ACTION_SCREEN_ON);

		registerReceiver(mReceiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public class LockScreenStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				//if screen is turn off show the textview
//				if (!isShowing) {
//					Log.v("tmpay","잠금----");
//					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//					rootView = layoutInflater.inflate(R.layout.call_popup_top, null);
//					windowManager.addView(rootView, params);
//					isShowing = true;
//				}
			}

			else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				//Handle resuming events if user is present/screen is unlocked remove the textview immediately
				if (isShowing) {
					Log.v("ifeelbluu","오픈----");
					windowManager.removeViewImmediate(rootView);
					isShowing = false;
				}
			}

			else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				//if screen is turn off show the textview

				if (!isShowing) {
					Log.v("ifeelbluu","열림----");
					LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
					rootView = layoutInflater.inflate(R.layout.call_popup_top, null);

					windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
					Display display = windowManager.getDefaultDisplay();
					int width = (int) (display.getWidth() * 0.9);

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
						params = new WindowManager.LayoutParams(
								WindowManager.LayoutParams.WRAP_CONTENT,
								WindowManager.LayoutParams.WRAP_CONTENT,
								WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//							| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
								,
								PixelFormat.TRANSLUCENT);
					}else{
						params = new WindowManager.LayoutParams(
								width, WindowManager.LayoutParams.WRAP_CONTENT,
								WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
								WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
										| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
										| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
										| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON, PixelFormat.TRANSLUCENT);
					}

					windowManager.addView(rootView, params);
					isShowing = true;
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		//unregister receiver when the service is destroy
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}

		//remove view if it is showing and the service is destroy
		if (isShowing) {
			windowManager.removeViewImmediate(rootView);
			isShowing = false;
		}
		super.onDestroy();
	}

}
