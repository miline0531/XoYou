package com.redrover.xoyou.activity.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.DetailDateListviewAdapter;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.model.MongDateList;
import com.redrover.xoyou.view.bottomsheet.BottomSheetDefaultListFragment;
import com.redrover.xoyou.view.bottomsheet.model.BottomSheetData;

public class MymongTabFragment_04 extends Fragment {
    private View v;
    private ArrayList<MongDateList> dateLists;
    private DetailDateListviewAdapter detailDateListViewAdapter;
    private RecyclerView rcv_list;
    private TextView txt_year;
    private RelativeLayout btn_year;

    public static MymongTabFragment_04 newInstance(boolean isTooltipView) {
        MymongTabFragment_04 f = new MymongTabFragment_04();
        return f;
    }

    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_4, container, false);

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
        rcv_list = v.findViewById(R.id.rcv_list);
        txt_year = v.findViewById(R.id.txt_year);
        btn_year = v.findViewById(R.id.btn_year);

        txt_year.setText("2022");

        getList();
        filterDate("2022");


        btn_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearSelect();
            }
        });
    }

    ArrayList<BottomSheetData> yearList;
    public void filterDate(String Value){

        FliterdateLists = new ArrayList<>();
        for(int i=0; i<dateLists.size(); i++){
            if(dateLists.get(i).geteDate().contains(Value)){
                FliterdateLists.add(dateLists.get(i));
                Log.v("ifeelbluu", "FliterdateLists :: " + dateLists.get(i).geteDate());
            }
        }

        setList();
    }

    private ArrayList<MongDateList> FliterdateLists;
    public void setList(){
        detailDateListViewAdapter = new DetailDateListviewAdapter(getActivity(),FliterdateLists);
        rcv_list.setAdapter(detailDateListViewAdapter);
    }

    public void showYearSelect(){
        BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
        bottomSheetDLFragment.setData(yearList);
        bottomSheetDLFragment.setListener(mBottomSheetEventListener);
        bottomSheetDLFragment.setCancelable(false);
        bottomSheetDLFragment.show(getActivity().getSupportFragmentManager(), "SELECT1");
    }


    public void getList(){
        try{
            JSONArray dateList = Constants.StoryJob.getJSONArray("MongDateList");
            dateLists = new ArrayList<>();
            yearList = new ArrayList<>();
            String year = "";
            for(int i=0; i<dateList.length(); i++){
                JSONObject job = dateList.getJSONObject(i);
                MongDateList item = new MongDateList();
                item.setNo("");
                item.setInfo(job.getString("Info"));
                item.setDateFormat(job.getString("DateFormat"));
                item.setColor(job.getString("Color"));
                item.setDou(job.getString("Dou"));
                item.setHyeonSil(job.getString("HyeonSil"));
                item.setHyeonSilABC(job.getString("HyeonSilABC"));

                item.setsDate(job.getString("sDate"));

                String eDate = job.getString("eDate");
                String tempYear = eDate.substring(0,4);
                if(!year.equals(tempYear)){
                    year = tempYear;
                    BottomSheetData itemyear = new BottomSheetData(i,year,false);
                    if(year.equals("2022")) itemyear.setCheck(true);
                    yearList.add(itemyear);
                    Log.v("ifeelbluu", "itemyear :: " + year);
                }
                item.seteDate(eDate);
                dateLists.add(item);
            }
        }catch (Exception e){

        }
    }

    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                yearList.get(prev).setCheck(false);
            yearList.get(type).setCheck(true);
            txt_year.setText(yearList.get(type).getItemTitle());
            filterDate(txt_year.getText().toString());
        }
    };

}
