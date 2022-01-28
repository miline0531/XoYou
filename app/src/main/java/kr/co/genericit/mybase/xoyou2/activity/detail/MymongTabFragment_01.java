package kr.co.genericit.mybase.xoyou2.activity.detail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.graph.CustomGauge;
import kr.co.genericit.mybase.xoyou2.graph.SeekCircle;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class MymongTabFragment_01 extends Fragment {
    private View v;

    private ImageView center_circleview;
    private TextView textProgress;
    private SeekCircle mGraph1;
    private CustomGauge mGraph2;
    private PieChart mGraph3;
    private PieData data;
    private TextView mGraph2textView;
    private CircularProgressView progressView1,progressView2,progressView3,progressView4;
    private TextView progressTxt1,progressTxt2,progressTxt3,progressTxt4;
    private ArrayList<Float> mGraph3Data;

    private String mTitle = "title";
    private String mTitleType = "title";

    public static final int[] BLACK_COLORS = {
            Color.argb(70,255, 90, 90),
            Color.argb(70,242, 203, 97),
            Color.argb(70,61, 183, 204),
            Color.argb(70,165, 102, 255),
            Color.argb(70,71, 200, 62),
            Color.argb(70,255, 0, 127),
            Color.argb(70,116, 116, 116)
    };
    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    public static MymongTabFragment_01 newInstance(boolean isTooltipView) {

        MymongTabFragment_01 f = new MymongTabFragment_01();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_1, container, false);

        initView(v);
        setView(v);
//        int Graph1Value = 88;
        animProgress1(mGraph1Value);
        animProgress2(mGraph2Value);

//        int value = 67;
//        mGraph2.setValue(value);
//        mGraph2textView.setText("재물: "+value+"%");
//        setProgressValue(1,22);
//        setProgressValue(2,48);
//        setProgressValue(3,77);
//        setProgressValue(4,100);

        progressTxt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGraph3Data = new ArrayList<Float>();
                mGraph3Data.add(16.7f);
                mGraph3Data.add(14.7f);
                mGraph3Data.add(17.9f);
                mGraph3Data.add(17.3f);
                mGraph3Data.add(6.2f);
                mGraph3Data.add(27.8f);
                changeGraph3();
            }
        });

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = v.findViewById(R.id.scrollView).getHeight();
            }
        });

        return v;
    }

    private TimerTask mGraph1second;
    private int mGraph1Count = 0;
    private int mGraph1Value = 0;
    private final Handler mGraph1Handler = new Handler();
    public void animProgress1(int value) {
        mGraph1Count = 0;
        mGraph1Value = value;

        mGraph1second = new TimerTask() {
            @Override
            public void run() {
                if(mGraph1Value>=mGraph1Count){
                    graph1Update();
                }else{
                    mGraph1second.cancel();
                }
                mGraph1Count++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(mGraph1second, 0, 10);
    }
    protected void graph1Update() {
        Runnable updater = new Runnable() {
            public void run() {
                mGraph1.setProgress(mGraph1Count);
            }
        };
        mGraph1Handler.post(updater);
    }

    private TimerTask mGraph2second;
    private int mGraph2Count = 0;
    private int mGraph2Value = 67;
    private final Handler mGraph2Handler = new Handler();
    public void animProgress2(int value) {


        mGraph2Count = 0;
        mGraph2Value = value;

        mGraph2textView.setText(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_1_real) + value + "%");

        mGraph2second = new TimerTask() {
            @Override
            public void run() {
                if(mGraph2Value>=mGraph2Count){
                    graph2Update();
                }else{
                    mGraph2second.cancel();
                }
                mGraph2Count++;
            }
        };
        Timer timer = new Timer();
        timer.schedule(mGraph2second, 0, 10);
    }
    protected void graph2Update() {
        Runnable updater = new Runnable() {
            public void run() {
                mGraph2.setValue(mGraph2Count);
            }
        };
        mGraph2Handler.post(updater);
    }


    public void initView(View v){
        //mGraph1
        mGraph1 = v.findViewById(R.id.seekCircle);
        textProgress = v.findViewById(R.id.textProgress);
        center_circleview = v.findViewById(R.id.center_circleview);
        mGraph1.setEnabled(false);
        mGraph1.setOnSeekCircleChangeListener(new SeekCircle.OnSeekCircleChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekCircle seekCircle)
            {}

            @Override
            public void onStartTrackingTouch(SeekCircle seekCircle)
            {}

            @Override
            public void onProgressChanged(SeekCircle seekCircle, int progress, boolean fromUser)
            {
                updateText();
            }
        });


        //mGraph2
        mGraph2 = v.findViewById(R.id.detail_graph2);
        mGraph2textView = v.findViewById(R.id.textView2);
        mGraph2.setEndValue(100);
        mGraph2.setStartValue(0);

        //mGraph3
        mGraph3 = v.findViewById(R.id.piechart);


        //middleGraph
        progressView1 = v.findViewById(R.id.progressView1);
        progressView2 = v.findViewById(R.id.progressView2);
        progressView3 = v.findViewById(R.id.progressView3);
        progressView4 = v.findViewById(R.id.progressView4);
        progressTxt1 = v.findViewById(R.id.progressTxt1);
        progressTxt2 = v.findViewById(R.id.progressTxt2);
        progressTxt3 = v.findViewById(R.id.progressTxt3);
        progressTxt4 = v.findViewById(R.id.progressTxt4);
        progressView1.setProgressTextEnabled(false);
        progressView2.setProgressTextEnabled(false);
        progressView3.setProgressTextEnabled(false);
        progressView4.setProgressTextEnabled(false);


    }



    private void setGraph3Data(){
        mGraph3Data = new ArrayList<Float>();
        Log.v("ifeelbluu","jobYG.getJSONObject(i).getString(\"Dou\")" + jobYG.length());
        try{
            for(int i = 0; i<jobYG.length(); i++){
                mGraph3Data.add(Float.parseFloat(jobYG.getJSONObject(i).getString("Dou")));

                Log.v("ifeelbluu","jobYG.getJSONObject(i).getString(\"Dou\")" + jobYG.getJSONObject(i).getString("Dou"));
            }
        }catch (Exception e){

        }

    }

    private void changeGraph3(){
        ArrayList NoOfEmp = new ArrayList();

        for(int i=0; i< mGraph3Data.size(); i++){
            NoOfEmp.add(new Entry(mGraph3Data.get(i), i));
        }

        List<PieEntry> entries = new ArrayList<>();

        try{
            for(int i = 0; i<jobYG.length(); i++){
                entries.add(new PieEntry(mGraph3Data.get(i), jobYG.getJSONObject(i).getString("Name")));
            }
        }catch (Exception e){

        }
        PieDataSet set = new PieDataSet(entries, "");

        DecimalFormat tFormat = new DecimalFormat("###.##");
        set.setValueFormatter(new PercentFormatter(tFormat));
        set.setValueTextSize(16f);
        set.setValueTextColor(Color.WHITE);/* this line not working */
        set.setSelectionShift(0f);
        set.setSliceSpace(0f); //아이템 간격
        set.setColors(BLACK_COLORS);


        data = new PieData(set); // MPAndroidChart v3.X 오류 발생
        mGraph3.setData(data);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gowunbatang_regular.ttf");
        mGraph3.setEntryLabelTypeface(typeFace);

        set.setColors(BLACK_COLORS);
        mGraph3.setEntryLabelTextSize(16f);
        mGraph3.animateXY(1000, 1000);
        mGraph3.setRotationEnabled(false);
        mGraph3.setEnabled(false);
        mGraph3.setContextClickable(false);
        mGraph3.setDrawHoleEnabled(true);
//        pieChart.setBackgroundColor(R.color.transparent);

        mGraph3.setHoleColor(Color.argb(0,0, 0, 0));
        mGraph3.setHoleRadius(77);

        mGraph3.getLegend().setEnabled(false);
        mGraph3.getDescription().setEnabled(false);

        mGraph3.notifyDataSetChanged();
        mGraph3.invalidate();
    }

//    public void setProgressValue(int index, int progress){
//        switch (index){
//            case 1:
//                progressView1.setProgress(progress,true);
//                progressTxt1.setText("근본\n"+progress+"%");
//                break;
//            case 2:
//                progressView2.setProgress(progress,true);
//                progressTxt2.setText("귀인\n"+progress+"%");
//                break;
//            case 3:
//                progressView3.setProgress(progress,true);
//                progressTxt3.setText("환경\n"+progress+"%");
//                break;
//            case 4:
//                progressView4.setProgress(progress,true);
//                progressTxt4.setText("활동\n"+progress+"%");
//                break;
//        }
//    }

    private void updateText()
    {
        if (textProgress != null && mGraph1 != null)
        {
            int progress = mGraph1.getProgress();
            textProgress.setText(mTitleType+"\n"+Integer.toString(progress) + "%");
        }
    }
    JSONArray jobYG = null;
    private void setView(View v){
        String Hae = "";
        String Hyeon = "";
        String HyeonSil = "";
        String MongDou = "";
        float HyeonSilValue = 0f;
        float MongDouValue = 0f;

        try {
            Hae = Constants.StoryJob.getString("Hae");
            Hyeon = Constants.StoryJob.getString("Hyeon");
            MongDou = Constants.StoryJob.getString("MongDou"); //그래프 1
            HyeonSil = Constants.StoryJob.getString("HyeonSil"); //그래프 2
            mTitle = Constants.StoryJob.getString("GoodBadMong"); //그래프 title
            mTitleType = Constants.StoryJob.getString("Name"); //Name
            MongDouValue = Float.parseFloat(MongDou);
            HyeonSilValue = Float.parseFloat(HyeonSil);
            jobYG = Constants.StoryJob.getJSONArray("MongYeonGwanList"); //그래프 3

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView)v.findViewById(R.id.txt_hae)).setText(Hae);
        ((TextView)v.findViewById(R.id.txt_hyeon)).setText(Hyeon);

        //현실율
        mGraph2Value = (int)(HyeonSilValue*100);
        mGraph1Value = (int)(MongDouValue*100);

        setGraph3Data();
        changeGraph3();

        RelativeLayout root_bg = v.findViewById(R.id.root_bg);

//        mTitle = "흉몽";
//        mTitle = "악몽";
//        mTitle = "길몽";
//        mTitle = "영몽";
        String goodbad = "good";

            if (mGraph1Value >= 100)
            {
                mGraph1.setProgressColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color_g1));
            }
            else if (mGraph1Value > 66)
            {
                mGraph1.setProgressColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color_g2));
            }
            else if (mGraph1Value > 33)
            {
                mGraph1.setProgressColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color_g3));
            }
            else
            {
                mGraph1.setProgressColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color_g4));
            }

        if(goodbad.equals("good")){
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_good);
            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.detail_menu1_graph2_good));
            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.detail_menu1_graph2_good));
        }else{
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_bad);
            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.detail_menu1_graph2_bad));
            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.detail_menu1_graph2_bad));
        }

        if (mGraph1Value > 80)
        {
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_1);
//            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color1));
//            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color1));
        }
        else if (mGraph1Value > 60)
        {
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_2);
//            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color2));
//            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color2));
        }
        else if (mGraph1Value > 40)
        {
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_3);
//            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color3));
//            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color3));
        }
        else if (mGraph1Value > 20)
        {
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_4);
//            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color4));
//            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color4));
        }
        else
        {
            center_circleview.setBackgroundResource(R.drawable.mong_circle_color_5);
//            mGraph2.setPointStartColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color5));
//            mGraph2.setPointEndColor(ContextCompat.getColor(getActivity(), R.color.mong_dou_color5));
        }


    }

}
