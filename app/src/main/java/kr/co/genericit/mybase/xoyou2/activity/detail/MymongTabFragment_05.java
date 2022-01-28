package kr.co.genericit.mybase.xoyou2.activity.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MapViewActivity;
import kr.co.genericit.mybase.xoyou2.adapter.DetailLocationRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.model.DetailLocation;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class MymongTabFragment_05 extends Fragment {
    private View v;

    public TextView txt_view_title;
    private ViewPager2 viewPager;
    private ViewsSliderAdapter mAdapter;
    private TextView[] dots;
    private int[] layouts;
    private LinearLayout layoutDots;

    public static MymongTabFragment_05 newInstance(boolean isTooltipView) {
        MymongTabFragment_05 f = new MymongTabFragment_05();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_5, container, false);

        setList();
        initView(v);

        return v;
    }

    public void initView(View v){
        viewPager = v.findViewById(R.id.view_pager);
        layoutDots = v.findViewById(R.id.layoutDots);
        txt_view_title = v.findViewById(R.id.txt_view_title);

        layouts = new int[]{
                R.layout.fragment_detail_5_view1,
                R.layout.fragment_detail_5_view1};

        mAdapter = new ViewsSliderAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.registerOnPageChangeCallback(pageChangeCallback);

        // adding bottom dots
        addBottomDots(0);
    }

    /*
     * Adds bottom dots indicator
     * */

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
//            dots[i].setTextColor(colorsInactive[currentPage]);
            dots[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.mong_grey_default));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
//            dots[currentPage].setTextColor(colorsActive[currentPage]);
            dots[currentPage].setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    /*
     * ViewPager page change listener
     */
    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            addBottomDots(position);

            if(position == 0){
                txt_view_title.setText(CommandUtil.getInstance().getStr(R.string.fragment_detail_1));
                txt_view_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.txt_good));

            }else{
                txt_view_title.setText(CommandUtil.getInstance().getStr(R.string.fragment_detail_2));
                txt_view_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.txt_bad));
            }

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
            } else {
                // still pages are left
            }
        }
    };

    public class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ViewsSliderAdapter() {
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);

            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            RecyclerView recyclerView = null;
            if(position == 0){
                txt_view_title.setText(CommandUtil.getInstance().getStr(R.string.fragment_detail_1));
                txt_view_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.txt_good));

                recyclerView = holder.itemView.findViewById(R.id.rcv_detail5);
                detailLocationChartAdapter0 = new DetailLocationRecyclerviewAdapter(getActivity());
                detailLocationChartAdapter0.setTaskList(detailLocationChart0);
                detailLocationChartAdapter0.setListOnClickListener(onClickItem0);
                recyclerView.setAdapter(detailLocationChartAdapter0);
            }else{
                txt_view_title.setText(CommandUtil.getInstance().getStr(R.string.fragment_detail_2));
                txt_view_title.setTextColor(ContextCompat.getColor(getActivity(), R.color.txt_bad));

                recyclerView = holder.itemView.findViewById(R.id.rcv_detail5);
                detailLocationChartAdapter1 = new DetailLocationRecyclerviewAdapter(getActivity());
                detailLocationChartAdapter1.setTaskList(detailLocationChart1);
                detailLocationChartAdapter1.setListOnClickListener(onClickItem1);
                recyclerView.setAdapter(detailLocationChartAdapter1);

            }
            getListData(recyclerView,position);

        }

        @Override
        public int getItemViewType(int position) {
            return layouts[position];
        }

        @Override
        public int getItemCount() {
            return layouts.length;
        }

        public class SliderViewHolder extends RecyclerView.ViewHolder {
            public View slideItemView;
            public SliderViewHolder(View view) {
                super(view);
                slideItemView = view;
            }

            public View getSlideItemView() {
                return slideItemView;
            }
        }
    }

    private List<DetailLocation> detailLocationChart0;
    private List<DetailLocation> detailLocationChart1;
    private DetailLocationRecyclerviewAdapter detailLocationChartAdapter0;
    private DetailLocationRecyclerviewAdapter detailLocationChartAdapter1;
    private RecyclerView recyclerView;

    DetailLocationRecyclerviewAdapter.listOnClickListener onClickItem0 = new DetailLocationRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Log.v("ifeelbluu", "detailLocationChart.get(id).getTitle() :: " +detailLocationChart0.get(id).getTitle());
            String Dou = detailLocationChart0.get(id).getDou();
            String Lat = detailLocationChart0.get(id).getLat();
            String Lng = detailLocationChart0.get(id).getLng();
            String Adr = detailLocationChart0.get(id).getTitle();
            Intent i= new Intent(getActivity(), MapViewActivity.class);
            i.putExtra("Dou",Dou);
            i.putExtra("Lat",Lat);
            i.putExtra("Lng",Lng);
            i.putExtra("Adr",Adr.replace("· ",""));
            startActivity(i);
        }
    };

    DetailLocationRecyclerviewAdapter.listOnClickListener onClickItem1 = new DetailLocationRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Log.v("ifeelbluu", "detailLocationChart.get(id).getTitle() :: " +detailLocationChart1.get(id).getTitle());
            String Dou = detailLocationChart1.get(id).getDou();
            String Lat = detailLocationChart1.get(id).getLat();
            String Lng = detailLocationChart1.get(id).getLng();
            String Adr = detailLocationChart1.get(id).getTitle();
            Intent i= new Intent(getActivity(), MapViewActivity.class);
            i.putExtra("Dou",Dou);
            i.putExtra("Lat",Lat);
            i.putExtra("Lng",Lng);
            i.putExtra("Adr",Adr.replace("· ",""));
            startActivity(i);
        }
    };

    private void setList(){
        try {
            JSONArray goodList = Constants.StoryJob.getJSONArray("MongGoodJusoList");
            detailLocationChart0 = new ArrayList<>();
            for(int i=0; i<goodList.length(); i++){
                JSONObject job = goodList.getJSONObject(i);
                DetailLocation item = new DetailLocation();
                item.setId(i);
                item.setTitle("· " + job.getString("JuSo"));
                item.setSub1("· " + job.getString("You"));
                item.setSub2("· "+ job.getString("InYeon"));
                item.setSub3("· "+ job.getString("SaGeon"));
                item.setSub4("· "+ job.getString("SaGo"));
                item.setDou(job.getString("Dou"));
                item.setLat(job.getString("Lat"));
                item.setLng(job.getString("Lng"));
                detailLocationChart0.add(item);
            }

            JSONArray badList = Constants.StoryJob.getJSONArray("MongBadJusoList");

            detailLocationChart1 = new ArrayList<>();
            for(int i=0; i<badList.length(); i++){
                JSONObject job = badList.getJSONObject(i);
                DetailLocation item = new DetailLocation();
                item.setId(i);
                item.setTitle("· " + job.getString("JuSo"));
                item.setSub1("· " + job.getString("You"));
                item.setSub2("· "+ job.getString("InYeon"));
                item.setSub3("· "+ job.getString("SaGeon"));
                item.setSub4("· "+ job.getString("SaGo"));
                item.setDou(job.getString("Dou"));
                item.setLat(job.getString("Lat"));
                item.setLng(job.getString("Lng"));
                detailLocationChart1.add(item);
            }

        }catch (Exception e){

        }
    }
    private void getListData(RecyclerView recyclerView, int currentTab){
        if(currentTab == 0){

            detailLocationChartAdapter0.setTaskList(detailLocationChart0);
            recyclerView.setAdapter(detailLocationChartAdapter0);
        }else{

            detailLocationChartAdapter1.setTaskList(detailLocationChart1);
            recyclerView.setAdapter(detailLocationChartAdapter1);
        }

    }
}
