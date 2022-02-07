package com.redrover.xoyou.activity.bid;

import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
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

import java.util.ArrayList;

import com.redrover.xoyou.R;

public class BidTabFragment extends Fragment {
    private View v;
    private BarChart chart4;


    public static BidTabFragment newInstance(boolean isTooltipView) {
        BidTabFragment f = new BidTabFragment();
        return f;
    }

    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_bid_graph, container, false);

        initView(v);
        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = v.findViewById(R.id.scrollView).getHeight();
            }
        });
        return v;
    }

    public void initView(View v){
        chart4 = v.findViewById(R.id.barchart4);

    }

    public void chartSetting(ArrayList<BarEntry> list, String[] labels){
        chart4 = v.findViewById(R.id.barchart4);
        setChart(chart4,list,labels,"정");
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

        setLegends(chart,category);
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

    public void setLegends(BarChart chart,String category){

        Legend l = chart.getLegend();

        l.getEntries();
        l.setTextColor(Color.WHITE);

        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

        l.setYEntrySpace(10f);

        l.setWordWrapEnabled(true);

        LegendEntry l1=new LegendEntry("꿈속에서 "+category,Legend.LegendForm.CIRCLE,10f,2f,null, ContextCompat.getColor(getActivity(), R.color.progress_level1));
        LegendEntry l2=new LegendEntry("꿈꾼 후의 "+category, Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level2));

        l.setCustom(new LegendEntry[]{l1,l2});

        l.setEnabled(true);

    }

    private LineData generateLineData() {

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        for (int index = 0; index < 6; index++) {
            entries.add(new Entry(index, index+3));
            String timeString = index+"";
            labels.add(timeString);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "TEST");

        LineData d = new LineData(lineDataSet);
        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(ContextCompat.getColor(getActivity(), R.color.btn_ok_color));
        set.setLineWidth(1.5f);
        set.setCircleColor(ContextCompat.getColor(getActivity(), R.color.btn_ok_color));
        set.setCircleSize(3f);
        set.setFillColor(ContextCompat.getColor(getActivity(), R.color.btn_ok_color));
//        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

}
