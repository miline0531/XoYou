package kr.co.genericit.mybase.xoyou2.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.databinding.ActivitySignview2Binding;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class SignView2Activity extends CommonActivity {
    private ActivitySignview2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySignview2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);


        initView();

        binding.btnMymongSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String point = binding.signView.getPoints();

                if(point.equals("[]")){
                    //서명X
                    CommandUtil.getInstance().showCommonOneButtonDialog(SignView2Activity.this,CommandUtil.getInstance().getStr(R.string.mong_sign_essential),CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
                }else{
                    Log.v("ifeelbluu","getPoints :: " + point);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.signView.setClear();
            }
        });
    }

    public void initView() {
//        WindowManager twindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display = twindowManager.getDefaultDisplay();
//
//        int pxWidth = display.getWidth();
//        int pxHeight = display.getHeight();
//        Log.v("ifeelbluu", "setBaseSize::pxWidth : " + pxWidth);
//        Log.v("ifeelbluu", "setBaseSize::pxHeight : " + pxHeight);
//        binding.signView.setBaseSize(pxWidth, pxHeight);
//        binding.underLineSignView.setBrushSize(50f);
//        binding.underLineSignView.setColor(Color.BLACK);

        binding.signView.setAlpha(1.0f);
        binding.signView.setColor(Color.BLACK);
        binding.signView.setClear();
        binding.signView.setBrushSize(2f);
        int overmargin = 0;
        binding.signView.setOverMargin(0, overmargin);
        int wmargin = 160;
        int hmargin = 160;
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float wpx = 0;
        float hpx = 0;
        wpx = wmargin * (metrics.densityDpi / 160f);
        hpx = hmargin * (metrics.densityDpi / 160f);

        try {
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        } catch (Exception e) {
            // TODO: handle exception
        }
        int pxWidth = metrics.widthPixels;
        int pxHeight = metrics.heightPixels;

        WindowManager twindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = twindowManager.getDefaultDisplay();

        pxWidth = display.getWidth();
        pxHeight = display.getHeight();

        Log.v("SizeTestInfo","pxWidth === " + pxWidth);
        Log.v("SizeTestInfo","pxHeight === " + pxHeight);

        int width = pxWidth;
//        int height = (int)((pxWidth*MemoPadSize_H)/MemoPadSize_W);
        int height = (int)(pxHeight);
        Log.v("SizeTestInfo","replaceW === " + width);
        Log.v("SizeTestInfo","replaceH === " + height);
        binding.signView.setBaseSize(width, height);
        binding.signView.setAnnotInfo("Memo", "", width, height, 0, 0, "");
        binding.signView.setVisibility(View.VISIBLE);
    }

    float mStartX = 0f;
    float mStartY = 0f;
    private Path signPath = new Path();
    private ArrayList<Integer> mTypeList = new ArrayList<Integer>();
    public ArrayList<PointF> mPointList = new ArrayList<PointF>();

    private void actionDown(Float x ,Float y) {
        mPointList.add(new PointF(x, y));
        mTypeList.add(0);
    }

    private void actionMove(Float x ,Float y) {
        mPointList.add(new PointF(x, y));
        mTypeList.add(1);
    }

    private void actionUp(Float x ,Float y) {
        mPointList.add(new PointF(x, y));
        mTypeList.add(2);
    }

    private void pathString(){
        String result = "[";
        String x = "";
        String y = "";
        for(int i = 0; i < mPointList.size(); i++){

            PointF nPoint = mPointList.get(i);
            int nType = mTypeList.get(i);
            //start
            if(nType == 0){
                //Log.d("start",nPoint.x+" "+nPoint.y);
                String comma = "";
                if(!result.equals("["))
                {
                    comma = ",";
                }else comma = "";
                x = comma + "{\"x\":["+((nPoint.x));
                y = "\"y\":["+((nPoint.y));
            }//move
            else if(nType == 1){
                //Log.d("move",nPoint.x+" "+nPoint.y);
                x += ","+((nPoint.x));
                y += ","+((nPoint.y));
            }//up
            else if(nType == 2){
                x += ","+((nPoint.x)) +"]";
                y += ","+((nPoint.y)) +"]";
                result += x+","+y+"}";
            }
        }
        result += "]";

        Log.d("result",result);
    }
}