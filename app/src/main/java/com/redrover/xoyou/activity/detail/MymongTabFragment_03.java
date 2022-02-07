package com.redrover.xoyou.activity.detail;

import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.utils.CommandUtil;

public class MymongTabFragment_03 extends Fragment {
    private View v;
    private CombinedChart chart;
    private CombinedChart chart2;
    private CombinedChart chart3;
    private CombinedChart chart4;


    public static MymongTabFragment_03 newInstance(boolean isTooltipView) {
        MymongTabFragment_03 f = new MymongTabFragment_03();
        return f;
    }

    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_3, container, false);

        initView(v);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = v.findViewById(R.id.scrollView).getHeight();
            }
        });
        return v;
    }

    TextView label_1,label_2,label_3,label_4;
    public void initView(View v){
        chart = v.findViewById(R.id.barchart);
        chart2 = v.findViewById(R.id.barchart2);
        chart3 = v.findViewById(R.id.barchart3);
        chart4 = v.findViewById(R.id.barchart4);

        label_1 = v.findViewById(R.id.label_1);
        label_2 = v.findViewById(R.id.label_2);
        label_3 = v.findViewById(R.id.label_3);
        label_4 = v.findViewById(R.id.label_4);


        JSONArray SinMongList1_usList = null; //1라인
        JSONArray SinMongList1_sinList = null; //1바
        JSONArray SinMongList2_usList = null; //2라인
        JSONArray SinMongList2_sinList = null; //2바
        JSONArray SinMongList3_usList = null; //3라인
        JSONArray SinMongList3_sinList = null; //3바
        JSONArray SinMongList4_usList = null; //4라인
        JSONArray SinMongList4_sinList = null; //4바
        try {
            SinMongList1_sinList = Constants.StoryJob.getJSONArray("SinMongList1_sinList");
            SinMongList1_usList = Constants.StoryJob.getJSONArray("SinMongList1_usList");
            SinMongList2_sinList = Constants.StoryJob.getJSONArray("SinMongList2_sinList");
            SinMongList2_usList = Constants.StoryJob.getJSONArray("SinMongList2_usList");
            SinMongList3_sinList = Constants.StoryJob.getJSONArray("SinMongList3_sinList");
            SinMongList3_usList = Constants.StoryJob.getJSONArray("SinMongList3_usList");
            SinMongList4_sinList = Constants.StoryJob.getJSONArray("SinMongList4_sinList");
            SinMongList4_usList = Constants.StoryJob.getJSONArray("SinMongList4_usList");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<BarEntry> list = new ArrayList<>();
        ArrayList<BarEntry> list2 = new ArrayList<>();
        ArrayList<BarEntry> list3 = new ArrayList<>();
        ArrayList<BarEntry> list4 = new ArrayList<>();
        LineData lineList = null;
        LineData lineList2 = null;
        LineData lineList3 = null;
        LineData lineList4 = null;

        //라인데이터 셋팅
        if(SinMongList1_usList != null){
            lineList = getLineData(SinMongList1_usList);
            lineList2 = getLineData(SinMongList2_usList);
            lineList3 = getLineData(SinMongList3_usList);
            lineList4 = getLineData(SinMongList4_usList);
        }

        //바데이터 셋팅
        if(SinMongList1_sinList !=null){
            list = getBarData(SinMongList1_sinList);
            list2 = getBarData(SinMongList2_sinList);
            list3 = getBarData(SinMongList3_sinList);
            list4 = getBarData(SinMongList4_sinList);
        }

        //라벨 셋팅
        String[] labels = getResources().getStringArray(R.array.mong_detail_tab3_labels_1);
        String[] labels2 = getResources().getStringArray(R.array.mong_detail_tab3_labels_2);
        String[] labels3 = getResources().getStringArray(R.array.mong_detail_tab3_labels_3);
        String[] labels4 = getResources().getStringArray(R.array.mong_detail_tab3_labels_4);

        //차트뷰 셋팅
        setCombindCart(chart,lineList,list,labels, CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_category_1));
        setCombindCart(chart2,lineList2,list2,labels2,CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_category_2));
        setCombindCart(chart3,lineList3,list3,labels3,CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_category_3));
        setCombindCart(chart4,lineList4,list4,labels4,CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_category_4));

        //결과값 텍스트
        label_1.setText(setLabelText(labels, list, lineList));
        label_2.setText(setLabelText(labels2, list2, lineList2));
        label_3.setText(setLabelText(labels3, list3, lineList3));
        label_4.setText(setLabelText(labels4, list4, lineList4));
    }

    private String setLabelText(String[] label, ArrayList<BarEntry> list, LineData lineList ){
        String rst = "";
        for(int i=0; i<label.length; i++){
            if(i!=0){
                rst+="\n";
            }
            rst += "[" + label[i] +CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_label_txt);

            if(lineList.getDataSets().get(0).getEntryForIndex(i).getY() > list.get(i).getY()){
                rst += CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_label_end_1);
            }else if(lineList.getDataSets().get(0).getEntryForIndex(i).getY() < list.get(i).getY()){
                rst += CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_label_end_2);
            }else{
                rst += CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_label_end_3);
            }
        }


        return rst;
    }

    private ArrayList<BarEntry> getBarData(JSONArray obj){
        ArrayList<BarEntry> barData = new ArrayList<>();

        for(int i=0; i<obj.length(); i++){
            try {
                JSONObject job = obj.getJSONObject(i);
                String iDou = job.getString("iDou");
                barData.add(new BarEntry(i, Float.parseFloat(iDou)));
            }catch (Exception e){

            }
        }

        return barData;
    }

    private LineData getLineData(JSONArray obj){
        LineData lineData = null;

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();

        for (int index = 0; index < obj.length(); index++) {

            try {
                JSONObject job = obj.getJSONObject(index);
                String iDou = job.getString("iDou");

                entries.add(new Entry(index, Float.parseFloat(iDou)));
                String timeString = index+"";
                labels.add(timeString);
            }catch (Exception e){

            }
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "TEST");

        lineData = new LineData(lineDataSet);
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setColor(ContextCompat.getColor(getActivity(), R.color.white));
        set.setLineWidth(1.5f);
        set.setCircleColor(ContextCompat.getColor(getActivity(), R.color.white));
        set.setCircleSize(3f);
        set.setFillColor(ContextCompat.getColor(getActivity(), R.color.white));
//        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineData.addDataSet(set);

        return lineData;
    }


    private void setCombindCart(CombinedChart chart,LineData linedata, ArrayList<BarEntry> list, String[] labels, String category){
//        chart.setHighlightFullBarEnabled(false);
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);

        //데이터
        BarDataSet barDataSet = new BarDataSet(list, "TEST");
        barDataSet.setColors(BARCHART_COLORS);
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(Color.WHITE);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(labels));
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(10f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        chart.setDrawValueAboveBar(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
//        chart.setFitBars(true);
//        chart.setData(data);
        chart.animateY(2000);

        CombinedData data1 = new CombinedData();
        data1.setData(data);

        //라인데이터 추가
        data1.setData(linedata);
        chart.setData(data1);

        setLegends(chart,category);
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

    public void setChart(BarChart chart, ArrayList<BarEntry> list, String[] labels,String category){
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
        rightAxis.setDrawAxisLine(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(labels));
        xAxis.setTextColor(Color.WHITE);
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
        chart.animateY(2000);

//        setLegends(chart,category);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values) {
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

    public void setLegends(CombinedChart chart,String category){

        Legend l = chart.getLegend();

        l.getEntries();
        l.setTextColor(Color.WHITE);

        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

        l.setYEntrySpace(10f);
        l.setTextSize(16f);
        l.setWordWrapEnabled(true);

        LegendEntry l1=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_legend_label_1)+category,Legend.LegendForm.CIRCLE,10f,2f,null, ContextCompat.getColor(getActivity(), R.color.progress_level2));
        LegendEntry l2=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_3_legend_label_2)+category, Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(getActivity(), R.color.white));

        l.setCustom(new LegendEntry[]{l1,l2});

        l.setEnabled(true);

    }
}
