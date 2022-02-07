package com.redrover.xoyou.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.adapter.ReccomendRecyclerviewAdapter;


public class MyRecommendFragment extends Fragment {
    private Context mContext;
    private NestedScrollView scrollView;

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
        View v = inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView = v.findViewById(R.id.rcv_recommend_list);
        setRecycler();

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //Manage화면
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    public void setRecycler(){
        ArrayList<String> data = new ArrayList<>();
        for(int i=0; i<12; i++){
            data.add("꿈 추천"+i);
        }
        mAdapter = new ReccomendRecyclerviewAdapter(mContext,data);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,3));
    }

}