package com.redrover.xoyou.activity.main;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.adapter.FindMeRecyclerviewAdapter;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestGetRealUn;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.utils.RecyclerDecoration;


public class FindFragment_bak extends Fragment {
    private Context mContext;
    private RadarChart chart_radar;
    private NestedScrollView scrollView;

    private TextView txt_nowtime,txt_today,txt_month,txt_year;
    private TextView findme_today, txt_un_content;

    //Manage화면
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //context가져오기
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).visibleFloatingButton(View.VISIBLE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find, container, false);

        initView(v);
//        lineChartInit();
        bottomListInit();

        v.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).MainOpenDrawer();
            }
        });

        AppBarLayout collapsingView = v.findViewById(R.id.collapsingView);
        collapsingView.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int visible = 0;
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0){
                    visible = View.GONE;
                }else{
                    visible = View.VISIBLE;
                }
                ((MainActivity)getActivity()).visibleFloatingButton(visible);
            }
        });

        return v;
    }

    private void initView(View v) {
        recyclerView = v.findViewById(R.id.rcv_findme_list);

        chart_radar = v.findViewById(R.id.chart_radar);

        txt_year = v.findViewById(R.id.txt_year);
        txt_month = v.findViewById(R.id.txt_month);
        txt_today = v.findViewById(R.id.txt_today);
        txt_nowtime = v.findViewById(R.id.txt_nowtime);
        txt_un_content = v.findViewById(R.id.txt_un_content);
        findme_today = v.findViewById(R.id.findme_today);

        txt_year.setOnClickListener(onClickTabAction);
        txt_month.setOnClickListener(onClickTabAction);
        txt_today.setOnClickListener(onClickTabAction);
        txt_nowtime.setOnClickListener(onClickTabAction);

        getRealUn();
    }

    private View.OnClickListener onClickTabAction = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            txt_year.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            txt_year.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txt_month.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            txt_month.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txt_today.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            txt_today.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));
            txt_nowtime.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparent));
            txt_nowtime.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey));

            int id = view.getId();
            switch (id){
                case R.id.txt_year:
                    txt_year.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    txt_year.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    txt_un_content.setText(yearContent);
                    break;
                case R.id.txt_month:
                    txt_month.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    txt_month.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    txt_un_content.setText(monthContent);
                    break;
                case R.id.txt_today:
                    txt_today.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    txt_today.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    txt_un_content.setText(todayContent);
                    break;
                case R.id.txt_nowtime:
                    txt_nowtime.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                    txt_nowtime.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    txt_un_content.setText(nowTimeContent);
                    break;
            }
        }
    };

    private void bottomListInit(){
            ArrayList<String> data = new ArrayList<>();
            for(int i=0; i<12; i++){
                data.add("아이템"+i);
            }
            mAdapter = new FindMeRecyclerviewAdapter(mContext,data);
            recyclerView.setAdapter(mAdapter);

            RecyclerDecoration spaceDecoration = new RecyclerDecoration(10,"singleLineHorizontal");
            recyclerView.addItemDecoration(spaceDecoration);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }




    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;
        private String[] mValues;
        public MyYAxisValueFormatter(String[] values) {
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

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            String rst_str = "";
            if((int) value%2==1){
                rst_str = mValues[(int) value];
            }

            return rst_str;
        }

        /** this is only needed if numbers are returned, else return 0 */

        public int getDecimalDigits() { return 0; }
    }


    public void setData(){
        Log.v("ifeelbluu", "mGraphData1 :: " + mGraphData1.size());

        ArrayList<RadarEntry> rValue1 = new ArrayList<>();
        for(int i=0; i<mGraphData1.size(); i++){
            Log.v("ifeelbluu", "mGraphData1 :: ["+i+"] " + mGraphData1.get(i));
            rValue1.add(new RadarEntry(mGraphData1.get(i)));
        }
        ArrayList<RadarEntry> rValue2 = new ArrayList<>();
        for(int i=0; i<mGraphData2.size(); i++){
            rValue2.add(new RadarEntry(mGraphData2.get(i)));
        }
        ArrayList<RadarEntry> rValue3 = new ArrayList<>();
        for(int i=0; i<mGraphData3.size(); i++){
            rValue3.add(new RadarEntry(mGraphData3.get(i)));
        }

        RadarDataSet rset1 = new RadarDataSet(rValue1,"rdata1");
        rset1.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level1));
        rset1.setDrawValues(false);
        rset1.setDrawFilled(true);
        rset1.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level1));
        RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata2");
        rset2.setDrawValues(false);
        rset2.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level2));
        rset2.setDrawFilled(true);
        rset2.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level2));
        RadarDataSet rset3 = new RadarDataSet(rValue3,"rdata3");
        rset3.setDrawValues(false);
        rset3.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level3));
        rset3.setDrawFilled(true);
        rset3.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level3));

        RadarData rdata = new RadarData();
        rdata.addDataSet(rset1);
        rdata.addDataSet(rset2);
        rdata.addDataSet(rset3);

        chart_radar.setData(rdata);

        String[] labels =  {"6","", "8","", "10","", "12","", "14","", "16","", "18","", "20","", "22"};
        XAxis xAxis = chart_radar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setTextColor(Color.WHITE);
        chart_radar.getYAxis().setEnabled(false);

        Legend l = chart_radar.getLegend();
        l.getEntries();
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setYEntrySpace(10f);
        l.setWordWrapEnabled(true);
        LegendEntry l1=new LegendEntry(graphLabel.get(0),Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level1));
        LegendEntry l2=new LegendEntry(graphLabel.get(1), Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level2));
        LegendEntry l3=new LegendEntry(graphLabel.get(2), Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level3));
        l.setCustom(new LegendEntry[]{l1,l2,l3});
        l.setEnabled(true);

        chart_radar.getDescription().setEnabled(false);
        chart_radar.setTouchEnabled(false);
        chart_radar.invalidate();
    }
    String yearContent = "";
    String monthContent = "";
    String todayContent = "";
    String nowTimeContent = "";
    ArrayList<String> graphLabel;
    ArrayList<Float> mGraphData1;
    ArrayList<Float> mGraphData2;
    ArrayList<Float> mGraphData3;
    private void getRealUn(){
        ActionRuler.getInstance().addAction(new ActionRequestGetRealUn(getActivity(), new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {

                try {
                    DefaultResult result = response;
                    if(result.isSuccess()){
                        //시간
                        long now = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
                        SimpleDateFormat week = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sdf.format(now);

                        Log.d("getData","week :: " + week.format(now));
                        Log.d("getData","getData :: " + result.getData());
                        JSONObject job = new JSONObject(result.getData());
                        yearContent = job.getString("tab1_year");
                        monthContent = job.getString("tab1_month");
                        todayContent = job.getString("tab1_day");
                        nowTimeContent = job.getString("tab1_real");

                        graphLabel = new ArrayList<>();
                        mGraphData1 = new ArrayList<>();
                        mGraphData2 = new ArrayList<>();
                        mGraphData3 = new ArrayList<>();
                        String temp1 = job.getString("tab2_list_1");
                        String temp2 = job.getString("tab2_list_2");
                        String temp3 = job.getString("tab2_list_3");
                        Log.v("ifeelbluu", "temp ::[1] " + temp1);
                        Log.v("ifeelbluu", "temp ::[2] " + temp2);
                        Log.v("ifeelbluu", "temp ::[3] " + temp3);

                        JSONArray graphData1 = new JSONArray(temp1);
                        JSONArray graphData2 = new JSONArray(temp2);
                        JSONArray graphData3 = new JSONArray(temp3);
                        Log.v("ifeelbluu", "iDou :: " + graphData1.length());
                        for(int i = 0; i<graphData1.length(); i++){
                            JSONObject graphDataItem = graphData1.getJSONObject(i);
                            String iDou = graphDataItem.getString("iDou");
                            String Name = graphDataItem.getString("Name");
                            if(i==0){
                                graphLabel.add(Name);
                            }
                            mGraphData1.add(Float.parseFloat(iDou));
                        }
                        for(int i = 0; i<graphData2.length(); i++){
                            JSONObject graphDataItem = graphData2.getJSONObject(i);
                            String iDou = graphDataItem.getString("iDou");
                            String Name = graphDataItem.getString("Name");
                            if(i==0){
                                graphLabel.add(Name);
                            }
                            mGraphData2.add(Float.parseFloat(iDou));
                        }
                        for(int i = 0; i<graphData3.length(); i++){
                            JSONObject graphDataItem = graphData3.getJSONObject(i);
                            String iDou = graphDataItem.getString("iDou");
                            String Name = graphDataItem.getString("Name");
                            if(i==0){
                                graphLabel.add(Name);
                            }
                            mGraphData3.add(Float.parseFloat(iDou));
                        }



                        findme_today.setText(time+getDayOfweek(week.format(now)));
                        txt_un_content.setText(yearContent);

                        setData();

                    }else{

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {

            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public String getDayOfweek(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] week = mContext.getResources().getStringArray(R.array.str_week);
        Calendar cal = Calendar.getInstance();
        Date getDate; try { getDate = format.parse(date);
            cal.setTime(getDate);
            int w = cal.get(Calendar.DAY_OF_WEEK)-1;
            return "("+week[w]+")";
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}