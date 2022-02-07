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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.broadcast.IncomingCallBroadcastReceiver;
import com.redrover.xoyou.R;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.model.MyCallChart;
import com.redrover.xoyou.network.requestxo.ActionRequestMycallGetData;
import com.redrover.xoyou.network.response.DefaultResult;

public class CallingService extends Service {
	private IncomingCallBroadcastReceiver mReceiver = null;
	public static final String EXTRA_CALL_NUMBER = "call_number";
	public static boolean RingRing = false;
	protected View rootView;
	TextView tv_call_number;
	TextView tv_call_name;
	String call_number;
	WindowManager.LayoutParams params;
	private WindowManager windowManager;
	IBinder mBinder = new MyBinder();

	//텀 초단위
	int term = 2;
	boolean calling = true;

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
		rootView = layoutInflater.inflate(R.layout.call_popup_top, null);

		Log.v("hongjin","=========================" + rootView.isShown() );

		tv_call_number = (TextView)rootView.findViewById(R.id.tv_call_number);
		tv_call_name = (TextView)rootView.findViewById(R.id.tv_call_name);
		((ImageButton)rootView.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removePopup();
			}
		});

		Log.v("hongjin","onCreate Service");
//		ButterKnife.inject(this, rootView);
		setDraggable();

		initViewPager();
//		initLineGraph();

	}

	private ViewPager2 viewPager;
	private RelativeLayout rootLayoutBg;
	private LinearLayout mTab1,mTab2;
	private TextView mTabLabel1,mTabLabel2;
	private View mTabLine1,mTabLine2;

	private int[] fragments;
	private ViewsSliderAdapter mAdapter;

	private void initViewPager(){
		viewPager = rootView.findViewById(R.id.view_pager);
		rootLayoutBg = rootView.findViewById(R.id.rootLayoutBg);
		mTab1 = rootView.findViewById(R.id.tab1);
		mTab2 = rootView.findViewById(R.id.tab2);
		mTabLabel1 = rootView.findViewById(R.id.tab1_label);
		mTabLabel2 = rootView.findViewById(R.id.tab2_label);
		mTabLine1 = rootView.findViewById(R.id.tab1_line);
		mTabLine2 = rootView.findViewById(R.id.tab2_line);

		fragments = new int[]{R.layout.fragment_line_graph,
				R.layout.fragment_bar_graph};

		mAdapter = new ViewsSliderAdapter();
		viewPager.setAdapter(mAdapter);
		viewPager.registerOnPageChangeCallback(pageChangeCallback);

		mTab1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(0);
			}
		});
		mTab2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(1);
			}
		});
	}

	ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
		@Override
		public void onPageSelected(int position) {
			super.onPageSelected(position);
			switch (position){
				case 0:
					mTabLabel1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_text_on));
					mTabLabel2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_text_off));
					mTabLine1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_line_on));
					mTabLine2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_line_off));
					break;
				case 1:
					mTabLabel1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_text_off));
					mTabLabel2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_text_on));
					mTabLine1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_line_off));
					mTabLine2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_selected_line_on));
					break;
			}
//            setMainTopText(mainText);
		}
	};

	public class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
		public ViewsSliderAdapter() {

		}

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(viewType, parent, false);
			return new ViewsSliderAdapter.SliderViewHolder(view);
		}

		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

			if(position == 0 ){ //라인차트
				//팝업 초기화
				if(lineChart == null){
					lineChart = holder.itemView.findViewById(R.id.lineChart);
					//lineChartInit(lineChart);
					startCall();
				}
			}else{ //바차트
				barChart = holder.itemView.findViewById(R.id.barchart);

			}
		}

		@Override
		public int getItemViewType(int position) {
			return fragments[position];
		}

		@Override
		public int getItemCount() {
			return fragments.length;
		}

		public class SliderViewHolder extends RecyclerView.ViewHolder {
			public SliderViewHolder(View view) {
				super(view);

			}
		}

		private View.OnClickListener onClickTabAction = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		};
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
					tv_call_number.setText(phoneNumberHyphenAdd(call_number,"N"));
				}
			}
			//#팝업 데이터 초기화
			yVals1 = new ArrayList<>(); //item1 y
			yVals2 = new ArrayList<>(); //item2 y
			entry_count = 0; //item x
			//testCount = 0;
			sampleColorIndex = 0;
			rootView.invalidate();
			if(lineChart != null){
				Log.v("ifeelbluu", "startService :: [lineChart] not null" );
				//lineChartInit(lineChart);
				startCall();
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
				isWatch = false;
				//memory.stop();
				//memory.destroy();
				memory = null;
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

	//그래프 라이브러리------start
	private LineChart lineChart;
	private BarChart barChart;
	private boolean isWatch = false;
	private WatchThread memory;
	private int sWatch = 0;

	ArrayList<BarEntry> list = new ArrayList<>();
	private ArrayList<Entry> yVals1 = new ArrayList<>(); //item1 y
	private ArrayList<Entry> yVals2 = new ArrayList<>(); //item2 y
	private int entry_count = 0; //item x

	//private int testCount = 0;
	private int sampleColorIndex = 0;
	private void lineChartInit(LineChart chart1, MyCallChart data) {

		rootLayoutBg.setBackgroundColor(data.getIcolor());
		setData(chart1, data.getLine_data0(), data.getLine_data1());

		chart1.animateX(0); //anim
		chart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
		XAxis XAxis = chart1.getXAxis();
		XAxis.setLabelCount(10,false);
		XAxis.setTextColor(Color.BLACK);
		XAxis.setDrawAxisLine(true);
		XAxis.setDrawGridLines(true);
		XAxis.setValueFormatter(new MyXAxisValueFormatter());

		YAxis rightYAxis = chart1.getAxisRight();
		YAxis leftYAxis = chart1.getAxisLeft();
		leftYAxis.setEnabled(false);
		rightYAxis.setEnabled(false);

		chart1.getDescription().setEnabled(false);
		chart1.getLegend().setEnabled(true);
		chart1.setTouchEnabled(false);
		chart1.setEnabled(false);
		setLegends(chart1);

		if(barChart!=null)
			barChartInit(barChart, data);

		/*
		if(testCount % 3 == 0){

		}
		*/
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

	public void barChartInit(BarChart chart,MyCallChart c_data){
		/*
		list = new ArrayList<>();
		for(int i=0; i<6; i++){
			Random r = new Random();
			int ran = r.nextInt(9) + 1;
			list.add(new BarEntry(i, Float.valueOf(ran)));
		}
		*/
		list = new ArrayList<>();
		list.add(new BarEntry(0, c_data.getWoul().floatValue()));
		list.add(new BarEntry(1, c_data.getWisim().floatValue()));
		list.add(new BarEntry(2, c_data.getJaman().floatValue()));
		list.add(new BarEntry(3, c_data.getJogup().floatValue()));
		list.add(new BarEntry(4, c_data.getJjazung().floatValue()));
		list.add(new BarEntry(5, c_data.getChojo().floatValue()));

		String[] labels = {"우울", "의심", "자만", "조급", "짜증", "초조"};
		chart.setTouchEnabled(false);
		chart.getDescription().setEnabled(false);

		//샘플데이터
		BarDataSet barDataSet = new BarDataSet(list, "TEST");
		barDataSet.setColors(BARCHART_COLORS);
		barDataSet.setDrawValues(false);

		BarData data = new BarData(barDataSet);
		YAxis leftAxis = chart.getAxisLeft();
		YAxis rightAxis = chart.getAxisRight();
		leftAxis.setDrawAxisLine(true);
		rightAxis.setDrawAxisLine(true);
		leftAxis.setAxisLineColor(Color.GRAY);
		leftAxis.setEnabled(false);
		rightAxis.setEnabled(false);
		XAxis xAxis = chart.getXAxis();
		xAxis.setValueFormatter(new BarXAxisValueFormatter(labels));
		xAxis.setTextColor(Color.BLACK);
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setGridColor(Color.BLACK);
		xAxis.setAxisLineColor(Color.BLACK);
		xAxis.setDrawAxisLine(false);
		xAxis.setDrawGridLines(false);
		xAxis.setEnabled(true);
		chart.setDrawValueAboveBar(false);
		chart.setScaleEnabled(false);
		chart.setPinchZoom(false);
		chart.setFitBars(true);
		chart.setData(data);
		chart.animateY(0);

		Legend l = chart.getLegend();
		l.setEnabled(false);
	}


	public void setLegends(LineChart chart1){

		Legend l = chart1.getLegend();

		l.getEntries();
		l.setTextColor(Color.BLACK);

		l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

		l.setYEntrySpace(10f);
		l.setTextSize(16f);

		l.setWordWrapEnabled(true);

		LegendEntry l1=new LegendEntry("진정성",Legend.LegendForm.CIRCLE,10f,2f,null, ContextCompat.getColor(this, R.color.progress_circle_2));
		LegendEntry l2=new LegendEntry("과장", Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(this, R.color.progress_circle_4));

		l.setCustom(new LegendEntry[]{l1,l2});

		l.setEnabled(true);

	}

	public void setData(LineChart chart1, Double val1, Double val2){
		if(yVals1.size() == 10) {
			yVals1.remove(0);
			yVals2.remove(0);
			yVals1.add(new Entry(entry_count,val1.floatValue()));
			yVals2.add(new Entry(entry_count,val2.floatValue()));
		}else{
			yVals1.add(new Entry(entry_count,val1.floatValue()));
			yVals2.add(new Entry(entry_count,val2.floatValue()));
		}

		entry_count = entry_count + term;

		LineDataSet set1, set2;
		set1 = new LineDataSet(yVals1,"진정성");
		set1.setDrawCircles(true);
		set1.setLineWidth(1f);
		set1.setDrawValues(false);
		set1.setColor(ContextCompat.getColor(this, R.color.progress_circle_2));
		set1.setCircleColor(ContextCompat.getColor(this, R.color.progress_circle_2));
		set1.setCircleColorHole(ContextCompat.getColor(this, R.color.progress_circle_2));
		set1.setCircleSize(2f);
		set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

		set2 = new LineDataSet(yVals2,"과장");
		set2.setDrawCircles(true);
		set2.setLineWidth(1f);
		set2.setDrawValues(false);
		set2.setColor(ContextCompat.getColor(this, R.color.progress_circle_4));
		set2.setCircleColor(ContextCompat.getColor(this, R.color.progress_circle_4));
		set2.setCircleColorHole(ContextCompat.getColor(this, R.color.progress_circle_4));
		set2.setCircleSize(2f);
		set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

		LineData data = new LineData(set1,set2);
		chart1.setData(data);
	}

	public class MyXAxisValueFormatter implements IAxisValueFormatter {
		public MyXAxisValueFormatter() {
		}

		@Override
		public String getFormattedValue(float value, AxisBase axis) {
			// "value" represents the position of the label on the axis (x or y)
			String rst_str = "";
			int min = (int)value / 60;
			int sec = (int)value % 60;

			String tempzero = "";
			if(sec < 10){
				tempzero = "0";
			}

			rst_str = min + ":" + tempzero + (int)sec;

			return rst_str;
		}

		/** this is only needed if numbers are returned, else return 0 */

		public int getDecimalDigits() { return 0; }
	}

	public class BarXAxisValueFormatter implements IAxisValueFormatter {

		private DecimalFormat mFormat;
		private String[] mValues;
		public BarXAxisValueFormatter(String[] values) {
			this.mValues = values;
		}

		@Override
		public String getFormattedValue(float value, AxisBase axis) {
			// "value" represents the position of the label on the axis (x or y)
			return mValues[(int) value];
		}

		/** this is only needed if numbers are returned, else return 0 */

		public int getDecimalDigits() { return 0; }
	}

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
					Thread.sleep(term * 1000);
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
				getMyCallGetData();
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


	public void getMyCallGetData(){
		ActionRuler.getInstance().addAction(new ActionRequestMycallGetData( "ifeelbluu12", "01071036707",
				"진정성", "유혹", "혐오심" , new ActionResultListener<DefaultResult>() {
			@Override
			public void onResponseResult(DefaultResult response) {
				try {
					String result = response.getData();

					JSONObject jsonObject = new JSONObject(result);
					JSONObject borderColor = jsonObject.getJSONObject("borderColor");
					Double R = borderColor.getDouble("R");
					Double G = borderColor.getDouble("G");
					Double B = borderColor.getDouble("B");
					int r = (int) Math.round(255 * R);
					int g = (int) Math.round(255 * G);
					int b = (int) Math.round(255 * B);
					int icolor = Color.argb(255, r, g, b);

					String simri_data = jsonObject.getString("simri_data");
					Double line_data0 = jsonObject.getDouble("line_data0");
					Double line_data1 = jsonObject.getDouble("line_data1");

					JSONArray list_col_data = jsonObject.getJSONArray("list_col_data");

					Double woul = 0.0;
					Double wisim = 0.0;
					Double jaman = 0.0;
					Double jogup = 0.0;
					Double jjazung = 0.0;
					Double chojo = 0.0;

					for(int i =0; i<list_col_data.length(); i++){
						JSONObject obj = list_col_data.getJSONObject(i);
						if(obj.getString("name").equals("우울")){
							woul = obj.getDouble("dou");
						}else if(obj.getString("name").equals("의심")){
							wisim = obj.getDouble("dou");
						}else if(obj.getString("name").equals("자만")){
							jaman = obj.getDouble("dou");
						}else if(obj.getString("name").equals("조급")){
							jogup = obj.getDouble("dou");
						}else if(obj.getString("name").equals("짜증")){
							jjazung = obj.getDouble("dou");
						}else if(obj.getString("name").equals("초조")){
							chojo = obj.getDouble("dou");
						}
					}

					MyCallChart data = new MyCallChart();
					data.setIcolor(icolor);
					data.setSimri_data(simri_data);
					data.setLine_data0(line_data0);
					data.setLine_data1(line_data1);
					data.setWoul(woul);
					data.setWisim(wisim);
					data.setJaman(jaman);
					data.setJogup(jogup);
					data.setJjazung(jjazung);
					data.setChojo(chojo);

					tv_call_number.setText(simri_data);
					lineChartInit(lineChart,data);

				} catch (Exception e) {
					Log.e("TEST","에러" + e.getMessage());
					e.printStackTrace();
				}
			}
			@Override
			public void onResponseError(String message) {
				Log.d("TEST","에러 : " + message);
			}
		}));
		ActionRuler.getInstance().runNext();
	}
}

