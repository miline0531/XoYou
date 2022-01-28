package kr.co.genericit.mybase.xoyou2.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.broadcast.IncomingCallBroadcastReceiver;

public class CallingService extends Service {
	private IncomingCallBroadcastReceiver mReceiver = null;
	public static final String EXTRA_CALL_NUMBER = "call_number";
	public static boolean RingRing = false;
	protected View rootView;

//	@InjectView(R.id.tv_call_number)
	TextView tv_call_number;
	TextView tv_call_name;
	TextView txt_report_count;
	//Button btn_search_report;
	String call_number;
	WindowManager.LayoutParams params;
	private WindowManager windowManager;

	IBinder mBinder = new MyBinder();

	private CircularProgressView progressView1;
	private CircularProgressView progressView2;
	private CircularProgressView progressView3;
	private CircularProgressView progressView4;

	private TextView progressTxt1;
	private TextView progressTxt2;
	private TextView progressTxt3;
	private TextView progressTxt4;

	class MyBinder extends Binder {
		CallingService getService() { // 서비스 객체를 리턴
			return CallingService.this;
		}
	}

	@Override public IBinder onBind(Intent intent) {
// Not used
		return mBinder;
	}

	private void startForeGround() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default");
		builder.setSmallIcon(R.drawable.ic_launcher_foreground);
		builder.setContentTitle("title");
		builder.setContentText("실행중");
//		Intent notificationIntent = new Intent(this, ServiceActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		builder.setContentIntent(pendingIntent);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			manager.createNotificationChannel(new NotificationChannel("default","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
		}

		startForeground(1, builder.build());
	}
	@Override
	public void onCreate() {
		super.onCreate();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForeGround();
		}
		mReceiver = new IncomingCallBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		registerReceiver(mReceiver, filter);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		int width = (int) (display.getWidth() * 0.9); //Display 사이즈의 90%

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			params = new WindowManager.LayoutParams(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
					| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
					| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
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
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		rootView = layoutInflater.inflate(R.layout.call_popup_top, null);

		Log.v("hongjin","=========================" + rootView.isShown() );
		tv_call_number = (TextView)rootView.findViewById(R.id.tv_call_number);
		tv_call_name = (TextView)rootView.findViewById(R.id.tv_call_name);
		txt_report_count = (TextView)rootView.findViewById(R.id.txt_report_count);

		progressView1 = (CircularProgressView)rootView.findViewById(R.id.progressView1);
		progressView2 = (CircularProgressView)rootView.findViewById(R.id.progressView2);
		progressView3 = (CircularProgressView)rootView.findViewById(R.id.progressView3);
		progressView4 = (CircularProgressView)rootView.findViewById(R.id.progressView4);

		progressTxt1 = (TextView)rootView.findViewById(R.id.progressTxt1);
		progressTxt2 = (TextView)rootView.findViewById(R.id.progressTxt2);
		progressTxt3 = (TextView)rootView.findViewById(R.id.progressTxt3);
		progressTxt4 = (TextView)rootView.findViewById(R.id.progressTxt4);

		((ImageButton)rootView.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removePopup();
			}
		});

		//btn_search_report = (Button) rootView.findViewById(R.id.btn_search_report);


		Log.v("hongjin","onCreate Service");
//		ButterKnife.inject(this, rootView);
		setDraggable();
		//sampleProgress();
	}

	int cnt = 0;
	private void sampleProgress() {
		try {
			setProgressValue(1, ((int)(Math.random()*100)+1));
			setProgressValue(2, ((int)(Math.random()*100)+1));
			setProgressValue(3, ((int)(Math.random()*100)+1));
			setProgressValue(4, ((int)(Math.random()*100)+1));
			//setProgressValue(1, cnt++);
			//setProgressValue(2, cnt++);
			//setProgressValue(3, cnt++);
			//setProgressValue(4, cnt++);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public void setProgressValue(int index, int progress){
		try {
			switch (index){
				case 1:
					progressView1.setProgress(progress,true);
					progressTxt1.setText("재물\n"+progress+"%");
					break;
				case 2:
					progressView2.setProgress(progress,true);
					progressTxt2.setText("연애\n"+progress+"%");
					break;
				case 3:
					progressView3.setProgress(progress,true);
					progressTxt3.setText("건강\n"+progress+"%");
					break;
				case 4:
					progressView4.setProgress(progress,true);
					progressTxt4.setText("직업\n"+progress+"%");
					break;
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	private void setDraggable() {
		rootView.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;
			@Override public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						initialX = params.x;
						initialY = params.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						return true;
					case MotionEvent.ACTION_UP:
						return true;
					case MotionEvent.ACTION_MOVE:
						params.x = initialX + (int) (event.getRawX() - initialTouchX);
						params.y = initialY + (int) (event.getRawY() - initialTouchY);
						if (rootView != null)
							windowManager.updateViewLayout(rootView, params);
						return true;
				}
				return false;
			}
		});
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		try{
			Log.e("hongjin", "onStartCommandonStartCommandonStartCommand");

			if(intent.getStringExtra(EXTRA_CALL_NUMBER) != null && intent.getStringExtra(EXTRA_CALL_NUMBER).equals("") == false) {
				if (intent.getStringExtra(EXTRA_CALL_NUMBER).equals("-99")) {
					RingRing = false;
					removePopup();
					return START_REDELIVER_INTENT;
				} else {
					RingRing = true;
				}
				windowManager.addView(rootView, params);
				setExtra(intent);

				if (intent.getStringExtra(EXTRA_CALL_NUMBER).equals("-99")) {
					removePopup();
				}

				Log.e("hongjin", "call_number = " + call_number);

				String rst = null;
				try {
					//todo 전화번호 검색

					rst = "오늘의 귀인입니다~ 꼭 약속을 잡으세요~:::30";
				} catch (Exception e) {
					rst = null;
				}
				Log.e("hongjin", "rst = " + rst);

				if (rst != null) {
					String[] rstmsg = rst.split(":::");
					tv_call_name.setText(rstmsg[0]);
					String countmessage = "피해사례가 등록되어있습니다. ("+rstmsg[1]+"건)";
					txt_report_count.setText(countmessage);
//					Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//					vibe.vibrate(500);
					startCall();
				} else {
					removePopup();
				}

				if (!TextUtils.isEmpty(call_number)) {
					tv_call_number.setText(phoneNumberHyphenAdd(call_number,"N"));
				}
			}
		}catch (Exception e){
			Log.e("ifeelbluu",e.getMessage());
		}
		return START_REDELIVER_INTENT;
	}
	private void setExtra(Intent intent) {
		if (intent == null) {
			removePopup();
			return;
		}
		call_number = intent.getStringExtra(EXTRA_CALL_NUMBER);
		/*
		btn_search_report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//터치 인텐트
//				Intent i = new Intent(CallingService.this, MainTabActivity.class);
//				i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				i.putExtra("PN",call_number.replaceAll("-", ""));
//				CallingService.this.startActivity(i);
				removePopup();
			}
		});
    	*/
	}
	@Override public void onDestroy() {
		super.onDestroy();
		removePopup();
	}
	public void removePopup() {
		try{
			if (rootView != null && windowManager != null) windowManager.removeView(rootView);
		}catch (Exception e){

		}
	}

	public static String phoneNumberHyphenAdd(String num, String mask) {

		String formatNum = "";
//		if (StringUtils.NVL(num).equals("")) return formatNum;
		num = num.replaceAll("-","");

		if (num.length() == 11) {
			if (mask.equals("Y")) {
				formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
			}else{
				formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
			}
		}else if(num.length()==8){
			formatNum = num.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
		}else{
			if(num.indexOf("02")==0){
				if(mask.equals("Y")){
					formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-****-$3");
				}else{
					formatNum = num.replaceAll("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
				}
			}else{
				if(mask.equals("Y")){
					formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-****-$3");
				}else{
					formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
				}
			}
		}
		return formatNum;
	}

	boolean isWatch = false;
	WatchThread memory;
	int sWatch = 0;
	public void startCall(){
		isWatch = true;
		memory = new WatchThread();
		memory.start();
		Log.e("ifeelbluu","startCall ::: start");
	}


	class WatchThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (isWatch) {
				try {
					ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
					ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
					activityManager.getMemoryInfo(mi);
					Thread.sleep(5000);
					sWatch++;
					int min = sWatch / 60;
					int sec = sWatch % 60;
					Message msg = new Message();
					Log.e("ifeelbluu","startCall ::: sWatch : " + sWatch);
					if(sWatch == 0){
						isWatch = false;
						msg.what = 2222;
					}else{
						msg.what = 1111;
					}
					String tempzero = "";
					if(sec < 10){
						tempzero = "0";
					}
					msg.obj = String.valueOf(min + ":" + tempzero + sec);
					mWatchHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					Log.e("ifeelbluu","startCall ::: " +  e.getMessage());
					e.printStackTrace();
				}
				if(isWatch == false){
					sWatch = 0;
				}
			}

		}
	}

	Handler mWatchHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == 1111) {
				Log.v("ifeelbluu", "startCall ::: text : " + (String)msg.obj);
				//tv_call_name.setText((String)msg.obj);
				sampleProgress();
			}else{
				try {
					isWatch = false;
					memory.stop();
					memory.destroy();
					memory = null;
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return false;
		}
	});
}

