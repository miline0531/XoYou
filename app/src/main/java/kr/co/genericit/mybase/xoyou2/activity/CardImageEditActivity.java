package kr.co.genericit.mybase.xoyou2.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.CardImageAddRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;

public class CardImageEditActivity extends CommonActivity {
	private RecyclerView recyclerView;
	private ImageView mCardImageView;

	private int masking_box_w = 90;
	private int masking_box_h = 90;

	private int findRes(String name, String type) {
		return getResources().getIdentifier(name, type, this.getPackageName());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_card_image_edit);
		super.onCreate(savedInstanceState);

		//카드 기본 배경이미지 셋팅
		mCardImageView = findViewById(R.id.img_01);
		int bgIndex = getIntent().getIntExtra("cardBgIndex",-1);
		if(bgIndex == -1){
			String uriString = getIntent().getStringExtra("uri");
			Uri uri = Uri.parse(uriString);
			mCardImageView.setImageURI(uri);
		}else{
			Drawable drawable = ContextCompat.getDrawable(this, findRes("card_back"+(bgIndex), "drawable"));
			mCardImageView.setImageDrawable(drawable);
		}

		//완료버튼
		((TextView) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						RelativeLayout container = (RelativeLayout) findViewById(R.id.layout01);//move_mask
						container.setDrawingCacheEnabled(true);
						container.buildDrawingCache();
//						container.setDrawingCacheQuality(RelativeLayout.DRAWING_CACHE_QUALITY_HIGH);
						Bitmap bitmap = container.getDrawingCache();

						String path = saveBitmapToPng(bitmap, Constants.CardImageName);

						container.setDrawingCacheEnabled(false);

//						Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(Constants.cardImageData, 0, Constants.cardImageData.length));
//						cardBg = findViewById(R.id.card_bg);
//        				cardBg.setImageDrawable(image);

						setResult(RESULT_OK);
						finish();

//						Intent it = new Intent(CardImageEditActivity.this, CardEditActivity.class);
//						startActivity(it);
					}
				});


		mainLayout = (RelativeLayout) findViewById(R.id.layout01);
		recyclerView = findViewById(R.id.rcv_card_image_add);
		setRecyclerView();
		UI_SET();
	}

	public String saveBitmapToPng(Bitmap bitmap , String name) {
		/**
		 * 캐시 디렉토리에 비트맵을 이미지파일로 저장하는 코드입니다.
		 *
		 * @version target API 28 ★ API29이상은 테스트 하지않았습니다.★
		 * @param Bitmap bitmap - 저장하고자 하는 이미지의 비트맵
		 * @param String fileName - 저장하고자 하는 이미지의 비트맵
		 *
		 * File storage = 저장이 될 저장소 위치
		 *
		 * return = 저장된 이미지의 경로
		 *
		 * 비트맵에 사용될 스토리지와 이름을 지정하고 이미지파일을 생성합니다.
		 * FileOutputStream으로 이미지파일에 비트맵을 추가해줍니다.
		 */

		File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
		String fileName = name + ".png";
		File imgFile = new File(storage, fileName);
		try {
			imgFile.createNewFile();
			FileOutputStream out = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); //썸네일로 사용하므로 퀄리티를 낮게설정
			out.close();
		} catch (FileNotFoundException e) {
			Log.e("saveBitmapToJpg","FileNotFoundException : " + e.getMessage());
		} catch (IOException e) {
			Log.e("saveBitmapToJpg","IOException : " + e.getMessage());
		}
		Log.d("imgPath" , getCacheDir() + "/" +fileName);
		return getCacheDir() + "/" +fileName;
	}



	public void setRecyclerView(){
		LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
		recyclerView.setLayoutManager(layoutManager);

		ArrayList<String> dataList = new ArrayList<>();
		for(int i=0; i<3; i++){
			dataList.add(String.valueOf(i));
		}

		CardImageAddRecyclerviewAdapter adapter = new CardImageAddRecyclerviewAdapter(this,dataList);
		adapter.setOnSelectCardListener(onSelectCardListener);
		recyclerView.setAdapter(adapter);
	}

	CardImageAddRecyclerviewAdapter.onSelectCardListener onSelectCardListener = new CardImageAddRecyclerviewAdapter.onSelectCardListener() {
		@Override
		public void onClickCard(int id) {
			btn_01_create(id);

		}
	};


	boolean check = true;
	private ImageView image1;
	public void UI_SET(){
//		image1 = new ImageView(CardImageEditActivity.this);
//		image1.setLayoutParams(new LayoutParams(
//				LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT));
//		image1.setBackgroundResource(R.drawable.ic_card_img_item_1);
//		mainLayout.addView(image1);
//		mLayoutHgt = mainLayout.getHeight();
//
//		LayoutParams layoutParams = (LayoutParams) image1.getLayoutParams();
//		layoutParams.width = masking_box_w;
//		layoutParams.height = masking_box_h;
//		layoutParams.leftMargin = 360;
//		layoutParams.topMargin = 295;
//		image1.setLayoutParams(layoutParams);
//		image1.setOnTouchListener(onTouchListener());
	}

	public void btn_01_create(int index){
		mainLayout = (RelativeLayout) findViewById(R.id.layout01);
		image = new ImageView(CardImageEditActivity.this);

		LayoutParams a = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		image.setBackgroundResource(findRes("ic_card_img_item_"+(index+1),"drawable"));
		mainLayout.addView(image);

		a.width = masking_box_w;
		a.height = masking_box_h;
		a.leftMargin = 300;
		a.topMargin = 300;
		image.setLayoutParams(a);
//		image.setBackgroundResource(R.drawable.img_mask);
//		mainLayout.addView(image);
		image.setOnTouchListener(onTouchListener());
		mLayoutHgt = mainLayout.getHeight();
	}

	private ViewGroup mainLayout;
	private int mLayoutHgt = 0;

	private ImageView image;
	private int xDelta;
	private int yDelta;
	private int xSize;
	private int ySize;
	private int viewWidth = 0;
	private int viewHeight = 0;
	private boolean isSizeUpPoint = false;
	private boolean isMovePoint = false;



	private OnTouchListener onTouchListener() {
		return new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(mLayoutHgt == 0){
					mLayoutHgt = mainLayout.getHeight();
				}
				int point =(int)(view.getWidth() * 0.80);
				int pointy =(int)(view.getHeight() * 0.80);
				Log.v("ifeelbluu", "isMovePoint == " + isMovePoint);
				Log.v("ifeelbluu", "isSizeUpPoint == " + isSizeUpPoint);

				if(point < (int)event.getX() || pointy < (int)event.getY()){
					if(event.getAction() == MotionEvent.ACTION_DOWN && isMovePoint == true)
						isMovePoint = false;
				}

				if(point < (int)event.getX() || isSizeUpPoint == true || pointy < (int)event.getY() && isMovePoint == false){
					if(isMovePoint == true){
						return false;
					}
					final int x = (int) event.getRawX();
					final int y = (int) event.getRawY();


					switch (event.getAction() & MotionEvent.ACTION_MASK) {

						case MotionEvent.ACTION_DOWN:
							isMovePoint = false;
							isSizeUpPoint = true;
							LayoutParams lParams = (LayoutParams)
									view.getLayoutParams();
							xSize = x;
							ySize = y;
							viewWidth = view.getWidth();
							viewHeight = view.getHeight();

							break;

						case MotionEvent.ACTION_UP:
							isSizeUpPoint = false;
							break;

						case MotionEvent.ACTION_MOVE:
							Log.v("ifeelbluu","ACTION_MOVE :: 11" );
							LayoutParams layoutParams = (LayoutParams) view
									.getLayoutParams();
							if(xSize < x){
								//가로up
								layoutParams.width = viewWidth + (x-xSize);
							}else{
								//가로down
								if(viewWidth - (xSize-x) <225){
									layoutParams.width = 225;
								}else{
									layoutParams.width = viewWidth - (xSize-x);
								}

							}
							if(ySize < y){
								//세로up
								if((layoutParams.topMargin + viewHeight + (y-ySize)) > mLayoutHgt){

								}else{
									layoutParams.height = viewHeight + (y-ySize);
								}
							}else{
								//세로down
								if(viewHeight - (ySize-y) < 38){
									layoutParams.height = 38;
								}else{
									layoutParams.height = viewHeight - (ySize-y);
								}

							}
							view.setLayoutParams(layoutParams);
							break;
					}
					mainLayout.invalidate();

					return true;
				}else{
					if(isSizeUpPoint == true){
						return false;
					}
					final int x = (int) event.getRawX();
					final int y = (int) event.getRawY();

					switch (event.getAction() & MotionEvent.ACTION_MASK) {

						case MotionEvent.ACTION_DOWN:
							isSizeUpPoint = false;
							isMovePoint = true;
							LayoutParams lParams = (LayoutParams)
									view.getLayoutParams();

							xDelta = x - lParams.leftMargin;
							yDelta = y - lParams.topMargin;

							break;

						case MotionEvent.ACTION_UP:
							isMovePoint = false;
							break;

						case MotionEvent.ACTION_MOVE:
							Log.v("ifeelbluu","ACTION_MOVE :: 22" );
							if(isMovePoint == false){
								return false;
							}
							LayoutParams layoutParams = (LayoutParams) view
									.getLayoutParams();
							if(x - xDelta <0){
								layoutParams.leftMargin = 0;
							}else{
								int mWidth = ((int)layoutParams.leftMargin+layoutParams.width) - mainLayout.getWidth();

								if(mWidth >= 50){
									int rMargin = mWidth;
									layoutParams.leftMargin = x - xDelta + rMargin;
									layoutParams.rightMargin = rMargin;
									Log.v("ifeelbluu","가로체크 삭제==");
									isMovePoint = false;
									mainLayout.removeView(view);
									mainLayout.invalidate();
									event.setAction(MotionEvent.ACTION_UP);
								}else{
									layoutParams.leftMargin = x - xDelta;
									Log.v("ifeelbluu", "layoutParams.leftMargin :: " + layoutParams.leftMargin);
								}

							}

							if(y - yDelta <0){
								layoutParams.topMargin = 0;
							}else if((y - yDelta +view.getHeight()) > mLayoutHgt){
								layoutParams.topMargin = mLayoutHgt - view.getHeight();
							}else{
								layoutParams.topMargin = y - yDelta;
							}

//							layoutParams.rightMargin = 0;
							layoutParams.bottomMargin = 0;
							view.setLayoutParams(layoutParams);

							break;
					}
					mainLayout.invalidate();
					return true;
				}
			}


		};
	}
}
