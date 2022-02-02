package kr.co.genericit.mybase.xoyou2.fregment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestGetRealUn;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class MainFragment4 extends Fragment {
    public Activity ac;
    private Context mContext;
    JWSharePreference sharePreference = new JWSharePreference();
    String name;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //context가져오기
        mContext = context;
    }

    public static MainFragment4 newInstance(int number, Activity _ac) {
        MainFragment4 fragment2 = new MainFragment4(_ac);
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    public MainFragment4(Activity _ac){
        this.ac = _ac;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

//        fragments = new int[]{R.layout.fragment_main_top_1,
//                R.layout.fragment_main_top_2,
//                R.layout.fragment_main_top_3,
//                R.layout.fragment_main_top_4};

        View view = inflater.inflate(R.layout.fragment_main_top_4, container, false);
        name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,"");
        initView(view);
        getRealUn();
        return view;
    }

    private ViewPager2 viewPager;
    private LinearLayout layoutDots;
    private int[] fragments;
    private TextView[] dots;
    private ViewsSliderAdapter mAdapter;
    private boolean initDotted = true;
    private int CurrentPosition = 0;
    public ArrayList<View> mainTopView;
    private TextView txt_empty,top_result;
    private RelativeLayout layout_viewpager;
    private void initView(View v) {
        viewPager = v.findViewById(R.id.view_pager2);
        layoutDots = v.findViewById(R.id.layoutDots2);
        txt_empty = v.findViewById(R.id.txt_empty);
        top_result = v.findViewById(R.id.top_result);
        layout_viewpager = v.findViewById(R.id.layout_viewpager2);

//        setViewPager();
    }

    public void setViewPager(){
        //fragments = new int[]{R.layout.fragment_find_un,R.layout.fragment_find_graph};
        fragments = new int[]{R.layout.fragment_find_graph,R.layout.fragment_find_un};
        mainTopView = new ArrayList<>();
        mAdapter = new ViewsSliderAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.registerOnPageChangeCallback(pageChangeCallback);

        addBottomDots(0);
    }

    private void addBottomDots(int currentPage) {
        Log.v("ifeelbluu","addBottomDots :: " +currentPage);
        dots = new TextView[fragments.length];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.mong_grey_default));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

    }


    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if(!initDotted) addBottomDots(position);
            initDotted = false;

            CurrentPosition = position;
            Log.v("ifeelbluu", "mainTopView Size :: " + mainTopView.size());



            switch (position){
                case 0:
                    break;
                case 1:
                    //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load2));
                    break;
            }
//            setMainTopText(mainText);
        }
    };

    public void setMainTopText(String text){
        //TextView mainText = mainTopView.get(CurrentPosition).findViewById(R.id.main_title);
        top_result.setText(text);
    }

    public class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private TextView txt_nowtime,txt_today,txt_month,txt_year;
        private TextView txt_date, txt_info, txt_un_content;

        private TextView txt_date2, txt_info2;
        private RadarChart chart_radar;

        public ViewsSliderAdapter() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new ViewsSliderAdapter.SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(mainTopView.size()==2){
                mainTopView.set(position, holder.itemView);
            }else{
                mainTopView.add(holder.itemView);
            }

            if(position == 1 ){
                txt_year = mainTopView.get(0).findViewById(R.id.txt_year);
                txt_month = mainTopView.get(0).findViewById(R.id.txt_month);
                txt_today = mainTopView.get(0).findViewById(R.id.txt_today);
                txt_nowtime = mainTopView.get(0).findViewById(R.id.txt_nowtime);
                txt_un_content = mainTopView.get(0).findViewById(R.id.txt_un_content);
                txt_date = mainTopView.get(0).findViewById(R.id.txt_date);
                txt_info = mainTopView.get(0).findViewById(R.id.txt_info);

                txt_year.setOnClickListener(onClickTabAction);
                txt_month.setOnClickListener(onClickTabAction);
                txt_today.setOnClickListener(onClickTabAction);
                txt_nowtime.setOnClickListener(onClickTabAction);
                String[] dates = currentDate.split("-");
                txt_date.setText(currentDay+ dates[3]+ CommandUtil.getInstance().getStr(R.string.mong_findme_hour_and)+(Integer.parseInt(dates[3])+1)+CommandUtil.getInstance().getStr(R.string.mong_findme_hour));
                txt_info.setText(CommandUtil.getInstance().getStr(R.string.mong_findme_un));
                //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_now));
                setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_now));
                txt_un_content.setText(nowTimeContent);
            }else if(position == 0){
                txt_date2 = mainTopView.get(0).findViewById(R.id.txt_date);
                txt_info2 = mainTopView.get(0).findViewById(R.id.txt_info);
                chart_radar = mainTopView.get(0).findViewById(R.id.chart_radar);

                txt_date2.setText(currentDay);
                txt_info2.setText(loadTime);
                setData();
            }

        }

        @Override
        public int getItemViewType(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }

        public class SliderViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;

            public SliderViewHolder(View view) {
                super(view);
            }
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
                String[] date;
                switch (id){
                    case R.id.txt_year:
                        txt_year.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                        txt_year.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        txt_un_content.setText(yearContent);
                        //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_year));
                        date = currentDate.split("-");
                        txt_date.setText(date[0]+CommandUtil.getInstance().getStr(R.string.mong_findme_year));
                        txt_info.setText(CommandUtil.getInstance().getStr(R.string.mong_findme_un));
                        break;
                    case R.id.txt_month:
                        txt_month.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                        txt_month.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        txt_un_content.setText(monthContent);
                        //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_month));
                        date = currentDate.split("-");
                        txt_date.setText(date[0]+CommandUtil.getInstance().getStr(R.string.mong_findme_year)+date[1]+CommandUtil.getInstance().getStr(R.string.mong_findme_month));
                        txt_info.setText(CommandUtil.getInstance().getStr(R.string.mong_findme_un));
                        break;
                    case R.id.txt_today:
                        txt_today.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                        txt_today.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        txt_un_content.setText(todayContent);
                        //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_day));
                        txt_date.setText(currentDay);
                        txt_info.setText(CommandUtil.getInstance().getStr(R.string.mong_findme_un));
                        break;
                    case R.id.txt_nowtime:
                        txt_nowtime.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey));
                        txt_nowtime.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                        txt_un_content.setText(nowTimeContent);
                        //((MainActivity)getActivity()).setMainTopText(name+CommandUtil.getInstance().getStr(R.string.mong_findme_un_load1_now));
                        date = currentDate.split("-");
                        txt_date.setText(currentDay+ date[3]+CommandUtil.getInstance().getStr(R.string.mong_findme_hour_and)+(Integer.parseInt(date[3])+1)+CommandUtil.getInstance().getStr(R.string.mong_findme_hour));
                        txt_info.setText(CommandUtil.getInstance().getStr(R.string.mong_findme_un));
                        break;
                }
            }
        };

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
            rset1.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level3));
            rset1.setDrawValues(false);
            rset1.setDrawFilled(true);
            rset1.setLineWidth(0f);
            rset1.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level3));
            RadarDataSet rset2 = new RadarDataSet(rValue2,"rdata2");
            rset2.setDrawValues(false);
            rset2.setLineWidth(2f);
            rset2.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level2));
            rset2.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level2));
            RadarDataSet rset3 = new RadarDataSet(rValue3,"rdata3");
            rset3.setDrawValues(false);
            rset3.setLineWidth(2f);
            rset3.setColor(ContextCompat.getColor(getActivity(), R.color.progress_level1));
            rset3.setFillColor(ContextCompat.getColor(getActivity(), R.color.progress_level1));

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
//            l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setYEntrySpace(14f);
            l.setXEntrySpace(14f);
            l.setTextSize(14f);
            l.setWordWrapEnabled(true);
            LegendEntry l1=new LegendEntry(graphLabel.get(0),Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level3));
            LegendEntry l2=new LegendEntry(graphLabel.get(1), Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level2));
            LegendEntry l3=new LegendEntry(graphLabel.get(2), Legend.LegendForm.CIRCLE,14f,2f,null,ContextCompat.getColor(getActivity(), R.color.progress_level1));
            l.setCustom(new LegendEntry[]{l1,l2,l3});
            l.setEnabled(true);

            chart_radar.getDescription().setEnabled(false);
            chart_radar.setTouchEnabled(false);
            chart_radar.invalidate();
        }
    }



    String currentDay = "";
    String currentDate = "";
    String yearContent = "";
    String monthContent = "";
    String todayContent = "";
    String nowTimeContent = "";
    String loadTime = "";
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
                        SimpleDateFormat weekH = new SimpleDateFormat("yyyy-MM-dd-HH");
                        String time = sdf.format(now);
                        currentDate = weekH.format(now);
                        JSONObject job = new JSONObject(result.getData());
                        yearContent = job.getString("tab1_year");
                        monthContent = job.getString("tab1_month");
                        todayContent = job.getString("tab1_day");
                        nowTimeContent = job.getString("tab1_real");
                        loadTime = job.getString("tab2_load_time");

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


                        currentDay = time+getDayOfweek(week.format(now));

//                        setData();
                        setViewPager();

                    }else{
                        layout_viewpager.setVisibility(View.GONE);
                        txt_empty.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    layout_viewpager.setVisibility(View.GONE);
                    txt_empty.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                layout_viewpager.setVisibility(View.GONE);
                txt_empty.setVisibility(View.VISIBLE);
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