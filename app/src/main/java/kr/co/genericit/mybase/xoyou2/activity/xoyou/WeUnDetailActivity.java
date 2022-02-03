package kr.co.genericit.mybase.xoyou2.activity.xoyou;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MymongBidActivity;
import kr.co.genericit.mybase.xoyou2.adapter.MainFrag2ListAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.common.NetInfo;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.WeListObj;
import kr.co.genericit.mybase.xoyou2.model.WeYouUnDataListObj;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;

public class WeUnDetailActivity extends AppCompatActivity {
    private WeYouUnDataListObj obj;
    public Context mContext;
    CommonUtil dataSet = CommonUtil.getInstance();

    //SKY
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();
    private MainFrag2ListAdapter m_Adapter;

    //그래프
    private JSONArray tab1_my = null; //1라인
    private JSONArray tab1_yu = null; //1바
    private JSONArray tab2_my = null; //2라인
    private JSONArray tab2_yu = null; //2바
    private JSONArray tab3_my = null; //3라인
    private JSONArray tab3_yu = null; //3바
    private JSONArray tab4_my = null; //4라인
    private JSONArray tab4_yu = null; //4바
    private JSONArray tab5_my = null; //4라인
    private JSONArray tab5_yu = null; //4바
    private TextView txt_body;

    //1번째 그래프
    private ArrayList<BarEntry> list = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private LineData linelist = null;
    private LineData resultlinelist = null;
    private CombinedChart barchart;

    private RadarChart chart_radar2;
    private RadarChart chart_radar3;
    private RadarChart chart_radar4;
    private RadarChart chart_radar5;

    //2번째 그래프
    private ArrayList<String> graphLabel;
    private ArrayList<Float> mGraphData1;
    private String[] labels_2;

    //3번째 그래프
    private ArrayList<String> graphLabel3;
    private ArrayList<Float> mGraphData3;
    private String[] labels_3;

    //4번째 그래프
    private ArrayList<String> graphLabel4;
    private ArrayList<Float> mGraphData4;
    private String[] labels_4;

    //5번째 그래프
    private ArrayList<String> graphLabel5;
    private ArrayList<Float> mGraphData5;
    private String[] labels_5;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weundetail);
        getSupportActionBar().hide();

        txt_body = findViewById(R.id.txt_body);
        barchart = findViewById(R.id.barchart);
        chart_radar2 = findViewById(R.id.chart_radar2);
        chart_radar3 = findViewById(R.id.chart_radar3);
        chart_radar4 = findViewById(R.id.chart_radar4);
        chart_radar5 = findViewById(R.id.chart_radar5);


        obj = getIntent().getParcelableExtra("obj");
        mContext = this;

        getUnDataDetail();

        findViewById(R.id.common_left_btn).setOnClickListener(btnListener);

    }
    private void getUnDataDetail(){
        CommandUtil.getInstance().showLoadingDialog(WeUnDetailActivity.this);
        map.clear();
        map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_WE_UN_DETAIL);
        map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        map.put("date", dataSet.FullPatternDate("yyyyMMddHHmmss"));
        map.put("yuSeq", obj.getNo());

        //스레드 생성
        mThread = new AccumThread(WeUnDetailActivity.this, mAfterAccum, map, 5, 0, null);
        mThread.start();        //스레드 시작!!
    }


    Handler mAfterAccum = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CommandUtil.getInstance().dismissLoadingDialog();
            if(msg.arg1 == 0) {
                String res  = (String)msg.obj;
                SkyLog.d("res 0: " + res);
                try {
                    JSONObject jsonObject_succes = new JSONObject(res);                     //SUCCESS
                    if(jsonObject_succes.getString("success").equals("true")){
                        JSONObject jsonObject_data = new JSONObject(jsonObject_succes.getString("data"));

                        tab1_my = new JSONArray(jsonObject_data.getString("tab1_my"));
                        tab1_yu = new JSONArray(jsonObject_data.getString("tab1_yu"));
                        tab2_my = new JSONArray(jsonObject_data.getString("tab2_my"));
                        tab2_yu = new JSONArray(jsonObject_data.getString("tab2_yu"));
                        tab3_my = new JSONArray(jsonObject_data.getString("tab3_my"));
                        tab3_yu = new JSONArray(jsonObject_data.getString("tab3_yu"));
                        tab4_my = new JSONArray(jsonObject_data.getString("tab4_my"));
                        tab4_yu = new JSONArray(jsonObject_data.getString("tab4_yu"));
                        tab5_my = new JSONArray(jsonObject_data.getString("tab5_my"));
                        tab5_yu = new JSONArray(jsonObject_data.getString("tab5_yu"));


                        txt_body.setText(jsonObject_data.getString("feel"));
                        //그래프 1
                        initTab1();

                        //그래프 2
                        initTab2();

                        //그래프 3
                        initTab3();

                        //그래프 4
                        initTab4();

                        //그래프 5
                        initTab5();

                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDialog(mContext,
                                jsonObject_succes.getString("error") + getClass().toString(),
                                mContext.getResources().getString(R.string.str_cofirm),
                                CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,
                                null);
                    }
                }catch (Exception e){
                    SkyLog.d("e :: " + e);
                }
            }
        }
    };

    //1번째 그래프....
    private void initTab1(){

        try {
            list = new ArrayList<>();
            labels = new ArrayList<>();
            ArrayList<Entry> entries = new ArrayList<Entry>();
            for(int i=0; i<tab1_my.length(); i++){
                JSONObject jsonObjectMy = tab1_my.getJSONObject(i);
                JSONObject jsonObjectYu = tab1_yu.getJSONObject(i);
                SkyLog.d("tab1_my :: " + jsonObjectMy.getString("Name"));
                list.add(new BarEntry(i, Float.valueOf(jsonObjectMy.getString("iDou"))));
                labels.add(jsonObjectMy.getString("Name"));

                entries.add(new Entry(i, Float.parseFloat(jsonObjectYu.getString("iDou"))));
            }
            LineDataSet lineDataSet = new LineDataSet(entries, "Line");
            linelist = new LineData(lineDataSet);
            LineDataSet set = new LineDataSet(entries, "Line DataSet1");
            set.setColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.btn_ok_color));
            set.setLineWidth(1.5f);
            set.setCircleColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.btn_ok_color));
            set.setCircleSize(3f);
            set.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.btn_ok_color));
            //        set.setDrawCubic(true);
            set.setDrawValues(false);
            set.setValueTextSize(10f);
            set.setValueTextColor(Color.BLACK);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            //        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            linelist.addDataSet(set);

            //(CombinedChart chart, LineData linedata, ArrayList<BarEntry> list, String[] labels, String category)
            setCombindCart(barchart,
                    list,
                    labels,
                    CommandUtil.getInstance().getStr(R.string.mong_bid_cartegory)
            );
        }catch (Exception e){
            SkyLog.d("e :: " + e);
        }

    }
    //2번째 그래프....
    private void initTab2(){
        try {
            graphLabel = new ArrayList<>();
            mGraphData1 = new ArrayList<>();


            for(int i = 0; i<tab2_my.length(); i++){
                JSONObject graphDataItem = tab2_my.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel.add(Name);
                }
                mGraphData1.add(Float.parseFloat(iDou));
            }

            for(int i = 0; i<tab2_yu.length(); i++){
                JSONObject graphDataItem = tab2_yu.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel.add(Name);
                }
                mGraphData1.add(Float.parseFloat(iDou));
            }


            ArrayList<RadarEntry> rValue1 = new ArrayList<>();
            ArrayList<RadarEntry> rValue2 = new ArrayList<>();
            labels_2 = new String[tab2_my.length()];



            for(int i=0; i<tab2_my.length(); i++){
                JSONObject jsonObjectMy = tab2_my.getJSONObject(i);
                JSONObject jsonObjectYu = tab2_yu.getJSONObject(i);

                rValue1.add(new RadarEntry(Float.valueOf(jsonObjectMy.getString("iDou"))));
                rValue2.add(new RadarEntry(Float.valueOf(jsonObjectYu.getString("iDou"))));
                labels_2[i] = jsonObjectYu.getString("Name");

            }

            RadarDataSet rset1 = new RadarDataSet(rValue1,"rdata1");
            rset1.setColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            rset1.setDrawValues(false);
            rset1.setDrawFilled(true);
            rset1.setLineWidth(0f);
            rset1.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata2");
            rset2.setDrawValues(false);
            rset2.setLineWidth(2f);
            rset2.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));

            RadarData rdata = new RadarData();
            rdata.addDataSet(rset1);
            rdata.addDataSet(rset2);

            chart_radar2.setData(rdata);

            XAxis xAxis = chart_radar2.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels_2));
            xAxis.setTextColor(Color.WHITE);
            chart_radar2.getYAxis().setEnabled(false);

            Legend l = chart_radar2.getLegend();
            l.getEntries();
            l.setTextColor(Color.WHITE);
            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setYEntrySpace(14f);
            l.setXEntrySpace(14f);
            l.setTextSize(14f);
            l.setWordWrapEnabled(true);
            LegendEntry l1=new LegendEntry(graphLabel.get(0),Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            LegendEntry l2=new LegendEntry(graphLabel.get(1), Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));
            l.setCustom(new LegendEntry[]{l1,l2});
            l.setEnabled(true);

            chart_radar2.getDescription().setEnabled(false);
            chart_radar2.setTouchEnabled(false);
            chart_radar2.invalidate();
        }catch (Exception e){
            SkyLog.d("e :: " + e);
        }

    }
    //3번째 그래프....
    private void initTab3(){
        try {
            graphLabel3 = new ArrayList<>();
            mGraphData3 = new ArrayList<>();


            for(int i = 0; i<tab3_my.length(); i++){
                JSONObject graphDataItem = tab3_my.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel3.add(Name);
                }
                mGraphData3.add(Float.parseFloat(iDou));
            }

            for(int i = 0; i<tab3_yu.length(); i++){
                JSONObject graphDataItem = tab3_yu.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel3.add(Name);
                }
                mGraphData3.add(Float.parseFloat(iDou));
            }


            ArrayList<RadarEntry> rValue1 = new ArrayList<>();
            ArrayList<RadarEntry> rValue2 = new ArrayList<>();
            labels_3 = new String[tab3_my.length()];



            for(int i=0; i<tab3_my.length(); i++){
                JSONObject jsonObjectMy = tab3_my.getJSONObject(i);
                JSONObject jsonObjectYu = tab3_yu.getJSONObject(i);

                rValue1.add(new RadarEntry(Float.valueOf(jsonObjectMy.getString("iDou"))));
                rValue2.add(new RadarEntry(Float.valueOf(jsonObjectYu.getString("iDou"))));
                labels_3[i] = jsonObjectYu.getString("Name");

            }

            RadarDataSet rset1 = new RadarDataSet(rValue1,"rdata1");
            rset1.setColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            rset1.setDrawValues(false);
            rset1.setDrawFilled(true);
            rset1.setLineWidth(0f);
            rset1.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata2");
            rset2.setDrawValues(false);
            rset2.setLineWidth(2f);
            rset2.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));

            RadarData rdata = new RadarData();
            rdata.addDataSet(rset1);
            rdata.addDataSet(rset2);

            chart_radar3.setData(rdata);

            XAxis xAxis = chart_radar3.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels_2));
            xAxis.setTextColor(Color.WHITE);
            chart_radar3.getYAxis().setEnabled(false);

            Legend l = chart_radar3.getLegend();
            l.getEntries();
            l.setTextColor(Color.WHITE);
//            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setYEntrySpace(14f);
            l.setXEntrySpace(14f);
            l.setTextSize(14f);
            l.setWordWrapEnabled(true);
            LegendEntry l1=new LegendEntry(graphLabel3.get(0),Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            LegendEntry l2=new LegendEntry(graphLabel3.get(1), Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));
            l.setCustom(new LegendEntry[]{l1,l2});
            l.setEnabled(true);

            chart_radar3.getDescription().setEnabled(false);
            chart_radar3.setTouchEnabled(false);
            chart_radar3.invalidate();
        }catch (Exception e){
            SkyLog.d("e :: " + e);
        }

    }
    //4번째 그래프....
    private void initTab4(){
        try {
            graphLabel4 = new ArrayList<>();
            mGraphData4 = new ArrayList<>();


            for(int i = 0; i<tab4_my.length(); i++){
                JSONObject graphDataItem = tab4_my.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel4.add(Name);
                }
                mGraphData4.add(Float.parseFloat(iDou));
            }

            for(int i = 0; i<tab4_yu.length(); i++){
                JSONObject graphDataItem = tab4_yu.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel4.add(Name);
                }
                mGraphData4.add(Float.parseFloat(iDou));
            }


            ArrayList<RadarEntry> rValue1 = new ArrayList<>();
            ArrayList<RadarEntry> rValue2 = new ArrayList<>();
            labels_4 = new String[tab4_my.length()];



            for(int i=0; i<tab4_my.length(); i++){
                JSONObject jsonObjectMy = tab4_my.getJSONObject(i);
                JSONObject jsonObjectYu = tab4_yu.getJSONObject(i);

                rValue1.add(new RadarEntry(Float.valueOf(jsonObjectMy.getString("iDou"))));
                rValue2.add(new RadarEntry(Float.valueOf(jsonObjectYu.getString("iDou"))));
                labels_4[i] = jsonObjectYu.getString("Name");

            }

            RadarDataSet rset1 = new RadarDataSet(rValue1,"rdata41");
            rset1.setColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            rset1.setDrawValues(false);
            rset1.setDrawFilled(true);
            rset1.setLineWidth(0f);
            rset1.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata42");
            rset2.setDrawValues(false);
            rset2.setLineWidth(2f);
            rset2.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));

            RadarData rdata = new RadarData();
            rdata.addDataSet(rset1);
            rdata.addDataSet(rset2);

            chart_radar4.setData(rdata);

            XAxis xAxis = chart_radar4.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels_4));
            xAxis.setTextColor(Color.WHITE);
            chart_radar4.getYAxis().setEnabled(false);

            Legend l = chart_radar4.getLegend();
            l.getEntries();
            l.setTextColor(Color.WHITE);
//            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setYEntrySpace(14f);
            l.setXEntrySpace(14f);
            l.setTextSize(14f);
            l.setWordWrapEnabled(true);
            LegendEntry l1=new LegendEntry(graphLabel4.get(0),Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            LegendEntry l2=new LegendEntry(graphLabel4.get(1), Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));
            l.setCustom(new LegendEntry[]{l1,l2});
            l.setEnabled(true);

            chart_radar4.getDescription().setEnabled(false);
            chart_radar4.setTouchEnabled(false);
            chart_radar4.invalidate();
        }catch (Exception e){
            SkyLog.d("e :: " + e);
        }

    }
    //5번째 그래프....
    private void initTab5(){
        try {
            graphLabel5 = new ArrayList<>();
            mGraphData5 = new ArrayList<>();


            for(int i = 0; i<tab5_my.length(); i++){
                JSONObject graphDataItem = tab5_my.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel5.add(Name);
                }
                mGraphData5.add(Float.parseFloat(iDou));
            }

            for(int i = 0; i<tab5_yu.length(); i++){
                JSONObject graphDataItem = tab5_yu.getJSONObject(i);
                String iDou = graphDataItem.getString("iDou");
                String Name = graphDataItem.getString("Name");
                if(i==0){
                    graphLabel5.add(Name);
                }
                mGraphData5.add(Float.parseFloat(iDou));
            }


            ArrayList<RadarEntry> rValue1 = new ArrayList<>();
            ArrayList<RadarEntry> rValue2 = new ArrayList<>();
            labels_5 = new String[tab5_my.length()];



            for(int i=0; i<tab5_my.length(); i++){
                JSONObject jsonObjectMy = tab5_my.getJSONObject(i);
                JSONObject jsonObjectYu = tab5_yu.getJSONObject(i);

                rValue1.add(new RadarEntry(Float.valueOf(jsonObjectMy.getString("iDou"))));
                rValue2.add(new RadarEntry(Float.valueOf(jsonObjectYu.getString("iDou"))));
                labels_5[i] = jsonObjectYu.getString("Name");

            }

            RadarDataSet rset1 = new RadarDataSet(rValue1,"rdata1");
            rset1.setColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            rset1.setDrawValues(false);
            rset1.setDrawFilled(true);
            rset1.setLineWidth(0f);
            rset1.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata2");
            rset2.setDrawValues(false);
            rset2.setLineWidth(2f);
            rset2.setFillColor(ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));

            RadarData rdata = new RadarData();
            rdata.addDataSet(rset1);
            rdata.addDataSet(rset2);

            chart_radar5.setData(rdata);

            XAxis xAxis = chart_radar5.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels_5));
            xAxis.setTextColor(Color.WHITE);
            chart_radar5.getYAxis().setEnabled(false);

            Legend l = chart_radar5.getLegend();
            l.getEntries();
            l.setTextColor(Color.WHITE);
//            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setYEntrySpace(12f);
            l.setXEntrySpace(12f);
            l.setTextSize(12f);
            l.setWordWrapEnabled(true);
            LegendEntry l1=new LegendEntry(graphLabel5.get(0),Legend.LegendForm.CIRCLE,12f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level3));
            LegendEntry l2=new LegendEntry(graphLabel5.get(1), Legend.LegendForm.CIRCLE,12f,2f,null,ContextCompat.getColor(WeUnDetailActivity.this, R.color.progress_level2));
            l.setCustom(new LegendEntry[]{l1,l2});
            l.setEnabled(true);

            chart_radar5.getDescription().setEnabled(false);
            chart_radar5.setTouchEnabled(false);
            chart_radar5.invalidate();
        }catch (Exception e){
            SkyLog.d("e :: " + e);
        }

    }

    private final int[] BARCHART_COLORS = {
            rgb("#FF3636"), rgb("#A6A6A6"),
            rgb("#4374D9"), rgb("#6B9900"),
            rgb("#F2CB61"), rgb("#8041D9"),
            rgb("#3DB7CC"), rgb("#D9418C")
    };

    private int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.argb(90,r, g, b);
    }

    private void setCombindCart(CombinedChart chart, ArrayList<BarEntry> list, ArrayList<String> labels, String category){
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);

        //샘플데이터
        BarDataSet barDataSet = new BarDataSet(list, "TEST");
        barDataSet.setColors(BARCHART_COLORS);
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(Color.BLACK);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(list.size());
        xAxis.setValueFormatter(new MyXAxisValueFormatter(labels));
        xAxis.setTextColor(Color.BLACK);    //OK

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.BLACK);       //OK
        xAxis.setAxisLineColor(Color.BLACK);    //OK
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        chart.setDrawValueAboveBar(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(true);
//        chart.setFitBars(true);
//        chart.setData(data);
        chart.animateY(2000);

        CombinedData data1 = new CombinedData();
        data1.setData(data);

        //라인데이터 추가
        data1.setData(linelist);
//        data1.setData(resultlinelist);
        chart.setData(data1);

        chart.getLegend().setEnabled(false);
//        setLegends(chart,category);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;
        private ArrayList<String> mValues;
        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues.get((int)value);

        }

        /** this is only needed if numbers are returned, else return 0 */

        public int getDecimalDigits() { return 0; }
    }

    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.common_left_btn:
                    finish();
                    break;
            }
        }
    };
}