package com.redrover.xoyou.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignatureView extends View {
	public static int MemoPadSize_W = 1000;
	public static int MemoPadSize_H = 2000;

	private boolean mErase = false;
	private String contentId = "";
	private String points = "";
	private String paints = "";
	private float org_wid = 900;
	private float org_hgt = 800;	
	private float start_x= 500;
	private float start_y = 500;
	
	boolean touch_status = false;
	int addcount = 0;
	
	private float mAnnotCancelX, mAnnotCancelY;
	private Path mAnnotCancelPath = null;
	public ArrayList<PointF> marrCancelPoint = null;
	
    private Paint mPaint;
    private ArrayList<PointF> mPointList;
    private ArrayList<Integer> mTypeList;
    private ArrayList<Integer> mPaintList;
    private ArrayList<Float> mBrush;
    private Path mPath;
    private boolean misSig=false;
    public int wid = 0;
	public int hgt = 0;

	private int maxWidthSize = 0;
	private int maxHeightSize = 0;
	
	private float heightMargin = 0;
	private float widthMargin = 0;
    private static float mZoomRatio = 2.0f;
    
    private Bitmap mBitmap;

    private Canvas mCanvas;

    private Paint mBitmapPaint;
    
	Context mContext;
	
	private boolean isOverSign = true;

	public float brSize = 50;
	
	public class mMemoPaths{
		public Path item_Path;
		public Paint item_Paint;
		public mMemoPaths(Path item_Path, Paint item_Paint) {
			super();
			this.item_Path = item_Path;
			this.item_Paint = item_Paint;
		}
		public Path getItem_Path() {
			return item_Path;
		}
		public void setItem_Path(Path item_Path) {
			this.item_Path = item_Path;
		}
		public Paint getItem_Paint() {
			return item_Paint;
		}
		public void setItem_Paint(Paint item_Paint) {
			this.item_Paint = item_Paint;
		}
	}
    public SignatureView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SignatureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public SignatureView(Context context) {
		super(context);
		init(context);
	}
	
/*	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int wid = right - left;
		int hgt = bottom - top;
		this.wid = wid;
		this.hgt = hgt;
	}*/
	
	private void setOverSign(boolean isOverSign){
		this.isOverSign = isOverSign;
	}

	public void setOverMargin(float widMargin, float hgtMargin){
		widthMargin = widMargin;
		heightMargin = hgtMargin;
	}

	public int prevColor = 0;
	public void setErase(boolean val){
		mErase = val;
		if(val == true){//지우개모드전환
			prevColor = mPaint.getColor();
			mPaint.setAntiAlias(true);
			setColor(Color.MAGENTA);
			mPaint.setAlpha(40);
		}else{//펜슬모드전환
			setColor(prevColor);
			mPaint.setAlpha(255);
		}
	}
	
	public boolean getErase(){
		return mErase;
	}
	
	public void setClear(){
		mPath = null;
		mPointList.clear();
        mTypeList.clear();
        mPaintList.clear();
        mBrush.clear();
		mPath = new Path();
		misSig = false;
		if(mBitmap != null){
			mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
			mCanvas = new Canvas(mBitmap);
		}
		invalidate();
	}
	
	public void initCanvas(){
		Log.v("ifeelbluu", "=-= onSizeChanged" );
        mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
		mCanvas = new Canvas(mBitmap);
		
		try {
			mCanvas.drawPath(mPath, mPaint);
		} catch (Exception e) {
			Log.v("ifeelbluu","e =-= " + e.getMessage().toString());
		}
	}
	
	public void setColor(int color){
		mPaint.setColor(color);
	}
	
	private void init(Context context){
		
		mContext = context;
		
		mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setStrokeWidth(brSize * mZoomRatio);
        mPath = new Path();
        mPointList = new ArrayList<PointF>();
        mTypeList = new ArrayList<Integer>();
        mPaintList = new ArrayList<Integer>();
        mBrush = new ArrayList<Float>();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
      //컬러픽
//        paths.add(new mMemoPaths(mPath,mPaint));
        Log.v("ifeelbluu","new path init =======");
        
	}

	
	public void setBrushSize(float brushSize){
		Log.v("ifeelbluu","brushSize === " + brushSize);
		brSize = brushSize;
		mPaint.setStrokeWidth(brSize * mZoomRatio);
	}

	public int getSignaturePopupViewWidth(){
		return wid;
	}

	public int getSignaturePopupViewHeight(){
		return hgt;
	}
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
		Log.v("ifeelbluu", "=-= onSizeChanged" );
		mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
		mCanvas = new Canvas(mBitmap);

		try {
			mCanvas.drawPath(mPath, mPaint);
		} catch (Exception e) {
			Log.e("ifeelbluu","e =-= " + e.getMessage().toString());
			// TODO: handle exception
		}
    }
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    	//int measuredWidth = measure(widthMeasureSpec);
		//int measuredHeight = measure(heightMeasureSpec);

		//setMeasuredDimension(measuredWidth, measuredHeight);
        setMeasuredDimension(wid, hgt);
    }
    
    private int measure(int measureSpec) {
		int result = 0;
		int specSize = MeasureSpec.getSize(measureSpec);
		result = specSize;

		return result;
	}
    public void setColorPaint(int c){
    	mPaint.setColor(c);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
    	try {
    		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
        canvas.drawPath(mPath, mPaint);
    	
    	//컬러픽
//        for (mMemoPaths p : paths) {    
//        	mPaint.setColor(p.getItem_Paint().getColor());
//            canvas.drawPath(p.getItem_Path(), mPaint);
//            Log.v("ifeelbluu", "getItem_Path = " + p.getItem_Path().toString());
//            Log.v("ifeelbluu", "getColor  = " + p.getItem_Paint().getColor());
//        }
        
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    
//    private void touch_start(float x, float y) {
//    	mPath.reset();
//    	mPath.moveTo(x, y);
//        mX = x;
//        mY = y;
//    }
//    
//    private void touch_move(float x, float y) {
//    	float dx = Math.abs(x - mX);
//		float dy = Math.abs(y - mY);
//		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
//			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
//			mX = x;
//			mY = y;
//		}
//    }
//    private void touch_up() {
//    	mPath.lineTo(mX, mY);
//		mCanvas.drawPath(mPath, mPaint);
//		mPath.reset();
//    }
    private void touch_start(float x, float y) {
    	if (mErase == true) {
    		setColor(Color.MAGENTA);
			mPaint.setAlpha(40);
    		mPath.reset();
        	mPath.moveTo(x, y);
			if (marrCancelPoint == null)
				marrCancelPoint = new ArrayList<PointF>();
			if (mAnnotCancelPath == null)
				mAnnotCancelPath = new Path();

			marrCancelPoint.clear();
			mAnnotCancelPath.reset();
			
			marrCancelPoint.add(new PointF(x, y));

			mAnnotCancelX = x;
			mAnnotCancelY = y;
			mAnnotCancelPath.moveTo(x, y);
			return;
		}else{
			if(touch_status == true){
	    		Log.v("ifeelbluu_touch","touch_start[remove] === " + mPointList.size());
	    		for(int i = 0; i<addcount; i++){
	    			mTypeList.remove(mTypeList.size()-1);
	    	        mPointList.remove(mPointList.size()-1);
	    	        mPaintList.remove(mPaintList.size()-1);
	    	        mBrush.remove(mBrush.size()-1);
	    	        Log.v("ifeelbluu_touch","touch_start[remove_end] === " + mPointList.size());
	    		}
	    	}
	    	touch_status = true;
	    	Log.v("ifeelbluu_touch","touch_start === " + x + " / " + y);
	    	mPath.reset();
	    	mPath.moveTo(x, y);
	    	mTypeList.add(0);
	    	mPointList.add(new PointF(x, y));
	    	mPaintList.add(mPaint.getColor());
	    	addcount ++;
	    	mBrush.add(brSize);
	        mX = x;
	        mY = y;
		}
    }
    
    private void touch_move(float x, float y) {
    	Log.v("ifeelbluu_touch","touch_move === " + x + " / " + y);
    	if (mErase == true) {
			float dx = Math.abs(x - mAnnotCancelX);
			float dy = Math.abs(y - mAnnotCancelY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mAnnotCancelX, mAnnotCancelY,
						(x + mAnnotCancelX) / 2, (y + mAnnotCancelY) / 2);
				mAnnotCancelPath.quadTo(mAnnotCancelX, mAnnotCancelY,
						(x + mAnnotCancelX) / 2, (y + mAnnotCancelY) / 2);

				mAnnotCancelX = x;
				mAnnotCancelY = y;
				marrCancelPoint.add(new PointF(x, y));
			}
			
			return;
		}else{
			addcount ++;
			if(isOverSign){
		    	mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
		        mX = x;
		        mY = y;
		        misSig=true;
		        mTypeList.add(1);
		        mPointList.add(new PointF(x, y));
		        mPaintList.add(mPaint.getColor());
		        mBrush.add(brSize);
	    	}else{
	    		if(x < 0 || y < 0 || x > wid || y > hgt){
	    			if(!(mX < 0 || mY < 0 || mX > wid || mY > hgt)){
	    				mPath.lineTo(mX, mY);
	    		    	mPointList.add(new PointF(mX, mY));
	    		    	mPaintList.add(mPaint.getColor());
	    		    	mBrush.add(brSize);
	    		    	mTypeList.add(2);
	    			}
	    		}else{
	    			if(mX < 0 || mY < 0 || mX > wid || mY > hgt){
	    				mPath.moveTo(x, y);
	    		    	mTypeList.add(0);
	    		    	mPointList.add(new PointF(x, y));
	    		    	mPaintList.add(mPaint.getColor());
	    		    	mBrush.add(brSize);
	    			}else{
		    			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
		    	        misSig=true;
		    	        mTypeList.add(1);
		    	        mPointList.add(new PointF(x, y));
		    	        mPaintList.add(mPaint.getColor());
		    	        mBrush.add(brSize);
	    			}
	    		}
	    		mX = x;
	    		mY = y;
	    	}
		}
    }
    private void touch_up() {
    	
    	Log.v("ifeelbluu_touch","touch_up === " + mX + " / " + mY);
    	if(mErase == true){
		mAnnotCancelPath.lineTo(mAnnotCancelX, mAnnotCancelY);
		PointF pt = new PointF();
		pt.x = mAnnotCancelX;
		pt.y = mAnnotCancelY;
		marrCancelPoint.add(pt);
		// mAnnotCancelPath.close();
		Log.d("MAPWorkBoard", "MODE WMODE_CANCEL STARED!!!!!!");
		cancelAnnot();
		mAnnotCancelPath = null;
		marrCancelPoint = null;
		Log.d("MAPWorkBoard", "MODE WMODE_CANCEL COMPLETED!!!!!!");
		mPath.reset();
		return;
    	}else{
    		touch_status = false;
        	addcount = 0;
        	mPaintList.add(mPaint.getColor());
        	getRGBColor(mPaint.getColor());
        	mBrush.add(brSize);
        	Log.v("ifeelbluu", " mPaint ===== add (" + mPaint.getColor() + ")");
        	mPath.lineTo(mX, mY);
        	mPointList.add(new PointF(mX, mY));
        	mTypeList.add(2);
            // commit the path to our offscreen
            //mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
        	mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
    	}
    }
    
    public int cancelAnnot() {
		if (mErase == false)
			return 0;

		int nSignCount = 0;
		int nCount = 0;

//		for (int i = mPointList.size() - 1; i >= 0; i--) {
//			MAPAnnotInfo annInfo = mCurPager.marrAnnotItem.get(i);
//			if (mCurSelItem.szformidtxt.equals(annInfo.mFormID)) {
//				nCount = annInfo.mArrAnnotPoint.size();
//				nSignCount = annInfo.removeAtIntersect(marrCancelPoint);
//				// 서명 포인트가 변경된 경우 path재정의 필요 
//				if (nSignCount != nCount)
//					annInfo.resetPath();
//			}
//		}
//		nSignCount = 0;
//		nCount = 0;
		if (mPointList != null) {
			nCount = mPointList.size();
			Log.v("cancelAnnot", "nCount === " + nCount);
			nSignCount = removeAtIntersect(marrCancelPoint);
			Log.v("cancelAnnot", "nSignCount === " + nSignCount);
			
			if (nSignCount == 0) {
				if(mBitmap != null){
					mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
					mCanvas = new Canvas(mBitmap);
				}
			} else {
				// 서명 포인트가 변경된 경우 path재정의 필요 
				if (nSignCount != nCount){
					if(mBitmap != null){
						mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
						mCanvas = new Canvas(mBitmap);
					}
					resetPath();
					invalidate();
				}
//				mPath = mCurAnnot.mAnnotPath;
//				mPointList = mCurAnnot.mArrAnnotPoint;
			}
		}
		return nCount;
	}
    
	private void annot_touch_start(float x, float y, Path path) {
		//path.reset();
		path.moveTo(x, y);
	}
	private void annot_touch_move(float x, float y, float dX, float dY, Path path) {
		path.quadTo(dX, dY, (x + dX)/2, (y + dY)/2);
	}
	private void annot_touch_up(float dX, float dY, Path path) {
		path.lineTo(dX, dY);
		try {
			if(mBitmap == null)
			mBitmap = Bitmap.createBitmap(wid, hgt, Bitmap.Config.ARGB_4444);
			
			if(mCanvas == null)
			mCanvas = new Canvas(mBitmap);
			
			mCanvas.drawPath(path, mPaint);
			Log.v("ifeelbluu",mPointList.size() + " / " + mTypeList.size() + " / " + mPaintList.size() + " / " + mBrush.size());
		} catch (Exception e) {
			Log.e("ifeelbluu", e.getMessage().toString());
		}
	}
    
	private RectF GetRoundRC() {
		RectF rc = new RectF();

		if (mPointList.size() == 0) {
			return null;
		}

		float x1, x2, y1, y2;
		x1 = 3000;
		y1 = 3000;
		x2 = 0;
		y2 = 0;

		for (int i = 0; i < mPointList.size(); i++) {
			PointF pt = mPointList.get(i);
			if (x1 >= pt.x) x1 = pt.x;
			if (x2 <= pt.x) x2 = pt.x;
			if (y1 >= pt.y) y1 = pt.y;
			if (y2 <= pt.y) y2 = pt.y;
		}

		rc.left = x1;
		rc.top = y1;
		rc.right = x2;
		rc.bottom = y2;

		return rc;

	}
	
	@SuppressWarnings("unchecked")
	private void onAnnotLoad(){
		float tempy = getTranslationY();
		
		ArrayList<PointF> pointList = new ArrayList<PointF>();
		mTypeList = new ArrayList<Integer>();
		try {
			JSONArray array = new JSONArray(points);
			
			for(int i=0 ; i<array.length();i++)
			{
				JSONObject obj2 = (JSONObject)array.get(i);
				Object x = obj2.get("x");
				JSONArray arrx = (JSONArray)x;
				Object y = obj2.get("y");
				JSONArray arry = (JSONArray)y;
				Object z = obj2.get("z");
				JSONArray arrz = (JSONArray)z;
				
				Log.v("ifeelbluu","obj2.get(color) ==== " + obj2.get("color"));
				String temp = (String)obj2.get("color");
				
				for(int j=0 ; j<arrx.length();j++)
				{
					PointF point = new PointF(Float.parseFloat(arrx.get(j).toString()), Float.parseFloat(arry.get(j).toString()));
					pointList.add(point);
					//start 0 move 1 up 2
					int type = 0;
					if(j == 0)
					{
						type = 0;
					}else if(j+1 == arrx.length())
					{
						type = 2;
					}else 
						type = 1;
					mTypeList.add(type);
					mPaintList.add(getHexConvert(temp));
					mBrush.add(Float.parseFloat(arrz.get(j).toString()+"f"));
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("ifeelbluu","onAnnotLoad == "+ e.getMessage().toString());
		} 
		
		float temp_wid = MemoPadSize_W;
		float temp_hgt = MemoPadSize_H;
		RectF rc = new RectF();
		rc.left = (float) start_x;
//		rc.top = (float) start_y+((float)DocMainActivity.MemoTopBarSize/2);
		rc.top = (float) start_y;
		rc.right = (float) (rc.left+temp_wid);
		rc.bottom = (float) (rc.top+temp_hgt);
		//scaleWidhtttttt onAnnod 패스를 다시 그린다
		float scaleWidth = (wid-(widthMargin*2)) / (float)rc.width();// 사인뷰*스케일 / 사인뷰
		float scaleHeight = (hgt-(heightMargin*2)) / (float)rc.height();
	    
		float scale;
		if (scaleWidth < scaleHeight) {
			scale = scaleHeight;
//			rc.top = rcDispl th()-(popupSigRect.width() * scale)) / 2;
		}
		else {
			scale = scaleWidth;
			//rc.left = rcDisplay.left;
			//rc.top = rc.top+ (rc.height()-(popupSigRect.height() * scale)) / 2;
		}
		
    	
		float pointX = 0;
		float pointY = 0;
		
		Path tPath = null;
		for(int i = 0; i < pointList.size(); i++){
			
			PointF point = new PointF();
			
			int nType = mTypeList.get(i);
			PointF nPoint = pointList.get(i);
			Log.v("ifeelbluu",mPointList.size() + " / " + mTypeList.size() + " / " + mPaintList.size() + " / " + mBrush.size());
			point.set(((nPoint.x-rc.left) * scale)+widthMargin, ((nPoint.y-rc.top) * scale)+heightMargin);
			if(nType == 0){
				tPath = new Path();
				annot_touch_start(point.x, point.y, tPath);
				pointX = point.x;
				pointY = point.y;
			}
			else if(nType == 1){
				annot_touch_move(point.x, point.y, pointX, pointY, tPath);
				pointX = point.x;
				pointY = point.y;
			}
			else if(nType == 2){
				if(mPaintList.size() != 0){
					setColorPaint(mPaintList.get(i));
					setBrushSize(mBrush.get(i));
				}
				annot_touch_up(pointX, pointY, tPath);
			}
			mPointList.add(point);
		}
		
		pointList.clear();
		pointList = null;
		//mAnnotInfo.mArrTouchType.clear();
		//mAnnotInfo.mArrTouchType = null;
		invalidate();
	}

	private int findRes(String name, String type) {
		return getResources().getIdentifier(name, type,
				mContext.getPackageName());
	}
	
	public void setBaseSize(int width, int height){
		maxWidthSize = width;
		maxHeightSize = height;
	}
	public void init(){
		invalidate();
	}

	public void setAnnotInfo(String _contentId,String Points,float _org_wid,float _org_hgt,float _start_x,float _start_y, String Paints){
		
		this.contentId = _contentId;
		this.points = Points;
		this.paints = Paints;
		this.org_wid = _org_wid;
		this.org_hgt = _org_hgt;
		this.start_x = _start_x;
		this.start_y = _start_y;
		
		if(maxWidthSize == 0 || maxHeightSize == 0){
			DisplayMetrics displayMetrics = new DisplayMetrics();
			((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
			Resources resources = getResources();
		    DisplayMetrics metrics = resources.getDisplayMetrics();
		    float px = 150 * (metrics.densityDpi / 160f);
			maxWidthSize = displayMetrics.widthPixels-(int)px;
			maxHeightSize = displayMetrics.heightPixels-(int)px;
		}
		
		int deviceWidth = maxWidthSize;
		int deviceHeight = maxHeightSize;
		
		//scaleWidhtttttt setAnnot
		float scaleWidth = (float)deviceWidth / org_wid;// 디바이스가로 / 사인뷰가로
		float scaleHeight = (float)deviceHeight / org_hgt;
		
		float scale;
		if (scaleWidth > scaleHeight) {
			scale = scaleHeight;
		}
		else {
			scale = scaleWidth;
		}
		
		wid = (int)((org_wid * scale) + (widthMargin*2));
		hgt = (int)((org_hgt * scale) + (heightMargin*2));
		if(hgt > deviceHeight){
			heightMargin = (int)((hgt - deviceHeight)/2.0f);
			hgt = deviceHeight;
		}
		if(wid > deviceWidth){
			widthMargin = (int)((wid - deviceWidth)/2.0f);
			wid = deviceWidth;
		}
		Log.v("ifeelbluu","wid = " + wid + " / hgt = " + hgt + " / scale ==== " + scale );
		Log.v("ifeelbluu","heightMargin = " + heightMargin + " / widthMargin = " + widthMargin);
		measure(wid, hgt);
		onAnnotLoad();
	}
	public String getContentId()
	{
		return contentId;
	}
	
	public String getPaintColors(){
		String rst = new String();
		for(int i = 0; i<mPaintList.size(); i++){
			rst += mPaintList.get(i);
			
			if( i != mPaintList.size()-1){
				rst += ",";
			}
		}
		return rst;
	}
	
	public String getPoints()
	{
		String result = "[";
		String x = "";
		String y = "";
		String z = "";
		
    	RectF popupSigRect = new RectF(0, 0, wid-(widthMargin*2), (hgt)-(heightMargin*2));
    	float temp_wid = MemoPadSize_W;
		float temp_hgt = MemoPadSize_H;
    	RectF rc = new RectF();
		rc.left = (float) start_x;
		rc.top = (float) start_y;
		rc.right = (float) (rc.left+temp_wid);
		rc.bottom = (float) (rc.top+temp_hgt);
    	//scaleWidhtttttt getpoint 사인패스를 가져와서 스크립트에 저장 
		float scaleWidth = rc.width() / (float)popupSigRect.width(); //사인뷰 가로/사인뷰*스케일
		float scaleHeight = rc.height() / (float)popupSigRect.height();
		
		float scale;
		if (scaleWidth > scaleHeight) {
			scale = scaleHeight;
		}
		else {
			scale = scaleWidth;
		}
		
		for(int i = 0; i < mPointList.size(); i++){

			PointF nPoint = mPointList.get(i);
			int nType = mTypeList.get(i);
			float brush = mBrush.get(i);
			//start
			if(nType == 0){
				//Log.d("start",nPoint.x+" "+nPoint.y);
				String comma = "";
				if(!result.equals("["))
				{
					comma = ",";
				}else comma = "";
				x = comma + "{\"x\":["+(((nPoint.x-widthMargin) * scale)+rc.left);
				y = "\"y\":["+(((nPoint.y-heightMargin) * scale)+rc.top);
				z = "\"z\":["+brush;
			}//move
			else if(nType == 1){
				//Log.d("move",nPoint.x+" "+nPoint.y);
				x += ","+(((nPoint.x-widthMargin) * scale)+rc.left);
				y += ","+(((nPoint.y-heightMargin) * scale)+rc.top);
				z += ","+brush;
			}//up
			else if(nType == 2){
				//Log.d("up",nPoint.x+" "+nPoint.y);
				//Log.d("up","---------------------------------------------");
				x += ","+(((nPoint.x-widthMargin) * scale)+rc.left) +"]";
				y += ","+(((nPoint.y-heightMargin) * scale)+rc.top) +"]";
				z += ","+brush+"]";
				String getColor = getRGBColor(mPaintList.get(i));
				result += x+","+y+","+z+","+"\"color\":\""+getColor+"\"}";
			}
		}
		result += "]";
		Log.d("result",result);
		return result;
		
	}
	
	

    public boolean isSignature(){
    	return misSig;
    }
    
    public boolean mSignEnable = true;
    public void setSignEnable(boolean type){
    	mSignEnable = type;
    }
    private VelocityTracker mVelocityTracker;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mSignEnable == false){
        	return false;
        }
    	
    	if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);


        float x = event.getX();
        float y = event.getY();
        
        switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN:
	            touch_start(x, y);
	            invalidate();
	            break;
	        case MotionEvent.ACTION_MOVE:
	            touch_move(x, y);
	            invalidate();
	            break;
	        case MotionEvent.ACTION_UP:
	            touch_up();
	            invalidate();
	            if (mVelocityTracker != null) {
	                mVelocityTracker.recycle();
	                mVelocityTracker = null;
	            }
	            break;
        }
        return true;
    }
    
 // PATH 초기화 
 	public void resetPath()
 	{
 		float nPointX = 0;
 		float nPointY = 0;
 		Path mPath = null;
 		
 		for(int i=0;i<mPointList.size();i++)
 		{
 			PointF point = new PointF();
 			PointF ptAnnot = mPointList.get(i);
 			int annotType = mTypeList.get(i);
// 			point.set(((ptAnnot.x-rc.left) * scale)+widthMargin, ((ptAnnot.y-rc.top) * scale)+heightMargin);
 			point.set(ptAnnot.x, ptAnnot.y);
 			
 			switch(annotType)
 			{
 			case 0:
 				mPath = new Path();
 				annot_touch_start(point.x, point.y, mPath);
 				nPointX = point.x;
 				nPointY = point.y;
 				break;
 			case 1:		
 				annot_touch_move(point.x, point.y, nPointX, nPointY, mPath);
 				nPointX = ptAnnot.x;
 				nPointY = ptAnnot.y;
 				break;
 			case 2:
 				if(mPaintList.size() != 0){
					setColorPaint(mPaintList.get(i));
				}
 				annot_touch_up(nPointX, nPointY, mPath);
 				break;
 			}
 		}
 	}
 	
 	private ArrayList<PointF> mArrFindPoint=null;
 	public int removeAtIntersect(ArrayList<PointF> arrCancel)
 	{
 		mArrFindPoint=new ArrayList<PointF>();
 		
 		boolean bWork = false;
 		int nPrevType = 2;
 		int nCount = mPointList.size();
 		//Rect rcCancel = GetAnnotRoundRC(arrCancel);
 		
 		ArrayList<PointF> arrSign = new ArrayList<PointF>();
 		
 		if (arrCancel.size() < 3)
 			return nCount;
 		
 		mArrFindPoint.clear();

 		arrSign.clear();
 		for(int i=1;i<mPointList.size();i++)
 		{
 			PointF ptAnnot = mPointList.get(i);
 			int   annotType = mTypeList.get(i);
 			
 			// 서명 라인 처
 			switch(annotType)
 			{
 			case 0:
 				bWork=false;
 				if(nPrevType!=2)
 					bWork=true;
 				break;
 			case 1:
 				bWork=false;
 				break;
 			case 2:
 				bWork=true;
 				break;
 			}
 			nPrevType=annotType;
 						
 			// 포인터 추가
 			arrSign.add(ptAnnot);
 			
 			// 교차점 찾기 
 			if (bWork)
 			{
 				//Rect rcAnnot = GetAnnotRoundRC(arrSign);
 				int nDupCnt=0;
 				if (arrSign.size() > 0)
 				{

 					for(int k=0;k<arrCancel.size()-2;k++)
 					{
 						PointF pt3 = arrCancel.get(k);
 						PointF pt4 = arrCancel.get(k+2);
 						
 						for(int j=0;j<arrSign.size()-2;j++)
 						{
 							PointF pt1 = arrSign.get(j);
 							PointF pt2 = arrSign.get(j+2);
 							// 교차점을 찾는 함수 호출 
 							int nCnt = getLineIntersection(pt1.x, pt1.y, pt2.x, pt2.y, pt3.x, pt3.y, pt4.x, pt4.y);
 							
 							
 							if (nCnt>0)
 							{
 								Log.v("MAPAnnotInfo", "findIntersection");
 								nDupCnt++;
 							}
 						}
 					}
 						
 				}
 				if (nDupCnt > 0)
 					mArrFindPoint.add(arrSign.get(0));

 				arrSign.clear();
 			}
 		}
 		
 		// 삭제처리 
 		for(int d=0;d<mArrFindPoint.size();d++)
 		{
 			boolean bResult = removeAtPoint(mArrFindPoint.get(d));
 			if (bResult==true)
 				Log.d("MAPAnnotInfo", "removeAtIntersect delete annotpoint  ");
 		}

 		return mPointList.size();
 	}
 	
 	public boolean removeAtPoint(PointF ptKey)
 	{
 		boolean bFind = false;
 		int nStart = -1;
 		int nEnd = -1;
 		
 		// 동일한 좌표가 존재하는지 검색 
 		for(int i=0;i<mPointList.size();i++)
 		{
 			PointF ptAnnot = mPointList.get(i);
 			int   annotType = mTypeList.get(i);
 			
 			
 			if (ptAnnot.x == ptKey.x && ptAnnot.y == ptKey.y)
 				bFind = true;

 			if (annotType == 0)
 			{
 				nStart = i;
 			}
 			else if (annotType == 2)
 			{
 				nEnd = i;
 				if (bFind == true)
 					break;
 			}
 			
 			if(bFind == true){
 	 			Log.v("MAPAnnotInfo","["+i+"]ptAnnot.x = " + ptAnnot.x );
 	 			Log.v("MAPAnnotInfo","["+i+"]ptAnnot.y = " + ptAnnot.y );
 	 			Log.v("MAPAnnotInfo","["+i+"]ptKey.x = " + ptKey.x );
 	 			Log.v("MAPAnnotInfo","["+i+"]ptKey.y = " + ptKey.y );
 	 			}
 		}
 		
 		// 찾은 경우 해당 좌표 영역 삭제
 		if (bFind == true && nStart < nEnd)
 		{
 			Log.d("MAPAnnotInfo", "delete annotpoint  [" + nStart + "-"+nEnd + "] ==>" + mPointList.size());
 			
 			for(int j=nEnd;j>=nStart;j--)
 			{
 				mPointList.remove(j);
 				mTypeList.remove(j);
 				mPaintList.remove(j);
 				mBrush.remove(j);
 			}
 		}
 		
 		return bFind;
 	}
 	
 	public int getLineIntersection(float p0_x, float p0_y, float p1_x, float p1_y, 
 		    float p2_x, float p2_y, float p3_x, float p3_y)
 	{
 	    float s1_x, s1_y, s2_x, s2_y;
 	    s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
 	    s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

 	    float s, t;
 	    s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
 	    t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

 	    if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
 	    {
 	        return 1;
 	    }
 	    
 	    return 0;
 	}
 	
 	public String getRGBColor(int val){
 		int r = Color.red(val);
        int g = Color.green(val);
        int b = Color.blue(val);
        int a = Color.alpha(val);
        int RGB = Color.argb(a, r, g, b);
        String hex = Integer.toHexString(RGB & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        
        if(hex.length()<6){
        	for(int i=hex.length(); i<=6; i++){
        		hex+="0";
        	}
        }
        
        Log.v("ifeelbluu","getRGBColor ==== " + hex);
        
        return hex;
 	}
 	
 	public int getHexConvert(String hex){
 		int rst = Color.parseColor(hex);
 		return rst;
 	}
}