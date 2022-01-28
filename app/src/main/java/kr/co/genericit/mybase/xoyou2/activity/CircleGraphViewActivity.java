package kr.co.genericit.mybase.xoyou2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vaibhavlakhera.circularprogressview.CircularProgressView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.graph.CustomGauge;
import kr.co.genericit.mybase.xoyou2.graph.SeekCircle;


public class CircleGraphViewActivity extends FragmentActivity implements OnMapReadyCallback {
    private TextView textProgress;
    private SeekCircle mGraph1;
    private CustomGauge mGraph2;
    private PieChart mGraph3;
    private PieData data;
    private TextView mGraph2textView;
    private CircularProgressView progressView1,progressView2,progressView3,progressView4;
    private TextView progressTxt1,progressTxt2,progressTxt3,progressTxt4;
    private ArrayList<Float> mGraph3Data;

    private String mTitle = "";

    public static final int[] BLACK_COLORS = {
            Color.argb(70,255, 90, 90),
            Color.argb(70,242, 203, 97),
            Color.argb(70,61, 183, 204),
            Color.argb(70,165, 102, 255),
            Color.argb(70,71, 200, 62),
            Color.argb(70,255, 0, 127),
            Color.argb(70,116, 116, 116)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_graph);
//        getSupportActionBar().hide();

        initView();
        int Graph1Value = 88;
        animProgress1(Graph1Value);
        int Graph2Value = 68;
        animProgress2(Graph2Value);

//        int value = 67;
//        mGraph2.setValue(value);
//        mGraph2textView.setText("재물: "+value+"%");
        setProgressValue(1,22);
        setProgressValue(2,48);
        setProgressValue(3,77);
        setProgressValue(4,100);

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


//        googleMapInit();

//        Intent i = new Intent(this, LoginActivity.class);
//        startActivity(i);
    }

    private TimerTask mGraph1second;
    private int mGraph1Count = 0;
    private int mGraph1Value = 0;
    private final Handler mGraph1Handler = new Handler();
    public void animProgress1(int value) {
        if(value < 35){
            mGraph1.setProgressColor(ContextCompat.getColor(this, R.color.progress_level1));
        }else if(value > 34 && value < 70){
            mGraph1.setProgressColor(ContextCompat.getColor(this, R.color.progress_level2));
        }else{
            mGraph1.setProgressColor(ContextCompat.getColor(this, R.color.progress_level3));
        }
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
        if(value < 35){
            mGraph2.setPointStartColor(ContextCompat.getColor(this, R.color.progress_level1));
            mGraph2.setPointEndColor(ContextCompat.getColor(this, R.color.progress_level1));
        }else if(value > 34 && value < 70){
            mGraph2.setPointStartColor(ContextCompat.getColor(this, R.color.progress_level2));
            mGraph2.setPointEndColor(ContextCompat.getColor(this, R.color.progress_level2));
        }else{
            mGraph2.setPointStartColor(ContextCompat.getColor(this, R.color.progress_level3));
            mGraph2.setPointEndColor(ContextCompat.getColor(this, R.color.progress_level3));
        }

        mGraph2Count = 0;
        mGraph2Value = value;
        mGraph2textView.setText("재물: "+value+"%");

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


    public void initView(){
        //mGraph1
        mGraph1 = findViewById(R.id.seekCircle);
        textProgress = findViewById(R.id.textProgress);
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
        mGraph2 = findViewById(R.id.detail_graph2);
        mGraph2textView = findViewById(R.id.textView2);
        mGraph2.setEndValue(100);
        mGraph2.setStartValue(0);

        //mGraph3
        mGraph3 = findViewById(R.id.piechart);
        setGraph3Data();
        changeGraph3();

        //middleGraph
        progressView1 = findViewById(R.id.progressView1);
        progressView2 = findViewById(R.id.progressView2);
        progressView3 = findViewById(R.id.progressView3);
        progressView4 = findViewById(R.id.progressView4);
        progressView4 = findViewById(R.id.progressView4);
        progressTxt1 = findViewById(R.id.progressTxt1);
        progressTxt2 = findViewById(R.id.progressTxt2);
        progressTxt3 = findViewById(R.id.progressTxt3);
        progressTxt4 = findViewById(R.id.progressTxt4);
        progressView1.setProgressTextEnabled(false);
        progressView2.setProgressTextEnabled(false);
        progressView3.setProgressTextEnabled(false);
        progressView4.setProgressTextEnabled(false);

    }



    private void setGraph3Data(){
        mGraph3Data = new ArrayList<Float>();
        mGraph3Data.add(16.7f);
        mGraph3Data.add(14.7f);
        mGraph3Data.add(17.9f);
        mGraph3Data.add(17.3f);
        mGraph3Data.add(26.2f);
        mGraph3Data.add(7.8f);
    }

    private void changeGraph3(){
        ArrayList NoOfEmp = new ArrayList();
        for(int i=0; i< mGraph3Data.size(); i++){
            NoOfEmp.add(new Entry(mGraph3Data.get(i), i));
        }

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(mGraph3Data.get(0), "조상"));
        entries.add(new PieEntry(mGraph3Data.get(1), "자식"));
        entries.add(new PieEntry(mGraph3Data.get(2), "사업"));
        entries.add(new PieEntry(mGraph3Data.get(3), "건강"));
        entries.add(new PieEntry(mGraph3Data.get(4), "학업"));
        entries.add(new PieEntry(mGraph3Data.get(5), "재능"));
        PieDataSet set = new PieDataSet(entries, "");

        DecimalFormat tFormat = new DecimalFormat("###.##");
        set.setValueFormatter(new PercentFormatter(tFormat));
        set.setValueTextSize(12f);
        set.setValueTextColor(Color.WHITE);/* this line not working */
        set.setSelectionShift(0f);
        set.setSliceSpace(0f); //아이템 간격
        set.setColors(BLACK_COLORS);

        data = new PieData(set); // MPAndroidChart v3.X 오류 발생
        mGraph3.setData(data);

        set.setColors(BLACK_COLORS);
        mGraph3.animateXY(1000, 1000);
        mGraph3.setRotationEnabled(false);
        mGraph3.setEnabled(false);
        mGraph3.setContextClickable(false);
        mGraph3.setDrawHoleEnabled(true);
//        pieChart.setBackgroundColor(R.color.transparent);

        mGraph3.setHoleColor(Color.argb(0,0, 0, 0));
        mGraph3.setHoleRadius(70);

        mGraph3.getLegend().setEnabled(false);
        mGraph3.getDescription().setEnabled(false);

        mGraph3.notifyDataSetChanged();
        mGraph3.invalidate();
    }

    public void setProgressValue(int index, int progress){
        switch (index){
            case 1:
                progressView1.setProgress(progress,true);
                progressTxt1.setText("근본\n"+progress+"%");
                break;
            case 2:
                progressView2.setProgress(progress,true);
                progressTxt2.setText("귀인\n"+progress+"%");
                break;
            case 3:
                progressView3.setProgress(progress,true);
                progressTxt3.setText("환경\n"+progress+"%");
                break;
            case 4:
                progressView4.setProgress(progress,true);
                progressTxt4.setText("활동\n"+progress+"%");
                break;
        }
    }

    private void updateText()
    {
        if (textProgress != null && mGraph1 != null)
        {
            int progress = mGraph1.getProgress();
            textProgress.setText(mTitle+"\n"+Integer.toString(progress) + "%");
        }
    }


    private GoogleMap mMap;

    private void googleMapInit(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}