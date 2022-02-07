package com.redrover.xoyou.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.kr.sky.Check_Preferences;


public class ActivityEx extends Activity {

	protected ProgressDialog customDialog = null;
	CommonUtil dataSet = CommonUtil.getInstance();
	private static Typeface mTypeface = null;
	public LayoutInflater mLayoutInflater;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		if (!PermissionUtils.canWhitePhone(this)) {  // 폰 정보 권한이 없다면
			Log.i("SKY", "폰권한이 없다면");
		} else {
			// 권한 있음
			Log.e("SKY", "권한 있음");
//			logConfigurator.setFileName(Environment.getExternalStorageDirectory() + "/" + DEFINE.PROJECTTYPE + "/" +  dataSet.FullPatternDate("yyyy-MM-dd")  + "_file.log");
//			logConfigurator.configure();
		}


		mLayoutInflater	=	(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.parseColor("#000000"));
		}
//		if (mTypeface == null) {
//			mTypeface = Typeface.createFromAsset(this.getAssets(), "NanumGothic_0.ttf"); // 외부폰트 사용
//			//			mTypeface = Typeface.MONOSPACE;	// 내장 폰트 사용
//		}
//		setGlobalFont(getWindow().getDecorView());
		// 또는
		// View view = findViewById(android.R.id.content);
		// setGlobalFont(view);
	}



	@RequiresApi(api = Build.VERSION_CODES.DONUT)
	protected void drawBigImage(ImageView imageView, int resId) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inSampleSize = 1;
			options.inPurgeable = true;
			Bitmap src = BitmapFactory.decodeResource(getResources(), resId, options);
			Bitmap resize = Bitmap.createScaledBitmap(src, options.outWidth, options.outHeight, true);
			imageView.setImageBitmap(resize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//이미지 셋팅
	public int setimg(String _resName){
		String resName = "@drawable/" + _resName;
		String packName = this.getPackageName(); // 패키지명
		int resID = this.getResources().getIdentifier(resName, "drawable", packName);
		return resID;
	}
	private void setGlobalFont(View view) {
		if (view != null) {
			if(view instanceof ViewGroup){
				ViewGroup vg = (ViewGroup)view;
				int vgCnt = vg.getChildCount();
				for(int i=0; i < vgCnt; i++){
					View v = vg.getChildAt(i);
					if(v instanceof TextView){
						((TextView) v).setTypeface(mTypeface);
					}
					setGlobalFont(v);
				}
			}
		}
	}

	public Boolean loginmember_stauts(){
		if (Check_Preferences.getAppPreferences(this, "member_status").equals("Member")) {
			return true;
		}else{
			return false;
		}
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if ( customDialog != null)
			customDialog.dismiss();
	}
	protected void DialogSimple()
	{
		AlertDialog.Builder alt_shut = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
		alt_shut.setTitle("알림");
		alt_shut.setMessage("종료하시겠습니까?")
		.setCancelable(false)
		.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		})
		.setNegativeButton("취소", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		AlertDialog alert_shut = alt_shut.create();
		alert_shut.show();
	}
	public void confirmDialog(String message) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this , AlertDialog.THEME_HOLO_LIGHT);
		ab.setTitle("お知らせ");
		ab.setMessage(message);
		ab.setPositiveButton("確認", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				return;
			}
		})
		.show();
	}

	public static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}  
	// 특정 폴더의 파일 목록을 구해서 반환
	public String[] getFileList(String strPath) {
		// 폴더 경로를 지정해서 File 객체 생성
		File fileRoot = new File(strPath);
		// 해당 경로가 폴더가 아니라면 함수 탈출
		if( fileRoot.isDirectory() == false )
			return null;
		// 파일 목록을 구한다
		String[] fileList = fileRoot.list();
		return fileList;
	}

}
