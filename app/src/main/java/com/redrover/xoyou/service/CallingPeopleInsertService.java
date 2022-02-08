package com.redrover.xoyou.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.icu.text.DecimalFormat;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.redrover.xoyou.R;
import com.redrover.xoyou.broadcast.IncomingCallBroadcastReceiver;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.MyCallChart;
import com.redrover.xoyou.network.request.ActionRequestAddRelation;
import com.redrover.xoyou.network.request.ActionRequestAddRelation2;
import com.redrover.xoyou.network.requestxo.ActionRequestMycallGetData;
import com.redrover.xoyou.network.response.AddRelationResult;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.popup.ContractInsertPopUp;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CallingPeopleInsertService extends Service {
	private IncomingCallBroadcastReceiver mReceiver = null;
	public static final String EXTRA_CALL_NUMBER = "call_number";
	public static boolean RingRing = false;
	protected View rootView;
	private EditText relationEdt, nameEdt, tv_birthday , edt_phone;
	String call_number;
	WindowManager.LayoutParams params;
	private WindowManager windowManager;
	IBinder mBinder = new MyBinder();
	private String IN_SEQ="",USER_ID="",GWANGYE="",NICK_NAME="",NAME="",MW="1",BIRTH_DATE="",IMAGE_URL="" , callNumber = "";
	private JWSharePreference jwSharePreference;
	private RadioGroup radioGroup;
	private String gender = "0";
	private boolean isFinish = false;
	private int mYear =0, mMonth=0, mDay=0;
	private LinearLayout vDatePicker ;
	private NumberPicker picker_year ;
	private NumberPicker picker_month ;
	private static final int MAX_YEAR = 2099;
	private static final int MIN_YEAR = 1980;


	class MyBinder extends Binder {
		CallingPeopleInsertService getService() { // 서비스 객체를 리턴
			return CallingPeopleInsertService.this;
		}
	}

	@Override public IBinder onBind(Intent intent) {
// Not used
		return mBinder;
	}

	private void startForeGround() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default");
		builder.setSmallIcon(R.drawable.ic_launcher_foreground);
		builder.setContentTitle("xoYou 심리분석");
		builder.setContentText("관계인 심리분석 콜 대기중");
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
		rootView = layoutInflater.inflate(R.layout.activity_contract_calling, null);

		Log.v("hongjin","=========================" + rootView.isShown() );

		edt_phone = (EditText)rootView.findViewById(R.id.edt_phone);
		relationEdt = (EditText)rootView.findViewById(R.id.edt_relation);
		nameEdt = (EditText)rootView.findViewById(R.id.edt_name);
		tv_birthday = (EditText)rootView.findViewById(R.id.tv_birthday);
		radioGroup = (RadioGroup)rootView.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

		Calendar calendar = new GregorianCalendar();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		vDatePicker = (LinearLayout)rootView.findViewById(R.id.vDatePicker);
		picker_month = (NumberPicker)rootView.findViewById(R.id.picker_month);
		picker_year = (NumberPicker)rootView.findViewById(R.id.picker_year);
		Calendar cal = Calendar.getInstance();

		picker_month.setMinValue(1);
		picker_month.setMaxValue(12);
		picker_month.setValue(cal.get(Calendar.MONTH) + 1);

		int year = cal.get(Calendar.YEAR);
		picker_year.setMinValue(MIN_YEAR);
		picker_year.setMaxValue(MAX_YEAR);
		picker_year.setValue(year);

		//datePicker.init(mYear, mMonth, mDay,mOnDateChangedListener);


		((TextView)rootView.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removePopup();
			}
		});

		((Button)rootView.findViewById(R.id.btn_cal_cancel)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				vDatePicker.setVisibility(View.GONE);
				//removePopup();
			}
		});


		((Button)rootView.findViewById(R.id.btn_cal_confirm)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SkyLog.d("picker_year.getValue() :: " + picker_year.getValue());
				SkyLog.d("picker_month.getValue() :: " + picker_month.getValue());
				vDatePicker.setVisibility(View.GONE);
				String year = "" + picker_year.getValue();
				String month = "" + picker_month.getValue();
				tv_birthday.setText("" + year.substring(2,4) + (month.length() == 1 ? "0"+month : month) );
				//removePopup();
			}
		});
		((Button)rootView.findViewById(R.id.btn_birthday)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SkyLog.d("데이터 피커 호출");
				vDatePicker.setVisibility(View.VISIBLE);
			}
		});

		((TextView)rootView.findViewById(R.id.btn_setup)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				jwSharePreference = new JWSharePreference();
				IN_SEQ = jwSharePreference.getString(JWSharePreference.PREFERENCE_USER_SEQ,"");
				USER_ID = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
				GWANGYE = relationEdt.getText().toString();
				NAME = nameEdt.getText().toString();
				NICK_NAME = NAME;
				MW = gender;
				BIRTH_DATE = tv_birthday.getText().toString();
				IMAGE_URL = "";
				callNumber = edt_phone.getText().toString();


				if(GWANGYE.equals("")||NAME.equals("")||NICK_NAME.equals("")||BIRTH_DATE.equals("")||callNumber.equals("")){
					//todo 입력오류 얼럿!!
					Toast.makeText(getApplicationContext() , "필수항목을 모두 입력해주세요." , Toast.LENGTH_SHORT).show();
				}else{
					requestAddRelationship(); //등록

				}
			}
		});

		Log.v("hongjin","onCreate Service");
//		ButterKnife.inject(this, rootView);
		setDraggable();
	}

	//라디오 그룹 클릭 리스너
	RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
			if(i == R.id.rg_btn1){
				gender = "0";
			} else if(i == R.id.rg_btn2){
				gender = "1";
			}
		}
	};
	private void requestAddRelationship(){
		final String year = BIRTH_DATE.substring(0,4);
		//CommandUtil.getInstance().showLoadingDialog(getApplicationContext());
		SkyLog.d(""+IN_SEQ+"\t"+USER_ID+"\t"+GWANGYE+"\t"+NICK_NAME+"\t"+NAME+"\t"+MW+"\t"+year+"\t"+IMAGE_URL);
		ActionRuler.getInstance().addAction(new ActionRequestAddRelation2(getApplicationContext(),IN_SEQ,USER_ID,GWANGYE,NICK_NAME,NAME,MW,year,IMAGE_URL,callNumber, new ActionResultListener<AddRelationResult>() {
			@Override
			public void onResponseResult(AddRelationResult response) {
				try {
					CommandUtil.getInstance().dismissLoadingDialog();
					AddRelationResult result = response;

					SkyLog.d("관계인 등록 결과 :: " + result);
					String message = "";
					if(result.isSuccess()){
						//성공
						message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_success);
						isFinish = true;
					}else{
						message = CommandUtil.getInstance().getStr(R.string.add_relation_reg_fail);
					}

					Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
					if(isFinish) removePopup();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onResponseError(String message) {
				LogUtil.LogD("관계인 등록 실패 !!");
			}
		}));
		ActionRuler.getInstance().runNext();

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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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

					rst = "나쁜놈:::30";
				} catch (Exception e) {
					rst = null;
				}
				Log.e("hongjin", "rst = " + rst);

				if (rst != null) {
//					Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//					vibe.vibrate(500);
				} else {
					removePopup();
				}

				if (!TextUtils.isEmpty(call_number)) {
					edt_phone.setText(phoneNumberHyphenAdd(call_number,"N").replace("-" , "").replace(" ", ""));
				}
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Log.e("ifeelbluu","키보드 올리기");
						InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
						manager.showSoftInput(edt_phone, InputMethodManager.SHOW_IMPLICIT);
					}
				}, 10000);// 0.6초 정도 딜레이를 준 후 시작
			}else{
				SkyLog.d("전화 끊김!");
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						Log.e("ifeelbluu","키보드 올리기");
						InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
						manager.showSoftInput(edt_phone, InputMethodManager.SHOW_IMPLICIT);
					}
				}, 600);// 0.6초 정도 딜레이를 준 후 시작
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
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		removePopup();
	}

	//#팝업 닫기
	public void removePopup() {
		try{
			if (rootView != null && windowManager != null) windowManager.removeView(rootView);

			try {
				//isWatch = false;
				//memory.stop();
				//memory.destroy();
				//memory = null;
			} catch (Exception e) {
				Log.e("ifeelbluu","error :: " + e.getMessage());
			}
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


	public final int[] BARCHART_COLORS = {
			rgb("#FF3636"), rgb("#A6A6A6"),
			rgb("#4374D9"), rgb("#6B9900"),
			rgb("#F2CB61"), rgb("#8041D9"),
			rgb("#3DB7CC"), rgb("#D9418C")
	};

	public int rgb(String hex) {
		int color = (int) Long.parseLong(hex.replace("#", ""), 16);
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		return Color.argb(90,r, g, b);
	}



}

